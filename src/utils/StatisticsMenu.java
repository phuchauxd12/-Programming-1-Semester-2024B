package utils;

import java.util.Scanner;

import static utils.CarAndAutoPartMenu.getOption;

public class StatisticsMenu {

    public static void menu() {
        System.out.println("Statistics Menu");
        System.out.println("----------------");
        System.out.println("1. List all auto parts sold in a day/week/month");
        // Manager or Mechanic Menu
        System.out.println("2. List all services in a day/week/month");
        // Salesperson Menu
        System.out.println("2. Revenue of transactions sold by me");
        // Mechanic Menu
        System.out.println("3. Revenue of services done by me");
        // Manager or Salesperson Menu
        System.out.println("3. List all cars sold in a day/week/month");
        System.out.println("4. List all transactions in a day/week/month");
        // Manager Menu
        System.out.println("5. Number of cars sold in a month");
        System.out.println("6. Revenue in a day/week/month");
        System.out.println("7. Revenue of services done by a mechanic");
        System.out.println("8. Revenue of transactions sold by a salesperson");
        // Exit
        System.out.println("9. Exit");
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
                    // List all auto parts sold in a day/week/month
                    System.out.println("List all auto parts sold in a day/week/month");
                    break;
                case 2:
                    // (if manager or mechanic) List all services in a day/week/month
                    System.out.println("List all services in a day/week/month");
                    // (if salesperson) Revenue of transactions sold by me
                    System.out.println("Revenue of transactions sold by me");
                    break;
                case 3:
                    // (if mechanic) Revenue of services done by me
                    System.out.println("Revenue of services done by me");
                    // (if manager or salesperson) List all cars sold in a day/week/month
                    System.out.println("List all cars sold in a day/week/month");
                    break;
                case 4:
                    // (if manager or salesperson) List all cars sold in a day/week/month
                    System.out.println("List all cars sold in a day/week/month");
                    // else sout you are not authorized --> mainMenu();
                    break;
                case 5:
                    // (if manager) List all transactions in a day/week/month
                    System.out.println("List all transactions in a day/week/month");
                    // else sout you are not authorized --> mainMenu();
                    break;
                case 6:
                    // (if manager) Number of cars sold in a month
                    System.out.println("Number of cars sold in a month");
                    // else sout you are not authorized --> mainMenu();
                    break;
                case 7:
                    // (if manager) Revenue in a day/week/month
                    System.out.println("Revenue in a day/week/month");
                    // else sout you are not authorized --> mainMenu();
                    break;
                case 8:
                    // (if manager) Revenue of services done by a mechanic
                    System.out.println("Revenue of services done by a mechanic");
                    // else sout you are not authorized --> mainMenu();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 9);
        Menu.mainMenu();
    }
}