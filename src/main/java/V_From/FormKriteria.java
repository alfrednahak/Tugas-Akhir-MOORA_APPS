package V_From;

import Connection.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormKriteria extends javax.swing.JFrame {

    private DefaultTableModel model;

    public FormKriteria() {
        initComponents();
        setLocationRelativeTo(null); // Tampil di tengah layar
        initTable();
        loadData();
    }

    private void initTable() {
        model = new DefaultTableModel();
        model.addColumn("Kode Kriteria");
        model.addColumn("Nama Kriteria");
        model.addColumn("type");
        model.addColumn("Bobot");
        tblKriteria.setModel(model);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tb_kriteria");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kode_kriteria"),
                    rs.getString("nama_kriteria"),
                    rs.getString("type"),
                    rs.getDouble("bobot")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Load Data: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtKode.setText("");
        txtNama.setText("");
        cbJenis.setSelectedIndex(0);
        txtBobot.setText("");
        txtKode.setEditable(true);
        txtKode.requestFocus();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_Induk = new javax.swing.JPanel();
        pnl_Inputan = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        cbJenis = new javax.swing.JComboBox<>();
        txtBobot = new javax.swing.JTextField();
        lblBobot = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKriteria = new javax.swing.JTable();
        btnUbah = new javax.swing.JButton();
        jScrollBar1 = new javax.swing.JScrollBar();
        pnlJUDUL = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu_Menu = new javax.swing.JMenu();
        jMenuBuah = new javax.swing.JMenuItem();
        jMenuPenilaian = new javax.swing.JMenuItem();
        jMenu_MenuUtama = new javax.swing.JMenuItem();
        jMenuLogin = new javax.swing.JMenuItem();
        jMenuAkun = new javax.swing.JMenu();
        jMenuLogout = new javax.swing.JMenuItem();
        jMenuUtama = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("KELOLA KRITERIA & BOBOT");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_Induk.setBackground(new java.awt.Color(233, 237, 243));
        pnl_Induk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnl_Inputan.setBackground(new java.awt.Color(233, 237, 243));
        pnl_Inputan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_Inputan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setBackground(new java.awt.Color(233, 237, 243));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Kode Kriteria");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_Inputan.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 190, 20));

        jLabel3.setBackground(new java.awt.Color(233, 237, 243));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Nama Kriteria");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_Inputan.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 190, 20));

        txtKode.setBackground(new java.awt.Color(233, 237, 243));
        txtKode.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtKode.setForeground(new java.awt.Color(10, 10, 10));
        txtKode.setAutoscrolls(false);
        txtKode.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_Inputan.add(txtKode, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 190, 40));

        txtNama.setBackground(new java.awt.Color(233, 237, 243));
        txtNama.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNama.setForeground(new java.awt.Color(10, 10, 10));
        txtNama.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_Inputan.add(txtNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 190, 40));

        cbJenis.setBackground(new java.awt.Color(233, 237, 243));
        cbJenis.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        cbJenis.setForeground(new java.awt.Color(10, 10, 10));
        cbJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Benefit", "Cost" }));
        cbJenis.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cbJenis.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pnl_Inputan.add(cbJenis, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 40, 130, 40));

        txtBobot.setBackground(new java.awt.Color(233, 237, 243));
        txtBobot.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtBobot.setForeground(new java.awt.Color(10, 10, 10));
        txtBobot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_Inputan.add(txtBobot, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 40, 100, 35));

        lblBobot.setBackground(new java.awt.Color(233, 237, 243));
        lblBobot.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblBobot.setForeground(new java.awt.Color(0, 0, 0));
        lblBobot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBobot.setText("Bobot");
        lblBobot.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl_Inputan.add(lblBobot, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 100, 20));

        pnl_Induk.add(pnl_Inputan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 930, 90));

        btnSimpan.setBackground(new java.awt.Color(0, 128, 128));
        btnSimpan.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Save.png"))); // NOI18N
        btnSimpan.setText("SIMPAN");
        btnSimpan.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSimpan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        pnl_Induk.add(btnSimpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 180, 110, 35));

        btnHapus.setBackground(new java.awt.Color(255, 255, 255));
        btnHapus.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(30, 95, 91));
        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Delete.png"))); // NOI18N
        btnHapus.setText("HAPUS");
        btnHapus.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnHapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        pnl_Induk.add(btnHapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 180, 110, 35));

        btnBatal.setBackground(new java.awt.Color(255, 255, 255));
        btnBatal.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(30, 95, 91));
        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Sync2.png"))); // NOI18N
        btnBatal.setText("RESET");
        btnBatal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnBatal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        pnl_Induk.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 180, 110, 35));

        tblKriteria.setBackground(new java.awt.Color(233, 237, 243));
        tblKriteria.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblKriteria.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblKriteria.setForeground(new java.awt.Color(10, 10, 10));
        tblKriteria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "kode kriteria", "nama kriteria", "type", "bobot"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKriteria.setFillsViewportHeight(true);
        tblKriteria.setGridColor(new java.awt.Color(221, 226, 234));
        tblKriteria.setRowHeight(25);
        tblKriteria.setSelectionBackground(new java.awt.Color(0, 51, 51));
        tblKriteria.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblKriteria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKriteriaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKriteria);
        if (tblKriteria.getColumnModel().getColumnCount() > 0) {
            tblKriteria.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblKriteria.getColumnModel().getColumn(1).setPreferredWidth(400);
            tblKriteria.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblKriteria.getColumnModel().getColumn(3).setPreferredWidth(194);
        }

        pnl_Induk.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 950, 310));

        btnUbah.setBackground(new java.awt.Color(0, 128, 128));
        btnUbah.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnUbah.setForeground(new java.awt.Color(255, 255, 255));
        btnUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Edit.png"))); // NOI18N
        btnUbah.setText("EDIT");
        btnUbah.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnUbah.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });
        pnl_Induk.add(btnUbah, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 110, 35));
        pnl_Induk.add(jScrollBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 230, 30, 300));

        pnlJUDUL.setBackground(new java.awt.Color(0, 128, 128));
        pnlJUDUL.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlJUDUL.setForeground(new java.awt.Color(255, 255, 255));
        pnlJUDUL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(0, 128, 128));
        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("KELOLA DATA KRITERIA & BOBOT");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlJUDUL.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 370, 40));

        pnl_Induk.add(pnlJUDUL, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 930, 60));

        getContentPane().add(pnl_Induk, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 550));

        jMenu_Menu.setText("Menu");
        jMenu_Menu.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N

        jMenuBuah.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuBuah.setText("Form Buah");
        jMenuBuah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuBuahActionPerformed(evt);
            }
        });
        jMenu_Menu.add(jMenuBuah);

        jMenuPenilaian.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuPenilaian.setText("Form Penilaian");
        jMenuPenilaian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPenilaianActionPerformed(evt);
            }
        });
        jMenu_Menu.add(jMenuPenilaian);

        jMenu_MenuUtama.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenu_MenuUtama.setText("Form Menu Utama");
        jMenu_MenuUtama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu_MenuUtamaActionPerformed(evt);
            }
        });
        jMenu_Menu.add(jMenu_MenuUtama);

        jMenuLogin.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuLogin.setText("FormLogin");
        jMenuLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuLoginActionPerformed(evt);
            }
        });
        jMenu_Menu.add(jMenuLogin);

        jMenuBar1.add(jMenu_Menu);

        jMenuAkun.setText("Akun");
        jMenuAkun.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N

        jMenuLogout.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuLogout.setText("Logout / Keluar");
        jMenuLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuLogoutActionPerformed(evt);
            }
        });
        jMenuAkun.add(jMenuLogout);

        jMenuUtama.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenuUtama.setText("Menu Utama");
        jMenuUtama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuUtamaActionPerformed(evt);
            }
        });
        jMenuAkun.add(jMenuUtama);

        jMenuBar1.add(jMenuAkun);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        try {
    Connection conn = Koneksi.getKoneksi();
    String sql = "INSERT INTO tb_kriteria (kode_kriteria, nama_kriteria, type, bobot) VALUES (?, ?, ?, ?)";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, txtKode.getText());
    pstmt.setString(2, txtNama.getText());
    pstmt.setString(3, cbJenis.getSelectedItem().toString());
    pstmt.setDouble(4, Double.parseDouble(txtBobot.getText()));

    pstmt.executeUpdate();
    JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
    loadData();
    resetForm();
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal Simpan: " + e.getMessage());
}
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
if (confirm == JOptionPane.YES_OPTION) {
    try {
        Connection conn = Koneksi.getKoneksi();
        String sql = "DELETE FROM tb_kriteria WHERE kode_kriteria=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, txtKode.getText());

        pstmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus!");
        loadData();
        resetForm();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal Hapus: " + e.getMessage());
    }
}
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        resetForm();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tblKriteriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKriteriaMouseClicked
        int row = tblKriteria.getSelectedRow();
