package utils.menu;

import user.User;
import utils.UserSession;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

abstract class Menu {
    protected static Scanner input = new Scanner(System.in);
    protected Map<Integer, String> menuItems;
    protected Map<Integer, Runnable> menuActions;
    protected final User currentUser = UserSession.getCurrentUser();

    public Menu() {
        this.menuItems = new LinkedHashMap<>();
        this.menuActions = new LinkedHashMap<>();
    }

    protected abstract void initializeMenu(MenuOption menuOption);

    protected void displayMenu(String welcomeMessage) {
        int option = 100;
        do {
            System.out.println(welcomeMessage);
            //Display menu base on Type of Menu, Menu Role .This function is used only for children class
            System.out.println("---------------------");
            menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
            System.out.println("---------------------");

            option = getOption(option, input);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
    }

//    protected abstract void mainMenu();

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

    public static int getFilteredOption() {
        int option = 100;
        System.out.print("1. View all available: ");
        System.out.print("2. Filtered by period: ");
        boolean validInput = false;
        while (!validInput) {
            option = getOption(option, input);
            if (option == 1 || option == 2) {
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter 1 or 2");
            }
        }
        return option;
    }

    protected void exit() {
        System.out.println("Exiting Menu!");
    }
}
