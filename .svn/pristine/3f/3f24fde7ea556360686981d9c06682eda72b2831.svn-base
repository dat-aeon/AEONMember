package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.canner.stepsview.StepsView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.ApplicationFormErrMesgBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.EmergencyContactFormBean;
import mm.com.aeon.vcsaeon.beans.GuarantorFormBean;
import mm.com.aeon.vcsaeon.beans.OccupationDataFormBean;
import mm.com.aeon.vcsaeon.beans.TownshipListBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.APPLICATION_PLOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CHANNEL_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_ID;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DA_TITLES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MYANMAR;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.dateToString;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isEmptyOrNull;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class SmallLoanApplicationDataFragment extends PagerRootFragment implements LanguageChangeListener {

    private TextView labelName;
    private TextView labelDob;
    private TextView labelNrc;
    private TextView labelFatherName;
    private TextView labelNationality;
    private TextView labelGender;
    private TextView labelMaritalStatus;
    private TextView labelResidentType;
    private TextView labelLivingWith;
    private TextView labelStayTime;
    private TextView labelMobileNo;
    private TextView labelResidentMobileNo;
    private TextView labelOtherPhone;
    private TextView labelEmail;
    private TextView labelStayYear;
    private TextView labelStayMonth;
    private TextView lblEducation;

    private TextView errName;
    private TextView errDob;
    private TextView errNrc;
    private TextView errFatherName;
    private TextView errResidentType;
    private TextView errLivingWith;
    private TextView errMobileNo;
    private TextView errResidenceTelNo;
    private TextView errOtherPhoneNo;
    private TextView errEmail;
    private TextView errNationality;
    private TextView errStayTime;

    private Integer errNameLocale;
    private Integer errDobLocale;
    private Integer errNrcLocale;
    private Integer errFatherNameLocale;
    private Integer errCurAddressLocale;
    private Integer errPerAddressLocale;
    private Integer errResidentTypeLocale;
    private Integer errLivingWithLocale;
    private Integer errMobileNoLocale;
    private Integer errResidenceTelNoLocale;
    private Integer errOtherPhoneNoLocale;
    private Integer errEmailLocale;
    private Integer errNationalityLocale;
    private Integer errStayTimeLocale;
    private Integer errAddressStreetLocale;
    private Integer errAddressTownshipLocale;
    private Integer errAddressCityLocale;
    private Integer errAddressQuarterLocale;

    private EditText fatherName;
    private EditText radio_Nation_detail;
    private EditText residentTypeDes;
    private EditText livingWithDes;
    private EditText residentMobileNo;
    private EditText otherPhoneNo;
    private EditText email;

    private Button btnAppNext;
    private Button btnAppSave;

    private LinearLayout nextOccupation;
    private TextView occupationTitle;
    private TextView applicationTitle;

    private static String stateDivCodeVal;
    private static String nrcTypeVal;
   /* private static List<String> addressTownshipList = new ArrayList<>();
    private static List<String> addressCityList = new ArrayList<>();*/

    private static String appName;
    private static String appDob;
    private static String appNrc;
    private static String nrc_number;
    private static String appFatherName;
    private static String appNationalityDes;
    private static String appResidentTypeDes;
    private static String appLivingWithDes;
    private static String appMobilePhone;
    private static String appResidentPhone;
    private static String appResidentOtherPhone;
    private static String appEmail;
    private static String townshipCodeVal;

    private static int appResidentType;
    private static int appLivingWith;
    private static int appStayYear;
    private static int appStayMonth;
    private static int appEducation;

    private static String curLang;
    private static int customerId;
    private static int appNationality = 1;
    private static int appGender = 1;
    private static int appMaritalStatus = 1;

    ScrollView scrollView;

    private Spinner spnResidentType;
    private Spinner spnLivingWith;
    private Spinner applicantStayYear;
    private Spinner applicantStayMonth;
    private Spinner applicantEducation;

    private RadioGroup nationality;
    private RadioGroup gender;
    private RadioGroup mariage;
    private RadioButton myanNational;
    private RadioButton otherNational;
    private RadioButton appGenderMale;
    private RadioButton appGenderFemale;
    private RadioButton appSingle;
    private RadioButton appMarried;

    private TextView txtname;
    private TextView txtdob;
    private TextView applicantNrcFilled;
    private TextView txtphoneNo;

    private View view;
    private View serviceUnavailable;
    private static SharedPreferences validationPreferences;
    UserInformationFormBean userInformationFormBean;
    private static int selectedPosition;

    private boolean isInit;
    private String fatherNameCheck;
    private String curAddressCheck;
    private String perAddressCheck;
    private String residentMobileNoCheck;
    private String emailCheck;
    private String residentDetailCheck;
    private String livingWithDetailCheck;
    private String nationalityDetailCheck;

    private TextView errAddressStreet;
    private TextView errAddressTownship;
    private TextView errAddressCity;
    private TextView errAddressQuarter;

    private TextView lblAddressTitleCurrent;
    private TextView lblCurApartment;
    private TextView lblCurRoomNo;
    private TextView lblCurFloorNo;
    private TextView lblCurStreet;
    private TextView lblCurQuarter;
    private TextView lblCurTownship;
    private TextView lblCurCity;

    private TextView lblAddressTitlePermanent;
    private TextView lblPerApartment;
    private TextView lblPerRoomNo;
    private TextView lblPerFloorNo;
    private TextView lblPerStreet;
    private TextView lblPerQuarter;
    private TextView lblPerTownship;
    private TextView lblPerCity;

    private EditText curApartmentNo;
    private EditText curRoomNo;
    private EditText curFloorNo;
    private EditText curStreet;
    private EditText curQuarter;

    private EditText perApartmentNo;
    private EditText perRoomNo;
    private EditText perFloorNo;
    private EditText perStreet;
    private EditText perQuarter;

    private String curAddrApartmentNo;
    private String curAddrRoomNo;
    private String curAddrFloorNo;
    private String curAddrStreet;
    private String curAddrQuarter;

    private String currentTownshipCheck;
    private String currentCityCheck;
    private String currentStreetCheck;
    private String currentQuarterCheck;

    private String perAddrApartmentNo;
    private String perAddrRoomNo;
    private String perAddrFloorNo;
    private String perAddrStreet;
    private String perAddrQuarter;

    AutoCompleteTextView autoCurrentTownship;
    AutoCompleteTextView autoCurrentCity;

    AutoCompleteTextView autoPermanentTownship;
    AutoCompleteTextView autoPermanentCity;

    private int curAddrTownship;
    private int curAddrCity;
    private int perAddrTownship;
    private int perAddrCity;

    private String currentTownshipValidate;
    private String currentCityValidate;
    private String permanentTownshipValidate;
    private String permanentCityValidate;

    private String curCityName;
    private String perCityName;
    private List<String> selectCityList;
    private List<String> selectCityListPermanent;
    private List<String> selectedCurrentTownshipList;
    private List<Integer> cityId;
    private List<Integer> townshipId;
    private int townshipid;
    private int saveCityid;

    private List<String> perTownshipList;
    private List<Integer> perTownshipId;

    private List<TownshipListBean> townshipBeanList = new ArrayList<>();
    private List<CityTownshipResBean> cityTownshipList;

    private final int CURRENT = 1;
    private final int PERMANENT = 2;

    private MenuItem languageFlag;
    SharedPreferences sharedPreferences;

    private StateProgressBar appStepView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_small_loan_application_data, container, false);
        isInit = true;
        setHasOptionsMenu(true);

        String[] pages = {"Application\nData", "Occupation\nData","Emergency\nContact", "Guarantor\nData", "Loan\nConfirmation"};
        appStepView = view.findViewById(R.id.app_stepped_bar);
        appStepView.setStateDescriptionData(pages);
        appStepView.setOnStateItemClickListener(new OnStateItemClickListener() {
            @Override
            public void onStateItemClick(StateProgressBar stateProgressBar, StateItem stateItem, int stateNumber, boolean isCurrentState) {
                if(stateNumber == 1){
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(0, true);
                }else if(stateNumber == 2){
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(1, true);
                }else if(stateNumber == 3){
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(2, true);
                }else if(stateNumber == 4){
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(3, true);
                }else if(stateNumber == 5){
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(4, true);
                }
            }
        });

        if(!MainMenuActivityDrawer.isOccupationLanguageFlag){
            ((MainMenuActivityDrawer)getActivity()).setLanguageListener(SmallLoanApplicationDataFragment.this);
        }

        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);
        curLang = PreferencesManager.getCurrentLanguage(getActivity());

        // Education label
        lblEducation = view.findViewById(R.id.applicant_text_education);

        // Current Address label
        lblAddressTitleCurrent = view.findViewById(R.id.applicant_current_address);
        lblCurApartment = view.findViewById(R.id.applicant_apartment_no);
        lblCurRoomNo = view.findViewById(R.id.applicant_room_no);
        lblCurFloorNo = view.findViewById(R.id.applicant_floor_no);
        lblCurStreet = view.findViewById(R.id.applicant_street);
        lblCurQuarter = view.findViewById(R.id.applicant_quarter);
        lblCurTownship = view.findViewById(R.id.applicant_township);
        lblCurCity = view.findViewById(R.id.applicant_city);

        // Permanent Address label
        lblAddressTitlePermanent = view.findViewById(R.id.application_permanent_address);
        lblPerApartment = view.findViewById(R.id.app_txt_apartment_no);
        lblPerRoomNo = view.findViewById(R.id.app_txt_room_no);
        lblPerFloorNo = view.findViewById(R.id.app_txt_floor_no);
        lblPerStreet = view.findViewById(R.id.app_txt_street);
        lblPerQuarter = view.findViewById(R.id.app_txt_quarter);
        lblPerTownship = view.findViewById(R.id.app_txt_township);
        lblPerCity = view.findViewById(R.id.app_txt_city);

        // Current Address EditText
        curApartmentNo = view.findViewById(R.id.app_cur_appartment_no);
        curRoomNo = view.findViewById(R.id.app_cur_room_no);
        curFloorNo = view.findViewById(R.id.app_cur_floor_no);
        curStreet = view.findViewById(R.id.app_cur_street);
        curQuarter = view.findViewById(R.id.app_cur_quarter);

        curApartmentNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        curRoomNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        curFloorNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        curStreet.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        curQuarter.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});


        // Permanent Address EditText
        perApartmentNo = view.findViewById(R.id.app_per_apartment_no);
        perRoomNo = view.findViewById(R.id.app_per_room_no);
        perFloorNo = view.findViewById(R.id.app_per_floor_no);
        perStreet = view.findViewById(R.id.app_per_street);
        perQuarter = view.findViewById(R.id.app_per_quarter);

        perApartmentNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        perRoomNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        perFloorNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        perStreet.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        perQuarter.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});

        // Current/Permanent Address Spinner
        autoCurrentTownship = view.findViewById(R.id.app_cur_township);
        autoCurrentCity = view.findViewById(R.id.app_cur_city);
        autoPermanentTownship = view.findViewById(R.id.app_per_township);
        autoPermanentCity = view.findViewById(R.id.app_per_city);

        labelName = view.findViewById(R.id.applicant_text_name);
        labelDob = view.findViewById(R.id.applicant_text_dob);
        labelNrc = view.findViewById(R.id.applicant_text_nrc);
        labelFatherName = view.findViewById(R.id.applicant_text_father);
        labelNationality = view.findViewById(R.id.applicant_nationality);
        labelGender = view.findViewById(R.id.applicant_gender);
        labelMaritalStatus = view.findViewById(R.id.applicant_marital_status);
        labelResidentType = view.findViewById(R.id.text_resident_type);
        labelLivingWith = view.findViewById(R.id.text_living_with);
        labelStayTime = view.findViewById(R.id.applicant_stay);
        labelMobileNo = view.findViewById(R.id.applicant_mobile_no);
        labelResidentMobileNo = view.findViewById(R.id.applicant_resident_mobile);
        labelOtherPhone = view.findViewById(R.id.applicant_other_mobile);
        labelEmail = view.findViewById(R.id.applicant_email);
        labelStayYear = view.findViewById(R.id.lbl_app_stay_year);
        labelStayMonth = view.findViewById(R.id.lbl_app_stay_month);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);
        customerId = userInformationFormBean.getCustomerId();

        validationPreferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        PreferencesManager.addEntryToPreferences(validationPreferences, CUSTOMER_ID, String.valueOf(customerId));
        sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());

        StepsView stepsView = view.findViewById(R.id.stepsView_1);
        stepsView.setLabels(DA_TITLES)
                .setBarColorIndicator(getContext().getColor(R.color.gray))
                .setProgressColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setLabelColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setCompletedPosition(0)
                .drawView();

        serviceUnavailable = view.findViewById(R.id.service_unavailable_apply_doc);
        serviceUnavailable.setVisibility(View.GONE);

        spnResidentType = view.findViewById(R.id.spinner_resident_type);
        spnLivingWith = view.findViewById(R.id.spinner_living_with);
        applicantStayYear = view.findViewById(R.id.app_spinner_stay_year);
        applicantStayMonth = view.findViewById(R.id.app_spinner_stay_month);
        applicantEducation = view.findViewById(R.id.app_spinner_education);

        // Current Address Error Messages
        errAddressStreet = view.findViewById(R.id.applicant_err_street);
        errAddressStreetLocale = R.string.da_mesg_blank;

        errAddressTownship = view.findViewById(R.id.applicant_err_township);
        errAddressTownshipLocale = R.string.da_mesg_blank;

        errAddressCity = view.findViewById(R.id.applicant_err_city);
        errAddressCityLocale = R.string.da_mesg_blank;

        errAddressQuarter = view.findViewById(R.id.applicant_err_quarter);
        errAddressQuarterLocale = R.string.da_mesg_blank;

        errName = view.findViewById(R.id.applicant_err_name);
        errNameLocale = R.string.da_mesg_blank;

        errDob = view.findViewById(R.id.reg_err_dob);
        errDobLocale = R.string.da_mesg_blank;

        errNrc = view.findViewById(R.id.applicant_err_nrc);
        errNrcLocale = R.string.da_mesg_blank;

        errFatherName = view.findViewById(R.id.applicant_err_father);
        errFatherNameLocale = R.string.da_mesg_blank;

        errResidentType = view.findViewById(R.id.applicant_err_residenttype);
        errResidentTypeLocale = R.string.da_mesg_blank;

        errMobileNo = view.findViewById(R.id.applicant_err_mobileno);
        errMobileNoLocale = R.string.da_mesg_blank;

        errResidenceTelNo = view.findViewById(R.id.applicant_err_residentno);
        errResidenceTelNoLocale = R.string.da_mesg_blank;

        errOtherPhoneNo = view.findViewById(R.id.applicant_err_other_phone);
        errOtherPhoneNoLocale = R.string.da_mesg_blank;

        errEmail = view.findViewById(R.id.applicant_err_email);
        errEmailLocale = R.string.da_mesg_blank;

        errNationality = view.findViewById(R.id.applicant_err_nationality);
        errNationalityLocale = R.string.da_mesg_blank;

        errLivingWith = view.findViewById(R.id.applicant_err_livingwith);
        errLivingWithLocale = R.string.da_mesg_blank;

        errStayTime = view.findViewById(R.id.applicant_err_stay);
        errStayTimeLocale = R.string.da_mesg_blank;

        /*name = view.findViewById(R.id.applicant_input_name);*/
        txtname = view.findViewById(R.id.applicant_input_name);
        /*applicantDob = view.findViewById(R.id.applicant_input_dob);*/
        txtdob = view.findViewById(R.id.applicant_input_dob);

        fatherName = view.findViewById(R.id.applicant_input_father);
        fatherName.requestFocus();
        radio_Nation_detail = view.findViewById(R.id.applicant_nationality_description);
        livingWithDes = view.findViewById(R.id.living_with_description);
        residentTypeDes = view.findViewById(R.id.resident_description);
        /*mobileNo = view.findViewById(R.id.applicant_input_mobile);*/
        txtphoneNo = view.findViewById(R.id.applicant_input_mobile);
        residentMobileNo = view.findViewById(R.id.applicant_resident_tel);
        otherPhoneNo = view.findViewById(R.id.applicant_input_other);
        email = view.findViewById(R.id.applicant_input_email);
        applicantNrcFilled = view.findViewById(R.id.applicant_nrc_number);

        fatherName.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});
        radio_Nation_detail.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});
        residentTypeDes.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});
        livingWithDes.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});

        myanNational = view.findViewById(R.id.app_radioMyanmar);
        otherNational = view.findViewById(R.id.app_radioOther);

        appGenderMale = view.findViewById(R.id.app_radioMale);
        appGenderFemale = view.findViewById(R.id.app_radioFemale);

        appSingle = view.findViewById(R.id.app_radioSingle);
        appMarried = view.findViewById(R.id.app_radioMarried);

        nationality = view.findViewById(R.id.radioNationality);
        gender = view.findViewById(R.id.radioGender);
        mariage = view.findViewById(R.id.radioMariage);


        currentTownshipCheck = autoCurrentTownship.getText().toString();
        if (!isEmptyOrNull(currentTownshipCheck)) {
            autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentCityCheck = autoCurrentCity.getText().toString();
        if (!isEmptyOrNull(currentCityCheck)) {
            autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        cityTownshipList = PreferencesManager.getCityListInfo(getActivity());
        setUpCityList(cityTownshipList);
        curCityName = selectCityList.get(0);
        setUpCurrentTownshipList(cityTownshipList, curCityName);

        // Current City
        ArrayAdapter<String> curAddressCity = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, selectCityList);
        curAddressCity.setDropDownViewResource(R.layout.dialog_nrc_division);
        curAddressCity.setNotifyOnChange(true);
        autoCurrentCity.setThreshold(1);
        autoCurrentCity.setAdapter(curAddressCity);

        autoCurrentCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCurrentCity.setText((String) parent.getAdapter().getItem(position));
                curAddrCity = cityId.get(position);
                curCityName = (String) parent.getAdapter().getItem(position);
                setUpCurrentTownshipList(cityTownshipList, curCityName);
                autoCurrentTownship.setText(BLANK);
                autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));

                // Current Township
                ArrayAdapter<String> curAddressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, selectedCurrentTownshipList);
                curAddressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                curAddressTownship.setNotifyOnChange(true);
                autoCurrentTownship.setThreshold(1);
                autoCurrentTownship.setAdapter(curAddressTownship);

            }
        });

        autoCurrentCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    autoCurrentCity.showDropDown();
                    autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!selectCityList.contains(autoCurrentCity.getText().toString())) {
                        autoCurrentCity.setText(BLANK);
                        autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        autoCurrentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCurrentCity.showDropDown();
            }
        });

        autoCurrentTownship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCurrentTownship.setText((String) parent.getAdapter().getItem(position));
                curAddrTownship = townshipId.get(position);
            }
        });
        autoCurrentTownship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoCurrentTownship.showDropDown();
                    autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!selectedCurrentTownshipList.contains(autoCurrentTownship.getText().toString())) {
                        autoCurrentTownship.setText(BLANK);
                        autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        autoCurrentTownship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCurrentTownship.showDropDown();
            }
        });

        perCityName = selectCityList.get(0);
        setUpPermanentTownshipList(cityTownshipList, perCityName);

        // Company City
        ArrayAdapter<String> perAddressCity = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, selectCityList);
        perAddressCity.setDropDownViewResource(R.layout.dialog_nrc_division);
        perAddressCity.setNotifyOnChange(true);
        autoPermanentCity.setThreshold(1);
        autoPermanentCity.setAdapter(perAddressCity);

        autoPermanentCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoPermanentCity.setText((String) parent.getAdapter().getItem(position));
                perAddrCity = cityId.get(position);
                perCityName = (String) parent.getAdapter().getItem(position);
                setUpPermanentTownshipList(cityTownshipList, perCityName);
                autoPermanentTownship.setText(BLANK);

                // Company Township
                final ArrayAdapter<String> guarCompanyTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, perTownshipList);
                guarCompanyTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                guarCompanyTownship.setNotifyOnChange(true);
                autoPermanentTownship.setThreshold(1);
                autoPermanentTownship.setAdapter(guarCompanyTownship);

            }
        });

        autoPermanentTownship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoPermanentTownship.setText((String) parent.getAdapter().getItem(position));
                perAddrTownship = perTownshipId.get(position);
            }
        });

        autoPermanentCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!selectCityList.contains(autoPermanentCity.getText().toString())) {
                        autoPermanentCity.setText(BLANK);
                    }
                } else {
                    autoPermanentCity.showDropDown();
                }
            }
        });

        autoPermanentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoPermanentCity.showDropDown();
            }
        });

        autoPermanentTownship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoPermanentTownship.showDropDown();
                    autoPermanentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!perTownshipList.contains(autoPermanentTownship.getText().toString())) {
                        autoPermanentTownship.setText(BLANK);
                    }
                }
            }
        });

        autoPermanentTownship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoPermanentTownship.showDropDown();
            }
        });


        final String[] residentType = getResources().getStringArray(R.array.applicant_resident_type);
        ArrayAdapter<String> adapterResident = new ArrayAdapter<String>(getActivity(), R.layout.relation_spinner, residentType);
        spnResidentType.setAdapter(adapterResident);

        final String[] livingWith = getResources().getStringArray(R.array.applicant_living_with);
        ArrayAdapter<String> adapterLiving = new ArrayAdapter<String>(getActivity(), R.layout.relation_spinner, livingWith);
        spnLivingWith.setAdapter(adapterLiving);

        final String[] yearOfStay = getResources().getStringArray(R.array.year_array);
        ArrayAdapter<String> adapterStayYear = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, yearOfStay);
        applicantStayYear.setAdapter(adapterStayYear);

        final String[] yearOfMonth = getResources().getStringArray(R.array.month_array);
        ArrayAdapter<String> adapterStayMonth = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, yearOfMonth);
        applicantStayMonth.setAdapter(adapterStayMonth);

        final String[] education = getResources().getStringArray(R.array.applicant_education);
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<String>(getActivity(), R.layout.relation_spinner, education);
        applicantEducation.setAdapter(adapterEducation);

        applicantEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                appEducation = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        applicantStayYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                appStayYear = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        applicantStayMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                appStayMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        appNrc = userInformationFormBean.getNrcNo();
        setUpNrcInfo(appNrc);
        nationality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Myanmar")) {
                    appNationality = CommonConstants.MYANMAR;
                } else {
                    appNationality = CommonConstants.OTHER;
                }
                displayNationalityDesc();
            }
        });

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Male")) {
                    appGender = CommonConstants.MALE;
                } else {
                    appGender = CommonConstants.FEMALE;
                }
            }
        });

        mariage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Single")) {
                    appMaritalStatus = CommonConstants.SINGLE;
                } else {
                    appMaritalStatus = CommonConstants.MARRIED;
                }
            }
        });

        spnLivingWith.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appLivingWith = position + 1;
                displayLivingWithDesc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnResidentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                appResidentType = position + 1;
                displayResidentDesc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        scrollView = view.findViewById(R.id.application_data_scroll);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        btnAppNext = view.findViewById(R.id.btn_appdata_next);
        btnAppNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDataOnPageChanged();
                viewPager.setCurrentItem(1, true);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.e("pager scroll ", "Application Data "+ selectedPosition );

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                if(selectedPosition == 0){
                    ((MainMenuActivityDrawer)getActivity()).setLanguageListener(SmallLoanApplicationDataFragment.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (selectedPosition == 1) {
                    setUpDataOnPageChanged();

                } else if (selectedPosition == 0) {
                    curLang = PreferencesManager.getCurrentLanguage(getContext());
                    changeLabel(curLang);

                    if (MainMenuActivityDrawer.isSubmitclickAppData) {
                        MainMenuActivityDrawer.isSubmitclickAppData = false;
                        showValidationMsg(curLang);
                    }
                    //Log.e("pager change ", "Application Data");
                }
            }

        });

        occupationTitle = view.findViewById(R.id.da_occu_title);
        applicationTitle = view.findViewById(R.id.da_app_data_title);

        /*nextOccupation = view.findViewById(R.id.go_to_occupation);
        nextOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDataOnPageChanged();
                viewPager.setCurrentItem(1, true);
            }
        });*/

        btnAppSave = view.findViewById(R.id.btn_appdata_save);
        btnAppSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveApplicationToDatabase();
            }
        });
        curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        fatherNameCheck = fatherName.getText().toString();
        if (!isEmptyOrNull(fatherNameCheck)) {
            fatherName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        residentMobileNoCheck = residentMobileNo.getText().toString();

        emailCheck = email.getText().toString();
        /*if(!isEmptyOrNull(emailCheck)){
            email.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }*/

        currentStreetCheck = curStreet.getText().toString();
        if (!isEmptyOrNull(currentStreetCheck)) {
            curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        currentTownshipCheck = autoCurrentTownship.getText().toString();
        if (!isEmptyOrNull(currentTownshipCheck)) {
            autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentCityCheck = autoCurrentCity.getText().toString();
        if (!isEmptyOrNull(currentCityCheck)) {
            autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        residentDetailCheck = residentTypeDes.getText().toString();
        if (!isEmptyOrNull(residentDetailCheck)) {
            residentTypeDes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        livingWithDetailCheck = livingWithDes.getText().toString();
        if (!isEmptyOrNull(livingWithDetailCheck)) {
            livingWithDes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        nationalityDetailCheck = radio_Nation_detail.getText().toString();
        if (!isEmptyOrNull(nationalityDetailCheck)) {
            radio_Nation_detail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        residentTypeDes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    residentTypeDes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    residentDetailCheck = residentTypeDes.getText().toString();
                    if (isEmptyOrNull(residentDetailCheck)) {
                        residentTypeDes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        livingWithDes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    livingWithDes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    livingWithDetailCheck = livingWithDes.getText().toString();
                    if (isEmptyOrNull(livingWithDetailCheck)) {
                        livingWithDes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        radio_Nation_detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    radio_Nation_detail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    nationalityDetailCheck = radio_Nation_detail.getText().toString();
                    if (isEmptyOrNull(nationalityDetailCheck)) {
                        radio_Nation_detail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        fatherName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    fatherName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    fatherNameCheck = fatherName.getText().toString();
                    if (isEmptyOrNull(fatherNameCheck)) {
                        fatherName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        curStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    currentStreetCheck = curStreet.getText().toString();
                    if (isEmptyOrNull(currentStreetCheck)) {
                        curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        currentQuarterCheck = curQuarter.getText().toString();
        if (!isEmptyOrNull(currentQuarterCheck)) {
            curQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        curQuarter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    curQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    currentQuarterCheck = curQuarter.getText().toString();
                    if (isEmptyOrNull(currentQuarterCheck)) {
                        curQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        /*autoCurrentCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus){
                    autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                }else{
                    currentCityCheck = autoCurrentCity.getText().toString();
                    if(isEmptyOrNull(currentCityCheck)){
                        autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });*/
       /* autoCurrentTownship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus){
                    autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                }else{
                    currentTownshipCheck = autoCurrentTownship.getText().toString();
                    if(isEmptyOrNull(currentTownshipCheck)){
                        autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });*/
        /*email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus){
                    email.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                }else{
                    emailCheck = email.getText().toString();
                    if(isEmptyOrNull(emailCheck)){
                        email.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });*/

        setupCustomerData();
        replaceLastRegisterInfo();
        return view;
    }

    private void setupCustomerData() {
        txtname.setText(userInformationFormBean.getName());
        txtdob.setText(CommonUtils.dateToString(userInformationFormBean.getDateOfBirth()));
        txtphoneNo.setText(userInformationFormBean.getPhoneNo());
        appNrc = userInformationFormBean.getNrcNo();
        setUpNrcInfo(appNrc);
    }

    private void replaceLastRegisterInfo() {

        if (PreferencesManager.isDaftSavedErrExisted(getActivity())) {
            ApplicationFormErrMesgBean savedInformation
                    = PreferencesManager.getErrMesgInfo(getContext());

            errNameLocale = savedInformation.getAppNameLocale();
            errDobLocale = savedInformation.getAppDobLocale();
            errMobileNoLocale = savedInformation.getAppMobileNoLocale();
            errStayTimeLocale = savedInformation.getAppStayTimeLocale();

            errNrcLocale = savedInformation.getAppNrcLocale();
            errFatherNameLocale = savedInformation.getAppFatherNameLocale();
            errCurAddressLocale = savedInformation.getAppCurAddressLocale();
            errPerAddressLocale = savedInformation.getAppPerAddressLocale();
            errResidentTypeLocale = savedInformation.getAppResidentTypeLocale();
            errLivingWithLocale = savedInformation.getAppLivingWithLocale();
            errResidenceTelNoLocale = savedInformation.getAppResidentMobileNoLocale();
            errOtherPhoneNoLocale = savedInformation.getAppOtherPhoneNoLocale();
            errEmailLocale = savedInformation.getAppEmailLocale();
            errNationalityLocale = savedInformation.getAppNationalityLocale();

            errAddressTownshipLocale = savedInformation.getAppAddressTownshipLocale();
            errAddressCityLocale = savedInformation.getAppAddressCityLoacle();
            errAddressStreetLocale = savedInformation.getAppAddressStreetLocale();
            errAddressQuarterLocale = savedInformation.getAppAddressQuarterLoacle();

            if (errAddressTownshipLocale != R.string.da_mesg_blank) {
                errAddressTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), errAddressTownshipLocale, getContext()));
                errAddressTownship.setVisibility(View.VISIBLE);
            }

            if (errAddressCityLocale != R.string.da_mesg_blank) {
                errAddressCity.setText(CommonUtils.getLocaleString(new Locale(curLang), errAddressCityLocale, getContext()));
                errAddressCity.setVisibility(View.VISIBLE);
            }

            if (errAddressStreetLocale != R.string.da_mesg_blank) {
                errAddressStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), errAddressStreetLocale, getContext()));
                errAddressStreet.setVisibility(View.VISIBLE);
            }

            if (errAddressQuarterLocale != R.string.da_mesg_blank) {
                errAddressQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), errAddressQuarterLocale, getContext()));
                errAddressQuarter.setVisibility(View.VISIBLE);
            }


            if (errNameLocale != R.string.da_mesg_blank) {
                errName.setText(CommonUtils.getLocaleString(new Locale(curLang), errNameLocale, getContext()));
                errName.setVisibility(View.VISIBLE);
            }

            if (errDobLocale != R.string.da_mesg_blank) {
                errDob.setText(CommonUtils.getLocaleString(new Locale(curLang), errDobLocale, getContext()));
                errDob.setVisibility(View.VISIBLE);
            }

            if (errStayTimeLocale != R.string.da_mesg_blank) {
                errStayTime.setText(CommonUtils.getLocaleString(new Locale(curLang), errStayTimeLocale, getContext()));
                errStayTime.setVisibility(View.VISIBLE);
            }

            if (errNrcLocale != R.string.da_mesg_blank) {
                errNrc.setText(CommonUtils.getLocaleString(new Locale(curLang), errNrcLocale, getContext()));
                errNrc.setVisibility(View.VISIBLE);
            }

            if (errFatherNameLocale != R.string.da_mesg_blank) {
                errFatherName.setText(CommonUtils.getLocaleString(new Locale(curLang), errFatherNameLocale, getContext()));
                errFatherName.setVisibility(View.VISIBLE);
            }

            if (errResidentTypeLocale != R.string.da_mesg_blank) {
                errResidentType.setText(CommonUtils.getLocaleString(new Locale(curLang), errResidentTypeLocale, getContext()));
                errResidentType.setVisibility(View.VISIBLE);
            }

            if (errLivingWithLocale != R.string.da_mesg_blank) {
                errLivingWith.setText(CommonUtils.getLocaleString(new Locale(curLang), errLivingWithLocale, getContext()));
                errLivingWith.setVisibility(View.VISIBLE);
            }

            if (errOtherPhoneNoLocale != R.string.da_mesg_blank) {
                errOtherPhoneNo.setText(CommonUtils.getLocaleString(new Locale(curLang), errOtherPhoneNoLocale, getContext()));
                errOtherPhoneNo.setVisibility(View.VISIBLE);
            }

            if (errEmailLocale != R.string.da_mesg_blank) {
                errEmail.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmailLocale, getContext()));
                errEmail.setVisibility(View.VISIBLE);
            }

            if (errNationalityLocale != R.string.da_mesg_blank) {
                errNationality.setText(CommonUtils.getLocaleString(new Locale(curLang), errNationalityLocale, getContext()));
                errNationality.setVisibility(View.VISIBLE);
            }
        }

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            ApplicationRegisterSaveReqBean savedInformation
                    = PreferencesManager.getDaftSavedInfo(getActivity());
            try {
                txtname.setText(userInformationFormBean.getName());
                final String dob = dateToString(userInformationFormBean.getDateOfBirth());
                txtdob.setText(dob);
                txtphoneNo.setText(userInformationFormBean.getPhoneNo());

                fatherName.setText(savedInformation.getFatherName());
                applicantEducation.setSelection(savedInformation.getHighestEducationTypeId()-1);
                radio_Nation_detail.setText(savedInformation.getNationalityOther());
                residentTypeDes.setText(savedInformation.getTypeOfResidenceOther());
                livingWithDes.setText(savedInformation.getLivingWithOther());
                residentMobileNo.setText(savedInformation.getResidentTelNo());
                otherPhoneNo.setText(savedInformation.getOtherPhoneNo());
                email.setText(savedInformation.getEmail());
                spnResidentType.setSelection(savedInformation.getTypeOfResidence() - 1);
                applicantStayYear.setSelection(savedInformation.getYearOfStayYear());
                applicantStayMonth.setSelection(savedInformation.getYearOfStayMonth());

                curApartmentNo.setText(savedInformation.getCurrentAddressBuildingNo());
                curRoomNo.setText(savedInformation.getCurrentAddressRoomNo());
                curFloorNo.setText(savedInformation.getCurrentAddressFloor());
                curStreet.setText(savedInformation.getCurrentAddressStreet());
                curQuarter.setText(savedInformation.getCurrentAddressQtr());

                spnLivingWith.setSelection(savedInformation.getLivingWith() - 1);

                if (savedInformation.getNationality() == 1) {
                    myanNational.setChecked(true);
                } else {
                    otherNational.setChecked(true);
                }

                if (savedInformation.getGender() == 1) {
                    appGenderMale.setChecked(true);
                } else if (savedInformation.getGender() == 2) {
                    appGenderFemale.setChecked(true);
                }

                if (savedInformation.getMaritalStatus() == 1) {
                    appSingle.setChecked(true);
                } else {
                    appMarried.setChecked(true);
                }

                if (savedInformation.getCurrentAddressCity() == 0) {
                    autoCurrentCity.setText(BLANK);
                } else {
                    autoCurrentCity.setText(getCity(cityId, selectCityList, savedInformation.getCurrentAddressCity()));
                    this.setSelectedTownshipList(savedInformation.getCurrentAddressCity(), CURRENT);

                    setUpCurrentTownshipList(cityTownshipList, autoCurrentCity.getText().toString());
                    ArrayAdapter<String> curAddressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, selectedCurrentTownshipList);
                    curAddressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                    curAddressTownship.setNotifyOnChange(true);
                    autoCurrentTownship.setThreshold(1);
                    autoCurrentTownship.setAdapter(curAddressTownship);
                }
                if (savedInformation.getCurrentAddressTownship() == 0) {
                    autoCurrentTownship.setText("");
                } else {
                    autoCurrentTownship.setText(getTownship(cityTownshipList, savedInformation.getCurrentAddressCity(), savedInformation.getCurrentAddressTownship(), CURRENT));
                }
                perApartmentNo.setText(savedInformation.getPermanentAddressBuildingNo());
                perRoomNo.setText(savedInformation.getPermanentAddressRoomNo());
                perFloorNo.setText(savedInformation.getPermanentAddressFloor());
                perStreet.setText(savedInformation.getPermanentAddressStreet());
                perQuarter.setText(savedInformation.getPermanentAddressQtr());

                if (savedInformation.getPermanentAddressCity() == 0) {
                    autoPermanentCity.setText("");
                } else {
                    autoPermanentCity.setText(getCity(cityId, selectCityList, savedInformation.getPermanentAddressCity()));
                    this.setSelectedTownshipList(savedInformation.getCurrentAddressCity(), PERMANENT);

                    setUpPermanentTownshipList(cityTownshipList, autoPermanentCity.getText().toString());
                    final ArrayAdapter<String> guarCompanyTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, perTownshipList);
                    guarCompanyTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                    guarCompanyTownship.setNotifyOnChange(true);
                    autoPermanentTownship.setThreshold(1);
                    autoPermanentTownship.setAdapter(guarCompanyTownship);
                }
                if (savedInformation.getPermanentAddressTownship() == 0) {
                    autoPermanentTownship.setText("");
                } else {
                    autoPermanentTownship.setText(getTownship(cityTownshipList, savedInformation.getPermanentAddressCity(), savedInformation.getPermanentAddressTownship(), PERMANENT));
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                //this.languageFlag = item;
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeLabel(LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeLabel(LANG_EN);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setUpApplierFormData() {

        appName = txtname.getText().toString();
        appDob = txtdob.getText().toString();
        appFatherName = fatherName.getText().toString();
        appNationalityDes = radio_Nation_detail.getText().toString();
        appResidentTypeDes = residentTypeDes.getText().toString();
        appLivingWithDes = livingWithDes.getText().toString();
        appMobilePhone = txtphoneNo.getText().toString();
        appResidentPhone = residentMobileNo.getText().toString();
        appResidentOtherPhone = otherPhoneNo.getText().toString();
        appEmail = email.getText().toString();

        // Applicant's Address
        curAddrApartmentNo = curApartmentNo.getText().toString();
        curAddrRoomNo = curRoomNo.getText().toString();
        curAddrFloorNo = curFloorNo.getText().toString();
        curAddrStreet = curStreet.getText().toString();
        curAddrQuarter = curQuarter.getText().toString();
        currentTownshipValidate = autoCurrentTownship.getText().toString().trim();
        currentCityValidate = autoCurrentCity.getText().toString().trim();

        perAddrApartmentNo = perApartmentNo.getText().toString();
        perAddrRoomNo = perRoomNo.getText().toString();
        perAddrFloorNo = perFloorNo.getText().toString();
        perAddrStreet = perStreet.getText().toString();
        perAddrQuarter = perQuarter.getText().toString();
        permanentTownshipValidate = autoPermanentTownship.getText().toString();
        permanentCityValidate = autoPermanentCity.getText().toString();
    }

    void appLoadInputData() {
        setUpApplierFormData();
        ApplicationRegisterSaveReqBean registerSaveReqBean
                = new ApplicationRegisterSaveReqBean();

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            registerSaveReqBean = PreferencesManager.getDaftSavedInfo(getActivity());
        }

        /*OccupationDataFormBean occupationData= new OccupationDataFormBean();
        EmergencyContactFormBean emergencyContact = new EmergencyContactFormBean();
        GuarantorFormBean guarantorData = new GuarantorFormBean();*/

        /*registerSaveReqBean.setApplicantCompanyInfoDto(occupationData);
        registerSaveReqBean.setEmergencyContactInfoDto(emergencyContact);
        registerSaveReqBean.setGuarantorInfoDto(guarantorData);*/

        registerSaveReqBean.setName(appName);
        registerSaveReqBean.setDob(appDob);
        registerSaveReqBean.setNrcNo(appNrc);
        registerSaveReqBean.setFatherName(appFatherName.toUpperCase().trim());
        registerSaveReqBean.setHighestEducationTypeId(appEducation);
        registerSaveReqBean.setNationality(appNationality);
        registerSaveReqBean.setNationalityOther(appNationalityDes.toUpperCase().trim());
        registerSaveReqBean.setGender(appGender);
        registerSaveReqBean.setMaritalStatus(appMaritalStatus);
        registerSaveReqBean.setTypeOfResidence(appResidentType);
        registerSaveReqBean.setTypeOfResidenceOther(appResidentTypeDes.toUpperCase().trim());
        registerSaveReqBean.setLivingWith(appLivingWith);
        registerSaveReqBean.setLivingWithOther(appLivingWithDes);
        registerSaveReqBean.setYearOfStayYear(appStayYear);
        registerSaveReqBean.setYearOfStayMonth(appStayMonth);
        registerSaveReqBean.setMobileNo(appMobilePhone);
        registerSaveReqBean.setResidentTelNo(appResidentPhone);
        registerSaveReqBean.setOtherPhoneNo(appResidentOtherPhone);
        registerSaveReqBean.setEmail(appEmail);

        registerSaveReqBean.setCurrentAddressBuildingNo(curAddrApartmentNo);
        registerSaveReqBean.setCurrentAddressRoomNo(curAddrRoomNo);
        registerSaveReqBean.setCurrentAddressFloor(curAddrFloorNo);
        registerSaveReqBean.setCurrentAddressStreet(curAddrStreet);
        registerSaveReqBean.setCurrentAddressQtr(curAddrQuarter);

        int city_id = getCityId(cityId, selectCityList, autoCurrentCity.getText().toString());
        int town_id = getTownshipId(cityTownshipList, city_id, autoCurrentTownship.getText().toString());

        registerSaveReqBean.setCurrentAddressTownship(town_id);
        registerSaveReqBean.setCurrentAddressCity(city_id);

        registerSaveReqBean.setPermanentAddressBuildingNo(perAddrApartmentNo);
        registerSaveReqBean.setPermanentAddressRoomNo(perAddrRoomNo);
        registerSaveReqBean.setPermanentAddressFloor(perAddrFloorNo);
        registerSaveReqBean.setPermanentAddressStreet(perAddrStreet);
        registerSaveReqBean.setPermanentAddressQtr(perAddrQuarter);

        if (isEmptyOrNull(permanentCityValidate)) {
            registerSaveReqBean.setPermanentAddressCity(null);
            registerSaveReqBean.setPermanentAddressTownship(null);

        } else {
            int per_city_id = getCityId(cityId, selectCityList, permanentCityValidate);

            registerSaveReqBean.setPermanentAddressCity(per_city_id);
            if (isEmptyOrNull(permanentTownshipValidate)) {
                registerSaveReqBean.setPermanentAddressTownship(0);
            } else {
                int per_town_id = getTownshipId(cityTownshipList, per_city_id, permanentTownshipValidate);
                registerSaveReqBean.setPermanentAddressTownship(per_town_id);
            }
        }

        /*Log.e("permanent city :",String.valueOf(per_city_id));
        Log.e("permanent town :",String.valueOf(per_town_id));*/

        /*int per_city_id = getCityId(cityId, selectCityList,autoPermanentCity.getText().toString());
        int per_town_id = getTownshipId(cityTownshipList, per_city_id, autoPermanentTownship.getText().toString());
        registerSaveReqBean.setPermanentAddressTownship(per_town_id);
        registerSaveReqBean.setPermanentAddressCity(per_city_id);*/

        PreferencesManager.saveDaftSavedInfo(getActivity(), registerSaveReqBean);
    }

    private void saveApplicationToDatabase() {

        setUpApplierFormData();

        ApplicationRegisterSaveReqBean saveDataBean
                = new ApplicationRegisterSaveReqBean();
        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            saveDataBean = PreferencesManager.getDaftSavedInfo(getActivity());
        }

        /*mandatory fields*/
        saveDataBean.setDaApplicationTypeId(APPLICATION_PLOAN);
        saveDataBean.setCustomerId(customerId);
        saveDataBean.setChannelType(CHANNEL_MOBILE);

        /*data fields*/
        saveDataBean.setName(appName);
        saveDataBean.setDob(appDob);
        saveDataBean.setNrcNo(appNrc);
        saveDataBean.setFatherName(appFatherName.toUpperCase().trim());
        saveDataBean.setHighestEducationTypeId(appEducation);
        saveDataBean.setNationality(appNationality);
        saveDataBean.setNationalityOther(appNationalityDes.toUpperCase().trim());

        saveDataBean.setGender(appGender);
        saveDataBean.setMaritalStatus(appMaritalStatus);

        saveDataBean.setTypeOfResidence(appResidentType);
        saveDataBean.setTypeOfResidenceOther(appResidentTypeDes.toUpperCase().trim());

        saveDataBean.setLivingWith(appLivingWith);
        saveDataBean.setLivingWithOther(appLivingWithDes);

        saveDataBean.setYearOfStayYear(appStayYear);
        saveDataBean.setYearOfStayMonth(appStayMonth);

        saveDataBean.setMobileNo(appMobilePhone);
        saveDataBean.setResidentTelNo(appResidentPhone);
        saveDataBean.setOtherPhoneNo(appResidentOtherPhone);
        saveDataBean.setEmail(appEmail);

        int city_id = getCityId(cityId, selectCityList, currentCityValidate);
        int town_id = getTownshipId(cityTownshipList, city_id, currentTownshipValidate);

        saveDataBean.setCurrentAddressBuildingNo(curAddrApartmentNo);
        saveDataBean.setCurrentAddressRoomNo(curAddrRoomNo);
        saveDataBean.setCurrentAddressFloor(curAddrFloorNo);
        saveDataBean.setCurrentAddressStreet(curAddrStreet);
        saveDataBean.setCurrentAddressQtr(curAddrQuarter);
        saveDataBean.setCurrentAddressCity(city_id);
        saveDataBean.setCurrentAddressTownship(town_id);

        saveDataBean.setPermanentAddressBuildingNo(perAddrApartmentNo);
        saveDataBean.setPermanentAddressRoomNo(perAddrRoomNo);
        saveDataBean.setPermanentAddressFloor(perAddrFloorNo);
        saveDataBean.setPermanentAddressStreet(perAddrStreet);
        saveDataBean.setPermanentAddressQtr(perAddrQuarter);

        if (isEmptyOrNull(permanentCityValidate)) {
            saveDataBean.setPermanentAddressCity(0);
            saveDataBean.setPermanentAddressTownship(0);
        } else {
            int per_city_id = getCityId(cityId, selectCityList, permanentCityValidate);
            saveDataBean.setPermanentAddressCity(per_city_id);
            if (isEmptyOrNull(permanentTownshipValidate)) {
                saveDataBean.setPermanentAddressTownship(0);
            } else {
                int per_town_id = getTownshipId(cityTownshipList, per_city_id, permanentTownshipValidate);
                saveDataBean.setPermanentAddressTownship(per_town_id);
            }
        }

        if (saveDataBean.isBeanValid()) {

            if (!CommonUtils.isNetworkAvailable(getActivity())) {

                showNetworkErrorDialog(getActivity(), getNetErrMsg());

            } else {

                final ProgressDialog saveDialog = new ProgressDialog(getActivity());
                saveDialog.setMessage("Saving Data...");
                saveDialog.setCancelable(false);
                saveDialog.show();

                final String accessToken = PreferencesManager.getAccessToken(getActivity());

                Service saveRegisterData = APIClient.getApplicationRegisterService();
                Call<BaseResponse<ApplicationRegisterSaveReqBean>> req = saveRegisterData.saveRegisterData(accessToken, saveDataBean);

                req.enqueue(new Callback<BaseResponse<ApplicationRegisterSaveReqBean>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<ApplicationRegisterSaveReqBean>> call, Response<BaseResponse<ApplicationRegisterSaveReqBean>> response) {

                        if (response.isSuccessful()) {

                            BaseResponse baseResponse = response.body();

                            if (baseResponse != null) {

                                if (baseResponse.getStatus().equals(SUCCESS)) {

                                    ApplicationRegisterSaveReqBean localSaveReqBean = (ApplicationRegisterSaveReqBean) baseResponse.getData();
                                    PreferencesManager.saveDaftSavedInfo(getActivity(), localSaveReqBean);

                                    saveDialog.dismiss();
                                    showSnackBarMessage("Application Data saved.");

                                } else {
                                    saveDialog.dismiss();
                                    showSnackBarMessage("Application Data cannot be saved.");
                                }

                            } else {
                                saveDialog.dismiss();
                                showSnackBarMessage("Application Data cannot be saved.");
                            }

                        } else {
                            closeDialog(saveDialog);
                            showSnackBarMessage("Application Data cannot be saved.");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<ApplicationRegisterSaveReqBean>> call, Throwable t) {
                        closeDialog(saveDialog);
                        showSnackBarMessage("Application Data save failed.");
                    }
                });
            }

        } else {
            showSnackBarMessage("Application Data is required to save.");
        }
    }

    private void showValidationMsg(String curLang) {

        setUpApplierFormData();

        ApplicationFormErrMesgBean errAppMesgBean = PreferencesManager.getErrMesgInfo(getContext());

        if (errAppMesgBean == null) {
            errAppMesgBean = new ApplicationFormErrMesgBean();
        }

        /*Current Township*/
        if (CommonUtils.isEmptyOrNull(currentTownshipValidate)) {
            errAddressTownship.setVisibility(View.VISIBLE);
            errAddressTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_township, getActivity()));
            errAddressTownshipLocale = R.string.da_err_township;
            errAppMesgBean.setAppAddressTownshipLocale(errAddressTownshipLocale);
        } else {
            errAddressTownship.setVisibility(View.GONE);
            errAddressTownshipLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppAddressTownshipLocale(errAddressTownshipLocale);
        }

        /*Current City*/
        if (CommonUtils.isEmptyOrNull(currentCityValidate)) {
            errAddressCity.setVisibility(View.VISIBLE);
            errAddressCity.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_city, getActivity()));
            errAddressCityLocale = R.string.da_err_city;
            errAppMesgBean.setAppAddressCityLoacle(errAddressCityLocale);
        } else {
            errAddressCity.setVisibility(View.GONE);
            errAddressCityLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppAddressCityLoacle(errAddressCityLocale);
        }

        /*Current Street*/
        if (CommonUtils.isEmptyOrNull(curAddrStreet)) {
            errAddressStreet.setVisibility(View.VISIBLE);
            errAddressStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_street, getActivity()));
            errAddressStreetLocale = R.string.da_err_street;
            errAppMesgBean.setAppAddressStreetLocale(errAddressStreetLocale);
        } else {
            errAddressStreet.setVisibility(View.GONE);
            errAddressStreetLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppAddressStreetLocale(errAddressStreetLocale);
        }

        /*Current Quarter*/
        if (CommonUtils.isEmptyOrNull(curAddrQuarter)) {
            errAddressQuarter.setVisibility(View.VISIBLE);
            errAddressQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_quarter, getActivity()));
            errAddressQuarterLocale = R.string.da_err_quarter;
            errAppMesgBean.setAppAddressQuarterLoacle(errAddressQuarterLocale);
        } else {
            errAddressQuarter.setVisibility(View.GONE);
            errAddressQuarterLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppAddressQuarterLoacle(errAddressQuarterLocale);
        }

        /*Name*/
        if (appName == null || appName.equals(BLANK) || appName.equals(SPACE)) {
            errName.setVisibility(View.VISIBLE);
            errName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_name_err, getActivity()));
            errNameLocale = R.string.register_name_err;
            errAppMesgBean.setAppNameLocale(errNameLocale);

        } else if (!isPureAscii(appName)) {
            errName.setVisibility(View.VISIBLE);
            errName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_name_format_err, getActivity()));
            errNameLocale = R.string.register_name_format_err;
            errAppMesgBean.setAppNameLocale(errNameLocale);

        } else {
            errName.setVisibility(View.GONE);
            errNameLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppNameLocale(errNameLocale);
        }

        /*Date of Birth*/
        if (appDob == null || appDob.equals(BLANK)) {
            errDob.setVisibility(View.VISIBLE);
            errDob.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_dob_err, getActivity()));
            errDobLocale = R.string.register_dob_err;
            errAppMesgBean.setAppDobLocale(errDobLocale);

        } else {
            errDob.setVisibility(View.GONE);
            errDobLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppDobLocale(errDobLocale);
        }

        /*Father's Name*/
        if (appFatherName == null || appFatherName.equals(BLANK) || appFatherName.equals(SPACE)) {
            errFatherName.setVisibility(View.VISIBLE);
            errFatherName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_fatherName_require_err, getActivity()));
            errFatherNameLocale = R.string.da_fatherName_require_err;
            errAppMesgBean.setAppFatherNameLocale(errFatherNameLocale);

        } else if (!isPureAscii(appFatherName)) {
            errFatherName.setVisibility(View.VISIBLE);
            errFatherName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_fatherName_format_err, getActivity()));
            errFatherNameLocale = R.string.da_fatherName_format_err;
            errAppMesgBean.setAppFatherNameLocale(errFatherNameLocale);

        } else {
            errFatherName.setVisibility(View.GONE);
            errFatherNameLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppFatherNameLocale(errFatherNameLocale);
        }

        /*Nationality*/
        if (appNationality == CommonConstants.OTHER) {
            if (appNationalityDes == null || appNationalityDes.equals(BLANK) || appNationalityDes.equals(SPACE)) {
                errNationality.setVisibility(View.VISIBLE);
                errNationality.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_nationality_require_err, getActivity()));
                errNationalityLocale = R.string.da_nationality_require_err;
                errAppMesgBean.setAppNationalityLocale(errNationalityLocale);
            } else {
                errNationality.setVisibility(View.GONE);
                errNationalityLocale = R.string.da_mesg_blank;
                errAppMesgBean.setAppNationalityLocale(errNationalityLocale);
            }
        }

        /*Type of Residence*/
        if (appResidentType == CommonConstants.RESIDENT_OTHER) {
            if (appResidentTypeDes == null || appResidentTypeDes.equals(BLANK) || appResidentTypeDes.equals(SPACE)) {
                errResidentType.setVisibility(View.VISIBLE);
                errResidentType.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_residentType_require_err, getActivity()));
                errResidentTypeLocale = R.string.da_residentType_require_err;
                errAppMesgBean.setAppResidentTypeLocale(errResidentTypeLocale);
            } else {
                errResidentType.setVisibility(View.GONE);
                errResidentTypeLocale = R.string.da_mesg_blank;
                errAppMesgBean.setAppResidentTypeLocale(errResidentTypeLocale);
            }
        }

        /*Living With*/
        if (appLivingWith == CommonConstants.LIVING_OTHER) {
            if (appLivingWithDes == null || appLivingWithDes.equals(BLANK) || appLivingWithDes.equals(SPACE)) {
                errLivingWith.setVisibility(View.VISIBLE);
                errLivingWith.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_living_require_err, getActivity()));
                errLivingWithLocale = R.string.da_living_require_err;
                errAppMesgBean.setAppLivingWithLocale(errLivingWithLocale);
            } else {
                errLivingWith.setVisibility(View.GONE);
                errLivingWithLocale = R.string.da_mesg_blank;
                errAppMesgBean.setAppLivingWithLocale(errLivingWithLocale);
            }
        }

        /*Year of Stay*/
        if (appStayYear == 0 && appStayMonth == 0) {
            errStayTime.setVisibility(View.VISIBLE);
            errStayTime.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_yearStay_require_err, getActivity()));
            errStayTimeLocale = R.string.da_yearStay_require_err;
            errAppMesgBean.setAppStayTimeLocale(errStayTimeLocale);

        } else if (appStayMonth > 11) {
            errStayTime.setVisibility(View.VISIBLE);
            errStayTime.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_yearStayMonth_exceed_err, getActivity()));
            errStayTimeLocale = R.string.da_yearStayMonth_exceed_err;
            errAppMesgBean.setAppStayTimeLocale(errStayTimeLocale);

        } else {
            errStayTime.setVisibility(View.GONE);
            errStayTimeLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppStayTimeLocale(errStayTimeLocale);
        }

        /*Mobile No.*/
        if (appMobilePhone == null || appMobilePhone.equals(BLANK)) {
            errMobileNo.setVisibility(View.VISIBLE);

        } else if (!CommonUtils.isPhoneNoValid(appMobilePhone)) {
            errMobileNo.setVisibility(View.VISIBLE);
        } else {
            errMobileNo.setVisibility(View.GONE);
        }

        /*Residence No.*/
        if (appResidentPhone != null && !appResidentPhone.equals(BLANK)) {
            if (!CommonUtils.isNumberValid(appResidentPhone)) {
                errResidenceTelNo.setVisibility(View.VISIBLE);
                errResidenceTelNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_residentTelNo_format_err, getActivity()));
                errResidenceTelNoLocale = R.string.da_residentTelNo_format_err;
                errAppMesgBean.setAppResidentMobileNoLocale(errResidenceTelNoLocale);

            } else {
                errResidenceTelNo.setVisibility(View.GONE);
                errResidenceTelNoLocale = R.string.da_mesg_blank;
                errAppMesgBean.setAppResidentMobileNoLocale(errResidenceTelNoLocale);
            }
        } else {
            errResidenceTelNo.setVisibility(View.GONE);
            errResidenceTelNoLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppResidentMobileNoLocale(errResidenceTelNoLocale);
        }

        /*Other Phone No.*/
        if (appResidentOtherPhone != null && !appResidentOtherPhone.equals(BLANK)) {
            if (!CommonUtils.isNumberValid(appResidentOtherPhone)) {
                errOtherPhoneNo.setVisibility(View.VISIBLE);
                errOtherPhoneNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_otherPhoneNo_format_err, getActivity()));
                errOtherPhoneNoLocale = R.string.da_otherPhoneNo_format_err;
                errAppMesgBean.setAppOtherPhoneNoLocale(errOtherPhoneNoLocale);

            } else {
                errOtherPhoneNo.setVisibility(View.GONE);
                errOtherPhoneNoLocale = R.string.da_mesg_blank;
                errAppMesgBean.setAppOtherPhoneNoLocale(errOtherPhoneNoLocale);
            }
        } else {
            errOtherPhoneNo.setVisibility(View.GONE);
            errOtherPhoneNoLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppOtherPhoneNoLocale(errOtherPhoneNoLocale);
        }

        /*Email*/
        if (!isEmptyOrNull(appEmail)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(appEmail).matches()) {
                errEmail.setVisibility(View.VISIBLE);
                errEmail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_email_format_err, getActivity()));
                errEmailLocale = R.string.da_email_format_err;
                errAppMesgBean.setAppEmailLocale(errEmailLocale);
            } else {
                errEmail.setVisibility(View.GONE);
                errEmailLocale = R.string.da_mesg_blank;
                errAppMesgBean.setAppEmailLocale(errEmailLocale);
            }
        } else {
            errEmail.setVisibility(View.GONE);
            errEmailLocale = R.string.da_mesg_blank;
            errAppMesgBean.setAppEmailLocale(errEmailLocale);
        }

        PreferencesManager.saveErrorMesgInfo(getContext(), errAppMesgBean);
    }

    boolean checkApplicationData() {

        boolean validate = true;
        setUpApplierFormData();

        /*Current Township*/
        if (CommonUtils.isEmptyOrNull(currentTownshipValidate)) {
            validate = false;
        }

        /*Current City*/
        if (CommonUtils.isEmptyOrNull(currentCityValidate)) {
            validate = false;
        }

        /*Current Street*/
        if (CommonUtils.isEmptyOrNull(curAddrStreet)) {
            validate = false;
        }

        /*Current Quarter*/
        if (CommonUtils.isEmptyOrNull(curAddrQuarter)) {
            validate = false;
        }

        /*Name*/
        if (CommonUtils.isEmptyOrNull(appName)) {
            validate = false;
        } else if (!isPureAscii(appName)) {
            validate = false;
        }

        /*Date of Birth*/
        if (CommonUtils.isEmptyOrNull(appDob)) {
            validate = false;
        }

        /*Father's Name*/
        if (CommonUtils.isEmptyOrNull(appFatherName)) {
            validate = false;
        } else if (!isPureAscii(appFatherName)) {
            validate = false;
        }

        /*Nationality*/
        if (appNationality == CommonConstants.OTHER) {
            if (CommonUtils.isEmptyOrNull(appNationalityDes)) {
                validate = false;
            }
        }

        /*Type of Residence*/
        if (appResidentType == CommonConstants.RESIDENT_OTHER) {
            if (CommonUtils.isEmptyOrNull(appResidentTypeDes)) {
                validate = false;
            }
        }

        /*Year of Stay*/
        if (appStayYear == 0 && appStayMonth == 0) {
            validate = false;
        }

        if (appStayMonth > 11) {
            validate = false;
        }

        /*Mobile No.*/
        if (CommonUtils.isEmptyOrNull(appMobilePhone)) {
            validate = false;

        } else if (!CommonUtils.isPhoneNoValid(appMobilePhone)) {
            validate = false;
        }

        if (appResidentPhone != null && !appResidentPhone.equals(BLANK)) {
            if (!CommonUtils.isNumberValid(appResidentPhone)) {
                validate = false;
            }
        }

        if (appResidentOtherPhone != null && !appResidentOtherPhone.equals(BLANK)) {
            if (!CommonUtils.isNumberValid(appResidentOtherPhone)) {
                validate = false;
            }
        }

        /*Email*/
        if (!isEmptyOrNull(appEmail)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(appEmail).matches()) {
                validate = false;
            }
        }

        return validate;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.commit();
    }

    public void changeLabel(String language) {
        /*occupationTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_title, getContext()));*/
        applicationTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_application_data_title, getContext()));

        labelName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_name, getActivity()));
        errName.setText(CommonUtils.getLocaleString(new Locale(language), errNameLocale, getActivity()));

        labelDob.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_dob, getActivity()));
        errDob.setText(CommonUtils.getLocaleString(new Locale(language), errDobLocale, getActivity()));

        labelNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_nrc, getActivity()));
        errNrc.setText(CommonUtils.getLocaleString(new Locale(language), errNrcLocale, getActivity()));

        labelFatherName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_father, getActivity()));
        errFatherName.setText(CommonUtils.getLocaleString(new Locale(language), errFatherNameLocale, getActivity()));

        labelNationality.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_nationality, getActivity()));
        errNationality.setText(CommonUtils.getLocaleString(new Locale(language), errNationalityLocale, getActivity()));

        labelGender.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_gender, getActivity()));
        labelMaritalStatus.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_marital, getActivity()));

        labelResidentType.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_residenceType, getActivity()));
        errResidentType.setText(CommonUtils.getLocaleString(new Locale(language), errResidentTypeLocale, getActivity()));

        labelLivingWith.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_livingWith, getActivity()));
        errLivingWith.setText(CommonUtils.getLocaleString(new Locale(language), errLivingWithLocale, getActivity()));

        labelStayTime.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_yearStay, getActivity()));
        errStayTime.setText(CommonUtils.getLocaleString(new Locale(language), errStayTimeLocale, getActivity()));

        labelMobileNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_mobileNo, getActivity()));
        errMobileNo.setText(CommonUtils.getLocaleString(new Locale(language), errMobileNoLocale, getActivity()));

        labelResidentMobileNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_residentNo, getActivity()));
        errResidenceTelNo.setText(CommonUtils.getLocaleString(new Locale(language), errResidenceTelNoLocale, getActivity()));

        labelOtherPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_otherPhone, getActivity()));
        errOtherPhoneNo.setText(CommonUtils.getLocaleString(new Locale(language), errOtherPhoneNoLocale, getActivity()));

        labelEmail.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_email, getActivity()));
        errEmail.setText(CommonUtils.getLocaleString(new Locale(language), errEmailLocale, getActivity()));

        labelStayYear.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_stay_year, getActivity()));
        labelStayMonth.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_stay_month, getActivity()));

        lblAddressTitleCurrent.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_currAddress, getActivity()));
        lblAddressTitlePermanent.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_perAddress, getActivity()));

        lblEducation.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_education, getActivity()));

        lblCurApartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_apartment_no, getActivity()));
        lblCurRoomNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_room_no, getActivity()));
        lblCurFloorNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_floor_no, getActivity()));
        lblCurStreet.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_street, getActivity()));
        lblCurQuarter.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_quarter, getActivity()));
        lblCurTownship.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_township, getActivity()));
        lblCurCity.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_city, getActivity()));

        lblPerApartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_apartment_no, getActivity()));
        lblPerRoomNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_room_no, getActivity()));
        lblPerFloorNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_floor_no, getActivity()));
        lblPerStreet.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_app_data_street, getActivity()));
        lblPerQuarter.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_permanent_quarter, getActivity()));
        lblPerTownship.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_app_data_township, getActivity()));
        lblPerCity.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_app_data_city, getActivity()));

        errAddressStreet.setText(CommonUtils.getLocaleString(new Locale(language), errAddressStreetLocale, getActivity()));
        errAddressQuarter.setText(CommonUtils.getLocaleString(new Locale(language), errAddressQuarterLocale, getActivity()));
        errAddressTownship.setText(CommonUtils.getLocaleString(new Locale(language), errAddressTownshipLocale, getActivity()));
        errAddressCity.setText(CommonUtils.getLocaleString(new Locale(language), errAddressCityLocale, getActivity()));

        btnAppNext.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_next_btn, getActivity()));
        btnAppSave.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_save_btn, getActivity()));

        PreferencesManager.setCurrentLanguage(getContext(), language);
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    void setUpNrcInfo(String nrc) {
        if (nrc != null) {
            applicantNrcFilled.setText(nrc);
        }
    }

    void setUpDataOnPageChanged() {
        appLoadInputData();
        MainMenuActivityDrawer.appDataCorrect = checkApplicationData();
        Log.e("Application Data", String.valueOf(MainMenuActivityDrawer.appDataCorrect));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG", "Application Data : onStart()");

        residentDetailCheck = residentTypeDes.getText().toString();
        if (!isEmptyOrNull(residentDetailCheck)) {
            residentTypeDes.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        fatherNameCheck = fatherName.getText().toString();
        if (!isEmptyOrNull(fatherNameCheck)) {
            fatherName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emailCheck = email.getText().toString();
        if (!isEmptyOrNull(emailCheck)) {
            email.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentStreetCheck = curStreet.getText().toString();
        if (!isEmptyOrNull(currentStreetCheck)) {
            curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentQuarterCheck = curQuarter.getText().toString();
        if (!isEmptyOrNull(currentQuarterCheck)) {
            curQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentTownshipCheck = autoCurrentTownship.getText().toString();
        if (!isEmptyOrNull(currentTownshipCheck)) {
            autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentCityCheck = autoCurrentCity.getText().toString();
        if (!isEmptyOrNull(currentCityCheck)) {
            autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        nationalityDetailCheck = radio_Nation_detail.getText().toString();
        if (!isEmptyOrNull(nationalityDetailCheck)) {
            radio_Nation_detail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "Application Data : onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG", "Application Data : onPause()");

    }

    void displayNationalityDesc() {
        if (appNationality == MYANMAR) {
            radio_Nation_detail.setVisibility(View.GONE);
            radio_Nation_detail.setText(BLANK);
        } else {
            radio_Nation_detail.setVisibility(View.VISIBLE);
        }
    }

    void displayResidentDesc() {
        if (appResidentType == 5) {
            residentTypeDes.setVisibility(View.VISIBLE);
        } else {
            residentTypeDes.setVisibility(View.GONE);
            errResidentType.setText(BLANK);
            errResidentType.setVisibility(View.GONE);
        }
    }

    void displayLivingWithDesc() {
        if (appLivingWith == 6) {
            livingWithDes.setVisibility(View.VISIBLE);
        } else {
            livingWithDes.setVisibility(View.GONE);
        }
    }

    void setUpCityList(List<CityTownshipResBean> cityTownshipList) {
        selectCityList = new ArrayList<>();
        selectCityListPermanent = new ArrayList<>();
        cityId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            selectCityList.add(listBean.getName());
            cityId.add(listBean.getCityId());
        }
    }

    void setUpCurrentTownshipList(List<CityTownshipResBean> cityTownshipList, String cityName) {
        selectedCurrentTownshipList = new ArrayList<>();
        townshipId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getName().equals(cityName)) {
                townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    selectedCurrentTownshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }
            }
        }
    }

    void setUpPermanentTownshipList(List<CityTownshipResBean> cityTownshipList, String cityName) {
        perTownshipList = new ArrayList<>();
        perTownshipId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getName().equals(cityName)) {
                townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    perTownshipList.add(townshipBean.getName());
                    perTownshipId.add(townshipBean.getTownshipId());
                }
            }
        }
    }

    String getCity(List<Integer> cityID, List<String> cityName, int cityId) {
        String city = "";
        for (int id = 0; id < cityID.size(); id++) {
            if (cityID.get(id) == cityId) {
                city = cityName.get(id);
            }
        }
        return city;
    }

    int getCityId(List<Integer> cityID, List<String> cityName, String name) {
        for (int id = 0; id < cityName.size(); id++) {
            if (cityName.get(id).equals(name)) {
                saveCityid = cityID.get(id);
            }
        }
        return saveCityid;
    }

    /*String getTownship_1(List<CityTownshipResBean> cityTownshipList, int cityId, int townId, int index){
        String township = "";
        selectedCurrentTownshipList = new ArrayList<>();
        townshipId = new ArrayList<>();
        for(CityTownshipResBean listBean: cityTownshipList){
            if(listBean.getCityId()== cityId){
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for(TownshipListBean townshipBean : townshipBeanList){
                    if (index == CURRENT){
                        selectedCurrentTownshipList.add(townshipBean.getName());
                    } else {
                        perTownshipList.add(townshipBean.getName());
                    }
                    townshipId.add(townshipBean.getTownshipId());
                }
                if (index == CURRENT) {
                    ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, selectedCurrentTownshipList);
                    addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                    addressTownship.setNotifyOnChange(true);
                    autoCurrentTownship.setThreshold(1);
                    autoCurrentTownship.setAdapter(addressTownship);

                } else {
                    ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, perTownshipList);
                    addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                    addressTownship.setNotifyOnChange(true);
                    autoPermanentTownship.setThreshold(1);
                    autoPermanentTownship.setAdapter(addressTownship);

                }
                break;
            }
        }

        for(int id = 0; id<townshipId.size(); id++){
            if(townshipId.get(id) == townId){
                if (index == CURRENT){
                    township = selectedCurrentTownshipList.get(id);

                } else {
                    township = perTownshipList.get(id);
                }

            }
        }

        return township;
    }*/

    String getTownship(List<CityTownshipResBean> cityTownshipList, int cityId, int townId, int index) {
        String township = "";
        List<String> replaceTownshipName = new ArrayList<>();
        List<Integer> replaceTownshipId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getCityId() == cityId) {
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    replaceTownshipName.add(townshipBean.getName());
                    replaceTownshipId.add(townshipBean.getTownshipId());
                }

                ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, replaceTownshipName);
                addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                addressTownship.setNotifyOnChange(true);
                if (index == CURRENT) {
                    autoCurrentTownship.setThreshold(1);
                    autoCurrentTownship.setAdapter(addressTownship);

                } else {
                    autoPermanentTownship.setThreshold(1);
                    autoPermanentTownship.setAdapter(addressTownship);

                }
                break;
            }
        }

        for (int id = 0; id < replaceTownshipId.size(); id++) {
            if (replaceTownshipId.get(id) == townId) {
                township = replaceTownshipName.get(id);
            }
        }

        return township;
    }

    private void setSelectedTownshipList(int cityId, int index) {

        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getCityId() == cityId) {
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    selectedCurrentTownshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }

                ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, selectedCurrentTownshipList);
                addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                addressTownship.setNotifyOnChange(true);
                if (index == CURRENT) {
                    autoCurrentTownship.setThreshold(1);
                    autoCurrentTownship.setAdapter(addressTownship);

                } else {
                    autoPermanentTownship.setThreshold(1);
                    autoPermanentTownship.setAdapter(addressTownship);

                }
                break;
            }
        }
    }

    int getTownshipId(List<CityTownshipResBean> cityTownshipList, int cityId, String name) {
        List<String> saveTownshipList = new ArrayList<>();
        List<Integer> saveTownshipId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getCityId() == cityId) {
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    saveTownshipList.add(townshipBean.getName());
                    saveTownshipId.add(townshipBean.getTownshipId());
                }
            }
        }

        for (int id = 0; id < saveTownshipList.size(); id++) {
            if (saveTownshipList.get(id).equals(name)) {
                townshipid = saveTownshipId.get(id);
            }
        }

        return townshipid;
    }

    Date stringToDate(String date) {
        Date appDate = new Date();
        try {
            appDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (Exception e) {
            //
        }
        return appDate;
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
        Log.e("Change Language", "Application Flag");
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment());
        Log.e("Menu Back", "Action");
    }
}