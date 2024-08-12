package utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static utils.CarAndAutoPartMenu.getOption;

public class Menu {
    public static void menu() {
        System.out.println("MAIN MENU");
        System.out.println("----------------");
        System.out.println("1. User Menu");
        System.out.println("2. Car and Auto Part Menu");
        System.out.println("3. Transaction Menu");
        System.out.println("4. Statistics Menu");
        System.out.println("5. Exit");
        System.out.println("----------------");
    }

    public static LocalDate getStartDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate;
        while (true) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine());
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid start date format. Please try again.");
            }
        }
        return startDate;
    }

    public static LocalDate getEndDate(LocalDate startDate) {
        Scanner scanner = new Scanner(System.in);
        LocalDate endDate;
        while (true) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            try {
                scanner.nextLine();
                endDate = LocalDate.parse(scanner.nextLine());
                if (!endDate.isBefore(startDate)) {
                    break;
                }
                System.out.println("End date cannot be before start date. Please try again.");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid end date format. Please try again.");
            }
        }
        return endDate;
    }

    public static void mainMenu() {

        int option = 0;
        Scanner input = new Scanner(System.in);
        do {
            menu();
            option = getOption(option, input);
            switch (option) {
                case 1:
                    UserMenu.mainMenu();
                    break;
                case 2:
                    CarAndAutoPartMenu.MainMenu();
                    break;
                case 3:
                    System.out.println("Transaction Menu");
                    break;
                case 4:

                    break;
                case 5:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 5);
        System.exit(0);
    }
}
