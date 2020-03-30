package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;
import java.util.List;

public class ExistedMemberRegistrationInfoReqBean implements Serializable {

    private String dob;
    private String nrcNo;
    private String phoneNo;
    private String password;
    private byte[] photoByte;
    private List<AnsweredSecurityQuestionReqBean> customerSecurityQuestionDtoList;
    private AppUsageInfoReqBean appUsageInfoDto;

    public String getDob() {
        return dob;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getPhotoByte() {
        return photoByte;
    }

    public void setPhotoByte(byte[] photoByte) {
        this.photoByte = photoByte;
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
