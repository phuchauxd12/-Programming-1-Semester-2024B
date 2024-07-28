package autoPart;


import utils.CarAndAutoPartMenu;

import java.time.LocalDateTime;
import java.util.Scanner;

public class autoPart {
    private String partID;
    private String partName;
    private String partManufacturer;
    private Condition condition;
    private int warrantyMonths;
    private double price;
    private String addNotes;
    private Status status = Status.AVAILABLE;
    private LocalDateTime soldDate = null;
    private boolean isDeleted = false;
    private static long partCounter = 0;

    public enum Condition {
        NEW,
        USED,
        REFURBISHED
    }

    public enum Status {
        SOLD,
        AVAILABLE
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
        return "p-" + (++partCounter);
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

    public LocalDateTime getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDateTime soldDate) {
        this.soldDate = soldDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public static void createPart() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter part name: ");
        String partName = input.next();
        System.out.println("Enter part manufacturer: ");
        String partManufacturer = input.next();
        System.out.println("Enter part condition (NEW, USED, REFURBISHED): ");
        Condition condition;
        try {
            condition = Condition.valueOf(input.next().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid condition. Please enter NEW, USED, or REFURBISHED.");
            condition = Condition.valueOf(input.next().toUpperCase());
        }
        System.out.println("Enter warranty duration (in months): ");
        int warrantyMonths = input.nextInt();
        System.out.println("Enter price: ");
        double price = input.nextDouble();
        input.nextLine();
        System.out.println("Enter additional notes: ");
        String addNotes = input.nextLine();
        autoPart part = new autoPart(partName, partManufacturer, condition, warrantyMonths, price, addNotes);
        System.out.println("Part created successfully.");
        System.out.println(part);
        CarAndAutoPartMenu.getAutoPartsList().add(part);
    }

    public static void displayAllParts() {
        for (autoPart part : CarAndAutoPartMenu.getAutoPartsList()) {
            System.out.println(part);
        }
    }

    @Override
    public String toString() {
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
}
