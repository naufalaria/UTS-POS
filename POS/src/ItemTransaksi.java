/**
 * COMPOSITION / ASSOCIATION: ItemTransaksi "memiliki" sebuah Produk.
 * Tipe atribut memakai superclass Produk, sehingga bisa berisi
 * Makanan / Minuman / Elektronik (POLYMORPHISM).
 */
public class ItemTransaksi {

    private Produk produk;
    private int jumlah;

    public ItemTransaksi(Produk produk, int jumlah) {
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public Produk getProduk() { return produk; }
    public int getJumlah() { return jumlah; }
    public void tambahJumlah(int tambahan) { this.jumlah += tambahan; }

    public double getSubtotal() {
        return produk.getHargaSetelahDiskon() * jumlah;
    }
}
