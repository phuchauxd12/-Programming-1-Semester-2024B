package transaction;

import autoPart.autoPart;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class SaleTransaction implements Serializable {
    private String transactionId;
    private LocalDate transactionDate;
    private String clientId;
    private String salespersonId;
    private List<autoPart> purchasedItems;
    private double discount;
    private double totalAmount;
    private String additionalNotes;


    // Constructor
    public SaleTransaction(LocalDate transactionDate, String clientId, String salespersonId, double discount, double totalAmount) {
        this.transactionId = generateSaleTransactionID();
        this.transactionDate = transactionDate;
        this.clientId = clientId;
        this.salespersonId = salespersonId;
        this.purchasedItems = purchasedItems;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.additionalNotes = "";
    }

    private String generateSaleTransactionID() {
        return "t-" + UUID.randomUUID().toString();
    }
    public String getTransactionId() { return transactionId; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getClientId() { return clientId; }
    public String getSalespersonId() { return salespersonId;}
    public List<autoPart> getPurchasedItems() { return purchasedItems; }
    public double getDiscount() { return discount; }
    public double getTotalAmount() { return totalAmount; }
    public String getNotes() { return additionalNotes; }


    public void setTransactionId(String id) {  this.transactionId = id; }
    public void setTransactionDate(LocalDate date) {  this.transactionDate = date; }
    public void setClientId(String clientId) {  this.clientId = clientId; }
    public void setSalespersonId(String salespersonId) {  this.salespersonId = salespersonId;}
    public void setPurchasedItems(List<autoPart> purchasedItems) {  this.purchasedItems = purchasedItems; }
    public void setDiscount(double discount) {  this.discount = discount; }
    public void setTotalAmount(double totalAmount) {  this.totalAmount = totalAmount; }
    public void setNotes(String additionalNotes) {  this.additionalNotes = additionalNotes; }

    public String getFormattedSaleTransactionDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Date: ").append(transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n");
        sb.append("Client ID: ").append(clientId).append("\n");
        sb.append("Salesperson ID: ").append(salespersonId).append("\n");
        sb.append("Discount: $").append(String.format("%.2f", discount)).append("\n");
        sb.append("Amount: $").append(String.format("%.2f", totalAmount)).append("\n");

        // TODO: need AutoPart Class for this operation
        // if (!purchasedItems.isEmpty()) {
        //     List<String> partNames = new ArrayList<>();
        //     for (AutoPart part : purchasedItems) {
        //         partNames.add(part.getPartName());
        //     }
        //     sb.append("Purchased Items: ").append(String.join(", ", partNames)).append("\n");
        // }

        if (!additionalNotes.isEmpty()) {
            sb.append("Notes: ").append(additionalNotes).append("\n");
        }
        return sb.toString();
    }
}