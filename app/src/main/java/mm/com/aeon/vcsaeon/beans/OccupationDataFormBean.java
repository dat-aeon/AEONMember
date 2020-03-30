package mm.com.aeon.vcsaeon.beans;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DEFAULT_ZERO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ONE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ZERO;

public class OccupationDataFormBean {

    private Integer daApplicantCompanyInfoId;
    private Integer daApplicationInfoId;
    private String companyName;
    private String companyAddress;
    private String companyTelNo;
    private String contactTimeFrom;
    private String contactTimeTo;
    private String department;
    private String position;
    private Integer yearOfServiceYear;
    private Integer yearOfServiceMonth;
    private Integer companyStatus;
    private String companyStatusOther;
    private Double monthlyBasicIncome;
    private Double otherIncome;
    private Double totalIncome;
    private Integer salaryDay;

    private String companyAddressBuildingNo;
    private String companyAddressRoomNo;
    private String companyAddressFloor;
    private String companyAddressStreet;
    private String companyAddressQtr;
    private Integer companyAddressTownship;
    private Integer companyAddressCity;

    public String getCompanyAddressBuildingNo() {
        return companyAddressBuildingNo;
    }

    public void setCompanyAddressBuildingNo(String companyAddressBuildingNo) {
        this.companyAddressBuildingNo = companyAddressBuildingNo;
    }

    public String getCompanyAddressRoomNo() {
        return companyAddressRoomNo;
    }

    public void setCompanyAddressRoomNo(String companyAddressRoomNo) {
        this.companyAddressRoomNo = companyAddressRoomNo;
    }

    public String getCompanyAddressFloor() {
        return companyAddressFloor;
    }

    public void setCompanyAddressFloor(String companyAddressFloor) {
        this.companyAddressFloor = companyAddressFloor;
    }

    public String getCompanyAddressStreet() {
        return companyAddressStreet;
    }

    public void setCompanyAddressStreet(String companyAddressStreet) {
        this.companyAddressStreet = companyAddressStreet;
    }

    public String getCompanyAddressQtr() {
        return companyAddressQtr;
    }

    public void setCompanyAddressQtr(String companyAddressQtr) {
        this.companyAddressQtr = companyAddressQtr;
    }

    public Integer getCompanyAddressTownship() {
        return companyAddressTownship;
    }

    public void setCompanyAddressTownship(Integer companyAddressTownship) {
        this.companyAddressTownship = companyAddressTownship;
    }

    public Integer getCompanyAddressCity() {
        return companyAddressCity;
    }

    public void setCompanyAddressCity(Integer companyAddressCity) {
        this.companyAddressCity = companyAddressCity;
    }

    public Integer getDaApplicantCompanyInfoId() {
        return daApplicantCompanyInfoId;
    }

    public void setDaApplicantCompanyInfoId(Integer daApplicantCompanyInfoId) {
        this.daApplicantCompanyInfoId = daApplicantCompanyInfoId;
    }

    public Integer getDaApplicationInfoId() {
        return daApplicationInfoId;
    }

    public void setDaApplicationInfoId(Integer daApplicationInfoId) {
        this.daApplicationInfoId = daApplicationInfoId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyTelNo() {
        return companyTelNo;
    }

    public void setCompanyTelNo(String companyTelNo) {
        this.companyTelNo = companyTelNo;
    }

    public String getContactTimeFrom() {
        return contactTimeFrom;
    }

    public void setContactTimeFrom(String contactTimeFrom) {
        this.contactTimeFrom = contactTimeFrom;
    }

    public String getContactTimeTo() {
        return contactTimeTo;
    }

    public void setContactTimeTo(String contactTimeTo) {
        this.contactTimeTo = contactTimeTo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getYearOfServiceYear() {
        if(yearOfServiceYear == null){
            return ZERO;
        }
        return yearOfServiceYear;
    }

    public void setYearOfServiceYear(Integer yearOfServiceYear) {
        this.yearOfServiceYear = yearOfServiceYear;
    }

    public Integer getYearOfServiceMonth() {
        if(yearOfServiceMonth == null){
            return ZERO;
        }
        return yearOfServiceMonth;
    }

    public void setYearOfServiceMonth(Integer yearOfServiceMonth) {
        this.yearOfServiceMonth = yearOfServiceMonth;
    }

    public Integer getCompanyStatus() {
        if(companyStatus == null){
            return ONE;
        }
        return companyStatus;
    }

    public void setCompanyStatus(Integer companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getCompanyStatusOther() {
        return companyStatusOther;
    }

    public void setCompanyStatusOther(String companyStatusOther) {
        this.companyStatusOther = companyStatusOther;
    }

    public Double getMonthlyBasicIncome() {
        if(monthlyBasicIncome == null){
            return DEFAULT_ZERO;
        }
        return monthlyBasicIncome;
    }

    public void setMonthlyBasicIncome(Double monthlyBasicIncome) {
        this.monthlyBasicIncome = monthlyBasicIncome;
    }

    public Double getOtherIncome() {
        if(otherIncome == null){
            return DEFAULT_ZERO;
        }
        return otherIncome;
    }

    public void setOtherIncome(Double otherIncome) {
        this.otherIncome = otherIncome;
    }

    public Double getTotalIncome() {
        if(totalIncome == null){
            return DEFAULT_ZERO;
        }
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Integer getSalaryDay() {
        if(salaryDay == null) {
            salaryDay = 1;
        }
        return salaryDay;
    }

    public void setSalaryDay(Integer salaryDay) {
        this.salaryDay = salaryDay;
    }
}
