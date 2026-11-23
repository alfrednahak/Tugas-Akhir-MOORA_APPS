package V_From;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*; //Fungsi nya untuk buat efek hover kalo mouse ada di atas btn.
import Connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// IMPORT JFREECHART
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import java.text.DecimalFormat;
import org.jfree.chart.axis.NumberAxis;

public class Form_MenuUtama extends javax.swing.JFrame {
    /**                     FUNGSI????
     *  VAR HEADER & STATISTIK
     * Supaya nanti judul Header bisa berubah otomatis pas admin pindah2 halaman.
        Contohnya:
        - Buka Dashboard → judul Dashboard.
        - Buka Data Buah → judul Data Buah.
        - Buka Penilaian → judul Penilaian.
          Jadi kita tidak perlu bikin Header baru setiap kali indah2 halaman.
     */
private JLabel headerTitleLabel;
private JLabel headerBreadcrumbLabel;
// =================================
// VARIABEL STATISTIK DASHBOARD
// =================================
//fungsi nya untuk ubah angka dan nama buah secara otomatis dari database.
private JLabel totalBuahValueLabel;
private JLabel totalKriteriaValueLabel;
private JLabel bestFruitValueLabel;
private JLabel bestFruitDetailLabel;

// =================================
// VARIABEL NAVIGASI CARDLAYOUT
// =================================
// Atur perpindahan halaman
private CardLayout cardLayout;
// Tempat tampung seluruh halaman
private JPanel pageContainer;
// Simpan tombol Sidebar yang sedang aktif
private JButton activeMenuButton;
// Halaman Pusat Laporan (khusus menu Laporan)
private FormLaporan formLaporan;
// Simpan JFrame asli biar komponen form yang dipindahkan tetap connect
private final java.util.List<JFrame> embeddedFormOwners = new java.util.ArrayList<>();

private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Form_MenuUtama.class.getName());
public Form_MenuUtama() {
        initComponents();    
        buildMainLayout();   
    // Memuat statistik dari database
        loadDashboardStatistics();
}

