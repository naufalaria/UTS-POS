/** IMPLEMENTS interface Pembayaran: e-wallet, biaya admin flat Rp500. */
public class PembayaranEwallet implements Pembayaran {

    @Override
    public String getNamaMetode() { return "E-Wallet"; }

    @Override
    public double hitungBiaya(double total) { return 500; }

    @Override
    public boolean prosesBayar(double tagihan, double uangDiterima) {
        return uangDiterima >= tagihan;
    }
}
