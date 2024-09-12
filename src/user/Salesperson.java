package user;

import java.time.LocalDate;


public class Salesperson extends Employee {

    public Salesperson(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType) throws Exception {
        super(userName, password, name, dob, address, phoneNum, email, userType);
    }

    @Override
    public String toString() {
        return "Salesperson{" +
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
                "} ";
    }
}
