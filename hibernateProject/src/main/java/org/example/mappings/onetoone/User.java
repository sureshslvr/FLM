package org.example.mappings.onetoone;

import jakarta.persistence.*;
import jakarta.persistence.OneToOne;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "pass_num")
    private PassPort passPort;

    public User(){}

    public User(int id, String userName,PassPort passPort) {
        this.id = id;
        this.userName = userName;
        this.passPort=passPort;
    }

    public User(String userName,PassPort passPort) {
        this.userName = userName;
        this.passPort=passPort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PassPort getPassPort() {
        return passPort;
    }

    public void setPassPort(PassPort passPort) {
        this.passPort = passPort;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passPort=" + passPort +
                '}';
    }
}
