package utils;

import autoPart.autoPart;
import car.Car;
import user.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import static utils.Menu.getOption;

public class CarAndAutoPartMenu {
    private static final Scanner input = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Consumer<Scanner>> menuActions = new LinkedHashMap<>();


    public CarAndAutoPartMenu(User user) {
        switch (user) {
            case Manager manager -> initializeMenu(MenuOption.MANAGER, user);
            case Salesperson salesperson -> initializeMenu(MenuOption.SALESPERSON, user);
            case Mechanic mechanic -> initializeMenu(MenuOption.MECHANIC, user);
            case Client client -> initializeMenu(MenuOption.CLIENT, user);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    private void initializeMenu(MenuOption menuOption, User user) {
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "Add a car");
                menuItems.put(2, "Display all cars");
                menuItems.put(3, "Add an auto part");
                menuItems.put(4, "Display all auto parts");
                menuItems.put(5, "Delete a car");
                menuItems.put(6, "Delete an auto part");
                menuItems.put(7, "Update a car");
                menuItems.put(8, "Update an auto part");
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> addCar(user));
                menuActions.put(2, this::displayAllCars);
                menuActions.put(3, this::addAutoPart);
                menuActions.put(4, this::displayAllParts);
                menuActions.put(5, this::deleteCar);
                menuActions.put(6, this::deletePart);
                menuActions.put(7, _ -> {
                    try {
                        updateCar(user);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                menuActions.put(8, _ -> {
                    try {
                        updatePart(user);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                menuActions.put(10, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Display all cars"); // that are for sale
                menuItems.put(2, "Search for a car");
                menuItems.put(3, "Display all auto parts"); // that are for sale
                menuItems.put(4, "Search for an auto part");
                menuItems.put(10, "Exit");

                menuActions.put(1, s -> displayAllCarsForSale());
                menuActions.put(3, s -> displayAllPartsForSale());
                menuActions.put(10, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Display all cars"); // for walk in only (dunno yet)
                menuItems.put(2, "Add car"); // for walk in only
                menuItems.put(3, "Display all auto parts"); // that are available
                menuItems.put(10, "Exit");

                menuActions.put(1, s -> displayAllCars());
                menuActions.put(2, _ -> addCar(user));
                menuActions.put(3, s -> displayAllPartsForSale());
                menuActions.put(10, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Display all cars"); // Display cars that are for sale
                menuItems.put(2, "Display all auto parts"); // Display auto parts that are for sale
                menuItems.put(10, "Exit");

                menuActions.put(1, s -> displayAllCarsForSale());
                menuActions.put(2, s -> displayAllPartsForSale());
                menuActions.put(10, this::exit);
            }
        }
    }


    // MANAGER Functions
    private void addCar(User user) {
        Car car = Car.createCar(user);
        carsList.add(car);
    }

    private void displayAllCars(Scanner scanner) {
        displayAllCars();
    }

    private void addAutoPart(Scanner scanner) {
        autoPart part = autoPart.createPart();
        autoPartsList.add(part);
    }

    private void displayAllParts(Scanner scanner) {
        displayAllParts();
    }

    private void deleteCar(Scanner scanner) {
        displayAllCars();
        System.out.println("Enter the car ID to delete:");
        String carID = scanner.next();
        Car car = findCarByID(carID);
        if (car != null) {
            car.setDeleted(true);
            System.out.println("Car with ID " + carID + " has been deleted.");
        } else {
            System.out.println("Car with ID " + carID + " not found.");
        }
    }

    private void deletePart(Scanner scanner) {
        displayAllParts();
        System.out.println("Enter the part ID to delete:");
        String partID = scanner.next();
        autoPart part = findAutoPartByID(partID);
        if (part != null) {
            part.setDeleted(true);
            System.out.println("Part with ID " + partID + " has been deleted.");
        } else {
            System.out.println("Part with ID " + partID + " not found.");
        }
    }

    private void updateCar(User user) throws Exception {
        Scanner scanner = new Scanner(System.in);
        displayAllCars();
        System.out.println("Enter the car ID to update:");
        String carID = scanner.next();
        Car car = findCarByID(carID);
        if (car != null) {
            car.updateCar(user);
            System.out.println("Car with ID " + carID + " has been updated.");
        } else {
            System.out.println("Car with ID " + carID + " not found.");
        }
    }

    private void updatePart(User user) throws Exception {
        Scanner scanner = new Scanner(System.in);
        displayAllParts();
        System.out.println("Enter the part ID to update:");
        String partID = scanner.next();
        autoPart part = findAutoPartByID(partID);
        if (part != null) {
            part.updatePart(user);
            System.out.println("Part with ID " + partID + " has been updated.");
        } else {
            System.out.println("Part with ID " + partID + " not found.");
        }
    }

    // Menu functions
    public void displayMenu() {
        System.out.println("Welcome to the Car and Auto Part Menu!");
        System.out.println("---------------------");
        menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
        System.out.println("---------------------");
    }

    public void mainMenu(User user) throws Exception {
        int option = 0;
        do {
            displayMenu();
            option = getOption(option, input);
            menuActions.getOrDefault(option, s -> System.out.println("Invalid option. Please try again.")).accept(input);
        } while (option != 10);
        Menu.mainMenu(user);
    }

    // Static helper functions
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
        for (Car car : carsList) {
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

    public static void displayAllCarsForSale() {
        System.out.println("Displaying all cars for sale:");
        for (Car car : carsList) {
            if (car.getStatus() == Status.AVAILABLE) {
                System.out.println(car);
            }
        }
        System.out.println("----------------");
    }

    public static void displayAllPartsForSale() {
        System.out.println("Displaying all Auto Parts for sale:");
        for (autoPart part : getAutoPartsList()) {
            if (part.getStatus() == Status.AVAILABLE) {
                System.out.println(part);
            }
        }
        System.out.println("----------------");
    }

    public static void getNumberOfCarsSoldInSpecificPeriod(LocalDate startDate, LocalDate endDate) {
        int carSold = 0;
        for (Car car : getCarsList()) {
            if (car.getStatus() == Status.SOLD && car.getSoldDate().isBefore(endDate) && car.getSoldDate().isAfter(startDate)) {
                carSold += 1;
            }
        }
        System.out.println("Number of cars sold between " + startDate + " and " + endDate + ": " + carSold);
    }

    public static void getAllCarsSoldInSpecificPeriod(LocalDate startDate, LocalDate endDate) {
        for (Car car : getCarsList()) {
            if (car.getStatus() == Status.SOLD && car.getSoldDate().isBefore(endDate) && car.getSoldDate().isAfter(startDate)) {
                System.out.println(car);
            }
        }
    }


    private void exit(Scanner s) {
        System.out.println("Exiting...");
    }

}
