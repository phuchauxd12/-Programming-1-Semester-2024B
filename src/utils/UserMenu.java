package utils;

import user.User;

import java.util.Scanner;

import static utils.Menu.getOption;


public class UserMenu {
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

    public static void mainMenu() throws Exception {
        int option = 0;
        Scanner input = new Scanner(System.in);
        do {
            menu();
            option = getOption(option, input);
            switch (option) {
                case 1:
                    User.viewAllUsers();
                    break;
                case 2:
                    System.out.println("View Users by Role");
                    break;
                case 3:
                    System.out.println("View User by ID");
                    break;
                case 4:
                    System.out.println("Delete User");
                    break;
                case 5:
                    input.nextLine();// remove the /n in nextInt
                    System.out.println("Update User");
                    System.out.println("Please input the user ID:");
                    String userID = input.nextLine();
                    var userUpdate =User.getUserById(userID);
                    if(userUpdate!= null) {
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
//                            User.modifyUser(userID,updateOption,);
                            if (updateOption == 8) {
                                continueUpdate = false;
                            }
                        } while (continueUpdate);
                    }
                    else {
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
//        Menu.mainMenu();
    }
}
