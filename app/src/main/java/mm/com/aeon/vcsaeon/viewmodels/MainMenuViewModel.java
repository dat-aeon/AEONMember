package mm.com.aeon.vcsaeon.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.HowToUseVideoResBean;
import mm.com.aeon.vcsaeon.beans.SingleLoginCheck;
import mm.com.aeon.vcsaeon.beans.SingleLoginStatus;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.repositories.SingleLoginStatusRepository;

public class MainMenuViewModel extends ViewModel {

    private MutableLiveData<SingleLoginStatus> mutableLiveData;
    private SingleLoginStatusRepository singleLoginStatusRepository;

    public void init(Context context) {
        if (mutableLiveData != null) {
            return;
        }

        SingleLoginCheck singleLoginCheck
                = PreferencesManager.getSingleLoginCheck(context);

        singleLoginStatusRepository = SingleLoginStatusRepository.getInstance();
        mutableLiveData = singleLoginStatusRepository.getSingleLoginStatus(singleLoginCheck, null);
    }

    public LiveData<SingleLoginStatus> getSingleLoginStatus() {
        return mutableLiveData;
    }
}
