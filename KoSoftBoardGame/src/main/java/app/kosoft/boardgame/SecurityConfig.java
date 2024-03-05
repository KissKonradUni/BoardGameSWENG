package app.kosoft.boardgame;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

/**
 * A biztonsági beállításokat tartalmazó osztály.
 */
// Ne ellenőrizze a helyesírást, nem tud magyarul
@SuppressWarnings("ALL")

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Konfigurációs metódus, amely egy InMemoryUserDetailsManager objektumot hoz létre.
     * Ezzel a felhasználók adatait tárolhatjuk memóriában, még adatbázis használata nélkül.
     * @return InMemoryUserDetailsManager objektum
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.withUsername("user1")
            .password(passwordEncoder().encode("user1Pass"))
            .roles("USER")
            .build();
        UserDetails user2 = User.withUsername("user2")
            .password(passwordEncoder().encode("user2Pass"))
            .roles("USER")
            .build();
        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("adminPass"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user1, user2, admin);//store users in memory
    }

    /**
     * Konfigurációs metódus, amely egy SecurityFilterChain objektumot hoz létre. <br><br>
     * Jelenlegi működése: <br>
     * <p>
     *  -> Engedélyezd az alábbi dolgok elérését mindenki számára: <br>
     *  |-> / <br>
     *  |-> /login <br>
     *  |-> /logout <br>
     *  |-> /register <br>
     *  |-> /static/** (statikus fájlok) <br>
     *  -> Minden más eléréshez be kell jelentkezni. <br>
     *  -> Bejelentkezési oldal: /login (post metódussal lehet bejelentkezni) <br>
     *  -> Kijelentkezési oldal: /logout <br>
     * </p>
     * @param http HttpSecurity objektum
     * @return SecurityFilterChain objektum
     * @throws Exception bármi lehet
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                    "/",
                    "/login",
                    "/logout",
                    "/register",
                    "/static/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public SimpleUrlLogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleUrlLogoutSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
