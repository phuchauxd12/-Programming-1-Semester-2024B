package activityLog;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ActivityLog implements Serializable {
    private String activityId;
    private String userId;
    private String username;
    private LocalDate date;
    private String activityName;

    public ActivityLog(String userId, String username, LocalDate date, String activityName) {
        this.activityId = generateActivityId();
        this.userId = userId;
        this.username = username;
        this.date = date;
        this.activityName = activityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getActivityName() {
        return activityName;
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

    private static String generateActivityId() {
        return "a-" + UUID.randomUUID().toString();
    }
}