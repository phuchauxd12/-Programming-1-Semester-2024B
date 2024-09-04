package services;

import autoPart.autoPart;
import car.Car;
import data.Database;
import data.service.ServiceDatabase;
import user.Client;
import user.Mechanic;
import user.User;
import utils.DatePrompt;
import utils.UserSession;
import utils.menu.CarAndAutoPartMenu;
import utils.menu.UserMenu;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceList {
    public static List<Service> services;

    // This code run one time when create an instance of a class
    static {
        try {
            if (!Database.isDatabaseExist(ServiceDatabase.path)) {
                ServiceDatabase.createDatabase();
            }
            ;
            services = ServiceDatabase.loadService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void addService(String mechanicId) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String type = null;

        System.out.print("Enter transaction date (dd/MM/yyyy): ");
        String input = DatePrompt.sanitizeDateInput(scanner.nextLine());
        LocalDate serviceDate = DatePrompt.validateAndParseDate(input);

        System.out.print("Enter client Id: ");
        String clientId = scanner.nextLine();
        boolean clientFound = false;
        for(User user: UserMenu.getUserList()){
            if(user.getUserID().equals(clientId) && user instanceof Client){
                clientFound = true;
                break;
            }
        }
        if (!clientFound) {
            throw new Exception("Client ID not found.");
        }

        System.out.print("Type 1 if the service was made by AUTO136. Type 2 if the service was made by OTHER: ");
        int serviceByInput = Integer.parseInt(scanner.nextLine());
        ServiceBy serviceBy = (serviceByInput == 1) ? ServiceBy.AUTO136 : ServiceBy.OTHER;

        Service.serviceType selectedServiceType = null;
        double serviceCost = 0;
        List<String> partNames = List.of();
        
        if (serviceBy == ServiceBy.AUTO136) {
            
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
            selectedServiceType = serviceTypes.get(serviceTypeIndex);

            serviceCost = selectedServiceType.getPrice();

            scanner.nextLine();

            System.out.println("Enter ID of replaced parts (separate by comma, or leave empty if none): ");
            String partNamesInput = scanner.nextLine();
            partNames = partNamesInput.isEmpty() ? Collections.emptyList() :
                    Arrays.stream(partNamesInput.split(","))
                            .map(String::trim)
                            .map(partName -> partName.replaceAll(" +", " "))
                            .collect(Collectors.toList());
        } else {
            System.out.print("Enter service type done by others: ");
            type = scanner.nextLine();
            
        }

        System.out.print("Enter additional notes: ");
        String notes = scanner.nextLine();

        System.out.print("Enter car ID if your car is already registered in the database. If not press ENTER to register the car in the database: ");
        String carId = scanner.nextLine();



        Service service = new Service(serviceDate, clientId, mechanicId, selectedServiceType, partNames, serviceBy, carId, serviceCost, notes);
        Service.addService(service);
        service.setServiceTypeByOther(type);
    }


    public static Service getServiceById(String serviceId) {
        for (Service service : services) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    public  static List<Service> getServicesByCLient(String clientId){
        var serviceByCLient = services.stream().filter(service -> service.getClientId().equals(clientId)).toList();
        return  serviceByCLient;
    }
    public List<Service> getAllServices() {
        return services;
    }

    public static void displayAllServices() {

        User current = UserSession.getCurrentUser();
        if(current.getRole().equals(User.ROLE.MANAGER)){
            for (Service service : services) {
                if (!service.isDeleted()) {
                    System.out.println(service.getFormattedServiceDetails());
                    System.out.println("___________________________________");
                }
            }
        } else if (current.getRole().equals(User.ROLE.EMPLOYEE)) {
            if(current instanceof Mechanic){
                for (Service service : services) {
                    if (!service.isDeleted() && service.getMechanicId() == current.getUserID()) {
                        System.out.println(service.getFormattedServiceDetails());
                        System.out.println("___________________________________");
                    }
                }
            }
            else {
                System.out.println("Your role is not able to access this field");
            }
        } else  if (current instanceof Client){
            for (Service service : getServicesByCLient(current.getUserID())) {
                if (!service.isDeleted()) {
                    System.out.println(service.getFormattedServiceDetails());
                    System.out.println("___________________________________");
                }
            }
        }
    }

    public static List<Service> getServicesBetween(LocalDate startDate, LocalDate endDate) {
        List<Service> filteredServices = new ArrayList<>();
        for (Service service : services) {
            LocalDate serviceDate = service.getServiceDate();
            if ((serviceDate.isEqual(startDate) || serviceDate.isAfter(startDate)) &&
                    (serviceDate.isEqual(endDate) || serviceDate.isBefore(endDate.plusDays(1))) && !service.isDeleted()) {
                filteredServices.add(service);
            }
        }
        return filteredServices;
    }

    public static List<Service> getServiceByMechanic(String mechanicId) {
        var serviceByMechanic = services.stream()
                .filter(service -> service.getMechanicId().equals(mechanicId)).toList();
        return serviceByMechanic;
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
    public static List<Car> listCarDoneServiceByMechanic(String mechanicId, LocalDate startDate, LocalDate endDate) {
        List<Car> walkInCars = new ArrayList<>();
        for (Service service : getServicesBetween(startDate, endDate)) {
            if (service.getMechanicId().equals(mechanicId)) {
                for (Car car : CarAndAutoPartMenu.getCarsList()) {
                    if (car.getCarID().equals(service.getCarId())) {
                        walkInCars.add(car);
                    }
                }
            }
        }
        return walkInCars;
    }
    public static List<autoPart> listAutoPartUsedInMechanicService(String mechanicId, LocalDate startDate, LocalDate endDate) {
        List<autoPart> usedParts = new ArrayList<>();

        for (Service service : getServicesBetween(startDate, endDate)) {
            if (service.getMechanicId().equals(mechanicId)) {
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
        System.out.printf("Service Statistics from %s to %s:\n", startDate, endDate);
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

        // Service Usage
        System.out.println("Usages of Service:");
        for (Map.Entry<String, Integer> entry : serviceUsageCount.entrySet()) {
            System.out.printf("Service Type: %s, Usage: $%.2f\n", entry.getKey(), entry.getValue());
        }

        // Revenue by Service Type
        System.out.println("Revenue of Service:");
        for (Map.Entry<String, Double> entry : serviceRevenue.entrySet()) {
            System.out.printf("Service Type: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
        }

        // Revenue by Mechanic
        System.out.println("Revenue of Mechanic:");
        for (Map.Entry<String, Double> entry : mechanicRevenue.entrySet()) {
            System.out.printf("Mechanic ID: %s, Revenue: $%.2f\n", entry.getKey(), entry.getValue());
        }
    }

    public static void viewServiceByMechanic(String mechanicId, LocalDate startDate, LocalDate endDate) {
        List<Service> servicesInRange = getServicesBetween(startDate, endDate);

        List<Service> filteredService = servicesInRange.stream()
                .filter(service -> service.getMechanicId().equals(mechanicId))
                .toList();

        double totalServiceRevenue = calculateMechanicRevenue(mechanicId, startDate, endDate);

        int serviceCount = filteredService.size();

        System.out.println("Services done by mechanic with ID: " + mechanicId);
        System.out.println("Total Service Revenue: $" + String.format("%.2f", totalServiceRevenue));
        System.out.println("Total Number of Service: " + serviceCount);

        if (!filteredService.isEmpty()) {
            System.out.println("\nService Details:");
            for (Service service : filteredService) {
                System.out.println(service.getFormattedServiceDetails());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}