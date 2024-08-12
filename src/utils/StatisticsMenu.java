package utils;

import user.Mechanic;
import user.Salesperson;
import user.User;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class StatisticsMenu {

    private static final Scanner input = new Scanner(System.in);

    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Consumer<Scanner>> menuActions = new LinkedHashMap<>();

    public enum MenuOption {
        MANAGER,
        SALESPERSON,
        MECHANIC,
        CLIENT,
    }


    public StatisticsMenu(MenuOption menuOption) {
        initializeMenu(menuOption);
    }

    private void initializeMenu(MenuOption menuOption) {
        switch (menuOption) {
            case MANAGER:
                menuItems.put(1, "Number of Cars sold (in specific period)");
                menuItems.put(2, "Revenue of Services by a mechanic (in specific period)");
                menuItems.put(3, "Revenue of Sales by a salesperson (in specific period)");
                menuItems.put(4, "Revenue of Shop (in specific period)");
                menuItems.put(5, "List All Cars Sold (in specific period)");
                menuItems.put(6, "List All Transactions (in specific period)");
                menuItems.put(7, "List All Services (in specific period)");
                menuItems.put(8, "List all Sales by a salesperson");
                menuItems.put(9, "List all Services by a mechanic");
                menuItems.put(10, "Exit");

                menuActions.put(1, this::getNumberOfCarsSoldInSpecificPeriod);
                menuActions.put(2, this::ManagerProcessMechanicRevenue);
                menuActions.put(3, this::ManagerProcessSalespersonRevenue);
                menuActions.put(5, this::getAllCarsSoldInSpecificPeriod);
                menuActions.put(6, this::getAllTransactionsInSpecificPeriod);
                menuActions.put(7, this::getAllServicesInSpecificPeriod);
                menuActions.put(8, this::getAllSalespersonSales);
                menuActions.put(9, this::getAllMechanicServices);
                menuActions.put(10, this::exit);
                break;

            case SALESPERSON:
                menuItems.put(1, "List All Transactions by me");
                menuItems.put(2, "List Transactions by me (Day/week/month)");
                menuItems.put(3, "List All Cars sold by me");
                menuItems.put(4, "List Cars sold by me (Day/week/month)");
                menuItems.put(5, "Revenue of Sales by me");
                menuItems.put(6, "Revenue of Sales by me (specific period)");
                menuItems.put(10, "Exit");

                // Add salesperson-specific actions here
                menuActions.put(1, this::getAllTransactionsByMe);
                menuActions.put(2, this::getAllTransactionsByMeInSpecificPeriod);
                menuActions.put(3, this::getAllCarsSoldBySalesperson);
                menuActions.put(4, this::getAllCarsSoldBySalespersonInSpecificPeriod);
                menuActions.put(5, this::SalespersonRevenue);
                menuActions.put(6, this::SalespersonRevenueInSpecificPeriod);
                menuActions.put(10, this::exit);
                break;

            case MECHANIC:
                menuItems.put(1, "List All Services done by me");
                menuItems.put(2, "List Services done by me (Day/week/month)");
                menuItems.put(3, "Revenue of Services by me");
                menuItems.put(4, "Revenue of Services by me (Day/week/month)");
                menuItems.put(10, "Exit");

                // Add mechanic-specific actions here
                menuActions.put(1, this::getAllMechanicServices);
                menuActions.put(2, this::getAllMechanicServicesInSpecificPeriod);
                menuActions.put(3, this::getRevenueOfServices);
                menuActions.put(4, this::getRevenueOfServicesInSpecificPeriod);
                menuActions.put(10, this::exit);
                break;

            case CLIENT:
                menuItems.put(1, "List All Services done for me");
                menuItems.put(2, "List All Services done for me (day/week/month)");
                menuItems.put(3, "List all Sales Transactions for me");
                menuItems.put(4, "List All Transactions done for me (day/week/month)");
                menuItems.put(10, "Exit");

                // Add client-specific actions here
                menuActions.put(1, this::getAllClientServices);
                menuActions.put(2, this::getAllClientServicesInSpecificPeriod);
                menuActions.put(3, this::getAllClientSalesTransactions);
                menuActions.put(4, this::getAllClientTransactionsInSpecificPeriod);
                menuActions.put(10, this::exit);
                break;

        }
    }


    public void displayMenu() {
        System.out.println("Statistics Menu");
        System.out.println("----------------");
        menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
        System.out.println("----------------");
    }

    private int getOption() {
        System.out.print("Enter your choice: ");
        while (!StatisticsMenu.input.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            StatisticsMenu.input.next();
        }
        return StatisticsMenu.input.nextInt();
    }

    public void mainMenu() {
        int option = 0;
        while (option != 10) { // 10 is assumed to be the exit option
            displayMenu();
            option = getOption();
            menuActions.getOrDefault(option, s -> System.out.println("Invalid option. Please try again.")).accept(input);
        }
    }

    public static void main(String[] args) {
        StatisticsMenu menu = new StatisticsMenu(MenuOption.MANAGER);
        menu.mainMenu();
        Menu.mainMenu();
    }

    // Manager functions
    private void getNumberOfCarsSoldInSpecificPeriod(Scanner s) {
        System.out.println("Fetching number of cars sold in a specific period...");
        // Add logic to fetch the number of cars sold
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        CarAndAutoPartMenu.getNumberOfCarsSoldInSpecificPeriod(startDate, endDate);
    }

    private void ManagerProcessMechanicRevenue(Scanner s) {
        User.displayAllMechanics();
        String mechanicId = promptForUserId("mechanic");
        Mechanic mechanic = (Mechanic) User.getUserById(mechanicId);
        if (mechanic != null) {
            LocalDate startDate = Menu.getStartDate();
            LocalDate endDate = Menu.getEndDate(startDate);
            double result = mechanic.getRevenueInASpecificPeriod(startDate, endDate);
            System.out.println("Total Revenue of Services by " + mechanicId + ": " + result);
        } else {
            System.out.println("Mechanic not found. Please try again.");
        }
    }

    private void ManagerProcessSalespersonRevenue(Scanner s) {
        User.displayAllSalespersons();
        String salespersonId = promptForUserId("salesperson");
        Salesperson salesperson = (Salesperson) User.getUserById(salespersonId);
        if (salesperson != null) {
            LocalDate startDate = Menu.getStartDate();
            LocalDate endDate = Menu.getEndDate(startDate);
            // Get start date

            double result = salesperson.getRevenueInSpecificPeriod(startDate, endDate);
            System.out.println("Total Revenue of Sales by " + salespersonId + ": " + result);
        } else {
            System.out.println("Salesperson not found. Please try again.");
        }
    }

    private void getAllCarsSoldInSpecificPeriod(Scanner s) {
        System.out.println("Fetching all cars sold in a specific period...");
        // Add logic to fetch all cars sold
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        CarAndAutoPartMenu.getAllCarsSoldInSpecificPeriod(startDate, endDate);
    }

    private void getAllTransactionsInSpecificPeriod(Scanner scanner) {
        // TODO: Implement this method (current implementation quick and dirty)
        for (User user : User.userList) {
            if (user instanceof Salesperson salesperson) {
                salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
            }
        }
    }

    private void getAllServicesInSpecificPeriod(Scanner scanner) {
        // TODO: Implement this method (current implementation quick and dirty)
        for (User user : User.userList) {
            if (user instanceof Mechanic mechanic) {
                mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
            }
        }
    }

    private void getAllMechanicServices(Scanner s) {
//        if (loggedInUser instanceof Mechanic) {
//            Mechanic mechanic = (Mechanic) loggedInUser;
//            mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
//        } else if (loggedInUser instanceof Manager) {
//            User.displayAllMechanics();
//            String mechanicId;
//            Mechanic mechanic;
//            while (true) {
//                System.out.print("Enter mechanic ID: ");
//                mechanicId = input.nextLine();
//                if (!mechanicId.isEmpty()) {
//                    mechanic = (Mechanic) User.getUserById(mechanicId);
//                    if (mechanic != null) {
//                        break;
//                    } else {
//                        System.out.println("Mechanic not found. Please try again.");
//                    }
//                    System.out.println("Mechanic ID cannot be empty. Please try again.");
//                }
//            }
//            mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
//        }
    }

    private void getAllSalespersonSales(Scanner scanner) {
//        if (loggedinUser instanceof Salesperson) {
//            Salesperson salesperson = (Salesperson) loggedinUser;
//            salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
//        } else if (loggedinUser instanceof Manager) {
//            User.displayAllSalespersons();
//            String salespersonId;
//            Salesperson salesperson;
//            while (true) {
//                System.out.print("Enter salesperson ID: ");
//                salespersonId = input.nextLine();
//                if (!salespersonId.isEmpty()) {
//                    salesperson = (Salesperson) User.getUserById(salespersonId);
//                    if (salesperson != null) {
//                        break;
//                    } else {
//                        System.out.println("Salesperson not found. Please try again.");
//                    }
//                    System.out.println("Salesperson ID cannot be empty. Please try again.");
//                }
//            }
//            salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
//        }
    }


    // Salesperson functions
    private void getAllTransactionsByMe(Scanner scanner) {
        Salesperson salesperson = null; // TODO: Get the current salesperson
        salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
    }

    private void getAllTransactionsByMeInSpecificPeriod(Scanner scanner) {
        Salesperson salesperson = null; // TODO: Get the current salesperson
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        salesperson.saleTransactionMadeByMe(startDate, endDate);
    }

    private void SalespersonRevenue(Scanner scanner) {
        Salesperson salesperson = null;  // TODO: Get the current salesperson
        double result = salesperson.getRevenueInSpecificPeriod(LocalDate.of(1970, 1, 1), LocalDate.now());
        System.out.println("Total Revenue of Sales by " + salesperson.getName() + ": " + result);
    }

    private void SalespersonRevenueInSpecificPeriod(Scanner scanner) {
        Salesperson salesperson = null;  // TODO: Get the current salesperson
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        double result = salesperson.getRevenueInSpecificPeriod(startDate, endDate);
        System.out.println("Total Revenue of Sales by " + salesperson.getName() + ": " + result);
    }

    private void getAllCarsSoldBySalesperson(Scanner scanner) {
        Salesperson salesperson = null; // TODO: Get the current salesperson
        salesperson.viewCarsSoldByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
    }

    private void getAllCarsSoldBySalespersonInSpecificPeriod(Scanner scanner) {
        Salesperson salesperson = null; // TODO: Get the current salesperson
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        salesperson.viewCarsSoldByMe(startDate, endDate);
    }

    // Mechanic functions
    private void getAllMechanicServicesInSpecificPeriod(Scanner s) {
//            Mechanic mechanic = (Mechanic) loggedInUser;
//        LocalDate startDate = Menu.getStartDate(s);
//        LocalDate endDate = Menu.getEndDate(startDate, s);
//            mechanic.servicesMadeByMe(startDate, endDate);
    }

    private void getRevenueOfServicesInSpecificPeriod(Scanner scanner) {
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        Mechanic mechanic = null; // TODO: get Mechanic
        double result = mechanic.getRevenueInASpecificPeriod(startDate, endDate);
        System.out.println("Total Revenue of Services by " + mechanic.getName() + ": " + result);
    }

    private void getRevenueOfServices(Scanner scanner) {
        Mechanic mechanic = null; // TODO: get Mechanic
        double result = mechanic.getRevenueInASpecificPeriod(LocalDate.of(1970, 1, 1), LocalDate.now());
        System.out.println("Total Revenue of Services by " + mechanic.getName() + ": " + result);
    }

    private String promptForUserId(String role) {
        String userId;
        input.nextLine(); // Clear the newline character left by nextInt()
        while (true) {
            System.out.print("Enter " + role + " ID: ");
            userId = input.nextLine();
            if (!userId.isEmpty()) {
                break;
            } else {
                System.out.println(role + " ID cannot be empty. Please try again.");
            }
        }
        return userId;
    }

    // Client functions
    private void getAllClientTransactionsInSpecificPeriod(Scanner scanner) {
//        LocalDate startDate = Menu.getStartDate();
//        LocalDate endDate = Menu.getEndDate(startDate);
//        Client client = null; // TODO: Get the current client
//        client.viewTransactionsHistoryInSpecificPeriod(startDate, endDate);
    }

    private void getAllClientSalesTransactions(Scanner scanner) {
//        Client client = null; // TODO: Get the current client
//        client.viewTransactionsHistoryInSpecificPeriod(LocalDate.of(1970, 1, 1), LocalDate.now());
    }

    private void getAllClientServicesInSpecificPeriod(Scanner scanner) {
//        Client client = null; // TODO: Get the current client
//        LocalDate startDate = Menu.getStartDate();
//        LocalDate endDate = Menu.getEndDate(startDate);
//        client.viewServiceHistoryInSpecificPeriod(startDate, endDate);
    }

    private void getAllClientServices(Scanner scanner) {
//        Client client = null; // TODO: Get the current client
//        client.viewServiceHistoryInSpecificPeriod(LocalDate.of(1970, 1, 1), LocalDate.now());
    }

    private void exit(Scanner s) {
        System.out.println("Exiting...");
    }


}
