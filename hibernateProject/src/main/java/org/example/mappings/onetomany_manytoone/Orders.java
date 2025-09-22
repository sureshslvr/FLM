package org.example.mappings.onetomany_manytoone;

import jakarta.persistence.*;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    private String productName;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;

    public Orders(int orderId, String productName, int quantity, Person person) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.person = person;
    }

    public Orders(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public Orders() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Order{" +
                "quantity=" + quantity +
                ", productName='" + productName + '\'' +
                '}';
    }
}
