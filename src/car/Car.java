package car;

import services.Service;
import services.ServiceBy;
import utils.CurrencyFormat;
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
    private String clientID;


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

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public List<Service> getServiceHistory() {
        return serviceHistory;
    }

    public void removeService(String serviceId) {
        serviceHistory.removeIf(service -> service.getServiceId().equals(serviceId));
    }


    public void addServiceToHistory(Service service) {
        serviceHistory.add(service);
    }

    public String displayServiceHistory() {
        StringBuilder history = new StringBuilder();
        for (Service service : serviceHistory) {
            history.append("-------------------------\n");
            history.append("Service By: ").append(service.getServiceBy()).append("\n");
            history.append("Service Date: ").append(service.getServiceDate()).append("\n");
            if (service.getServiceBy() == ServiceBy.AUTO136) {
                history.append("Service Type: ").append(service.getServiceType()).append("\n");
            } else {
                history.append("Service Type: ").append(service.getServiceTypeByOther()).append("\n");
            }
            history.append("-------------------------\n");
        }
        return history.toString();
    }


    public String toStringDetailed() {
        return "Car ID: " + carID + "\n" +
                "Car Make: " + carMake + "\n" +
                "Car Model: " + carModel + "\n" +
                "Car Year: " + carYear + "\n" +
                "Color: " + color + "\n" +
                "Mileage: " + mileage + "\n" +
                "Price: " + CurrencyFormat.format(price) + "\n" +
                "Status: " + status + "\n" +
                (status != Status.AVAILABLE ? "Client ID: " + clientID + "\n" : "") +
                "Additional Notes: " + addNotes + "\n" +
                "Sold Date: " + soldDate + "\n" +
                "Deleted: " + isDeleted + "\n" +
                "Service History: \n" + displayServiceHistory() + "\n";
    }

    @Override
    public String toString() {
        return "{" +
                "carID ='" + carID + "'" +
                ", carMake ='" + carMake + "'" +
                ", carModel ='" + carModel + "'" +
                ", carYear = " + carYear +
                ", color ='" + color + "'" + ", price = " + CurrencyFormat.format(price) +
                "}";
    }
}
