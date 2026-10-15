/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.PenerimaDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import model.Penerima;
import model.PenerimaIndividu;
import model.PenerimaLembaga;
import util.InputUtil;
import view.MessageView;
import view.PenerimaView;

public class PenerimaService {

    private final PenerimaDAO penerimaDAO;
    private final PenerimaView penerimaView;
    private final MessageView messageView;

    public PenerimaService() {
        penerimaDAO = new PenerimaDAO();
        penerimaView = new PenerimaView();
        messageView = new MessageView();
    }

    public void tableData() {
        try {
            List<Penerima> dataPenerima = penerimaDAO.findAll();
            penerimaView.tableData(dataPenerima);
        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }
    }

    public void dataPenerima(Scanner scanner) {
        boolean berjalanPenerima = true;

        while (berjalanPenerima) {
            tableData();
            penerimaView.tampilkanMenuPilihan();

            int pilihanPenerima = InputUtil.bacaInt(scanner, ">> ");

            switch (pilihanPenerima) {
                case 1 -> tambahPenerima(scanner);
                case 2 -> updatePenerima(scanner);
                case 3 -> hapusPenerima(scanner);
                case 4 -> berjalanPenerima = false;
                default -> {
                    messageView.tampilkanPilihanTidakValid();
                    InputUtil.tekanEnter(scanner);
                }
            }
        }
    }

    private void tambahPenerima(Scanner scanner) {
        try {
            penerimaView.tampilkanPilihanJenisPenerima();

            int pilihanJenis;

            while (true) {
                pilihanJenis = InputUtil.bacaInt(scanner, "Jenis Penerima: ");

                if (pilihanJenis == 1 || pilihanJenis == 2) {
                    break;
                }
                penerimaView.tampilkanJenisPenerimaTidakValid();
            }

            Penerima penerima;

            if (pilihanJenis == 1) {
                String deskripsiPenerima = InputUtil.bacaString(scanner, "Deskripsi Penerima: ");
                penerima = new PenerimaIndividu(deskripsiPenerima);
            } else {
                String namaLembaga = InputUtil.bacaString(scanner, "Nama Lembaga: ");
                String jenisLembaga = InputUtil.bacaString(scanner, "Jenis Lembaga: ");
                String namaPengelola = InputUtil.bacaNama(scanner, "Nama Pengelola: ");

                penerima = new PenerimaLembaga(
                        namaLembaga,
                        jenisLembaga,
                        namaPengelola
                );
            }

            if (penerimaDAO.insert(penerima)) {
                messageView.tampilkanBerhasilTambah();
            } else {
                messageView.tampilkanGagalTambah();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    private void updatePenerima(Scanner scanner) {
        try {
            String idPenerima = InputUtil.bacaString(scanner, "ID Penerima: ");
            Penerima penerima = cariPenerima(idPenerima);

            if (penerima == null) {
                penerimaView.tampilkanIdPenerimaTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }
            penerimaView.tampilkanDataUpdate(penerima);

            System.out.print("Yakin ingin meng-update data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (!konfirmasi.equalsIgnoreCase("y")) {
                messageView.tampilkanUpdateDibatalkan();
                InputUtil.tekanEnter(scanner);
                return;
            }

            if (penerima instanceof PenerimaIndividu individu) {
                String deskripsiPenerima = InputUtil.bacaString(scanner, "Deskripsi Penerima: ");
                individu.setDeskripsiPenerima(deskripsiPenerima);
            } else if (penerima instanceof PenerimaLembaga lembaga) {
                String namaLembaga = InputUtil.bacaString(scanner, "Nama Lembaga: ");
                String jenisLembaga = InputUtil.bacaString(scanner, "Jenis Lembaga: ");
                String namaPengelola = InputUtil.bacaNama(scanner, "Nama Pengelola: ");

                lembaga.setNamaLembaga(namaLembaga);
                lembaga.setJenisLembaga(jenisLembaga);
                lembaga.setNamaPengelola(namaPengelola);
            }

            if (penerimaDAO.update(penerima)) {
                messageView.tampilkanBerhasilUpdate();
            } else {
                messageView.tampilkanGagalUpdate();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    private void hapusPenerima(Scanner scanner) {
        try {
            String idPenerima = InputUtil.bacaString(scanner, "ID Penerima: ");
            Penerima penerima = cariPenerima(idPenerima);

            if (penerima == null) {
                penerimaView.tampilkanIdPenerimaTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }
            penerimaView.tampilkanDataHapus(penerima);

            System.out.print("Yakin ingin menghapus data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (!konfirmasi.equalsIgnoreCase("y")) {
                messageView.tampilkanHapusDibatalkan();
                InputUtil.tekanEnter(scanner);
                return;
            }

            if (penerimaDAO.delete(idPenerima)) {
                messageView.tampilkanBerhasilHapus();
            } else {
                messageView.tampilkanGagalHapus();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    public Penerima cariPenerima(String idPenerima)
            throws SQLException {
        return penerimaDAO.findById(idPenerima);
    }
}