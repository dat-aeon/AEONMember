package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;
import java.util.List;

public class ApplicationRegisterReqBean implements Serializable {

    private Integer daApplicationInfoId;
    private Integer daApplicationTypeId;
    private String name;
    private String dob;
    private String nrcNo;
    private String fatherName;
    private Integer nationality;
    private String nationalityOther;
    private Integer gender;
    private Integer maritalStatus;
    private String currentAddress;
    private String permanentAddress;
    private Integer typeOfResidence;
    private String typeOfResidenceOther;
    private Integer livingWith;
    private String livingWithOther;
    private Integer yearOfStayYear;
    private Integer yearOfStayMonth;
    private String mobileNo;
    private String residentTelNo;
    private String otherPhoneNo;
    private String email;
    private Integer customerId;
    private Integer daLoanTypeId;
    private Double financeAmount;
    private Integer financeTerm;
    private Integer daProductTypeId;
    private String productDescription;
    private Integer channelType;
    private Integer highestEducationTypeId;

    private String currentAddressFloor;
    private String currentAddressBuildingNo;
    private String currentAddressRoomNo;
    private String currentAddressStreet;
    private String currentAddressQtr;
    private Integer currentAddressTownship;
    private Integer currentAddressCity;

    private String permanentAddressBuildingNo;
    private String permanentAddressRoomNo;
    private String permanentAddressFloor;
    private String permanentAddressStreet;
    private String permanentAddressQtr;
    private Integer permanentAddressTownship;
    private Integer permanentAddressCity;

    private List<ApplicationInfoAttachmentFormBean> applicationInfoAttachmentDtoList;
    private OccupationDataFormBean applicantCompanyInfoDto;
    private EmergencyContactFormBean emergencyContactInfoDto;
    private GuarantorFormBean guarantorInfoDto;

    public Integer getHighestEducationTypeId() {
        return highestEducationTypeId;
    }

    public void setHighestEducationTypeId(Integer highestEducationTypeId) {
        this.highestEducationTypeId = highestEducationTypeId;
    }

    public Integer getDaApplicationInfoId() {
        return daApplicationInfoId;
    }

    public void setDaApplicationInfoId(Integer daApplicationInfoId) {
        this.daApplicationInfoId = daApplicationInfoId;
    }

    public String getCurrentAddressFloor() {
        return currentAddressFloor;
    }

    public void setCurrentAddressFloor(String currentAddressFloor) {
        this.currentAddressFloor = currentAddressFloor;
    }

    public String getCurrentAddressBuildingNo() {
        return currentAddressBuildingNo;
    }

    public void setCurrentAddressBuildingNo(String currentAddressBuildingNo) {
        this.currentAddressBuildingNo = currentAddressBuildingNo;
    }

    public String getCurrentAddressRoomNo() {
        return currentAddressRoomNo;
    }

    public void setCurrentAddressRoomNo(String currentAddressRoomNo) {
        this.currentAddressRoomNo = currentAddressRoomNo;
    }

    public String getCurrentAddressStreet() {
        return currentAddressStreet;
    }

    public void setCurrentAddressStreet(String currentAddressStreet) {
        this.currentAddressStreet = currentAddressStreet;
    }

    public String getCurrentAddressQtr() {
        return currentAddressQtr;
    }

    public void setCurrentAddressQtr(String currentAddressQtr) {
        this.currentAddressQtr = currentAddressQtr;
    }

    public Integer getCurrentAddressTownship() {
        return currentAddressTownship;
    }

    public void setCurrentAddressTownship(Integer currentAddressTownship) {
        this.currentAddressTownship = currentAddressTownship;
    }

    public Integer getCurrentAddressCity() {
        return currentAddressCity;
    }

    public void setCurrentAddressCity(Integer currentAddressCity) {
        this.currentAddressCity = currentAddressCity;
    }

    public String getPermanentAddressBuildingNo() {
        return permanentAddressBuildingNo;
    }

    public void setPermanentAddressBuildingNo(String permanentAddressBuildingNo) {
        this.permanentAddressBuildingNo = permanentAddressBuildingNo;
    }

    public String getPermanentAddressRoomNo() {
        return permanentAddressRoomNo;
    }

    public void setPermanentAddressRoomNo(String permanentAddressRoomNo) {
        this.permanentAddressRoomNo = permanentAddressRoomNo;
    }

    public String getPermanentAddressFloor() {
        return permanentAddressFloor;
    }

    public void setPermanentAddressFloor(String permanentAddressFloor) {
        this.permanentAddressFloor = permanentAddressFloor;
    }

    public String getPermanentAddressStreet() {
        return permanentAddressStreet;
    }

    public void setPermanentAddressStreet(String permanentAddressStreet) {
        this.permanentAddressStreet = permanentAddressStreet;
    }

