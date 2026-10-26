/** INHERITANCE: ProdukElektronik extends Produk. Tidak ada diskon (memakai default induk). */
public class ProdukElektronik extends Produk {

    private int garansiBulan;

    public ProdukElektronik(String kode, String nama, double harga, int stok, int garansiBulan) {
        super(kode, nama, harga, stok);
        this.garansiBulan = garansiBulan;
    }

    public int getGaransiBulan() { return garansiBulan; }

    @Override
    public String getKategori() { return "Elektronik"; }

    @Override
    public String getInfoTambahan() { return "Garansi " + garansiBulan + " bln"; }

    // getPersenDiskon() TIDAK di-override -> memakai versi Produk (0%)
}
