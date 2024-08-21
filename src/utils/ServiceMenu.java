package utils;

import services.Service;
import services.ServiceList;
import user.User;

import java.time.LocalDate;
import java.util.*;

public class ServiceMenu {
    private static HashSet<String> menuList = new HashSet<String>();
    private static Map<Integer, Runnable> menuActions = new HashMap<>();


    // Constructor to initialize the menuList and menuActions
    public ServiceMenu() {
        menuList.add("Display All Services");
        menuList.add("Create a Service");
        menuList.add("Update a Service");
        menuList.add("Search a service by ID");
        menuList.add("Search between specific dates");
        menuList.add("Delete a Service");
        menuList.add("Exit");

        menuActions.put(1, this::displayAllServices);
        menuActions.put(2, this::createServiceWrapper);
        menuActions.put(3, this::updateServiceWrapper);
        menuActions.put(4, this::searchServiceById);
        menuActions.put(5, this::searchServiceByDate);
        menuActions.put(6, this::deleteServiceWrapper);
        menuActions.put(7, this::exit);
    }

    // Method to display the menu
    public void displayMenu() {
        int currentIndex = 1;
        System.out.println("This is the Service Menu.");
        for (String menuItem : menuList) {
            System.out.printf("%d  %s \n", currentIndex, menuItem);
            currentIndex++;
        }
    }

    public void mainMenu(User user, Menu mainMenu) throws Exception {
        Scanner serviceMenuInput = new Scanner(System.in);
        int option = 0;
        while (option != 7) {
            displayMenu();
            option = Menu.getOption(option, serviceMenuInput);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        mainMenu.mainMenu();
    }

    // Method to get the user's option

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