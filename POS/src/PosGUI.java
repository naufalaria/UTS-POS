import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * TAMPILAN GUI (Java Swing) aplikasi Point of Sales.
 * INHERITANCE: PosGUI extends JFrame (mewarisi class jendela bawaan Java).
 * Semua logika bisnis tetap memakai class OOP: Produk, Kasir, Transaksi, Pembayaran.
 */
public class PosGUI extends JFrame {

    // ---------------------------------------------------------- WARNA & FONT
    private static final Color BIRU = new Color(33, 90, 160);
    private static final Color BIRU_TUA = new Color(22, 63, 128);
    private static final Color BIRU_MUDA = new Color(56, 128, 215);
    private static final Color HIJAU = new Color(28, 140, 88);
    private static final Color MERAH = new Color(200, 55, 55);
    private static final Color LATAR = new Color(241, 244, 250);
    private static final Color TEKS = new Color(30, 36, 50);
    private static final Color TEKS_ABU = new Color(110, 118, 132);
    private static final Color PILIH = new Color(219, 234, 254);

    private static Font f(int gaya, int ukuran) {
        return new Font("Segoe UI", gaya, ukuran);
    }

    // ----------------------------------------------------------------- DATA
    // POLYMORPHISM: satu list bertipe Produk berisi berbagai subclass
    private final ArrayList<Produk> daftarProduk = new ArrayList<>();
    private final Kasir kasir;
    private Transaksi transaksi;

    private DefaultTableModel modelProduk;
    private DefaultTableModel modelKeranjang;
    private JTable tabelProduk;
    private JTable tabelKeranjang;
    private JSpinner spJumlah;
    private JComboBox<Pembayaran> cbMetode;
    private JTextField txtUang;
    private JLabel lblTotal, lblBiaya, lblTagihan, lblKembalian, lblJam;

