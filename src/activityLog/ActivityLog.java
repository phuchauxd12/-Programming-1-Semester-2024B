package activityLog;

import data.Database;
import data.activityLog.ActivityLogDatabase;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ActivityLog implements Serializable {
    private String activityId;
    private String userId;
    private LocalDate date;
    private String activityName;

    public ActivityLog(String userId, LocalDate date, String activityName) {
        this.activityId = generateActivityId();
        this.userId = userId;
        this.date = date;
        this.activityName = activityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getActivityName() {
        return activityName;
    }

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

    public static void addActivityLog(String userId, LocalDate date, String activityName) throws Exception {
        ActivityLog newLog = new ActivityLog(userId, date, activityName);
        activityLogs.add(newLog);
        ActivityLogDatabase.saveActivityLogData(activityLogs);
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
        return new ArrayList<>(activityLogs);
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
            System.out.println("Date: " + log.getDate());
            System.out.println("Activity Name: " + log.getActivityName());
            System.out.println("---------------------------");
        }
    }

    private static String generateActivityId() {
        return "a-" + UUID.randomUUID().toString();
    }
}