package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class ApplicationLastInfoReqBean implements Serializable {
    private Integer customerId;
    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
