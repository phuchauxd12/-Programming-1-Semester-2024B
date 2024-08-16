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




    public void addService(String mechanicId) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction date (YYYY-MM-DD): ");
        LocalDate transactionDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter client ID: ");
        String clientId = scanner.nextLine();

        System.out.print("Enter serviceType: ");
        String serviceType = scanner.nextLine();

        System.out.println("Enter name of replaced parts (separate by comma): ");
        String partNamesInput = scanner.nextLine();
        List<String> partNames = Arrays.stream(partNamesInput.split(","))
                                        .map(String::trim)
                                        .map(partName -> partName.replaceAll(" +", " "))  // Replace multiple spaces with a single space
                                        .collect(Collectors.toList());

        System.out.print("Type 1 if the service was made by AUTO136. Type 2 if the service was made by OTHER: ");
        int serviceByInput = Integer.parseInt(scanner.nextLine());
        ServiceBy serviceBy = (serviceByInput == 1) ? ServiceBy.AUTO136 : ServiceBy.OTHER;

        System.out.print("Enter car ID: ");
        String carId = scanner.nextLine();

        System.out.print("Enter service cost: ");
        double serviceCost = scanner.nextDouble();

        Service service = new Service(transactionDate, clientId, mechanicId, serviceType, partNames, serviceBy, carId, serviceCost);
        Service.addService(service);

        User user = UserMenu.getUserList().stream()
                .filter(u -> u.getUserID().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;
            client.updateTotalSpending(service.getTotalCost());
            UserDatabase.saveUsersData(UserMenu.getUserList());// Assuming Client class has this method
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

    public void displayAllServices(){
        for (Service service : services) {
            System.out.println(service);
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

    public List<Service> getServicesBetween(LocalDate startDate, LocalDate endDate) {
        List<Service> filteredServices = new ArrayList<>();
        for (Service service : services) {
            if ((service.getServiceDate().isEqual(startDate) || service.getServiceDate().isAfter(startDate)) &&
                    (service.getServiceDate().isEqual(endDate) || service.getServiceDate().isBefore(endDate))) {
                filteredServices.add(service);
            }
        }
        return filteredServices;
    }

    public void updateService() throws Exception {
        displayAllServices();
        Service.updateService();
    }


    public void deleteService() throws Exception {
        displayAllServices();
        Service.deleteService();
    }

    public double calculateTotalServiceRevenue() {
        double total = 0.0;
        for (Service service : services) {
            total += service.getTotalCost();
        }
        return total;
    }


    public int calculateAutoPartUsed(LocalDate startDate, LocalDate endDate) {
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

    public double[] calculateServiceRevenueAndCount(LocalDate startDate, LocalDate endDate) {
        double totalServiceRevenue = 0.0;
        int serviceCount = 0;

        for (Service service : getServicesBetween(startDate, endDate)) {
            totalServiceRevenue += service.getTotalCost();
            serviceCount++;
        }

        return new double[]{totalServiceRevenue, serviceCount};
    }

    public double calculateMechanicRevenue(String mechanicId, LocalDate startDate, LocalDate endDate) {

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

    public void viewServiceStatistics(LocalDate startDate, LocalDate endDate) {
        double[] serviceRevenueAndCount = calculateServiceRevenueAndCount(startDate, endDate);
        double totalServiceRevenue = serviceRevenueAndCount[0];
        int serviceCount = (int) serviceRevenueAndCount[1];
        int autoPartUsed = calculateAutoPartUsed(startDate, endDate);

        Map<String, Double> clientRevenue = new HashMap<>();

        for (Service service : getServicesBetween(startDate, endDate)) {

            clientRevenue.put(service.getClientId(), clientRevenue.getOrDefault(service.getClientId(), 0.0) + service.getTotalCost());
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