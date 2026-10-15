-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 15, 2026 at 11:58 AM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pengelolaan_food_redistribution_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `donasi`
--

CREATE TABLE `donasi` (
  `id_donasi` varchar(10) NOT NULL,
  `id_donatur` varchar(10) NOT NULL,
  `nama_makanan` varchar(100) NOT NULL,
  `jumlah_porsi` int NOT NULL,
  `status_kelayakan` varchar(20) NOT NULL
) ;

--
-- Dumping data for table `donasi`
--

INSERT INTO `donasi` (`id_donasi`, `id_donatur`, `nama_makanan`, `jumlah_porsi`, `status_kelayakan`) VALUES
('DN01', 'DT01', 'Muffin', 20, 'Layak'),
('DN02', 'DT02', 'Sapi Lada Hitam', 15, 'Layak'),
('DN03', 'DT03', 'Ayam Bistik', 10, 'Layak'),
('DN04', 'DT04', 'Nasi Ayam', 15, 'Layak'),
('DN05', 'DT04', 'ADA', 4, 'Layak');

-- --------------------------------------------------------

--
-- Table structure for table `donatur`
--

CREATE TABLE `donatur` (
  `id_donatur` varchar(10) NOT NULL,
  `nama_donatur` varchar(100) NOT NULL,
  `jenis_donatur` varchar(30) NOT NULL,
  `jenis_kegiatan` varchar(100) DEFAULT NULL,
  `nama_instansi` varchar(100) DEFAULT NULL,
  `jenis_instansi` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `donatur`
--

INSERT INTO `donatur` (`id_donatur`, `nama_donatur`, `jenis_donatur`, `jenis_kegiatan`, `nama_instansi`, `jenis_instansi`) VALUES
('DT01', 'Budi Santoso', 'Donatur Instansi', NULL, 'Hotel Sejahtera', 'Hotel'),
('DT02', 'Siti Rahma', 'Donatur Instansi', NULL, 'Restoran Makmur', 'Restoran'),
('DT03', 'Andi Wijaya', 'Donatur Instansi', NULL, 'Dapur Berkah', 'Usaha Kuliner'),
('DT04', 'Aulia Fatmawati', 'Donatur Individu', 'Acara Syukuran', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `penerima`
--

CREATE TABLE `penerima` (
  `id_penerima` varchar(10) NOT NULL,
  `jenis_penerima` varchar(20) NOT NULL,
  `deskripsi_penerima` varchar(255) DEFAULT NULL,
  `nama_lembaga` varchar(100) DEFAULT NULL,
  `jenis_lembaga` varchar(100) DEFAULT NULL,
  `nama_pengelola` varchar(100) DEFAULT NULL
) ;

--
-- Dumping data for table `penerima`
--

INSERT INTO `penerima` (`id_penerima`, `jenis_penerima`, `deskripsi_penerima`, `nama_lembaga`, `jenis_lembaga`, `nama_pengelola`) VALUES
('PN01', 'Lembaga', NULL, 'Rumah Singgah Harapan', 'Rumah Singgah', 'Putri Ayu'),
('PN02', 'Lembaga', NULL, 'Panti Asuhan Kasih Ibu', 'Panti Asuhan', 'Siti Rahma'),
('PN03', 'Lembaga', NULL, 'Panti Asuhan Cinta Harapan', 'Rumah Singgah', 'Budi Santoso'),
('PN04', 'Individu', 'Orang yang membutuhkan', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `penyaluran`
--

CREATE TABLE `penyaluran` (
  `id_penyaluran` varchar(10) NOT NULL,
  `id_donasi` varchar(10) NOT NULL,
  `id_penerima` varchar(10) NOT NULL,
  `nama_kegiatan` varchar(150) NOT NULL,
  `tanggal_penyaluran` date NOT NULL,
  `status_penyaluran` varchar(40) NOT NULL,
  `jumlah_porsi` int NOT NULL,
  `petugas` varchar(100) NOT NULL
) ;

--
-- Dumping data for table `penyaluran`
--

INSERT INTO `penyaluran` (`id_penyaluran`, `id_donasi`, `id_penerima`, `nama_kegiatan`, `tanggal_penyaluran`, `status_penyaluran`, `jumlah_porsi`, `petugas`) VALUES
('PY01', 'DN01', 'PN01', 'Penyaluran ke Rumah Singgah', '2026-09-10', 'Dalam Proses Penyaluran', 20, 'Petugas Komunitas'),
('PY02', 'DN02', 'PN02', 'Penyaluran ke Panti Asuhan', '2026-09-01', 'Penyaluran Selesai', 15, 'Petugas Komunitas'),
('PY03', 'DN03', 'PN02', 'Penyaluran ke Panti Asuhan', '2026-07-10', 'Dalam Proses Penyaluran', 10, 'Petugas Komunitas'),
('PY04', 'DN04', 'PN02', 'Pemabagian Makanan untuk Warga', '2026-10-15', 'Dalam Proses Penyaluran', 15, 'Relawan');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `donasi`
--
ALTER TABLE `donasi`
  ADD PRIMARY KEY (`id_donasi`),
  ADD KEY `fk_donasi_donatur` (`id_donatur`);

--
-- Indexes for table `donatur`
--
ALTER TABLE `donatur`
  ADD PRIMARY KEY (`id_donatur`);

--
-- Indexes for table `penerima`
--
ALTER TABLE `penerima`
  ADD PRIMARY KEY (`id_penerima`);

--
-- Indexes for table `penyaluran`
--
ALTER TABLE `penyaluran`
  ADD PRIMARY KEY (`id_penyaluran`),
  ADD KEY `fk_penyaluran_donasi` (`id_donasi`),
  ADD KEY `fk_penyaluran_penerima` (`id_penerima`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `donasi`
--
ALTER TABLE `donasi`
  ADD CONSTRAINT `fk_donasi_donatur` FOREIGN KEY (`id_donatur`) REFERENCES `donatur` (`id_donatur`);

--
-- Constraints for table `penyaluran`
--
ALTER TABLE `penyaluran`
  ADD CONSTRAINT `fk_penyaluran_donasi` FOREIGN KEY (`id_donasi`) REFERENCES `donasi` (`id_donasi`),
  ADD CONSTRAINT `fk_penyaluran_penerima` FOREIGN KEY (`id_penerima`) REFERENCES `penerima` (`id_penerima`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
