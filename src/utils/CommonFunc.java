package utils;

import activityLog.ActivityLog;

public class CommonFunc {
    public static void addActivityLogForCurrentUser(String activityName) throws Exception {
        String userId = UserSession.getCurrentUser().getUserID();
        String username = UserSession.getCurrentUser().getUserName();
        ActivityLog.addActivityLog(userId, username, activityName);
    }
}
