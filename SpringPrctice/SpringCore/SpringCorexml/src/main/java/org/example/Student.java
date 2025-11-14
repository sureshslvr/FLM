package org.example;

public class Student {
    private int id;
    private String name;
    private Address address;
    public Student(){
        System.out.println("student class");
    }

    public Student(int id, String name,Address address) {
        this.id = id;
        this.name = name;
        this.address=address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
