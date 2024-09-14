package transaction;

import autoPart.autoPart;
import car.Car;
import data.Database;
import data.transaction.SaleTransactionDatabase;
import user.Client;
import user.Manager;
import user.Salesperson;
import user.User;
import utils.CurrencyFormat;
import utils.DatePrompt;
import utils.Status;
import utils.UserSession;
import utils.menu.ActivityLogMenu;
import utils.menu.CarAndAutoPartMenu;
import utils.menu.UserMenu;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SaleTransactionList {
    public static List<SaleTransaction> transactions;

    // This code run one time when create an instance of a class
    static {
        try {
            if (!Database.isDatabaseExist(SaleTransactionDatabase.path)) {
                SaleTransactionDatabase.createDatabase();
            }
            transactions = SaleTransactionDatabase.loadSaleTransaction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addSaleTransaction(String salespersonId) throws Exception {
        Scanner scanner = new Scanner(System.in);
        LocalDate transactionDate = DatePrompt.getDate("transaction");
        User client = null;
        while (client == null) {
            System.out.println("Client:");
            UserMenu.getUserList().stream().filter(user -> user instanceof Client).forEach(System.out::println);
            System.out.print("Enter client ID: ");
            String clientId = scanner.nextLine();

            String finalClientId = clientId;
            client = UserMenu.getUserList().stream()
                    .filter(u -> u.getUserID().equals(finalClientId))
                    .findFirst()
                    .orElse(null);

            if (!(client instanceof Client)) {
                System.out.print("Invalid client ID. Please press enter to re-enter client ID or type 'quit' to exit: ");
                clientId = scanner.nextLine();
                if (clientId.equalsIgnoreCase("quit")) {
                    System.out.println("Exiting..");
                    return;
                }
            }
        }
        System.out.println("Car:");
        CarAndAutoPartMenu.getCarsList().stream().filter(car -> !car.isDeleted() && car.getStatus() == Status.AVAILABLE).forEach(System.out::println);
        System.out.println("Part:");
        CarAndAutoPartMenu.getAutoPartsList().stream().filter(part -> !part.isDeleted() && part.getStatus() == Status.AVAILABLE).forEach(System.out::println);
        System.out.println("Enter new item IDs purchased (separated by comma): ");
        String itemIdsInput = scanner.nextLine();
        Set<String> newItemIds = Arrays.stream(itemIdsInput.split(","))
                .map(String::trim)
                .map(item -> item.replaceAll(" +", " "))
                .collect(Collectors.toSet());

        SaleTransaction transaction = new SaleTransaction(transactionDate, client.getUserID(), salespersonId, newItemIds);
        boolean success = SaleTransaction.addSaleTransaction(transaction);
        if (success) {
            System.out.println("Sale transaction created successfully!");
            System.out.println(transaction.getFormattedSaleTransactionDetails());
            try {
                ActivityLogMenu.addActivityLogForCurrentUser("Created a new transaction with ID: " + transaction.getTransactionId());
            } catch (Exception e) {
                System.out.println("Error logging sale transaction action history: " + e.getMessage());
            }
        }
    }

    public static SaleTransaction getSaleTransactionById(String transactionId) {
        for (SaleTransaction transaction : transactions) {
            if (!transaction.isDeleted() && transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    public static List<SaleTransaction> getTransactionsByClient(String clientId) {
        var transactionByClient = transactions.stream()
                .filter(transaction -> transaction.getClientId().equals(clientId)).toList();
        return transactionByClient;
    }

    public static List<SaleTransaction> getTransactionsBySalePerson(String salespersonId) {
        var transactionBySaleperson = transactions.stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId)).toList();
        return transactionBySaleperson;
    }

    public List<SaleTransaction> getAllSaleTransactions() {
        return new ArrayList<>(transactions); // Return a copy to avoid modification
    }

    public static void displayAllSaleTransactions() {
        User current = UserSession.getCurrentUser();
        if (current instanceof Manager) {
            for (SaleTransaction transaction : transactions) {
                if (!transaction.isDeleted()) {
                    System.out.println(transaction.getFormattedSaleTransactionDetails());
                    System.out.println("___________________________________");
                }
            }
        } else if (current instanceof Salesperson) {
            for (SaleTransaction transaction : getTransactionsBySalePerson(current.getUserID())) {
                if (!transaction.isDeleted()) {
                    System.out.println(transaction.getFormattedSaleTransactionDetails());
                    System.out.println("___________________________________");
                }
            }
        } else if (current instanceof Client) {
            for (SaleTransaction transaction : getTransactionsByClient(current.getUserID())) {
                if (!transaction.isDeleted()) {
                    System.out.println(transaction.getFormattedSaleTransactionDetails());
                    System.out.println("___________________________________");
                }
            }
        }
    }


    public static void updateSaleTransaction() throws Exception {
        displayAllSaleTransactions();
        SaleTransaction.updateSaleTransaction();
    }


    public static void deleteSaleTransaction() throws Exception {
        displayAllSaleTransactions();
        SaleTransaction.deleteSaleTransaction();
    }

    public static double calculateTotalSalesRevenue() {
        double total = 0.0;
        for (SaleTransaction transaction : transactions) {
            total += transaction.getTotalAmount();
        }
        return total;
    }

    public static List<SaleTransaction> getSaleTransactionsBetween(LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> filteredTransactions = new ArrayList<>();
        for (SaleTransaction transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate();
            if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                    (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate)) && !transaction.isDeleted()) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public static int calculateCarsSold(LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);
        int carCount = 0;

        for (SaleTransaction transaction : transactionsInRange) {
            carCount += transaction.getPurchasedCars().size();
        }

        return carCount;
    }

    public static int calculateAutoPartsSold(LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);
        int partCount = 0;

        for (SaleTransaction transaction : transactionsInRange) {
            partCount += transaction.getPurchasedAutoParts().size();
        }

        return partCount;
    }

    public static List<Car> listCarsSoldBySalePerson(String salespersonId, LocalDate startDate, LocalDate endDate) {
        List<Car> carsSold = new ArrayList<>();

        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);
        for (SaleTransaction transaction : transactionsInRange) {
            if (transaction.getSalespersonId().equals(salespersonId)) {
                carsSold.addAll(transaction.getPurchasedCars());
            }
        }

        return carsSold;
    }

    public static List<autoPart> listAutoPartsSoldBySalesPerson(String salespersonId, LocalDate startDate, LocalDate endDate) {
        List<autoPart> autoPartsSold = new ArrayList<>();

        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);
        for (SaleTransaction transaction : transactionsInRange) {
            if (transaction.getSalespersonId().equals(salespersonId)) {
                autoPartsSold.addAll(transaction.getPurchasedAutoParts());
            }
        }
        return autoPartsSold;
    }

    public static double[] calculateRevenueAndCount(LocalDate startDate, LocalDate endDate) {
        double totalSalesRevenue = 0.0;
        int transactionCount = 0;

        for (SaleTransaction transaction : getSaleTransactionsBetween(startDate, endDate)) {
            totalSalesRevenue += transaction.getTotalAmount();
            transactionCount++;
        }

        return new double[]{totalSalesRevenue, transactionCount};
    }

    public static double calculateSalespersonRevenue(String salespersonId, LocalDate startDate, LocalDate endDate) {
        double totalSalesRevenue = 0.0;
        int transactionCount = 0;

        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);
        List<SaleTransaction> filteredTransactions = transactionsInRange.stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId))
                .toList();

        totalSalesRevenue = filteredTransactions.stream()
                .mapToDouble(SaleTransaction::getTotalAmount)
                .sum();

        return totalSalesRevenue;
    }

    public static void viewSalesStatistics(LocalDate startDate, LocalDate endDate) {
        double[] revenueAndCount = calculateRevenueAndCount(startDate, endDate);
        double totalSalesRevenue = revenueAndCount[0];
        int transactionCount = (int) revenueAndCount[1];
        int carsSold = calculateCarsSold(startDate, endDate);
        int autoPartsSold = calculateAutoPartsSold(startDate, endDate);

        Map<String, Double> clientRevenue = new HashMap<>();
        Map<String, Integer> salespersonSales = new HashMap<>();
        Map<String, Double> salespersonRevenue = new HashMap<>();

        for (SaleTransaction transaction : getSaleTransactionsBetween(startDate, endDate)) {

            clientRevenue.put(transaction.getClientId(), clientRevenue.getOrDefault(transaction.getClientId(), 0.0) + transaction.getTotalAmount());

            String salespersonId = transaction.getSalespersonId();
            salespersonRevenue.put(salespersonId, salespersonRevenue.getOrDefault(salespersonId, 0.0) + transaction.getTotalAmount());
            salespersonSales.put(salespersonId, salespersonSales.getOrDefault(salespersonId, 0) + 1);

        }

        // Find the salesperson with the top revenue
        String topSalespersonByRevenue = salespersonRevenue.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        double maxRevenue = salespersonRevenue.getOrDefault(topSalespersonByRevenue, 0.0);

        // Find top salesperson by sales count
        String topSalespersonByCount = salespersonSales.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        int maxSalesCount = salespersonSales.getOrDefault(topSalespersonByCount, 0);
        System.out.println("-------------------------------------------------");
        // Statistics info
        System.out.printf("Sales Statistics from %s to %s:\n", startDate, endDate);
        System.out.printf("Total Sales Revenue: %s\n", CurrencyFormat.format(totalSalesRevenue));
        System.out.printf("Total Number of Transactions: %d\n", transactionCount);
        System.out.printf("Total Number of Cars Sold: %d\n", carsSold);
        System.out.printf("Total Number of AutoParts Sold: %d\n", autoPartsSold);
        System.out.println("-------------------------------------------------");
        // Revenue by client
        System.out.println("Revenue by Client:");
        for (Map.Entry<String, Double> entry : clientRevenue.entrySet()) {
            System.out.printf("Client ID: %s, Revenue: %s\n", UserMenu.getUserById(entry.getKey()).getName(), CurrencyFormat.format(entry.getValue()));
        }
        System.out.println("-------------------------------------------------");
        // Top salesperson by revenue count
        if (topSalespersonByRevenue != null) {
            System.out.printf("Top Salesperson by Revenue: Name: %s, Revenue: %s\n", UserMenu.getUserById(topSalespersonByRevenue).getName(), CurrencyFormat.format(maxRevenue));
        }
        System.out.println("-------------------------------------------------");
        // top salesperson by sale count
        if (topSalespersonByCount != null) {
            System.out.printf("Top Salesperson by Sales Count: ID: %s, Sales Count: %d\n", UserMenu.getUserById(topSalespersonByCount).getName(), maxSalesCount);
        }
        System.out.println("-------------------------------------------------");
        System.out.println("Revenue by Salesperson:");
        for (Map.Entry<String, Double> entry : salespersonRevenue.entrySet()) {
            System.out.printf("Salesperson : %s, Revenue: %s\n", UserMenu.getUserById(entry.getKey()).getName(), CurrencyFormat.format(entry.getValue()));
        }
        System.out.println("-------------------------------------------------");
        System.out.println("Sale by Salesperson:");
        for (Map.Entry<String, Integer> entry : salespersonSales.entrySet()) {
            System.out.printf("Salesperson : %s, Sale: %d\n", UserMenu.getUserById(entry.getKey()).getName(), entry.getValue());
        }
        System.out.println("-------------------------------------------------");
    }

    public static void viewTransactionsBySalesperson(String salespersonId, LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> transactionsInRange = getSaleTransactionsBetween(startDate, endDate);

        List<SaleTransaction> filteredTransactions = transactionsInRange.stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId))
                .toList();

        double totalSalesRevenue = calculateSalespersonRevenue(salespersonId, startDate, endDate);
        int transactionCount = filteredTransactions.size();
        System.out.println("-------------------------------------------------");
        System.out.println("Sales Transactions for Salesperson: " + UserMenu.getUserById(salespersonId).getName());
        System.out.println("Total Sales Revenue: " + String.format("%s", CurrencyFormat.format(totalSalesRevenue)));
        System.out.println("Total Number of Transactions: " + transactionCount);
        System.out.println("-------------------------------------------------");
        if (!filteredTransactions.isEmpty()) {
            System.out.println("\nTransaction Details:");
            for (SaleTransaction transaction : filteredTransactions) {
                System.out.println(transaction.getFormattedSaleTransactionDetails());
                System.out.println("--------------------------------------------------");
            }
        }
    }


}