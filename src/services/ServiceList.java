package services;

import autoPart.autoPart;
import data.Database;
import data.service.ServiceDatabase;
import data.user.UserDatabase;
import user.Client;
import user.User;
import utils.UserMenu;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceList {
    public static List<Service> services;
    // This code run one time when create an instance of a class
    static {
        try {
            if(!Database.isDatabaseExist(ServiceDatabase.path)){
                ServiceDatabase.createDatabase();
            };
            services = ServiceDatabase.loadService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    public static void addService(String mechanicId) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction date (YYYY-MM-DD): ");
        LocalDate transactionDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter client name: ");
        String clientId = scanner.nextLine();

        System.out.println("Select a service category:");
        Service.Category[] categories = Service.Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int categoryIndex = scanner.nextInt() - 1;
        Service.Category selectedCategory = categories[categoryIndex];

        System.out.println("Select a service type:");
        List<Service.serviceType> serviceTypes = Service.serviceType.getByCategory(selectedCategory);
        for (int i = 0; i < serviceTypes.size(); i++) {
            System.out.println((i + 1) + ". " + serviceTypes.get(i));
        }
        int serviceTypeIndex = scanner.nextInt() - 1;
        Service.serviceType selectedServiceType = serviceTypes.get(serviceTypeIndex);

        double serviceCost = selectedServiceType.getPrice();

        scanner.nextLine();

        System.out.println("Enter ID of replaced parts (separate by comma, or leave empty if none): ");
        String partNamesInput = scanner.nextLine();
        List<String> partNames = partNamesInput.isEmpty() ? Collections.emptyList() :
                Arrays.stream(partNamesInput.split(","))
                        .map(String::trim)
                        .map(partName -> partName.replaceAll(" +", " "))
                        .collect(Collectors.toList());

        System.out.print("Type 1 if the service was made by AUTO136. Type 2 if the service was made by OTHER: ");
        int serviceByInput = Integer.parseInt(scanner.nextLine());
        ServiceBy serviceBy = (serviceByInput == 1) ? ServiceBy.AUTO136 : ServiceBy.OTHER;

        System.out.print("Enter car ID: ");
        String carId = scanner.nextLine();

        Service service = new Service(transactionDate, clientId, mechanicId, selectedServiceType, partNames, serviceBy, carId, serviceCost);
        Service.addService(service);

        User user = UserMenu.getUserList().stream()
                .filter(u -> u.getUserID().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;
            client.updateTotalSpending(service.getTotalCost());
            UserDatabase.saveUsersData(UserMenu.getUserList());
        }
    }


    public static Service getServiceById(String serviceId) {
        for (Service service : services) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    public List<Service> getAllServices() {
        return services;
    }

    // TODO: display tất cả các service được thực hiện hay tất cả accs sẻvice hiện có (service type)?
    public static void displayAllServices(){
        for (Service service : services) {
            if(!service.isDeleted()) {
                System.out.println(service.getFormattedServiceDetails());
                System.out.println("___________________________________");
            }
        }
    }

//    public void updateService(Service updatedService) {
//        for (int i = 0; i < services.size(); i++) {
//            if (services.get(i).getServiceId().equals(updatedService.getServiceId())) {
//                services.set(i, updatedService);
//                break;
//            }
//        }
//    }
//
//    public void deleteService(String serviceId) {
//        services.removeIf(service -> service.getServiceId().equals(serviceId));
//    }

    public static List<Service> getServicesBetween(LocalDate startDate, LocalDate endDate) {
        List<Service> filteredServices = new ArrayList<>();
        for (Service service : services) {
            LocalDate serviceDate = service.getServiceDate();
            if ((serviceDate.isEqual(startDate) || serviceDate.isAfter(startDate)) &&
                    (serviceDate.isEqual(endDate) || serviceDate.isBefore(endDate.plusDays(1)))) {
                filteredServices.add(service);
            }
        }
        return filteredServices;
    }

    public static void updateService() throws Exception {
        displayAllServices();
        Service.updateService();
    }


    public static void deleteService() throws Exception {
        displayAllServices();
        Service.deleteService();
    }

    public static double calculateTotalServiceRevenue() {
        double total = 0.0;
        for (Service service : services) {
            total += service.getTotalCost();
        }
        return total;
    }


    public static int calculateAutoPartUsed(LocalDate startDate, LocalDate endDate) {
        List<Service> servicesInRange = getServicesBetween(startDate, endDate);
        int autoPartCount = 0;

        for (Service service : servicesInRange) {
            autoPartCount += service.getReplacedParts().size();
        }

        return autoPartCount;
    }

    public List<autoPart> listAutoPartUsedInDateRange(LocalDate startDate, LocalDate endDate) {
        List<autoPart> usedParts = new ArrayList<>();

        for (Service service : getServicesBetween(startDate, endDate)) {
            if (!service.isDeleted()) {
                usedParts.addAll(service.getReplacedParts());
            }
        }

        return usedParts;
    }

    public static double[] calculateServiceRevenueAndCount(LocalDate startDate, LocalDate endDate) {
        double totalServiceRevenue = 0.0;
        int serviceCount = 0;

        for (Service service : getServicesBetween(startDate, endDate)) {
            totalServiceRevenue += service.getTotalCost();
            serviceCount++;
        }

        return new double[]{totalServiceRevenue, serviceCount};
    }

    public static double calculateMechanicRevenue(String mechanicId, LocalDate startDate, LocalDate endDate) {

        double totalServiceRevenue = 0.0;

        List<Service> servicesInRange = getServicesBetween(startDate, endDate);
        List<Service> filteredServices = servicesInRange.stream()
                .filter(service -> service.getMechanicId().equals(mechanicId))
                .toList();

        totalServiceRevenue = filteredServices.stream()
                .mapToDouble(Service::getTotalCost)
                .sum();

        return totalServiceRevenue;
    }

    public static void viewServiceStatistics(LocalDate startDate, LocalDate endDate) {
        double[] serviceRevenueAndCount = calculateServiceRevenueAndCount(startDate, endDate);
        double totalServiceRevenue = serviceRevenueAndCount[0];
        int serviceCount = (int) serviceRevenueAndCount[1];
        int autoPartUsed = calculateAutoPartUsed(startDate, endDate);

        Map<String, Double> clientRevenue = new HashMap<>();
        Map<String, Integer> serviceUsageCount = new HashMap<>();
        Map<String, Double> serviceRevenue = new HashMap<>();
        Map<String, Double> mechanicRevenue = new HashMap<>();

        for (Service service : getServicesBetween(startDate, endDate)) {

            clientRevenue.put(service.getClientId(), clientRevenue.getOrDefault(service.getClientId(), 0.0) + service.getTotalCost());

            Service.serviceType serviceType = service.getServiceType();
            serviceUsageCount.put(String.valueOf(serviceType), serviceUsageCount.getOrDefault(serviceType, 0) + 1);
            serviceRevenue.put(String.valueOf(serviceType), serviceRevenue.getOrDefault(serviceType, 0.0) + service.getTotalCost());


            String mechanicId = service.getMechanicId(); // Assuming you have this method
            mechanicRevenue.put(mechanicId,
                    mechanicRevenue.getOrDefault(mechanicId, 0.0) + service.getTotalCost());
        }

        // Find most used service
        String mostUsedService = serviceUsageCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Find highest revenue service
        String highestRevenueService = serviceRevenue.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        double highestRevenue = serviceRevenue.getOrDefault(highestRevenueService, 0.0);

        // Find the mechanic with the top revenue
        String topMechanicId = null;
        double maxRevenue = 0.0;

        for (Map.Entry<String, Double> entry : mechanicRevenue.entrySet()) {
            if (entry.getValue() > maxRevenue) {
                maxRevenue = entry.getValue();
                topMechanicId = entry.getKey();
            }
        }

        // Statistics info
        System.out.printf("Sales Statistics from %s to %s:\n", startDate, endDate);
        System.out.printf("Total Services Revenue: $%.2f\n", totalServiceRevenue);
        System.out.printf("Total Number of Services: %d\n", serviceCount);
        System.out.printf("Total Number of AutoPart used: %d\n", autoPartUsed);

        // Revenue by client
        System.out.println("Revenue by Client:");
        for (Map.Entry<String, Double> entry : clientRevenue.entrySet()) {
            System.out.printf("Client ID: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
        }

        // Top mechanic
        if (topMechanicId != null) {
            System.out.printf("Top Mechanic ID: %s, Revenue: $%.2f\n", topMechanicId, maxRevenue);
        } else {
            System.out.println("No service transactions in the given period.");
        }

        if (mostUsedService != null) {
            System.out.printf("Most Used Service: %s\n", mostUsedService);
        }

        if (highestRevenueService != null) {
            System.out.printf("Highest Revenue Service: %s, Revenue: $%.2f\n", highestRevenueService, highestRevenue);
        }
    }

    public void viewServiceByMechanic(String mechanicId, LocalDate startDate, LocalDate endDate) {
        List<Service> servicesInRange = getServicesBetween(startDate, endDate);

        List<Service> filteredService = servicesInRange.stream()
                .filter(service -> service.getMechanicId().equals(mechanicId))
                .toList();

        double totalServiceRevenue = calculateMechanicRevenue(mechanicId, startDate, endDate);

        int serviceCount = filteredService.size();

        System.out.println("Sales Transactions for Salesperson ID: " + mechanicId);
        System.out.println("Total Sales Revenue: $" + String.format("%.2f", totalServiceRevenue));
        System.out.println("Total Number of Transactions: " + serviceCount);

        if (!filteredService.isEmpty()) {
            System.out.println("\nTransaction Details:");
            for (Service service : filteredService) {
                System.out.println(service.getFormattedServiceDetails());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}