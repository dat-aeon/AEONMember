package mm.com.aeon.vcsaeon.beans;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ONE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;

public class EmergencyContactFormBean {

    private Integer daEmergencyContactInfoId;
    private Integer daApplicationInfoId;
    private String name;
    private Integer relationship;
    private String relationshipOther;
    private String currentAddress;
    private String mobileNo;
    private String residentTelNo;
    private String otherPhoneNo;

    private String currentAddressBuildingNo;
    private String currentAddressRoomNo;
    private String currentAddressFloor;
    private String currentAddressStreet;
    private String currentAddressQtr;
    private Integer currentAddressTownship;
    private Integer currentAddressCity;

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

    public String getCurrentAddressFloor() {
        return currentAddressFloor;
    }

    public void setCurrentAddressFloor(String currentAddressFloor) {
        this.currentAddressFloor = currentAddressFloor;
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
        if(currentAddressTownship == null){
            currentAddressTownship = 0;
        }
        return currentAddressTownship;
    }

    public void setCurrentAddressTownship(Integer currentAddressTownship) {
        this.currentAddressTownship = currentAddressTownship;
    }

    public Integer getCurrentAddressCity() {
        if(currentAddressCity == null){
            currentAddressCity = 0;
        }
        return currentAddressCity;
    }

    public void setCurrentAddressCity(Integer currentAddressCity) {
        this.currentAddressCity = currentAddressCity;
    }

    public Integer getDaEmergencyContactInfoId() {
        return daEmergencyContactInfoId;
    }

    public void setDaEmergencyContactInfoId(Integer daEmergencyContactInfoId) {
        this.daEmergencyContactInfoId = daEmergencyContactInfoId;
    }

    public Integer getDaApplicationInfoId() {
        return daApplicationInfoId;
    }

    public void setDaApplicationInfoId(Integer daApplicationInfoId) {
        this.daApplicationInfoId = daApplicationInfoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRelationship() {
        if(relationship == null){
            return ONE;
        }
        return relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }

    public String getRelationshipOther() {
        return relationshipOther;
    }

    public void setRelationshipOther(String relationshipOther) {
        this.relationshipOther = relationshipOther;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
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
}
