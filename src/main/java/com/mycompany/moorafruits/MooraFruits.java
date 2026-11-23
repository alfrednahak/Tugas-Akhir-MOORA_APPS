package com.mycompany.moorafruits;
    import V_From.FormLogin;
        import com.formdev.flatlaf.FlatLightLaf;
            import javax.swing.SwingUtilities;
                public class MooraFruits {
                    public static void main(String[] args) {
                // Aktifkan tema FlatLaf sebelum form dibuat
            FlatLightLaf.setup();
       // Buka halaman Login
   SwingUtilities.invokeLater(() -> {
new FormLogin().setVisible(true);
});
    }
        }