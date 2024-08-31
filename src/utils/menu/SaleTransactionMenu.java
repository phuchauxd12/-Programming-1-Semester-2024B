package utils.menu;

import services.ServiceList;
import transaction.SaleTransaction;
import transaction.SaleTransactionList;
import user.Manager;
import user.Salesperson;
import user.User;
import utils.UserSession;

import java.util.Scanner;

public class SaleTransactionMenu extends Menu {


    public SaleTransactionMenu() {
        super();
        switch (currentUser) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            case Salesperson s -> initializeMenu(MenuOption.SALESPERSON);
            case null, default -> initializeMenu(null);
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


                menuActions.put(1, this::displayAllTransactions);
                menuActions.put(2, this::searchTransactionById);
                menuActions.put(3, this::deleteTransactionWrapper);
                menuActions.put(4, this::createTransactionWrapper);
                menuActions.put(5, this::updateTransactionWrapper);
                menuActions.put(0, this::exit);
            }
            case null, default -> System.out.print("");
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
        User currentUser = UserSession.getCurrentUser();
        SaleTransaction transaction = SaleTransactionList.getSaleTransactionById(transactionID);
        if (currentUser.getRole() == User.ROLE.MANAGER){
            if (transaction != null && !transaction.isDeleted()) {
                System.out.println("Transaction found!");
                System.out.println(transaction.getFormattedSaleTransactionDetails());
            }
            System.out.println("No transaction found with ID: " + transactionID);
        } else if (currentUser.getRole() == User.ROLE.EMPLOYEE){
            if (currentUser instanceof Salesperson){
                if(transaction != null && !transaction.isDeleted()){
                    if(transaction.getSalespersonId().equals(currentUser.getUserID())){
                        System.out.println("Transaction found!");
                        System.out.println(transaction.getFormattedSaleTransactionDetails());
                    } else {
                        System.out.println("You are not allow to access this transaction: " + transactionID);
                    }
                } else {
                    System.out.println("No transaction found with ID: " + transactionID);
                }
            }
        }
    }

    private void deleteTransaction() throws Exception {
        System.out.println("Deleting a transaction...");
        SaleTransactionList.deleteSaleTransaction();
    }

    private void createNewTransaction() throws Exception {
        System.out.println("Creating a new transaction...");
        if (UserSession.getCurrentUser().getRole() == User.ROLE.MANAGER) {
            Scanner input = new Scanner(System.in);
            User saleperson = null;
            while (saleperson == null) {
                System.out.println("Enter saleperson ID: ");
                String saleprsonId = input.nextLine();

                String finalSalepersonId = saleprsonId;
                saleperson = UserMenu.getUserList().stream()
                        .filter(u -> u.getUserID().equals(finalSalepersonId))
                        .findFirst()
                        .orElse(null);

                if (saleperson == null) {
                    System.out.print("Invalid client ID. Please press enter to retype or 'quit' to exit: ");
                    saleprsonId = input.nextLine();
                    if (saleprsonId.equalsIgnoreCase("quit")) {
                        System.out.println("Exiting..");
                        return;
                    }
                }
            }
            SaleTransactionList.addSaleTransaction(saleperson.getUserID());
        } else {
            ServiceList.addService(UserSession.getCurrentUser().getUserName());
        }
    }

    private void updateTransaction() throws Exception {
        System.out.println("Updating a transaction...");
        SaleTransactionList.updateSaleTransaction();
    }


}
