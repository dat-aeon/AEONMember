package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class SecurityQAReqBean implements Serializable {

    private int customerId;

    public SecurityQAReqBean(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
