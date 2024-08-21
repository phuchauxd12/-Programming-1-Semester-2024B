package utils;

import autoPart.autoPart;
import car.Car;
import data.Database;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import user.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import static utils.Menu.getOption;

public class CarAndAutoPartMenu {
    private static final Scanner input = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Consumer<Scanner>> menuActions = new LinkedHashMap<>();


    public CarAndAutoPartMenu(User user, Menu mainMenu) {
        switch (user) {
            case Manager _ -> initializeMenu(MenuOption.MANAGER, user, mainMenu);
            case Salesperson _ -> initializeMenu(MenuOption.SALESPERSON, user, mainMenu);
            case Mechanic _ -> initializeMenu(MenuOption.MECHANIC, user, mainMenu);
            case Client _ -> initializeMenu(MenuOption.CLIENT, user, mainMenu);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    private void initializeMenu(MenuOption menuOption, User user, Menu mainMenu) {
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
                menuItems.put(9, "Search for car");
                menuItems.put(10, "Search for auto part");
                menuItems.put(100, "Exit");

                menuActions.put(1, _ -> addCar(user));
                menuActions.put(2, _ -> displayAllCars(null));
                menuActions.put(3, this::addAutoPart);
                menuActions.put(4, _ -> displayAllParts(null));
                menuActions.put(5, this::deleteCar);
                menuActions.put(6, this::deletePart);
                menuActions.put(7, _ -> updateCar(mainMenu));
                menuActions.put(8, _ -> updatePart(mainMenu));
                menuActions.put(9, _ -> searchForCar());
                menuActions.put(10, _ -> searchForPart());
                menuActions.put(100, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Display all cars"); // that are for sale
                menuItems.put(2, "Search for a car");
                menuItems.put(3, "Display all auto parts"); // that are for sale
                menuItems.put(4, "Search for an auto part");
                menuItems.put(100, "Exit");

                menuActions.put(1, _ -> displayAllCars(Status.AVAILABLE));
                menuActions.put(2, _ -> searchForCar());
                menuActions.put(3, _ -> displayAllParts(Status.AVAILABLE));
                menuActions.put(4, _ -> searchForPart());
                menuActions.put(100, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Display all cars");
                menuItems.put(2, "Add car");
                menuItems.put(3, "Display all auto parts");
                menuItems.put(4, "Search for car");
                menuItems.put(5, "Search for auto part");
                menuItems.put(100, "Exit");

                menuActions.put(1, _ -> displayAllCars(Status.WALK_IN));
                menuActions.put(2, _ -> addCar(user));
                menuActions.put(3, _ -> displayAllParts(Status.AVAILABLE));
                menuActions.put(4, _ -> searchForCar());
                menuActions.put(5, _ -> searchForPart());
                menuActions.put(100, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Display all cars"); // Display cars that are for sale
                menuItems.put(2, "Display all auto parts"); // Display auto parts that are for sale
                menuItems.put(3, "Search for car");
                menuItems.put(4, "Search for auto part");
                menuItems.put(100, "Exit");

                menuActions.put(1, _ -> displayAllCars(Status.AVAILABLE));
                menuActions.put(2, _ -> displayAllParts(Status.AVAILABLE));
                menuActions.put(3, _ -> searchForCar());
                menuActions.put(4, _ -> searchForPart());
                menuActions.put(100, this::exit);
            }
        }
    }

    private void searchForCar() {
        System.out.println("Enter the car ID:");
        String carID = input.nextLine();
        Car car = findCarByID(carID);
        if (car != null) {
            System.out.println(car.toStringDetailed());
        } else {
            System.out.println("Car not found.");
        }
    }

    private void searchForPart() {
        System.out.println("Enter the part ID:");
        String partID = input.nextLine();
        autoPart part = findAutoPartByID(partID);
        if (part != null) {
            System.out.println(part.toStringDetailed());
        } else {
            System.out.println("Part not found.");
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


    private void addAutoPart(Scanner scanner) {
        autoPart part = autoPart.createPart();
        try {
            autoPart.addPartToList(part);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    private void updateCar(Menu mainMenu) {
        try {
            Car.updateCar();
            mainMenu(mainMenu);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePart(Menu mainMenu) {
        try {
            autoPart.updatePart();
            mainMenu(mainMenu);
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

    public void mainMenu(Menu mainMenu) throws Exception {
        int option = 0;
        do {
            displayMenu();
            option = getOption(option, input);
            menuActions.getOrDefault(option, _ -> System.out.println("Invalid option. Please try again.")).accept(input);
        } while (option != 100);
        mainMenu.mainMenu();
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

    public static void displayAllCars(Status status) {
        if (status == null) {
            System.out.println("Displaying all cars:");
            for (Car car : carsList) {
                System.out.println(car);
            }
        } else {
            System.out.println("Displaying all cars with" + status + " status:");
            for (Car car : carsList) {

                if (car.getStatus() == status) {
                    System.out.println(car);
                }
            }
        }
        System.out.println("----------------");
        System.out.println("To see detailed information of a specific car, please use the search function!");
    }

    public static void displayAllParts(Status status) {
        if (status == null) {
            System.out.println("Displaying all Auto Parts:");
            for (autoPart part : autoPartsList) {
                System.out.println(part);
            }
        } else {
            System.out.println("Displaying all Auto Parts with" + status + " status:");
            for (autoPart part : autoPartsList) {
                if (part.getStatus() == status) {
                    System.out.println(part);
                }
            }
        }
        System.out.println("----------------");
        System.out.println("To see detailed information of a specific part, please use the search function!");
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