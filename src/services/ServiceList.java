package services;

import autoPart.autoPart;
import car.Car;
import data.Database;
import data.service.ServiceDatabase;
import user.Client;
import user.Manager;
import user.Mechanic;
import user.User;
import utils.CurrencyFormat;
import utils.DatePrompt;
import utils.Status;
import utils.UserSession;
import utils.menu.ActivityLogMenu;
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


    public static void addService() throws Exception {
        Scanner scanner = new Scanner(System.in);

        String type = null;
        LocalDate serviceDate = DatePrompt.getDate("service");
        System.out.println("Clients Available:");
        UserMenu.getUserList().stream().filter(user -> user instanceof Client).forEach(System.out::println);
        System.out.print("Enter client Id: ");
        String clientId = scanner.nextLine();
        boolean clientFound = false;
        for (User user : UserMenu.getUserList()) {
            if (user.getUserID().equals(clientId) && user instanceof Client) {
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
        Set<String> partNames = Set.of();
        String mechanicID = null;


        if (serviceBy == ServiceBy.AUTO136) {
            if (UserSession.getCurrentUser() instanceof Manager) {
                System.out.println("Mechanics available:");
                UserMenu.getUserList().stream().filter(user -> user instanceof Mechanic).forEach(System.out::println);
                System.out.println("Enter mechanic ID: ");
                mechanicID = scanner.nextLine();

                boolean mechanicFound = false;
                for (User user : UserMenu.getUserList()) {
                    if (user.getUserID().equals(mechanicID) && user instanceof Mechanic) {
                        mechanicFound = true;
                        break;
                    }
                }
                if (!mechanicFound) {
                    throw new Exception("Mechanic ID not found.");
                }
            } else {
                mechanicID = UserSession.getCurrentUser().getUserID();
            }

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
            System.out.println("Part:");
            CarAndAutoPartMenu.getAutoPartsList().stream().filter(part -> !part.isDeleted() && part.getStatus() == Status.AVAILABLE).forEach(System.out::println);

            System.out.println("Enter ID of replaced parts (separate by comma, or leave empty if none): ");
            String partNamesInput = scanner.nextLine();
            partNames = partNamesInput.isEmpty() ? Collections.emptySet() :
                    Arrays.stream(partNamesInput.split(","))
                            .map(String::trim)
                            .map(partName -> partName.replaceAll(" +", " "))
                            .collect(Collectors.toSet());
        } else {
            System.out.print("Enter service type done by others: ");
            type = scanner.nextLine();

        }

        System.out.print("Enter additional notes: ");
        String notes = scanner.nextLine();

        System.out.println("Service Car:");
        CarAndAutoPartMenu.getCarsList().stream().filter(car -> !car.isDeleted() && car.getClientID() != null && car.getClientID().equals(clientId)).forEach(System.out::println);
        System.out.print("Enter car ID if your car is already registered in the database. If not press ENTER to register the car in the database: ");
        String carId = scanner.nextLine();
        Service service = new Service(serviceDate, clientId, mechanicID, selectedServiceType, partNames, serviceBy, carId, serviceCost, notes);
        Service.addService(service);
        service.setServiceTypeByOther(type);
        System.out.println(service.getFormattedServiceDetails());

        try {
            ActivityLogMenu.addActivityLogForCurrentUser("Created a new service with ID: " + service.getServiceId());
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
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

    public static List<Service> getServicesByCLient(String clientId) {
        var serviceByCLient = services.stream().filter(service -> service.getClientId().equals(clientId)).toList();
        return serviceByCLient;
    }

    public List<Service> getAllServices() {
        return services;
    }

    public static void displayAllServices() {

        User current = UserSession.getCurrentUser();
        if (current instanceof Manager) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. View all available");
            System.out.println("2. View all service done by AUTO136");
            System.out.println("3. View all service done by OTHER");
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    for (Service service : services) {
                        if (!service.isDeleted()) {
                            System.out.println(service.getFormattedServiceDetails());
                            System.out.println("___________________________________");
                        }
                    }
                    break;
                case 2:
                    System.out.println("All service done by AUTO136");
                    for (Service service : services) {
                        if (!service.isDeleted()) {
                            if (service.getServiceBy() == ServiceBy.AUTO136) {
                                System.out.println(service.getFormattedServiceDetails());
                                System.out.println("___________________________________");
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("All service done by others");
                    for (Service service : services) {
                        if (!service.isDeleted()) {
                            if (service.getServiceBy() == ServiceBy.OTHER) {
                                System.out.println(service.getFormattedServiceDetails());
                                System.out.println("___________________________________");
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Please enter a valid option!");
                    break;
            }
        } else if (current instanceof Mechanic) {
            for (Service service : services) {
                if (!service.isDeleted() && service.getMechanicId() != null && service.getMechanicId().equals(current.getUserID())) {
                    System.out.println(service.getFormattedServiceDetails());
                    System.out.println("___________________________________");
                }
            }
        } else if (current instanceof Client) {
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
                .filter(service -> service.getMechanicId() != null && service.getMechanicId().equals(mechanicId)).toList();
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
            if (service.getMechanicId() != null && service.getMechanicId().equals(mechanicId)) {
                walkInCars.add(CarAndAutoPartMenu.findCarByID(service.getCarId()));
            }
        }
        return walkInCars;
    }

    public static List<autoPart> listAutoPartUsedInMechanicService(String mechanicId, LocalDate startDate, LocalDate endDate) {
        List<autoPart> usedParts = new ArrayList<>();

        for (Service service : getServicesBetween(startDate, endDate)) {
            if (service.getMechanicId() != null && service.getMechanicId().equals(mechanicId)) {
                usedParts.addAll(service.getReplacedParts());
            }
        }

        return usedParts;
    }

    public static double[] calculateServiceRevenueAndCount(LocalDate startDate, LocalDate endDate) {
        double totalServiceRevenue = 0.0;
        int serviceCount = 0;
        int serviceByAUTO136 = 0;

        for (Service service : getServicesBetween(startDate, endDate)) {
            if (service.getServiceBy() == ServiceBy.OTHER) {
                serviceByAUTO136++;
            }
            totalServiceRevenue += service.getTotalCost();
            serviceCount++;
        }

        return new double[]{totalServiceRevenue, serviceCount, serviceByAUTO136};
    }

    public static double calculateMechanicRevenue(String mechanicId, LocalDate startDate, LocalDate endDate) {

        double totalServiceRevenue = 0.0;

        List<Service> servicesInRange = getServicesBetween(startDate, endDate);
        List<Service> filteredServices = servicesInRange.stream()
                .filter(service -> service.getMechanicId() != null && service.getMechanicId().equals(mechanicId))
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
        int serviceByOther = 0;
        int autoPartUsed = calculateAutoPartUsed(startDate, endDate);

        Map<String, Double> clientRevenue = new HashMap<>();
        Map<String, Integer> serviceUsageCount = new HashMap<>();
        Map<String, Double> serviceRevenue = new HashMap<>();
        Map<String, Double> mechanicRevenue = new HashMap<>();

        for (Service service : getServicesBetween(startDate, endDate)) {

            if (!ServiceBy.AUTO136.equals(service.getServiceBy())) {
                serviceByOther++;
                continue;
            }

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
        System.out.println("-------------------------------------------------");
        System.out.println("Total Services Revenue: " + CurrencyFormat.format(totalServiceRevenue));
        System.out.printf("Total Number of Services: %d\n", serviceCount);
        System.out.printf("Number of Services by AUTO136: %d\n", serviceCount - serviceByOther);
        System.out.printf("Total Number of AutoPart used: %d\n", autoPartUsed);
        System.out.println("-------------------------------------------------");
        // Revenue by client
        System.out.println("Revenue by Client:");
        for (Map.Entry<String, Double> entry : clientRevenue.entrySet()) {
            System.out.printf("Client ID: %s, Revenue: %s\n", UserMenu.getUserById(entry.getKey()).getName(), CurrencyFormat.format(entry.getValue()));
        }
        System.out.println("-------------------------------------------------");
        // Top mechanic
        if (topMechanicId != null) {
            System.out.printf("Top Mechanic: %s, Revenue: %s\n", UserMenu.getUserById(topMechanicId).getName(), CurrencyFormat.format(maxRevenue));
        } else {
            System.out.println("No service transactions in the given period.");
        }
        System.out.println("-------------------------------------------------");
        if (mostUsedService != null) {
            System.out.printf("Most Used Service: %s\n", mostUsedService);
        }
        System.out.println("-------------------------------------------------");
        if (highestRevenueService != null) {
            System.out.printf("Highest Revenue Service: %s, Revenue: %s\n", highestRevenueService, CurrencyFormat.format(highestRevenue));
        }
        System.out.println("-------------------------------------------------");
        System.out.println("Usages of Service:");
        for (Map.Entry<String, Integer> entry : serviceUsageCount.entrySet()) {
            System.out.printf("Service Type: %s, Usage: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println("-------------------------------------------------");
        // Revenue by Service Type
        System.out.println("Revenue of Service:");
        for (Map.Entry<String, Double> entry : serviceRevenue.entrySet()) {
            System.out.printf("Service Type: %s, Revenue: %s\n", entry.getKey(), CurrencyFormat.format(entry.getValue()));
        }
        System.out.println("-------------------------------------------------");
        // Revenue by Mechanic
        System.out.println("Revenue of Mechanic:");
        for (Map.Entry<String, Double> entry : mechanicRevenue.entrySet()) {
            System.out.printf("Mechanic: %s, Revenue: %s\n", UserMenu.getUserById(entry.getKey()).getName(), CurrencyFormat.format(entry.getValue()));
        }
        System.out.println("-------------------------------------------------");
    }

    public static void viewServiceByMechanic(String mechanicId, LocalDate startDate, LocalDate endDate) {
        List<Service> servicesInRange = getServicesBetween(startDate, endDate).stream().filter(service -> service.getMechanicId() != null).toList();

        List<Service> filteredService = servicesInRange.stream()
                .filter(service -> mechanicId.equals(service.getMechanicId()))
                .toList();

        double totalServiceRevenue = calculateMechanicRevenue(mechanicId, startDate, endDate);

        int serviceCount = filteredService.size();
        System.out.println("-------------------------------------------------");
        System.out.println("Services done by mechanic: " + UserMenu.getUserById(mechanicId).getName());
        System.out.println("Total Service Revenue: " + CurrencyFormat.format(totalServiceRevenue));
        System.out.println("Total Number of Service: " + serviceCount);
        System.out.println("-------------------------------------------------");
    }
}