package com.example.kosoftboardgame;

public class User {

    private String name;
    private String passwd;

    public User(String name, String passwd, int id) {
        this.name = name;
        this.passwd = passwd;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
