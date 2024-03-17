package app.kosoft.boardgame;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
public class User {

    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Getter
    private String username;
    private String email;
    @Setter
    private String passwd;



    public User(String name, String passwd, long id) {
        this.username = name;
        this.passwd = passwd;
        this.id = id;
    }

    protected User() {}


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + username + '\'' +
                ", id=" + id +
                '}';
    }

    public String getPassword() {
        return passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}