//                          METHOD BUILDMAINLAYOUT
//      Buat enam halaman dan kasih nama identitas masing-masing.
private void buildMainLayout() {

    // =================================
    // 1. PENGATURAN JENDELA UTAMA
    // =================================
    setTitle("Toko Buah Segar Naomi - SPK MOORA");
    setSize(1280, 720);
    setMinimumSize(
        new Dimension(1100, 650)
    );
    setLocationRelativeTo(null);
    // =================================
    // 2. MEMBERSIHKAN CONTENT PANE
    // =================================
    getContentPane().removeAll();
    getContentPane().setLayout(
        new BorderLayout()
    );
    // =================================
    // 3. MEMBUAT SIDEBAR
    // =================================
    JPanel sidebar = createSidebar();
    // =================================
    // 4. MEMBUAT AREA UTAMA
    // =================================
    JPanel mainArea = new JPanel(
        new BorderLayout()
    );
    mainArea.setBackground(
        new Color(245, 247, 250)
    );
    // Membuat Header
    JPanel header = createHeader();
    // =================================
    // 5. MEMBUAT CARDLAYOUT
    // =================================
    cardLayout = new CardLayout();
    pageContainer = new JPanel(
        cardLayout
    );
    pageContainer.setBackground(
        new Color(245, 247, 250)
    );
    // =================================
    // 6. MENAMBAHKAN HALAMAN DASHBOARD
    // =================================
//  DASHBOARD
    pageContainer.add(
        createContent(),
        "DASHBOARD"
    );
//  FORM PENILAIAN
    FormPenilaian formPenilaian = new FormPenilaian();
    formPenilaian.prepareForDashboardCard();
    embeddedFormOwners.add(formPenilaian);

    pageContainer.add(
        createEmbeddedFormPage(formPenilaian),
        "PENILAIAN"
    );
//  HASIL MOORA    
    CetakanHasilRanking formHasil = new CetakanHasilRanking();
    formHasil.prepareForDashboardCard();
    embeddedFormOwners.add(formHasil);

    pageContainer.add(
        createEmbeddedFormPage(formHasil),
        "HASIL_MOORA"
    );
//      LAPORAN
formLaporan = new FormLaporan();
formLaporan.setJMenuBar(null);
embeddedFormOwners.add(formLaporan);
  // Ambil isi form trus lepas dari JFrame-nya, supaya bisa pasang di dashboard
    java.awt.Container isiLaporan = formLaporan.getContentPane();
    formLaporan.setContentPane(new JPanel());
    pageContainer.add(
            isiLaporan,
            "LAPORAN"
            );
//  FORM_BUAH
    FormBuah formBuah = new FormBuah();
    pageContainer.add(
            createEmbeddedFormPage(formBuah),
            "DATA_BUAH"
    );
//  FORM_KRITERIA    
    FormKriteria formKriteria  = new FormKriteria();
    pageContainer.add(
    createEmbeddedFormPage(formKriteria),
            "KRITERIA"
    );
//         ===========================
//          || HALAMAN FORM TRANSAKSI ||
//        ===========================
FormTransaksi formTransaksi = new FormTransaksi();
embeddedFormOwners.add(formTransaksi);
pageContainer.add(
    createEmbeddedFormPage(formTransaksi),
    "TRANSAKSI"
);
//       ===========================
//        ||  HALAMAN FORM BADSTOCK  ||
//       ===========================
FormBadstock formBadstock = new FormBadstock();
embeddedFormOwners.add(formBadstock);
pageContainer.add(
    createEmbeddedFormPage(formBadstock),
    "BADSTOCK"
);
    // =================================
    // ||  GABUNGKAN HEADER DAN HALAMAN ||
    // =================================
    mainArea.add(
        header,
        BorderLayout.NORTH
    );
    mainArea.add(
        pageContainer,
        BorderLayout.CENTER
    );
    // =================================
    // 13. GABUNGIN SIDEBAR DAN MAIN AREA
    // =================================
    getContentPane().add(
        sidebar,
        BorderLayout.WEST
    );
    getContentPane().add(
        mainArea,
        BorderLayout.CENTER
    );
    // =================================
    // 14. MENAMPILKAN DASHBOARD PERTAMA
    // =================================
    cardLayout.show(
        pageContainer,
        "DASHBOARD"
    );
    // =================================
    // 15. REFRESH TAMPILAN
    // =================================
    revalidate();
    repaint();
}
// =============================================
// ADAPTER FORM LAMA KE CARDLAYOUT DASHBOARD
// =============================================
//  Method ini yang buat isi form lama bisa ditampilkan -
//  di area CardLayout. Objek JFrame aslinya tetap disimpan di embeddedFormOwners 
//  biar referensi form sama event CRUD nya ga diconnect.
private JPanel createEmbeddedFormPage(JFrame legacyForm) {
    // Ambil isi tampilan form lama
    java.awt.Container formContent = legacyForm.getContentPane();
    // Simpan ukuran asli form
    Dimension originalSize = formContent.getPreferredSize();
    // Sembunyikan menu navigasi lama
    // Navigasi sekarang menggunakan Sidebar Dashboard
    legacyForm.setJMenuBar(null);
    // Mencegah form lama menutup seluruh aplikasi
    legacyForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    // Lepaskan content pane dari JFrame lama
    legacyForm.setContentPane(new JPanel());
    // Tempat baru untuk isi form
    JPanel contentWrapper = new JPanel(new BorderLayout());
    contentWrapper.setBackground(
        new Color(245, 247, 250)
    );
    contentWrapper.add(
        formContent,
        BorderLayout.CENTER
    );
    // Pertahankan ukuran asli form agar komponen tidak terpotong
    if (originalSize != null
            && originalSize.width > 0
            && originalSize.height > 0) {
        contentWrapper.setPreferredSize(originalSize);
    }
    JScrollPane scrollPane = new JScrollPane(
    contentWrapper,
    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
);
scrollPane.setBorder(
    BorderFactory.createEmptyBorder()
);
scrollPane.setWheelScrollingEnabled(true);
scrollPane.getVerticalScrollBar().setUnitIncrement(24);
scrollPane.getHorizontalScrollBar().setUnitIncrement(24);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
    // Panel akhir untuk CardLayout
    JPanel page = new JPanel(new BorderLayout());

    page.add(
        scrollPane,
        BorderLayout.CENTER
    );
    aktifkanScrollMouseWheel(contentWrapper);
    return page;
}
private void aktifkanScrollMouseWheel(Component komponen) {
    if (komponen instanceof JScrollPane) {
        JScrollPane sp = (JScrollPane) komponen;
        sp.setWheelScrollingEnabled(true);
        sp.getVerticalScrollBar().setUnitIncrement(24);
        sp.getHorizontalScrollBar().setUnitIncrement(24);
    }
    komponen.addMouseWheelListener(e -> {
        JScrollPane target = cariScrollPane(komponen, e.isShiftDown());
        if (target == null) {
            return;
        }
        JScrollBar bar = e.isShiftDown()
                ? target.getHorizontalScrollBar()
                : target.getVerticalScrollBar();
        int delta = (int) Math.round(
                e.getPreciseWheelRotation() * bar.getUnitIncrement() * 3
        );
        int batas = bar.getMaximum() - bar.getVisibleAmount();
        int posisi = Math.max(
                bar.getMinimum(),
                Math.min(batas, bar.getValue() + delta)
        );
        bar.setValue(posisi);
        e.consume();
    });
    if (komponen instanceof Container) {
        for (Component child : ((Container) komponen).getComponents()) {
            aktifkanScrollMouseWheel(child);
        }
    }
}
private JScrollPane cariScrollPane(Component komponen, boolean horizontal) {
    Component current = komponen;
    while (current != null) {
        if (current instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) current;
            JScrollBar bar = horizontal
                    ? sp.getHorizontalScrollBar()
                    : sp.getVerticalScrollBar();
            if (bar.getMaximum() > bar.getVisibleAmount()) {
                return sp;
            }
        }
        current = current.getParent();
    }
    return null;
}
//METHOD SIDEBAR
private JPanel createSidebar() {
    JPanel sidebar = new JPanel();
    sidebar.setLayout(
        new BoxLayout(sidebar, BoxLayout.Y_AXIS)
    );
    sidebar.setBackground(new Color(23, 41, 59));
    sidebar.setPreferredSize(
        new Dimension(245, 720)
    );
    sidebar.setBorder(
        new EmptyBorder(28, 18, 20, 18)
    );
    // =============================
    // ||   1. LOGO DAN NAMA APLIKASI    ||
    // =============================
    JPanel brandPanel = new JPanel(
        new FlowLayout(FlowLayout.LEFT, 8, 0)
    );
    brandPanel.setOpaque(false);
    brandPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel logoIcon = new JLabel("●");
    logoIcon.setFont(
        new Font("SansSerif", Font.BOLD, 24)
    );
    logoIcon.setForeground(
        new Color(45, 212, 191)
    );
    JLabel logoText = new JLabel("TOKO BUAH NAOMI");
    logoText.setFont(
        new Font("SansSerif", Font.BOLD, 14)
    );
    logoText.setForeground(Color.WHITE);
    brandPanel.add(logoIcon);
    brandPanel.add(logoText);
    sidebar.add(brandPanel);
    // SUBTITLE
    JLabel subtitle = new JLabel(
        "SPK KUALITAS BUAH"
    );
    subtitle.setFont(
        new Font("SansSerif", Font.PLAIN, 11)
    );
    subtitle.setForeground(
        new Color(148, 163, 184)
    );
    subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    sidebar.add(Box.createVerticalStrut(5));
    sidebar.add(subtitle);
    // ==============
    // ||   2. PEMISAH   ||
    // ==============
    sidebar.add(Box.createVerticalStrut(28));
    JSeparator separator1 = new JSeparator();
    separator1.setForeground(
        new Color(55, 72, 91)
    );
    separator1.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 1)
    );
    sidebar.add(separator1);
    sidebar.add(Box.createVerticalStrut(25));
    // =====================
    // ||   3. LABEL MAIN MENU   ||
    // =====================
    JLabel menuLabel = new JLabel("MAIN MENU");
    menuLabel.setFont(
        new Font("SansSerif", Font.BOLD, 11)
    );
    menuLabel.setForeground(
        new Color(148, 163, 184)
    );
    menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    sidebar.add(menuLabel);
    sidebar.add(Box.createVerticalStrut(15));
    // ==========================
    // ||   4. DAFTAR MENU NAVIGASI  ||
    // ==========================
    sidebar.add(
        createMenuButton("▦", "Dashboard", true)
    );
    sidebar.add(Box.createVerticalStrut(8));
    sidebar.add(
        createMenuButton("●", "Data Buah", false)
    );
    sidebar.add(Box.createVerticalStrut(8));
    sidebar.add(
        createMenuButton("☷", "Kriteria & Bobot", false)
    );
    sidebar.add(Box.createVerticalStrut(8));
    sidebar.add(
        createMenuButton("✎", "Penilaian", false)
    );
