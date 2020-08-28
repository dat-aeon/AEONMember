package mm.com.aeon.vcsaeon.beans;

import java.io.Serializable;

public class AutoReplyMessageBean implements Serializable {

    private String messageEng;
    private String messageMya;

    public String getMessageEng() {
        return messageEng;
    }

    public void setMessageEng(String messageEng) {
        this.messageEng = messageEng;
    }

    public String getMessageMya() {
        return messageMya;
    }

    public void setMessageMya(String messageMya) {
        this.messageMya = messageMya;
    }
}
