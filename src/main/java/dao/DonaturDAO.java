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
import model.Donatur;
import model.DonaturIndividu;
import model.DonaturInstansi;

public class DonaturDAO {

    public List<Donatur> findAll() throws SQLException {
        List<Donatur> daftarDonatur = new ArrayList<>();

        String sql = "SELECT * FROM donatur " + "ORDER BY id_donatur";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String id = rs.getString("id_donatur");
                    String nama = rs.getString("nama_donatur");
                    String jenis = rs.getString("jenis_donatur");

                    if ("Donatur Individu".equals(jenis)) {
                        daftarDonatur.add(new DonaturIndividu(
                            id,
                            nama,
                            rs.getString("jenis_kegiatan")
                        ));
                    } else if ("Donatur Instansi".equals(jenis)) {
                        daftarDonatur.add(new DonaturInstansi(
                            id,
                            nama,
                            rs.getString("nama_instansi"),
                            rs.getString("jenis_instansi")
                        ));
                    } else {
                        throw new SQLException("Jenis donatur tidak dikenal: " + jenis);
                    }
                }
            }
        }

        return daftarDonatur;
    }

    public Donatur findById(String idDonatur) throws SQLException {
        String sql = "SELECT * FROM donatur "
                   + "WHERE id_donatur = ?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal.");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, idDonatur);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String id = rs.getString("id_donatur");
                        String nama = rs.getString("nama_donatur");
                        String jenis = rs.getString("jenis_donatur");

                        if ("Donatur Individu".equals(jenis)) {
                            return new DonaturIndividu(
                                id,
                                nama,
                                rs.getString("jenis_kegiatan")
                            );
                        } else if ("Donatur Instansi".equals(jenis)) {
                            return new DonaturInstansi(
                                id,
                                nama,
                                rs.getString("nama_instansi"),
                                rs.getString("jenis_instansi")
                            );
                        } else {
                            throw new SQLException(
                                "Jenis donatur tidak dikenal: " + jenis
                            );
                        }
                    }
                }
            }
        }

        return null;
    }

    public boolean insert(Donatur donatur) throws SQLException {
        String sql = "INSERT INTO donatur "
                   + "(id_donatur, nama_donatur, jenis_donatur, "
                   + "jenis_kegiatan, nama_instansi, jenis_instansi) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, donatur.getIdDonatur());
                pstmt.setString(2, donatur.getNamaDonatur());
                pstmt.setString(3, donatur.getJenisDonatur());

                if (donatur instanceof DonaturIndividu individu) {
                    pstmt.setString(4, individu.getJenisKegiatan());
                    pstmt.setNull(5, Types.VARCHAR);
                    pstmt.setNull(6, Types.VARCHAR);
                } else if (donatur instanceof DonaturInstansi instansi) {
                    pstmt.setNull(4, Types.VARCHAR);
                    pstmt.setString(5, instansi.getNamaInstansi());
                    pstmt.setString(6, instansi.getJenisInstansi());
                } else {
                    throw new SQLException("Jenis donatur tidak valid -__-");
                }

                return pstmt.executeUpdate() > 0;
            }
        }
    }

    public boolean update(Donatur donatur) throws SQLException {
        String sql = "UPDATE donatur "
                   + "SET nama_donatur = ?, jenis_donatur = ?, "
                   + "jenis_kegiatan = ?, nama_instansi = ?, "
                   + "jenis_instansi = ? "
                   + "WHERE id_donatur = ?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, donatur.getNamaDonatur());
                pstmt.setString(2, donatur.getJenisDonatur());

                if (donatur instanceof DonaturIndividu individu) {
                    pstmt.setString(3, individu.getJenisKegiatan());
                    pstmt.setNull(4, Types.VARCHAR);
                    pstmt.setNull(5, Types.VARCHAR);
                } else if (donatur instanceof DonaturInstansi instansi) {
                    pstmt.setNull(3, Types.VARCHAR);
                    pstmt.setString(4, instansi.getNamaInstansi());
                    pstmt.setString(5, instansi.getJenisInstansi());
                } else {
                    throw new SQLException("Jenis donatur tidak valid -__-");
                }

                pstmt.setString(6, donatur.getIdDonatur());

                return pstmt.executeUpdate() > 0;
            }
        }
    }

    public boolean delete(String idDonatur) throws SQLException {
        String sql = "DELETE FROM donatur WHERE id_donatur = ?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) {
                throw new SQLException("Koneksi database gagal T-T");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, idDonatur);

                return pstmt.executeUpdate() > 0;
            }
        }
    }
}