package utils.menu;

import autoPart.autoPart;
import car.Car;
import data.Database;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import user.*;
import utils.Status;
import utils.UserSession;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CarAndAutoPartMenu extends Menu {


    public CarAndAutoPartMenu() {
        super();
        switch (currentUser) {
            case Manager c -> initializeMenu(MenuOption.MANAGER);
            case Salesperson c -> initializeMenu(MenuOption.SALESPERSON);
            case Mechanic c -> initializeMenu(MenuOption.MECHANIC);
            case Client c -> initializeMenu(MenuOption.CLIENT);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    @Override
    protected void initializeMenu(MenuOption menuOption) {
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
                menuItems.put(0, "Exit");

                menuActions.put(1, this::addCar);
                menuActions.put(2, this::displayAllCars);
                menuActions.put(3, this::addAutoPart);
                menuActions.put(4, this::displayAllParts);
                menuActions.put(5, this::deleteCarWrapper);
                menuActions.put(6, this::deletePartWrapper);
                menuActions.put(7, this::updateCarWrapper);
                menuActions.put(8, this::updatePartWrapper);
                menuActions.put(9, this::searchForCar);
                menuActions.put(10, this::searchForPart);
                menuActions.put(0, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Display all cars"); // that are for sale
                menuItems.put(2, "Search for a car");
                menuItems.put(3, "Display all auto parts"); // that are for sale
                menuItems.put(4, "Search for an auto part");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::displayAllCars);
                menuActions.put(2, this::searchForCar);
                menuActions.put(3, this::displayAllParts);
                menuActions.put(4, this::searchForPart);
                menuActions.put(0, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Display all cars");
                menuItems.put(2, "Add car");
                menuItems.put(3, "Display all auto parts");
                menuItems.put(4, "Search for car");
                menuItems.put(5, "Search for auto part");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::displayAllCars);
                menuActions.put(2, this::addCar);
                menuActions.put(3, this::displayAllParts);
                menuActions.put(4, this::searchForCar);
                menuActions.put(5, this::searchForPart);
                menuActions.put(0, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Display all cars"); // Display cars that are for sale
                menuItems.put(2, "Display all auto parts"); // Display auto parts that are for sale
                menuItems.put(3, "Search for car");
                menuItems.put(4, "Search for auto part");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::displayAllCars);
                menuActions.put(2, this::displayAllParts);
                menuActions.put(3, this::searchForCar);
                menuActions.put(4, this::searchForPart);
                menuActions.put(0, this::exit);
            }
        }
    }


    // Menu Functions
    private void addCar() {
        Car car = createCar();
        try {
            addCarToList(car);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void addAutoPart() {
        autoPart part = createPart();
        try {
            addPartToList(part);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void deleteCarWrapper() {
        try {
            displayAllCars();
            Scanner input = new Scanner(System.in);
            System.out.println("Please input the car's ID to delete:");
            String carID = input.next();
            Car car = findCarByID(carID);
            deleteCar(car);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deletePartWrapper() {
        try {
            deletePart();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCarWrapper() {
        try {
            displayAllCars();
            Car car = null;
            Scanner input = new Scanner(System.in);
            while (car == null) {
                System.out.println("Please input the car's ID to update:");
                String carID = input.next();
                car = CarAndAutoPartMenu.findCarByID(carID);
                if (car == null) {
                    System.out.println("Not found/invalid car ID. Please try again.");
                }
            }
            updateCar(car);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePartWrapper() {
        try {
            updatePart();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    // List functions
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

    public void displayAllCars() {
        User user = UserSession.getCurrentUser();
        if (user instanceof Manager) {
            Status status = null;
            System.out.println("Would you like to filter your search?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            try {
                System.out.println("Enter an option: ");
                int option = input.nextInt();
                if (option == 1) {
                    System.out.println("Filter by status:");
                    System.out.println("1. Available");
                    System.out.println("2. Sold");
                    System.out.println("3. Walk-in");
                    System.out.println("Enter an option: ");
                    int statusOption = input.nextInt();
                    switch (statusOption) {
                        case 1 -> status = Status.AVAILABLE;
                        case 2 -> status = Status.SOLD;
                        case 3 -> status = Status.WALK_IN;
                        default -> System.out.println("Invalid option. Displaying all cars.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Displaying all cars.");
                input.nextLine(); // Clear the invalid input
            }

            if (status != null) {
                System.out.println("Displaying all cars with " + status + " status:");
                Status finalStatus = status;
                carsList.stream()
                        .filter(car -> car.getStatus() == finalStatus)
                        .forEach(System.out::println);
            } else {
                System.out.println("Displaying all cars:");
                carsList.forEach(System.out::println);
            }
        } else if (user instanceof Mechanic) {
            System.out.println("Displaying all cars with walk-in status:");
            carsList.stream()
                    .filter(car -> car.getStatus() == Status.WALK_IN)
                    .forEach(System.out::println);
        } else {
            System.out.println("Displaying all cars for sale:");
            carsList.stream()
                    .filter(car -> car.getStatus() == Status.AVAILABLE)
                    .forEach(System.out::println);
        }
        System.out.println("----------------");
        System.out.println("To see detailed information of a specific car, please use the search function!");
    }

    public void displayAllParts() {
        User user = UserSession.getCurrentUser();
        if (user instanceof Manager) {
            Status status = null;
            System.out.println("Would you like to filter your search?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            try {
                System.out.println("Enter an option: ");
                int option = input.nextInt();
                if (option == 1) {
                    System.out.println("Filter by status:");
                    System.out.println("1. Available");
                    System.out.println("2. Sold");
                    System.out.println("Enter an option: ");
                    int statusOption = input.nextInt();
                    switch (statusOption) {
                        case 1 -> status = Status.AVAILABLE;
                        case 2 -> status = Status.SOLD;
                        default -> System.out.println("Invalid option. Displaying all cars.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Displaying all cars.");
                input.nextLine(); // Clear the invalid input
            }
            if (status != null) {
                System.out.println("Displaying all parts with " + status + " status:");
                Status finalStatus = status;
                autoPartsList.stream()
                        .filter(car -> car.getStatus() == finalStatus)
                        .forEach(System.out::println);
            } else {
                System.out.println("Displaying all cars:");
                autoPartsList.forEach(System.out::println);
            }
        } else {
            System.out.println("Displaying all parts for sale:");
            autoPartsList.stream()
                    .filter(part -> part.getStatus() == Status.AVAILABLE)
                    .forEach(System.out::println);
        }
        System.out.println("----------------");
        System.out.println("To see detailed information of a specific part, please use the search function!");
    }

    // Car CRUD functions
    public static Car createCar() {
        User user = UserSession.getCurrentUser();
        Scanner input = new Scanner(System.in);
        Status status;
        if (user instanceof Manager) {
            int option;
            while (true) {
                System.out.println("Car for sale or a customer car for repair?");
                System.out.println("1. New Car for Sale");
                System.out.println("2. Car for Repair");
                try {
                    System.out.println("Enter an option: ");
                    option = input.nextInt();
                    if (option == 1 || option == 2) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please input 1 or 2.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please input 1 or 2.");
                    input.nextLine(); // Clear the invalid input
                }
            }
            status = switch (option) {
                case 1 -> Status.AVAILABLE;
                case 2 -> Status.WALK_IN;
                default -> throw new IllegalStateException("Unexpected value: " + option);
            };
        } else if (user instanceof Mechanic) {
            status = Status.WALK_IN;
        } else {
            status = Status.AVAILABLE;
        }
        System.out.println("Please input the car's make:");
        String carMake = input.next();
        System.out.println("Please input the car's model:");
        String carModel = input.next();
        int carYear = CarAndAutoPartMenu.getNewCarYear(input);
        System.out.println("Please input the car's color:");
        String color = input.next().toUpperCase();
        double mileage;
        while (true) {
            try {
                System.out.println("Please input the car's mileage:");
                mileage = input.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please input a valid mileage");
                input.nextLine();
            }
        }
        double price;
        while (true) {
            try {
                System.out.println("Please input the car's price:");
                price = input.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please input a valid price");
                input.nextLine();
            }
        }
        input.nextLine();
        System.out.println("Please input any additional notes:");
        String addNotes = input.nextLine();
        Car newCar = new Car(carMake, carModel, carYear, color, mileage, price, addNotes, status);
        System.out.println("Car created successfully!");
        System.out.println(newCar);
        return newCar;
    }

    public void deleteCar(Car car) throws Exception {
        if (car != null) {
            car.setDeleted(true);
            CarDatabase.saveCarData(carsList);
            System.out.println("Car deleted successfully!");
        } else {
            System.out.println("Car not found.");
        }
    }

    public static void updateCar(Car car) throws Exception {
        int option = 100;
        do {
            System.out.println("What would you like to update?");
            System.out.println("1. Car Make");
            System.out.println("2. Car Model");
            System.out.println("3. Car Year");
            System.out.println("4. Car Color");
            System.out.println("5. Car Mileage");
            System.out.println("6. Car Price");
            System.out.println("7. Additional Notes");
            System.out.println("0. Exit");
            option = MainMenu.getOption(option, input);
            switch (option) {
                case 1:
                    System.out.println("The current car's make: " + car.getCarMake());
                    System.out.println("Please input the car's make:");
                    String newCarMake = input.next();
                    car.setCarMake(newCarMake);
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 2:
                    System.out.println("The current car's model: " + car.getCarModel());
                    System.out.println("Please input the car's model:");
                    String newCarModel = input.next();
                    car.setCarModel(newCarModel);
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 3:
                    System.out.println("The current car's year: " + car.getCarModel());
                    int newCarYear;
                    newCarYear = CarAndAutoPartMenu.getNewCarYear(input);
                    car.setCarYear(newCarYear);
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 4:
                    System.out.println("The current car's color: " + car.getColor());
                    System.out.println("Please input the car's color:");
                    car.setColor(input.next().toUpperCase());
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 5:
                    System.out.println("The current car's mileage: " + car.getMileage());
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
                    car.setMileage(newMileage);
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 6:
                    System.out.println("The current car's price: " + car.getPrice());
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
                    car.setPrice(newPrice);
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 7:
                    System.out.println("The current additional notes: " + car.getAddNotes());
                    input.nextLine();
                    System.out.println("Please input any additional notes:");
                    car.setAddNotes(input.nextLine());
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 0:
                    CarDatabase.saveCarData(carsList);// Save into database
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (option != 0);
    }

    private static int getNewCarYear(Scanner input) {
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
        return newCarYear;
    }

    public static void addCarToList(Car car) throws Exception {
        carsList.add(car);
        CarDatabase.saveCarData(carsList);
        System.out.println("Car successfully added to list!");
    }

    private void searchForCar() {
        input.nextLine();
        System.out.println("Enter the car ID:");
        String carID = input.nextLine();
        Car car = findCarByID(carID);
        if (car != null) {
            System.out.println("----------------");
            System.out.println(car.toStringDetailed());
            System.out.println("----------------");
        } else {
            System.out.println("Car not found.");
        }
    }


    // AutoPart CRUD functions
    public static autoPart createPart() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter part name: ");
        String partName = input.next();
        System.out.println("Enter part manufacturer: ");
        String partManufacturer = input.next();
        System.out.println("Enter part condition (NEW, USED, REFURBISHED): ");
        autoPart.Condition condition;
        try {
            condition = autoPart.Condition.valueOf(input.next().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid condition. Please enter NEW, USED, or REFURBISHED.");
            condition = autoPart.Condition.valueOf(input.next().toUpperCase());
        }
        System.out.println("Enter warranty duration (in months): ");
        int warrantyMonths;
        while (true) {
            try {
                warrantyMonths = input.nextInt();
                break; // Exit loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next(); // Clear the invalid input from the scanner buffer
            }
        }
        double price;
        System.out.println("Enter price: ");
        while (true) {
            try {
                price = input.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }
        }
        input.nextLine();
        System.out.println("Enter additional notes: ");
        String addNotes = input.nextLine();
        autoPart part = new autoPart(partName, partManufacturer, condition, warrantyMonths, price, addNotes);
        System.out.println("Part created successfully.");
        System.out.println(part);
        return part;
    }

    public static void addPartToList(autoPart part) throws Exception {
        autoPartsList.add(part);
        AutoPartDatabase.saveAutoPartData(autoPartsList);
        System.out.println("Part successfully added to list!");
    }

    public void deletePart() throws Exception {
        displayAllParts();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the part ID of the part you want to delete: ");
        String partID = input.next();
        autoPart part = CarAndAutoPartMenu.findAutoPartByID(partID);
        if (part == null) {
            System.out.println("Part not found.");
            return;
        }
        part.setDeleted(true);
        AutoPartDatabase.saveAutoPartData(autoPartsList);
        System.out.println("Part deleted successfully.");
        System.out.println(part);
    }

    public void updatePart() throws Exception {
        displayAllParts();
        autoPart part = null;
        Scanner input = new Scanner(System.in);
        while (part == null) {
            System.out.println("Enter the part ID of the part you want to update: ");
            String partID = input.next();
            part = CarAndAutoPartMenu.findAutoPartByID(partID);
            if (part == null) {
                System.out.println("Not found/invalid part ID. Please try again.");
            }
        }
        int option = 0;
        while (option != 7) {
            System.out.println("What would you like to update?");
            System.out.println("1. Part Name");
            System.out.println("2. Part Manufacturer");
            System.out.println("3. Condition");
            System.out.println("4. Warranty Duration");
            System.out.println("5. Price");
            System.out.println("6. Additional Notes");
            System.out.println("7. Exit");
            option = MainMenu.getOption(option, input);
            switch (option) {
                case 1:
                    System.out.println("Enter new part name: ");
                    part.setPartName(input.next());
                    System.out.println("Part name updated successfully.");
                    System.out.println(part);
                    break;
                case 2:
                    System.out.println("Enter new part manufacturer: ");
                    part.setPartManufacturer(input.next());
                    System.out.println("Part manufacturer updated successfully.");
                    System.out.println(part);
                    break;
                case 3:
                    System.out.println("Enter new part condition (NEW, USED, REFURBISHED): ");
                    autoPart.autoPart.Condition condition;
                    try {
                        condition = autoPart.Condition.valueOf(input.next().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid condition. Please enter NEW, USED, or REFURBISHED.");
                        condition = autoPart.Condition.valueOf(input.next().toUpperCase());
                    }
                    part.setCondition(condition);
                    System.out.println("Part condition updated successfully.");
                    System.out.println(part);
                    break;
                case 4:
                    System.out.println("Enter new warranty duration (in months): ");
                    int warrantyMonths;
                    while (true) {
                        try {
                            warrantyMonths = input.nextInt();
                            break; // Exit loop if input is valid
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            input.next(); // Clear the invalid input from the scanner buffer
                        }
                    }
                    part.setWarrantyMonths(warrantyMonths);
                    System.out.println("Warranty duration updated successfully.");
                    System.out.println(part);
                    break;
                case 5:
                    System.out.println("Enter new price: ");
                    double price;
                    while (true) {
                        try {
                            price = input.nextDouble();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            input.next();
                        }
                    }
                    part.setPrice(price);
                    System.out.println("Price updated successfully.");
                    System.out.println(part);
                    break;
                case 6:
                    input.nextLine();
                    System.out.println("Enter new additional notes: ");
                    part.setAddNotes(input.nextLine());
                    System.out.println("Additional notes updated successfully.");
                    System.out.println(part);
                    break;
                case 7:
                    AutoPartDatabase.saveAutoPartData(autoPartsList);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void searchForPart() {
        input.nextLine();
        System.out.println("Enter the part ID:");
        String partID = input.nextLine();
        autoPart part = findAutoPartByID(partID);
        if (part != null) {
            System.out.println("----------------");
            System.out.println(part.toStringDetailed());
            System.out.println("----------------");
        } else {
            System.out.println("Part not found.");
        }
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
}