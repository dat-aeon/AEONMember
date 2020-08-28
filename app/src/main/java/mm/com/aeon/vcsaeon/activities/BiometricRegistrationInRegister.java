package mm.com.aeon.vcsaeon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CurrentUserInformationResBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.LoginInfoReqBean;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_GRANT_TYPE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.PROFILE_URL;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class BiometricRegistrationInRegister extends BaseActivity {

    Toolbar toolbar;
    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackBtn;

    TextView txtTitle;
    TextView txtPhone;
    TextView txtPhoneErr;
    TextView txtPwd;
    TextView txtPwdErr;

    TextView warningTitle;
    TextView warningContent;

    EditText textPhone;
    EditText textPwd;

    Button btnRegister;
    String biometricPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biometric_registration);

        toolbar = findViewById(R.id.toolbar_main_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);

        menuBarName = toolbar.findViewById(R.id.menu_bar_name);
        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText(R.string.menu_level_one);

        //get logged in phone number from fragment.
        String installPhone = PreferencesManager.getInstallPhoneNo(getApplicationContext()).trim();
        menuBarPhoneNo.setText(installPhone);
        menuBarName.setVisibility(View.GONE);

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

        menuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainMenuActivityDrawer.class));
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        txtTitle = findViewById(R.id.bio_reg_form_title);
        txtPhone = findViewById(R.id.reg_bio_ph);
        txtPhoneErr = findViewById(R.id.reg_bio_ph_err);
        txtPwd = findViewById(R.id.reg_bio_pwd);
        txtPwdErr = findViewById(R.id.bio_reg_pwd_err);

        textPhone = findViewById(R.id.text_reg_bio_ph);
        textPwd = findViewById(R.id.text_reg_bio_pwd);

        warningTitle = findViewById(R.id.txt_warning);
        warningContent = findViewById(R.id.biometric_warning_text);

        btnRegister = findViewById(R.id.btn_reg_bio);

        biometricPhone = PreferencesManager.getBiometricRegPhoneNo(getApplicationContext());
        textPhone.setText(biometricPhone);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = true;
                final String curLang2 = PreferencesManager.getCurrentLanguage(getApplicationContext());

                final String phoneNo = textPhone.getText().toString();
                final String password = textPwd.getText().toString();

                if (phoneNo == null || phoneNo.equals(BLANK)) {
                    txtPhoneErr.setVisibility(View.VISIBLE);
                    validation = false;
                } else if (!CommonUtils.isPhoneNoValid(phoneNo)) {
                    changePhoneLabel(curLang2);
                    txtPhoneErr.setVisibility(View.VISIBLE);
                    validation = false;
                } else {
                    txtPhoneErr.setVisibility(View.GONE);
                }

                if (password == null || password.equals(BLANK)) {
                    txtPwdErr.setVisibility(View.VISIBLE);
                    validation = false;
                } else if (password.length() < 6 || password.length() > 16 || password.equals(SPACE)) {
                    changePwdLabel(curLang2);
                    txtPwdErr.setVisibility(View.VISIBLE);
                    validation = false;
                } else {
                    txtPwdErr.setVisibility(View.GONE);
                }

                if (validation) {

                    LoginInfoReqBean loginInfoReqBean = new LoginInfoReqBean();
                    loginInfoReqBean.setUsername(phoneNo);
                    loginInfoReqBean.setPassword(password);
                    loginInfoReqBean.setGrant_type(PASSWORD);

                    BiometricRegistrationInRegister.this.setTheme(R.style.MessageDialogTheme);
                    final ProgressDialog bioRegDialog = new ProgressDialog(BiometricRegistrationInRegister.this);
                    bioRegDialog.setMessage(getString(R.string.login_in_progress));
                    bioRegDialog.setCancelable(false);
                    bioRegDialog.show();

                    Service loginService = APIClient.getAuthUserService();
                    Call<BaseResponse<LoginAccessTokenInfo>> loginReq = loginService.doLogin(loginInfoReqBean.getUsername(),
                            loginInfoReqBean.getPassword(), PARAM_GRANT_TYPE, getLoginDeviceId());

                    loginReq.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {
                            if (response.isSuccessful()) {
                                BaseResponse loginBaseResponse = response.body();
                                if (loginBaseResponse.getStatus().equals(SUCCESS)) {
                                    closeDialog(bioRegDialog);

                                    //get login-response data.
                                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) loginBaseResponse.getData();

                                    //set token info to preferences.
                                    PreferencesManager.keepAccessToken(getApplicationContext(), loginAccessTokenInfo.getAccessToken());
                                    PreferencesManager.keepRefreshToken(getApplicationContext(), loginAccessTokenInfo.getRefreshToken());

                                    //set current user-info to preference.
                                    final CurrentUserInformationResBean curUserInfo = loginAccessTokenInfo.getUserInformationResDto();
                                    UserInformationFormBean userInformationFormBean = new UserInformationFormBean();
                                    userInformationFormBean.setCustomerId(curUserInfo.getCustomerId());
                                    userInformationFormBean.setCustomerNo(curUserInfo.getCustomerNo());
                                    userInformationFormBean.setPhoneNo(curUserInfo.getPhoneNo());
                                    userInformationFormBean.setCustomerTypeId(curUserInfo.getCustomerTypeId());
                                    userInformationFormBean.setName(curUserInfo.getName());
                                    userInformationFormBean.setDateOfBirth(curUserInfo.getDateOfBirth());
                                    userInformationFormBean.setNrcNo(curUserInfo.getNrcNo());
                                    userInformationFormBean.setPhotoPath(PROFILE_URL + curUserInfo.getPhotoPath());
                                    userInformationFormBean.setHotlinePhone(curUserInfo.getHotlinePhone());
                                    userInformationFormBean.setMemberNo(curUserInfo.getMemberNo());
                                    userInformationFormBean.setMemberNoValid(curUserInfo.isMemberNoValid());
                                    userInformationFormBean.setCustAgreementListDtoList(curUserInfo.getCustomerAgreementDtoList());

                                    //set phone and current-user information.
                                    final String userInfoFormJson = new Gson().toJson(userInformationFormBean);
                                    PreferencesManager.setCurrentUserInfo(getApplicationContext(), userInfoFormJson);
                                    PreferencesManager.setCurrentLoginPhoneNo(getApplicationContext(), userInformationFormBean.getPhoneNo());

                                    //set biometric-login data.
                                    SharedPreferences myPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
                                    PreferencesManager.setBiometricRegistered(getApplicationContext(), true);
                                    PreferencesManager.addEntryToPreferences(myPreferences, "biometric_phone", phoneNo);
                                    PreferencesManager.addEntryToPreferences(myPreferences, "biometric_pwd", password);

                                    //clear coupon_info | Go to main menu.
                                    startActivity(intentMainMenuDrawer(getApplicationContext()));
                                    finish();

                                } else {
                                    closeDialog(bioRegDialog);
                                    showWarningDialog(BiometricRegistrationInRegister.this, getInvalidInfoMsg(curLang2));
                                }
                            } else {
                                closeDialog(bioRegDialog);
                                showErrorDialog(BiometricRegistrationInRegister.this, getString(R.string.login_unavailable));
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                            closeDialog(bioRegDialog);
                            showErrorDialog(BiometricRegistrationInRegister.this, getString(R.string.login_on_failure));
                        }
                    });
                }
            }
        });

        textPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textPhone.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainMenuActivityDrawer.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            if (item.getTitle().equals(LANG_MM)) {
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
            } else if (item.getTitle().equals(LANG_EN)) {
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addValueToPreference(String lang) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
        PreferencesManager.addEntryToPreferences(sharedPreferences, "lang", lang);
    }

    public void changeLabel(String language) {
        txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_title, getApplicationContext()));
        txtPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_phone_label, getApplicationContext()));
        txtPhoneErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_phone_err, getApplicationContext()));
        txtPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_pwd_label, getApplicationContext()));
        txtPwdErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_pwd_err, getApplicationContext()));
        textPhone.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_phone_hint, getApplicationContext()));
        textPwd.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_password_holder, getApplicationContext()));
        btnRegister.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_submit_btn, getApplicationContext()));
        warningTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_warning_title, getApplicationContext()));
        warningContent.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_warning_content, getApplicationContext()));
    }

    public void changePhoneLabel(String language) {
        txtPhoneErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_phone_format_err, getApplicationContext()));
    }

    public void changePwdLabel(String language) {
        txtPwdErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_bio_pwd_format_err, getApplicationContext()));
    }

    public String getInvalidInfoMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.login_invalid, getApplicationContext());
    }

    private static Intent intentMainMenuDrawer(Context context) {
        PreferencesManager.clearCouponInfo(context);
        Intent intent = new Intent(context, MainMenuActivityDrawer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

}



