package mm.com.aeon.vcsaeon.beans;

public class L2MessageUnReadCountReqBean {
    private int customerId;

    public L2MessageUnReadCountReqBean(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
