package autoPart;


import data.autoPart.AutoPartDatabase;
import user.User;
import utils.CarAndAutoPartMenu;
import utils.Menu;
import utils.Status;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
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
    private LocalDateTime soldDate = null;
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

    public static autoPart createPart() {
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
        int warrantyMonths;
        while (true) {
            try {
                warrantyMonths = input.nextInt();
                break; // Exit loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next(); // Clear the invalid input from the scanner buffer
            }
        }
        double price;
        System.out.println("Enter price: ");
        while (true) {
            try {
                price = input.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.next();
            }
        }
        input.nextLine();
        System.out.println("Enter additional notes: ");
        String addNotes = input.nextLine();
        autoPart part = new autoPart(partName, partManufacturer, condition, warrantyMonths, price, addNotes);
        System.out.println("Part created successfully.");
        System.out.println(part);
        return part;
    }

    public static void addPartToList(autoPart part) throws Exception {
        List<autoPart> autoPartList = AutoPartDatabase.loadAutoParts();
        autoPartList.add(part);
        AutoPartDatabase.saveAutoPartData(autoPartList);
        System.out.println("Part successfully added to list!");
    }


    public static void deletePart() throws Exception {
        CarAndAutoPartMenu.displayAllParts();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the part ID of the part you want to delete: ");
        String partID = input.next();
        autoPart part = CarAndAutoPartMenu.findAutoPartByID(partID);
        if (part == null) {
            System.out.println("Part not found.");
            return;
        }
        part.setDeleted(true);
        AutoPartDatabase.saveAutoPartData(AutoPartDatabase.loadAutoParts());
        System.out.println("Part deleted successfully.");
        System.out.println(part);
    }

    public static void updatePart(User user) throws Exception {
        CarAndAutoPartMenu.displayAllParts();
        autoPart part = null;
        Scanner input = new Scanner(System.in);
        while (part == null) {
            System.out.println("Enter the part ID of the part you want to update: ");
            String partID = input.next();
            part = CarAndAutoPartMenu.findAutoPartByID(partID);
            if (part == null) {
                System.out.println("Not found/invalid part ID. Please try again.");
            }
        }
        int option = 0;
        while (option != 7) {
            System.out.println("What would you like to update?");
            System.out.println("1. Part Name");
            System.out.println("2. Part Manufacturer");
            System.out.println("3. Condition");
            System.out.println("4. Warranty Duration");
            System.out.println("5. Price");
            System.out.println("6. Additional Notes");
            System.out.println("7. Exit");
            option = Menu.getOption(option, input);
            switch (option) {
                case 1:
                    System.out.println("Enter new part name: ");
                    part.setPartName(input.next());
                    System.out.println("Part name updated successfully.");
                    System.out.println(part);
                    break;
                case 2:
                    System.out.println("Enter new part manufacturer: ");
                    part.setPartManufacturer(input.next());
                    System.out.println("Part manufacturer updated successfully.");
                    System.out.println(part);
                    break;
                case 3:
                    System.out.println("Enter new part condition (NEW, USED, REFURBISHED): ");
                    Condition condition;
                    try {
                        condition = Condition.valueOf(input.next().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid condition. Please enter NEW, USED, or REFURBISHED.");
                        condition = Condition.valueOf(input.next().toUpperCase());
                    }
                    part.setCondition(condition);
                    System.out.println("Part condition updated successfully.");
                    System.out.println(part);
                    break;
                case 4:
                    System.out.println("Enter new warranty duration (in months): ");
                    int warrantyMonths;
                    while (true) {
                        try {
                            warrantyMonths = input.nextInt();
                            break; // Exit loop if input is valid
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            input.next(); // Clear the invalid input from the scanner buffer
                        }
                    }
                    part.setWarrantyMonths(warrantyMonths);
                    System.out.println("Warranty duration updated successfully.");
                    System.out.println(part);
                    break;
                case 5:
                    System.out.println("Enter new price: ");
                    double price;
                    while (true) {
                        try {
                            price = input.nextDouble();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            input.next();
                        }
                    }
                    part.setPrice(price);
                    System.out.println("Price updated successfully.");
                    System.out.println(part);
                    break;
                case 6:
                    input.nextLine();
                    System.out.println("Enter new additional notes: ");
                    part.setAddNotes(input.nextLine());
                    System.out.println("Additional notes updated successfully.");
                    System.out.println(part);
                    break;
                case 7:
                    AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
                    CarAndAutoPartMenu menu = new CarAndAutoPartMenu(user);
                    menu.mainMenu(user);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
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
