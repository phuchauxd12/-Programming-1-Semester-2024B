package utils;

import autoPart.autoPart;
import car.Car;
import data.Database;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import user.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

import static utils.Menu.getOption;

public class CarAndAutoPartMenu {
    private static final Scanner input = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Consumer<Scanner>> menuActions = new LinkedHashMap<>();


    public CarAndAutoPartMenu(User user) {
        switch (user) {
            case Manager _ -> initializeMenu(MenuOption.MANAGER, user);
            case Salesperson _ -> initializeMenu(MenuOption.SALESPERSON, user);
            case Mechanic _ -> initializeMenu(MenuOption.MECHANIC, user);
            case Client _ -> initializeMenu(MenuOption.CLIENT, user);
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
                menuActions.put(7, _ -> updateCar(user));
                menuActions.put(8, _ -> updatePart(user));
                menuActions.put(10, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Display all cars"); // that are for sale
                menuItems.put(2, "Search for a car");
                menuItems.put(3, "Display all auto parts"); // that are for sale
                menuItems.put(4, "Search for an auto part");
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> displayAllCarsForSale());
                menuActions.put(3, _ -> displayAllPartsForSale());
                menuActions.put(10, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Display all cars"); // for walk in only (dunno yet)
                menuItems.put(2, "Add car"); // for walk in only
                menuItems.put(3, "Display all auto parts"); // that are available
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> displayAllCars());
                menuActions.put(2, _ -> addCar(user));
                menuActions.put(3, _ -> displayAllPartsForSale());
                menuActions.put(10, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Display all cars"); // Display cars that are for sale
                menuItems.put(2, "Display all auto parts"); // Display auto parts that are for sale
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> displayAllCarsForSale());
                menuActions.put(2, _ -> displayAllPartsForSale());
                menuActions.put(10, this::exit);
            }
        }
    }


    // MANAGER Functions
    private void addCar(User user) {
        Car car = Car.createCar(user);
        try {
            Car.addCarToList(car);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayAllCars(Scanner scanner) {
        displayAllCars();
    }

    private void addAutoPart(Scanner scanner) {
        autoPart part = autoPart.createPart();
        try {
            autoPart.addPartToList(part);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayAllParts(Scanner scanner) {
        displayAllParts();
    }

    private void deleteCar(Scanner scanner) {
        try {
            Car.deleteCar();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deletePart(Scanner scanner) {
        try {
            autoPart.deletePart();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCar(User user) {
        try {
            Car.updateCar(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePart(User user) {
        try {
            autoPart.updatePart(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
            menuActions.getOrDefault(option, _ -> System.out.println("Invalid option. Please try again.")).accept(input);
        } while (option != 10);
        Menu.mainMenu(user);
    }

    // Static helper functions
    private static List<Car> carsList;

    static {
        try {
            if (!Database.isDatabaseExist(CarDatabase.path)) {
                CarDatabase.createDatabase();
            }
            carsList = CarDatabase.loadCars();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<autoPart> autoPartsList;

    static {
        try {
            if (!Database.isDatabaseExist(AutoPartDatabase.path)) {
                AutoPartDatabase.createDatabase();
            }
            autoPartsList = AutoPartDatabase.loadAutoParts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Car> getCarsList() {
        return carsList;
    }

    public static List<autoPart> getAutoPartsList() {
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
        for (autoPart part : autoPartsList) {
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
        for (Car car : carsList) {
            if (car.getStatus() == Status.SOLD && car.getSoldDate().isBefore(endDate) && car.getSoldDate().isAfter(startDate)) {
                carSold += 1;
            }
        }
        System.out.println("Number of cars sold between " + startDate + " and " + endDate + ": " + carSold);
    }

    public static void getAllCarsSoldInSpecificPeriod(LocalDate startDate, LocalDate endDate) {
        for (Car car : carsList) {
            if (car.getStatus() == Status.SOLD && car.getSoldDate().isBefore(endDate) && car.getSoldDate().isAfter(startDate)) {
                System.out.println(car);
            }
        }
    }


    private void exit(Scanner s) {
        System.out.println("Exiting...");
    }

}
