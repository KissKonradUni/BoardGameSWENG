package app.kosoft.boardgame.db;

import app.kosoft.boardgame.data.KoSoftUserDetails;
import app.kosoft.boardgame.data.RegistrationData;
import app.kosoft.boardgame.data.ValidationFailure;
import com.google.gson.Gson;
import com.surrealdb.connection.SurrealWebSocketConnection;
import com.surrealdb.driver.SyncSurrealDriver;
import com.surrealdb.driver.model.QueryResult;

import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

class SurrealDBConfigJson implements Serializable {
    public String HOST = "localhost";
    public int PORT = 8000;
    public String USERNAME = "root";
    public String PASSWORD = "root";
    public String NAMESPACE = "kosoft";
    public String DATABASE = "boardgames";
}

public class SurrealDB {
    private static SurrealDB INSTANCE;

    private SurrealWebSocketConnection connection;
    private SyncSurrealDriver driver;

    private SurrealDB() {
        try {
            SurrealDBConfigJson config = readConfig();

            connection = new SurrealWebSocketConnection(config.HOST, config.PORT, false);
            connection.connect(5);

            driver = new SyncSurrealDriver(connection);
            driver.signIn(config.USERNAME, config.PASSWORD);
            driver.use(config.NAMESPACE, config.DATABASE);
        } catch (Exception e) {
            System.err.println("SurrealDB kapcsolat létrehozása sikertelen: " + e.getMessage());
        }
    }

    private SurrealDBConfigJson readConfig() {
        File configFile = new File("surrealdb.json");

        SurrealDBConfigJson config = new SurrealDBConfigJson();
        if (!configFile.exists()) {
            System.err.println("A SurrealDB konfigurációs fájl nem található! (surrealdb.json)");
            System.err.println("Alapbeállítások használata...");
        } else {
            Gson gson = new Gson();
            try {
                config = gson.fromJson(new FileReader(configFile), SurrealDBConfigJson.class);
            } catch (Exception e) {
                System.err.println("A SurrealDB konfigurációs fájl olvasása sikertelen: " + e.getMessage());
                System.err.println("Alapbeállítások használata...");
            }
        }

        return config;
    }

    public static SurrealDB getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SurrealDB();
        }
        return INSTANCE;
    }

    public SyncSurrealDriver getDriver() {
        return driver;
    }

    public static void resetConnection() {
        INSTANCE.connection.disconnect();
        INSTANCE.connection = null;
        INSTANCE.driver = null;
        INSTANCE = null;
        getInstance();
    }

    public ValidationFailure tryRegister(RegistrationData data) {
        ValidationFailure validationFailure = data.isValid();
        if (validationFailure != null) {
            return validationFailure;
        }

        List<QueryResult<KoSoftUserDetails>> result = driver.query(
            "SELECT * FROM users WHERE username = $username OR email = $email",
            Map.of("username", data.username, "email", data.email),
            KoSoftUserDetails.class
        );
        // Az első query eredménye nem üres, tehát már létezik a felhasználó
        if (!result.getFirst().getResult().isEmpty()) {
            KoSoftUserDetails queryResult = result.getFirst().getResult().getFirst();
            if (queryResult.getUsername().equals(data.username)) {
                return ValidationFailure.USERNAME_EXISTS;
            } else {
                return ValidationFailure.EMAIL_EXISTS;
            }
        }

        driver.create("users", new KoSoftUserDetails(data.username, data.password, data.email));
        return null;
    }
}
