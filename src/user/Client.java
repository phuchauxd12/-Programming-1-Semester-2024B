package user;

import services.Service;
import services.ServiceList;
import transaction.SaleTransaction;
import transaction.SaleTransactionList;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Client extends User {
    private Membership membership;
    private double totalSpending;
    private SaleTransactionList saleTransactionList;
    private ServiceList serviceList;

    public Client(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status, Membership membership) {
        super(userName, password, name, dob, address, phoneNum, email, userType, status);
        this.membership = membership;
        this.totalSpending = 0.0;
    }

    public SaleTransactionList getSaleTransactionList() {
        return saleTransactionList;
    }

    public ServiceList getServiceList() {
        return serviceList;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public double getTotalSpending() {
        return totalSpending;
    }

    public void updateMembership() {
        if (totalSpending < 30000000) {
            membership.setMembershipType(Membership.MembershipType.Normal);
        } else if (totalSpending <= 100000000) {
            membership.setMembershipType(Membership.MembershipType.Silver);
        } else if (totalSpending <= 250000000) {
            membership.setMembershipType(Membership.MembershipType.Gold);
        } else {
            membership.setMembershipType(Membership.MembershipType.Platinum);
        }
    }

    public void updateTotalSpending(double amount) {
        this.totalSpending += amount;
        updateMembership();
    }

    public void viewTransactionsHistory() {
        List<SaleTransaction> transactions = saleTransactionList.getAllSaleTransactions();
        System.out.println("Transaction history for client ID " + userID + ":");
        boolean hasTransactions = false;
        for (SaleTransaction transaction : transactions) {
            if (transaction.getClientId().equals(userID)) {
                System.out.println(transaction.getFormattedSaleTransactionDetails());
                hasTransactions = true;
            }
        }
        if (!hasTransactions) {
            System.out.println("No transactions found for client ID " + userID);
        }
    }

    public void viewTransactionsHistoryInSpecificPeriod() {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate;
        LocalDate endDate;
        // Get start date
        while (true) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine());
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid start date format. Please try again.");
            }
        }

        // Get end date
        while (true) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine());
                if (!endDate.isBefore(startDate)) {
                    break;
                }
                System.out.println("End date cannot be before start date. Please try again.");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid end date format. Please try again.");
            }
        }
        List<SaleTransaction> transactionsInRange = saleTransactionList.getSaleTransactionsBetween(startDate, endDate);
        for (SaleTransaction transaction : transactionsInRange) {
            System.out.println(transaction.getFormattedSaleTransactionDetails());
        }
    }

    public void viewServiceHistoryInSpecificPeriod() {
        Scanner scanner = new Scanner(System.in);
        LocalDate startDate;
        LocalDate endDate;
        // Get start date
        while (true) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine());
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid start date format. Please try again.");
            }
        }

        // Get end date
        while (true) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine());
                if (!endDate.isBefore(startDate)) {
                    break;
                }
                System.out.println("End date cannot be before start date. Please try again.");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid end date format. Please try again.");
            }
        }
        List<Service> servicesInRange = serviceList.getServicesBetween(startDate, endDate);
        for (Service service : servicesInRange) {
            System.out.println(service.getFormattedServiceDetails());
        }
    }

    public void viewServiceHistory() {
        List<Service> services = serviceList.getAllServices();
        System.out.println("Service history for client ID " + userName + ":");
        boolean hasTransactions = false;
        for (Service service : services) {
            if (service.getClientId().equals(userName)) {
                System.out.println(service.getFormattedServiceDetails());
                hasTransactions = true;
            }
        }
        if (!hasTransactions) {
            System.out.println("No transactions found for client ID " + userID);
        }
    }


    @Override
    public String toString() {
        return "Client{" +
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
                ", membership=" + membership +
                ", totalSpending=" + totalSpending +
                '}';
    }
}
