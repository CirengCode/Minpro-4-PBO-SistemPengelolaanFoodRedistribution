/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGenerator {

    public static synchronized String generateId(String prefix) {
        String tabel;
        String kolomId;

        switch (prefix) {
            case "DT0" -> {
                tabel = "donatur";
                kolomId = "id_donatur";
            }
            case "DN0" -> {
                tabel = "donasi";
                kolomId = "id_donasi";
            }
            case "PN0" -> {
                tabel = "penerima";
                kolomId = "id_penerima";
            }
            case "PY0" -> {
                tabel = "penyaluran";
                kolomId = "id_penyaluran";
            }
            default -> throw new IllegalArgumentException(
                    "ID tidak dikenal: " + prefix);
        }

        String kode = prefix.substring(0, prefix.length() - 1);

        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING("
                + kolomId + ", " + (kode.length() + 1)
                + ") AS UNSIGNED)), 0) AS nomor_terbesar "
                + "FROM " + tabel;

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int nomor = rs.getInt("nomor_terbesar") + 1;
                return kode + String.format("%02d", nomor);
            }

            return kode + "01";

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal menghasilkan ID dari database.", e);
        }
    }
}
