/**
 * ENCAPSULATION: data akun untuk login (username, password, peran).
 * Atribut private, diakses lewat getter.
 */
public class Akun {

    private String username;
    private String password;
    private String peran;

    public Akun(String username, String password, String peran) {
        this.username = username;
        this.password = password;
        this.peran = peran;
    }

    public String getUsername() { return username; }
    public String getPeran() { return peran; }

    /** Cek apakah username dan password yang diketik cocok dengan akun ini. */
    public boolean cocok(String usernameInput, String passwordInput) {
        return username.equalsIgnoreCase(usernameInput) && password.equals(passwordInput);
    }
}
