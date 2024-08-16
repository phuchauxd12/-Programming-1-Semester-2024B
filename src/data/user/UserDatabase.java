package data.user;

import data.Database;
import user.User;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    // Method to add a new user to the database
    public static void addUser(User newUser) throws Exception {
        // Load the current list of users
        loadUsers();

        // Add the new user to the list
        userList.add(newUser);

        // Save the updated list back to the file
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(userList);
            System.out.println("Account had been created and stored in the database.");
        } catch (IOException e) {
            throw new Exception("Error while saving the updated user list to the database file.");
        }
    }
    public static Optional<User> findUserByID(String userID) throws Exception {
        // Load the current list of users
        loadUsers();

        // Find the user with the given userID
        return userList.stream()
                .filter(user -> user.getUserID().equals(userID))
                .findFirst();
    }
    public static void deleteUser(String userID) throws Exception {
        // Load the current list of users
        loadUsers();
        var foundUser = findUserByID(userID);
        // Add the new user to the list
        if(foundUser.isPresent()){
            userList = userList.stream()
                    .filter(user -> !user.getUserID().equals(userID))
                    .collect(Collectors.toList());

            // Save the updated list back to the file
            try (FileOutputStream fileOut = new FileOutputStream(path);
                 ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
                objOut.writeObject(userList);
                System.out.println("Account had been deleted in the database.");
            } catch (IOException e) {
                throw new Exception("Error while deleting the updated user list to the database file.");
            }
        }
        else {
            System.out.println("No userID match the account in the database.");
        }
    }
    public static void updateUser(String userID, int option) throws Exception {
        Optional<User> foundUserByID = findUserByID(userID);

        if (foundUserByID.isPresent()) {
            User foundUser = foundUserByID.get();
            Scanner input = new Scanner(System.in);
                switch (option) {
                    case 1:
                        System.out.println("Please input the new user name:");
                        String newUserName = input.next();
                        foundUser.setUserName(newUserName);
                        System.out.println("Updated successfully!");
                        System.out.println(foundUser);
                        break;
                    case 2:
                        System.out.println("Please input the new password:");
                        String newPassword = input.next();
                        foundUser.setPassword(newPassword);
                        System.out.println("Updated successfully!");
                        System.out.println(foundUser);
                        break;
                    case 3:
                        System.out.println("Please input the new name:");
                        String newName = input.next();
                        foundUser.setName(newName);
                        System.out.println("Updated successfully!");
                        System.out.println(foundUser);
                        break;
                    case 4:
                        LocalDate newDob;
                        while (true) {
                            try {
                                System.out.println("Please input the new date of birth (YYYY-MM-DD):");
                                newDob = LocalDate.parse(input.next());
                                break;
                            } catch (Exception e) {
                                System.out.println("Invalid input. Please input a valid date in the format YYYY-MM-DD.");
                            }
                        }
                        foundUser.setDob(newDob);
                        System.out.println("Updated successfully!");
                        System.out.println(foundUser);
                        break;
                    case 5:
                        System.out.println("Please input the new address:");
                        input.nextLine(); // Clear newline
                        String newAddress = input.nextLine();
                        foundUser.setAddress(newAddress);
                        System.out.println("Updated successfully!");
                        System.out.println(foundUser);
                        break;
                    case 6:
                        int newPhoneNum;
                        while (true) {
                            try {
                                System.out.println("Please input the new phone number:");
                                newPhoneNum = input.nextInt();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please input a valid phone number.");
                                input.nextLine();
                            }
                        }
                        foundUser.setPhoneNum(newPhoneNum);
                        System.out.println("Updated successfully!");
                        System.out.println(foundUser);
                        break;
                    case 7:
                        System.out.println("Please input the new email:");
                        String newEmail = input.next();
                        foundUser.setEmail(newEmail);
                        System.out.println("Updated successfully!");
                        System.out.println(foundUser);
                        break;

                    case 8:
                        break;
                    default:
                        System.out.println("Invalid option.");
                        return;
                }

            Database.<User>saveDatabase(path, userList, "User had been updated in the database.", "Error while updating the database file.");
        } else {
            System.out.println("No userID match the account in the database.");
        }
    }
}