package transaction;

import car.Car;
import data.Database;
import data.transaction.SaleTransactionDatabase;
import user.Client;
import user.Membership;
import user.User;
import utils.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SaleTransaction implements Serializable {
    private String transactionId;
    private LocalDate transactionDate;
    private String clientId;
    private String salespersonId;
    private List<Car> purchasedItems;
    private double discount;
    private double totalAmount;
    private boolean isDeleted;
    private String additionalNotes;

    public static List<SaleTransaction> saleTransactionList;
    // This code run one time when create an instance of a class
    static {
        try {
            if(!Database.isDatabaseExist(SaleTransactionDatabase.path)){
               SaleTransactionDatabase.createDatabase();
            };
            saleTransactionList = SaleTransactionDatabase.loadSaleTransaction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // Constructor
    public SaleTransaction(LocalDate transactionDate, String clientId, String salespersonId, List<String> carIds) throws Exception {

        this.transactionId = generateSaleTransactionID();
        this.transactionDate = transactionDate;
        this.clientId = clientId;
        this.salespersonId = salespersonId;
        this.purchasedItems = retrieveCars(carIds);;
        this.discount = calculateDiscount(clientId);
        this.totalAmount = calculateTotalAmount(purchasedItems, discount);;
        this.additionalNotes = "";
        this.isDeleted = false;
    }

    public static void addSaleTransaction(SaleTransaction saleTransaction) throws Exception {
        saleTransactionList.add(saleTransaction);
        SaleTransactionDatabase.saveSaleTransaction(saleTransactionList);
        System.out.println("sale transaction added: ");
    }

    public static SaleTransaction getSaleTransactionById(String saleTransactionId) {
        for (SaleTransaction saleTransaction: SaleTransaction.saleTransactionList) {
            if (saleTransaction.getTransactionId().equals(saleTransactionId)) {
                return saleTransaction;
            }
        }
        return null;
    }
    public static void updateSaleTransaction() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction ID to update: ");
        String transactionId = scanner.nextLine();

        SaleTransaction transaction = SaleTransaction.getSaleTransactionById(transactionId);
        if (transaction != null && !transaction.isDeleted()) {
            System.out.println("Which field would you like to update?");
            System.out.println("1. Transaction Date");
            System.out.println("2. Purchased Items");
            System.out.println("3. Additional Notes");
            System.out.print("Enter the number corresponding to the field (or multiple numbers separated by space): ");
            String choiceInput = scanner.nextLine();
            List<String> choices = List.of(choiceInput.split("\\s+"));

            for (String choice : choices) {
                switch (choice) {
                    case "1":
                        System.out.print("Enter new transaction date (YYYY-MM-DD): ");
                        LocalDate newDate = LocalDate.parse(scanner.nextLine());
                        transaction.setTransactionDate(newDate);
                        break;
                    case "2":
                        // Set old cars' status to "AVAILABLE"
                        for (Car car : transaction.getPurchasedItems()) {
                            car.setStatus(Status.AVAILABLE);
                        }

                        System.out.println("Enter new item IDs purchased (separated by space): ");
                        String carIdsInput = scanner.nextLine();
                        List<String> newCarIds = List.of(carIdsInput.split("\\s+"));

                        List<Car> newPurchasedItems = transaction.retrieveCars(newCarIds);
                        transaction.setPurchasedItems(newPurchasedItems);

                        // Set new cars' status to "SOLD"
                        for (Car car : newPurchasedItems) {
                            car.setStatus(Status.SOLD);
                        }

                        // Update total amount and discount based on new cars
                        double newDiscount = transaction.calculateDiscount(transaction.getClientId());
                        transaction.setDiscount(newDiscount);
                        double newTotalAmount = transaction.calculateTotalAmount(newPurchasedItems, newDiscount);
                        transaction.setTotalAmount(newTotalAmount);

                        // Update client's total spending
                        Client client = (Client) User.userList.stream()
                                .filter(u -> u.getUserID().equals(transaction.getClientId()))
                                .findFirst()
                                .orElse(null);
                        if (client != null) {
                            client.updateTotalSpending(transaction.getTotalAmount());
                        }
                        break;
                    case "3":
                        System.out.print("Enter additional notes (or leave blank): ");
                        String additionalNotes = scanner.nextLine();
                        transaction.setNotes(additionalNotes);
                        break;
                    default:
                        System.out.println("Invalid choice: " + choice);
                        return;
                }
                SaleTransactionDatabase.saveSaleTransaction(saleTransactionList);
            }

            System.out.println("Sale transaction updated successfully:");
            System.out.println(transaction.getFormattedSaleTransactionDetails());
        } else {
            System.out.println("Transaction not found or it has been deleted.");
        }
    }
    public static void deleteSaleTransaction() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction ID to delete: ");
        String transactionId = scanner.nextLine();

        SaleTransaction transaction = SaleTransaction.getSaleTransactionById(transactionId);
        if (transaction != null) {
            transaction.markAsDeleted();
            SaleTransactionDatabase.saveSaleTransaction(saleTransactionList);
            System.out.println("Sale transaction marked as deleted.");
        } else {
            System.out.println("Transaction not found.");
        }
    }
    List<Car> retrieveCars(List<String> carIds) {
        List<Car> cars = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not
        for (String carId :carIds) {
            Optional<Car> carOpt = Car.carList.stream()
                    .filter(car -> car.getCarID().equalsIgnoreCase(carId))
                    .findFirst();
            carOpt.ifPresent(cars::add);
        }
        return cars;
    }

    double calculateDiscount(String clientId)  {
        // find membership of that specific clientId
        User user = User.userList.stream()
                .filter(u -> u.getUserID().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;  // Cast to Client
            Membership membership = client.getMembership();  // Access getMembership
            if (membership != null) {
                return membership.getDiscount();  // Assuming discount rate is a fraction (e.g., 0.15 for 15%)
            }
        }
        return 0;
    }

    double calculateTotalAmount(List<Car> purchasedItems, double discount) {
        double total = 0;
        for (Car car : purchasedItems) {
            total += car.getPrice();
        }
        return total * (1 - discount);
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    private String generateSaleTransactionID() {
        return "t-" + UUID.randomUUID().toString();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSalespersonId() {
        return salespersonId;
    }

    public List<Car> getPurchasedItems() {
        return purchasedItems;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getNotes() {
        return additionalNotes;
    }


    public void setTransactionId(String id) {
        this.transactionId = id;
    }

    public void setTransactionDate(LocalDate date) {
        this.transactionDate = date;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setSalespersonId(String salespersonId) {
        this.salespersonId = salespersonId;
    }

    public void setPurchasedItems(List<Car> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }


    public String getFormattedSaleTransactionDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Date: ").append(transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n");
        sb.append("Client ID: ").append(clientId).append("\n");
        sb.append("Salesperson ID: ").append(salespersonId).append("\n");
        sb.append("Discount: $").append(String.format("%.2f", discount)).append("\n");
        sb.append("Amount: $").append(String.format("%.2f", totalAmount)).append("\n");

        // TODO: need Car Class for this operation
        // if (!purchasedItems.isEmpty()) {
        //     List<String> carIds = new ArrayList<>();
        //     for (Car car : purchasedItems) {
        //         carIds.add(car.getCarID());
        //     }
        //     sb.append("Purchased Items: ").append(String.join(", ", carIds)).append("\n");
        // }

        if (!additionalNotes.isEmpty()) {
            sb.append("Notes: ").append(additionalNotes).append("\n");
        }
        return sb.toString();
    }
}