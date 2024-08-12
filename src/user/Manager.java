package user;

import car.Car;
import autoPart.autoPart;
import services.Service;
import services.ServiceList;
import transaction.SaleTransaction;
import transaction.SaleTransactionList;
import utils.CarAndAutoPartMenu;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Manager extends User {

    private SaleTransactionList saleTransactionList;
    private ServiceList serviceList;

    public Manager(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status, SaleTransactionList saleTransactionList, ServiceList serviceList) {
        super(userName, password, name, dob, address, phoneNum, email, userType, status);
        this.saleTransactionList = saleTransactionList;
        this.serviceList = serviceList;
    }

    // Sale Transactions
    public void viewSaleTransactions(LocalDate startDate, LocalDate endDate) {
        List<SaleTransaction> transactions = saleTransactionList.getSaleTransactionsBetween(startDate, endDate);
        transactions.forEach(transaction -> System.out.println(transaction.getFormattedSaleTransactionDetails()));
    }

    public void searchSaleTransaction(String transactionId) {
        saleTransactionList.getAllSaleTransactions().stream()
                .filter(transaction -> transaction.getTransactionId().equals(transactionId))
                .forEach(transaction -> System.out.println(transaction.getFormattedSaleTransactionDetails()));
    }

    // Services
    public void viewServices(LocalDate startDate, LocalDate endDate) {
        List<Service> services = serviceList.getServicesBetween(startDate, endDate);
        services.forEach(service -> System.out.println(service.getFormattedServiceDetails()));
    }

    public void searchService(String serviceId) {
        serviceList.getAllServices().stream()
                .filter(service -> service.getServiceId().equals(serviceId))
                .forEach(service -> System.out.println(service.getFormattedServiceDetails()));
    }

    // Cars
//    public void searchCar(String carId) {
//        CarAndAutoPartMenu.searchCar(carId);
//    }
//
//    public void viewAllCars() {
//        CarAndAutoPartMenu.viewAllCars();
//    }
//
//    // Auto Parts
//    public void searchAutoPart(String partId) {
//        CarAndAutoPartMenu.searchAutoPart(partId);
//    }
//
//    public void viewAutoParts() {
//        CarAndAutoPartMenu.viewAutoParts();
//    }


    // Statistics
    public long calculateCarsSoldInMonth(LocalDate month) {
        return CarAndAutoPartMenu.getCarsList().stream()
                .filter(car -> car.getSoldDate().getMonth().equals(month.getMonth()) && car.getSoldDate().getYear() == month.getYear())
                .count();
    }

    public double calculateRevenue(LocalDate startDate, LocalDate endDate) {
        double totalSalesRevenue = saleTransactionList.getSaleTransactionsBetween(startDate, endDate).stream()
                .mapToDouble(SaleTransaction::getTotalAmount)
                .sum();
        double totalServiceRevenue = serviceList.getServicesBetween(startDate, endDate).stream()
                .mapToDouble(Service::getServiceCost)
                .sum();
        return totalSalesRevenue + totalServiceRevenue;
    }

    public double calculateServiceRevenueByMechanic(String mechanicId, LocalDate startDate, LocalDate endDate) {
        return serviceList.getServicesBetween(startDate, endDate).stream()
                .filter(service -> service.getMechanicId().equals(mechanicId))
                .mapToDouble(Service::getServiceCost)
                .sum();
    }

    public double calculateTransactionRevenueBySalesperson(String salespersonId, LocalDate startDate, LocalDate endDate) {
        return saleTransactionList.getSaleTransactionsBetween(startDate, endDate).stream()
                .filter(transaction -> transaction.getSalespersonId().equals(salespersonId))
                .mapToDouble(SaleTransaction::getTotalAmount)
                .sum();
    }


    public List<Car> listCarsSold(LocalDate startDate, LocalDate endDate) {
        return CarAndAutoPartMenu.getCarsList().stream()
                .filter(car -> !car.getSoldDate().isBefore(startDate) && !car.getSoldDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<SaleTransaction> listTransactions(LocalDate startDate, LocalDate endDate) {
        return saleTransactionList.getSaleTransactionsBetween(startDate, endDate);
    }

    public List<Service> listServices(LocalDate startDate, LocalDate endDate) {
        return serviceList.getServicesBetween(startDate, endDate);
    }

    public List<autoPart> listAutoPartsSold(LocalDate startDate, LocalDate endDate) {
        return CarAndAutoPartMenu.getAutoPartsList().stream()
                .filter(part -> !part.getSoldDate().isBefore(startDate.atStartOfDay()) && !part.getSoldDate().isAfter(endDate.atStartOfDay()))
                .collect(Collectors.toList());
    }
}