    public String getPermanentAddressQtr() {
        return permanentAddressQtr;
    }

    public void setPermanentAddressQtr(String permanentAddressQtr) {
        this.permanentAddressQtr = permanentAddressQtr;
    }

    public Integer getPermanentAddressTownship() {
        return permanentAddressTownship;
    }

    public void setPermanentAddressTownship(Integer permanentAddressTownship) {
        this.permanentAddressTownship = permanentAddressTownship;
    }

    public Integer getPermanentAddressCity() {
        return permanentAddressCity;
    }

    public void setPermanentAddressCity(Integer permanentAddressCity) {
        this.permanentAddressCity = permanentAddressCity;
    }

    public Integer getDaApplicationTypeId() {
        return daApplicationTypeId;
    }

    public void setDaApplicationTypeId(Integer daApplicationTypeId) {
        this.daApplicationTypeId = daApplicationTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public Integer getNationality() {
        return nationality;
    }

    public void setNationality(Integer nationality) {
        this.nationality = nationality;
    }

    public String getNationalityOther() {
        return nationalityOther;
    }

    public void setNationalityOther(String nationalityOther) {
        this.nationalityOther = nationalityOther;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Integer getTypeOfResidence() {
        return typeOfResidence;
    }

    public void setTypeOfResidence(Integer typeOfResidence) {
        this.typeOfResidence = typeOfResidence;
    }

    public String getTypeOfResidenceOther() {
        return typeOfResidenceOther;
    }

    public void setTypeOfResidenceOther(String typeOfResidenceOther) {
        this.typeOfResidenceOther = typeOfResidenceOther;
    }

    public Integer getLivingWith() {
        return livingWith;
    }

    public void setLivingWith(Integer livingWith) {
        this.livingWith = livingWith;
    }

    public String getLivingWithOther() {
        return livingWithOther;
    }

    public void setLivingWithOther(String livingWithOther) {
        this.livingWithOther = livingWithOther;
    }

    public Integer getYearOfStayYear() {
        return yearOfStayYear;
    }

    public void setYearOfStayYear(Integer yearOfStayYear) {
        this.yearOfStayYear = yearOfStayYear;
    }

    public Integer getYearOfStayMonth() {
        return yearOfStayMonth;
    }

    public void setYearOfStayMonth(Integer yearOfStayMonth) {
        this.yearOfStayMonth = yearOfStayMonth;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getResidentTelNo() {
        return residentTelNo;
    }

    public void setResidentTelNo(String residentTelNo) {
        this.residentTelNo = residentTelNo;
    }

    public String getOtherPhoneNo() {
        return otherPhoneNo;
    }

    public void setOtherPhoneNo(String otherPhoneNo) {
        this.otherPhoneNo = otherPhoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getDaLoanTypeId() {
        return daLoanTypeId;
    }

    public void setDaLoanTypeId(Integer daLoanTypeId) {
        this.daLoanTypeId = daLoanTypeId;
    }

    public Double getFinanceAmount() {
        return financeAmount;
    }

    public void setFinanceAmount(Double financeAmount) {
        this.financeAmount = financeAmount;
    }

    public Integer getFinanceTerm() {
        return financeTerm;
    }

    public void setFinanceTerm(Integer financeTerm) {
        this.financeTerm = financeTerm;
    }

    public Integer getDaProductTypeId() {
        return daProductTypeId;
    }

    public void setDaProductTypeId(Integer daProductTypeId) {
        this.daProductTypeId = daProductTypeId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public List<ApplicationInfoAttachmentFormBean> getApplicationInfoAttachmentDtoList() {
        return applicationInfoAttachmentDtoList;
    }

    public void setApplicationInfoAttachmentDtoList(List<ApplicationInfoAttachmentFormBean> applicationInfoAttachmentDtoList) {
        this.applicationInfoAttachmentDtoList = applicationInfoAttachmentDtoList;
    }

    public OccupationDataFormBean getApplicantCompanyInfoDto() {
        return applicantCompanyInfoDto;
    }

    public void setApplicantCompanyInfoDto(OccupationDataFormBean applicantCompanyInfoDto) {
        this.applicantCompanyInfoDto = applicantCompanyInfoDto;
    }

    public EmergencyContactFormBean getEmergencyContactInfoDto() {
        return emergencyContactInfoDto;
    }

    public void setEmergencyContactInfoDto(EmergencyContactFormBean emergencyContactInfoDto) {
        this.emergencyContactInfoDto = emergencyContactInfoDto;
    }

    public GuarantorFormBean getGuarantorInfoDto() {
        return guarantorInfoDto;
    }

    public void setGuarantorInfoDto(GuarantorFormBean guarantorInfoDto) {
        this.guarantorInfoDto = guarantorInfoDto;
    }
}
