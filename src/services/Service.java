package services;

import autoPart.autoPart;
import car.Car;
import data.autoPart.AutoPartDatabase;
import data.service.ServiceDatabase;
import data.user.UserDatabase;
import user.*;
import utils.menu.CarAndAutoPartMenu;
import utils.Status;
import utils.menu.UserMenu;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public Service(LocalDate serviceDate, String clientId, String mechanicId, String serviceType, List<String> partIds, ServiceBy serviceBy, String carId, double serviceCost) throws Exception {
        this.serviceId = generateServiceId();
        this.serviceDate = serviceDate;
        this.clientId = clientId;
        this.mechanicId = mechanicId;
        this.serviceType = serviceType;
        this.replacedParts = retrieveParts(partIds);
        this.serviceBy = serviceBy;
        this.carId = carId;
        this.serviceCost = serviceCost;
        this.totalCost = calculateTotalAmount(replacedParts, calculateDiscount(clientId), serviceCost);
        this.additionalNotes = "";
        this.isDeleted = false;

    }

    public static void addService(Service service) throws Exception {

        boolean clientExists = false;
        for (User user : UserMenu.getUserList()) {
            if (user.getUserName().equals(service.clientId) && user instanceof Client) {
                clientExists = true;
                Client client = (Client) user;
                client.updateTotalSpending(service.serviceCost);
                break;
            }
        }

        if (!clientExists) {
            throw new Exception("Client ID not found in the database. Please create a new client before adding the service.");
        }

        boolean carExists = false;
        for (Car car : CarAndAutoPartMenu.getCarsList()) {
            if (car.getCarID().equals(service.getCarId())) {
                carExists = true;
                break;
            }
        }

        if (!carExists) {
            System.out.println("No car exist in the database. Please create new car:");
            Scanner input = new Scanner(System.in);
            Status status = Status.WALK_IN;
            System.out.println("Please input the car's make:");
            String carMake = input.next();
            System.out.println("Please input the car's model:");
            String carModel = input.next();
            int carYear = Car.getNewCarYear(input);
            System.out.println("Please input the car's color:");
            String color = input.next().toUpperCase();
            double mileage;
            while (true) {
                try {
                    System.out.println("Please input the car's mileage:");
                    mileage = input.nextDouble();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please input a valid mileage");
                    input.nextLine();
                }
            }
            double price = 0;
            input.nextLine();
            System.out.println("Please input any additional notes:");
            String addNotes = input.nextLine();
            Car newCar = new Car(carMake, carModel, carYear, color, mileage, price, addNotes, status);
            try {
                Car.addCarToList(newCar);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Car created successfully!");
        } else {
            ServiceList.services.add(service);
            if (!service.replacedParts.isEmpty()) {
                for (autoPart part : service.replacedParts) {
                    part.setStatus(Status.SOLD);
                }
            }
        }

        UserDatabase.saveUsersData(UserMenu.getUserList());
        ServiceDatabase.saveService(ServiceList.services);
        AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
        System.out.println("Service added successfully!");
    }

    public static void updateService() throws Exception {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Enter service ID to update: ");
        String serviceId = scanner.nextLine();

        Service service = ServiceList.getServiceById(serviceId);
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
                        for (autoPart part : service.getReplacedParts()) {
                            part.setStatus(Status.AVAILABLE);
                            AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
                        }

                        System.out.println("Enter new replaced parts (part Ids separated by comma): ");
                        String partIdsInput = scanner.nextLine();
                        List<String> partIds = Arrays.stream(partIdsInput.split(","))
                                .map(String::trim)
                                .map(partName -> partName.replaceAll(" +", " "))  // Replace multiple spaces with a single space
                                .collect(Collectors.toList());

                        List<autoPart> newReplacedParts = service.retrieveParts(partIds);
                        service.setReplacedParts(newReplacedParts);

                        double newDiscount = service.calculateDiscount(service.getClientId());
                        double newservice = service.calculateTotalAmount(newReplacedParts, newDiscount, service.getServiceCost());
                        service.setTotalCost(newservice);

                        for (autoPart part : newReplacedParts) {
                            part.setStatus(Status.SOLD);
                            AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
                        }

                        Client client = (Client) UserMenu.getUserList().stream()
                                .filter(u -> u.getUserID().equals(service.getClientId()))
                                .findFirst()
                                .orElse(null);
                        if (client != null) {
                            client.updateTotalSpending(service.getTotalCost());
                            UserDatabase.saveUsersData(UserMenu.getUserList());
                        } else {
                            System.out.println("Client not found");
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

        Service service = ServiceList.getServiceById(serviceId);
        if (service != null) {
            service.markAsDeleted();
            ServiceDatabase.saveService(ServiceList.services);
            System.out.println("Sale transaction marked as deleted.");
        } else {
            System.out.println("Transaction not found.");
        }
    }

    List<autoPart> retrieveParts(List<String> partIds) throws Exception {
        List<autoPart> parts = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not

        for (String partId : partIds) {
            Optional<autoPart> partOpt = CarAndAutoPartMenu.getAutoPartsList().stream()
                    .filter(part -> part.getPartID().equalsIgnoreCase(partId))
                    .findFirst();
            partOpt.ifPresent(parts::add);
        }
        return parts;
    }

    double calculateDiscount(String clientId) {
        User user = UserMenu.getUserList().stream()
                .filter(u -> u.getUserName().equals(clientId))
                .findFirst()
                .orElse(null);

        if (user != null && user instanceof Client) {
            Client client = (Client) user;
            Membership membership = client.getMembership();
            if (membership != null) {
                return membership.getDiscount();
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


//    public String getFormattedServiceDetails() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("Service ID: ").append(serviceId).append("\n");
//        sb.append("Service Date: ").append(serviceDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n");
//        sb.append("Client ID: ").append(clientId).append("\n");
//        sb.append("Mechanic ID: ").append(mechanicId).append("\n");
//        sb.append("Service Type: ").append(serviceType).append("\n");
//        sb.append("Service By: ").append(serviceBy).append("\n");
//        sb.append("Service Cost: ").append(serviceCost).append("\n");
//
//        // TODO: need AutoPart Class for this operation
//        // if (!replacedParts.isEmpty()) {
//        //     List<String> partNames = new ArrayList<>();
//        //     for (AutoPart part : replacedParts) {
//        //         partNames.add(part.getPartName());
//        //     }
//        //     sb.append("Replaced Parts: ").append(String.join(", ", partNames)).append("\n");
//        // }
//
//        sb.append("Service Cost: $").append(String.format("%.2f", serviceCost)).append("\n");
//        if (!additionalNotes.isEmpty()) {
//            sb.append("Notes: ").append(additionalNotes).append("\n");
//        }
//        return sb.toString();
//    }

    public String getFormattedServiceDetails() {
        StringBuilder sb = new StringBuilder();

        sb.append("Service ID: ").append(serviceId).append("\n");
        sb.append("Service Date: ").append(serviceDate.format(DateTimeFormatter.ISO_LOCAL_DATE)).append("\n");
        sb.append("Client ID: ").append(clientId).append("\n");
        sb.append("Mechanic ID: ").append(mechanicId).append("\n");
        sb.append("Service Type: ").append(serviceType).append("\n");
        sb.append("Service By: ").append(serviceBy).append("\n");
        sb.append("Car ID: ").append(carId).append("\n");
        sb.append("Service Cost: $").append(String.format("%.2f", serviceCost)).append("\n");

        if (!replacedParts.isEmpty()) {
            List<String> partNames = replacedParts.stream()
                    .map(autoPart::getPartName)  // Assuming getPartName() returns the name of the part
                    .collect(Collectors.toList());
            sb.append("Replaced Parts: ").append(String.join(", ", partNames)).append("\n");
        } else {
            sb.append("Replaced Parts: None\n");
        }

        if (!additionalNotes.isEmpty()) {
            sb.append("Notes: ").append(additionalNotes).append("\n");
        }

        return sb.toString();
    }

}
