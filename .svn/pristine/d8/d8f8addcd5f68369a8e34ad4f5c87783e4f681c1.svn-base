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

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.AnsweredSecurityQuestionReqBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.SecQAUpdateInfoResDto;
import mm.com.aeon.vcsaeon.beans.SecurityQAReqBean;
import mm.com.aeon.vcsaeon.beans.SecurityQAUpdateInfo;
import mm.com.aeon.vcsaeon.beans.UpdateUserQAInfoReqBean;
import mm.com.aeon.vcsaeon.beans.UpdateUserQAInfoResBean;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.INCORRECT_PASSWORD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REFRESH_TOKEN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showSuccessDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.CODE_TOKEN_TIMEOUT;

public class NavSecQAInfoUpdateTabFragment extends BaseFragment {

    View view;
    View serviceUnavailable;

    Button btnUpdate;
    TextView txtErrPwd;
    EditText textPwd;
    ImageView imageView;

    TextView textTitle;

    TextView txtPwd;

    Service getSecurityQuestionService;
    Service doUpdateSecQAService;

    static int numSecQues=0;
    static int numToUpdate=0;
    static int ansCount=0;

    int i;

    static List<SecurityQAUpdateInfo> ansSecurityQAUpdateInfoList;
    SecurityQAUpdateInfo securityQAUpdateInfo;

    private static List<TextView> textViewAList = new ArrayList<TextView>();
    private static List<EditText> editTextList = new ArrayList<EditText>();
    private static List<TextView> textViewQList = new ArrayList<TextView>();

    private static List<SecQAUpdateInfoResDto> secQAUpdateInfoResDtoList;

    UserInformationFormBean userInformationFormBean;

    List<AnsweredSecurityQuestionReqBean> securityAnsweredInfoList;

