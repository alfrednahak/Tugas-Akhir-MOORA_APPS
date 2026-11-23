package V_From;

import Connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class FormBadstock extends javax.swing.JFrame {    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormBadstock.class.getName());
    private boolean sedangResetFilter = false;
    public FormBadstock() {
    initComponents();
    // Mengatur posisi isi kartu statistik
    aturTampilanKartuStatistik();
    // =====================================
    // PENGATURAN FRAME
    // =====================================
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    // =====================================
    // MENYIAPKAN MODEL TABEL
    // =====================================
    siapkanModelTabel();
    tblData.setRowHeight(30);
    tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    tblData.getTableHeader().setReorderingAllowed(false);
    int[] lebarKolom = {
        45,  // No
        150, // Tanggal Pencatatan
        100, // Kode Buah
        150, // Nama Buah
        280, // Jenis BadStock
        90,  // Jumlah
        80,  // Satuan
        250  // Keterangan
    };
    for (int i = 0; i < lebarKolom.length; i++) {
        tblData.getColumnModel()
                .getColumn(i)
                .setPreferredWidth(lebarKolom[i]);
    }
    jScrollPane1.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    jScrollPane1.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    // =====================================
    // PENGATURAN SCROLL SELURUH FORM
    // =====================================
    mainPanel.setPreferredSize(new java.awt.Dimension(1100, 950));
    javax.swing.JScrollPane formScrollPane =
            new javax.swing.JScrollPane(mainPanel);
    formScrollPane.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    formScrollPane.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    setContentPane(formScrollPane);
    setSize(1120, 760);
    setLocationRelativeTo(null);
    // Mengembalikan posisi seluruh halaman dan tabel ke paling kiri dan atas.
javax.swing.SwingUtilities.invokeLater(() -> {
    formScrollPane.getViewport().setViewPosition(
            new java.awt.Point(0, 0)
    );
    jScrollPane1.getViewport().setViewPosition(
            new java.awt.Point(0, 0)
    );
});
    // =====================================
    // MENGAKTIFKAN FILTER DAN PENCARIAN
    // =====================================
    isiComboFilterJenis();
    pasangListenerPencarianDanFilter();
    //                ====================
    //                  || TOMBOL REFRESH  ||
    //               ====================
    btnRefresh.addActionListener(e -> refreshSemuaData());
    // Tombol Detail BadStock
    btnDetail.addActionListener(e -> tampilkanDetailBadstock());
    // Tombol untuk memproses BadStock yang belum tercatat.
    btnTambah.addActionListener(e -> {
    prosesBadstockTidakLayak(this, true);
    refreshSemuaData(); 
});
    //     =============================
    //      ||  MEMUAT DATA PERTAMA KALI  ||
    //    =============================
refreshSemuaData();

//         ======================
//          || TOMBOL REFRESH ALL  ||
//         ======================
//  FUNGSI NYA UNTUK RESET PENCARIAN DAN FILTER
btnRefreshAll.addActionListener(e -> {
    sedangResetFilter = true;
    try {
        // Kosongkan pencarian.
        txtCari.setText("");

        // Supaya  filter balik ke Semua Jenis (Default).
        cmbFilterJenis.setSelectedIndex(0);
        // Hapus pilihan baris tabel.
        tblData.clearSelection();
    } finally {
        sedangResetFilter = false;
    }
    // Sinkronisasi database dan muat seluruh data terbaru.
    refreshSemuaData();
});
}
   
// METHOD PREPARE MODELTABLE
private void siapkanModelTabel() {

    DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "No",
                "Tanggal Pencatatan",
                "Kode Buah",
                "Nama Buah",
                "Jenis BadStock",
                "Jumlah",
                "Satuan",
                "Keterangan",
                "ID_BADSTOCK"
            }
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    tblData.setModel(model);

    /**             HIDE KOLOM BADSTOCK
     *                          KENAPA??
     * Karena nanti tombol Detail membutuhkan identitas asli data yang dipilih. 
     * ID tersebut tidak perlu ditampilkan kepada pengguna, tetapi tetap tersedia di dalam model tabel.
     * ID tetap tersimpan dalam model untuk fungsi Detail nantinya.
     */
    
    tblData.removeColumn(
            tblData.getColumnModel().getColumn(8)
    );
}

// METHOD filter jenis BadStock
private void isiComboFilterJenis() {

    cmbFilterJenis.removeAllItems();

    cmbFilterJenis.addItem("Semua Jenis");

    cmbFilterJenis.addItem(
            "Buah Tidak Layak Jual - Hasil Penilaian"
    );

    cmbFilterJenis.addItem(
            "BadStock - Buah tidak terjual"
    );
}

// METHOD PENCARIAN DAN LISTENER FILTER
private void pasangListenerPencarianDanFilter() {
    // Pencarian berjalan otomatis ketika pengguna mengetik.
    txtCari.getDocument().addDocumentListener(
            new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (!sedangResetFilter) {
                muatDataBadstock();
            }
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            if (!sedangResetFilter) {
                muatDataBadstock();
            }
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!sedangResetFilter) {
                muatDataBadstock();
            }
        }
    });

    // Filter berdasarkan jenis BadStock.
    cmbFilterJenis.addActionListener(e -> {
        if (!sedangResetFilter) {
            muatDataBadstock();
        }
    });
}
    