sidebar.add(Box.createVerticalStrut(8));
// MENU TRANSAKSI
sidebar.add(
    createMenuButton("▣", "Transaksi", false)
);
sidebar.add(Box.createVerticalStrut(8));
// MENU BADSTOCK
sidebar.add(
    createMenuButton("⚠", "BadStock", false)
);
    sidebar.add(Box.createVerticalStrut(8));
    sidebar.add(
    createMenuButton("★", "Hasil MOORA", false)
);
sidebar.add(Box.createVerticalStrut(8));
sidebar.add(
    createMenuButton("▤", "Laporan", false)
);
    // =======================
    // ||   5. MENU BAGIAN BAWAH ||
    // =======================
    sidebar.add(Box.createVerticalGlue());
    JSeparator separator2 = new JSeparator();
    separator2.setForeground(
        new Color(55, 72, 91)
    );
    separator2.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 1)
    );
    sidebar.add(separator2);
    sidebar.add(Box.createVerticalStrut(15));
    sidebar.add(
        createMenuButton("↪", "Logout", false)
    );
    sidebar.add(Box.createVerticalStrut(12));
    // ==============
    // ||   6. FOOTER    ||
    // ==============
    JLabel footer = new JLabel(
        "SPK MOORA  |  VERSION 1.0"
    );
    footer.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );
    footer.setForeground(
        new Color(100, 116, 139)
    );
    footer.setAlignmentX(Component.LEFT_ALIGNMENT);
    sidebar.add(footer);
    return sidebar;
}
//METHOD MENU_BTN
private JButton createMenuButton(
        String icon,
        String text,
        boolean active) {
    // =======================
    // ||   1. MEMBUAT TOMBOL    ||
    // =======================
    JButton button = new JButton(
        icon + "    " + text
    );
    // ======================
    // ||   2. FONT DAN UKURAN   ||
    // ======================
    button.setFont(
        new Font("Segoe UI", Font.BOLD, 13)
    );
    button.setHorizontalAlignment(
        SwingConstants.LEFT
    );
    button.setPreferredSize(
        new Dimension(205, 44)
    );
    button.setMinimumSize(
        new Dimension(205, 44)
    );
    button.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 44)
    );
    button.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    // ========================
    // ||   3. BORDER DAN CURSOR ||
    // ========================
    button.setBorder(
        new EmptyBorder(0, 14, 0, 14)
    );
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setCursor(
        Cursor.getPredefinedCursor(
            Cursor.HAND_CURSOR
        )
    );
    button.setToolTipText(text);
    // ========================
    // \\   4. PENGATURAN WARNA  ||
    // ========================
    Color normalBackground =
        new Color(23, 41, 59);
    Color activeBackground =
        new Color(15, 118, 110);
    Color hoverBackground =
        new Color(42, 62, 82);
    Color normalText =
        new Color(203, 213, 225);
   if (button != activeMenuButton) {
        button.setBackground(
            hoverBackground
        );
        button.setForeground(
            Color.WHITE
        );
        activeMenuButton = button;
    } else {
        button.setBackground(
            normalBackground
        );
        button.setForeground(
            normalText
        );
    }
    // =======================
    // ||   5. EFEK HOVER MOUSE  ||
    // =======================
    button.addMouseListener(
        new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    button.setBackground(
                        hoverBackground
                    );
                    button.setForeground(
                        Color.WHITE
                    );
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
              if (button != activeMenuButton) {
        button.setBackground(
            normalBackground
        );
        button.setForeground(
            normalText
        );
                }
            }
        }
    );
    // ============================
    // ||   6. EVENT KLIK MENU SIDEBAR  ||
    // ============================
button.addActionListener(e -> {
    // ===========
    // ||   LOGOUT  ||
    // ===========
    if (text.equals("Logout")) {
        handleLogout();
    } else {
        // Ubah warna menu aktif
        setActiveMenuButton(button);
        // Pindah halaman
        navigateToPage(text);
    }
});
    return button;
}

//  METHOD ACTIVE_MENU_BTN
private void setActiveMenuButton(JButton button) {
    // =========================
    // ||   1. WARNA MENU NORMAL ||
    // =========================
    Color normalBackground =
        new Color(23, 41, 59);
    Color normalText =
        new Color(203, 213, 225);
    // ======================
    // ||   2. WARNA MENU AKTIF  ||
    // ======================
    Color activeBackground =
        new Color(15, 118, 110);
    // ==============================
    // ||   3. KEMBALIKAN TOMBOL LAMA   ||
    // ||   KE WARNA NORMAL                        ||
    // ==============================
    if (activeMenuButton != null) {
        activeMenuButton.setBackground(
            normalBackground
        );
        activeMenuButton.setForeground(
            normalText
        );
    }
    //   4. SIMPAN TOMBOL YANG BARU DIPILIH
    activeMenuButton = button;
    //  5. AKTIFKAN TOMBOL BARU
    activeMenuButton.setBackground(
        activeBackground
    );
    activeMenuButton.setForeground(
        Color.WHITE
    );
}
    //   METHOD HANDLE LOGOUT
