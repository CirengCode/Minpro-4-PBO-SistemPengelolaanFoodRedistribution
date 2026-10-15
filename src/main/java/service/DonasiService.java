/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.DonasiDAO;
import java.sql.SQLException;
import java.util.Scanner;
import model.Donasi;
import util.InputUtil;
import view.DonaturView;
import view.DonasiView;
import view.MessageView;

public class DonasiService {
    private final DonasiDAO donasiDAO;
    private final DonaturService donaturService;
    private final DonaturView donaturView;
    private final DonasiView donasiView;
    private final MessageView messageView;

    public DonasiService(DonaturService donaturService) {
        this.donasiDAO = new DonasiDAO();
        this.donaturService = donaturService;
        this.donaturView = new DonaturView();
        this.donasiView = new DonasiView();
        this.messageView = new MessageView();
    }

    public void tableData() {
        try {
            donasiView.tableData(donasiDAO.findAll());
        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }
    }
    
    public void dataDonasi(Scanner scanner) {
        boolean berjalanDonasi = true;

        while (berjalanDonasi) {
            tableData();
            donasiView.tampilkanMenuPilihan();

            int pilihanDonasi = InputUtil.bacaInt(scanner, ">> ");
            switch (pilihanDonasi) {
                case 1 -> tambahDonasi(scanner);
                case 2 -> updateDonasi(scanner);
                case 3 -> hapusDonasi(scanner);
                case 4 -> berjalanDonasi = false;
                default -> {
                    messageView.tampilkanPilihanTidakValid();
                    InputUtil.tekanEnter(scanner);
                }
            }
        }
    }

    private void tambahDonasi(Scanner scanner) {
        try {
            String idDonatur = bacaIdDonaturValid(scanner);
            String namaMakanan = InputUtil.bacaString(scanner, "Nama Makanan: ");
            int jumlahPorsi = InputUtil.bacaInt(scanner, "Jumlah Porsi: ");
            String statusKelayakan = bacaStatusKelayakan(scanner);
            
            Donasi donasi = new Donasi(
                idDonatur,
                namaMakanan,
                jumlahPorsi,
                statusKelayakan
            );

            if (donasiDAO.insert(donasi)) {
                messageView.tampilkanBerhasilTambah();
            } else {
                messageView.tampilkanGagalTambah();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    private void updateDonasi(Scanner scanner) {
        try {
            String idDonasi = InputUtil.bacaString(scanner, "ID Donasi: ");
            Donasi donasi = cariDonasi(idDonasi);

            if (donasi == null) {
                donasiView.tampilkanIdDonasiTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }

            donasiView.tampilkanDataUpdate(donasi);

            System.out.print("Yakin ingin meng-update data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (!konfirmasi.equalsIgnoreCase("y")) {
                messageView.tampilkanUpdateDibatalkan();
                InputUtil.tekanEnter(scanner);
                return;
            }

            String idDonatur = bacaIdDonaturValid(scanner);

            String namaMakanan = InputUtil.bacaString(scanner, "Nama Makanan: ");
            int jumlahPorsi = InputUtil.bacaInt(scanner, "Jumlah Porsi: ");

            String statusKelayakan = bacaStatusKelayakan(scanner);

            donasi.setIdDonatur(idDonatur);
            donasi.setNamaMakanan(namaMakanan);
            donasi.setJumlahPorsi(jumlahPorsi);
            donasi.setStatusKelayakan(statusKelayakan);

            if (donasiDAO.update(donasi)) {
                messageView.tampilkanBerhasilUpdate();
            } else {
                messageView.tampilkanGagalUpdate();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    private void hapusDonasi(Scanner scanner) {
        try {
            String idDonasi = InputUtil.bacaString(scanner, "ID Donasi: ");
            Donasi donasi = cariDonasi(idDonasi);

            if (donasi == null) {
                donasiView.tampilkanIdDonasiTidakValid();
                InputUtil.tekanEnter(scanner);
                return;
            }

            donasiView.tampilkanDataHapus(donasi);

            System.out.print("Yakin ingin menghapus data? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (!konfirmasi.equalsIgnoreCase("y")) {
                messageView.tampilkanHapusDibatalkan();
                InputUtil.tekanEnter(scanner);
                return;
            }

            if (donasiDAO.delete(idDonasi)) {
                messageView.tampilkanBerhasilHapus();
            } else {
                messageView.tampilkanGagalHapus();
            }

        } catch (SQLException e) {
            messageView.tampilkanGagalDatabase(e.getMessage());
        }

        InputUtil.tekanEnter(scanner);
    }

    private String bacaIdDonaturValid(Scanner scanner)
            throws SQLException {

        while (true) {
            String idDonatur = InputUtil.bacaString(scanner, "ID Donatur: ");

            if (donaturService.cariDonatur(idDonatur) != null) {
                return idDonatur;
            }

            donaturView.tampilkanIdDonaturTidakValid();
        }
    }

    private String bacaStatusKelayakan(Scanner scanner) {
        donasiView.tampilkanPilihanStatusKelayakan();

        while (true) {
            int pilihanStatus = InputUtil.bacaInt(scanner, "Status Kelayakan: ");

            if (pilihanStatus == 1) {
                return "Layak";
            }

            if (pilihanStatus == 2) {
                return "Tidak Layak";
            }

            donasiView.tampilkanStatusTidakValid();
        }
    }

    public Donasi cariDonasi(String idDonasi)
            throws SQLException {
        return donasiDAO.findById(idDonasi);
    }
}

