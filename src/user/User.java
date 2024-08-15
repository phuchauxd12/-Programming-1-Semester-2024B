package user;

import data.Database;
import data.user.UserDatabase;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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


    public User() {

    }

    public static enum ROLE {
        MANAGER,
        EMPLOYEE,
        CLIENT,
        ADMIN
    }

    public User(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status) throws Exception {
        this.userID = generateUserId();
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.phoneNum = phoneNum;
        this.email = email;
        this.userType = userType;
        this.status = status;
    }

    private String generateUserId() {
        return "u-" + UUID.randomUUID().toString();
    }

    public ROLE getRole() {
        return this.userType;
    }

    public void viewProfile() {
        System.out.println("Viewing profile for user: " + this.userName);
    }

    public void modifyProfile(int option, String newValue) throws Exception {
        switch (option) {
            case 1:
                this.setUserName(newValue);
                break;
            case 2:
                this.setPassword(newValue);
                break;
            case 3:
                this.setName(newValue);
                break;
            case 4:
                this.setAddress(newValue);
                break;
            case 5:
                this.setPhoneNum(Integer.parseInt(newValue));
                break;
            case 6:
                this.setEmail(newValue);
                break;
            case 7:
                this.setDob(LocalDate.parse(newValue));

                break;

            default:
                System.out.println("Invalid field specified.");
                return;
        };
        UserDatabase.saveUsersData(userList);

    }

    // Static methods for managing users
    public static List<User> userList;
    // This code run one time when create an instance of a class
    static {
        try {
            if(!Database.isDatabaseExist(UserDatabase.path)){
                UserDatabase.createDatabase();
            };
            userList = UserDatabase.loadUsers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void addUser(User user) throws Exception {
        userList.add(user);
        UserDatabase.saveUsersData(userList);
        System.out.println("User added: " + user.getUserName());
    }

    public static User getUserById(String userId) {
        for (User user : userList) {
            if (user.getUserID().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public static List<Mechanic> getAllMechanics() {
        List<Mechanic> mechanics = new ArrayList<>();
        for (User user : userList) {
            if (user instanceof Mechanic) {
                mechanics.add((Mechanic) user);
            }
        }
        return mechanics;
    }

    public static String displayAllMechanics() {
        List<Mechanic> mechanics = getAllMechanics();
        StringBuilder mechanicList = new StringBuilder();
        for (Mechanic mechanic : mechanics) {
            mechanicList.append(mechanic.toString()).append("\n");
        }
        return mechanicList.toString();
    }

    public static List<Salesperson> getAllSalespersons() {
        List<Salesperson> salespersons = new ArrayList<>();
        for (User user : userList) {
            if (user instanceof Salesperson) {
                salespersons.add((Salesperson) user);
            }
        }
        return salespersons;
    }

    public static String displayAllSalespersons() {
        List<Salesperson> salespersons = getAllSalespersons();
        StringBuilder salepersonsList = new StringBuilder();
        for (Salesperson saleperson : salespersons) {
            salepersonsList.append(saleperson.toString()).append("\n");
        }
        return salepersonsList.toString();
    }

    public static void deleteUser(String userID) {
        userList.removeIf(user -> user.getUserID().equals(userID));
        System.out.println("User deleted with ID: " + userID);
    }

    public static void modifyUser(String userID, int option, String newValue) throws Exception {
        for (User user : userList) {
            if (user.getUserID().equals(userID)) {
                user.modifyProfile(option, newValue);
                System.out.println("User modified with ID: " + userID);
                return;
            }
        }
        System.out.println("User not found with ID: " + userID);
    }

    public static void viewAllUsers() {
        System.out.println("Viewing all users:");
        for (User user : userList) {
            System.out.println("UserID: " + user.getUserID() + ", UserName: " + user.getUserName());
        }
    }

    public static User login(String userName, String password) {
        for (User user : userList) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                System.out.println("Login successful for user: " + userName);
                return user;
            }
        }
        System.out.println("Invalid username or password");
        return null;
    }

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ROLE getUserType() {
        return userType;
    }

    public void setUserType(ROLE userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