private void handleLogout() {
    // 1. KONFIRMASI LOGOUT
    int pilihan = JOptionPane.showConfirmDialog(
        this,
        "Apakah Anda yakin ingin keluar dari akun?",
        "Konfirmasi Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    // 2. JIKA PILIH NO
    if (pilihan != JOptionPane.YES_OPTION) {
        return;
    }
    try {
   // 3. MEMBUAT OBJECT FORM LOGIN
        FormLogin formLogin = new FormLogin();
        // Mengatur perilaku ketika Form Login ditutup
        formLogin.setDefaultCloseOperation(
            JFrame.DISPOSE_ON_CLOSE
        );
        // Mastiin  ukuran komponen Form Login
        formLogin.pack();
        // Atur posisi Form Login di tengah layar
        formLogin.setLocationRelativeTo(null);
        // 4. MENAMPILKAN FORM LOGIN
        formLogin.setVisible(true);
        // Bawa Form Login ke depan
        formLogin.toFront();
        formLogin.requestFocus();
        // 5. MENUTUP MENU UTAMA
        this.dispose();
    } catch (Exception e) {
        // Tampilkan detail error di Output NetBeans
        e.printStackTrace();
        JOptionPane.showMessageDialog(
            this,
            "Gagal membuka Form Login:\n" + e.getMessage(),
            "Logout Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

//              METHOD NAVIIGASI TO PAGE
//tugas untuk terjemahkan nama btn menjadi nama halaman.
private void navigateToPage(String menu) {
// 1. VARIABEL HALAMAN
    String pageName;
    String pageTitle;
    String breadcrumb;
// 2. MENENTUKAN HALAMAN TUJUAN
    switch (menu) {
        case "Dashboard":
            pageName = "DASHBOARD";
            pageTitle = "Dashboard";
            breadcrumb =
                "HALAMAN UTAMA / DASHBOARD";
            break;
        case "Data Buah":
            pageName = "DATA_BUAH";
            pageTitle = "Data Buah";
            breadcrumb =
                "HALAMAN UTAMA / DATA MASTER / DATA BUAH";
            break;
        case "Kriteria & Bobot":
            pageName = "KRITERIA";
            pageTitle = "Kriteria & Bobot";
            breadcrumb =
                "HALAMAN UTAMA / DATA MASTER / KRITERIA";
            break;
        case "Penilaian":
            pageName = "PENILAIAN";
            pageTitle = "Penilaian";
            breadcrumb =
                "HALAMAN UTAMA / PROSES MOORA / PENILAIAN";
            break;
        case "Hasil MOORA":
            pageName = "HASIL_MOORA";
            pageTitle = "Hasil MOORA";
            breadcrumb =
                "HALAMAN UTAMA / PROSES MOORA / HASIL";
            break;
            case "Transaksi":
    pageName = "TRANSAKSI";
    pageTitle = "Transaksi Buah";
    breadcrumb =
        "HALAMAN UTAMA / TRANSAKSI / STOK & PENJUALAN";
    break;
case "BadStock":
    pageName = "BADSTOCK";
    pageTitle = "Manajemen BadStock";
    breadcrumb =
        "HALAMAN UTAMA / MANAJEMEN BADSTOCK";
    break;
        case "Laporan":
            pageName = "LAPORAN";
            pageTitle = "Laporan";
            breadcrumb =
                "HALAMAN UTAMA / LAPORAN";
            // Muat ulang data tiap kali menu Laporan dibuka
            if (formLaporan != null) {
                formLaporan.muatData();
}
            break;
        default:
            JOptionPane.showMessageDialog(
                this,
                "Halaman tidak ditemukan.",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
    }

    // =================================
    // 3. MEMERIKSA CARDLAYOUT
    // =================================

    if (cardLayout == null ||
        pageContainer == null) {

        JOptionPane.showMessageDialog(
            this,
            "CardLayout belum diinisialisasi.",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    // =================================
    // 4. MENAMPILKAN HALAMAN
    // =================================

    cardLayout.show(
        pageContainer,
        pageName
    );

    // =================================
    // 5. MENGUBAH JUDUL HEADER
    // =================================

    headerTitleLabel.setText(
        pageTitle
    );

    // =================================
    // 6. MENGUBAH BREADCRUMB
    // =================================

    headerBreadcrumbLabel.setText(
        breadcrumb
    );

    // =================================
    // 7. REFRESH TAMPILAN
    // =================================

    pageContainer.revalidate();

    pageContainer.repaint();
}

//METHOD HEADER
private JPanel createHeader() { //Kasih tau lok halaman

    // PANEL HEADER UTAMA
    JPanel header = new JPanel(
        new BorderLayout(20, 0)
    );

    header.setBackground(Color.WHITE);

    header.setPreferredSize(
        new Dimension(0, 86)
    );

    header.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(
                0, 0, 1, 0,
                new Color(226, 232, 240)
            ),
            new EmptyBorder(12, 28, 12, 28)
        )
    );

    // ==================================
    // 1. BAGIAN KIRI: BREADCRUMB & JUDUL
    // ==================================

    JPanel leftPanel = new JPanel();

    leftPanel.setLayout(
        new BoxLayout(
            leftPanel,
            BoxLayout.Y_AXIS
        )
    );

    leftPanel.setOpaque(false);

    // BREADCRUMB
    headerBreadcrumbLabel = new JLabel(
        "HALAMAN UTAMA  /  DASHBOARD"
    );

    headerBreadcrumbLabel.setFont(
        new Font("SansSerif", Font.BOLD, 10)
    );

    headerBreadcrumbLabel.setForeground(
        new Color(148, 163, 184)
    );

    // JUDUL HALAMAN
    headerTitleLabel = new JLabel( //Tampilkan judul halaman
        "Dashboard"
    );

    headerTitleLabel.setFont(
        new Font("SansSerif", Font.BOLD, 20)
    );

    headerTitleLabel.setForeground(
        new Color(30, 41, 59)
    );

    leftPanel.add(headerBreadcrumbLabel);

    leftPanel.add(
        Box.createVerticalStrut(5)
    );

    leftPanel.add(headerTitleLabel);

    header.add(
        leftPanel,
        BorderLayout.WEST
    );

    // ==================================
    // 2. BAGIAN KANAN
    // ==================================

    JPanel rightPanel = new JPanel(
        new FlowLayout(
            FlowLayout.RIGHT,
            12,
            8
        )
    );

    rightPanel.setOpaque(false);

    // ==================================
    // 3. TOMBOL NOTIFIKASI
    // ==================================

    JButton notificationButton = new JButton("!"); //Tombol notifikasi

    notificationButton.setToolTipText(
        "Notifikasi"
    );

    notificationButton.setFont(
        new Font("SansSerif", Font.BOLD, 16)
    );

    notificationButton.setPreferredSize(
        new Dimension(36, 36)
    );

    notificationButton.setFocusPainted(false);
    notificationButton.setBorderPainted(false);
    notificationButton.setOpaque(true);

    notificationButton.setBackground(
        new Color(241, 245, 249)
    );

    notificationButton.setForeground(
        new Color(51, 65, 85)
    );

    notificationButton.setCursor(
        Cursor.getPredefinedCursor(
            Cursor.HAND_CURSOR
        )
    );

    // ==================================
    // 4. AVATAR ADMIN
    // ==================================

    JPanel avatarPanel = new JPanel( //Identitas visual ADM
        new GridBagLayout()
    );

    avatarPanel.setPreferredSize(
        new Dimension(38, 38)
    );

    avatarPanel.setBackground(
        new Color(15, 118, 110)
    );

    JLabel avatarText = new JLabel("AD");

    avatarText.setFont(
        new Font("SansSerif", Font.BOLD, 12)
    );

    avatarText.setForeground(Color.WHITE);

    avatarPanel.add(avatarText);

    // ==================================
    // 5. INFORMASI ADMIN
    // ==================================

    JPanel adminInfo = new JPanel(); //Nama dan jabatan admin

    adminInfo.setLayout(
        new BoxLayout(
            adminInfo,
            BoxLayout.Y_AXIS
        )
    );

    adminInfo.setOpaque(false);

    JLabel adminName = new JLabel(
        "Administrator"
    );

    adminName.setFont(
        new Font("SansSerif", Font.BOLD, 12)
    );

    adminName.setForeground(
        new Color(30, 41, 59)
    );

    JLabel adminRole = new JLabel(
        "Pengelola Sistem"
    );

    adminRole.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    adminRole.setForeground(
        new Color(100, 116, 139)
    );

    adminInfo.add(adminName);

    adminInfo.add(
        Box.createVerticalStrut(3)
    );

    adminInfo.add(adminRole);

    // ==================================
    // 6. STATUS ADMIN
    // ==================================

    JPanel statusPanel = new JPanel( //Status admin aktif
        new FlowLayout(
            FlowLayout.CENTER,
            5,
            0
        )
    );

    statusPanel.setBackground(
        new Color(220, 252, 231)
    );

    statusPanel.setBorder(
        new EmptyBorder(7, 10, 7, 10)
    );

    JLabel statusLabel = new JLabel(
        "● ACTIVE"
    );

    statusLabel.setFont(
        new Font("SansSerif", Font.BOLD, 10)
    );

    statusLabel.setForeground(
        new Color(21, 128, 61)
    );

    statusPanel.add(statusLabel);

    // ==================================
    // 7. GABUNGKAN BAGIAN KANAN
    // ==================================

    rightPanel.add(notificationButton);
    rightPanel.add(avatarPanel);
    rightPanel.add(adminInfo);
    rightPanel.add(statusPanel);

    header.add(
        rightPanel,
        BorderLayout.EAST
    );

    return header;
}

//METHOD CONTENT
private JPanel createContent() {
// 1. PANEL UTAMA CONTENT
    JPanel content = new JPanel(
        new BorderLayout(0, 22)
    );
    content.setBackground(
        new Color(245, 247, 250)
    );
    content.setBorder(
        new EmptyBorder(28, 28, 28, 28)
    );
// 2. JUDUL DASHBOARD
    JPanel intro = new JPanel();
    intro.setLayout(
        new BoxLayout(intro, BoxLayout.Y_AXIS)
    );
    intro.setOpaque(false);
    JLabel heading = new JLabel(
        "Dashboard Overview"
    );
    heading.setFont(
        new Font("SansSerif", Font.BOLD, 23)
    );
    heading.setForeground(
        new Color(30, 41, 59)
    );
    heading.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    JLabel description = new JLabel(
        "Ringkasan Sistem Pendukung Keputusan Kualitas Buah"
    );
    description.setFont(
        new Font("SansSerif", Font.PLAIN, 13)
    );
    description.setForeground(
        new Color(100, 116, 139)
    );
    description.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    intro.add(heading);
    intro.add(
        Box.createVerticalStrut(7)
    );
    intro.add(description);
    content.add(
        intro,
        BorderLayout.NORTH
    );
// 3. PANEL KONTEN YANG BISA DI-SCROLL
    JPanel dashboardStack = new JPanel();
    dashboardStack.setLayout(
        new BoxLayout(
            dashboardStack,
            BoxLayout.Y_AXIS
        )
    );
    dashboardStack.setOpaque(false);
    dashboardStack.setBorder(
        new EmptyBorder(0, 0, 12, 8)
    );
// 4. PANEL STATISTIK
    JPanel statsPanel = new JPanel(
        new GridLayout(1, 4, 16, 0)
    );
    statsPanel.setOpaque(false);
    statsPanel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    // KARTU 1 - TOTAL BUAH
    totalBuahValueLabel = new JLabel("...");
    JLabel buahDetailLabel = new JLabel(
        "Jumlah alternatif terdaftar"
    );
    statsPanel.add(
        createStatCard(
            "●",
            "TOTAL DATA ALTERNATIF",
            totalBuahValueLabel,
            buahDetailLabel,
            new Color(220, 38, 38)
        )
    );
    // KARTU 2 - TOTAL KRITERIA
    totalKriteriaValueLabel = new JLabel("...");
    JLabel kriteriaDetailLabel = new JLabel(
        "Kriteria penilaian"
    );
    statsPanel.add(
        createStatCard(
            "☷",
            "TOTAL KRITERIA",
            totalKriteriaValueLabel,
            kriteriaDetailLabel,
            new Color(37, 99, 235)
        )
    );
    // KARTU 3 - METODE SPK
    JLabel metodeValueLabel = new JLabel("MOORA");
    JLabel metodeDetailLabel = new JLabel(
        "Metode pengambilan keputusan"
    );
    statsPanel.add(
        createStatCard(
            "Σ",
            "METODE SPK",
            metodeValueLabel,
            metodeDetailLabel,
            new Color(5, 150, 105)
        )
    );
    // KARTU 4 - BUAH TERBAIK
    bestFruitValueLabel = new JLabel("Belum ada");
    bestFruitDetailLabel = new JLabel(
        "Belum ada hasil perhitungan"
    );
    statsPanel.add(
        createStatCard(
            "★",
            "BUAH TERBAIK",
            bestFruitValueLabel,
            bestFruitDetailLabel,
            new Color(217, 119, 6)
        )
    );
    statsPanel.setPreferredSize(
        new Dimension(930, 145)
    );
    statsPanel.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 145)
    );
    dashboardStack.add(statsPanel);
    dashboardStack.add(
        Box.createVerticalStrut(18)
    );
// 5. GRAFIK MOORA
    JPanel chartPanel = createMooraChartPanel();
    chartPanel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    chartPanel.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 285)
    );
    dashboardStack.add(chartPanel);
    dashboardStack.add(
        Box.createVerticalStrut(18)
    );
// 6. WELCOME BANNER DAN INFO SISTEM
    JPanel lowerPanel = new JPanel(
        new BorderLayout(0, 18)
    );
    lowerPanel.setOpaque(false);
    lowerPanel.add(
        createWelcomeBanner(),
        BorderLayout.NORTH
    );
    lowerPanel.add(
        createProcessInfoPanel(),
        BorderLayout.CENTER
    );
    lowerPanel.setPreferredSize(
        new Dimension(930, 300)
    );
    lowerPanel.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 300)
    );
    lowerPanel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    dashboardStack.add(lowerPanel);
