package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;
import java.util.List;

public class NewMemberRegistrationInfoReqBean implements Serializable {

    private String dob;
    private String nrcNo;
    private String phoneNo;
    private String name;
    private String password;
    private List<AnsweredSecurityQuestionReqBean> customerSecurityQuestionDtoList;
    private AppUsageInfoReqBean appUsageInfoDto;

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNrcNo() {
        return nrcNo;
    }

    public void setNrcNo(String nrcNo) {
        this.nrcNo = nrcNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AnsweredSecurityQuestionReqBean> getCustomerSecurityQuestionDtoList() {
        return customerSecurityQuestionDtoList;
    }

    public void setCustomerSecurityQuestionDtoList(List<AnsweredSecurityQuestionReqBean> customerSecurityQuestionDtoList) {
        this.customerSecurityQuestionDtoList = customerSecurityQuestionDtoList;
    }

    public AppUsageInfoReqBean getAppUsageInfoDto() {
        return appUsageInfoDto;
    }

    public void setAppUsageInfoDto(AppUsageInfoReqBean appUsageInfoDto) {
        this.appUsageInfoDto = appUsageInfoDto;
    }
}
