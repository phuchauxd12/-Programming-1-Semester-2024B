package data.user;

import user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDatabase {
    private static final String path = "src/data/user/UserAccount.txt";
    private static List<User> listUser;

    public static void createDatabase() throws Exception {
        File file = new File(path);

        if (!file.exists()) {
            // Initialize the list of users
            listUser = new ArrayList<>();

            try {
                FileOutputStream fileOut = new FileOutputStream(file);
                ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

                objOut.writeObject(listUser);
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
            listUser = (List<User>) objIn.readObject();
            return listUser;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading users from the database file.");
        }
    }

    // Method to add a new user to the database
    public static void addUser(User newUser) throws Exception {
        // Load the current list of users
        loadUsers();

        // Add the new user to the list
        listUser.add(newUser);

        // Save the updated list back to the file
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(listUser);
            System.out.println("Account had been created and stored in the database.");
        } catch (IOException e) {
            throw new Exception("Error while saving the updated user list to the database file.");
        }
    }
    public static Optional<User> findUserByID(String userID) throws Exception {
        // Load the current list of users
        loadUsers();

        // Find the user with the given userID
        return listUser.stream()
                .filter(user -> user.getUserID().equals(userID))
                .findFirst();
    }
    public static void deleteUser(String userID) throws Exception {
        // Load the current list of users
        loadUsers();
        var foundUser = findUserByID(userID);
        // Add the new user to the list
        if(foundUser.isPresent()){
            listUser = listUser.stream()
                    .filter(user -> !user.getUserID().equals(userID))
                    .collect(Collectors.toList());

            // Save the updated list back to the file
            try (FileOutputStream fileOut = new FileOutputStream(path);
                 ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
                objOut.writeObject(listUser);
                System.out.println("Account had been deleted in the database.");
            } catch (IOException e) {
                throw new Exception("Error while deleting the updated user list to the database file.");
            }
        }
        else {
            System.out.println("No userID match the account in the database.");
        }
    }
}