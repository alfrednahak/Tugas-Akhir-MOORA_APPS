package V_From;

import Connection.Koneksi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import  java.util.Locale;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

    public class FormPenilaian extends javax.swing.JFrame {    
        private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormPenilaian.class.getName());
    
        int sx, sy;
   //    =========================================================================
   //     || KONSTANTA (angka-angka aturan, sengaja dikumpulkan di sini supaya mudah diganti) ||
   //    =========================================================================
    // Batas bawah dan atas skala penilaian C1..C8 (sesuai Batasan Masalah: skala 1-5).
    private static final double SKALA_MIN = 1.0;
    private static final double SKALA_MAX = 5.0;

    // Toleransi selisih total bobot terhadap 1,00 (untuk menutupi pembulatan desimal).
    private static final double TOLERANSI_BOBOT = 0.001; 
    // Faktor pengali untuk mengubah Yi menjadi Si.
    private static final double FAKTOR_SKOR_KELAYAKAN = 100.0;
    // Ambang kelayakan tetap pada skala Si (15 = Yi 0,15).
    private static final double AMBANG_TETAP = 15.0;
    /** =======================================================================
    *    || DATA BOBOT & JENIS KRITERIA (diisi dari tabel tb_kriteria oleh method muatKriteria) ||
    /** =======================================================================
 
    /** bobotKriteria[0] = bobot C1, ..., bobotKriteria[7] = bobot C8. */
    private final double[] bobotKriteria = new double[8];
    /** kriteriaBenefit[j] = true jika kriteria Benefit, false jika Cost. */
    private final boolean[] kriteriaBenefit = new boolean[8];

    private static class DataNilai {
        String id;                   // id_penilaian
        double[] c = new double[8];  // c[0] = C1, c[1] = C2, ..., c[7] = C8
    }
 
    // ================
    //  || KONSTRUKTOR ||
    // ================
    public FormPenilaian() {
        initComponents();            // membangun tampilan (kode bawaan NetBeans, jangan diubah)
        pasangListenerPencarianDanFilter();
        setLocationRelativeTo(null); // form muncul di tengah layar pada saat dirunn
        sesuaikanLabelKriteria();    // samakan teks label dengan nama kriteria di Bab I
        loadComboBuah();             // isi pilihan buah dari tabel tb_buah_buahan
        loadTableData();             // tampilkan data penilaian di tabel bawah
 
        // Saat baris tabel diklik, isi data baris itu ke form di atas
        tblPenilaian.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                isiFormDariTabel();
            }    
        });
    }
private void aturTampilan() {

    // =====================================
    // WARNA TEMA
    // =====================================
    java.awt.Color hijau = new java.awt.Color(0, 87, 73);
    java.awt.Color hijauHover = new java.awt.Color(0, 110, 91);
    java.awt.Color putih = java.awt.Color.WHITE;
    java.awt.Color background = new java.awt.Color(244, 247, 246);
    java.awt.Color border = new java.awt.Color(215, 225, 221);
    java.awt.Color merah = new java.awt.Color(180, 45, 45);

    java.awt.Font fontUtama =
            new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12);

    // =====================================
    // BACKGROUND FORM
    // =====================================
    getContentPane().setBackground(background);

    // =====================================
    // JUDUL FORM
    // =====================================

    lblJudul.setFont(
            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15)
    );
    lblJudul.setForeground(putih);

    // =====================================
    // PANEL TRANSAKSI
    // =====================================
    pnl_IndukInputan.setBackground(background);

    pnl_IndukInputan.setBorder(
        javax.swing.BorderFactory.createTitledBorder(
            javax.swing.BorderFactory.createLineBorder(border),
            "Data Transaksi Buah",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13),
            hijau
        )
    );

    // =====================================
    // PANEL KRITERIA DAN TRANSAKSI
    // =====================================
    javax.swing.JPanel[] panels = {
        pnlC1_C4, pnlC5_C8, pnlId_Penilaian
    };

    for (javax.swing.JPanel panel : panels) {
        panel.setBackground(hijau);
        panel.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                    new java.awt.Color(0, 65, 55), 1
                ),
                javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)
            )
        );
    }

    // =====================================
    // FONT LABEL
    // =====================================
    javax.swing.JLabel[] labels = {
        lbl_PilihBuah, lbl_IdPenilaian, lbl_NilaiYi, lbl_StatusLayak,
        lbl_C1, lbl_C3, lbl_C2, lbl_C4,
        lbl_C5, lbl_C6, lbl_C7, lbl_C8
    };

    for (javax.swing.JLabel label : labels) {
        label.setFont(
            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)
        );
        label.setForeground(putih);
    }

    // =====================================
    // TEXT FIELD
    // =====================================
    javax.swing.JTextField[] fields = {
        txtIdPenilaian,
        txtC1, txtC2, txtC3, txtC4,
        txtC5, txtC6, txtC7, txtC8,
        txtNilaiYi, txtStatusLayak
    };

    for (javax.swing.JTextField field : fields) {
        field.setFont(fontUtama);
        field.setForeground(new java.awt.Color(35, 45, 42));
        field.setBackground(putih);
        field.setBorder(
            javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(border),
                javax.swing.BorderFactory.createEmptyBorder(5, 8, 5, 8)
            )
        );
        field.setCaretColor(hijau);
    }

    // Field hasil perhitungan
    txtNilaiYi.setBackground(new java.awt.Color(235, 242, 239));
    txtStatusLayak.setBackground(new java.awt.Color(235, 242, 239));

    // =====================================
    // COMBOBOX BUAH
    // =====================================
    cbBuah.setFont(fontUtama);
    cbBuah.setBackground(putih);
    cbBuah.setForeground(new java.awt.Color(35, 45, 42));
    cbBuah.setCursor(
        new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
    );

    // =====================================
    // TABEL PENILAIAN
    // =====================================
    tblPenilaian.setFont(fontUtama);
    tblPenilaian.setRowHeight(28);
    tblPenilaian.setForeground(new java.awt.Color(40, 50, 46));
    tblPenilaian.setBackground(putih);
    tblPenilaian.setSelectionBackground(
        new java.awt.Color(210, 235, 226)
    );
    tblPenilaian.setSelectionForeground(
        new java.awt.Color(0, 65, 55)
    );
    tblPenilaian.setGridColor(
        new java.awt.Color(230, 235, 232)
    );
    tblPenilaian.setShowVerticalLines(false);
    tblPenilaian.setShowHorizontalLines(true);
    tblPenilaian.setIntercellSpacing(
        new java.awt.Dimension(0, 1)
    );
    tblPenilaian.setFillsViewportHeight(true);

    // Header tabel
    tblPenilaian.getTableHeader().setFont(
        new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12)
    );
    tblPenilaian.getTableHeader().setBackground(hijau);
    tblPenilaian.getTableHeader().setForeground(putih);
    tblPenilaian.getTableHeader().setPreferredSize(
        new java.awt.Dimension(0, 34)
    );
    tblPenilaian.getTableHeader().setReorderingAllowed(false);

    // Rata tengah isi tabel
    javax.swing.table.DefaultTableCellRenderer tengah =
        new javax.swing.table.DefaultTableCellRenderer();

    tengah.setHorizontalAlignment(
        javax.swing.SwingConstants.CENTER
    );

    for (int i = 2; i <= 10; i++) {
        tblPenilaian.getColumnModel()
                .getColumn(i).setCellRenderer(tengah);
    }

    // Lebar kolom tabel
    int[] lebarKolom = {
        100, 90,
        50, 50, 50, 50, 50, 50, 50, 50,
        90, 145
    };

    for (int i = 0; i < lebarKolom.length; i++) {
        tblPenilaian.getColumnModel()
                .getColumn(i)
                .setPreferredWidth(lebarKolom[i]);
    }

    jspLoadData.setBorder(
        javax.swing.BorderFactory.createLineBorder(border)
    );

    jspLoadData.getViewport().setBackground(putih);

    // =====================================
    // TOMBOL
    // =====================================
    javax.swing.JButton[] tombol = {
        btnHitungUlang, btnSimpan1, btnReset1,
        btnUbah1, btnHapus1, btnHitung
    };

    for (javax.swing.JButton button : tombol) {
        button.setFont(
            new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11)
        );
        button.setForeground(putih);
        button.setBackground(hijau);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(
            new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
        );
        button.setMargin(
            new java.awt.Insets(5, 10, 5, 10)
        );
    }

    // Tombol hapus dibedakan
    btnHapus1.setBackground(merah);

    // Tombol perhitungan utama
    btnHitung.setBackground(hijauHover);

    // Jarak antar tombol
    pnlBTN.setOpaque(false);
    pnlBTN.setLayout(
        new java.awt.GridLayout(1, 6, 8, 0)
    );
}

