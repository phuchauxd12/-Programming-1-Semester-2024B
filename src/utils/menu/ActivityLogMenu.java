package utils.menu;

import activityLog.ActivityLog;
import utils.DatePrompt;
import user.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ActivityLogMenu extends Menu {


    public ActivityLogMenu() {
        super();
        switch (currentUser) {
            case Manager m -> initializeMenu(MenuOption.MANAGER);
            // All other users have the same menu options
            case null -> throw new IllegalArgumentException("Unsupported user type");
            default -> initializeMenu(MenuOption.MECHANIC);
        }
    }

    @Override
    protected void initializeMenu(MenuOption menuOption) {
        switch (menuOption) {
            case MANAGER -> {
                menuItems.put(1, "View all Activity Logs");
                menuItems.put(2, "View all Activity Logs by User ID");
                menuItems.put(3, "Filter Activity Log by date");
                menuItems.put(4, "View all my Activity Logs");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::viewAllActivityLogs);
                menuActions.put(2, this::viewActivityLogsByUserID);
                menuActions.put(3, this::filterActivityLogByDate);
                menuActions.put(4, this::viewMyActivityLogs);
                menuActions.put(0, this::exit);
            }
            case MECHANIC -> {
                menuItems.put(1, "View all my Activity Logs");
                menuItems.put(2, "Filter Activity Log by date");
                menuItems.put(0, "Exit");

                menuActions.put(1, this::viewMyActivityLogs);
                menuActions.put(2, this::filterActivityLogByDate);
                menuActions.put(0, this::exit);
            }
        }
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
            ActivityLog.displayLogs(List.of(log));
        } else {
            System.out.println("No activity log found with the given ID.");
        }
    }

    private void viewMyActivityLogs() {

        String userId = currentUser.getUserID();
        List<ActivityLog> userLogs = ActivityLog.viewMyActivityLog(userId);
        if (!userLogs.isEmpty()) {
            ActivityLog.displayLogs(userLogs);
        } else {
            System.out.println("No activity logs found for the given User ID.");
        }
    }

    private void viewActivityLogsByUserID() {
        Scanner scanner = new Scanner(System.in);
        String userId;
        System.out.print("Enter User ID: ");
        userId = scanner.next();
        if (currentUser instanceof Manager) {
            System.out.print("Enter User ID: ");
            userId = scanner.next();
        } else {
            userId = currentUser.getUserID();
        }
        List<ActivityLog> userLogs = ActivityLog.viewMyActivityLog(userId);
        if (!userLogs.isEmpty()) {
            ActivityLog.displayLogs(userLogs);
        } else {
            System.out.println("No activity logs found for the given User ID.");
        }
    }

    private void filterActivityLogByDate() {
        List<ActivityLog> eligibleLogs;
        String userId = currentUser.getUserID();
        if (currentUser instanceof Manager) {
            eligibleLogs = ActivityLog.getAllActivityLog();
        } else {
            eligibleLogs = ActivityLog.viewMyActivityLog(userId);
        }
        LocalDate startDate = DatePrompt.getStartDate();
        LocalDate endDate = DatePrompt.getEndDate(startDate);
        List<ActivityLog> logsByDate = eligibleLogs.stream()
                .filter(log -> (log.getDate().isEqual(startDate) || log.getDate().isAfter(startDate)) &&
                        (log.getDate().isEqual(endDate) || log.getDate().isBefore(endDate)))
                .toList();
        if (!logsByDate.isEmpty()) {
            ActivityLog.displayLogs(logsByDate);
        } else {
            System.out.println("No activity logs found in the given date range.");
        }
    }


}