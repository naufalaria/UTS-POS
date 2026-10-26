/** INHERITANCE: ProdukMinuman extends Produk. Diskon 10%. */
public class ProdukMinuman extends Produk {

    private int ukuranMl;

    public ProdukMinuman(String kode, String nama, double harga, int stok, int ukuranMl) {
        super(kode, nama, harga, stok);
        this.ukuranMl = ukuranMl;
    }

    public int getUkuranMl() { return ukuranMl; }

    @Override
    public String getKategori() { return "Minuman"; }

    @Override
    public String getInfoTambahan() { return ukuranMl + " ml"; }

    @Override
    public double getPersenDiskon() { return 10; }
}
