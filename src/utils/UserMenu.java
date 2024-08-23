package utils;

import data.Database;
import data.user.UserDatabase;
import user.Mechanic;
import user.Salesperson;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static user.User.ROLE;
import static utils.Menu.getOption;


public class UserMenu {
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

    public static void menu() {
        System.out.println("User Menu");
        System.out.println("----------------");
        System.out.println("1. View All Users");
        System.out.println("2. View Users by Role");
        System.out.println("3. View User by ID");
        System.out.println("4. Delete User");
        System.out.println("5. Update User");
        System.out.println("6. Exit");
        System.out.println("----------------");
    }

    public static void mainMenu(Menu mainMenu) throws Exception {
        int option = 0;
        Scanner input = new Scanner(System.in);
        do {
            menu();
            option = getOption(option, input);
            switch (option) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    System.out.println("View Users by Role");
                    System.out.println("Select a role:");
                    System.out.println("1. CLIENT");
                    System.out.println("2. SALESPERSON");
                    System.out.println("3. MECHANIC");
                    System.out.println("4. MANAGER");
                    int roleOption = input.nextInt();

                    User.ROLE role = switch (roleOption) {
                        case 1 -> ROLE.CLIENT;
                        case 2 -> ROLE.EMPLOYEE;
                        case 3 -> ROLE.MANAGER;
                        default -> {
                            System.out.println("Invalid role option.");
                            yield null;
                        }
                    };

                    if (role != null) {
                        viewUsersByRole(role);
                    }
                    break;
                case 3:
                    input.nextLine();
                    System.out.println("View User by ID");
                    System.out.println("Please input the user ID:");
                    String userIDforview = input.nextLine();
                    getUserById(userIDforview);
                    break;
                case 4:
                    System.out.println("Delete User:");
                    UserMenu.displayAllUsers();
                    System.out.println("Please input the user's ID to delete:");
                    String userID = input.next();
                    User.deleteUser(userID);
                    break;
                case 5:
                    input.nextLine();// remove the /n in nextInt
                    System.out.println("Update User");
                    System.out.println("Please input the user ID:");
                    String userIDforUpdate = input.nextLine();
                    var userUpdate = getUserById(userIDforUpdate);
                    if (userUpdate != null) {
                        boolean continueUpdate = true;
                        int updateOption = 0;
                        do {
                            System.out.println("What would you like to update?");
                            System.out.println("1. User Name");
                            System.out.println("2. Password");
                            System.out.println("3. Name");
                            System.out.println("4. Date of Birth");
                            System.out.println("5. Address");
                            System.out.println("6. Phone Number");
                            System.out.println("7. Email");
                            System.out.println("8. Exit");
                            updateOption = Menu.getOption(updateOption, input);
                            User.modifyUser(userIDforUpdate, updateOption);
                            if (updateOption == 8) {
                                continueUpdate = false;
                            }
                        } while (continueUpdate);
                    } else {
                        System.out.println("No userID match the account in the database.");
                    }
                    break;
                case 6:
                    System.out.println("Exiting User Menu");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        } while (option != 6);
        mainMenu.mainMenu();
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

    public static void viewAllUsers() {
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

    public static void viewUsersByRole(ROLE role) {
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
