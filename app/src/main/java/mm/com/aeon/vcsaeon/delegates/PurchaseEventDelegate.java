package mm.com.aeon.vcsaeon.delegates;

public interface PurchaseEventDelegate {
    void onTouchPhoneCall(int agentId, String phoneNo, int messageId);
    void onClickUrlLink(String url);
    void onTouchReadMore(int currentIndex, int endingIndex);
}
