package data.car;

import car.Car;
import data.Database;
import utils.Menu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CarDatabase {
    private static final String path = "src/data/car/CarAccount.txt";
    private static List<Car> carList;


    public static void createDatabase() throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // Initialize the list of users
            carList = new ArrayList<>();
            Database.<Car>saveDatabase(path, carList, "Database file created", "Error while creating the database file.");
        } else {
            System.out.println("Database file already exists at " + path);
        }
    }

    // Method to load the list of users from the file
    public static List<Car> loadCars() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            carList = (List<Car>) objIn.readObject();
            return carList;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading cars from the database file.");
        }
    }

    // Method to add a new user to the database
    public static void addCar(Car newCar) throws Exception {
        // Load the current list of users
        loadCars();

        // Add the new user to the list
        carList.add(newCar);

        // Save the updated list back to the file
        Database.<Car>saveDatabase(path, carList, "Car had been created and stored in the database.", "Error while saving the updated car list to the database file.");
    }

    public static Car findCarByID(String carID) throws Exception {
        // Load the current list of users
        loadCars();

        // Find the user with the given userID
        var foundCar = carList.stream()
                .filter(user -> user.getCarID().equals(carID))
                .findFirst();
        return foundCar.orElse(null);
    }

    public static void deleteCar(String carID) throws Exception {
        var foundCar = findCarByID(carID);
        // Add the new user to the list
        if (foundCar != null) {
            carList = carList.stream()
                    .filter(user -> !user.getCarID().equals(carID))
                    .collect(Collectors.toList());

            Database.<Car>saveDatabase(path, carList, "Account had been deleted in the database.", "Error while deleting the car in the database file.");

        } else {
            System.out.println("No carID match the account in the database.");
        }
    }

    public static void updateCar(String carID) throws Exception {
        var foundCar = findCarByID(carID);
        if (foundCar != null) {
            Scanner input = new Scanner(System.in);
            boolean continueUpdate = true;
            int option = 0;
            do {
                System.out.println("What would you like to update?");
                System.out.println("1. Car Make");
                System.out.println("2. Car Model");
                System.out.println("3. Car Year");
                System.out.println("4. Car Color");
                System.out.println("5. Car Mileage");
                System.out.println("6. Car Price");
                System.out.println("7. Additional Notes");
                System.out.println("8. Exit");
                option = Menu.getOption(option, input);
                switch (option) {
                    case 1:
                        System.out.println("Please input the car's make:");
                        String newCarMake = input.next();
                        foundCar.setCarMake(newCarMake);
                        System.out.println("Updated successfully!");
                        System.out.println(foundCar);
                        break;
                    case 2:
                        System.out.println("Please input the car's model:");
                        String newCarModel = input.next();
                        foundCar.setCarModel(newCarModel);
                        System.out.println("Updated successfully!");
                        System.out.println(foundCar);
                        break;
                    case 3:
                        int newCarYear;
                        while (true) {
                            try {
                                System.out.println("Please input the car's year:");
                                newCarYear = input.nextInt();
                                if (newCarYear > 1900) {
                                    break;
                                } else {
                                    System.out.println("Invalid input. The year must be over 1900.");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please input a valid year");
                                input.nextLine();
                            }
                        }
                        foundCar.setCarYear(newCarYear);
                        System.out.println("Updated successfully!");
                        System.out.println(foundCar);
                        break;
                    case 4:
                        System.out.println("Please input the car's color:");
                        foundCar.setColor(input.next().toUpperCase());
                        System.out.println("Updated successfully!");
                        System.out.println(foundCar);
                        break;
                    case 5:
                        double newMileage;
                        while (true) {
                            try {
                                System.out.println("Please input the car's mileage:");
                                newMileage = input.nextDouble();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please input a valid mileage");
                                input.nextLine();
                            }
                        }
                        foundCar.setMileage(newMileage);
                        System.out.println("Updated successfully!");
                        System.out.println(foundCar);
                        break;
                    case 6:
                        double newPrice;
                        while (true) {
                            try {
                                System.out.println("Please input the car's price:");
                                newPrice = input.nextDouble();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please input a valid price");
                                input.nextLine();
                            }
                        }
                        foundCar.setPrice(newPrice);
                        System.out.println("Updated successfully!");
                        System.out.println(foundCar);
                        break;
                    case 7:
                        input.nextLine();
                        System.out.println("Please input any additional notes:");
                        foundCar.setAddNotes(input.nextLine());
                        System.out.println("Updated successfully!");
                        System.out.println(foundCar);
                        break;
                    case 8:
                        System.out.println(carList);
                        continueUpdate = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                        break;
                }

            } while ((continueUpdate));

            Database.<Car>saveDatabase(path, carList, "Car had been updated in the database.", "Error while updating the database file.");
        } else {
            System.out.println("No carID match the account in the database.");
        }
    }
}