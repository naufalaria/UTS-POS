import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // POLYMORPHISM: satu list bertipe Produk berisi berbagai subclass
    private static final ArrayList<Produk> daftarProduk = new ArrayList<>();
    private static final ArrayList<Akun> daftarAkun = new ArrayList<>();
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        isiDataAwal();
        isiDataAkun();

        System.out.println("=== APLIKASI POINT OF SALES (KASIR) ===");
        Kasir kasir = null;
        for (int percobaan = 1; percobaan <= 3 && kasir == null; percobaan++) {
            System.out.print("Username : ");
            String user = input.nextLine().trim();
            System.out.print("Password : ");
            String pass = input.nextLine();

            for (int i = 0; i < daftarAkun.size(); i++) {
                Akun a = daftarAkun.get(i);
                if (a.cocok(user, pass)) {
                    kasir = new Kasir(String.format("K-%03d", i + 1), a.getUsername(), "Pagi");
                    break;
                }
            }
            if (kasir == null) {
                System.out.println("Username atau password salah (" + percobaan + "/3).");
            }
        }
        if (kasir == null) {
            System.out.println("Login gagal 3 kali. Program ditutup.");
            return;
        }
        System.out.println("Login sebagai: " + kasir);   // toString() -> getPeran() milik Kasir

        boolean jalan = true;
        while (jalan) {
            System.out.println("\n----------- MENU -----------");
            System.out.println("1. Lihat daftar produk");
            System.out.println("2. Transaksi baru");
            System.out.println("3. Tambah stok produk");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            String pilih = input.nextLine().trim();

            switch (pilih) {
                case "1": tampilProduk(); break;
                case "2": transaksiBaru(kasir); break;
                case "3": tambahStok(); break;
                case "0": jalan = false; break;
                default: System.out.println("Menu tidak valid.");
            }
        }
        System.out.println("Program selesai. Sampai jumpa!");
    }

    private static void isiDataAkun() {
        daftarAkun.add(new Akun("admin", "admin123", "Admin"));
        daftarAkun.add(new Akun("aria", "kasir123", "Kasir"));
    }

    private static void isiDataAwal() {
        // UPCASTING: objek subclass disimpan ke variabel superclass
        daftarProduk.add(new ProdukMakanan("MKN01", "Roti Tawar", 15000, 20, "30-12-2026"));
        daftarProduk.add(new ProdukMakanan("MKN02", "Mie Instan", 3500, 100, "01-06-2027"));
        daftarProduk.add(new ProdukMinuman("MNM01", "Air Mineral", 4000, 50, 600));
        daftarProduk.add(new ProdukMinuman("MNM02", "Teh Botol", 6000, 40, 450));
        daftarProduk.add(new ProdukElektronik("ELK01", "Charger HP", 75000, 10, 12));
        daftarProduk.add(new ProdukElektronik("ELK02", "Earphone", 50000, 15, 6));
    }

    private static void tampilProduk() {
        System.out.println("\nDAFTAR PRODUK (total jenis: " + Produk.getJumlahJenisProduk() + ")");
        for (Produk p : daftarProduk) {
            System.out.println(p);      // toString() versi masing-masing subclass
        }
    }

    private static Produk cariProduk(String kode) {
        for (Produk p : daftarProduk) {
            if (p.getKode().equalsIgnoreCase(kode)) {
                return p;
            }
        }
        return null;
    }

    private static void tambahStok() {
        tampilProduk();
        System.out.print("Kode produk : ");
        Produk p = cariProduk(input.nextLine().trim());
        if (p == null) {
            System.out.println("Produk tidak ditemukan.");
            return;
        }
        int jumlah = bacaAngka("Jumlah tambah stok : ");
        p.tambahStok(jumlah);
        System.out.println("Stok " + p.getNama() + " sekarang: " + p.getStok());
    }

    private static void transaksiBaru(Kasir kasir) {
        Transaksi trx = new Transaksi(kasir);
        tampilProduk();
        System.out.println("\nMasukkan kode produk (ketik 0 untuk selesai).");

        while (true) {
            System.out.print("Kode produk : ");
            String kode = input.nextLine().trim();
            if (kode.equals("0")) break;

            Produk p = cariProduk(kode);
            if (p == null) {
                System.out.println("Produk tidak ditemukan.");
                continue;
            }
            int jumlah = bacaAngka("Jumlah      : ");
            if (jumlah <= 0) {
                System.out.println("Jumlah harus lebih dari 0.");
                continue;
            }
            try {
                trx.tambahItem(p, jumlah);
                System.out.println("+ " + jumlah + " x " + p.getNama() + " ditambahkan.");
            } catch (StokTidakCukupException e) {
                System.out.println("! " + e.getMessage());
            }
        }

        if (trx.kosong()) {
            System.out.println("Transaksi dibatalkan (tidak ada item).");
            return;
        }

        double total = trx.hitungTotal();
        System.out.println("\nTotal belanja: Rp" + Util.rupiah(total));
        System.out.println("Metode pembayaran: 1. Tunai  2. Debit  3. E-Wallet");
        int m = bacaAngka("Pilih : ");

        // POLYMORPHISM: variabel bertipe INTERFACE, objeknya class berbeda
        Pembayaran metode;
        switch (m) {
            case 2:  metode = new PembayaranDebit(); break;
            case 3:  metode = new PembayaranEwallet(); break;
            default: metode = new PembayaranTunai();
        }

        double tagihan = metode.hitungTagihan(total);
        System.out.println("Total tagihan (" + metode.getNamaMetode() + "): Rp" + Util.rupiah(tagihan));

        double uang = tagihan;
        if (metode instanceof PembayaranTunai) {
            uang = bacaAngka("Uang diterima : Rp");
        }

        if (metode.prosesBayar(tagihan, uang)) {
            trx.cetakStruk(metode, uang);
        } else {
            trx.batalkan();   // kembalikan stok
            System.out.println("Pembayaran gagal: uang kurang. Transaksi dibatalkan, stok dikembalikan.");
        }
    }

    private static int bacaAngka(String pesan) {
        System.out.print(pesan);
        try {
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka.");
            return 0;
        }
    }
}