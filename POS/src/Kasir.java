/**
 * INHERITANCE: Kasir mewarisi Orang (extends).
 * Menambah atribut sendiri (shift) dan meng-OVERRIDE getPeran().
 */
public class Kasir extends Orang {

    private String shift;

    public Kasir(String id, String nama, String shift) {
        super(id, nama);          // memanggil constructor superclass
        this.shift = shift;
    }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    @Override   // POLYMORPHISM (method overriding)
    public String getPeran() {
        return "Kasir";
    }
}