//METHOD UTAMA UNTUK BACA DB
private void muatDataBadstock() {

    DefaultTableModel model =
            (DefaultTableModel) tblData.getModel();

    // Kosongkan tabel sebelum memuat ulang.
    model.setRowCount(0);

    String pencarian = txtCari.getText().trim();

    String pilihanFilter =
            cmbFilterJenis.getSelectedItem() == null
            ? "Semua Jenis"
            : cmbFilterJenis.getSelectedItem().toString();

    String jenisDatabase = "";

    if (pilihanFilter.equals(
            "Buah Tidak Layak Jual - Hasil Penilaian")) {

        jenisDatabase = "TIDAK_LAYAK";

    } else if (pilihanFilter.equals(
            "BadStock - Buah tidak terjual")) {

        jenisDatabase = "TIDAK_TERJUAL";
    }

    StringBuilder sql = new StringBuilder();

    sql.append(
        "SELECT "
        + "b.id_badstock, "
        + "b.tanggal_badstock, "
        + "b.kode_buah, "
        + "COALESCE(bb.nama_buah, '(Nama buah tidak ditemukan)') AS nama_buah, "
        + "CASE "
        + "WHEN b.jenis_badstock = 'TIDAK_LAYAK' "
        + "THEN 'Buah Tidak Layak Jual - Hasil Penilaian' "
        + "WHEN b.jenis_badstock = 'TIDAK_TERJUAL' "
        + "THEN 'BadStock - Buah tidak terjual' "
        + "END AS jenis_tampilan, "
        + "b.jumlah_badstock, "
        + "COALESCE(NULLIF(s.simbol, ''), NULLIF(s.nama_satuan, ''), '-') AS satuan, "
        + "b.keterangan "
        + "FROM tb_badstock b "
        + "LEFT JOIN tb_buah_buahan bb "
        + "ON b.kode_buah = bb.kode_buah "
        + "LEFT JOIN tb_satuan s "
        + "ON b.id_satuan = s.id_satuan "
        + "WHERE 1=1 "
    );

    // Filter pencarian.
    if (!pencarian.isEmpty()) {
        sql.append(
            "AND ("
            + "b.kode_buah LIKE ? "
            + "OR bb.nama_buah LIKE ? "
            + "OR b.keterangan LIKE ? "
            + "OR CAST(b.id_badstock AS CHAR) LIKE ?"
            + ") "
        );
    }

    // Filter jenis BadStock.
    if (!jenisDatabase.isEmpty()) {
        sql.append("AND b.jenis_badstock = ? ");
    }

    sql.append(
        "ORDER BY b.tanggal_badstock DESC, b.id_badstock DESC"
    );

    try {

        Connection conn = Koneksi.getKoneksi();

        if (conn == null) {
            throw new Exception("Koneksi database tidak tersedia.");
        }

        try (PreparedStatement pst =
                conn.prepareStatement(sql.toString())) {

            int parameter = 1;

            if (!pencarian.isEmpty()) {

                String keyword = "%" + pencarian + "%";

                pst.setString(parameter++, keyword);
                pst.setString(parameter++, keyword);
                pst.setString(parameter++, keyword);
                pst.setString(parameter++, keyword);
            }

            if (!jenisDatabase.isEmpty()) {
                pst.setString(parameter++, jenisDatabase);
            }

            try (ResultSet rs = pst.executeQuery()) {

                int nomor = 1;

                SimpleDateFormat formatTanggal =
                        new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                while (rs.next()) {

                    Timestamp tanggal =
                            rs.getTimestamp("tanggal_badstock");

                    String tanggalTampil =
                            tanggal == null
                            ? "-"
                            : formatTanggal.format(tanggal);

                    model.addRow(new Object[]{

                        nomor++,

                        tanggalTampil,

                        rs.getString("kode_buah"),

                        rs.getString("nama_buah"),

                        rs.getString("jenis_tampilan"),

                        rs.getBigDecimal("jumlah_badstock")
                        .stripTrailingZeros()
                        .toPlainString(),

                        rs.getString("satuan"),

                        rs.getString("keterangan"),

                        rs.getLong("id_badstock")
                    });
                }
            }
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Gagal memuat data BadStock:\n" + e.getMessage(),
                "Kesalahan Database",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

// FORMAT TAMPILAN STTAISTIK CARD
private String formatKartuStatistik(long jumlah, String judul) {

    return "<html>"
            + "<div style='text-align:center;'>"
            + "<span style='font-size:24pt;"
            + "font-weight:bold;"
            + "color:#1E335A;'>"
            + jumlah
            + "</span>"
            + "<br>"
            + "<span style='font-size:10pt;"
            + "color:#333333;'>"
            + judul
            + "</span>"
            + "</div>"
            + "</html>";
}

// METHOD SET TAMPILAN STATISTIK CARD
private void aturTampilanKartuStatistik() {

    aturPosisiKartu(
            pnlTotalBadstock,
            lblCube,
            totalBadstockLabel
    );

    aturPosisiKartu(
            pnlTidakLayakJual,
            lblWarning,
            tidaklayakjualLabel
    );

    aturPosisiKartu(
            pnlTidakTerjual,
            lblPending,
            tidakterjualLabel
    );
}

// LANJUTAN METHOD SET TAMPILAN STATISTIK CARD
private void aturPosisiKartu(
        javax.swing.JPanel panel,
        javax.swing.JLabel icon,
        javax.swing.JLabel label) {

    // Mengatur posisi komponen secara manual.
    panel.setLayout(null);

    // Icon tetap berada di sisi kiri atas.
    java.awt.Dimension ukuranIcon = icon.getPreferredSize();

    icon.setBounds(
            15,
            10,
            ukuranIcon.width,
            ukuranIcon.height
    );

    // Angka dan judul diposisikan di tengah kartu.
    label.setHorizontalAlignment(
            javax.swing.SwingConstants.CENTER
    );

    label.setVerticalAlignment(
            javax.swing.SwingConstants.CENTER
    );

    label.setBounds(
            10,
            45,
            290,
            75
    );
}

// GET DATA STATISTIK DARI DB
private void perbaruiStatistik() {

    String sql =
            "SELECT "
            + "COUNT(*) AS total_badstock, "

            + "COALESCE(SUM("
            + "CASE WHEN jenis_badstock = 'TIDAK_LAYAK' "
            + "THEN 1 ELSE 0 END"
            + "), 0) AS total_tidak_layak, "

            + "COALESCE(SUM("
            + "CASE WHEN jenis_badstock = 'TIDAK_TERJUAL' "
            + "THEN 1 ELSE 0 END"
            + "), 0) AS total_tidak_terjual "

            + "FROM tb_badstock";

    try {

        Connection conn = Koneksi.getKoneksi();

        if (conn == null) {
            throw new Exception("Koneksi database tidak tersedia.");
        }

        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {

                long totalBadstock =
                        rs.getLong("total_badstock");

                long totalTidakLayak =
                        rs.getLong("total_tidak_layak");

                long totalTidakTerjual =
                        rs.getLong("total_tidak_terjual");

                // Memperbarui kartu Total BadStock.
                totalBadstockLabel.setText(
                        formatKartuStatistik(
                                totalBadstock,
                                "Total BadStock"
                        )
                );

                // Memperbarui kartu Tidak Layak Jual.
                tidaklayakjualLabel.setText(
                        formatKartuStatistik(
                                totalTidakLayak,
                                "Tidak Layak Jual"
                        )
                );

                // Memperbarui kartu Tidak Terjual.
                tidakterjualLabel.setText(
                        formatKartuStatistik(
                                totalTidakTerjual,
                                "Tidak Terjual"
                        )
                );
            }
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Gagal memperbarui statistik BadStock:\n"
                + e.getMessage(),
                "Kesalahan Database",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}

// CREATE FUNGSI SYNC KESELURUHAN
private void refreshSemuaData() {

    try {

        Connection conn = Koneksi.getKoneksi();

        if (conn == null) {
            throw new Exception("Koneksi database tidak tersedia.");
        }

        // Sinkronkan data BadStock dengan status penilaian terbaru.
        sinkronkanBadstockPenilaian(conn);

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Gagal sinkronisasi BadStock:\n" + e.getMessage(),
                "Kesalahan Database",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }

    // Muat ulang tabel dan statistik.
    muatDataBadstock();
    perbaruiStatistik();
}

//   ========================
//    ||  DETAIL DATA BADSTOCK  ||
//   ========================
private void tampilkanDetailBadstock() {

    // 1. Memastikan pengguna memilih baris.
    int barisView = tblData.getSelectedRow();

    if (barisView < 0) {

        JOptionPane.showMessageDialog(
                this,
                "Pilih salah satu data BadStock terlebih dahulu.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    try {

        // 2. Mengambil ID dari kolom tersembunyi.
        int barisModel =
                tblData.convertRowIndexToModel(barisView);

        Object nilaiID =
                tblData.getModel().getValueAt(barisModel, 8);

        long idBadstock =
                Long.parseLong(nilaiID.toString());

        // 3. Mengambil koneksi database.
        Connection conn = Koneksi.getKoneksi();

        if (conn == null) {
            throw new Exception("Koneksi database tidak tersedia.");
        }

        // 4. Query detail BadStock.
        String sql =
                "SELECT "
                + "bs.id_badstock, "
                + "bs.tanggal_badstock, "
                + "bs.kode_buah, "
                + "COALESCE(bb.nama_buah, '-') AS nama_buah, "
                + "bs.jenis_badstock, "
                + "bs.jumlah_badstock, "
                + "COALESCE(s.nama_satuan, '-') AS nama_satuan, "
                + "COALESCE(s.simbol, '') AS simbol, "
                + "bs.keterangan, "
                + "bs.id_penilaian, "
                + "bs.id_stok "
                + "FROM tb_badstock bs "
                + "LEFT JOIN tb_buah_buahan bb "
                + "ON bs.kode_buah = bb.kode_buah "
                + "LEFT JOIN tb_satuan s "
                + "ON bs.id_satuan = s.id_satuan "
                + "WHERE bs.id_badstock = ?";

        try (PreparedStatement pst =
                conn.prepareStatement(sql)) {

            pst.setLong(1, idBadstock);

            try (ResultSet rs = pst.executeQuery()) {

                if (!rs.next()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Data BadStock tidak ditemukan.",
                            "Informasi",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    refreshSemuaData();
                    return;
                }

                // 5. Mengambil seluruh informasi.
                String kodeBuah =
                        rs.getString("kode_buah");

                String namaBuah =
                        rs.getString("nama_buah");

                String jenisDatabase =
                        rs.getString("jenis_badstock");

                String tanggal =
                        rs.getTimestamp("tanggal_badstock") == null
                        ? "-"
                        : new SimpleDateFormat(
                                "dd-MM-yyyy HH:mm:ss"
                        ).format(
                                rs.getTimestamp("tanggal_badstock")
                        );

                java.math.BigDecimal jumlah =
                        rs.getBigDecimal("jumlah_badstock");

                String jumlahTampil =
                        jumlah == null
                        ? "0"
                        : jumlah.stripTrailingZeros().toPlainString();

                String namaSatuan =
                        rs.getString("nama_satuan");

                String simbol =
                        rs.getString("simbol");

                String satuanTampil =
                        (simbol == null || simbol.trim().isEmpty())
                        ? namaSatuan
                        : namaSatuan + " (" + simbol + ")";

                String keterangan =
                        rs.getString("keterangan");

                String idPenilaian =
                        rs.getString("id_penilaian");

                String idStok =
                        rs.getString("id_stok");

                // 6. Menentukan sumber pencatatan.
                String jenisTampil;
                String sumber;

                if ("TIDAK_LAYAK".equalsIgnoreCase(jenisDatabase)) {

                    jenisTampil =
                            "Buah Tidak Layak Jual - Hasil Penilaian";

                    sumber = "FormPenilaian (MOORA)";

                } else if ("TIDAK_TERJUAL".equalsIgnoreCase(jenisDatabase)) {

                    jenisTampil =
                            "BadStock - Buah tidak terjual";

                    sumber = "FormTransaksi (stok melewati 7 hari)";

                } else {

                    jenisTampil = jenisDatabase;
                    sumber = "Tidak diketahui";
                }

                // 7. Menyusun tampilan detail.
                StringBuilder detail = new StringBuilder();

                detail.append("INFORMASI DETAIL BADSTOCK\n");
                detail.append("====================================\n\n");

                detail.append("ID BadStock       : ")
                        .append(idBadstock).append("\n");

                detail.append("Tanggal Pencatatan: ")
                        .append(tanggal).append("\n\n");

                detail.append("INFORMASI BUAH\n");
                detail.append("------------------------------------\n");

                detail.append("Kode Buah         : ")
                        .append(kodeBuah).append("\n");

                detail.append("Nama Buah         : ")
                        .append(namaBuah).append("\n");

                detail.append("Jumlah            : ")
                        .append(jumlahTampil).append("\n");

                detail.append("Satuan            : ")
                        .append(satuanTampil).append("\n\n");

                detail.append("INFORMASI BADSTOCK\n");
                detail.append("------------------------------------\n");

                detail.append("Jenis BadStock    : ")
                        .append(jenisTampil).append("\n");

                detail.append("Sumber Data       : ")
                        .append(sumber).append("\n");

                detail.append("Keterangan        : ")
                        .append(keterangan).append("\n\n");

                detail.append("IDENTITAS SUMBER\n");
                detail.append("------------------------------------\n");

                detail.append("ID Penilaian      : ")
                        .append(idPenilaian == null ? "-" : idPenilaian)
                        .append("\n");

                detail.append("ID Stok Transaksi : ")
                        .append(idStok == null ? "-" : idStok)
                        .append("\n");

                // 8. Menampilkan dialog detail.
                javax.swing.JTextArea areaDetail =
                        new javax.swing.JTextArea(detail.toString());

                areaDetail.setEditable(false);
                areaDetail.setLineWrap(true);
                areaDetail.setWrapStyleWord(true);

                areaDetail.setFont(
                        new java.awt.Font(
                                "Monospaced",
                                java.awt.Font.PLAIN,
                                13
                        )
                );

                areaDetail.setCaretPosition(0);

                javax.swing.JScrollPane scrollDetail =
                        new javax.swing.JScrollPane(areaDetail);

                scrollDetail.setPreferredSize(
                        new java.awt.Dimension(540, 420)
                );

                JOptionPane.showMessageDialog(
                        this,
                        scrollDetail,
                        "Detail BadStock - " + namaBuah,
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }

    } catch (Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Gagal menampilkan detail BadStock:\n"
                + e.getMessage(),
                "Kesalahan Database",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

// ======================================================
//  ||SINKRONISASI BADSTOCK DENGAN HASIL PENILAIAN TERBARU ||
// ======================================================
private static int sinkronkanBadstockPenilaian(
        Connection conn) throws Exception {

    String sql =
            "DELETE bs FROM tb_badstock bs "
            + "LEFT JOIN tb_penilaian_buah p "
            + "ON p.id_penilaian = bs.id_penilaian "
            + "WHERE bs.id_penilaian IS NOT NULL "
            + "AND bs.jenis_badstock = 'TIDAK_LAYAK' "
            + "AND ("
            + "p.id_penilaian IS NULL "
            + "OR COALESCE(p.status_layak, '') <> 'Tidak Layak Jual' "
            + "OR bs.kode_buah <> p.kode_buah"
            + ")";

    try (PreparedStatement pst = conn.prepareStatement(sql)) {

        return pst.executeUpdate();
    }
}

//  =================================
//  || CLASS PEMBANTU DATA BADSTOCK  ||
// =================================
private static class SatuanBadstock {

    int id;
    String nama;
    String simbol;

    SatuanBadstock(int id, String nama, String simbol) {
        this.id = id;
        this.nama = nama;
        this.simbol = simbol;
    }

    @Override
    public String toString() {

        if (simbol == null || simbol.trim().isEmpty()) {
            return nama;
        }

        return nama + " (" + simbol + ")";
    }
}
private static class DataPenilaianBadstock {

    String idPenilaian;
    String kodeBuah;
    String namaBuah;

    DataPenilaianBadstock(
            String idPenilaian,
            String kodeBuah,
            String namaBuah) {

        this.idPenilaian = idPenilaian;
        this.kodeBuah = kodeBuah;
        this.namaBuah = namaBuah;
    }
}

//  ===================================
//   || AMBIL DATA SATUAN DARI DATABASE  ||
// ===================================
private static List<SatuanBadstock> ambilDaftarSatuan(
        Connection conn) throws Exception {

    List<SatuanBadstock> daftar = new ArrayList<>();

    String sql =
            "SELECT id_satuan, nama_satuan, simbol "
            + "FROM tb_satuan "
            + "ORDER BY id_satuan";

    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {

            daftar.add(
                    new SatuanBadstock(
                            rs.getInt("id_satuan"),
                            rs.getString("nama_satuan"),
                            rs.getString("simbol")
                    )
            );
        }
    }

    return daftar;
}

//   ==============================================
//    ||  PERIKSA JUMLAH ALTERNATIF YANG SUDAH DINILAI  ||  
//   ==============================================
//Fungsi nya untuk makesure  dialog otomatis baru diproses setelah data penilaian sampai  30 alt
private static int hitungJumlahPenilaian(Connection conn)
        throws Exception {

    String sql = "SELECT COUNT(*) FROM tb_penilaian_buah";

    try (PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }
    }

    return 0;
}

// ======================================
// PROSES OTOMATIS BUAH TIDAK LAYAK JUAL |
// SUMBER: FORM PENILAIAN MOORA                     |
// ======================================
public static void prosesBadstockTidakLayak(
        Component parent,
        boolean tampilkanInfo) {

    int berhasil = 0;
    int dibatalkan = 0;

    try {

        Connection conn = Koneksi.getKoneksi();

        if (conn == null) {
            throw new Exception("Koneksi database tidak tersedia.");
        }
        // Bersihkan catatan penilaian yang sudah tidak relevan.
            sinkronkanBadstockPenilaian(conn);

        // -------------------------------------------------
        // 1. PASTIKAN 30 ALTERNATIF SUDAH DINILAI
        // -------------------------------------------------

        int jumlahPenilaian = hitungJumlahPenilaian(conn);

        if (jumlahPenilaian != 30) {

            if (tampilkanInfo) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Pencatatan otomatis belum dapat dilakukan.\n\n"
                        + "Jumlah alternatif yang sudah dinilai: "
                        + jumlahPenilaian + " dari 30.",
                        "Penilaian Belum Lengkap",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            return;
        }

        // -------------------------------------------------
        // 2. AMBIL DATA SATUAN
        // -------------------------------------------------

        List<SatuanBadstock> daftarSatuan =
                ambilDaftarSatuan(conn);

        if (daftarSatuan.isEmpty()) {
            throw new Exception(
                    "Data satuan pada tabel tb_satuan masih kosong."
            );
        }

        // -------------------------------------------------
        // 3. AMBIL BUAH TIDAK LAYAK YANG BELUM TERCATAT
        // -------------------------------------------------

        List<DataPenilaianBadstock> daftarBadstock =
                new ArrayList<>();

        String sql =
                "SELECT "
                + "p.id_penilaian, "
                + "p.kode_buah, "
                + "COALESCE(b.nama_buah, p.kode_buah) AS nama_buah "
                + "FROM tb_penilaian_buah p "
                + "LEFT JOIN tb_buah_buahan b "
                + "ON p.kode_buah = b.kode_buah "
                + "WHERE p.status_layak = ? "
                + "AND NOT EXISTS ("
                + "SELECT 1 FROM tb_badstock bs "
                + "WHERE bs.id_penilaian = p.id_penilaian"
                + ") "
                + "ORDER BY p.id_penilaian";

        try (PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "Tidak Layak Jual");

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    daftarBadstock.add(
                            new DataPenilaianBadstock(
                                    rs.getString("id_penilaian"),
                                    rs.getString("kode_buah"),
                                    rs.getString("nama_buah")
                            )
                    );
                }
            }
        }

        // -------------------------------------------------
        // 4. PERIKSA APAKAH ADA DATA YANG PERLU DIPROSES
        // -------------------------------------------------

        if (daftarBadstock.isEmpty()) {

            if (tampilkanInfo) {

                JOptionPane.showMessageDialog(
                        parent,
                        "Tidak ada buah Tidak Layak Jual "
                        + "yang belum dicatat ke BadStock. Semua sudah Tercatat!!!",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            return;
        }

        
        // -------------------------------------------------
        // 5. DIALOG INPUT JUMLAH, SATUAN, DAN KETERANGAN
        // -------------------------------------------------

        for (DataPenilaianBadstock data : daftarBadstock) {

            // INPUT JUMLAH
            // INPUT JUMLAH: BILANGAN BULAT
JSpinner spJumlah = new JSpinner(
        new SpinnerNumberModel(
                1,
                1,
                999999999,
                1
        )
);

// Format angka tanpa desimal.
JSpinner.NumberEditor editor =
        new JSpinner.NumberEditor(
                spJumlah,
                "#,##0"
        );

spJumlah.setEditor(editor);

spJumlah.setPreferredSize(
        new Dimension(280, 36)
);
            // INPUT SATUAN
            JComboBox<SatuanBadstock> cmbSatuan =
                    new JComboBox<>(
                            daftarSatuan.toArray(
                                    new SatuanBadstock[0]
                            )
                    );

            cmbSatuan.setPreferredSize(
                    new Dimension(280, 36)
            );

            // INPUT KETERANGAN
            JTextArea txtKeterangan =
                    new JTextArea(
                            "Buah dinyatakan tidak layak jual "
                            + "berdasarkan hasil penilaian MOORA.",
                            4,
                            40
                    );

            txtKeterangan.setFont(
                    new java.awt.Font("Arial", 0, 13)
            );

            txtKeterangan.setLineWrap(true);
            txtKeterangan.setWrapStyleWord(true);

            JScrollPane scrollKeterangan =
                    new JScrollPane(txtKeterangan);

            scrollKeterangan.setPreferredSize(
                    new Dimension(480, 100)
            );

            // INFORMASI BUAH
            JLabel informasi = new JLabel(
                    "<html>"
                    + "<div style='font-family:Arial;'>"
                    + "<b style='font-size:14px;'>"
                    + "BUAH TIDAK LAYAK JUAL"
                    + "</b>"
                    + "<br><br>"
                    + "ID Penilaian: " + data.idPenilaian
                    + "<br>"
                    + "Kode Buah: " + data.kodeBuah
                    + "<br>"
                    + "Nama Buah: " + data.namaBuah
                    + "<br><br>"
                    + "Masukkan jumlah dan satuan buah."
                    + "</div>"
                    + "</html>"
            );

            // PANEL UTAMA DIALOG
            JPanel panelUtama =
                    new JPanel(new GridBagLayout());

            GridBagConstraints gbc =
                    new GridBagConstraints();

            gbc.insets = new Insets(6, 8, 6, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // BARIS 1: INFORMASI BUAH
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.weightx = 1;

            panelUtama.add(informasi, gbc);

            // BARIS 2: JUMLAH BUAH
            gbc.gridy = 1;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            gbc.weightx = 0;

            panelUtama.add(
                    new JLabel("Jumlah Buah"),
                    gbc
            );

            gbc.gridx = 1;
            gbc.weightx = 1;

            panelUtama.add(spJumlah, gbc);

            // BARIS 3: SATUAN
            gbc.gridy = 2;
            gbc.gridx = 0;
            gbc.weightx = 0;

            panelUtama.add(
                    new JLabel("Satuan"),
                    gbc
            );

            gbc.gridx = 1;
            gbc.weightx = 1;

            panelUtama.add(cmbSatuan, gbc);

            // BARIS 4: JUDUL KETERANGAN
            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.weightx = 1;

            panelUtama.add(
                    new JLabel("Keterangan Kondisi"),
                    gbc
            );

            // BARIS 5: AREA KETERANGAN
            gbc.gridy = 4;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;

            panelUtama.add(
                    scrollKeterangan,
                    gbc
            );

            // UKURAN DIALOG
            panelUtama.setPreferredSize(
                    new Dimension(520, 340)
            );

            int pilihan = JOptionPane.showConfirmDialog(
                    parent,
                    panelUtama,
                    "Pencatatan BadStock - " + data.namaBuah,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

        // -------------------------------------------------
        // 6. JIKA PENGGUNA MENEKAN CANCEL
        // -------------------------------------------------

            if (pilihan != JOptionPane.OK_OPTION) {

                dibatalkan++;

                // Hentikan antrean.
                // Data yang belum diproses bisa dilanjutkan
                // melalui tombol TAMBAH di FormBadstock.
                break;
            }

            SatuanBadstock satuanTerpilih =
                    (SatuanBadstock) cmbSatuan.getSelectedItem();

            if (satuanTerpilih == null) {
                throw new Exception(
                        "Silakan pilih satuan buah."
                );
            }

           // Mengambil jumlah sebagai bilangan bulat.
            int jumlahInput =
            ((Number) spJumlah.getValue()).intValue();

// Mengubah menjadi BigDecimal untuk penyimpanan JDBC.
            BigDecimal jumlah = BigDecimal.valueOf(jumlahInput);

            if (jumlah.compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception(
                        "Jumlah BadStock harus lebih besar dari nol."
                );
            }

            String keterangan =
                    txtKeterangan.getText().trim();

            if (keterangan.isEmpty()) {

                keterangan =
                        "Buah dinyatakan tidak layak jual "
                        + "berdasarkan hasil penilaian MOORA.";
            }

            // -------------------------------------------------
            // 7. SIMPAN KE TABEL TB_BADSTOCK
            // -------------------------------------------------

            String insertSQL =
                    "INSERT INTO tb_badstock "
                    + "("
                    + "kode_buah, "
                    + "id_satuan, "
                    + "id_penilaian, "
                    + "jenis_badstock, "
                    + "jumlah_badstock, "
                    + "keterangan"
                    + ") "
                    + "VALUES (?, ?, ?, 'TIDAK_LAYAK', ?, ?)";

            try (PreparedStatement pst =
                    conn.prepareStatement(insertSQL)) {

                pst.setString(1, data.kodeBuah);
                pst.setInt(2, satuanTerpilih.id);
                pst.setString(3, data.idPenilaian);
                pst.setBigDecimal(4, jumlah);
                pst.setString(5, keterangan);

                pst.executeUpdate();

                berhasil++;
            }
        }

        // -------------------------------------------------
        // 8. INFORMASI HASIL PEMROSESAN
        // -------------------------------------------------

        if (tampilkanInfo) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Proses pencatatan BadStock selesai.\n\n"
                    + "Berhasil dicatat: " + berhasil + "\n"
                    + "Dibatalkan: " + dibatalkan,
                    "Hasil Pencatatan",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

    } catch (Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                parent,
                "Gagal memproses BadStock:\n"
                + e.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE
        );
    }
}













    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        subtitleLabel = new javax.swing.JLabel();
        pnlTotalBadstock = new javax.swing.JPanel();
        totalBadstockLabel = new javax.swing.JLabel();
        lblCube = new javax.swing.JLabel();
        pnlTidakLayakJual = new javax.swing.JPanel();
        lblWarning = new javax.swing.JLabel();
        tidaklayakjualLabel = new javax.swing.JLabel();
        pnlTidakTerjual = new javax.swing.JPanel();
        lblPending = new javax.swing.JLabel();
        tidakterjualLabel = new javax.swing.JLabel();
        pnlFutter = new javax.swing.JPanel();
        dataBadStockLabel = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        cmbFilterJenis = new javax.swing.JComboBox<>();
        pnlBTN = new javax.swing.JPanel();
        btnTambah = new javax.swing.JButton();
        btnDetail = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        btnRefreshAll = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PENGELOLAAN BADSTOCK");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        mainPanel.setPreferredSize(new java.awt.Dimension(1100, 750));
        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        headerPanel.setBackground(new java.awt.Color(30, 51, 90));
        headerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MANAJEMEN BADSTOCK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Malgun Gothic Semilight", 1, 18), new java.awt.Color(255, 255, 255))); // NOI18N
        headerPanel.setForeground(new java.awt.Color(255, 255, 255));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        subtitleLabel.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        subtitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        subtitleLabel.setText("Pengelolaan buah tidak layak jual dan stock buah tidak terjual");
        headerPanel.add(subtitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        mainPanel.add(headerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 1010, 110));

        pnlTotalBadstock.setBackground(new java.awt.Color(255, 255, 255));
        pnlTotalBadstock.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlTotalBadstock.setForeground(new java.awt.Color(0, 0, 0));
        pnlTotalBadstock.setPreferredSize(new java.awt.Dimension(220, 120));

        totalBadstockLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        totalBadstockLabel.setForeground(new java.awt.Color(0, 0, 0));
        totalBadstockLabel.setText("Total BadStock");

        lblCube.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_cube.png"))); // NOI18N

        javax.swing.GroupLayout pnlTotalBadstockLayout = new javax.swing.GroupLayout(pnlTotalBadstock);
        pnlTotalBadstock.setLayout(pnlTotalBadstockLayout);
        pnlTotalBadstockLayout.setHorizontalGroup(
            pnlTotalBadstockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalBadstockLayout.createSequentialGroup()
                .addGroup(pnlTotalBadstockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTotalBadstockLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(totalBadstockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlTotalBadstockLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblCube)))
                .addContainerGap(167, Short.MAX_VALUE))
        );
        pnlTotalBadstockLayout.setVerticalGroup(
            pnlTotalBadstockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTotalBadstockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCube)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(totalBadstockLabel)
                .addGap(14, 14, 14))
        );

        mainPanel.add(pnlTotalBadstock, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 310, 130));

        pnlTidakLayakJual.setBackground(new java.awt.Color(255, 255, 255));
        pnlTidakLayakJual.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlTidakLayakJual.setForeground(new java.awt.Color(0, 0, 0));
        pnlTidakLayakJual.setPreferredSize(new java.awt.Dimension(220, 120));

        lblWarning.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Warning.png"))); // NOI18N

        tidaklayakjualLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tidaklayakjualLabel.setForeground(new java.awt.Color(0, 0, 0));
        tidaklayakjualLabel.setText("Tidak Layak Jual");

        javax.swing.GroupLayout pnlTidakLayakJualLayout = new javax.swing.GroupLayout(pnlTidakLayakJual);
        pnlTidakLayakJual.setLayout(pnlTidakLayakJualLayout);
        pnlTidakLayakJualLayout.setHorizontalGroup(
            pnlTidakLayakJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTidakLayakJualLayout.createSequentialGroup()
                .addGroup(pnlTidakLayakJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTidakLayakJualLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblWarning))
                    .addGroup(pnlTidakLayakJualLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(tidaklayakjualLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(167, Short.MAX_VALUE))
        );
        pnlTidakLayakJualLayout.setVerticalGroup(
            pnlTidakLayakJualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTidakLayakJualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWarning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(tidaklayakjualLabel)
                .addGap(15, 15, 15))
        );

        mainPanel.add(pnlTidakLayakJual, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 160, 310, 130));

        pnlTidakTerjual.setBackground(new java.awt.Color(255, 255, 255));
        pnlTidakTerjual.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlTidakTerjual.setForeground(new java.awt.Color(0, 0, 0));
        pnlTidakTerjual.setPreferredSize(new java.awt.Dimension(220, 120));

        lblPending.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Pending.png"))); // NOI18N

        tidakterjualLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tidakterjualLabel.setForeground(new java.awt.Color(0, 0, 0));
        tidakterjualLabel.setText("Tidak Terjual");

        javax.swing.GroupLayout pnlTidakTerjualLayout = new javax.swing.GroupLayout(pnlTidakTerjual);
        pnlTidakTerjual.setLayout(pnlTidakTerjualLayout);
        pnlTidakTerjualLayout.setHorizontalGroup(
            pnlTidakTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTidakTerjualLayout.createSequentialGroup()
                .addGroup(pnlTidakTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTidakTerjualLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblPending))
                    .addGroup(pnlTidakTerjualLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(tidakterjualLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        pnlTidakTerjualLayout.setVerticalGroup(
            pnlTidakTerjualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTidakTerjualLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPending)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(tidakterjualLabel)
                .addGap(15, 15, 15))
        );

        mainPanel.add(pnlTidakTerjual, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 160, 310, 130));

        pnlFutter.setBackground(new java.awt.Color(255, 255, 255));
        pnlFutter.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlFutter.setForeground(new java.awt.Color(102, 102, 102));
        pnlFutter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dataBadStockLabel.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        dataBadStockLabel.setText("Data BadStock");
        pnlFutter.add(dataBadStockLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        txtCari.setBackground(new java.awt.Color(204, 204, 204));
        txtCari.setForeground(new java.awt.Color(102, 102, 102));
        txtCari.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCari.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlFutter.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 540, 40));

        cmbFilterJenis.setBackground(new java.awt.Color(204, 204, 204));
        cmbFilterJenis.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cmbFilterJenis.setForeground(new java.awt.Color(5, 5, 5));
        cmbFilterJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Filter Jenis" }));
        cmbFilterJenis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlFutter.add(cmbFilterJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 50, 200, 40));

        pnlBTN.setOpaque(false);
        pnlBTN.setLayout(new java.awt.GridLayout(1, 0));

        btnTambah.setBackground(new java.awt.Color(204, 204, 255));
        btnTambah.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(20, 20, 20));
        btnTambah.setText("Tambah");
        pnlBTN.add(btnTambah);

        btnDetail.setBackground(new java.awt.Color(204, 204, 255));
        btnDetail.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnDetail.setForeground(new java.awt.Color(20, 20, 20));
        btnDetail.setText("Detail");
        pnlBTN.add(btnDetail);

        btnRefresh.setBackground(new java.awt.Color(204, 204, 255));
        btnRefresh.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(20, 20, 20));
        btnRefresh.setText("Refresh");
        pnlBTN.add(btnRefresh);

        pnlFutter.add(pnlBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 540, 50));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
                "No", "Tanggal Pencatatan", "Kode Buah", "Nama Buah", "Jenis BadStock", "Jumlah", "Satuan", "Keterangan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblData);

        pnlFutter.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 161, 960, 260));

        btnRefreshAll.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRefreshAll.setForeground(new java.awt.Color(0, 204, 0));
        btnRefreshAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_VerySmallSync.png"))); // NOI18N
        btnRefreshAll.setText("Sync");
        btnRefreshAll.setContentAreaFilled(false);
        btnRefreshAll.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlFutter.add(btnRefreshAll, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 10, -1, 20));

        mainPanel.add(pnlFutter, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 1010, 430));

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setForeground(new java.awt.Color(10, 10, 10));
        jMenu1.setText("Menu");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jMenuItem1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem1.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jMenuItem1.setForeground(new java.awt.Color(10, 10, 10));
        jMenuItem1.setText("Menu Utama/Dashboard");
        jMenuItem1.addActionListener(this::jMenuItem1ActionPerformed);
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(255, 255, 255));
        jMenu2.setForeground(new java.awt.Color(10, 10, 10));
        jMenu2.setText("Akun");
        jMenu2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new FormPenilaian().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FormBadstock().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefreshAll;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cmbFilterJenis;
    private javax.swing.JLabel dataBadStockLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCube;
    private javax.swing.JLabel lblPending;
    private javax.swing.JLabel lblWarning;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pnlBTN;
    private javax.swing.JPanel pnlFutter;
    private javax.swing.JPanel pnlTidakLayakJual;
    private javax.swing.JPanel pnlTidakTerjual;
    private javax.swing.JPanel pnlTotalBadstock;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JTable tblData;
    private javax.swing.JLabel tidaklayakjualLabel;
    private javax.swing.JLabel tidakterjualLabel;
    private javax.swing.JLabel totalBadstockLabel;
    private javax.swing.JTextField txtCari;
    // End of variables declaration//GEN-END:variables
}
