/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Donasi;

public class DonasiDAO {

    public List<Donasi> findAll() throws SQLException {
        List<Donasi> daftarDonasi = new ArrayList<>();

        String sql = "SELECT * FROM donasi "
                   + "ORDER BY id_donasi";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    daftarDonasi.add(new Donasi(
                        rs.getString("id_donasi"),
                        rs.getString("id_donatur"),
                        rs.getString("nama_makanan"),
                        rs.getInt("jumlah_porsi"),
                        rs.getString("status_kelayakan")
                    ));
                }
            }
        }

        return daftarDonasi;
    }

    public Donasi findById(String idDonasi) throws SQLException {
        String sql = "SELECT * FROM donasi "
                   + "WHERE id_donasi = ?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, idDonasi);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Donasi(
                            rs.getString("id_donasi"),
                            rs.getString("id_donatur"),
                            rs.getString("nama_makanan"),
                            rs.getInt("jumlah_porsi"),
                            rs.getString("status_kelayakan")
                        );
                    }
                }
            }
        }

        return null;
    }

    public boolean insert(Donasi donasi) throws SQLException {
        String sql = "INSERT INTO donasi "
                   + "(id_donasi, id_donatur, nama_makanan, "
                   + "jumlah_porsi, status_kelayakan) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, donasi.getIdDonasi());
                pstmt.setString(2, donasi.getIdDonatur());
                pstmt.setString(3, donasi.getNamaMakanan());
                pstmt.setInt(4, donasi.getJumlahPorsi());
                pstmt.setString(5, donasi.getStatusKelayakan());

                return pstmt.executeUpdate() > 0;
            }
        }
    }

    public boolean update(Donasi donasi) throws SQLException {
        String sql = "UPDATE donasi "
                   + "SET id_donatur = ?, nama_makanan = ?, "
                   + "jumlah_porsi = ?, status_kelayakan = ? "
                   + "WHERE id_donasi = ?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, donasi.getIdDonatur());
                pstmt.setString(2, donasi.getNamaMakanan());
                pstmt.setInt(3, donasi.getJumlahPorsi());
                pstmt.setString(4, donasi.getStatusKelayakan());
                pstmt.setString(5, donasi.getIdDonasi());

                return pstmt.executeUpdate() > 0;
            }
        }
    }

    public boolean delete(String idDonasi) throws SQLException {
        String sql = "DELETE FROM donasi "
                   + "WHERE id_donasi = ?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, idDonasi);

                return pstmt.executeUpdate() > 0;
            }
        }
    }
}