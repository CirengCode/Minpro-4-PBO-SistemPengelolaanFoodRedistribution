/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

public class MessageView {

    public void tampilkanPilihanTidakValid() {
        System.out.println("------------------------------------");
        System.out.println("[Mohon maaf, pilihan tidak valid! T-T]");
        System.out.println("------------------------------------");
    }

    public void tampilkanBerhasilTambah() {
        System.out.println("------------------------------------");
        System.out.println("[ Yay! Data berhasil ditambahkan ^^ ]");
        System.out.println("------------------------------------");
    }

    public void tampilkanBerhasilUpdate() {
        System.out.println("------------------------------------");
        System.out.println("[ Yay! Data berhasil diperbarui ^^  ]");
        System.out.println("------------------------------------");
    }

    public void tampilkanBerhasilHapus() {
        System.out.println("------------------------------------");
        System.out.println("[ Yay! Data berhasil dihapus ^^     ]");
        System.out.println("------------------------------------");
    }

    public void tampilkanUpdateDibatalkan() {
        System.out.println("------------------------------------");
        System.out.println("[       Update dibatalkan ^^        ]");
        System.out.println("------------------------------------");
    }

    public void tampilkanHapusDibatalkan() {
        System.out.println("------------------------------------");
        System.out.println("[        Hapus dibatalkan ^^        ]");
        System.out.println("------------------------------------");
    }

    public void tampilkanGagalDatabase(String pesan) {
        System.out.println("------------------------------------");
        System.out.println("[ Operasi database gagal! T-T      ]");
        System.out.println("Detail: " + pesan);
        System.out.println("------------------------------------");
    }

    public void tampilkanGagalTambah() {
        System.out.println("------------------------------------");
        System.out.println("[ Gagal menambahkan data -__-!     ]");
        System.out.println("[ Silakan coba kembali.            ]");
        System.out.println("------------------------------------");
    }

    public void tampilkanGagalUpdate() {
        System.out.println("------------------------------------");
        System.out.println("[ Gagal memperbarui data -__-!     ]");
        System.out.println("[ Silakan coba kembali.            ]");
        System.out.println("------------------------------------");
    }

    public void tampilkanGagalHapus() {
        System.out.println("------------------------------------");
        System.out.println("[ Gagal menghapus data -__-!       ]");
        System.out.println("[ Silakan coba kembali.            ]");
        System.out.println("------------------------------------");
    }
}
