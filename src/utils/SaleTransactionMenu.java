package utils;

import services.ServiceList;
import transaction.SaleTransactionList;
import user.*;

import java.util.*;

import static utils.Menu.getOption;

public class SaleTransactionMenu {
    private static final Scanner input = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Runnable> menuActions = new LinkedHashMap<>();

    public SaleTransactionMenu(User user) {
        switch (user) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            case Salesperson s -> initializeMenu(MenuOption.SALESPERSON);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    private void initializeMenu(MenuOption menuOption) {
        menuItems.put(2, "Search a transaction by ID");
        menuItems.put(3, "Delete a transaction");
        menuItems.put(4, "Create a new transaction");
        menuItems.put(5, "Update a transaction");
        menuItems.put(0, "Exit");
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "Display all transactions");

                menuActions.put(1, this::displayAllTransactions);
                menuActions.put(2, this::searchTransactionById);
                menuActions.put(3, this::deleteTransactionWrapper);
                menuActions.put(4, this::createTransactionWrapper);
                menuActions.put(5, this::updateTransactionWrapper);
                menuActions.put(0, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Display all transactions by me");


                menuActions.put(1, this::displayAllTransactions); // TODO: Display all transactions by salesperson
                menuActions.put(2, this::searchTransactionById); // TODO: Search transactions by salesperson
                menuActions.put(3, this::deleteTransactionWrapper); // TODO: Delete transactions by salesperson
                menuActions.put(4, this::createTransactionWrapper); // TODO: Create transactions by salesperson
                menuActions.put(5, this::updateTransactionWrapper); // TODO: Update transactions by salesperson
                menuActions.put(0, this::exit);
            }
        }
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
        if (ServiceList.getServiceById(transactionID) != null) {
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
        if (UserSession.getCurrentUser().getRole() == User.ROLE.MANAGER) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter saleperson ID: ");
            String salepersonID = input.nextLine();
            SaleTransactionList.addSaleTransaction(salepersonID);
        } else {
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
        System.out.println("Welcome to the Sales Transaction Menu!");
        System.out.println("---------------------");
        menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
        System.out.println("---------------------");
    }

    public void mainMenu(Menu mainMenu) {
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
        mainMenu.mainMenu();
    }
}
