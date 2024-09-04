package user;


import services.Service;

import java.time.LocalDate;
import java.util.List;

public class Mechanic extends Employee {
    private List<Service> myServices;
    public Mechanic(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType) throws Exception {
        super(userName, password, name, dob, address, phoneNum, email, userType);
    }
}
