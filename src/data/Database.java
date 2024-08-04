package data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class Database {
    public static <T> void saveDatabase(String path, List<T> arrObject, String success, String error) throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(path);
             ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {
            objOut.writeObject(arrObject);
            System.out.println(success);
        } catch (IOException e) {
            throw new Exception(error);
        }
    }
}