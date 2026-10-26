/**
 * INTERFACE: kontrak/aturan yang wajib dipenuhi class pembayaran.
 * Class yang implements harus mengisi semua method abstract-nya.
 */
public interface Pembayaran {

    String getNamaMetode();

    double hitungBiaya(double total);                       // biaya admin

    boolean prosesBayar(double tagihan, double uangDiterima);

    // DEFAULT METHOD (Java 8+): sudah punya isi, boleh dipakai langsung
    default double hitungTagihan(double total) {
        return total + hitungBiaya(total);
    }
}
