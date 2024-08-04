package user;

import transaction.SaleTransaction;
import transaction.SaleTransactionList;

import java.time.LocalDate;
import java.util.List;

public class Client extends User {
    private Membership membership;
    private double totalSpending;

    public Client(String userName, String password, String name, LocalDate dob, String address, int phoneNum, String email, ROLE userType, String status, Membership membership) {
        super(userName, password, name, dob, address, phoneNum, email, userType, status);
        this.membership = membership;
        this.totalSpending = 0.0;
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

    public void viewHistory(SaleTransactionList transactionList) {
        List<SaleTransaction> transactions = transactionList.getAllSaleTransactions();
        System.out.println("Transaction history for client ID " + userName + ":");
        boolean hasTransactions = false;
        for (SaleTransaction transaction : transactions) {
            if (transaction.getClientId().equals(userName)) {
                System.out.println(transaction.getFormattedSaleTransactionDetails());
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
