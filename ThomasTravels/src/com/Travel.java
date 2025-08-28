package com;


import java.util.ArrayList;
import java.util.List;

public class Travel {

    public static boolean isCarDriver(Driver driver) {
        return driver.getCategory().equalsIgnoreCase("Car");
    }
  

    public static String RetrivebyDriverId(ArrayList<Driver> driverList, int driverID) {
		/*
		 * for (Driver driver : driverList) { if (driver.getId() == driverID) { return
		 * driver.toString(); } } return "Driver not found";
		 */
    	 return driverList.stream()
    			.filter(driver->driver.getId()==driverID)
    			.findFirst()
    			.map(d->String.format("driver name is %s belongs to category %s and travelled distance of %d", d.getName(),d.getCategory(),d.getTotalDistance()))
    			.orElse("Driver not found");
    }

    public static Long RetriveCountOfDriver(ArrayList<Driver> driverList, String category) {
		/*
		 * int count = 0; for (Driver driver : driverList) { if
		 * (driver.getCategory().equalsIgnoreCase(category)) { count++; } } return
		 * count;
		 */
        
        return driverList.stream()
        		.filter(d->d.getCategory().equals(category))
        		.count();
    }

    public static List<Driver> retriveDriver(ArrayList<Driver> driverList, String category) {
//        ArrayList<Driver> result = new ArrayList<>();
//        for (Driver driver : driverList) {
//            if (driver.getCategory().equalsIgnoreCase(category)) {
//                result.add(driver);
//            }
//        }
//        return result;
        
        return driverList.stream()
        	.filter(d->d.getCategory().equalsIgnoreCase(category))
        	.toList();
        
        
    }

    public static Driver RetriveMaximumDistanceTravelledDriver(ArrayList<Driver> driverList) {
		/*
		 * Driver maxDriver = driverList.get(0); for (Driver driver : driverList) { if
		 * (driver.getTotalDistance() > maxDriver.getTotalDistance()) { maxDriver =
		 * driver; } } return maxDriver;
		 */
        
        return driverList.stream()
        	.sorted((d1,d2)->Double.compare(d2.getTotalDistance(), d1.getTotalDistance()))
        	.findFirst()
        	.get();
    }
}
