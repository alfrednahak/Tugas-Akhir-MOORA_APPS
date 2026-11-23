package V_From;

import Connection.Koneksi;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
//Import tambahan untuk PDF, Print & Img
 import com.lowagie.text.Document;
 import com.lowagie.text.Element;
 import com.lowagie.text.Font;
 import com.lowagie.text.FontFactory;
 import com.lowagie.text.PageSize;
 import com.lowagie.text.Paragraph;
 import com.lowagie.text.Phrase;
 import com.lowagie.text.pdf.PdfPCell;
 import com.lowagie.text.pdf.PdfPTable;
 import com.lowagie.text.pdf.PdfWriter;
 import com.lowagie.text.Chunk;
 import com.lowagie.text.Image;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.ColumnText;

public class CetakanHasilRanking extends javax.swing.JFrame {    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CetakanHasilRanking.class.getName());
    int sx,sy;
    // Variabel untuk melacak status layar (default awal: false / normal)    
    public CetakanHasilRanking(){
    initComponents();
    this.setExtendedState(Frame.MAXIMIZED_BOTH);
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
public void componentResized(java.awt.event.ComponentEvent evt) {
    int lebarFrame = getContentPane().getWidth();
            
            // Atur jarak (offset) dari ujung kanan layar
            // Sesuaikan angka pengurang (-50, -80, -140) jika jaraknya kurang pas
            lblKELUAR.setLocation(lebarFrame - 50, lblKELUAR.getY());
//            lblMaximize.setLocation(lebarFrame - 85, lblMaximize.getY());
            lblAdmin.setLocation(lebarFrame - 245, lblAdmin.getY());
        }
    });
// TAHAP 1: panel bulat + gradasi
        // ngilangin border bevel lama (persegi) dari 3 kartu ringkasan
        pnlAlternatif.setBorder(null);
        pnlKriteria.setBorder(null);
        pnlPeringkat.setBorder(null);
        // Kartu biasa: putih, sudut membulat
        ((PanelBulat) pnlAlternatif).setRadius(16);
        ((PanelBulat) pnlKriteria).setRadius(16);
        // Kartu Peringkat 1: gradasi teal diagonal (sama dengan preview)
        ((PanelBulat) pnlPeringkat).setRadius(16);
        ((PanelBulat) pnlPeringkat).setGradasi(
                new java.awt.Color(11, 138, 132),    // #0b8a84
                new java.awt.Color(10, 111, 143),    // #0a6f8f
                true);
        lblPeringkat1.setForeground(new java.awt.Color(225, 243, 241));  // #e1f3f1
        lblPeringkatTitle.setForeground(java.awt.Color.WHITE);
// END TAHAP 1

//  (BARIS BARU)
    tblRanking.setForeground(java.awt.Color.BLACK); // teks tabel putih tidak terbaca di baris putih
    lblDicetak.setText("Dicetak pada " + tanggalHariIni());
 
    btnTampilkanOutput.addActionListener(e -> tampilkanRanking());
    btnRefresh.addActionListener(e -> refreshHalaman());
    btnSimpanPDF.addActionListener(e -> simpanPdf());
    
    isiComboFilter();
    tampilkanRanking();
}
  
