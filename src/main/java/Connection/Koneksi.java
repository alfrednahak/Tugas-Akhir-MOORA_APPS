//-->> Fredrius Nahak 202143502239
package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    private static Connection koneksi;

public static Connection getKoneksi() {
    try {
        if (koneksi == null || koneksi.isClosed()) {
            // Pastikan port di bawah sama dengan port MySQL di XAMPP
            String url = "jdbc:mysql://localhost:3306/db_spk_fruits?useSSL=false&allowPublicKeyRetrieval=true";
            String user = "root";
            String pass = ""; // kosong untuk XAMPP bawaan
 
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            koneksi = DriverManager.getConnection(url, user, pass);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Koneksi Database Gagal: " + e.getMessage());
    }
    return koneksi;
}      
}