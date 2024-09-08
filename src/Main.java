import data.user.UserDatabase;
import user.User;
import utils.UserSession;
import utils.menu.MainMenu;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        /* Add Users to Database*/
//        Membership membership = new Membership();
//        Manager manager = new Manager("manager", "pass123", "Johnny Stack", LocalDate.of(1990, 12, 1), "123 Main St", 1234567890, "johnny.stack@example.com", User.ROLE.MANAGER);
//        Mechanic mechanic1 = new Mechanic("m_timmy", "password123", "Timmy Toe", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "timmy.toe@example.com", User.ROLE.EMPLOYEE);
//        Mechanic mechanic2 = new Mechanic("m_willy", "password123", "Willy Wonka", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "willy.wonka@example.com", User.ROLE.EMPLOYEE);
//        Salesperson sale1 = new Salesperson("s_jackson", "password123", "Jackson Wang", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "jackson.wang@example.com", User.ROLE.EMPLOYEE);
//        Salesperson sale2 = new Salesperson("s_aimee", "password123", "Aim Lee", LocalDate.of(1985, 5, 15), "123 Main St", 1234567890, "jackson.wang@example.com", User.ROLE.EMPLOYEE);
//        Client john = new Client("john_doe", "password1234", "John Doe", LocalDate.of(1990, 12, 1), "123 Main St", 1234567890, "john.doe@example.com", User.ROLE.CLIENT, membership);
//        Client sarah = new Client("sarah_smith", "password012", "Sarah Smith", LocalDate.of(2000, 5, 22), "012 Maple Ln", 987643210, "sarah.smith@example.com", User.ROLE.CLIENT, membership);
//        Client mary = new Client("mary_johnson", "password678", "Mary Johnson", LocalDate.of(1960, 9, 18), "678 Willow Dr", 987643210, "mary.johnson@example.com", User.ROLE.CLIENT, membership);
//        Client lily = new Client("lily_james", "password678", "Lily James", LocalDate.of(1960, 9, 18), "678 Willow Dr", 987643210, "lily.james@example.com", User.ROLE.CLIENT, membership);
//
//        UserDatabase.createDatabase();
//        User.addUser(manager);
//        User.addUser(mechanic1);
//        User.addUser(mechanic2);
//        User.addUser(sale1);
//        User.addUser(sale2);
//        User.addUser(john);
//        User.addUser(sarah);
//        User.addUser(mary);
//        User.addUser(lily);
////
////        /* Add Auto Part and Car */
//        autoPart part1 = new autoPart("Brake Pad", "ACDelco", autoPart.Condition.NEW, 12, 615000, "Fits most Honda vehicles");
//        autoPart part2 = new autoPart("Oil Filter", "Fram", autoPart.Condition.NEW, 6, 270000, "Compatible with various car models");
//        autoPart part3 = new autoPart("Spark Plug", "NGK", autoPart.Condition.REFURBISHED, 18, 370000, "High-performance spark plugs");
//        autoPart part4 = new autoPart("Air Filter", "K&N", autoPart.Condition.NEW, 20, 600000, "High-performance air filter for better airflow");
//        autoPart part5 = new autoPart("Battery", "Exide", autoPart.Condition.NEW, 10, 2500000, "12V car battery suitable for most sedans");
//        autoPart part6 = new autoPart("Timing Belt", "Gates", autoPart.Condition.NEW, 15, 1500000, "Durable timing belt for most Toyota models");
//        autoPart part7 = new autoPart("Windshield Wiper", "Bosch", autoPart.Condition.REFURBISHED, 30, 300000, "All-weather windshield wiper blades");
//        autoPart part8 = new autoPart("Brake Disc", "Brembo", autoPart.Condition.NEW, 8, 2800000, "High-performance brake disc for SUVs");
//        autoPart part9 = new autoPart("Alternator", "Delphi", autoPart.Condition.USED, 5, 3200000, "Used alternator for various European cars");
//        autoPart part10 = new autoPart("Headlight Bulb", "Philips", autoPart.Condition.NEW, 25, 450000, "Bright white headlight bulb for nighttime driving");
//        autoPart part11 = new autoPart("Radiator", "Denso", autoPart.Condition.USED, 12, 3400000, "Used radiator for Honda Civics");
//        autoPart part12 = new autoPart("Fuel Pump", "Bosch", autoPart.Condition.NEW, 10, 1800000, "Fuel pump compatible with multiple car models");
//        autoPart part13 = new autoPart("Clutch Kit", "Sachs", autoPart.Condition.NEW, 7, 4200000, "Complete clutch kit for manual transmission vehicles");
//        autoPart part14 = new autoPart("Engine Mount", "Anchor", autoPart.Condition.USED, 9, 900000, "Used engine mount for vibration control");
//        autoPart part15 = new autoPart("Exhaust Pipe", "Walker", autoPart.Condition.NEW, 12, 2200000, "Stainless steel exhaust pipe for improved performance");
//        autoPart part16 = new autoPart("Oxygen Sensor", "Denso", autoPart.Condition.NEW, 20, 1500000, "Oxygen sensor for fuel efficiency optimization");
//        autoPart part17 = new autoPart("Turbocharger", "Garrett", autoPart.Condition.REFURBISHED, 6, 12500000, "Refurbished turbocharger for performance boost");
//        autoPart part18 = new autoPart("Brake Caliper", "Centric", autoPart.Condition.NEW, 10, 2000000, "Brake caliper for precise braking control");
//        autoPart part19 = new autoPart("Water Pump", "Aisin", autoPart.Condition.USED, 8, 1200000, "Used water pump for engine cooling");
//        autoPart part20 = new autoPart("Timing Chain", "Melling", autoPart.Condition.NEW, 18, 1100000, "Reliable timing chain for smooth engine operation");
//        autoPart part21 = new autoPart("Fog Light", "Hella", autoPart.Condition.NEW, 15, 550000, "Fog light for improved visibility in adverse conditions");
//        autoPart part22 = new autoPart("Shock Absorber", "KYB", autoPart.Condition.REFURBISHED, 12, 2400000, "Refurbished shock absorber for smooth ride");
//        autoPart part23 = new autoPart("Tail Light", "Depo", autoPart.Condition.USED, 20, 950000, "Used tail light for rear visibility");
//        autoPart part24 = new autoPart("Ignition Coil", "Delphi", autoPart.Condition.NEW, 25, 700000, "Ignition coil for efficient engine start");
//        autoPart part25 = new autoPart("Muffler", "Walker", autoPart.Condition.NEW, 12, 3500000, "Quiet muffler for exhaust noise reduction");
//        autoPart part26 = new autoPart("Catalytic Converter", "MagnaFlow", autoPart.Condition.NEW, 8, 8500000, "Catalytic converter for emission control");
//        autoPart part27 = new autoPart("Engine Oil", "Mobil 1", autoPart.Condition.NEW, 50, 800000, "High-performance synthetic engine oil");
//        autoPart part28 = new autoPart("Drive Shaft", "GKN", autoPart.Condition.USED, 6, 3500000, "Used drive shaft for rear-wheel-drive vehicles");
//        autoPart part29 = new autoPart("Power Steering Pump", "Aisin", autoPart.Condition.REFURBISHED, 8, 4200000, "Refurbished power steering pump for easy handling");
//        autoPart part30 = new autoPart("Transmission Filter", "WIX", autoPart.Condition.NEW, 30, 400000, "Transmission filter for automatic transmissions");
//
//
//        AutoPartDatabase.createDatabase();
//        CarAndAutoPartMenu.addPartToList(part1);
//        CarAndAutoPartMenu.addPartToList(part2);
//        CarAndAutoPartMenu.addPartToList(part3);
//        CarAndAutoPartMenu.addPartToList(part4);
//        CarAndAutoPartMenu.addPartToList(part5);
//        CarAndAutoPartMenu.addPartToList(part6);
//        CarAndAutoPartMenu.addPartToList(part7);
//        CarAndAutoPartMenu.addPartToList(part8);
//        CarAndAutoPartMenu.addPartToList(part9);
//        CarAndAutoPartMenu.addPartToList(part10);
//        CarAndAutoPartMenu.addPartToList(part11);
//        CarAndAutoPartMenu.addPartToList(part12);
//        CarAndAutoPartMenu.addPartToList(part13);
//        CarAndAutoPartMenu.addPartToList(part14);
//        CarAndAutoPartMenu.addPartToList(part15);
//        CarAndAutoPartMenu.addPartToList(part16);
//        CarAndAutoPartMenu.addPartToList(part17);
//        CarAndAutoPartMenu.addPartToList(part18);
//        CarAndAutoPartMenu.addPartToList(part19);
//        CarAndAutoPartMenu.addPartToList(part20);
//        CarAndAutoPartMenu.addPartToList(part21);
//        CarAndAutoPartMenu.addPartToList(part22);
//        CarAndAutoPartMenu.addPartToList(part23);
//        CarAndAutoPartMenu.addPartToList(part24);
//        CarAndAutoPartMenu.addPartToList(part25);
//        CarAndAutoPartMenu.addPartToList(part26);
//        CarAndAutoPartMenu.addPartToList(part27);
//        CarAndAutoPartMenu.addPartToList(part28);
//        CarAndAutoPartMenu.addPartToList(part29);
//        CarAndAutoPartMenu.addPartToList(part30);
//
//
//       Car car1 = new Car("Toyota", "Camry", 2010, "Black", 120000, 950000000.0, "", Status.SOLD);
//        Car car2 = new Car("Toyota", "Camry", 2012, "Silver", 100000, 900000000.0, "", Status.AVAILABLE);
//        Car car3 = new Car("Ford", "Focus", 2015, "White", 80000, 750000000.0, "", Status.WALK_IN);
//        Car car4 = new Car("Ford", "Focus", 2017, "Red", 60000, 800000000.0, "", Status.AVAILABLE);
//        Car car5 = new Car("Hyundai", "Elantra", 2012, "Blue", 90000, 700000000.0, "", Status.SOLD);
//        Car car6 = new Car("Hyundai", "Elantra", 2014, "Gray", 85000, 730000000.0, "", Status.AVAILABLE);
//        Car car7 = new Car("Mazda", "3", 2019, "Gray", 50000, 850000000.0, "", Status.WALK_IN);
//        Car car8 = new Car("Kia", "Optima", 2014, "Gray", 100000, 680000000.0, "", Status.AVAILABLE);
//        Car car9 = new Car("Subaru", "Impreza", 2011, "Red", 95000, 550000000.0, "", Status.SOLD);
//        Car car10 = new Car("Nissan", "Altima", 2009, "Green", 130000, 500000000.0, "", Status.AVAILABLE);
//        Car car11 = new Car("Honda", "Civic", 2005, "Blue", 110000, 600000000.0, "", Status.SOLD);
//        Car car12 = new Car("BMW", "3 Series", 2018, "Silver", 30000, 2000000000.0, "", Status.AVAILABLE);
//        Car car13 = new Car("BMW", "3 Series", 2020, "White", 25000, 2200000000.0, "", Status.SOLD);
//        Car car14 = new Car("Toyota", "Corolla", 2018, "Silver", 40000, 780000000.0, "", Status.AVAILABLE);
//        Car car15 = new Car("Chevrolet", "Tahoe", 2015, "Black", 120000, 1900000000, "", Status.SOLD);
//        Car car16 = new Car("Jeep", "Wrangler", 2021, "Red", 20000, 3200000000.0, "", Status.AVAILABLE);
//        Car car17 = new Car("Lexus", "RX", 2019, "Blue", 30000, 2800000000.0, "", Status.WALK_IN);
//        Car car18 = new Car("Honda", "Accord", 2019, "Blue", 40000, 1100000000.0, "", Status.SOLD);
//        Car car19 = new Car("Audi", "A4", 2016, "Black", 70000, 1500000000.0, "", Status.WALK_IN);
//        Car car20 = new Car("Audi", "A4", 2019, "Gray", 50000, 1600000000.0, "", Status.AVAILABLE);
//        Car car21 = new Car("Tesla", "Model 3", 2022, "White", 15000, 3500000000.0, "", Status.AVAILABLE);
//        Car car22 = new Car("Porsche", "Cayenne", 2021, "Black", 40000, 5000000000.0, "", Status.AVAILABLE);
//
//        CarDatabase.createDatabase();
//        CarAndAutoPartMenu.addCarToList(car1);
//        CarAndAutoPartMenu.addCarToList(car2);
//        CarAndAutoPartMenu.addCarToList(car3);
//        CarAndAutoPartMenu.addCarToList(car4);
//        CarAndAutoPartMenu.addCarToList(car5);
//        CarAndAutoPartMenu.addCarToList(car6);
//        CarAndAutoPartMenu.addCarToList(car7);
//        CarAndAutoPartMenu.addCarToList(car8);
//        CarAndAutoPartMenu.addCarToList(car9);
//        CarAndAutoPartMenu.addCarToList(car10);
//        CarAndAutoPartMenu.addCarToList(car11);
//        CarAndAutoPartMenu.addCarToList(car12);
//        CarAndAutoPartMenu.addCarToList(car13);
//        CarAndAutoPartMenu.addCarToList(car14);
//        CarAndAutoPartMenu.addCarToList(car15);
//        CarAndAutoPartMenu.addCarToList(car16);
//        CarAndAutoPartMenu.addCarToList(car17);
//        CarAndAutoPartMenu.addCarToList(car18);
//        CarAndAutoPartMenu.addCarToList(car19);
//        CarAndAutoPartMenu.addCarToList(car20);
//        CarAndAutoPartMenu.addCarToList(car21);
//        CarAndAutoPartMenu.addCarToList(car22);
//
//          /* Add Service*/
//        Service service1 = new Service(LocalDate.of(2024, 1, 15), john.getUserID(), mechanic1.getUserID(), Service.serviceType.Oil_Change, List.of(part2.getPartID()), ServiceBy.AUTO136, car3.getCarID(), Service.serviceType.Oil_Change.getPrice(), null);
//        Service service2 = new Service(LocalDate.of(2024, 2, 10), sarah.getUserID(), mechanic1.getUserID(), Service.serviceType.Brake_Service, List.of(part18.getPartID()), ServiceBy.AUTO136, car7.getCarID(), Service.serviceType.Brake_Service.getPrice(), null);
//        Service service3 = new Service(LocalDate.of(2024, 3, 5), mary.getUserID(), mechanic1.getUserID(), Service.serviceType.Tire_Rotation, List.of(), ServiceBy.AUTO136, car17.getCarID(), Service.serviceType.Tire_Rotation.getPrice(), "Rotated all four tires.");
//        Service service4 = new Service(LocalDate.of(2024, 4, 20), john.getUserID(), mechanic2.getUserID(), Service.serviceType.General_Maintenance_L1, List.of(part5.getPartID()), ServiceBy.AUTO136, car3.getCarID(), Service.serviceType.General_Maintenance_L1.getPrice(), "Battery tested and replaced.");
//        Service service5 = new Service(LocalDate.of(2024, 5, 12), sarah.getUserID(), mechanic2.getUserID(), Service.serviceType.Transmission_Repair, List.of(part30.getPartID()), ServiceBy.AUTO136, car7.getCarID(), Service.serviceType.Transmission_Repair.getPrice(), "Transmission fluid replaced and gears adjusted.");
//        Service service6 = new Service(LocalDate.of(2024, 6, 8), mary.getUserID(), mechanic2.getUserID(), Service.serviceType.Steering_Repair, List.of(), ServiceBy.AUTO136, car17.getCarID(), Service.serviceType.Steering_Repair.getPrice(), "Steering checked and repaired. Adjustments made to improve handling.");
//        Service service7 = new Service(LocalDate.of(2024, 7, 20), sarah.getUserID(), mechanic2.getUserID(), Service.serviceType.Cooling_System_Repair, List.of(), ServiceBy.AUTO136, car7.getCarID(), Service.serviceType.Cooling_System_Repair.getPrice(), "Coolant flushed, radiator checked.");
//        Service service8 = new Service(LocalDate.of(2024, 8, 15), mary.getUserID(), mechanic1.getUserID(), Service.serviceType.Air_Filter, List.of(part4.getPartID()), ServiceBy.AUTO136, car17.getCarID(), Service.serviceType.Air_Filter.getPrice(), "Air filter replaced with a high-efficiency filter.");
//        Service service9 = new Service(LocalDate.of(2024, 9, 10), lily.getUserID(), mechanic2.getUserID(), Service.serviceType.Accessory_Installation, List.of(part6.getPartID()), ServiceBy.AUTO136, car19.getCarID(), Service.serviceType.Accessory_Installation.getPrice(), "Timing belt replaced, engine recalibrated.");
//        Service service10 = new Service(LocalDate.of(2024, 10, 5), john.getUserID(), mechanic2.getUserID(), Service.serviceType.CarWash, List.of(), ServiceBy.AUTO136, car3.getCarID(), Service.serviceType.CarWash.getPrice(), null);
//        Service service11 = new Service(LocalDate.of(2024, 11, 2), john.getUserID(), mechanic1.getUserID(), Service.serviceType.Exhaust_System_Repair, List.of(), ServiceBy.AUTO136, car3.getCarID(), Service.serviceType.Exhaust_System_Repair.getPrice(), "Exhaust system repaired, muffler replaced.");
//        Service service12 = new Service(LocalDate.of(2024, 12, 1), lily.getUserID(), mechanic1.getUserID(), Service.serviceType.Brake_Service, List.of(part1.getPartID()), ServiceBy.AUTO136, car19.getCarID(), Service.serviceType.Brake_Service.getPrice(), "Brakes inspected and pads replaced.");
//        Service service13 = new Service(LocalDate.of(2024, 12, 15), lily.getUserID(), mechanic2.getUserID(), Service.serviceType.CarWash, List.of(), ServiceBy.AUTO136, car19.getCarID(), Service.serviceType.CarWash.getPrice(), null);
//        Service service14 = new Service(LocalDate.of(2024, 12, 20), john.getUserID(), mechanic2.getUserID(), Service.serviceType.Tire_Rotation, List.of(), ServiceBy.AUTO136, car3.getCarID(), Service.serviceType.Tire_Rotation.getPrice(), "Tires rotated and inspected.");
//        Service service15 = new Service(LocalDate.of(2024, 12, 25), lily.getUserID(), mechanic2.getUserID(), Service.serviceType.Oil_Change, List.of(), ServiceBy.AUTO136, car19.getCarID(), Service.serviceType.Oil_Change.getPrice(), "Oil changed, engine checked.");
//        Service service16 = new Service(LocalDate.of(2024, 12, 30), sarah.getUserID(), mechanic2.getUserID(), Service.serviceType.Paint_Job, List.of(), ServiceBy.AUTO136, car7.getCarID(), Service.serviceType.Paint_Job.getPrice(), null);
//        Service service17 = new Service(LocalDate.of(2024, 1, 10), sarah.getUserID(), null, null, null, ServiceBy.OTHER, car7.getCarID(), 0, "Oil change performed by an external service provider.");
//        service17.setServiceTypeByOther("Oil Change");
//        Service service18 = new Service(LocalDate.of(2024, 2, 22), lily.getUserID(), null, null, null, ServiceBy.OTHER,car19.getCarID() , 0, "Brake pads replaced by a local garage.");
//        service18.setServiceTypeByOther("Brake Pad Replacement");
//        Service service19 = new Service(LocalDate.of(2024, 3, 15), sarah.getUserID(), null, null, null, ServiceBy.OTHER, car7.getCarID(), 0, "Engine tuning carried out by a third-party specialist.");
//        service19.setServiceTypeByOther("Engine Tuning");
//        Service service20 = new Service(LocalDate.of(2024, 4, 5), mary.getUserID(), null, null, null, ServiceBy.OTHER, car17.getCarID(), 0, "Transmission fluid changed by an independent mechanic.");
//        service20.setServiceTypeByOther("Transmission Fluid Change");
//
//        ServiceDatabase.createDatabase();
//        Service.addService(service1);
//        Service.addService(service2);
//        Service.addService(service3);
//        Service.addService(service4);
//        Service.addService(service5);
//        Service.addService(service6);
//        Service.addService(service7);
//        Service.addService(service8);
//        Service.addService(service9);
//        Service.addService(service10);
//        Service.addService(service11);
//        Service.addService(service12);
//        Service.addService(service13);
//        Service.addService(service14);
//        Service.addService(service15);
//        Service.addService(service16);
//        Service.addService(service17);
//        Service.addService(service18);
//        Service.addService(service19);
//        Service.addService(service20);
//

        /* Add Transaction*/
