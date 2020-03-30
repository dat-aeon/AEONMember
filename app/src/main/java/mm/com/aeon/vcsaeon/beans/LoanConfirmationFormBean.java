package mm.com.aeon.vcsaeon.beans;

public class LoanConfirmationFormBean {
    private Integer customerId;
    private Integer daLoanTypeId;
    private Double financeAmount;
    private Integer financeTerm;
    private Integer daProductTypeId;
    private String productDescription;
    private Integer channelType;
    private Integer loanTerm;
    private Integer loanTermId;

    private byte[] nrcFront;
    private byte[] nrcBack;
    private byte[] incomeProof;
    private byte[] residentProof;
    private byte[] guarantorFront;
    private byte[] guarantorBack;
    private byte[] criminalProof;
    private byte[] applicantPhoto;
    private byte[] applicatnSignature;

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

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Integer getLoanTermId() {
        return loanTermId;
    }

    public void setLoanTermId(Integer loanTermId) {
        this.loanTermId = loanTermId;
    }

    public byte[] getNrcFront() {
        return nrcFront;
    }

    public void setNrcFront(byte[] nrcFront) {
        this.nrcFront = nrcFront;
    }

    public byte[] getNrcBack() {
        return nrcBack;
    }

    public void setNrcBack(byte[] nrcBack) {
        this.nrcBack = nrcBack;
    }

    public byte[] getIncomeProof() {
        return incomeProof;
    }

    public void setIncomeProof(byte[] incomeProof) {
        this.incomeProof = incomeProof;
    }

    public byte[] getResidentProof() {
        return residentProof;
    }

    public void setResidentProof(byte[] residentProof) {
        this.residentProof = residentProof;
    }

    public byte[] getGuarantorFront() {
        return guarantorFront;
    }

    public void setGuarantorFront(byte[] guarantorFront) {
        this.guarantorFront = guarantorFront;
    }

    public byte[] getGuarantorBack() {
        return guarantorBack;
    }

    public void setGuarantorBack(byte[] guarantorBack) {
        this.guarantorBack = guarantorBack;
    }

    public byte[] getCriminalProof() {
        return criminalProof;
    }

    public void setCriminalProof(byte[] criminalProof) {
        this.criminalProof = criminalProof;
    }

    public byte[] getApplicantPhoto() {
        return applicantPhoto;
    }

    public void setApplicantPhoto(byte[] applicantPhoto) {
        this.applicantPhoto = applicantPhoto;
    }

    public byte[] getApplicatnSignature() {
        return applicatnSignature;
    }

    public void setApplicatnSignature(byte[] applicatnSignature) {
        this.applicatnSignature = applicatnSignature;
    }
}
