/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.Koneksi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.Penyaluran;

public class PenyaluranDAO {

    private final DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd-MM-uuuu", Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT);

    public List<Penyaluran> findAll() throws SQLException {
        List<Penyaluran> dataPenyaluran = new ArrayList<>();

        String sql = "SELECT * FROM penyaluran";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                dataPenyaluran.add(mapResultSet(rs));
            }
        }

        return dataPenyaluran;
    }

    public Penyaluran findById(String idPenyaluran)
            throws SQLException {

        String sql = "SELECT * FROM penyaluran "
                   + "WHERE id_penyaluran = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPenyaluran);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }

        return null;
    }

    public boolean insert(Penyaluran penyaluran)
            throws SQLException {

        String sql = "INSERT INTO penyaluran "
                   + "(id_penyaluran, id_donasi, id_penerima, "
                   + "nama_kegiatan, tanggal_penyaluran, "
                   + "status_penyaluran, jumlah_porsi, petugas) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, penyaluran.getIdPenyaluran());
            stmt.setString(2, penyaluran.getIdDonasi());
            stmt.setString(3, penyaluran.getIdPenerima());
            stmt.setString(4, penyaluran.getNamaKegiatan());
            stmt.setDate(5, konversiTanggalSQL(penyaluran.getTanggalPenyaluran()));
            stmt.setString(6, penyaluran.getStatusPenyaluran());
            stmt.setInt(7, penyaluran.getJumlahPorsi());
            stmt.setString(8, penyaluran.getPetugas());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean update(Penyaluran penyaluran)
            throws SQLException {

        String sql = "UPDATE penyaluran SET "
                   + "id_donasi = ?, "
                   + "id_penerima = ?, "
                   + "nama_kegiatan = ?, "
                   + "tanggal_penyaluran = ?, "
                   + "status_penyaluran = ?, "
                   + "jumlah_porsi = ?, "
                   + "petugas = ? "
                   + "WHERE id_penyaluran = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, penyaluran.getIdDonasi());
            stmt.setString(2, penyaluran.getIdPenerima());
            stmt.setString(3, penyaluran.getNamaKegiatan());
            stmt.setDate(4, konversiTanggalSQL(penyaluran.getTanggalPenyaluran()));
            stmt.setString(5, penyaluran.getStatusPenyaluran());
            stmt.setInt(6, penyaluran.getJumlahPorsi());
            stmt.setString(7, penyaluran.getPetugas());
            stmt.setString(8, penyaluran.getIdPenyaluran());

            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateStatus(String idPenyaluran, String statusPenyaluran)
            throws SQLException {

        String sql = "UPDATE penyaluran SET status_penyaluran = ? "
                + "WHERE id_penyaluran = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statusPenyaluran);
            stmt.setString(2, idPenyaluran);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(String idPenyaluran)
            throws SQLException {

        String sql = "DELETE FROM penyaluran "
                   + "WHERE id_penyaluran = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPenyaluran);

            return stmt.executeUpdate() > 0;
        }
    }

    private Penyaluran mapResultSet(ResultSet rs)
            throws SQLException {

        Date tanggal = rs.getDate("tanggal_penyaluran");

        return new Penyaluran(
            rs.getString("id_penyaluran"),
            rs.getString("id_donasi"),
            rs.getString("id_penerima"),
            rs.getString("nama_kegiatan"),
            tanggal == null ? null : tanggal.toLocalDate().format(formatter),
            rs.getString("status_penyaluran"),
            rs.getInt("jumlah_porsi"),
            rs.getString("petugas")
        );
    }

    private Date konversiTanggalSQL(String tanggal) {
        LocalDate tanggalLokal = LocalDate.parse(
            tanggal, formatter
        );

        return Date.valueOf(tanggalLokal);
    }
}
