package com;

public class Driver {
    private int id;
    private String name;
    private String category;
    private int totalDistance;

    
    public Driver() { }
    
    public Driver(int id, String name, String category, int totalDistance) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.totalDistance = totalDistance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        return "Driver name is " + name +
               " belonging to the category " + category +
               " traveled " + totalDistance + " KM so far.";
    }
}