// Nonaktifkan tombol keluar khusus kalo  form ditampilkan di Dashboard
public void prepareForDashboardCard() {
lblKELUAR.setVisible(false);
}
private void sesuaikanLabelKriteria() {
        // Nama kriteria disamakan dengan penulisan di Bab I skripsi
        lbl_C1.setText("C1 - Warna Kulit");
        lbl_C2.setText("C2 - Ukuran");
        lbl_C3.setText("C3 - Tekstur / Kekenyalan");
        lbl_C4.setText("C4 - Tingkat Kematangan");
        lbl_C5.setText("C5 - Berat");               // sebelumnya "Berat / Bobot", bentrok dengan arti bobot kriteria
        lbl_C6.setText("C6 - Ketebalan Kulit");
        lbl_C7.setText("C7 - Daya Tahan Simpan");
        lbl_C8.setText("C8 - Kerusakan Fisik");     // dipendekkan agar tidak menimpa kolom input
 
        // Petunjuk yang muncul saat kursor diarahkan ke kolom input
        String petunjukBenefit = "Isi angka " + (int) SKALA_MIN + "-" + (int) SKALA_MAX
                + " (Benefit: makin besar = makin baik)";
        String petunjukCost = "Isi angka " + (int) SKALA_MIN + "-" + (int) SKALA_MAX
                + " (Cost: makin besar = makin rusak/buruk)";
        txtC1.setToolTipText(petunjukBenefit);
        txtC2.setToolTipText(petunjukBenefit);
        txtC3.setToolTipText(petunjukBenefit);
        txtC4.setToolTipText(petunjukBenefit);
        txtC5.setToolTipText(petunjukBenefit);
        txtC6.setToolTipText(petunjukBenefit);
        txtC7.setToolTipText(petunjukBenefit);
        txtC8.setToolTipText(petunjukCost);
    } 
    //              ============================
    //               ||  MEMUAT DATA KE TAMPILAN  ||
    //              ============================
    //  Isi cmb buah dari tabel tb_buah_buahan. Format item: "B001 - Apel". 
    private void loadComboBuah() {
        cbBuah.removeAllItems();
        cbBuah.addItem("-- Pilih Buah --");   // item pertama = belum memilih
        try {
            Connection conn = Koneksi.getKoneksi();   // koneksi bersama, jangan ditutup
            // try-with-resources: Statement dan ResultSet otomatis ditutup setelah selesai
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM tb_buah_buahan ORDER BY kode_buah")) {
                while (rs.next()) {
                    cbBuah.addItem(rs.getString("kode_buah") + " - " + rs.getString("nama_buah"));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data buah: " + e.getMessage());
        }
    }
    
//    FUNCT REFRESH
    private void pasangListenerPencarianDanFilter() {
    // Pencarian berjalan otomatis ketika pengguna ngetik.
    txtCari.getDocument().addDocumentListener(
            new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            loadTableData();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            loadTableData();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            loadTableData();
        }
    });

    // Filter berdasarkan jenis BadStock.
//    txtCari.addActionListener(e -> loadTableData());
}   private void loadTableData() {
    // Model tabel dibuat tidak bisa diedit
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    // Judul kolom tabel
    model.addColumn("ID Penilaian");
    model.addColumn("Kode Buah");
    model.addColumn("C1");
    model.addColumn("C2");
    model.addColumn("C3");
    model.addColumn("C4");
    model.addColumn("C5");
    model.addColumn("C6");
    model.addColumn("C7");
    model.addColumn("C8");
    model.addColumn("Nilai Yi");
    model.addColumn("Status Layak");

    String pencarian = txtCari.getText().trim();

    try {
        Connection conn = Koneksi.getKoneksi();
        
        // Buat query SQL dengan filter WHERE LIKE supaya pencarian berfungsi
        // Kita bisa cari berdasarkan id_penilaian ATAU kode_buah
        String sql = "SELECT * FROM tb_penilaian_buah WHERE id_penilaian LIKE ? OR kode_buah LIKE ? ORDER BY id_penilaian";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Masukkan keyword pencarian dengan simbol persen (%) agar pencarian bersifat fleksibel 
            // (misal ketik "PNL" atau "B001" bisa langsung ketemu)
            String keyword = "%" + pencarian + "%";
            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("id_penilaian"),
                        rs.getString("kode_buah"),
                        rs.getDouble("c1"),
                        rs.getDouble("c2"),
                        rs.getDouble("c3"),
                        rs.getDouble("c4"),
                        rs.getDouble("c5"),
                        rs.getDouble("c6"),
                        rs.getDouble("c7"),
                        rs.getDouble("c8"),
                        rs.getDouble("nilai_yi"),
                        rs.getString("status_layak")
                    });
                }
            }
        }

        tblPenilaian.setModel(model);   // Tampilkan data yang sudah difilter ke tabel
         aturTabel();     // pasang lagi lebar kolom & rata tengah
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal load tabel: " + e.getMessage());
    }
}
  
    // Atur tampilan tabel. Harus dipanggil SETELAH setModel(),
    // karena setModel() membuat ulang kolom dan menghapus lebar/renderer.
    private void aturTabel() {
        tblPenilaian.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tblPenilaian.setRowHeight(26);
        tblPenilaian.getTableHeader().setReorderingAllowed(false);

        javax.swing.table.DefaultTableCellRenderer tengah =
                new javax.swing.table.DefaultTableCellRenderer();
        tengah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        int[] lebar = {100, 90, 50, 50, 50, 50, 50, 50, 50, 50, 90, 145};
        for (int i = 0; i < lebar.length; i++) {
            javax.swing.table.TableColumn kolom = tblPenilaian.getColumnModel().getColumn(i);
            kolom.setPreferredWidth(lebar[i]);
            if (i >= 2 && i <= 10) {          // C1..C8 dan Nilai Yi rata tengah
                kolom.setCellRenderer(tengah);
            }
        }
    }

