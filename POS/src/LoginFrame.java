import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 * Layar login kasir (Java Swing).
 * INHERITANCE: LoginFrame extends JFrame.
 * Hasil login berupa objek Kasir yang diberikan ke PosGUI.
 */
public class LoginFrame extends JFrame {

    private static final Color BIRU_TUA = new Color(22, 63, 128);
    private static final Color BIRU_MUDA = new Color(56, 128, 215);
    private static final Color BIRU_TOMBOL = new Color(33, 90, 160);
    private static final Color ABU_TEKS = new Color(110, 118, 132);
    private static final Color MERAH = new Color(200, 50, 50);

    // Daftar akun disimpan di dalam kode (tanpa database)
    private final ArrayList<Akun> daftarAkun = new ArrayList<>();

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbShift;
    private JLabel lblError;

    public LoginFrame() {
        super("Login Kasir - Point of Sales");
        isiDataAwal();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new GridLayout(1, 2));
        add(buatPanelKiri());
        add(buatPanelKanan());
    }

    private void isiDataAwal() {
        daftarAkun.add(new Akun("admin", "admin123", "Admin"));
        daftarAkun.add(new Akun("aria", "kasir123", "Kasir"));
    }

    // ------------------------------------------------ Panel kiri (branding)
    private JPanel buatPanelKiri() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                g2.setPaint(new GradientPaint(0, 0, BIRU_TUA, w, h, BIRU_MUDA));
                g2.fillRect(0, 0, w, h);

                // lingkaran dekoratif transparan
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fill(new Ellipse2D.Double(-80, h - 220, 300, 300));
                g2.setColor(new Color(255, 255, 255, 18));
                g2.fill(new Ellipse2D.Double(w - 170, -90, 280, 280));
                g2.setColor(new Color(255, 255, 255, 14));
                g2.fill(new Ellipse2D.Double(w - 120, h - 150, 170, 170));

                g2.dispose();
            }
        };

        // ikon keranjang digambar manual
        JComponent ikon = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillOval(0, 0, w, h);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // gagang
                g2.drawLine(24, 28, 36, 28);
                g2.drawLine(36, 28, 44, 62);
                // badan keranjang
                g2.drawLine(44, 62, 92, 62);
                g2.drawLine(38, 38, 100, 38);
                g2.drawLine(100, 38, 92, 62);
                // roda
                g2.fillOval(48, 76, 14, 14);
                g2.fillOval(78, 76, 14, 14);
                g2.dispose();
            }
        };
        ikon.setPreferredSize(new Dimension(120, 120));

        JLabel judul = new JLabel("POINT OF SALES");
        judul.setFont(new Font("SansSerif", Font.BOLD, 30));
        judul.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Sistem Kasir Sederhana");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sub.setForeground(new Color(215, 230, 250));

        JLabel info = new JLabel("<html><div style='text-align:center'>Kelola produk, transaksi,<br>dan pembayaran dalam satu aplikasi.</div></html>");
        info.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.setForeground(new Color(190, 212, 242));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 18, 0);
        panel.add(ikon, c);
        c.gridy = 1;
        c.insets = new Insets(0, 0, 4, 0);
        panel.add(judul, c);
        c.gridy = 2;
        c.insets = new Insets(0, 0, 22, 0);
        panel.add(sub, c);
        c.gridy = 3;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(info, c);
        return panel;
    }

    // ------------------------------------------------- Panel kanan (form)
    private JPanel buatPanelKanan() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel judul = new JLabel("Selamat Datang");
        judul.setFont(new Font("SansSerif", Font.BOLD, 28));
        judul.setForeground(new Color(30, 36, 50));

        JLabel sub = new JLabel("Silakan masuk untuk memulai shift kasir");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(ABU_TEKS);

        txtUsername = new JTextField();
        styleInput(txtUsername);

        txtPassword = new JPasswordField();
        styleInput(txtPassword);

        JCheckBox chkLihat = new JCheckBox("Tampilkan password");
        chkLihat.setFont(new Font("SansSerif", Font.PLAIN, 12));
        chkLihat.setForeground(ABU_TEKS);
        chkLihat.setBackground(Color.WHITE);
        chkLihat.setFocusPainted(false);
        char bawaan = txtPassword.getEchoChar();
        chkLihat.addActionListener(e -> txtPassword.setEchoChar(chkLihat.isSelected() ? (char) 0 : bawaan));

        cbShift = new JComboBox<>(new String[]{"Pagi", "Siang", "Malam"});
        cbShift.setFont(new Font("SansSerif", Font.PLAIN, 16));
        cbShift.setBackground(Color.WHITE);
        cbShift.setPreferredSize(new Dimension(0, 46));

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblError.setForeground(MERAH);

        TombolBulat btnMasuk = new TombolBulat("MASUK");
        btnMasuk.addActionListener(e -> login());
        getRootPane().setDefaultButton(btnMasuk);     // tekan Enter = login

        JLabel footer = new JLabel("UTS PBO RIfki Naufal Aria");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(new Color(160, 166, 178));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        int y = 0;
        c.gridy = y++; c.insets = new Insets(0, 0, 4, 0);   panel.add(judul, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 24, 0);  panel.add(sub, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 6, 0);   panel.add(labelForm("Username"), c);
        c.gridy = y++; c.insets = new Insets(0, 0, 14, 0);  panel.add(txtUsername, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 6, 0);   panel.add(labelForm("Password"), c);
        c.gridy = y++; c.insets = new Insets(0, 0, 4, 0);   panel.add(txtPassword, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 14, 0);  panel.add(chkLihat, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 6, 0);   panel.add(labelForm("Shift Kerja"), c);
        c.gridy = y++; c.insets = new Insets(0, 0, 8, 0);   panel.add(cbShift, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 14, 0);  panel.add(lblError, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 24, 0);  panel.add(btnMasuk, c);
        c.gridy = y++; c.insets = new Insets(0, 0, 0, 0);   panel.add(footer, c);
        return panel;
    }

    private JLabel labelForm(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(new Color(60, 68, 84));
        return l;
    }

    private void styleInput(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 16));
        f.setPreferredSize(new Dimension(0, 46));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 206, 218), 1, true),
                new EmptyBorder(6, 12, 6, 12)));
    }

    // -------------------------------------------------------------- Aksi
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username dan password harus diisi.");
            return;
        }

        Akun akun = null;
        int nomor = 0;
        for (int i = 0; i < daftarAkun.size(); i++) {
            if (daftarAkun.get(i).cocok(username, password)) {
                akun = daftarAkun.get(i);
                nomor = i + 1;
                break;
            }
        }

        if (akun == null) {
            lblError.setText("Username atau password salah.");
            txtPassword.setText("");
            txtPassword.requestFocus();
            return;
        }

        String shift = (String) cbShift.getSelectedItem();

        // Objek Kasir dibuat dari akun yang berhasil login lalu diberikan ke PosGUI
        Kasir kasir = new Kasir(String.format("K-%03d", nomor), akun.getUsername(), shift);
        new PosGUI(kasir).setVisible(true);
        dispose();
    }

    // ------------------------------------------- Tombol bulat (inner class)
    private static class TombolBulat extends JButton {
        private boolean hover = false;

        TombolBulat(String teks) {
            super(teks);
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(0, 50));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? BIRU_MUDA : BIRU_TOMBOL);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}