// Nonaktifkan tombol window karena navigasi pake Sidebar Dashboard
public void prepareForDashboardCard() {
    lblKELUAR.setVisible(false);
//    lblMaximize.setVisible(false);
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpnlHeader = new javax.swing.JPanel();
        lblLogoPrinter = new javax.swing.JLabel();
        lblAdmin = new javax.swing.JLabel();
        lblJudulHeader = new javax.swing.JLabel();
        lblSubJudulHeader = new javax.swing.JLabel();
        lblKELUAR = new javax.swing.JLabel();
        jpnlFilter = new javax.swing.JPanel();
        lblFileLaporan = new javax.swing.JLabel();
        lblPeriode = new javax.swing.JLabel();
        cmbTampikanKelayakan = new javax.swing.JComboBox<>();
        cmbTampikanPeringkat = new javax.swing.JComboBox<>();
        lblKategori = new javax.swing.JLabel();
        cmbKategoriBuah = new javax.swing.JComboBox<>();
        lbl = new javax.swing.JLabel();
        btnTampilkanOutput = new javax.swing.JButton();
        jpnlRingkasan = new javax.swing.JPanel();
        lblRingkasan = new javax.swing.JLabel();
        pnlAlternatif =  new V_From.PanelBulat();
        lblJumlahAlternatif = new javax.swing.JLabel();
        lblAlternatif = new javax.swing.JLabel();
        pnlPeringkat = new V_From.PanelBulat();
        lblPeringkat1 = new javax.swing.JLabel();
        lblPeringkatTitle = new javax.swing.JLabel();
        pnlKriteria = new V_From.PanelBulat();
        lblJumlahKriteria = new javax.swing.JLabel();
        lblKriteria = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRanking = new javax.swing.JTable();
        lblHasilRanking = new javax.swing.JLabel();
        jpnlFuter = new javax.swing.JPanel();
        lblDicetak = new javax.swing.JLabel();
        pnlTombol = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnSimpanPDF = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();

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

        jpnlHeader.setBackground(new java.awt.Color(0, 0, 102));
        jpnlHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogoPrinter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Print.png"))); // NOI18N
        jpnlHeader.add(lblLogoPrinter, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 36, 19));

        lblAdmin.setBackground(new java.awt.Color(0, 0, 153));
        lblAdmin.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblAdmin.setForeground(new java.awt.Color(255, 255, 255));
        lblAdmin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAdmin.setText("Admin");
        lblAdmin.setAutoscrolls(true);
        lblAdmin.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jpnlHeader.add(lblAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 10, 70, 30));

        lblJudulHeader.setFont(new java.awt.Font("Arial Black", 1, 13)); // NOI18N
        lblJudulHeader.setForeground(new java.awt.Color(51, 51, 255));
        lblJudulHeader.setText("Cetak Hasil Ranking");
        jpnlHeader.add(lblJudulHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        lblSubJudulHeader.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblSubJudulHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblSubJudulHeader.setText("SPK pemilihan buah terbaik dengan metode MOORA");
        jpnlHeader.add(lblSubJudulHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, -1, -1));

        lblKELUAR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_X.png"))); // NOI18N
        lblKELUAR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblKELUARMouseClicked(evt);
            }
        });
        jpnlHeader.add(lblKELUAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, -1, -1));

        jpnlFilter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFileLaporan.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        lblFileLaporan.setForeground(new java.awt.Color(0, 0, 0));
        lblFileLaporan.setText("Filter Laporan");
        jpnlFilter.add(lblFileLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, 20));

        lblPeriode.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblPeriode.setText("Status Kelayakan");
        jpnlFilter.add(lblPeriode, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, 20));

        cmbTampikanKelayakan.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cmbTampikanKelayakan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SEMUA" }));
        cmbTampikanKelayakan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbTampikanKelayakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTampikanKelayakanKeyPressed(evt);
            }
        });
        jpnlFilter.add(cmbTampikanKelayakan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, -1));
        cmbTampikanKelayakan.getAccessibleContext().setAccessibleName("");

        cmbTampikanPeringkat.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cmbTampikanPeringkat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua" }));
        cmbTampikanPeringkat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmbTampikanPeringkat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbTampikanPeringkatKeyPressed(evt);
            }
        });
        jpnlFilter.add(cmbTampikanPeringkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 150, -1));

        lblKategori.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblKategori.setText("Kategori Buah");
        jpnlFilter.add(lblKategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, -1, 20));

        cmbKategoriBuah.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cmbKategoriBuah.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Kategori" }));
        cmbKategoriBuah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpnlFilter.add(cmbKategoriBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, -1, -1));

        lbl.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lbl.setText("Tampikan Peringkat");
        jpnlFilter.add(lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, -1, 20));

        btnTampilkanOutput.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnTampilkanOutput.setForeground(new java.awt.Color(0, 0, 0));
        btnTampilkanOutput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Search for Love.png"))); // NOI18N
        btnTampilkanOutput.setText("Tampikan");
        btnTampilkanOutput.setBorderPainted(false);
        btnTampilkanOutput.setContentAreaFilled(false);
        btnTampilkanOutput.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTampilkanOutput.addActionListener(this::btnTampilkanOutputActionPerformed);
        jpnlFilter.add(btnTampilkanOutput, new org.netbeans.lib.awtextra.AbsoluteConstraints(571, 55, -1, 34));

        jpnlRingkasan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRingkasan.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        lblRingkasan.setForeground(new java.awt.Color(0, 0, 0));
        lblRingkasan.setText("Ringkasan");
        jpnlRingkasan.add(lblRingkasan, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 8, -1, 20));

        pnlAlternatif.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblJumlahAlternatif.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblJumlahAlternatif.setText("Jumlah Alternatif");

        lblAlternatif.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblAlternatif.setForeground(new java.awt.Color(0, 0, 0));
        lblAlternatif.setText("30");

        javax.swing.GroupLayout pnlAlternatifLayout = new javax.swing.GroupLayout(pnlAlternatif);
        pnlAlternatif.setLayout(pnlAlternatifLayout);
        pnlAlternatifLayout.setHorizontalGroup(
            pnlAlternatifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlternatifLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnlAlternatifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlternatif)
                    .addComponent(lblJumlahAlternatif))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        pnlAlternatifLayout.setVerticalGroup(
            pnlAlternatifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAlternatifLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJumlahAlternatif, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAlternatif, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpnlRingkasan.add(pnlAlternatif, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, 83));

        pnlPeringkat.setBackground(new java.awt.Color(39, 80, 10));
        pnlPeringkat.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblPeringkat1.setBackground(new java.awt.Color(192, 221, 151));
        lblPeringkat1.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblPeringkat1.setText("Peringkat 1");

        lblPeringkatTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblPeringkatTitle.setForeground(new java.awt.Color(234, 243, 222));
        lblPeringkatTitle.setText("Apel Fuji");

        javax.swing.GroupLayout pnlPeringkatLayout = new javax.swing.GroupLayout(pnlPeringkat);
        pnlPeringkat.setLayout(pnlPeringkatLayout);
        pnlPeringkatLayout.setHorizontalGroup(
            pnlPeringkatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPeringkatLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnlPeringkatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPeringkatTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPeringkat1))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        pnlPeringkatLayout.setVerticalGroup(
            pnlPeringkatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPeringkatLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPeringkat1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPeringkatTitle)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jpnlRingkasan.add(pnlPeringkat, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, -1, -1));

        pnlKriteria.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblJumlahKriteria.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblJumlahKriteria.setText("Jumlah Kriteria");

        lblKriteria.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblKriteria.setForeground(new java.awt.Color(0, 0, 0));
        lblKriteria.setText("8");

        javax.swing.GroupLayout pnlKriteriaLayout = new javax.swing.GroupLayout(pnlKriteria);
        pnlKriteria.setLayout(pnlKriteriaLayout);
        pnlKriteriaLayout.setHorizontalGroup(
            pnlKriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKriteriaLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnlKriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblKriteria)
                    .addComponent(lblJumlahKriteria))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        pnlKriteriaLayout.setVerticalGroup(
            pnlKriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKriteriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJumlahKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jpnlRingkasan.add(pnlKriteria, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, -1));

        tblRanking.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        tblRanking.setForeground(new java.awt.Color(255, 255, 255));
        tblRanking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Rank", "Kode Buah", "Nama Buah", "Nilai Yi", "Keterangan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblRanking);

        lblHasilRanking.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        lblHasilRanking.setForeground(new java.awt.Color(0, 0, 0));
        lblHasilRanking.setText("Tabel Hasil Ranking");

        jpnlFuter.setLayout(new java.awt.BorderLayout());

        lblDicetak.setBackground(new java.awt.Color(51, 51, 255));
        lblDicetak.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblDicetak.setText("Dicetak pada 27 Oktober 2026");
        jpnlFuter.add(lblDicetak, java.awt.BorderLayout.WEST);

        pnlTombol.setLayout(new java.awt.GridLayout(1, 0));

        btnRefresh.setBackground(new java.awt.Color(102, 102, 102));
        btnRefresh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Sync.png"))); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlTombol.add(btnRefresh);

        btnSimpanPDF.setBackground(new java.awt.Color(102, 102, 102));
        btnSimpanPDF.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSimpanPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Save.png"))); // NOI18N
        btnSimpanPDF.setText("Simpan PDF");
        btnSimpanPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnlTombol.add(btnSimpanPDF);

        btnCetak.setBackground(new java.awt.Color(51, 51, 255));
        btnCetak.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Print.png"))); // NOI18N
        btnCetak.setText("Cetak");
        btnCetak.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCetak.addActionListener(this::btnCetakActionPerformed);
        pnlTombol.add(btnCetak);

        jpnlFuter.add(pnlTombol, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpnlFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpnlFuter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblHasilRanking)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpnlRingkasan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlRingkasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHasilRanking)
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpnlFuter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
// METHOD REFRESH
//  METHOD PRINT
    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
       // 1. Tidak ada data = tidak ada yang dicetak
    if (tblRanking.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Tidak ada data ranking untuk dicetak.",
                "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    try {
        // 2. Untuk bikin PDF laporan di folder sementara (pake desain yang sama dengan Method Simpan PDF)
        java.io.File sementara = java.io.File.createTempFile("Hasil_Ranking_MOORA_", ".pdf");
        sementara.deleteOnExit();
        buatPdf(sementara);
        // 3. Untuk buka di PDF viewer: user juga bisa pratinjau dulu, trus cetak pakie shortcut Ctrl+P
        java.awt.Desktop.getDesktop().open(sementara);
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Gagal menyiapkan cetakan: " + e.getMessage()
                + "\n\nPastikan di komputer ini ada aplikasi pembaca PDF (Adobe Reader, Edge, Chrome, dll).",
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnCetakActionPerformed
//METHOD PDF
private void simpanPdf() {
    DefaultTableModel model = (DefaultTableModel) tblRanking.getModel(); 
    // 1. Kalo tabel kosong, tidak ada bisa bikin  PDF
    if (model.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Tidak ada data ranking untuk disimpan.",
                "WARNING !!!", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    // 2. ini Dialog "Simpan sebagai" dengan nama file bawaan berisi tanggal
    javax.swing.JFileChooser pemilih = new javax.swing.JFileChooser();
    pemilih.setDialogTitle("Simpan Hasil Ranking sebagai PDF");
    pemilih.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Dokumen PDF (*.pdf)", "pdf"));
    pemilih.setSelectedFile(new java.io.File(
            "Hasil_Ranking_MOORA_" + java.time.LocalDate.now() + ".pdf"));
 
    if (pemilih.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
        return;   // MK: user klick Cancel
    }
 
    // Makesure extension file harus .pdf
    java.io.File file = pemilih.getSelectedFile();
    if (!file.getName().toLowerCase().endsWith(".pdf")) {
        file = new java.io.File(file.getAbsolutePath() + ".pdf");
    }
 
    // Kalo file sudah ada, minta konfirm sebelum ditimpa
    if (file.exists()) {
        int timpa = javax.swing.JOptionPane.showConfirmDialog(this,
                "File sudah ada. Timpa file lama?",
                "Konfirmasi", javax.swing.JOptionPane.YES_NO_OPTION);
        if (timpa != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
    }
 
    // 3. Buat PDF-nya (seluruh isi laporan dibuat di method buatPdf)
    try {
        buatPdf(file);
    } catch (Exception e) {
        // Penyebab paling umum: file dengan nama sama sedang terbuka di PDF reader
        javax.swing.JOptionPane.showMessageDialog(this,
                "Gagal buat PDF: " + e.getMessage()
                + "\n\nKalo file yang sama sedang dibuka di program lain, tutup dulu baru coba lagi.",
                "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }
 
    // 4. Berhasil: tawarkan untuk buka  file PDF
    int buka = javax.swing.JOptionPane.showConfirmDialog(this,
            "PDF berhasil disimpan di:\n" + file.getAbsolutePath() + "\n\nBuka file sekarang?",
            "Sukses", javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
 
    if (buka == javax.swing.JOptionPane.YES_OPTION) {
        try {
            java.awt.Desktop.getDesktop().open(file);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "File sudah disimpan, tapi tidak bisa buka otomatis.\nSilahkan buka manual dari lokasi di atas.");
        }
    }
} 

// ====================================================================
// -->> BAGIAN 2: buatPdf(): inti pembuatan laporan PDF (untukSimpan PDF dan Cetak) ||
// ====================================================================
//METHOD PDF
private void buatPdf(java.io.File file) throws Exception {
    DefaultTableModel model = (DefaultTableModel) tblRanking.getModel();

    // ---------- Warna ----------
    java.awt.Color biruTua   = new java.awt.Color(0, 0, 102);
    java.awt.Color hijauTua  = new java.awt.Color(39, 80, 10);
    java.awt.Color abuGaris  = new java.awt.Color(190, 190, 190);
    java.awt.Color latarInfo = new java.awt.Color(244, 246, 252);

    // ---------- Huruf ----------
    Font fToko       = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, biruTua);
    Font fAlamat     = FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Color.DARK_GRAY);
    Font fJudul      = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, java.awt.Color.BLACK);
    Font fSub        = FontFactory.getFont(FontFactory.HELVETICA, 10, java.awt.Color.DARK_GRAY);
    Font fCatatan    = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, java.awt.Color.DARK_GRAY);
    Font fInfo       = FontFactory.getFont(FontFactory.HELVETICA, 10, java.awt.Color.BLACK);
    Font fInfoTebal  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, java.awt.Color.BLACK);
    Font fSeksi      = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, biruTua);
    Font fHeader     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, java.awt.Color.WHITE);
    Font fIsi        = FontFactory.getFont(FontFactory.HELVETICA, 10, java.awt.Color.BLACK);
    Font fIsiTebal   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, java.awt.Color.BLACK);
    Font fKartuLabel = FontFactory.getFont(FontFactory.HELVETICA, 9, java.awt.Color.DARK_GRAY);
    Font fKartuNilai = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, java.awt.Color.BLACK);
    Font fKartuLabelHijau = FontFactory.getFont(FontFactory.HELVETICA, 9, new java.awt.Color(192, 221, 151));
    Font fKartuNilaiHijau = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new java.awt.Color(234, 243, 222));
    Font fFooter     = FontFactory.getFont(FontFactory.HELVETICA, 8, java.awt.Color.GRAY);

    Document dokumen = new Document(PageSize.A4, 40, 40, 40, 55);

    try (java.io.FileOutputStream keluaran = new java.io.FileOutputStream(file)) {

        PdfWriter penulis = PdfWriter.getInstance(dokumen, keluaran);

        // ---------- Footer otomatis di setiap halaman ----------
        penulis.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter w, Document d) {
                PdfContentByte cb = w.getDirectContent();
                cb.setColorStroke(new java.awt.Color(190, 190, 190));
                cb.setLineWidth(0.6f);
                cb.moveTo(d.left(), d.bottom() - 12);
                cb.lineTo(d.right(), d.bottom() - 12);
                cb.stroke();
                ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                        new Phrase("Toko Buah Naomi - SPK Pemilihan Buah-Buahan Metode MOORA", fFooter),
                        d.left(), d.bottom() - 26, 0);
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                        new Phrase("Halaman " + w.getPageNumber(), fFooter),
                        d.right(), d.bottom() - 26, 0);
            }
        });

        dokumen.open();

        // ================= 1. KOP SURAT =================
        Image logo = muatLogo();
        PdfPTable kop = new PdfPTable(new float[]{1.3f, 5f, 1.3f});
        kop.setWidthPercentage(100);

        PdfPCell selLogo = (logo != null) ? new PdfPCell(logo) : new PdfPCell(new Phrase(""));
        selLogo.setBorder(PdfPCell.NO_BORDER);
        selLogo.setHorizontalAlignment(Element.ALIGN_LEFT);
        selLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        kop.addCell(selLogo);

        PdfPCell selNama = new PdfPCell();
        selNama.setBorder(PdfPCell.NO_BORDER);
        selNama.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph pToko = new Paragraph("TOKO BUAH NAOMI", fToko);
        pToko.setAlignment(Element.ALIGN_CENTER);
        Paragraph pAlamat = new Paragraph(
                "Jl. H.R. Ismail No.11, RT.8/RW.3, Cakung, Jakarta Timur", fAlamat);
        pAlamat.setAlignment(Element.ALIGN_CENTER);
        selNama.addElement(pToko);
        selNama.addElement(pAlamat);
        kop.addCell(selNama);

        PdfPCell selKosong = new PdfPCell(new Phrase(""));
        selKosong.setBorder(PdfPCell.NO_BORDER);
        kop.addCell(selKosong);
        dokumen.add(kop);

        // Garis pemisah kop
        PdfPTable garis = new PdfPTable(1);
        garis.setWidthPercentage(100);
        PdfPCell selGaris = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 3)));
        selGaris.setBorder(PdfPCell.BOTTOM);
        selGaris.setBorderWidthBottom(2.5f);
        selGaris.setBorderColorBottom(biruTua);
        selGaris.setPadding(0);
        garis.addCell(selGaris);
        garis.setSpacingBefore(4);
        garis.setSpacingAfter(14);
        dokumen.add(garis);

        // ================= 2. JUDUL LAPORAN =================
        Paragraph judul = new Paragraph("LAPORAN HASIL RANKING", fJudul);
        judul.setAlignment(Element.ALIGN_CENTER);
        dokumen.add(judul);

        Paragraph subJudul = new Paragraph(
                "Sistem Pendukung Keputusan Pemilihan Buah-Buahan Layak Jual Menggunakan Metode MOORA", fSub);
        subJudul.setAlignment(Element.ALIGN_CENTER);
        subJudul.setSpacingAfter(14);
        dokumen.add(subJudul);

        // ================= 3. KARTU RINGKASAN =================
        PdfPTable kartu = new PdfPTable(new float[]{3f, 0.25f, 3f, 0.25f, 4.5f});
        kartu.setWidthPercentage(100);
        kartu.addCell(buatKartu("Jumlah Alternatif", lblAlternatif.getText(),
                fKartuLabel, fKartuNilai, java.awt.Color.WHITE, abuGaris));
        kartu.addCell(buatSpacer());
        kartu.addCell(buatKartu("Jumlah Kriteria", lblKriteria.getText(),
                fKartuLabel, fKartuNilai, java.awt.Color.WHITE, abuGaris));
        kartu.addCell(buatSpacer());
        kartu.addCell(buatKartu("Peringkat 1", lblPeringkatTitle.getText(),
                fKartuLabelHijau, fKartuNilaiHijau, hijauTua, hijauTua));
        kartu.setSpacingAfter(12);
        dokumen.add(kartu);

        // ================= 4. INFO FILTER =================
        String[][] filter = {
            {"Tanggal cetak",          tanggalHariIni()},
            {"Status kelayakan",       String.valueOf(cmbTampikanKelayakan.getSelectedItem())},
            {"Peringkat ditampilkan",  String.valueOf(cmbTampikanPeringkat.getSelectedItem())},
            {"Diurutkan berdasarkan",  String.valueOf(cmbKategoriBuah.getSelectedItem())}
        };
        PdfPTable tabelFilter = new PdfPTable(new float[]{2.3f, 0.3f, 3f, 2.3f, 0.3f, 3f});
        tabelFilter.setWidthPercentage(100);
        for (String[] baris : filter) {
            tabelFilter.addCell(buatSelFilter(baris[0], fInfoTebal, latarInfo));
            tabelFilter.addCell(buatSelFilter(":", fInfo, latarInfo));
            tabelFilter.addCell(buatSelFilter(baris[1], fInfo, latarInfo));
        }
        tabelFilter.setSpacingAfter(16);
        dokumen.add(tabelFilter);

        // ================= 5. TABEL RANKING =================
        Paragraph seksi = new Paragraph(
                "Tabel Hasil Ranking  (" + model.getRowCount() + " buah)", fSeksi);
        seksi.setSpacingAfter(6);
        dokumen.add(seksi);

        PdfPTable tabel = new PdfPTable(new float[]{1f, 1.6f, 4.2f, 1.9f, 2.6f});
        tabel.setWidthPercentage(100);
        tabel.setHeaderRows(1);   // header diulang otomatis di tiap halaman

        String[] judulKolom = {"Rank", "Kode Buah", "Nama Buah", "Nilai Yi", "Keterangan"};
        for (String kolom : judulKolom) {
            tabel.addCell(buatSel(kolom, fHeader, Element.ALIGN_CENTER, biruTua));
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            String rank   = String.valueOf(model.getValueAt(i, 0));
            String kode   = String.valueOf(model.getValueAt(i, 1));
            String nama   = String.valueOf(model.getValueAt(i, 2));
            String nilai  = String.valueOf(model.getValueAt(i, 3));
            String status = String.valueOf(model.getValueAt(i, 4));

            // Latar: juara 1-3  warna emas / perak / perunggu muda, sisanya selang-seling
            java.awt.Color latar;
            switch (rank) {
                case "1": latar = new java.awt.Color(255, 243, 205); break;
                case "2": latar = new java.awt.Color(232, 234, 237); break;
                case "3": latar = new java.awt.Color(250, 232, 215); break;
                default:
                    latar = (i % 2 == 0) ? java.awt.Color.WHITE : new java.awt.Color(244, 246, 252);
            }
            Font fRank = rank.matches("[123]") ? fIsiTebal : fIsi;

            boolean layak = status.equalsIgnoreCase("Layak Jual");
            Font fStatus = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
                    layak ? new java.awt.Color(0, 110, 40) : new java.awt.Color(170, 30, 30));

            tabel.addCell(buatSel(rank,   fRank,   Element.ALIGN_CENTER, latar));
            tabel.addCell(buatSel(kode,   fIsi,    Element.ALIGN_CENTER, latar));
            tabel.addCell(buatSel(nama,   fRank,   Element.ALIGN_LEFT,   latar));
            tabel.addCell(buatSel(nilai,  fIsi,    Element.ALIGN_CENTER, latar));
            tabel.addCell(buatSel(status, fStatus, Element.ALIGN_CENTER, latar));
        }
        dokumen.add(tabel);

        // ================= 6. CATATAN =================
        Paragraph catatan = new Paragraph(
                "Catatan: Nilai Yi =  hasil optimasi dari Metode MOORA. "
              + "Semakin besar nilai Yi, maka semakin tinggi peringkat alternatif nya.", fCatatan);
        catatan.setSpacingBefore(10);
        catatan.setSpacingAfter(18);
        dokumen.add(catatan);

        // ================= 7. TANDA TANGAN =================
        PdfPTable ttd = new PdfPTable(new float[]{6f, 3.2f});
        ttd.setWidthPercentage(100);
        ttd.setKeepTogether(true);   //  Supaya blok TTD  tidak terpotong antar_halaman

        PdfPCell kiri = new PdfPCell(new Phrase(""));
        kiri.setBorder(PdfPCell.NO_BORDER);
        ttd.addCell(kiri);

        PdfPCell kanan = new PdfPCell();
        kanan.setBorder(PdfPCell.NO_BORDER);
        Paragraph tgl = new Paragraph("Jakarta, " + tanggalHariIni(), fInfo);
        Paragraph mengetahui = new Paragraph("Mengetahui,", fInfo);
        Paragraph jabatan = new Paragraph("Owner Toko Buah Naomi", fInfoTebal);
        Paragraph garisTtd = new Paragraph("\n\n\n(................................................)", fInfo);
        tgl.setAlignment(Element.ALIGN_CENTER);
        mengetahui.setAlignment(Element.ALIGN_CENTER);
        jabatan.setAlignment(Element.ALIGN_CENTER);
        garisTtd.setAlignment(Element.ALIGN_CENTER);
        kanan.addElement(tgl);
        kanan.addElement(mengetahui);
        kanan.addElement(jabatan);
        kanan.addElement(garisTtd);
        ttd.addCell(kanan);
        dokumen.add(ttd);

        dokumen.close();   // wajib supaya file PDF selesai ditulis
    }
}