//    Kosongkan seluruh isian form dan mengembalikannya ke kondisi awal.
    private void clearForm() {
        txtIdPenilaian.setText("");
        txtIdPenilaian.setEditable(true);   // buka kembali kunci ID agar bisa diisi data baru
        cbBuah.setSelectedIndex(0);         // kembali ke "-- Pilih Buah --"
        txtC1.setText("");
        txtC2.setText("");
        txtC3.setText("");
        txtC4.setText("");
        txtC5.setText("");
        txtC6.setText("");
        txtC7.setText("");
        txtC8.setText("");
        txtNilaiYi.setText("");
        txtStatusLayak.setText("");
        tblPenilaian.clearSelection();      // hapus sorotan baris yang tadi dipilih di tabel
        txtIdPenilaian.requestFocus();      // kursor kembali ke kolom ID Penilaian
    }
    
    // =====================================
    // METHOD PEMBANTU (konversi dan validasi)
    // =====================================
//  Mengubah isi JTextField menjadi angka. Koma (3,5) otomatis dianggap titik (3.5). 
    private double angka(javax.swing.JTextField tf) {
        return Double.parseDouble(tf.getText().trim().replace(",", "."));
    }
 
//  Tulis angka dengan 4 desimal dan TITIK desimal (0.2867), apa pun pengaturan bahasa komputer. 
    private String format4(double nilai) {
        return String.format(Locale.US, "%.4f", nilai);
    }
 
//  Ambil kode buah dari cmb. Contoh: "B001 - Apel" menjadi "B001". 
    private String ambilKodeBuah() {
        return cbBuah.getSelectedItem().toString().split(" - ")[0];
    }
 
    private boolean validasiSkala() {

    // Kumpul seluruh kolom penilaian C1 sampai C8.
    javax.swing.JTextField[] kolom = {
        txtC1, txtC2, txtC3, txtC4,
        txtC5, txtC6, txtC7, txtC8
    };

    // Menentukan nama setiap kriteria untuk pesan kesalahan.
    String[] namaKriteria = {
        "Warna Kulit",
        "Ukuran",
        "Tekstur/Kekenyalan",
        "Tingkat Kematangan",
        "Berat",
        "Ketebalan Kulit",
        "Daya Tahan Simpan",
        "Tingkat Kerusakan/Cacat Fisik"
    };

    // Memeriksa setiap kolom penilaian.
    for (int i = 0; i < kolom.length; i++) {

        // Mengambil teks dan menghapus spasi di awal/akhir.
        String teks = kolom[i].getText().trim();

        // Memastikan kolom tidak kosong.
        if (teks.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nilai " + namaKriteria[i] + " wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);

            kolom[i].requestFocus();
            return false;
        }

        // Mengubah input menjadi angka.
        double nilai;

        try {
            nilai = Double.parseDouble(teks.replace(",", "."));
        } catch (NumberFormatException e) {

            // Menampilkan pesan jika input bukan angka.
            JOptionPane.showMessageDialog(this,
                    "Nilai " + namaKriteria[i] + " harus berupa angka!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);

            kolom[i].requestFocus();
            return false;
        }

        // Memastikan angka berada dalam skala bulat 1 sampai 5.
        if (Double.isNaN(nilai)
                || Double.isInfinite(nilai)
                || nilai < SKALA_MIN
                || nilai > SKALA_MAX
                || nilai != Math.rint(nilai)) {

            JOptionPane.showMessageDialog(this,
                    "Nilai " + namaKriteria[i]
                    + " harus berupa bilangan bulat 1 sampai 5!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);

            kolom[i].requestFocus();
            return false;
        }
    }

    // Semua kolom sudah memenuhi ketentuan.
    return true;
}
 
    // =====================================================================
    // MEMBACA BOBOT KRITERIA DAN DATA PENILAIAN DARI DATABASE
    // =====================================================================
    private void muatKriteria(Connection conn) throws Exception {
        java.util.Arrays.fill(bobotKriteria, 0);
        java.util.Arrays.fill(kriteriaBenefit, false);
        boolean[] ditemukan = new boolean[8];   // penanda: kriteria C1..C8 sudah terbaca atau belum
 
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tb_kriteria")) {
            while (rs.next()) {
                String kode = rs.getString("kode_kriteria").trim();
                int idx;
                try {
                    // "C1" menjadi indeks 0, "C8" menjadi indeks 7
                    idx = Integer.parseInt(kode.toUpperCase().replace("C", "")) - 1;
                } catch (NumberFormatException e) {
                    throw new Exception("Kode kriteria '" + kode + "' tidak valid. Gunakan C1 sampai C8.");
                }
                if (idx >= 0 && idx < 8) {
                    bobotKriteria[idx] = rs.getDouble("bobot");
                    String tipe = rs.getString("type");
                    kriteriaBenefit[idx] = (tipe != null) && "Benefit".equalsIgnoreCase(tipe.trim());
                    ditemukan[idx] = true;
                }
            }
        }
 
        // Pastikan kedelapan kriteria ada
        for (int j = 0; j < 8; j++) {
            if (!ditemukan[j]) {
                throw new Exception("Kriteria C" + (j + 1)
                        + " belum ada di tabel kriteria. Lengkapi dulu di Menu Kriteria.");
            }
        }
 
        // Pastikan tidak ada bobot negatif dan total bobot = 1,00
        double totalBobot = 0;
        for (int j = 0; j < 8; j++) {
            if (bobotKriteria[j] < 0) {
                throw new Exception("Bobot C" + (j + 1) + " tidak boleh negatif.");
            }
            totalBobot += bobotKriteria[j];
        }
        if (Math.abs(totalBobot - 1.0) > TOLERANSI_BOBOT) {
            throw new Exception("Total bobot kriteria harus 1,00 (sekarang "
                    + String.format(Locale.US, "%.2f", totalBobot)
                    + "). Perbaiki dulu di Menu Kriteria.");
        }
    }
    private List<DataNilai> bacaSemuaPenilaian(Connection conn, String idKecuali) throws Exception {
        List<DataNilai> daftar = new ArrayList<>();
        String sql = "SELECT id_penilaian, c1, c2, c3, c4, c5, c6, c7, c8 FROM tb_penilaian_buah";
 
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                if (idKecuali != null && idKecuali.equals(rs.getString("id_penilaian"))) {
                    continue;   // lewati baris yang dikecualikan
                }
                DataNilai d = new DataNilai();
                d.id = rs.getString("id_penilaian");
                for (int j = 0; j < 8; j++) {
                    d.c[j] = rs.getDouble("c" + (j + 1));   // kolom c1..c8
                }
                daftar.add(d);
            }
        }
        return daftar;
    }
    
    // ========================
    // INTI PERHITUNGAN MOORA
    // ========================
    private double[] hitungYi(List<DataNilai> data) {
        // Langkah 1: penyebut normalisasi = akar jumlah kuadrat tiap kolom (C1..C8)
        double[] penyebut = new double[8];
        for (DataNilai d : data) {
            for (int j = 0; j < 8; j++) {
                penyebut[j] += d.c[j] * d.c[j];   // jumlahkan kuadrat nilai kolom j
            }
        }
        for (int j = 0; j < 8; j++) {
            penyebut[j] = Math.sqrt(penyebut[j]); // akar jumlah kuadrat
        }
 
        // Langkah 2: normalisasi, kali bobot, lalu benefit dijumlahkan dan cost dikurangkan
        double[] yi = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < 8; j++) {
                // jika penyebut 0 (semua nilai kolom 0), nilai ternormalisasi dianggap 0
                double normal = (penyebut[j] == 0) ? 0 : data.get(i).c[j] / penyebut[j];
                double berbobot = normal * bobotKriteria[j];
                yi[i] += kriteriaBenefit[j] ? berbobot : -berbobot;   // Benefit (+), Cost (-)
            }
        }
        return yi;
    }
 
   // Mengambil ambang batas tetap yang sudah ditentukan.
    private double hitungAmbang(double[] yi) {

    // Mengembalikan ambang tetap sebesar 15 pada skala Si.
    return AMBANG_TETAP;
}

    // Ubah nilai preferensi MOORA (Yi) menjadi skor kelayakan (Si).
    private double hitungSkorKelayakan(double nilaiYi) {

    // Mengalikan nilai Yi dengan faktor pengali 100.
    return nilaiYi * FAKTOR_SKOR_KELAYAKAN;
}

