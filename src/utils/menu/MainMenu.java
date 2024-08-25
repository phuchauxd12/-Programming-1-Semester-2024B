package utils.menu;

import user.Client;
import user.Manager;
import user.Mechanic;
import user.Salesperson;

public class MainMenu extends Menu {
    private final CarAndAutoPartMenu carAndAutoPartMenu= new CarAndAutoPartMenu();
    private final StatisticsMenu statisticsMenu= new StatisticsMenu();
    private final SaleTransactionMenu saleTransactionMenu= new SaleTransactionMenu();
    private final ServiceMenu serviceMenu = new ServiceMenu();
    private final ActivityLogMenu activityLogMenu= new ActivityLogMenu();
    private final UserMenu userMenu = new UserMenu();
    private final UserProfileMenu UserProfileMenu= new UserProfileMenu();

    public MainMenu() {
            super();
//        statisticsMenu = new StatisticsMenu(this);
//        saleTransactionMenu = new SaleTransactionMenu(this);
//        serviceMenu = new ServiceMenu(this);
//        activityLogMenu = new ActivityLogMenu(this);
//        userMenu = new UserMenu(this);
//        UserProfileMenu = new UserProfileMenu(this);
        switch (currentUser) {
            case Manager c -> initializeMenu(MenuOption.MANAGER);
            case Salesperson c -> initializeMenu(MenuOption.SALESPERSON);
            case Mechanic c -> initializeMenu(MenuOption.MECHANIC);
            case Client c -> initializeMenu(MenuOption.CLIENT);
            case null, default -> throw new IllegalArgumentException("Unsupported user type");
        }
    }

    @Override
    protected void initializeMenu(MenuOption menuOption) {
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Transaction Menu");
                menuItems.put(4, "Service Menu");
                menuItems.put(5, "Statistics Menu");
                menuItems.put(6, "Manage Users Menu");
                menuItems.put(7, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::transactionMenu);
                menuActions.put(4, this::serviceMenu);
                menuActions.put(5, this::statisticsMenu);
                menuActions.put(6, this::manageUsersMenu);
                menuActions.put(7, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
            case SALESPERSON -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Transaction Menu");
                menuItems.put(4, "Statistics Menu");
                menuItems.put(5, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::transactionMenu);
                menuActions.put(4, this::statisticsMenu);
                menuActions.put(5, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Service Menu");
                menuItems.put(4, "Statistics Menu");
                menuItems.put(5, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::serviceMenu);
                menuActions.put(4, this::statisticsMenu);
                menuActions.put(5, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
            case CLIENT -> {
                menuItems.put(1, "Manage Profile Menu");
                menuItems.put(2, "Car And Auto Part Menu");
                menuItems.put(3, "Statistics Menu");
                menuItems.put(4, "Activity Log Menu");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::manageProfileMenu);
                menuActions.put(2, this::carAndAutoPartMenu);
                menuActions.put(3, this::statisticsMenu);
                menuActions.put(4, this::activityLogMenu);
                menuActions.put(0, this::exit);
            }
        }
    }


    // Menu actions
    private void activityLogMenu() {
        try {
            activityLogMenu.displayMenu("Welcome to the Activity Log Menu");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Get the activity log menu
    }

    private void manageUsersMenu() {
        if (currentUser instanceof Manager) {
            try {
                userMenu.displayMenu("Welcome to the User Menu");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("You do not have permission to access this menu.");
        }
    }

    private void statisticsMenu() {
        try {

            statisticsMenu.displayMenu("Welcome to the Statistics Menu!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void serviceMenu() {
        try {
            serviceMenu.displayMenu("Welcome to the Service Menu");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void transactionMenu() {
        try {
            saleTransactionMenu.displayMenu("Welcome to the Sale Transaction Menu!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void carAndAutoPartMenu() {
        try {
            carAndAutoPartMenu.displayMenu("Welcome to the Car and Auto Part Menu!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void manageProfileMenu() {
        try {
            UserProfileMenu.displayMenu("Welcome to the User Profile Menu");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // menu functions

    public void mainMenu() {
        displayMenu("Welcome to the Main Menu!");
        System.exit(0);
    }


}
