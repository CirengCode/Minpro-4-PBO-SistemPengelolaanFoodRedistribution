/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.DonaturDAO;
import java.sql.SQLException;
import java.util.Scanner;
import model.Donatur;
import model.DonaturIndividu;
import model.DonaturInstansi;
import util.InputUtil;
import view.DonaturView;
import view.MessageView;

public class DonaturService {
    private final DonaturDAO donaturDAO = new DonaturDAO();
    private final DonaturView donaturView;
    private final MessageView messageView;

    public DonaturService() {
        donaturView = new DonaturView();
        messageView = new MessageView();
    }

    public void tableData() {
        try {
            donaturView.tableData(donaturDAO.findAll());
        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }
    }
    
    public void dataDonatur(Scanner scanner) {
        boolean berjalanDonatur = true;

        while (berjalanDonatur) {
            tableData();
            donaturView.tampilkanMenuPilihan();

            int pilihanDonatur = InputUtil.bacaInt(scanner, ">> ");

            switch (pilihanDonatur) {
                case 1 -> tambahDonatur(scanner);
                case 2 -> updateDonatur(scanner);
                case 3 -> hapusDonatur(scanner);
                case 4 -> berjalanDonatur = false;
                default -> {
                    messageView.tampilkanPilihanTidakValid();
                    InputUtil.tekanEnter(scanner);
                }
            }
        }
    }

    private void tambahDonatur(Scanner scanner) {
        String namaDonatur = InputUtil.bacaNama(scanner, "Nama Donatur: ");

        donaturView.tampilkanPilihanJenisDonatur();

        int pilihanJenis;
        while (true) {
            pilihanJenis = InputUtil.bacaInt(scanner, "Jenis Donatur: ");

            if (pilihanJenis == 1 || pilihanJenis == 2) {
                break;
            }

            donaturView.tampilkanJenisDonaturTidakValid();
        }

        try {
            Donatur donatur;

            if (pilihanJenis == 1) {
                String jenisKegiatan =
                        InputUtil.bacaString(scanner, "Jenis Kegiatan: ");

                donatur = new DonaturIndividu(
                        namaDonatur, jenisKegiatan
                );
            } else {
                String namaInstansi = InputUtil.bacaString(scanner, "Nama Instansi: ");

                String jenisInstansi = InputUtil.bacaString(scanner, "Jenis Instansi: ");

                donatur = new DonaturInstansi(
                        namaDonatur, namaInstansi, jenisInstansi
                );
            }

            if (donaturDAO.insert(donatur)) {
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

    private void updateDonatur(Scanner scanner) {
        String idDonatur = InputUtil.bacaString(scanner, "ID Donatur: ");

        try {
            Donatur donatur = cariDonatur(idDonatur);

            if (donatur == null) {
                donaturView.tampilkanIdDonaturTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }

            donaturView.tampilkanDataUpdate(donatur);

            System.out.print("Yakin ingin meng-update data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (konfirmasi.equalsIgnoreCase("y")) {
                String namaDonatur = InputUtil.bacaNama(scanner, "Nama Donatur: ");

                if (donatur instanceof DonaturIndividu individu) {
                    String jenisKegiatan = InputUtil.bacaString(scanner, "Jenis Kegiatan: ");

                    individu.setNamaDonatur(namaDonatur);
                    individu.setJenisKegiatan(jenisKegiatan);

                } else if (donatur instanceof DonaturInstansi instansi) {
                    String namaInstansi = InputUtil.bacaString(scanner, "Nama Instansi: ");
                    String jenisInstansi = InputUtil.bacaString(scanner, "Jenis Instansi: ");

                    instansi.setNamaDonatur(namaDonatur);
                    instansi.setNamaInstansi(namaInstansi);
                    instansi.setJenisInstansi(jenisInstansi);
                }

                if (donaturDAO.update(donatur)) {
                    messageView.tampilkanBerhasilUpdate();
                } else {
                    messageView.tampilkanGagalUpdate();
                }

            } else {
                messageView.tampilkanUpdateDibatalkan();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    private void hapusDonatur(Scanner scanner) {
        String idDonatur = InputUtil.bacaString(scanner, "ID Donatur: ");
        
        try {
            Donatur donatur = cariDonatur(idDonatur);

            if (donatur == null) {
                donaturView.tampilkanIdDonaturTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }

            donaturView.tampilkanDataHapus(donatur);

            System.out.print("Yakin ingin menghapus data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (konfirmasi.equalsIgnoreCase("y")) {
                if (donaturDAO.delete(idDonatur)) {
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

    public Donatur cariDonatur(String idDonatur)
            throws SQLException {
        return donaturDAO.findById(idDonatur);
    }
}