// 7. SCROLL PANE
    JScrollPane scrollPane = new JScrollPane(
        dashboardStack
    );
    scrollPane.setBorder(null);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );
    scrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    scrollPane.getVerticalScrollBar().setUnitIncrement(18);
    content.add(
        scrollPane,
        BorderLayout.CENTER
    );
    return content;
}
//     METHOD CREATE PLACEHOLDER
//  Untuk buat satu method yang bisa digunakan berkali-kali.
private JPanel createPlaceholderPage(
        String title,
        String description) {
// 1. PANEL UTAMA
    JPanel page = new JPanel(
        new BorderLayout()
    );
    page.setBackground(
        new Color(245, 247, 250)
    );
    page.setBorder(
        new EmptyBorder(28, 28, 28, 28)
    );
// 2. PANEL INFORMASI
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(
        new BoxLayout(
            infoPanel,
            BoxLayout.Y_AXIS
        )
    );
    infoPanel.setBackground(Color.WHITE);
    infoPanel.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(28, 28, 28, 28)
        )
    );
// 3. JUDUL HALAMAN
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(
        new Font("SansSerif", Font.BOLD, 23)
    );
    titleLabel.setForeground(
        new Color(30, 41, 59)
    );
    titleLabel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
// 4. DESKRIPSI HALAMAN
    JLabel descriptionLabel = new JLabel(
        "<html><body style='width:550px'>"
        + description
        + "</body></html>"
    );
    descriptionLabel.setFont(
        new Font("SansSerif", Font.PLAIN, 13)
    );
    descriptionLabel.setForeground(
        new Color(100, 116, 139)
    );
    descriptionLabel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    // 5. STATUS HALAMAN
    JLabel statusLabel = new JLabel(
        "HALAMAN SIAP DIINTEGRASIKAN"
    );
    statusLabel.setFont(
        new Font("SansSerif", Font.BOLD, 11)
    );
    statusLabel.setForeground(
        new Color(15, 118, 110)
    );
    statusLabel.setAlignmentX(
        Component.LEFT_ALIGNMENT
    );
    // 6. MENYUSUN KOMPONEN
    infoPanel.add(titleLabel);
    infoPanel.add(
        Box.createVerticalStrut(12)
    );
    infoPanel.add(descriptionLabel);
    infoPanel.add(
        Box.createVerticalStrut(20)
    );
    infoPanel.add(statusLabel);
