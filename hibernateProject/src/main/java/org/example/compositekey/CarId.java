package org.example.compositekey;

import jakarta.persistence.Embeddable;

@Embeddable
public class CarId {
    private int id;
    private String engineNum;

    public CarId() {
    }
    public CarId(int id, String engineNum) {
        this.id = id;
        this.engineNum = engineNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    @Override
    public String toString() {
        return "CarId{" +
                "id=" + id +
                ", engineNum='" + engineNum + '\'' +
                '}';
    }
}
