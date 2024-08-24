package utils;

import activityLog.ActivityLog;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ActivityLogMenu {
    private HashSet<String> menuList = new HashSet<>();
    private Map<Integer, Runnable> menuActions = new HashMap<>();

    public ActivityLogMenu() {
        menuList.add("View all Activity Logs");
        menuList.add("Get an Activity Log");
        menuList.add("View all my Activity Logs");
        menuList.add("Filter Activity Log by date");
        menuList.add("Exit");

        menuActions.put(1, this::viewAllActivityLogs);
        menuActions.put(2, this::getActivityLogById);
        menuActions.put(3, this::viewMyActivityLogs);
        menuActions.put(4, this::filterActivityLogByDate);
        menuActions.put(5, this::exit);
    }

    private void viewAllActivityLogs() {
        ActivityLog.viewAllActivityLog();
    }

    private void getActivityLogById() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Activity ID: ");
        String activityId = scanner.next();
        ActivityLog log = ActivityLog.getActivityLog(activityId);
        if (log != null) {
            System.out.println("Activity ID: " + log.getActivityId());
            System.out.println("User ID: " + log.getUserId());
            System.out.println("Date: " + log.getDate());
            System.out.println("Activity Name: " + log.getActivityName());
        } else {
            System.out.println("No activity log found with the given ID.");
        }
    }

    private void viewMyActivityLogs() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your User ID: ");
        String userId = scanner.next();
        List<ActivityLog> userLogs = ActivityLog.viewMyActivityLog(userId);
        if (!userLogs.isEmpty()) {
            for (ActivityLog log : userLogs) {
                System.out.println("Activity ID: " + log.getActivityId());
                System.out.println("Date: " + log.getDate());
                System.out.println("Activity Name: " + log.getActivityName());
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("No activity logs found for the given User ID.");
        }
    }

    private void filterActivityLogByDate() {
        System.out.println("Enter the start date:");
        LocalDate startDate = Menu.getStartDate();
        System.out.println("Enter the end date:");
        LocalDate endDate = Menu.getStartDate();

        List<ActivityLog> logsByDate = ActivityLog.getActivityLogByDate(startDate, endDate);
        if (!logsByDate.isEmpty()) {
            for (ActivityLog log : logsByDate) {
                System.out.println("Activity ID: " + log.getActivityId());
                System.out.println("User ID: " + log.getUserId());
                System.out.println("Date: " + log.getDate());
                System.out.println("Activity Name: " + log.getActivityName());
                System.out.println("---------------------------");
            }
        } else {
            System.out.println("No activity logs found in the given date range.");
        }
    }

    private void exit() {
        System.out.println("Exiting...");
    }

    public void displayMenu() {
        int currentIndex = 1;

        System.out.println("This is Activity Log Menu. Please select an option:");
        for (String menuItem : menuList) {
            System.out.printf("%d  %s \n", currentIndex, menuItem);
            currentIndex++;
        }
    }

    private int getOption(Scanner input) {
        System.out.print("Enter your choice: ");
        while (!input.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            input.next();
        }
        return input.nextInt();
    }

    public void mainMenu() {
        Scanner input = new Scanner(System.in);
        int option = 0;
        while (option != 5) { // Assuming 5 is the exit option
            displayMenu();
            option = getOption(input);
            Runnable action = menuActions.get(option);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        ActivityLogMenu menu = new ActivityLogMenu();
        menu.mainMenu();
    }
}