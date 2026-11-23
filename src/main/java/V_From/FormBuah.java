package V_From;
import Connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class FormBuah extends javax.swing.JFrame {
    private DefaultTableModel model;
// PRIVATE STTAIC FINAL INI MEMANG SUDAH ADA PAS BIKIN JFORMBUAH.   
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FormBuah.class.getName());


    public FormBuah() {
    initComponents();
    setLocationRelativeTo(null); // Agar form tampil di tengah layar
    initTable();
    loadData();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlUtama = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        pnl_BtnInputan = new javax.swing.JPanel();
        lblKodeBuah = new javax.swing.JLabel();
        lblNamaBuah = new javax.swing.JLabel();
        txtKodeBuah = new javax.swing.JTextField();
        txtNamaBuah = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBuah = new javax.swing.JTable();
        btnUbah = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu_Menu = new javax.swing.JMenu();
        jMenuFormKriteria = new javax.swing.JMenuItem();
        jMenuFormLogin = new javax.swing.JMenuItem();
        jMenuFormPenilaian = new javax.swing.JMenuItem();
        jMenuAkun = new javax.swing.JMenu();
        jMenuLogout = new javax.swing.JMenuItem();
        jMenuFormMenuUtama = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FORM BUAH _ NAOMI FRUITS");
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlUtama.setBackground(new java.awt.Color(233, 237, 243));
        pnlUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 128, 128));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblJudul.setBackground(new java.awt.Color(0, 128, 128));
        lblJudul.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(255, 255, 255));
        lblJudul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJudul.setText("FORM BUAH _ NAOMI FRUITS");
        lblJudul.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(lblJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 10, 340, 40));

        pnlUtama.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 970, 60));

        pnl_BtnInputan.setBackground(new java.awt.Color(233, 237, 243));
        pnl_BtnInputan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_BtnInputan.setForeground(new java.awt.Color(10, 10, 10));
        pnl_BtnInputan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblKodeBuah.setBackground(new java.awt.Color(233, 237, 243));
        lblKodeBuah.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblKodeBuah.setForeground(new java.awt.Color(10, 10, 10));
        lblKodeBuah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKodeBuah.setText("Kode Buah");
        lblKodeBuah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_BtnInputan.add(lblKodeBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 190, 20));

        lblNamaBuah.setBackground(new java.awt.Color(233, 237, 243));
        lblNamaBuah.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblNamaBuah.setForeground(new java.awt.Color(10, 10, 10));
        lblNamaBuah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNamaBuah.setText(" Nama Buah");
        lblNamaBuah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_BtnInputan.add(lblNamaBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 190, 20));

        txtKodeBuah.setBackground(new java.awt.Color(233, 237, 243));
        txtKodeBuah.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtKodeBuah.setForeground(new java.awt.Color(10, 10, 10));
        txtKodeBuah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtKodeBuah.setCaretColor(new java.awt.Color(255, 255, 255));
        txtKodeBuah.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtKodeBuah.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        pnl_BtnInputan.add(txtKodeBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 190, 40));

        txtNamaBuah.setBackground(new java.awt.Color(233, 237, 243));
        txtNamaBuah.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNamaBuah.setForeground(new java.awt.Color(10, 10, 10));
        txtNamaBuah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtNamaBuah.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtNamaBuah.addActionListener(this::txtNamaBuahActionPerformed);
        pnl_BtnInputan.add(txtNamaBuah, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 190, 40));

        pnlUtama.add(pnl_BtnInputan, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 890, 90));

        tblBuah.setBackground(new java.awt.Color(233, 237, 243));
        tblBuah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblBuah.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblBuah.setForeground(new java.awt.Color(34, 34, 34));
        tblBuah.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "kode buah", "nama buah"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBuah.setFillsViewportHeight(true);
        tblBuah.setGridColor(new java.awt.Color(228, 216, 216));
        tblBuah.setIntercellSpacing(new java.awt.Dimension(0, 20));
        tblBuah.setName(""); // NOI18N
        tblBuah.setRowHeight(34);
        tblBuah.setSelectionBackground(new java.awt.Color(233, 201, 201));
        tblBuah.setSelectionForeground(new java.awt.Color(34, 34, 34));
        tblBuah.setShowHorizontalLines(true);
        tblBuah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuahMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBuah);
        if (tblBuah.getColumnModel().getColumnCount() > 0) {
            tblBuah.getColumnModel().getColumn(0).setPreferredWidth(140);
            tblBuah.getColumnModel().getColumn(1).setPreferredWidth(440);
        }

        pnlUtama.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 890, 300));

        btnUbah.setBackground(new java.awt.Color(0, 128, 128));
        btnUbah.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnUbah.setForeground(new java.awt.Color(255, 255, 255));
        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Edit.png"))); // NOI18N
        btnUbah.setText("Edit");
        btnUbah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnUbah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbah.addActionListener(this::btnUbahActionPerformed);
        pnlUtama.add(btnUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 190, 110, 35));

        btnSimpan.setBackground(new java.awt.Color(0, 128, 128));
        btnSimpan.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Save.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(this::btnSimpanActionPerformed);
        pnlUtama.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 190, 110, 35));

        btnHapus.setBackground(new java.awt.Color(233, 237, 243));
        btnHapus.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(30, 95, 91));
        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Delete.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnHapus.addActionListener(this::btnHapusActionPerformed);
        pnlUtama.add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 190, 110, 35));

        btnReset.setBackground(new java.awt.Color(233, 237, 243));
        btnReset.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnReset.setForeground(new java.awt.Color(30, 95, 91));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Sync2.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnReset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReset.addActionListener(this::btnResetActionPerformed);
        pnlUtama.add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 190, 110, 35));

        getContentPane().add(pnlUtama, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 550));

        jMenu_Menu.setText("Menu");
        jMenu_Menu.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N

        jMenuFormKriteria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuFormKriteria.setText("Form Kriteria");
        jMenuFormKriteria.addActionListener(this::jMenuFormKriteriaActionPerformed);
        jMenu_Menu.add(jMenuFormKriteria);

        jMenuFormLogin.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuFormLogin.setText("Form Login");
        jMenuFormLogin.addActionListener(this::jMenuFormLoginActionPerformed);
        jMenu_Menu.add(jMenuFormLogin);

        jMenuFormPenilaian.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuFormPenilaian.setText("Form Penilaian");
        jMenuFormPenilaian.addActionListener(this::jMenuFormPenilaianActionPerformed);
        jMenu_Menu.add(jMenuFormPenilaian);

        jMenuBar1.add(jMenu_Menu);

        jMenuAkun.setText("Akun");
        jMenuAkun.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N

        jMenuLogout.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuLogout.setText("Logout / Keluar");
        jMenuLogout.addActionListener(this::jMenuLogoutActionPerformed);
        jMenuAkun.add(jMenuLogout);

        jMenuFormMenuUtama.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuFormMenuUtama.setText("Menu Utama");
        jMenuFormMenuUtama.addActionListener(this::jMenuFormMenuUtamaActionPerformed);
        jMenuAkun.add(jMenuFormMenuUtama);

        jMenuBar1.add(jMenuAkun);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNamaBuahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaBuahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaBuahActionPerformed

    private void tblBuahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuahMouseClicked
        int row = tblBuah.getSelectedRow();