// Tentukan status kelayakan base on skor dan Threshold.
private String tentukanStatus(double yi, double ambang) {

    // Mengubah nilai Yi menjadi skor kelayakan Si.
    double skorKelayakan = hitungSkorKelayakan(yi);

    // Memeriksa apakah skor mencapai atau melebihi ambang batas.
    if (skorKelayakan >= ambang) {

        // Mengembalikan status jika alternatif memenuhi ambang.
        return "Layak Jual";
    }

    // Mengembalikan status jika alternatif tidak memenuhi ambang.
    return "Tidak Layak Jual";
}

// Berikan keterangan bahwa sistem menggunakan ambang tetap.
private String keteranganAmbang() {

    // Menampilkan ambang tetap dalam format dua angka desimal.
    return "Ambang tetap Si = "
            + String.format(Locale.US, "%.2f", AMBANG_TETAP);
}

private int hitungUlangSemua() throws Exception {

    // Mengambil koneksi database.
    Connection conn = Koneksi.getKoneksi();

    // Membaca bobot dan jenis seluruh kriteria.
    muatKriteria(conn);

    // Mengambil seluruh data penilaian dari database.
    List<DataNilai> data = bacaSemuaPenilaian(conn, null);

    // Menghentikan proses jika belum ada data.
    if (data.isEmpty()) {
        return 0;
    }

    // Menghitung nilai optimasi seluruh alternatif.
    double[] yi = hitungYi(data);

    // Mengambil ambang batas tetap sebesar 15 pada skala Si.
    double ambang = hitungAmbang(yi);

    // Menyiapkan perintah untuk memperbarui nilai Yi dan status.
    String sql = "UPDATE tb_penilaian_buah "
            + "SET nilai_yi=?, status_layak=? WHERE id_penilaian=?";

    // Menjalankan pembaruan menggunakan PreparedStatement.
    try (PreparedStatement pst = conn.prepareStatement(sql)) {

        // Memproses seluruh alternatif.
        for (int i = 0; i < data.size(); i++) {

            // Membulatkan Yi menjadi empat angka desimal.
            double yiEmpatDesimal =
                    Math.round(yi[i] * 10000.0) / 10000.0;

            // Menyimpan nilai Yi yang sudah dibulatkan.
            pst.setDouble(1, yiEmpatDesimal);

            // Menentukan status menggunakan nilai Yi yang sama.
            pst.setString(2, tentukanStatus(yiEmpatDesimal, ambang));

            // Menentukan ID alternatif yang akan diperbarui.
            pst.setString(3, data.get(i).id);

            // Memasukkan perintah ke dalam batch.
            pst.addBatch();
        }

        // Menjalankan seluruh pembaruan database.
        pst.executeBatch();
    }

    // Mengembalikan jumlah alternatif yang telah dihitung.
    return data.size();
}

    private boolean sudahDinilai(Connection conn, String kodeBuah, String idKecuali) throws Exception {
        String sql = "SELECT COUNT(*) FROM tb_penilaian_buah WHERE kode_buah=? AND id_penilaian<>?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, kodeBuah);
            pst.setString(2, idKecuali);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private void isiFormDariTabel() {
        int baris = tblPenilaian.getSelectedRow();
        if (baris < 0) {
            return;   // tidak ada baris yang dipilih
        }
 
        txtIdPenilaian.setText(tblPenilaian.getValueAt(baris, 0).toString());
        txtIdPenilaian.setEditable(false);   // ID adalah kunci data, tidak boleh diubah saat edit
 
        // Pilih buah yang sesuai di combo (item combo berformat "B001 - Nama")
        String kode = tblPenilaian.getValueAt(baris, 1).toString();
        cbBuah.setSelectedIndex(0);
        for (int i = 0; i < cbBuah.getItemCount(); i++) {
            if (cbBuah.getItemAt(i).startsWith(kode + " - ")) {
                cbBuah.setSelectedIndex(i);
                break;
            }
        }
 
        txtC1.setText(tblPenilaian.getValueAt(baris, 2).toString());
        txtC2.setText(tblPenilaian.getValueAt(baris, 3).toString());
        txtC3.setText(tblPenilaian.getValueAt(baris, 4).toString());
        txtC4.setText(tblPenilaian.getValueAt(baris, 5).toString());
        txtC5.setText(tblPenilaian.getValueAt(baris, 6).toString());
        txtC6.setText(tblPenilaian.getValueAt(baris, 7).toString());
        txtC7.setText(tblPenilaian.getValueAt(baris, 8).toString());
        txtC8.setText(tblPenilaian.getValueAt(baris, 9).toString());
        txtNilaiYi.setText(tblPenilaian.getValueAt(baris, 10).toString());
        txtStatusLayak.setText(tblPenilaian.getValueAt(baris, 11).toString());
    }                    
                    
    // =======================================================
    // Code dibawah ini Bawaan dari NetBeans (Generated Code)
    // =======================================================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        pnl_IndukInputan = new javax.swing.JPanel();
        pnlC1_C4 = new javax.swing.JPanel();
        lbl_C1 = new javax.swing.JLabel();
        lbl_C3 = new javax.swing.JLabel();
        lbl_C2 = new javax.swing.JLabel();
        lbl_C4 = new javax.swing.JLabel();
        txtC4 = new javax.swing.JTextField();
        txtC3 = new javax.swing.JTextField();
        txtC1 = new javax.swing.JTextField();
        txtC2 = new javax.swing.JTextField();
        pnlC5_C8 = new javax.swing.JPanel();
        lbl_C5 = new javax.swing.JLabel();
        lbl_C6 = new javax.swing.JLabel();
        lbl_C7 = new javax.swing.JLabel();
        lbl_C8 = new javax.swing.JLabel();
        txtC5 = new javax.swing.JTextField();
        txtC6 = new javax.swing.JTextField();
        txtC7 = new javax.swing.JTextField();
        txtC8 = new javax.swing.JTextField();
        pnlId_Penilaian = new javax.swing.JPanel();
        lbl_IdPenilaian = new javax.swing.JLabel();
        lbl_LogoIdPenilaian = new javax.swing.JLabel();
        txtIdPenilaian = new javax.swing.JTextField();
        lbl_PilihBuah = new javax.swing.JLabel();
        lbl_LogocbBuah = new javax.swing.JLabel();
        cbBuah = new javax.swing.JComboBox<>();
        lbl_LogoYi = new javax.swing.JLabel();
        txtNilaiYi = new javax.swing.JTextField();
        lbl_NilaiYi = new javax.swing.JLabel();
        lbl_StatusLayak = new javax.swing.JLabel();
        txtStatusLayak = new javax.swing.JTextField();
        jspLoadData = new javax.swing.JScrollPane();
        tblPenilaian = new javax.swing.JTable();
        lblKELUAR = new javax.swing.JLabel();
        pnlBTN = new javax.swing.JPanel();
        btnHitungUlang = new javax.swing.JButton();
        btnSimpan1 = new javax.swing.JButton();
        btnReset1 = new javax.swing.JButton();
        btnUbah1 = new javax.swing.JButton();
        btnHapus1 = new javax.swing.JButton();
        btnHitung = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        pnlUtama = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        lbl_LogoNaomi = new javax.swing.JLabel();
        MenuInduk = new javax.swing.JMenuBar();
        mainMenu = new javax.swing.JMenu();
        JItemMenuBuah = new javax.swing.JMenuItem();
        JItemMenuKriteria = new javax.swing.JMenuItem();
        JItemCetakRanking = new javax.swing.JMenuItem();
        jmenuBadStock = new javax.swing.JMenuItem();
        MenuAkun = new javax.swing.JMenu();
        jItemMenuUtama = new javax.swing.JMenuItem();
        JItemLogout = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_IndukInputan.setBackground(new java.awt.Color(233, 237, 243));
        pnl_IndukInputan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_IndukInputan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlC1_C4.setBackground(new java.awt.Color(233, 237, 243));
        pnlC1_C4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC1_C4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_C1.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C1.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C1.setText("C1 - Warna Kulit");
        lbl_C1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC1_C4.add(lbl_C1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 170, 40));

        lbl_C3.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C3.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C3.setText("C3 - Tekstur / Kekenyalan");
        lbl_C3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC1_C4.add(lbl_C3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 170, 40));

        lbl_C2.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C2.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C2.setText("C2 - Ukuran Buah");
        lbl_C2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC1_C4.add(lbl_C2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 170, 40));

        lbl_C4.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C4.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C4.setText("C4 - Tingkat Kematangan");
        lbl_C4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC1_C4.add(lbl_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 170, 40));

        txtC4.setBackground(new java.awt.Color(233, 237, 243));
        txtC4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC4KeyPressed(evt);
            }
        });
        pnlC1_C4.add(txtC4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 120, 40));

        txtC3.setBackground(new java.awt.Color(233, 237, 243));
        txtC3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC3KeyPressed(evt);
            }
        });
        pnlC1_C4.add(txtC3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 120, 40));

        txtC1.setBackground(new java.awt.Color(233, 237, 243));
        txtC1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC1.addActionListener(this::txtC1ActionPerformed);
        txtC1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC1KeyPressed(evt);
            }
        });
        pnlC1_C4.add(txtC1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 120, 40));

        txtC2.setBackground(new java.awt.Color(233, 237, 243));
        txtC2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC2KeyPressed(evt);
            }
        });
        pnlC1_C4.add(txtC2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 120, 40));

        pnl_IndukInputan.add(pnlC1_C4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 330, 250));

        pnlC5_C8.setBackground(new java.awt.Color(233, 237, 243));
        pnlC5_C8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC5_C8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_C5.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C5.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C5.setText("C5 - Berat");
        lbl_C5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC5_C8.add(lbl_C5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 220, 40));

        lbl_C6.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C6.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C6.setText("C6 - Ketebalan Kulit");
        lbl_C6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC5_C8.add(lbl_C6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 220, 40));

        lbl_C7.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C7.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C7.setText("C7 - Daya Tahan Simpan");
        lbl_C7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC5_C8.add(lbl_C7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 220, 40));

        lbl_C8.setBackground(new java.awt.Color(233, 237, 243));
        lbl_C8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl_C8.setForeground(new java.awt.Color(10, 10, 10));
        lbl_C8.setText("C8 - Tingkat Kerusakan/Cacat Fisik");
        lbl_C8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlC5_C8.add(lbl_C8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 220, 40));

        txtC5.setBackground(new java.awt.Color(233, 237, 243));
        txtC5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC5.addActionListener(this::txtC5ActionPerformed);
        txtC5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC5KeyPressed(evt);
            }
        });
        pnlC5_C8.add(txtC5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 110, 40));

        txtC6.setBackground(new java.awt.Color(233, 237, 243));
        txtC6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC6KeyPressed(evt);
            }
        });
        pnlC5_C8.add(txtC6, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 110, 40));

        txtC7.setBackground(new java.awt.Color(233, 237, 243));
        txtC7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC7KeyPressed(evt);
            }
        });
        pnlC5_C8.add(txtC7, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 110, 40));

        txtC8.setBackground(new java.awt.Color(233, 237, 243));
        txtC8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtC8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtC8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtC8KeyPressed(evt);
            }
        });
        pnlC5_C8.add(txtC8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 200, 110, 40));

        pnl_IndukInputan.add(pnlC5_C8, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 360, 250));

        pnlId_Penilaian.setBackground(new java.awt.Color(233, 237, 243));
        pnlId_Penilaian.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlId_Penilaian.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_IdPenilaian.setBackground(new java.awt.Color(233, 237, 243));
        lbl_IdPenilaian.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_IdPenilaian.setForeground(new java.awt.Color(10, 10, 10));
        lbl_IdPenilaian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_IdPenilaian.setText("Id_penilaian");
        lbl_IdPenilaian.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lbl_IdPenilaian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlId_Penilaian.add(lbl_IdPenilaian, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, 40));

        lbl_LogoIdPenilaian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_AccountIDPENILAIAN2.png"))); // NOI18N
        pnlId_Penilaian.add(lbl_LogoIdPenilaian, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, 40));

        txtIdPenilaian.setBackground(null);
        txtIdPenilaian.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtIdPenilaian.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 207, 216)), javax.swing.BorderFactory.createEmptyBorder(0, 29, 0, 4)));
        pnlId_Penilaian.add(txtIdPenilaian, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 140, 40));

        lbl_PilihBuah.setBackground(new java.awt.Color(233, 237, 243));
        lbl_PilihBuah.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_PilihBuah.setForeground(new java.awt.Color(10, 10, 10));
        lbl_PilihBuah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_PilihBuah.setText("Pilih buah");
        lbl_PilihBuah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lbl_PilihBuah.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlId_Penilaian.add(lbl_PilihBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 100, 40));

        lbl_LogocbBuah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_buah2.png"))); // NOI18N
        pnlId_Penilaian.add(lbl_LogocbBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, -1, 40));

        cbBuah.setBackground(new java.awt.Color(233, 237, 243));
        cbBuah.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        cbBuah.setForeground(new java.awt.Color(10, 10, 10));
        cbBuah.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih Buah --", " " }));
        cbBuah.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 207, 216)), javax.swing.BorderFactory.createEmptyBorder(0, 30, 0, 4)));
        cbBuah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlId_Penilaian.add(cbBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 140, 40));

        lbl_LogoYi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Calculator.png"))); // NOI18N
        pnlId_Penilaian.add(lbl_LogoYi, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 30, 40));

        txtNilaiYi.setEditable(false);
        txtNilaiYi.setBackground(null);
        txtNilaiYi.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtNilaiYi.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(201, 207, 216), 2), javax.swing.BorderFactory.createEmptyBorder(0, 29, 0, 4)));
        pnlId_Penilaian.add(txtNilaiYi, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 140, 40));

        lbl_NilaiYi.setBackground(new java.awt.Color(233, 237, 243));
        lbl_NilaiYi.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_NilaiYi.setForeground(new java.awt.Color(10, 10, 10));
        lbl_NilaiYi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_NilaiYi.setText("Nilai yi");
        lbl_NilaiYi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lbl_NilaiYi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlId_Penilaian.add(lbl_NilaiYi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 100, 40));

        lbl_StatusLayak.setBackground(new java.awt.Color(233, 237, 243));
        lbl_StatusLayak.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lbl_StatusLayak.setForeground(new java.awt.Color(10, 10, 10));
        lbl_StatusLayak.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_StatusLayak.setText("Status Layak");
        lbl_StatusLayak.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lbl_StatusLayak.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        pnlId_Penilaian.add(lbl_StatusLayak, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 100, 40));

        txtStatusLayak.setEditable(false);
        txtStatusLayak.setBackground(null);
        txtStatusLayak.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtStatusLayak.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlId_Penilaian.add(txtStatusLayak, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, 140, 40));

        pnl_IndukInputan.add(pnlId_Penilaian, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 290, 250));

        getContentPane().add(pnl_IndukInputan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 1020, 290));

        jspLoadData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jspLoadDataKeyPressed(evt);
            }
        });

        tblPenilaian.setBackground(new java.awt.Color(233, 237, 243));
        tblPenilaian.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblPenilaian.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        tblPenilaian.setForeground(new java.awt.Color(10, 10, 10));
        tblPenilaian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Penilaian", "Kode Buah", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "Nilai Yi", "Status Layak"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jspLoadData.setViewportView(tblPenilaian);

        getContentPane().add(jspLoadData, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 1035, 273));

        lblKELUAR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblKELUARMouseClicked(evt);
            }
        });
        getContentPane().add(lblKELUAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 0, -1, -1));

        pnlBTN.setOpaque(false);
        pnlBTN.setLayout(new java.awt.GridLayout(1, 0));

        btnHitungUlang.setBackground(new java.awt.Color(0, 51, 51));
        btnHitungUlang.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnHitungUlang.setForeground(new java.awt.Color(255, 255, 255));
        btnHitungUlang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_HitungUlang.png"))); // NOI18N
        btnHitungUlang.setText("HITUNG ULANG");
        btnHitungUlang.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHitungUlang.addActionListener(this::btnHitungUlangActionPerformed);
        pnlBTN.add(btnHitungUlang);

        btnSimpan1.setBackground(new java.awt.Color(0, 51, 51));
        btnSimpan1.setFont(new java.awt.Font("Sylfaen", 1, 12)); // NOI18N
        btnSimpan1.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_SavePenilaian.png"))); // NOI18N
        btnSimpan1.setText("SIMPAN");
        btnSimpan1.setToolTipText("");
        btnSimpan1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSimpan1.addActionListener(this::btnSimpanActionPerformed);
        pnlBTN.add(btnSimpan1);

        btnReset1.setBackground(new java.awt.Color(0, 51, 51));
        btnReset1.setFont(new java.awt.Font("Sylfaen", 1, 12)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(255, 255, 255));
        btnReset1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_SyncPenilaian.png"))); // NOI18N
        btnReset1.setText("RESET");
        btnReset1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnReset1.addActionListener(this::btnResetActionPerformed);
        pnlBTN.add(btnReset1);

        btnUbah1.setBackground(new java.awt.Color(0, 51, 51));
        btnUbah1.setFont(new java.awt.Font("Sylfaen", 1, 12)); // NOI18N
        btnUbah1.setForeground(new java.awt.Color(255, 255, 255));
        btnUbah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_EditPenilaian.png"))); // NOI18N
        btnUbah1.setText("EDIT");
        btnUbah1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbah1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnUbah1.setPreferredSize(new java.awt.Dimension(100, 36));
        btnUbah1.addActionListener(this::btnUbahActionPerformed);
        pnlBTN.add(btnUbah1);

        btnHapus1.setBackground(new java.awt.Color(0, 51, 51));
        btnHapus1.setFont(new java.awt.Font("Sylfaen", 1, 12)); // NOI18N
        btnHapus1.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_DeletePenilaian.png"))); // NOI18N
        btnHapus1.setText("HAPUS");
        btnHapus1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHapus1.addActionListener(this::btnHapusActionPerformed);
        pnlBTN.add(btnHapus1);

        btnHitung.setBackground(new java.awt.Color(0, 51, 51));
        btnHitung.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnHitung.setForeground(new java.awt.Color(255, 255, 255));
        btnHitung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_HitungMOORA.png"))); // NOI18N
        btnHitung.setText("HITUNG MOORA");
        btnHitung.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnHitung.addActionListener(this::btnHitungActionPerformed);
        pnlBTN.add(btnHitung);

        getContentPane().add(pnlBTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 690, 900, 40));

        txtCari.setBackground(new java.awt.Color(233, 237, 243));
        txtCari.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtCari.setForeground(new java.awt.Color(10, 10, 10));
        txtCari.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 365, 300, 35));

        pnlUtama.setBackground(new java.awt.Color(233, 237, 243));
        pnlUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJudul.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(10, 10, 10));
        lblJudul.setText("FORM PENILAIAN & PERHITUNGAN MOORA");
        lblJudul.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.pink, java.awt.Color.yellow, java.awt.Color.pink, java.awt.Color.yellow));
        pnlUtama.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 520, 40));

        lbl_LogoNaomi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/img_Logo_Naomi_64x64.png"))); // NOI18N
        pnlUtama.add(lbl_LogoNaomi, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 70));

        getContentPane().add(pnlUtama, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, 1050, 750));

        MenuInduk.setBackground(new java.awt.Color(233, 237, 243));
        MenuInduk.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        mainMenu.setBackground(new java.awt.Color(233, 237, 243));
        mainMenu.setForeground(new java.awt.Color(0, 0, 0));
        mainMenu.setText("Menu");
        mainMenu.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N

        JItemMenuBuah.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        JItemMenuBuah.setForeground(new java.awt.Color(0, 0, 0));
        JItemMenuBuah.setText("Menu Buah");
        JItemMenuBuah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JItemMenuBuah.addActionListener(this::JItemMenuBuahActionPerformed);
        mainMenu.add(JItemMenuBuah);

        JItemMenuKriteria.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        JItemMenuKriteria.setForeground(new java.awt.Color(0, 0, 0));
        JItemMenuKriteria.setText("Menu Kriteria");
        JItemMenuKriteria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JItemMenuKriteria.addActionListener(this::JItemMenuKriteriaActionPerformed);
        mainMenu.add(JItemMenuKriteria);

        JItemCetakRanking.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        JItemCetakRanking.setForeground(new java.awt.Color(0, 0, 0));
        JItemCetakRanking.setText("Menu Hasil Ranking");
        JItemCetakRanking.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JItemCetakRanking.addActionListener(this::JItemCetakRankingActionPerformed);
        mainMenu.add(JItemCetakRanking);

        jmenuBadStock.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jmenuBadStock.setForeground(new java.awt.Color(5, 5, 5));
        jmenuBadStock.setText("Menu BadStock");
        jmenuBadStock.addActionListener(this::jmenuBadStockActionPerformed);
        mainMenu.add(jmenuBadStock);

        MenuInduk.add(mainMenu);

        MenuAkun.setBackground(new java.awt.Color(233, 237, 243));
        MenuAkun.setForeground(new java.awt.Color(10, 10, 10));
        MenuAkun.setText("Akun");
        MenuAkun.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N

        jItemMenuUtama.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jItemMenuUtama.setForeground(new java.awt.Color(0, 0, 0));
        jItemMenuUtama.setText("Menu Utama");
        jItemMenuUtama.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jItemMenuUtama.addActionListener(this::jItemMenuUtamaActionPerformed);
        MenuAkun.add(jItemMenuUtama);

        JItemLogout.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        JItemLogout.setForeground(new java.awt.Color(0, 0, 0));
        JItemLogout.setText("Logout / Keluar");
        JItemLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        JItemLogout.addActionListener(this::JItemLogoutActionPerformed);
        MenuAkun.add(JItemLogout);

        MenuInduk.add(MenuAkun);

        setJMenuBar(MenuInduk);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtC5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtC5ActionPerformed

    }//GEN-LAST:event_txtC5ActionPerformed

    private void JItemMenuBuahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JItemMenuBuahActionPerformed
        new FormBuah().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_JItemMenuBuahActionPerformed

    private void JItemMenuKriteriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JItemMenuKriteriaActionPerformed
    new FormKriteria().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_JItemMenuKriteriaActionPerformed

    private void JItemCetakRankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JItemCetakRankingActionPerformed
        new CetakanHasilRanking().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_JItemCetakRankingActionPerformed

    private void jItemMenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jItemMenuUtamaActionPerformed
        new Form_MenuUtama().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jItemMenuUtamaActionPerformed

    private void JItemLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JItemLogoutActionPerformed
    int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
    "Apakah Anda yakin ingin Logout?",
    "Konfirmasi Logout", javax.swing.JOptionPane.YES_NO_OPTION);
   
    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
