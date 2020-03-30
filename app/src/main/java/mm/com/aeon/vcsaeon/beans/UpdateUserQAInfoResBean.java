package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;
import java.util.List;

public class UpdateUserQAInfoResBean implements Serializable {

    private int numOfSecQues;
    private int numOfAnsChar;
    private List<SecQAUpdateInfoResDto> customerSecurityQuestionDtoList;

    public int getNumOfSecQues() {
        return numOfSecQues;
    }

    public void setNumOfSecQues(int numOfSecQues) {
        this.numOfSecQues = numOfSecQues;
    }

    public int getNumOfAnsChar() {
        return numOfAnsChar;
    }

    public void setNumOfAnsChar(int numOfAnsChar) {
        this.numOfAnsChar = numOfAnsChar;
    }

    public List<SecQAUpdateInfoResDto> getCustomerSecurityQuestionDtoList() {
        return customerSecurityQuestionDtoList;
    }

    public void setCustomerSecurityQuestionDtoList(List<SecQAUpdateInfoResDto> customerSecurityQuestionDtoList) {
        this.customerSecurityQuestionDtoList = customerSecurityQuestionDtoList;
    }
}