    public PosGUI(Kasir kasir) {
        super("Point of Sales - Toko Serba Ada");
        this.kasir = kasir;
        this.transaksi = new Transaksi(kasir);

        isiDataAwal();
        buatTampilan();
        refreshProduk();
        refreshKeranjang();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void isiDataAwal() {
        daftarProduk.add(new ProdukMakanan("MKN01", "Roti Tawar", 15000, 20, "30-12-2026"));
        daftarProduk.add(new ProdukMakanan("MKN02", "Mie Instan", 3500, 100, "01-06-2027"));
        daftarProduk.add(new ProdukMinuman("MNM01", "Air Mineral", 4000, 50, 600));
        daftarProduk.add(new ProdukMinuman("MNM02", "Teh Botol", 6000, 40, 450));
        daftarProduk.add(new ProdukElektronik("ELK01", "Charger HP", 75000, 10, 12));
        daftarProduk.add(new ProdukElektronik("ELK02", "Earphone", 50000, 15, 6));
    }

    // ------------------------------------------------------------ TAMPILAN
    private void buatTampilan() {
        getContentPane().setBackground(LATAR);
        setLayout(new BorderLayout());
        add(buatHeader(), BorderLayout.NORTH);

        JPanel isi = new JPanel(new BorderLayout(16, 16));
        isi.setOpaque(false);
        isi.setBorder(new EmptyBorder(16, 16, 16, 16));
        isi.add(buatPanelProduk(), BorderLayout.CENTER);
        isi.add(buatPanelKeranjang(), BorderLayout.EAST);
        add(isi, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------- Header atas
    private JPanel buatHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, BIRU_TUA, getWidth(), 0, BIRU_MUDA));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 22));
                g2.fill(new Ellipse2D.Double(getWidth() - 260, -80, 220, 220));
                g2.dispose();
            }
        };
        header.setBorder(new EmptyBorder(12, 22, 12, 22));

        // kiri: judul
        JPanel kiri = new JPanel(new GridLayout(2, 1));
        kiri.setOpaque(false);
        JLabel judul = new JLabel("POINT OF SALES");
        judul.setFont(f(Font.BOLD, 24));
        judul.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Toko Serba Ada");
        sub.setFont(f(Font.PLAIN, 13));
        sub.setForeground(new Color(205, 224, 250));
        kiri.add(judul);
        kiri.add(sub);

        // kanan: jam + info kasir
        lblJam = new JLabel();
        lblJam.setFont(f(Font.PLAIN, 14));
        lblJam.setForeground(new Color(225, 236, 252));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(
                "EEEE, dd MMMM yyyy   HH:mm:ss", Locale.forLanguageTag("id-ID"));
        lblJam.setText(LocalDateTime.now().format(fmt));
        new javax.swing.Timer(1000, e -> lblJam.setText(LocalDateTime.now().format(fmt))).start();

        final String inisial = kasir.getNama().substring(0, 1).toUpperCase();
        JComponent avatar = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 235));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(BIRU);
                g2.setFont(f(Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(inisial, (getWidth() - fm.stringWidth(inisial)) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(46, 46));

        JPanel teksKasir = new JPanel(new GridLayout(2, 1));
        teksKasir.setOpaque(false);
        JLabel nama = new JLabel(kasir.getNama());
        nama.setFont(f(Font.BOLD, 15));
        nama.setForeground(Color.WHITE);
        JLabel peran = new JLabel(kasir.getPeran() + "  |  Shift " + kasir.getShift());
        peran.setFont(f(Font.PLAIN, 12));
        peran.setForeground(new Color(205, 224, 250));
        teksKasir.add(nama);
        teksKasir.add(peran);

        JPanel kanan = new JPanel(new GridBagLayout());
        kanan.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 26);
        kanan.add(lblJam, c);
        c.insets = new Insets(0, 0, 0, 12);
        kanan.add(avatar, c);
        c.insets = new Insets(0, 0, 0, 0);
        kanan.add(teksKasir, c);

        header.add(kiri, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);
        return header;
    }

    // ------------------------------------------------- Panel produk (kiri)
    private JPanel buatPanelProduk() {
        Kartu kartu = new Kartu(Color.WHITE, 18);
        kartu.setLayout(new BorderLayout(0, 12));
        kartu.setBorder(new EmptyBorder(16, 18, 18, 18));
        kartu.add(judulKartu("Daftar Produk", "Pilih produk, atur jumlah, lalu tambahkan ke keranjang"),
                BorderLayout.NORTH);

        modelProduk = new DefaultTableModel(
                new String[]{"Kode", "Nama", "Kategori", "Harga", "Diskon", "Stok", "Info"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelProduk = new JTable(modelProduk);
        int[] rata = {SwingConstants.LEFT, SwingConstants.LEFT, SwingConstants.LEFT,
                SwingConstants.RIGHT, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.LEFT};
        gayaTabel(tabelProduk, rata, true);
        int[] lebar = {75, 120, 100, 100, 80, 65, 135};
        for (int i = 0; i < lebar.length; i++) {
            tabelProduk.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }
        kartu.add(bungkusTabel(tabelProduk), BorderLayout.CENTER);

        JPanel aksi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        aksi.setOpaque(false);
        JLabel lblJumlah = new JLabel("Jumlah");
        lblJumlah.setFont(f(Font.BOLD, 13));
        lblJumlah.setForeground(TEKS_ABU);
        spJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spJumlah.setPreferredSize(new Dimension(84, 38));
        spJumlah.setFont(f(Font.PLAIN, 15));
        Tombol btnTambah = new Tombol("+  Tambah ke Keranjang", BIRU, Color.WHITE);
        btnTambah.setPreferredSize(new Dimension(240, 38));
        btnTambah.addActionListener(e -> tambahKeKeranjang());
        Tombol btnStok = new Tombol("Tambah Stok", new Color(226, 231, 241), TEKS);
        btnStok.setPreferredSize(new Dimension(150, 38));
        btnStok.addActionListener(e -> tambahStok());
        aksi.add(lblJumlah);
        aksi.add(spJumlah);
        aksi.add(btnTambah);
        aksi.add(btnStok);
        kartu.add(aksi, BorderLayout.SOUTH);
        return kartu;
    }

    // ---------------------------------------------- Panel keranjang (kanan)
    private JPanel buatPanelKeranjang() {
        Kartu kartu = new Kartu(Color.WHITE, 18);
        kartu.setLayout(new BorderLayout(0, 12));
        kartu.setBorder(new EmptyBorder(16, 18, 18, 18));
        kartu.setPreferredSize(new Dimension(520, 0));
        kartu.add(judulKartu("Keranjang Belanja", "Rincian item yang akan dibayar"), BorderLayout.NORTH);

        modelKeranjang = new DefaultTableModel(
                new String[]{"Nama", "Qty", "Harga", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelKeranjang = new JTable(modelKeranjang) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getRowCount() == 0) {                    // tulisan saat keranjang kosong
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setFont(f(Font.PLAIN, 14));
                    g2.setColor(new Color(150, 158, 172));
                    String s = "Keranjang masih kosong";
                    Rectangle r = getVisibleRect();
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(s, r.x + (r.width - fm.stringWidth(s)) / 2, r.y + r.height / 2);
                    g2.dispose();
                }
            }
        };
        int[] rata = {SwingConstants.LEFT, SwingConstants.CENTER, SwingConstants.RIGHT, SwingConstants.RIGHT};
        gayaTabel(tabelKeranjang, rata, false);
        tabelKeranjang.setRowHeight(32);
        int[] lebar = {200, 55, 120, 135};
        for (int i = 0; i < lebar.length; i++) {
            tabelKeranjang.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);
        }
        kartu.add(bungkusTabel(tabelKeranjang), BorderLayout.CENTER);

        // ---- ringkasan pembayaran
        lblTotal = new JLabel("Rp0", SwingConstants.RIGHT);
        lblTotal.setFont(f(Font.BOLD, 14));
        lblTotal.setForeground(TEKS);
        lblBiaya = new JLabel("Rp0", SwingConstants.RIGHT);
        lblBiaya.setFont(f(Font.PLAIN, 14));
        lblBiaya.setForeground(TEKS);
        lblTagihan = new JLabel("Rp0", SwingConstants.RIGHT);
        lblTagihan.setFont(f(Font.BOLD, 24));
        lblTagihan.setForeground(Color.WHITE);
        lblKembalian = new JLabel("-", SwingConstants.RIGHT);
        lblKembalian.setFont(f(Font.BOLD, 15));
        lblKembalian.setForeground(TEKS_ABU);

        // Combo berisi objek-objek yang mengimplementasi interface Pembayaran
        cbMetode = new JComboBox<>(new Pembayaran[]{
                new PembayaranTunai(), new PembayaranDebit(), new PembayaranEwallet()});
        cbMetode.setFont(f(Font.PLAIN, 14));
        cbMetode.setBackground(Color.WHITE);
        cbMetode.setPreferredSize(new Dimension(220, 32));
        cbMetode.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, value, index, sel, focus);
                setText(((Pembayaran) value).getNamaMetode());   // polymorphism lewat interface
                setBorder(new EmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
        cbMetode.addActionListener(e -> updateRingkasan());

        txtUang = new JTextField();
        txtUang.setFont(f(Font.PLAIN, 15));
        txtUang.setHorizontalAlignment(JTextField.RIGHT);
        txtUang.setPreferredSize(new Dimension(220, 32));
        txtUang.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 206, 218), 1, true), new EmptyBorder(2, 10, 2, 10)));
        txtUang.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { updateRingkasan(); }
        });

        Kartu boxTotal = new Kartu(BIRU, 14);
        boxTotal.setLayout(new BorderLayout());
        boxTotal.setBorder(new EmptyBorder(6, 16, 9, 16));
        JLabel lblT = new JLabel("TOTAL TAGIHAN");
        lblT.setFont(f(Font.BOLD, 12));
        lblT.setForeground(new Color(200, 220, 248));
        boxTotal.add(lblT, BorderLayout.WEST);
        boxTotal.add(lblTagihan, BorderLayout.EAST);

        Tombol btnBayar = new Tombol("BAYAR", HIJAU, Color.WHITE);
        btnBayar.setFont(f(Font.BOLD, 17));
        btnBayar.addActionListener(e -> bayar());
        Tombol btnHapus = new Tombol("Hapus Item", new Color(226, 231, 241), TEKS);
        btnHapus.addActionListener(e -> hapusItem());
        Tombol btnBatal = new Tombol("Batal", new Color(252, 226, 226), MERAH);
        btnBatal.addActionListener(e -> batalTransaksi());
        JPanel tombolKecil = new JPanel(new GridLayout(1, 2, 8, 0));
        tombolKecil.setOpaque(false);
        tombolKecil.add(btnHapus);
        tombolKecil.add(btnBatal);

        JPanel ringkas = new JPanel();
        ringkas.setOpaque(false);
        ringkas.setLayout(new BoxLayout(ringkas, BoxLayout.Y_AXIS));
        ringkas.add(baris("Total belanja", lblTotal));
        ringkas.add(Box.createVerticalStrut(3));
        ringkas.add(baris("Biaya admin", lblBiaya));
        ringkas.add(Box.createVerticalStrut(6));
        ringkas.add(baris("Metode pembayaran", cbMetode));
        ringkas.add(Box.createVerticalStrut(5));
        ringkas.add(baris("Uang diterima (Rp)", txtUang));
        ringkas.add(Box.createVerticalStrut(8));
        ringkas.add(boxTotal);
        ringkas.add(Box.createVerticalStrut(5));
        ringkas.add(baris("Kembalian", lblKembalian));
        ringkas.add(Box.createVerticalStrut(8));
        ringkas.add(bungkus(btnBayar, 44));
        ringkas.add(Box.createVerticalStrut(6));
        ringkas.add(bungkus(tombolKecil, 36));

        kartu.add(ringkas, BorderLayout.SOUTH);
        return kartu;
    }

    // ----------------------------------------------------- Helper tampilan
    private JPanel judulKartu(String judul, String sub) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setOpaque(false);
        JLabel a = new JLabel(judul);
        a.setFont(f(Font.BOLD, 19));
        a.setForeground(TEKS);
        JLabel b = new JLabel(sub);
        b.setFont(f(Font.PLAIN, 12));
        b.setForeground(TEKS_ABU);
        p.add(a);
        p.add(b);
        return p;
    }

    private JPanel baris(String label, JComponent nilai) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(f(Font.PLAIN, 14));
        l.setForeground(TEKS_ABU);
        p.add(l, BorderLayout.WEST);
        p.add(nilai, BorderLayout.EAST);
        return p;
    }

    private JPanel bungkus(JComponent isi, int tinggi) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(isi, BorderLayout.CENTER);
        p.setPreferredSize(new Dimension(0, tinggi));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, tinggi));
        return p;
    }

    private JScrollPane bungkusTabel(JTable tabel) {
        JScrollPane sp = new JScrollPane(tabel);
        sp.setBorder(new LineBorder(new Color(228, 232, 240)));
        sp.getViewport().setBackground(Color.WHITE);
        return sp;
    }

    private void gayaTabel(JTable t, int[] rata, boolean tabelProduk) {
        t.setRowHeight(36);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setDefaultRenderer(Object.class, new IsiRenderer(rata, tabelProduk));

        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 38));
        h.setDefaultRenderer(new HeaderRenderer(rata));
    }

    // ------------------------------------------------------------- REFRESH
    private void refreshProduk() {
        modelProduk.setRowCount(0);
        for (Produk p : daftarProduk) {
            modelProduk.addRow(new Object[]{
                    p.getKode(), p.getNama(), p.getKategori(),
                    "Rp" + Util.rupiah(p.getHarga()),
                    (int) p.getPersenDiskon() + "%",
                    p.getStok(), p.getInfoTambahan()});
        }
    }

    private void refreshKeranjang() {
        modelKeranjang.setRowCount(0);
        for (ItemTransaksi item : transaksi.getDaftarItem()) {
            Produk p = item.getProduk();
            modelKeranjang.addRow(new Object[]{
                    p.getNama(), item.getJumlah(),
                    "Rp" + Util.rupiah(p.getHargaSetelahDiskon()),
                    "Rp" + Util.rupiah(item.getSubtotal())});
        }
        updateRingkasan();
    }

    private void updateRingkasan() {
        double total = transaksi.hitungTotal();
        Pembayaran m = (Pembayaran) cbMetode.getSelectedItem();
        double biaya = m.hitungBiaya(total);
        double tagihan = m.hitungTagihan(total);

        boolean tunai = m instanceof PembayaranTunai;
        txtUang.setEnabled(tunai);
        if (!tunai) txtUang.setText("");

        lblTotal.setText("Rp" + Util.rupiah(total));
        lblBiaya.setText("Rp" + Util.rupiah(biaya));
        lblTagihan.setText("Rp" + Util.rupiah(tagihan));

        double uang = tunai ? bacaUang() : tagihan;
        if (total > 0 && uang >= tagihan) {
            lblKembalian.setText("Rp" + Util.rupiah(uang - tagihan));
            lblKembalian.setForeground(HIJAU);
        } else {
            lblKembalian.setText("-");
            lblKembalian.setForeground(TEKS_ABU);
        }
    }

    private double bacaUang() {
        String angka = txtUang.getText().replaceAll("[^0-9]", "");
        return angka.isEmpty() ? 0 : Double.parseDouble(angka);
    }

    // -------------------------------------------------------------- AKSI
    private void tambahKeKeranjang() {
        int baris = tabelProduk.getSelectedRow();
        if (baris < 0) {
            pesan("Pilih produk dulu di tabel.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Produk p = daftarProduk.get(baris);
        int jumlah = (Integer) spJumlah.getValue();
        try {
            transaksi.tambahItem(p, jumlah);
        } catch (StokTidakCukupException ex) {
            pesan(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
        spJumlah.setValue(1);
        refreshProduk();
        tabelProduk.setRowSelectionInterval(baris, baris);
        refreshKeranjang();
    }

    private void hapusItem() {
        int baris = tabelKeranjang.getSelectedRow();
        if (baris < 0) {
            pesan("Pilih item di keranjang yang mau dihapus.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        transaksi.hapusItem(baris);   // stok otomatis dikembalikan
        refreshProduk();
        refreshKeranjang();
    }

    private void batalTransaksi() {
        if (transaksi.kosong()) return;
        int ok = JOptionPane.showConfirmDialog(this, "Batalkan semua item di keranjang?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            transaksi.batalkan();
            txtUang.setText("");
            refreshProduk();
            refreshKeranjang();
        }
    }

    private void tambahStok() {
        int baris = tabelProduk.getSelectedRow();
        if (baris < 0) {
            pesan("Pilih produk dulu di tabel.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Produk p = daftarProduk.get(baris);
        String in = JOptionPane.showInputDialog(this, "Tambah stok untuk " + p.getNama() + ":", "10");
        if (in == null) return;
        try {
            int jumlah = Integer.parseInt(in.trim());
            if (jumlah <= 0) throw new NumberFormatException();
            p.tambahStok(jumlah);
            refreshProduk();
            tabelProduk.setRowSelectionInterval(baris, baris);
        } catch (NumberFormatException ex) {
            pesan("Masukkan angka yang lebih dari 0.", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bayar() {
        if (transaksi.kosong()) {
            pesan("Keranjang masih kosong.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pembayaran metode = (Pembayaran) cbMetode.getSelectedItem();
        double tagihan = metode.hitungTagihan(transaksi.hitungTotal());
        double uang = (metode instanceof PembayaranTunai) ? bacaUang() : tagihan;

        if (!metode.prosesBayar(tagihan, uang)) {
            pesan("Uang diterima kurang dari total tagihan.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tampilStruk(transaksi.buatStruk(metode, uang));

        // Mulai transaksi baru
        transaksi = new Transaksi(kasir);
        txtUang.setText("");
        refreshProduk();
        refreshKeranjang();
    }

    private void tampilStruk(String teks) {
        JTextArea area = new JTextArea(teks);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setEditable(false);
        area.setBorder(new EmptyBorder(10, 12, 10, 12));
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(450, 420));
        JOptionPane.showMessageDialog(this, sp, "Struk Pembayaran", JOptionPane.PLAIN_MESSAGE);
    }

    private void pesan(String teks, int tipe) {
        JOptionPane.showMessageDialog(this, teks, "Informasi", tipe);
    }

    // =====================================================================
    //  INNER CLASS KHUSUS TAMPILAN
    //  Semuanya memakai INHERITANCE dan OVERRIDING (polymorphism):
    //  Kartu extends JPanel, Tombol extends JButton,
    //  IsiRenderer / HeaderRenderer extends DefaultTableCellRenderer.
    // =====================================================================

    /** Panel dengan sudut membulat dan bayangan tipis. */
    private static class Kartu extends JPanel {
        private final Color warna;
        private final int radius;

        Kartu(Color warna, int radius) {
            this.warna = warna;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            if (warna.equals(Color.WHITE)) {
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(0, 2, w, h - 2, radius, radius);
            }
            g2.setColor(warna);
            g2.fillRoundRect(0, 0, w, h - 3, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Tombol bulat dengan efek hover. */
    private static class Tombol extends JButton {
        private final Color warna;
        private boolean hover = false;

        Tombol(String teks, Color warna, Color warnaTeks) {
            super(teks);
            this.warna = warna;
            setFont(f(Font.BOLD, 14));
            setForeground(warnaTeks);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? warna.darker() : warna);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Renderer isi tabel: zebra, rata kanan/tengah, warna kategori, stok menipis. */
    private static class IsiRenderer extends DefaultTableCellRenderer {
        private final int[] rata;
        private final boolean tabelProduk;

        IsiRenderer(int[] rata, boolean tabelProduk) {
            this.rata = rata;
            this.tabelProduk = tabelProduk;
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                       boolean fokus, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, false, row, col);
            setBorder(new EmptyBorder(0, 12, 0, 12));
            setHorizontalAlignment(rata[col]);
            setFont(f(Font.PLAIN, 14));
            setForeground(TEKS);
            if (sel) {
                setBackground(PILIH);
            } else {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(247, 249, 253));
            }

            if (tabelProduk && col == 2 && v != null) {            // warna kategori
                setFont(f(Font.BOLD, 13));
                String k = v.toString();
                if (k.equals("Makanan")) setForeground(new Color(212, 118, 20));
                else if (k.equals("Minuman")) setForeground(new Color(30, 120, 200));
                else setForeground(new Color(130, 70, 190));
            }
            if (tabelProduk && col == 5 && v instanceof Integer && (Integer) v <= 5) {
                setFont(f(Font.BOLD, 14));                          // stok menipis
                setForeground(MERAH);
            }
            return this;
        }
    }

    /** Renderer judul kolom. */
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        private final int[] rata;

        HeaderRenderer(int[] rata) {
            this.rata = rata;
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel,
                                                       boolean fokus, int row, int col) {
            super.getTableCellRendererComponent(t, v, false, false, row, col);
            setBackground(new Color(236, 241, 249));
            setForeground(new Color(50, 70, 110));
            setFont(f(Font.BOLD, 13));
            setHorizontalAlignment(rata[col]);
            setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 0, new Color(210, 218, 232)),
                    new EmptyBorder(0, 12, 0, 12)));
            return this;
        }
    }

    // ---------------------------------------------------------------- MAIN
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}