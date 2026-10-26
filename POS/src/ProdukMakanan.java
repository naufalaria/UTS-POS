/** INHERITANCE: ProdukMakanan extends Produk. Diskon 5%. */
public class ProdukMakanan extends Produk {

    private String tanggalKadaluarsa;

    public ProdukMakanan(String kode, String nama, double harga, int stok, String tanggalKadaluarsa) {
        super(kode, nama, harga, stok);
        this.tanggalKadaluarsa = tanggalKadaluarsa;
    }

    public String getTanggalKadaluarsa() { return tanggalKadaluarsa; }

    @Override
    public String getKategori() { return "Makanan"; }

    @Override
    public String getInfoTambahan() { return "Exp: " + tanggalKadaluarsa; }

    @Override
    public double getPersenDiskon() { return 5; }   // beda dengan induk (0)
}
