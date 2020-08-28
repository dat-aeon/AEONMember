package mm.com.aeon.vcsaeon.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.core.content.res.ResourcesCompat;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ForceResetPasswordReqBean;
import mm.com.aeon.vcsaeon.beans.RegSecurityQuestionInfoResBean;
import mm.com.aeon.vcsaeon.beans.ResetPasswordConfirmedInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ResetPasswordConfirmedInfoResBean;
import mm.com.aeon.vcsaeon.beans.ResetPwdAnsweredSecQuesReqBean;
import mm.com.aeon.vcsaeon.beans.SecurityQuestionResDto;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.INVALID_CUSTOMER_ANSWER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NOT_EXIST_CUSTOMER_INFO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isUnique;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class SecurityConfirmationActivity extends BaseActivity {

    Toolbar toolbar;
    Button btnConfirm;
    Button btnCall;

    TextView textTitle;
    TextView textDuplicateQuestion;

    Service getSecurityQuestionService;
    List<Map<String, String>> securityQAMapList;

    static int numSecQues = 0;
    static int numAnsCharacter = 0;

    private static List<TextView> textViewList = new ArrayList<TextView>();
    private static List<EditText> editTextList = new ArrayList<EditText>();
    private static List<Spinner> spinnerList = new ArrayList<Spinner>();

    static List<SecurityQuestionResDto> securityQuestionResDtoList;
    ForceResetPasswordReqBean forceResetPasswordReqBean;

    Service checkResetPwdInfoService;
    View serviceUnavailable;

    static ArrayList<String> tempAnswers;
    static ArrayList<Integer> tempSpinnerPosition;

    //menu
    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security_confirmation);

        forceResetPasswordReqBean = (ForceResetPasswordReqBean) getIntent().getSerializableExtra("force_password_reset_bean");
        numSecQues = getIntent().getIntExtra("num_sec_question", 0);

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

        menuBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        btnConfirm = findViewById(R.id.btn_confirm_sec_qa);
        btnCall = findViewById(R.id.btn_reset_pwd_call);
        btnConfirm.setEnabled(false);

        textTitle = findViewById(R.id.sec_qa_form_title);
        textDuplicateQuestion = findViewById(R.id.duplicate_question2);

        serviceUnavailable = findViewById(R.id.service_unavailable_sq);
        serviceUnavailable.setVisibility(View.GONE);

        securityQAMapList = new ArrayList<>();

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        getSecurityQuestionService = APIClient.getUserService();
        final Call<BaseResponse<RegSecurityQuestionInfoResBean>> req = getSecurityQuestionService.getSecurityQuestion();

        this.setTheme(R.style.MessageDialogTheme);
        final ProgressDialog loadSecQuesDialog = new ProgressDialog(this);
        loadSecQuesDialog.setMessage(getString(R.string.progress_loading));
        loadSecQuesDialog.setCancelable(false);
        loadSecQuesDialog.show();

        req.enqueue(new Callback<BaseResponse<RegSecurityQuestionInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<RegSecurityQuestionInfoResBean>> call, Response<BaseResponse<RegSecurityQuestionInfoResBean>> response) {

                if (response.isSuccessful()) {

                    BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        try {

                            final RegSecurityQuestionInfoResBean regSecurityQuestionInfoResBean =
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

                            //AnswerLayout.
                            numAnsCharacter = regSecurityQuestionInfoResBean.getNumOfAnsChar();
                            securityQuestionResDtoList = regSecurityQuestionInfoResBean.getSecurityQuestionDtoList();

                            textViewList.clear();
                            editTextList.clear();
                            spinnerList.clear();

                            //create 3 linear layout.
                            for (int i = 0; i < numSecQues; i++) {

                                //Question Layout
                                LinearLayout questionLayout = new LinearLayout(getApplicationContext());
                                LinearLayout.LayoutParams queLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                queLayoutParam.setMargins(5, 0, 0, 5);
                                questionLayout.setLayoutParams(queLayoutParam);
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
                                answerLayout.setGravity(Gravity.CENTER_VERTICAL);
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

                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.security_qa_spinner_item, questionList);
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
                                errTextView.setPadding(80, 0, 0, 0);
                                errTextView.setVisibility(View.GONE);
                                errTextView.setText(getString(R.string.secquest_err_ans_blank));
                                errTextView.setTextColor(getColor(R.color.red));
                                errTextView.setTextSize(14);
                                errTextView.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));

                                //Answer TextBox
                                EditText textInputLayout = new EditText(getApplicationContext());
                                final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                lparams.setMargins(10, 0, 0, 0);
                                textInputLayout.setLayoutParams(lparams);
                                textInputLayout.setTextSize(14);
                                textInputLayout.setHeight(40);
                                textInputLayout.setPadding(8, 10, 0, 10);
                                textInputLayout.setTextColor(getResources().getColor(R.color.black));
                                textInputLayout.setSingleLine(true);
                                textInputLayout.setHintTextColor(getResources().getColor(R.color.grayLight));
                                textInputLayout.setBackground(getDrawable(R.drawable.edit_text_style));
                                textInputLayout.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));
                                if (tempAnswers != null) {
                                    //Indexing previous entered answers.
                                    if (tempAnswers.size() > i) {
                                        textInputLayout.setText(tempAnswers.get(i));
                                    } else {
                                        tempAnswers.add(BLANK);
                                        textInputLayout.setText(tempAnswers.get(i));
                                    }
                                }
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

                            btnConfirm.setEnabled(true);
                            closeDialog(loadSecQuesDialog);

                        } catch (Exception e) {

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
                closeDialog(loadSecQuesDialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = true;

                List<ResetPwdAnsweredSecQuesReqBean> resetPwdAnsweredSecQuesList = new ArrayList<>();
                ResetPwdAnsweredSecQuesReqBean resetPwdAnsSecQuesReqBean;

                String[] answers = new String[numSecQues];
                int[] spinnerPosition = new int[numSecQues];
                int[] spinnerPosition2 = new int[numSecQues]; //for check questions duplication.
                int errShowCount = 0;

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
                        showNetworkErrorDialog(SecurityConfirmationActivity.this, getNetErrMsg());
                    } else {

                        for (int j = 0; j < editTextList.size(); j++) {

                            resetPwdAnsSecQuesReqBean = new ResetPwdAnsweredSecQuesReqBean();
                            resetPwdAnsSecQuesReqBean.setSecQuesId(securityQuestionResDtoList.get(spinnerPosition[j]).getSecQuestionId());
                            resetPwdAnsSecQuesReqBean.setAnswer(answers[j]);
                            resetPwdAnsweredSecQuesList.add(resetPwdAnsSecQuesReqBean);
                        }

                        //network call
                        ResetPasswordConfirmedInfoReqBean resetPwdConfInfoReqBean = new
                                ResetPasswordConfirmedInfoReqBean();
                        resetPwdConfInfoReqBean.setNrcNo(forceResetPasswordReqBean.getNrcNo());
                        resetPwdConfInfoReqBean.setPhoneNo(forceResetPasswordReqBean.getPhoneNo());
                        resetPwdConfInfoReqBean.setSecurityQuestionAnswerReqDtoList(resetPwdAnsweredSecQuesList);

                        checkResetPwdInfoService = APIClient.getUserService();
                        Call<BaseResponse<ResetPasswordConfirmedInfoResBean>> req3 = checkResetPwdInfoService.checkResetPwdInfo(resetPwdConfInfoReqBean);

                        SecurityConfirmationActivity.this.setTheme(R.style.MessageDialogTheme);
                        final ProgressDialog checkPwdInfoDialog = new ProgressDialog(SecurityConfirmationActivity.this);
                        checkPwdInfoDialog.setMessage("Checking Info...");
                        checkPwdInfoDialog.setCancelable(false);
                        checkPwdInfoDialog.show();

                        req3.enqueue(new Callback<BaseResponse<ResetPasswordConfirmedInfoResBean>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<ResetPasswordConfirmedInfoResBean>> call,
                                                   Response<BaseResponse<ResetPasswordConfirmedInfoResBean>> response) {

                                if (response.isSuccessful()) {

                                    BaseResponse baseResponse = response.body();

                                    if (baseResponse.getStatus().equals(SUCCESS)) {

                                        closeDialog(checkPwdInfoDialog);

                                        ResetPasswordConfirmedInfoResBean resetPwdConfirmedInfoResBean =
                                                (ResetPasswordConfirmedInfoResBean) baseResponse.getData();
                                        String phoneNo = forceResetPasswordReqBean.getPhoneNo();

                                        Intent intent = intentResetPassword(getApplicationContext(), resetPwdConfirmedInfoResBean, phoneNo);
                                        startActivity(intent);

                                    } else {

                                        closeDialog(checkPwdInfoDialog);

                                        if (baseResponse.getMessageCode().equals(INVALID_CUSTOMER_ANSWER)) {
                                            showWarningDialog(SecurityConfirmationActivity.this, getQAIncorrectMsg(curLang));
                                        } else if (baseResponse.getMessageCode().equals(NOT_EXIST_CUSTOMER_INFO)) {
                                            showWarningDialog(SecurityConfirmationActivity.this, "Invalid Phone or NRC number.");
                                        }
                                    }

                                } else {
                                    closeDialog(checkPwdInfoDialog);
                                    showWarningDialog(SecurityConfirmationActivity.this, "Checking unsuccessful!");
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse<ResetPasswordConfirmedInfoResBean>> call, Throwable t) {
                                closeDialog(checkPwdInfoDialog);
                                showErrorDialog(SecurityConfirmationActivity.this, getString(R.string.service_unavailable));
                            }
                        });
                    }
                }
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCallAllowed()) {
                    final String hotlinePhoneNo = PreferencesManager.getHotlinePhone(getApplicationContext());
                    if (hotlinePhoneNo == null || hotlinePhoneNo.equals(BLANK)) {
                        showSnackBarMessage(getString(R.string.message_call_not_available));
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX + hotlinePhoneNo));
                        startActivity(callIntent);
                    }
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }
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

    public void addValueToPreference(String lang) {
        PreferencesManager.setCurrentLanguage(getApplicationContext(), lang);
    }

    public void changeLabel(String language) {
        btnConfirm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.secquestconfirm_confrim_button, getApplicationContext()));
        btnCall.setText(CommonUtils.getLocaleString(new Locale(language), R.string.aboutus_all_now_button, getApplicationContext()));
        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.secquestconfirm_title, getApplicationContext()));
        textDuplicateQuestion.setText(CommonUtils.getLocaleString(new Locale(language), R.string.secquest_err_que_same, getApplicationContext()));
        addValueToPreference(language);
    }

    public String getAnsBlankErrMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.reg_sec_ans_blank, getApplicationContext());
    }

    public String getAnsCharErrMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.secquest_ans_err, getApplicationContext());
    }

    public String getQAIncorrectMsg(String language) {
        return CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_sq_wrong, getApplicationContext());
    }

    //Check call allowed or not.
    public boolean isCallAllowed() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SecurityConfirmationActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    0);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }

    private int getDp(int pixel) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int dp = (int) (pixel * scale + 0.5f);
        return dp;
    }

    private static Intent intentResetPassword(Context context, ResetPasswordConfirmedInfoResBean resetPwdInfo, String phoneNo) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra("reset_pwd_conf_res_bean", resetPwdInfo);
        intent.putExtra("phone_no", phoneNo);
        return intent;
    }

}
