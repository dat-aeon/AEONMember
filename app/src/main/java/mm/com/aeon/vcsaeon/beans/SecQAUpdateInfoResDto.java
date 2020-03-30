package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class SecQAUpdateInfoResDto implements Serializable {

    private int custSecQuesId;
    private int secQuesId;
    private int customerId;
    private String questionMyan;
    private String questionEng;
    private String answer;

    public int getCustSecQuesId() {
        return custSecQuesId;
    }

    public void setCustSecQuesId(int custSecQuesId) {
        this.custSecQuesId = custSecQuesId;
    }

    public int getSecQuesId() {
        return secQuesId;
    }

    public void setSecQuesId(int secQuesId) {
        this.secQuesId = secQuesId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getQuestionMyan() {
        return questionMyan;
    }

    public void setQuestionMyan(String questionMyan) {
        this.questionMyan = questionMyan;
    }

    public String getQuestionEng() {
        return questionEng;
    }

    public void setQuestionEng(String questionEng) {
        this.questionEng = questionEng;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
