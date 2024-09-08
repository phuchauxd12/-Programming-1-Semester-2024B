package utils.menu;

import services.Service;
import services.ServiceList;
import user.Manager;
import user.Mechanic;
import user.User;
import utils.DatePrompt;
import utils.UserSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ServiceMenu extends Menu {


    // Constructor to initialize the menuList and menuActions
    public ServiceMenu() {
        super();
        switch (currentUser) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            case Mechanic m -> initializeMenu(MenuOption.MECHANIC);
            case null, default -> initializeMenu(null);
        }
    }

    @Override
    protected void initializeMenu(MenuOption menuOption) {
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "Display All Services");
                menuItems.put(2, "Display All Services In Specific Period");
                menuItems.put(3, "Display All Services By Specific Mechanic");

                menuActions.put(1, this::displayAllServices);
                menuActions.put(2, this::getAllServicesInSpecificPeriod);
                menuActions.put(3, this::getAllMechanicServices);
                menuActions.put(4, this::createServiceWrapper);
                menuActions.put(5, this::updateServiceWrapper);
                menuActions.put(6, this::searchServiceById);
                menuActions.put(7, this::deleteServiceWrapper);
                menuActions.put(0, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Display All Services by me");

                menuActions.put(1, this::displayAllServices);
                menuActions.put(4, this::createServiceWrapper);
                menuActions.put(5, this::updateServiceWrapper);
                menuActions.put(6, this::searchServiceById);
                menuActions.put(7, this::deleteServiceWrapper);
                menuActions.put(0, this::exit);
            }
            case null, default -> System.out.print("");
        }
        menuItems.put(4, "Create a Service");
        menuItems.put(5, "Update a Service");
        menuItems.put(6, "Search a service by ID");
        menuItems.put(7, "Delete a Service");
        menuItems.put(0, "Exit");

    }


    // Placeholder methods for menu actions

    private void createServiceWrapper() {
        try {
            createService();
            ActivityLogMenu.addActivityLogForCurrentUser("Create service wrapper");
        } catch (Exception e) {
            System.out.println("Error creating service: " + e.getMessage());
        }
    }

    private void updateServiceWrapper() {
//        ServiceList.services.stream().filter(service -> !service.isDeleted()).forEach(service -> System.out.println(service.getFormattedServiceDetails()));
        try {
            updateService();
            ActivityLogMenu.addActivityLogForCurrentUser("Update service wrapper");
        } catch (Exception e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    private void deleteServiceWrapper() {
//        ServiceList.services.stream().filter(service -> !service.isDeleted()).forEach(service -> System.out.println(service.getFormattedServiceDetails()));
        try {
            deleteService();
            ActivityLogMenu.addActivityLogForCurrentUser("Delete service wrapper");
        } catch (Exception e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
    }

    private void displayAllServices() {
        System.out.println("Displaying all services...");
        ServiceList.displayAllServices();

        try{
            ActivityLogMenu.addActivityLogForCurrentUser("Create service wrapper");
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void getAllServicesInSpecificPeriod() {
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);

        List<Service> services = ServiceList.getServicesBetween(startDate, endDate);

        if (services.isEmpty()) {
            System.out.println("No services found within the specified period.");
        } else {
            System.out.println("Services between " + startDate + " and " + endDate + ":");
            for (Service service : services) {
                System.out.println(service.getFormattedServiceDetails());
            }
        }

        try {
            String activityName = "View all services in a specific period";
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void getAllMechanicServices() {
        String activityName;
        List<Service> services;
        UserMenu.displayAllMechanics();
        String mechanicId;
        Mechanic mechanic;
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Enter mechanic ID: ");
            mechanicId = input.nextLine();
            if (!mechanicId.isEmpty()) {
                mechanic = (Mechanic) UserMenu.getUserById(mechanicId);
                if (mechanic != null) {
                    break;
                } else {
                    System.out.println("Mechanic not found. Please try again.");
                }
                System.out.println("Mechanic ID cannot be empty. Please try again.");
            }
        }
        activityName = "View all services made by Mechanic named " + mechanic.getUserName() + " with ID " + mechanic.getUserID();
        services = ServiceList.getServiceByMechanic(mechanicId);
        if (services.isEmpty()) {
            System.out.println("No services found for this mechanic.");
        } else {
            System.out.println("All available service of the mechanic : ");
            for (Service service : services) {
                System.out.println(service.getFormattedServiceDetails());
            }
        }
        try {
            ActivityLogMenu.addActivityLogForCurrentUser(activityName);
        } catch (Exception e) {
            System.out.println("Error logging statistic action history: " + e.getMessage());
        }
    }

    private void createService() throws Exception {
        System.out.println("Creating a new service...");
        ServiceList.addService();

        try{
            ActivityLogMenu.addActivityLogForCurrentUser("Create service wrapper");
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void updateService() throws Exception {
        System.out.println("Updating a service...");
        ServiceList.updateService();

        try{
            ActivityLogMenu.addActivityLogForCurrentUser("Update service");
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void deleteService() throws Exception {
        System.out.println("Deleting a service...");
        ServiceList.deleteService();

        try{
            ActivityLogMenu.addActivityLogForCurrentUser("Delete service");
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void searchServiceById() {
        ServiceList.services.stream().filter(service -> !service.isDeleted()).forEach(System.out::println);
        System.out.println("Searching service by ID...");
        Scanner input = new Scanner(System.in);
        System.out.println("Enter service ID: ");
        String serviceID = input.nextLine();
        User currentUser = UserSession.getCurrentUser();
        Service service = ServiceList.getServiceById(serviceID);
        if (currentUser.getRole() == User.ROLE.MANAGER) {
            if (service != null && !service.isDeleted()) {
                System.out.println("Service found!");
                System.out.println(ServiceList.getServiceById(serviceID).getFormattedServiceDetails());
            } else {
                System.out.println("No service found with ID: " + serviceID);
            }
        } else if (currentUser.getRole() == User.ROLE.EMPLOYEE) {
            if (currentUser instanceof Mechanic) {
                if (service != null && !service.isDeleted()) {
                    if (service.getMechanicId().equals(currentUser.getUserID())) {
                        System.out.println("Service found!");
                        System.out.println(ServiceList.getServiceById(serviceID).getFormattedServiceDetails());
                    } else {
                        System.out.println("You are not allow to access this transaction: " + serviceID);
                    }
                } else {
                    System.out.println("No service found with ID: " + serviceID);
                }
            }
        }

        try{
            ActivityLogMenu.addActivityLogForCurrentUser("Search service by ID: " + serviceID);
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

}