package V_From;

import Connection.Koneksi;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.sql.Statement;

public class FormTransaksi extends javax.swing.JFrame {     
// Model tabel daftar stok.
private DefaultTableModel modelStok;
// Peta untuk connect tampilan combo dengan data database.
private final Map<String, BuahData> petaBuah = new LinkedHashMap<>();
private final Map<String, SatuanData> petaSatuan = new LinkedHashMap<>();
private final Map<String, StokData> petaStok = new LinkedHashMap<>();
// Timer untuk periksa stok yang sudah  7 hari.
private javax.swing.Timer timerPemeriksaanStok;
// Simpan data buah yang layak jual.
private static class BuahData {
    String kode;
    String nama;

    BuahData(String kode, String nama) {
        this.kode = kode;
        this.nama = nama;
    }
}

// Simpan data satuan.
private static class SatuanData {
    int id;
    String nama;
    String simbol;

    SatuanData(int id, String nama, String simbol) {
        this.id = id;
        this.nama = nama;
        this.simbol = simbol;
    }
}

// Simpan informasi setiap batch stok.
private static class StokData {
    long id;
    String kodeBuah;
    String namaBuah;
    BigDecimal sisa;
    String simbol;
    String tanggalMasuk;

    StokData(long id, String kodeBuah, String namaBuah,
             BigDecimal sisa, String simbol, String tanggalMasuk) {

        this.id = id;
        this.kodeBuah = kodeBuah;
        this.namaBuah = namaBuah;
        this.sisa = sisa;
        this.simbol = simbol;
        this.tanggalMasuk = tanggalMasuk;
    }
}

// Simpan stok yang sudah mencapai batas 7 hari.
private static class StokKadaluarsa {

    long idStok;
    String kodeBuah;
    int idSatuan;
    String idPenilaian;
    BigDecimal sisa;

    StokKadaluarsa(
            long idStok,
            String kodeBuah,
            int idSatuan,
            String idPenilaian,
            BigDecimal sisa) {

        this.idStok = idStok;
        this.kodeBuah = kodeBuah;
        this.idSatuan = idSatuan;
        this.idPenilaian = idPenilaian;
        this.sisa = sisa;
    }
}

private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormTransaksi.class.getName());
public FormTransaksi() {

    // Buat seluruh komponen GUI.
    initComponents();

    // Tentukan size area desain supaya bisa digulir.
    pnlUtama.setPreferredSize(new Dimension(1100, 850));

    // Buat scrollbar untuk seluruh form.
    javax.swing.JScrollPane scrollForm =
            new javax.swing.JScrollPane(pnlUtama);

    // Tampilkan scrollbar vertikal ketika butuh.
    scrollForm.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );

    // Tampilkan scrollbar horizontal jika butuh.
    scrollForm.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    // Clean container utama JFrame.
    getContentPane().removeAll();

    // Menggunakan layout BorderLayout.
    getContentPane().setLayout(new BorderLayout());

    // Masukkan seluruh panel di dalam scrollbar.
    getContentPane().add(scrollForm, BorderLayout.CENTER);

    // Atur ukuran JFrame.
    setSize(1100, 720);

    // Tempatkan form di tengah layar.
    setLocationRelativeTo(null);

    // Allow pengguna untuk ubah ukuran jendela.
    setResizable(true);

    // Siapkan tabel stok.
    initModelTabel();

    // Atur informasi waktu.
    aturLabelWaktu();

    // Pasang event tombol.
    pasangAksi();

    // Memuat data awal.
    muatDataAwal();

    // Running pemeriksaan stok otomatis.
    mulaiPemeriksaanOtomatis();
} 

// Ambil Timestamp stok base on ID yang baru diSave.
private String ambilTimestampStok(long idStok)
    throws SQLException {

    String sql =
        "SELECT DATE_FORMAT(tanggal_masuk, "
      + "'%d-%m-%Y %H:%i:%s') AS waktu "
      + "FROM tb_stok_transaksi "
      + "WHERE id_stok = ?";

    Connection conn = Koneksi.getKoneksi();

    try (PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setLong(1, idStok);

        try (ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                return rs.getString("waktu");
            }
        }
    }

    return "-";
}

