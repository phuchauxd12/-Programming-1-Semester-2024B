package utils;

import user.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private static final Scanner input = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Runnable> menuActions = new LinkedHashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final User currentUser;
    private final CarAndAutoPartMenu carAndAutoPartMenu;
    private final StatisticsMenu statisticsMenu;
    private final SaleTransactionMenu saleTransactionMenu;
    private final ServiceMenu serviceMenu;

    public Menu() {
        this.currentUser = UserSession.getCurrentUser();
        carAndAutoPartMenu = new CarAndAutoPartMenu(currentUser);
        statisticsMenu = new StatisticsMenu(currentUser);
        saleTransactionMenu = new SaleTransactionMenu();
        serviceMenu = new ServiceMenu();
        switch (currentUser) {
            case Manager _ -> initializeMenu(MenuOption.MANAGER);
            case Salesperson _ -> initializeMenu(MenuOption.SALESPERSON);
            case Mechanic _ -> initializeMenu(MenuOption.MECHANIC);
            case Client _ -> initializeMenu(MenuOption.CLIENT);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    private void initializeMenu(MenuOption menuOption) {
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Transaction Menu");
                menuItems.put(4, "Service Menu");
                menuItems.put(5, "Statistics Menu");
                menuItems.put(6, "Manage Users Menu");
                menuItems.put(7, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::transactionMenu);
                menuActions.put(4, this::serviceMenu);
                menuActions.put(5, this::statisticsMenu);
                menuActions.put(6, this::manageUsersMenu);
                menuActions.put(7, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Transaction Menu");
                menuItems.put(4, "Statistics Menu");
                menuItems.put(5, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::transactionMenu);
                menuActions.put(4, this::statisticsMenu);
                menuActions.put(5, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Service Menu");
                menuItems.put(4, "Statistics Menu");
                menuItems.put(5, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::serviceMenu);
                menuActions.put(4, this::statisticsMenu);
                menuActions.put(5, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Statistics Menu");
                menuItems.put(4, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::statisticsMenu);
                menuActions.put(4, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
        }
    }


    // Menu actions
    private void activityLogMenu() {
        System.out.println("Activity Log Menu");
        // Get the activity log menu
    }

    private void manageUsersMenu() {
        if (currentUser instanceof Manager) {
            try {
                UserMenu.mainMenu(this);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("You do not have permission to access this menu.");
        }
    }

    private void statisticsMenu() {
        try {
            statisticsMenu.mainMenu(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void serviceMenu() {
        try {
            serviceMenu.mainMenu(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void transactionMenu() {
        try {
            saleTransactionMenu.mainMenu(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void carAndAutoPartMenu() {
        try {
            carAndAutoPartMenu.mainMenu(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void manageProfileMenu() {
        try {
            UserProfileMenu.mainMenu(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void exit() {
        System.out.println("Exiting the program...");
    }

    // menu functions
    public void displayMenu() {
        System.out.println("Welcome to the Main Menu!");
        System.out.println("---------------------");
        menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
        System.out.println("---------------------");
    }

    public void mainMenu() {
        int option = 100;
        do {
            displayMenu();
            option = getOption(option, input);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
        System.exit(0);
    }

    // Static helper functions
    public static LocalDate getStartDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate;
        while (true) {
            System.out.print("Enter start date (dd/MM/yyyy): ");
            try {
                String input = sanitizeDateInput(scanner.nextLine());
                startDate = validateAndParseDate(input);
                break;
            } catch (DateTimeParseException | IllegalArgumentException | IndexOutOfBoundsException e) {
                System.out.println("Invalid start date. Please try again.");
            }
        }
        return startDate;
    }

    public static LocalDate getEndDate(LocalDate startDate) {
        Scanner scanner = new Scanner(System.in);
        LocalDate endDate;
        while (true) {
            System.out.print("Enter end date (dd/MM/yyyy): ");
            try {
                String input = sanitizeDateInput(scanner.nextLine());
                endDate = validateAndParseDate(input);
                if (!endDate.isBefore(startDate)) {
                    break;
                }
                System.out.println("End date cannot be before start date. Please try again.");
            } catch (DateTimeParseException | IllegalArgumentException | IndexOutOfBoundsException e) {
                System.out.println("Invalid end date. Please try again.");
            }
        }
        return endDate;
    }

    public static String sanitizeDateInput(String input) {
        // Split the input by "/"
        String[] dateParts = input.split("/");

        // Ensure each part (day, month, year) is correctly formatted
        String day = (dateParts[0].length() == 1) ? "0" + dateParts[0] : dateParts[0];
        String month = (dateParts[1].length() == 1) ? "0" + dateParts[1] : dateParts[1];
        String year = dateParts[2];

        // Join back the sanitized parts into a correctly formatted date string
        return day + "/" + month + "/" + year;
    }

    public static LocalDate validateAndParseDate(String dateStr) {
        LocalDate parsedDate = LocalDate.parse(dateStr, formatter);
        // Additional validation to ensure the date is legitimate (e.g., no 30th February)
        if (!parsedDate.format(formatter).equals(dateStr)) {
            throw new IllegalArgumentException("Invalid date.");
        }
        return parsedDate;
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
}


