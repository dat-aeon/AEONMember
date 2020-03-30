package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class CheckAccountLockReqBean implements Serializable {

    private String phoneNo;
    private String nrcNo;

    public CheckAccountLockReqBean(String phoneNo, String nrcNo) {
        this.phoneNo = phoneNo;
        this.nrcNo = nrcNo;
    }
}
