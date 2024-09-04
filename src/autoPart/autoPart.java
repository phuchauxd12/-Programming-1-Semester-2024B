package autoPart;


import utils.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class autoPart implements Serializable {
    private String partID;
    private String partName;
    private String partManufacturer;
    private Condition condition;
    private int warrantyMonths;
    private double price;
    private String addNotes;
    private Status status = Status.AVAILABLE;
    private LocalDate soldDate = null;
    private boolean isDeleted = false;


    public enum Condition {
        NEW,
        USED,
        REFURBISHED
    }


    public autoPart(String partName, String partManufacturer, Condition condition, int warrantyMonths, double price, String addNotes) {
        this.partID = generatePartID();
        this.partName = partName;
        this.partManufacturer = partManufacturer;
        this.condition = condition;
        this.warrantyMonths = warrantyMonths;
        this.price = price;
        this.addNotes = addNotes;
    }

    public String generatePartID() {
        return "p-" + UUID.randomUUID().toString();
    }

    public String getPartID() {
        return partID;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartManufacturer() {
        return partManufacturer;
    }

    public void setPartManufacturer(String partManufacturer) {
        this.partManufacturer = partManufacturer;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddNotes() {
        return addNotes;
    }

    public void setAddNotes(String addNotes) {
        this.addNotes = addNotes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDate soldDate) {
        this.soldDate = soldDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


    public String toStringDetailed() {
        return "{" +
                "partID ='" + partID + '\'' +
                ", partName ='" + partName + '\'' +
                ", partManufacturer ='" + partManufacturer + '\'' +
                ", condition = " + condition +
                ", warrantyMonths = " + warrantyMonths +
                ", price = " + price +
                ", addNotes ='" + addNotes + '\'' +
                ", status = " + status +
                ", soldDate = " + soldDate +
                ", isDeleted = " + isDeleted +
                '}';
    }

    @Override
    public String toString() {
        return "{" +
                "partID ='" + partID + '\'' +
                ", partName ='" + partName + '\'' +
                ", partManufacturer ='" + partManufacturer + '\'' +
                ", condition = " + condition +
                '}';
    }
}
