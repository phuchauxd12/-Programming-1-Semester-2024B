package utils.menu;

import services.ServiceList;
import transaction.SaleTransactionList;
import user.Manager;
import user.Salesperson;
import user.User;
import utils.CommonFunc;
import utils.UserSession;

import java.util.Scanner;

public class SaleTransactionMenu extends Menu {


    public SaleTransactionMenu() {
        super();
        switch (currentUser) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            case Salesperson s -> initializeMenu(MenuOption.SALESPERSON);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    @Override
    protected void initializeMenu(MenuOption menuOption) {
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
        menuItems.put(2, "Search a transaction by ID");
        menuItems.put(3, "Delete a transaction");
        menuItems.put(4, "Create a new transaction");
        menuItems.put(5, "Update a transaction");
        menuItems.put(0, "Exit");
    }

    private void createTransactionWrapper() {
        try {
            createNewTransaction();
            CommonFunc.addActivityLogForCurrentUser("Create new transaction");
        } catch (Exception e) {
            System.out.println("Error creating transaction: " + e.getMessage());
        }
    }

    private void updateTransactionWrapper() {
        try {
            updateTransaction();
            CommonFunc.addActivityLogForCurrentUser("Update transaction wrapper");
        } catch (Exception e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    private void deleteTransactionWrapper() {
        try {
            deleteTransaction();
            CommonFunc.addActivityLogForCurrentUser("Delete transaction wrapper");
        } catch (Exception e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
    }

    private void displayAllTransactions() {
        System.out.println("Displaying all transactions...");
        SaleTransactionList.displayAllSaleTransactions();
        try{
            CommonFunc.addActivityLogForCurrentUser("View all transactions");
        } catch (Exception e) {
            System.out.println("Error logging sale transaction action history: " + e.getMessage());
        }
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

        try{
            CommonFunc.addActivityLogForCurrentUser("Search transaction by ID: " + transactionID);
        } catch (Exception e) {
            System.out.println("Error logging sale transaction action history: " + e.getMessage());
        }
    }

    private void deleteTransaction() throws Exception {
        System.out.println("Deleting a transaction...");
        SaleTransactionList.deleteSaleTransaction();

        try{
            CommonFunc.addActivityLogForCurrentUser("Delete sale transaction");
        } catch (Exception e) {
            System.out.println("Error logging sale transaction action history: " + e.getMessage());
        }
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

        try{
            CommonFunc.addActivityLogForCurrentUser("Create a new transaction");
        } catch (Exception e) {
            System.out.println("Error logging sale transaction action history: " + e.getMessage());
        }
    }

    private void updateTransaction() throws Exception {
        System.out.println("Updating a transaction...");
        SaleTransactionList.updateSaleTransaction();

        try{
            CommonFunc.addActivityLogForCurrentUser("Update a new transaction");
        } catch (Exception e) {
            System.out.println("Error logging sale transaction action history: " + e.getMessage());
        }
    }


}
