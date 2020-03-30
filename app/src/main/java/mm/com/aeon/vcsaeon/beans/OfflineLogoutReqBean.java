package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class OfflineLogoutReqBean implements Serializable {

    private int customerId;
    private String logoutTime;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }

}
