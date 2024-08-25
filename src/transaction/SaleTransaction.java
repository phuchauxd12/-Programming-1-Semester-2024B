package transaction;

import autoPart.autoPart;
import car.Car;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import data.transaction.SaleTransactionDatabase;
import data.user.UserDatabase;
import user.Client;
import user.Membership;
import user.User;
import utils.menu.CarAndAutoPartMenu;
import utils.Status;
import utils.menu.UserMenu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class SaleTransaction implements Serializable {
    private String transactionId;
    private LocalDate transactionDate;
    private String clientId;
    private String salespersonId;
    private List<Car> purchasedCars;
    private List<autoPart> purchasedAutoParts;
    private double discount;
    private double totalAmount;
    private boolean isDeleted;
    private String additionalNotes;

    // Constructor
    public SaleTransaction(LocalDate transactionDate, String clientId, String salespersonId, List<String> itemIds) throws Exception {

        this.transactionId = generateSaleTransactionID();
        this.transactionDate = transactionDate;
        this.clientId = clientId;
        this.salespersonId = salespersonId;
        this.purchasedCars = retrieveCars(itemIds);
        this.purchasedAutoParts = retrieveAutoParts(itemIds);
        this.discount = calculateDiscount(clientId);
        this.totalAmount = calculateTotalAmount(purchasedCars, purchasedAutoParts, discount);
        this.additionalNotes = "";
        this.isDeleted = false;
    }

    public static void addSaleTransaction(SaleTransaction saleTransaction) throws Exception {
        SaleTransactionList.transactions.add(saleTransaction);
        for (User user : UserMenu.getUserList()) {
            if (user.getUserName().equals(saleTransaction.clientId)) {
                Client client = (Client) user;
                client.updateTotalSpending(saleTransaction.totalAmount);
            }
        }
        for (Car car : saleTransaction.purchasedCars) {
            car.setStatus(Status.SOLD);
        }
        for (autoPart autoPart : saleTransaction.purchasedAutoParts) {
            autoPart.setStatus(Status.SOLD);
        }

        UserDatabase.saveUsersData(UserMenu.getUserList());
        SaleTransactionDatabase.saveSaleTransaction(SaleTransactionList.transactions);
        CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
        AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());

        System.out.println("SaleTransaction added successfully!");
    }

    public static void updateSaleTransaction() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction ID to update: ");
        String transactionId = scanner.nextLine();

        SaleTransaction transaction = SaleTransactionList.getSaleTransactionById(transactionId);
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
                        for (Car car : transaction.getPurchasedCars()) {
                            car.setStatus(Status.AVAILABLE);
                        }

                        for (autoPart autoPart : transaction.getPurchasedAutoParts()) {
                            autoPart.setStatus(Status.AVAILABLE);
                        }

                        System.out.println("Enter new item IDs purchased (separated by comma): ");
                        String itemIdsInput = scanner.nextLine();
                        List<String> newItemIds = Arrays.stream(itemIdsInput.split(","))
                                .map(String::trim)
                                .map(item -> item.replaceAll(" +", " "))
                                .collect(Collectors.toList());

                        List<Car> newPurchasedCars = transaction.retrieveCars(newItemIds);
                        List<autoPart> newPurchasedAutoParts = transaction.retrieveAutoParts(newItemIds);
                        transaction.setPurchasedCars(newPurchasedCars);
                        transaction.setPurchasedAutoParts(newPurchasedAutoParts);

                        // Set new cars' status to "SOLD"
                        for (Car car : newPurchasedCars) {
                            car.setStatus(Status.SOLD);
                            CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
                        }

                        for (autoPart autoPart : newPurchasedAutoParts) {
                            autoPart.setStatus(Status.SOLD);
                            AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
                        }

                        // Update total amount and discount based on new cars
                        double newDiscount = transaction.calculateDiscount(transaction.getClientId());
                        transaction.setDiscount(newDiscount);
                        double newTotalAmount = transaction.calculateTotalAmount(newPurchasedCars, newPurchasedAutoParts, newDiscount);
                        transaction.setTotalAmount(newTotalAmount);

                        // Update client's total spending
                        Client client = (Client) UserMenu.getUserList().stream()
                                .filter(u -> u.getUserID().equals(transaction.getClientId()))
                                .findFirst()
                                .orElse(null);
                        if (client != null) {
                            client.updateTotalSpending(transaction.getTotalAmount());
                            UserDatabase.saveUsersData(UserMenu.getUserList());
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
                SaleTransactionDatabase.saveSaleTransaction(SaleTransactionDatabase.loadSaleTransaction());
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

        SaleTransaction transaction = SaleTransactionList.getSaleTransactionById(transactionId);
        if (transaction != null) {
            transaction.markAsDeleted();
            SaleTransactionDatabase.saveSaleTransaction(SaleTransactionDatabase.loadSaleTransaction());
            System.out.println("Sale transaction marked as deleted.");
        } else {
            System.out.println("Transaction not found.");
        }
    }

    private List<Car> retrieveCars(List<String> itemIds) throws Exception {
        List<Car> cars = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not
        List<Car> allCars = CarDatabase.loadCars();
        for (String itemId : itemIds) {
            Optional<Car> carOpt = allCars.stream()
                    .filter(car -> car.getCarID().equalsIgnoreCase(itemId))
                    .findFirst();
            carOpt.ifPresent(cars::add);
        }
        return cars;
    }

    private List<autoPart> retrieveAutoParts(List<String> itemIds) throws Exception {
        List<autoPart> autoParts = new ArrayList<>();
        List<autoPart> allAutoParts = AutoPartDatabase.loadAutoParts();

        for (String itemId : itemIds) {
            Optional<autoPart> autoPartOpt = allAutoParts.stream()
                    .filter(autoPart -> autoPart.getPartID().equalsIgnoreCase(itemId))
                    .findFirst();
            autoPartOpt.ifPresent(autoParts::add);
        }
        return autoParts;
    }

    double calculateDiscount(String clientId) {
        // find membership of that specific clientId
        User user = UserMenu.getUserList().stream()
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

    double calculateTotalAmount(List<Car> cars, List<autoPart> parts, double discount) {
        double total = 0;
        if (!cars.isEmpty()) {
            for (Car car : cars) {
                total += car.getPrice();
            }
        }
        if (!parts.isEmpty()) {
            for (autoPart autoPart : parts) {
                total += autoPart.getPrice();
            }
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

    public List<Car> getPurchasedCars() {
        return purchasedCars;
    }

    public List<autoPart> getPurchasedAutoParts() {
        return purchasedAutoParts;
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

    public void setPurchasedCars(List<Car> purchasedCars) {
        this.purchasedCars = purchasedCars;
    }

    public void setPurchasedAutoParts(List<autoPart> purchasedAutoParts) {
        this.purchasedAutoParts = purchasedAutoParts;
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
        if (!purchasedCars.isEmpty()) {
            List<String> cars = new ArrayList<>();
            for (Car car : purchasedCars) {
                cars.add(car.getCarModel());
            }
            sb.append("Purchased Items: ").append(String.join(", ", cars)).append("\n");
        }
        if (!purchasedAutoParts.isEmpty()) {
            List<String> carIds = new ArrayList<>();
            for (autoPart part : purchasedAutoParts) {
                carIds.add(part.getPartName());
            }
            sb.append("Purchased Items: ").append(String.join(", ", carIds)).append("\n");
        }

        if (!additionalNotes.isEmpty()) {
            sb.append("Notes: ").append(additionalNotes).append("\n");
        }
        return sb.toString();
    }
}