// 7. MEMASUKKAN PANEL INFORMASI
    page.add(
        infoPanel,
        BorderLayout.NORTH
    );
    return page;
}
//METHODE GRAFIK
private JPanel createMooraChartPanel() {
    // 1. PANEL UTAMA GRAFIK
    JPanel panel = new JPanel(
        new BorderLayout(0, 10)
    );
    panel.setBackground(Color.WHITE);
    panel.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(15, 18, 15, 18)
        )
    );
    panel.setPreferredSize(
        new Dimension(930, 285)
    );
    // 2. JUDUL GRAFIK
    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(
        new BoxLayout(
            titlePanel,
            BoxLayout.Y_AXIS
        )
    );
    titlePanel.setOpaque(false);
    JLabel chartTitle = new JLabel(
        "TOP 5 BUAH DENGAN NILAI MOORA TERTINGGI"
    );
    chartTitle.setFont(
        new Font("SansSerif", Font.BOLD, 13)
    );
    chartTitle.setForeground(
        new Color(30, 41, 59)
    );
    JLabel chartSubtitle = new JLabel(
        "Perbandingan nilai Yi berdasarkan hasil perhitungan MOORA"
    );
    chartSubtitle.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );
    chartSubtitle.setForeground(
        new Color(100, 116, 139)
    );
    titlePanel.add(chartTitle);
    titlePanel.add(
        Box.createVerticalStrut(5)
    );
    titlePanel.add(chartSubtitle);
    panel.add(
        titlePanel,
        BorderLayout.NORTH
    );
    // 3. DATASET GRAFIK
    DefaultCategoryDataset dataset =
        new DefaultCategoryDataset(); //tempat tampung data yang akan ditampilkan di grafik.
    // 4. AMBIL KONEKSI DATABASE
    Connection conn = Koneksi.getKoneksi();
    if (conn == null) {
        JLabel errorLabel = new JLabel(
            "Koneksi database tidak tersedia.",
            SwingConstants.CENTER
        );
        errorLabel.setForeground(
            new Color(220, 38, 38)
        );
        panel.add(
            errorLabel,
            BorderLayout.CENTER
        );
        return panel;
    }
 // 5. QUERY TOP 5 BUAH
//  Query untuk ambil lima alternatif dengan nilai Yi terbesar.
    String sql =
        "SELECT b.nama_buah, p.kode_buah, p.nilai_yi " +
        "FROM tb_penilaian_buah p " +
        "JOIN tb_buah_buahan b " +
        "ON p.kode_buah = b.kode_buah " +
        "WHERE p.nilai_yi IS NOT NULL " +
        "ORDER BY p.nilai_yi DESC, p.kode_buah ASC " +
        "LIMIT 5";
    try (
        PreparedStatement ps =
            conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {
        while (rs.next()) {
            String namaBuah =
                rs.getString("nama_buah");
            String kodeBuah =
                rs.getString("kode_buah");
            double nilaiYi =
                rs.getDouble("nilai_yi");
            if (namaBuah == null) {
                namaBuah = kodeBuah;
            }
            // Nama kategori grafik
            String kategori =
                namaBuah + " (" + kodeBuah + ")";
            // Masukkan data ke dataset
            /*
               Contohnya:
            - Anggur B005 → 0,2722.
            - Apel B001 → 0,2699.
            - Kedondong B006 → 0,2519.
            */
            dataset.addValue(
                nilaiYi,
                "Nilai Yi",
                kategori
            );
        }

    } catch (SQLException e) {

        JLabel errorLabel = new JLabel(
            "Gagal mengambil data grafik: "
            + e.getMessage(),
            SwingConstants.CENTER
        );

        errorLabel.setForeground(
            new Color(220, 38, 38)
        );

        panel.add(
            errorLabel,
            BorderLayout.CENTER
        );

        return panel;
    }

    // =================================
    // 6. PERIKSA DATA GRAFIK
    // =================================

    if (dataset.getColumnCount() == 0) {

        JLabel emptyLabel = new JLabel(
            "Belum ada data hasil perhitungan MOORA.",
            SwingConstants.CENTER
        );

        emptyLabel.setForeground(
            new Color(100, 116, 139)
        );

        panel.add(
            emptyLabel,
            BorderLayout.CENTER
        );

        return panel;
    }

    // =================================
    // 7. MEMBUAT BAR CHART
    // =================================

    JFreeChart chart = ChartFactory.createBarChart(
        null,
        "Nama Buah",
        "Nilai Yi",
        dataset,
        PlotOrientation.HORIZONTAL, //Artinya batang grafik akan ditampilkan secara horizontal. jadi, nama buah lebih gampang dibaca.
        false,
        true,
        false
    );

    chart.setBackgroundPaint(Color.WHITE);

    // =================================
    // 8.           ATUR TAMPILAN GRAFIK
    // =================================

    CategoryPlot plot = chart.getCategoryPlot();

    // =================================
    //              ATUR SKALA NILAI YI
    // =================================

NumberAxis rangeAxis =
    (NumberAxis) plot.getRangeAxis();

// Pastikan skala mencakup angka nol
rangeAxis.setAutoRangeIncludesZero(true);

// Gunakan skala otomatis base on data
rangeAxis.setAutoRange(true);

    plot.setBackgroundPaint(
        new Color(248, 250, 252)
    );

    plot.setOutlineVisible(false);

    plot.setRangeGridlinePaint(
        new Color(226, 232, 240)
    );

    // FONT LABEL SUMBU
    plot.getDomainAxis().setTickLabelFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    plot.getRangeAxis().setTickLabelFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    // =================================
    // 9. MENGATUR WARNA BATANG
    // =================================

    BarRenderer renderer =
        (BarRenderer) plot.getRenderer();

    renderer.setSeriesPaint(
        0,
        new Color(15, 118, 110)
    );

    renderer.setDrawBarOutline(false);

    // Menampilkan nilai Yi pada batang grafik
    renderer.setDefaultItemLabelGenerator(
        new StandardCategoryItemLabelGenerator(
            "{2}",
            new DecimalFormat("0.0000")
        )
    );

    renderer.setDefaultItemLabelsVisible(true);

    // =================================
    // 10. MEMASUKKAN/CONNECT GRAFIK KE SWING/JFRAME
    // =================================

    ChartPanel chartComponent =
    new ChartPanel(chart);

// Nonaktifkan zoom menggunakan mouse
chartComponent.setMouseZoomable(false);

// Nonaktifkan zoom menggunakan mouse wheel
chartComponent.setMouseWheelEnabled(false);

chartComponent.setPreferredSize(
    new Dimension(900, 220)
);

    panel.add(
        chartComponent,
        BorderLayout.CENTER
    );

    return panel;
}

