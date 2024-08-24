package utils.menu;

import user.User;
import utils.UserSession;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

abstract class Menu {
    protected static Scanner input = new Scanner(System.in);
    protected final Map<Integer, String> menuItems = new LinkedHashMap<>();
    protected final Map<Integer, Runnable> menuActions = new LinkedHashMap<>();
    protected final User currentUser = UserSession.getCurrentUser();


    protected abstract void initializeMenu(MenuOption menuOption);

    protected void displayMenu() {
        System.out.println("---------------------");
        menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
        System.out.println("---------------------");
    }

    protected abstract void mainMenu();

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

    protected void exit() {
        System.out.println("Exiting Menu!");
    }
}
