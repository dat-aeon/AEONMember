package mm.com.aeon.vcsaeon.delegates;

public interface DAEnquiryDelegate {
    void onViewApplicationDetail(int daApplicationInfoId);
    void onViewPurchaseDetail(int daApplicationInfoId);
    void onEditAttachments(int daApplicationInfoId);
    void onCancel(int daApplicationInfoId, String language);
}
