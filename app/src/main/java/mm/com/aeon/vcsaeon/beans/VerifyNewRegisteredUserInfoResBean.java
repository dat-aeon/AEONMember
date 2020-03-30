package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class VerifyNewRegisteredUserInfoResBean implements Serializable {

    private String verifyStatus; //"VALID" | "NOT_VALID"
    private String customerNo;

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

}
