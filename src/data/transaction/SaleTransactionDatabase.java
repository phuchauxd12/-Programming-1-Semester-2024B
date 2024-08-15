package data.transaction;

import data.Database;
import transaction.SaleTransaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SaleTransactionDatabase {
    public static final String path = "src/data/transaction/SaleTransaction.txt";
    private static List<SaleTransaction> saleTransactionList;


    public static void createDatabase() throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // Initialize the list of users
            saleTransactionList = new ArrayList<>();
            Database.<SaleTransaction>saveDatabase(path, saleTransactionList, "Database file created", "Error while creating the database file.");
        } else {
            System.out.println("Database file already exists at " + path);
        }
    }

    // Method to load the list of users from the file
    public static List<SaleTransaction> loadSaleTransaction() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            saleTransactionList = (List<SaleTransaction>) objIn.readObject();
            return saleTransactionList;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading transactions from the database file.");
        }
    }

    // Method to add a new user to the database
    public static void addSaleTransaction(SaleTransaction newTransaction) throws Exception {

        loadSaleTransaction();


        saleTransactionList.add(newTransaction);

        // Save the updated list back to the file
        Database.<SaleTransaction>saveDatabase(path, saleTransactionList, "Transaction had been created and stored in the database.", "Error while saving the updated car list to the database file.");
    }

    public static SaleTransaction findSaleTransactionByID(String transactionID) throws Exception {

        loadSaleTransaction();


        var foundTransaction = saleTransactionList.stream()
                .filter(transaction -> transaction.getTransactionId().equals(transactionID))
                .findFirst();
        return foundTransaction.orElse(null);
    }

    public static void deleteSaleTransaction(String transactionID) throws Exception {
        var foundTransaction = findSaleTransactionByID(transactionID);
        // Add the new user to the list
        if (foundTransaction != null) {
            saleTransactionList = saleTransactionList.stream()
                    .filter(transaction -> !transaction.getTransactionId().equals(transactionID))
                    .collect(Collectors.toList());

            Database.<SaleTransaction>saveDatabase(path, saleTransactionList, "Transaction had been deleted in the database.", "Error while deleting the car in the database file.");

        } else {
            System.out.println("No carID match the account in the database.");
        }
    }
    public  static  void saveSaleTransaction(List<SaleTransaction> dataList) throws Exception {
        Database.<SaleTransaction>saveDatabase(path, dataList, "Sale Transaction had been updated in the database.", "Error while updating the database file.");
    }
}
