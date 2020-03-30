package mm.com.aeon.vcsaeon.beans;

import java.util.List;

public class PurchaseAttachEditReqBean {

    private int daApplicationInfoId;
    private List<PurchaseAttachInfoEditReqBean> applicationInfoAttachmentDtoList;

    public int getDaApplicationInfoId() {
        return daApplicationInfoId;
    }

    public void setDaApplicationInfoId(int daApplicationInfoId) {
        this.daApplicationInfoId = daApplicationInfoId;
    }

    public List<PurchaseAttachInfoEditReqBean> getApplicationInfoAttachmentDtoList() {
        return applicationInfoAttachmentDtoList;
    }

    public void setApplicationInfoAttachmentDtoList(List<PurchaseAttachInfoEditReqBean> applicationInfoAttachmentDtoList) {
        this.applicationInfoAttachmentDtoList = applicationInfoAttachmentDtoList;
    }
}
