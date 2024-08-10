package utils;

import autoPart.autoPart;
import car.Car;

import java.util.ArrayList;
import java.util.InputMismatchException;
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

    public static void displayAllCars() {
        System.out.println("Displaying all cars:");
        for (Car car : getCarsList()) {
            System.out.println(car);
        }
        System.out.println("----------------");
    }

    public static void displayAllParts() {
        System.out.println("Displaying all Auto Parts:");
        for (autoPart part : getAutoPartsList()) {
            System.out.println(part);
        }
        System.out.println("----------------");
    }

    public static int getOption(int option, Scanner input) {
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.println("Enter an option:");
                option = input.nextInt();
                validInput = true; // If input is valid, exit the loop
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number");
                input.next(); // Clear the invalid input from the scanner buffer
            }
        }
        return option;
    }

    public static void menu() {
        System.out.println("Welcome to the Car and Auto Part Menu!");
        System.out.println("---------------------");
        System.out.println("1. Add a car");
        System.out.println("2. Display all cars");
        System.out.println("3. Add an auto part");
        System.out.println("4. Display all auto parts");
        System.out.println("5. Delete a car");
        System.out.println("6. Delete an auto part");
        System.out.println("7. Update a car");
        System.out.println("8. Update an auto part");
        System.out.println("9. Exit");
        System.out.println("---------------------");
    }

    public static void MainMenu() {
        int option = 0;
        Scanner input = new Scanner(System.in);
        do {
            menu();
            option = getOption(option, input);
            switch (option) {
                case 1:
                    Car car = Car.createCar();
                    Car.addCarToList(car);
                    break;
                case 2:
                    displayAllCars();
                    break;
                case 3:
                    autoPart part = autoPart.createPart();
                    autoPart.addPartToList(part);
                    break;
                case 4:
                    displayAllParts();
                    break;
                case 5:
                    Car.deleteCar();
                    break;
                case 6:
                    autoPart.deletePart();
                    break;
                case 7:
                    Car.updateCar();
                    break;
                case 8:
                    autoPart.updatePart();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    Menu.mainMenu();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 8);
    }


}
