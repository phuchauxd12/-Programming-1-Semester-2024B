package user;

import transaction.SaleTransaction;
import transaction.SaleTransactionList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Salesperson extends Employee {

    private SaleTransactionList transactionList;

    public Salesperson(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status, SaleTransactionList transactionList) {
        super(userName, password, name, dob, address, phoneNum, email, userType, status, transactionList, null);
        this.transactionList = transactionList;
    }

    // All sales statistics for the given date range
    public void viewSalesStatistics(LocalDate startDate, LocalDate endDate) {
        double totalSalesRevenue = 0.0;
        int transactionCount = 0;

        Map<String, Double> clientRevenue = new HashMap<>();

        // Total sales revenue
        for (SaleTransaction transaction : transactionList.getAllSaleTransactions()) {
            if ((transaction.getTransactionDate().isEqual(startDate) || transaction.getTransactionDate().isAfter(startDate)) &&
                    (transaction.getTransactionDate().isEqual(endDate) || transaction.getTransactionDate().isBefore(endDate))) {
                totalSalesRevenue += transaction.getTotalAmount();
                transactionCount++;

                // Update client revenue
                clientRevenue.put(transaction.getClientId(), clientRevenue.getOrDefault(transaction.getClientId(), 0.0) + transaction.getTotalAmount());
            }
        }

        // Statistics info
        System.out.printf("Sales Statistics from %s to %s:\n", startDate, endDate);
        System.out.printf("Total Sales Revenue: $%.2f\n", totalSalesRevenue);
        System.out.printf("Total Number of Transactions: %d\n", transactionCount);

        // Revenue by client
        System.out.println("Revenue by Client:");
        for (Map.Entry<String, Double> entry : clientRevenue.entrySet()) {
            System.out.printf("Client ID: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
        }
    }

    // Transactions done by this salesperson in specific date range
    public void viewTransactionsBySalesperson(LocalDate startDate, LocalDate endDate) {
        String salespersonId = this.getUserName(); // Get the salesperson ID

        List<SaleTransaction> transactionsInRange = transactionList.getSaleTransactionsBetween(startDate, endDate);

        List<SaleTransaction> filteredTransactions = transactionsInRange.stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId))
                .toList();

        double totalSalesRevenue = filteredTransactions.stream()
                .mapToDouble(SaleTransaction::getTotalAmount)
                .sum();
        int transactionCount = filteredTransactions.size();

        System.out.println("Sales Transactions for Salesperson ID: " + salespersonId);
        System.out.println("Total Sales Revenue: $" + String.format("%.2f", totalSalesRevenue));
        System.out.println("Total Number of Transactions: " + transactionCount);

        if (!filteredTransactions.isEmpty()) {
            System.out.println("\nTransaction Details:");
            for (SaleTransaction transaction : filteredTransactions) {
                System.out.println(transaction.getFormattedSaleTransactionDetails());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}