// Kartu ringkasan: label kecil ada di atas, nilai besar ada di bawah.
private PdfPCell buatKartu(String label, String nilai, Font fLabel, Font fNilai,
                           java.awt.Color latar, java.awt.Color tepi) {
    PdfPCell sel = new PdfPCell();
    sel.setBackgroundColor(latar);
    sel.setBorderColor(tepi);
    sel.setBorderWidth(1f);
    sel.setPaddingTop(8);
    sel.setPaddingBottom(10);
    sel.setPaddingLeft(12);
    sel.addElement(new Paragraph(label, fLabel));
    sel.addElement(new Paragraph(nilai, fNilai));
    return sel;
}

// Sel kosong tanpa garis, dipake  untuk jarak antar_kartu.
private PdfPCell buatSpacer() {
    PdfPCell sel = new PdfPCell(new Phrase(""));
    sel.setBorder(PdfPCell.NO_BORDER);
    return sel;
}

// Sel info filter: tanpa garis tepi, dengan latar abu lightblue.
private PdfPCell buatSelFilter(String teks, Font font, java.awt.Color latar) {
    PdfPCell sel = new PdfPCell(new Phrase(teks, font));
    sel.setBorder(PdfPCell.NO_BORDER);
    sel.setBackgroundColor(latar);
    sel.setPadding(5);
    return sel;
}
 
