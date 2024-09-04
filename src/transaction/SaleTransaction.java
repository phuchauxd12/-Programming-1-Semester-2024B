package transaction;

import autoPart.autoPart;
import car.Car;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import data.transaction.SaleTransactionDatabase;
import data.user.UserDatabase;
import user.Client;
import user.Membership;
import user.Salesperson;
import user.User;
import utils.DatePrompt;
import utils.Status;
import utils.UserSession;
import utils.menu.CarAndAutoPartMenu;
import utils.menu.UserMenu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            if (user.getUserID().equals(saleTransaction.clientId)) {
                Client client = (Client) user;
                client.updateTotalSpending(saleTransaction.totalAmount);
                break;
            }
        }
        for (Car purchasedCar : saleTransaction.getPurchasedCars()) {
            for (Car car : CarAndAutoPartMenu.getCarsList()) {
                if (car.getCarID().equals(purchasedCar.getCarID())) {
                    car.setStatus(Status.SOLD);
                    car.setSoldDate(saleTransaction.transactionDate);
                    break;
                }
            }
        }
        for (autoPart autoPart : saleTransaction.purchasedAutoParts) {
            for (autoPart parts : CarAndAutoPartMenu.getAutoPartsList()) {
                if (parts.getPartID().equals(autoPart.getPartID())) {
                    parts.setStatus(Status.SOLD);
                    parts.setSoldDate(saleTransaction.transactionDate);
                    break;
                }
            }
        }

        UserDatabase.saveUsersData(UserMenu.getUserList());
        SaleTransactionDatabase.saveSaleTransaction(SaleTransactionList.transactions);
        CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
        AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());

        System.out.println("SaleTransaction added successfully!");
    }

    public static void updateSaleTransaction() throws Exception {
        Scanner scanner = new Scanner(System.in);
        User current = UserSession.getCurrentUser();

        System.out.print("Enter transaction ID to update: ");
        String transactionId = scanner.nextLine();

        SaleTransaction transaction = null;
        if (current.getRole().equals(User.ROLE.MANAGER)) {
            transaction = SaleTransactionList.getSaleTransactionById(transactionId);
        } else if (current.getRole().equals(User.ROLE.EMPLOYEE)) {
            if (current instanceof Salesperson) {
                SaleTransaction detectedTransaction = SaleTransactionList.getSaleTransactionById(transactionId);
                if (detectedTransaction.getSalespersonId().equals(current.getUserID())) {
                    transaction = detectedTransaction;
                }
            }
        }

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
                        try {
                            System.out.print("Enter new transaction date (dd/MM/yyyy): ");
                            String input = DatePrompt.sanitizeDateInput(scanner.nextLine());
                            LocalDate newDate = DatePrompt.validateAndParseDate(input);
                            transaction.setTransactionDate(newDate);
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter a valid date (YYYY-MM-DD).");
                            return;
                        }
                        break;
                    case "2":
                        // Set old cars' status to "AVAILABLE"
                        for (Car oldCar : transaction.getPurchasedCars()) {
                            for (Car car : CarAndAutoPartMenu.getCarsList()) {
                                if (car.getCarID().equals(oldCar.getCarID())) {
                                    car.setStatus(Status.AVAILABLE);
                                    car.setSoldDate(null);
                                    break;
                                }
                            }
                        }

                        for (autoPart oldPart : transaction.getPurchasedAutoParts()) {
                            for (autoPart autoPart : CarAndAutoPartMenu.getAutoPartsList()) {
                                if (autoPart.getPartID().equals(oldPart.getPartID())) {
                                    autoPart.setStatus(Status.AVAILABLE);
                                    autoPart.setSoldDate(null);
                                    break;
                                }
                            }
                        }

                        // Minus the old spending amount
                        for (User user : UserMenu.getUserList()) {
                            if (user.getUserID().equals(transaction.clientId)) {
                                Client client = (Client) user;
                                client.updateTotalSpending(-transaction.totalAmount);
                                UserDatabase.saveUsersData(UserMenu.getUserList());
                                break;
                            }
                        }
                        System.out.println("Car:");
                        CarAndAutoPartMenu.getCarsList().stream().filter(car -> !car.isDeleted() && car.getStatus() == Status.AVAILABLE).forEach(System.out::println);
                        System.out.println("Part:");
                        CarAndAutoPartMenu.getAutoPartsList().stream().filter(part -> !part.isDeleted() && part.getStatus() == Status.AVAILABLE).forEach(System.out::println);
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
                        for (Car newCar : newPurchasedCars) {
                            for (Car car : CarAndAutoPartMenu.getCarsList()) {
                                if (car.getCarID().equals(newCar.getCarID())) {
                                    car.setStatus(Status.SOLD);
                                    car.setSoldDate(transaction.transactionDate);
                                    break;
                                }
                            }
                        }

                        for (autoPart newPart : newPurchasedAutoParts) {
                            for (autoPart autoPart : CarAndAutoPartMenu.getAutoPartsList()) {
                                if (autoPart.getPartID().equals(newPart.getPartID())) {
                                    autoPart.setStatus(Status.SOLD);
                                    autoPart.setSoldDate(transaction.transactionDate);
                                    break;
                                }
                            }
                        }

                        // Update total amount and discount based on new cars
                        double newDiscount = transaction.calculateDiscount(transaction.getClientId());
                        transaction.setDiscount(newDiscount);
                        double newTotalAmount = transaction.calculateTotalAmount(newPurchasedCars, newPurchasedAutoParts, newDiscount);
                        transaction.setTotalAmount(newTotalAmount);

                        // Update User new spending
                        for (User user : UserMenu.getUserList()) {
                            if (user.getUserID().equals(transaction.clientId)) {
                                Client client = (Client) user;
                                client.updateTotalSpending(transaction.totalAmount);
                                UserDatabase.saveUsersData(UserMenu.getUserList());
                                break;
                            }
                        }


                        AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
                        CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
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
                SaleTransactionDatabase.saveSaleTransaction(SaleTransactionList.transactions);
            }

            System.out.println("Sale transaction updated successfully:");
            System.out.println(transaction.getFormattedSaleTransactionDetails());
        } else {
            System.out.println("Transaction not found or it has been deleted.");
        }
    }

    public static void deleteSaleTransaction() throws Exception {
        Scanner scanner = new Scanner(System.in);
        User current = UserSession.getCurrentUser();

        System.out.print("Enter transaction ID to delete: ");
        String transactionId = scanner.nextLine();
        SaleTransaction transaction = null;
        if (current.getRole().equals(User.ROLE.MANAGER)) {
            transaction = SaleTransactionList.getSaleTransactionById(transactionId);
        } else if (current.getRole().equals(User.ROLE.EMPLOYEE)) {
            if (current instanceof Salesperson) {
                SaleTransaction detectedTransaction = SaleTransactionList.getSaleTransactionById(transactionId);
                if (detectedTransaction.getSalespersonId().equals(current.getUserID())) {
                    transaction = detectedTransaction;
                }
            }
        }

        if (transaction != null) {
            transaction.markAsDeleted();

            // Set purchased items in the transaction to be available
            for (Car oldCar : transaction.getPurchasedCars()) {
                for (Car car : CarAndAutoPartMenu.getCarsList()) {
                    if (car.getCarID().equals(oldCar.getCarID())) {
                        car.setStatus(Status.AVAILABLE);
                        car.setSoldDate(null);
                        break;
                    }
                }
            }

            for (autoPart oldPart : transaction.getPurchasedAutoParts()) {
                for (autoPart autoPart : CarAndAutoPartMenu.getAutoPartsList()) {
                    if (autoPart.getPartID().equals(oldPart.getPartID())) {
                        autoPart.setStatus(Status.AVAILABLE);
                        autoPart.setSoldDate(null);
                        break;
                    }
                }
            }

            // Minus the old spending amount
            for (User user : UserMenu.getUserList()) {
                if (user.getUserID().equals(transaction.clientId)) {
                    Client client = (Client) user;
                    client.updateTotalSpending(-transaction.totalAmount);
                    break;
                }
            }

            AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
            UserDatabase.saveUsersData(UserMenu.getUserList());
            CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
            SaleTransactionDatabase.saveSaleTransaction(SaleTransactionList.transactions);
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

    @Override
    public String toString() {
        return "SaleTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", transactionDate=" + transactionDate +
                ", clientId='" + clientId + '\'' +
                ", salespersonId='" + salespersonId + '\'' +
                ", purchasedCars=" + purchasedCars +
                ", purchasedAutoParts=" + purchasedAutoParts +
                ", discount=" + discount +
                ", totalAmount=" + totalAmount +
                ", isDeleted=" + isDeleted +
                ", additionalNotes='" + additionalNotes + '\'' +
                '}';
    }

    public String getFormattedSaleTransactionDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Date: ").append(transactionDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
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