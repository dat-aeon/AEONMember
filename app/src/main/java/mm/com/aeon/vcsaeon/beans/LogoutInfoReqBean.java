package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class LogoutInfoReqBean implements Serializable {

    /*private String appUsageDetailId;
    private String logoutTime;*/
    private int customerId;

    public LogoutInfoReqBean(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
