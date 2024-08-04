package services;

import autoPart.autoPart;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private String additionalNotes;
    private static int serviceCounter = 1;

    // Constructor
    public Service(LocalDate serviceDate, String clientId, String mechanicId, String serviceType, ServiceBy serviceBy, String carId, double serviceCost) {
        this.serviceId = "s-" + serviceCounter++;
        this.serviceDate = serviceDate;
        this.clientId = clientId;
        this.mechanicId = mechanicId;
        this.serviceType = serviceType;
        this.replacedParts = new ArrayList<autoPart>();
        this.serviceBy = serviceBy;
        this.carId = carId;
        this.serviceCost = serviceCost;
        this.additionalNotes = "";
    }

    // Getters
    public String getServiceId() { return serviceId; }
    public LocalDate getServiceDate() { return serviceDate; }
    public String getClientId() { return clientId; }
    public String getMechanicId() { return mechanicId; }
    public String getServiceType() { return serviceType; }
    public List<autoPart> getReplacedParts() { return replacedParts; }
    public ServiceBy getServiceBy() { return serviceBy; }
    public String getCarId() { return carId; }
    public double getServiceCost() { return serviceCost; }
    public String getAdditionalNotes() { return additionalNotes; }

    // Setters
    public void setServiceId(String id) { this.serviceId = id; }
    public void setServiceDate(LocalDate date) { this.serviceDate = date; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public void setMechanicId(String id) { this.mechanicId = id; }
    public void setServiceType(String type) { this.serviceType = type; }
    public void setReplacedParts(List<autoPart> parts) { this.replacedParts = parts; }
    public void setServiceBy(ServiceBy serviceBy) { this.serviceBy = serviceBy; }
    public void setCarId(String carId) { this.carId = carId; }
    public void setServiceCost(double cost) { this.serviceCost = cost; }
    public void setAdditionalNotes(String notes) { this.additionalNotes = notes; }

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
