package services;

import autoPart.autoPart;
import data.autoPart.AutoPartDatabase;
import data.service.ServiceDatabase;
import data.user.UserDatabase;
import user.Client;
import user.Membership;
import user.User;
import utils.CarAndAutoPartMenu;
import utils.UserMenu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Service implements Serializable {
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
    public Service(LocalDate serviceDate, String clientId, String mechanicId, String serviceType, List<String> partNames, ServiceBy serviceBy, String carId, double serviceCost) throws Exception {
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

//    public static void addService(String mechanicId) throws Exception {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("Enter transaction date (YYYY-MM-DD): ");
//        LocalDate transactionDate = LocalDate.parse(scanner.nextLine());
//
//        System.out.print("Enter client ID: ");
//        String clientId = scanner.nextLine();
//
//        System.out.print("Enter serviceType: ");
//        String serviceType = scanner.nextLine();
//
//        System.out.println("Enter name of replaced parts (seperated by space): ");
//        String partNamesInput = scanner.nextLine();
//        List<String> partNames = List.of(partNamesInput.split("\\s+"));
//
//        System.out.print("Type 1 if the service was made by AUTO136. Type 2 if the service was made by OTHER: ");
//        int serviceByInput = Integer.parseInt(scanner.nextLine());
//        ServiceBy serviceBy = (serviceByInput == 1) ? ServiceBy.AUTO136 : ServiceBy.OTHER;
//
//        System.out.print("Enter car ID: ");
//        String carId = scanner.nextLine();
//
//        System.out.print("Enter service cost: ");
//        double serviceCost = scanner.nextDouble();
//
//        Service service = new Service(transactionDate, clientId, mechanicId, serviceType, partNames, serviceBy, carId, serviceCost);
//        Service.serviceList.add(service);
//        ServiceDatabase.saveService(serviceList);
//
//        User user = User.userList.stream()
//                .filter(u -> u.getUserID().equals(clientId))
//                .findFirst()
//                .orElse(null);
//
//        if (user != null && user instanceof Client) {
//            Client client = (Client) user;
//            client.updateTotalSpending(service.getServiceCost());  // Assuming Client class has this method
//        }
//
//        System.out.println("Service added successfully:");
//        System.out.println(service.getFormattedServiceDetails());
//
//    }
    public static void addService(Service service) throws Exception {
        // TO DO: Thêm method để track xem liệu client id có trong database không. Nếu không yêu cầu tạo hoặc không cho thực hiện.
        ServiceList.services.add(service);
        for(User user: UserMenu.getUserList()){
            if(user.getUserName().equals(service.clientId)){
                Client client = (Client) user;
                client.updateTotalSpending(service.serviceCost);
            }
        }
        UserDatabase.saveUsersData(UserMenu.getUserList());
        ServiceDatabase.saveService(ServiceList.services);
        System.out.println("Service added successfully:");
    }
    public static Service getServiceById(String serviceId) {
        for (Service service : ServiceList.services) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    public static void updateService() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter service ID to update: ");
        String serviceId = scanner.nextLine();

        Service service = Service.getServiceById(serviceId);
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
                        Client client = (Client) UserMenu.getUserList().stream()
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

                ServiceDatabase.saveService(ServiceList.services);
            }

            // Update the service in the list successfully
            System.out.println("Service updated successfully:");
            System.out.println(service.getFormattedServiceDetails());
        } else {
            System.out.println("Service not found or it has been deleted.");
        }
    }

    public static void deleteService() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter transaction ID to delete: ");
        String serviceId = scanner.nextLine();

        Service service = Service.getServiceById(serviceId);
        if (service != null) {
            service.markAsDeleted();
            ServiceDatabase.saveService(ServiceList.services);
            System.out.println("Sale transaction marked as deleted.");
        } else {
            System.out.println("Transaction not found.");
        }
    }

    List<autoPart> retrieveParts(List<String> partNames) {
        List<autoPart> parts = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not
        for (String partName :partNames) {
            Optional<autoPart> partOpt = AutoPartDatabase.loadAutoParts().stream()
                    .filter(part -> part.getPartName().equalsIgnoreCase(partName))
                    .findFirst();
            partOpt.ifPresent(parts::add);
        }
        return parts;
    }

    double calculateDiscount(String clientId) {
        // find membership of that specific clientId
        User user = UserMenu.getUserList().stream()
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