if (row != -1) {
    txtKodeBuah.setText(model.getValueAt(row, 0).toString());
    txtNamaBuah.setText(model.getValueAt(row, 1).toString());
    txtKodeBuah.setEditable(false);
}
    }//GEN-LAST:event_tblBuahMouseClicked

    private void jMenuFormKriteriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFormKriteriaActionPerformed
    new FormKriteria().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuFormKriteriaActionPerformed

    private void jMenuFormLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFormLoginActionPerformed
    new FormLogin().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuFormLoginActionPerformed

    private void jMenuFormPenilaianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFormPenilaianActionPerformed
    new FormPenilaian().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuFormPenilaianActionPerformed

    private void jMenuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuLogoutActionPerformed
    System.exit(0);
    }//GEN-LAST:event_jMenuLogoutActionPerformed

    private void jMenuFormMenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuFormMenuUtamaActionPerformed
        new Form_MenuUtama().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuFormMenuUtamaActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetForm();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "UPDATE tb_buah_buahan SET nama_buah=? WHERE kode_buah=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtNamaBuah.getText());
            pstmt.setString(2, txtKodeBuah.getText());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Buah Berhasil Diubah!");
            loadData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Ubah Buah: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus buah ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = Koneksi.getKoneksi();
                String sql = "DELETE FROM tb_buah_buahan WHERE kode_buah=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, txtKodeBuah.getText());

                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data Buah Berhasil Dihapus!");
                loadData();
                resetForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal Hapus Buah: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "INSERT INTO tb_buah_buahan (kode_buah, nama_buah) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, txtKodeBuah.getText());
            pstmt.setString(2, txtNamaBuah.getText());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data Buah Berhasil Disimpan!");
            loadData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Simpan Buah: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

//TRIO METHOD GACOR
private void initTable() {
    model = new DefaultTableModel();
    model.addColumn("Kode Buah");
    model.addColumn("Nama Buah");
    tblBuah.setModel(model);
}
private void loadData() {
    model.setRowCount(0);
    try {
        Connection conn = Koneksi.getKoneksi();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM tb_buah_buahan");

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("kode_buah"),
                rs.getString("nama_buah")
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal Load Data Buah: " + e.getMessage());
    }
}
private void resetForm() {
    txtKodeBuah.setText("");
    txtNamaBuah.setText("");
    txtKodeBuah.setEditable(true);
    txtKodeBuah.requestFocus();
    }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FormBuah().setVisible(true));
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JMenu jMenuAkun;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuFormKriteria;
    private javax.swing.JMenuItem jMenuFormLogin;
    private javax.swing.JMenuItem jMenuFormMenuUtama;
    private javax.swing.JMenuItem jMenuFormPenilaian;
    private javax.swing.JMenuItem jMenuLogout;
    private javax.swing.JMenu jMenu_Menu;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKodeBuah;
    private javax.swing.JLabel lblNamaBuah;
    private javax.swing.JPanel pnlUtama;
    private javax.swing.JPanel pnl_BtnInputan;
    private javax.swing.JTable tblBuah;
    private javax.swing.JTextField txtKodeBuah;
    private javax.swing.JTextField txtNamaBuah;
    // End of variables declaration//GEN-END:variables
}

