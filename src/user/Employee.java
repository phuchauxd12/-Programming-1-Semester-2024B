package user;

import java.io.Serializable;

public class Employee extends User implements Serializable {
    public Employee(String userName, String password, String name, String address, int phoneNum, String email, ROLE userType) {
        super(userName, password, name, address, phoneNum, email, userType);
    }

}
