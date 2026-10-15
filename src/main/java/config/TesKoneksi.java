/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.SQLException;

public class TesKoneksi {

    public static void main(String[] args) {
        try (Connection conn = Koneksi.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("--------------------------------");
                System.out.println("Yay! Koneksi database berhasil ^^");
                System.out.println("Database: " + conn.getCatalog());
                System.out.println("--------------------------------");
            } else {
                System.out.println("Koneksi gagal :(");
            }

        } catch (SQLException e) {
            System.out.println(
                    "Gagal memeriksa koneksi: " + e.getMessage()
            );
        }
    }
}

