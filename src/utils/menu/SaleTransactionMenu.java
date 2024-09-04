package utils.menu;

import services.ServiceList;
import transaction.SaleTransaction;
import transaction.SaleTransactionList;
import user.Manager;
import user.Salesperson;
import user.User;
import utils.CommonFunc;
import utils.DatePrompt;
import utils.UserSession;

import java.time.LocalDate;
import java.util.List;
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
                menuActions.put(2, this::getAllTransactionsInSpecificPeriod);
                menuActions.put(3, this::getAllSalespersonSales);
                menuActions.put(3, this::searchTransactionById);
                menuActions.put(4, this::deleteTransactionWrapper);
                menuActions.put(5, this::createTransactionWrapper);
                menuActions.put(6, this::updateTransactionWrapper);
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
            CommonFunc.addActivityLogForCurrentUser("Create new transaction");
        } catch (Exception e) {
            System.out.println("Error creating transaction: " + e.getMessage());
        }
    }

    private void updateTransactionWrapper() {
//        SaleTransactionList.transactions.stream().filter(transaction -> !transaction.isDeleted()).forEach(transaction -> System.out.println(transaction.getFormattedSaleTransactionDetails()));
        try {
            updateTransaction();
            CommonFunc.addActivityLogForCurrentUser("Update transaction wrapper");
        } catch (Exception e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    private void deleteTransactionWrapper() {
//        SaleTransactionList.transactions.stream().filter(transaction -> !transaction.isDeleted()).forEach(transaction -> System.out.println(transaction.getFormattedSaleTransactionDetails()));
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

    private void getAllTransactionsInSpecificPeriod() {
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);

        List<SaleTransaction> transactions = SaleTransactionList.getSaleTransactionsBetween(startDate, endDate);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found within the specified period.");
        } else {
            System.out.println("Transactions between " + startDate + " and " + endDate + ":");
            for (SaleTransaction transaction : transactions) {
                System.out.println(transaction.getFormattedSaleTransactionDetails());
            }
        }
        try{
            String activityName = "View all transactions in a specific period";
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllSalespersonSales() {
        String activityName;
        List<SaleTransaction> transactions;
            UserMenu.displayAllSalespersons();
            String salespersonId;
            Salesperson salesperson;
            while (true) {
                System.out.print("Enter salesperson ID: ");
                salespersonId = input.nextLine();
                if (!salespersonId.isEmpty()) {
                    salesperson = (Salesperson) UserMenu.getUserById(salespersonId);
                    if (salesperson != null) {
                        break;
                    } else {
                        System.out.println("Salesperson not found. Please try again.");
                    }
                    System.out.println("Salesperson ID cannot be empty. Please try again.");
                }
            }
            activityName = "View all sales made Salesperson named " + salesperson.getUserName() + " with ID " + salesperson.getUserID();
            transactions = SaleTransactionList.getTransactionsBySalePerson(salespersonId);
            if (transactions.isEmpty()) {
                System.out.println("No transactions was made by this salesperson.");
            } else {
                System.out.println("All available transactions of the salesperson : ");
                for (SaleTransaction transaction : transactions) {
                    System.out.println(transaction.getFormattedSaleTransactionDetails());
                }
            }

        try{
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void searchTransactionById() {
        SaleTransactionList.transactions.stream().filter(transaction -> !transaction.isDeleted()).forEach(System.out::println);
        System.out.println("Searching transaction by ID...");
        Scanner input = new Scanner(System.in);
        System.out.println("Enter transaction ID: ");
        String transactionID = input.nextLine();
        User currentUser = UserSession.getCurrentUser();
        SaleTransaction transaction = SaleTransactionList.getSaleTransactionById(transactionID);
        if (currentUser instanceof  Manager){
            if (transaction != null && !transaction.isDeleted()) {
                System.out.println("Transaction found!");
                System.out.println(transaction.getFormattedSaleTransactionDetails());
            }
            System.out.println("No transaction found with ID: " + transactionID);
        } else if (currentUser instanceof Salesperson){
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
        System.out.println("No transaction found with ID: " + transactionID);

        try{
            CommonFunc.addActivityLogForCurrentUser("Search transaction by ID: " + transactionID);
        } catch (Exception e) {
            System.out.println("Error logging sale transaction action history: " + e.getMessage());
        }
    }

    private void deleteTransaction() throws Exception {
        System.out.println("Deleting a transaction...");
        User currentUser = UserSession.getCurrentUser();
        if (currentUser.getRole() == User.ROLE.MANAGER){
            SaleTransactionList.deleteSaleTransaction();
        } else if (currentUser.getRole() == User.ROLE.EMPLOYEE){
            if (currentUser instanceof Salesperson){
                //TODO: 
            }
        }

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
            User salesperson = null;
            while (salesperson == null) {
                System.out.println("Sale person:");
                UserMenu.getUserList().stream().filter(user -> user instanceof Salesperson).forEach(System.out::println);
                System.out.println("Enter salesperson ID: ");
                String salespersonId = input.nextLine();

                String finalSalespersonId = salespersonId;
                salesperson = UserMenu.getUserList().stream()
                        .filter(u -> u.getUserID().equals(finalSalespersonId))
                        .findFirst()
                        .orElse(null);

                if (salesperson == null) {
                    System.out.print("Invalid client ID. Please press enter to retype or 'quit' to exit: ");
                    salespersonId = input.nextLine();
                    if (salespersonId.equalsIgnoreCase("quit")) {
                        System.out.println("Exiting..");
                        return;
                    }
                }
            }
            SaleTransactionList.addSaleTransaction(salesperson.getUserID());
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
