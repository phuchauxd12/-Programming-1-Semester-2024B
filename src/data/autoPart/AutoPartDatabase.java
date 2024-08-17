package data.autoPart;

import autoPart.autoPart;
import data.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class AutoPartDatabase {
    public static final String path = "src/data/autoPart/AutoPart.txt";
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


    public  static  void saveAutoPartData(List<autoPart> dataList) throws Exception {
        Database.<autoPart>saveDatabase(path, dataList, "Auto part had been updated in the database.", "Error while updating the database file.");
    }
}