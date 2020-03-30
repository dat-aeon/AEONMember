package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.beans.VerifyNewRegisteredUserInfoReqBean;
import mm.com.aeon.vcsaeon.beans.VerifyNewRegisteredUserInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BIRTH_DATE_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CALL_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MIN_AGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NOT_VALID;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.VALID;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isAgreementNoValid;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isNrcCodeValid;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class VerificationMemberInfoFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    View serviceUnavailable;
    Button btnVerify;
    Button btnCallAeon;
    TextView txtAgreementNo;
    TextView txtDateOfBirth;
    TextView txtNrc;
    TextView textTitle;
    TextView textVerify;
    TextView textDob;
    static Spinner spinnerDivCode;
    static AutoCompleteTextView autoCompleteTwspCode;
    Spinner spinnerType;
    static EditText textRegCode;

    Service getNrcInfoService;
    Service checkVerifyMemberService;

    static List<String> townshipCode = new ArrayList<>();

    static String stateDivCodeVal;
    static String nrcTypeVal;
    String textRegCodeVal;

    static EditText textAgreementNo;
    static EditText textDateOfBirth;

    TextView txtErrAgreementNo;
    TextView txtErrDateOfBirth;
    TextView txtErrNrc;

    VerifyNewRegisteredUserInfoReqBean verifyNewRegUserInfoReqBean;
    UserInformationFormBean userInformationFormBean;
    private String hotlinePhoneNo;

    SharedPreferences preferences;

    public static String tempAgreementNoVal;
    public static String tempDateOfBirth;
    public static int tempDivCode=0;
    public static String tempTwspCode=BLANK;
    public static int tempNrcType=0;
    public static String tempNrcCode;

    @Override
    public void onPause() {
        super.onPause();
        tempAgreementNoVal=textAgreementNo.getText().toString();
        tempDateOfBirth=textDateOfBirth.getText().toString();
        tempNrcCode=textRegCode.getText().toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //get preferences info.
        userInformationFormBean = new UserInformationFormBean();
        preferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.verify_member_info, container, false);
        // close button listener
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);


        btnVerify = view.findViewById(R.id.btn_verify);
        btnCallAeon = view.findViewById(R.id.btn_call_aeon);

        serviceUnavailable = view.findViewById(R.id.service_unavailable_verify_info);
        serviceUnavailable.setVisibility(View.GONE);

        textAgreementNo = view.findViewById(R.id.txt_agr_no);
        textAgreementNo.requestFocus();
        textDateOfBirth = view.findViewById(R.id.txt_dob);

        textRegCode = view.findViewById(R.id.txt_reg_no);
        textTitle = view.findViewById(R.id.verify_memb_title);
        textVerify = view.findViewById(R.id.verify_text);

        txtErrAgreementNo = view.findViewById(R.id.reg_err_agr_no);
        txtErrAgreementNo.setVisibility(View.GONE);
        txtErrDateOfBirth = view.findViewById(R.id.reg_err_dob);
        txtErrDateOfBirth.setVisibility(View.GONE);
        txtErrNrc = view.findViewById(R.id.reg_err_nrc);
        txtErrDateOfBirth.setVisibility(View.GONE);
        textDob = view.findViewById(R.id.text_dob);

        txtAgreementNo = view.findViewById(R.id.verify_agree_no);
        txtDateOfBirth = view.findViewById(R.id.verify_dob);
        txtNrc = view.findViewById(R.id.verify_nrc_no);

        spinnerDivCode = view.findViewById(R.id.spinner_div_code);
        autoCompleteTwspCode = view.findViewById(R.id.auto_comp_twsp_code3);
        spinnerType = view.findViewById(R.id.spinner_nrc_type);

        final String[] nrcType = getResources().getStringArray(R.array.nrc_type);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getActivity(),R.layout.nrc_spinner_item_3, nrcType);
        spinnerType.setAdapter(adapterType);
        spinnerType.setSelection(tempNrcType);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        final String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        textAgreementNo.setText(tempAgreementNoVal);
        textAgreementNo.setSelection(textAgreementNo.getText().length());
        textDateOfBirth.setText(tempDateOfBirth);
        textRegCode.setText(tempNrcCode);

        //Get NRC Information.
        getNrcInfoService = APIClient.getUserService();
        final Call<BaseResponse<List<TownshipCodeResDto>>> req = getNrcInfoService.getTownshipCode();

        getActivity().setTheme(R.style.MessageDialogTheme);
        final ProgressDialog twspCodeDialog = new ProgressDialog(getActivity());
        twspCodeDialog.setMessage(getString(R.string.progress_loading_nrc));
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
                            ArrayAdapter<String> adapterDiv = new ArrayAdapter<String>(getActivity(),R.layout.nrc_spinner_item_1, stateDivCode);
                            spinnerDivCode.setAdapter(adapterDiv);
                            spinnerDivCode.setSelection(tempDivCode);
                            stateDivCodeVal = stateDivCode[tempDivCode];
                            nrcTypeVal = nrcType[tempNrcType];

                            spinnerDivCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    stateDivCodeVal = stateDivCode[position];
                                    tempDivCode=position;

                                    for (TownshipCodeResDto townshipCodeResDto :townshipCodeResDtoList) {
                                        if(String.valueOf(townshipCodeResDto.getStateId()).equals(stateDivCodeVal)){
                                            townshipCode=townshipCodeResDto.getTownshipCodeList();
                                            break;
                                        }
                                    }

                                    autoCompleteTwspCode.setText(tempTwspCode);
                                    //autoCompleteTwspCode.setText(BLANK);

                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),R.layout.nrc_spinner_item_2, townshipCode);
                                    adapter2.setDropDownViewResource(R.layout.dialog_nrc_division);
                                    adapter2.setNotifyOnChange(true);
                                    autoCompleteTwspCode.setThreshold(1);
                                    autoCompleteTwspCode.setAdapter(adapter2);

                                    autoCompleteTwspCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            autoCompleteTwspCode.setText((String) parent.getAdapter().getItem(position));
                                            tempTwspCode=autoCompleteTwspCode.getText().toString();
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
                                    tempNrcType = position;
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {}
                            });


                            autoCompleteTwspCode.setText(tempTwspCode);

                            closeDialog(twspCodeDialog);
                            btnVerify.setEnabled(true);

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

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String townshipCodeVal = autoCompleteTwspCode.getText().toString();
                textRegCodeVal = textRegCode.getText().toString();
                String nrcNo = stateDivCodeVal + "/" + townshipCodeVal + nrcTypeVal + textRegCodeVal;
                String agreementNo = textAgreementNo.getText().toString();
                String dateOfBirth = textDateOfBirth.getText().toString();

                final String curLang2 = PreferencesManager.getCurrentLanguage(getActivity());

                //form validation
                boolean validate = true;

                if(agreementNo==null || agreementNo.equals(BLANK)){
                    txtErrAgreementNo.setVisibility(View.VISIBLE);
                    validate=false;
                } else if(!isAgreementNoValid(agreementNo)){
                    txtErrAgreementNo.setText(getString(R.string.verify_agreementno_err_format));
                    changeAgreementNoInvalid(curLang2);
                    txtErrAgreementNo.setVisibility(View.VISIBLE);
                    validate=false;
                } else {
                    txtErrAgreementNo.setVisibility(View.GONE);
                }

                if(dateOfBirth==null || dateOfBirth.equals(BLANK)){
                    txtErrDateOfBirth.setVisibility(View.VISIBLE);
                    validate=false;
                } else {
                    txtErrDateOfBirth.setVisibility(View.GONE);
                }

                if(textRegCodeVal==null || textRegCodeVal.equals(BLANK) || townshipCodeVal==null || townshipCodeVal.equals(BLANK)){
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validate=false;
                } else if(!isNrcCodeValid(textRegCodeVal)){
                    changeErrRegCode(curLang2);
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validate = false;
                } else if(!townshipCode.contains(autoCompleteTwspCode.getText().toString())){
                    autoCompleteTwspCode.setText(BLANK);
                    txtErrNrc.setVisibility(View.VISIBLE);
                    validate=false;
                } else {
                    txtErrNrc.setVisibility(View.GONE);
                }

                if(validate) {
                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        showNetworkErrorDialog(getActivity(),getNetErrMsg());
                    } else {

                        verifyNewRegUserInfoReqBean = new VerifyNewRegisteredUserInfoReqBean();
                        verifyNewRegUserInfoReqBean.setAgreementNo(agreementNo);
                        verifyNewRegUserInfoReqBean.setDateOfBirth(dateOfBirth);
                        verifyNewRegUserInfoReqBean.setNrcNo(nrcNo);
                        verifyNewRegUserInfoReqBean.setCustomerId(String.valueOf(userInformationFormBean.getCustomerId()));

                        checkVerifyMemberService = APIClient.getUserService();
                        Call<BaseResponse<VerifyNewRegisteredUserInfoResBean>> req2 = checkVerifyMemberService.checkVerifyMember(
                                PreferencesManager.getAccessToken(getActivity()),verifyNewRegUserInfoReqBean);

                        getActivity().setTheme(R.style.MessageDialogTheme);
                        final ProgressDialog verifyMemInfoDialog = new ProgressDialog(getActivity());
                        verifyMemInfoDialog.setMessage(getString(R.string.progress_check_to_verify));
                        verifyMemInfoDialog.setCancelable(false);
                        verifyMemInfoDialog.show();

                        req2.enqueue(new Callback<BaseResponse<VerifyNewRegisteredUserInfoResBean>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<VerifyNewRegisteredUserInfoResBean>> call, Response<BaseResponse<VerifyNewRegisteredUserInfoResBean>> response) {

                                if (response.isSuccessful()) {

                                    BaseResponse baseResponse = response.body();

                                    if(baseResponse.getStatus().equals(SUCCESS)){

                                        closeDialog(verifyMemInfoDialog);

                                        VerifyNewRegisteredUserInfoResBean verifyNewRegUserInfoResBean =
                                                (VerifyNewRegisteredUserInfoResBean) baseResponse.getData();

                                        final String verifyStatusCode = verifyNewRegUserInfoResBean.getVerifyStatus();

                                        switch (verifyStatusCode){
                                            case VALID :
                                                VerifySecurityQuestionFragment.tempAnswers=null;
                                                PreferencesManager.addEntryToPreferences(preferences, "customer_no", verifyNewRegUserInfoResBean.getCustomerNo());
                                                /*replaceFragment(new VerifySecurityQuestionFragment());*/
                                                replaceFragment(new VerifyPhotoUploadFragment());
                                                break;
                                            case NOT_VALID :
                                                showWarningDialog(getActivity(),getVerifiedFailedMsg(curLang2));
                                                break;
                                                default:
                                                    showWarningDialog(getActivity(),getVerifiedFailedMsg(curLang2));
                                                    break;
                                        }

                                    } else {
                                        closeDialog(verifyMemInfoDialog);
                                        showErrorDialog(getActivity(),getString(R.string.message_verify_failed));
                                    }

                                } else {
                                    closeDialog(verifyMemInfoDialog);
                                    showErrorDialog(getActivity(),getString(R.string.message_verify_failed));
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse<VerifyNewRegisteredUserInfoResBean>> call, Throwable t) {
                                closeDialog(verifyMemInfoDialog);
                                showErrorDialog(getActivity(),getString(R.string.service_unavailable));
                            }
                        });
                    }
                }
            }
        });

        textDateOfBirth.setOnClickListener(new View.OnClickListener() {
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
                        textDateOfBirth.setText(sdf.format(myCalendar.getTime()));
                    }
                });
                myCalendar.add(Calendar.YEAR, -MIN_AGE);
                dialog.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                dialog.setMaxDate(myCalendar.getTimeInMillis());
                dialog.show(getChildFragmentManager(), "TAG");
            }
        });

        btnCallAeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Action Call
                hotlinePhoneNo = userInformationFormBean.getHotlinePhone();

                int permission = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    makeCallRequest();
                } else {
                    if(hotlinePhoneNo==null || hotlinePhoneNo.equals(BLANK)){
                        //Toast.makeText(getActivity(),getString(R.string.message_call_not_available), Toast.LENGTH_SHORT).show();
                        displayMessage(getActivity(),getString(R.string.message_call_not_available));
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX+hotlinePhoneNo));
                        startActivity(callIntent);
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

    public void changeLabel(String language){

        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_title, getActivity()));

        txtErrAgreementNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_agreementno_err, getActivity()));
        txtErrDateOfBirth.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_dob_err, getActivity()));
        txtErrNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_nrc_err, getActivity()));

        btnVerify.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_verify_button, getActivity()));
        btnCallAeon.setText(CommonUtils.getLocaleString(new Locale(language), R.string.aboutus_all_now_button, getActivity()));

        textVerify.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_callnow_notice, getActivity()));

        txtAgreementNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_agreementno_label, getActivity()));
        txtDateOfBirth.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_dob_label, getActivity()));
        txtNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_nrc_label, getActivity()));

        textAgreementNo.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.verify_agreementno_holder, getActivity()));
        textDateOfBirth.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.verify_dob_holder, getActivity()));
        textRegCode.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.register_reg_code, getActivity()));

        textDob.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_age_limit, getActivity()));

    }

    public void changeErrRegCode(String language){
        txtErrNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_nrc_format_err, getActivity()));
    }

    public String getVerifiedFailedMsg(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.verify_err_invalid, getActivity());
    }

    public void changeAgreementNoInvalid(String language){
        txtErrAgreementNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.verify_agreementno_err_format, getActivity()));
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    protected void makeCallRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                CALL_REQUEST_CODE);
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new NavMembershipFragment());
    }
}
