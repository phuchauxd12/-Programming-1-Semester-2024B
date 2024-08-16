package utils;

import user.User;

import java.util.Scanner;

import static utils.Menu.getOption;


public class UserProfileMenu {
    public static void menu() {
        System.out.println("Profile Menu");
        System.out.println("----------------");
        System.out.println("1. Modify Account");
        System.out.println("2. Delete Account");
        System.out.println("3. Exit");
        System.out.println("----------------");
    }

    public static void mainMenu() throws Exception {
        int option = 0;
        Scanner input = new Scanner(System.in);
        do {
            menu();
            option = getOption(option, input);
            String userID = UserSession.getCurrentUser().getUserID();
            switch (option) {
                case 1:
                    input.nextLine();
                    System.out.println("Update User");
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
                            User.modifyUser(userID,updateOption);
                            if (updateOption == 8) {
                                continueUpdate = false;
                            }
                        } while (continueUpdate);
                    break;
                case 2:
                    User.deleteUser(userID);
                    break;
                case 3:
                    System.out.println("Exiting User Menu");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        } while (option != 3);
//        Menu.mainMenu();
    }
}
