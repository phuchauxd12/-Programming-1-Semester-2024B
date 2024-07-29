package utils;

import autoPart.autoPart;
import car.Car;

import java.util.ArrayList;
import java.util.Scanner;

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

    public static void menu() {
        System.out.println("Welcome to the Car and Auto Part Menu!");
        System.out.println("1. Add a car");
        System.out.println("2. Add an auto part");
        System.out.println("3. Display all cars");
        System.out.println("4. Display all auto parts");
        System.out.println("5. Delete a car");
        System.out.println("6. Delete an auto part");
        System.out.println("7. Update a car");
        System.out.println("8. Update an auto part");
        System.out.println("9. Exit");
    }

    public static void main(String[] args) {
        int option = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            menu();
            System.out.println("Enter an option:");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    Car.createCar();
                    break;
                case 2:
                    autoPart.createPart();
                    break;
                case 3:
                    Car.displayAllCars();
                    break;
                case 4:
                    autoPart.displayAllParts();
                    break;
                case 5:
                    Car.displayAllCars();
                    Car.deleteCar();
                    Car.displayAllCars();
                    break;
                case 6:
                    autoPart.displayAllParts();
                    autoPart.deletePart();
                    autoPart.displayAllParts();
                    break;
                case 7:
                    Car.displayAllCars();
                    Car.updateCar();
                    break;
                case 8:
                    autoPart.displayAllParts();
                    autoPart.updatePart();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 8);
    }
}
