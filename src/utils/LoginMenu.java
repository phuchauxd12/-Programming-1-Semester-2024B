package utils;

import user.User;
import java.util.Scanner;

public class LoginMenu {

    public static void displayLoginMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("LoginMenu: - Doan");
            System.out.println("Please login, input username:");
            System.out.print("=> Input username: ");
            String username = scanner.nextLine();

            System.out.print("Input password:\n=> Input password: ");
            String password = scanner.nextLine();

            User user = User.login(username, password);
            if (user != null) {
                System.out.println("=> Log in successfully");
                break;
            } else {
                System.out.println("=> Invalid username or password.");
                System.out.println("1. Retry");
                System.out.println("2. Exit");
                System.out.print("Please choose an option (1 or 2): ");
                String choice = scanner.nextLine();

                if (choice.equals("2")) {
                    System.out.println("Exiting...");
                    break;
                }
            }
        }

    }
}
