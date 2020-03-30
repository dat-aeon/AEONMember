package mm.com.aeon.vcsaeon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CheckAccountLockReqBean;
import mm.com.aeon.vcsaeon.beans.CheckAccountLockResBean;
import mm.com.aeon.vcsaeon.beans.ForceResetPasswordReqBean;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ACCOUNT_LOCKED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NOT_EXIST_CUSTOMER_INFO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REQUIRED_INPUT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isNetworkAvailable;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isNrcCodeValid;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class CheckAccountLockActivity extends BaseActivity {

    Toolbar toolbar;

    private EditText textPhoneNo;
    private EditText textRegCode;
    private Button btnNext;
    private View serviceUnavailable;

    private TextView txtTitle;
    private TextView txtPhone;
    private TextView txtNrc;

    private TextView txtErrPhone;
    private TextView txtErrNrc;

    static String stateDivCodeVal;
    static String nrcTypeVal;

    static Spinner spinnerDivCode;
    static AutoCompleteTextView autoCompleteTwspCode;
    Spinner spinnerType;

    //menu
    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

    static List<String> townshipCode = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_verify);

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

        btnNext = findViewById(R.id.btn_next);
        textPhoneNo = findViewById(R.id.txt_phone);
        textRegCode = findViewById(R.id.txt_reset_reg_no);

        txtTitle = findViewById(R.id.conf_info_form_title);
        txtPhone = findViewById(R.id.reset_phone);
        txtNrc = findViewById(R.id.reset_nrc_no);

        serviceUnavailable = findViewById(R.id.service_unavailable_sq);
        serviceUnavailable.setVisibility(View.GONE);

        txtErrPhone = findViewById(R.id.verify_err_ph);
        txtErrPhone.setVisibility(View.GONE);
        txtErrNrc = findViewById(R.id.verify_err_nrc);
        txtErrNrc.setVisibility(View.GONE);

        textPhoneNo.setText(PreferencesManager.getCurrentLoginPhoneNo(getApplicationContext()));
        textPhoneNo.setSelection(textPhoneNo.getText().length());

        final String[] nrcType = getResources().getStringArray(R.array.nrc_type);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getApplicationContext(),R.layout.nrc_spinner_item_3, nrcType);

        spinnerDivCode = findViewById(R.id.spinner_div_code);
        autoCompleteTwspCode = findViewById(R.id.auto_comp_twsp_code2);
        spinnerType = findViewById(R.id.spinner_nrc_type);
        spinnerType.setAdapter(adapterType);

        //load nrc.
        Service getNrcInfoService = APIClient.getUserService();
        final Call<BaseResponse<List<TownshipCodeResDto>>> req = getNrcInfoService.getTownshipCode();

        CheckAccountLockActivity.this.setTheme(R.style.MessageDialogTheme);
        final ProgressDialog twspCodeDialog = new ProgressDialog(CheckAccountLockActivity.this);
        twspCodeDialog.setMessage(getString(R.string.dialog_loading_nrc));
        twspCodeDialog.setCancelable(false);
        twspCodeDialog.show();

        btnNext.setEnabled(false);

        req.enqueue(new Callback<BaseResponse<List<TownshipCodeResDto>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<TownshipCodeResDto>>> call, Response<BaseResponse<List<TownshipCodeResDto>>> response) {

                if(response.isSuccessful()){

                    BaseResponse baseResponse = response.body();

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        try{

                            final List<TownshipCodeResDto> townshipCodeResDtoList =
                                    (List<TownshipCodeResDto>)baseResponse.getData();

                            townshipCode=townshipCodeResDtoList.get(0).getTownshipCodeList();


                            final String[] stateDivCode = getResources().getStringArray(R.array.div_code);
                            ArrayAdapter<String> adapterDiv = new ArrayAdapter<String>(getApplicationContext(),R.layout.nrc_spinner_item_1, stateDivCode);

                            spinnerDivCode.setAdapter(adapterDiv);
                            stateDivCodeVal = stateDivCode[0];
                            nrcTypeVal = nrcType[0];

                            spinnerDivCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    stateDivCodeVal = stateDivCode[position];
                                    for (TownshipCodeResDto townshipCodeResDto :townshipCodeResDtoList) {
                                        if(String.valueOf(townshipCodeResDto.getStateId()).equals(stateDivCodeVal)){
                                            townshipCode=townshipCodeResDto.getTownshipCodeList();
                                            break;
                                        }
                                    }

                                    autoCompleteTwspCode.setText(BLANK);
                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(),R.layout.nrc_spinner_item_2, townshipCode);
                                    adapter2.setDropDownViewResource(R.layout.dialog_nrc_division);
                                    adapter2.setNotifyOnChange(true);
                                    autoCompleteTwspCode.setThreshold(1);
                                    autoCompleteTwspCode.setAdapter(adapter2);

                                    autoCompleteTwspCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            //townshipCodeVal = (String) parent.getAdapter().getItem(position);
                                            autoCompleteTwspCode.setText((String) parent.getAdapter().getItem(position));
                                        }
                                    });

                                    autoCompleteTwspCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                        @Override
                                        public void onFocusChange(View v, boolean hasFocus) {
                                            if(!hasFocus){
                                                if(!townshipCode.contains(autoCompleteTwspCode.getText().toString())){
                                                    autoCompleteTwspCode.setText(BLANK);
                                                }
                                            } else {
                                                autoCompleteTwspCode.showDropDown();
                                            }
                                        }
                                    });

                                    autoCompleteTwspCode.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            autoCompleteTwspCode.showDropDown();
                                        }
                                    });
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {}

                            });

                            spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    nrcTypeVal = nrcType[position];
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {}
                            });

                            btnNext.setEnabled(true);
                            closeDialog(twspCodeDialog);

                        } catch (Exception e){

                            closeDialog(twspCodeDialog);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    closeDialog(twspCodeDialog);
                    serviceUnavailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<TownshipCodeResDto>>> call, Throwable t) {
                closeDialog(twspCodeDialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String townshipCodeVal = autoCompleteTwspCode.getText().toString();
                String textRegCodeVal = textRegCode.getText().toString();
                final String nrcNo = stateDivCodeVal + "/" + townshipCodeVal + nrcTypeVal + textRegCodeVal;
                final String phoneNo = textPhoneNo.getText().toString();

                final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());

                boolean validation = true;

                if(phoneNo==null || phoneNo.equals(BLANK)){
                    txtErrPhone.setVisibility(View.VISIBLE);
                    validation=false;
                } else if(!CommonUtils.isPhoneNoValid(phoneNo)){
                    txtErrPhone.setText(getString(R.string.login_phoneno_format_err_msg));
                    changeErrPhone(curLang);
                    txtErrPhone.setVisibility(View.VISIBLE);
                    validation=false;
                } else {
                    txtErrPhone.setVisibility(View.GONE);
                }

                if(textRegCodeVal==null || textRegCodeVal.equals(BLANK)|| townshipCodeVal==null || townshipCodeVal.equals(BLANK)){
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validation = false;
                } else if(!isNrcCodeValid(textRegCodeVal)){
                    txtErrNrc.setText(getString(R.string.register_nrc_format_err));
                    changeErrNrc(curLang);
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validation = false;
                } else if(!townshipCode.contains(autoCompleteTwspCode.getText().toString())){
                    autoCompleteTwspCode.setText(BLANK);
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validation = false;
                } else {
                    txtErrNrc.setVisibility(View.GONE);
                }

                if(validation){

                    if(!isNetworkAvailable(getApplicationContext())){
                        showNetworkErrorDialog(CheckAccountLockActivity.this,getNetErrMsg());
                    } else {

                        CheckAccountLockActivity.this.setTheme(R.style.MessageDialogTheme);
                        final ProgressDialog checkAccInfoDialog = new ProgressDialog(CheckAccountLockActivity.this);
                        checkAccInfoDialog.setMessage(getString(R.string.progress_account_check));
                        checkAccInfoDialog.setCancelable(false);
                        checkAccInfoDialog.show();

                        Service checkAccountLock = APIClient.getUserService();
                        Call<BaseResponse<CheckAccountLockResBean>> req = checkAccountLock.checkAccountLock(new CheckAccountLockReqBean(phoneNo,nrcNo));

                        req.enqueue(new Callback<BaseResponse<CheckAccountLockResBean>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<CheckAccountLockResBean>> call, Response<BaseResponse<CheckAccountLockResBean>> response) {

                                if(response.isSuccessful()){

                                    BaseResponse baseResponse = response.body();

                                    if(baseResponse.getStatus().equals(SUCCESS)){

                                        closeDialog(checkAccInfoDialog);

                                        CheckAccountLockResBean checkAccountLockResBean = (CheckAccountLockResBean) baseResponse.getData();

                                        ForceResetPasswordReqBean forceResetPasswordReqBean = new ForceResetPasswordReqBean();
                                        forceResetPasswordReqBean.setPhoneNo(phoneNo);
                                        forceResetPasswordReqBean.setNrcNo(nrcNo);

                                        if(checkAccountLockResBean.getLockStatus()==ACCOUNT_LOCKED){
                                            //Go to Change Password.
                                            Intent intent = new Intent(CheckAccountLockActivity.this, ForcePasswordResetActivity.class);
                                            intent.putExtra("force_password_reset_bean",forceResetPasswordReqBean);
                                            startActivity(intent);

                                        } else {

                                            //clear sec-question-ans data.
                                            SecurityConfirmationActivity.tempAnswers=null;
                                            SecurityConfirmationActivity.tempSpinnerPosition=null;

                                            //Go Security Questions Answers Check.
                                            PreferencesManager.setHotlinePhone(getApplicationContext(), checkAccountLockResBean.getHotlinePhone());
                                            Intent intent = new Intent(CheckAccountLockActivity.this, SecurityConfirmationActivity.class);
                                            intent.putExtra("force_password_reset_bean",forceResetPasswordReqBean);
                                            intent.putExtra("num_sec_question",checkAccountLockResBean.getCustomerSecurityQuestionCount());
                                            startActivity(intent);
                                        }

                                    } else {

                                        closeDialog(checkAccInfoDialog);

                                        if(baseResponse.getMessageCode().equals(NOT_EXIST_CUSTOMER_INFO)){
                                            showWarningDialog(CheckAccountLockActivity.this,getNoUserInfoMsg(curLang));
                                        } else if(baseResponse.getMessageCode().equals(REQUIRED_INPUT)){
                                            showWarningDialog(CheckAccountLockActivity.this,getString(R.string.ph_nrc_input_required));
                                        } else {
                                            showErrorDialog(CheckAccountLockActivity.this,getString(R.string.message_check_lock_failed));
                                        }
                                    }

                                } else {
                                    closeDialog(checkAccInfoDialog);
                                    showErrorDialog(CheckAccountLockActivity.this,getString(R.string.message_check_lock_failed));
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse<CheckAccountLockResBean>> call, Throwable t) {
                                closeDialog(checkAccInfoDialog);
                                showErrorDialog(CheckAccountLockActivity.this,getString(R.string.message_check_lock_failed));
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
       /* getMenuInflater().inflate(R.menu.toolbar_menu, menu);
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
        txtPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_phoneno_label, getApplicationContext()));
        txtNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_label, getApplicationContext()));
        txtErrPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_phoneno_err, getApplicationContext()));
        txtErrNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_err, getApplicationContext()));
        textPhoneNo.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_phoneno_holder, getApplicationContext()));
        btnNext.setText(CommonUtils.getLocaleString(new Locale(language), R.string.secquestconfirm_confrim_button, getApplicationContext()));
        txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.forgot_pwd_confirm_info, getApplicationContext()));
        addValueToPreference(language);
    }

    public void changeErrPhone(String language){
        txtErrPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.login_phoneno_format_err_msg, getApplicationContext()));
    }

    public void changeErrNrc(String language){
        txtErrNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_format_err, getApplicationContext()));
    }

    public String getNoUserInfoMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.no_acc_info, getApplicationContext());
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }
}
