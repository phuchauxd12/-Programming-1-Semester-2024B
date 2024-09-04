package user;

import transaction.SaleTransaction;
import transaction.SaleTransactionList;

import java.time.LocalDate;
import java.util.List;


public class Salesperson extends Employee {
    private List<SaleTransaction> myTransaction;

    public Salesperson(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType) throws Exception {
        super(userName, password, name, dob, address, phoneNum, email, userType);
    }

    public void addMyTransaction(SaleTransaction transaction) throws Exception {
        myTransaction.add(transaction);
    }

    // All sales statistics for the given date range
//    public void viewSalesStatistics(LocalDate startDate, LocalDate endDate) {
//        double totalSalesRevenue = 0.0;
//        int transactionCount = 0;
//
//        Map<String, Double> clientRevenue = new HashMap<>();
//
//        // Total sales revenue
//        for (SaleTransaction transaction : transactionList.getAllSaleTransactions()) {
//            if ((transaction.getTransactionDate().isEqual(startDate) || transaction.getTransactionDate().isAfter(startDate)) &&
//                    (transaction.getTransactionDate().isEqual(endDate) || transaction.getTransactionDate().isBefore(endDate))) {
//                totalSalesRevenue += transaction.getTotalAmount();
//                transactionCount++;
//
//                // Update client revenue
//                clientRevenue.put(transaction.getClientId(), clientRevenue.getOrDefault(transaction.getClientId(), 0.0) + transaction.getTotalAmount());
//            }
//        }
//
//        // Statistics info
//        System.out.printf("Sales Statistics from %s to %s:\n", startDate, endDate);
//        System.out.printf("Total Sales Revenue: $%.2f\n", totalSalesRevenue);
//        System.out.printf("Total Number of Transactions: %d\n", transactionCount);
//
//        // Revenue by client
//        System.out.println("Revenue by Client:");
//        for (Map.Entry<String, Double> entry : clientRevenue.entrySet()) {
//            System.out.printf("Client ID: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
//        }
//    }
//
    
//    public double getRevenueInSpecificPeriod(LocalDate startDate, LocalDate endDate) {
//
//        double totalSalesRevenue = 0.0;
//
//        List<SaleTransaction> transactionsInRange = transactionList.getSaleTransactionsBetween(startDate, endDate);
//        List<SaleTransaction> filteredTransactions = transactionsInRange.stream()
//                .filter(transaction -> transaction.getSalespersonId().equals(this.getUserName()))
//                .toList();
//
//        totalSalesRevenue = filteredTransactions.stream()
//                .mapToDouble(SaleTransaction::getTotalAmount)
//                .sum();
//
//        return totalSalesRevenue;
//    }


//    public void saleTransactionMadeByMe(LocalDate startDate, LocalDate endDate) {
//
//        String salespersonId = this.getUserID();
//        SaleTransactionList.displayAllSaleTransactions();
//    }

    public void viewCarsSoldByMe(LocalDate startDate, LocalDate endDate) {
        String salespersonId = this.getUserName();
        int carsSold = SaleTransactionList.getSaleTransactionsBetween(startDate, endDate).stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId))
                .mapToInt(transaction -> transaction.getPurchasedCars().size())
                .sum();

        System.out.printf("Total Number of Cars Sold by Salesperson ID %s from %s to %s: %d\n",
                salespersonId, startDate, endDate, carsSold);
    }
}