if (row != -1) {
    txtKode.setText(model.getValueAt(row, 0).toString());
    txtNama.setText(model.getValueAt(row, 1).toString());
    cbJenis.setSelectedItem(model.getValueAt(row, 2).toString());
    txtBobot.setText(model.getValueAt(row, 3).toString());
    txtKode.setEditable(false);
}
    }//GEN-LAST:event_tblKriteriaMouseClicked

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
             try {
    Connection conn = Koneksi.getKoneksi();
    String sql = "UPDATE tb_kriteria SET nama_kriteria=?, type=?, bobot=? WHERE kode_kriteria=?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, txtNama.getText());
    pstmt.setString(2, cbJenis.getSelectedItem().toString());
    pstmt.setDouble(3, Double.parseDouble(txtBobot.getText()));
    pstmt.setString(4, txtKode.getText());

    pstmt.executeUpdate();
    JOptionPane.showMessageDialog(this, "Data Berhasil Diubah!");
    loadData();
    resetForm();
} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal Ubah: " + e.getMessage());
}
    }//GEN-LAST:event_btnUbahActionPerformed

    private void jMenuLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuLoginActionPerformed
    new FormLogin().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuLoginActionPerformed

    private void jMenu_MenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu_MenuUtamaActionPerformed
    new Form_MenuUtama().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenu_MenuUtamaActionPerformed

    private void jMenuPenilaianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPenilaianActionPerformed
    new FormPenilaian().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuPenilaianActionPerformed

    private void jMenuBuahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuBuahActionPerformed
    new FormBuah().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuBuahActionPerformed

    private void jMenuLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuLogoutActionPerformed
    System.exit(0);
    }//GEN-LAST:event_jMenuLogoutActionPerformed

    private void jMenuUtamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuUtamaActionPerformed
    new Form_MenuUtama().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jMenuUtamaActionPerformed


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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormKriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormKriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormKriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormKriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormKriteria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cbJenis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenuAkun;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuBuah;
    private javax.swing.JMenuItem jMenuLogin;
    private javax.swing.JMenuItem jMenuLogout;
    private javax.swing.JMenuItem jMenuPenilaian;
    private javax.swing.JMenuItem jMenuUtama;
    private javax.swing.JMenu jMenu_Menu;
    private javax.swing.JMenuItem jMenu_MenuUtama;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBobot;
    private javax.swing.JPanel pnlJUDUL;
    private javax.swing.JPanel pnl_Induk;
    private javax.swing.JPanel pnl_Inputan;
    private javax.swing.JTable tblKriteria;
    private javax.swing.JTextField txtBobot;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
