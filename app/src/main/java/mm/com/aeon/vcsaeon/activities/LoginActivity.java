package mm.com.aeon.vcsaeon.activities;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import androidx.core.content.res.ResourcesCompat;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.BiometricSensorStatus;
import mm.com.aeon.vcsaeon.beans.CouponInfoReqBean;
import mm.com.aeon.vcsaeon.beans.CouponInfoResBean;
import mm.com.aeon.vcsaeon.beans.CurrentUserInformationResBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.LoginInfoReqBean;
import mm.com.aeon.vcsaeon.beans.OfflineLogoutReqBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.FingerprintHandler;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.beans.BiometricSensorStatus.BIOMETRIC_OK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ACCOUNT_LOCKED_STATUS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.FAILED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.PROFILE_URL;
import static mm.com.aeon.vcsaeon.common_utils.PreferencesManager.clearIsDestroy;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showMessageDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class LoginActivity extends BaseActivity {

    Toolbar toolbar;

    Button btnLogin;
    Button btnForgotPwd;

    TextView txtErrPhone;
    TextView txtErrPwd;

    EditText textPhoneNo;
    EditText textPassword;

    ImageView imgBiometric;
    ImageView imgPattern;
    ImageView imgVisible;

    Service usrLoginService;

    LoginInfoReqBean loginInfoReqBean;
    CurrentUserInformationResBean currUserInfoResBean;
    UserInformationFormBean userInformationFormBean;

    SharedPreferences preferences;

    static KeyStore keyStore;
    static KeyGenerator keyGenerator;
    static FingerprintManager fingerprintManager;

    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.setFinishOnTouchOutside(false);
/*

        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);
        menuBarName = toolbar.findViewById(R.id.menu_bar_name);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText("Lv.1 : Application User");
        //get logged in phone number from fragment.
        String lastLoggedInPhone = PreferencesManager.getCurrentLoginPhoneNo(getApplicationContext()).trim();
        menuBarPhoneNo.setText(lastLoggedInPhone);
        menuBarName.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,30,0,0);
        menuBarLevelInfo.setLayoutParams(params);

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

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));
*/

        //Check and do offline logout.
        if(PreferencesManager.isDestroy(getApplicationContext())){
            final String lastActivatedInfo = PreferencesManager.getLastActivatedInfo(getApplicationContext());
            if(!lastActivatedInfo.equals(BLANK)){
                checkAndUpdateOfflineLogout(new Gson().fromJson(lastActivatedInfo,OfflineLogoutReqBean.class));
            }
        }

        btnLogin = findViewById(R.id.btn_login);
        btnForgotPwd = findViewById(R.id.btn_forgot_pwd);

        imgBiometric = findViewById(R.id.btn_biometric);
        //imgPattern = findViewById(R.id.btn_pattern);

        txtErrPhone = findViewById(R.id.login_err_phone);
        txtErrPwd = findViewById(R.id.login_err_pwd);

        textPhoneNo = findViewById(R.id.txt_phone_no);
        textPassword = findViewById(R.id.txt_pwd);
        textPassword.setFocusable(true);

        imgVisible = findViewById(R.id.img_paw_visible);

        preferences = PreferencesManager.getCurrentUserPreferences(getApplicationContext());

        //change labels language.
        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        //get logged in phone number from fragment.
        String lastLoggedInPhone = PreferencesManager.getCurrentLoginPhoneNo(getApplicationContext()).trim();
        textPhoneNo.setText(lastLoggedInPhone);

        //do validation | login.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            final String curLang2 = PreferencesManager.getCurrentLanguage(getApplicationContext());

            boolean validation = true;
            String loginPhoneNo = textPhoneNo.getText().toString();
            String password = textPassword.getText().toString();

            if(loginPhoneNo==null || loginPhoneNo.equals(BLANK)){
                txtErrPhone.setVisibility(View.VISIBLE);
                validation = false;
            } else if(!CommonUtils.isPhoneNoValid(loginPhoneNo)) {
                txtErrPhone.setText(getString(R.string.login_phoneno_format_err_msg));
                changeErrPhone(curLang2);
                txtErrPhone.setVisibility(View.VISIBLE);
                validation = false;
            } else {
                txtErrPhone.setVisibility(View.GONE);
            }

            if(password==null || password.equals(BLANK)){
                txtErrPwd.setVisibility(View.VISIBLE);
                validation = false;
            } else if(password.length()<6 && password.length()>16){
                txtErrPwd.setText(getString(R.string.login_password_format_err_msg));
                changeErrPwd(curLang2);
                txtErrPwd.setVisibility(View.VISIBLE);
                validation = false;
            } else {
                txtErrPwd.setVisibility(View.GONE);
            }

            if(validation) {

                if (!CommonUtils.isNetworkAvailable(getApplicationContext())) {
                    showNetworkErrorDialog(LoginActivity.this,getNetErrMsg());
                } else {

                    loginInfoReqBean = new LoginInfoReqBean();
                    loginInfoReqBean.setUsername(loginPhoneNo);
                    loginInfoReqBean.setPassword(password);

                    usrLoginService = APIClient.getAuthUserService();

                    LoginActivity.this.setTheme(R.style.MessageDialogTheme);
                    final ProgressDialog loginDialog = new ProgressDialog(LoginActivity.this);
                    loginDialog.setMessage(getString(R.string.login_in_progress));
                    loginDialog.setCancelable(false);
                    loginDialog.show();

                    Service loginService = APIClient.getAuthUserService();
                    Call<BaseResponse<LoginAccessTokenInfo>> loginReq = loginService.doLogin(loginInfoReqBean.getUsername(),
                            loginInfoReqBean.getPassword(), PASSWORD, getLoginDeviceId());

                    loginReq.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {

                            if(response.isSuccessful()){

                                BaseResponse loginBaseResponse = response.body();

                                if(loginBaseResponse.getStatus().equals(SUCCESS)){

                                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) loginBaseResponse.getData();

                                    PreferencesManager.keepAccessToken(getApplicationContext(),loginAccessTokenInfo.getAccessToken());
                                    PreferencesManager.keepRefreshToken(getApplicationContext(),loginAccessTokenInfo.getRefreshToken());

                                    currUserInfoResBean = loginAccessTokenInfo.getUserInformationResDto();

                                    userInformationFormBean = new UserInformationFormBean();
                                    userInformationFormBean.setCustomerId(currUserInfoResBean.getCustomerId());
                                    userInformationFormBean.setCustomerNo(currUserInfoResBean.getCustomerNo());
                                    userInformationFormBean.setPhoneNo(currUserInfoResBean.getPhoneNo());
                                    userInformationFormBean.setCustomerTypeId(currUserInfoResBean.getCustomerTypeId());
                                    userInformationFormBean.setName(currUserInfoResBean.getName());
                                    userInformationFormBean.setDateOfBirth(currUserInfoResBean.getDateOfBirth());
                                    userInformationFormBean.setNrcNo(currUserInfoResBean.getNrcNo());
                                    userInformationFormBean.setPhotoPath(PROFILE_URL+currUserInfoResBean.getPhotoPath());
                                    userInformationFormBean.setMemberNo(currUserInfoResBean.getMemberNo());
                                    userInformationFormBean.setMemberNoValid(currUserInfoResBean.isMemberNoValid());
                                    userInformationFormBean.setHotlinePhone(currUserInfoResBean.getHotlinePhone());
                                    userInformationFormBean.setCustAgreementListDtoList(currUserInfoResBean.getCustomerAgreementDtoList());

                                    String userInfoFormJson = new Gson().toJson(userInformationFormBean);
                                    PreferencesManager.setCurrentUserInfo(getApplicationContext(),userInfoFormJson);
                                    PreferencesManager.setCurrentLoginPhoneNo(getApplicationContext(),userInformationFormBean.getPhoneNo());

                                    //get coupon informations.
                                    Service getCouponInfoService = APIClient.getUserService();
                                    Call<BaseResponse<List<CouponInfoResBean>>> reqCouponInfo =
                                            getCouponInfoService.getCouponInfo(PreferencesManager.getAccessToken(getApplicationContext()),
                                                    new CouponInfoReqBean(userInformationFormBean.getCustomerId()));

                                    reqCouponInfo.enqueue(new Callback<BaseResponse<List<CouponInfoResBean>>>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse<List<CouponInfoResBean>>> call, Response<BaseResponse<List<CouponInfoResBean>>> response) {

                                            if(response.isSuccessful()){

                                                BaseResponse baseResponse0 = response.body();

                                                if(baseResponse0.getStatus().equals(SUCCESS)){

                                                    closeDialog(loginDialog);

                                                    final List<CouponInfoResBean> couponInfoResBeanList =
                                                            (List<CouponInfoResBean>) baseResponse0.getData();

                                                    String couponInfoJson = new Gson().toJson(couponInfoResBeanList);
                                                    PreferencesManager.addEntryToPreferences(preferences, "cur_coupon_info", couponInfoJson);
                                                    startActivity(new Intent(LoginActivity.this, MainMenuActivityDrawer.class));
                                                    finish();

                                                } else {
                                                    closeDialog(loginDialog);
                                                    showWarningDialog(LoginActivity.this,getString(R.string.message_coupon_info_unavailable));
                                                }

                                            } else {
                                                closeDialog(loginDialog);
                                                showWarningDialog(LoginActivity.this,getString(R.string.message_get_coupon_info_failed));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse<List<CouponInfoResBean>>> call, Throwable t) {
                                            closeDialog(loginDialog);
                                            showWarningDialog(LoginActivity.this,getString(R.string.message_get_coupon_info_failed));
                                        }
                                    });

                                } else if(loginBaseResponse.getStatus().equals(FAILED)){
                                    closeDialog(loginDialog);
                                    if(loginBaseResponse.getMessageCode().equals(ACCOUNT_LOCKED_STATUS)){
                                        final Dialog dialog = new Dialog(LoginActivity.this);
                                        dialog.setContentView(R.layout.warning_message_dialog);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                        dialog.setCancelable(false);
                                        Button btnOk = dialog.findViewById(R.id.btn_ok);
                                        TextView messageBody = dialog.findViewById(R.id.text_message);
                                        messageBody.setText(getAccLockedMessage());
                                        btnOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                                Intent intent = newIntent(getApplicationContext());
                                                startActivity(intent);
                                            }
                                        });
                                        dialog.show();
                                    } else {
                                        showWarningDialog(LoginActivity.this, getErrAlertMessage());
                                    }
                                } else {
                                    closeDialog(loginDialog);
                                    showWarningDialog(LoginActivity.this, getErrAlertMessage());
                                }

                            } else {
                                closeDialog(loginDialog);
                                showErrorDialog(LoginActivity.this,getString(R.string.login_unavailable));
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                            closeDialog(loginDialog); //crashed.
                            showErrorDialog(LoginActivity.this,getString(R.string.login_on_failure));
                        }
                    });
                }
            }
            }
        });

        /*imgPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PatternRegistrationActivity.class));

            }
        });*/

        imgBiometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricSensorStatus biometricSensorStatus = CommonUtils.checkBiometricSensor(getApplicationContext());
                switch (biometricSensorStatus){
                    case BIOMETRIC_OK :
                        startActivity(new Intent(LoginActivity.this, BiometricRegistrationActivity.class));
                        break;
                    case NOT_ENROLLED :
                        showMessageDialog(LoginActivity.this,getBiometricNotEnrolledMsg());
                        break;
                    case DOES_NOT_SUPPORT :
                        showMessageDialog(LoginActivity.this,getBiometricNotSupportMsg());
                        break;
                }
            }
        });

        btnForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = newIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        imgVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textPassword.getInputType()==129){
                    textPassword.setInputType(1);
                    textPassword.setSelection(textPassword.getText().length());
                    imgVisible.setImageDrawable(getDrawable(R.drawable.ic_visibility_off_black_24dp));
                } else {
                    textPassword.setInputType(129);
                    textPassword.setSelection(textPassword.getText().length());
                    imgVisible.setImageDrawable(getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                }
                textPassword.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));
            }
        });

        textPhoneNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textPhoneNo.setFocusableInTouchMode(true);
                return false;
            }
        });

        textPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textPassword.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();

        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,"lang");
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        boolean isBiometricRegistered = PreferencesManager.isBiometricRegistered(getApplicationContext());

        //Check biometric sensor in device.
        BiometricSensorStatus biometricSensorStatus = CommonUtils.checkBiometricSensor(getApplicationContext());
        Log.e("Biometric login ", isBiometricRegistered + "");

        //Do FingerPrint Login.
        if(isBiometricRegistered && biometricSensorStatus.equals(BIOMETRIC_OK)){

            if (checkFinger()) {
                final FingerprintHandler fph = new FingerprintHandler("fp_msg");
                FingerprintManager.CryptoObject cryptoObject;
                // We are ready to set up the cipher and the key
                try {
                    generateKey();
                    Cipher cipher = generateCipher();
                    cryptoObject =
                            new FingerprintManager.CryptoObject(cipher);
                    CancellationSignal signal = new CancellationSignal();

                    //fph.doAuth(fingerprintManager, cryptoObject);
                    fingerprintManager.authenticate(cryptoObject, signal, 0, new FingerprintManager.AuthenticationCallback() {

                        @Override
                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                        }

                        @Override
                        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                            super.onAuthenticationHelp(helpCode, helpString);
                        }

                        @Override
                        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);

                            try{

                                SharedPreferences curPref = PreferencesManager.getApplicationPreference(getApplicationContext());
                                String bioPhoneNo = PreferencesManager.getStringEntryFromPreferences(curPref,"biometric_phone");
                                String bioPassword = PreferencesManager.getStringEntryFromPreferences(curPref, "biometric_pwd");
                                //Log.e("Biometric data ", bioPhoneNo + bioPassword);

                                loginInfoReqBean = new LoginInfoReqBean();
                                loginInfoReqBean.setUsername(bioPhoneNo);
                                loginInfoReqBean.setPassword(bioPassword);

                                Service loginService = APIClient.getAuthUserService();
                                Call<BaseResponse<LoginAccessTokenInfo>> loginReq = loginService.doLogin(loginInfoReqBean.getUsername(),
                                        loginInfoReqBean.getPassword(), PASSWORD, getLoginDeviceId());

                                LoginActivity.this.setTheme(R.style.MessageDialogTheme);
                                final ProgressDialog bioLoginDialog = new ProgressDialog(LoginActivity.this);
                                bioLoginDialog.setTitle(getString(R.string.login_title));
                                bioLoginDialog.setMessage(getString(R.string.login_in_progress));
                                bioLoginDialog.setCancelable(false);
                                bioLoginDialog.show();

                                loginReq.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {

                                        if(response.isSuccessful()){

                                            BaseResponse loginBaseResponse = response.body();

                                            if(loginBaseResponse.getStatus().equals(SUCCESS)){

                                                LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) loginBaseResponse.getData();

                                                PreferencesManager.keepAccessToken(getApplicationContext(),loginAccessTokenInfo.getAccessToken());
                                                PreferencesManager.keepRefreshToken(getApplicationContext(),loginAccessTokenInfo.getRefreshToken());

                                                currUserInfoResBean = loginAccessTokenInfo.getUserInformationResDto();

                                                userInformationFormBean = new UserInformationFormBean();
                                                userInformationFormBean.setCustomerId(currUserInfoResBean.getCustomerId());
                                                userInformationFormBean.setCustomerNo(currUserInfoResBean.getCustomerNo());
                                                userInformationFormBean.setPhoneNo(currUserInfoResBean.getPhoneNo());
                                                userInformationFormBean.setCustomerTypeId(currUserInfoResBean.getCustomerTypeId());
                                                userInformationFormBean.setName(currUserInfoResBean.getName());
                                                userInformationFormBean.setDateOfBirth(currUserInfoResBean.getDateOfBirth());
                                                userInformationFormBean.setNrcNo(currUserInfoResBean.getNrcNo());
                                                userInformationFormBean.setPhotoPath(PROFILE_URL+currUserInfoResBean.getPhotoPath());
                                                userInformationFormBean.setCustAgreementListDtoList(currUserInfoResBean.getCustomerAgreementDtoList());
                                                userInformationFormBean.setMemberNo(currUserInfoResBean.getMemberNo());
                                                userInformationFormBean.setMemberNoValid(currUserInfoResBean.isMemberNoValid());
                                                userInformationFormBean.setHotlinePhone(currUserInfoResBean.getHotlinePhone());

                                                String userInfoFormJson = new Gson().toJson(userInformationFormBean);
                                                PreferencesManager.setCurrentUserInfo(getApplicationContext(),userInfoFormJson);
                                                PreferencesManager.addEntryToPreferences(preferences,"cur_ph_no", userInformationFormBean.getPhoneNo());

                                                //get coupon informations.
                                                Service getCouponInfoService = APIClient.getUserService();
                                                Call<BaseResponse<List<CouponInfoResBean>>> reqCouponInfo =
                                                        getCouponInfoService.getCouponInfo(PreferencesManager.getAccessToken(getApplicationContext()),
                                                                new CouponInfoReqBean(userInformationFormBean.getCustomerId()));

                                                reqCouponInfo.enqueue(new Callback<BaseResponse<List<CouponInfoResBean>>>() {
                                                    @Override
                                                    public void onResponse(Call<BaseResponse<List<CouponInfoResBean>>> call, Response<BaseResponse<List<CouponInfoResBean>>> response) {

                                                        if(response.isSuccessful()){

                                                            BaseResponse baseResponse0 = response.body();

                                                            if(baseResponse0.getStatus().equals(SUCCESS)){

                                                                final List<CouponInfoResBean> couponInfoResBeanList =
                                                                        (List<CouponInfoResBean>) baseResponse0.getData();
                                                                String couponInfoJson = new Gson().toJson(couponInfoResBeanList);
                                                                PreferencesManager.addEntryToPreferences(preferences, "cur_coupon_info", couponInfoJson);
                                                                closeDialog(bioLoginDialog);
                                                                //startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                                                                startActivity(new Intent(LoginActivity.this, MainMenuActivityDrawer.class));
                                                                finish();

                                                            } else {

                                                                closeDialog(bioLoginDialog);
                                                                showWarningDialog(LoginActivity.this,getString(R.string.message_coupon_info_unavailable));
                                                            }

                                                        } else {

                                                            closeDialog(bioLoginDialog);
                                                            showWarningDialog(LoginActivity.this,getString(R.string.message_get_coupon_info_failed));
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<BaseResponse<List<CouponInfoResBean>>> call, Throwable t) {

                                                        closeDialog(bioLoginDialog);
                                                        showWarningDialog(LoginActivity.this,getString(R.string.message_get_coupon_info_failed));
                                                    }
                                                });

                                            } else if(loginBaseResponse.getStatus().equals(FAILED)){

                                                closeDialog(bioLoginDialog);

                                                if(loginBaseResponse.getMessageCode().equals(ACCOUNT_LOCKED_STATUS)){
                                                    final Dialog dialog = new Dialog(LoginActivity.this);
                                                    dialog.setContentView(R.layout.warning_message_dialog);
                                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    dialog.setCancelable(false);
                                                    Button btnOk = dialog.findViewById(R.id.btn_ok);
                                                    TextView messageBody = dialog.findViewById(R.id.text_message);
                                                    messageBody.setText(getAccLockedMessage());
                                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                            Intent intent = newIntent(getApplicationContext());
                                                            startActivity(intent);
                                                        }
                                                    });

                                                    dialog.show();

                                                } else {

                                                    final Dialog mDdialog = new Dialog(LoginActivity.this);
                                                    mDdialog.setContentView(R.layout.biometric_not_match_dialog);
                                                    mDdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    mDdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    mDdialog.setCancelable(false);
                                                    TextView textMessage = mDdialog.findViewById(R.id.text_message);
                                                    textMessage.setText(getInvalidBiometricMsg());

                                                    Button btnCancel = mDdialog.findViewById(R.id.btn_skip);

                                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mDdialog.dismiss();
                                                        }
                                                    });

                                                    Button btnOk = mDdialog.findViewById(R.id.btn_upgrade);
                                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            //(2) | get response
                                                            mDdialog.dismiss();
                                                            startActivity(new Intent(LoginActivity.this, BiometricRegistrationActivity.class));
                                                        }
                                                    });
                                                    mDdialog.show();
                                                }

                                            } else {

                                                closeDialog(bioLoginDialog);
                                                showWarningDialog(LoginActivity.this, getErrAlertMessage());
                                            }

                                        } else {
                                            closeDialog(bioLoginDialog);
                                            showErrorDialog(LoginActivity.this,getString(R.string.login_unavailable));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                                        closeDialog(bioLoginDialog);
                                        showErrorDialog(LoginActivity.this,getString(R.string.login_on_failure));
                                    }
                                });

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            showSnackBarMessage(getString(R.string.biometric_auth_failed));
                        }

                    }, new Handler());

                } catch (Exception fpe) {
                    // Handle exception
                    showSnackBarMessage(getString(R.string.biometric_auth_error));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.toolbar_main_menu, menu);

        SharedPreferences langPreference = PreferencesManager.getApplicationPreference(getApplicationContext());
        String curLang = PreferencesManager.getStringEntryFromPreferences(langPreference,"lang");
