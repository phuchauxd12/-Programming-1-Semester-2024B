package utils.menu;

import autoPart.autoPart;
import car.Car;
import services.Service;
import services.ServiceBy;
import services.ServiceList;
import transaction.SaleTransaction;
import transaction.SaleTransactionList;
import user.Client;
import user.Manager;
import user.Mechanic;
import user.Salesperson;
import utils.CurrencyFormat;
import utils.DatePrompt;
import utils.Status;
import utils.UserSession;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            case MANAGER -> {
                menuItems.put(1, "Number of Cars sold");
                menuItems.put(2, "View Service Statistics");
                menuItems.put(3, "View Sales Statistics");
                menuItems.put(4, "View Mechanic Service Statistics");
                menuItems.put(5, "View Salesperson Revenue Statistics");
                menuItems.put(6, "Revenue of Shop");
                menuItems.put(7, "List All Cars Sold");
                menuItems.put(8, "View AutoPart Statistic");
                menuItems.put(9, "View Car Statistic");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::getNumberOfCarsSoldInSpecificPeriod);
                menuActions.put(2, this::processServiceRevenue);
                menuActions.put(3, this::processSalesStatistic);
                menuActions.put(4, this::processMechanicRevenue);
                menuActions.put(5, this::processSalespersonRevenue);
                menuActions.put(6, this::processTotalRevenue);
                menuActions.put(7, this::getAllCarsSoldInSpecificPeriod);
                menuActions.put(8, this::viewAutoPartStatistics);
                menuActions.put(9, this::viewCarStatistics);
                menuActions.put(0, this::exit);
            }

            case SALESPERSON -> {
                menuItems.put(1, "List all items sold by me");
                menuItems.put(2, "Revenue of Sales by me");
                menuItems.put(0, "Exit");

                // Add salesperson-specific actions here
                menuActions.put(1, this::getAllItemsSoldBySalesperson);
                menuActions.put(2, this::salespersonRevenue);
                menuActions.put(0, this::exit);
            }

            case MECHANIC -> {
                menuItems.put(1, "My Auto Part Statistic");
                menuItems.put(2, "My Car Statistic");
                menuItems.put(3, "Revenue of Services by me");
                menuItems.put(0, "Exit");

                // Add mechanic-specific actions here
                menuActions.put(1, this::allPartUsedByMechanic);
                menuActions.put(2, this::allCarServicedByMechanic);
                menuActions.put(3, this::getRevenueOfServices);
                menuActions.put(0, this::exit);
            }

            case CLIENT -> {
                menuItems.put(1, "List All Services done for me");
                menuItems.put(2, "List all Sales Transactions for me");
                menuItems.put(0, "Exit");

                // Add client-specific actions here
                menuActions.put(1, this::getAllClientServices);
                menuActions.put(2, this::getAllClientSalesTransactions);
                menuActions.put(0, this::exit);
            }
        }
    }


    // Manager functions


    private void getNumberOfCarsSoldInSpecificPeriod() {
        int option = Menu.getFilteredOption();
        LocalDate startDate;
        LocalDate endDate;
        switch (option) {
            case 1:
                System.out.println("Fetching number of cars sold...");
                startDate = LocalDate.of(1970, 1, 1);
                endDate = LocalDate.now();
                CarAndAutoPartMenu.getNumberOfCarsSoldInSpecificPeriod(startDate, endDate);
                try {
                    ActivityLogMenu.addActivityLogForCurrentUser("Get number of all cars sold ");
                } catch (Exception e) {
                    System.out.println("Error logging statistic action history: " + e.getMessage());
                }
                break;
            case 2:
                System.out.println("Fetching number of cars sold in a specific period...");
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                CarAndAutoPartMenu.getNumberOfCarsSoldInSpecificPeriod(startDate, endDate);
                try {
                    ActivityLogMenu.addActivityLogForCurrentUser("Get number of cars sold from " + startDate + " to " + endDate);
                } catch (Exception e) {
                    System.out.println("Error logging statistic action history: " + e.getMessage());
                }
                break;
        }
    }

    private void processServiceRevenue() {
        String activityName = "";
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
        ServiceList.viewServiceStatistics(startDate, endDate);
        activityName = "View service statistics ";

        try {
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void processMechanicRevenue() {
        String activityName = "";
        UserMenu.displayAllMechanics();
        String mechanicId = promptForUserId("mechanic");
        Mechanic mechanic = (Mechanic) UserMenu.getUserById(mechanicId);
        if (mechanic != null) {
            LocalDate startDate = LocalDate.of(1970, 1, 1);
            LocalDate endDate = LocalDate.now();
            int option = Menu.getFilteredOption();
            switch (option) {
                case 1:
                    break;
                case 2:
                    startDate = DatePrompt.getStartDate();
                    endDate = DatePrompt.getEndDate(startDate);
                    break;
            }
            ServiceList.viewServiceByMechanic(mechanicId, startDate, endDate);
            activityName = "View revenue made by mechanic named " + mechanic.getName() + " with ID " + mechanic.getUserID();
        } else {
            System.out.println("Mechanic not found. Please try again.");
        }

        try {
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void processSalesStatistic() {
        String activityName = "";
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
        SaleTransactionList.viewSalesStatistics(startDate, endDate);
        activityName = "View sales statistics";

        try {
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void processSalespersonRevenue() {
        String activityName = "";
        UserMenu.displayAllSalespersons();
        String salespersonId = promptForUserId("salesperson");
        Salesperson salesperson = (Salesperson) UserMenu.getUserById(salespersonId);
        if (salesperson != null) {
            LocalDate startDate = LocalDate.of(1970, 1, 1);
            LocalDate endDate = LocalDate.now();
            int option = Menu.getFilteredOption();
            switch (option) {
                case 1:
                    break;
                case 2:
                    startDate = DatePrompt.getStartDate();
                    endDate = DatePrompt.getEndDate(startDate);
                    break;
            }
            SaleTransactionList.viewTransactionsBySalesperson(salespersonId, startDate, endDate);
            activityName = "View total revenue of sales by salesperson named " + salesperson.getName() + " with ID " + salesperson.getUserID();

        } else {
            activityName = "View total revenue of sales by a salesperson which is not found! ";
            System.out.println("Salesperson not found. Please try again.");

        }

        try {
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void processTotalRevenue() {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
        double totalSalesRevenue = SaleTransactionList.calculateRevenueAndCount(startDate, endDate)[0];
        double totalServiceRevenue = ServiceList.calculateServiceRevenueAndCount(startDate, endDate)[0];
        int totalSalesTransactions = (int) SaleTransactionList.calculateRevenueAndCount(startDate, endDate)[1];
        int totalServiceTransactions = (int) ServiceList.calculateServiceRevenueAndCount(startDate, endDate)[2];

        double totalRevenue = totalSalesRevenue + totalServiceRevenue;
        int totalTransactions = totalSalesTransactions + totalServiceTransactions;

        Map<String, Double> employeeRevenue = calculateEmployeeRevenue(startDate, endDate);
        Map<String, Double> clientRevenue = calculateClientRevenue(startDate, endDate);

        System.out.printf("Combined Statistics from %s to %s:\n", startDate, endDate);
        System.out.printf("Total Combined Revenue: " + CurrencyFormat.format(totalRevenue));
        System.out.printf("\nTotal Combined Transactions (Service and Sale): %d\n", totalTransactions);

        System.out.println("\nRevenue by Employee:");
        employeeRevenue.forEach((employeeId, revenue) -> System.out.printf("Employee : %s, Revenue: %s\n", UserMenu.getUserById(employeeId).getName(), CurrencyFormat.format(revenue)));

        System.out.println("\nRevenue by Client:");
        clientRevenue.forEach((clientId, revenue) -> System.out.printf("Client: %s, Revenue: %s\n", UserMenu.getUserById(clientId).getName(), CurrencyFormat.format(revenue)));

        try {
            String activityName = "View total revenue by client and employee from " + startDate + " to " + endDate;
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
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
            if (ServiceBy.OTHER == service.getServiceBy()) {
                continue;
            }
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
        System.out.printf("Total Parts Sold/Used: %d\n", totalPartsSold);

        System.out.println("Part Condition Statistics:");
        partConditionStats.forEach((condition, count) -> System.out.printf("%s: %d\n", condition, count));

        try {
            String activityName = "View Auto Part Statistics";
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void viewCarStatistics() {
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
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
        System.out.printf("Total Car Revenue: %s\n", CurrencyFormat.format(totalCarRevenue));
        System.out.printf("Total Cars Sold: %d\n", totalCarsSold);
        System.out.printf("Average Car Price: %s\n", CurrencyFormat.format(averageCarPrice));

        System.out.println("\nTop Selling Car Model: " + topSellingCarModel);
        System.out.println("Sales: " + totalCarsSold);

        System.out.println("Number of car sales by Car Model:");
        for (Map.Entry<String, Integer> entry : carsSoldByModel.entrySet()) {
            System.out.printf("Car Model: %s, Sales: %d\n", entry.getKey(), entry.getValue());
        }


        System.out.println("\nCar Model with highest Revenue: " + highestRevenueCarModel);
        System.out.println("Max Revenue: " + CurrencyFormat.format(maxRevenue));


        System.out.println("Revenue by Car Model:");
        for (Map.Entry<String, Double> entry : revenueByCarModel.entrySet()) {
            System.out.printf("Car Model: %s, Revenue: %s\n", entry.getKey(), CurrencyFormat.format(entry.getValue()));
        }
        System.out.println("Total Cars in Repair:" + carsInRepair.size());

        System.out.println("Cars in Repair:");
        for (Map.Entry<String, Integer> entry : carsInRepair.entrySet()) {
            System.out.printf("Car ID: %s, Services Count: %d\n", entry.getKey(), entry.getValue());
        }

        try {
            String activityName = "View car statistic";
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
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
        // Add logic to fetch all cars sold
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                System.out.println("Fetching all cars sold...");
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                System.out.println("Fetching all cars sold in a specific period...");
                break;
        }
        CarAndAutoPartMenu.getAllCarsSoldInSpecificPeriod(startDate, endDate);

        try {
            String activityName = "View all cars sold from " + startDate + " to " + endDate;
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void salespersonRevenue() {
        Salesperson salesperson = (Salesperson) UserSession.getCurrentUser();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
        double result = SaleTransactionList.calculateSalespersonRevenue(salesperson.getUserID(), startDate, endDate);
        System.out.println("Total Sale Transaction Made:");
        System.out.println("\nTotal Revenue of Sales by " + salesperson.getName() + ": " + CurrencyFormat.format(result));

        try {
            String activityName = "View total revenue made by a salesperson named " + salesperson.getName() + " with ID: " + salesperson.getUserID();
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }


    private void getAllItemsSoldBySalesperson() {
        Salesperson salesperson = (Salesperson) UserSession.getCurrentUser();
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
            default:
                System.out.println("Invalid option selected.");
                return;
        }

        List<Car> carsSold = SaleTransactionList.listCarsSoldBySalePerson(salesperson.getUserID(), startDate, endDate);

        List<autoPart> autoPartsSold = SaleTransactionList.listAutoPartsSoldBySalesPerson(salesperson.getUserID(), startDate, endDate);

        // Count the total number of cars and auto parts sold
        int totalCarsSold = carsSold.size();
        int totalPartsSold = autoPartsSold.size();

        // Count the usage of each part
        Map<String, Integer> partUsageCount = new HashMap<>();
        for (autoPart part : autoPartsSold) {
            partUsageCount.put(part.getPartName(), partUsageCount.getOrDefault(part.getPartName(), 0) + 1);
        }

        // Sort and get top 3 most sold parts
        List<Map.Entry<String, Integer>> sortedParts = new ArrayList<>(partUsageCount.entrySet());
        sortedParts.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        List<Map.Entry<String, Integer>> topParts = sortedParts.stream().limit(3).collect(Collectors.toList());

        // Display sold cars
        System.out.println("Cars sold by " + salesperson.getUserName() + " (" + salesperson.getUserID() + "):");
        for (Car car : carsSold) {
            System.out.println(car);
        }
        System.out.println("Total cars sold: " + totalCarsSold);

        // Display sold auto parts
        System.out.println("\nAuto parts sold by " + salesperson.getUserName() + " (" + salesperson.getUserID() + "):");
        for (autoPart part : autoPartsSold) {
            System.out.println(part + " - Sold " + partUsageCount.get(part.getPartName()) + " times");
        }
        System.out.println("Total auto parts sold: " + totalPartsSold);

        // Display top 3 most sold parts
        System.out.println("\nTop 3 most sold parts:");
        for (Map.Entry<String, Integer> entry : topParts) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " times");
        }

        try {
            String activityName = "View all cars sold by a salesperson named " + salesperson.getUserName() + " with ID: " + salesperson.getUserID() + " from " + startDate + " to " + endDate;
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }


    // Mechanic functions

    private void allPartUsedByMechanic() {
        Mechanic mechanic = (Mechanic) currentUser;
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
        List<autoPart> autoPartUsage = ServiceList.listAutoPartUsedInMechanicService(mechanic.getUserID(), startDate, endDate);

        // Count the usage of each part and total usage
        Map<String, Integer> partUsageCount = new HashMap<>();
        int totalPartUsage = 0;
        for (autoPart part : autoPartUsage) {
            partUsageCount.put(part.getPartName(), partUsageCount.getOrDefault(part.getPartName(), 0) + 1);
            totalPartUsage++;
        }

        // Sort and get top 3 most used parts
        List<Map.Entry<String, Integer>> sortedParts = new ArrayList<>(partUsageCount.entrySet());
        sortedParts.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        List<Map.Entry<String, Integer>> topParts = sortedParts.stream().limit(3).collect(Collectors.toList());


        System.out.println("\nTop 3 most used parts:");
        for (Map.Entry<String, Integer> entry : topParts) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " times");
        }

        System.out.println("\nTotal number of parts used: " + totalPartUsage);

        System.out.println(mechanic.getName() + " (" + mechanic.getUserID() + ") used these auto parts in service:");
        for (autoPart part : autoPartUsage) {
            System.out.println(part + " - Used " + partUsageCount.get(part.getPartName()) + " times");
        }

        try {
            String activityName = "View all services made by a mechanic named " + mechanic.getUserName() + " with ID: " + mechanic.getUserID() + " from " + startDate + " to " + endDate;
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }

    }

    private void allCarServicedByMechanic() {
        Mechanic mechanic = (Mechanic) currentUser;
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
        List<Car> walkInCars = ServiceList.listCarDoneServiceByMechanic(mechanic.getUserID(), startDate, endDate);

        // Count the service occurrences for each car
        Map<String, Integer> carServiceCount = new HashMap<>();
        for (Car car : walkInCars) {
            carServiceCount.put(car.getCarID(), carServiceCount.getOrDefault(car.getCarID(), 0) + 1);
        }

        // Find the car with the most services
        String mostServicedCar = Collections.max(carServiceCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        System.out.println(mechanic.getName() + " (" + mechanic.getUserID() + ") serviced these cars:");
        for (Car car : walkInCars) {
            System.out.println(car + " - Serviced " + carServiceCount.get(car.getCarID()) + " times");
        }

        System.out.println("\nThe car with the most services by this mechanic is: " + mostServicedCar + " with " + carServiceCount.get(mostServicedCar) + " services.");

        try {
            String activityName = "View Revenue of services made by a mechanic named " + mechanic.getName() + " with ID: " + mechanic.getUserID() + " from " + startDate + " to " + endDate;
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getRevenueOfServices() {
        Mechanic mechanic = (Mechanic) currentUser;
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.now();
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                break;
            case 2:
                startDate = DatePrompt.getStartDate();
                endDate = DatePrompt.getEndDate(startDate);
                break;
        }
        double result = ServiceList.calculateMechanicRevenue(mechanic.getUserID(), startDate, endDate);
        System.out.println("Total Revenue of Services by " + mechanic.getName() + ": " + CurrencyFormat.format(result));

        try {
            String activityName = "View Revenue of services made by a mechanic named " + mechanic.getName() + " with ID: " + mechanic.getUserID();
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    // General Function

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


    private void getAllClientSalesTransactions() {
        Client client = (Client) currentUser;
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                SaleTransactionList.transactions.stream().filter(transaction -> (transaction.getClientId().equals(client.getUserID()) && !transaction.isDeleted()))
                        .forEach(transaction -> System.out.println(transaction.getFormattedSaleTransactionDetails()));
                try {
                    String activityName = "View all transactions of client named " + client.getName() + " with ID: " + client.getUserID();
                    ActivityLogMenu.addActivityLogForCurrentUser(activityName);
                } catch (Exception e) {
                    System.out.println("Error logging statistic action history: " + e.getMessage());
                }
                break;
            case 2:
                LocalDate startDate = DatePrompt.getStartDate();
                LocalDate endDate = DatePrompt.getEndDate(startDate);
                client.viewTransactionsHistory(startDate, endDate);

                try {
                    String activityName = "View all transactions of client named " + client.getName() + " with ID: " + client.getUserID() + " from " + startDate + " to " + endDate;
                    ActivityLogMenu.addActivityLogForCurrentUser(activityName);
                } catch (Exception e) {
                    System.out.println("Error logging statistic action history: " + e.getMessage());
                }
                break;

            default:
                System.out.println("Invalid option!");
                break;
        }
    }


    private void getAllClientServices() {
        Client client = (Client) currentUser;
        int option = Menu.getFilteredOption();
        switch (option) {
            case 1:
                ServiceList.services.stream().filter(service -> (service.getClientId().equals(client.getUserID()) && !service.isDeleted()))
                        .forEach(service -> System.out.println(service.getFormattedServiceDetails()));
                try {
                    String activityName = "View all services of client named " + client.getName() + " with ID: " + client.getUserID();
                    ActivityLogMenu.addActivityLogForCurrentUser(activityName);
                } catch (Exception e) {
                    System.out.println("Error logging statistic action history: " + e.getMessage());
                }
                break;
            case 2:
                LocalDate startDate = DatePrompt.getStartDate();
                LocalDate endDate = DatePrompt.getEndDate(startDate);
                client.viewServiceHistoryInSpecificPeriod(startDate, endDate);

                try {
                    String activityName = "View all services of client named " + client.getName() + " with ID: " + client.getUserID() + " from " + startDate + " to " + endDate;
                    ;
                    ActivityLogMenu.addActivityLogForCurrentUser(activityName);
                } catch (Exception e) {
                    System.out.println("Error logging statistic action history: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Invalid option!");
                break;
        }

    }

}
