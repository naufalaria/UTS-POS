/** CUSTOM EXCEPTION: dilempar ketika stok kurang dari jumlah beli. */
public class StokTidakCukupException extends Exception {

    public StokTidakCukupException(String namaProduk, int stokTersedia, int diminta) {
        super("Stok " + namaProduk + " tidak cukup. Tersedia: "
                + stokTersedia + ", diminta: " + diminta);
    }
}
