package mm.com.aeon.vcsaeon.repositories;

import mm.com.aeon.vcsaeon.beans.AskProductMessageCountReqBean;
import mm.com.aeon.vcsaeon.beans.AskProductMessageCountResBean;
import mm.com.aeon.vcsaeon.beans.L2MessageUnReadCountReqBean;
import mm.com.aeon.vcsaeon.beans.L2MessageUnReadCountResBean;
import mm.com.aeon.vcsaeon.delegates.MessageCountDisplayDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ZERO;

public class MessagingRepository {

    private MessagingRepository() {
    }

    private static MessagingRepository messagingRepository;

    public static MessagingRepository getInstance() {
        if (messagingRepository == null) {
            return new MessagingRepository();
        }
        return messagingRepository;
    }

    Service service = APIClient.getUserService();

    public void getL2UnreadMessageCount(int customerId, final MessageCountDisplayDelegate delegate) {
        Call<BaseResponse<L2MessageUnReadCountResBean>> req =
                service.getL2UnreadMessageCount(new L2MessageUnReadCountReqBean(customerId));
        req.enqueue(new Callback<BaseResponse<L2MessageUnReadCountResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<L2MessageUnReadCountResBean>> call, Response<BaseResponse<L2MessageUnReadCountResBean>> response) {
                if (response.body().isResponseOk()) {
                    delegate.onDisplayL2MessageCount(response.body().getData().getLevel2MessageUnReadCount());
                } else {
                    delegate.onDisplayL2MessageCount(ZERO);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<L2MessageUnReadCountResBean>> call, Throwable t) {
                delegate.onDisplayL2MessageCount(ZERO);
            }
        });
    }

    public void getAskProductMessageCount(int customerId, final MessageCountDisplayDelegate delegate) {
        Call<BaseResponse<AskProductMessageCountResBean>> req =
                service.getAskProductUnreadMessageCount(new AskProductMessageCountReqBean(customerId));
        req.enqueue(new Callback<BaseResponse<AskProductMessageCountResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<AskProductMessageCountResBean>> call, Response<BaseResponse<AskProductMessageCountResBean>> response) {
                if (response.body().isResponseOk()) {
                    delegate.onDisplayProductAskMsgCount(response.body().getData().getAskProductUnReadCount());
                } else {
                    delegate.onDisplayProductAskMsgCount(ZERO);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<AskProductMessageCountResBean>> call, Throwable t) {
                delegate.onDisplayProductAskMsgCount(ZERO);
            }
        });
    }

}
