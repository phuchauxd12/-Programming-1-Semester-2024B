package utils.menu;

import activityLog.ActivityLog;
import data.Database;
import data.activityLog.ActivityLogDatabase;
import user.Manager;
import utils.DatePrompt;
import utils.UserSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ActivityLogMenu extends Menu {
    private static List<ActivityLog> activityLogs;

    static {
        try {
            if (!Database.isDatabaseExist(ActivityLogDatabase.path)) {
                ActivityLogDatabase.createDatabase();
            }
            ;
            activityLogs = ActivityLogDatabase.loadActivityLogs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addActivityLog(String userId, String username, String activityName) throws Exception {
        ActivityLog newLog = new ActivityLog(userId, username, LocalDate.now(), activityName);
        activityLogs.add(newLog);
        ActivityLogDatabase.saveActivityLogData(activityLogs);
    }

    public static void addActivityLogForCurrentUser(String activityName) throws Exception {
        String userId = UserSession.getCurrentUser().getUserID();
        String username = UserSession.getCurrentUser().getUserName();
        addActivityLog(userId, username, activityName);
    }

    public static ActivityLog getActivityLog(String activityId) {
        for (ActivityLog log : activityLogs) {
            if (log.getActivityId().equals(activityId)) {
                return log;
            }
        }
        return null;
    }

    public List<ActivityLog> getActivityLogByDate(LocalDate startDate, LocalDate endDate) {
        return activityLogs.stream()
                .filter(log -> (log.getDate().isEqual(startDate) || log.getDate().isAfter(startDate)) &&
                        (log.getDate().isEqual(endDate) || log.getDate().isBefore(endDate)))
                .collect(Collectors.toList());
    }

    public static List<ActivityLog> viewMyActivityLog(String userId) {
        return activityLogs.stream()
                .filter(log -> log.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public static List<ActivityLog> getAllActivityLog() {
        System.out.println(activityLogs);
        return activityLogs;
    }

    public static void viewAllActivityLog() {
        List<ActivityLog> allLogs = getAllActivityLog();
        System.out.println("All Activity Logs:");
        displayLogs(allLogs);
    }

    public static void displayLogs(List<ActivityLog> allLogs) {
        for (ActivityLog log : allLogs) {
            System.out.println("Activity ID: " + log.getActivityId());
            System.out.println("User ID: " + log.getUserId());
            System.out.println("Username: " + log.getUsername());
            System.out.println("Date: " + log.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Activity Name: " + log.getActivityName());
            System.out.println("---------------------------");
        }
    }


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
        viewAllActivityLog();

        try {
            addActivityLogForCurrentUser("Viewed all activity logs");
        } catch (Exception e) {
            System.out.println("Error logging activity log action history: " + e.getMessage());
        }

    }

    private void getActivityLogById() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Activity ID: ");
        String activityId = scanner.next();
        ActivityLog log = getActivityLog(activityId);
        if (log != null) {
            ActivityLog.displayLogs(List.of(log));
        } else {
            System.out.println("No activity log found with the given ID.");
        }

        try {
            addActivityLogForCurrentUser("Searched for activity log with ID: " + activityId);
        } catch (Exception e) {
            System.out.println("Error logging activity log action history: " + e.getMessage());
        }
    }

    private void viewMyActivityLogs() {

        String userId = currentUser.getUserID();
        List<ActivityLog> userLogs = viewMyActivityLog(userId);
        if (!userLogs.isEmpty()) {
            ActivityLog.displayLogs(userLogs);
        } else {
            System.out.println("No activity logs found for the given User ID.");
        }

        try {
            addActivityLogForCurrentUser("Viewed all my activity logs");
        } catch (Exception e) {
            System.out.println("Error logging activity log action history: " + e.getMessage());
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
        List<ActivityLog> userLogs = viewMyActivityLog(userId);
        if (!userLogs.isEmpty()) {
            ActivityLog.displayLogs(userLogs);
        } else {
            System.out.println("No activity logs found for the given User ID.");
        }

        try {
            addActivityLogForCurrentUser("Viewed all activity logs of user with ID: " + userId);
        } catch (Exception e) {
            System.out.println("Error logging activity log action history: " + e.getMessage());
        }
    }

    private void filterActivityLogByDate() {
        List<ActivityLog> eligibleLogs;
        String userId = currentUser.getUserID();
        if (currentUser instanceof Manager) {
            eligibleLogs = getAllActivityLog();
        } else {
            eligibleLogs = viewMyActivityLog(userId);
        }
        LocalDate startDate = DatePrompt.getDate("start");
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

        try {
            addActivityLogForCurrentUser("Viewed filtered activity logs from" + startDate + " to " + endDate);
        } catch (Exception e) {
            System.out.println("Error logging activity log action history: " + e.getMessage());
        }
    }


}