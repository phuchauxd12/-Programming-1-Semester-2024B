package car;

import services.Service;
import utils.Status;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Car implements Serializable {
    private String carID;
    private String carMake;
    private String carModel;
    private int carYear;
    private String color;
    private double mileage;
    private double price;
    private Status status;
    private String addNotes;
    private LocalDate soldDate = null;
    private boolean isDeleted = false;
    private List<Service> serviceHistory = new ArrayList<>();


    public Car(String carMake, String carModel, int carYear, String color, double mileage, double price, String addNotes, Status status) {
        this.carID = generateCarID();
        this.carMake = carMake;
        this.carModel = carModel;
        this.carYear = carYear;
        this.color = color;
        this.mileage = mileage;
        this.price = price;
        this.addNotes = addNotes;
        this.status = status;
    }

    private String generateCarID() {
        return "c-" + UUID.randomUUID();
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

    public LocalDate getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDate soldDate) {
        this.soldDate = soldDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public List<Service> getServiceHistory() {
        return serviceHistory;
    }


    public void addServiceToHistory(Service service) {
        serviceHistory.add(service);
    }

    public void displayServiceHistory() {
        for (Service service : serviceHistory) {
            System.out.println("Service By: " + service.getServiceBy());
            System.out.println("Service Date: " + service.getServiceDate());
            System.out.println("Service Type: " + service.getServiceType());
        }
    }


    public String toStringDetailed() {
        return "Car ID: " + carID + "\n" +
                "Car Make: " + carMake + "\n" +
                "Car Model: " + carModel + "\n" +
                "Car Year: " + carYear + "\n" +
                "Color: " + color + "\n" +
                "Mileage: " + mileage + "\n" +
                "Price: " + price + "\n" +
                "Status: " + status + "\n" +
                "Additional Notes: " + addNotes + "\n" +
                "Sold Date: " + soldDate + "\n" +
                "Deleted: " + isDeleted + "\n" +
                "Service History: " + serviceHistory + "\n";
    }

    @Override
    public String toString() {
        return "{" +
                "carID ='" + carID + "'" +
                ", carMake ='" + carMake + "'" +
                ", carModel ='" + carModel + "'" +
                ", carYear = " + carYear +
                ", color ='" + color + "'" + ", price = " + price +
                "}";
    }
}
