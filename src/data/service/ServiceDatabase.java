package data.service;

import data.Database;
import services.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ServiceDatabase {
    public static final String path = "src/data/service/Service.txt";
    private static List<Service> serviceList;


    public static void createDatabase() throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // Initialize the list of users
           serviceList = new ArrayList<>();
            Database.<Service>saveDatabase(path, serviceList, "Database file created", "Error while creating the database file.");
        } else {
            System.out.println("Database file already exists at " + path);
        }
    }

    // Method to load the list of users from the file
    public static List<Service> loadService() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            serviceList = (List<Service>) objIn.readObject();
            return serviceList;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading services from the database file.");
        }
    }


    public  static  void saveService(List<Service> dataList) throws Exception {
        Database.<Service>saveDatabase(path, dataList, "Service had been updated in the database.", "Error while updating the database file.");
    }
}
