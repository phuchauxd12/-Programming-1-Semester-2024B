package data.activityLog;

import activityLog.ActivityLog;
import data.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDatabase {
    public static final String path = "src/data/activityLog/ActivityLog.txt";
    private static List<activityLog.ActivityLog> activityLogList;


    public static void createDatabase() throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            activityLogList = new ArrayList<>();
            Database.<ActivityLog>saveDatabase(path, activityLogList, "Database file created", "Error while creating the database file.");
        } else {
            System.out.println("Database file already exists at " + path);
        }
    }

    // Method to load the list of users from the file
    public static List<ActivityLog> loadActivityLogs() throws Exception {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            activityLogList = (List<ActivityLog>) objIn.readObject();
            return activityLogList;
        } catch (IOException | ClassNotFoundException e) {
            throw new Exception("Error while loading activity logs from the database file.");
        }
    }


    public  static  void saveActivityLogData(List<ActivityLog> dataList) throws Exception {
        Database.<ActivityLog>saveDatabase(path, dataList, "Activity Log had been updated in the database.", "Error while updating the database file.");
    }
}
