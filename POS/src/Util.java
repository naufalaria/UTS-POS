import java.util.Locale;

/** Class utilitas dengan method STATIC (dipanggil tanpa membuat objek). */
public class Util {

    public static String rupiah(double nilai) {
        return String.format(Locale.US, "%,.0f", nilai).replace(',', '.');
    }
}