//Buat satu sel tabel berisi garis tepi abu-abu (dipake untuk tabel ranking).
private PdfPCell buatSel(String teks, Font font, int perataan, java.awt.Color latar) {
    PdfPCell sel = new PdfPCell(new Phrase(teks, font));
    sel.setHorizontalAlignment(perataan);                      // rata kiri / tengah
    sel.setBackgroundColor(latar);                             // warna latar sel
    sel.setPadding(6);                                         // jarak teks ke tepi sel
    sel.setBorderColor(new java.awt.Color(190, 190, 190));     // warna garis tepi
    return sel;
}
 
// Buat satu sel TANPA garis tepi (dipake untuk blok keterangan yang di atas tabel).
private PdfPCell buatSelInfo(String teks, Font font) {
    PdfPCell sel = new PdfPCell(new Phrase(teks, font));
    sel.setBorder(PdfPCell.NO_BORDER);
    sel.setPaddingBottom(3);
    return sel;
}

//METHOD ADD LOGO KE PDF
private Image muatLogo() {
    try {
        // Tanda "/" di awal = mulai dari akar folder resources
        java.net.URL lokasiLogo = getClass().getResource("/img/img_LogoNaomi.jpg");
        if (lokasiLogo == null) {
            return null;                 // file logo belum ditaro di folder yang benar
        }
        Image logo = Image.getInstance(lokasiLogo);
        logo.scaleToFit(90, 90);         // muat dalam kotak 70 x 70 - 90 x 90 poin, proporsi nya tetap
        return logo;
    } catch (Exception e) {
        return null;                     // gagal baca gambar/logo: lanjut jalan tanpa logo
    }
}

    private void cmbTampikanKelayakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTampikanKelayakanKeyPressed
    if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
    btnTampilkanOutput.requestFocus();
}
    }//GEN-LAST:event_cmbTampikanKelayakanKeyPressed

    private void cmbTampikanPeringkatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbTampikanPeringkatKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
        btnTampilkanOutput.requestFocus();
        }
    }//GEN-LAST:event_cmbTampikanPeringkatKeyPressed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        sx = evt.getX();
        sy = evt.getY();
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation( x - sx, y - sy);
    }//GEN-LAST:event_formMouseDragged

    private void lblKELUARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKELUARMouseClicked
    dispose();
    }//GEN-LAST:event_lblKELUARMouseClicked

    private void btnTampilkanOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTampilkanOutputActionPerformed

    }//GEN-LAST:event_btnTampilkanOutputActionPerformed
