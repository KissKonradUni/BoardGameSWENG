package app.kosoft.boardgame;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

    @GeneratedValue
    @Id
    private long id;
    private String username;
    private String passwd;


    public User(String name, String passwd, long id) {
        this.username = name;
        this.passwd = passwd;
        this.id = id;
    }

    protected User() {}



    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }


    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + username + '\'' +
                ", id=" + id +
                '}';
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {
        return passwd;
    }
}
