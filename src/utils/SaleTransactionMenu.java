package utils;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class SaleTransactionMenu {
    HashSet<String> menuList = new HashSet<String>();
    private Map<Integer, Runnable> menuActions= new HashMap<>();

    public SaleTransactionMenu() {
        menuList.add("Display all transactions");
        menuList.add("Search a transaction by ID");
        menuList.add("Delete a transaction");
        menuList.add("Create a new transaction");
        menuList.add("Update a transaction");
        menuList.add("Exit");

        menuActions.put(1, this::displayAllTransactions);
        menuActions.put(2, this::searchTransactionById);
        menuActions.put(3, this::deleteTransaction);
        menuActions.put(4, this::createNewTransaction);
        menuActions.put(5, this::updateTransaction);
        menuActions.put(6, this::exit);
    }

    private void displayAllTransactions() {
        System.out.println("Displaying all transactions...");
        // Add logic to display all transactions
    }

    private void searchTransactionById() {
        System.out.println("Searching transaction by ID...");
        // Add logic to search a transaction by ID
    }

    private void deleteTransaction() {
        System.out.println("Deleting a transaction...");
        // Add logic to delete a transaction
    }

    private void createNewTransaction() {
        System.out.println("Creating a new transaction...");
        // Add logic to create a new transaction
    }

    private void updateTransaction() {
        System.out.println("Updating a transaction...");
        // Add logic to update a transaction
    }

    private void exit() {
        System.out.println("Exiting...");
    }

    public void displayMenu() {
        int currentIndex = 1;

        System.out.println("This is the Sale Transaction Menu.");
        for (String menuItem : menuList) {
            System.out.printf("%d  %s \n", currentIndex, menuItem);
            currentIndex++;
        }
    }

    private int getOption(Scanner input) {
        System.out.print("Enter your choice: ");
        while (!input.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            input.next();
        }
        return input.nextInt();
    }

    public void mainMenu() {
        Scanner input = new Scanner(System.in);
        int option = 0;
        while (option != 6) {
            displayMenu();
            option = getOption(input);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
