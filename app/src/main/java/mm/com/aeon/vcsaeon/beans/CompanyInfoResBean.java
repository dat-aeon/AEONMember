package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class CompanyInfoResBean implements Serializable {

    private Integer companyInfoId;
    private String addressEn;
    private String addressMm;
    private String hotlinePhone;
    private String webAddress;
    private String socialMediaAddress;
    private String aboutCompanyEn;
    private String aboutCompanyMm;

    public Integer getCompanyInfoId() {
        return companyInfoId;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public String getAddressMm() {
        return addressMm;
    }

    public String getHotlinePhone() {
        return hotlinePhone;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public String getSocialMediaAddress() {
        return socialMediaAddress;
    }

    public String getAboutCompanyEn() {
        return aboutCompanyEn;
    }

    public String getAboutCompanyMm() {
        return aboutCompanyMm;
    }
}
