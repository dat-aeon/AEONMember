package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class ResetPwdAnsweredSecQuesReqBean implements Serializable {

    private int secQuesId;
    private String answer;

    public int getSecQuesId() {
        return secQuesId;
    }

    public void setSecQuesId(int secQuesId) {
        this.secQuesId = secQuesId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
