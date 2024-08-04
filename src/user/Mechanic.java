package user;


import services.Service;
import services.ServiceList;

import java.time.LocalDate;
import java.util.List;

public class Mechanic extends Employee {
    private ServiceList serviceList;

    public Mechanic(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status, ServiceList serviceList) {
        super(userName, password, name, dob, address, phoneNum, email, userType, status, null, serviceList);
        this.serviceList = serviceList;
    }

    // View overall service statistics within a date range
    public void viewServiceStatistics(LocalDate startDate, LocalDate endDate) {
        List<Service> filteredServices = serviceList.getServicesBetween(startDate, endDate);

        // Calculate statistics
        double totalServiceCost = filteredServices.stream()
                .mapToDouble(Service::getServiceCost)
                .sum();
        int totalServices = filteredServices.size();

        // Print statistics
        System.out.println("Service Statistics for the Period:");
        System.out.println("Total Service Cost: $" + String.format("%.2f", totalServiceCost));
        System.out.println("Total Number of Services: " + totalServices);

        // Optionally, print details of each service (for more granularity)
        if (!filteredServices.isEmpty()) {
            System.out.println("\nService Details:");
            for (Service service : filteredServices) {
                System.out.println(service.getFormattedServiceDetails());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    // View service statistics for a specific mechanic
    public void viewServiceByMechanic(LocalDate startDate, LocalDate endDate) {
        String mechanicId = this.userName;

        List<Service> filteredServices = serviceList.getServicesBetween(startDate, endDate);
        filteredServices = filteredServices.stream()
                .filter(service -> service.getMechanicId().equals(mechanicId))
                .toList();

        // Calculate statistics
        double totalServiceCost = filteredServices.stream()
                .mapToDouble(Service::getServiceCost)
                .sum();
        int totalServices = filteredServices.size();

        System.out.println("Service Statistics for Mechanic ID: " + mechanicId);
        System.out.println("Total Service Cost: $" + String.format("%.2f", totalServiceCost));
        System.out.println("Total Number of Services: " + totalServices);

        if (!filteredServices.isEmpty()) {
            System.out.println("\nService Details:");
            for (Service service : filteredServices) {
                System.out.println(service.getFormattedServiceDetails());
                System.out.println("--------------------------------------------------");
            }
        }
    }
}
