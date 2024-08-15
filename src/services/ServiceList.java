package services;

import autoPart.autoPart;
import user.Client;
import user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.*;

public class ServiceList {
    private List<Service> services;

    public ServiceList() {
        this.services = new ArrayList<>();
    }




    public void addService(String mechanicId) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction date (YYYY-MM-DD): ");
        LocalDate transactionDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter client ID: ");
        String clientId = scanner.nextLine();

        System.out.print("Enter serviceType: ");
        String serviceType = scanner.nextLine();

        System.out.println("Enter name of replaced parts (seperated by space): ");
        String partNamesInput = scanner.nextLine();
        List<String> partNames = List.of(partNamesInput.split("\\s+"));

        System.out.print("Type 1 if the service was made by AUTO136. Type 2 if the service was made by OTHER: ");
        int serviceByInput = Integer.parseInt(scanner.nextLine());
        ServiceBy serviceBy = (serviceByInput == 1) ? ServiceBy.AUTO136 : ServiceBy.OTHER;

        System.out.print("Enter car ID: ");
        String carId = scanner.nextLine();

        System.out.print("Enter service cost: ");
        double serviceCost = scanner.nextDouble();

        Service service = new Service(transactionDate, clientId, mechanicId, serviceType, partNames, serviceBy, carId, serviceCost);
        services.add(service);


        User user = User.userList.stream()
                .filter(u -> u.getUserID().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;
            client.updateTotalSpending(service.getServiceCost());  // Assuming Client class has this method
        }

        System.out.println("Sale transaction added successfully:");
        System.out.println(service.getFormattedServiceDetails());

    }

    public Service getServiceById(String serviceId) {
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

    public void updateService()  {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter service ID to update: ");
        String serviceId = scanner.nextLine();

        Service service = getServiceById(serviceId);
        if (service != null && !service.isDeleted()) {
            System.out.println("Which field would you like to update?");
            System.out.println("1. Service Date");
            System.out.println("2. Service Type");
            System.out.println("3. Service By");
            System.out.println("4. Car ID");
            System.out.println("5. Replaced Parts");
            System.out.println("6. Service Cost");
            System.out.println("7. Additional Notes");
            System.out.print("Enter the number corresponding to the field (or multiple numbers separated by space): ");
            String choiceInput = scanner.nextLine();
            List<String> choices = List.of(choiceInput.split("\\s+"));

            for (String choice : choices) {
                switch (choice) {
                    case "1":
                        System.out.print("Enter new service date (YYYY-MM-DD): ");
                        LocalDate newDate = LocalDate.parse(scanner.nextLine());
                        service.setServiceDate(newDate);
                        break;
                    case "2":
                        System.out.print("Enter new service type: ");
                        String newType = scanner.nextLine();
                        service.setServiceType(newType);
                        break;
                    case "3":
                        System.out.print("Type 1 if the service was made by AUTO136. Type 2 if the service was made by OTHER: ");
                        int serviceByInput = Integer.parseInt(scanner.nextLine());
                        ServiceBy newServiceBy = (serviceByInput == 1) ? ServiceBy.AUTO136 : ServiceBy.OTHER;
                        service.setServiceBy(newServiceBy);
                        break;
                    case "4":
                        System.out.print("Enter new car ID: ");
                        String newCarId = scanner.nextLine();
                        service.setCarId(newCarId);
                        break;
                    case "5":
                        System.out.println("Enter new replaced parts (part IDs separated by space): ");
                        String partIdsInput = scanner.nextLine();
                        List<String> partIds = List.of(partIdsInput.split("\\s+"));

                        // Retrieve and update replaced parts
                        List<autoPart> newReplacedParts = service.retrieveParts(partIds);
                        service.setReplacedParts(newReplacedParts);

                        double newDiscount = service.calculateDiscount(service.getClientId());
                        double newservice = service.calculateTotalAmount(newReplacedParts, newDiscount, service.getServiceCost());
                        service.setTotalCost(newservice);

                        // Update client's total spending
                        Client client = (Client) User.userList.stream()
                                .filter(u -> u.getUserID().equals(service.getClientId()))
                                .findFirst()
                                .orElse(null);
                        if (client != null) {
                            client.updateTotalSpending(service.getTotalCost());
                        }

                        break;
                    case "6":
                        System.out.print("Enter service cost: ");
                        double serviceCost = scanner.nextDouble();
                        service.setServiceCost(serviceCost);
                        break;
                    case "7":
                        System.out.print("Enter additional notes (or leave blank): ");
                        String additionalNotes = scanner.nextLine();
                        service.setAdditionalNotes(additionalNotes);
                        break;
                    default:
                        System.out.println("Invalid choice: " + choice);
                }
            }

            // Update the service in the list successfully
            System.out.println("Service updated successfully:");
            System.out.println(service.getFormattedServiceDetails());
        } else {
            System.out.println("Service not found or it has been deleted.");
        }
    }


    public void deleteService() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction ID to delete: ");
        String serviceId = scanner.nextLine();

        Service service = getServiceById(serviceId);
        if (service != null) {
            service.markAsDeleted();
            System.out.println("Sale transaction marked as deleted.");
        } else {
            System.out.println("Transaction not found.");
        }
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