    static List<String> ansList;
    private String curLang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.information_update_tab, container, false);
        setHasOptionsMenu(true);

        serviceUnavailable = view.findViewById(R.id.service_unavailable_update_qa);
        serviceUnavailable.setVisibility(View.GONE);

        btnUpdate = view.findViewById(R.id.btn_update_info);
        textPwd = view.findViewById(R.id.txt_new_conf_pwd_upd);
        txtErrPwd = view.findViewById(R.id.verify_err_pas);
        imageView = view.findViewById(R.id.img_pwd_visible);
        txtPwd = view.findViewById(R.id.txt_pwd_update);

        textTitle = view.findViewById(R.id.sec_qa_upd_form_title);

        curLang = PreferencesManager.getCurrentLanguage(getActivity());
        changeLabel(curLang);

        getSecurityQuestionService = APIClient.getUserService();
        Call<BaseResponse<UpdateUserQAInfoResBean>> req = getSecurityQuestionService.getUpdateSecurityQuestion(
                PreferencesManager.getAccessToken(getActivity()),
                new SecurityQAReqBean(userInformationFormBean.getCustomerId()));

        getActivity().setTheme(R.style.MessageDialogTheme);
        final ProgressDialog loadSecQuesDialog = new ProgressDialog(getActivity());
        loadSecQuesDialog.setMessage("Loading...");
        loadSecQuesDialog.setCancelable(false);
        loadSecQuesDialog.show();

        req.enqueue(new Callback<BaseResponse<UpdateUserQAInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<UpdateUserQAInfoResBean>> call, Response<BaseResponse<UpdateUserQAInfoResBean>> response) {

                if (response.isSuccessful()) {

                    final BaseResponse baseResponse = response.body();
                    final UpdateUserQAInfoResBean updateUserQAInfoResBean =
                            (UpdateUserQAInfoResBean) baseResponse.getData();

                    secQAUpdateInfoResDtoList =  updateUserQAInfoResBean.getCustomerSecurityQuestionDtoList();
                    ansCount = updateUserQAInfoResBean.getNumOfAnsChar();

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        try{

                            ansList = new ArrayList<>();

                            LinearLayout securityAQLayout = view.findViewById(R.id.security_qa_layout_upd_info);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,20,0,5);

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

                            for(int j=0;j<numToUpdate;j++){

                                SecQAUpdateInfoResDto  secQAUpdateInfoResDto = secQAUpdateInfoResDtoList.get(j);

                                //Question Layout
                                LinearLayout questionLayout = new LinearLayout(getActivity());
                                LinearLayout.LayoutParams queLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                queLayoutParam.setMargins(5,0,0,5);
                                questionLayout.setOrientation(LinearLayout.HORIZONTAL);

                                //TextQues
                                TextView questionLabel = new TextView(getActivity());
                                questionLabel.setText("Q"+(j+1)+":");
                                questionLabel.setMinWidth(70);
                                questionLabel.setTextSize(12);
                                questionLabel.setGravity(Gravity.RIGHT);
                                questionLabel.setTextColor(getActivity().getColor(R.color.colorPrimary));
                                questionLabel.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
                                questionLayout.addView(questionLabel);

                                //Answer Layout.
                                LinearLayout answerLayout = new LinearLayout(getActivity());
                                LinearLayout.LayoutParams ansLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getDp(40));
                                ansLayoutParam.setMargins(5,0,0,15);
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
                                questionTextParam.setMargins(10,0,0,0);
                                textQuestion.setLayoutParams(questionTextParam);
                                textQuestion.setGravity(Gravity.CENTER_VERTICAL);
                                textQuestion.setTextColor(getActivity().getColor(R.color.colorPrimary));
                                textQuestion.setTextSize(14);
                                textQuestion.setId(secQAUpdateInfoResDto.getSecQuesId());
                                if(curLang.equals(LANG_MM)){
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
                                errTextView.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
                                errTextView.setTextColor(getActivity().getColor(R.color.red));

                                //Answer TextBox
                                final EditText textInputLayout = new EditText(getActivity());
                                final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,0.9f);
                                lparams.setMargins(10,0,0,0);
                                textInputLayout.setLayoutParams(lparams);
                                textInputLayout.setTextSize(14);
                                textInputLayout.setHeight(40);
                                textInputLayout.setPadding(8,0,0,0);
                                textInputLayout.setTextColor(getResources().getColor(R.color.black));
                                textInputLayout.setSingleLine(true);
                                textInputLayout.setText(secQAUpdateInfoResDto.getAnswer());
                                textInputLayout.setSelection(textInputLayout.getText().length());
                                textInputLayout.setHintTextColor(getResources().getColor(R.color.grayLight));
                                textInputLayout.setBackground(getActivity().getDrawable(R.drawable.edit_text_style));
                                textInputLayout.setInputType(InputType.TYPE_CLASS_TEXT |
                                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                textInputLayout.setSelection(textInputLayout.length());
                                textInputLayout.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
                                ansList.add(secQAUpdateInfoResDto.getAnswer());
                                answerLayout.addView(textInputLayout);

                                final ImageView imgVisible = new ImageView(getActivity());
                                final LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(getDp(30),LinearLayout.LayoutParams.MATCH_PARENT);
                                imgParam.setMargins(10,0,0,0);
                                imgVisible.setLayoutParams(imgParam);
                                imgVisible.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                                imgVisible.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(textInputLayout.getInputType()==129){
                                            textInputLayout.setInputType(1);
                                            textInputLayout.setSelection(textInputLayout.getText().length());
                                            imgVisible.setImageDrawable(getActivity().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                                        } else {
                                            textInputLayout.setInputType(129);
                                            textInputLayout.setSelection(textInputLayout.getText().length());
                                            imgVisible.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                                        }
                                        textInputLayout.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
                                    }
                                });
                                answerLayout.addView(imgVisible);

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

                            btnUpdate.setEnabled(true);
                            closeDialog(loadSecQuesDialog);

                        } catch (Exception e){

                            closeDialog(loadSecQuesDialog);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }

                    } else {
                        closeDialog(loadSecQuesDialog);
                        serviceUnavailable.setVisibility(View.VISIBLE);
                    }

                } else if(response.code()==CODE_TOKEN_TIMEOUT){

                    //Refresh Token.
                    Service refreshTokenService = APIClient.getAuthUserService();
                    Call<BaseResponse<LoginAccessTokenInfo>> refreshToken = refreshTokenService.refreshToken(
                            REFRESH_TOKEN,PreferencesManager.getRefreshToken(getActivity())
                    );
                    refreshToken.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {
                            if(response.isSuccessful()){
                                BaseResponse baseResponse = response.body();
                                if(baseResponse.getStatus().equals(SUCCESS)){
                                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) baseResponse.getData();
                                    PreferencesManager.keepAccessToken(getActivity(),loginAccessTokenInfo.getAccessToken());
                                    PreferencesManager.keepRefreshToken(getActivity(),loginAccessTokenInfo.getRefreshToken());
                                    closeDialog(loadSecQuesDialog);
                                    //replaceFragment(new NavSecQAInfoUpdateTabFragment());
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
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                            closeDialog(loadSecQuesDialog);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    closeDialog(loadSecQuesDialog);
                    serviceUnavailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UpdateUserQAInfoResBean>> call, Throwable t) {
                closeDialog(loadSecQuesDialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean validation = true;
                String password = textPwd.getText().toString();

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

                    if(answer.equals(ansList.get(i))){
                        textViewAList.get(i).setVisibility(View.GONE);
                    } else if(answer.length()>ansCount){
                        textViewAList.get(i).setText(getCountErrMsg(ansCount));
                        textViewAList.get(i).setVisibility(View.VISIBLE);
                        validation = false;
                    }

                }

                if(password==null || password.equals(BLANK)){
                    txtErrPwd.setVisibility(View.VISIBLE);
                    validation=false;
                } else {
                    txtErrPwd.setVisibility(View.GONE);
                }

                if(validation) {

                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        showNetworkErrorDialog(getActivity(),getNetErrMsg());
                    } else {

                        securityAnsweredInfoList = new ArrayList<>();

                        ansSecurityQAUpdateInfoList = new ArrayList<>();
                        securityQAUpdateInfo = new SecurityQAUpdateInfo();

                        for (int j = 0; j < secQuestionIds.length; j++) {
                            securityQAUpdateInfo = new SecurityQAUpdateInfo();
                            securityQAUpdateInfo.setCustSecQuesId(custSecQuestionIds[j]);
                            securityQAUpdateInfo.setSecQuesId(secQuestionIds[j]);
                            securityQAUpdateInfo.setAnswer(answers[j]);
                            ansSecurityQAUpdateInfoList.add(securityQAUpdateInfo);
                        }

                        final UpdateUserQAInfoReqBean updateUserQAInfoReqBean = new UpdateUserQAInfoReqBean();
                        updateUserQAInfoReqBean.setPassword(password);
                        updateUserQAInfoReqBean.setCustomerId(userInformationFormBean.getCustomerId());
                        updateUserQAInfoReqBean.setSecurityQuestionAnswerReqDtoList(ansSecurityQAUpdateInfoList);


                        doUpdateSecQAService = APIClient.getUserService();
                        Call<BaseResponse> req2 = getSecurityQuestionService.doUpdateSecQAInfo(
                                PreferencesManager.getAccessToken(getActivity()),
                                updateUserQAInfoReqBean
                        );

                        getActivity().setTheme(R.style.MessageDialogTheme);
                        final ProgressDialog updSeqAnsDialog = new ProgressDialog(getActivity());
                        updSeqAnsDialog.setMessage(getString(R.string.progress_update_sq_ans));
                        updSeqAnsDialog.setCancelable(false);
                        updSeqAnsDialog.show();
                        curLang = PreferencesManager.getCurrentLanguage(getContext());

                        req2.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                if (response.isSuccessful()) {

                                    BaseResponse baseResponse = response.body();

                                    if(baseResponse.getStatus().equals(SUCCESS)){

                                        closeDialog(updSeqAnsDialog);
                                        textPwd.setText(BLANK);
                                        showSuccessDialog(getContext(),getAlertMessage(curLang));
                                        //replaceFragment(new NavSecQAInfoUpdateTabFragment());

                                    } else {

                                        closeDialog(updSeqAnsDialog);

                                        if(baseResponse.getMessageCode().equals(INCORRECT_PASSWORD)){
                                            textPwd.setText(BLANK);
                                            showWarningDialog(getActivity(),getAlertPwdErrMessage(curLang));
                                        } else {
                                            textPwd.setText(BLANK);
                                            showWarningDialog(getActivity(),getString(R.string.message_sq_update_un_success));
                                        }
                                    }

                                } else {
                                    closeDialog(updSeqAnsDialog);
                                    showErrorDialog(getActivity(),getString(R.string.service_unavailable));
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                closeDialog(updSeqAnsDialog);
                                showErrorDialog(getActivity(),getString(R.string.service_unavailable));
                            }
                        });
                    }
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textPwd.getInputType()==129){
                    textPwd.setInputType(1);
                    textPwd.setSelection(textPwd.getText().length());
                    imageView.setImageDrawable(getActivity().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                } else {
                    textPwd.setInputType(129);
                    textPwd.setSelection(textPwd.getText().length());
                    imageView.setImageDrawable(getActivity().getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                }
                textPwd.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_favorite:
                //this.languageFlag = item;
                if(item.getTitle().equals(LANG_MM)){
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeLabel(LANG_MM);

                } else if(item.getTitle().equals(LANG_EN)){
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeLabel(LANG_EN);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLabel(String language){
        txtPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_password_label, getActivity()));
        textPwd.setHint(CommonUtils.getLocaleString(new Locale(language),R.string.infoupdate_err_pwd, getActivity()));
        txtErrPwd.setText(CommonUtils.getLocaleString(new Locale(language), R.string.infoupdate_err_pwd, getActivity()));
        btnUpdate.setText(CommonUtils.getLocaleString(new Locale(language), R.string.infoupdate_update_button, getActivity()));
        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.sec_ans_update, getActivity()));

        for (int i = 0; i < textViewQList.size(); i++){

            if(language.equals(LANG_MM)){
                textViewQList.get(i).setText(secQAUpdateInfoResDtoList.get(i).getQuestionMyan());
            } else {
                textViewQList.get(i).setText(secQAUpdateInfoResDtoList.get(i).getQuestionEng());
            }
            textViewQList.get(i).setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
        }

        PreferencesManager.setCurrentLanguage(getContext(), language);
    }

    public String getAlertMessage(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.infoupdate_err_upd_success, getActivity());
    }

    public String getAlertPwdErrMessage(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.infoupdate_err_pwd_invalid, getActivity());
    }

    public String getAnsBlankErrMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.reg_sec_ans_blank, getActivity());
    }

    public String getAnsCharErrMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.secquest_ans_err, getActivity());
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.commit();
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

    private String getCountErrMsg(int count){
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.count_upd_msg, getActivity()).replace("[count]",String.valueOf(count));
    }
}
