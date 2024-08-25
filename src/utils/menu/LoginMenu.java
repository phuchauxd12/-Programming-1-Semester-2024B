package utils.menu;

import data.user.UserDatabase;
import user.User;
import utils.UserSession;

import java.util.Optional;
import java.util.Scanner;

public class LoginMenu {

    public static void displayLoginMenu() throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("LoginMenu:");
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            Optional<User> foundUser = UserDatabase.loadUsers().stream()
                    .filter(user -> user.getUserName().equals(username) && user.getPassword().equals(password))
                    .findFirst();

            if (foundUser.isPresent()) {
                User user = foundUser.get();
                System.out.println("Login successful! Welcome, " + user.getName());
                UserSession.setCurrentUser(user);
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
