package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class StatusReadFlagReq implements Serializable {
    private int customerId;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
