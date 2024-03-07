package app.kosoft.boardgame.config;

import app.kosoft.boardgame.data.AccountDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Bean
    public AccountDetailsService accountDetailsService() {
        return new AccountDetailsService();
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
        http.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests((authorize) -> authorize
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
