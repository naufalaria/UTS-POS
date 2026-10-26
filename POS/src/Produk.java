/**
 * ABSTRACT CLASS Produk: kerangka umum semua jenis barang.
 * Subclass: ProdukMakanan, ProdukMinuman, ProdukElektronik.
 */
public abstract class Produk {

    // ENCAPSULATION: semua atribut private
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    // STATIC attribute: milik class, dipakai bersama semua objek
    private static int jumlahJenisProduk = 0;

    // CONSTRUCTOR
    public Produk(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        jumlahJenisProduk++;
    }

    // GETTER & SETTER
    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }

    public void setHarga(double harga) {
        if (harga > 0) {              // validasi di dalam setter
            this.harga = harga;
        }
    }

    public void tambahStok(int jumlah) {
        if (jumlah > 0) {
            this.stok += jumlah;
        }
    }

    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah > stok) {
            throw new StokTidakCukupException(nama, stok, jumlah);
        }
        this.stok -= jumlah;
    }

    public static int getJumlahJenisProduk() { return jumlahJenisProduk; }

    // ABSTRACT METHODS: tiap subclass WAJIB mengisi sendiri
    public abstract String getKategori();
    public abstract String getInfoTambahan();

    // Method biasa yang boleh di-OVERRIDE (default: tanpa diskon)
    public double getPersenDiskon() {
        return 0;
    }

    public double getHargaSetelahDiskon() {
        // memanggil getPersenDiskon() -> versi subclass yang dijalankan (POLYMORPHISM)
        return harga - (harga * getPersenDiskon() / 100);
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-20s | %-11s | Rp%9s | Stok: %3d | %s",
                kode, nama, getKategori(), Util.rupiah(harga), stok, getInfoTambahan());
    }
}
