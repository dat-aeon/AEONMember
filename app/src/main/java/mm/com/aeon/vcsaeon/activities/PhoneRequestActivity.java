package mm.com.aeon.vcsaeon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;
import java.util.UUID;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.HotlineInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.camera.core.CameraX.getContext;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_TERM_ACCEPTED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TERM_ACCEPTED;

public class PhoneRequestActivity extends BaseActivity {

    TextView requestTitle;
    EditText phoneNoText;
    Button nextBtn;
    String currLang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_phoneno_activity);

        nextBtn = findViewById(R.id.btn_next);
        requestTitle = findViewById(R.id.request_title);
        phoneNoText = findViewById(R.id.text_phone_no);

        currLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        requestTitle.setText(CommonUtils.getLocaleString(new Locale(currLang), R.string.phone_request_text, getApplicationContext()));
        nextBtn.setText(CommonUtils.getLocaleString(new Locale(currLang), R.string.btn_next, getApplicationContext()));
        getHotlinePhoneNo();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CommonUtils.isPhoneNoValid(phoneNoText.getText().toString())){
                    phoneNoText.setHint(CommonUtils.getLocaleString(new Locale(currLang), R.string.register_phoneno_format_err_msg, getApplicationContext()));
                    phoneNoText.setHintTextColor(getColor(android.R.color.holo_red_dark));
                    phoneNoText.setText(BLANK);

                } else {
                    SharedPreferences preferences = PreferencesManager.getApplicationPreference(getApplicationContext());
                    PreferencesManager.addEntryToPreferences(preferences, PARAM_TERM_ACCEPTED, TERM_ACCEPTED);
                    PreferencesManager.setInstallPhoneNo(getApplicationContext(),phoneNoText.getText().toString());
                    startActivity(new Intent(PhoneRequestActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    private void getHotlinePhoneNo(){
        Service service = APIClient.getUserService();
        Call<BaseResponse<HotlineInfoResBean>> reqPh = service.getHotlineInfo();
        reqPh.enqueue(new Callback<BaseResponse<HotlineInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<HotlineInfoResBean>> call, Response<BaseResponse<HotlineInfoResBean>> response) {
                if(response.isSuccessful()){
                    BaseResponse baseResponse = response.body();
                    if(baseResponse.getStatus().equals(SUCCESS)){
                        HotlineInfoResBean hotlineInfoResBean = (HotlineInfoResBean) baseResponse.getData();
                        PreferencesManager.setHotlinePhone(getApplicationContext(),hotlineInfoResBean.getHotlinePhone());
                    } else {
                        PreferencesManager.setHotlinePhone(getApplicationContext(),BLANK);
                    }
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<HotlineInfoResBean>> call, Throwable t) {
                PreferencesManager.setHotlinePhone(getApplicationContext(),BLANK);
            }
        });
    }
}
