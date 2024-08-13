package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class ServiceMenu {
    private HashSet<String> menuList = new HashSet<String>();
    private Map<Integer, Runnable> menuActions = new HashMap<>();

    // Constructor to initialize the menuList and menuActions
    public ServiceMenu() {
        menuList.add("Display All Services");
        menuList.add("Create a Service");
        menuList.add("Update a Service");
        menuList.add("Search a service by ID");
        menuList.add("Delete a Service");
        menuList.add("Exit");

        menuActions.put(1, this::displayAllServices);
        menuActions.put(2, this::createService);
        menuActions.put(3, this::updateService);
        menuActions.put(4, this::deleteService);
        menuActions.put(5, this::searchServiceById);
        menuActions.put(6, this::exit);
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

    public void mainMenu() {
        Scanner serviceMenuInput = new Scanner(System.in);
        int option = 0;
        while (option != 6) {
            displayMenu();
            option = Menu.getOption(option, serviceMenuInput);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to get the user's option

    // Placeholder methods for menu actions
    private void displayAllServices() {
        System.out.println("Displaying all services...");
        // Add logic to display all services
    }

    private void createService() {
        System.out.println("Creating a new service...");
        // Add logic to create a new service
    }

    private void updateService() {
        System.out.println("Updating a service...");
        // Add logic to update a service
    }

    private void deleteService() {
        System.out.println("Deleting a service...");
        // Add logic to delete a service
    }

    private void searchServiceById() {
        System.out.println("Searching service by ID...");
        // Add logic to search services
    }

    private void exit() {
        System.out.println("Exiting...");
    }
}