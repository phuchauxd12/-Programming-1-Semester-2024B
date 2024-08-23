package utils;

import services.Service;
import services.ServiceList;
import user.*;

import java.time.LocalDate;
import java.util.*;

import static utils.Menu.getOption;

public class ServiceMenu {
    private static final Scanner input = new Scanner(System.in);
    private final Map<Integer, String> menuItems = new LinkedHashMap<>();
    private final Map<Integer, Runnable> menuActions = new LinkedHashMap<>();


    // Constructor to initialize the menuList and menuActions
    public ServiceMenu(User user) {
        switch (user) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            case Mechanic m -> initializeMenu(MenuOption.MECHANIC);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    private void initializeMenu(MenuOption menuOption) {
        menuItems.put(2, "Create a Service");
        menuItems.put(3, "Update a Service");
        menuItems.put(4, "Search a service by ID");
        menuItems.put(5, "Search between specific dates");
        menuItems.put(6, "Delete a Service");
        menuItems.put(0, "Exit");
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "Display All Services");

                menuActions.put(1, this::displayAllServices);
                menuActions.put(2, this::createServiceWrapper);
                menuActions.put(3, this::updateServiceWrapper);
                menuActions.put(4, this::searchServiceById);
                menuActions.put(5, this::searchServiceByDate);
                menuActions.put(6, this::deleteServiceWrapper);
                menuActions.put(0, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Display All Services by me");

                menuActions.put(1, this::displayAllServices); // TODO: Display all services by mechanic
                menuActions.put(2, this::createServiceWrapper);
                menuActions.put(3, this::updateServiceWrapper); // TODO: Only able to update services by mechanic
                menuActions.put(4, this::searchServiceById); // TODO: Only able to display services by mechanic
                menuActions.put(5, this::searchServiceByDate); // TODO: Only able to search services by mechanic
                menuActions.put(6, this::deleteServiceWrapper); // TODO: Only able to delete services by mechanic
                menuActions.put(0, this::exit);
            }
        }
    }

    // Method to display the menu
    public void displayMenu() {
        System.out.println("Welcome to the Service Menu!");
        System.out.println("---------------------");
        menuItems.forEach((key, value) -> System.out.println(key + ". " + value));
        System.out.println("---------------------");
    }

    public void mainMenu(Menu mainMenu) {
        int option = 100;
        do {
            displayMenu();
            option = getOption(option, input);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        } while (option != 0);
        mainMenu.mainMenu();
    }

    // Placeholder methods for menu actions

    private void createServiceWrapper() {
        try {
            createService();
        } catch (Exception e) {
            System.out.println("Error creating service: " + e.getMessage());
        }
    }

    private void updateServiceWrapper() {
        try {
            updateService();
        } catch (Exception e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    private void deleteServiceWrapper() {
        try {
            deleteService();
        } catch (Exception e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
    }

    private void displayAllServices() {
        System.out.println("Displaying all services...");
        ServiceList.displayAllServices();
    }

    private void createService() throws Exception {
        System.out.println("Creating a new service...");
        if (UserSession.getCurrentUser().getRole() == User.ROLE.MANAGER) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter mechanic ID: ");
            String mechanicID = input.nextLine();
            ServiceList.addService(mechanicID);
        } else {
            ServiceList.addService(UserSession.getCurrentUser().getUserName());
        }
    }

    private void updateService() throws Exception {
        System.out.println("Updating a service...");
        ServiceList.updateService();
    }

    private void deleteService() throws Exception {
        System.out.println("Deleting a service...");
        ServiceList.deleteService();
    }

    private void searchServiceById() {
        System.out.println("Searching service by ID...");
        Scanner input = new Scanner(System.in);
        System.out.println("Enter service ID: ");
        String serviceID = input.nextLine();
        if (ServiceList.getServiceById(serviceID) != null) {
            System.out.println("Service found!");
            System.out.println(ServiceList.getServiceById(serviceID).getFormattedServiceDetails());
        }
        System.out.println("No service found with ID: " + serviceID);
    }

    private void searchServiceByDate() {
        System.out.println("Searching service between ...");
        LocalDate startDate = Menu.getStartDate();
        LocalDate endDate = Menu.getEndDate(startDate);
        List<Service> servicesBetween = ServiceList.getServicesBetween(startDate, endDate);
        if (!servicesBetween.isEmpty()) {
            System.out.println("Service found!");
            for (Service service : servicesBetween) {
                if (!service.isDeleted()) {
                    System.out.println(service.getFormattedServiceDetails());
                }
            }
        }
        System.out.println("Non service done between : " + startDate + " to " + endDate);
    }

    private void exit() {
        System.out.println("Exiting...");
    }
}