//Matikan Program    
    System.exit(0);
//    new FormLogin().setVisible(true);
//    this.dispose();
}
    }//GEN-LAST:event_JItemLogoutActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        //Panggil Method Clear() disni
        clearForm();
        txtCari.setText("");
        tblPenilaian.clearSelection();
        loadTableData();
    }//GEN-LAST:event_btnResetActionPerformed
    
    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
           try {
        if (txtIdPenilaian.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Klik salah satu baris di tabel dulu, lalu ubah nilainya.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbBuah.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih buah terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Memastikan nilai penilaian sesuai skala 1 sampai 5.
if (!validasiSkala()) {
    return;
}
        String id = txtIdPenilaian.getText().trim();
        String kodeBuah = ambilKodeBuah();
        Connection conn = Koneksi.getKoneksi();
 
        // REMINDER!!! JANGAN SAMPAI BUAH DIGANTI KE BUAH YANG SUDAH  PUNYA PENILAIAN LAIN.!!!!
        if (sudahDinilai(conn, kodeBuah, id)) {
            JOptionPane.showMessageDialog(this,
                    "Buah " + kodeBuah + " sudah punya penilaian lain.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        String sql = "UPDATE tb_penilaian_buah SET kode_buah=?, c1=?, c2=?, c3=?, c4=?,"
                + " c5=?, c6=?, c7=?, c8=? WHERE id_penilaian=?";
 
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, kodeBuah);
            pst.setDouble(2, angka(txtC1));
            pst.setDouble(3, angka(txtC2));
            pst.setDouble(4, angka(txtC3));
            pst.setDouble(5, angka(txtC4));
            pst.setDouble(6, angka(txtC5));
            pst.setDouble(7, angka(txtC6));
            pst.setDouble(8, angka(txtC7));
            pst.setDouble(9, angka(txtC8));
            pst.setString(10, id);   // WHERE id_penilaian = ?
            pst.executeUpdate();
        }
 
        // Nilai C berubah, jadi Yi semua alternatif ikut berubah
        int jumlah = hitungUlangSemua();
 
        // Periksa kembali status seluruh alternatif
        // setelah nilai penilaian diubah.
        FormBadstock.prosesBadstockTidakLayak(this, false);
        
        JOptionPane.showMessageDialog(this,
                "Data berhasil diperbarui.\nNilai Yi " + jumlah + " alternatif sudah dihitung ulang.",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        loadTableData();
        clearForm();
 
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Pastikan kolom C1 s.d C8 diisi dengan angka valid!",
                "Error Format Angka", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal mengubah data: " + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
}
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
   if (txtIdPenilaian.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Klik salah satu baris di tabel dulu, lalu tekan HAPUS.",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
 
    int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus data dengan ID: " + txtIdPenilaian.getText() + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
    if (konfirmasi != JOptionPane.YES_OPTION) {
        return;
    }
 
    try {
        Connection conn = Koneksi.getKoneksi();
        try (PreparedStatement pst = conn.prepareStatement(
                "DELETE FROM tb_penilaian_buah WHERE id_penilaian=?")) {
            pst.setString(1, txtIdPenilaian.getText().trim());
            pst.executeUpdate();
        }
 
        hitungUlangSemua();   // sisa alternatif dihitung ulang
        FormBadstock.prosesBadstockTidakLayak(this, false);
        JOptionPane.showMessageDialog(this, "Data berhasil dihapus!",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        loadTableData();
        clearForm();
 
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnHapusActionPerformed
      
    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
         try {
        if (txtNilaiYi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Silakan klik tombol 'HITUNG MOORA' terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbBuah.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih buah terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Memastikan nilai penilaian sesuai skala 1 sampai 5.
        if (!validasiSkala()) {
        return;
}
        String id = txtIdPenilaian.getText().trim();
        String kodeBuah = ambilKodeBuah();
        Connection conn = Koneksi.getKoneksi();
 
        // Satu buah hanya boleh dinilai satu kali (satu buah = satu alternatif)
        if (sudahDinilai(conn, kodeBuah, id)) {
            JOptionPane.showMessageDialog(this,
                    "Buah " + kodeBuah + " sudah punya penilaian.\n"
                    + "Satu buah hanya boleh dinilai satu kali. Gunakan EDIT untuk mengubahnya.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        String sql = "INSERT INTO tb_penilaian_buah (id_penilaian, kode_buah, c1, c2, c3, c4,"
                + " c5, c6, c7, c8, nilai_yi, status_layak)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
 
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, id);
            pst.setString(2, kodeBuah);
            pst.setDouble(3, angka(txtC1));
            pst.setDouble(4, angka(txtC2));
            pst.setDouble(5, angka(txtC3));
            pst.setDouble(6, angka(txtC4));
            pst.setDouble(7, angka(txtC5));
            pst.setDouble(8, angka(txtC6));
            pst.setDouble(9, angka(txtC7));
            pst.setDouble(10, angka(txtC8));
            pst.setDouble(11, angka(txtNilaiYi)); // sementara, akan dihitung ulang di bawah
            pst.setString(12, txtStatusLayak.getText());
            pst.executeUpdate();
        }
 
        // PENTING: karena alternatif bertambah, Yi semua buah harus dihitung ulang
        int jumlah = hitungUlangSemua();
 
        // Proses buah Tidak Layak Jual ke BadStock
        // jika seluruh 30 alternatif sudah dinilai.
        FormBadstock.prosesBadstockTidakLayak(this, false);
        
        JOptionPane.showMessageDialog(this,
                "Data berhasil disimpan.\nNilai Yi " + jumlah + " alternatif sudah dihitung ulang.",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        loadTableData();
        clearForm();
 
    } catch (java.sql.SQLIntegrityConstraintViolationException e) {
        // Kunci utama kembar: ID Penilaian sudah dipakai
        JOptionPane.showMessageDialog(this,
                "ID Penilaian '" + txtIdPenilaian.getText().trim() + "' sudah dipakai. Gunakan ID lain.",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Pastikan kolom C1 s.d C8 diisi dengan angka valid!",
                "Error Format Angka", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();   // detail lengkap tetap muncul di console NetBeans
        JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungActionPerformed
        try {
        // Validasi: semua isian wajib terisi
        if (txtIdPenilaian.getText().trim().isEmpty() || cbBuah.getSelectedIndex() == 0
                || txtC1.getText().trim().isEmpty() || txtC2.getText().trim().isEmpty()
                || txtC3.getText().trim().isEmpty() || txtC4.getText().trim().isEmpty()
                || txtC5.getText().trim().isEmpty() || txtC6.getText().trim().isEmpty()
                || txtC7.getText().trim().isEmpty() || txtC8.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Semua data transaksi dan kriteria (C1-C8) wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
         // Memastikan seluruh nilai C1 sampai C8 valid.
        if (!validasiSkala()) {
            return;
        }
        // Data yang sementara diketik dijadikan satu atau sebagai"alternatif baru"
        DataNilai baru = new DataNilai();
        baru.id = txtIdPenilaian.getText().trim();
        baru.c[0] = angka(txtC1);
        baru.c[1] = angka(txtC2);
        baru.c[2] = angka(txtC3);
        baru.c[3] = angka(txtC4);
        baru.c[4] = angka(txtC5);
        baru.c[5] = angka(txtC6);
        baru.c[6] = angka(txtC7);
        baru.c[7] = angka(txtC8);
 
        Connection conn = Koneksi.getKoneksi();
        muatKriteria(conn);
 
        // Data lain di database (tanpa baris yang id-nya sama) + data baru ini
        List<DataNilai> data = bacaSemuaPenilaian(conn, baru.id);
        data.add(baru);
 
        double[] yi = hitungYi(data);
        double ambang = hitungAmbang(yi);
        double yiBaru = yi[yi.length - 1];   // alternatif terakhir = data yang sementara diketik
 
        txtNilaiYi.setText(String.format("%.4f", yiBaru));
        txtStatusLayak.setText(tentukanStatus(yiBaru, ambang));
 
        JOptionPane.showMessageDialog(this,
                "Perhitungan MOORA berhasil!\nAmbang kelayakan: " + String.format("%.4f", ambang),
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
 
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "Pastikan kolom C1 s.d C8 diisi dengan angka valid!",
                "Error Format Angka", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Terjadi kesalahan perhitungan: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
}
    }//GEN-LAST:event_btnHitungActionPerformed

    private void jspLoadDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jspLoadDataKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jspLoadDataKeyPressed

    private void txtC5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC5KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
    txtC6.requestFocus();
}
    }//GEN-LAST:event_txtC5KeyPressed

    private void txtC6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC6KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
    txtC7.requestFocus();
}
    }//GEN-LAST:event_txtC6KeyPressed

    private void txtC7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC7KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
    txtC8.requestFocus();
}
    }//GEN-LAST:event_txtC7KeyPressed

    private void txtC8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC8KeyPressed
    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
     // Panggil langsung method aksi hitung MOORA di sini
    btnHitungActionPerformed(null);
}
    }//GEN-LAST:event_txtC8KeyPressed

    private void btnHitungUlangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungUlangActionPerformed
     try {

        int jumlah = hitungUlangSemua();

        // Periksa dan proses BadStock yang belum tercatat.
        FormBadstock.prosesBadstockTidakLayak(this, false);

        loadTableData();

        JOptionPane.showMessageDialog(
                this,
                "Nilai Yi dan status " + jumlah
                + " alternatif berhasil dihitung ulang.",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
        );

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Gagal menghitung ulang: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    }//GEN-LAST:event_btnHitungUlangActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - sx, y - sy);
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        sx = evt.getX();
        sy = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void lblKELUARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKELUARMouseClicked
dispose();
    }//GEN-LAST:event_lblKELUARMouseClicked

    private void jmenuBadStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenuBadStockActionPerformed
        new FormBadstock().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jmenuBadStockActionPerformed

    private void txtC2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC2KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            txtC3.requestFocus();
        }
    }//GEN-LAST:event_txtC2KeyPressed

    private void txtC1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC1KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            txtC2.requestFocus();
        }
    }//GEN-LAST:event_txtC1KeyPressed

    private void txtC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtC1ActionPerformed
        //        try {
            //        // Validation: Ensure all inputs are filled
            //        if (txtC1.getText().isEmpty() || txtC2.getText().isEmpty() || txtC3.getText().isEmpty() ||
                //            txtC4.getText().isEmpty() || txtC5.getText().isEmpty() || txtC6.getText().isEmpty() ||
                //            txtC7.getText().isEmpty() || txtC8.getText().isEmpty()) {
                //            JOptionPane.showMessageDialog(this, "Harap isi semua nilai kriteria C1 s.d C8!");
                //            return;
                //        }
            //        // Read Inputs
            //        double c1 = Double.parseDouble(txtC1.getText());
            //        double c2 = Double.parseDouble(txtC2.getText());
            //        double c3 = Double.parseDouble(txtC3.getText());
            //        double c4 =
    }//GEN-LAST:event_txtC1ActionPerformed

    private void txtC3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC3KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            txtC4.requestFocus();
        }
    }//GEN-LAST:event_txtC3KeyPressed

    private void txtC4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtC4KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            txtC5.requestFocus();
        }
    }//GEN-LAST:event_txtC4KeyPressed
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
        java.awt.EventQueue.invokeLater(() -> new FormPenilaian().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem JItemCetakRanking;
    private javax.swing.JMenuItem JItemLogout;
    private javax.swing.JMenuItem JItemMenuBuah;
    private javax.swing.JMenuItem JItemMenuKriteria;
    private javax.swing.JMenu MenuAkun;
    private javax.swing.JMenuBar MenuInduk;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnHitung;
    private javax.swing.JButton btnHitungUlang;
    private javax.swing.JButton btnReset1;
    private javax.swing.JButton btnSimpan1;
    private javax.swing.JButton btnUbah1;
    private javax.swing.JComboBox<String> cbBuah;
    private javax.swing.JMenuItem jItemMenuUtama;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jmenuBadStock;
    private javax.swing.JScrollPane jspLoadData;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKELUAR;
    private javax.swing.JLabel lbl_C1;
    private javax.swing.JLabel lbl_C2;
    private javax.swing.JLabel lbl_C3;
    private javax.swing.JLabel lbl_C4;
    private javax.swing.JLabel lbl_C5;
    private javax.swing.JLabel lbl_C6;
    private javax.swing.JLabel lbl_C7;
    private javax.swing.JLabel lbl_C8;
    private javax.swing.JLabel lbl_IdPenilaian;
    private javax.swing.JLabel lbl_LogoIdPenilaian;
    private javax.swing.JLabel lbl_LogoNaomi;
    private javax.swing.JLabel lbl_LogoYi;
    private javax.swing.JLabel lbl_LogocbBuah;
    private javax.swing.JLabel lbl_NilaiYi;
    private javax.swing.JLabel lbl_PilihBuah;
    private javax.swing.JLabel lbl_StatusLayak;
    private javax.swing.JMenu mainMenu;
    private javax.swing.JPanel pnlBTN;
    private javax.swing.JPanel pnlC1_C4;
    private javax.swing.JPanel pnlC5_C8;
    private javax.swing.JPanel pnlId_Penilaian;
    private javax.swing.JPanel pnlUtama;
    private javax.swing.JPanel pnl_IndukInputan;
    private javax.swing.JTable tblPenilaian;
    private javax.swing.JTextField txtC1;
    private javax.swing.JTextField txtC2;
    private javax.swing.JTextField txtC3;
    private javax.swing.JTextField txtC4;
    private javax.swing.JTextField txtC5;
    private javax.swing.JTextField txtC6;
    private javax.swing.JTextField txtC7;
    private javax.swing.JTextField txtC8;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtIdPenilaian;
    private javax.swing.JTextField txtNilaiYi;
    private javax.swing.JTextField txtStatusLayak;
    // End of variables declaration//GEN-END:variables
}
