import autoPart.autoPart;
import car.Car;
import data.car.CarDatabase;
import data.transaction.SaleTransactionDatabase;
import data.user.UserDatabase;
import services.Service;
import services.ServiceBy;
import user.*;
import utils.LoginMenu;
import utils.Menu;
import utils.Status;

import java.time.LocalDate;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        Membership membership = new Membership();
        Manager manager = new Manager("manager1", "pass123", "Johnny Stack", LocalDate.of(1990, 12, 1), "123 Main St", 1234567890, "johnny.stack@example.com", User.ROLE.MANAGER, null, null);
        Client client = new Client("john_doe", "password1234", "Timmy Toe", LocalDate.of(1990, 12, 1), "123 Main St", 1234567890, "timmy.toe@example.com", User.ROLE.CLIENT, membership);
        Mechanic mechanic = new Mechanic("mechanic", "password123", "John Doe", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.EMPLOYEE, null);
        Salesperson sale1 = new Salesperson("salesperson1", "password123", "Jackson Wang", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "jackson.wang@example.com", User.ROLE.EMPLOYEE, null);
//        Client client = new Client("john_doe", "password123", "John Doe", LocalDate.of(1990, 12, 1), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.CLIENT, "Active", membership);
//        Client client1 = new Client("sarah_smith", "password012", "Sarah Smith", LocalDate.of(2000, 5, 22), "012 Maple Ln", 987643210, "sarah.smith@example.com", User.ROLE.CLIENT, "Active", membership);
//        Client client2 = new Client("mary_johnson", "password678", "Mary Johnson", LocalDate.of(1960, 9, 18), "678 Willow Dr", 987643210, "mary.johnson@example.com", User.ROLE.CLIENT, "Active", membership);
        autoPart part1 = new autoPart("Brake Pad", "ACDelco", autoPart.Condition.NEW, 12, 24.99, "Fits most Honda vehicles");
        autoPart part2 = new autoPart("Oil Filter", "Fram", autoPart.Condition.NEW, 6, 9.99, "Compatible with various car models");
        autoPart part3 = new autoPart("Spark Plug", "NGK", autoPart.Condition.REFURBISHED, 18, 14.99, "High-performance spark plugs");

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
        Car car = new Car("Honda", "Civic", 2018, "Black", 45000, 15999.99, "Clean title, no accidents", Status.AVAILABLE);
        CarDatabase.createDatabase();
        Car.addCarToList(car);
//        System.out.println(CarDatabase.loadCars());
//        System.out.println(client);
//        client.updateTotalSpending(300000000);
//        System.out.println(client);
        Service service1 = new Service(LocalDate.of(2024, 1, 15), "john_doe", "mechanic1", Service.serviceType.Oil_Change, List.of("p-01bdb224-c3b7-486b-a883-8f235287ae29", "p-c428b805-081f-49d0-a8c3-b55cc6cb8d6d") , ServiceBy.AUTO136, "c-01642fbe-1e71-46f2-a3e7-6e94ee863c7c", Service.serviceType.Oil_Change.getPrice());
        Service service2 = new Service(LocalDate.of(2024, 1, 15), "john_doe", "mechanic1", Service.serviceType.Brake_Service, List.of("Brake Pad", "Oil Filter") , ServiceBy.AUTO136, "c-01642fbe-1e71-46f2-a3e7-6e94ee863c7c", Service.serviceType.Brake_Service.getPrice());

//        ServiceList serviceList = new ServiceList();
//        serviceList.addService(new Service(LocalDate.of(2024, 1, 15), "client1", "mechanic1", "Oil Change", ServiceBy.AUTO136, "car1", 29.99));
//        serviceList.addService(new Service(LocalDate.of(2024, 2, 20), "client2", "mechanic2", "Brake Replacement", ServiceBy.AUTO136, "car2", 199.99));
//        serviceList.addService(new Service(LocalDate.of(2024, 3, 10), "client3", "mechanic1", "Tire Rotation", ServiceBy.AUTO136, "car3", 49.99));
//        serviceList.addService(new Service(LocalDate.of(2024, 4, 5), "client4", "mechanic3", "Battery Replacement", ServiceBy.OTHER, "car4", 119.99));
//
//        Mechanic mechanic = new Mechanic("mechanic1", "password123", "John Doe", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.EMPLOYEE, "Active", serviceList);
//        Client client = new Client("client001", "password123", "Client One", LocalDate.of(1990, 6, 10), "789 Pine St", 123456789, "client.one@example.com", User.ROLE.CLIENT, "Active", new Membership());
//
        SaleTransactionDatabase.createDatabase();
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


//        var partlist = new ArrayList<String>();
////        partlist.add(part1.getPartName());
////        partlist.add(part2.getPartName());
////        partlist.add(part3.getPartName());
//        AutoPartDatabase.createDatabase();
//        autoPart.addPartToList(part1);
//        autoPart.addPartToList(part2);
//        autoPart.addPartToList(part3);
//        AutoPartDatabase.loadAutoParts().stream().forEach(System.out::println);
//        AutoPartDatabase.updateAutoPart("p-e383be13-38ff-4cce-8650-04f8a194329f",1);
//        UserDatabase.createDatabase();
//        User.addUser(client);
//        User.addUser(manager);
//        User.addUser(sale1);
//        User.addUser(mechanic);
        var listUser = UserDatabase.loadUsers();
        System.out.println(listUser);


//        ServiceDatabase.createDatabase();
//        Service.addService(service1);
//        Service.addService(service2);
//        System.out.println(ServiceDatabase.loadService());
//        var listUser2 = UserDatabase.loadUsers();
//        System.out.println(listUser2);
        LoginMenu.displayLoginMenu();
//        UserMenu.mainMenu();

//        System.out.println(UserSession.getCurrentUser());
//        UserProfileMenu.mainMenu();
//        ServiceMenu.mainMenu();
//        System.out.println(UserSession.getCurrentUser()); // chưa update được ngay sau khi modify info (Nhưng chắc cũng không cần vì role, type, ID của user không được tự thay đổi)

        Menu mainMenu = new Menu();
        mainMenu.mainMenu();

//        SaleTransactionMenu transactionMenu = new SaleTransactionMenu();
//        transactionMenu.mainMenu();
//
//        ServiceMenu serviceMenu = new ServiceMenu();
//        serviceMenu.mainMenu();
        // Add some activity logs
//        ActivityLog.addActivityLog("user1", LocalDate.of(2024, 8, 15), "Login");
//        ActivityLog.addActivityLog("user2", LocalDate.of(2024, 8, 16), "Upload Document");
//        ActivityLog.addActivityLog("user1", LocalDate.of(2024, 8, 17), "Logout");

    }

}