//METHOD DESIGN CARD
private JPanel createStatCard(
        String iconText,
        String titleText,
        JLabel valueLabel,
        JLabel detailLabel,
        Color accentColor) {

    // PANEL KARTU
    JPanel card = new JPanel(
        new BorderLayout(0, 12)
    );

    card.setBackground(Color.WHITE);

    card.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(17, 16, 17, 16)
        )
    );

    // =================================
    // BAGIAN ATAS: IKON DAN JUDUL
    // =================================

    JPanel topPanel = new JPanel(
        new BorderLayout(8, 0)
    );

    topPanel.setOpaque(false);

    JLabel iconLabel = new JLabel(iconText);

    iconLabel.setFont(
        new Font("SansSerif", Font.BOLD, 22)
    );

    iconLabel.setForeground(accentColor);

    JLabel titleLabel = new JLabel(titleText);

    titleLabel.setFont(
        new Font("SansSerif", Font.BOLD, 10)
    );

    titleLabel.setForeground(
        new Color(100, 116, 139)
    );

    topPanel.add(
        iconLabel,
        BorderLayout.WEST
    );

    topPanel.add(
        titleLabel,
        BorderLayout.CENTER
    );

    card.add(
        topPanel,
        BorderLayout.NORTH
    );

    // =================================
    // BAGIAN TENGAH: NILAI STATISTIK
    // =================================

    valueLabel.setFont(
        new Font("SansSerif", Font.BOLD, 23)
    );

    valueLabel.setForeground(
        new Color(30, 41, 59)
    );

    valueLabel.setHorizontalAlignment(
        SwingConstants.LEFT
    );

    card.add(
        valueLabel,
        BorderLayout.CENTER
    );

    // =================================
    // BAGIAN BAWAH: KETERANGAN
    // =================================

    detailLabel.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    detailLabel.setForeground(
        new Color(148, 163, 184)
    );

    card.add(
        detailLabel,
        BorderLayout.SOUTH
    );

    card.setPreferredSize(
        new Dimension(220, 145)
    );

    return card;
}

//METHOD CONNECT CARD STATISTICS DGN DB
private void loadDashboardStatistics() {

    // =================================
    // 1. CEK KOMPONEN
    // =================================

    if (totalBuahValueLabel == null ||
        totalKriteriaValueLabel == null ||
        bestFruitValueLabel == null ||
        bestFruitDetailLabel == null) {

        return;
    }

    // =================================
    // 2. AMBIL KONEKSI DATABASE
    // =================================

    Connection conn = Koneksi.getKoneksi();

    if (conn == null) {

        totalBuahValueLabel.setText("-");
        totalKriteriaValueLabel.setText("-");

        bestFruitValueLabel.setText(
            "Tidak tersedia"
        );

        bestFruitDetailLabel.setText(
            "Koneksi database gagal"
        );

        return;
    }

    try {

        // =================================
        // 3. MENGHITUNG TOTAL DATA BUAH
        // =================================

        String sqlBuah =
            "SELECT COUNT(*) FROM tb_buah_buahan";

        try (
            PreparedStatement ps =
                conn.prepareStatement(sqlBuah);

            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                int totalBuah = rs.getInt(1);

                totalBuahValueLabel.setText(
                    String.valueOf(totalBuah)
                );
            }
        }

        // =================================
        // 4. MENGHITUNG TOTAL KRITERIA
        // =================================

        String sqlKriteria =
            "SELECT COUNT(*) FROM tb_kriteria";

        try (
            PreparedStatement ps =
                conn.prepareStatement(sqlKriteria);

            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                int totalKriteria = rs.getInt(1);

                totalKriteriaValueLabel.setText(
                    String.valueOf(totalKriteria)
                );
            }
        }
        
