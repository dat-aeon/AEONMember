package mm.com.aeon.vcsaeon.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.FreeMessageUserReqBean;
import mm.com.aeon.vcsaeon.beans.FreeMessageUserResBean;
import mm.com.aeon.vcsaeon.beans.HowToUseVideoResBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class VideoPlayActivity extends BaseActivity {

    Toolbar toolbar;
    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

    TextView txtTitle;
    TextView warningTitle;
    TextView warningContent;
    VideoView videoView;
    RelativeLayout layoutLoading;
    ImageView imgLoading;

    VideoView view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play_activity);

        layoutLoading = findViewById(R.id.layout_loading);
        imgLoading = findViewById(R.id.img_loading);
        showLoading();

        toolbar = (Toolbar) findViewById(R.id.toolbar_main_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBackbtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackbtn.setVisibility(View.VISIBLE);

        menuBarName = toolbar.findViewById(R.id.menu_bar_name);
        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        if (PreferencesManager.getMainNavFlag(this)) {
            menuBarLevelInfo.setText(R.string.menu_level_one);
            menuBarName.setVisibility(View.GONE);

            //get install phone number from fragment.
            String installPhone = PreferencesManager.getInstallPhoneNo(getApplicationContext()).trim();
            menuBarPhoneNo.setText(installPhone);

        } else {
            UserInformationFormBean userInformationFormBean = new UserInformationFormBean();
            final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(this);
            userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

            menuBarLevelInfo.setText(R.string.menu_level_two);
            menuBarName.setText(userInformationFormBean.getName());
            menuBarName.setVisibility(View.VISIBLE);
            //get logged in phone number from fragment.
            String lastLoggedInPhone = PreferencesManager.getCurrentLoginPhoneNo(getApplicationContext()).trim();
            menuBarPhoneNo.setText(lastLoggedInPhone);

        }


        myTitleBtn = toolbar.findViewById(R.id.my_flag);
        engTitleBtn = toolbar.findViewById(R.id.en_flag);

        myTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(myTitleBtn.getTag().toString());
            }
        });

        engTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(engTitleBtn.getTag().toString());
            }
        });

        menuBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTitle = findViewById(R.id.bio_reg_form_title);
        warningTitle = findViewById(R.id.txt_warning);
        warningContent = findViewById(R.id.biometric_warning_text);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }
       videoView = (VideoView)findViewById(R.id.video_view);
        setVideoLink();
    }

    public void changeLabel(String language){
        txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.video_play_title, getApplicationContext()));
        warningTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.video_play_warning_title, getApplicationContext()));
        warningContent.setText(CommonUtils.getLocaleString(new Locale(language), R.string.video_play_warning, getApplicationContext()));
    }

    private void setVideoLink() {

        if (!CommonUtils.isNetworkAvailable(getApplicationContext())) {
            showNetworkErrorDialog(getApplicationContext(), getNetErrMsg());

        } else {

            Service howToUseService = APIClient.getUserService();
            Call<BaseResponse<HowToUseVideoResBean>> req = howToUseService.getVideoLink();

            req.enqueue(new Callback<BaseResponse<HowToUseVideoResBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<HowToUseVideoResBean>> call, Response<BaseResponse<HowToUseVideoResBean>> response) {

                    if (response.isSuccessful()) {
                        BaseResponse baseResponse = response.body();

                        if (baseResponse != null) {
                            if (baseResponse.getStatus().equals(SUCCESS)) {
                                try {
                                    //closeLoading();
                                    HowToUseVideoResBean resBean = (HowToUseVideoResBean) baseResponse.getData();

                                    //String path = "android.resource://" + getPackageName() + "/" + R.raw.aeon_sample_video;
                                    Log.e("video get", SUCCESS);
                                    String path = "https://ass.aeoncredit.com.mm/daso/how-to-use-video/" + resBean.getFileName();

                                    videoView.setVideoURI(Uri.parse(path));
                                    videoView.start();
                                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mediaPlayer) {
                                            Log.e("video", "prepare");
                                            closeLoading();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    closeLoading();
                                }

                            } else {
                                closeLoading();
                            }
                        } else {
                            closeLoading();
                        }
                    } else {
                        closeLoading();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<HowToUseVideoResBean>> call, Throwable t) {
                    closeLoading();
                }
            });
        }

    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }


    private void closeLoading() {
        layoutLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        Glide.with(getApplicationContext()).load(R.drawable.loading).into(imgLoading);
        layoutLoading.setVisibility(View.VISIBLE);
    }

}
