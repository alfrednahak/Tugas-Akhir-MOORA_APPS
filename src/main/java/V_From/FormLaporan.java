package V_From;

import Connection.Koneksi;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.*;
import java.awt.print.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * COBA Pusat Laporan SPK MOORA (Toko Buah Naomi), 100% full-code tdk GUI Builder.
 *
 * <p>Class ini HANYA berisi  konten laporan. Sidebar dan header aplikasi
 * dari Form_MenuUtama, jadi tidak disentuh sama sekali. API publik yang
 * dipake Form_MenuUtama tidak berubah dn tidk dirubah: konstruktor {@code FormLaporan()},
 * {@code getContentPane()} dan {@code muatData()}.</p>
 *
 * <p>Peta isi file:
 * BLOK 1 Tema, BLOK 2 Model data, BLOK 3 Komponen UI kustom, BLOK 4 Layout Manager,
 * BLOK 5 Kerangka halaman, BLOK 6 Dashboard, BLOK 7 Laporan lain,
 * BLOK 8 Cetak dan gambar kustom.</p>
 */
public class FormLaporan extends JFrame {

    // =====================================================================
    //  BLOK 1: TEMA (warna dan font, diambil dari preview)
    // =====================================================================

    //    Ganti ke "Arial" bila ingin persis font preview (preview dibuat dengan Arial).
    private static final String FONT = "Segoe UI";
    private static final Color BG         = new Color(217, 221, 229); // #d9dde5
    private static final Color TEAL       = new Color(11, 138, 132);  // #0b8a84
    private static final Color TEAL_END   = new Color(10, 111, 143);  // #0a6f8f (ujung gradasi hero)
    private static final Color TEAL_DARK  = new Color(11, 107, 102);  // #0b6b66
    private static final Color TEAL_SOFT  = new Color(225, 243, 241); // #e1f3f1
    private static final Color TEXT       = new Color(31, 45, 61);    // #1f2d3d
    private static final Color MUTED      = new Color(74, 91, 107);   // #4a5b6b
    private static final Color BODY       = new Color(51, 65, 79);    // #33414f
    private static final Color BORDER     = new Color(227, 234, 238); // #e3eaee
    private static final Color WHITE      = Color.WHITE;
    private static final Color AMBER      = new Color(224, 161, 0);   // #e0a100
    private static final Color AMBER_DARK = new Color(162, 107, 0);   // #a26b00
    private static final Color BLUE       = new Color(47, 53, 214);   // #2f35d6
    private static final Color BLUE_SOFT  = new Color(238, 240, 255); // #eef0ff
    private static final Color GRAY_BTN   = new Color(95, 107, 120);  // #5f6b78
    private static final Color YELLOW     = new Color(255, 213, 74);  // #ffd54a
    private static final Color ERROR_TEXT = new Color(190, 45, 45);
    private static final int GAP = 12;          // jarak antar kartu dan antar seksi
    private static final int RADIUS_KARTU = 14;
    private static final String JENIS_RANKING   = "Hasil Ranking MOORA";
    private static final String JENIS_BUAH      = "Data Buah";
    private static final String JENIS_KRITERIA  = "Kriteria dan Bobot";
    private static final String JENIS_RINGKASAN = "Ringkasan Eksekutif";
    private static final String JENIS_PANDUAN   = "Panduan Menggunakan Aplikasi MOORA";

