package mm.com.aeon.vcsaeon.beans;

public class PurchaseConfirmationReqBean {

    private int daPurchaseInfoId;
    private int customerId;
    private int daApplicationInfoId;

    public int getDaPurchaseInfoId() {
        return daPurchaseInfoId;
    }

    public void setDaPurchaseInfoId(int daPurchaseInfoId) {
        this.daPurchaseInfoId = daPurchaseInfoId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getDaApplicationInfoId() {
        return daApplicationInfoId;
    }

    public void setDaApplicationInfoId(int daApplicationInfoId) {
        this.daApplicationInfoId = daApplicationInfoId;
    }
}
