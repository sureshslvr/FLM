package org.example.compositekey;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Car {
    @EmbeddedId
    @Id
    private CarId carId;
    private String name;

    public  Car(){}
    public Car(CarId carId, String name) {
        this.carId = carId;
        this.name = name;
    }

    public CarId getCarId() {
        return carId;
    }

    public void setCarId(CarId carId) {
        this.carId = carId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", name='" + name + '\'' +
                '}';
    }
}
