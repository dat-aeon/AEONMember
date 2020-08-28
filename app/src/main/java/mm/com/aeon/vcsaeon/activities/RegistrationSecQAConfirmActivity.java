package mm.com.aeon.vcsaeon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.core.content.res.ResourcesCompat;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.AnsweredSecurityQuestionReqBean;
import mm.com.aeon.vcsaeon.beans.AppUsageInfoReqBean;
import mm.com.aeon.vcsaeon.beans.BiometricSensorStatus;
import mm.com.aeon.vcsaeon.beans.CurrentUserInformationResBean;
import mm.com.aeon.vcsaeon.beans.CustomerRegistrationFormBean;
import mm.com.aeon.vcsaeon.beans.ExistedMemberRegistrationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.MemberRegistrationInfoFormBean;
import mm.com.aeon.vcsaeon.beans.NewMemberRegistrationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.RegSecurityQuestionInfoResBean;
import mm.com.aeon.vcsaeon.beans.SecurityQuestionResDto;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MEMBER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PASSWORD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.hideKeyboard;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isUnique;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class RegistrationSecQAConfirmActivity extends BaseActivity {

    Toolbar toolbar;
    Button btnSave;
    Service getSecurityQuestionService;

    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackBtn;

    TextView textTitle;
    TextView textDuplicateQuestion;

    static int numSecQues = 0;
    static int numAnsCharacter = 0;

    private static List<TextView> textViewList = new ArrayList<TextView>();
    private static List<EditText> editTextList = new ArrayList<EditText>();
    private static List<Spinner> spinnerList = new ArrayList<Spinner>();

    private static List<SecurityQuestionResDto> securityQuestionResDtoList;
    ExistedMemberRegistrationInfoReqBean existedMemberRegistrationInfoReqBean;
    static String message;

    Service registerNewUserService;

    //data
    MemberRegistrationInfoFormBean memRegInfoFormBean;
    NewMemberRegistrationInfoReqBean newMemberRegistrationInfoReqBean;
    AppUsageInfoReqBean appUsageInfoReqBean;
    List<AnsweredSecurityQuestionReqBean> securityAnsweredInfoList;
    AnsweredSecurityQuestionReqBean ansSecQuesReqBean;
    CustomerRegistrationFormBean customerRegistrationFormBean;

    SharedPreferences preferences;
    SharedPreferences myPreferences;

    View serviceUnavailable;

    static ArrayList<String> tempAnswers;
    static ArrayList<Integer> tempSpinnerPosition;

    @Override
    protected void onPause() {
        super.onPause();

        tempAnswers = new ArrayList<>();
        tempSpinnerPosition = new ArrayList<>();

        for (int i = 0; i < editTextList.size(); i++) {
            String answer = editTextList.get(i).getText().toString().trim();
            tempAnswers.add(answer);
            tempSpinnerPosition.add(spinnerList.get(i).getSelectedItemPosition());
        }

    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_security_qa);

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
                recreate();
            }
        });

        engTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(engTitleBtn.getTag().toString());
                recreate();
            }
        });

        menuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        serviceUnavailable = findViewById(R.id.service_unavailable_sqa);
        serviceUnavailable.setVisibility(View.GONE);

        btnSave = findViewById(R.id.btn_confirm_sec_qa_2);

        textTitle = findViewById(R.id.sec_qa_reg_form_title);
        textDuplicateQuestion = findViewById(R.id.duplicate_question);

        appUsageInfoReqBean = new AppUsageInfoReqBean();
        appUsageInfoReqBean.setCpuArchitecture(System.getProperty("os.arch"));
        appUsageInfoReqBean.setInstructionSet(Build.CPU_ABI);
        appUsageInfoReqBean.setManufacture(Build.MANUFACTURER);
        appUsageInfoReqBean.setOsType(CommonConstants.OS_TYPE);
        appUsageInfoReqBean.setOsVersion(Build.VERSION.RELEASE);
        appUsageInfoReqBean.setPhoneModel(Build.MODEL);
        appUsageInfoReqBean.setResolution(getDeviceResolution());
        appUsageInfoReqBean.setSdk(String.valueOf(Build.VERSION.SDK_INT));

        memRegInfoFormBean = new MemberRegistrationInfoFormBean();
        memRegInfoFormBean = (MemberRegistrationInfoFormBean) getIntent().getSerializableExtra("member_reg_form_bean");

        customerRegistrationFormBean = new CustomerRegistrationFormBean();
        customerRegistrationFormBean = (CustomerRegistrationFormBean) getIntent().getSerializableExtra("cust_reg_form_data");

        message = getIntent().getStringExtra("message");

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        changeLabel(curLang);


        this.setTheme(R.style.MessageDialogTheme);
        final ProgressDialog loadSecQuesDialog = new ProgressDialog(this);
        loadSecQuesDialog.setMessage(getString(R.string.progress_loading));
        loadSecQuesDialog.setCancelable(false);
        loadSecQuesDialog.show();

        getSecurityQuestionService = APIClient.getUserService();
        final Call<BaseResponse<RegSecurityQuestionInfoResBean>> req = getSecurityQuestionService.getSecurityQuestion();

        req.enqueue(new Callback<BaseResponse<RegSecurityQuestionInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<RegSecurityQuestionInfoResBean>> call, Response<BaseResponse<RegSecurityQuestionInfoResBean>> response) {

                if (response.isSuccessful()) {

                    BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        try {

                            RegSecurityQuestionInfoResBean regSecurityQuestionInfoResBean =
                                    (RegSecurityQuestionInfoResBean) baseResponse.getData();

                            LinearLayout securityAQLayout = findViewById(R.id.security_qa_layout_fp);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 0, 0, 5);

                            LinearLayout separatorLayout0 = new LinearLayout(getApplicationContext());
                            LinearLayout.LayoutParams sLayout0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                            sLayout0.setMargins(0, 0, 0, 5);
                            separatorLayout0.setLayoutParams(sLayout0);
                            separatorLayout0.setBackgroundColor(getColor(R.color.grayLight));
                            securityAQLayout.addView(separatorLayout0);

                            textViewList.clear();
                            editTextList.clear();
                            spinnerList.clear();

                            numSecQues = regSecurityQuestionInfoResBean.getNumOfSecQues();
                            numAnsCharacter = regSecurityQuestionInfoResBean.getNumOfAnsChar();
                            securityQuestionResDtoList = regSecurityQuestionInfoResBean.getSecurityQuestionDtoList();

                            //create 3 linear layout.
                            for (int i = 0; i < numSecQues; i++) {

                                //Question Layout
                                LinearLayout questionLayout = new LinearLayout(getApplicationContext());
                                LinearLayout.LayoutParams queLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                queLayoutParam.setMargins(0, 0, 0, 5);
                                questionLayout.setOrientation(LinearLayout.HORIZONTAL);

                                //TextQues
                                TextView questionLabel = new TextView(getApplicationContext());
                                questionLabel.setText("Q" + (i + 1) + ":");
                                questionLabel.setMinWidth(70);
                                questionLabel.setTextSize(12);
                                questionLabel.setGravity(Gravity.RIGHT);
                                questionLabel.setTextColor(getColor(R.color.colorPrimary));
                                questionLabel.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));
                                questionLayout.addView(questionLabel);

                                //Answer Layout.
                                LinearLayout answerLayout = new LinearLayout(getApplicationContext());
                                LinearLayout.LayoutParams ansLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getDp(40));
                                ansLayoutParam.setMargins(5, 0, 0, 15);
                                answerLayout.setLayoutParams(ansLayoutParam);
                                answerLayout.setOrientation(LinearLayout.HORIZONTAL);

                                TextView ansLabel = new TextView(getApplicationContext());
                                ansLabel.setText("Ans" + (i + 1) + ": ");
                                ansLabel.setTextColor(getColor(R.color.colorPrimary));
                                ansLabel.setMinWidth(70);
                                ansLabel.setTextSize(12);
                                ansLabel.setGravity(Gravity.RIGHT);
                                ansLabel.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));
                                answerLayout.addView(ansLabel);

                                //Question Spinner
                                Spinner appCompatSpinner = new Spinner(getApplicationContext());
                                appCompatSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                appCompatSpinner.setGravity(Gravity.CENTER_VERTICAL);

                                final List<String> questionList = new ArrayList<>();

                                for (SecurityQuestionResDto securityQuestionResDto : securityQuestionResDtoList) {
                                    if (curLang.equals(LANG_MM)) {
                                        questionList.add(securityQuestionResDto.getQuestionMM());
                                    } else {
                                        questionList.add(securityQuestionResDto.getQuestionEN());
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.security_qa_spinner_item, questionList);
                                appCompatSpinner.setAdapter(adapter);
                                appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });

                                //Indexing previous selected questions-indexes.
                                if (tempSpinnerPosition != null) {
                                    if (tempSpinnerPosition.size() > i) {
                                        appCompatSpinner.setSelection(tempSpinnerPosition.get(i));
                                    } else {
                                        tempSpinnerPosition.add(0);
                                        appCompatSpinner.setSelection(tempSpinnerPosition.get(i));
                                    }
                                }

                                //questionLayout.addView(imageView);
                                questionLayout.addView(appCompatSpinner);
                                questionLayout.setLayoutParams(params);

                                //Error for answer textbox.
                                TextView errTextView = new TextView(getApplicationContext());
                                errTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                errTextView.setVisibility(View.GONE);
                                errTextView.setPadding(80, 0, 0, 0);
                                errTextView.setText(getString(R.string.secquest_err_ans_blank));
                                errTextView.setTextColor(getColor(R.color.red));
                                errTextView.setTextSize(14);
                                errTextView.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));

                                //Answer TextBox
                                EditText textInputLayout = new EditText(getApplicationContext());
                                textInputLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                textInputLayout.setTextSize(14);
                                textInputLayout.setHeight(40);
                                textInputLayout.setPadding(8, 0, 0, 0);
                                textInputLayout.setTextColor(getResources().getColor(R.color.black));
                                textInputLayout.setSingleLine(true);
                                InputFilter[] FilterArray = new InputFilter[1];
                                FilterArray[0] = new InputFilter.LengthFilter(numAnsCharacter);
                                textInputLayout.setFilters(FilterArray);
                                textInputLayout.setHintTextColor(getResources().getColor(R.color.grayLight));
                                textInputLayout.setBackground(getDrawable(R.drawable.edit_text_style));
                                if (tempAnswers != null) {
                                    //Indexing previous entered answers.
                                    if (tempAnswers.size() > i) {
                                        textInputLayout.setText(tempAnswers.get(i));
                                    } else {
                                        tempAnswers.add(BLANK);
                                        textInputLayout.setText(tempAnswers.get(i));
                                    }
                                }
                                textInputLayout.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));
                                answerLayout.addView(textInputLayout);

                                textViewList.add(errTextView);
                                editTextList.add(textInputLayout);
                                spinnerList.add(appCompatSpinner);

                                LinearLayout separatorLayout = new LinearLayout(getApplicationContext());
                                LinearLayout.LayoutParams sLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                                sLayout.setMargins(0, 10, 0, 5);
                                separatorLayout.setLayoutParams(sLayout);
                                separatorLayout.setBackgroundColor(getColor(R.color.grayLight));

                                securityAQLayout.addView(questionLayout);
                                securityAQLayout.addView(errTextView);
                                securityAQLayout.addView(answerLayout);
                                securityAQLayout.addView(separatorLayout);

                            }

                            btnSave.setEnabled(true);
                            closeDialog(loadSecQuesDialog);

                        } catch (Exception e) {

                            e.printStackTrace();
                            closeDialog(loadSecQuesDialog);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }

                    } else {
                        closeDialog(loadSecQuesDialog);
                        serviceUnavailable.setVisibility(View.VISIBLE);
                    }

                } else {
                    closeDialog(loadSecQuesDialog);
                    serviceUnavailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<RegSecurityQuestionInfoResBean>> call, Throwable t) {
                //network error.
                closeDialog(loadSecQuesDialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = true;

                String[] answers = new String[numSecQues];
                int[] spinnerPosition = new int[numSecQues];
                int[] spinnerPosition2 = new int[numSecQues]; //for check questions duplication.
                int errShowCount = 0;

                //check and set answered information.
                for (int i = 0; i < editTextList.size(); i++) {

                    String answer = editTextList.get(i).getText().toString().trim();
                    spinnerPosition2[i] = spinnerList.get(i).getSelectedItemPosition();

                    if (answer != null && answer.length() > 0) {
                        answers[i] = answer;
                        spinnerPosition[i] = spinnerList.get(i).getSelectedItemPosition();
                        textViewList.get(i).setVisibility(View.GONE);
                    } else {
                        textViewList.get(i).setVisibility(View.VISIBLE);
                        validation = false;
                        errShowCount++;
                    }

                    if (answer.isEmpty()) {
                        textViewList.get(i).setText(getAnsBlankErrMsg(curLang));
                        textViewList.get(i).setVisibility(View.VISIBLE);
                        validation = false;
                        errShowCount++;
                    } else if (!isPureAscii(answer)) {
                        textViewList.get(i).setText(getAnsCharErrMsg(curLang));
                        textViewList.get(i).setVisibility(View.VISIBLE);
                        validation = false;
                        errShowCount++;
                    }

                }

                //check duplication question selected.
                if ((!isUnique(spinnerPosition2)) && (errShowCount == 0)) {
                    textDuplicateQuestion.setVisibility(View.VISIBLE);
                    validation = false;
                } else {
                    textDuplicateQuestion.setVisibility(View.GONE);
                }

                if (validation) {

                    if (!CommonUtils.isNetworkAvailable(getApplicationContext())) {
                        showNetworkErrorDialog(RegistrationSecQAConfirmActivity.this, getNetErrMsg());

                    } else {

                        securityAnsweredInfoList = new ArrayList<>();
                        for (int j = 0; j < spinnerPosition.length; j++) {
                            ansSecQuesReqBean = new AnsweredSecurityQuestionReqBean();
                            ansSecQuesReqBean.setSecQuesId(securityQuestionResDtoList.get(spinnerPosition[j]).getSecQuestionId());
                            ansSecQuesReqBean.setAnswer(answers[j]);
                            securityAnsweredInfoList.add(ansSecQuesReqBean);
                        }

                        if (message.equals(MEMBER)) {

                            existedMemberRegistrationInfoReqBean = new ExistedMemberRegistrationInfoReqBean();
                            existedMemberRegistrationInfoReqBean.setDob(customerRegistrationFormBean.getDateOfBirth());
                            existedMemberRegistrationInfoReqBean.setNrcNo(customerRegistrationFormBean.getNrcNo());
                            existedMemberRegistrationInfoReqBean.setPhoneNo(customerRegistrationFormBean.getPhoneNo());
                            existedMemberRegistrationInfoReqBean.setPassword(customerRegistrationFormBean.getPassword());
                            existedMemberRegistrationInfoReqBean.setAppUsageInfoDto(appUsageInfoReqBean);
                            existedMemberRegistrationInfoReqBean.setCustomerSecurityQuestionDtoList(securityAnsweredInfoList);

                            PreferencesManager.setRegistrationCompleted(getApplicationContext(), true);
                            Intent intent = new Intent(RegistrationSecQAConfirmActivity.this, RegistrationPhotoUploadActivity.class);
                            intent.putExtra(EXISTED_REG_MEM_INFO, existedMemberRegistrationInfoReqBean);
                            startActivity(intent);

                        } else { //non-member.

                            newMemberRegistrationInfoReqBean = new NewMemberRegistrationInfoReqBean();
                            newMemberRegistrationInfoReqBean.setPassword(customerRegistrationFormBean.getPassword());
                            newMemberRegistrationInfoReqBean.setPhoneNo(customerRegistrationFormBean.getPhoneNo());
                            newMemberRegistrationInfoReqBean.setName(customerRegistrationFormBean.getName());
                            newMemberRegistrationInfoReqBean.setDob(customerRegistrationFormBean.getDateOfBirth());
                            newMemberRegistrationInfoReqBean.setNrcNo(customerRegistrationFormBean.getNrcNo());
                            newMemberRegistrationInfoReqBean.setAppUsageInfoDto(appUsageInfoReqBean);
                            newMemberRegistrationInfoReqBean.setCustomerSecurityQuestionDtoList(securityAnsweredInfoList);

                            RegistrationSecQAConfirmActivity.this.setTheme(R.style.MessageDialogTheme);
                            final ProgressDialog registerNewDialog = new ProgressDialog(RegistrationSecQAConfirmActivity.this);
                            registerNewDialog.setMessage(getString(R.string.progress_registering));
                            registerNewDialog.setCancelable(false);
                            registerNewDialog.show();

                            registerNewUserService = APIClient.getUserService();
                            Call<BaseResponse> req2 = registerNewUserService.registerNewCustomer(newMemberRegistrationInfoReqBean);

                            req2.enqueue(new Callback<BaseResponse>() {

                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                    if (response.isSuccessful()) {

                                        final BaseResponse baseResponse = response.body();

                                        if (baseResponse.getStatus().equals(SUCCESS)) {

                                            //do login. | call login api.
                                            Service loginService = APIClient.getAuthUserService();

                                            Call<BaseResponse<LoginAccessTokenInfo>> loginReq = loginService.doLogin(customerRegistrationFormBean.getPhoneNo(),
                                                    customerRegistrationFormBean.getPassword(), PASSWORD, getLoginDeviceId());

                                            loginReq.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                                                @Override
                                                public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {

                                                    if (response.isSuccessful()) {

                                                        final BaseResponse loginBaseResponse = response.body();

                                                        if (loginBaseResponse.getStatus().equals(SUCCESS)) {

                                                            closeDialog(registerNewDialog);

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
                                                            userInformationFormBean.setPhotoPath(curUserInfo.getPhotoPath());
                                                            userInformationFormBean.setHotlinePhone(curUserInfo.getHotlinePhone());
                                                            userInformationFormBean.setMemberNo(curUserInfo.getMemberNo());
                                                            userInformationFormBean.setMemberNoValid(curUserInfo.isMemberNoValid());
                                                            userInformationFormBean.setCustAgreementListDtoList(curUserInfo.getCustomerAgreementDtoList());

                                                            final String userInfoFormJson = new Gson().toJson(userInformationFormBean);
                                                            preferences = PreferencesManager.getCurrentUserPreferences(getApplicationContext());
                                                            PreferencesManager.setCurrentUserInfo(getApplicationContext(), userInfoFormJson);
                                                            PreferencesManager.setCurrentLoginPhoneNo(getApplicationContext(), userInformationFormBean.getPhoneNo());
                                                            PreferencesManager.setBiometricRegPhoneNo(getApplicationContext(), userInformationFormBean.getPhoneNo());

                                                            PreferencesManager.setRegistrationCompleted(getApplicationContext(), true);
                                                            //Check biometric sensor in device.
                                                            BiometricSensorStatus biometricSensorStatus = CommonUtils.checkBiometricSensor(getApplicationContext());

                                                            switch (biometricSensorStatus) {

                                                                case BIOMETRIC_OK:
                                                                    //Biometric Registration Suggestion.
                                                                    final Dialog dialog = new Dialog(RegistrationSecQAConfirmActivity.this);
                                                                    dialog.setCancelable(false);
                                                                    dialog.setContentView(R.layout.biometric_registration_dialog);
                                                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                    TextView textView = dialog.findViewById(R.id.text_message);
                                                                    textView.setText(getBioSuggessionAlertMsg(curLang));
                                                                    Button btnYes = dialog.findViewById(R.id.btn_yes);
                                                                    Button btnNo = dialog.findViewById(R.id.btn_no);

                                                                    btnYes.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            dialog.dismiss();
                                                                            startActivity(intentBiometricRegister(getApplicationContext()));
                                                                            finish();
                                                                        }
                                                                    });

                                                                    btnNo.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            dialog.dismiss();
                                                                            //clear coupon_info | Go to main menu.
                                                                            PreferencesManager.clearCouponInfo(getApplicationContext());
                                                                            startActivity(intentMainMenuDrawer(RegistrationSecQAConfirmActivity.this));
                                                                            finish();
                                                                        }
                                                                    });

                                                                    dialog.show();
                                                                    break;

                                                                default:
                                                                    //clear coupon_info | Go to main menu.
                                                                    PreferencesManager.clearCouponInfo(getApplicationContext());
                                                                    startActivity(intentMainMenuDrawer(RegistrationSecQAConfirmActivity.this));
                                                                    finish();
                                                                    break;
                                                            }

                                                        } else {
                                                            closeDialog(registerNewDialog);
                                                            goToLoginScreen();
                                                        }

                                                    } else {
                                                        closeDialog(registerNewDialog);
                                                        goToLoginScreen();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                                                    closeDialog(registerNewDialog);
                                                    goToLoginScreen();
                                                }
                                            });

                                        } else {

                                            closeDialog(registerNewDialog);

                                            if (baseResponse.getMessageCode().equals(DUPLICATED_PHONE_NO)) {
                                                showWarningDialog(RegistrationSecQAConfirmActivity.this, getDuplicatePhoneNo(curLang));

                                            } else if (baseResponse.getMessageCode().equals(DUPLICATED_NRC_NO)) {
                                                showWarningDialog(RegistrationSecQAConfirmActivity.this, getDuplicateNrcNo(curLang));

                                            } else if (baseResponse.getMessageCode().equals(DUPLICATED_CUSTOMER_INFO)) {
                                                showWarningDialog(RegistrationSecQAConfirmActivity.this, getDuplicateCustomerInfo(curLang));

                                            } else if (baseResponse.getMessageCode().equals(IMPORT_PH_DUPLICATE)) {
                                                showWarningDialog(RegistrationSecQAConfirmActivity.this, getImportPhoneNoDuplicate(curLang));
                                            } else {
                                                showErrorDialog(RegistrationSecQAConfirmActivity.this, getRegisterFailedMsg(curLang));
                                            }
                                        }

                                    } else {
                                        closeDialog(registerNewDialog);
                                        showErrorDialog(RegistrationSecQAConfirmActivity.this, getRegisterFailedMsg(curLang));
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    closeDialog(registerNewDialog);
                                    showErrorDialog(RegistrationSecQAConfirmActivity.this, getString(R.string.service_unavailable));
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        changeLabel(PreferencesManager.getCurrentLanguage(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
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

            if (item.getTitle().equals(LANG_MM)) {
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
                recreate();
            } else if (item.getTitle().equals(LANG_EN)) {
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
                recreate();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Get screen resolutions.
    private String getDeviceResolution() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x + "x" + size.y;
    }

    private void addValueToPreference(String lang) {
        PreferencesManager.setCurrentLanguage(getApplicationContext(), lang);
    }

    private void changeLabel(String language) {
        btnSave.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_save_button, getApplicationContext()));
        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.sec_qa_reg_title, getApplicationContext()));
        textDuplicateQuestion.setText(CommonUtils.getLocaleString(new Locale(language), R.string.secquest_err_que_same, getApplicationContext()));
        addValueToPreference(language);
    }

    //get Messages
    private String getAnsBlankErrMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.reg_sec_ans_blank, getApplicationContext());
    }

    private String getAnsCharErrMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.secquest_ans_err, getApplicationContext());
    }

    private String getBioSuggessionAlertMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.bio_dialog_reg, getApplicationContext());
    }

    private String getDuplicatePhoneNo(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.register_ph_no_dup, getApplicationContext());
    }

    private String getDuplicateNrcNo(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_dup, getApplicationContext());
    }

    private String getDuplicateCustomerInfo(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.register_duplicate, getApplicationContext());
    }

    private String getImportPhoneNoDuplicate(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.register_imp_phone_no_duplicate, getApplicationContext());
    }

    private String getRegisterFailedMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.register_failed, getApplicationContext());
    }

    //Show Dialog and go to Login Screen.
    private void goToLoginScreen() {
        final Dialog dialog = new Dialog(RegistrationSecQAConfirmActivity.this);
        dialog.setContentView(R.layout.success_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        messageBody.setText(getString(R.string.registration_done));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(RegistrationSecQAConfirmActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }

    //convert int to dp.
    private int getDp(int pixel) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int dp = (int) (pixel * scale + 0.5f);
        return dp;
    }

    private static Intent intentMainMenuDrawer(Context context) {
        Intent intent = new Intent(context, MainMenuActivityDrawer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    private static Intent intentBiometricRegister(Context context) {
        Intent intent = new Intent(context, BiometricRegistrationInRegister.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
