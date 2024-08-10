package user;


import services.Service;
import services.ServiceList;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Mechanic extends Employee {
    private ServiceList serviceList;

    public Mechanic(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status, ServiceList serviceList) {
        super(userName, password, name, dob, address, phoneNum, email, userType, status, null, serviceList);
        this.serviceList = serviceList;
    }

    // View overall service statistics within a date range
//    public void viewServiceStatistics(LocalDate startDate, LocalDate endDate) {
//        List<Service> filteredServices = serviceList.getServicesBetween(startDate, endDate);
//
//        // Calculate statistics
//        double totalServiceCost = filteredServices.stream()
//                .mapToDouble(Service::getServiceCost)
//                .sum();
//        int totalServices = filteredServices.size();
//
//        // Print statistics
//        System.out.println("Service Statistics for the Period:");
//        System.out.println("Total Service Cost: $" + String.format("%.2f", totalServiceCost));
//        System.out.println("Total Number of Services: " + totalServices);
//
//        // Optionally, print details of each service (for more granularity)
//        if (!filteredServices.isEmpty()) {
//            System.out.println("\nService Details:");
//            for (Service service : filteredServices) {
//                System.out.println(service.getFormattedServiceDetails());
//                System.out.println("--------------------------------------------------");
//            }
//        }
//    }
//
//    // View service statistics for a specific mechanic
//    public void viewServiceByMechanic(LocalDate startDate, LocalDate endDate) {
//        String mechanicId = this.userName;
//
//        List<Service> filteredServices = serviceList.getServicesBetween(startDate, endDate);
//        filteredServices = filteredServices.stream()
//                .filter(service -> service.getMechanicId().equals(mechanicId))
//                .toList();
//
//        // Calculate statistics
//        double totalServiceCost = filteredServices.stream()
//                .mapToDouble(Service::getServiceCost)
//                .sum();
//        int totalServices = filteredServices.size();
//
//        System.out.println("Service Statistics for Mechanic ID: " + mechanicId);
//        System.out.println("Total Service Cost: $" + String.format("%.2f", totalServiceCost));
//        System.out.println("Total Number of Services: " + totalServices);
//
//        if (!filteredServices.isEmpty()) {
//            System.out.println("\nService Details:");
//            for (Service service : filteredServices) {
//                System.out.println(service.getFormattedServiceDetails());
//                System.out.println("--------------------------------------------------");
//            }
//        }
//    }
    public double getRevenueInASpecificPeriod() {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate;
        LocalDate endDate;
        double totalServiceRevenue = 0.0;


        // Get start date
        while (true) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine());
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid start date format. Please try again.");
            }
        }

        // Get end date
        while (true) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine());
                if (!endDate.isBefore(startDate)) {
                    break;
                }
                System.out.println("End date cannot be before start date. Please try again.");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid end date format. Please try again.");
            }
        }

        // Calculate total service revenue
        List<Service> servicesInRange = serviceList.getServicesBetween(startDate, endDate);
        String finalMechanicId = this.userID;
        List<Service> filteredServices = servicesInRange.stream()
                .filter(service -> service.getMechanicId().equals(finalMechanicId))
                .toList();

        totalServiceRevenue = filteredServices.stream()
                .mapToDouble(Service::getTotalCost)
                .sum();

        return totalServiceRevenue;
    }

    public double getAllRevenue() {
        double totalServiceRevenue = 0.0;

        // Retrieve all services
        List<Service> allServices = serviceList.getAllServices();

        // Filter services by mechanic ID
        List<Service> filteredServices = allServices.stream()
                .filter(service -> service.getMechanicId().equals(this.userID))
                .toList();

        // Calculate total service revenue
        totalServiceRevenue = filteredServices.stream()
                .mapToDouble(Service::getTotalCost)
                .sum();

        return totalServiceRevenue;
    }

    public void addService() {
        serviceList.addService(this.getUserName());
    }

    public void servicesMadeByMe(LocalDate startDate, LocalDate endDate) {
        String mechanicId = this.getUserName();
        serviceList.viewServiceByMechanic(mechanicId, startDate, endDate);
    }
}
