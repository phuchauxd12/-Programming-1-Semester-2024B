package user;
import java.time.LocalDate;

public class Manager extends User {

    public Manager(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status) {
        super(userName, password, name, dob, address, phoneNum, email, userType, status);
    }

    public void addEntity() {
    }

    public void removeEntity() {
    }

    public void viewAllEntities() {
    }

    public void generateStatistics() {
    }
}