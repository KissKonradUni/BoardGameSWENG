package app.kosoft.boardgame.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class KoSoftUserDetails implements UserDetails, Serializable {

    private String id;
    private Collection<String> authorities;
    private String email;
    private String password;
    private String username;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private boolean credentialsNonExpired;

    private transient Collection<? extends GrantedAuthority> grantedAuthorities;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public KoSoftUserDetails(String username, String password, String email, Boolean enabled, Boolean accountNonExpired, Boolean accountNonLocked, boolean credentialsNonExpired, Collection<String> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;

        if (authorities.stream().anyMatch(s -> s.startsWith("ROLE_")))
            throw new IllegalArgumentException("A jogosultságoknak nem szabad, hogy tartalmazzanak ROLE_ előtagot!");

        authorities.forEach(s -> s = "ROLE_" + s);
        this.authorities = authorities;

        this.grantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public KoSoftUserDetails(String username, String password, String email) {
        this(
            username,
            encoder.encode(password),
            email,
            true,
            true,
            true,
            true,
            List.of("USER")
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Beállítja a jelszót
     * @param password Egy előre lehashelt jelszó
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
