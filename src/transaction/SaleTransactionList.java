package transaction;

import car.Car;
import data.Database;
import data.car.CarDatabase;
import data.transaction.SaleTransactionDatabase;
import data.user.UserDatabase;
import user.Client;
import user.User;
import utils.CarAndAutoPartMenu;
import utils.Status;
import utils.UserMenu;

import java.time.LocalDate;
import java.util.*;

public class SaleTransactionList {
    public static List<SaleTransaction> transactions;
    // This code run one time when create an instance of a class
    static {
        try {
            if(!Database.isDatabaseExist(SaleTransactionDatabase.path)){
                SaleTransactionDatabase.createDatabase();
            };
            transactions = SaleTransactionDatabase.loadSaleTransaction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addSaleTransaction(String salespersonId) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction date (YYYY-MM-DD): ");
        LocalDate transactionDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter client ID: ");
        String clientId = scanner.nextLine();

        System.out.println("Enter item IDs purchased (seperated by space): ");
        String carIdsInput = scanner.nextLine();
        List<String> carIds = List.of(carIdsInput.split("\\s+"));

        SaleTransaction transaction = new SaleTransaction(transactionDate, clientId, salespersonId, carIds);
        SaleTransaction.addSaleTransaction(transaction);

        for (Car car : transaction.getPurchasedItems()) {
            car.setStatus(Status.SOLD);
            CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
        }

        User user = UserMenu.getUserList().stream()
                .filter(u -> u.getUserID().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;
            client.updateTotalSpending(transaction.getTotalAmount());
            UserDatabase.saveUsersData(UserMenu.getUserList());
        }

        System.out.println("Sale transaction added successfully:");
        System.out.println(transaction.getFormattedSaleTransactionDetails());

    }

    public static SaleTransaction getSaleTransactionById(String transactionId) {
        for (SaleTransaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    public List<SaleTransaction> getAllSaleTransactions() {
        return new ArrayList<>(transactions); // Return a copy to avoid modification
    }

    public void displayAllSaleTransactions() {
        for (SaleTransaction transaction : transactions) {
            System.out.println(transaction.getFormattedSaleTransactionDetails());
        }
    }

    public void updateSaleTransaction() throws Exception {
        displayAllSaleTransactions();
        SaleTransaction.updateSaleTransaction();
    }


    public void deleteSaleTransaction() throws Exception {
        displayAllSaleTransactions();
        SaleTransaction.deleteSaleTransaction();
    }

    public double calculateTotalSales() {
        double total = 0.0;
        for (SaleTransaction transaction : transactions) {
            total += transaction.getTotalAmount();
        }
        return total;
    }

    public List<SaleTransaction> getSaleTransactionsBetween(LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> filteredTransactions = new ArrayList<>();
        for (SaleTransaction transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate();
            if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                    (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate))) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public int calculateCarsSold(LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);
        int carCount = 0;

        for (SaleTransaction transaction : transactionsInRange) {
            carCount += transaction.getPurchasedItems().size();
        }

        return carCount;
    }

    public List<Car> listCarsSoldInDateRange(LocalDate startDate, LocalDate endDate) {
        List<Car> carsSold = new ArrayList<>();

        for (SaleTransaction transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate();
            if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                    (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate)) &&
                    !transaction.isDeleted()) {

                carsSold.addAll(transaction.getPurchasedItems());
            }
        }

        return carsSold;
    }

    public double[] calculateRevenueAndCount(LocalDate startDate, LocalDate endDate) {
        double totalSalesRevenue = 0.0;
        int transactionCount = 0;

        for (SaleTransaction transaction : getSaleTransactionsBetween(startDate, endDate)) {
            totalSalesRevenue += transaction.getTotalAmount();
            transactionCount++;
        }

        return new double[]{totalSalesRevenue, transactionCount};
    }

    public double calculateSalespersonRevenue(String salespersonId, LocalDate startDate, LocalDate endDate) {
        double totalSalesRevenue = 0.0;

        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);
        List<SaleTransaction> filteredTransactions = transactionsInRange.stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId))
                .toList();

        totalSalesRevenue = filteredTransactions.stream()
                .mapToDouble(SaleTransaction::getTotalAmount)
                .sum();

        return totalSalesRevenue;
    }

    public void viewSalesStatistics(LocalDate startDate, LocalDate endDate) {
        double[] revenueAndCount = calculateRevenueAndCount(startDate, endDate);
        double totalSalesRevenue = revenueAndCount[0];
        int transactionCount = (int) revenueAndCount[1];
        int carsSold = calculateCarsSold(startDate, endDate);

        Map<String, Double> clientRevenue = new HashMap<>();

        for (SaleTransaction transaction : getSaleTransactionsBetween(startDate, endDate)) {

            clientRevenue.put(transaction.getClientId(), clientRevenue.getOrDefault(transaction.getClientId(), 0.0) + transaction.getTotalAmount());
        }

        // Statistics info
        System.out.printf("Sales Statistics from %s to %s:\n", startDate, endDate);
        System.out.printf("Total Sales Revenue: $%.2f\n", totalSalesRevenue);
        System.out.printf("Total Number of Transactions: %d\n", transactionCount);
        System.out.printf("Total Number of Cars Sold: %d\n", carsSold);

        // Revenue by client
        System.out.println("Revenue by Client:");
        for (Map.Entry<String, Double> entry : clientRevenue.entrySet()) {
            System.out.printf("Client ID: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
        }
    }

    public void viewTransactionsBySalesperson(String salespersonId, LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);

        List<SaleTransaction> filteredTransactions = transactionsInRange.stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId))
                .toList();

        double totalSalesRevenue = calculateSalespersonRevenue(salespersonId, startDate, endDate);
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