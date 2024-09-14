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
                menuActions.put(2, this::displayCars);
                menuActions.put(3, this::addAutoPart);
                menuActions.put(4, this::displayParts);
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

                menuActions.put(1, this::displayCars);
                menuActions.put(2, this::searchForCar);
                menuActions.put(3, this::displayParts);
                menuActions.put(4, this::searchForPart);
                menuActions.put(0, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Display all cars"); // that are for repair
                menuItems.put(2, "Add car"); // only able to add cars for walk in
                menuItems.put(3, "Display all auto parts"); // that are for sale
                menuItems.put(4, "Search for car"); // that are not deleted
                menuItems.put(5, "Search for auto part"); // that are not deleted
                menuItems.put(0, "Exit");

                menuActions.put(1, this::displayCars);
                menuActions.put(2, this::addCar);
                menuActions.put(3, this::displayParts);
                menuActions.put(4, this::searchForCar);
                menuActions.put(5, this::searchForPart);
                menuActions.put(0, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Display all cars"); // Display cars that are for sale
                menuItems.put(2, "Display all auto parts"); // Display auto parts that are for sale
                menuItems.put(3, "Search for car"); // that are not deleted
                menuItems.put(4, "Search for auto part"); // that are not deleted
                menuItems.put(0, "Exit");

                menuActions.put(1, this::displayCars);
                menuActions.put(2, this::displayParts);
                menuActions.put(3, this::searchForCar);
                menuActions.put(4, this::searchForPart);
                menuActions.put(0, this::exit);
            }
        }
    }


    private void searchForCar() {
        displayCars();
        input.nextLine();
        System.out.println("Enter the car ID:");
        String carID = input.nextLine();
        Car car = findCarByID(carID);
        if (car != null) {
            if (!(currentUser instanceof Manager) && car.isDeleted()) {
                System.out.println("Car not found.");
                return;
            }
            if (currentUser instanceof Mechanic && car.getClientID() == null) {
                System.out.println("You are not authorized to view this car.");
                return;
            }
            if (currentUser instanceof Client && !car.getClientID().equals(currentUser.getUserID())) {
                System.out.println("You are not authorized to view this car.");
                return;
            }
            System.out.println("----------------");
            System.out.println(car.toStringDetailed());
            System.out.println("----------------");
        } else {
            System.out.println("Car not found.");
        }

        try {
            ActivityLogMenu.addActivityLogForCurrentUser("Searched for car with ID: " + carID);
        } catch (Exception e) {
            System.out.println("Error logging car action history: " + e.getMessage());
        }
    }

    private void searchForPart() {
        displayParts();
        input.nextLine();
        System.out.println("Enter the part ID:");
        String partID = input.nextLine();
        autoPart part = findAutoPartByID(partID);
        if (part != null) {
            if (!(currentUser instanceof Manager) && part.isDeleted()) {
                System.out.println("Part not found.");
                return;
            }
            System.out.println("----------------");
            System.out.println(part.toStringDetailed());
            System.out.println("----------------");
        } else {
            System.out.println("Part not found.");
        }


        try {
            ActivityLogMenu.addActivityLogForCurrentUser("Searched for auto part with ID: " + partID);
        } catch (Exception e) {
            System.out.println("Error logging auto part action history: " + e.getMessage());
        }

    }


    // MANAGER Functions
    // Menu Functions
    private void addCar() {
        Car car = createCar();
        try {
            ActivityLogMenu.addActivityLogForCurrentUser("Added new car with ID: " + car.getCarID());
            addCarToList(car);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addAutoPart() {
        autoPart part = createPart();
        try {
            addPartToList(part);
            ActivityLogMenu.addActivityLogForCurrentUser("Added new auto part with ID: " + part.getPartID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteCarWrapper() {
        try {
            carsList.stream().filter(car -> !car.isDeleted()).forEach(System.out::println); // Get all cars that are not deleted
            Scanner input = new Scanner(System.in);
            System.out.println("Enter the car ID of the car you want to delete:");
            String carID = input.next();
            Car car = findCarByID(carID);
            deleteCar(car);
            ActivityLogMenu.addActivityLogForCurrentUser("Deleted car with ID: " + carID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deletePartWrapper() {
        try {
            autoPartsList.stream().filter(part -> !part.isDeleted()).forEach(System.out::println); // Get all parts that are not deleted
            Scanner input = new Scanner(System.in);
            System.out.println("Enter the part ID of the part you want to delete: ");
            String partID = input.next();
            autoPart part = CarAndAutoPartMenu.findAutoPartByID(partID);
            deletePart(part);
            ActivityLogMenu.addActivityLogForCurrentUser("Deleted auto part with ID: " + partID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCarWrapper() {
        try {
            displayCars();
            Scanner input = new Scanner(System.in);
            System.out.println("Please input the car's ID to update:");
            String carID = input.next();
            Car car = findCarByID(carID);
            if (car == null) {
                System.out.println("Not found/invalid car ID. Please try again.");
            } else {
                updateCar(car);
            }

            ActivityLogMenu.addActivityLogForCurrentUser("Attempted to update car with ID: " + carID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePartWrapper() {
        try {
            displayParts();
            Scanner input = new Scanner(System.in);
            System.out.println("Please input the part's ID to update:");
            String partID = input.next();
            autoPart part = findAutoPartByID(partID);
            if (part == null) {
                System.out.println("Not found/invalid car ID. Please try again.");
            } else {
                updatePart(part);
            }

            ActivityLogMenu.addActivityLogForCurrentUser("Attempted to update auto part with ID: " + partID);
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

    public void displayCars() {
        int deletedOption = 0;
        User user = UserSession.getCurrentUser();
        if (user instanceof Manager) {
            System.out.println("Would you like to include deleted cars?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            while (true) {
                try {
                    System.out.println("Enter an option: ");
                    deletedOption = input.nextInt();
                    if (deletedOption == 1 || deletedOption == 2) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please input 1 or 2");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please try again.");
                    input.nextLine(); // Clear the invalid input
                }
            }
            boolean includeDeleted = deletedOption == 1;
            if (includeDeleted) {
                carsList.forEach(System.out::println);
            } else {
                carsList.stream()
                        .filter(car -> !car.isDeleted())
                        .forEach(System.out::println);
            }
        } else if (user instanceof Mechanic) {
            System.out.println("Displaying all cars that are available for repair:");
            carsList.stream()
                    .filter(car -> car.getClientID() != null && !car.isDeleted())
                    .forEach(System.out::println);
        } else {
            System.out.println("Displaying all cars for sale:");
            carsList.stream()
                    .filter(car -> car.getStatus() == Status.AVAILABLE && !car.isDeleted())
                    .forEach(System.out::println);
            if (user instanceof Client) {
                System.out.println("Displaying all cars that you own:");
                carsList.stream()
                        .filter(car -> car.getClientID() != null && car.getClientID().equals(user.getUserID()))
                        .forEach(System.out::println);
            }
        }
        System.out.println("----------------");
        System.out.println("To see detailed information of a specific car, please use the search function!");


        try {
            ActivityLogMenu.addActivityLogForCurrentUser("Viewed all cars");
        } catch (Exception e) {
            System.out.println("Error logging car action history: " + e.getMessage());
        }
    }

    public void displayParts() {
        int option = 0;
        int statusOption = 0;
        int deletedOption = 0;
        User user = UserSession.getCurrentUser();
        if (user instanceof Manager) {
            System.out.println("Would you like to include deleted parts?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            while (true) {
                try {
                    System.out.println("Enter an option: ");
                    deletedOption = input.nextInt();
                    if (deletedOption == 1 || deletedOption == 2) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please input 1 or 2");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please try again.");
                    input.nextLine(); // Clear the invalid input
                }
            }
            boolean includeDeleted = deletedOption == 1;
            if (includeDeleted) {
                autoPartsList.forEach(System.out::println);
            } else {
                autoPartsList.stream()
                        .filter(part -> !part.isDeleted())
                        .forEach(System.out::println);
            }
        } else {
            System.out.println("Displaying all parts for sale:");
            autoPartsList.stream()
                    .filter(part -> part.getStatus() == Status.AVAILABLE && !part.isDeleted())
                    .forEach(System.out::println);
        }
        System.out.println("----------------");
        System.out.println("To see detailed information of a specific part, please use the search function!");

        try {
            ActivityLogMenu.addActivityLogForCurrentUser("Viewed all auto parts");
        } catch (Exception e) {
            System.out.println("Error logging auto part action history: " + e.getMessage());
        }
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
        String clientID = null;
        if (status == Status.WALK_IN) {
            UserMenu.getUserList().stream().filter(users -> users instanceof Client).forEach(System.out::println);
            System.out.println("Please input the client's ID of this car:");
            while (true) {
                clientID = input.next();
                User client = UserMenu.getUserById(clientID);
                if (client instanceof Client) {
                    break;
                } else {
                    System.out.println("Invalid client ID. Please try again.");
                }
            }
        }
        input.nextLine();
        System.out.println("Please input the car's make:");
        String carMake = input.nextLine();
        System.out.println("Please input the car's model:");
        String carModel = input.nextLine();
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
        if (status == Status.WALK_IN) {
            newCar.setClientID(clientID);
        }
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
            input.nextLine();
            switch (option) {
                case 1:
                    System.out.println("The current car's make: " + car.getCarMake());
                    System.out.println("Please input the car's make:");
                    String newCarMake = input.nextLine();
                    car.setCarMake(newCarMake);
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 2:
                    System.out.println("The current car's model: " + car.getCarModel());
                    System.out.println("Please input the car's model:");
                    String newCarModel = input.nextLine();
                    car.setCarModel(newCarModel);
                    System.out.println("Updated successfully!");
                    System.out.println(car);
                    break;
                case 3:
                    System.out.println("The current car's year: " + car.getCarYear());
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

    public static int getNewCarYear(Scanner input) {
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


    // AutoPart CRUD functions
    public static autoPart createPart() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter part name: ");
        String partName = input.nextLine();
        System.out.println("Enter part manufacturer: ");
        String partManufacturer = input.nextLine();
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

    public void deletePart(autoPart part) throws Exception {
        if (part == null) {
            System.out.println("Part not found.");
            return;
        }
        part.setDeleted(true);
        AutoPartDatabase.saveAutoPartData(autoPartsList);
        System.out.println("Part deleted successfully.");
        System.out.println(part);
    }

    public void updatePart(autoPart part) throws Exception {
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
            input.nextLine();
            switch (option) {
                case 1:
                    System.out.println("Enter new part name: ");
                    part.setPartName(input.nextLine());
                    System.out.println("Part name updated successfully.");
                    System.out.println(part);
                    break;
                case 2:
                    System.out.println("Enter new part manufacturer: ");
                    part.setPartManufacturer(input.nextLine());
                    System.out.println("Part manufacturer updated successfully.");
                    System.out.println(part);
                    break;
                case 3:
                    System.out.println("Enter new part condition (NEW, USED, REFURBISHED): ");
                    autoPart.Condition condition;
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


    public static void getNumberOfCarsSoldInSpecificPeriod(LocalDate startDate, LocalDate endDate) {
        int carSold = 0;
        for (Car car : carsList) {
            if (car.getStatus() == Status.SOLD && car.getSoldDate() != null && (car.getSoldDate().isBefore(endDate) || car.getSoldDate().isEqual(endDate))
                    && (car.getSoldDate().isAfter(startDate) || car.getSoldDate().isEqual(startDate))) {
                carSold += 1;
            }
        }
        System.out.println("Number of cars sold between " + startDate + " and " + endDate + ": " + carSold);
    }

    public static void getAllCarsSoldInSpecificPeriod(LocalDate startDate, LocalDate endDate) {
        for (Car car : carsList) {
            if (car.getStatus() == Status.SOLD && car.getSoldDate() != null && (car.getSoldDate().isBefore(endDate) || car.getSoldDate().isEqual(endDate)) && (car.getSoldDate().isAfter(startDate)
                    || car.getSoldDate().isEqual(startDate))) {
                System.out.println(car);
            }
        }
    }
}