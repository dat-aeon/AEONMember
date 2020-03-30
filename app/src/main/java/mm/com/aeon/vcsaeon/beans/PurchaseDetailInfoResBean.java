package mm.com.aeon.vcsaeon.beans;

import java.util.List;

public class PurchaseDetailInfoResBean {

    private int daPurchaseInfoId;
    private int daApplicationInfoId;
    private int customerId;
    private String productCode;
    private String productName;
    private int daLoanTypeId;
    private String agreementNo;
    private String purchaseDate;
    private String model;
    private String brand;
    private double price;
    private double cashDownAmount;
    private int outletId;
    private String outletName;
    private String invoiceNo;
    private int agentId;
    private String agentName;
    private double financeAmount;
    private int financeTerm;
    private double processingFees;
    private double compulsoryAmount;
    private int status;
    private int settlementAmount;
    private String purchaseLocation;
    private List<PurchaseInfoAttachmentResBean> purchaseInfoAttachmentDtoList;
    private List<PurchaseProductInfoBean> purchaseInfoProductDtoList;
    private String createdBy;
    private String updatedBy;

    public String getPurchaseLocation() {
        return purchaseLocation;
    }

    public void setPurchaseLocation(String purchaseLocation) {
        this.purchaseLocation = purchaseLocation;
    }

    public List<PurchaseProductInfoBean> getPurchaseInfoProductDtoList() {
        return purchaseInfoProductDtoList;
    }

    public void setPurchaseInfoProductDtoList(List<PurchaseProductInfoBean> purchaseInfoProductDtoList) {
        this.purchaseInfoProductDtoList = purchaseInfoProductDtoList;
    }

    public double getFinanceAmount() {
        return financeAmount;
    }

    public void setFinanceAmount(double financeAmount) {
        this.financeAmount = financeAmount;
    }

    public int getFinanceTerm() {
        return financeTerm;
    }

    public void setFinanceTerm(int financeTerm) {
        this.financeTerm = financeTerm;
    }

    public double getProcessingFees() {
        return processingFees;
    }

    public void setProcessingFees(double processingFees) {
        this.processingFees = processingFees;
    }

    public double getCompulsoryAmount() {
        return compulsoryAmount;
    }

    public void setCompulsoryAmount(double compulsoryAmount) {
        this.compulsoryAmount = compulsoryAmount;
    }

    public int getDaPurchaseInfoId() {
        return daPurchaseInfoId;
    }

    public void setDaPurchaseInfoId(int daPurchaseInfoId) {
        this.daPurchaseInfoId = daPurchaseInfoId;
    }

    public int getDaApplicationInfoId() {
        return daApplicationInfoId;
    }

    public void setDaApplicationInfoId(int daApplicationInfoId) {
        this.daApplicationInfoId = daApplicationInfoId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getDaLoanTypeId() {
        return daLoanTypeId;
    }

    public void setDaLoanTypeId(int daLoanTypeId) {
        this.daLoanTypeId = daLoanTypeId;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCashDownAmount() {
        return cashDownAmount;
    }

    public void setCashDownAmount(double cashDownAmount) {
        this.cashDownAmount = cashDownAmount;
    }

    public int getOutletId() {
        return outletId;
    }

    public void setOutletId(int outletId) {
        this.outletId = outletId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(int settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public List<PurchaseInfoAttachmentResBean> getPurchaseInfoAttachmentDtoList() {
        return purchaseInfoAttachmentDtoList;
    }

    public void setPurchaseInfoAttachmentDtoList(List<PurchaseInfoAttachmentResBean> purchaseInfoAttachmentDtoList) {
        this.purchaseInfoAttachmentDtoList = purchaseInfoAttachmentDtoList;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
