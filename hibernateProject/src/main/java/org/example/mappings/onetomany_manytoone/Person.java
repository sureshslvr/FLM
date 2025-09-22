package org.example.mappings.onetomany_manytoone;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    private String username;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private List<Orders> orders;

    public Person(int userId, String username, List<Orders> order) {
        this.personId = userId;
        this.username = username;
        this.orders = order;
    }

    public Person(String username) {
        this.username = username;
    }

    public Person() { }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + personId +
                ", username='" + username + '\'' +
                '}';
    }
}