// Satu-satunya tempat yang panggil Koneksi.
// Ambil koneksi simultan dari class Koneksi.
private java.sql.Connection bukaKoneksi() throws Exception {
    java.sql.Connection c = Koneksi.getKoneksi();
    if (c == null) {
        throw new Exception("Koneksi database gagal. Cek MySQL di XAMPP.");
    }
    return c;
}
private String tanggalHariIni() {
    return java.time.LocalDate.now().format(
        java.time.format.DateTimeFormatter.ofPattern(
            "d MMMM yyyy", java.util.Locale.forLanguageTag("id-ID")));
}
 
// Refresh semua filter ke kondisi awal, kemudian muat ulang lagi data yang dari database.
private void refreshHalaman() {
    // 1. Reset semua combo ke awal
    cmbTampikanKelayakan.setSelectedIndex(0);   // "Semua"
    cmbTampikanPeringkat.setSelectedIndex(0);   // "Semua"
    if (cmbKategoriBuah.getItemCount() > 0) {
        cmbKategoriBuah.setSelectedIndex(0);    // "Peringkat Tertinggi"
    }

    // 2. Clear tabel yang terselect
    tblRanking.clearSelection();

    // 3. Update tanggal cetak (kalo aplikasi dibiarin kebuka terus lewat tengah malam)
    lblDicetak.setText("Dicetak pada " + tanggalHariIni());

    // 4. Muat ulang data dari database
    tampilkanRanking();
}

