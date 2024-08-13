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

    public static void mainMenu() {
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
                    System.out.println("Update User");
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
