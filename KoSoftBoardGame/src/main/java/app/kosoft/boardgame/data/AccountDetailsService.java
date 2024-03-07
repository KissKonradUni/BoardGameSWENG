package app.kosoft.boardgame.data;

import app.kosoft.boardgame.db.SurrealDB;
import com.surrealdb.driver.SyncSurrealDriver;
import com.surrealdb.driver.model.QueryResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountDetailsService implements UserDetailsService, UserDetailsPasswordService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = loadUser(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("Felhasználó nem található: " + username);
        }
        return userDetails;
    }

    private KoSoftUserDetails loadUser(String username) {
        SyncSurrealDriver driver = SurrealDB.getInstance().getDriver();

        Map<String, String> params = Map.of("username", username);
        List<QueryResult<KoSoftUserDetails>> result = driver.query(
            "SELECT * FROM users WHERE username = $username",
            params,
            KoSoftUserDetails.class
        );
        if (!result.getFirst().getResult().isEmpty()) {
            return result.getFirst().getResult().getFirst();
        }

        return null;
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        KoSoftUserDetails userDetails = (KoSoftUserDetails) user;
        userDetails.setPassword(newPassword);

        SyncSurrealDriver driver = SurrealDB.getInstance().getDriver();
        driver.update("userDetails.password", userDetails.getPassword());

        return userDetails;
    }
}