//        SaleTransaction transaction1 = new SaleTransaction(LocalDate.of(2024, 1, 5), john.getUserID(), sale1.getUserID(), List.of(part16.getPartID()));
//        SaleTransaction transaction2 = new SaleTransaction(LocalDate.of(2024, 2, 10), lily.getUserID(), sale2.getUserID(), List.of(car1.getCarID()));
//        SaleTransaction transaction3 = new SaleTransaction(LocalDate.of(2024, 3, 15), mary.getUserID(), sale1.getUserID(), List.of(car10.getCarID()));
//        SaleTransaction transaction4 = new SaleTransaction(LocalDate.of(2024, 4, 20), john.getUserID(), sale2.getUserID(), List.of(part10.getPartID()));
//        SaleTransaction transaction5 = new SaleTransaction(LocalDate.of(2024, 5, 25), mary.getUserID(), sale1.getUserID(), List.of(part27.getPartID()));
//
//        SaleTransactionDatabase.createDatabase();
//        SaleTransaction.addSaleTransaction(transaction1);
//        SaleTransaction.addSaleTransaction(transaction2);
//        SaleTransaction.addSaleTransaction(transaction3);
//        SaleTransaction.addSaleTransaction(transaction4);
//        SaleTransaction.addSaleTransaction(transaction5);


        System.out.println("COSC2081 GROUP ASSIGNMENT");
        System.out.println("AUTO136 CAR DEALERSHIP MANAGEMENT SYSTEM");
        System.out.println("Instructor: Mr. Minh Vu & Mr. Dung Nguyen");
        System.out.println("Group: TEAM 1");
        System.out.println("s3958304, Ton That Huu Luan");
        System.out.println("s3979030, Nguyen Phuc Doan");
        System.out.println("s3919659, Cao Ngoc Phuong Uyen");
        System.out.println("s3975133, Doan Nguyen Phu Chau");

//        UserDatabase.loadUsers().stream().forEach(System.out::println);
//        AutoPartDatabase.loadAutoParts().stream().forEach(System.out::println);
//        CarDatabase.loadCars().stream().forEach(System.out::println);
//        ServiceDatabase.loadService().stream().forEach(System.out::println);
//        SaleTransactionDatabase.loadSaleTransaction().stream().forEach(System.out::println);

        /* Login */
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please login to continue:");
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            Optional<User> foundUser = UserDatabase.loadUsers().stream()
                    .filter(user -> user.getUserName().equals(username) && user.getPassword().equals(password))
                    .findFirst();

            if (foundUser.isPresent()) {
                User user = foundUser.get();
                System.out.println("Login successful! Welcome, " + user.getName());
                UserSession.setCurrentUser(user);
                break;
            } else {
                System.out.println("=> Invalid username or password.");
                System.out.println("1. Retry");
                System.out.println("2. Exit");
                System.out.print("Please choose an option (1 or 2): ");
                String choice = scanner.nextLine();

                if (choice.equals("2")) {
                    System.out.println("Exiting...");
                    break;
                }
            }
        }
        
        MainMenu menu = new MainMenu();
        menu.mainMenu();

    }

}