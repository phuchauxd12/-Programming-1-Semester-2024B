package transaction;

import autoPart.autoPart;
import car.Car;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import data.transaction.SaleTransactionDatabase;
import data.user.UserDatabase;
import user.*;
import utils.CurrencyFormat;
import utils.DatePrompt;
import utils.Status;
import utils.UserSession;
import utils.menu.ActivityLogMenu;
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
    public SaleTransaction(LocalDate transactionDate, String clientId, String salespersonId, Set<String> itemIds) throws Exception {

        this.transactionId = generateSaleTransactionID();
        this.transactionDate = transactionDate;
        this.clientId = clientId;
        this.salespersonId = salespersonId;
        this.purchasedCars = retrieveCars(itemIds);
        this.purchasedAutoParts = retrieveAutoParts(itemIds);
        this.discount = calculateDiscount(clientId);
        this.totalAmount = calculateTotalAmount(discount);
        Client client = (Client) UserMenu.getUserById(clientId);
        client.updateTotalSpending(totalAmount);
        this.additionalNotes = "";
        this.isDeleted = false;
    }

    public static boolean addSaleTransaction(SaleTransaction saleTransaction) throws Exception {
        if (saleTransaction.purchasedCars.isEmpty() && saleTransaction.purchasedAutoParts.isEmpty()) {
            System.out.println("No valid items found. Sale Transaction not added.");
            return false;
        }
        SaleTransactionList.transactions.add(saleTransaction);

        for (Car car : saleTransaction.purchasedCars) {
            car.setStatus(Status.SOLD);
            car.setSoldDate(saleTransaction.transactionDate);
            car.setClientID(saleTransaction.clientId);
            break;

        }
        for (autoPart parts : saleTransaction.purchasedAutoParts) {
            parts.setStatus(Status.SOLD);
            parts.setSoldDate(saleTransaction.transactionDate);
            break;
        }
        UserDatabase.saveUsersData(UserMenu.getUserList());
        SaleTransactionDatabase.saveSaleTransaction(SaleTransactionList.transactions);
        CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
        AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
        System.out.println("SaleTransaction added successfully!");
        return true;
    }

    public static void updateSaleTransaction() throws Exception {
        Scanner scanner = new Scanner(System.in);
        User current = UserSession.getCurrentUser();

        System.out.print("Enter transaction ID to update: ");
        String transactionId = scanner.nextLine();

        SaleTransaction transaction;
        if (current instanceof Manager) {
            transaction = SaleTransactionList.getSaleTransactionById(transactionId);
        } else if (current instanceof Salesperson) {
            SaleTransaction detectedTransaction = SaleTransactionList.getSaleTransactionById(transactionId);
            if (detectedTransaction.getSalespersonId().equals(current.getUserID())) {
                transaction = detectedTransaction;
            } else {
                transaction = null;
            }
        } else {
            transaction = null;
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
                            LocalDate newDate = DatePrompt.getDate("new transaction");
                            transaction.setTransactionDate(newDate);
                            for (Car car : transaction.getPurchasedCars()) {
                                car.setSoldDate(newDate);
                            }
                            for (autoPart part : transaction.getPurchasedAutoParts()) {
                                part.setSoldDate(newDate);
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter a valid date (YYYY-MM-DD).");
                            return;
                        }
                        break;
                    case "2":
                        List<Car> oldCars = transaction.getPurchasedCars();
                        List<autoPart> oldParts = transaction.getPurchasedAutoParts();
                        // Set old cars' status to "AVAILABLE"
                        for (Car car : transaction.getPurchasedCars()) {
                            car.setStatus(Status.AVAILABLE);
                            car.setSoldDate(null);
                            car.setClientID(null);
                            break;
                        }
                        for (autoPart oldPart : transaction.getPurchasedAutoParts()) {
                            oldPart.setStatus(Status.AVAILABLE);
                            oldPart.setSoldDate(null);
                            break;
                        }
                        System.out.println("Car:");
                        CarAndAutoPartMenu.getCarsList().stream().filter(car -> !car.isDeleted() && car.getStatus() == Status.AVAILABLE).forEach(System.out::println);
                        System.out.println("Part:");
                        CarAndAutoPartMenu.getAutoPartsList().stream().filter(part -> !part.isDeleted() && part.getStatus() == Status.AVAILABLE).forEach(System.out::println);
                        System.out.println("Enter new item IDs purchased (separated by comma) (leave blank if you want to keep old items): ");
                        String itemIdsInput = scanner.nextLine();
                        Set<String> newItemIds = Arrays.stream(itemIdsInput.split(","))
                                .map(String::trim)
                                .map(item -> item.replaceAll(" +", " "))
                                .collect(Collectors.toSet());

                        List<Car> newPurchasedCars = transaction.retrieveCars(newItemIds);
                        List<autoPart> newPurchasedAutoParts = transaction.retrieveAutoParts(newItemIds);
                        if (newPurchasedCars.isEmpty() && newPurchasedAutoParts.isEmpty()) {
                            for (Car oldCar : oldCars) {
                                for (Car car : CarAndAutoPartMenu.getCarsList()) {
                                    if (car.getCarID().equals(oldCar.getCarID())) {
                                        car.setStatus(Status.SOLD);
                                        car.setSoldDate(transaction.transactionDate);
                                        car.setClientID(transaction.clientId);
                                        break;
                                    }
                                }
                            }
                            for (autoPart oldPart : oldParts) {
                                for (autoPart autoPart : CarAndAutoPartMenu.getAutoPartsList()) {
                                    if (autoPart.getPartID().equals(oldPart.getPartID())) {
                                        autoPart.setStatus(Status.SOLD);
                                        autoPart.setSoldDate(transaction.transactionDate);
                                        break;
                                    }
                                }
                            }
                            System.out.println("No valid items found. Sale Transaction not updated.");
                            return;
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

                        transaction.setPurchasedCars(newPurchasedCars);
                        transaction.setPurchasedAutoParts(newPurchasedAutoParts);

                        // Set new cars' status to "SOLD"
                        for (Car car : newPurchasedCars) {
                            car.setStatus(Status.SOLD);
                            car.setSoldDate(transaction.transactionDate);
                            car.setClientID(transaction.clientId);
                            break;
                        }

                        for (autoPart newPart : newPurchasedAutoParts) {
                            newPart.setStatus(Status.SOLD);
                            newPart.setSoldDate(transaction.transactionDate);
                            break;
                        }

                        // Update total amount and discount based on new cars
                        double newDiscount = transaction.calculateDiscount(transaction.getClientId());
                        transaction.setDiscount(newDiscount);
                        double newTotalAmount = transaction.calculateTotalAmount(newDiscount);
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
            try {
                ActivityLogMenu.addActivityLogForCurrentUser("Updated transaction with ID: " + transactionId);
            } catch (Exception e) {
                System.out.println("Error logging sale transaction action history: " + e.getMessage());
            }
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
        if (current instanceof Manager) {
            transaction = SaleTransactionList.getSaleTransactionById(transactionId);
        } else if (current instanceof Salesperson) {
            SaleTransaction detectedTransaction = SaleTransactionList.getSaleTransactionById(transactionId);
            if (detectedTransaction.getSalespersonId().equals(current.getUserID())) {
                transaction = detectedTransaction;
            }
        }

        if (transaction != null) {
            transaction.markAsDeleted();

            // Set purchased items in the transaction to be available
            for (Car car : transaction.getPurchasedCars()) {
                car.setStatus(Status.AVAILABLE);
                car.setSoldDate(null);
                car.setClientID(null);
                break;

            }

            for (autoPart oldPart : transaction.getPurchasedAutoParts()) {
                oldPart.setStatus(Status.AVAILABLE);
                oldPart.setSoldDate(null);
                break;
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
            try {
                ActivityLogMenu.addActivityLogForCurrentUser("Deleted sales transaction with ID: " + transactionId);
            } catch (Exception e) {
                System.out.println("Error logging sale transaction action history: " + e.getMessage());
            }
        } else {
            System.out.println("Transaction not found.");
        }
    }

    private List<Car> retrieveCars(Set<String> itemIds) throws Exception {
        List<Car> cars = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not
        List<Car> allCars = CarAndAutoPartMenu.getCarsList();
        for (String itemId : itemIds) {
            Optional<Car> carOpt = allCars.stream()
                    .filter(car -> car.getCarID().equalsIgnoreCase(itemId) && car.getStatus() == Status.AVAILABLE)
                    .findFirst();
            carOpt.ifPresent(cars::add);
        }
        return cars;
    }

    private List<autoPart> retrieveAutoParts(Set<String> itemIds) throws Exception {
        List<autoPart> autoParts = new ArrayList<>();
        List<autoPart> allAutoParts = CarAndAutoPartMenu.getAutoPartsList();

        for (String itemId : itemIds) {
            Optional<autoPart> autoPartOpt = allAutoParts.stream()
                    .filter(autoPart -> autoPart.getPartID().equalsIgnoreCase(itemId) && autoPart.getStatus() == Status.AVAILABLE)
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

    private double calculateSubTotal(List<Car> cars, List<autoPart> parts) {
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
        return total;
    }

    private double calculateTotalAmount(double discount) {
        return calculateSubTotal(purchasedCars, purchasedAutoParts) * (1 - discount);
    }

    private double getDiscountAmount(double discount) {
        return calculateSubTotal(purchasedCars, purchasedAutoParts) * discount;
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
                ", totalAmount=" + CurrencyFormat.format(totalAmount) +
                ", isDeleted=" + isDeleted +
                ", additionalNotes='" + additionalNotes + '\'' +
                '}';
    }

    public String getFormattedSaleTransactionDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Date: ").append(transactionDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        sb.append("Client: ").append(UserMenu.getUserById(clientId).getName()).append("\n");
        sb.append("Salesperson: ").append(UserMenu.getUserById(salespersonId).getName()).append("\n");
        sb.append("Subtotal: ").append(CurrencyFormat.format(calculateSubTotal(purchasedCars, purchasedAutoParts))).append("\n");
        sb.append("Discount: ").append(CurrencyFormat.format(getDiscountAmount(discount))).append("\n");
        sb.append("Total: ").append(CurrencyFormat.format(totalAmount)).append("\n");
        if (!purchasedCars.isEmpty()) {
            List<String> cars = new ArrayList<>();
            for (Car car : purchasedCars) {
                cars.add(car.getCarMake() + " " + car.getCarModel() + " " + car.getCarYear());
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