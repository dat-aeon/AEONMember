package mm.com.aeon.vcsaeon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ForceResetPasswordReqBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.INVALID_PASSWORD_LENGTH;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD_WEAK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class ForcePasswordResetActivity extends BaseActivity {

    private Button btnResetPwd;
    private Toolbar toolbar;
    private TextView txtNewPwd;
    private TextView txtConfPwd;

    private EditText textNewPwd;
    private EditText textConfNewPwd;

    private TextView txtErrNewPwd;
    private TextView txtErrConfNewPwd;
    private TextView textTitle;
    private TextView textWarning;

    //menu
    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

    ForceResetPasswordReqBean forceResetPasswordReqBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force_password_reset);

        forceResetPasswordReqBean = (ForceResetPasswordReqBean) getIntent().getSerializableExtra("force_password_reset_bean");

        btnResetPwd = findViewById(R.id.btn_change_pwd);

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
        menuBarLevelInfo.setText(R.string.menu_level_one);
        //get install phone number from fragment.
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

        textNewPwd = findViewById(R.id.txt_new_pwd);
        textConfNewPwd = findViewById(R.id.txt_new_conf_pwd);

        txtErrNewPwd = findViewById(R.id.reset_new_pwd_err);
        txtErrConfNewPwd = findViewById(R.id.reset_new_conf_pwd_err);

        txtNewPwd = findViewById(R.id.text_new_pwd);
        txtConfPwd = findViewById(R.id.txt_conf_pwd_re);

        textWarning = findViewById(R.id.text_pwd);

        textTitle = findViewById(R.id.reset_title);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        changeLabel(curLang);

        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                final String newPwd = textNewPwd.getText().toString();
                final String confNewPwd = textConfNewPwd.getText().toString();

                boolean validation = true;

                final String curLang2 = PreferencesManager.getCurrentLanguage(getApplicationContext());

                if(newPwd==null || newPwd.equals(BLANK)){
                    txtErrNewPwd.setVisibility(View.VISIBLE);
                    validation = false;
                } else if(newPwd.length()<6 || newPwd.length()>16 || newPwd.equals(SPACE)){
                    txtErrNewPwd.setText(getString(R.string.register_pwd_format_err_msg));
                    changeErrPwd(curLang2);
                    txtErrNewPwd.setVisibility(View.VISIBLE);
                    validation = false;
                } else {
                    txtErrNewPwd.setVisibility(View.GONE);
                }

                if(confNewPwd==null || confNewPwd.equals(BLANK)){
                    txtErrConfNewPwd.setVisibility(View.VISIBLE);
                    validation = false;
                } else if(!newPwd.equals(confNewPwd)){
                    txtErrConfNewPwd.setText(getString(R.string.resetpass_conpass_err2));
                    changeErrConfPwd(curLang2);
                    txtErrConfNewPwd.setVisibility(View.VISIBLE);
                    validation = false;
                } else if(confNewPwd.length()<6 || confNewPwd.length()>16){
                    txtErrConfNewPwd.setText(getString(R.string.register_pwd_format_err_msg));
                    changeErrConfPwd(curLang2);
                    txtErrConfNewPwd.setVisibility(View.VISIBLE);
                    validation = false;
                } else {
                    txtErrConfNewPwd.setVisibility(View.GONE);
                }

                if(validation){

                    if (!CommonUtils.isNetworkAvailable(getApplicationContext())) {
                        showNetworkErrorDialog(ForcePasswordResetActivity.this,getNetErrMsg());
                    } else {

                        //network call
                        forceResetPasswordReqBean.setPassword(newPwd);

                        Service updateResetPwdService = APIClient.getUserService();
                        Call<BaseResponse> req = updateResetPwdService.forcePasswordReset(forceResetPasswordReqBean);

                        ForcePasswordResetActivity.this.setTheme(R.style.MessageDialogTheme);
                        final ProgressDialog forceResetPwdDialog = new ProgressDialog(ForcePasswordResetActivity.this);
                        forceResetPwdDialog.setMessage("Changing Password...");
                        forceResetPwdDialog.setCancelable(false);
                        forceResetPwdDialog.show();

                        req.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call,
                                                   Response<BaseResponse> response) {

                                if (response.isSuccessful()) {

                                    BaseResponse baseResponse = response.body();

                                    if(baseResponse.getStatus().equals(SUCCESS)){

                                        closeDialog(forceResetPwdDialog);

                                        //Update Biometric data.
                                        SharedPreferences myPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
                                        PreferencesManager.setBiometricRegistered(getApplicationContext(),true);
                                        PreferencesManager.addEntryToPreferences(myPreferences, "biometric_phone", forceResetPasswordReqBean.getPhoneNo());
                                        PreferencesManager.addEntryToPreferences(myPreferences, "biometric_pwd", newPwd);

                                        //Set Current login phone no.
                                        PreferencesManager.setCurrentLoginPhoneNo(getApplicationContext(),forceResetPasswordReqBean.getPhoneNo());

                                        //Show Dialog nad go to login.
                                        final Dialog dialog = new Dialog(ForcePasswordResetActivity.this);
                                        dialog.setContentView(R.layout.success_message_dialog);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        dialog.setCancelable(false);
                                        Button btnOk = dialog.findViewById(R.id.btn_ok);
                                        TextView messageBody = dialog.findViewById(R.id.text_message);
                                        messageBody.setText(getString(R.string.pwd_update_success));
                                        PreferencesManager.setRegistrationCompleted(getApplicationContext(),true);
                                        btnOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();

                                                Intent intent = new Intent(ForcePasswordResetActivity.this, LoginActivity.class);
                                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);

                                                //startActivity(new Intent(ForcePasswordResetActivity.this, LoginActivity.class));
                                            }
                                        });
                                        dialog.show();

                                    } else {

                                        closeDialog(forceResetPwdDialog);

                                        if(baseResponse.getMessageCode().equals(PASSWORD_WEAK)){
                                            changePwdStrength(curLang2);
                                            txtErrNewPwd.setVisibility(View.VISIBLE);
                                        }

                                        if(baseResponse.getMessageCode().equals(INVALID_PASSWORD_LENGTH)){
                                            changePwdStrength(curLang2);
                                            txtErrNewPwd.setVisibility(View.VISIBLE);
                                        }

                                    }

                                } else {
                                    closeDialog(forceResetPwdDialog);
                                    showErrorDialog(ForcePasswordResetActivity.this,getString(R.string.message_password_failed));
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                closeDialog(forceResetPwdDialog);
                                showErrorDialog(ForcePasswordResetActivity.this,getString(R.string.service_unavailable));
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,"lang");
        changeLabel(curLang);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    public void changeLabel(String language){
        txtNewPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_newpass_label, getApplicationContext()));
        txtConfPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_conpass_label, getApplicationContext()));
        textNewPwd.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_hint, getApplicationContext()));
        textConfNewPwd.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_hint, getApplicationContext()));
        txtErrNewPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_newpass_err, getApplicationContext()));
        txtErrConfNewPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_conpass_err2, getApplicationContext()));
        btnResetPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_reset_button, getApplicationContext()));
        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_title, getApplicationContext()));
        textWarning.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_pwd_limit, getApplicationContext()));
        addValueToPreference(language);
    }

    public void changePwdStrength(String language){
        txtErrNewPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_pwd_strength_err_msg, getApplicationContext()));
    }

    public void changeErrPwd(String language){
        txtErrNewPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_pwd_format_err_msg, getApplicationContext()));
    }

    public void changeErrConfPwd(String language){
        txtErrConfNewPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_confpwd_format_err_msg, getApplicationContext()));
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }
}
