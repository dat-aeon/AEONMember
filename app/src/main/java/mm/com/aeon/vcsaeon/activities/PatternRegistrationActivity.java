package mm.com.aeon.vcsaeon.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.LoginInfoReqBean;
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
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class PatternRegistrationActivity extends BaseActivity {

    Toolbar toolbar;

    TextView txtTitle;
    TextView txtPhone;
    TextView txtPhoneErr;
    TextView txtPwd;
    TextView txtPwdErr;

    TextView warningTitle;
    TextView warningContent;

    EditText textPhone;
    EditText textPwd;

    PatternLockView patternLockView;

    Button btnRegister;

    String biometricPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_registration);

        toolbar = findViewById(R.id.my_toolbar_bio_reg);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        txtTitle = findViewById(R.id.pattern_reg_form_title);
        txtPhone = findViewById(R.id.reg_pattern_ph);
        txtPhoneErr = findViewById(R.id.reg_pattern_ph_err);
        txtPwd = findViewById(R.id.reg_pattern_pwd);
        txtPwdErr = findViewById(R.id.pattern_reg_pwd_err);

        textPhone = findViewById(R.id.text_reg_pattern_ph);
        textPwd = findViewById(R.id.text_reg_pattern_pwd);

        warningTitle = findViewById(R.id.txt_warning);
        warningContent = findViewById(R.id.pattern_warning_text);

        patternLockView = findViewById(R.id.patternView);

        btnRegister = findViewById(R.id.btn_reg_pattern);

        biometricPhone = PreferencesManager.getBiometricRegPhoneNo(getApplicationContext());
        textPhone.setText(biometricPhone);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                Log.d(getClass().getName(), "Pattern complete: " +
                        PatternLockUtils.patternToString(patternLockView, pattern));
                if (PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase("123")) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    Toast.makeText(PatternRegistrationActivity.this, "Welcome back, CodingDemos", Toast.LENGTH_LONG).show();
                } else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    Toast.makeText(PatternRegistrationActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCleared() {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation=true;
                final String curLang2 = PreferencesManager.getCurrentLanguage(getApplicationContext());

                final String phoneNo = textPhone.getText().toString();
                final String password = textPwd.getText().toString();

                if(phoneNo==null || phoneNo.equals(BLANK)){
                    txtPhoneErr.setVisibility(View.VISIBLE);
                    validation=false;
                } else if(!CommonUtils.isPhoneNoValid(phoneNo)){
                    changePhoneLabel(curLang2);
                    txtPhoneErr.setVisibility(View.VISIBLE);
                    validation=false;
                } else {
                    txtPhoneErr.setVisibility(View.GONE);
                }

                if(password==null || password.equals(BLANK)){
                    txtPwdErr.setVisibility(View.VISIBLE);
                    validation=false;
                } else if(password.length()<6 || password.length()>16 || password.equals(SPACE)){
                    changePwdLabel(curLang2);
                    txtPwdErr.setVisibility(View.VISIBLE);
                    validation=false;
                } else {
                    txtPwdErr.setVisibility(View.GONE);
                }

                if(validation){

                    LoginInfoReqBean loginInfoReqBean = new LoginInfoReqBean();
                    loginInfoReqBean.setUsername(phoneNo);
                    loginInfoReqBean.setPassword(password);
                    loginInfoReqBean.setGrant_type(PASSWORD);

                    PatternRegistrationActivity.this.setTheme(R.style.MessageDialogTheme);
                    final ProgressDialog bioRegDialog = new ProgressDialog(PatternRegistrationActivity.this);
                    bioRegDialog.setMessage(getString(R.string.login_in_progress));
                    bioRegDialog.setCancelable(false);
                    bioRegDialog.show();

                    Service loginService = APIClient.getAuthUserService();
                    Call<BaseResponse<LoginAccessTokenInfo>> loginReq = loginService.doLogin(loginInfoReqBean.getUsername(),
                            loginInfoReqBean.getPassword(),PARAM_GRANT_TYPE, getLoginDeviceId());

                    loginReq.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {

                            if(response.isSuccessful()){

                                BaseResponse loginBaseResponse = response.body();

                                if(loginBaseResponse.getStatus().equals(SUCCESS)){

                                    closeDialog(bioRegDialog);

                                    //mark-biometric-registered
                                    SharedPreferences myPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
                                    PreferencesManager.addEntryToPreferences(myPreferences,"biometric_reg","true");
                                    PreferencesManager.addEntryToPreferences(myPreferences, "biometric_phone", phoneNo);
                                    PreferencesManager.addEntryToPreferences(myPreferences, "biometric_pwd", password);
                                    showSnackBarMessage(getString(R.string.reg_pattern_success_msg));
                                    finish();

                                } else {
                                    closeDialog(bioRegDialog);
                                    showWarningDialog(PatternRegistrationActivity.this,getInvalidInfoMsg(curLang2));
                                }

                            } else {
                                closeDialog(bioRegDialog);
                                showErrorDialog(PatternRegistrationActivity.this,getString(R.string.login_unavailable));
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                            closeDialog(bioRegDialog);
                            showErrorDialog(PatternRegistrationActivity.this,getString(R.string.login_on_failure));
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        SharedPreferences langPreference = PreferencesManager.getApplicationPreference(getApplicationContext());
        String curLang = PreferencesManager.getStringEntryFromPreferences(langPreference,"lang");

        if(curLang.equals(LANG_MM)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
            menu.getItem(0).setTitle(LANG_EN);
            changeLabel(LANG_MM);
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
            menu.getItem(0).setTitle(LANG_MM);
            changeLabel(LANG_EN);
        }

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

    public void addValueToPreference(String lang){
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
        PreferencesManager.addEntryToPreferences(sharedPreferences,"lang",lang);
    }

    public void changeLabel(String language){
        if(biometricPhone.equals(BLANK)){
            txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_title, getApplicationContext()));
        } else {
            txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.upd_pattern_title, getApplicationContext()));
        }
        txtPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_phone_label, getApplicationContext()));
        txtPhoneErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_phone_err, getApplicationContext()));
        txtPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_pwd_label, getApplicationContext()));
        txtPwdErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_pwd_err, getApplicationContext()));
        textPhone.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_phone_hint, getApplicationContext()));
        textPwd.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_password_holder, getApplicationContext()));
        btnRegister.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_submit_btn, getApplicationContext()));
        warningTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_warning_title, getApplicationContext()));
        warningContent.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_warning_content, getApplicationContext()));
    }

    public void changePhoneLabel(String language){
        txtPhoneErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_phone_format_err, getApplicationContext()));
    }

    public void changePwdLabel(String language){
        txtPwdErr.setText(CommonUtils.getLocaleString(new Locale(language), R.string.reg_pattern_pwd_format_err, getApplicationContext()));
    }

    public String getInvalidInfoMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.login_invalid, getApplicationContext());
    }

}
