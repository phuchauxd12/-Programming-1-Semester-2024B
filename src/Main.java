import autoPart.autoPart;
import data.user.UserDatabase;
import user.Client;
import user.Mechanic;
import user.Membership;
import user.User;
import utils.UserMenu;

import java.time.LocalDate;


public class Main {
    public static void main(String[] args) throws Exception {
        Membership membership = new Membership();
        Client client = new Client("john_doe", "password1234", "John Doe", LocalDate.of(1990, 12, 1), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.CLIENT, "Active", membership);
        Mechanic mechanic = new Mechanic("mechanic1", "password123", "John Doe", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.EMPLOYEE, "Active", null);
        System.out.println("COSC2081 GROUP ASSIGNMENT");
        System.out.println("AUTO136 CAR DEALERSHIP MANAGEMENT SYSTEM");
        System.out.println("Instructor: Mr. Minh Vu & Mr. Dung Nguyen");
        System.out.println("Group: TEAM 1");
        System.out.println("s3958304, Ton That Huu Luan");
        System.out.println("s3979030, Nguyen Phuc Doan");
        System.out.println("s3919659, Cao Ngoc Phuong Uyen");
        System.out.println("s3975133, Doan Nguyen Phu Chau");
//        Membership membership = new Membership();
//        Client client = new Client("john_doe", "password123", "John Doe", LocalDate.of(1990, 12, 1), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.CLIENT, "Active", membership);

//        System.out.println(client);
//        client.updateTotalSpending(300000000);
//        System.out.println(client);

//        ServiceList serviceList = new ServiceList();
//        serviceList.addService(new Service(LocalDate.of(2024, 1, 15), "client1", "mechanic1", "Oil Change", ServiceBy.AUTO136, "car1", 29.99));
//        serviceList.addService(new Service(LocalDate.of(2024, 2, 20), "client2", "mechanic2", "Brake Replacement", ServiceBy.AUTO136, "car2", 199.99));
//        serviceList.addService(new Service(LocalDate.of(2024, 3, 10), "client3", "mechanic1", "Tire Rotation", ServiceBy.AUTO136, "car3", 49.99));
//        serviceList.addService(new Service(LocalDate.of(2024, 4, 5), "client4", "mechanic3", "Battery Replacement", ServiceBy.OTHER, "car4", 119.99));
//
//        Mechanic mechanic = new Mechanic("mechanic1", "password123", "John Doe", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.EMPLOYEE, "Active", serviceList);
//        Client client = new Client("client001", "password123", "Client One", LocalDate.of(1990, 6, 10), "789 Pine St", 123456789, "client.one@example.com", User.ROLE.CLIENT, "Active", new Membership());
//
//        SaleTransactionList transactionList = new SaleTransactionList();
//
//        transactionList.addSaleTransaction(new SaleTransaction(LocalDate.of(2024, 1, 15), "client001", "salesperson001", 10.0, 150.0));
//        transactionList.addSaleTransaction(new SaleTransaction(LocalDate.of(2024, 2, 10), "client001", "salesperson002", 5.0, 200.0));
//        transactionList.addSaleTransaction(new SaleTransaction(LocalDate.of(2024, 3, 20), "client003", "salesperson001", 15.0, 250.0));
//        transactionList.addSaleTransaction(new SaleTransaction(LocalDate.of(2024, 3, 25), "client004", "salesperson004", 20.0, 300.0));
//
//        Employee employee1 = new Employee("employee1", "password123", "John Doe", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.EMPLOYEE, "Active", transactionList, serviceList);
//        Salesperson sale1 = new Salesperson("salesperson001", "password123", "John Doe", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.EMPLOYEE, "Active", transactionList);
//
//        LocalDate startDate = LocalDate.of(2024, 1, 1);
//        LocalDate endDate = LocalDate.of(2024, 5, 15);

//        mechanic.viewServiceByMechanic(startDate, endDate);
//        mechanic.viewServiceStatistics(startDate, endDate);
//        employee1.listItemsSold(startDate, endDate);
//        employee1.generateOwnStatistics(startDate, endDate);
//        client.viewHistory(transactionList);
//        sale1.viewSalesStatistics(startDate,endDate);
//        sale1.viewTransactionsBySalesperson(startDate,endDate);

        /* Add to database.txt file*/

        autoPart part1 = new autoPart("Brake Pad", "ACDelco", autoPart.Condition.NEW, 12, 24.99, "Fits most Honda vehicles");

        autoPart part2 = new autoPart("Oil Filter", "Fram", autoPart.Condition.NEW, 6, 9.99, "Compatible with various car models");

        autoPart part3 = new autoPart("Spark Plug", "NGK", autoPart.Condition.REFURBISHED, 18, 14.99, "High-performance spark plugs");
//        AutoPartDatabase.createDatabase();
//        AutoPartDatabase.addAutoPart(part1);
//        AutoPartDatabase.addAutoPart(part2);
//        AutoPartDatabase.addAutoPart(part3);
//        AutoPartDatabase.loadAutoParts().stream().forEach(System.out::println);
//        AutoPartDatabase.updateAutoPart("p-e383be13-38ff-4cce-8650-04f8a194329f",1);
        UserDatabase.createDatabase();
        UserDatabase.addUser(client);
        UserDatabase.addUser(mechanic);
        var listUser = UserDatabase.loadUsers();
        System.out.println(listUser);
        UserMenu.mainMenu();
//        LoginMenu.displayLoginMenu();

//        System.out.println(UserSession.getCurrentUser());
//        UserProfileMenu.mainMenu();
//        System.out.println(UserSession.getCurrentUser()); // chưa update được ngay sau khi modify info (Nhưng chắc cũng không cần vì role, type, ID của user không được tự thay đổi)

//        Menu.mainMenu(mechanic);

//        SaleTransactionMenu transactionMenu = new SaleTransactionMenu();
//        transactionMenu.mainMenu();
//
//        ServiceMenu serviceMenu = new ServiceMenu();
//        serviceMenu.mainMenu();

    }

}