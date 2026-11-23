package V_From;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 * Panel dengan sudut membulat, bayangan tipis, dan opsi gradasi.
 * Dipakai ulang untuk kartu ringkasan, grafik, Top 5, donat, dan kalender.
 *
 * Cara pakai (di kode):
 *   PanelBulat p = new PanelBulat();
 *   p.setRadius(16);                                  // sudut membulat
 *   p.setBackground(Color.WHITE);                     // warna solid
 *   p.setGradasi(new Color(11,138,132),
 *                new Color(10,111,143), true);        // gradasi diagonal
 */
public class PanelBulat extends JPanel {

    private int radius = 16;
    private Color gradasiAtas = null;     // null = pakai warna solid
    private Color gradasiBawah = null;
    private boolean diagonal = false;
    private boolean bayangan = true;
    private Color warnaGaris = new Color(227, 234, 238);

    private static final int TEBAL_BAYANGAN = 3;

    public PanelBulat() {
        setOpaque(false);                 // WAJIB: agar sudut tidak terlihat kotak
        setBackground(Color.WHITE);
    }

    // ---------- pengaturan ----------
    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    /** Aktifkan gradasi. diagonal=true -> miring 135 derajat, false -> atas ke bawah. */
    public void setGradasi(Color atas, Color bawah, boolean diagonal) {
        this.gradasiAtas = atas;
        this.gradasiBawah = bawah;
        this.diagonal = diagonal;
        repaint();
    }

    /** Matikan gradasi (kembali ke warna solid dari setBackground). */
    public void hapusGradasi() {
        this.gradasiAtas = null;
        this.gradasiBawah = null;
        repaint();
    }

    public void setBayangan(boolean bayangan) {
        this.bayangan = bayangan;
        repaint();
    }

    /** Warna garis tepi tipis. Kirim null untuk tanpa garis. */
    public void setWarnaGaris(Color warnaGaris) {
        this.warnaGaris = warnaGaris;
        repaint();
    }

    // ---------- menggambar ----------
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        int s = bayangan ? TEBAL_BAYANGAN : 0;
        int lebar = getWidth() - s;
        int tinggi = getHeight() - s;

        // 1. bayangan (beberapa lapis transparan, bergeser ke kanan-bawah)
        for (int i = 1; i <= s; i++) {
            g2.setColor(new Color(0, 0, 0, 14));
            g2.fill(new RoundRectangle2D.Float(i, i, lebar, tinggi, radius, radius));
        }

        // 2. badan kartu: gradasi atau solid
        if (gradasiAtas != null && gradasiBawah != null) {
            GradientPaint gp = diagonal
                ? new GradientPaint(0, 0, gradasiAtas, lebar, tinggi, gradasiBawah)
                : new GradientPaint(0, 0, gradasiAtas, 0, tinggi, gradasiBawah);
            g2.setPaint(gp);
        } else {
            g2.setColor(getBackground());
        }
        g2.fill(new RoundRectangle2D.Float(0, 0, lebar, tinggi, radius, radius));

        // 3. garis tepi tipis (hanya untuk kartu solid)
        if (warnaGaris != null && gradasiAtas == null) {
            g2.setColor(warnaGaris);
            g2.draw(new RoundRectangle2D.Float(0, 0, lebar - 1, tinggi - 1, radius, radius));
        }

        g2.dispose();
    }
}