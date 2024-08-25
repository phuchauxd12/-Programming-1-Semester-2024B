package utils.menu;

import data.Database;
import data.user.UserDatabase;
import user.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static user.User.ROLE;


public class UserMenu extends FunctionalMenu {
    public UserMenu(MainMenu mainMenu) {
        super(mainMenu);
        switch (currentUser) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
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
                menuItems.put(6, "Exit");

                menuActions.put(1, this::viewAllUsers);
                menuActions.put(2, this::viewUsersByRole);
                menuActions.put(3, this::viewUserById);
                menuActions.put(4, this::deleteUser);
                menuActions.put(5, this::updateUser);
                menuActions.put(6, this::exit);
            }
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }


    public void mainMenu() {
        System.out.println("Welcome to the User Menu");
        super.mainMenu();
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

    public static String displayAllMechanics() {
        List<Mechanic> mechanics = getAllMechanics();
        StringBuilder mechanicList = new StringBuilder();
        for (Mechanic mechanic : mechanics) {
            mechanicList.append(mechanic.toString()).append("\n");
        }
        return mechanicList.toString();
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

    public static String displayAllSalespersons() {
        List<Salesperson> salespersons = getAllSalespersons();
        StringBuilder salepersonsList = new StringBuilder();
        for (Salesperson saleperson : salespersons) {
            salepersonsList.append(saleperson.toString()).append("\n");
        }
        return salepersonsList.toString();
    }

    private void updateUser() {
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

                try {
                    User.modifyUser(userIDForUpdate, updateOption);
                } catch (Exception e) {
                    System.out.println("Error while updating user: " + e.getMessage());
                }
                if (updateOption == 8) {
                    continueUpdate = false;
                }
            } while (continueUpdate);
        } else {
            System.out.println("No userID match the account in the database.");
        }
    }

    private void deleteUser() {
        Scanner input = new Scanner(System.in);
        System.out.println("Delete User:");
        UserMenu.displayAllUsers();
        System.out.println("Please input the user's ID to delete:");
        String userID = input.next();
        try {
            User.deleteUser(userID);
        } catch (Exception e) {
            System.out.println("Error while deleting user: " + e.getMessage());
        }
    }

    private void viewUserById() {
        Scanner input = new Scanner(System.in);
        System.out.println("View User by ID");
        System.out.println("Please input the user ID:");
        String userIDforview = input.nextLine();
        System.out.println(getUserById(userIDforview));
    }

    public void viewUsersByRole() {
        System.out.println("View Users by Role");
        System.out.println("Select a role:");
        System.out.println("1. CLIENT");
        System.out.println("2. SALESPERSON");
        System.out.println("3. MECHANIC");
        System.out.println("4. MANAGER");
        int roleOption = input.nextInt();

        ROLE role = switch (roleOption) {
            case 1 -> ROLE.CLIENT;
            case 2 -> ROLE.EMPLOYEE;
            case 3 -> ROLE.MANAGER;
            default -> {
                System.out.println("Invalid role option.");
                yield null;
            }
        };
        try {
            List<User> userByRoleList = new ArrayList<>();

            for (User user : UserList) {
                if (user.getRole() == role) {
                    userByRoleList.add(user);
                }
            }

            if (userByRoleList.isEmpty()) {
                System.out.println("No users found with role: " + role);
            } else {
                userByRoleList.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error while viewing users by role: " + e.getMessage());
        }
    }
}
