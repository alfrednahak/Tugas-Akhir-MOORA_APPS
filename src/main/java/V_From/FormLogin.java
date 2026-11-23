package V_From;

import Connection.Koneksi;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.Frame;
import javax.swing.JFrame;

public class FormLogin extends javax.swing.JFrame {
int sx, sy;
public FormLogin() {
initComponents();

    // =================================
    // 2. MENGATUR UKURAN FORM
    // =================================
    // Mengembalikan JFrame ke ukuran normal
    setExtendedState(JFrame.NORMAL);
    // Ukuran desain utama
    jPanelCard.setPreferredSize(
        new Dimension(1100, 550)
    );
    // Menyesuaikan ukuran JFrame dengan komponen
    pack();
    // Mencegah ukuran jendela berubah
    setResizable(false);
    // =================================
    // 3. POSISI FORM DI TENGAH LAYAR
    // =================================
    setLocationRelativeTo(null);
    // =================================
    // 4. MENGATUR BACKGROUND
    // =================================
    getContentPane().setBackground(
        new java.awt.Color(20, 24, 33)
    );
    pnlJudul.setBackground(
        new java.awt.Color(28, 33, 43)
    );
    pnlM.setBackground(
        new java.awt.Color(35, 41, 52)
    );
    pnlFruits.setBackground(
        new java.awt.Color(20, 24, 33)
    );
    // =================================
    // 5. MEMBERSIHKAN PASSWORD
    // =================================
    jPassword.setText("");
    // =================================
    // 6. FOKUS KE USERNAME
    // =================================
    javax.swing.SwingUtilities.invokeLater(() -> {
        txtUsername.requestFocusInWindow();
    });
        txtUsername.addActionListener(e -> {
        jPassword.requestFocusInWindow();
    });
        jPassword.addActionListener(e -> {
        btnLogin.doClick();
    });    
}
    private void resetForm(){
        txtUsername.setText("");
        jPassword.setText("");
 }
    
@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelCard = new javax.swing.JPanel();
        pnlJudul = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblLogoM = new javax.swing.JLabel();
        pnlM = new javax.swing.JPanel();
        btnLogin = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblLock = new javax.swing.JLabel();
        jPassword = new javax.swing.JPasswordField();
        lblPeople = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblKELUAR = new javax.swing.JLabel();
        lblMINIMIZE = new javax.swing.JLabel();
        lblFruits = new javax.swing.JLabel();
        pnlFruits = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanelCard.setBackground(new java.awt.Color(20, 24, 33));
        jPanelCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlJudul.setBackground(new java.awt.Color(0, 51, 51));
        pnlJudul.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlJudul.setForeground(new java.awt.Color(153, 153, 153));
        pnlJudul.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        pnlJudul.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("LOGIN SISTEM");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setIconTextGap(8);
        pnlJudul.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("SPK MOORA");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setIconTextGap(8);
        pnlJudul.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, -1, -1));

        jPanelCard.add(pnlJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 340, -1));

        lblLogoM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogoM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/img_MLogin.png"))); // NOI18N
        lblLogoM.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanelCard.add(lblLogoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 540, 550));

        pnlM.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelCard.add(pnlM, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 540, 550));

        btnLogin.setBackground(new java.awt.Color(26, 122, 110));
        btnLogin.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Login.png"))); // NOI18N
        btnLogin.setText("LOGIN");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        jPanelCard.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 300, -1, -1));

        btnCancel.setBackground(new java.awt.Color(26, 122, 110));
        btnCancel.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Cancel.png"))); // NOI18N
        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanelCard.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 300, -1, -1));

        lblUsername.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblUsername.setText("Username");
        jPanelCard.add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 120, -1, -1));

        lblPassword.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(255, 255, 255));
        lblPassword.setText("Password");
        jPanelCard.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 220, -1, -1));

        lblLock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Lock.png"))); // NOI18N
        jPanelCard.add(lblLock, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 250, -1, 20));

        jPassword.setBackground(new java.awt.Color(0, 0, 0));
        jPassword.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPassword.setForeground(new java.awt.Color(255, 255, 255));
        jPassword.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jPassword.setText("jPasswordField1");
        jPassword.setCaretColor(new java.awt.Color(255, 255, 255));
        jPassword.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPasswordMouseEntered(evt);
            }
        });
        jPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordKeyPressed(evt);
            }
        });
        jPanelCard.add(jPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 240, 230, 40));

        lblPeople.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_Profile.png"))); // NOI18N
        jPanelCard.add(lblPeople, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 150, 30, 20));

        txtUsername.setBackground(new java.awt.Color(0, 0, 0));
        txtUsername.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtUsername.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsername.setCaretColor(new java.awt.Color(0, 0, 153));
        txtUsername.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        jPanelCard.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 140, 230, 40));

        lblKELUAR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_X.png"))); // NOI18N
        lblKELUAR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblKELUARMouseClicked(evt);
            }
        });
        jPanelCard.add(lblKELUAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 10, -1, -1));

        lblMINIMIZE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icon_MinMax.png"))); // NOI18N
        lblMINIMIZE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblMINIMIZEMouseClicked(evt);
            }
        });
        jPanelCard.add(lblMINIMIZE, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 10, -1, -1));

        lblFruits.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/img_ModernFruits.png"))); // NOI18N
        jPanelCard.add(lblFruits, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 540, -1));

        pnlFruits.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanelCard.add(pnlFruits, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 540, 550));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanelCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        sx = evt.getX();
        sy = evt.getY();
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x  - sx, y - sy);
    }//GEN-LAST:event_formMouseDragged

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int pilihan = JOptionPane.showConfirmDialog(
        this,
        "Apakah Anda yakin ingin keluar dari aplikasi?",
        "Konfirmasi Keluar",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    if (pilihan == JOptionPane.YES_OPTION) {
        System.exit(0);
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
    // =================================
    // 1. MENGAMBIL INPUT USER
   // =================================
    String username = txtUsername.getText().trim();
    // txtPassword saat ini masih JTextField
    String password = jPassword.getText();
    
    // =================================
    // 2. VALIDASI INPUT KOSONG
    // =================================
    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "Username dan Password tidak boleh kosong!",
            "Peringatan",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // =================================
    // 3. MENGAMBIL KONEKSI DATABASE
    // =================================
    Connection conn = Koneksi.getKoneksi();
    if (conn == null) {
        JOptionPane.showMessageDialog(
            this,
            "Koneksi database gagal. Periksa MySQL!",
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }
    // =================================
    // 4. QUERY LOGIN KE TABEL TB_USER
    // =================================
    String sql =
        "SELECT id_user, username, nama_lengkap, level " +
        "FROM tb_user " +
        "WHERE username = ? AND password = ? " +
        "LIMIT 1";
    try (
        PreparedStatement pst =
            conn.prepareStatement(sql)
    ) {
        // Memasukkan username dan password
        pst.setString(1, username);
        pst.setString(2, password);
        // Menjalankan query
        try (ResultSet rs = pst.executeQuery()) {
            // =================================
            // 5. JIKA LOGIN BERHASIL
            // =================================
            if (rs.next()) {
                String namaLengkap =
                    rs.getString("nama_lengkap");
                // Jika nama lengkap kosong,
                // gunakan username sebagai pengganti.
                if (namaLengkap == null ||
                    namaLengkap.trim().isEmpty()) {
                    namaLengkap = username;
                }
                JOptionPane.showMessageDialog(
                    this,
                    "Login Berhasil!\nSelamat Datang, "
                    + namaLengkap,
                    "Login Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
                );
                // =================================
                // 6. BUKA DASHBOARD UTAMA
                // =================================
                Form_MenuUtama dashboard =
                    new Form_MenuUtama();
                dashboard.setLocationRelativeTo(null);
                dashboard.setVisible(true);

                // =================================
                // 7. TUTUP FORM LOGIN
                // =================================
                this.dispose();
            } else {
                // =================================
                // 8. JIKA LOGIN GAGAL
                // =================================
                JOptionPane.showMessageDialog(
                    this,
                    "Username atau Password salah!\n"
                    + "Silakan periksa kembali.",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE
                );

                jPassword.setText("");
                jPassword.requestFocusInWindow();
            }
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
            this,
            "Terjadi kesalahan saat mengakses database:\n"
            + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void lblKELUARMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblKELUARMouseClicked
    this.dispose();
    }//GEN-LAST:event_lblKELUARMouseClicked

    private void lblMINIMIZEMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblMINIMIZEMouseClicked
// Meminimalkan jendela ke taskbar Windows
    this.setState(Frame.ICONIFIED);
    }//GEN-LAST:event_lblMINIMIZEMouseClicked

    private void jPasswordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPasswordMouseEntered

    }//GEN-LAST:event_jPasswordMouseEntered

    private void jPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordKeyPressed

    }//GEN-LAST:event_jPasswordKeyPressed
        public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
          java.util.logging.Logger.getLogger(FormLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanelCard;
    private javax.swing.JPasswordField jPassword;
    private javax.swing.JLabel lblFruits;
    private javax.swing.JLabel lblKELUAR;
    private javax.swing.JLabel lblLock;
    private javax.swing.JLabel lblLogoM;
    private javax.swing.JLabel lblMINIMIZE;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPeople;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlFruits;
    private javax.swing.JPanel pnlJudul;
    private javax.swing.JPanel pnlM;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
