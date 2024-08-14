package data.autoPart;

import autoPart.autoPart;
import data.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AutoPartDatabase {
    private static final String path = "src/data/autoPart/AutoPart.txt";
    private static List<autoPart> autoPartList;

    public static void createDatabase() throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // Initialize the list of auto parts
            autoPartList = new ArrayList<>();
            Database.<autoPart>saveDatabase(path, autoPartList, "Database file created", "Error while creating the database file.");
        } else {
            System.out.println("Database file already exists at " + path);
        }
    }

    // Method to load the list of auto parts from the file
    public static List<autoPart> loadAutoParts() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            autoPartList = (List<autoPart>) objIn.readObject();
            return autoPartList;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading auto parts from the database file.");
        }
    }

    // Method to add a new auto part to the database
    public static void addAutoPart(autoPart newAutoPart) throws Exception {

        loadAutoParts();

        // Add the new auto part to the list
        autoPartList.add(newAutoPart);

        // Save the updated list back to the file
        Database.<autoPart>saveDatabase(path, autoPartList, "Auto part has been created and stored in the database.", "Error while saving the updated auto part list to the database file.");
    }

    public static autoPart findAutoPartByID(String autoPartID) throws Exception {

        loadAutoParts();

        // Find the auto part with the given ID
        var foundAutoPart = autoPartList.stream()
                .filter(autoPart -> autoPart.getPartID().equals(autoPartID))
                .findFirst();
        return foundAutoPart.orElse(null);
    }

    public static void deleteAutoPart(String autoPartID) throws Exception {
        var foundAutoPart = findAutoPartByID(autoPartID);
        if (foundAutoPart != null) {
            autoPartList = autoPartList.stream()
                    .filter(autoPart -> !autoPart.getPartID().equals(autoPartID))
                    .collect(Collectors.toList());

            Database.<autoPart>saveDatabase(path, autoPartList, "Auto part has been deleted in the database.", "Error while deleting the auto part in the database file.");
        } else {
            System.out.println("No autoPartID matches the account in the database.");
        }
    }

    public static void updateAutoPart(String autoPartID,int option) throws Exception {
        var foundAutoPart = findAutoPartByID(autoPartID);
        if (foundAutoPart != null) {
            Scanner input = new Scanner(System.in);

//                System.out.println("What would you like to update?");
//                System.out.println("1. Part Name");
//                System.out.println("2. Part Manufacturer");
//                System.out.println("3. Condition");
//                System.out.println("4. Warranty Duration");
//                System.out.println("5. Price");
//                System.out.println("6. Additional Notes");
//                System.out.println("7. Exit");

                switch (option) {
                    case 1:
                        System.out.println("Enter new part name: ");
                        foundAutoPart.setPartName(input.next());
                        System.out.println("Part name updated successfully.");
                        System.out.println(foundAutoPart);
                        break;
                    case 2:
                        System.out.println("Enter new part manufacturer: ");
                        foundAutoPart.setPartManufacturer(input.next());
                        System.out.println("Part manufacturer updated successfully.");
                        System.out.println(foundAutoPart);
                        break;
                    case 3:
                        System.out.println("Enter new part condition (NEW, USED, REFURBISHED): ");
                        autoPart.Condition condition;
                        try {
                            condition = autoPart.Condition.valueOf(input.next().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid condition. Please enter NEW, USED, or REFURBISHED.");
                            condition = autoPart.Condition.valueOf(input.next().toUpperCase());
                        }
                        foundAutoPart.setCondition(condition);
                        System.out.println("Part condition updated successfully.");
                        System.out.println(foundAutoPart);
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
                        foundAutoPart.setWarrantyMonths(warrantyMonths);
                        System.out.println("Warranty duration updated successfully.");
                        System.out.println(foundAutoPart);
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
                        foundAutoPart.setPrice(price);
                        System.out.println("Price updated successfully.");
                        System.out.println(foundAutoPart);
                        break;
                    case 6:
                        input.nextLine();
                        System.out.println("Enter new additional notes: ");
                        foundAutoPart.setAddNotes(input.nextLine());
                        System.out.println("Additional notes updated successfully.");
                        System.out.println(foundAutoPart);
                        break;
                    case 7:
                        System.out.println(foundAutoPart);
                        break;
                    default:
                        System.out.println("Invalid option.");
                        return; //End the method
                }


                Database.<autoPart>saveDatabase(path, autoPartList, "Auto part has been updated in the database.", "Error while updating the database file.");
        } else {
            System.out.println("No autoPartID matches the account in the database.");
        }
    }
}