    private static final DecimalFormat DF4;
    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(new Locale("id", "ID"));
        sym.setDecimalSeparator(',');
        DF4 = new DecimalFormat("0.0000", sym);
    }

    // ====================================
    //  ||  BLOK 2: MODEL DATA (dipisah dari UI)    ||
    // ====================================
    private static final class Ranking {
        final String nama, status;
        final double yi;
        Ranking(String nama, double yi, String status) { this.nama = nama; this.yi = yi; this.status = status; }
    }
    //    Seluruh data yang dibaca dashboard. UI hanya membaca, tidak pernah query sendiri untuk dashboard.
    private static final class DataLaporan {
        boolean tersedia;
        final java.util.List<Ranking> ranking = new ArrayList<>();
        int totalBuah, totalKriteria, layak, tidakLayak;

        Ranking terbaik() { return ranking.isEmpty() ? null : ranking.get(0); }

        int persenLayak() {
            int total = layak + tidakLayak;
            return total == 0 ? 0 : Math.round(layak * 100f / total);
        }

        int persenTidakLayak() { return (layak + tidakLayak) == 0 ? 0 : 100 - persenLayak(); }
        
        void muat() {
            tersedia = false;
            ranking.clear();
            totalBuah = totalKriteria = layak = tidakLayak = 0;

            try (Connection c = Koneksi.getKoneksi()) {
                if (c == null || c.isClosed()) throw new SQLException("Koneksi database tidak tersedia.");
                tersedia = true;

                String sqlRanking = "SELECT b.nama_buah, p.kode_buah, p.nilai_yi, p.status_layak "
                    + "FROM tb_penilaian_buah p LEFT JOIN tb_buah_buahan b ON b.kode_buah=p.kode_buah "
                    + "WHERE p.nilai_yi IS NOT NULL ORDER BY p.nilai_yi DESC, p.kode_buah ASC";
                try (PreparedStatement ps = c.prepareStatement(sqlRanking); ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String nama = rs.getString("nama_buah");
                        if (nama == null || nama.trim().isEmpty()) nama = rs.getString("kode_buah");
                        ranking.add(new Ranking(nama, rs.getDouble("nilai_yi"), rs.getString("status_layak")));
                    }
                }

                String sqlCount = "SELECT COUNT(*) total, "
                    + "SUM(CASE WHEN status_layak='Layak Jual' THEN 1 ELSE 0 END) layak, "
                    + "SUM(CASE WHEN status_layak='Tidak Layak Jual' THEN 1 ELSE 0 END) tidak_layak "
                    + "FROM tb_penilaian_buah";
                try (PreparedStatement ps = c.prepareStatement(sqlCount); ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalBuah = rs.getInt("total");
                        layak = rs.getInt("layak");
                        tidakLayak = rs.getInt("tidak_layak");
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM tb_kriteria");
                     ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) totalKriteria = rs.getInt(1);
                }
            } catch (Exception ex) {
                tersedia = false;
                System.err.println("FormLaporan: gagal memuat data: " + ex.getMessage());
            }
        }
    }

    // ========================================================
    //  || BLOK 3: KOMPONEN UI KUSTOM (tidak bergantung Look and Feel) ||
    // ========================================================
    //    Panel membulat: warna rata atau gradasi 135 derajat, garis tepi dan bayangan lembut opsional.
    private static class RoundPanel extends JPanel {
        private final int radius;
        private final Color fill, fill2, line;
        private final boolean shadow;

        RoundPanel(int radius, Color fill, Color fill2, Color line, boolean shadow) {
            this.radius = radius; this.fill = fill; this.fill2 = fill2;
            this.line = line; this.shadow = shadow;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int bh = shadow ? h - 3 : h;          // 3 px terbawah untuk bayangan
            if (shadow) {
                for (int i = 3; i >= 1; i--) {
                    g.setColor(new Color(20, 40, 60, 9));
                    g.fillRoundRect(0, i, w, bh, radius * 2, radius * 2);
                }
            }
            if (fill2 != null) g.setPaint(new GradientPaint(0, 0, fill, w, h, fill2));
            else g.setColor(fill);
            g.fillRoundRect(0, 0, w, bh, radius * 2, radius * 2);
            if (line != null) {
                g.setColor(line);
                g.drawRoundRect(0, 0, w - 1, bh - 1, radius * 2, radius * 2);
            }
            g.dispose();
            super.paintComponent(gr);
        }
    }
    //    Tombol bentuk pil.
    private static class PillButton extends JButton {
        private final Color fill, line;
        PillButton(String text, Color fill, Color fg, Color line, int size, int padV, int padH) {
            super(text);
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            this.fill = fill; this.line = line;
            setFont(new Font(FONT, Font.BOLD, size));
            setForeground(fg);
            setContentAreaFilled(false); setOpaque(false);
            setFocusPainted(false); setBorderPainted(false);
            setBorder(new EmptyBorder(padV, padH, padV, padH));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g.setColor(getModel().isPressed() ? fill.darker() : fill);
            g.fillRoundRect(0, 0, w, h, h, h);
            if (line != null) {
                g.setColor(line);
                g.drawRoundRect(0, 0, w - 1, h - 1, h, h);
            }
            g.dispose();
            super.paintComponent(gr);
        }
    }
    //    Chip bentuk pil ("Dipilih", "Pilih", "Baru").
    private static class PillLabel extends JLabel {
        private final Color fill, line;
        PillLabel(String text, Color fg, Color fill, Color line) {
            super(text, SwingConstants.CENTER);
            this.fill = fill; this.line = line;
            setFont(new Font(FONT, Font.BOLD, 10));
            setForeground(fg);
            setOpaque(false);
            setBorder(new EmptyBorder(3, 10, 3, 10));
        }
        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g.setColor(fill);
            g.fillRoundRect(0, 0, w, h, h, h);
            g.setColor(line);
            g.drawRoundRect(0, 0, w - 1, h - 1, h, h);
            g.dispose();
            super.paintComponent(gr);
        }
    }
    //    Lingkaran berisi angka (nomor Top 5 dan nomor langkah panduan).
    private static class Bulat extends JLabel {
        private final Color bg;
        Bulat(String text, int size, int fontSize, Color fg, Color bg) {
            super(text, SwingConstants.CENTER);
            this.bg = bg;
            setFont(new Font(FONT, Font.BOLD, fontSize));
            setForeground(fg);
            setOpaque(false);
            Dimension d = new Dimension(size, size);
            setPreferredSize(d); setMinimumSize(d); setMaximumSize(d);
        }
        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int d = Math.min(getWidth(), getHeight());
            g.setColor(bg);
            g.fillOval((getWidth() - d) / 2, (getHeight() - d) / 2, d, d);
            g.dispose();
            super.paintComponent(gr);
        }
    }
    //    Label dengan jarak antar-huruf 0,8 px (judul kartu di preview).
    private static class SpasiLabel extends JLabel {
        private final int extra;
        SpasiLabel(String text, int size, int style, Color color) {
            super(text);
            Map<TextAttribute, Object> atribut = new HashMap<>();
            atribut.put(TextAttribute.TRACKING, 0.07f);
            setFont(new Font(FONT, style, size).deriveFont(atribut));
            setForeground(color);
            setAlignmentX(Component.LEFT_ALIGNMENT);
            extra = (int) Math.ceil(text.length() * size * 0.07) + 2;
        }
        @Override public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(d.width + extra, d.height);
        }
    }

    //    Bar progres membulat (Komposisi Kelayakan Buah).
    private static class BarPanel extends JPanel {
        private final double fraction;
        private final Color color;
        BarPanel(int value, int max, Color color) {
            this.color = color;
            this.fraction = max <= 0 ? 0 : Math.min(1.0, value / (double) max);
            setOpaque(false);
            setPreferredSize(new Dimension(100, 9));
            setMinimumSize(new Dimension(10, 9));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 9));
        }
        @Override protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g.setColor(BORDER);
            g.fillRoundRect(0, 0, w, h, h, h);
            if (fraction > 0) {
                g.setColor(color);
                g.fillRoundRect(0, 0, Math.max(h, (int) (w * fraction)), h, h, h);
            }
            g.dispose();
        }
    }

    // ======================================
    //  ||  BLOK 4: LAYOUT MANAGER DASHBOARD    ||
    // ======================================
    /**             
     * Grid 3 kolom x 2 baris dengan rasio persis preview:
     * kolom 2,3 : 1 : 1,35 dan baris 280 : 182, jarak antar sel 12 px.
     *
     * <p>Kenapa bukan GridBagLayout? krn GridBagLayout itu dia bagi  sisa ruang base on weight nya
     * dari ukuran preferred masing2 sel, jarak (insets) ikut terhitung di
     * dalam sel, jadi rasio kolom nya meleset beberapa piksel dan berubah ngikutin
     * isi card nya. Layout ini hitung lebar/tinggi langsung dari rasio, jadi hasilnya
     * deterministik di ukuran jendela apa pun.</p>
     */
    private static final class DashLayout implements LayoutManager {
        private static final double[] KOLOM = {2.3, 1.0, 1.35};
        private static final double TINGGI_ATAS = 280.0, TINGGI_TOTAL = 462.0;

        @Override public void addLayoutComponent(String name, Component comp) { }
        @Override public void removeLayoutComponent(Component comp) { }
        @Override public Dimension preferredLayoutSize(Container p) { return new Dimension(760, 474); }
        @Override public Dimension minimumLayoutSize(Container p) { return new Dimension(600, 400); }

        @Override public void layoutContainer(Container p) {
            if (p.getComponentCount() < 6) return;
            double total = KOLOM[0] + KOLOM[1] + KOLOM[2];
            double lebar = p.getWidth() - 2 * GAP;
            int[] kw = new int[3];
            kw[0] = (int) Math.round(lebar * KOLOM[0] / total);
            kw[1] = (int) Math.round(lebar * KOLOM[1] / total);
            kw[2] = (int) lebar - kw[0] - kw[1];
            int hAtas = (int) Math.round((p.getHeight() - GAP) * TINGGI_ATAS / TINGGI_TOTAL);
            int hBawah = p.getHeight() - GAP - hAtas;
            int x = 0;
            for (int k = 0; k < 3; k++) {
                p.getComponent(k).setBounds(x, 0, kw[k], hAtas);
                p.getComponent(k + 3).setBounds(x, hAtas + GAP, kw[k], hBawah);
                x += kw[k] + GAP;
            }
        }
    }
    
    // =============================
    //  ||  BLOK 5: KERANGKA HALAMAN    ||
    // =============================
    private final DataLaporan data = new DataLaporan();
    private final JPanel root = new JPanel(new BorderLayout(0, GAP));
    private final JPanel reportArea = new JPanel(new BorderLayout());
    private final JLabel lblJenis = new JLabel(JENIS_RANKING);
    private String jenisTerpilih = JENIS_RANKING;

    public FormLaporan() {
        setTitle("Pusat Laporan - MooraFruits");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(950, 620));
        setSize(1030, 640);
        setLocationRelativeTo(null);
        data.muat();          // data dimuat DULU supaya render pertama sudah berisi data
        susunKerangka();
    }

    //    Dipanggil Form_MenuUtama setiap halaman Laporan dibuka. Memuat data lalu menggambar ulang.
    public void muatData() {
        data.muat();
        if (reportArea.getParent() != null) tampilkanLaporanTerpilih();
    }
    private void susunKerangka() {
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(14, 16, 14, 16));
        root.add(buatToolbar(), BorderLayout.NORTH);
        reportArea.setOpaque(false);
        root.add(reportArea, BorderLayout.CENTER);
        root.add(buatFooter(), BorderLayout.SOUTH);
        setContentPane(root);
        tampilkanLaporanTerpilih();
    }
    private JPanel buatToolbar() {
        JPanel wrapper = new JPanel(new BorderLayout(GAP, 0));
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(10, 38));

        RoundPanel selector = new RoundPanel(17, WHITE, null, null, true);
        selector.setLayout(new BoxLayout(selector, BoxLayout.X_AXIS));
        selector.setBorder(new EmptyBorder(0, 14, 3, 5));
        selector.setPreferredSize(new Dimension(10, 37));

        lblJenis.setFont(new Font(FONT, Font.BOLD, 12));
        lblJenis.setForeground(TEXT);
        PillButton tampilkan = new PillButton("Tampilkan", TEAL, WHITE, null, 12, 6, 14);
        tampilkan.addActionListener(e -> tampilkanLaporanTerpilih());

        selector.add(label("Jenis Laporan:", 12, Font.PLAIN, TEXT));
        selector.add(Box.createHorizontalStrut(6));
        selector.add(lblJenis);
        selector.add(Box.createHorizontalStrut(8));
        selector.add(tampilkan);

        JPanel kanan = new JPanel(new GridBagLayout());   // supaya pil tidak ikut melar
        kanan.setOpaque(false);
        kanan.add(selector);

        wrapper.add(label("Halo Administrator, selamat datang di Pusat Laporan", 15, Font.BOLD, TEXT), BorderLayout.WEST);
        wrapper.add(kanan, BorderLayout.EAST);
        return wrapper;
    }
    private JPanel buatFooter() {
        JPanel footer = new JPanel(new GridLayout(1, 3, GAP, 0));
        footer.setOpaque(false);
        footer.setPreferredSize(new Dimension(10, 40));
        PillButton refresh = new PillButton("Refresh", GRAY_BTN, WHITE, null, 14, 0, 16);
        PillButton pdf = new PillButton("Simpan PDF", TEAL, WHITE, null, 14, 0, 16);
        PillButton cetak = new PillButton("Cetak", BLUE, WHITE, null, 14, 0, 16);
        refresh.addActionListener(e -> muatData());
        pdf.addActionListener(e -> cetak(true));
        cetak.addActionListener(e -> cetak(false));
        footer.add(refresh);
        footer.add(pdf);
        footer.add(cetak);
        return footer;
    }
    private void tampilkanLaporanTerpilih() {
        reportArea.removeAll();
        lblJenis.setText(jenisTerpilih);

        if (JENIS_PANDUAN.equals(jenisTerpilih)) reportArea.add(buatPanduan(), BorderLayout.CENTER);
        else if (JENIS_BUAH.equals(jenisTerpilih)) reportArea.add(buatTabelBuah(), BorderLayout.CENTER);
        else if (JENIS_KRITERIA.equals(jenisTerpilih)) reportArea.add(buatTabelKriteria(), BorderLayout.CENTER);
        else if (JENIS_RINGKASAN.equals(jenisTerpilih)) reportArea.add(buatRingkasan(), BorderLayout.CENTER);
        else reportArea.add(buatDashboardRanking(), BorderLayout.CENTER);

        reportArea.revalidate();
        reportArea.repaint();
        lblJenis.revalidate();
    }
    private void pilihJenis(String jenis) {
        jenisTerpilih = jenis;
        tampilkanLaporanTerpilih();
    }
    // ---- helper pembuat komponen ----
    private JLabel label(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font(FONT, style, size));
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
    private JLabel labelSpasi(String text, int size, int style, Color color) {
        return new SpasiLabel(text, size, style, color);
    }
    //    Kartu putih membulat tanpa judul (padding bawah 17 = 14 + 3 px bayangan).
    private RoundPanel kartuBase() {
        RoundPanel p = new RoundPanel(RADIUS_KARTU, WHITE, null, null, true);
        p.setLayout(new BorderLayout(0, 0));
        p.setBorder(new EmptyBorder(14, 16, 17, 16));
        return p;
    }
    //  Kartu putih dengan judul huruf kapital di NORTH. Isi ditaruh di CENTER.
    private RoundPanel kartu(String judul) {
        RoundPanel p = kartuBase();
        JPanel w = new JPanel(new BorderLayout());
        w.setOpaque(false);
        w.setBorder(new EmptyBorder(0, 0, 8, 0));
        w.add(labelSpasi(judul.toUpperCase(Locale.ROOT), 11, Font.BOLD, TEXT), BorderLayout.WEST);
        p.add(w, BorderLayout.NORTH);
        return p;
    }
    //  Menempelkan komponen ke bagian atas area (tidak melar ke bawah).
    private JPanel rataAtas(JComponent isi) {
        JPanel atas = new JPanel(new BorderLayout());
        atas.setOpaque(false);
        atas.add(isi, BorderLayout.NORTH);
        return atas;
    }
    // ==========================================
    //  || BLOK 6: DASHBOARD HASIL RANKING (6 kartu)   ||
    // ==========================================
    private JPanel buatDashboardRanking() {
        JPanel page = new JPanel(new DashLayout());
        page.setOpaque(false);
        page.add(buatHeroGrafik());   // kolom 1 baris 1
        page.add(buatTop5());         // kolom 2 baris 1
        page.add(buatJenisLaporan()); // kolom 3 baris 1
        page.add(buatKomposisi());    // kolom 1 baris 2
        page.add(buatDonut());        // kolom 2 baris 2
        page.add(buatKalender());     // kolom 3 baris 2
        return page;
    }

    private JPanel buatHeroGrafik() {
        Ranking terbaik = data.terbaik();
        boolean adaData = data.tersedia && terbaik != null;

        RoundPanel p = new RoundPanel(RADIUS_KARTU, TEAL, TEAL_END, null, true);
        p.setLayout(new BorderLayout(10, 0));
        p.setBorder(new EmptyBorder(14, 16, 17, 16));

        JPanel kiri = new JPanel();
        kiri.setOpaque(false);
        kiri.setLayout(new BoxLayout(kiri, BoxLayout.Y_AXIS));
        kiri.setPreferredSize(new Dimension(150, 10));
        kiri.add(labelSpasi("NILAI YI TERTINGGI", 11, Font.BOLD, WHITE));
        kiri.add(Box.createVerticalStrut(2));
        kiri.add(label("Peringkat 1", 11, Font.PLAIN, WHITE));
        kiri.add(Box.createVerticalStrut(34));
        kiri.add(label(adaData ? DF4.format(terbaik.yi) : "-", 36, Font.BOLD, WHITE));
        kiri.add(Box.createVerticalStrut(4));
        kiri.add(label(!data.tersedia ? "Database tidak tersedia" : (adaData ? terbaik.nama : "-"), 13, Font.PLAIN, WHITE));
        kiri.add(Box.createVerticalStrut(30));
        String status = adaData && terbaik.status != null ? terbaik.status : "-";
        kiri.add(label("Status: " + status, 10, Font.PLAIN, WHITE));

        JPanel kanan = new JPanel(new BorderLayout(0, 10));
        kanan.setOpaque(false);
        JLabel judulGrafik = labelSpasi("GRAFIK NILAI YI - 10 TERATAS", 11, Font.BOLD, WHITE);
        judulGrafik.setHorizontalAlignment(SwingConstants.RIGHT);
        kanan.add(judulGrafik, BorderLayout.NORTH);
        kanan.add(new LineChartPanel(new ArrayList<>(data.ranking)), BorderLayout.CENTER);

        p.add(kiri, BorderLayout.WEST);
        p.add(kanan, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatTop5() {
        RoundPanel p = kartu("Top 5 Ranking");
        JPanel rows = new JPanel();
        rows.setOpaque(false);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        if (!data.tersedia || data.ranking.isEmpty()) {
            rows.add(label("Belum ada hasil perhitungan MOORA.", 12, Font.PLAIN, MUTED));
        } else {
            for (int i = 0; i < Math.min(5, data.ranking.size()); i++) {
                Ranking r = data.ranking.get(i);
                JPanel baris = new JPanel(new BorderLayout(10, 0));
                baris.setOpaque(false);
                baris.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER), new EmptyBorder(6, 6, 6, 6)));
                baris.add(new Bulat(String.valueOf(i + 1), 18, 10, TEAL_DARK, TEAL_SOFT), BorderLayout.WEST);
                baris.add(label(r.nama, 12, Font.PLAIN, TEXT), BorderLayout.CENTER);
                baris.add(label(DF4.format(r.yi), 12, Font.BOLD, TEXT), BorderLayout.EAST);
                baris.setMaximumSize(new Dimension(Integer.MAX_VALUE, baris.getPreferredSize().height));
                rows.add(baris);
            }
        }
        p.add(rows, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatJenisLaporan() {
        RoundPanel p = kartu("Jenis Laporan");
        JPanel rows = new JPanel(new GridLayout(5, 1, 0, 5));
        rows.setOpaque(false);
        String[][] daftar = {
            {JENIS_RANKING,   "Peringkat " + data.ranking.size() + " buah"},
            {JENIS_BUAH,      "Daftar alternatif"},
            {JENIS_KRITERIA,  data.totalKriteria + " kriteria penilaian"},
            {JENIS_RINGKASAN, "Rekap kelayakan"},
            {JENIS_PANDUAN,   "Langkah dari login sampai logout"}
        };
        for (String[] jenis : daftar) rows.add(buatItemJenis(jenis[0], jenis[1]));
        p.add(rows, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatItemJenis(final String judul, String keterangan) {
        boolean dipilih = judul.equals(jenisTerpilih);
        boolean baru = !dipilih && JENIS_PANDUAN.equals(judul);

        RoundPanel item = new RoundPanel(10,
            dipilih ? TEAL_SOFT : (baru ? BLUE_SOFT : WHITE), null,
            dipilih ? TEAL : (baru ? BLUE : BORDER), false);
        item.setLayout(new BorderLayout(8, 0));
        item.setBorder(new EmptyBorder(6, 10, 6, 10));

        JPanel teks = new JPanel();
        teks.setOpaque(false);
        teks.setLayout(new BoxLayout(teks, BoxLayout.Y_AXIS));
        teks.add(Box.createVerticalGlue());
        teks.add(label(judul, 12, Font.BOLD, TEXT));
        teks.add(Box.createVerticalStrut(1));
        teks.add(label(keterangan, 10, Font.PLAIN, MUTED));
        teks.add(Box.createVerticalGlue());

        PillLabel chip;
        if (dipilih) chip = new PillLabel("Dipilih", WHITE, TEAL, TEAL);
        else if (baru) chip = new PillLabel("Baru", BLUE, WHITE, BLUE);
        else chip = new PillLabel("Pilih", TEAL_DARK, WHITE, TEAL);
        JPanel chipWrap = new JPanel(new GridBagLayout());   // chip tidak melar atas-bawah
        chipWrap.setOpaque(false);
        chipWrap.add(chip);

        item.add(teks, BorderLayout.CENTER);
        item.add(chipWrap, BorderLayout.EAST);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { pilihJenis(judul); }
        });
        return item;
    }
    private JPanel buatKomposisi() {
        RoundPanel p = kartu("Komposisi Kelayakan Buah");
        int total = data.layak + data.tidakLayak;

        JPanel stats = new JPanel(new GridLayout(1, 3, 5, 0));
        stats.setOpaque(false);
        stats.add(buatStat("Jumlah Alternatif", data.totalBuah, MUTED));
        stats.add(buatStat("Jumlah Kriteria", data.totalKriteria, TEAL_DARK));
        stats.add(buatStat("Layak Jual", data.layak, AMBER_DARK));

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(buatBarisTeks("Layak Jual", data.layak + " buah (" + data.persenLayak() + "%)"));
        body.add(Box.createVerticalStrut(4));
        body.add(new BarPanel(data.layak, total, TEAL));
        body.add(Box.createVerticalStrut(8));
        body.add(buatBarisTeks("Tidak Layak Jual", data.tidakLayak + " buah (" + data.persenTidakLayak() + "%)"));
        body.add(Box.createVerticalStrut(4));
        body.add(new BarPanel(data.tidakLayak, total, AMBER));
        body.add(Box.createVerticalStrut(10));
        body.add(stats);

        p.add(rataAtas(body), BorderLayout.CENTER);
        return p;
    }
    private JPanel buatBarisTeks(String kiri, String kanan) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.add(label(kiri, 11, Font.PLAIN, TEXT), BorderLayout.WEST);
        r.add(label(kanan, 11, Font.BOLD, TEXT), BorderLayout.EAST);
        return r;
    }
    private JPanel buatStat(String judul, int nilai, Color warna) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = label(judul, 11, Font.PLAIN, MUTED);
        JLabel v = label(String.valueOf(nilai), 28, Font.BOLD, warna);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        v.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(t);
        p.add(v);
        return p;
    }
    private JPanel buatDonut() {
        RoundPanel p = kartu("Persentase Layak Jual");
        JPanel legenda = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        legenda.setOpaque(false);
        legenda.add(buatItemLegenda("Layak", data.layak, TEAL_DARK));
        legenda.add(buatItemLegenda("Tidak Layak", data.tidakLayak, AMBER_DARK));

        JPanel body = new JPanel(new BorderLayout(0, 2));
        body.setOpaque(false);
        body.add(new DonutPanel(data.persenLayak()), BorderLayout.CENTER);
        body.add(legenda, BorderLayout.SOUTH);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    private JPanel buatItemLegenda(String teks, int nilai, Color warna) {
        JPanel i = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        i.setOpaque(false);
        i.add(label(teks, 11, Font.PLAIN, TEXT));
        i.add(label(String.valueOf(nilai), 11, Font.BOLD, warna));
        return i;
    }
    private JPanel buatKalender() {
        Locale id = new Locale("id", "ID");
        String bulan = new SimpleDateFormat("MMMM yyyy", id).format(new java.util.Date()).toUpperCase(id);

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.add(labelSpasi("TANGGAL CETAK", 11, Font.BOLD, TEXT), BorderLayout.WEST);
        head.add(labelSpasi(bulan, 11, Font.BOLD, TEXT), BorderLayout.EAST);

        RoundPanel p = kartuBase();
        p.setLayout(new BorderLayout(0, 8));
        p.add(head, BorderLayout.NORTH);
        p.add(new CalendarPanel(), BorderLayout.CENTER);
        return p;
    }

    // ===============================================
//     ||   BLOK 7: LAPORAN LAIN (tabel, ringkasan, panduan)    ||
    // ===============================================
//    private JPanel buatTabelBuah() {
//        DefaultTableModel model = modelTidakBisaDiedit(new String[]{"No", "Kode Buah", "Nama Buah"});
//        String sql = "SELECT kode_buah, nama_buah FROM tb_buah_buahan ORDER BY kode_buah";
//        try (Connection c = Koneksi.getKoneksi(); PreparedStatement ps = c.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//            int no = 0;
//            while (rs.next()) model.addRow(new Object[]{++no, rs.getString(1), rs.getString(2)});
//        } catch (Exception ex) { return panelError("Gagal memuat data buah: " + ex.getMessage()); }
//        return panelTabel("LAPORAN DATA BUAH", "Daftar seluruh alternatif buah yang terdaftar.", model);
//    }
    
private JPanel buatTabelBuah() {
    DefaultTableModel model = modelTidakBisaDiedit(
        new String[]{"No", "Kode Buah", "Nama Buah"}
    );

    String sql = "SELECT kode_buah, nama_buah FROM tb_buah_buahan ORDER BY kode_buah";

    try (Connection c = Koneksi.getKoneksi();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        int no = 0;

        while (rs.next()) {
            model.addRow(new Object[]{
                ++no,
                rs.getString(1),
                rs.getString(2)
            });
        }

    } catch (Exception ex) {
        return panelError("Gagal memuat data buah: " + ex.getMessage());
    }

    // Header
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);

    JLabel judul = label("LAPORAN DATA BUAH", 18, Font.BOLD, TEXT);

    PillButton kembali = new PillButton(
        "Kembali", WHITE, TEAL_DARK, TEAL, 12, 6, 14
    );

    kembali.addActionListener(e -> {
        pilihJenis(JENIS_RANKING);
    });

    header.add(judul, BorderLayout.WEST);
    header.add(kembali, BorderLayout.EAST);

    // Panel laporan
    JPanel hasil = panelTabel(
        "LAPORAN DATA BUAH",
        "Daftar seluruh alternatif buah yang terdaftar.",
        model
    );

    hasil.add(header, BorderLayout.NORTH);

    return hasil;
}
private JPanel buatTabelKriteria() {
    DefaultTableModel model = modelTidakBisaDiedit(
        new String[]{"Kode Kriteria", "Bobot", "Jenis"}
    );
    String sql = "SELECT kode_kriteria, bobot, type FROM tb_kriteria ORDER BY kode_kriteria";
    try (Connection c = Koneksi.getKoneksi();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString(1),
                DF4.format(rs.getDouble(2)),
                rs.getString(3)
            });
        }
    } catch (Exception ex) {
        return panelError("Gagal memuat kriteria: " + ex.getMessage());
    }
    // Header
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);
    JLabel judul = label("LAPORAN KRITERIA DAN BOBOT", 18, Font.BOLD, TEXT);
    PillButton kembali = new PillButton(
        "Kembali", WHITE, TEAL_DARK, TEAL, 12, 6, 14
    );
    kembali.addActionListener(e -> {
        pilihJenis(JENIS_RANKING);
    });
    header.add(judul, BorderLayout.WEST);
    header.add(kembali, BorderLayout.EAST);
    // Panel laporan
    JPanel hasil = panelTabel(
        "LAPORAN KRITERIA DAN BOBOT",
        "Bobot dan jenis kriteria dibaca langsung dari tabel tb_kriteria.",
        model
    );
    hasil.add(header, BorderLayout.NORTH);
    return hasil;
}
    private JPanel buatRingkasan() {
    Ranking terbaik = data.terbaik();
    String namaTerbaik = terbaik == null ? "-" : terbaik.nama;
    String yiTerbaik = DF4.format(terbaik == null ? 0 : terbaik.yi);

    RoundPanel p = new RoundPanel(RADIUS_KARTU, WHITE, null, null, true);
    p.setLayout(new BorderLayout(0, 14));
    p.setBorder(new EmptyBorder(22, 24, 25, 24));

    // Header
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);

    JLabel judul = label("RINGKASAN EKSEKUTIF", 18, Font.BOLD, TEXT);

    PillButton kembali = new PillButton(
        "Kembali", WHITE, TEAL_DARK, TEAL, 12, 6, 14
    );

    // Navigasi kembali ke halaman Ranking
    kembali.addActionListener(e -> {
        pilihJenis(JENIS_RANKING);
    });

    header.add(judul, BorderLayout.WEST);
    header.add(kembali, BorderLayout.EAST);

    // Isi ringkasan
    JTextArea isi = new JTextArea();
    isi.setEditable(false);
    isi.setLineWrap(true);
    isi.setWrapStyleWord(true);
    isi.setFont(new Font(FONT, Font.PLAIN, 13));
    isi.setForeground(TEXT);
    isi.setBackground(WHITE);

    isi.setText(
        "Ringkasan hasil Sistem Pendukung Keputusan Pemilihan Buah Layak Jual menggunakan metode MOORA.\n\n"
        + "Jumlah alternatif yang telah dinilai: " + data.totalBuah + ".\n"
        + "Buah berstatus Layak Jual: " + data.layak + ".\n"
        + "Buah berstatus Tidak Layak Jual: " + data.tidakLayak + ".\n"
        + "Persentase Layak Jual: " + data.persenLayak() + "%.\n"
        + "Jumlah kriteria: " + data.totalKriteria + ".\n"
        + "Alternatif dengan nilai Yi tertinggi: " + namaTerbaik + " (" + yiTerbaik + ").\n\n"
        + "Nilai Yi yang lebih tinggi menunjukkan alternatif dengan preferensi lebih baik berdasarkan bobot dan jenis kriteria yang tersimpan pada database.\n\n"
        + "Catatan: status kelayakan mengikuti ambang yang diterapkan pada aplikasi. Laporan ini tidak menggantikan pemeriksaan kondisi fisik buah secara langsung."
    );

    JScrollPane scroll = new JScrollPane(isi);
    scroll.setBorder(null);
    scroll.getViewport().setBackground(WHITE);

    // Susunan komponen
    p.add(header, BorderLayout.NORTH);
    p.add(scroll, BorderLayout.CENTER);

    return p;
}
    private JPanel buatPanduan() {
        JPanel page = new JPanel(new BorderLayout(0, GAP));
        page.setOpaque(false);

        JPanel teks = new JPanel();
        teks.setOpaque(false);
        teks.setLayout(new BoxLayout(teks, BoxLayout.Y_AXIS));
        teks.add(label("Panduan Menggunakan Aplikasi MOORA", 16, Font.BOLD, TEXT));
        teks.add(Box.createVerticalStrut(3));
        teks.add(label("Ikuti urutan 1 sampai 9 supaya paham, supaya hasil perhitungan dan laporan akurat.", 11, Font.PLAIN, BODY));

        PillButton kembali = new PillButton("Kembali", WHITE, TEAL_DARK, TEAL, 12, 6, 14);
        kembali.addActionListener(e -> pilihJenis(JENIS_RANKING));
        JPanel kananHead = new JPanel(new GridBagLayout());
        kananHead.setOpaque(false);
        kananHead.add(kembali);

        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);
        head.add(teks, BorderLayout.WEST);
        head.add(kananHead, BorderLayout.EAST);

        String[][] langkah = {
            {"Login ke Aplikasi", "Buka aplikasi lalu masukkan username dan password Administrator, kemudian klik Login. Jika gagal, periksa huruf besar-kecil dan pastikan MySQL di XAMPP sudah menyala."},
            {"Kenali Dashboard", "Setelah masuk, Dashboard menampilkan ringkasan data. Gunakan menu di sisi kiri untuk berpindah halaman. Menu yang sedang dibuka ditandai warna hijau toska."},
            {"Isi Data Buah", "Buka Data Buah lalu tambahkan semua alternatif lengkap dengan kode dan nama buah. Pastikan tidak ada kode ganda, contohnya B001 untuk Apel."},
            {"Atur Kriteria dan Bobot", "Isi daftar kriteria penilaian (saat ini " + data.totalKriteria + "), tentukan jenisnya Benefit atau Cost, lalu isi bobot tiap kriteria. Total bobot harus tepat 100%."},
            {"Lakukan Penilaian", "Beri nilai setiap buah untuk setiap kriteria. Seluruh kolom harus terisi, karena nilai yang kosong membuat hasil perhitungan tidak lengkap."},
            {"Catat Transaksi dan BadStock", "Catat penjualan di menu Transaksi dan buah rusak di menu BadStock agar kondisi stok toko selalu terdata dengan benar."},
            {"Lihat Hasil MOORA", "Buka Hasil MOORA untuk melihat normalisasi, optimasi, nilai Yi, dan peringkat. Buah dengan nilai Yi tertinggi menjadi rekomendasi terbaik."},
            {"Buat Laporan", "Masuk ke menu Laporan, pilih Jenis Laporan, atur filter, lalu klik Tampilkan. Pakai Simpan PDF untuk arsip atau Cetak untuk dokumen siap tanda tangan."},
            {"Logout", "Setelah selesai, klik Logout di bagian bawah menu kiri supaya akun tetap aman dan tidak dipakai orang lain."}
        };
        JPanel grid = new JPanel(new GridLayout(3, 3, GAP, GAP));
        grid.setOpaque(false);
        for (int i = 0; i < langkah.length; i++) grid.add(buatKartuLangkah(i + 1, langkah[i][0], langkah[i][1]));

        page.add(head, BorderLayout.NORTH);
        page.add(grid, BorderLayout.CENTER);
        return page;
    }

    private JPanel buatKartuLangkah(int nomor, String judul, String isi) {
        RoundPanel card = kartuBase();
        card.setLayout(new BorderLayout(10, 0));

        JPanel nomorWrap = new JPanel(new BorderLayout());   // lingkaran tetap 26 px, menempel di atas
        nomorWrap.setOpaque(false);
        nomorWrap.add(new Bulat(String.valueOf(nomor), 26, 12, WHITE, TEAL), BorderLayout.NORTH);

        JTextArea deskripsi = new JTextArea(isi);
        deskripsi.setEditable(false); deskripsi.setLineWrap(true); deskripsi.setWrapStyleWord(true);
        deskripsi.setFont(new Font(FONT, Font.PLAIN, 11)); deskripsi.setForeground(BODY);
        deskripsi.setOpaque(false); deskripsi.setBorder(null); deskripsi.setFocusable(false);

        JPanel detail = new JPanel(new BorderLayout(0, 5));
        detail.setOpaque(false);
        detail.add(label(judul, 13, Font.BOLD, TEXT), BorderLayout.NORTH);
        detail.add(deskripsi, BorderLayout.CENTER);

        card.add(nomorWrap, BorderLayout.WEST);
        card.add(detail, BorderLayout.CENTER);
        return card;
    }

    private DefaultTableModel modelTidakBisaDiedit(String[] kolom) {
        return new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private JPanel panelTabel(String judul, String subjudul, DefaultTableModel model) {
        RoundPanel p = new RoundPanel(RADIUS_KARTU, WHITE, null, null, true);
        p.setLayout(new BorderLayout(0, 12));
        p.setBorder(new EmptyBorder(18, 20, 21, 20));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(label(judul, 17, Font.BOLD, TEXT));
        header.add(label(subjudul, 11, Font.PLAIN, MUTED));

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font(FONT, Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font(FONT, Font.BOLD, 11));
        table.getTableHeader().setBackground(new Color(232, 241, 242));

        p.add(header, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private JPanel panelError(String pesan) {
        RoundPanel p = new RoundPanel(RADIUS_KARTU, WHITE, null, null, true);
        p.setLayout(new GridBagLayout());
        p.add(label(pesan, 12, Font.PLAIN, ERROR_TEXT));
        return p;
    }

    // ====================================
//     ||   BLOK 8: CETAK DAN GAMBAR KUSTOM   ||
    // ====================================

    private void cetak(boolean pdf) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName(pdf ? "Laporan MOORA - Simpan PDF" : "Laporan MOORA");
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) graphics.create();
            double sx = (pageFormat.getImageableWidth() - 20) / root.getWidth();
            double sy = (pageFormat.getImageableHeight() - 20) / root.getHeight();
            double scale = Math.min(sx, sy);
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2.scale(scale, scale);
            root.printAll(g2);
            g2.dispose();
            return Printable.PAGE_EXISTS;
        });
        if (job.printDialog()) {
            try { job.print(); }
            catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Gagal mencetak laporan:\n" + ex.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //     Grafik area + garis Yi 10 besar. Koordinat mengikuti SVG preview (viewBox 380 x 150).
    private static final class LineChartPanel extends JPanel {
        private final java.util.List<Ranking> data;

        LineChartPanel(java.util.List<Ranking> data) { this.data = data; setOpaque(false); }

        @Override protected void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            int n = Math.min(10, data.size());
            if (n == 0) {
                g.setColor(WHITE);
                g.setFont(new Font(FONT, Font.PLAIN, 11));
                g.drawString("Belum ada data grafik", 15, getHeight() / 2);
                g.dispose();
                return;
            }
            double s = Math.min(getWidth() / 380.0, getHeight() / 150.0);
            double ox = (getWidth() - 380 * s) / 2, oy = (getHeight() - 150 * s) / 2;
            double max = data.get(0).yi, min = data.get(n - 1).yi;
            double[] xs = new double[n], ys = new double[n];
            for (int i = 0; i < n; i++) {
                xs[i] = ox + (10 + (n == 1 ? 0 : i * 360.0 / (n - 1))) * s;
                double rel = (max - min) < 0.000001 ? 0.5 : (max - data.get(i).yi) / (max - min);
                ys[i] = oy + (23.4 + rel * (113.0 - 23.4)) * s;
            }
            double base = oy + 130 * s;

            Path2D.Double area = new Path2D.Double();
            area.moveTo(xs[0], base);
            for (int i = 0; i < n; i++) area.lineTo(xs[i], ys[i]);
            area.lineTo(xs[n - 1], base);
            area.closePath();
            g.setColor(new Color(255, 255, 255, 51));      // putih 20%
            g.fill(area);

            g.setColor(WHITE);
            g.setStroke(new BasicStroke((float) Math.max(1.5, 2 * s), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 1; i < n; i++) g.draw(new Line2D.Double(xs[i - 1], ys[i - 1], xs[i], ys[i]));

            double r = Math.max(3, 3.5 * s);
            g.setFont(new Font(FONT, Font.BOLD, 8));
            FontMetrics fm = g.getFontMetrics();
            for (int i = 0; i < n; i++) {
                g.setColor(YELLOW);
                g.fill(new Ellipse2D.Double(xs[i] - r, ys[i] - r, 2 * r, 2 * r));
                g.setColor(WHITE);
                String tick = "R" + (i + 1);
                g.drawString(tick, (int) (xs[i] - fm.stringWidth(tick) / 2.0), (int) (oy + 146 * s));
            }
            g.dispose();
        }
    }

    //    Donat persentase: ring 8 px, diameter garis tengah 67 px (setara SVG 84 px di preview).
    private static final class DonutPanel extends JPanel {
        private final int percent;

        DonutPanel(int percent) { this.percent = percent; setOpaque(false); }

        @Override protected void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            double d = Math.min(67, Math.min(getWidth() - 16, getHeight() - 10));
            double x = (getWidth() - d) / 2, y = (getHeight() - d) / 2;
            g.setStroke(new BasicStroke(8.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(BORDER);
            g.draw(new Ellipse2D.Double(x, y, d, d));
            if (percent > 0) {
                g.setColor(TEAL);
                g.draw(new Arc2D.Double(x, y, d, d, 90, -percent * 3.6, Arc2D.OPEN));
            }
            String s = percent + "%";
            g.setFont(new Font(FONT, Font.BOLD, 17));
            g.setColor(TEXT);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(s, (int) (getWidth() / 2.0 - fm.stringWidth(s) / 2.0),
                (int) (y + d / 2 + (fm.getAscent() - fm.getDescent()) / 2.0));
            g.dispose();
        }
    }

    //    Kalender bulan berjalan; tanggal hari ini ditandai kotak membulat teal.
    private static final class CalendarPanel extends JPanel {
        CalendarPanel() { setOpaque(false); }

        @Override protected void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            Graphics2D g = (Graphics2D) gr.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Calendar c = Calendar.getInstance(new Locale("id", "ID"));
            int today = c.get(Calendar.DAY_OF_MONTH);
            String[] hari = {"Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};
            double cellW = getWidth() / 7.0;

            g.setFont(new Font(FONT, Font.BOLD, 9));
            g.setColor(MUTED);
            FontMetrics fm = g.getFontMetrics();
            for (int i = 0; i < 7; i++)
                g.drawString(hari[i], (int) ((i + 0.5) * cellW - fm.stringWidth(hari[i]) / 2.0), fm.getAscent());

            c.set(Calendar.DAY_OF_MONTH, 1);
            int first = c.get(Calendar.DAY_OF_WEEK) - 1;
            int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            int top = 14, rowH = 16, pitch = 18;
            for (int day = 1; day <= daysInMonth; day++) {
                int index = first + day - 1, col = index % 7, row = index / 7;
                int y0 = top + row * pitch;
                boolean hariIni = day == today;
                g.setFont(new Font(FONT, hariIni ? Font.BOLD : Font.PLAIN, 10));
                fm = g.getFontMetrics();
                if (hariIni) {
                    g.setColor(TEAL);
                    g.fillRoundRect((int) (col * cellW) + 1, y0, (int) cellW - 2, rowH, 12, 12);
                    g.setColor(WHITE);
                } else {
                    g.setColor(TEXT);
                }
                String s = String.valueOf(day);
                g.drawString(s, (int) ((col + 0.5) * cellW - fm.stringWidth(s) / 2.0),
                    y0 + (rowH + fm.getAscent() - fm.getDescent()) / 2);
            }
            g.dispose();
        }
    }

//    Jalankan form sendiri untuk uji tampilan (di aplikasi, form dipake lewat Form_MenuUtama).

@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormLaporan().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
