package user;

import java.io.Serializable;

public class Membership implements Serializable {
    private MembershipType membershipType;
    private double discount;

    public static enum MembershipType {
        Normal,
        Silver,
        Gold,
        Platinum
    }

    public Membership() {
        this.membershipType = MembershipType.Normal;
        setDiscount(this.membershipType);
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
        setDiscount(membershipType);
    }

    private void setDiscount(MembershipType membershipType) {
        switch (membershipType) {
            case Normal:
                this.discount = 0.0;
                break;
            case Silver:
                this.discount = 0.10;
                break;
            case Gold:
                this.discount = 0.15;
                break;
            case Platinum:
                this.discount = 0.20;
                break;
            default:
                this.discount = 0.0;
                break;
        }
    }

    @Override
    public String toString() {
        return "Membership{" +
                "membershipType=" + membershipType +
                ", discount=" + discount +
                '}';
    }
}