*/
       /* if(curLang.equals(LANG_MM)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
            menu.getItem(0).setTitle(LANG_EN);
            changeLabel(LANG_MM);
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
            menu.getItem(0).setTitle(LANG_MM);
            changeLabel(LANG_EN);
        }*/
        /*menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
        menu.getItem(0).setTitle(LANG_MM);
        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
        menu.getItem(1).setTitle(LANG_EN);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_myFlag) {
            /*if(item.getTitle().equals(LANG_MM)){
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
            } else if(item.getTitle().equals(LANG_EN)){
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
            }*/
            changeLabel(LANG_MM);
            addValueToPreference(LANG_MM);
            return true;
        }
        if (id == R.id.action_engFlag) {
            /*if(item.getTitle().equals(LANG_MM)){
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
            } else if(item.getTitle().equals(LANG_EN)){
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
            }*/
            changeLabel(LANG_EN);
            addValueToPreference(LANG_EN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //check finger-print sensor.
    private boolean checkFinger() {
        // Keyguard Manager
        KeyguardManager keyguardManager = (KeyguardManager)
                getSystemService(KEYGUARD_SERVICE);
        // Fingerprint Manager
        fingerprintManager = (FingerprintManager)
                getSystemService(FINGERPRINT_SERVICE);
        try {

            //Device does not support getSystemService() method.
            if(fingerprintManager == null){
                return false;
            }

            // Check if the fingerprint sensor is present
            if (!fingerprintManager.isHardwareDetected()) {
                return false;
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                showSnackBarMessage(getString(R.string.message_no_fingerprint_config));
                return false;
            }

            if (!keyguardManager.isKeyguardSecure()) {
                showSnackBarMessage(getString(R.string.message_sec_scrren_unavailable));
                return false;
            }

        } catch(SecurityException se) {
            se.printStackTrace();
        }
        return true;
    }

    //generate keystore.
    private void generateKey() throws Exception {
        try {
            // Get the reference to the key store
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            // Key generator to generate the key
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init( new
                    KeyGenParameterSpec.Builder("key_name",
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        }
        catch(KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new Exception(exc);
        }
    }

    private Cipher generateCipher() throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            SecretKey key = (SecretKey) keyStore.getKey("key_name",
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        }
        catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | UnrecoverableKeyException
                | KeyStoreException exc) {
            exc.printStackTrace();
            throw new Exception(exc);
        }
    }

    public void addValueToPreference(String lang){
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
        PreferencesManager.addEntryToPreferences(sharedPreferences,"lang",lang);
    }

    public void changeLabel(String language){
        txtErrPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.login_phoneno_err_msg, getApplicationContext()));
        txtErrPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.login_password_err_msg, getApplicationContext()));
        textPhoneNo.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.login_phoneno_holder, getApplicationContext()));
        textPassword.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.login_password_holder, getApplicationContext()));
        btnLogin.setText(CommonUtils.getLocaleString(new Locale(language), R.string.login_login_btn, getApplicationContext()));
        btnForgotPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.login_forgotpassword_link, getApplicationContext()));
        //toolbar.setTitle(CommonUtils.getLocaleString(new Locale(language), R.string.login_title, getApplicationContext()));
        addValueToPreference(language);
    }

    public void changeErrPhone(String language){
        txtErrPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.login_phoneno_format_err_msg, getApplicationContext()));
    }

    public void changeErrPwd(String language){
        txtErrPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.login_password_format_err_msg, getApplicationContext()));
    }

    public String getErrAlertMessage(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.login_invalid, getApplicationContext());
    }

    //update logout info.
    public void checkAndUpdateOfflineLogout(OfflineLogoutReqBean offlineLogoutReqBean){
        final Service offlineLogout = APIClient.getUserService();
        final Call<BaseResponse> req = offlineLogout.offlineLogout(offlineLogoutReqBean);
        req.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    BaseResponse baseResponse = response.body();
                    if(baseResponse.getStatus().equals(SUCCESS)){
                        //showSnackBarMessage(getString(R.string.logout_info_updated));
                        clearIsDestroy(getApplicationContext());
                    } else {
                        showSnackBarMessage(getString(R.string.message_logout_failed));
                    }
                } else {
                    showSnackBarMessage(getString(R.string.message_logout_failed));
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                showSnackBarMessage(getString(R.string.message_logout_failed));
            }
        });
    }

    private static Intent newIntent(Context context){
        Intent intent = new Intent(context, CheckAccountLockActivity.class);
        return intent;
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }

    private String getInvalidBiometricMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.bio_login_invalid, getApplicationContext());
    }

    private String getBiometricNotEnrolledMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.biometric_not_enrolled, getApplicationContext());
    }

    private String getBiometricNotSupportMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.biometric_not_support, getApplicationContext());
    }

    private String getAccLockedMessage(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.msg_account_locked, getApplicationContext());
    }

}