// =================================
// 5. MENCARI BUAH TERBAIK
// MENGGUNAKAN TABEL PENILAIAN BUAH
// =================================

        String sqlBestFruit =
            "SELECT b.nama_buah, p.kode_buah, " +
            "p.nilai_yi, p.status_layak " +
            "FROM tb_penilaian_buah p " +
            "LEFT JOIN tb_buah_buahan b " +
            "ON b.kode_buah = p.kode_buah " +
            "WHERE p.nilai_yi IS NOT NULL " +
            "ORDER BY p.nilai_yi DESC " +
            "LIMIT 1";

        try (
            PreparedStatement ps =
                conn.prepareStatement(sqlBestFruit);

            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                String namaBuah =
                    rs.getString("nama_buah");

                String kodeBuah =
                    rs.getString("kode_buah");

                double nilaiYi =
                    rs.getDouble("nilai_yi");

                String status =
                    rs.getString("status_layak");

                // Jika nama buah tidak ditemukan,
                // tampilkan kode buah sebagai pengganti.
                if (namaBuah == null ||
                    namaBuah.trim().isEmpty()) {

                    namaBuah = kodeBuah;
                }

                // Tampilkan nama buah terbaik
                bestFruitValueLabel.setText(namaBuah);

                bestFruitValueLabel.setToolTipText(
                    namaBuah + " (" + kodeBuah + ")"
                );

                // Tampilkan nilai Yi dan status kelayakan
                bestFruitDetailLabel.setText(
                    String.format(
                        java.util.Locale.forLanguageTag("id-ID"),
                        "Yi: %.4f | %s",
                        nilaiYi,
                        status == null ? "-" : status
                    )
                );

            } else {

                bestFruitValueLabel.setText("Belum Ada");

                bestFruitDetailLabel.setText(
                    "Belum ada hasil perhitungan"
                );
            }
        }

    } catch (SQLException e) {

        totalBuahValueLabel.setText("-");
        totalKriteriaValueLabel.setText("-");

        bestFruitValueLabel.setText(
            "Tidak tersedia"
        );

        bestFruitDetailLabel.setText(
            "Gagal membaca hasil database"
        );

        JOptionPane.showMessageDialog(
            this,
            "Gagal memuat statistik dashboard:\n"
            + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

/*                    METHODE CREATE WELCOMEBANNER
*Method ini untuk buat banner dengan warna gelap, judul sambutan, 
*deskripsi sistem, tanggal otomatis, dgn  identitas metode MOORA.
*/
private JPanel createWelcomeBanner() {

    // PANEL UTAMA BANNER
    JPanel banner = new JPanel(
        new BorderLayout(20, 0)
    );

    banner.setBackground(
        new Color(23, 41, 59)
    );

    banner.setBorder(
        new EmptyBorder(22, 25, 22, 25)
    );

    banner.setPreferredSize(
        new Dimension(0, 145)
    );

    // =================================
    // 1. BAGIAN KIRI BANNER
    // =================================

    JPanel leftPanel = new JPanel();

    leftPanel.setLayout(
        new BoxLayout(
            leftPanel,
            BoxLayout.Y_AXIS
        )
    );

    leftPanel.setOpaque(false);

    // LABEL WELCOME
    JLabel welcomeLabel = new JLabel(
        "WELCOME TO TOKO BUAH SEGAR NAOMI"
    );

    welcomeLabel.setFont(
        new Font("SansSerif", Font.BOLD, 10)
    );

    welcomeLabel.setForeground(
        new Color(94, 234, 212)
    );

    // JUDUL BANNER
    JLabel title = new JLabel(
        "Selamat Datang!"
    );

    title.setFont(
        new Font("SansSerif", Font.BOLD, 25)
    );

    title.setForeground(Color.WHITE);

    // DESKRIPSI
    JLabel description = new JLabel(
        "<html><body style='width:480px'>"
        + "Sistem Pendukung Keputusan Pemilihan "
        + "Buah-Buahan Layak Jual menggunakan "
        + "metode MOORA untuk membantu menentukan "
        + "kualitas buah terbaik."
        + "</body></html>"
    );

    description.setFont(
        new Font("SansSerif", Font.PLAIN, 12)
    );

    description.setForeground(
        new Color(203, 213, 225)
    );

    // INFORMASI TAMBAHAN
    String tanggal = java.time.LocalDate.now().format(
        java.time.format.DateTimeFormatter.ofPattern(
            "dd MMMM yyyy",
            java.util.Locale.forLanguageTag("id-ID")
        )
    );

    JLabel footer = new JLabel(
        "Dashboard Admin  |  " + tanggal
    );

    footer.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    footer.setForeground(
        new Color(148, 163, 184)
    );

    // SUSUN KOMPONEN KIRI
    leftPanel.add(welcomeLabel);

    leftPanel.add(
        Box.createVerticalStrut(8)
    );

    leftPanel.add(title);

    leftPanel.add(
        Box.createVerticalStrut(7)
    );

    leftPanel.add(description);

    leftPanel.add(
        Box.createVerticalStrut(9)
    );

    leftPanel.add(footer);

    banner.add(
        leftPanel,
        BorderLayout.CENTER
    );

    // =================================
    // 2. BAGIAN KANAN BANNER
    // =================================

    JPanel methodPanel = new JPanel();

    methodPanel.setLayout(
        new BoxLayout(
            methodPanel,
            BoxLayout.Y_AXIS
        )
    );

    methodPanel.setBackground(
        new Color(15, 118, 110)
    );

    methodPanel.setBorder(
        new EmptyBorder(15, 18, 15, 18)
    );

    methodPanel.setPreferredSize(
        new Dimension(135, 95)
    );

    methodPanel.setMinimumSize(
        new Dimension(135, 95)
    );

    // IKON
    JLabel methodIcon = new JLabel("⚖");

    methodIcon.setFont(
        new Font("SansSerif", Font.PLAIN, 27)
    );

    methodIcon.setForeground(Color.WHITE);

    // NAMA METODE
    JLabel methodTitle = new JLabel("MOORA");

    methodTitle.setFont(
        new Font("SansSerif", Font.BOLD, 21)
    );

    methodTitle.setForeground(Color.WHITE);

    // STATUS METODE
    JLabel methodStatus = new JLabel(
        "METODE AKTIF"
    );

    methodStatus.setFont(
        new Font("SansSerif", Font.BOLD, 9)
    );

    methodStatus.setForeground(
        new Color(204, 251, 241)
    );

    methodPanel.add(methodIcon);

    methodPanel.add(
        Box.createVerticalStrut(2)
    );

    methodPanel.add(methodTitle);

    methodPanel.add(
        Box.createVerticalStrut(4)
    );

    methodPanel.add(methodStatus);

    banner.add(
        methodPanel,
        BorderLayout.EAST
    );

    return banner;
}

/*  PANEL FLOW PENGGUNAAN SISTEM
*   untuk bantu user biar mereka tau tahapan penggunaan aplikasi.
*/
private JPanel createProcessInfoPanel() {

    JPanel panel = new JPanel(
        new BorderLayout(0, 10)
    );

    panel.setOpaque(false);

    // JUDUL PANEL
    JLabel heading = new JLabel(
        "ALUR PENGGUNAAN SISTEM"
    );

    heading.setFont(
        new Font("SansSerif", Font.BOLD, 12)
    );

    heading.setForeground(
        new Color(51, 65, 85)
    );

    panel.add(
        heading,
        BorderLayout.NORTH
    );

    // =================================
    // PANEL TIGA LANGKAH
    // =================================

    JPanel stepsPanel = new JPanel(
        new GridLayout(1, 3, 12, 0)
    );

    stepsPanel.setOpaque(false);

    // LANGKAH 1
    stepsPanel.add(
        createProcessStep(
            "01",
            "DATA BUAH",
            "Mengelola data alternatif buah "
            + "yang akan dinilai."
        )
    );

    // LANGKAH 2
    stepsPanel.add(
        createProcessStep(
            "02",
            "PENILAIAN",
            "Memasukkan nilai setiap alternatif "
            + "berdasarkan kriteria C1 sampai C8."
        )
    );

    // LANGKAH 3
    stepsPanel.add(
        createProcessStep(
            "03",
            "PERHITUNGAN MOORA",
            "Menghitung nilai optimasi dan "
            + "menentukan peringkat buah."
        )
    );

    panel.add(
        stepsPanel,
        BorderLayout.CENTER
    );

    return panel;
}

//  KOMPONEN SETIAP LANGKAH
//  Supaya tampilan tiga langkah bisa punya desain yang konsisten
private JPanel createProcessStep(
        String number,
        String title,
        String description) {

    JPanel step = new JPanel();

    step.setLayout(
        new BoxLayout(
            step,
            BoxLayout.Y_AXIS
        )
    );

    step.setBackground(Color.WHITE);

    step.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(226, 232, 240)
            ),
            new EmptyBorder(12, 14, 12, 14)
        )
    );

    // NOMOR LANGKAH
    JLabel numberLabel = new JLabel(
        "STEP " + number
    );

    numberLabel.setFont(
        new Font("SansSerif", Font.BOLD, 10)
    );

    numberLabel.setForeground(
        new Color(15, 118, 110)
    );

    // JUDUL LANGKAH
    JLabel titleLabel = new JLabel(title);

    titleLabel.setFont(
        new Font("SansSerif", Font.BOLD, 12)
    );

    titleLabel.setForeground(
        new Color(30, 41, 59)
    );

    // DESKRIPSI
    JLabel descriptionLabel = new JLabel(
        "<html><body style='width:180px'>"
        + description
        + "</body></html>"
    );

    descriptionLabel.setFont(
        new Font("SansSerif", Font.PLAIN, 10)
    );

    descriptionLabel.setForeground(
        new Color(100, 116, 139)
    );

    // SUSUN KOMPONEN
    step.add(numberLabel);

    step.add(
        Box.createVerticalStrut(7)
    );

    step.add(titleLabel);

    step.add(
        Box.createVerticalStrut(6)
    );

    step.add(descriptionLabel);

    return step;
}















//  =====================================
//  ||  DIBAWAH INI CODE BAWAAN NETBEANS    ||
//  =====================================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 840, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 575, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String args[]) {
        FlatLightLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new Form_MenuUtama().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
