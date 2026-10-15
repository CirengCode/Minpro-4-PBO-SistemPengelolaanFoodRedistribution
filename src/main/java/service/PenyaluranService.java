/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.PenyaluranDAO;
import java.sql.SQLException;
import java.util.Scanner;
import model.Donasi;
import model.Penyaluran;
import util.InputUtil;
import view.DonasiView;
import view.MessageView;
import view.PenerimaView;
import view.PenyaluranView;

public class PenyaluranService {
    private final PenyaluranDAO penyaluranDAO = new PenyaluranDAO();
    private final DonasiService donasiService;
    private final PenerimaService penerimaService;
    private final PenyaluranView penyaluranView;
    private final DonasiView donasiView;
    private final PenerimaView penerimaView;
    private final MessageView messageView;

    public PenyaluranService(DonasiService donasiService, PenerimaService penerimaService) {
        this.donasiService = donasiService;
        this.penerimaService = penerimaService;
        penyaluranView = new PenyaluranView();
        donasiView = new DonasiView();
        penerimaView = new PenerimaView();
        messageView = new MessageView();
    }

    public void tableData() {
        try {
            penyaluranView.tableData(penyaluranDAO.findAll());
        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }
    }
    
    private int totalPorsiPenyaluran(String idDonasi)
            throws SQLException {
        return totalPorsiPenyaluran(idDonasi, null);
    }

    private int totalPorsiPenyaluran(
            String idDonasi,
            String idPenyaluranDikecualikan)
            throws SQLException {

        int total = 0;

        for (Penyaluran p : penyaluranDAO.findAll()) {
            boolean donasiSama = p.getIdDonasi()
                    .equalsIgnoreCase(idDonasi);

            boolean bukanDataDikecualikan =
                    idPenyaluranDikecualikan == null
                    || !p.getIdPenyaluran().equalsIgnoreCase(
                            idPenyaluranDikecualikan);

            if (donasiSama && bukanDataDikecualikan) {
                total += p.getJumlahPorsi();
            }
        }

        return total;
    }

    private int sisaPorsiDonasi(String idDonasi)
            throws SQLException {

        Donasi donasi = donasiService.cariDonasi(idDonasi);

        if (donasi == null) {
            return 0;
        }

        return donasi.getJumlahPorsi()
                - totalPorsiPenyaluran(idDonasi);
    }

    public void dataPenyaluran(Scanner scanner) {
        boolean berjalanPenyaluran = true;

        while (berjalanPenyaluran) {
            tableData();
            penyaluranView.tampilkanMenuPilihan();

            int pilihan = InputUtil.bacaInt(scanner, ">> ");

            switch (pilihan) {
                case 1 -> tambahPenyaluran(scanner);
                case 2 -> updatePenyaluran(scanner);
                case 3 -> hapusPenyaluran(scanner);
                case 4 -> berjalanPenyaluran = false;
                default -> {
                    messageView.tampilkanPilihanTidakValid();
                    InputUtil.tekanEnter(scanner);
                }
            }
        }
    }

