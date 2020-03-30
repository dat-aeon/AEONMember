package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.SecQAUpdateInfoResDto;
import mm.com.aeon.vcsaeon.beans.SecurityQAReqBean;
import mm.com.aeon.vcsaeon.beans.UpdateUserQAInfoResBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.beans.VerifyAnsweredSecQuesReqBean;
import mm.com.aeon.vcsaeon.beans.VerifyQAInfoReqBean;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NOT_EXIST_CUSTOMER_ANSWER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class VerifySecurityQuestionFragment extends BaseFragment {

    View view;
    View serviceUnavailable;

    TextView textTitle;

    Button btnVerify;

    static int numSecQues=0;
    static int numToUpdate=0;
    int i=0;

    private static List<EditText> editTextList = new ArrayList<EditText>();
    private static List<TextView> textViewAList = new ArrayList<TextView>();
    private static List<TextView> textViewQList = new ArrayList<TextView>();

    private static List<SecQAUpdateInfoResDto> secQAUpdateInfoResDtoList;

    Service getSecurityQuestionService;
    Service verifySecurityQAService;

    UserInformationFormBean userInformationFormBean;

    VerifyQAInfoReqBean verifyQAInfoReqBean;

    static ArrayList<String> tempAnswers;

    @Override
    public void onPause() {
        super.onPause();
        tempAnswers = new ArrayList<>();
        for (int i = 0; i < editTextList.size(); i++) {
            String answer = editTextList.get(i).getText().toString().trim();
            tempAnswers.add(answer);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.verify_security_qa, container, false);

        textTitle = view.findViewById(R.id.sec_qa_verify_form_title);
        btnVerify = view.findViewById(R.id.btn_verify_sq);

        serviceUnavailable = view.findViewById(R.id.service_unavailable_verify_qa);
        serviceUnavailable.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        final String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        getActivity().setTheme(R.style.MessageDialogTheme);
        final ProgressDialog loadSecQuesDialog = new ProgressDialog(getActivity());
        loadSecQuesDialog.setMessage(getString(R.string.progress_loading));
        loadSecQuesDialog.setCancelable(false);
        loadSecQuesDialog.show();

        getSecurityQuestionService = APIClient.getUserService();
        Call<BaseResponse<UpdateUserQAInfoResBean>> req = getSecurityQuestionService.getUpdateSecurityQuestion(
                PreferencesManager.getAccessToken(getActivity()),
                new SecurityQAReqBean(userInformationFormBean.getCustomerId()));

        req.enqueue(new Callback<BaseResponse<UpdateUserQAInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<UpdateUserQAInfoResBean>> call, Response<BaseResponse<UpdateUserQAInfoResBean>> response) {

                if(response.isSuccessful()) {

                    final BaseResponse baseResponse = response.body();
                    final UpdateUserQAInfoResBean updateUserQAInfoResBean =
                            (UpdateUserQAInfoResBean) baseResponse.getData();

                    secQAUpdateInfoResDtoList = updateUserQAInfoResBean.getCustomerSecurityQuestionDtoList();

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        try{

                            LinearLayout securityAQLayout = view.findViewById(R.id.security_qa_layout_fp);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(5, 20, 0, 5);

                            LinearLayout separatorLayout0 = new LinearLayout(getActivity());
                            LinearLayout.LayoutParams sLayout0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                            sLayout0.setMargins(0,0,0,5);
                            separatorLayout0.setLayoutParams(sLayout0);
                            separatorLayout0.setBackgroundColor(getActivity().getColor(R.color.grayLight));
                            securityAQLayout.addView(separatorLayout0);

                            textViewAList.clear();
                            editTextList.clear();
                            textViewQList.clear();

                            numToUpdate = secQAUpdateInfoResDtoList.size();
                            numSecQues = secQAUpdateInfoResDtoList.size();

                            for (int j = 0; j < numToUpdate; j++) {

                                SecQAUpdateInfoResDto secQAUpdateInfoResDto = secQAUpdateInfoResDtoList.get(j);

                                //Question Layout
                                LinearLayout questionLayout = new LinearLayout(getActivity());
                                LinearLayout.LayoutParams queLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                queLayoutParam.setMargins(5, 0, 0, 5);
                                questionLayout.setLayoutParams(queLayoutParam);
                                questionLayout.setOrientation(LinearLayout.HORIZONTAL);

                                //TextQues
                                TextView questionLabel = new TextView(getActivity());
                                questionLabel.setText("Q"+(j+1)+":");
                                questionLabel.setMinWidth(70);
                                questionLabel.setTextSize(12);
                                questionLabel.setGravity(Gravity.RIGHT);
                                questionLabel.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
                                questionLabel.setTextColor(getActivity().getColor(R.color.colorPrimary));
                                questionLayout.addView(questionLabel);

                                //Answer Layout.
                                LinearLayout answerLayout = new LinearLayout(getActivity());
                                LinearLayout.LayoutParams ansLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getDp(40));
                                ansLayoutParam.setMargins(5, 0, 0, 15);
                                answerLayout.setLayoutParams(ansLayoutParam);
                                answerLayout.setOrientation(LinearLayout.HORIZONTAL);

                                TextView ansLabel = new TextView(getActivity());
                                ansLabel.setText("Ans"+(j+1)+": ");
                                ansLabel.setTextColor(getActivity().getColor(R.color.colorPrimary));
                                ansLabel.setMinWidth(70);
                                ansLabel.setTextSize(12);
                                ansLabel.setGravity(Gravity.RIGHT);
                                ansLabel.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
                                answerLayout.addView(ansLabel);

                                TextView textQuestion = new TextView(getActivity());
                                LinearLayout.LayoutParams questionTextParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                questionTextParam.setMargins(10, 0, 0, 0);
                                textQuestion.setLayoutParams(questionTextParam);
                                textQuestion.setGravity(Gravity.CENTER_VERTICAL);
                                textQuestion.setTextColor(getActivity().getColor(R.color.colorPrimary));
                                textQuestion.setTextSize(14);
                                textQuestion.setId(secQAUpdateInfoResDto.getSecQuesId());
                                if (curLang.equals(LANG_MM)) {
                                    textQuestion.setText(secQAUpdateInfoResDto.getQuestionMyan());
                                } else {
                                    textQuestion.setText(secQAUpdateInfoResDto.getQuestionEng());
                                }
                                textQuestion.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));

                                questionLayout.addView(textQuestion);
                                questionLayout.setLayoutParams(params);

                                //Error for answer textbox.
                                TextView errTextView = new TextView(getActivity());
                                errTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                errTextView.setTextSize(14);
                                errTextView.setVisibility(View.GONE);
                                errTextView.setPadding(80,0,0,0);
                                errTextView.setText(getString(R.string.secquest_err_ans_blank));
                                errTextView.setTextColor(getActivity().getColor(R.color.red));
                                errTextView.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));

                                //Answer TextBox
                                EditText textInputLayout = new EditText(getActivity());
                                final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                lparams.setMargins(10, 0, 0, 0);
                                textInputLayout.setLayoutParams(lparams);
                                textInputLayout.setTextSize(14);
                                textInputLayout.setHeight(40);
                                textInputLayout.setPadding(8, 0, 0, 0);
                                textInputLayout.setTextColor(getResources().getColor(R.color.black));
                                textInputLayout.setSingleLine(true);
                                textInputLayout.setSelection(textInputLayout.getText().length());
                                textInputLayout.setHintTextColor(getResources().getColor(R.color.grayLight));
                                textInputLayout.setBackground(getActivity().getDrawable(R.drawable.edit_text_style));
                                if(tempAnswers!=null){
                                    //Indexing previous entered answers.
                                    if(tempAnswers.size()>j){
                                        textInputLayout.setText(tempAnswers.get(j));
                                    } else {
                                        tempAnswers.add(BLANK);
                                        textInputLayout.setText(tempAnswers.get(j));
                                    }
                                }
                                textInputLayout.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));

                                answerLayout.addView(textInputLayout);
                                textViewAList.add(errTextView);
                                editTextList.add(textInputLayout);
                                textViewQList.add(textQuestion);

                                LinearLayout separatorLayout = new LinearLayout(getActivity());
                                LinearLayout.LayoutParams sLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                                sLayout.setMargins(0,10,0,5);
                                separatorLayout.setLayoutParams(sLayout);
                                separatorLayout.setBackgroundColor(getActivity().getColor(R.color.grayLight));

                                securityAQLayout.addView(questionLayout);
                                securityAQLayout.addView(errTextView);
                                securityAQLayout.addView(answerLayout);
                                securityAQLayout.addView(separatorLayout);
                            }

                            btnVerify.setEnabled(true);
                            closeDialog(loadSecQuesDialog);

                        } catch (Exception e){

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
            public void onFailure(Call<BaseResponse<UpdateUserQAInfoResBean>> call, Throwable t) {
                //network error.
                closeDialog(loadSecQuesDialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = true;

                String[] answers = new String[numSecQues];
                int[] secQuestionIds = new int[numToUpdate];
                int[] custSecQuestionIds = new int[numToUpdate];

                for (int i = 0; i < editTextList.size(); i++) {

                    String answer = editTextList.get(i).getText().toString().trim();

                    if(answer!=null && answer.length()>0){
                        answers[i] = answer;
                        secQuestionIds[i] = secQAUpdateInfoResDtoList.get(i).getSecQuesId();
                        custSecQuestionIds[i] = secQAUpdateInfoResDtoList.get(i).getCustSecQuesId();
                        textViewAList.get(i).setVisibility(View.GONE);
                    } else {
                        textViewAList.get(i).setVisibility(View.VISIBLE);
                        validation = false;
                    }

                    if(answer.isEmpty()){
                        textViewAList.get(i).setText(getAnsBlankErrMsg(curLang));
                        textViewAList.get(i).setVisibility(View.VISIBLE);
                        validation=false;
                    } else if(!isPureAscii(answer)) {
                        textViewAList.get(i).setText(getAnsCharErrMsg(curLang));
                        textViewAList.get(i).setVisibility(View.VISIBLE);
                        validation=false;
                    }

                }

                if(validation) {

                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        showNetworkErrorDialog(getActivity(),getNetErrMsg());
                    } else {

                        List<VerifyAnsweredSecQuesReqBean> resetPwdAnsweredSecQuesList = new ArrayList<>();

                        for (int j = 0; j < secQuestionIds.length; j++) {
                            VerifyAnsweredSecQuesReqBean verifyAnsweredSecQuesReqBean =
                                    new VerifyAnsweredSecQuesReqBean();
                            verifyAnsweredSecQuesReqBean.setSecQuesId(secQuestionIds[j]);
                            verifyAnsweredSecQuesReqBean.setAnswer(answers[j]);
                            resetPwdAnsweredSecQuesList.add(verifyAnsweredSecQuesReqBean);
                        }

                        verifyQAInfoReqBean = new VerifyQAInfoReqBean();
                        verifyQAInfoReqBean.setCustomerId(String.valueOf(userInformationFormBean.getCustomerId()));
                        verifyQAInfoReqBean.setSecurityQuestionAnswerReqDtoList(resetPwdAnsweredSecQuesList);

                        verifySecurityQAService = APIClient.getUserService();
                        Call<BaseResponse> req2 = verifySecurityQAService.verifyAnswer(
                                PreferencesManager.getAccessToken(getActivity()),
                                verifyQAInfoReqBean
                        );

                        getActivity().setTheme(R.style.MessageDialogTheme);
                        final ProgressDialog verifySecAnsDialog = new ProgressDialog(getActivity());
                        verifySecAnsDialog.setMessage(getString(R.string.progress_verify));
                        verifySecAnsDialog.setCancelable(false);
                        verifySecAnsDialog.show();

                        req2.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                if (response.isSuccessful()) {

                                    BaseResponse baseResponse = response.body();

                                    if(baseResponse.getStatus().equals(SUCCESS)){
                                        closeDialog(verifySecAnsDialog);
                                        replaceFragment(new VerifyPhotoUploadFragment());
                                    } else {
                                        closeDialog(verifySecAnsDialog);
                                        if(baseResponse.getMessageCode().equals(NOT_EXIST_CUSTOMER_ANSWER)){
                                            //no customer
                                            showWarningDialog(getActivity(),getNoUserInfoMsg(curLang));

                                        } else if(baseResponse.getMessageCode().equals(INVALID_CUSTOMER_ANSWER)){
                                            //customer exist. | answer not match.
                                            showWarningDialog(getActivity(),getQAInfoWrongMsg(curLang));
                                        }
                                    }

                                } else {
                                    closeDialog(verifySecAnsDialog);
                                    showErrorDialog(getActivity(),getString(R.string.message_verify_failed));
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                closeDialog(verifySecAnsDialog);
                                showErrorDialog(getActivity(),getString(R.string.message_verify_failed));
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.addToBackStack("verify_member");
        transaction.commit();
    }

    public String getAnsBlankErrMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.reg_sec_ans_blank, getActivity());
    }

    public String getAnsCharErrMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.secquest_ans_err, getActivity());
    }

    public void changeLabel(String language){
        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_security_question_title, getActivity()));
        btnVerify.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_verify_button, getActivity()));
    }

    public String getQAInfoWrongMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.resetpass_sq_wrong, getActivity());
    }

    public String getNoUserInfoMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.no_acc_info, getActivity());
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    private int getDp(int pixel){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int dp = (int) (pixel * scale + 0.5f);
        return dp;
    }

}