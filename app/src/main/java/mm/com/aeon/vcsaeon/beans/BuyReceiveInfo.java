package mm.com.aeon.vcsaeon.beans;

public class BuyReceiveInfo {

    private String agentName;
    private String brand;
    private String price;
    private String category;
    private String phoneNo;
    private String urlLink;
    private String agentLogoUrl;
    private String receivedTime;

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getAgentLogoUrl() {
        return agentLogoUrl;
    }

    public void setAgentLogoUrl(String agentLogoUrl) {
        this.agentLogoUrl = agentLogoUrl;
    }
}
