/** IMPLEMENTS interface Pembayaran: kartu debit, biaya admin 1%. */
public class PembayaranDebit implements Pembayaran {

    @Override
    public String getNamaMetode() { return "Kartu Debit"; }

    @Override
    public double hitungBiaya(double total) { return total * 0.01; }

    @Override
    public boolean prosesBayar(double tagihan, double uangDiterima) {
        return uangDiterima >= tagihan;   // dianggap saldo cukup
    }
}