//Kategori Urutan
private void isiComboKategori() {
    DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
    m.addElement("Semua Kategori");
    try {
        java.sql.Connection conn = bukaKoneksi();
        try (PreparedStatement ps = conn.prepareStatement(
                 "SELECT DISTINCT kategori FROM tb_buah_buahan "
               + "WHERE kategori IS NOT NULL ORDER BY kategori");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                m.addElement(rs.getString(1));
            }
        }
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Gagal memuat kategori: " + e.getMessage());
    }
    cmbKategoriBuah.setModel(m);
}

private void isiComboFilter() {
    cmbTampikanKelayakan.setModel(new DefaultComboBoxModel<>(
        new String[]{"Semua", "Layak Jual", "Tidak Layak Jual"}));
    cmbTampikanPeringkat.setModel(new DefaultComboBoxModel<>(
        new String[]{"Semua", "3 Teratas", "5 Teratas"}));

    // Slot "Kategori Buah" dipakai ulang sebagai pilihan pengurutan
    lblKategori.setText("Urutkan Berdasarkan");
    cmbKategoriBuah.setModel(new DefaultComboBoxModel<>(
        new String[]{"Peringkat Tertinggi", "Peringkat Terendah",
                     "Nama Buah (A-Z)", "Kode Buah"}));
}
// Jalankan query yang hasilnya satu nilai saja (angka atau teks).
private String ambilSatuNilai(java.sql.Connection conn, String sql) throws Exception {
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getString(1) : "-";
    }
}
 
