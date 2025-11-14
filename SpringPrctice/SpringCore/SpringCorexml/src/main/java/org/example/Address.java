package org.example;
/*this class used for dependency injection demonstration*/
public class Address {

    private String street,area;

    public Address() {
    }

    public Address(String street, String area) {
        this.street = street;
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
