package utils.menu;

import services.Service;
import services.ServiceList;
import user.Manager;
import user.Mechanic;
import user.User;
import utils.CommonFunc;
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

                menuActions.put(1, this::displayAllServices);
                menuActions.put(2, this::createServiceWrapper);
                menuActions.put(3, this::updateServiceWrapper);
                menuActions.put(4, this::searchServiceById);
                menuActions.put(5, this::searchServiceByDate); // TODO: Only able to search services by mechanic
                menuActions.put(6, this::deleteServiceWrapper); // TODO: Only able to delete services by mechanic
                menuActions.put(0, this::exit);
            }
            case null, default -> System.out.print("");
        }
        menuItems.put(2, "Create a Service");
        menuItems.put(3, "Update a Service");
        menuItems.put(4, "Search a service by ID");
        menuItems.put(5, "Search between specific dates");
        menuItems.put(6, "Delete a Service");
        menuItems.put(0, "Exit");

    }


    // Placeholder methods for menu actions

    private void createServiceWrapper() {
        try {
            createService();
            CommonFunc.addActivityLogForCurrentUser("Create service wrapper");
        } catch (Exception e) {
            System.out.println("Error creating service: " + e.getMessage());
        }
    }

    private void updateServiceWrapper() {
//        ServiceList.services.stream().filter(service -> !service.isDeleted()).forEach(service -> System.out.println(service.getFormattedServiceDetails()));
        try {
            updateService();
            CommonFunc.addActivityLogForCurrentUser("Update service wrapper");
        } catch (Exception e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
    }

    private void deleteServiceWrapper() {
//        ServiceList.services.stream().filter(service -> !service.isDeleted()).forEach(service -> System.out.println(service.getFormattedServiceDetails()));
        try {
            deleteService();
            CommonFunc.addActivityLogForCurrentUser("Delete service wrapper");
        } catch (Exception e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
    }

    private void displayAllServices() {
        System.out.println("Displaying all services...");
        ServiceList.displayAllServices();

        try{
            CommonFunc.addActivityLogForCurrentUser("Create service wrapper");
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void createService() throws Exception {
        System.out.println("Creating a new service...");
        if (UserSession.getCurrentUser().getRole() == User.ROLE.MANAGER) {
            System.out.println("Mechanic:");
            UserMenu.getUserList().stream().filter(user -> user instanceof Mechanic).forEach(System.out::println);
            Scanner input = new Scanner(System.in);
            System.out.println("Enter mechanic ID: ");
            String mechanicID = input.nextLine();

            boolean mechanicFound = false;
            for (User user : UserMenu.getUserList()) {
                if (user.getUserID().equals(mechanicID) && user instanceof Mechanic) {
                    ServiceList.addService(mechanicID);
                    mechanicFound = true;
                    break;
                }
            }
            if (!mechanicFound) {
                throw new Exception("Mechanic ID not found.");
            }
        } else {
            ServiceList.addService(UserSession.getCurrentUser().getUserName());
        }

        try{
            CommonFunc.addActivityLogForCurrentUser("Create service wrapper");
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void updateService() throws Exception {
        System.out.println("Updating a service...");
        ServiceList.updateService();

        try{
            CommonFunc.addActivityLogForCurrentUser("Update service");
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void deleteService() throws Exception {
        System.out.println("Deleting a service...");
        ServiceList.deleteService();

        try{
            CommonFunc.addActivityLogForCurrentUser("Delete service");
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
        if (currentUser.getRole() == User.ROLE.MANAGER){
            if (service != null && !service.isDeleted()) {
                System.out.println("Service found!");
                System.out.println(ServiceList.getServiceById(serviceID).getFormattedServiceDetails());
            }
            else {
                System.out.println("No service found with ID: " + serviceID);
            }
        } else if (currentUser.getRole() == User.ROLE.EMPLOYEE){
            if (currentUser instanceof Mechanic){
                if(service != null && !service.isDeleted()){
                    if(service.getMechanicId().equals(currentUser.getUserID())){
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
            CommonFunc.addActivityLogForCurrentUser("Search service by ID: " + serviceID);
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

    private void searchServiceByDate() {
        System.out.println("Searching service between ...");
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
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

        try{
            CommonFunc.addActivityLogForCurrentUser("Search service by date: " + startDate + " to " + endDate);
        } catch (Exception e) {
            System.out.println("Error logging service action history: " + e.getMessage());
        }
    }

}