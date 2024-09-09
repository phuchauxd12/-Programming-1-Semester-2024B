package utils.menu;

import data.Database;
import data.user.UserDatabase;
import user.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class UserMenu extends Menu {
    public UserMenu() {
        super();
        switch (currentUser) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            case null, default -> initializeMenu(null);
        }
    }


    protected void initializeMenu(MenuOption menuOption) {
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "View All Users");
                menuItems.put(2, "View Users by Role");
                menuItems.put(3, "View User by ID");
                menuItems.put(4, "Delete User");
                menuItems.put(5, "Update User");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::viewAllUsers);
                menuActions.put(2, this::viewUsersByRole);
                menuActions.put(3, this::viewUserById);
                menuActions.put(4, this::deleteUser);
                menuActions.put(5, this::updateUser);
                menuActions.put(0, this::exit);
            }
            case null, default -> System.out.print("");
        }
    }


    private static List<User> UserList;

    static {
        try {
            if (!Database.isDatabaseExist(UserDatabase.path)) {
                UserDatabase.createDatabase();
            }
            UserList = UserDatabase.loadUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> getUserList() {
        return UserList;
    }

    public static User getUserById(String userId) {
        for (User user : UserList) {
            if (user.getUserID().equals(userId)) {
                return user;
            }
        }
        try {
            String activityName = "Searched for user with ID: " + userId;
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error while add log in for get user by id: " + e.getMessage());
        }
        return null;
    }

    public static void displayAllUsers() {
        System.out.println("Displaying all users:");
        for (User user : UserList) {
            System.out.println(user);
        }
        System.out.println("----------------");
    }

    public void viewAllUsers() {
        System.out.println("Viewing all users:");
        for (User user : UserList) {
            System.out.println("UserID: " + user.getUserID() + ", UserName: " + user.getUserName());
        }
    }

    public static List<Mechanic> getAllMechanics() {
        List<Mechanic> mechanics = new ArrayList<>();
        for (User user : UserList) {
            if (user instanceof Mechanic) {
                mechanics.add((Mechanic) user);
            }
        }
        return mechanics;
    }

    public static void displayAllMechanics() {
        List<Mechanic> mechanics = getAllMechanics();
        StringBuilder mechanicList = new StringBuilder();
        for (Mechanic mechanic : mechanics) {
            mechanicList.append(mechanic.toString()).append("\n");
        }
        System.out.println(mechanicList);
    }

    public static List<Salesperson> getAllSalespersons() {
        List<Salesperson> salespersons = new ArrayList<>();
        for (User user : UserList) {
            if (user instanceof Salesperson) {
                salespersons.add((Salesperson) user);
            }
        }
        return salespersons;
    }

    public static void displayAllSalespersons() {
        List<Salesperson> salespersons = getAllSalespersons();
        salespersons.forEach(salesperson -> System.out.println(salesperson.getUserInfo()));
    }

    private void updateUser() {
        UserList.forEach(user -> System.out.println(user.getUserInfo()));
        Scanner input = new Scanner(System.in);
        System.out.println("Update User");
        System.out.println("Please input the user ID:");
        String userIDForUpdate = input.nextLine();
        var userUpdate = getUserById(userIDForUpdate);
        if (userUpdate != null) {
            boolean continueUpdate = true;
            int updateOption = 0;
            do {
                System.out.println("What would you like to update?");
                System.out.println("1. User Name");
                System.out.println("2. Password");
                System.out.println("3. Name");
                System.out.println("4. Address");
                System.out.println("5. Phone Number");
                System.out.println("6. Email");
                System.out.println("7. Date of Birth");
                System.out.println("8. Exit");
                updateOption = getOption(updateOption, input);
                if (updateOption == 8) {
                    continueUpdate = false;
                    continue;
                }
                try {
                    User.modifyUser(userIDForUpdate, updateOption);
                    String activityName = "Updated user with id" + userUpdate.getUserID();
                    ActivityLogMenu.addActivityLogForCurrentUser(activityName);
                } catch (Exception e) {
                    System.out.println("Error while updating user: " + e.getMessage());
                }

            } while (continueUpdate);
        } else {
            System.out.println("No userID match the account in the database.");
        }
    }

    private void deleteUser() {
        UserList.forEach(user -> System.out.println(user.getUserInfo()));
        Scanner input = new Scanner(System.in);
        System.out.println("Delete User:");
        UserMenu.displayAllUsers();
        System.out.println("Please input the user's ID to delete:");
        String userID = input.next();
        try {
            String activityName = "Deleted user with id" + userID;
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
            User.deleteUser(userID);
        } catch (Exception e) {
            System.out.println("Error while deleting user: " + e.getMessage());
        }
    }

    private void viewUserById() {
        UserList.forEach(System.out::println);
        Scanner input = new Scanner(System.in);
        System.out.println("View User by ID");
        System.out.println("Please input the user ID:");
        String userIDforview = input.nextLine();
        String activityName = "Searched for user with ID" + userIDforview;
        try {
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
            var user = getUserById(userIDforview);
            if (user != null) {
                System.out.println(user.getUserInfo());
            } else {
                System.out.println("User not found!");
            }
        } catch (Exception e) {
            System.out.println("Error while viewing users by role: " + e.getMessage());
        }
    }

    public void viewUsersByRole() {
        int option = 100;
        System.out.println("View Users by Role");
        System.out.println("Select a role:");
        System.out.println("1. CLIENT");
        System.out.println("2. SALESPERSON");
        System.out.println("3. MECHANIC");
        option = MainMenu.getOption(option, input);
        Class<?> selectedRole = null;
        switch (option) {
            case 1 -> selectedRole = Client.class;
            case 2 -> selectedRole = Salesperson.class;
            case 3 -> selectedRole = Mechanic.class;
            default -> System.out.println("Invalid option. Please try again.");
        }
        if (selectedRole != null) {
            try {
                List<User> userByRoleList = new ArrayList<>();
                for (User user : UserList) {
                    if (selectedRole.isInstance(user)) {
                        userByRoleList.add(user);
                    }
                }
                if (userByRoleList.isEmpty()) {
                    System.out.println("No users found with role: " + selectedRole.getSimpleName());
                } else {
                    userByRoleList.forEach(System.out::println);
                }
                String activityName = "View user with role: " + selectedRole.getSimpleName();
                ActivityLogMenu.addActivityLogForCurrentUser(activityName);
            } catch (Exception e) {
                System.out.println("Error while viewing users by role: " + e.getMessage());
            }
        }
    }
}
