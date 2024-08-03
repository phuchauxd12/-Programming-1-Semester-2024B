package user;

import java.io.Serializable;

public class Client extends User implements Serializable {
    private Membership membership;
    private double totalSpending;

    public Client(String userName, String password, String name, String address, int phoneNum, String email,double totalSpending) {
        super(userName, password, name, address, phoneNum, email, ROLE.CLIENT);
        this.membership = new Membership();
        this.totalSpending = totalSpending;
    }

    public void updateMembership() {
        if (totalSpending >= 250000000) {
            membership.setMembershipType(Membership.MembershipType.Platinum);
            membership.setDiscount("15%");
        } else if (totalSpending >= 100000000) {
            membership.setMembershipType(Membership.MembershipType.Gold);
            membership.setDiscount("10%");
        } else if (totalSpending >= 30000000) {
            membership.setMembershipType(Membership.MembershipType.Silver);
            membership.setDiscount("5%");
        } else {
            membership.setMembershipType(Membership.MembershipType.Normal);
        }
    }

    public void updateTotalSpending() {
        // Implementation for updating total spending
    }

    public void viewHistory() {
        // Implementation for viewing history
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
                ", membership=" + membership.getMembershipType() +
                ", totalSpending=" + totalSpending +
                '}';
    }
}