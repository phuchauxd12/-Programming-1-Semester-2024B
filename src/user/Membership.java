package user;

import java.io.Serializable;

public class Membership implements Serializable {
    private MembershipType membershipType;
    private String discount;

    public static enum MembershipType {
        Normal,
        Silver,
        Gold,
        Platinum

    }
    public Membership() {
        this.membershipType = MembershipType.Normal;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public String getDiscount() {
        return discount;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Membership{" +
                "membershipType=" + membershipType +
                ", discount='" + discount + '\'' +
                '}';
    }
}
