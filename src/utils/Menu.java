package utils;

import user.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Menu {
    private static final Scanner input = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Consumer<Scanner>> menuActions = new LinkedHashMap<>();

    public Menu(User user) {
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
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Transaction Menu");
                menuItems.put(4, "Service Menu");
                menuItems.put(5, "Statistics Menu");
                menuItems.put(6, "Manage Users Menu");
                menuItems.put(7, "Activity Log Menu");
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> manageProfileMenu(user));
                menuActions.put(2, _ -> carAndAutoPartMenu(user));
                menuActions.put(3, _ -> transactionMenu(user));
                menuActions.put(4, _ -> serviceMenu(user));
                menuActions.put(5, _ -> statisticsMenu(user));
                menuActions.put(6, _ -> manageUsersMenu(user));
                menuActions.put(7, _ -> activityLogMenu(user));
                menuActions.put(10, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Transaction Menu");
                menuItems.put(4, "Statistics Menu");
                menuItems.put(5, "Activity Log Menu");
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> manageProfileMenu(user));
                menuActions.put(2, _ -> carAndAutoPartMenu(user));
                menuActions.put(3, _ -> transactionMenu(user));
                menuActions.put(4, _ -> statisticsMenu(user));
                menuActions.put(5, _ -> activityLogMenu(user));
                menuActions.put(10, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Service Menu");
                menuItems.put(4, "Statistics Menu");
                menuItems.put(5, "Activity Log Menu");
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> manageProfileMenu(user));
                menuActions.put(2, _ -> carAndAutoPartMenu(user));
                menuActions.put(3, _ -> serviceMenu(user));
                menuActions.put(4, _ -> statisticsMenu(user));
                menuActions.put(5, _ -> activityLogMenu(user));
                menuActions.put(10, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Statistics Menu");
                menuItems.put(4, "Activity Log Menu");
                menuItems.put(10, "Exit");

                menuActions.put(1, _ -> manageProfileMenu(user));
                menuActions.put(2, _ -> carAndAutoPartMenu(user));
                menuActions.put(3, _ -> statisticsMenu(user));
                menuActions.put(4, _ -> activityLogMenu(user));
                menuActions.put(10, this::exit);
            }
        }
    }


    // Menu actions
    private void activityLogMenu(User user) {
        System.out.println("Activity Log Menu");
        // Get the activity log menu
    }

    private void manageUsersMenu(User user) {
        try {
            UserMenu.mainMenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void statisticsMenu(User user) {
        StatisticsMenu statisticsMenu = new StatisticsMenu(user);
        try {
            statisticsMenu.mainMenu(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void serviceMenu(User user) {
        // Get the service menu
        System.out.println("Service Menu");
    }

    private void transactionMenu(User user) {
        // Get the transaction menu
        System.out.println("Transaction Menu");
    }

    private void carAndAutoPartMenu(User user) {
        CarAndAutoPartMenu carAndAutoPartMenu = new CarAndAutoPartMenu(user);
        try {
            carAndAutoPartMenu.mainMenu(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void manageProfileMenu(User user) {
        // Get the user's profile menu
        System.out.println("Manage Profile Menu");
    }

    public void exit(Scanner input) {
        System.out.println("Exiting the program...");
    }

    // menu functions
    public void displayMenu() {
        System.out.println("Welcome to the Main Menu!");
        System.out.println("---------------------");
        menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
        System.out.println("---------------------");
    }

    public void mainMenu() throws Exception {
        int option = 0;
        do {
            displayMenu();
            option = getOption(option, input);
            menuActions.getOrDefault(option, _ -> System.out.println("Invalid option. Please try again.")).accept(input);
        } while (option != 10);
        System.exit(0);
    }

    // Static helper functions
    public static LocalDate getStartDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate;
        while (true) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine());
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid start date format. Please try again.");
            }
        }
        return startDate;
    }

    public static LocalDate getEndDate(LocalDate startDate) {
        Scanner scanner = new Scanner(System.in);
        LocalDate endDate;
        while (true) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine());
                if (!endDate.isBefore(startDate)) {
                    break;
                }
                System.out.println("End date cannot be before start date. Please try again.");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid end date format. Please try again.");
            }
        }
        return endDate;
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
