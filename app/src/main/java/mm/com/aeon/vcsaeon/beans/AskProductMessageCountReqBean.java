package mm.com.aeon.vcsaeon.beans;

public class AskProductMessageCountReqBean {

    private int customerId;

    public AskProductMessageCountReqBean(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
