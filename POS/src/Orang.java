/**
 * ABSTRACT CLASS (superclass level atas).
 * Tidak bisa di-instansiasi langsung: new Orang() -> error.
 * Menjadi induk bagi Kasir (INHERITANCE / pewarisan).
 */
public abstract class Orang {

    // ENCAPSULATION: atribut private, hanya bisa diakses lewat getter/setter
    private String id;
    private String nama;

    public Orang(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    // ABSTRACT METHOD: wajib di-override oleh subclass
    public abstract String getPeran();

    @Override
    public String toString() {
        return getPeran() + " " + nama + " (" + id + ")";
    }
}
