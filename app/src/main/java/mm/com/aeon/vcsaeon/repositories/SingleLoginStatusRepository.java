package mm.com.aeon.vcsaeon.repositories;

import androidx.lifecycle.MutableLiveData;
import mm.com.aeon.vcsaeon.beans.SingleLoginCheck;
import mm.com.aeon.vcsaeon.beans.SingleLoginStatus;
import mm.com.aeon.vcsaeon.delegates.MultipleCheckDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleLoginStatusRepository {

    private SingleLoginStatusRepository() {
    }

    private static SingleLoginStatusRepository singleLoginStatusRepository;

    public static SingleLoginStatusRepository getInstance() {
        if (singleLoginStatusRepository == null) {
            singleLoginStatusRepository = new SingleLoginStatusRepository();
        }
        return singleLoginStatusRepository;
    }

    final MutableLiveData<SingleLoginStatus> mutableLiveData
            = new MutableLiveData<>();
    Service service = APIClient.getUserService();

    public MutableLiveData<SingleLoginStatus> getSingleLoginStatus(SingleLoginCheck singleLoginCheck, final MultipleCheckDelegate delegate) {

        Call<BaseResponse<SingleLoginStatus>> request
                = service.getSingleLoginStatus(singleLoginCheck);

        request.enqueue(new Callback<BaseResponse<SingleLoginStatus>>() {
            @Override
            public void onResponse(Call<BaseResponse<SingleLoginStatus>> call,
                                   Response<BaseResponse<SingleLoginStatus>> response) {
                if (response.body().isResponseOk()) {
                    mutableLiveData.setValue(response.body().getData());
                    SingleLoginStatus singleLoginStatus = response.body().getData();
                    if (singleLoginStatus != null) {
                        if (singleLoginStatus.isLogoutFlag()) {
                            delegate.onDialogDisplay();
                        }
                    }
                } else {
                    mutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<SingleLoginStatus>> call, Throwable t) {
                mutableLiveData.setValue(null);
            }
        });

        return mutableLiveData;
    }
}
