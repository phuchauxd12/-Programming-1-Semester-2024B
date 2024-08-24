package utils;

import autoPart.autoPart;
import car.Car;
import services.Service;
import services.ServiceList;
import transaction.SaleTransaction;
import transaction.SaleTransactionList;
import user.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import static utils.Menu.getOption;

public class StatisticsMenu {

    private static final Scanner input = new Scanner(System.in);

    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Consumer<Scanner>> menuActions = new LinkedHashMap<>();


    public StatisticsMenu(User user) {
        switch (user) {
            case Manager c -> initializeMenu(MenuOption.MANAGER, user);
            case Salesperson c -> initializeMenu(MenuOption.SALESPERSON, user);
            case Mechanic c -> initializeMenu(MenuOption.MECHANIC, user);
            case Client c -> initializeMenu(MenuOption.CLIENT, user);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }


    private void initializeMenu(MenuOption menuOption, User user) {
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
                menuItems.put(10, "View autoPart statistic");
                menuItems.put(11, "Exit");

                menuActions.put(1, this::getNumberOfCarsSoldInSpecificPeriod);
                menuActions.put(2, this::ManagerProcessMechanicRevenue);
                menuActions.put(3, this::ManagerProcessSalespersonRevenue);
                menuActions.put(4, this::ManagerProcessTotalRevenue);
                menuActions.put(5, this::getAllCarsSoldInSpecificPeriod);
                menuActions.put(6, this::getAllTransactionsInSpecificPeriod);
                menuActions.put(7, this::getAllServicesInSpecificPeriod);
                menuActions.put(8, c -> getAllSalespersonSales(user));
                menuActions.put(9, c -> getAllMechanicServices(user));
                menuActions.put(10, c -> viewAutoPartStatistics());
                menuActions.put(11, this::exit);
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
                menuActions.put(1, c -> getAllSalespersonSales(user));
                menuActions.put(2, c -> getAllTransactionsByMeInSpecificPeriod(user));
                menuActions.put(3, c -> getAllCarsSoldBySalesperson(user));
                menuActions.put(4, c -> getAllCarsSoldBySalespersonInSpecificPeriod(user));
                menuActions.put(5, c -> SalespersonRevenue(user));
                menuActions.put(6, c -> SalespersonRevenueInSpecificPeriod(user));
                menuActions.put(10, this::exit);
                break;

            case MECHANIC:
                menuItems.put(1, "List All Services done by me");
                menuItems.put(2, "List Services done by me (Day/week/month)");
                menuItems.put(3, "Revenue of Services by me");
                menuItems.put(4, "Revenue of Services by me (Day/week/month)");
                menuItems.put(10, "Exit");

                // Add mechanic-specific actions here
                menuActions.put(1, c -> getAllMechanicServices(user));
                menuActions.put(2, c -> getAllMechanicServicesInSpecificPeriod(user));
                menuActions.put(3, c -> getRevenueOfServices(user));
                menuActions.put(4, c -> getRevenueOfServicesInSpecificPeriod(user));
                menuActions.put(10, this::exit);
                break;

            case CLIENT:
                menuItems.put(1, "List All Services done for me");
                menuItems.put(2, "List All Services done for me (day/week/month)");
                menuItems.put(3, "List all Sales Transactions for me");
                menuItems.put(4, "List All Transactions done for me (day/week/month)");
                menuItems.put(10, "Exit");

                // Add client-specific actions here
                menuActions.put(1, c -> getAllClientServices(user));
                menuActions.put(2, c -> getAllClientServicesInSpecificPeriod(user));
                menuActions.put(3, c -> getAllClientSalesTransactions(user));
                menuActions.put(4, c -> getAllClientTransactionsInSpecificPeriod(user));
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


    public void mainMenu(User user) throws Exception {
        int option = 0;
        while (option != 10) { // 10 is assumed to be the exit option
            displayMenu();
            option = getOption(option, input);
            menuActions.getOrDefault(option, c -> System.out.println("Invalid option. Please try again.")).accept(input);
        }
        Menu mainMenu = new Menu(user);
        mainMenu.mainMenu();
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
        UserMenu.displayAllMechanics();
        String mechanicId = promptForUserId("mechanic");
        Mechanic mechanic = (Mechanic) UserMenu.getUserById(mechanicId);
        if (mechanic != null) {
            LocalDate startDate = Menu.getStartDate();
            LocalDate endDate = Menu.getEndDate(startDate);
            double result = ServiceList.calculateMechanicRevenue(mechanic.getName(),startDate, endDate);
            System.out.println("Total Revenue of Services by " + mechanicId + ": " + result);
        } else {
            System.out.println("Mechanic not found. Please try again.");
        }
    }

    private void ManagerProcessSalespersonRevenue(Scanner s) {
        // TODO: chỉnh lại function để lưu ID ng dùng thay vì tên (test hiện tại dùng tên cho dễ test)
        UserMenu.displayAllSalespersons();
        String salespersonId = promptForUserId("salesperson");
        Salesperson salesperson = (Salesperson) UserMenu.getUserById(salespersonId);
        if (salesperson != null) {
            LocalDate startDate = Menu.getStartDate();
            LocalDate endDate = Menu.getEndDate(startDate);
            double result = SaleTransactionList.calculateSalespersonRevenue(salesperson.getUserName(), startDate, endDate);
            System.out.println("Total Revenue of Sales by " + salespersonId + ": " + result);
        } else {
            System.out.println("Salesperson not found. Please try again.");
        }
    }

    private void ManagerProcessTotalRevenue(Scanner s) {
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        double totalSalesRevenue = SaleTransactionList.calculateRevenueAndCount(startDate,endDate)[0];
        double totalServiceRevenue = ServiceList.calculateServiceRevenueAndCount(startDate,endDate)[0];
        int totalSalesTransactions = (int) SaleTransactionList.calculateRevenueAndCount(startDate,endDate)[1];
        int totalServiceTransactions = (int) ServiceList.calculateServiceRevenueAndCount(startDate,endDate)[1];

        double totalRevenue = totalSalesRevenue + totalServiceRevenue;
        int totalTransactions = totalSalesTransactions + totalServiceTransactions;

        Map<String, Double> employeeRevenue = calculateEmployeeRevenue(startDate, endDate);
        Map<String, Double> clientRevenue = calculateClientRevenue(startDate, endDate);

        System.out.printf("Combined Statistics from %s to %s:\n", startDate, endDate);
        System.out.printf("Total Combined Revenue: $%.2f\n", totalRevenue);
        System.out.printf("Total Combined Transactions: %d\n", totalTransactions);

        System.out.println("Revenue by Employee:");
        employeeRevenue.forEach((employeeId, revenue) -> System.out.printf("Employee ID: %s, Revenue: $%.2f\n", employeeId, revenue));

        System.out.println("Revenue by Client:");
        clientRevenue.forEach((clientId, revenue) -> System.out.printf("Client ID: %s, Revenue: $%.2f\n", clientId, revenue));
    }

    private static Map<String, Double> calculateEmployeeRevenue(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> employeeRevenue = new HashMap<>();

        for (SaleTransaction transaction : SaleTransactionList.getSaleTransactionsBetween(startDate, endDate)) {
            String salespersonId = transaction.getSalespersonId(); // Assuming you have this method
            employeeRevenue.put(salespersonId,
                    employeeRevenue.getOrDefault(salespersonId, 0.0) + transaction.getTotalAmount());
        }

        for (Service service : ServiceList.getServicesBetween(startDate, endDate)) {
            String mechanicId = service.getMechanicId();
            employeeRevenue.put(mechanicId,
                    employeeRevenue.getOrDefault(mechanicId, 0.0) + service.getTotalCost());
        }
        return employeeRevenue;
    }

    private static Map<String, Double> calculateClientRevenue(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> clientRevenue = new HashMap<>();

        for (SaleTransaction transaction : SaleTransactionList.getSaleTransactionsBetween(startDate, endDate)) {
            String clientId = transaction.getClientId();
            clientRevenue.put(clientId,
                    clientRevenue.getOrDefault(clientId, 0.0) + transaction.getTotalAmount());
        }

        // Service transactions
        for (Service service : ServiceList.getServicesBetween(startDate, endDate)) {
            String clientId = service.getClientId();
            clientRevenue.put(clientId,
                    clientRevenue.getOrDefault(clientId, 0.0) + service.getTotalCost());
        }

        return clientRevenue;
    }

    private static void viewAutoPartStatistics() {
        int totalPartsInStock = 0;
        int totalPartsSold = 0;
        Map<autoPart.Condition, Integer> partConditionStats = new HashMap<>();

        for (autoPart part: CarAndAutoPartMenu.getAutoPartsList()){
            if(part.getStatus() == Status.AVAILABLE && !part.isDeleted()){
                totalPartsInStock++;
                if(part.getCondition() == autoPart.Condition.NEW){
                    partConditionStats.put(autoPart.Condition.NEW, partConditionStats.getOrDefault(autoPart.Condition.NEW, 0) + 1);
                } else if(part.getCondition() == autoPart.Condition.USED){
                    partConditionStats.put(autoPart.Condition.USED, partConditionStats.getOrDefault(autoPart.Condition.USED, 0) + 1);
                } else {
                    partConditionStats.put(autoPart.Condition.REFURBISHED, partConditionStats.getOrDefault(autoPart.Condition.REFURBISHED, 0) + 1);
                }
            }
            if(part.getStatus() == Status.SOLD && !part.isDeleted()){
                totalPartsSold++;
            }
        }

        System.out.println("Auto Part Statistics:");
        System.out.printf("Total Parts In Stock: %d\n", totalPartsInStock);
        System.out.printf("Total Parts Sold: %d\n", totalPartsSold);

        System.out.println("Part Condition Statistics:");
        partConditionStats.forEach((condition, count) -> System.out.printf("%s: %d\n", condition, count));
    }

    public static void viewCarStatistics(LocalDate startDate, LocalDate endDate) {
        double totalCarRevenue = calculateTotalCarSellRevenueAndCount(startDate, endDate)[0];
        int totalCarsSold = (int) calculateTotalCarSellRevenueAndCount(startDate, endDate)[1];
        double averageCarPrice = totalCarsSold > 0 ? totalCarRevenue / totalCarsSold : 0;
        // Thống kê doanh thu từ các dòng xe khác nhau, giúp xác định dòng xe nào bán chạy nhất.
        Map<String, Double> revenueByCarModel = new HashMap<>();
        Map<String, Integer> carsSoldByModel = new HashMap<>();
        // Calculate total number of cars in repair (work-in) and number of services performed
        Map<String, Integer> carsInRepair = new HashMap<>();

        for (SaleTransaction transaction : SaleTransactionList.getSaleTransactionsBetween(startDate, endDate)) {
            for (Car car : transaction.getPurchasedCars()){
                String carType = car.getCarModel();
                revenueByCarModel.put(carType, revenueByCarModel.getOrDefault(carType, 0.0) + transaction.getTotalAmount());
                carsSoldByModel.put(carType, carsSoldByModel.getOrDefault(carType, 0) + 1);
            }
        }


        for (Service service : ServiceList.getServicesBetween(startDate, endDate)) {
            String carId = service.getCarId();
            carsInRepair.put(carId, carsInRepair.getOrDefault(carId, 0) + 1);
        }

        String topSellingCarModel = null;
        int maxSoldCount = 0;
        for (Map.Entry<String, Integer> entry : carsSoldByModel.entrySet()) {
            if (entry.getValue() > maxSoldCount) {
                maxSoldCount = entry.getValue();
                topSellingCarModel = entry.getKey();
            }
        }

        String highestRevenueCarModel = null;
        double maxRevenue = 0.0;
        for (Map.Entry<String, Double> entry : revenueByCarModel.entrySet()) {
            if (entry.getValue() > maxRevenue) {
                maxRevenue = entry.getValue();
                highestRevenueCarModel = entry.getKey();
            }
        }

        System.out.printf("Car Sales Statistics from %s to %s:\n", startDate, endDate);
        System.out.printf("Total Car Revenue: $%.2f\n", totalCarRevenue);
        System.out.printf("Total Cars Sold: %d\n", totalCarsSold);
        System.out.printf("Average Car Price: $%.2f\n", averageCarPrice);

        System.out.println("Number of car sales by Car Model:");
        for (Map.Entry<String, Integer> entry : carsSoldByModel.entrySet()) {
            System.out.printf("Car Model: %s, Sales: %d\n", entry.getKey(), entry.getValue());
        }

        System.out.println("Revenue by Car Model:");
        for (Map.Entry<String, Double> entry : revenueByCarModel.entrySet()) {
            System.out.printf("Car Model: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
        }
        System.out.println("Total Cars in Repair:" + carsInRepair.size());

        System.out.println("Cars in Repair:");
        for (Map.Entry<String, Integer> entry : carsInRepair.entrySet()) {
            System.out.printf("Car ID: %s, Services Count: %d\n", entry.getKey(), entry.getValue());
        }
    }

    private static double[] calculateTotalCarSellRevenueAndCount(LocalDate startDate, LocalDate endDate) {
        double totalRevenue = 0.0;
        int totalCarsSold = 0;
        for (SaleTransaction transaction : SaleTransactionList.getSaleTransactionsBetween(startDate, endDate)) {
            for (Car car : transaction.getPurchasedCars()){
                totalRevenue += car.getPrice();
                totalCarsSold++;
            }
        }
        return new double[]{totalRevenue, totalCarsSold};
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
        for (User user : UserMenu.getUserList()) {
            if (user instanceof Salesperson salesperson) {
                salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
            }
        }
    }

    private void getAllServicesInSpecificPeriod(Scanner scanner) {
        // TODO: Implement this method (current implementation quick and dirty)
        for (User user : UserMenu.getUserList()) {
            if (user instanceof Mechanic mechanic) {
                mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
            }
        }
    }

    private void getAllMechanicServices(User loggedInUser) {
        if (loggedInUser instanceof Mechanic mechanic) {
            mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
        } else if (loggedInUser instanceof Manager) {
            UserMenu.displayAllMechanics();
            String mechanicId;
            Mechanic mechanic;
            Scanner input = new Scanner(System.in);
            while (true) {
                System.out.print("Enter mechanic ID: ");
                mechanicId = input.nextLine();
                if (!mechanicId.isEmpty()) {
                    mechanic = (Mechanic) UserMenu.getUserById(mechanicId);
                    if (mechanic != null) {
                        break;
                    } else {
                        System.out.println("Mechanic not found. Please try again.");
                    }
                    System.out.println("Mechanic ID cannot be empty. Please try again.");
                }
            }
            mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
        }
    }

    private void getAllSalespersonSales(User loggedInUser) {
        if (loggedInUser instanceof Salesperson salesperson) {
            salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
        } else if (loggedInUser instanceof Manager) {
            UserMenu.displayAllSalespersons();
            String salespersonId;
            Salesperson salesperson;
            while (true) {
                System.out.print("Enter salesperson ID: ");
                salespersonId = input.nextLine();
                if (!salespersonId.isEmpty()) {
                    salesperson = (Salesperson) UserMenu.getUserById(salespersonId);
                    if (salesperson != null) {
                        break;
                    } else {
                        System.out.println("Salesperson not found. Please try again.");
                    }
                    System.out.println("Salesperson ID cannot be empty. Please try again.");
                }
            }
            salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
        }
    }


    // Salesperson functions
    private void getAllTransactionsByMeInSpecificPeriod(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        salesperson.saleTransactionMadeByMe(startDate, endDate);
    }

    private void SalespersonRevenue(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        double result = SaleTransactionList.calculateSalespersonRevenue(salesperson.getUserName(),LocalDate.of(1970, 1, 1), LocalDate.now());
        System.out.println("Total Revenue of Sales by " + salesperson.getName() + ": " + result);
    }

    private void SalespersonRevenueInSpecificPeriod(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        double result = SaleTransactionList.calculateSalespersonRevenue(salesperson.getUserName(), startDate, endDate);
        System.out.println("Total Revenue of Sales by " + salesperson.getName() + ": " + result);
    }

    private void getAllCarsSoldBySalesperson(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        salesperson.viewCarsSoldByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
    }

    private void getAllCarsSoldBySalespersonInSpecificPeriod(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        salesperson.viewCarsSoldByMe(startDate, endDate);
    }

    // Mechanic functions
    private void getAllMechanicServicesInSpecificPeriod(User loggedInUser) {
        Mechanic mechanic = (Mechanic) loggedInUser;
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        mechanic.servicesMadeByMe(startDate, endDate);
    }

    private void getRevenueOfServicesInSpecificPeriod(User loggedInUser) {
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        Mechanic mechanic = (Mechanic) loggedInUser;
        double result = ServiceList.calculateMechanicRevenue(mechanic.getName(),startDate, endDate);
        System.out.println("Total Revenue of Services by " + mechanic.getName() + ": " + result);
    }

    private void getRevenueOfServices(User loggedInUser) {
        Mechanic mechanic = (Mechanic) loggedInUser;
        double result = ServiceList.calculateMechanicRevenue(mechanic.getName(),LocalDate.of(1970, 1, 1), LocalDate.now());
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
    private void getAllClientTransactionsInSpecificPeriod(User loggedInUser) {
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        Client client = (Client) loggedInUser;
        client.viewTransactionsHistory(startDate, endDate);
    }

    private void getAllClientSalesTransactions(User loggedInUser) {
        Client client = (Client) loggedInUser;
        client.viewTransactionsHistory(LocalDate.of(1970, 1, 1), LocalDate.now());
    }

    private void getAllClientServicesInSpecificPeriod(User loggedInUser) {
        Client client = (Client) loggedInUser;
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        client.viewServiceHistoryInSpecificPeriod(startDate, endDate);
    }

    private void getAllClientServices(User loggedInUser) {
        Client client = (Client) loggedInUser;
        client.viewServiceHistoryInSpecificPeriod(LocalDate.of(1970, 1, 1), LocalDate.now());
    }

    private void exit(Scanner s) {
        System.out.println("Exiting...");
    }


}
