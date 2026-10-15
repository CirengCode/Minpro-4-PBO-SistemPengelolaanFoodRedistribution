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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.Penerima;
import model.PenerimaIndividu;
import model.PenerimaLembaga;

public class PenerimaDAO {

    public List<Penerima> findAll() throws SQLException {
        List<Penerima> dataPenerima = new ArrayList<>();

        String sql = "SELECT id_penerima, jenis_penerima, "
                + "deskripsi_penerima, nama_lembaga, "
                + "jenis_lembaga, nama_pengelola "
                + "FROM penerima "
                + "ORDER BY id_penerima";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                dataPenerima.add(mapResultSet(rs));
            }
        }

        return dataPenerima;
    }

    public Penerima findById(String idPenerima) throws SQLException {
        String sql = "SELECT id_penerima, jenis_penerima, "
                + "deskripsi_penerima, nama_lembaga, "
                + "jenis_lembaga, nama_pengelola "
                + "FROM penerima "
                + "WHERE id_penerima = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPenerima);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }

        return null;
    }

    public boolean insert(Penerima penerima) throws SQLException {
        String sql = "INSERT INTO penerima "
                + "(id_penerima, jenis_penerima, deskripsi_penerima, "
                + "nama_lembaga, jenis_lembaga, nama_pengelola) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, penerima.getIdPenerima());
            stmt.setString(2, penerima.getJenisPenerima());

            if (penerima instanceof PenerimaIndividu individu) {
                stmt.setString(3, individu.getDeskripsiPenerima());
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);

            } else if (penerima instanceof PenerimaLembaga lembaga) {
                stmt.setNull(3, Types.VARCHAR);
                stmt.setString(4, lembaga.getNamaLembaga());
                stmt.setString(5, lembaga.getJenisLembaga());
                stmt.setString(6, lembaga.getNamaPengelola());

            } else {
                throw new IllegalArgumentException("Jenis penerima tidak valid -__-");
            }

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean update(Penerima penerima) throws SQLException {
        String sql = "UPDATE penerima "
                + "SET jenis_penerima = ?, deskripsi_penerima = ?, "
                + "nama_lembaga = ?, jenis_lembaga = ?, "
                + "nama_pengelola = ? "
                + "WHERE id_penerima = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, penerima.getJenisPenerima());

            if (penerima instanceof PenerimaIndividu individu) {
                stmt.setString(2, individu.getDeskripsiPenerima());
                stmt.setNull(3, Types.VARCHAR);
                stmt.setNull(4, Types.VARCHAR);
                stmt.setNull(5, Types.VARCHAR);

            } else if (penerima instanceof PenerimaLembaga lembaga) {
                stmt.setNull(2, Types.VARCHAR);
                stmt.setString(3, lembaga.getNamaLembaga());
                stmt.setString(4, lembaga.getJenisLembaga());
                stmt.setString(5, lembaga.getNamaPengelola());

            } else {
                throw new IllegalArgumentException("Jenis penerima tidak valid -__-");
            }

            stmt.setString(6, penerima.getIdPenerima());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(String idPenerima) throws SQLException {
        String sql = "DELETE FROM penerima "
                + "WHERE id_penerima = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idPenerima);

            return stmt.executeUpdate() > 0;
        }
    }

    private Penerima mapResultSet(ResultSet rs) throws SQLException {
        String idPenerima = rs.getString("id_penerima");
        String jenisPenerima = rs.getString("jenis_penerima");

        if ("Individu".equalsIgnoreCase(jenisPenerima)) {
            return new PenerimaIndividu(
                    idPenerima,
                    rs.getString("deskripsi_penerima")
            );
        }

        if ("Lembaga".equalsIgnoreCase(jenisPenerima)) {
            return new PenerimaLembaga(
                    idPenerima,
                    rs.getString("nama_lembaga"),
                    rs.getString("jenis_lembaga"),
                    rs.getString("nama_pengelola")
            );
        }

        throw new SQLException("Jenis penerima tidak dikenali untuk ID: " + idPenerima);
    }
}
