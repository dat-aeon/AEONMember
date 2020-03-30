package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class CustomerInfoBean implements Serializable {

    private String memberStatus;
    private String hotlinePhone;
    private String memberPhoneNo;

    public String getMemberStatus() {
        return memberStatus;
    }

    public String getHotlinePhone() {
        return hotlinePhone;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void setHotlinePhone(String hotlinePhone) {
        this.hotlinePhone = hotlinePhone;
    }

    public String getMemberPhoneNo() {
        return memberPhoneNo;
    }

    public void setMemberPhoneNo(String memberPhoneNo) {
        this.memberPhoneNo = memberPhoneNo;
    }
}


