package services;

import autoPart.autoPart;
import user.Client;
import user.Membership;
import user.User;
import utils.CarAndAutoPartMenu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Optional;

public class Service {
    private String serviceId;
    private LocalDate serviceDate;
    private String clientId;
    private String mechanicId;
    private String serviceType;
    private List<autoPart> replacedParts;
    private ServiceBy serviceBy;
    private String carId;
    private double serviceCost;
    private double totalCost;
    private boolean isDeleted;
    private String additionalNotes;


    // Constructor
    public Service(LocalDate serviceDate, String clientId, String mechanicId, String serviceType, List<String> partNames, ServiceBy serviceBy, String carId, double serviceCost) {
        this.serviceId = generateServiceId();
        this.serviceDate = serviceDate;
        this.clientId = clientId;
        this.mechanicId = mechanicId;
        this.serviceType = serviceType;
        this.replacedParts = retrieveParts(partNames);
        this.serviceBy = serviceBy;
        this.carId = carId;
        this.serviceCost = serviceCost;
        this.totalCost = calculateTotalAmount(replacedParts, calculateDiscount(clientId), serviceCost);
        this.additionalNotes = "";
        this.isDeleted = false;

    }

    List<autoPart> retrieveParts(List<String> partNames) {
        List<autoPart> parts = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not
        for (String partName :partNames) {
            Optional<autoPart> partOpt = CarAndAutoPartMenu.getAutoPartsList().stream()
                    .filter(part -> part.getPartName().equalsIgnoreCase(partName))
                    .findFirst();
            partOpt.ifPresent(parts::add);
        }
        return parts;
    }

    double calculateDiscount(String clientId) {
        // find membership of that specific clientId
        User user = User.userList.stream()
                .filter(u -> u.getUserID().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;  // Cast to Client
            Membership membership = client.getMembership();  // Access getMembership
            if (membership != null) {
                return membership.getDiscount();  // Assuming discount rate is a fraction (e.g., 0.15 for 15%)
            }
        }
        return 0;
    }

    double calculateTotalAmount(List<autoPart> replacedParts, double discount, double serviceCost) {
        double total = 0;
        for (autoPart part : replacedParts) {
            total += part.getPrice();
        }
        return (total + serviceCost) * (1 - discount);
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    List<autoPart> retrieveParts(List<String> partNames) {
        List<autoPart> parts = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not
        for (String partName : partNames) {
            Optional<autoPart> partOpt = CarAndAutoPartMenu.getAutoPartsList().stream()
                    .filter(part -> part.getPartName().equalsIgnoreCase(partName))
                    .findFirst();
            partOpt.ifPresent(parts::add);
        }
        return parts;
    }

    double calculateDiscount(String clientId) {
        // find membership of that specific clientId
        User user = User.userList.stream()
                .filter(u -> u.getUserID().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;  // Cast to Client
            Membership membership = client.getMembership();  // Access getMembership
            if (membership != null) {
                return membership.getDiscount();  // Assuming discount rate is a fraction (e.g., 0.15 for 15%)
            }
        }
        return 0;
    }

    double calculateTotalAmount(List<autoPart> replacedParts, double discount, double serviceCost) {
        double total = 0;
        for (autoPart part : replacedParts) {
            total += part.getPrice();
        }
        return (total + serviceCost) * (1 - discount);
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    private String generateServiceId() {
        return "s-" + UUID.randomUUID().toString();
    }

    // Getters
    public String getServiceId() {
        return serviceId;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public String getClientId() {
        return clientId;
    }

    public String getMechanicId() {
        return mechanicId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<autoPart> getReplacedParts() {
        return replacedParts;
    }

    public ServiceBy getServiceBy() {
        return serviceBy;
    }

    public String getCarId() {
        return carId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    // Setters
    public void setServiceId(String id) {
        this.serviceId = id;
    }

    public void setServiceDate(LocalDate date) {
        this.serviceDate = date;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setMechanicId(String id) {
        this.mechanicId = id;
    }

    public void setServiceType(String type) {
        this.serviceType = type;
    }

    public void setReplacedParts(List<autoPart> parts) {
        this.replacedParts = parts;
    }

    public void setServiceBy(ServiceBy serviceBy) {
        this.serviceBy = serviceBy;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setServiceCost(double cost) {
        this.serviceCost = cost;
    }

    public void setTotalCost(double cost) {
        this.totalCost = cost;
    }

    public void setAdditionalNotes(String notes) {
        this.additionalNotes = notes;
    }


    public String getFormattedServiceDetails() {
        StringBuilder sb = new StringBuilder();

        sb.append("Service ID: ").append(serviceId).append("\n");
        sb.append("Service Date: ").append(serviceDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n");
        sb.append("Client ID: ").append(clientId).append("\n");
        sb.append("Mechanic ID: ").append(mechanicId).append("\n");
        sb.append("Service Type: ").append(serviceType).append("\n");
        sb.append("Service By: ").append(serviceBy).append("\n");
        sb.append("Service Cost: ").append(serviceCost).append("\n");

        // TODO: need AutoPart Class for this operation
        // if (!replacedParts.isEmpty()) {
        //     List<String> partNames = new ArrayList<>();
        //     for (AutoPart part : replacedParts) {
        //         partNames.add(part.getPartName());
        //     }
        //     sb.append("Replaced Parts: ").append(String.join(", ", partNames)).append("\n");
        // }

        sb.append("Service Cost: $").append(String.format("%.2f", serviceCost)).append("\n");
        if (!additionalNotes.isEmpty()) {
            sb.append("Notes: ").append(additionalNotes).append("\n");
        }
        return sb.toString();
    }
}
