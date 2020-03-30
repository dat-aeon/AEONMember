package mm.com.aeon.vcsaeon.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CheckMemberInformationReqBean;
import mm.com.aeon.vcsaeon.beans.CustomerInfoBean;
import mm.com.aeon.vcsaeon.beans.CustomerRegistrationFormBean;
import mm.com.aeon.vcsaeon.beans.HotlineInfoResBean;
import mm.com.aeon.vcsaeon.beans.MemberRegistrationInfoFormBean;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BIRTH_DATE_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CALL_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DUPLICATED_CUSTOMER_INFO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DUPLICATED_NRC_NO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DUPLICATED_NRC_NO_CORE_SYSTEM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DUPLICATED_PHONE_NO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.IMPORT_PH_DUPLICATE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MEMBER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MIN_AGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NON_MEMBER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD_WEAK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isAsciiString;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isNrcCodeValid;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class RegistrationActivity extends BaseActivity {

    Toolbar toolbar;
    EditText textName;
    EditText textDob;
    EditText textPhone;
    EditText textPwd;
    EditText textConfPwd;
    EditText textRegCode;
    TextView textCaption;
    TextView txtName;
    TextView txtDob;
    TextView txtAgeLimit;
    TextView txtNrc;
    TextView txtPhone;
    TextView txtPwd;
    TextView txtConfPwd;
    TextView txtErrName;
    TextView txtErrDob;
    TextView txtErrNrc;
    TextView txtErrorPhNo;
    TextView txtErrPwd;
    TextView txtErrConfPwd;
    TextView txtPwdLimit;
    Button btnSave;

    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

    static String stateDivCodeVal;
    static String nrcTypeVal;
    String textRegCodeVal;

    Service getNrcInfoService;
    Service checkMemberInfoService;

    private static String hotlinePhoneNo;
    View serviceUnavailable;

    CustomerRegistrationFormBean customerRegistrationFormBean;
    static List<String> townshipCode = new ArrayList<>();
    CheckMemberInformationReqBean checkMemberInformationReqBean;
    MemberRegistrationInfoFormBean memberRegistrationInfoFormBean;

    static Spinner spinnerDivCode;
    static AutoCompleteTextView autoCompleteTwspCode;
    Spinner spinnerType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

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

        textCaption = findViewById(R.id.reg_form_title);

        txtErrName = findViewById(R.id.reg_err_name);
        txtErrName.setVisibility(View.GONE);
        txtErrDob = findViewById(R.id.reg_err_dob);
        txtErrDob.setVisibility(View.GONE);
        txtErrNrc = findViewById(R.id.reg_err_nrc);
        txtErrNrc.setVisibility(View.GONE);
        txtErrorPhNo = findViewById(R.id.reg_err_phone);
        txtErrorPhNo.setVisibility(View.GONE);
        txtErrPwd = findViewById(R.id.reg_err_pwd);
        txtErrPwd.setVisibility(View.GONE);
        txtErrConfPwd = findViewById(R.id.reg_err_conf_pwd);
        txtErrConfPwd.setVisibility(View.GONE);

        txtName = findViewById(R.id.reg_text_name);
        txtDob = findViewById(R.id.reg_text_dob);
        txtAgeLimit = findViewById(R.id.text_dob);
        txtNrc = findViewById(R.id.reg_text_nrc);
        txtPhone = findViewById(R.id.reg_text_phone);
        txtPwd = findViewById(R.id.reg_text_pwd);
        txtConfPwd = findViewById(R.id.reg_text_conf_pwd);

        textName = findViewById(R.id.txt_name);
        textDob = findViewById(R.id.txt_dob);
        textPhone = findViewById(R.id.txt_phone_no);
        textPwd = findViewById(R.id.txt_pwd);
        textConfPwd = findViewById(R.id.txt_conf_pwd);
        textRegCode = findViewById(R.id.txt_reg_reg_no);
        txtPwdLimit = findViewById(R.id.text_pwd);

        serviceUnavailable = findViewById(R.id.service_unavailable_sq);
        serviceUnavailable.setVisibility(View.GONE);

        final String[] nrcType = getResources().getStringArray(R.array.nrc_type);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getApplicationContext(),R.layout.nrc_spinner_item_3, nrcType);

        spinnerDivCode = findViewById(R.id.spinner_div_code);
        autoCompleteTwspCode = findViewById(R.id.auto_comp_twsp_code);
        spinnerType = findViewById(R.id.spinner_nrc_type);
        spinnerType.setAdapter(adapterType);

        btnSave = findViewById(R.id.btn_save_registration);

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
        params.width = 140;

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        changeLabel(curLang);

        //get phone no. to call.
        getPhoneNo();

        //Get NRC Information.
        getNrcInfoService = APIClient.getUserService();
        final Call<BaseResponse<List<TownshipCodeResDto>>> req = getNrcInfoService.getTownshipCode();

        RegistrationActivity.this.setTheme(R.style.MessageDialogTheme);
        final ProgressDialog twspCodeDialog = new ProgressDialog(RegistrationActivity.this);
        twspCodeDialog.setMessage(getString(R.string.dialog_loading_nrc));
        twspCodeDialog.setCancelable(false);
        twspCodeDialog.show();

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

                                    autoCompleteTwspCode.setText(BLANK);

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

                            btnSave.setEnabled(true);
                            closeDialog(twspCodeDialog);

                        } catch (Exception e){

                            closeDialog(twspCodeDialog);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }

                    } else {
                        closeDialog(twspCodeDialog);
                        serviceUnavailable.setVisibility(View.VISIBLE);
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String curLang2 = PreferencesManager.getCurrentLanguage(getApplicationContext());
                memberRegistrationInfoFormBean = new MemberRegistrationInfoFormBean();

                textRegCodeVal = textRegCode.getText().toString();
                final String townshipCodeVal = autoCompleteTwspCode.getText().toString();
                final String nrcNo = stateDivCodeVal + "/" + townshipCodeVal + nrcTypeVal + textRegCodeVal;
                final String name = textName.getText().toString().trim();
                final String dateOfBirth = textDob.getText().toString();
                final String phoneNo = textPhone.getText().toString();
                final String password = textPwd.getText().toString();
                final String confPwd = textConfPwd.getText().toString();

                boolean validate = true;

                //form validation
                if (name == null || name.equals(BLANK) || name.equals(SPACE)) {
                    txtErrName.setVisibility(View.VISIBLE);
                    validate = false;
                } else if (!isPureAscii(name)) {
                    txtErrName.setText(getNameCharErrMsg(curLang2));
                    textName.setText(BLANK);
                    txtErrName.setVisibility(View.VISIBLE);
                    validate = false;
                } else {
                    txtErrName.setVisibility(View.GONE);
                }

                if(dateOfBirth == null || dateOfBirth.equals(BLANK)){
                    txtErrDob.setVisibility(View.VISIBLE);
                    validate = false;
                } else {
                    txtErrDob.setVisibility(View.GONE);
                }

                if(textRegCodeVal==null || textRegCodeVal.equals(BLANK) ||
                townshipCodeVal==null || townshipCodeVal.equals(BLANK)){
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(!isNrcCodeValid(textRegCodeVal)){
                    changeErrRegCode(curLang2);
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(!townshipCode.contains(autoCompleteTwspCode.getText().toString())){
                    autoCompleteTwspCode.setText(BLANK);
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validate = false;
                } else {
                    txtErrNrc.setVisibility(View.GONE);
                }

                if(phoneNo == null || phoneNo.equals(BLANK)){
                    txtErrorPhNo.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(!CommonUtils.isPhoneNoValid(phoneNo)){
                    txtErrorPhNo.setText(getString(R.string.register_phoneno_format_err_msg));
                    changeErrPhone(curLang2);
                    txtErrorPhNo.setVisibility(View.VISIBLE);
                    validate = false;
                } else {
                    txtErrorPhNo.setVisibility(View.GONE);
                }

                if(password == null || password.equals(BLANK)){
                    txtErrPwd.setVisibility(View.VISIBLE);
                    validate = false;
                } if(!isAsciiString(password)) {
                    txtErrPwd.setText(getNameCharErrMsg(curLang2));
                    textPwd.setText(BLANK);
                    txtErrPwd.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(password.length()<6 || password.length()>16 || password.equals(SPACE)){
                    txtErrPwd.setText(getString(R.string.register_pwd_format_err_msg));
                    changeErrPwd(curLang2);
                    txtErrPwd.setVisibility(View.VISIBLE);
                    validate = false;
                } else {
                    txtErrPwd.setVisibility(View.GONE);
                }

                if(confPwd == null || confPwd.equals(BLANK)){
                    txtErrConfPwd.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(confPwd.length()<6 || confPwd.length()>16){
                    txtErrConfPwd.setText(getString(R.string.register_pwd_format_err_msg));
                    changeErrConfPwd(curLang2);
                    txtErrConfPwd.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(!confPwd.equals(password)){
                    txtErrConfPwd.setText(getString(R.string.register_confpwd_format_err_msg));
                    changeErrConfPwd(curLang2);
                    textConfPwd.setText(BLANK);
                    txtErrConfPwd.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(!isAsciiString(password)) {
                    txtErrConfPwd.setText(getNameCharErrMsg(curLang2));
                    textConfPwd.setText(BLANK);
                    txtErrConfPwd.setVisibility(View.VISIBLE);
                    validate = false;
                } else {
                    txtErrConfPwd.setVisibility(View.GONE);
                }

                //if valid all
                if (validate) {

                    if (!CommonUtils.isNetworkAvailable(getApplicationContext())) {
                        showNetworkErrorDialog(RegistrationActivity.this,getNetErrMsg());
                    } else {

                        RegistrationActivity.this.setTheme(R.style.MessageDialogTheme);
                        final ProgressDialog checkMemberDialog = new ProgressDialog(RegistrationActivity.this);
                        checkMemberDialog.setMessage(getString(R.string.progress_checking_member));
                        checkMemberDialog.setCancelable(false);
                        checkMemberDialog.show();

                        //(1) | check with nrcNo | dateOfBirth.
                        checkMemberInformationReqBean =
                                new CheckMemberInformationReqBean();

                        checkMemberInformationReqBean.setNrcNo(nrcNo);
                        checkMemberInformationReqBean.setDateOfBirth(dateOfBirth);
                        checkMemberInformationReqBean.setPhoneNo(phoneNo);
                        checkMemberInformationReqBean.setPassword(password.trim());
                        checkMemberInformationReqBean.setName(name.trim());

                        checkMemberInfoService = APIClient.getUserService();
                        Call<BaseResponse<CustomerInfoBean>> req2 = checkMemberInfoService.checkMemberInfo(checkMemberInformationReqBean);

                        req2.enqueue(new Callback<BaseResponse<CustomerInfoBean>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<CustomerInfoBean>> call, Response<BaseResponse<CustomerInfoBean>> response) {

                                if (response.isSuccessful()) {

                                    BaseResponse baseResponse = response.body();

                                    if (baseResponse.getStatus().equals(SUCCESS)) {

                                        //clear sec-question-ans data.
                                        RegistrationSecQAConfirmActivity.tempAnswers=null;
                                        RegistrationSecQAConfirmActivity.tempSpinnerPosition=null;

                                        final CustomerInfoBean customerInfoBean = (CustomerInfoBean)baseResponse.getData();
                                        final String memberStatus = customerInfoBean.getMemberStatus();
                                        final String memberPhoneNo = customerInfoBean.getMemberPhoneNo();

                                        if (memberStatus.equals(MEMBER)) {

                                            closeDialog(checkMemberDialog);

                                            //Reuse the version-message-dialog.
                                            final Dialog dialog = new Dialog(RegistrationActivity.this);
                                            dialog.setContentView(R.layout.registration_success_dialog);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                            TextView textMessage = dialog.findViewById(R.id.text_message1);
                                            textMessage.setText(getMemberMessage(memberPhoneNo));

                                            TextView textMessage2 = dialog.findViewById(R.id.text_message2);
                                            textMessage2.setText(getMemberMessage2());

                                            Button btnCallNow = dialog.findViewById(R.id.rbtn_callNow);
                                            btnCallNow.setText("CALL NOW");
                                            btnCallNow.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (isCallAllowed()) {
                                                        dialog.dismiss();
                                                        if (hotlinePhoneNo == null || hotlinePhoneNo.equals(BLANK)) {
                                                            showSnackBarMessage("Call not available.");
                                                        } else {
                                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                            callIntent.setData(Uri.parse(PHONE_URI_PREFIX + hotlinePhoneNo));
                                                            startActivity(callIntent);
                                                        }
                                                    }
                                                }
                                            });

                                            Button btnOk = dialog.findViewById(R.id.rbtn_ok);
                                            btnOk.setText("OK");
                                            btnOk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //(2) | get response
                                                    dialog.dismiss();
                                                    customerRegistrationFormBean = new CustomerRegistrationFormBean();
                                                    customerRegistrationFormBean.setName(name);
                                                    customerRegistrationFormBean.setNrcNo(nrcNo);
                                                    customerRegistrationFormBean.setDateOfBirth(dateOfBirth);
                                                    customerRegistrationFormBean.setPhoneNo(memberPhoneNo);
                                                    customerRegistrationFormBean.setPassword(password);

                                                    Intent intent = new Intent(RegistrationActivity.this, RegistrationSecQAConfirmActivity.class);
                                                    intent.putExtra("cust_reg_form_data", customerRegistrationFormBean);
                                                    intent.putExtra("message", MEMBER);
                                                    startActivity(intent);
                                                }
                                            });
                                            dialog.show();

                                        } else {    //non-member.

                                            closeDialog(checkMemberDialog);

                                            //form-data.
                                            customerRegistrationFormBean = new CustomerRegistrationFormBean();
                                            customerRegistrationFormBean.setName(name);
                                            customerRegistrationFormBean.setDateOfBirth(dateOfBirth);
                                            customerRegistrationFormBean.setNrcNo(nrcNo);
                                            customerRegistrationFormBean.setPhoneNo(phoneNo);
                                            customerRegistrationFormBean.setPassword(password);

                                            Intent intent = new Intent(RegistrationActivity.this, RegistrationSecQAConfirmActivity.class);
                                            intent.putExtra("message", NON_MEMBER);
                                            intent.putExtra("cust_reg_form_data", customerRegistrationFormBean);
                                            startActivity(intent);
                                        }

                                    } else {

                                        closeDialog(checkMemberDialog);

                                        if(baseResponse.getMessageCode().equals(PASSWORD_WEAK)){
                                            changePwdStrength(curLang2);
                                            txtErrPwd.setVisibility(View.VISIBLE);
                                        }

                                        if(baseResponse.getMessageCode().equals(DUPLICATED_PHONE_NO)){
                                            txtErrorPhNo.setText(getString(R.string.register_ph_no_dup));
                                            txtErrorPhNo.setVisibility(View.VISIBLE);
                                            changeDuplicatePh(curLang2);
                                        }

                                        if(baseResponse.getMessageCode().equals(DUPLICATED_NRC_NO)){
                                            txtErrNrc.setText(getString(R.string.register_nrc_dup));
                                            txtErrNrc.setVisibility(View.VISIBLE);
                                            changeDuplicateNrc(curLang2);
                                        }

                                        if(baseResponse.getMessageCode().equals(DUPLICATED_CUSTOMER_INFO)){
                                            showWarningDialog(RegistrationActivity.this,geDuplicateUserInfoMessage(curLang2));
                                        }

                                        if(baseResponse.getMessageCode().equals(IMPORT_PH_DUPLICATE)){
                                            showWarningDialog(RegistrationActivity.this, getString(R.string.register_imp_phone_no_duplicate));
                                        }

                                        if(baseResponse.getMessageCode().equals(DUPLICATED_NRC_NO_CORE_SYSTEM)){
                                            showWarningDialog(RegistrationActivity.this, getString(R.string.register_duplicate_core));
                                        }
                                    }
                                } else {
                                    closeDialog(checkMemberDialog);
                                    showErrorDialog(RegistrationActivity.this,getString(R.string.service_unavailable));
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse<CustomerInfoBean>> call, Throwable t) {
                                //Network Error.
                                closeDialog(checkMemberDialog);
                                showErrorDialog(RegistrationActivity.this,getString(R.string.service_unavailable));
                            }
                        });
                    }
                }
            }
        });

        textDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerFragmentDialog dialog
                        = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                        final Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat(BIRTH_DATE_FORMAT, Locale.JAPAN);
                        textDob.setText(sdf.format(myCalendar.getTime()));
                    }
                });
                myCalendar.add(Calendar.YEAR, -MIN_AGE);
                dialog.setAccentColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                dialog.setMaxDate(myCalendar.getTimeInMillis());
                dialog.show(getSupportFragmentManager(), "TAG");
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

    private void addValueToPreference(String lang){
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    private void changeLabel(String language) {

        textName.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_name_holder, getApplicationContext()));
        textDob.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_dob_holder, getApplicationContext()));
        textPhone.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_phoneno_holder, getApplicationContext()));
        textPwd.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_password_holder, getApplicationContext()));
        textConfPwd.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_password_holder, getApplicationContext()));
        textRegCode.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_reg_code, getApplicationContext()));

        txtName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_name_label, getApplicationContext()));
        txtDob.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_dob_label, getApplicationContext()));
        txtNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_label, getApplicationContext()));
        txtPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_phoneno_label, getApplicationContext()));
        txtPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_password_label, getApplicationContext()));
        txtConfPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_conpassword_label, getApplicationContext()));

        txtErrName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_name_err, getApplicationContext()));
        txtErrDob.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_dob_err, getApplicationContext()));
        txtErrNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_err, getApplicationContext()));
        txtErrorPhNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_phoneno_err, getApplicationContext()));
        txtErrPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_password_err, getApplicationContext()));
        txtErrConfPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_conpassword_err, getApplicationContext()));

        btnSave.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_save_button, getApplicationContext()));

        textCaption.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_caption, getApplicationContext()));

        txtAgeLimit.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_age_limit, getApplicationContext()));
        txtPwdLimit.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_pwd_limit, getApplicationContext()));
        PreferencesManager.setCurrentLanguage(getApplicationContext(),language);
    }

    private String getMemberMessage(String memberPhoneNo){
        String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        String message = CommonUtils.getLocaleString(new Locale(language), R.string.reg_sq_conf_info, getApplicationContext());
        return CommonUtils.replacePhoneNo(message, memberPhoneNo);
    }

    private String getMemberMessage2(){
        String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.reg_sq_conf_info2, getApplicationContext());
    }

    private void changeErrPhone(String language){
        txtErrorPhNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_phoneno_format_err_msg, getApplicationContext()));
    }

    private void changeErrPwd(String language){
        txtErrPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_pwd_format_err_msg, getApplicationContext()));
    }

    private void changePwdStrength(String language){
        txtErrPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_pwd_strength_err_msg, getApplicationContext()));
    }

    private void changeErrConfPwd(String language){
        txtErrConfPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_confpwd_format_err_msg, getApplicationContext()));
    }

    private void changeErrRegCode(String language){
        txtErrNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_format_err, getApplicationContext()));
    }

    private void changeDuplicateNrc(String language){
        txtErrNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_dup, getApplicationContext()));
    }

    private void changeDuplicatePh(String language){
        txtErrorPhNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_ph_no_dup, getApplicationContext()));
    }

    private String geDuplicateUserInfoMessage(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.register_duplicate, getApplicationContext());
    }

    private String getNameCharErrMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.register_name_format_err, getApplicationContext());
    }

    //Check call allowed or not.
    private boolean isCallAllowed() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistrationActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void getPhoneNo(){
        Service service = APIClient.getUserService();
        Call<BaseResponse<HotlineInfoResBean>> reqPh = service.getHotlineInfo();
        reqPh.enqueue(new Callback<BaseResponse<HotlineInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<HotlineInfoResBean>> call, Response<BaseResponse<HotlineInfoResBean>> response) {
                if(response.isSuccessful()){
                    BaseResponse baseResponse = response.body();
                    if(baseResponse.getStatus().equals(SUCCESS)){
                        HotlineInfoResBean hotlineInfoResBean = (HotlineInfoResBean) baseResponse.getData();
                        hotlinePhoneNo=hotlineInfoResBean.getHotlinePhone();
                    } else {
                        hotlinePhoneNo=BLANK;
                    }
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<HotlineInfoResBean>> call, Throwable t) {
                hotlinePhoneNo=BLANK;
            }
        });
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.e("Register", "start");
        changeLabel(PreferencesManager.getCurrentLanguage(this));
    }
}
