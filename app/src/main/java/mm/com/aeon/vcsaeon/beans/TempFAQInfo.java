package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;
import java.util.List;

public class TempFAQInfo implements Serializable {

    private String faqCategory;
    private List<TempFAQInfoResDto> faqInfoResInfoList;

    public String getFaqCategory() {
        return faqCategory;
    }

    public void setFaqCategory(String faqCategory) {
        this.faqCategory = faqCategory;
    }

    public List<TempFAQInfoResDto> getFaqInfoResInfoList() {
        return faqInfoResInfoList;
    }

    public void setFaqInfoResInfoList(List<TempFAQInfoResDto> faqInfoResInfoList) {
        this.faqInfoResInfoList = faqInfoResInfoList;
    }
}
