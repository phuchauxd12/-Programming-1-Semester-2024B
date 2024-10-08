package services;

import autoPart.autoPart;
import car.Car;
import data.autoPart.AutoPartDatabase;
import data.car.CarDatabase;
import data.service.ServiceDatabase;
import data.user.UserDatabase;
import user.*;
import utils.CurrencyFormat;
import utils.DatePrompt;
import utils.Status;
import utils.UserSession;
import utils.menu.ActivityLogMenu;
import utils.menu.CarAndAutoPartMenu;
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
    private serviceType ServiceType;
    private String serviceTypeByOther;
    private List<autoPart> replacedParts;
    private ServiceBy serviceBy;
    private String carId;
    private double serviceCost;
    private double totalCost;
    private boolean isDeleted;
    private String additionalNotes;

    public enum Category {
        ROUTINE_MAINTENANCE,
        SYSTEM_REPAIR,
        BODY_AND_INTERIOR
    }

    public static enum serviceType {
        Oil_Change(Category.ROUTINE_MAINTENANCE, 1500000),
        Tire_Rotation(Category.ROUTINE_MAINTENANCE, 300000),
        Air_Filter(Category.ROUTINE_MAINTENANCE, 300000),
        General_Maintenance_L1(Category.ROUTINE_MAINTENANCE, 1800000),
        General_Maintenance_L2(Category.ROUTINE_MAINTENANCE, 2500000),
        General_Maintenance_L3(Category.ROUTINE_MAINTENANCE, 4000000),
        Brake_Service(Category.SYSTEM_REPAIR, 1300000),
        Transmission_Repair(Category.SYSTEM_REPAIR, 15000000),
        Steering_Repair(Category.SYSTEM_REPAIR, 3000000),
        Cooling_System_Repair(Category.SYSTEM_REPAIR, 2400000),
        Fuel_Injection_Repair(Category.SYSTEM_REPAIR, 3000000),
        Exhaust_System_Repair(Category.SYSTEM_REPAIR, 3000000),
        CarWash(Category.BODY_AND_INTERIOR, 300000),
        Paint_Job(Category.BODY_AND_INTERIOR, 2500000),
        Interior_Cleaning(Category.BODY_AND_INTERIOR, 800000),
        Seat_Upholstery_Replacement(Category.BODY_AND_INTERIOR, 3000000),
        Accessory_Installation(Category.BODY_AND_INTERIOR, 1200000);

        private final Category category;
        private final double price;

        serviceType(Category category, double price) {
            this.category = category;
            this.price = price;
        }

        public Category getCategory() {
            return category;
        }

        public double getPrice() {
            return price;
        }

        public static List<serviceType> getByCategory(Category category) {
            return Arrays.stream(values())
                    .filter(serviceType -> serviceType.getCategory() == category)
                    .collect(Collectors.toList());
        }

    }

    // Constructor
    public Service(LocalDate serviceDate, String clientId, String mechanicId, serviceType type, Set<String> partIds, ServiceBy serviceBy, String carId, double serviceCost, String notes) throws Exception {
        this.serviceId = generateServiceId();
        this.serviceDate = serviceDate;
        this.clientId = clientId;
        this.mechanicId = mechanicId;
        this.ServiceType = type;
        this.replacedParts = retrieveParts(partIds);
        this.serviceBy = serviceBy;
        this.carId = carId;
        this.serviceCost = serviceCost;
        this.totalCost = calculateTotalAmount(calculateDiscount(clientId));
        Client client = (Client) UserMenu.getUserById(clientId);
        client.updateTotalSpending(totalCost);
        this.additionalNotes = notes;
        this.isDeleted = false;

    }

    public static void addService(Service service) throws Exception {


        boolean carExists = false;
        for (Car car : CarAndAutoPartMenu.getCarsList()) {
            if (car.getCarID().equals(service.getCarId())) {
                carExists = true;
                break;
            }
        }

        if (carExists) {
            for (Car car : CarAndAutoPartMenu.getCarsList()) {
                if (car.getCarID().equals(service.getCarId())) {
                    car.addServiceToHistory(service);
                }
            }
        }

        if (!carExists) {
            Scanner input = new Scanner(System.in);
            Status status = Status.WALK_IN;
            System.out.println("Please input the car's make:");
            String carMake = input.nextLine();
            System.out.println("Please input the car's model:");
            String carModel = input.nextLine();
            int carYear = CarAndAutoPartMenu.getNewCarYear(input);
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
            newCar.setClientID(service.getClientId());
            try {
                service.setCarId(newCar.getCarID());
                CarAndAutoPartMenu.addCarToList(newCar);
                for (Car car : CarAndAutoPartMenu.getCarsList()) {
                    if (car.getCarID().equals(newCar.getCarID())) {
                        car.addServiceToHistory(service);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Car created successfully!");
        }

        ServiceList.services.add(service);
        if (!service.replacedParts.isEmpty()) {
            for (autoPart part : service.replacedParts) {
                for (autoPart parts : CarAndAutoPartMenu.getAutoPartsList()) {
                    if (parts.getPartID().equals(part.getPartID())) {
                        part.setStatus(Status.SOLD);
                        part.setSoldDate(service.getServiceDate());
                    }
                }
            }
        }

        CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
        UserDatabase.saveUsersData(UserMenu.getUserList());
        ServiceDatabase.saveService(ServiceList.services);
        AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
        System.out.println("Service added successfully!");
    }

    public static void updateService() throws Exception {
        Scanner scanner = new Scanner(System.in);
        User current = UserSession.getCurrentUser();

        System.out.print("Enter service ID to update: ");
        String serviceId = scanner.nextLine();

        Service service;
        if (current instanceof Manager) {
            service = ServiceList.getServiceById(serviceId);
        } else if (current instanceof Mechanic) {
            Service detectedService = ServiceList.getServiceById(serviceId);
            if (detectedService.getMechanicId().equals(current.getUserID())) {
                service = detectedService;
            } else {
                service = null;
            }
        } else {
            service = null;
        }
        if (service != null && !service.isDeleted()) {
            // Find the car associated with the service
            Car car = CarAndAutoPartMenu.getCarsList().stream()
                    .filter(c -> c.getCarID().equals(service.getCarId()))
                    .findFirst()
                    .orElse(null);
            if (car == null) {
                System.out.println("Car not found. Unable to update service.");
                return;
            }
            if (service.getServiceBy() == ServiceBy.AUTO136) {
                System.out.println("Which field would you like to update?");
                System.out.println("1. Service Date");
                System.out.println("2. Service Type");
                System.out.println("3. Car ID");
                System.out.println("4. Replaced Parts");
                System.out.println("5. Additional Notes");
                System.out.print("Enter the number corresponding to the field (or multiple numbers separated by space): ");
                String choiceInput = scanner.nextLine();
                List<String> choices = List.of(choiceInput.split("\\s+"));

                for (String choice : choices) {
                    switch (choice) {
                        case "1":
                            LocalDate newDate = DatePrompt.getDate("new service");
                            service.setServiceDate(newDate);
                            break;
                        case "2":
                            System.out.println("Available service types:");
                            for (int i = 0; i < serviceType.values().length; i++) {
                                System.out.println((i + 1) + ". " + serviceType.values()[i]);
                            }
                            System.out.print("Enter new service type (number): ");
                            int serviceTypeChoice = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            serviceType newServiceType = serviceType.values()[serviceTypeChoice - 1];
                            service.setServiceType(newServiceType);

                            // Update the cost based on the new service type
                            double newCost = newServiceType.getPrice();
                            service.setServiceCost(newCost);
                            //Update total cost
                            double previousTotalCost = service.getTotalCost();
                            double discount = service.calculateDiscount(service.getClientId());
                            double totalCost = service.calculateTotalAmount(discount);
                            service.setTotalCost(totalCost);
                            // Update client spending
                            Client user = (Client) UserMenu.getUserList().stream()
                                    .filter(u -> u.getUserID().equals(service.getClientId()))
                                    .findFirst()
                                    .orElse(null);
                            if (user != null) {
                                user.updateTotalSpending(totalCost - previousTotalCost);
                                UserDatabase.saveUsersData(UserMenu.getUserList());
                            } else {
                                System.out.println("Client not found");
                            }
                            break;
                        case "3":
                            CarAndAutoPartMenu.getCarsList().stream().filter(cars -> !cars.isDeleted() && cars.getClientID().equals(service.getClientId())).forEach(System.out::println);
                            System.out.print("Enter new car ID: ");
                            String newCarId = scanner.nextLine();

                            // Update the car ID and move the service history to the new car
                            Car oldCar = CarAndAutoPartMenu.findCarByID(service.getCarId());
                            Car newCar = CarAndAutoPartMenu.findCarByID(newCarId);


                            if (newCar != null) {
                                service.setCarId(newCarId);
                                newCar.addServiceToHistory(service);
                                oldCar.removeService(service.getServiceId());
                            } else {
                                System.out.println("Car not found");
                            }
                            break;
                        case "4":
                            // Mark old parts as available
                            for (autoPart oldPart : service.getReplacedParts()) {
                                for (autoPart parts : CarAndAutoPartMenu.getAutoPartsList()) {
                                    if (parts.getPartID().equals(oldPart.getPartID())) {
                                        parts.setStatus(Status.AVAILABLE);
                                    }
                                }
                            }
                            CarAndAutoPartMenu.getAutoPartsList().stream().filter(parts -> parts.getStatus() == Status.AVAILABLE).forEach(System.out::println);
                            // Get new parts
                            System.out.println("Enter new replaced parts (part IDs separated by comma, leave blank if none): ");
                            String partIdsInput = scanner.nextLine();
                            Set<String> partIds = partIdsInput.isEmpty() ? Collections.emptySet() : Arrays.stream(partIdsInput.split(","))
                                    .map(String::trim)
                                    .map(partId -> partId.replaceAll(" +", " "))  // Replace multiple spaces with a single space
                                    .collect(Collectors.toSet());

                            List<autoPart> newReplacedParts = service.retrieveParts(partIds);
                            service.setReplacedParts(newReplacedParts);

                            // Update total cost
                            double oldTotalCost = service.getTotalCost();
                            double newDiscount = service.calculateDiscount(service.getClientId());
                            double newTotalCost = service.calculateTotalAmount(newDiscount);
                            service.setTotalCost(newTotalCost);

                            // Mark new parts as sold
                            for (autoPart part : newReplacedParts) {
                                for (autoPart parts : CarAndAutoPartMenu.getAutoPartsList()) {
                                    if (parts.getPartID().equals(part.getPartID())) {
                                        parts.setStatus(Status.SOLD);
                                    }
                                }
                            }
                            AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());

                            // Update client spending
                            Client client = (Client) UserMenu.getUserList().stream()
                                    .filter(u -> u.getUserID().equals(service.getClientId()))
                                    .findFirst()
                                    .orElse(null);
                            if (client != null) {
                                client.updateTotalSpending(newTotalCost - oldTotalCost);
                                UserDatabase.saveUsersData(UserMenu.getUserList());
                            } else {
                                System.out.println("Client not found");
                            }
                            break;
                        case "5":
                            System.out.print("Enter additional notes (or leave blank): ");
                            String additionalNotes = scanner.nextLine();
                            service.setAdditionalNotes(additionalNotes);
                            break;
                        default:
                            System.out.println("Invalid choice: " + choice);
                    }
                }
            } else if (service.getServiceBy() == ServiceBy.OTHER) {
                System.out.println("Which field would you like to update?");
                System.out.println("1. Service Date");
                System.out.println("2. Service Type");
                System.out.println("3. Car ID");
                System.out.println("5. Additional Notes");
                System.out.print("Enter the number corresponding to the field (or multiple numbers separated by space): ");
                String choiceInput = scanner.nextLine();
                List<String> choices = List.of(choiceInput.split("\\s+"));

                for (String choice : choices) {
                    switch (choice) {
                        case "1":
                            System.out.print("Enter new service date (dd/MM/yyyy): ");
                            String input = DatePrompt.sanitizeDateInput(scanner.nextLine());
                            LocalDate newDate = DatePrompt.validateAndParseDate(input);
                            service.setServiceDate(newDate);
                            break;
                        case "2":
                            System.out.println("Enter new service types:");
                            String serviceTypes = scanner.nextLine();
                            service.setServiceTypeByOther(serviceTypes);
                            break;
                        case "3":
                            System.out.print("Enter new car ID: ");
                            String newCarId = scanner.nextLine();

                            // Update the car ID and move the service history to the new car
                            Car oldCar = CarAndAutoPartMenu.findCarByID(service.getCarId());
                            Car newCar = CarAndAutoPartMenu.findCarByID(newCarId);

                            if (newCar != null) {
                                service.setCarId(newCarId);
                                newCar.addServiceToHistory(service);
                                oldCar.removeService(service.getServiceId());
                            } else {
                                System.out.println("Car not found");
                            }

                            break;
                        case "5":
                            System.out.print("Enter additional notes (or leave blank): ");
                            String additionalNotes = scanner.nextLine();
                            service.setAdditionalNotes(additionalNotes);
                            break;
                        default:
                            System.out.println("Invalid choice: " + choice);
                    }
                }
            }

            //Update the service in the car's service history
            List<Service> serviceHistory = car.getServiceHistory();
            for (int i = 0; i < serviceHistory.size(); i++) {
                if (serviceHistory.get(i).getServiceId().equals(service.getServiceId())) {
                    serviceHistory.set(i, service);
                    break;
                }
            }

            CarDatabase.saveCarData(CarAndAutoPartMenu.getCarsList());
            ServiceDatabase.saveService(ServiceList.services);
            AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());

            // Display updated service details
            System.out.println("Service updated successfully:");
            ActivityLogMenu.addActivityLogForCurrentUser("Updated service with ID: " + service.getServiceId());
            System.out.println(service.getFormattedServiceDetails());
        } else {
            System.out.println("Service not found or it has been deleted.");
        }
    }


    public static void deleteService() throws Exception {
        Scanner scanner = new Scanner(System.in);
        User current = UserSession.getCurrentUser();

        System.out.print("Enter service ID to delete: ");
        String serviceId = scanner.nextLine();

        Service service;
        if (current instanceof Manager) {
            service = ServiceList.getServiceById(serviceId);
        } else if (current instanceof Mechanic) {
            Service detectedService = ServiceList.getServiceById(serviceId);
            assert detectedService != null;
            if (detectedService.getMechanicId() != null && detectedService.getMechanicId().equals(current.getUserID())) {
                service = detectedService;
            } else {
                service = null;
                System.out.println("You are not allow to access this service");
            }
        } else {
            service = null;
        }

        if (service != null) {
            //Find the car associated with the service
            Car car = CarAndAutoPartMenu.getCarsList().stream()
                    .filter(c -> c.getCarID().equals(service.getCarId()))
                    .findFirst()
                    .orElse(null);

            if (car == null) {
                System.out.println("Car not found. Unable to delete service.");
                return;
            }

            //Remove the service from the car's service history
            car.removeService(serviceId);

            service.markAsDeleted();
            // Update client total spending
            Client client = (Client) UserMenu.getUserList().stream()
                    .filter(u -> u.getUserID().equals(service.getClientId()))
                    .findFirst()
                    .orElse(null);
            if (client != null) {
                client.updateTotalSpending(-service.getTotalCost());
                UserDatabase.saveUsersData(UserMenu.getUserList());

            } else {
                System.out.println("Client not found");
            }
            for (autoPart part : service.getReplacedParts()) {
                for (autoPart parts : CarAndAutoPartMenu.getAutoPartsList()) {
                    if (parts.getPartID().equals(part.getPartID())) {
                        parts.setStatus(Status.AVAILABLE);
                    }
                }
            }
            AutoPartDatabase.saveAutoPartData(CarAndAutoPartMenu.getAutoPartsList());
            ServiceDatabase.saveService(ServiceList.services);
            System.out.println("Service marked as deleted.");
            try {
                ActivityLogMenu.addActivityLogForCurrentUser("Deleted service with ID: " + service.getServiceId());
            } catch (Exception e) {
                System.out.println("Error logging service action history: " + e.getMessage());
            }
        } else {
            System.out.println("Service not found.");
        }
    }

    List<autoPart> retrieveParts(Set<String> partIds) throws Exception {
        List<autoPart> parts = new ArrayList<>(); // check if we have the function to add the autoPart to the list or not

        if (partIds == null) {
            return parts;
        }

        for (String partId : partIds) {
            Optional<autoPart> partOpt = CarAndAutoPartMenu.getAutoPartsList().stream()
                    .filter(part -> part.getPartID().equalsIgnoreCase(partId) && part.getStatus() == Status.AVAILABLE)
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

    private double calculateSubTotal(List<autoPart> replacedParts, double serviceCost) {
        double total = 0;
        for (autoPart part : replacedParts) {
            total += part.getPrice();
        }
        return (total + serviceCost);
    }

    private double calculateTotalAmount(double discount) {
        return calculateSubTotal(replacedParts, serviceCost) * (1 - discount);
    }

    private double getDiscountAmount(double discount) {
        return calculateSubTotal(replacedParts, serviceCost) * discount;
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

    public serviceType getServiceType() {
        return ServiceType;
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

    public String getServiceTypeByOther() {
        return serviceTypeByOther;
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

    public void setServiceType(serviceType type) {
        this.ServiceType = type;
    }

    public void setServiceTypeByOther(String type) {
        this.serviceTypeByOther = type;
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
        Car car = CarAndAutoPartMenu.findCarByID(carId);
        sb.append("Service ID: ").append(serviceId).append("\n");
        sb.append("Service Date: ").append(serviceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        sb.append("Car: ").append(car.getCarMake()).append(" ").append(car.getCarModel()).append(" ").append(car.getCarYear()).append("\n");
        sb.append("Client: ").append(UserMenu.getUserById(clientId).getName()).append("\n");
        if (serviceBy == ServiceBy.AUTO136) {
            sb.append("Service By: AUTO136 \n");
            sb.append("Mechanic: ").append(UserMenu.getUserById(mechanicId).getName()).append("\n");
            sb.append("Service Type: ").append(ServiceType).append("\n");
            sb.append("Service Cost: ").append(CurrencyFormat.format(serviceCost)).append("\n");
            if (!replacedParts.isEmpty()) {
                List<String> partNames = replacedParts.stream()
                        .map(autoPart::getPartName)  // Assuming getPartName() returns the name of the part
                        .collect(Collectors.toList());
                sb.append("Replaced Parts: ").append(String.join(", ", partNames)).append("\n");
            } else {
                sb.append("Replaced Parts: None\n");
            }
            sb.append("Subtotal: ").append(CurrencyFormat.format(calculateSubTotal(replacedParts, serviceCost))).append("\n");
            sb.append("Discount: ").append(CurrencyFormat.format(getDiscountAmount(calculateDiscount(clientId)))).append("\n");
            sb.append("Total Cost: ").append(CurrencyFormat.format(totalCost)).append("\n");
        } else {
            sb.append("Service By: OTHER \n");
            sb.append("Service Type: ").append(serviceTypeByOther).append("\n");
        }

        if (additionalNotes != null && !additionalNotes.isEmpty()) {
            sb.append("Notes: ").append(additionalNotes).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceDate=" + serviceDate +
                ", clientId='" + clientId + '\'' +
                ", mechanicId='" + mechanicId + '\'' +
                '}';
    }
}
