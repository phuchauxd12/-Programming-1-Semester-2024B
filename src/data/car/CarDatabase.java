package data.car;

import car.Car;
import data.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class CarDatabase {
    public static final String path = "src/data/car/Car.txt";
    private static List<Car> carList;


    public static void createDatabase() throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // Initialize the list of users
            carList = new ArrayList<>();
            Database.<Car>saveDatabase(path, carList, "Database file created", "Error while creating the database file.");
        } else {
            System.out.println("Database file already exists at " + path);
        }
    }

    // Method to load the list of users from the file
    public static List<Car> loadCars() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            carList = (List<Car>) objIn.readObject();
            return carList;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading cars from the database file.");
        }
    }


    public  static  void saveCarData(List<Car> dataList) throws Exception {
        Database.<Car>saveDatabase(path, dataList, "Car had been updated in the database.", "Error while updating the database file.");
    }
}