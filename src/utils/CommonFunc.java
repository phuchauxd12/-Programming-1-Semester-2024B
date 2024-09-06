package utils;

import activityLog.ActivityLog;
import data.Database;
import data.activityLog.ActivityLogDatabase;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CommonFunc {
    private static List<ActivityLog> activityLogs;

    static {
        try {
            if (!Database.isDatabaseExist(ActivityLogDatabase.path)) {
                ActivityLogDatabase.createDatabase();
            };
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


    private static String generateActivityId() {
        return "a-" + UUID.randomUUID().toString();
    }
}
