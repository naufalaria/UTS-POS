import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Transaksi {

    private static int counter = 0;              // static: nomor urut otomatis

    private final String noTransaksi;
    private final Kasir kasir;
    private final LocalDateTime waktu;
    private final ArrayList<ItemTransaksi> daftarItem = new ArrayList<>();

    public Transaksi(Kasir kasir) {
        counter++;
        this.noTransaksi = String.format("TRX-%04d", counter);
        this.kasir = kasir;
        this.waktu = LocalDateTime.now();
    }

    public String getNoTransaksi() { return noTransaksi; }

    public List<ItemTransaksi> getDaftarItem() {
        return Collections.unmodifiableList(daftarItem);
    }

    // POLYMORPHISM (method OVERLOADING): nama sama, parameter berbeda
    public void tambahItem(Produk produk, int jumlah) throws StokTidakCukupException {
        produk.kurangiStok(jumlah);              // bisa melempar exception
        for (ItemTransaksi item : daftarItem) {
            if (item.getProduk() == produk) {    // produk sudah ada -> gabungkan
                item.tambahJumlah(jumlah);
                return;
            }
        }
        daftarItem.add(new ItemTransaksi(produk, jumlah));
    }

    public void tambahItem(Produk produk) throws StokTidakCukupException {
        tambahItem(produk, 1);                   // default beli 1
    }

    // Menghapus satu item dari keranjang dan mengembalikan stoknya
    public void hapusItem(int index) {
        ItemTransaksi item = daftarItem.remove(index);
        item.getProduk().tambahStok(item.getJumlah());
    }

    public boolean kosong() { return daftarItem.isEmpty(); }

    public double hitungTotal() {
        double total = 0;
        for (ItemTransaksi item : daftarItem) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Mengembalikan stok jika transaksi dibatalkan / pembayaran gagal
    public void batalkan() {
        for (ItemTransaksi item : daftarItem) {
            item.getProduk().tambahStok(item.getJumlah());
        }
        daftarItem.clear();
    }

    // Membuat teks struk (dipakai oleh versi console maupun GUI)
    public String buatStruk(Pembayaran metode, double uangDiterima) {
        double total = hitungTotal();
        double biaya = metode.hitungBiaya(total);
        double tagihan = metode.hitungTagihan(total);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Locale L = Locale.US;

        StringBuilder sb = new StringBuilder();
        sb.append("==============================================\n");
        sb.append("              TOKO SERBA ADA UTS\n");
        sb.append("==============================================\n");
        sb.append("No     : ").append(noTransaksi).append("\n");
        sb.append("Waktu  : ").append(waktu.format(fmt)).append("\n");
        sb.append("Kasir  : ").append(kasir.getNama()).append(" (")
          .append(kasir.getPeran()).append(", ").append(kasir.getShift()).append(")\n");
        sb.append("----------------------------------------------\n");
        for (ItemTransaksi item : daftarItem) {
            Produk p = item.getProduk();
            sb.append(p.getNama()).append("\n");
            String kiri = "  " + item.getJumlah() + " x Rp" + Util.rupiah(p.getHargaSetelahDiskon());
            if (p.getPersenDiskon() > 0) {
                kiri += String.format(L, " (diskon %.0f%%)", p.getPersenDiskon());
            }
            sb.append(String.format(L, "%-30s%14s%n", kiri, "Rp" + Util.rupiah(item.getSubtotal())));
        }
        sb.append("----------------------------------------------\n");
        sb.append(String.format(L, "%-26s Rp%15s%n", "Total belanja", Util.rupiah(total)));
        sb.append(String.format(L, "%-26s Rp%15s%n", "Biaya admin", Util.rupiah(biaya)));
        sb.append(String.format(L, "%-26s Rp%15s%n", "Total tagihan", Util.rupiah(tagihan)));
        sb.append(String.format(L, "%-26s Rp%15s%n", "Bayar (" + metode.getNamaMetode() + ")", Util.rupiah(uangDiterima)));
        sb.append(String.format(L, "%-26s Rp%15s%n", "Kembalian", Util.rupiah(uangDiterima - tagihan)));
        sb.append("==============================================\n");
        sb.append("        Terima kasih sudah berbelanja!\n");
        sb.append("==============================================\n");
        return sb.toString();
    }

    // Versi console: cukup mencetak hasil buatStruk()
    public void cetakStruk(Pembayaran metode, double uangDiterima) {
        System.out.println();
        System.out.print(buatStruk(metode, uangDiterima));
    }
}