private void tampilkanRanking() {
     DefaultTableModel model = (DefaultTableModel) tblRanking.getModel();
    model.setRowCount(0);

    String status = String.valueOf(cmbTampikanKelayakan.getSelectedItem());
    String batas  = String.valueOf(cmbTampikanPeringkat.getSelectedItem());
    String urutan = String.valueOf(cmbKategoriBuah.getSelectedItem());
    boolean pakaiFilter = !status.equals("Semua");

    String orderBy;
    switch (urutan) {
        case "Peringkat Terendah": orderBy = "p.nilai_yi ASC";   break;
        case "Nama Buah (A-Z)":    orderBy = "b.nama_buah ASC";  break;
        case "Kode Buah":          orderBy = "p.kode_buah ASC";  break;
        default:                   orderBy = "p.nilai_yi DESC";  break;
    }

    String sql =
        "SELECT (SELECT COUNT(*) + 1 FROM tb_penilaian_buah x "
      + "        WHERE x.nilai_yi > p.nilai_yi) AS peringkat, "
      + "       p.kode_buah, b.nama_buah, p.nilai_yi, p.status_layak "
      + "FROM tb_penilaian_buah p "
      + "JOIN tb_buah_buahan b ON p.kode_buah = b.kode_buah "
      + (pakaiFilter ? "WHERE p.status_layak = ? " : "")
      + "ORDER BY " + orderBy;

    if (batas.equals("3 Teratas")) {
        sql += " LIMIT 3";
    } else if (batas.equals("5 Teratas")) {
        sql += " LIMIT 5";
    }
 
    try {
        // PENTING: koneksi ini dipake sama2 unuk semua aplikasi,
        // jadi TIDAK boleh ditutup/close (jangan taro di dalam try-with-resources).
        java.sql.Connection conn = bukaKoneksi();
 
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
    if (pakaiFilter) {
        ps.setString(1, status);
    }
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("peringkat"),
                        rs.getString("kode_buah"),
                        rs.getString("nama_buah"),
                        String.format(java.util.Locale.forLanguageTag("id-ID"),
                                      "%.4f", rs.getDouble("nilai_yi")),
                        rs.getString("status_layak")
                    });
                }
            }
        }
 
        // Kotak ringkasan (tidak ikut terpengaruh filter)
        lblAlternatif.setText(ambilSatuNilai(conn,
            "SELECT COUNT(*) FROM tb_penilaian_buah"));
        lblKriteria.setText(ambilSatuNilai(conn,
            "SELECT COUNT(*) FROM tb_kriteria"));
        lblPeringkatTitle.setText(ambilSatuNilai(conn,
            "SELECT b.nama_buah FROM tb_penilaian_buah p "
          + "JOIN tb_buah_buahan b ON p.kode_buah = b.kode_buah "
          + "ORDER BY p.nilai_yi DESC LIMIT 1"));
 
        // =========================================================================
        // TAMBAHAN LOGIKA DINAMIS UNTUK LABEL TABEL HASIL RANKING
        // =========================================================================
        int jumlahBaris = tblRanking.getRowCount();
        if (status.equalsIgnoreCase("Layak Jual")) {
            lblHasilRanking.setText("Tabel Hasil Ranking - Total Buah Layak Jual " + jumlahBaris + " Buah");
        } else if (status.equalsIgnoreCase("Tidak Layak Jual")) {
            lblHasilRanking.setText("Tabel Hasil Ranking - Total Buah Tidak Layak Jual " + jumlahBaris + " Buah");
        } else {
            lblHasilRanking.setText("Tabel Hasil Ranking - Total Keseluruhan Buah " + jumlahBaris + " Buah");
        }
        // =========================================================================

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Gagal memuat data: " + e.getMessage());
    }        
} 

