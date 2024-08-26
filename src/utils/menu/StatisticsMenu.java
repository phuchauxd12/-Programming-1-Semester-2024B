package utils.menu;

import autoPart.autoPart;
import car.Car;
import services.Service;
import services.ServiceList;
import transaction.SaleTransaction;
import transaction.SaleTransactionList;
import user.*;
import utils.CommonFunc;
import utils.DatePrompt;
import utils.Status;
import utils.UserSession;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StatisticsMenu extends Menu {


    public StatisticsMenu() {
        super();
        switch (currentUser) {
            case Manager _ -> initializeMenu(MenuOption.MANAGER);
            case Salesperson _ -> initializeMenu(MenuOption.SALESPERSON);
            case Mechanic _ -> initializeMenu(MenuOption.MECHANIC);
            case Client _ -> initializeMenu(MenuOption.CLIENT);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }


    protected void initializeMenu(MenuOption menuOption) {
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
                menuItems.put(0, "Exit");

                menuActions.put(1, this::getNumberOfCarsSoldInSpecificPeriod);
                menuActions.put(2, this::ManagerProcessMechanicRevenue);
                menuActions.put(3, this::ManagerProcessSalespersonRevenue);
                menuActions.put(4, this::ManagerProcessTotalRevenue);
                menuActions.put(5, this::getAllCarsSoldInSpecificPeriod);
                menuActions.put(6, this::getAllTransactionsInSpecificPeriod);
                menuActions.put(7, this::getAllServicesInSpecificPeriod);
                menuActions.put(8, this::getAllSalespersonSales);
                menuActions.put(9, this::getAllMechanicServices);
                menuActions.put(10, this::viewAutoPartStatistics);
                menuActions.put(0, this::exit);
                break;

            case SALESPERSON:
                menuItems.put(1, "List All Transactions by me");
                menuItems.put(2, "List Transactions by me (Day/week/month)");
                menuItems.put(3, "List All Cars sold by me");
                menuItems.put(4, "List Cars sold by me (Day/week/month)");
                menuItems.put(5, "Revenue of Sales by me");
                menuItems.put(6, "Revenue of Sales by me (specific period)");
                menuItems.put(0, "Exit");

                // Add salesperson-specific actions here
                menuActions.put(1, this::getAllSalespersonSales);
                menuActions.put(2, () -> getAllTransactionsByMeInSpecificPeriod(currentUser));
                menuActions.put(3, () -> getAllCarsSoldBySalesperson(currentUser));
                menuActions.put(4, () -> getAllCarsSoldBySalespersonInSpecificPeriod(currentUser));
                menuActions.put(5, () -> SalespersonRevenue(currentUser));
                menuActions.put(6, () -> SalespersonRevenueInSpecificPeriod(currentUser));
                menuActions.put(0, this::exit);
                break;

            case MECHANIC:
                menuItems.put(1, "List All Services done by me");
                menuItems.put(2, "List Services done by me (Day/week/month)");
                menuItems.put(3, "Revenue of Services by me");
                menuItems.put(4, "Revenue of Services by me (Day/week/month)");
                menuItems.put(0, "Exit");

                // Add mechanic-specific actions here
                menuActions.put(1, this::getAllMechanicServices);
                menuActions.put(2, () -> getAllMechanicServicesInSpecificPeriod(currentUser));
                menuActions.put(3, () -> getRevenueOfServices(currentUser));
                menuActions.put(4, () -> getRevenueOfServicesInSpecificPeriod(currentUser));
                menuActions.put(0, this::exit);
                break;

            case CLIENT:
                menuItems.put(1, "List All Services done for me");
                menuItems.put(2, "List All Services done for me (day/week/month)");
                menuItems.put(3, "List all Sales Transactions for me");
                menuItems.put(4, "List All Transactions done for me (day/week/month)");
                menuItems.put(0, "Exit");

                // Add client-specific actions here
                menuActions.put(1, () -> getAllClientServices(currentUser));
                menuActions.put(2, () -> getAllClientServicesInSpecificPeriod(currentUser));
                menuActions.put(3, () -> getAllClientSalesTransactions(currentUser));
                menuActions.put(4, () -> getAllClientTransactionsInSpecificPeriod(currentUser));
                menuActions.put(0, this::exit);
                break;

        }
    }


    // Manager functions
    private void getNumberOfCarsSoldInSpecificPeriod() {
        System.out.println("Fetching number of cars sold in a specific period...");
        // Add logic to fetch the number of cars sold
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        CarAndAutoPartMenu.getNumberOfCarsSoldInSpecificPeriod(startDate, endDate);

        try{
            CommonFunc.addActivityLogForCurrentUser("Get number of cars sold from " + startDate + " to " + endDate);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void ManagerProcessMechanicRevenue() {
        UserMenu.displayAllMechanics();
        String mechanicId = promptForUserId("mechanic");
        Mechanic mechanic = (Mechanic) UserMenu.getUserById(mechanicId);
        if (mechanic != null) {
            LocalDate startDate = DatePrompt.getStartDate();
            LocalDate endDate = DatePrompt.getEndDate(startDate);
            double result = ServiceList.calculateMechanicRevenue(mechanic.getName(), startDate, endDate);
            System.out.println("Total Revenue of Services by " + mechanicId + ": " + result);
        } else {
            System.out.println("Mechanic not found. Please try again.");
        }

        try{
            String activityName = "View revenue made by mechanic named " + mechanic.getName() + " with ID " + mechanic.getUserID();
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void ManagerProcessSalespersonRevenue() {
        String activityName = "";
        // TODO: chỉnh lại function để lưu ID ng dùng thay vì tên (test hiện tại dùng tên cho dễ test)
        UserMenu.displayAllSalespersons();
        String salespersonId = promptForUserId("salesperson");
        Salesperson salesperson = (Salesperson) UserMenu.getUserById(salespersonId);
        if (salesperson != null) {
            LocalDate startDate = DatePrompt.getStartDate();
            LocalDate endDate = DatePrompt.getEndDate(startDate);
            double result = SaleTransactionList.calculateSalespersonRevenue(salesperson.getUserName(), startDate, endDate);
            System.out.println("Total Revenue of Sales by " + salespersonId + ": " + result);
            activityName = "View total revenue of sales made by " + salesperson.getName() + " with ID " + salesperson.getUserID();

        } else {
            activityName = "View total revenue of sales by a salesperson which is not found! ";
            System.out.println("Salesperson not found. Please try again.");

        }

        try{
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void ManagerProcessTotalRevenue() {
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        double totalSalesRevenue = SaleTransactionList.calculateRevenueAndCount(startDate, endDate)[0];
        double totalServiceRevenue = ServiceList.calculateServiceRevenueAndCount(startDate, endDate)[0];
        int totalSalesTransactions = (int) SaleTransactionList.calculateRevenueAndCount(startDate, endDate)[1];
        int totalServiceTransactions = (int) ServiceList.calculateServiceRevenueAndCount(startDate, endDate)[1];

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

        try{
            String activityName = "View total revenue by client and employee from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
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

    private void viewAutoPartStatistics() {
        int totalPartsInStock = 0;
        int totalPartsSold = 0;
        Map<autoPart.Condition, Integer> partConditionStats = new HashMap<>();

        for (autoPart part : CarAndAutoPartMenu.getAutoPartsList()) {
            if (part.getStatus() == Status.AVAILABLE && !part.isDeleted()) {
                totalPartsInStock++;
                if (part.getCondition() == autoPart.Condition.NEW) {
                    partConditionStats.put(autoPart.Condition.NEW, partConditionStats.getOrDefault(autoPart.Condition.NEW, 0) + 1);
                } else if (part.getCondition() == autoPart.Condition.USED) {
                    partConditionStats.put(autoPart.Condition.USED, partConditionStats.getOrDefault(autoPart.Condition.USED, 0) + 1);
                } else {
                    partConditionStats.put(autoPart.Condition.REFURBISHED, partConditionStats.getOrDefault(autoPart.Condition.REFURBISHED, 0) + 1);
                }
            }
            if (part.getStatus() == Status.SOLD && !part.isDeleted()) {
                totalPartsSold++;
            }
        }

        System.out.println("Auto Part Statistics:");
        System.out.printf("Total Parts In Stock: %d\n", totalPartsInStock);
        System.out.printf("Total Parts Sold: %d\n", totalPartsSold);

        System.out.println("Part Condition Statistics:");
        partConditionStats.forEach((condition, count) -> System.out.printf("%s: %d\n", condition, count));

        try{
            String activityName = "View Auto Part Statistics";
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
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
            for (Car car : transaction.getPurchasedCars()) {
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

        try{
            String activityName = "View car statistic";
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private static double[] calculateTotalCarSellRevenueAndCount(LocalDate startDate, LocalDate endDate) {
        double totalRevenue = 0.0;
        int totalCarsSold = 0;
        for (SaleTransaction transaction : SaleTransactionList.getSaleTransactionsBetween(startDate, endDate)) {
            for (Car car : transaction.getPurchasedCars()) {
                totalRevenue += car.getPrice();
                totalCarsSold++;
            }
        }
        return new double[]{totalRevenue, totalCarsSold};
    }

    private void getAllCarsSoldInSpecificPeriod() {
        System.out.println("Fetching all cars sold in a specific period...");
        // Add logic to fetch all cars sold
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        CarAndAutoPartMenu.getAllCarsSoldInSpecificPeriod(startDate, endDate);

        try{
            String activityName = "View all cars sold from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllTransactionsInSpecificPeriod() {
        // TODO: Implement this method (current implementation quick and dirty)
        for (User user : UserMenu.getUserList()) {
            if (user instanceof Salesperson salesperson) {
                salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
            }
        }

        try{
            String activityName = "View all transactions in a specific period";
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllServicesInSpecificPeriod() {
        // TODO: Implement this method (current implementation quick and dirty)
        for (User user : UserMenu.getUserList()) {
            if (user instanceof Mechanic mechanic) {
                mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
            }
        }

        try{
            String activityName = "View all services in a specific period";
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllMechanicServices() {
        String activityName = "";
        User loggedInUser = UserSession.getCurrentUser();

        if (loggedInUser instanceof Mechanic mechanic) {
            mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
            activityName = "View all services made by me as a mechanic";
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
            activityName = "View all services made by Mechanic named " + mechanic.getUserName() + " with ID " + mechanic.getUserID();
            mechanic.servicesMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
        }

        try{
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllSalespersonSales() {
        String activityName = "";

        User loggedInUser = UserSession.getCurrentUser();
        if (loggedInUser instanceof Salesperson salesperson) {
            activityName = "View all sales made by me as a salesperson";
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
            activityName = "View all sales made Salesperson named " + salesperson.getUserName() + " with ID " + salesperson.getUserID();
            salesperson.saleTransactionMadeByMe(LocalDate.of(1970, 1, 1), LocalDate.now());
        }

        try{
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }


    // Salesperson functions
    private void getAllTransactionsByMeInSpecificPeriod(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        salesperson.saleTransactionMadeByMe(startDate, endDate);

        try{
            String activityName = "View all transaction made by me from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }

    }

    private void SalespersonRevenue(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        double result = SaleTransactionList.calculateSalespersonRevenue(salesperson.getUserName(), LocalDate.of(1970, 1, 1), LocalDate.now());
        System.out.println("Total Revenue of Sales by " + salesperson.getName() + ": " + result);

        try{
            String activityName = "View total revenue made by a salesperson named " + salesperson.getName() + " with ID: " + salesperson.getUserID();
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void SalespersonRevenueInSpecificPeriod(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        double result = SaleTransactionList.calculateSalespersonRevenue(salesperson.getUserName(), startDate, endDate);
        System.out.println("Total Revenue of Sales by " + salesperson.getName() + ": " + result);


        try{
            String activityName = "View total revenue of a salesperson named " + salesperson.getName() + " with ID: " + salesperson.getUserID() + " from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllCarsSoldBySalesperson(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        salesperson.viewCarsSoldByMe(LocalDate.of(1970, 1, 1), LocalDate.now());

        try{
            String activityName = "View all cars sold by a salesperson named " + salesperson.getUserName() + " with ID: " + salesperson.getUserID();
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }

    }

    private void getAllCarsSoldBySalespersonInSpecificPeriod(User loggedInUser) {
        Salesperson salesperson = (Salesperson) loggedInUser;
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        salesperson.viewCarsSoldByMe(startDate, endDate);

        try{
            String activityName = "View all cars sold by a salesperson named " + salesperson.getUserName() + " with ID: " + salesperson.getUserID() + " from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    // Mechanic functions
    private void getAllMechanicServicesInSpecificPeriod(User loggedInUser) {
        Mechanic mechanic = (Mechanic) loggedInUser;
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        mechanic.servicesMadeByMe(startDate, endDate);

        try{
            String activityName = "View all services made by a mechanic named " + mechanic.getUserName() + " with ID: " + mechanic.getUserID() + " from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getRevenueOfServicesInSpecificPeriod(User loggedInUser) {
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        Mechanic mechanic = (Mechanic) loggedInUser;
        double result = ServiceList.calculateMechanicRevenue(mechanic.getName(), startDate, endDate);
        System.out.println("Total Revenue of Services by " + mechanic.getName() + ": " + result);

        try{
            String activityName = "View Revenue of services made by a mechanic named " + mechanic.getName() + " with ID: " + mechanic.getUserID() + " from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getRevenueOfServices(User loggedInUser) {
        Mechanic mechanic = (Mechanic) loggedInUser;
        double result = ServiceList.calculateMechanicRevenue(mechanic.getName(), LocalDate.of(1970, 1, 1), LocalDate.now());
        System.out.println("Total Revenue of Services by " + mechanic.getName() + ": " + result);

        try{
            String activityName = "View Revenue of services made by a mechanic named " + mechanic.getName() + " with ID: " + mechanic.getUserID();
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
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
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        Client client = (Client) loggedInUser;
        client.viewTransactionsHistory(startDate, endDate);


        try{
            String activityName = "View all transactions of client named " + client.getName() + " with ID: " + client.getUserID() + " from " + startDate + " to " + endDate;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllClientSalesTransactions(User loggedInUser) {
        Client client = (Client) loggedInUser;
        client.viewTransactionsHistory(LocalDate.of(1970, 1, 1), LocalDate.now());

        try{
            String activityName = "View all transactions of client named " + client.getName() + " with ID: " + client.getUserID();
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllClientServicesInSpecificPeriod(User loggedInUser) {
        Client client = (Client) loggedInUser;
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        client.viewServiceHistoryInSpecificPeriod(startDate, endDate);

        try{
            String activityName = "View all services of client named " + client.getName() + " with ID: " + client.getUserID() + " from " + startDate + " to " + endDate;;
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllClientServices(User loggedInUser) {
        Client client = (Client) loggedInUser;
        client.viewServiceHistoryInSpecificPeriod(LocalDate.of(1970, 1, 1), LocalDate.now());

        try{
            String activityName = "View all services of client named " + client.getName() + " with ID: " + client.getUserID();
            CommonFunc.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void exit(Scanner s) {
        System.out.println("Exiting...");
    }


}