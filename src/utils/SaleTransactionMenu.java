package utils;

import services.ServiceList;
import transaction.SaleTransactionList;
import user.User;

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
        menuActions.put(3, this::deleteTransactionWrapper);
        menuActions.put(4, this::createTransactionWrapper);
        menuActions.put(5, this::updateTransactionWrapper);
        menuActions.put(6, this::exit);
    }

    private void createTransactionWrapper() {
        try {
            createNewTransaction();
        } catch (Exception e) {
            System.out.println("Error creating transaction: " + e.getMessage());
        }
    }

    private void updateTransactionWrapper() {
        try {
            updateTransaction();
        } catch (Exception e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    private void deleteTransactionWrapper() {
        try {
            deleteTransaction();
        } catch (Exception e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
    }

    private void displayAllTransactions() {
        System.out.println("Displaying all transactions...");
        SaleTransactionList.displayAllSaleTransactions();
    }

    private void searchTransactionById() {
        System.out.println("Searching transaction by ID...");
        Scanner input = new Scanner(System.in);
        System.out.println("Enter transaction ID: ");
        String transactionID = input.nextLine();
        if(ServiceList.getServiceById(transactionID) != null){
            System.out.println("Transaction found!");
            System.out.println(SaleTransactionList.getSaleTransactionById(transactionID).getFormattedSaleTransactionDetails());
        }
        System.out.println("No transaction found with ID: " + transactionID);
    }

    private void deleteTransaction() throws Exception {
        System.out.println("Deleting a transaction...");
        SaleTransactionList.deleteSaleTransaction();
    }

    private void createNewTransaction() throws Exception {
        System.out.println("Creating a new transaction...");
        if(UserSession.getCurrentUser().getRole() == User.ROLE.MANAGER){
            Scanner input = new Scanner(System.in);
            System.out.println("Enter saleperson ID: ");
            String salepersonID = input.nextLine();
            SaleTransactionList.addSaleTransaction(salepersonID);
        }else {
            ServiceList.addService(UserSession.getCurrentUser().getUserName());
        }
    }

    private void updateTransaction() throws Exception {
        System.out.println("Updating a transaction...");
        SaleTransactionList.updateSaleTransaction();
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

    public void mainMenu(User user) {
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
