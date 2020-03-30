package mm.com.aeon.vcsaeon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.BiometricSensorStatus;
import mm.com.aeon.vcsaeon.beans.CurrentUserInformationResBean;
import mm.com.aeon.vcsaeon.beans.ExistedMemberRegistrationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.OTPInfoReqBean;
import mm.com.aeon.vcsaeon.beans.OTPInfoResBean;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DUPLICATED_CUSTOMER_INFO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DUPLICATED_NRC_NO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DUPLICATED_PHONE_NO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.EXISTED_REG_MEM_INFO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.IMPORT_PH_DUPLICATE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.OTP_STATUS_EXPIRED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.OTP_STATUS_VALID;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD_WEAK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REGISTER_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.PROFILE_URL;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class RegistrationGetOtpCodeAndSendActivity extends BaseActivity {

    Toolbar toolbar;
    TextView txtErrOtp;
    EditText textOtp;
    Button btnOk;
    Button btnResend;
    TextView txtTimer;
    TextView txtOtpTitle;

    ExistedMemberRegistrationInfoReqBean existedMemberRegistrationInfoReqBean;
    OTPInfoReqBean otpInfoReqBean;

    String mCurrentPhotoPath;
    File imgFile;
    SharedPreferences preferences;

    private static int msgType=0;

    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferencesManager.getCurrentUserPreferences(getApplicationContext());

        setContentView(R.layout.register_otp_get_and_send);

        toolbar = findViewById(R.id.toolbar_main_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBackbtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackbtn.setVisibility(View.GONE);

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

        menuBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        txtErrOtp = findViewById(R.id.reg_err_otp);
        textOtp = findViewById(R.id.txt_otp);

        btnOk = findViewById(R.id.btn_ok_otp);
        btnResend = findViewById(R.id.btn_re_send_otp);

        txtTimer = findViewById(R.id.txt_timer);

        txtOtpTitle = findViewById(R.id.txt_otp_title);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        existedMemberRegistrationInfoReqBean = new ExistedMemberRegistrationInfoReqBean();
        existedMemberRegistrationInfoReqBean = (ExistedMemberRegistrationInfoReqBean) getIntent().getSerializableExtra(EXISTED_REG_MEM_INFO);
        mCurrentPhotoPath = getIntent().getStringExtra(REGISTER_PHOTO);

        otpInfoReqBean = new OTPInfoReqBean();
        otpInfoReqBean.setPhoneNo(existedMemberRegistrationInfoReqBean.getPhoneNo());

        imgFile = new File(mCurrentPhotoPath);

        //Network call | get OTP.
        Service getOtpService = APIClient.getUserService();
        final Call<BaseResponse<OTPInfoResBean>> req = getOtpService.getOTPCode(otpInfoReqBean);

        RegistrationGetOtpCodeAndSendActivity.this.setTheme(R.style.MessageDialogTheme);
        final ProgressDialog reqOtpDialog = new ProgressDialog(RegistrationGetOtpCodeAndSendActivity.this);
        reqOtpDialog.setMessage(getString(R.string.progress_otp_request));
        reqOtpDialog.setCancelable(false);
        reqOtpDialog.show();

        req.enqueue(new Callback<BaseResponse<OTPInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<OTPInfoResBean>> call, Response<BaseResponse<OTPInfoResBean>> response) {

                if(response.isSuccessful()){

                    BaseResponse baseResponse = response.body();

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        closeDialog(reqOtpDialog);

                        final OTPInfoResBean otpInfoResBean = (OTPInfoResBean) baseResponse.getData();

                        btnOk.setEnabled(true);
                        btnOk.setBackground(getDrawable(R.drawable.button_border));

                        //waiting minutes for again OTP.
                        new CountDownTimer(120000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                txtTimer.setText(""+String.format("%02d:%02d",
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                        toMinutes(millisUntilFinished))));
                            }
                            public void onFinish() {
                                txtTimer.setText(getString(R.string.init_timer_text));
                                btnResend.setEnabled(true);
                                btnResend.setBackground(getDrawable(R.drawable.button_border));
                                PreferencesManager.addEntryToPreferences(preferences,"otp_status",OTP_STATUS_EXPIRED);
                            }
                        }.start();

                        showSnackBarMessage("OTP Code : " + otpInfoResBean.getOtpCode());

                        PreferencesManager.addEntryToPreferences(preferences,"otp_code",otpInfoResBean.getOtpCode());
                        if(otpInfoResBean!=null){
                            PreferencesManager.addEntryToPreferences(preferences,"otp_status",OTP_STATUS_VALID);
                        }

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //(1) Get Otp.
                                final String resOtpCode = PreferencesManager.getStringEntryFromPreferences(preferences,"otp_code");
                                final String otpStatus = PreferencesManager.getStringEntryFromPreferences(preferences,"otp_status");
                                String inputOtpCode = textOtp.getText().toString();
                                boolean validation = true;
                                txtErrOtp.setVisibility(View.GONE);

                                //(2) Validate OTP Format.
                                //otp has expired after 2".
                                if(otpStatus.equals(OTP_STATUS_VALID)){

                                    if(inputOtpCode.equals(BLANK)){

                                        textOtp.setText(BLANK);
                                        txtErrOtp.setText(getBlankOtpErrMsg());
                                        txtErrOtp.setVisibility(View.VISIBLE);
                                        validation=false;

                                    } else if(inputOtpCode.length()!=4){

                                        textOtp.setText(BLANK);
                                        txtErrOtp.setText(getInvalidOtpLengthMsg());
                                        txtErrOtp.setVisibility(View.VISIBLE);
                                        validation=false;

                                    } else if(!resOtpCode.equals(inputOtpCode)){

                                        textOtp.setText(BLANK);
                                        txtErrOtp.setText(getWrongOTPMsg());
                                        txtErrOtp.setVisibility(View.VISIBLE);
                                        validation=false;

                                    } else {

                                        txtErrOtp.setVisibility(View.GONE);
                                    }

                                } else {

                                    textOtp.setText(BLANK);
                                    txtErrOtp.setText(getExpiredOtpMsg());
                                    txtErrOtp.setVisibility(View.VISIBLE);
                                    validation=false;
                                }

                                if (validation) {

                                    if (!CommonUtils.isNetworkAvailable(getApplicationContext())) {
                                        showNetworkErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getNetErrMsg());
                                    } else {

                                        if(imgFile.exists()){

                                            byte[] bytes = CommonUtils.encodedFileToByteArray(imgFile);

                                            //set photo bytes.
                                            existedMemberRegistrationInfoReqBean.setPhotoByte(bytes);

                                            //call old member registration task.
                                            new RegisterOldCustomerAsynTask().execute(existedMemberRegistrationInfoReqBean);

                                        } else {
                                            final Snackbar snackbar = Snackbar.make(findViewById(R.id.root_otp_request_view), getString(R.string.img_destroy_snackbar_info), Snackbar.LENGTH_INDEFINITE)
                                                    .setAction("RETRY", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            finish();
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    }
                                }
                            }
                        });

                    } else {
                        closeDialog(reqOtpDialog);
                        btnOk.setEnabled(false);
                        showErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.opt_request_failed));
                    }
                } else {
                    closeDialog(reqOtpDialog);
                    btnOk.setEnabled(false);
                    showErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.opt_request_failed));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<OTPInfoResBean>> call, Throwable t) {
                closeDialog(reqOtpDialog);
                btnOk.setEnabled(false);
                showErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.service_unavailable));
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
            menu.getItem(0).setTitle(LANG_EN);
            changeLabel(LANG_MM);
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
            menu.getItem(0).setTitle(LANG_MM);
            changeLabel(LANG_EN);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            if(item.getTitle().equals(LANG_MM)){
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
            } else if(item.getTitle().equals(LANG_EN)){
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showSnackBarMessage(getString(R.string.message_back_disable));
    }

    private void addValueToPreference(String lang){
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    private void changeLabel(String language){

        txtOtpTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.otp_title, getApplicationContext()));
        btnOk.setText(CommonUtils.getLocaleString(new Locale(language), R.string.otp_btn_ok, getApplicationContext()));
        btnResend.setText(CommonUtils.getLocaleString(new Locale(language), R.string.otp_btn_resend, getApplicationContext()));
        textOtp.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.otp_code_hint, getApplicationContext()));

        switch (msgType){
            case 1 : txtErrOtp.setText(CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_blank, getApplicationContext()));
            break;
            case 2 : txtErrOtp.setText(CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_length, getApplicationContext()));
            break;
            case 3 : txtErrOtp.setText(CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_wrong, getApplicationContext()));
            break;
            case 4 : txtErrOtp.setText(CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_expired, getApplicationContext()));
            break;
        }
    }

    private String getBioSuggessionAlertMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.bio_dialog_reg, getApplicationContext());
    }

    //Show Dialog and go to Login Screen.
    private void goToLoginScreen(){
        final Dialog dialog = new Dialog(RegistrationGetOtpCodeAndSendActivity.this);
        dialog.setContentView(R.layout.success_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        messageBody.setText("Registration Done!");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(RegistrationGetOtpCodeAndSendActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }

    private String getBlankOtpErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        msgType=1;
        return CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_blank, getApplicationContext());
    }

    private String getInvalidOtpLengthMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        msgType=2;
        return CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_length, getApplicationContext());
    }

    private String getWrongOTPMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        msgType=3;
        return CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_wrong, getApplicationContext());
    }

    private String getExpiredOtpMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        msgType=4;
        return CommonUtils.getLocaleString(new Locale(language), R.string.otp_err_expired, getApplicationContext());
    }

    private class RegisterOldCustomerAsynTask extends AsyncTask<ExistedMemberRegistrationInfoReqBean, Integer, String>{

        ProgressDialog regDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            RegistrationGetOtpCodeAndSendActivity.this.setTheme(R.style.MessageDialogTheme);
            regDialog = new ProgressDialog(RegistrationGetOtpCodeAndSendActivity.this);
            regDialog.setMessage(getString(R.string.progress_registering));
            regDialog.setCancelable(false);
            regDialog.show();
        }

        @Override
        protected String doInBackground(ExistedMemberRegistrationInfoReqBean... existedMemberRegistrationInfoReqBeans) {

            final ExistedMemberRegistrationInfoReqBean registrationInfoReqBean = existedMemberRegistrationInfoReqBeans[0];

            try {

                final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());

                Service registerExistedService = APIClient.getUserService();
                Call<BaseResponse> req2 = registerExistedService.registerOldCustomer(registrationInfoReqBean);

                //(4) Register Old Member.
                req2.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        //success
                        if (response.isSuccessful()) {

                            final BaseResponse baseResponse = response.body();
                            if(baseResponse.getStatus().equals(SUCCESS)){

                                //(6) Do login.
                                Service loginService = APIClient.getAuthUserService();
                                Call<BaseResponse<LoginAccessTokenInfo>> loginReq = loginService.doLogin(registrationInfoReqBean.getPhoneNo(),
                                        registrationInfoReqBean.getPassword(),PASSWORD, getLoginDeviceId());

                                loginReq.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {

                                        if(response.isSuccessful()){

                                            BaseResponse baseResponse1 = response.body();

                                            if(baseResponse1.getStatus().equals(SUCCESS)){

                                                closeDialog(regDialog);

                                                LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) baseResponse1.getData();

                                                //set token infos to preferences.
                                                PreferencesManager.keepAccessToken(getApplicationContext(),loginAccessTokenInfo.getAccessToken());
                                                PreferencesManager.keepRefreshToken(getApplicationContext(),loginAccessTokenInfo.getRefreshToken());

                                                final CurrentUserInformationResBean curUserInfo = loginAccessTokenInfo.getUserInformationResDto();

                                                //(2) delete file.
                                                File delFile = new File(mCurrentPhotoPath);
                                                delFile.delete();

                                                UserInformationFormBean userInformationFormBean = new UserInformationFormBean();
                                                userInformationFormBean.setCustomerId(curUserInfo.getCustomerId());
                                                userInformationFormBean.setCustomerNo(curUserInfo.getCustomerNo());
                                                userInformationFormBean.setPhoneNo(curUserInfo.getPhoneNo());
                                                userInformationFormBean.setCustomerTypeId(curUserInfo.getCustomerTypeId());
                                                userInformationFormBean.setName(curUserInfo.getName());
                                                userInformationFormBean.setDateOfBirth(curUserInfo.getDateOfBirth());
                                                userInformationFormBean.setNrcNo(curUserInfo.getNrcNo());
                                                userInformationFormBean.setHotlinePhone(curUserInfo.getHotlinePhone());
                                                userInformationFormBean.setPhotoPath(PROFILE_URL+curUserInfo.getPhotoPath());
                                                userInformationFormBean.setMemberNo(curUserInfo.getMemberNo());
                                                userInformationFormBean.setMemberNoValid(curUserInfo.isMemberNoValid());
                                                userInformationFormBean.setCustAgreementListDtoList(curUserInfo.getCustomerAgreementDtoList());

                                                String userInfoFormJson = new Gson().toJson(userInformationFormBean);
                                                PreferencesManager.setCurrentUserInfo(getApplicationContext(),userInfoFormJson);
                                                PreferencesManager.setCurrentLoginPhoneNo(getApplicationContext(),userInformationFormBean.getPhoneNo());
                                                PreferencesManager.setBiometricRegPhoneNo(getApplicationContext(),userInformationFormBean.getPhoneNo());

                                                PreferencesManager.setRegistrationCompleted(getApplicationContext(),true);
                                                //Check biometric sensor in device.
                                                BiometricSensorStatus biometricSensorStatus = CommonUtils.checkBiometricSensor(getApplicationContext());

                                                switch (biometricSensorStatus){

                                                    case BIOMETRIC_OK:
                                                        //Biometric Registration Suggestion.
                                                        final Dialog biometricDialog = new Dialog(RegistrationGetOtpCodeAndSendActivity.this);
                                                        biometricDialog.setCancelable(false);
                                                        biometricDialog.setContentView(R.layout.biometric_registration_dialog);
                                                        biometricDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                        biometricDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                                        TextView textView = biometricDialog.findViewById(R.id.text_message);
                                                        textView.setText(getBioSuggessionAlertMsg(curLang));
                                                        Button btnYes = biometricDialog.findViewById(R.id.btn_yes);
                                                        Button btnNo = biometricDialog.findViewById(R.id.btn_no);

                                                        btnYes.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                biometricDialog.dismiss();
                                                                startActivity(intentBiometricRegister(getApplicationContext()));
                                                                finish();
                                                            }
                                                        });

                                                        btnNo.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                biometricDialog.dismiss();
                                                                //go to menu | show card.
                                                                startActivity(intentMainMenuDrawer(getApplicationContext()));
                                                                finish();
                                                            }
                                                        });

                                                        biometricDialog.show();
                                                        break;

                                                    default:
                                                        //go to menu | show card.
                                                        startActivity(intentMainMenuDrawer(getApplicationContext()));
                                                        finish();
                                                        break;
                                                }

                                            } else {
                                                closeDialog(regDialog);
                                                goToLoginScreen();
                                            }

                                        } else {
                                            closeDialog(regDialog);
                                            goToLoginScreen();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                                        closeDialog(regDialog);
                                        goToLoginScreen();
                                    }
                                });

                            } else {

                                closeDialog(regDialog);

                                if(baseResponse.getMessageCode().equals(PASSWORD_WEAK)){
                                    showWarningDialog(RegistrationGetOtpCodeAndSendActivity.this,"Password Weak. [temp]");

                                } else if(baseResponse.getMessageCode().equals(DUPLICATED_PHONE_NO)){
                                    showWarningDialog(RegistrationGetOtpCodeAndSendActivity.this, getString(R.string.register_ph_no_dup));

                                } else if(baseResponse.getMessageCode().equals(DUPLICATED_NRC_NO)){
                                    showWarningDialog(RegistrationGetOtpCodeAndSendActivity.this, getString(R.string.register_nrc_dup));

                                } else if(baseResponse.getMessageCode().equals(DUPLICATED_CUSTOMER_INFO)){
                                    showWarningDialog(RegistrationGetOtpCodeAndSendActivity.this, getString(R.string.register_duplicate));

                                } else if(baseResponse.getMessageCode().equals(IMPORT_PH_DUPLICATE)){
                                    showWarningDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.register_imp_phone_no_duplicate));
                                } else {
                                    showErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.register_failed));
                                }
                            }

                        } else {
                            closeDialog(regDialog);
                            //Display Message Detail.
                            showErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.register_failed));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        closeDialog(regDialog);
                        showErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.service_unavailable));
                    }

                });

            } catch (Exception e) {
                closeDialog(regDialog);
                showErrorDialog(RegistrationGetOtpCodeAndSendActivity.this,getString(R.string.register_failed));
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private static Intent intentMainMenuDrawer(Context context){
        PreferencesManager.clearCouponInfo(context);
        Intent intent = new Intent(context, MainMenuActivityDrawer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private static Intent intentBiometricRegister(Context context){
        Intent intent = new Intent(context, BiometricRegistrationInRegister.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void onStart(){
        super.onStart();
        changeLabel(PreferencesManager.getCurrentLanguage(this));
    }
}