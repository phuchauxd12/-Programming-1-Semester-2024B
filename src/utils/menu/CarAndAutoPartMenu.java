package utils.menu;

import autoPart.autoPart;
import car.Car;
import data.Database;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import user.*;
import utils.CommonFunc;
import utils.Status;
import utils.UserSession;

import java.time.LocalDate;
import java.util.List;

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
                menuActions.put(2, () -> displayAllCars(null));
                menuActions.put(3, this::addAutoPart);
                menuActions.put(4, () -> displayAllParts(null));
                menuActions.put(5, this::deleteCar);
                menuActions.put(6, this::deletePart);
                menuActions.put(7, this::updateCar);
                menuActions.put(8, this::updatePart);
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

                menuActions.put(1, () -> displayAllCars(Status.AVAILABLE));
                menuActions.put(2, this::searchForCar);
                menuActions.put(3, () -> displayAllParts(Status.AVAILABLE));
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

                menuActions.put(1, () -> displayAllCars(Status.WALK_IN));
                menuActions.put(2, this::addCar);
                menuActions.put(3, () -> displayAllParts(Status.AVAILABLE));
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

                menuActions.put(1, () -> displayAllCars(Status.AVAILABLE));
                menuActions.put(2, () -> displayAllParts(Status.AVAILABLE));
                menuActions.put(3, this::searchForCar);
                menuActions.put(4, this::searchForPart);
                menuActions.put(0, this::exit);
            }
        }
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

        try{
            CommonFunc.addActivityLogForCurrentUser("Search for car with ID: " + carID);
        }catch (Exception e){
            System.out.println("Error logging car action history: " + e.getMessage());
        }
    }

    private void searchForPart() {
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


        try{
            CommonFunc.addActivityLogForCurrentUser("Search for auto part with ID: " + partID);
        }catch (Exception e){
            System.out.println("Error logging auto part action history: " + e.getMessage());
        }

    }


    // MANAGER Functions
    private void addCar() {
        User user = UserSession.getCurrentUser();
        Car car = Car.createCar(user);
        try {
            Car.addCarToList(car);
            CommonFunc.addActivityLogForCurrentUser("Add new car");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void addAutoPart() {
        autoPart part = autoPart.createPart();
        try {
            autoPart.addPartToList(part);
            CommonFunc.addActivityLogForCurrentUser("Add new auto part");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void deleteCar() {
        try {
            Car.deleteCar();
            CommonFunc.addActivityLogForCurrentUser("Delete car");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deletePart() {
        try {
            autoPart.deletePart();
            CommonFunc.addActivityLogForCurrentUser("Delete auto part");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCar() {
        try {
            Car.updateCar();
            CommonFunc.addActivityLogForCurrentUser("Update car");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updatePart() {
        try {
            autoPart.updatePart();
            CommonFunc.addActivityLogForCurrentUser("Update auto part");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            System.out.println("Displaying all cars with " + status + " status:");
            for (Car car : carsList) {

                if (car.getStatus() == status) {
                    System.out.println(car);
                }
            }
        }
        System.out.println("----------------");
        System.out.println("To see detailed information of a specific car, please use the search function!");


        try{
            CommonFunc.addActivityLogForCurrentUser("View all cars");
        }catch (Exception e){
            System.out.println("Error logging car action history: " + e.getMessage());
        }
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

        try{
            CommonFunc.addActivityLogForCurrentUser("View all auto parts");
        }catch (Exception e){
            System.out.println("Error logging auto part action history: " + e.getMessage());
        }
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

        try{
            CommonFunc.addActivityLogForCurrentUser("View number of cars sold from" + startDate + " to " + endDate);
        }catch (Exception e){
            System.out.println("Error logging car action history: " + e.getMessage());
        }
    }

    public static void getAllCarsSoldInSpecificPeriod(LocalDate startDate, LocalDate endDate) {
        for (Car car : carsList) {
            if (car.getStatus() == Status.SOLD && car.getSoldDate().isBefore(endDate) && car.getSoldDate().isAfter(startDate)) {
                System.out.println(car);
            }
        }
    }
}