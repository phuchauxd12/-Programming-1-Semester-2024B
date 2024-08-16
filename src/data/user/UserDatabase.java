package data.user;

import data.Database;
import user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    public static final String path = "src/data/user/UserAccount.txt";
    private static List<User> userList;

    public static void createDatabase() throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // Initialize the list of users
            userList = new ArrayList<>();

            try {
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

                objOut.writeObject(userList);
                System.out.println("Database file created and initialized at " + path);
            } catch (IOException e) {
                throw new Exception("Error while creating the database file.");
            }
        } else {
            System.out.println("Database file already exists at " + path);
        }
    }

    // Method to load the list of users from the file
    public static List<User> loadUsers() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            userList = (List<User>) objIn.readObject();
            return userList;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading users from the database file.");
        }
    }


    public  static  void saveUsersData(List<User> dataList) throws Exception {
        Database.<User>saveDatabase(path, dataList, "User had been updated in the database.", "Error while updating the database file.");
    }
}