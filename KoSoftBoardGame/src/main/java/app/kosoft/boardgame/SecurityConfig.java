package app.kosoft.boardgame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;


/**
 * A biztonsági beállításokat tartalmazó osztály.
 */
// Ne ellenőrizze a helyesírást, nem tud magyarul
@SuppressWarnings("ALL")

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    private CustomUserDetailsService userDetailsService;


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Konfigurációs metódus, amely egy InMemoryUserDetailsManager objektumot hoz létre.
     * Ezzel a felhasználók adatait tárolhatjuk memóriában, még adatbázis használata nélkül.
     * @return InMemoryUserDetailsManager objektum
     */


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
            );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }



}