// Siapkan tabel supaya tidak bisa diedit secara langsung.
// Siapkan tabel stok supaya langsung tampilkan data.
private void initModelTabel() {

    modelStok = new DefaultTableModel(
        new Object[]{
            "ID Stok",
            "Kode Buah",
            "Nama Buah",
            "Jumlah Masuk",
            "Satuan",
            "Sisa Stok",
            "Tanggal Masuk",
            "Total Terjual",
            "Penjualan Terakhir",
            "Status Stok"
        }, 0
    ) {

        // Mencegah pengguna mengubah data langsung dari tabel.
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    // Memasang model ke JTable.
    tblStok.setModel(modelStok);

    // Mengatur warna tulisan agar data terlihat.
tblStok.setForeground(java.awt.Color.BLACK);

// Mengatur latar belakang tabel menjadi putih.
tblStok.setBackground(java.awt.Color.WHITE);

// Mengatur warna tulisan ketika baris dipilih.
tblStok.setSelectionForeground(java.awt.Color.WHITE);

// Mengatur warna latar baris yang dipilih.
tblStok.setSelectionBackground(
    new java.awt.Color(102, 0, 0)
);

// Mengatur warna garis tabel.
tblStok.setGridColor(
    new java.awt.Color(180, 180, 180)
);

// Mengaktifkan garis pembatas sel.
tblStok.setShowGrid(true);

// Mengatur tinggi setiap baris.
tblStok.setRowHeight(25);

// Mengatur warna tulisan header tabel.
tblStok.getTableHeader().setForeground(
    java.awt.Color.BLACK
);

// Mengatur warna latar header tabel.
tblStok.getTableHeader().setBackground(
    new java.awt.Color(230, 230, 230)
);
    
    
    // Mengaktifkan tabel.
    tblStok.setEnabled(true);

    // Mengizinkan pemilihan baris.
    tblStok.setRowSelectionAllowed(true);

    // Menonaktifkan pemilihan sel satu per satu.
    tblStok.setCellSelectionEnabled(false);

    // Mengatur tabel memenuhi area scrollbar.
    tblStok.setFillsViewportHeight(true);

    // Mengaktifkan pengaturan kolom dan scrollbar horizontal.
    tblStok.setAutoResizeMode(
        javax.swing.JTable.AUTO_RESIZE_OFF
    );

    // Menghubungkan JTable dengan JScrollPane.
    scrStok.setViewportView(tblStok);

    // Mengaktifkan scrollbar tabel.
    scrStok.setVerticalScrollBarPolicy(
        javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );

    scrStok.setHorizontalScrollBarPolicy(
        javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
}
// kasih informasi kalo waktu dicatat sama database.
private void aturLabelWaktu() {
    lblWaktuMasukInfo.setText(
        "Otomatis dicatat oleh database"
    );
    lblWaktuTransaksiInfo.setText(
        "Otomatis dicatat oleh database"
    );
    lblSisaStokInfo.setText(
        "Belum ada batch yang dipilih"
    );
}

// Memasang event untuk setiap tombol.
private void pasangAksi() {

    btnSimpanStok.addActionListener(e -> simpanStok());

    btnResetStok.addActionListener(e -> resetFormStok());

    btnProsesPenjualan.addActionListener(e -> prosesPenjualan());

    btnResetPenjualan.addActionListener(e -> resetFormPenjualan());

    btnRefreshStok.addActionListener(e -> refreshData());

    cmbStokPenjualan.addActionListener(
        e -> tampilkanSisaStokTerpilih()
    );
}

// Memuat seluruh data awal pada saat form dibuka.
private void muatDataAwal() {

    try {

        // Memindahkan sisa stok yang sudah berumur 7 hari.
        prosesStokLewat7Hari();

        // Memuat data ke setiap komponen.
        muatTampilan();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "Gagal memuat data transaksi: " + e.getMessage(),
            "Kesalahan",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

// Memuat ulang seluruh komponen yang mengambil data database.
private void muatTampilan() throws SQLException {

    muatComboBuahLayak();
    muatComboSatuan();
    muatComboStokPenjualan();
    muatTabelStok();
}

// Periksa ulang data dan perbarui tampilan.
private void refreshData() {

    try {

        int jumlahDipindahkan = prosesStokLewat7Hari();

        muatTampilan();

        JOptionPane.showMessageDialog(
            this,
            "Data berhasil diperbarui.\n"
            + "Stok yang dipindahkan ke badstock: "
            + jumlahDipindahkan,
            "Informasi",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "Gagal memperbarui data: " + e.getMessage(),
            "Kesalahan",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

// Memuat buah yang punya status Layak Jual.
private void muatComboBuahLayak() throws SQLException {

    petaBuah.clear();
    cmbBuahMasuk.removeAllItems();
    cmbBuahMasuk.addItem("-- Pilih Buah Layak Jual --");

    String sql =
        "SELECT DISTINCT b.kode_buah, b.nama_buah "
      + "FROM tb_buah_buahan b "
      + "WHERE EXISTS ("
      + " SELECT 1 FROM tb_penilaian_buah p "
      + " WHERE p.kode_buah = b.kode_buah "
      + " AND p.status_layak = 'Layak Jual'"
      + ") ORDER BY b.nama_buah";

    Connection conn = Koneksi.getKoneksi();

    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {

            String kode = rs.getString("kode_buah");
            String nama = rs.getString("nama_buah");

            BuahData data = new BuahData(kode, nama);

            String tampilan = kode + " - " + nama;

            petaBuah.put(tampilan, data);

            cmbBuahMasuk.addItem(tampilan);
        }
    }
}

// Memuat seluruh pilihan satuan dari tabel master.
private void muatComboSatuan() throws SQLException {

    petaSatuan.clear();

    cmbSatuanMasuk.removeAllItems();

    cmbSatuanMasuk.addItem("-- Pilih Satuan --");

    String sql =
        "SELECT id_satuan, nama_satuan, simbol "
      + "FROM tb_satuan ORDER BY id_satuan";

    Connection conn = Koneksi.getKoneksi();

    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {

            int id = rs.getInt("id_satuan");
            String nama = rs.getString("nama_satuan");
            String simbol = rs.getString("simbol");

            SatuanData data =
                new SatuanData(id, nama, simbol);

            String tampilan = simbol + " - " + nama;

            petaSatuan.put(tampilan, data);

            cmbSatuanMasuk.addItem(tampilan);
        }
    }
}

// Memuat batch stok yang masih aktif dan belum mencapai 7 hari.
private void muatComboStokPenjualan() throws SQLException {

    petaStok.clear();

    cmbStokPenjualan.removeAllItems();

    cmbStokPenjualan.addItem("-- Pilih Stok Tersedia --");

    String sql =
        "SELECT s.id_stok, s.kode_buah, b.nama_buah, "
      + "s.sisa_stok, u.simbol, s.tanggal_masuk "
      + "FROM tb_stok_transaksi s "
      + "JOIN tb_buah_buahan b ON s.kode_buah = b.kode_buah "
      + "JOIN tb_satuan u ON s.id_satuan = u.id_satuan "
      + "WHERE s.status_stok = 'AKTIF' "
      + "AND s.sisa_stok > 0 "
      + "AND s.tanggal_masuk > DATE_SUB(NOW(), INTERVAL 7 DAY) "
      + "ORDER BY s.tanggal_masuk ASC";

    Connection conn = Koneksi.getKoneksi();

    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {

            long id = rs.getLong("id_stok");

            String kode = rs.getString("kode_buah");
            String nama = rs.getString("nama_buah");

            BigDecimal sisa = rs.getBigDecimal("sisa_stok");

            String simbol = rs.getString("simbol");

            String tanggal = rs.getTimestamp(
                "tanggal_masuk"
            ).toString();

            StokData data = new StokData(
                id, kode, nama, sisa, simbol, tanggal
            );

            String tampilan =
                "#" + id + " | " + nama
                + " | " + formatJumlah(sisa) + " " + simbol
                + " | " + tanggal;

            petaStok.put(tampilan, data);

            cmbStokPenjualan.addItem(tampilan);
        }
    }

    lblSisaStokInfo.setText(
        "Pilih batch stok terlebih dahulu"
    );
}

// Tampilkan sisa stok sesuai batch yang dipilih.
private void tampilkanSisaStokTerpilih() {

    String pilihan = Objects.toString(
        cmbStokPenjualan.getSelectedItem(), ""
    );

    StokData data = petaStok.get(pilihan);

    if (data == null) {

        lblSisaStokInfo.setText("Belum dipilih");

        return;
    }

    lblSisaStokInfo.setText(
        formatJumlah(data.sisa) + " " + data.simbol
    );
}

// Tampilkan seluruh riwayat stok masuk.
// Memuat daftar stok beserta riwayat penjualan terakhir.
private void muatTabelStok() throws SQLException {

    // Menghapus isi tabel sebelum memuat data terbaru.
    modelStok.setRowCount(0);

    String sql =
        "SELECT "
      + "s.id_stok, "
      + "s.kode_buah, "
      + "b.nama_buah, "
      + "s.jumlah_masuk, "
      + "u.simbol, "
      + "s.sisa_stok, "
      + "s.tanggal_masuk, "
      + "COALESCE(p.total_terjual, 0) AS total_terjual, "
      + "p.penjualan_terakhir, "
      + "s.status_stok "
      + "FROM tb_stok_transaksi s "
      + "LEFT JOIN tb_buah_buahan b "
      + "ON s.kode_buah = b.kode_buah "
      + "LEFT JOIN tb_satuan u "
      + "ON s.id_satuan = u.id_satuan "
      + "LEFT JOIN ( "
      + "SELECT id_stok, "
      + "SUM(jumlah_terjual) AS total_terjual, "
      + "MAX(tanggal_transaksi) AS penjualan_terakhir "
      + "FROM tb_penjualan "
      + "GROUP BY id_stok "
      + ") p ON s.id_stok = p.id_stok "
      + "ORDER BY s.id_stok DESC";

    Connection conn = Koneksi.getKoneksi();

    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {

            // Mengambil waktu penjualan terakhir.
            java.sql.Timestamp waktuTerakhir =
                    rs.getTimestamp("penjualan_terakhir");

            // Menampilkan tanda minus jika belum ada penjualan.
            String tanggalPenjualan =
                    waktuTerakhir == null
                    ? "-"
                    : waktuTerakhir.toString();

            // Memasukkan data ke dalam tabel.
            modelStok.addRow(new Object[]{

                rs.getLong("id_stok"),

                rs.getString("kode_buah"),

                rs.getString("nama_buah"),

                formatJumlah(
                    rs.getBigDecimal("jumlah_masuk")
                ),

                rs.getString("simbol"),

                formatJumlah(
                    rs.getBigDecimal("sisa_stok")
                ),

                rs.getTimestamp("tanggal_masuk"),

                formatJumlah(
                    rs.getBigDecimal("total_terjual")
                ),

                tanggalPenjualan,

                rs.getString("status_stok")
            });
        }
    }

    // Menghilangkan seleksi baris secara otomatis.
    tblStok.clearSelection();

    // Menampilkan jumlah baris untuk pemeriksaan.
    System.out.println(
        "Jumlah batch stok yang ditampilkan: "
        + modelStok.getRowCount()
    );
}
// Ubah angka supaya tampilanya pake  koma desimal Indonesia.
private String formatJumlah(BigDecimal angka) {

    if (angka == null) {
        return "0";
    }

    return angka.stripTrailingZeros()
                .toPlainString()
                .replace('.', ',');
}

// Validasi jumlah supaya sesuai DECIMAL(12,3).
private BigDecimal validasiJumlah(String teks) {

    String nilai = teks.trim().replace(",", ".");

    // Maksimal 9 digit sebelum koma dan 3 digit sesudah koma.
    if (!nilai.matches("\\d{1,9}(\\.\\d{1,3})?")) {

        throw new IllegalArgumentException(
            "Jumlah harus berupa angka positif "
            + "dengan maksimal 3 angka desimal."
        );
    }

    BigDecimal jumlah = new BigDecimal(nilai)
        .setScale(3, RoundingMode.UNNECESSARY);

    if (jumlah.compareTo(BigDecimal.ZERO) <= 0) {

        throw new IllegalArgumentException(
            "Jumlah harus lebih besar dari nol."
        );
    }

    return jumlah;
}  

// Simpan stok masuk ke database.
private void simpanStok() {
        long idStokBaru = -1;
    try {

        // Mengambil buah yang dipilih.
        String pilihanBuah = Objects.toString(
            cmbBuahMasuk.getSelectedItem(), ""
        );

        BuahData buah = petaBuah.get(pilihanBuah);

        if (buah == null) {
            throw new IllegalArgumentException(
                "Silakan pilih buah terlebih dahulu."
            );
        }

        // Mengambil satuan yang dipilih.
        String pilihanSatuan = Objects.toString(
            cmbSatuanMasuk.getSelectedItem(), ""
        );

        SatuanData satuan = petaSatuan.get(pilihanSatuan);

        if (satuan == null) {
            throw new IllegalArgumentException(
                "Silakan pilih satuan stok."
            );
        }

        // Memvalidasi jumlah stok.
        BigDecimal jumlah = validasiJumlah(
            txtJumlahMasuk.getText()
        );

        Connection conn = Koneksi.getKoneksi();

        boolean autoCommitLama = conn.getAutoCommit();

        try {

            // Memulai transaksi database.
            conn.setAutoCommit(false);

            // Memastikan buah masih berstatus Layak Jual.
String sql =
    "INSERT INTO tb_stok_transaksi "
  + "(kode_buah, id_satuan, id_penilaian, "
  + "jumlah_masuk, sisa_stok) "
  + "SELECT ?, ?, p.id_penilaian, ?, ? "
  + "FROM tb_penilaian_buah p "
  + "WHERE p.kode_buah = ? "
  + "AND p.status_layak = 'Layak Jual' "
  + "LIMIT 1";

            try (PreparedStatement pst =
        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pst.setString(1, buah.kode);
                pst.setInt(2, satuan.id);
                pst.setBigDecimal(3, jumlah);
                pst.setBigDecimal(4, jumlah);
                pst.setString(5, buah.kode);

                int hasil = pst.executeUpdate();

                // Memastikan stok berhasil dimasukkan.
if (hasil != 1) {
    throw new SQLException("Stok gagal disimpan.");
}

//  Ambil ID stok yang baru dibuat.
try (ResultSet keys = pst.getGeneratedKeys()) {

    if (keys.next()) {
        idStokBaru = keys.getLong(1);
    } else {
        throw new SQLException(
            "ID stok baru tidak berhasil diperoleh."
        );
    }
}
                
                // Menolak penyimpanan jika buah tidak lagi layak.
                if (hasil != 1) {
                    throw new SQLException(
                        "Buah tidak lagi berstatus Layak Jual. "
                        + "Silakan periksa hasil penilaian."
                    );
                }
            }

            // Menyimpan transaksi secara permanen.
            conn.commit();

        } catch (Exception e) {

            // Membatalkan seluruh perubahan apabila gagal.
            conn.rollback();

            throw e;

        } finally {

            // Mengembalikan pengaturan koneksi sebelumnya.
            conn.setAutoCommit(autoCommitLama);
        }

        // Mengambil timestamp asli dari database.
        String waktuMasuk = ambilTimestampStok(idStokBaru);

        // Mengosongkan form stok setelah berhasil disimpan.
        resetFormStok();

        // Menampilkan waktu masuk pada panel stok.
        lblWaktuMasukInfo.setText(
            "Tercatat: " + waktuMasuk
        );

        // Menampilkan notifikasi keberhasilan.
        JOptionPane.showMessageDialog(
            this,
            "Stok buah berhasil disimpan.\n"
            + "Jumlah: " + formatJumlah(jumlah)
            + " " + satuan.simbol
            + "\nWaktu masuk: " + waktuMasuk,
            "Berhasil",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Memperbarui tabel dan pilihan batch.
        muatTampilanSetelahTransaksi();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "Gagal menyimpan stok:\n" + e.getMessage(),
            "Kesalahan",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

// Memuat ulang tampilan setelah transaksi berhasil.
private void muatTampilanSetelahTransaksi() {

    try {

        muatComboBuahLayak();
        muatComboSatuan();
        muatComboStokPenjualan();
        muatTabelStok();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "Transaksi berhasil, tetapi tampilan gagal diperbarui: "
            + e.getMessage(),
            "Peringatan",
            JOptionPane.WARNING_MESSAGE
        );
    }
}

/**                 METHOD PROSES PENJUALAN
 *    Ini bagian yang penting. Ketika buah terjual, stok akan langsung berkurang dari batch yang dipilih
 */
private void prosesPenjualan() {

    Connection conn = null;

    try {

        // Mengambil batch yang dipilih.
        String pilihan = Objects.toString(
            cmbStokPenjualan.getSelectedItem(), ""
        );

        StokData stok = petaStok.get(pilihan);

        if (stok == null) {
            throw new IllegalArgumentException(
                "Silakan pilih batch stok terlebih dahulu."
            );
        }

        // Memvalidasi jumlah buah yang terjual.
        BigDecimal jumlah = validasiJumlah(
            txtJumlahTerjual.getText()
        );

        // Memeriksa stok yang sudah mencapai 7 hari.
        prosesStokLewat7Hari();

        conn = Koneksi.getKoneksi();

        boolean autoCommitLama = conn.getAutoCommit();

        try {

            conn.setAutoCommit(false);

            // Memeriksa batch stok yang masih aktif dan belum mencapai 7 hari.
String sqlCek =
    "SELECT s.sisa_stok "
  + "FROM tb_stok_transaksi s "
  + "WHERE s.id_stok = ? "
  + "AND s.status_stok = 'AKTIF' "
  + "AND s.sisa_stok > 0 "
  + "AND s.tanggal_masuk > "
  + "DATE_SUB(NOW(), INTERVAL 7 DAY) "
  + "FOR UPDATE";

            BigDecimal sisaSekarang;

            try (PreparedStatement pst =
                    conn.prepareStatement(sqlCek)) {

                pst.setLong(1, stok.id);

                try (ResultSet rs = pst.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException(
                            "Batch stok sudah tidak aktif, "
                            + "habis, atau telah mencapai 7 hari."
                        );
                    }

                    sisaSekarang = rs.getBigDecimal("sisa_stok");
                }
            }

            // Memastikan penjualan tidak melebihi stok.
            if (jumlah.compareTo(sisaSekarang) > 0) {

                throw new IllegalArgumentException(
                    "Jumlah terjual melebihi sisa stok.\n"
                    + "Sisa stok tersedia: "
                    + formatJumlah(sisaSekarang)
                    + " " + stok.simbol
                );
            }

            // Mengurangi stok dan mengubah status jika habis.
            String sqlUpdate =
                "UPDATE tb_stok_transaksi "
              + "SET status_stok = "
              + "CASE WHEN sisa_stok - ? <= 0 "
              + "THEN 'HABIS' ELSE 'AKTIF' END, "
              + "sisa_stok = sisa_stok - ? "
              + "WHERE id_stok = ? "
              + "AND status_stok = 'AKTIF' "
              + "AND sisa_stok >= ? "
              + "AND tanggal_masuk > "
              + "DATE_SUB(NOW(), INTERVAL 7 DAY)";

            try (PreparedStatement pst =
                    conn.prepareStatement(sqlUpdate)) {

                pst.setBigDecimal(1, jumlah);
                pst.setBigDecimal(2, jumlah);
                pst.setLong(3, stok.id);
                pst.setBigDecimal(4, jumlah);

                int hasil = pst.executeUpdate();

                if (hasil != 1) {
                    throw new SQLException(
                        "Gagal memperbarui stok. "
                        + "Silakan periksa kembali sisa stok."
                    );
                }
            }

            // Menyimpan riwayat penjualan dengan timestamp otomatis.
            String sqlPenjualan =
                "INSERT INTO tb_penjualan "
              + "(id_stok, jumlah_terjual) VALUES (?, ?)";

            try (PreparedStatement pst =
                    conn.prepareStatement(sqlPenjualan)) {

                pst.setLong(1, stok.id);
                pst.setBigDecimal(2, jumlah);

                pst.executeUpdate();
            }

            // Menyelesaikan transaksi database.
            conn.commit();

        } catch (Exception e) {

            // Membatalkan pengurangan stok jika pencatatan gagal.
            conn.rollback();

            throw e;

        } finally {

            // Mengembalikan pengaturan koneksi sebelumnya.
            conn.setAutoCommit(autoCommitLama);
        }

        JOptionPane.showMessageDialog(
            this,
            "Penjualan berhasil dicatat.\n"
            + "Buah: " + stok.namaBuah
            + "\nJumlah terjual: "
            + formatJumlah(jumlah) + " " + stok.simbol,
            "Berhasil",
            JOptionPane.INFORMATION_MESSAGE
        );

        // Mengosongkan input penjualan.
        resetFormPenjualan();

        // Memperbarui stok dan riwayat.
        muatTampilanSetelahTransaksi();

    } catch (Exception e) {

        // Memperbarui tampilan jika batch berubah atau kedaluwarsa.
        if (conn != null) {
            try {
                muatComboStokPenjualan();
                muatTabelStok();
            } catch (Exception ignored) {
                // Tidak mengganti pesan kesalahan utama.
            }
        }

        JOptionPane.showMessageDialog(
            this,
            "Penjualan gagal:\n" + e.getMessage(),
            "Kesalahan",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

/**          METHOD PEMINDAHAN STOK AFTER 7 HARI
 *      Method ini memindahkan hanya sisa stok, bukan jumlah awal.
 * Contoh: stok masuk 10 kg, sudah terjual 7 kg, berarti yang dipindahkan ke badstock hanya 3 kg.
 */
private int prosesStokLewat7Hari() throws Exception {

    Connection conn = Koneksi.getKoneksi();

    boolean autoCommitLama = conn.getAutoCommit();

    int jumlahDipindahkan = 0;

    try {
        conn.setAutoCommit(false);
        // Mengambil batch aktif yang sudah mencapai 7 hari.
        String sqlCek =
    "SELECT id_stok, kode_buah, id_satuan, "
  + "id_penilaian, sisa_stok "
  + "FROM tb_stok_transaksi "
  + "WHERE status_stok = 'AKTIF' "
  + "AND sisa_stok > 0 "
  + "AND tanggal_masuk <= "
  + "DATE_SUB(NOW(), INTERVAL 7 DAY) "
  + "FOR UPDATE";

        List<StokKadaluarsa> daftar =
            new ArrayList<>();

        try (PreparedStatement pst =
                conn.prepareStatement(sqlCek);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                daftar.add(new StokKadaluarsa(
            rs.getLong("id_stok"),
            rs.getString("kode_buah"),
            rs.getInt("id_satuan"),
            rs.getString("id_penilaian"),
            rs.getBigDecimal("sisa_stok")
));
            }
        }

        // Memproses setiap batch yang sudah mencapai batas waktu.
        for (StokKadaluarsa stok : daftar) {

            if (stok.idPenilaian == null
        || stok.idPenilaian.trim().isEmpty()) {

    throw new SQLException(
        "Batch stok dengan ID " + stok.idStok
        + " belum memiliki ID Penilaian asal. "
        + "Periksa hubungan data sebelum pemindahan."
    );
}
            
            // Mengubah status stok menjadi DIPINDAHKAN.
            String sqlUpdate =
                "UPDATE tb_stok_transaksi "
              + "SET sisa_stok = 0, "
              + "status_stok = 'DIPINDAHKAN' "
              + "WHERE id_stok = ? "
              + "AND status_stok = 'AKTIF' "
              + "AND sisa_stok = ?";

            int hasilUpdate;

            try (PreparedStatement pst =
                    conn.prepareStatement(sqlUpdate)) {

                pst.setLong(1, stok.idStok);
                pst.setBigDecimal(2, stok.sisa);

                hasilUpdate = pst.executeUpdate();
            }

            // Mencatat badstock hanya jika batch berhasil diperbarui.
            if (hasilUpdate == 1) {

                String sqlInsert =
    "INSERT INTO tb_badstock "
  + "(kode_buah, id_satuan, id_stok, id_penilaian, "
  + "jenis_badstock, jumlah_badstock, keterangan) "
  + "VALUES (?, ?, ?, ?, 'TIDAK_TERJUAL', ?, ?)";

                try (PreparedStatement pst =
        conn.prepareStatement(sqlInsert)) {

    pst.setString(1, stok.kodeBuah);
    pst.setInt(2, stok.idSatuan);
    pst.setLong(3, stok.idStok);
    pst.setString(4, stok.idPenilaian);
    pst.setBigDecimal(5, stok.sisa);
    pst.setString(6, "Stock tidak terjual");

    pst.executeUpdate();
}
                jumlahDipindahkan++;
            }
        }

        // Menyimpan seluruh pemindahan jika berhasil.
        conn.commit();

    } catch (Exception e) {

        // Membatalkan semua perubahan jika salah satu proses gagal.
        conn.rollback();

        throw e;

    } finally {

        // Mengembalikan pengaturan koneksi sebelumnya.
        conn.setAutoCommit(autoCommitLama);
    }

    return jumlahDipindahkan;
}

/**                             METHOD RESET
 *      Method ini mengosongkan input setelah transaksi atau ketika tombol Reset ditekan.
 */
// Atur ulang form stok masuk.
private void resetFormStok() {

    cmbBuahMasuk.setSelectedIndex(0);

    cmbSatuanMasuk.setSelectedIndex(0);

    txtJumlahMasuk.setText("");

    txtJumlahMasuk.requestFocus();
}
// Atur ulang form penjualan.
private void resetFormPenjualan() {
    cmbStokPenjualan.setSelectedIndex(0);
    txtJumlahTerjual.setText("");
    lblSisaStokInfo.setText("Belum ada batch yang dipilih");
    txtJumlahTerjual.requestFocus();
}

/**             METHOD CEK STOK OTOMATIS
 *  Method ini menjalankan pemeriksaan setiap 60 detik selama FormTransaksi terbuka.
 *  Pemeriksaan juga berjalan saat aplikasi membuka FormTransaksi. Jadi, 
 * stok yang melewati tujuh hari ketika aplikasi sedang mati tetap akan diproses ketika form dibuka kembali.
 */
// Memulai timer pemeriksaan stok otomatis.
private void mulaiPemeriksaanOtomatis() {

    timerPemeriksaanStok = new javax.swing.Timer(
        60000,
        e -> {

            try {

                int jumlah = prosesStokLewat7Hari();

                // Refresh tampilan jika terdapat stok yang dipindahkan.
                if (jumlah > 0) {
                    muatTampilan();
                }

            } catch (Exception ex) {

                // Mencatat kesalahan pemeriksaan tanpa mengganggu pengguna.
                System.err.println(
                    "Pemeriksaan stok otomatis gagal: "
                    + ex.getMessage()
                );
            }
        }
    );

    timerPemeriksaanStok.start();
}
// untuk stop in timer pas  form ditutup.
@Override
public void dispose() {

    if (timerPemeriksaanStok != null) {
        timerPemeriksaanStok.stop();
    }

    super.dispose();
}


















    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlUtama = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        lblJudulForm = new javax.swing.JLabel();
        lblSubJudul = new javax.swing.JLabel();
        pnlStockMasuk = new javax.swing.JPanel();
        lblJudulStockMasuk = new javax.swing.JLabel();
        lblJumlahMasuk = new javax.swing.JLabel();
        cmbBuahMasuk = new javax.swing.JComboBox<>();
        pnlHeaderStokMasuk = new javax.swing.JPanel();
        lblBuahMasuk1 = new javax.swing.JLabel();
        txtJumlahMasuk = new javax.swing.JTextField();
        lblSatuanMasuk = new javax.swing.JLabel();
        cmbSatuanMasuk = new javax.swing.JComboBox<>();
        lblWaktuMasuk = new javax.swing.JLabel();
        lblWaktuMasukInfo = new javax.swing.JLabel();
        pnlBTN = new javax.swing.JPanel();
        btnSimpanStok = new javax.swing.JButton();
        btnResetStok = new javax.swing.JButton();
        pnlPenjualan = new javax.swing.JPanel();
        lblJudulPenjualan = new javax.swing.JLabel();
        lblPilihStok = new javax.swing.JLabel();
        cmbStokPenjualan = new javax.swing.JComboBox<>();
        pnlHeaderPenjualan = new javax.swing.JPanel();
        lblSisaStock = new javax.swing.JLabel();
        lblWaktuTransaksi = new javax.swing.JLabel();
        lblWaktuTransaksiInfo = new javax.swing.JLabel();
        pnlBTN1 = new javax.swing.JPanel();
        btnProsesPenjualan = new javax.swing.JButton();
        btnResetPenjualan = new javax.swing.JButton();
        lblSisaStokInfo = new javax.swing.JLabel();
        lblJumlahTerjual = new javax.swing.JLabel();
        txtJumlahTerjual = new javax.swing.JTextField();
        pnlDaftarStok = new javax.swing.JPanel();
        lblJudulDaftarStok = new javax.swing.JLabel();
        lblSubJudulDaftarBuah = new javax.swing.JLabel();
        scrStok = new javax.swing.JScrollPane();
        tblStok = new javax.swing.JTable();
        btnRefreshStok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Form Transaksi Buah ");
        setPreferredSize(new java.awt.Dimension(1100, 720));
        setResizable(false);

        pnlUtama.setPreferredSize(new java.awt.Dimension(1100, 720));
        pnlUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlHeader.setBackground(new java.awt.Color(102, 0, 0));
        pnlHeader.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblJudulForm.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblJudulForm.setForeground(new java.awt.Color(255, 255, 255));
        lblJudulForm.setText("FORM TRANSAKSI BUAH");

        lblSubJudul.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        lblSubJudul.setForeground(new java.awt.Color(255, 255, 255));
        lblSubJudul.setText("Pengelolaan Stock Masuk dan Penjualan Buah");

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap(361, Short.MAX_VALUE)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                        .addComponent(lblJudulForm)
                        .addGap(378, 378, 378))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                        .addComponent(lblSubJudul)
                        .addGap(366, 366, 366))))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(lblJudulForm)
                .addGap(18, 18, 18)
                .addComponent(lblSubJudul)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pnlUtama.add(pnlHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 980, -1));

        pnlStockMasuk.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlStockMasuk.setForeground(new java.awt.Color(255, 255, 255));
        pnlStockMasuk.setPreferredSize(new java.awt.Dimension(550, 100));
        pnlStockMasuk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJudulStockMasuk.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblJudulStockMasuk.setForeground(new java.awt.Color(255, 255, 255));
        lblJudulStockMasuk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudulStockMasuk.setText("STOK MASUK");
        lblJudulStockMasuk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlStockMasuk.add(lblJudulStockMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        lblJumlahMasuk.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblJumlahMasuk.setForeground(new java.awt.Color(0, 0, 0));
        lblJumlahMasuk.setText("Nama Buah");
        pnlStockMasuk.add(lblJumlahMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 80, 20));

        cmbBuahMasuk.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        cmbBuahMasuk.setForeground(new java.awt.Color(153, 153, 153));
        cmbBuahMasuk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Pilih Buah--" }));
        cmbBuahMasuk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlStockMasuk.add(cmbBuahMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        pnlHeaderStokMasuk.setBackground(new java.awt.Color(102, 102, 102));
        pnlHeaderStokMasuk.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout pnlHeaderStokMasukLayout = new javax.swing.GroupLayout(pnlHeaderStokMasuk);
        pnlHeaderStokMasuk.setLayout(pnlHeaderStokMasukLayout);
        pnlHeaderStokMasukLayout.setHorizontalGroup(
            pnlHeaderStokMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        pnlHeaderStokMasukLayout.setVerticalGroup(
            pnlHeaderStokMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        pnlStockMasuk.add(pnlHeaderStokMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 400, 40));

        lblBuahMasuk1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblBuahMasuk1.setForeground(new java.awt.Color(0, 0, 0));
        lblBuahMasuk1.setText("Jumlah Stock");
        pnlStockMasuk.add(lblBuahMasuk1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, 20));
        pnlStockMasuk.add(txtJumlahMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 120, 30));

        lblSatuanMasuk.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblSatuanMasuk.setForeground(new java.awt.Color(0, 0, 0));
        lblSatuanMasuk.setText("Satuan");
        pnlStockMasuk.add(lblSatuanMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 50, 20));

        cmbSatuanMasuk.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        cmbSatuanMasuk.setForeground(new java.awt.Color(153, 153, 153));
        cmbSatuanMasuk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Pilih Satuan--" }));
        cmbSatuanMasuk.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlStockMasuk.add(cmbSatuanMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 150, -1));

        lblWaktuMasuk.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblWaktuMasuk.setForeground(new java.awt.Color(0, 0, 0));
        lblWaktuMasuk.setText("Waktu Masuk");
        pnlStockMasuk.add(lblWaktuMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, 30));

        lblWaktuMasukInfo.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblWaktuMasukInfo.setForeground(new java.awt.Color(0, 0, 0));
        lblWaktuMasukInfo.setText("Otomatis saat disimpan");
        pnlStockMasuk.add(lblWaktuMasukInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        pnlBTN.setLayout(new java.awt.GridLayout(1, 0));

        btnSimpanStok.setBackground(new java.awt.Color(102, 0, 0));
        btnSimpanStok.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSimpanStok.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpanStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Save2.png"))); // NOI18N
        btnSimpanStok.setText("Simpan");
        btnSimpanStok.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSimpanStok.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlBTN.add(btnSimpanStok);

        btnResetStok.setBackground(new java.awt.Color(51, 51, 51));
        btnResetStok.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnResetStok.setForeground(new java.awt.Color(255, 255, 255));
        btnResetStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Sync3.png"))); // NOI18N
        btnResetStok.setText("Reset");
        btnResetStok.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnResetStok.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlBTN.add(btnResetStok);

        pnlStockMasuk.add(pnlBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 500, 40));

        pnlUtama.add(pnlStockMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 520, 320));

        pnlPenjualan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        pnlPenjualan.setPreferredSize(new java.awt.Dimension(550, 100));
        pnlPenjualan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJudulPenjualan.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblJudulPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        lblJudulPenjualan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudulPenjualan.setText("PENJUALAN");
        lblJudulPenjualan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlPenjualan.add(lblJudulPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        lblPilihStok.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblPilihStok.setForeground(new java.awt.Color(0, 0, 0));
        lblPilihStok.setText("Pilih Batch Stock");
        pnlPenjualan.add(lblPilihStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 120, 40));

        cmbStokPenjualan.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        cmbStokPenjualan.setForeground(new java.awt.Color(153, 153, 153));
        cmbStokPenjualan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Pilih Stok Tersedia--" }));
        cmbStokPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlPenjualan.add(cmbStokPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        pnlHeaderPenjualan.setBackground(new java.awt.Color(102, 102, 102));
        pnlHeaderPenjualan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout pnlHeaderPenjualanLayout = new javax.swing.GroupLayout(pnlHeaderPenjualan);
        pnlHeaderPenjualan.setLayout(pnlHeaderPenjualanLayout);
        pnlHeaderPenjualanLayout.setHorizontalGroup(
            pnlHeaderPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        pnlHeaderPenjualanLayout.setVerticalGroup(
            pnlHeaderPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        pnlPenjualan.add(pnlHeaderPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 400, 40));

        lblSisaStock.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblSisaStock.setForeground(new java.awt.Color(0, 0, 0));
        lblSisaStock.setText("Sisa Stock");
        pnlPenjualan.add(lblSisaStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, 20));

        lblWaktuTransaksi.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblWaktuTransaksi.setForeground(new java.awt.Color(0, 0, 0));
        lblWaktuTransaksi.setText("Waktu Transaksi");
        pnlPenjualan.add(lblWaktuTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 100, 30));

        lblWaktuTransaksiInfo.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblWaktuTransaksiInfo.setForeground(new java.awt.Color(0, 0, 0));
        lblWaktuTransaksiInfo.setText("Otomatis saat disimpan");
        pnlPenjualan.add(lblWaktuTransaksiInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        pnlBTN1.setLayout(new java.awt.GridLayout(1, 0));

        btnProsesPenjualan.setBackground(new java.awt.Color(0, 102, 0));
        btnProsesPenjualan.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnProsesPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        btnProsesPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_buy.png"))); // NOI18N
        btnProsesPenjualan.setText("PROSES PENJUALAN");
        btnProsesPenjualan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnProsesPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlBTN1.add(btnProsesPenjualan);

        btnResetPenjualan.setBackground(new java.awt.Color(51, 51, 51));
        btnResetPenjualan.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnResetPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        btnResetPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Sync3.png"))); // NOI18N
        btnResetPenjualan.setText("Reset");
        btnResetPenjualan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnResetPenjualan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlBTN1.add(btnResetPenjualan);

        pnlPenjualan.add(pnlBTN1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 480, 40));

        lblSisaStokInfo.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblSisaStokInfo.setForeground(new java.awt.Color(0, 0, 0));
        lblSisaStokInfo.setText("Belum dipilih");
        pnlPenjualan.add(lblSisaStokInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 400, 30));

        lblJumlahTerjual.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblJumlahTerjual.setForeground(new java.awt.Color(0, 0, 0));
        lblJumlahTerjual.setText("Jumlah Terjual");
        pnlPenjualan.add(lblJumlahTerjual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, 20));
        pnlPenjualan.add(txtJumlahTerjual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 190, 40));

        pnlUtama.add(pnlPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, 500, 320));

        pnlDaftarStok.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJudulDaftarStok.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblJudulDaftarStok.setForeground(new java.awt.Color(0, 0, 0));
        lblJudulDaftarStok.setText("DAFTAR STOCK BUAH");
        pnlDaftarStok.add(lblJudulDaftarStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 160, 30));

        lblSubJudulDaftarBuah.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblSubJudulDaftarBuah.setForeground(new java.awt.Color(0, 0, 0));
        lblSubJudulDaftarBuah.setText("Stok masuk dan sisa stok");
        pnlDaftarStok.add(lblSubJudulDaftarBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 30));

        tblStok.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        tblStok.setForeground(new java.awt.Color(255, 255, 255));
        tblStok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Stock", "Kode Buah", "Nama Buah", "Jumlah Masuk", "Satuan", "Sisa Stock", "Tanggal Masuk", "Status Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrStok.setViewportView(tblStok);

        pnlDaftarStok.add(scrStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 1020, 300));

        btnRefreshStok.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRefreshStok.setForeground(new java.awt.Color(0, 204, 0));
        btnRefreshStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_VerySmallSync.png"))); // NOI18N
        btnRefreshStok.setText("Sync");
        btnRefreshStok.setContentAreaFilled(false);
        btnRefreshStok.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlDaftarStok.add(btnRefreshStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 20, 90, 30));

        pnlUtama.add(pnlDaftarStok, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 480, 1030, 360));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlUtama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlUtama, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(() -> new FormTransaksi().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnProsesPenjualan;
    private javax.swing.JButton btnRefreshStok;
    private javax.swing.JButton btnResetPenjualan;
    private javax.swing.JButton btnResetStok;
    private javax.swing.JButton btnSimpanStok;
    private javax.swing.JComboBox<String> cmbBuahMasuk;
    private javax.swing.JComboBox<String> cmbSatuanMasuk;
    private javax.swing.JComboBox<String> cmbStokPenjualan;
    private javax.swing.JLabel lblBuahMasuk1;
    private javax.swing.JLabel lblJudulDaftarStok;
    private javax.swing.JLabel lblJudulForm;
    private javax.swing.JLabel lblJudulPenjualan;
    private javax.swing.JLabel lblJudulStockMasuk;
    private javax.swing.JLabel lblJumlahMasuk;
    private javax.swing.JLabel lblJumlahTerjual;
    private javax.swing.JLabel lblPilihStok;
    private javax.swing.JLabel lblSatuanMasuk;
    private javax.swing.JLabel lblSisaStock;
    private javax.swing.JLabel lblSisaStokInfo;
    private javax.swing.JLabel lblSubJudul;
    private javax.swing.JLabel lblSubJudulDaftarBuah;
    private javax.swing.JLabel lblWaktuMasuk;
    private javax.swing.JLabel lblWaktuMasukInfo;
    private javax.swing.JLabel lblWaktuTransaksi;
    private javax.swing.JLabel lblWaktuTransaksiInfo;
    private javax.swing.JPanel pnlBTN;
    private javax.swing.JPanel pnlBTN1;
    private javax.swing.JPanel pnlDaftarStok;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHeaderPenjualan;
    private javax.swing.JPanel pnlHeaderStokMasuk;
    private javax.swing.JPanel pnlPenjualan;
    private javax.swing.JPanel pnlStockMasuk;
    private javax.swing.JPanel pnlUtama;
    private javax.swing.JScrollPane scrStok;
    private javax.swing.JTable tblStok;
    private javax.swing.JTextField txtJumlahMasuk;
    private javax.swing.JTextField txtJumlahTerjual;
    // End of variables declaration//GEN-END:variables
}
