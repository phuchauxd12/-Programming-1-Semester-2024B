package transaction;

import car.Car;
import user.Client;
import user.Membership;
import user.User;
import utils.CarAndAutoPartMenu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    // Constructor
    public SaleTransaction(LocalDate transactionDate, String clientId, String salespersonId, List<String> carIds) {

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


    List<Car> retrieveCars(List<String> carIds) {
        List<Car> cars = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not
        for (String carId :carIds) {
            Optional<Car> carOpt = CarAndAutoPartMenu.getCarsList().stream()
                    .filter(car -> car.getCarID().equalsIgnoreCase(carId))
                    .findFirst();
            carOpt.ifPresent(cars::add);
        }
        return cars;
    }

    double calculateDiscount(String clientId) {
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