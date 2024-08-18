package activityLog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ActivityLog {
    private String activityId;
    private String userId;
    private LocalDate date;
    private String activityName;

    public ActivityLog(String activityId, String userId, LocalDate date, String activityName) {
        this.activityId = activityId;
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

    private static List<ActivityLog> activityLogs = new ArrayList<>();

    public static void addActivityLog(String userId, LocalDate date, String activityName) {
        String activityId = generateActivityId();
        ActivityLog newLog = new ActivityLog(activityId, userId, date, activityName);
        activityLogs.add(newLog);
    }

    public static ActivityLog getActivityLog(String activityId) {
        for (ActivityLog log : activityLogs) {
            if (log.getActivityId().equals(activityId)) {
                return log;
            }
        }
        return null;
    }

    public static List<ActivityLog> getActivityLogByDate(LocalDate startDate, LocalDate endDate) {
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
        for (ActivityLog log : allLogs) {
            System.out.println("Activity ID: " + log.getActivityId());
            System.out.println("User ID: " + log.getUserId());
            System.out.println("Date: " + log.getDate());
            System.out.println("Activity Name: " + log.getActivityName());
            System.out.println("---------------------------");
        }
    }

    private static String generateActivityId() {
        return UUID.randomUUID().toString();
    }
}