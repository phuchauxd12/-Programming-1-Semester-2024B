package utils;

import java.util.Scanner;

import static utils.CarAndAutoPartMenu.getOption;

public class StatisticsMenu {

    public static void managerMenu() {
        System.out.println("Statistics Menu");
        System.out.println("----------------");
        System.out.println("1. Revenue of All Cars Sold");
        System.out.println("2. Revenue of Services by a mechanic (in specific period)");
        System.out.println("3. Revenue of Sales by a salesperson (in specific period)");
        System.out.println("4. Revenue of Shop (in specific period)");
        System.out.println("5. List All Cars Sold (in specific period)");
        System.out.println("6. List All Transactions (in specific period)");
        System.out.println("7. List All Services (in specific period)");
        System.out.println("8. List all Sales by a salesperson");
        System.out.println("9. List all Services by a mechanic");
        System.out.println("10. Exit");
        System.out.println("----------------");
    }

    public static void salesPersonMenu() {
        System.out.println("Statistics Menu");
        System.out.println("----------------");
        System.out.println("1. List All Transactions by me");
        System.out.println("2. List Transactions by me (Day/week/month)");
        System.out.println("3. List All Cars sold by me");
        System.out.println("4. List Cars sold by me (Day/week/month)");
        System.out.println("5. List All AutoPart sold by me");
        System.out.println("6. List AutoPart sold by me (Day/week/month)");
        System.out.println("7. Revenue of Sales by me");
        System.out.println("8. Revenue of Sales by me (specific period)");
        System.out.println("10. Exit");
        System.out.println("----------------");
    }

    public static void mechanicMenu() {
        System.out.println("Statistics Menu");
        System.out.println("----------------");
        System.out.println("1. List All Services done by me");
        System.out.println("2. List Services done by me (Day/week/month)");
        System.out.println("3. Revenue of Services by me");
        System.out.println("4. Revenue of Services by me (Day/week/month)");
        System.out.println("10. Exit");
        System.out.println("----------------");
    }

    public static void clientMenu() {
        System.out.println("Statistics Menu");
        System.out.println("----------------");
        System.out.println("1. List All Services done for me");
        System.out.println("2. List All Services done for me (day/week/month)");
        System.out.println("3. List all Sales Transactions for me");
        System.out.println("4. List All Transactions done for me (day/week/month)");
        System.out.println("10. Exit");
        System.out.println("----------------");

    }

    public static void mainMenu() {
        int option = 0;
        Scanner input = new Scanner(System.in);
        do {
            option = getOption(option, input);
            switch (option) {
                case 1:
////                    (Mechanic menu)
//                    Mechanic mechanic;
//                    mechanic.getAllServices();

//                    (Client Menu)
//                    Client client = (Client) User.getUserById(clientId);
//                    client.viewServiceHistory();
                    break;
                case 2:
                    // (Manager menu)
//                    User.displayAllMechanics();
//                    String mechanicId;
//                    Mechanic mechanic;
//                    while (true) {
//                        System.out.print("Enter mechanic ID: ");
//                        mechanicId = input.nextLine();
//                        if (!mechanicId.isEmpty()) {
//                            mechanic = (Mechanic) User.getUserById(mechanicId);
//                            if (mechanic != null) {
//                                break;
//                            } else {
//                                System.out.println("Mechanic not found. Please try again.");
//                            }
//                            System.out.println("Mechanic ID cannot be empty. Please try again.");
//                        }
//                    }
//                    double result = mechanic.getRevenueInASpecificPeriod();
//                    System.out.println("Total Revenue of Sales by " + mechanicId + ": " + result);

                    // (Client menu)
//                    Client client;
//                    client.viewServiceHistoryInSpecificPeriod();

                    break;
                case 3:
                    // (Manager menu)
//                    User.displayAllSalespperson();
//                    String salespersonId;
//                    Salesperson salesperson;
//                    while (true) {
//                        System.out.print("Enter salesperson ID: ");
//                        salespersonId = input.nextLine();
//                        if (!salespersonId.isEmpty()) {
//                            salesperson = (Salesperson) User.getUserById(salespersonId);
//                            if (salesperson != null) {
//                                break;
//                            } else {
//                                System.out.println("Salesperson not found. Please try again.");
//                            }
//                            System.out.println("Salesperson ID cannot be empty. Please try again.");
//                        }
//                    }
//                    double result = salesperson.getRevenueInSpecificPeriod();
//                    System.out.println("Total Revenue of Sales by " + salespersonId + ": " + result);

//                    // (Mechanic menu)
//                    String mechanicId;
//                    Mechanic mechanic = (Mechanic) User.getUserById(mechanicId);
//                    double result = ServiceList.getRevenueByMechanic();
//                    System.out.println("Total Revenue of Sales by " + mechanicId + ": " + result);

//                    // (Client menu)
//                    Client client;
//                    client.viewTransactionsHistory();
                    break;
                case 4:
//                    // (Mechanic menu)
//                    String mechanicId;
//                    Mechanic mechanic = (Mechanic) User.getUserById(mechanicId);
//                    double result = mechanic.getRevenueInASpecificPeriod();
//                    System.out.println("Revenue of Sales by " + mechanicId + ": " + result);

//                    (Client menu)
//                    Client client;
//                    client.viewSalesTransactionsInSpecificPeriod();

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:
//                    // (Salesperson menu)
//                    String salespersonId;
//                    Salesperson salesperson = (Salesperson) User.getUserById(salespersonId);
//                    double result = salesperson.getAllRevenue();
//                    System.out.println("Total Revenue of Sales by " + salespersonId + ": " + result);
                    break;
                case 8:
                    // (Salesperson menu)
//                    String salespersonId;
//                    Salesperson salesperson = (Salesperson) User.getUserById(salespersonId);
//                    double result = salesperson.getRevenueInSpecificPeriod();
//                    System.out.println("Revenue of Sales by " + salespersonId + ": " + result);
                    break;
                case 9:
//                    (Manager menu)
//                    User.displayAllMechanics();
//                    String mechanicId;
//                    Mechanic mechanic;
//                    while (true) {
//                        System.out.print("Enter mechanic ID: ");
//                        mechanicId = input.nextLine();
//                        if (!mechanicId.isEmpty()) {
//                            mechanic = (Mechanic) User.getUserById(mechanicId);
//                            if (mechanic != null) {
//                                break;
//                            } else {
//                                System.out.println("Mechanic not found. Please try again.");
//                            }
//                            System.out.println("Mechanic ID cannot be empty. Please try again.");
//                        }
//                    }
//                    System.out.println(mechanic.getAllServices());
                    break;
                case 10:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 10);
        Menu.mainMenu();
    }
}