// ==========================================
// DIBAWAH INI ADALAH CODE BAWAAN NETBEANS!!: ||
// ==========================================
public static void main(String args[]) {
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
java.awt.EventQueue.invokeLater(() -> new CetakanHasilRanking().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSimpanPDF;
    private javax.swing.JButton btnTampilkanOutput;
    private javax.swing.JComboBox<String> cmbKategoriBuah;
    private javax.swing.JComboBox<String> cmbTampikanKelayakan;
    private javax.swing.JComboBox<String> cmbTampikanPeringkat;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpnlFilter;
    private javax.swing.JPanel jpnlFuter;
    private javax.swing.JPanel jpnlHeader;
    private javax.swing.JPanel jpnlRingkasan;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblAdmin;
    private javax.swing.JLabel lblAlternatif;
    private javax.swing.JLabel lblDicetak;
    private javax.swing.JLabel lblFileLaporan;
    private javax.swing.JLabel lblHasilRanking;
    private javax.swing.JLabel lblJudulHeader;
    private javax.swing.JLabel lblJumlahAlternatif;
    private javax.swing.JLabel lblJumlahKriteria;
    private javax.swing.JLabel lblKELUAR;
    private javax.swing.JLabel lblKategori;
    private javax.swing.JLabel lblKriteria;
    private javax.swing.JLabel lblLogoPrinter;
    private javax.swing.JLabel lblPeringkat1;
    private javax.swing.JLabel lblPeringkatTitle;
    private javax.swing.JLabel lblPeriode;
    private javax.swing.JLabel lblRingkasan;
    private javax.swing.JLabel lblSubJudulHeader;
    private javax.swing.JPanel pnlAlternatif;
    private javax.swing.JPanel pnlKriteria;
    private javax.swing.JPanel pnlPeringkat;
    private javax.swing.JPanel pnlTombol;
    private javax.swing.JTable tblRanking;
    // End of variables declaration//GEN-END:variables
}
