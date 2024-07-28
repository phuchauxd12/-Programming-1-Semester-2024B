package car;

import services.Service;
import utils.CarAndAutoPartMenu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Car {
    private String carID;
    private String carMake;
    private String carModel;
    private int carYear;
    private String color;
    private double mileage;
    private double price;
    private Status status = Status.AVAILABLE;
    private String addNotes;
    private LocalDateTime soldDate = null;
    private boolean isDeleted = false;
    private ArrayList<Service> serviceHistory;
    private static long carCounter = 0;

    public enum Status {
        AVAILABLE,
        SOLD
    }

    public Car(String carMake, String carModel, int carYear, String color, double mileage, double price, String addNotes) {
        this.carID = generateCarID();
        this.carMake = carMake;
        this.carModel = carModel;
        this.carYear = carYear;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
        this.addNotes = addNotes;
    }

    public String generateCarID() {
        return "c-" + (++carCounter);
    }

    public String getCarID() {
        return carID;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAddNotes() {
        return addNotes;
    }

    public void setAddNotes(String addNotes) {
        this.addNotes = addNotes;
    }

    public LocalDateTime getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDateTime soldDate) {
        this.soldDate = soldDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public ArrayList<Service> getServiceHistory() {
        return serviceHistory;
    }

    public void setServiceHistory(ArrayList<Service> serviceHistory) {
        this.serviceHistory = serviceHistory;
    }

    public static void createCar() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the car's make:");
        String carMake = input.next();
        System.out.println("Please input the car's model:");
        String carModel = input.next();
        System.out.println("Please input the car's year:");
        int carYear = input.nextInt();
        System.out.println("Please input the car's color:");
        String color = input.next();
        System.out.println("Please input the car's mileage:");
        double mileage = input.nextDouble();
        System.out.println("Please input the car's price:");
        double price = input.nextDouble();
        System.out.println("Please input any additional notes:");
        String addNotes = input.next();
        Car newCar = new Car(carMake, carModel, carYear, color, mileage, price, addNotes);
        System.out.println("Car created successfully!");
        System.out.println(newCar);
        CarAndAutoPartMenu.getCarsList().add(newCar);
    }

    public static void displayAllCars() {
        System.out.println("Displaying all cars:");
        for (Car car : CarAndAutoPartMenu.getCarsList()) {
            System.out.println(car);
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "carID ='" + carID + '\'' +
                ", carMake ='" + carMake + '\'' +
                ", carModel ='" + carModel + '\'' +
                ", carYear =" + carYear +
                ", color ='" + color + '\'' +
                ", mileage =" + mileage +
                ", price =" + price +
                ", status =" + status +
                ", addNotes ='" + addNotes + '\'' +
                ", soldDate =" + soldDate +
                ", isDeleted =" + isDeleted +
                ", serviceHistory =" + serviceHistory +
                '}';
    }
}
