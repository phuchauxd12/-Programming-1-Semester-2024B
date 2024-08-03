package user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class User implements Serializable {
    protected String userID;
    protected String userName;
    protected String password;
    protected String name;
    protected LocalDate dob;
    protected String address;
    protected int phoneNum;
    protected String email;
    protected ROLE userType;
    protected String status;

    protected static long userCounter =0;
    public User() {

    }

    public static enum ROLE {
        MANAGER,
        EMPLOYEE,
        CLIENT,
        ADMIN
    }

    public User( String userName, String password, String name,String address, int phoneNum, String email, ROLE userType) {
        this.userID = generateUserId();
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.dob = LocalDate.now();
        this.address = address;
        this.phoneNum = phoneNum;
        this.email = email;
        this.userType = userType;
    }

    private String generateUserId() {
        return "u-" + UUID.randomUUID().toString();
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", phoneNum=" + phoneNum +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                ", status='" + status + '\'' +
                '}';
    }
}
