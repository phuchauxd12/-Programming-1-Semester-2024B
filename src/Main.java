import data.user.UserDatabase;
import user.User;
import utils.UserSession;
import utils.menu.MainMenu;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println();
        System.out.println("COSC2081 GROUP ASSIGNMENT");
        System.out.println("AUTO136 CAR DEALERSHIP MANAGEMENT SYSTEM");
        System.out.println("Instructor: Mr. Minh Vu & Mr. Dung Nguyen");
        System.out.println("Group: TEAM 1");
        System.out.println("s3958304, Ton That Huu Luan");
        System.out.println("s3979030, Nguyen Phuc Doan");
        System.out.println("s3919659, Cao Ngoc Phuong Uyen");
        System.out.println("s3975133, Doan Nguyen Phu Chau");
        System.out.println();

        /* Login */
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please login to continue:");
            System.out.println("-------------------------");
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
                System.out.println("Invalid username or password.");
                System.out.println("1. Retry");
                System.out.println("2. Exit");
                boolean validInput = false;
                String choice;
                while (!validInput) {
                    System.out.print("Please choose an option (1 or 2): ");
                    choice = scanner.nextLine();
                    if (choice.equals("1") || choice.equals("2")) {
                        validInput = true;
                        if (choice.equals("2")) {
                            System.out.println("Goodbye!");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("Invalid input. Please enter 1 or 2");
                    }
                }
            }
        }
        MainMenu menu = new MainMenu();
        menu.mainMenu();
    }

}