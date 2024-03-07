package app.kosoft.boardgame.data;

import java.io.Serializable;

public class RegistrationData implements Serializable {
    public String email;
    public String username;
    public String password;

    public RegistrationData() {
    }

    public RegistrationData(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Ellenőrzi, hogy a regisztrációs adatok megfelelnek-e a követelményeknek.
     * Az email formátuma: Alfanumerikus karakterek, kötelező a @ és a . karakterek jelenléte.
     * A felhasználónév formátuma: Alfanumerikus karakterek a ., -, és _ karakterekkel kiegészítve, legalább 4 karakter hosszú.
     * A jelszó formátuma: Legalább 8 karakter hosszú, legalább egy kisbetűt, egy nagybetűt és egy számot tartalmaz, illetve legalább egy speciális karaktert tartalmaz.
     * @return true, ha a regisztrációs adatok megfelelnek a követelményeknek, egyébként false
     */
    public ValidationFailure isValid() {
        if (this.email == null || this.username == null || this.password == null) {
            return ValidationFailure.MISSING_DATA;
        }

        boolean email = this.email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        boolean username = this.username.matches("^[a-zA-Z0-9._-]{4,}$");
        // https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
        boolean password = this.password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

        if (!email) {
            return ValidationFailure.INVALID_EMAIL;
        } else if (!username) {
            return ValidationFailure.INVALID_USERNAME;
        } else if (!password) {
            return ValidationFailure.INVALID_PASSWORD;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "RegistrationData{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