    private void tambahPenyaluran(Scanner scanner) {
        try {
            String idDonasi;
            Donasi donasi;

            while (true) {
                idDonasi = InputUtil.bacaString(
                        scanner, "ID Donasi: ");

                donasi = donasiService.cariDonasi(idDonasi);

                if (donasi == null) {
                    donasiView.tampilkanIdDonasiTidakValid();
                    continue;
                }

                if (!"Layak".equalsIgnoreCase(
                        donasi.getStatusKelayakan())) {
                    penyaluranView.tampilkanStatusTidakLayak();
                    continue;
                }

                if (sisaPorsiDonasi(idDonasi) <= 0) {
                    penyaluranView.tampilkanPorsiDonasiTidakValid();
                    continue;
                }

                break;
            }

            String idPenerima;

            while (true) {
                idPenerima = InputUtil.bacaString(
                        scanner, "ID Penerima: ");

                if (penerimaService.cariPenerima(idPenerima) != null) {
                    break;
                }

                penerimaView.tampilkanIdPenerimaTidakValid();
            }

            String namaKegiatan = InputUtil.bacaString(
                    scanner, "Nama Kegiatan: ");

            String tanggalPenyaluran = InputUtil.bacaTanggal(
                    scanner, "Tanggal Penyaluran: ");

            String statusPenyaluran = "Belum Disalurkan";
            int jumlahPorsi;

            while (true) {
                int sisaPorsi = sisaPorsiDonasi(idDonasi);

                jumlahPorsi = InputUtil.bacaInt(
                        scanner,
                        "Jumlah Porsi (tersedia "
                        + sisaPorsi + "): ");

                if (jumlahPorsi > 0
                        && jumlahPorsi <= sisaPorsi) {
                    break;
                }

                penyaluranView.tampilkanJumlahPorsiTidakValid();
            }

            String petugas = InputUtil.bacaString(
                    scanner, "Petugas: ");

            Penyaluran penyaluran = new Penyaluran(
                    idDonasi,
                    idPenerima,
                    namaKegiatan,
                    tanggalPenyaluran,
                    statusPenyaluran,
                    jumlahPorsi,
                    petugas
            );

            if (penyaluranDAO.insert(penyaluran)) {
                messageView.tampilkanBerhasilTambah();
            } else {
                messageView.tampilkanGagalTambah();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        } catch (IllegalStateException e) {
            messageView.tampilkanGagalTambah();
        }

        InputUtil.tekanEnter(scanner);
    }

    private void updatePenyaluran(Scanner scanner) {
        try {
            String idPenyaluran = InputUtil.bacaString(
                    scanner, "ID Penyaluran: ");

            Penyaluran penyaluran =
                    penyaluranDAO.findById(idPenyaluran);

            if (penyaluran == null) {
                penyaluranView.tampilkanIdPenyaluranTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }

            penyaluranView.tampilkanDataUpdate(penyaluran);

            System.out.print(
                    "Yakin ingin meng-update data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (!konfirmasi.equalsIgnoreCase("y")) {
                messageView.tampilkanUpdateDibatalkan();
                InputUtil.tekanEnter(scanner);
                return;
            }

            String idDonasi;
            Donasi donasi;

            while (true) {
                idDonasi = InputUtil.bacaString(
                        scanner, "ID Donasi: ");

                donasi = donasiService.cariDonasi(idDonasi);

                if (donasi == null) {
                    donasiView.tampilkanIdDonasiTidakValid();
                    continue;
                }

                if (!"Layak".equalsIgnoreCase(
                        donasi.getStatusKelayakan())) {
                    penyaluranView.tampilkanStatusTidakLayak();
                    continue;
                }

                int totalPorsiLain = totalPorsiPenyaluran(
                        idDonasi, idPenyaluran);

                int sisaPorsi =
                        donasi.getJumlahPorsi() - totalPorsiLain;

                if (sisaPorsi <= 0) {
                    penyaluranView.tampilkanPorsiDonasiTidakValid();
                    continue;
                }

                break;
            }

            String idPenerima;

            while (true) {
                idPenerima = InputUtil.bacaString(
                        scanner, "ID Penerima: ");

                if (penerimaService.cariPenerima(idPenerima) != null) {
                    break;
                }

                penerimaView.tampilkanIdPenerimaTidakValid();
            }

            String namaKegiatan = InputUtil.bacaString(
                    scanner, "Nama Kegiatan: ");

            String tanggalPenyaluran = InputUtil.bacaTanggal(
                    scanner, "Tanggal Penyaluran: ");

            penyaluranView.tampilkanPilihanStatus();

            int pilihanStatus;

            while (true) {
                pilihanStatus = InputUtil.bacaInt(
                        scanner, "Status Penyaluran: ");

                if (pilihanStatus >= 1 && pilihanStatus <= 3) {
                    break;
                }

                penyaluranView.tampilkanStatusTidakValid();
            }

            String statusPenyaluran;

            if (pilihanStatus == 1) {
                statusPenyaluran = "Belum Disalurkan";
            } else if (pilihanStatus == 2) {
                statusPenyaluran = "Dalam Proses Penyaluran";
            } else {
                statusPenyaluran = "Penyaluran Selesai";
            }

            int jumlahPorsi;

            while (true) {
                int totalPorsiLain = totalPorsiPenyaluran(
                        idDonasi, idPenyaluran);

                donasi = donasiService.cariDonasi(idDonasi);

                int sisaPorsi =
                        donasi.getJumlahPorsi() - totalPorsiLain;

                jumlahPorsi = InputUtil.bacaInt(scanner, "Jumlah Porsi (tersedia " + sisaPorsi + "): ");
                if (jumlahPorsi > 0
                        && jumlahPorsi <= sisaPorsi) {
                    break;
                }

                penyaluranView.tampilkanJumlahPorsiTidakValid();
            }

            String petugas = InputUtil.bacaString(scanner, "Petugas: ");

            penyaluran.setIdDonasi(idDonasi);
            penyaluran.setIdPenerima(idPenerima);
            penyaluran.setNamaKegiatan(namaKegiatan);
            penyaluran.setTanggalPenyaluran(tanggalPenyaluran);
            penyaluran.setStatusPenyaluran(statusPenyaluran);
            penyaluran.setJumlahPorsi(jumlahPorsi);
            penyaluran.setPetugas(petugas);

            if (penyaluranDAO.update(penyaluran)) {
                messageView.tampilkanBerhasilUpdate();
            } else {
                messageView.tampilkanGagalUpdate();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        } catch (IllegalStateException e) {
            messageView.tampilkanGagalUpdate();
        }

        InputUtil.tekanEnter(scanner);
    }

    private void hapusPenyaluran(Scanner scanner) {
        try {
            String idPenyaluran = InputUtil.bacaString(
                    scanner, "ID Penyaluran: ");

            Penyaluran penyaluran =
                    penyaluranDAO.findById(idPenyaluran);

            if (penyaluran == null) {
                penyaluranView.tampilkanIdPenyaluranTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }

            penyaluranView.tampilkanDataHapus(penyaluran);

            System.out.print(
                    "Yakin ingin menghapus data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (konfirmasi.equalsIgnoreCase("y")) {
                if (penyaluranDAO.delete(idPenyaluran)) {
                    messageView.tampilkanBerhasilHapus();
                } else {
                    messageView.tampilkanGagalHapus();
                }
            } else {
                messageView.tampilkanHapusDibatalkan();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    public Penyaluran cariPenyaluran(String idPenyaluran)
            throws SQLException {
        return penyaluranDAO.findById(idPenyaluran);
    }
}

