package utils;

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
                    StatisticsMenu.mainMenu();
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
