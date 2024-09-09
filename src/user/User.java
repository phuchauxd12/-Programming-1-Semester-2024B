package user;

import data.user.UserDatabase;
import utils.DatePrompt;
import utils.menu.UserMenu;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
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

    public User(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType) throws Exception {
        this.userID = generateUserId();
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.phoneNum = phoneNum;
        this.email = email;
        this.userType = userType;
        this.status = "active";
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

    public void modifyProfile(int option) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input;
        switch (option) {
            case 1:
                System.out.println("Please enter new username: ");
                input = scanner.nextLine();
                this.setUserName(input);
                UserDatabase.saveUsersData(UserMenu.getUserList());
                break;
            case 2:
                System.out.println("Please enter new password: ");
                input = scanner.nextLine();
                this.setPassword(input);
                UserDatabase.saveUsersData(UserMenu.getUserList());
                break;
            case 3:
                System.out.println("Please enter new name: ");
                input = scanner.nextLine();
                this.setName(input);
                UserDatabase.saveUsersData(UserMenu.getUserList());
                break;
            case 4:
                System.out.println("Please enter new address: ");
                input = scanner.nextLine();
                this.setAddress(input);
                UserDatabase.saveUsersData(UserMenu.getUserList());
                break;
            case 5:
                System.out.println("Please enter new phone number: ");
                input = scanner.nextLine();
                this.setPhoneNum(Integer.parseInt(input));
                UserDatabase.saveUsersData(UserMenu.getUserList());
                break;
            case 6:
                System.out.println("Please enter new email: ");
                input = scanner.nextLine();
                this.setEmail(input);
                UserDatabase.saveUsersData(UserMenu.getUserList());
                break;
            case 7:
                LocalDate newDob = DatePrompt.getDate("new date of birth");
                this.setDob(newDob);
                UserDatabase.saveUsersData(UserMenu.getUserList());
                break;

            default:
                System.out.println("Invalid field specified.");
        }
    }

    public static void modifyUser(String userID, int option) throws Exception {
        for (User user : UserMenu.getUserList()) {
            if (user.getUserID().equals(userID)) {
                user.modifyProfile(option);
                System.out.println("User modified with ID: " + userID);
                return;
            }
        }
        System.out.println("User not found with ID: " + userID);
    }


    public static void addUser(User user) throws Exception {
        List<User> userList = UserMenu.getUserList();
        userList.add(user);
        UserDatabase.saveUsersData(userList);
        System.out.println("User added: " + user.getUserName());
    }


    public static void deleteUser(String userID) throws Exception {
        User user = UserMenu.getUserById(userID);
        if (user != null) {
            user.setStatus("deleted");
            UserDatabase.saveUsersData(UserMenu.getUserList());
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("User not found with ID: " + userID);
        }
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

    public String getUserInfo() {
        var sb = new StringBuilder();
        sb.append("User ID: ").append(userID).append("\n");
        sb.append("Username: ").append(userName).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Date of birth: ").append(dob).append("\n");
        sb.append("Address: ").append(address).append("\n");
        sb.append("Phone number: ").append(phoneNum).append("\n");
        sb.append("email: ").append(email).append("\n");
        return sb.toString();
    }
}

