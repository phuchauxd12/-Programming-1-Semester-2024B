package utils;

import autoPart.autoPart;
import car.Car;

import java.util.ArrayList;

public class CarAndAutoPartMenu {
    private static ArrayList<Car> carsList = new ArrayList<>();
    private static ArrayList<autoPart> autoPartsList = new ArrayList<>();

    public static ArrayList<Car> getCarsList() {
        return carsList;
    }

    public static ArrayList<autoPart> getAutoPartsList() {
        return autoPartsList;
    }

    public static Car findCarByID(String carID) {
        for (Car car : carsList) {
            if (car.getCarID().equals(carID)) {
                return car;
            }
        }
        return null;
    }

    public static autoPart findAutoPartByID(String partID) {
        for (autoPart autoPart : autoPartsList) {
            if (autoPart.getPartID().equals(partID)) {
                return autoPart;
            }
        }
        return null;
    }
    
}
