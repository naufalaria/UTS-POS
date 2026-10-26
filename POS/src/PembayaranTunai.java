/** IMPLEMENTS interface Pembayaran: tunai, tanpa biaya admin. */
public class PembayaranTunai implements Pembayaran {

    @Override
    public String getNamaMetode() { return "Tunai"; }

    @Override
    public double hitungBiaya(double total) { return 0; }

    @Override
    public boolean prosesBayar(double tagihan, double uangDiterima) {
        return uangDiterima >= tagihan;
    }
}
