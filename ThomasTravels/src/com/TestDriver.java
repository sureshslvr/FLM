package com;

import java.util.ArrayList;
import java.util.List;

public class TestDriver {
    public static void main(String[] args) {
        ArrayList<Driver> driverList = new ArrayList<>();
        
        String s="aaas";
        System.out.println(Math.round(5.5));
        
      
        

        driverList.add(new Driver(1, "Sudhagar", "Car", 42));
        driverList.add(new Driver(2, "Raj", "Auto", 15));
        driverList.add(new Driver(3, "Vinoth", "Lorry", 8));
        driverList.add(new Driver(4, "Ravi", "Car", 65));
        driverList.add(new Driver(5, "Kumar", "Auto", 10));

        //check weather car driver or not
        System.out.println("Is Ravi a car driver? " + Travel.isCarDriver(driverList.get(3)));
        System.out.println("Is Raj a car driver? " + Travel.isCarDriver(driverList.get(1)));

        //Retrieve driver details using ID
        System.out.println(Travel.RetrivebyDriverId(driverList, 1));
        System.out.println(Travel.RetrivebyDriverId(driverList, 2));

       //count the drivers using category
        System.out.println("Number of Auto drivers: " + Travel.RetriveCountOfDriver(driverList, "Auto"));
        System.out.println("Number of car drivers: " + Travel.RetriveCountOfDriver(driverList, "car"));

        //print all the drivers details of given category
        List<Driver> carDrivers = Travel.retriveDriver(driverList, "Car");
        System.out.println("Car Drivers:");
        for (Driver d : carDrivers) {
            System.out.println(d);
        }

        //gives the details of drivers who traveled maximum KMs
        Driver topDriver = Travel.RetriveMaximumDistanceTravelledDriver(driverList);
        System.out.println("Driver who traveled maximum distance: " + topDriver);
    }
}

