package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
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

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.canner.stepsview.StepsView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.ApplicationFormErrMesgBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.GuarantorFormBean;
import mm.com.aeon.vcsaeon.beans.NrcBean;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.beans.TownshipListBean;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BIRTH_DATE_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CHANNEL_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_ID;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DA_TITLES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.EMERGENCY_RELATION_OTHER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.GUARANTOR_LIVING_OTHER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.GUARANTOR_RELATION_OTHER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.GUARANTOR_RESIDENT_OTHER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOAN_AMOUNT_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MIN_AGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MYANMAR;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.OTHER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.dateToString2;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.getNrcFromString;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isEmptyOrNull;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isNrcCodeValid;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.stringToDate;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class SmallLoanGuarantorFragment extends PagerRootFragment implements LanguageChangeListener {

    private View view;
    private View serviceUnavailable;

    private TextView errName;
    private TextView errResidentTel;
    private TextView errNrc;
    private TextView errNationalityDetail;
    private TextView errMobileNo;
    private TextView errRelationshipDetail;
    private TextView errResidentTypeDetail;
    private TextView errCompanyName;
    private TextView errCompanyTel;
    private TextView errDepartment;
    private TextView errPositon;
    private TextView errDob;
    private TextView errMonthlyIncome;
    private TextView errStayTime;
    private TextView errService;
    private String companyStatusCheck;

    private Integer errNameLocale;
    private Integer errResidentTelLocale;
    private Integer errNrcLocale;
    private Integer errNationalityDetailLocale;
    private Integer errMobileNoLocale;
    private Integer errRelationshipDetailLocale;
    private Integer errResidentTypeDetailLocale;
    private Integer errCompanyNameLocale;
    private Integer errCompanyTelLocale;
    private Integer errDepartmentLocale;
    private Integer errPositonLocale;
    private Integer errDobLocale;
    private Integer errMonthlyIncomeLocale;
    private Integer errServiceLocale;
    private Integer errStayTimeLocale;

    private TextView labelName;
    private TextView labelDob;
    private TextView labelNrc;
    private TextView labelNationality;
    private TextView labelMobileNo;
    private TextView labelResidentTelNo;
    private TextView labelRelationship;
    private TextView labelResidentType;
    private TextView labelLivingWith;
    private TextView labelGender;
    private TextView labelMarital;
    private TextView labelStayTime;
    private TextView labelCompanyName;
    private TextView labelCompanyTel;
    private TextView labelDepartment;
    private TextView labelPositon;
    private TextView labelService;
    private TextView labelMonthlyIncome;
    private TextView labelTotalIncome;
    private TextView totalIncomeFilled;
    private TextView labelStayYear;
    private TextView labelStayMonth;
    private TextView labelServiceYear;
    private TextView labelServiceMonth;

    private EditText guar_name;
    private EditText guarantorDob;
    private EditText guar_nrc;
    private EditText guar_nationalityDetail;
    private EditText guar_mobileNo;
    private EditText guar_residentTelNo;
    private EditText guar_relaitonshipWith;
    private EditText guar_residentTypeDetail;
    private EditText guar_companyTel;
    private EditText guar_department;
    private EditText guar_position;
    private EditText guar_monthlyIncome;
    private EditText guar_companyName;

    private static String name;
    private static String dob;
    private static String nrc;
    private static String nrcNumber;
    private static String nationalityDetail;
    private static String mobileNo;
    private static String residentMobile;
    private static String relationshipDetail;
    private static String residentTypeDetail;
    private static String companyName;
    private static String companyTel;
    private static String department;
    private static String livingWtihDetail;
    private static String position;

    private static String stay_year;
    private static String stay_month;
    private static String service_year;
    private static String service_month;
    private static String monthly_income;
    private static String total_income;
    private static String townshipCodeVal;

    private int relationship;
    private int residentType;
    private int livingWith;
    private static int gender = 1;
    private static int nationality = 1;
    private static int maritalStatus = 1;
    private int stayYear;
    private int stayMonth;
    private int serviceYear;
    private int serviceMonth;

    private double monthlyIncome;
    private double totalIncome;
    private String curLang;
    private static int nrcTypePosition;
    private static int divCodePosition;

    private Spinner spinnerType;
    private Spinner spinnerRelation;
    private Spinner spnResidentType;
    private Spinner spnLivingWith;
    private Spinner guarStayYear;
    private Spinner guarStayMonth;
    private Spinner guarServiceYear;
    private Spinner guarServiceMonth;

    private RadioGroup radioNationality;
    private RadioGroup radioGender;
    private RadioGroup radioMaritalSataus;

    private RadioButton guarMyanNational;
    private RadioButton guarOtherNational;
    private RadioButton guarGenderMale;
    private RadioButton guarGenderFemale;
    private RadioButton guarMaritalSingle;
    private RadioButton guarMaritalMarried;

    private int selectedPosition;
    private static SharedPreferences validationPreferences;
    private String customerId;
    private static String stateDivCodeVal;
    private static String nrcTypeVal;
    private Spinner spinnerDivCode;
    private AutoCompleteTextView autoCompleteTwspCode;
    private static List<String> townshipCode = new ArrayList<>();
    private boolean isInit;

    private Button btnNext;
    private Button btnSave;

    private LinearLayout backToEmerData;
    private LinearLayout goToLoanData;
    private TextView loanDataTitle;
    private TextView guarantorTitle;
    private TextView emergencyTitle;

    private String nameCheck;
    private String dobCheck;
    private String nrcTownShipCheck;
    private String nrcNoCheck;
    private String mobileNoCheck;
    private String relationshipDetailCheck;
    private String currentAddressCheck;
    private String residentDetailCheck;
    private String companyDepartmentCheck;
    private String companyPositionCheck;
    private String monthlyIncomeCheck;
    private String nationalityDetailCheck;
    private String companyTelCheck;

    private TextView lblAddressTitleCurrent;
    private TextView lblCurApartment;
    private TextView lblCurRoomNo;
    private TextView lblCurFloorNo;
    private TextView lblCurStreet;
    private TextView lblCurQuarter;
    private TextView lblCurTownship;
    private TextView lblCurCity;

    private TextView lblAddressTitleCompany;
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

    private TextView errGuarantorStreet;
    private TextView errGuarantorQuarter;
    private TextView errGuarantorCity;
    private TextView errGuarantorTownship;

    private TextView errGuarantorComStreet;
    private TextView errGuarantorComQuarter;
    private TextView errGuarantorComCity;
    private TextView errGuarantorComTownship;

    private Integer errGuarantorStreetLocale;
    private Integer errGuarantorQuarterLocale;
    private Integer errGuarantorCityLocale;
    private Integer errGuarantorTownshipLocale;

    private Integer errGuarantorComStreetLocale;
    private Integer errGuarantorComQuarterLocale;
    private Integer errGuarantorComCityLocale;
    private Integer errGuarantorComTownshipLocale;

    private AutoCompleteTextView currentGuarantorTownship;
    private AutoCompleteTextView currentGuarantorCity;
    private AutoCompleteTextView guarantorCompanyTownship;
    private AutoCompleteTextView guarantorCompanyCity;

    private String currentApartmentNo;
    private String currentRoomNo;
    private String currentFloorNo;
    private String currentStreet;
    private String currentQuarter;

    private String permanentApartmentNo;
    private String permanentRoomNo;
    private String permanentFloorNo;
    private String permanentStreet;
    private String permanentQuarter;

    private String currentStreetCheck;
    private String currentCityCheck;
    private String currentTownshipCheck;
    private String companyStreetCheck;
    private String companyCityCheck;
    private String companyTownshipCheck;
    private String currentQuarterCheck;
    private String companyQuarterCheck;

    private int currentTownship;
    private int currentCity;
    private int currentCompanyTownship;
    private int currentCompanyCity;

    private String curCityName;
    private String perCityName;
    private List<String> cityList;
    private List<String> townshipList;
    private List<Integer> cityId;
    private List<Integer> townshipId;

    private List<String> perCityList;
    private List<String> perTownshipList;
    private List<Integer> perCityId;
    private List<Integer> perTownshipId;

    private List<TownshipListBean> townshipBeanList = new ArrayList<>();
    private List<CityTownshipResBean> cityTownshipList;

    private int saveCityid;
    private int saveCurTownshipid;
    private int savePerTownshipid;
    private String guarantorCity;
    private String guarantorTownship;
    private String appCompanyCity;
    private String appCompanyTownship;

    private final int CURRENT = 1;
    private final int COMPANY = 2;

    private StateProgressBar guarStepView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_small_loan_guarantor, container, false);
        setHasOptionsMenu(true);

        String[] pages = {"Application\nData", "Occupation\nData","Emergency\nContact", "Guarantor\nData", "Loan\nConfirmation"};
        guarStepView = view.findViewById(R.id.guar_stepped_bar);
        guarStepView.setStateDescriptionData(pages);
        guarStepView.setOnStateItemClickListener(new OnStateItemClickListener() {
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

        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);
        curLang = PreferencesManager.getCurrentLanguage(getContext());

        isInit = true;
        validationPreferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        customerId = PreferencesManager.getStringEntryFromPreferences(validationPreferences, CUSTOMER_ID);

        StepsView stepsView = view.findViewById(R.id.stepsView_4);
        stepsView.setLabels(DA_TITLES)
                .setBarColorIndicator(getContext().getColor(R.color.gray))
                .setProgressColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setLabelColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setCompletedPosition(3)
                .drawView();

        // Guarantor Current address Label
        lblAddressTitleCurrent = view.findViewById(R.id.guarantor_address_title);
        lblCurApartment = view.findViewById(R.id.guar_txt_apartment_no);
        lblCurRoomNo = view.findViewById(R.id.guar_txt_room_no);
        lblCurFloorNo = view.findViewById(R.id.guar_txt_floor_no);
        lblCurStreet = view.findViewById(R.id.guar_txt_street);
        lblCurQuarter = view.findViewById(R.id.guar_txt_quarter);
        lblCurTownship = view.findViewById(R.id.guar_txt_township);
        lblCurCity = view.findViewById(R.id.guar_txt_city);
        guar_companyName = view.findViewById(R.id.guarantor_company_name);

        // Guarantor Permanant address Label
        lblAddressTitleCompany = view.findViewById(R.id.guarantor_address);
        lblPerApartment = view.findViewById(R.id.guar_com_txt_apartment_no);
        lblPerRoomNo = view.findViewById(R.id.guar_com_txt_room_no);
        lblPerFloorNo = view.findViewById(R.id.guar_com_txt_floor_no);
        lblPerStreet = view.findViewById(R.id.guar_com_txt_street);
        lblPerQuarter = view.findViewById(R.id.guar_com_txt_quarter);
        lblPerTownship = view.findViewById(R.id.guar_com_txt_township);
        lblPerCity = view.findViewById(R.id.guar_com_txt_city);

        // Current Address EditText
        curApartmentNo = view.findViewById(R.id.guar_apartment_no);
        curRoomNo = view.findViewById(R.id.guar_room_no);
        curFloorNo = view.findViewById(R.id.guar_floor_no);
        curStreet = view.findViewById(R.id.guar_street);
        curQuarter = view.findViewById(R.id.guar_quarter);

        // Permanant Address EditText
        perApartmentNo = view.findViewById(R.id.guar_com_apartment_no);
        perRoomNo = view.findViewById(R.id.guar_com_room_no);
        perFloorNo = view.findViewById(R.id.guar_com_floor_no);
        perStreet = view.findViewById(R.id.guar_com_street);
        perQuarter = view.findViewById(R.id.guar_com_quarter);

        // Current/permanant Address
        currentGuarantorTownship = view.findViewById(R.id.guar_township);
        currentGuarantorCity = view.findViewById(R.id.guar_city);
        guarantorCompanyTownship = view.findViewById(R.id.guar_com_township);
        guarantorCompanyCity = view.findViewById(R.id.guar_com_city);

        serviceUnavailable = view.findViewById(R.id.service_unavailable_guarantor);
        serviceUnavailable.setVisibility(View.GONE);

        spinnerDivCode = view.findViewById(R.id.guarantor_div_code);
        autoCompleteTwspCode = view.findViewById(R.id.guarantor_twsp_code);
        spinnerType = view.findViewById(R.id.guarantor_nrc_type);
        spinnerRelation = view.findViewById(R.id.spinner_relation_with);
        spnResidentType = view.findViewById(R.id.guarantor_spinner_resident_type);
        spnLivingWith = view.findViewById(R.id.guarantor_spinner_living_with);

        guarMyanNational = view.findViewById(R.id.guar_radioMyanmar);
        guarOtherNational = view.findViewById(R.id.guar_radioOther);
        guarGenderMale = view.findViewById(R.id.guar_radioMale);
        guarGenderFemale = view.findViewById(R.id.guar_radioFemale);
        guarMaritalSingle = view.findViewById(R.id.guar_radioSingle);
        guarMaritalMarried = view.findViewById(R.id.guar_radioMarried);

        errGuarantorStreet = view.findViewById(R.id.guarantor_err_street);
        errGuarantorCity = view.findViewById(R.id.guarantor_err_city);
        errGuarantorTownship = view.findViewById(R.id.guarantor_err_township);
        errGuarantorComStreet = view.findViewById(R.id.guarantor_err_com_street);
        errGuarantorComCity = view.findViewById(R.id.guarantor_err_com_city);
        errGuarantorComTownship = view.findViewById(R.id.guarantor_err_com_township);
        errGuarantorQuarter = view.findViewById(R.id.guarantor_err_quarter);
        errGuarantorComQuarter = view.findViewById(R.id.guarantor_err_com_quarter);

        errGuarantorStreetLocale = R.string.da_mesg_blank;
        errGuarantorQuarterLocale = R.string.da_mesg_blank;
        errGuarantorComQuarterLocale = R.string.da_mesg_blank;
        errGuarantorCityLocale = R.string.da_mesg_blank;
        errGuarantorTownshipLocale = R.string.da_mesg_blank;
        errGuarantorComStreetLocale = R.string.da_mesg_blank;
        errGuarantorComCityLocale = R.string.da_mesg_blank;
        errGuarantorComTownshipLocale = R.string.da_mesg_blank;

        cityTownshipList = PreferencesManager.getCityListInfo(getActivity());
        setUpCityList(cityTownshipList);
        curCityName = cityList.get(0);
        setUpCurrentTownshipList(cityTownshipList,curCityName);

        // Current City
        ArrayAdapter<String> guaCurrentCity = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, cityList);
        guaCurrentCity.setDropDownViewResource(R.layout.dialog_nrc_division);
        guaCurrentCity.setNotifyOnChange(true);
        currentGuarantorCity.setThreshold(1);
        currentGuarantorCity.setAdapter(guaCurrentCity);
        currentGuarantorCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentGuarantorCity.setText((String) parent.getAdapter().getItem(position));
                currentCity = cityId.get(position);
                curCityName = (String)parent.getAdapter().getItem(position);
                setUpCurrentTownshipList(cityTownshipList,curCityName);
                currentGuarantorTownship.setText(BLANK);
                currentGuarantorTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));

                // Current Township
                final ArrayAdapter<String> guaCurrentTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                guaCurrentTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                guaCurrentTownship.setNotifyOnChange(true);
                currentGuarantorTownship.setThreshold(1);
                currentGuarantorTownship.setAdapter(guaCurrentTownship);

            }
        });

        currentGuarantorCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currentGuarantorCity.showDropDown();
                    currentGuarantorCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!cityList.contains(currentGuarantorCity.getText().toString())) {
                        currentGuarantorCity.setText(BLANK);
                        currentGuarantorCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        currentGuarantorCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGuarantorCity.showDropDown();
            }
        });

        currentGuarantorTownship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentGuarantorTownship.setText((String) parent.getAdapter().getItem(position));
                currentTownship = townshipId.get(position);
            }
        });

        currentGuarantorTownship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currentGuarantorTownship.showDropDown();
                    currentGuarantorTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    if (!townshipList.contains(currentGuarantorTownship.getText().toString())) {
                        currentGuarantorTownship.setText(BLANK);
                        currentGuarantorTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        currentGuarantorTownship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGuarantorTownship.showDropDown();
            }
        });



        curStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus){
                    curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                }else{
                    currentStreetCheck = curStreet.getText().toString();
                    if(isEmptyOrNull(currentStreetCheck)){
                        curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        perCityName = cityList.get(0);
        setUpPermanentTownshipList(cityTownshipList,perCityName);

        // Company City
        ArrayAdapter<String> guarCompanyCity = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, cityList);
        guarCompanyCity.setDropDownViewResource(R.layout.dialog_nrc_division);
        guarCompanyCity.setNotifyOnChange(true);
        guarantorCompanyCity.setThreshold(1);
        guarantorCompanyCity.setAdapter(guarCompanyCity);

        guarantorCompanyCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                guarantorCompanyCity.setText((String) parent.getAdapter().getItem(position));
                currentCompanyCity = cityId.get(position);
                perCityName = (String)parent.getAdapter().getItem(position);
                setUpPermanentTownshipList(cityTownshipList,perCityName);
                guarantorCompanyTownship.setText(BLANK);
                guarantorCompanyTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));

                // Company Township
                final ArrayAdapter<String> guarCompanyTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, perTownshipList);
                guarCompanyTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                guarCompanyTownship.setNotifyOnChange(true);
                guarantorCompanyTownship.setThreshold(1);
                guarantorCompanyTownship.setAdapter(guarCompanyTownship);

            }
        });

        guarantorCompanyCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    guarantorCompanyCity.showDropDown();
                    guarantorCompanyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!cityList.contains(guarantorCompanyCity.getText().toString())) {
                        guarantorCompanyCity.setText(BLANK);
                        guarantorCompanyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        guarantorCompanyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guarantorCompanyCity.showDropDown();
            }
        });

        //--------

        guarantorCompanyTownship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                guarantorCompanyTownship.setText((String) parent.getAdapter().getItem(position));
                currentCompanyTownship = perTownshipId.get(position);
            }
        });

        guarantorCompanyTownship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    guarantorCompanyTownship.showDropDown();
                    guarantorCompanyTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!perTownshipList.contains(guarantorCompanyTownship.getText().toString())) {
                        guarantorCompanyTownship.setText(BLANK);
                        guarantorCompanyTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        guarantorCompanyTownship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guarantorCompanyTownship.showDropDown();
            }
        });

        perStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus){
                    perStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                }else{
                    companyStreetCheck = curStreet.getText().toString();
                    if(isEmptyOrNull(companyStreetCheck)){
                        perStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        final String[] nrcType = getResources().getStringArray(R.array.nrc_type);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getActivity(), R.layout.da_nrc_spinner_item_1, nrcType);
        spinnerType.setAdapter(adapterType);

        final String[] guarantorRealation = getResources().getStringArray(R.array.guarantor_relation);
        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(getActivity(), R.layout.relation_spinner, guarantorRealation);
        spinnerRelation.setAdapter(adapterRelation);

        final String[] guarantorResident = getResources().getStringArray(R.array.applicant_resident_type);
        ArrayAdapter<String> adapterResident = new ArrayAdapter<String>(getActivity(), R.layout.relation_spinner, guarantorResident);
        spnResidentType.setAdapter(adapterResident);

        final String[] guarantorLivingWith = getResources().getStringArray(R.array.guarantor_livingWith);
        ArrayAdapter<String> adapterLivingWith = new ArrayAdapter<String>(getActivity(), R.layout.relation_spinner, guarantorLivingWith);
        spnLivingWith.setAdapter(adapterLivingWith);

        btnNext = view.findViewById(R.id.btn_guarantor_next);

        labelName = view.findViewById(R.id.guarantor_text_name);
        labelDob = view.findViewById(R.id.guarantor_text_dob);
        labelNrc = view.findViewById(R.id.guarantor_text_nrc);
        labelNationality = view.findViewById(R.id.guarantor_nationality);
        labelMobileNo = view.findViewById(R.id.guarantor_text_mobile_no);
        labelResidentTelNo = view.findViewById(R.id.guar_resident_mobile);
        labelRelationship = view.findViewById(R.id.text_relation_with);
        labelResidentType = view.findViewById(R.id.guar_resident_type);
        labelLivingWith = view.findViewById(R.id.guar_living_with);
        labelGender = view.findViewById(R.id.guarandor_gender);
        labelMarital = view.findViewById(R.id.guar_marital_status);
        labelStayTime = view.findViewById(R.id.guarantor_stay);
        labelCompanyName = view.findViewById(R.id.guar_company_name);
        labelCompanyTel = view.findViewById(R.id.guar_company_Tel);
        labelDepartment = view.findViewById(R.id.guar_department);
        labelPositon = view.findViewById(R.id.guar_position);
        labelService = view.findViewById(R.id.guar_service);
        labelMonthlyIncome = view.findViewById(R.id.guar_monthly_income);
        labelTotalIncome = view.findViewById(R.id.guar_total_income);
        labelStayYear = view.findViewById(R.id.lbl_guar_stay_year);
        labelStayMonth = view.findViewById(R.id.lbl_guar_stay_month);
        labelServiceYear = view.findViewById(R.id.lbl_guar_service_year);
        labelServiceMonth = view.findViewById(R.id.lbl_guar_service_month);

        errName = view.findViewById(R.id.guarantor_err_name);
        errNrc = view.findViewById(R.id.guarantor_err_nrc);
        errNationalityDetail = view.findViewById(R.id.guarantor_err_nationality);
        errMobileNo = view.findViewById(R.id.guarantor_err_mobile);
        errRelationshipDetail = view.findViewById(R.id.guarantor_err_relation_detail);
        errResidentTypeDetail = view.findViewById(R.id.guarantor_err_residentType);
        errCompanyName = view.findViewById(R.id.guarantor_err_company_name);
        errCompanyTel = view.findViewById(R.id.guarantor_err_companyTel);
        errDepartment = view.findViewById(R.id.guarantor_err_department);
        errPositon = view.findViewById(R.id.guarantor_err_position);
        errDob = view.findViewById(R.id.guarantor_err_dob);
        errMonthlyIncome = view.findViewById(R.id.guarantor_err_basic_inocme);
        errService = view.findViewById(R.id.guarantor_err_service_year);
        errStayTime = view.findViewById(R.id.guarantor_err_stay_year);
        errResidentTel = view.findViewById(R.id.guarantor_err_resident_tel);

        errNameLocale = R.string.da_mesg_blank;
        errResidentTelLocale = R.string.da_mesg_blank;
        errNrcLocale = R.string.da_mesg_blank;
        errNationalityDetailLocale = R.string.da_mesg_blank;
        errMobileNoLocale = R.string.da_mesg_blank;
        errRelationshipDetailLocale = R.string.da_mesg_blank;
        errResidentTypeDetailLocale = R.string.da_mesg_blank;
        errCompanyNameLocale = R.string.da_mesg_blank;
        errCompanyTelLocale = R.string.da_mesg_blank;
        errDepartmentLocale = R.string.da_mesg_blank;
        errPositonLocale = R.string.da_mesg_blank;
        errDobLocale = R.string.da_mesg_blank;
        errMonthlyIncomeLocale = R.string.da_mesg_blank;
        errServiceLocale = R.string.da_mesg_blank;
        errStayTimeLocale = R.string.da_mesg_blank;

        guar_name = view.findViewById(R.id.guarantor_input_name);
        guarantorDob = view.findViewById(R.id.guarantor_input_dob);
        guar_nrc = view.findViewById(R.id.guarantor_nrc_no);
        guar_nationalityDetail = view.findViewById(R.id.guarantor_nationality_detail);
        guar_mobileNo = view.findViewById(R.id.guarantor_input_mobile);
        guar_residentTelNo = view.findViewById(R.id.guarantor_residentTel);
        guar_relaitonshipWith = view.findViewById(R.id.guarantor_relation_detail);
        guar_residentTypeDetail = view.findViewById(R.id.guarantor_resident_detail);
        guar_companyTel = view.findViewById(R.id.guarantor_company_Tel);
        guar_department = view.findViewById(R.id.guarantor_department);
        guar_position = view.findViewById(R.id.guarantor_position);
        guar_monthlyIncome = view.findViewById(R.id.guarantor_basic_income);
        totalIncomeFilled = view.findViewById(R.id.guarantor_total_income);

        radioNationality = view.findViewById(R.id.guar_radioNationality);
        radioGender = view.findViewById(R.id.guar_radioGender);
        radioMaritalSataus = view.findViewById(R.id.guar_radioMariage);

        guarStayYear = view.findViewById(R.id.guar_spinner_stay_year);
        guarStayMonth = view.findViewById(R.id.guar_spinner_stay_month);
        guarServiceYear = view.findViewById(R.id.guar_spinner_service_year);
        guarServiceMonth = view.findViewById(R.id.guar_spinner_service_month);

        guar_name.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(256)});
        guar_companyName.setFilters(new InputFilter[]{ new InputFilter.AllCaps(),new InputFilter.LengthFilter(256)});
        guar_nationalityDetail.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(256)});
        guar_relaitonshipWith.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(256)});
        guar_residentTypeDetail.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(256)});
        guar_department.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(256)});
        guar_position.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(256)});
        curApartmentNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        curRoomNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        curFloorNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        curStreet.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        curQuarter.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        perApartmentNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        perRoomNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        perFloorNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        perStreet.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});
        perQuarter.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(100)});

        ScrollView scrollView=view.findViewById(R.id.guarator_scroll);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        guar_monthlyIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable view) {
                guar_monthlyIncome.removeTextChangedListener(this);
                try {

                    String originalString = view.toString();

                    if(originalString.equals(BLANK)){
                        totalIncomeFilled.setText(BLANK);
                    }else{
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }

                        Long longValue = Long.parseLong(originalString);
                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern(LOAN_AMOUNT_FORMAT);
                        String formattedString = formatter.format(longValue);

                        //setting text after format to EditText
                        totalIncomeFilled.setText(formattedString);
                        guar_monthlyIncome.setText(formattedString);
                        guar_monthlyIncome.setSelection(guar_monthlyIncome.getText().length());
                    }

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                guar_monthlyIncome.addTextChangedListener(this);
            }
        });

        final String[] yearOfStay = getResources().getStringArray(R.array.year_array);
        ArrayAdapter<String> adapterStayYear = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, yearOfStay);
        guarStayYear.setAdapter(adapterStayYear);

        final String[] monthOfStay = getResources().getStringArray(R.array.month_array);
        ArrayAdapter<String> adapterStayMonth = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, monthOfStay);
        guarStayMonth.setAdapter(adapterStayMonth);

        final String[] yearOfService = getResources().getStringArray(R.array.year_array);
        ArrayAdapter<String> adapterServiceYear = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, yearOfService);
        guarServiceYear.setAdapter(adapterServiceYear);

        final String[] monthOfService = getResources().getStringArray(R.array.month_array);
        ArrayAdapter<String> adapterServiceMonth = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, monthOfService);
        guarServiceMonth.setAdapter(adapterServiceMonth);

        guarServiceYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                serviceYear = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        guarServiceMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                serviceMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        guarStayYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                stayYear = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        guarStayMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                stayMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        radioNationality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Myanmar")) {
                    nationality = CommonConstants.MYANMAR;
                } else {
                    nationality = CommonConstants.OTHER;
                }
                displayNationalityDesc();
            }
        });

        radioMaritalSataus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Single")) {
                    maritalStatus = CommonConstants.SINGLE;
                } else {
                    maritalStatus = CommonConstants.MARRIED;
                }
            }
        });
        companyStatusCheck = guar_companyName.getText().toString();
        if (!isEmptyOrNull(companyStatusCheck)) {
            guar_companyName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_companyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_companyName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyStatusCheck = guar_companyName.getText().toString();
                    if (isEmptyOrNull(companyStatusCheck)) {
                        guar_companyName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        companyTelCheck = guar_companyTel.getText().toString();
        if (!isEmptyOrNull(companyTelCheck)) {
            guar_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_companyTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyTelCheck = guar_companyTel.getText().toString();
                    if (isEmptyOrNull(companyTelCheck)) {
                        guar_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Male")) {
                    gender = CommonConstants.MALE;
                } else {
                    gender = CommonConstants.FEMALE;
                }
            }
        });

        spinnerRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relationship = position + 1;
                displayRelationshipDesc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnResidentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                residentType = position + 1;
                displayResidentTypeDesc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnLivingWith.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                livingWith = position+1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDataOnPageChanged();
                viewPager.setCurrentItem(4, true);
            }
        });

        btnSave = view.findViewById(R.id.btn_guardata_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guarantorSaveToDatabase();
            }
        });

        /*backToEmerData = view.findViewById(R.id.back_emer_data);
        goToLoanData = view.findViewById(R.id.go_to_loan_data);*/
        guarantorTitle = view.findViewById(R.id.da_gua_data_title);
        emergencyTitle = view.findViewById(R.id.da_emer_data_title);
        loanDataTitle = view.findViewById(R.id.da_loan_data_title);

        /*backToEmerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDataOnPageChanged();
                viewPager.setCurrentItem( 2, true);
            }
        });

        goToLoanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDataOnPageChanged();
                viewPager.setCurrentItem( 4, true);
            }
        });*/

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                if(selectedPosition == 3){
                    ((MainMenuActivityDrawer)getActivity()).setLanguageListener(SmallLoanGuarantorFragment.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (selectedPosition == 4) {
                    setUpDataOnPageChanged();

                } else if (selectedPosition == 3) {
                    curLang = PreferencesManager.getCurrentLanguage(getContext());
                    changeLabel(curLang);
                    if (MainMenuActivityDrawer.isSubmitclickGuaData) {
                        MainMenuActivityDrawer.isSubmitclickGuaData = false;
                        showValidationMsg(curLang);
                    }
                }
            }

        });

        final List<TownshipCodeResDto> townshipCodeResDtoList = PreferencesManager.getTownshipCode(getActivity());
        townshipCode = townshipCodeResDtoList.get(0).getTownshipCodeList();

        final String[] stateDivCode = getResources().getStringArray(R.array.div_code);
        ArrayAdapter<String> adapterDiv = new ArrayAdapter<String>(getActivity(), R.layout.da_nrc_spinner_item_1, stateDivCode);
        spinnerDivCode.setAdapter(adapterDiv);
        stateDivCodeVal = stateDivCode[0];
        nrcTypeVal = nrcType[0];

        spinnerDivCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                stateDivCodeVal = stateDivCode[position];
                divCodePosition = position;
                for (TownshipCodeResDto townshipCodeResDto : townshipCodeResDtoList) {
                    if (String.valueOf(townshipCodeResDto.getStateId()).equals(stateDivCodeVal)) {
                        townshipCode = townshipCodeResDto.getTownshipCodeList();
                        break;
                    }
                }

                if (!isInit) {
                    if (isEmptyOrNull(autoCompleteTwspCode.getText().toString())) {
                        autoCompleteTwspCode.setText(BLANK);
                    }else if(!townshipCode.contains(autoCompleteTwspCode.getText().toString())){
                        autoCompleteTwspCode.setText(BLANK);
                    }
                }

                isInit = false;
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipCode);
                adapter2.setDropDownViewResource(R.layout.dialog_nrc_division);
                adapter2.setNotifyOnChange(true);
                autoCompleteTwspCode.setThreshold(1);
                autoCompleteTwspCode.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        autoCompleteTwspCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTwspCode.setText((String) parent.getAdapter().getItem(position));
            }
        });

        autoCompleteTwspCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!townshipCode.contains(autoCompleteTwspCode.getText().toString())) {
                        autoCompleteTwspCode.setText(BLANK);
                        autoCompleteTwspCode.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                } else {
                    autoCompleteTwspCode.showDropDown();
                    autoCompleteTwspCode.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                }
            }
        });

        autoCompleteTwspCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTwspCode.showDropDown();
            }
        });

        spinnerDivCode.setSelection(divCodePosition);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nrcTypeVal = nrcType[position];
                nrcTypePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        guarantorDob.setOnClickListener(new View.OnClickListener() {
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
                        guarantorDob.setText(sdf.format(myCalendar.getTime()));
                        guarantorDob.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                    }
                });
                myCalendar.add(Calendar.YEAR, -MIN_AGE);
                dialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.setMaxDate(myCalendar.getTimeInMillis());
                dialog.show(getChildFragmentManager(), "TAG");
            }
        });
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        replaceLastGuarantorInfo();
        //Log.e("Page create", "Gurantor fragment");

        currentQuarterCheck = curQuarter.getText().toString();
        if (!isEmptyOrNull(currentQuarterCheck)) {
            curQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyQuarterCheck = perQuarter.getText().toString();
        if (!isEmptyOrNull(companyQuarterCheck)) {
            perQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
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

        perQuarter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    perQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyQuarterCheck = perQuarter.getText().toString();
                    if (isEmptyOrNull(companyQuarterCheck)) {
                        perQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        currentStreetCheck = curStreet.getText().toString();
        if (!isEmptyOrNull(currentStreetCheck)) {
            curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentCityCheck = currentGuarantorCity.getText().toString();
        if (!isEmptyOrNull(currentCityCheck)) {
            currentGuarantorCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentTownshipCheck = currentGuarantorTownship.getText().toString();
        if (!isEmptyOrNull(currentTownshipCheck)) {
            currentGuarantorTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyStreetCheck = perStreet.getText().toString();
        if (!isEmptyOrNull(companyStreetCheck)) {
            perStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyCityCheck = guarantorCompanyCity.getText().toString();
        if (!isEmptyOrNull(companyCityCheck)) {
            guarantorCompanyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyTownshipCheck = guarantorCompanyTownship.getText().toString();
        if (!isEmptyOrNull(companyTownshipCheck)) {
            guarantorCompanyTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        nameCheck = guar_name.getText().toString();
        if (!isEmptyOrNull(nameCheck)) {
            guar_name.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_name.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    nameCheck = guar_name.getText().toString();
                    if (isEmptyOrNull(nameCheck)) {
                        guar_name.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        dobCheck = guarantorDob.getText().toString();
        if (!isEmptyOrNull(dobCheck)) {
            guarantorDob.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guarantorDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guarantorDob.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    dobCheck = guarantorDob.getText().toString();
                    if (isEmptyOrNull(dobCheck)) {
                        guarantorDob.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        nrcTownShipCheck = autoCompleteTwspCode.getText().toString();
        if (!isEmptyOrNull(nrcTownShipCheck)) {
            autoCompleteTwspCode.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        nrcNoCheck = guar_nrc.getText().toString();
        if (!isEmptyOrNull(nrcNoCheck)) {
            guar_nrc.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_nrc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_nrc.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    nrcNoCheck = guar_nrc.getText().toString();
                    if (isEmptyOrNull(nrcNoCheck)) {
                        guar_nrc.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        mobileNoCheck = guar_mobileNo.getText().toString();
        if (!isEmptyOrNull(mobileNoCheck)) {
            guar_mobileNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_mobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_mobileNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    mobileNoCheck = guar_mobileNo.getText().toString();
                    if (isEmptyOrNull(mobileNoCheck)) {
                        guar_mobileNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        relationshipDetailCheck = guar_relaitonshipWith.getText().toString();
        if (!isEmptyOrNull(relationshipDetailCheck)) {
            guar_relaitonshipWith.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_relaitonshipWith.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_relaitonshipWith.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    relationshipDetailCheck = guar_relaitonshipWith.getText().toString();
                    if (isEmptyOrNull(relationshipDetailCheck)) {
                        guar_relaitonshipWith.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        residentDetailCheck = guar_residentTypeDetail.getText().toString();
        if (!isEmptyOrNull(residentDetailCheck)) {
            guar_residentTypeDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_residentTypeDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_residentTypeDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    residentDetailCheck = guar_residentTypeDetail.getText().toString();
                    if (isEmptyOrNull(residentDetailCheck)) {
                        guar_residentTypeDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        companyDepartmentCheck = guar_department.getText().toString();
        if (!isEmptyOrNull(companyDepartmentCheck)) {
            guar_department.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_department.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_department.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyDepartmentCheck = guar_department.getText().toString();
                    if (isEmptyOrNull(companyDepartmentCheck)) {
                        guar_department.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        companyPositionCheck = guar_position.getText().toString();
        if (!isEmptyOrNull(companyPositionCheck)) {
            guar_position.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_position.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_position.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyPositionCheck = guar_position.getText().toString();
                    if (isEmptyOrNull(companyPositionCheck)) {
                        guar_position.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        monthlyIncomeCheck = guar_monthlyIncome.getText().toString();
        if (!isEmptyOrNull(monthlyIncomeCheck)) {
            guar_monthlyIncome.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_monthlyIncome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_monthlyIncome.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    monthlyIncomeCheck = guar_monthlyIncome.getText().toString();
                    if (isEmptyOrNull(monthlyIncomeCheck)) {
                        guar_monthlyIncome.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                        totalIncomeFilled.setText(BLANK);
                    } else {
                        totalIncomeFilled.setText(monthlyIncomeCheck);
                    }
                }
            }
        });

        nationalityDetailCheck = guar_nationalityDetail.getText().toString();
        if (!isEmptyOrNull(nationalityDetailCheck)) {
            guar_nationalityDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        guar_nationalityDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    guar_nationalityDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    nationalityDetailCheck = guar_nationalityDetail.getText().toString();
                    if (isEmptyOrNull(nationalityDetailCheck)) {
                        guar_nationalityDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        Log.e("Township_1 : ", String.valueOf(perTownshipList.size()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //replaceLastGuarantorInfo();
        //Log.e("Resume", "=================Guarantor Fragment");
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

    private void guarantorSaveToDatabase() {

        setUpGuarantorFormData();

        ApplicationRegisterSaveReqBean saveDataBean = new ApplicationRegisterSaveReqBean();

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            saveDataBean = PreferencesManager.getDaftSavedInfo(getActivity());
        }

        saveDataBean.setDaApplicationTypeId(APPLICATION_PLOAN);
        saveDataBean.setCustomerId(Integer.parseInt(customerId));
        saveDataBean.setChannelType(CHANNEL_MOBILE);

        GuarantorFormBean guarantorData = new GuarantorFormBean();
        guarantorData.setName(name);
        Log.e("Guarantor Name Print", name);
        guarantorData.setDob(dateToString2(dob));
        guarantorData.setNrcNo(nrc);
        guarantorData.setNationalityOther(nationalityDetail);
        guarantorData.setMobileNo(mobileNo);
        guarantorData.setResidentTelNo(residentMobile);
        guarantorData.setTypeOfResidenceOther(residentTypeDetail);
        guarantorData.setRelationshipOther(relationshipDetail);
        guarantorData.setCompanyName(companyName);
        guarantorData.setCompanyTelNo(companyTel);
        guarantorData.setDepartment(department);
        guarantorData.setPosition(position);

        guarantorData.setYearOfStayYear(stayYear);
        guarantorData.setYearOfStayMonth(stayMonth);
        guarantorData.setYearOfServiceYear(serviceYear);
        guarantorData.setYearOfServiceMonth(serviceMonth);
        guarantorData.setMonthlyBasicIncome(monthlyIncome);
        guarantorData.setTotalIncome(totalIncome);

        guarantorData.setGender(gender);
        guarantorData.setRelationship(relationship);
        guarantorData.setNationality(nationality);
        guarantorData.setTypeOfResidence(residentType);
        guarantorData.setLivingWith(livingWith);
        guarantorData.setMaritalStatus(maritalStatus);
        guarantorData.setLivingWithOther(livingWtihDetail);

        guarantorData.setCurrentAddressBuildingNo(currentApartmentNo);
        guarantorData.setCurrentAddressRoomNo(currentRoomNo);
        guarantorData.setCurrentAddressFloor(currentFloorNo);
        guarantorData.setCurrentAddressStreet(currentStreet);
        guarantorData.setCurrentAddressQtr(currentQuarter);

        int city_id = getCityId(cityId, cityList,guarantorCity);
        int town_id = getTownshipId(cityTownshipList, city_id, guarantorTownship);

        guarantorData.setCurrentAddressCity(city_id);
        guarantorData.setCurrentAddressTownship(town_id);

        guarantorData.setCompanyAddressBuildingNo(permanentApartmentNo);
        guarantorData.setCompanyAddressRoomNo(permanentRoomNo);
        guarantorData.setCompanyAddressFloor(permanentFloorNo);
        guarantorData.setCompanyAddressStreet(permanentStreet);
        guarantorData.setCompanyAddressQtr(permanentQuarter);

        int com_city_id = getCityId(cityId, cityList,appCompanyCity);
        int com_town_id = getTownshipId(cityTownshipList, com_city_id, appCompanyTownship);

        guarantorData.setCompanyAddressCity(com_city_id);
        guarantorData.setCompanyAddressTownship(com_town_id);

        saveDataBean.setGuarantorInfoDto(guarantorData);

        if (saveDataBean.isBeanValid()) {

            if (!CommonUtils.isNetworkAvailable(getActivity())) {
                showNetworkErrorDialog(getActivity(), getNetErrMsg());
            } else {

                final ProgressDialog saveDialog = new ProgressDialog(getActivity());
                saveDialog.setMessage("Saving Data...");
                saveDialog.setCancelable(false);
                saveDialog.show();

                Service saveRegisterData = APIClient.getApplicationRegisterService();
                Call<BaseResponse<ApplicationRegisterSaveReqBean>> req = saveRegisterData.saveRegisterData(PreferencesManager.getAccessToken(getActivity()), saveDataBean);

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
                                    showSnackBarMessage("Guarantor Info. saved.");

                                } else {
                                    saveDialog.dismiss();
                                    showSnackBarMessage("Guarantor Info. cannot be saved.");
                                }
                            } else {
                                saveDialog.dismiss();
                                showSnackBarMessage("Guarantor Info. cannot be saved.");
                            }
                        } else {
                            closeDialog(saveDialog);
                            showSnackBarMessage("Guarantor Info. cannot be saved.");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<ApplicationRegisterSaveReqBean>> call, Throwable t) {
                        closeDialog(saveDialog);
                        showSnackBarMessage("Guarantor Info. save failed.");
                    }
                });
            }

        } else {
            showSnackBarMessage("Guarantor Info. is required to save.");
        }
    }

    private void replaceLastGuarantorInfo() {

        // replace error message
        Log.e("replace Guarantor Data", PreferencesManager.isDaftSavedErrExisted(getActivity()) + "");

        if (PreferencesManager.isDaftSavedErrExisted(getActivity())) {
            ApplicationFormErrMesgBean savedInformation
                    = PreferencesManager.getErrMesgInfo(getContext());

            errNameLocale = savedInformation.getGuaName();
            errResidentTelLocale = savedInformation.getGuaResidentTelNo();
            errNrcLocale = savedInformation.getGuaNrc();
            errDobLocale = savedInformation.getGuaDob();
            errMobileNoLocale = savedInformation.getGuaMobileNo();
            errResidentTypeDetailLocale = savedInformation.getGuaTypeOfResident();
            errRelationshipDetailLocale = savedInformation.getGuaRelationshipLocale();
            errStayTimeLocale = savedInformation.getGuaYearOfStay();
            errCompanyNameLocale = savedInformation.getGuaCompanyName();
            errCompanyTelLocale = savedInformation.getGuaCompanyTelNo();
            errDepartmentLocale = savedInformation.getGuaCompanyDepartment();
            errPositonLocale = savedInformation.getGuaCompanyPosition();
            errServiceLocale = savedInformation.getGuaServiceYear();
            errMonthlyIncomeLocale = savedInformation.getGuaMonthlyIncome();

            errGuarantorStreetLocale = savedInformation.getGuaStreet();
            errGuarantorCityLocale = savedInformation.getGuaCity();
            errGuarantorTownshipLocale = savedInformation.getGuaTownship();
            errGuarantorComStreetLocale = savedInformation.getGuaComStreet();
            errGuarantorComCityLocale = savedInformation.getGuaComCity();
            errGuarantorComTownshipLocale = savedInformation.getGuaComTownship();
            errGuarantorQuarterLocale = savedInformation.getGuaQuarter();
            errGuarantorComQuarterLocale = savedInformation.getGuaComQuarter();

            if (errGuarantorStreetLocale != R.string.da_mesg_blank) {
                errGuarantorStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorStreetLocale, getContext()));
                errGuarantorStreet.setVisibility(View.VISIBLE);
            }
            if (errGuarantorQuarterLocale != R.string.da_mesg_blank) {
                errGuarantorQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorQuarterLocale, getContext()));
                errGuarantorQuarter.setVisibility(View.VISIBLE);
            }
            if (errGuarantorComQuarterLocale != R.string.da_mesg_blank) {
                errGuarantorComQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorComQuarterLocale, getContext()));
                errGuarantorComQuarter.setVisibility(View.VISIBLE);
            }
            if (errGuarantorCityLocale != R.string.da_mesg_blank) {
                errGuarantorCity.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorCityLocale, getContext()));
                errGuarantorCity.setVisibility(View.VISIBLE);
            }
            if (errGuarantorTownshipLocale != R.string.da_mesg_blank) {
                errGuarantorTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorTownshipLocale, getContext()));
                errGuarantorTownship.setVisibility(View.VISIBLE);
            }
            if (errGuarantorComStreetLocale != R.string.da_mesg_blank) {
                errGuarantorComStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorComStreetLocale, getContext()));
                errGuarantorComStreet.setVisibility(View.VISIBLE);
            }
            if (errGuarantorComCityLocale != R.string.da_mesg_blank) {
                errGuarantorComCity.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorComCityLocale, getContext()));
                errGuarantorComCity.setVisibility(View.VISIBLE);
            }
            if (errGuarantorComTownshipLocale != R.string.da_mesg_blank) {
                errGuarantorComTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorComTownshipLocale, getContext()));
                errGuarantorComTownship.setVisibility(View.VISIBLE);
            }

            if (errResidentTelLocale != R.string.da_mesg_blank) {
                errResidentTel.setText(CommonUtils.getLocaleString(new Locale(curLang), errResidentTelLocale, getContext()));
                errResidentTel.setVisibility(View.VISIBLE);
            }

            if (errNameLocale != R.string.da_mesg_blank) {
                errName.setText(CommonUtils.getLocaleString(new Locale(curLang), errNameLocale, getContext()));
                errName.setVisibility(View.VISIBLE);
            }

            if (errNrcLocale != R.string.da_mesg_blank) {
                errNrc.setText(CommonUtils.getLocaleString(new Locale(curLang), errNrcLocale, getContext()));
                errNrc.setVisibility(View.VISIBLE);
            }

            if (errDobLocale != R.string.da_mesg_blank) {
                errDob.setText(CommonUtils.getLocaleString(new Locale(curLang), errDobLocale, getContext()));
                errDob.setVisibility(View.VISIBLE);
            }
            if (errMobileNoLocale != R.string.da_mesg_blank) {
                errMobileNo.setText(CommonUtils.getLocaleString(new Locale(curLang), errMobileNoLocale, getContext()));
                errMobileNo.setVisibility(View.VISIBLE);
            }

            if (errCompanyTelLocale != R.string.da_mesg_blank) {
                errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyTelLocale, getContext()));
                errCompanyTel.setVisibility(View.VISIBLE);
            }

            if (errResidentTypeDetailLocale != R.string.da_mesg_blank) {
                errResidentTypeDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), errResidentTypeDetailLocale, getContext()));
                errResidentTypeDetail.setVisibility(View.VISIBLE);
            }

            if (errRelationshipDetailLocale != R.string.da_mesg_blank) {
                errRelationshipDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), errRelationshipDetailLocale, getContext()));
                errRelationshipDetail.setVisibility(View.VISIBLE);
            }

            if (errStayTimeLocale != R.string.da_mesg_blank) {
                errStayTime.setText(CommonUtils.getLocaleString(new Locale(curLang), errStayTimeLocale, getContext()));
                errStayTime.setVisibility(View.VISIBLE);
            }

            if (errCompanyNameLocale != R.string.da_mesg_blank) {
                errCompanyName.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyNameLocale, getContext()));
                errCompanyName.setVisibility(View.VISIBLE);
            }

            if (errDepartmentLocale != R.string.da_mesg_blank) {
                errDepartment.setText(CommonUtils.getLocaleString(new Locale(curLang), errDepartmentLocale, getContext()));
                errDepartment.setVisibility(View.VISIBLE);
            }

            if (errPositonLocale != R.string.da_mesg_blank) {
                errPositon.setText(CommonUtils.getLocaleString(new Locale(curLang), errPositonLocale, getContext()));
                errPositon.setVisibility(View.VISIBLE);
            }

            if (errServiceLocale != R.string.da_mesg_blank) {
                errService.setText(CommonUtils.getLocaleString(new Locale(curLang), errServiceLocale, getContext()));
                errService.setVisibility(View.VISIBLE);
            }

            if (errMonthlyIncomeLocale != R.string.da_mesg_blank) {
                errMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(curLang), errMonthlyIncomeLocale, getContext()));
                errMonthlyIncome.setVisibility(View.VISIBLE);
            }
        }

        // saved form bean
        Log.e("Replace Gua-Name", String.valueOf(PreferencesManager.isDaftSavedInfoExisted(getActivity())));
        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {

            ApplicationRegisterSaveReqBean savedInformation
                    = PreferencesManager.getDaftSavedInfo(getActivity());

            DecimalFormat df = new DecimalFormat("###.#");
            DecimalFormat comma = new DecimalFormat("#,###,###,###");

            try {

                GuarantorFormBean guarantorDataBean = savedInformation.getGuarantorInfoDto();
                if(guarantorDataBean.getNrcNo()!=BLANK){
                    NrcBean nrc = getNrcFromString(guarantorDataBean.getNrcNo(), getActivity());
                    setUpNrcInfo(nrc);
                }

                guar_name.setText(guarantorDataBean.getName());
                guarantorDob.setText(CommonUtils.stringToYMDDateFormatStr(guarantorDataBean.getDob()));
                guar_nationalityDetail.setText(guarantorDataBean.getNationalityOther());
                guar_mobileNo.setText(guarantorDataBean.getMobileNo());
                guar_residentTelNo.setText(guarantorDataBean.getResidentTelNo());
                guar_relaitonshipWith.setText(guarantorDataBean.getRelationshipOther());
                guar_residentTypeDetail.setText(guarantorDataBean.getTypeOfResidenceOther());
                guar_companyTel.setText(guarantorDataBean.getCompanyTelNo());
                guar_department.setText(guarantorDataBean.getDepartment());
                guar_position.setText(guarantorDataBean.getPosition());
                guar_companyName.setText(guarantorDataBean.getCompanyName());

                guarStayYear.setSelection(guarantorDataBean.getYearOfStayYear());
                guarStayMonth.setSelection(guarantorDataBean.getYearOfStayMonth());
                guarServiceYear.setSelection(guarantorDataBean.getYearOfServiceYear());
                guarServiceMonth.setSelection(guarantorDataBean.getYearOfServiceMonth());
                guar_monthlyIncome.setText(comma.format(guarantorDataBean.getMonthlyBasicIncome()));
                totalIncomeFilled.setText(comma.format(guarantorDataBean.getTotalIncome()));

                curApartmentNo.setText(guarantorDataBean.getCurrentAddressBuildingNo());
                curRoomNo.setText(guarantorDataBean.getCurrentAddressRoomNo());
                curFloorNo.setText(guarantorDataBean.getCurrentAddressFloor());
                curStreet.setText(guarantorDataBean.getCurrentAddressStreet());
                curQuarter.setText(guarantorDataBean.getCurrentAddressQtr());
                if (guarantorDataBean.getCurrentAddressCity()==0){
                    currentGuarantorCity.setText("");
                }else {
                    currentGuarantorCity.setText(getCity(cityId, cityList, guarantorDataBean.getCurrentAddressCity()));
                    this.setSelectedTownshipList(guarantorDataBean.getCurrentAddressCity().intValue(), CURRENT);

                    setUpCurrentTownshipList(cityTownshipList,currentGuarantorCity.getText().toString());
                    final ArrayAdapter<String> guaCurrentTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                    guaCurrentTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                    guaCurrentTownship.setNotifyOnChange(true);
                    currentGuarantorTownship.setThreshold(1);
                    currentGuarantorTownship.setAdapter(guaCurrentTownship);
                }
                if (guarantorDataBean.getCurrentAddressTownship()==0){
                    currentGuarantorTownship.setText("");
                }else {
                    currentGuarantorTownship.setText(getTownship(cityTownshipList, guarantorDataBean.getCurrentAddressCity(), guarantorDataBean.getCurrentAddressTownship(),CURRENT));
                }

                perApartmentNo.setText(guarantorDataBean.getCompanyAddressBuildingNo());
                perRoomNo.setText(guarantorDataBean.getCompanyAddressRoomNo());
                perFloorNo.setText(guarantorDataBean.getCompanyAddressFloor());
                perStreet.setText(guarantorDataBean.getCompanyAddressStreet());
                perQuarter.setText(guarantorDataBean.getCompanyAddressQtr());
                if (guarantorDataBean.getCompanyAddressCity()==0){
                    guarantorCompanyCity.setText("");
                    this.setSelectedTownshipList(guarantorDataBean.getCompanyAddressCity().intValue(), COMPANY);
                }else {
                    guarantorCompanyCity.setText(getCity(cityId, cityList, guarantorDataBean.getCompanyAddressCity()));
                    // bing township list
                    setUpPermanentTownshipList(cityTownshipList, guarantorCompanyCity.getText().toString());
                    final ArrayAdapter<String> guaPermanentTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, perTownshipList);
                    guaPermanentTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                    guaPermanentTownship.setNotifyOnChange(true);
                    currentGuarantorTownship.setThreshold(1);
                    currentGuarantorTownship.setAdapter(guaPermanentTownship);
                    Log.e("Township : ", String.valueOf(perTownshipList.size()));
                }
                if (guarantorDataBean.getCompanyAddressTownship()==0){
                    guarantorCompanyTownship.setText("");
                }else {
                    guarantorCompanyTownship.setText(getTownship(cityTownshipList, guarantorDataBean.getCompanyAddressCity(), guarantorDataBean.getCompanyAddressTownship(),COMPANY));
                }

                if (nationality == 1) {
                    guarMyanNational.setChecked(true);
                } else {
                    guarOtherNational.setChecked(true);
                }

                if (gender == 1) {
                    guarGenderMale.setChecked(true);
                } else {
                    guarGenderFemale.setChecked(true);
                }

                if (maritalStatus == 1) {
                    guarMaritalSingle.setChecked(true);
                } else {
                    guarMaritalMarried.setChecked(true);
                }

                spinnerRelation.setSelection(guarantorDataBean.getRelationship() - 1);
                spnResidentType.setSelection(guarantorDataBean.getTypeOfResidence() - 1);
                spnLivingWith.setSelection(guarantorDataBean.getLivingWith() - 1);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void showValidationMsg(String curLang) {

        //Log.e("show", "message");
        setUpGuarantorFormData();

        ApplicationFormErrMesgBean errMesgBean = PreferencesManager.getErrMesgInfo(getContext());

        if (errMesgBean == null) {
            errMesgBean = new ApplicationFormErrMesgBean();
        }
        /*Name*/
        if (CommonUtils.isEmptyOrNull(name)) {
            errName.setVisibility(View.VISIBLE);
            errName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_name_err, getActivity()));
            errNameLocale = R.string.register_name_err;
            errMesgBean.setGuaName(errNameLocale);

        }else {
            errName.setVisibility(View.GONE);
            errNameLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaName(errNameLocale);
        }

        if(!CommonUtils.isEmptyOrNull(residentMobile)){
            if(!CommonUtils.isNumberValid(residentMobile)){
                errResidentTel.setVisibility(View.VISIBLE);
                errResidentTel.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_residentTelNo_format_err, getActivity()));
                errResidentTelLocale = R.string.da_otherPhoneNo_format_err;
                errMesgBean.setGuaResidentTelNo(errResidentTelLocale);

            } else {
                errResidentTel.setVisibility(View.GONE);
                errResidentTelLocale = R.string.da_mesg_blank;
                errMesgBean.setGuaResidentTelNo(errResidentTelLocale);
            }
        } else {
            errResidentTel.setVisibility(View.GONE);
            errResidentTelLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaResidentTelNo(errResidentTelLocale);
        }

        /*Current Quarter*/
        if (CommonUtils.isEmptyOrNull(currentQuarter)) {
            errGuarantorQuarter.setVisibility(View.VISIBLE);
            errGuarantorQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_quarter, getActivity()));
            errGuarantorQuarterLocale = R.string.da_err_quarter;
            errMesgBean.setGuaQuarter(errGuarantorQuarterLocale);
        }else {
            errGuarantorQuarter.setVisibility(View.GONE);
            errGuarantorQuarterLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaQuarter(errGuarantorQuarterLocale);
        }

        /*Company Quarter*/
        if (CommonUtils.isEmptyOrNull(permanentQuarter)) {
            errGuarantorComQuarter.setVisibility(View.VISIBLE);
            errGuarantorComQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_quarter, getActivity()));
            errGuarantorComQuarterLocale = R.string.da_err_quarter;
            errMesgBean.setGuaComQuarter(errGuarantorComQuarterLocale);
        }else {
            errGuarantorComQuarter.setVisibility(View.GONE);
            errGuarantorComQuarterLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaComQuarter(errGuarantorComQuarterLocale);
        }

        /*Date of Birth*/
        if (CommonUtils.isEmptyOrNull(dob)) {
            errDob.setVisibility(View.VISIBLE);
            errDob.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_dob_err, getActivity()));
            errDobLocale = R.string.register_dob_err;
            errMesgBean.setGuaDob(errDobLocale);

        } else {
            errDob.setVisibility(View.GONE);
            errDobLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaDob(errDobLocale);
        }

        /*currentStreet = curStreet.getText().toString();
        permanentStreet = perStreet.getText().toString();
        guarantorCity = currentGuarantorCity.getText().toString();
        guarantorTownship = currentGuarantorTownship.getText().toString();
        appCompanyCity = guarantorCompanyCity.getText().toString();
        appCompanyTownship = guarantorCompanyTownship.getText().toString();*/

        /*guarantor street*/
        if (CommonUtils.isEmptyOrNull(currentStreet)) {
            errGuarantorStreet.setVisibility(View.VISIBLE);
            errGuarantorStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_street, getActivity()));
            errGuarantorStreetLocale = R.string.da_err_street;
            errMesgBean.setGuaStreet(errGuarantorStreetLocale);

        } else {
            errGuarantorStreet.setVisibility(View.GONE);
            errGuarantorStreetLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaStreet(errGuarantorStreetLocale);
        }

        /*guarantor city*/
        if (CommonUtils.isEmptyOrNull(guarantorCity)) {
            errGuarantorCity.setVisibility(View.VISIBLE);
            errGuarantorCity.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_city, getActivity()));
            errGuarantorCityLocale = R.string.da_err_city;
            errMesgBean.setGuaCity(errGuarantorCityLocale);

        } else {
            errGuarantorCity.setVisibility(View.GONE);
            errGuarantorCityLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaCity(errGuarantorCityLocale);
        }

        /*guarantor township*/
        if (CommonUtils.isEmptyOrNull(guarantorTownship)) {
            errGuarantorTownship.setVisibility(View.VISIBLE);
            errGuarantorTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_township, getActivity()));
            errGuarantorTownshipLocale = R.string.da_err_township;
            errMesgBean.setGuaTownship(errGuarantorTownshipLocale);

        } else {
            errGuarantorTownship.setVisibility(View.GONE);
            errGuarantorTownshipLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaTownship(errGuarantorTownshipLocale);
        }

        /*company street*/
        if (CommonUtils.isEmptyOrNull(permanentStreet)) {
            errGuarantorComStreet.setVisibility(View.VISIBLE);
            errGuarantorComStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_street, getActivity()));
            errGuarantorComStreetLocale = R.string.da_err_street;
            errMesgBean.setGuaComStreet(errGuarantorComStreetLocale);

        } else {
            errGuarantorComStreet.setVisibility(View.GONE);
            errGuarantorComStreetLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaComStreet(errGuarantorComStreetLocale);
        }

        /*company city */
        if (CommonUtils.isEmptyOrNull(appCompanyCity)) {
            errGuarantorComCity.setVisibility(View.VISIBLE);
            errGuarantorComCity.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_city, getActivity()));
            errGuarantorComCityLocale = R.string.da_err_city;
            errMesgBean.setGuaComCity(errGuarantorComCityLocale);

        } else {
            errDob.setVisibility(View.GONE);
            errDobLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaComCity(errDobLocale);
        }

        /*company township*/
        if (CommonUtils.isEmptyOrNull(appCompanyTownship)) {
            errGuarantorComTownship.setVisibility(View.VISIBLE);
            errGuarantorComTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_township, getActivity()));
            errGuarantorComTownshipLocale = R.string.da_err_township;
            errMesgBean.setGuaComTownship(errGuarantorComTownshipLocale);

        } else {
            errGuarantorComTownship.setVisibility(View.GONE);
            errGuarantorComTownshipLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaComTownship(errGuarantorComTownshipLocale);
        }

        /*Nrc No.*/
        if(nrcNumber==null || nrcNumber.equals(BLANK) ||
                townshipCodeVal==null || townshipCodeVal.equals(BLANK)){
            errNrc.setVisibility(View.VISIBLE);
            errNrc.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_nrc_err, getActivity()));
            errNrcLocale = R.string.register_nrc_err;
            errMesgBean.setGuaNrc(errNrcLocale);
        } else if(!isNrcCodeValid(nrcNumber)){
            errNrc.setVisibility(View.VISIBLE);
            errNrc.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_nrc_format_err, getActivity()));
            errNrcLocale = R.string.register_nrc_format_err;
            errMesgBean.setGuaNrc(errNrcLocale);
        } else if(!townshipCode.contains(autoCompleteTwspCode.getText().toString())){
            autoCompleteTwspCode.setText(BLANK);
            Log.e("township code", "blank");
        }else{
            errNrc.setVisibility(View.GONE);
            errNrcLocale = R.string.register_nrc_err;
            errMesgBean.setGuaNrc(errDobLocale);
        }

        /*Mobile No.*/
        if (CommonUtils.isEmptyOrNull(mobileNo)) {
            errMobileNo.setVisibility(View.VISIBLE);
            errMobileNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_phoneno_err, getActivity()));
            errMobileNoLocale = R.string.register_phoneno_err;
            errMesgBean.setGuaMobileNo(errMobileNoLocale);

        } else if (!CommonUtils.isPhoneNoValid(mobileNo)) {
            errMobileNo.setVisibility(View.VISIBLE);
            errMobileNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_phoneno_format_err_msg, getActivity()));
            errMobileNoLocale = R.string.register_phoneno_format_err_msg;
            errMesgBean.setGuaMobileNo(errMobileNoLocale);
        } else {
            errMobileNo.setVisibility(View.GONE);
            errMobileNoLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaMobileNo(errMobileNoLocale);
        }

        if (CommonUtils.isEmptyOrNull(companyTel)) {
            errCompanyTel.setVisibility(View.VISIBLE);
            errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_phoneno_err, getActivity()));
            errCompanyTelLocale = R.string.register_phoneno_err;
            errMesgBean.setGuaCompanyTelNo(errCompanyTelLocale);

        }else if(!CommonUtils.isPhoneNoValid(companyTel)){
            errCompanyTel.setVisibility(View.VISIBLE);
            errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_tel_no_format_err_msg, getActivity()));
            errCompanyTelLocale = R.string.register_tel_no_format_err_msg;
            errMesgBean.setGuaCompanyTelNo(errCompanyTelLocale);

        } else {
            errCompanyTel.setVisibility(View.GONE);
            errCompanyTelLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaCompanyTelNo(errCompanyTelLocale);
        }

        /*Nationality*/
        if (nationality == OTHER) {
            if (CommonUtils.isEmptyOrNull(nationalityDetail)) {
                errNationalityDetail.setVisibility(View.VISIBLE);
                errNationalityDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_nationality_require_err, getActivity()));
                errNationalityDetailLocale = R.string.da_nationality_require_err;
                errMesgBean.setGuaNationality(errNationalityDetailLocale);

            } else {
                errNationalityDetail.setVisibility(View.GONE);
                errNationalityDetailLocale = R.string.da_mesg_blank;
                errMesgBean.setGuaNationality(errNationalityDetailLocale);
            }
        }

        /*Relationship with Applicant*/
        if(relationship == EMERGENCY_RELATION_OTHER){
            if(relationshipDetail == null || relationshipDetail.equals(BLANK) || relationshipDetail.equals(SPACE)) {
                errRelationshipDetail.setVisibility(View.VISIBLE);
                errRelationshipDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_relationship_require_err, getActivity()));
                errRelationshipDetailLocale = R.string.da_relationship_require_err;
                errMesgBean.setGuaRelationshipLocale(errRelationshipDetailLocale);

            } else {
                errRelationshipDetail.setVisibility(View.GONE);
                errRelationshipDetailLocale = R.string.da_mesg_blank;
                errMesgBean.setGuaRelationshipLocale(errRelationshipDetailLocale);
            }
        }

        /*Type of Residence*/
        if(residentType == CommonConstants.RESIDENT_OTHER){
            if (residentTypeDetail == null || residentTypeDetail.equals(BLANK) || residentTypeDetail.equals(SPACE)) {
                errResidentTypeDetail.setVisibility(View.VISIBLE);
                errResidentTypeDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_residentType_require_err, getActivity()));
                errResidentTypeDetailLocale = R.string.da_residentType_require_err;
                errMesgBean.setGuaTypeOfResident(errResidentTypeDetailLocale);
            } else{
                errResidentTypeDetail.setVisibility(View.GONE);
                errResidentTypeDetailLocale = R.string.da_mesg_blank;
                errMesgBean.setGuaTypeOfResident(errResidentTypeDetailLocale);
            }
        }

        /*Year of Stay*/
        if (stayYear == 0 && stayMonth == 0) {
            errStayTime.setVisibility(View.VISIBLE);
            errStayTime.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_yearStay_require_err, getActivity()));
            errStayTimeLocale = R.string.da_yearStay_require_err;
            errMesgBean.setGuaYearOfStay(errStayTimeLocale);


        } else if (stayMonth > 11) {
            errStayTime.setVisibility(View.VISIBLE);
            errStayTime.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_yearStayMonth_exceed_err, getActivity()));
            errStayTimeLocale = R.string.da_yearStayMonth_exceed_err;
            errMesgBean.setGuaYearOfStay(errStayTimeLocale);


        } else {
            errStayTime.setVisibility(View.GONE);
            errStayTimeLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaYearOfStay(errStayTimeLocale);

        }

        /*Company Name*/
        if (CommonUtils.isEmptyOrNull(companyName)) {
            errCompanyName.setVisibility(View.VISIBLE);
            errCompanyName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_companyName_require_err, getActivity()));
            errCompanyNameLocale = R.string.da_companyName_require_err;
            errMesgBean.setGuaCompanyName(errCompanyNameLocale);


        } else {
            errCompanyName.setVisibility(View.GONE);
            errCompanyNameLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaCompanyName(errCompanyNameLocale);

        }

        /*Department*/
        if (CommonUtils.isEmptyOrNull(department)) {
            errDepartment.setVisibility(View.VISIBLE);
            errDepartment.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_department_require_err, getActivity()));
            errDepartmentLocale = R.string.da_department_require_err;
            errMesgBean.setGuaCompanyDepartment(errDepartmentLocale);


        } else {
            errDepartment.setVisibility(View.GONE);
            errDepartmentLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaCompanyDepartment(errDepartmentLocale);

        }

        /*Position*/
        if (CommonUtils.isEmptyOrNull(position)) {
            errPositon.setVisibility(View.VISIBLE);
            errPositon.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_position_require_err, getActivity()));
            errPositonLocale = R.string.da_position_require_err;
            errMesgBean.setGuaCompanyPosition(errPositonLocale);


        } else {
            errPositon.setVisibility(View.GONE);
            errPositonLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaCompanyPosition(errPositonLocale);

        }

        /*Year of Service*/
        if (serviceYear == 0 && serviceMonth == 0) {
            errService.setVisibility(View.VISIBLE);
            errService.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_serviceYear_require_err, getActivity()));
            errServiceLocale = R.string.da_serviceYear_require_err;
            errMesgBean.setGuaServiceYear(errServiceLocale);

        } else {
            errService.setVisibility(View.GONE);
            errServiceLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaServiceYear(errServiceLocale);

        }

        /*Monthly Basic Income*/
        if (monthlyIncome == 0.0) {
            errMonthlyIncome.setVisibility(View.VISIBLE);
            errMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_basicIncome_require_err, getActivity()));
            errMonthlyIncomeLocale = R.string.da_basicIncome_require_err;
            errMesgBean.setGuaMonthlyIncome(errMonthlyIncomeLocale);


        } else {
            errMonthlyIncome.setVisibility(View.GONE);
            errMonthlyIncomeLocale = R.string.da_mesg_blank;
            errMesgBean.setGuaMonthlyIncome(errMonthlyIncomeLocale);

        }

        PreferencesManager.saveErrorMesgInfo(getContext(), errMesgBean);
    }

    private boolean checkGuarantorData() {

        boolean validate = true;
        setUpGuarantorFormData();

        /*Name*/
        if (CommonUtils.isEmptyOrNull(name)) {
            validate = false;
        }
        if(!CommonUtils.isEmptyOrNull(residentMobile)){
            if(!CommonUtils.isNumberValid(residentMobile)) {
                validate = false;
            }
        }
        /*Date of Birth*/
        if (CommonUtils.isEmptyOrNull(currentStreet)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(currentQuarter)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(permanentQuarter)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(guarantorCity)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(guarantorTownship)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(permanentStreet)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(appCompanyCity)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(appCompanyTownship)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(dob)) {
            validate = false;
        }

        /*NRC NO.*/
        if(nrcNumber==null || nrcNumber.equals(BLANK) ||
                townshipCodeVal==null || townshipCodeVal.equals(BLANK)){
            validate = false;
        } else if(!isNrcCodeValid(nrcNumber)){
            validate = false;
        } else if(!townshipCode.contains(autoCompleteTwspCode.getText().toString())){
            autoCompleteTwspCode.setText(BLANK);
            Log.e("township code", "blank");
            validate = false;
        }

        /*Mobile No.*/
        if (CommonUtils.isEmptyOrNull(mobileNo)) {
            validate = false;
        } else if (!CommonUtils.isPhoneNoValid(mobileNo)) {
            validate = false;
        }

        /*Company Tel No.*/
        if (CommonUtils.isEmptyOrNull(companyTel)) {
            validate = false;
        } else if (!CommonUtils.isPhoneNoValid(companyTel)) {
            validate = false;
        }

        /*Nationality*/
        if (nationality == OTHER) {
            if (CommonUtils.isEmptyOrNull(nationalityDetail)) {
                validate = false;
            }
        }

        /*Relationship with Applicant*/
        if (relationship == GUARANTOR_RELATION_OTHER) {
            if (CommonUtils.isEmptyOrNull(relationshipDetail)) {
                validate = false;
            }
        }

        /*Type of Residence*/
        if (residentType == GUARANTOR_RESIDENT_OTHER) {
            if (CommonUtils.isEmptyOrNull(residentTypeDetail)) {
                validate = false;
            }
        }

        /*Year of Stay*/
        if (stayYear == 0 && stayMonth == 0) {
            validate = false;
        }

        if (stayMonth > 11) {
            validate = false;
        }

        /*Company Name*/
        if (CommonUtils.isEmptyOrNull(companyName)) {
            validate = false;
        }

        /*Department*/
        if (CommonUtils.isEmptyOrNull(department)) {
            validate = false;
        }

        /*Position*/
        if (CommonUtils.isEmptyOrNull(position)) {
            validate = false;
        }

        /*Year of Service*/
        if (serviceYear == 0 && serviceMonth == 0) {
            validate = false;
        }

        /*Monthly Basic Income*/
        if (monthlyIncome == 0.0) {
            validate = false;
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

        /*loanDataTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanConfirm_title, getContext()));
        emergencyTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_emergency_title, getContext()));*/

        guarantorTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_title, getContext()));

        labelName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_name, getActivity()));
        errName.setText(CommonUtils.getLocaleString(new Locale(language), errNameLocale, getActivity()));

        labelDob.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_dob, getActivity()));
        errDob.setText(CommonUtils.getLocaleString(new Locale(language), errDobLocale, getActivity()));

        errGuarantorStreet.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorStreetLocale, getActivity()));
        errGuarantorQuarter.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorQuarterLocale, getActivity()));
        errGuarantorComQuarter.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorComQuarterLocale, getActivity()));
        errGuarantorCity.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorCityLocale, getActivity()));
        errGuarantorTownship.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorTownshipLocale, getActivity()));
        errGuarantorComStreet.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorComStreetLocale, getActivity()));
        errGuarantorComCity.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorComCityLocale, getActivity()));
        errGuarantorComTownship.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorComTownshipLocale, getActivity()));

        labelNrc.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_nrc, getActivity()));
        errNrc.setText(CommonUtils.getLocaleString(new Locale(language), errNrcLocale, getActivity()));

        labelNationality.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_nationality, getActivity()));
        errNationalityDetail.setText(CommonUtils.getLocaleString(new Locale(language), errNationalityDetailLocale, getActivity()));

        labelMobileNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_mobileNo, getActivity()));
        errMobileNo.setText(CommonUtils.getLocaleString(new Locale(language), errMobileNoLocale, getActivity()));

        labelResidentTelNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_residentTelNo, getActivity()));

        labelRelationship.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_relationship, getActivity()));
        errRelationshipDetail.setText(CommonUtils.getLocaleString(new Locale(language), errRelationshipDetailLocale, getActivity()));

        labelResidentType.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_residenceType, getActivity()));
        errResidentTypeDetail.setText(CommonUtils.getLocaleString(new Locale(language), errResidentTypeDetailLocale, getActivity()));

        labelLivingWith.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_livingWith, getActivity()));

        labelGender.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_gender, getActivity()));

        labelMarital.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_marital, getActivity()));

        labelStayTime.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_yearStay, getActivity()));
        errStayTime.setText(CommonUtils.getLocaleString(new Locale(language), errStayTimeLocale, getActivity()));

        labelCompanyName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_companyName, getActivity()));
        errCompanyName.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyNameLocale, getActivity()));

        labelCompanyTel.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_companyTelNo, getActivity()));
        errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyTelLocale, getActivity()));

        labelDepartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_department, getActivity()));
        errDepartment.setText(CommonUtils.getLocaleString(new Locale(language), errDepartmentLocale, getActivity()));

        labelPositon.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_position, getActivity()));
        errPositon.setText(CommonUtils.getLocaleString(new Locale(language), errPositonLocale, getActivity()));

        labelService.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_serviceyear, getActivity()));
        errService.setText(CommonUtils.getLocaleString(new Locale(language), errServiceLocale, getActivity()));

        labelMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_basicIncome, getActivity()));
        errMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(language), errMonthlyIncomeLocale, getActivity()));

        labelTotalIncome.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_totalIncome, getActivity()));

        lblAddressTitleCurrent.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_currAddress, getActivity()));
        lblCurApartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_apartment_no, getActivity()));
        lblCurRoomNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_room_no, getActivity()));
        lblCurFloorNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_floor_no, getActivity()));
        lblCurStreet.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_street, getActivity()));
        lblCurQuarter.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_quarter, getActivity()));
        lblCurTownship.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_township, getActivity()));
        lblCurCity.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_city, getActivity()));

        lblAddressTitleCompany.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_companyAdd, getActivity()));
        lblPerApartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_apartment_no, getActivity()));
        lblPerRoomNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_room_no, getActivity()));
        lblPerFloorNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_floor_no, getActivity()));
        lblPerStreet.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_street, getActivity()));
        lblPerQuarter.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_quarter, getActivity()));
        lblPerTownship.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_township, getActivity()));
        lblPerCity.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_city, getActivity()));

        labelStayYear.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_stay_year, getActivity()));
        labelStayMonth.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_stay_month, getActivity()));
        labelServiceYear.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_service_year, getActivity()));
        labelServiceMonth.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_guarantor_service_month, getActivity()));

        btnNext.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_next_btn, getActivity()));
        btnSave.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_save_btn, getActivity()));

        PreferencesManager.setCurrentLanguage(getContext(), language);
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    void setUpGuarantorFormData() {

        name = guar_name.getText().toString();
        dob = guarantorDob.getText().toString();
        nrcNumber = guar_nrc.getText().toString();
        townshipCodeVal = autoCompleteTwspCode.getText().toString();
        companyName = guar_companyName.getText().toString();
        if(isEmptyOrNull(townshipCodeVal)||isEmptyOrNull(nrcNumber)){
            nrc = BLANK;
        }else{
            nrc = stateDivCodeVal + "/" + townshipCodeVal + nrcTypeVal + nrcNumber;
        }

        nationalityDetail = guar_nationalityDetail.getText().toString();
        mobileNo = guar_mobileNo.getText().toString();
        residentMobile = guar_residentTelNo.getText().toString();
        relationshipDetail = guar_relaitonshipWith.getText().toString();
        residentTypeDetail = guar_residentTypeDetail.getText().toString();
        companyTel = guar_companyTel.getText().toString();
        department = guar_department.getText().toString();
        position = guar_position.getText().toString();
        /*companyName*/

        monthly_income = guar_monthlyIncome.getText().toString();
        total_income = totalIncomeFilled.getText().toString();
        townshipCodeVal = autoCompleteTwspCode.getText().toString();

        currentApartmentNo = curApartmentNo.getText().toString();
        currentRoomNo = curRoomNo.getText().toString();
        currentFloorNo = curFloorNo.getText().toString();
        currentStreet = curStreet.getText().toString();
        currentQuarter = curQuarter.getText().toString();

        permanentApartmentNo = perApartmentNo.getText().toString();
        permanentRoomNo = perRoomNo.getText().toString();
        permanentFloorNo = perFloorNo.getText().toString();
        permanentStreet = perStreet.getText().toString();
        permanentQuarter = perQuarter.getText().toString();

        guarantorCity = currentGuarantorCity.getText().toString();
        guarantorTownship = currentGuarantorTownship.getText().toString();
        appCompanyCity = guarantorCompanyCity.getText().toString();
        appCompanyTownship = guarantorCompanyTownship.getText().toString();

        if (!monthly_income.equals(BLANK)) {
            char[] chars = monthly_income.toCharArray();
            String input = commaRemove(chars);
            monthlyIncome = Double.parseDouble(input);
        } else {
            monthlyIncome = 0;
        }

        if (!total_income.equals(BLANK)) {
            char[] chars = total_income.toCharArray();
            String input = commaRemove(chars);
            totalIncome = Double.parseDouble(input);
        } else {
            totalIncome = 0;
        }
    }

    void setUpNrcInfo(NrcBean nrc) {
        if (nrc != null) {
            spinnerDivCode.setSelection(nrc.getDivCode() - 1);
            spinnerType.setSelection(nrc.getNrcType());
            autoCompleteTwspCode.setText(nrc.getTownshipCode());
            guar_nrc.setText(String.valueOf(nrc.getRegistrationCode()));
            autoCompleteTwspCode.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
    }

    void appLoadInputData() {

        setUpGuarantorFormData();

        ApplicationRegisterSaveReqBean registerSaveReqBean
                = new ApplicationRegisterSaveReqBean();
        GuarantorFormBean guarantorFormBean
                = new GuarantorFormBean();
        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            registerSaveReqBean = PreferencesManager.getDaftSavedInfo(getActivity());

            if(registerSaveReqBean.getGuarantorInfoDto()!= null){
                guarantorFormBean.setDaGuarantorInfoId(registerSaveReqBean.getGuarantorInfoDto().getDaGuarantorInfoId());
            }

        }
        guarantorFormBean.setName(name);
        guarantorFormBean.setDob(dob);
        guarantorFormBean.setNrcNo(nrc);
        guarantorFormBean.setNationality(nationality);
        guarantorFormBean.setNationalityOther(nationalityDetail);
        guarantorFormBean.setMobileNo(mobileNo);
        guarantorFormBean.setResidentTelNo(residentMobile);
        guarantorFormBean.setRelationship(relationship);
        guarantorFormBean.setRelationshipOther(relationshipDetail);
        guarantorFormBean.setTypeOfResidence(residentType);
        guarantorFormBean.setTypeOfResidenceOther(residentTypeDetail);
        guarantorFormBean.setLivingWith(livingWith);
        guarantorFormBean.setLivingWithOther(livingWtihDetail);
        guarantorFormBean.setGender(gender);
        guarantorFormBean.setMaritalStatus(maritalStatus);
        guarantorFormBean.setYearOfServiceYear(serviceYear);
        guarantorFormBean.setYearOfServiceMonth(serviceMonth);
        guarantorFormBean.setCompanyName(companyName);
        guarantorFormBean.setCompanyTelNo(companyTel);
        guarantorFormBean.setDepartment(department);
        guarantorFormBean.setPosition(position);
        guarantorFormBean.setYearOfStayMonth(stayMonth);
        guarantorFormBean.setYearOfStayYear(stayYear);
        guarantorFormBean.setMonthlyBasicIncome(monthlyIncome);
        guarantorFormBean.setTotalIncome(totalIncome);

        guarantorFormBean.setCurrentAddressBuildingNo(currentApartmentNo);
        guarantorFormBean.setCurrentAddressRoomNo(currentRoomNo);
        guarantorFormBean.setCurrentAddressFloor(currentFloorNo);
        guarantorFormBean.setCurrentAddressStreet(currentStreet);
        guarantorFormBean.setCurrentAddressQtr(currentQuarter);

        int city_id = getCityId(cityId, cityList,guarantorCity);
        int town_id = getTownshipId(cityTownshipList, city_id, guarantorTownship);

        guarantorFormBean.setCurrentAddressCity(city_id);
        guarantorFormBean.setCurrentAddressTownship(town_id);

        guarantorFormBean.setCompanyAddressBuildingNo(permanentApartmentNo);
        guarantorFormBean.setCompanyAddressRoomNo(permanentRoomNo);
        guarantorFormBean.setCompanyAddressFloor(permanentFloorNo);
        guarantorFormBean.setCompanyAddressStreet(permanentStreet);
        guarantorFormBean.setCompanyAddressQtr(permanentQuarter);

        int company_city_id = getCityId(cityId, cityList,appCompanyCity);
        int company_town_id = getTownshipId(cityTownshipList, company_city_id, guarantorCompanyTownship.getText().toString());

        guarantorFormBean.setCompanyAddressCity(company_city_id);
        guarantorFormBean.setCompanyAddressTownship(company_town_id);

        registerSaveReqBean.setGuarantorInfoDto(guarantorFormBean);
        PreferencesManager.saveDaftSavedInfo(getActivity(), registerSaveReqBean);

    }

    void setUpDataOnPageChanged() {
        appLoadInputData();
        MainMenuActivityDrawer.guraDataCorrect = checkGuarantorData();
        //Log.e("Guarantor Data", String.valueOf(MainMenuActivityDrawer.guraDataCorrect));
    }

    @Override
    public void onStart() {
        super.onStart();
        dobCheck = guarantorDob.getText().toString();
        if (!isEmptyOrNull(dobCheck)) {
            guarantorDob.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        nrcTownShipCheck = autoCompleteTwspCode.getText().toString();
        if (!isEmptyOrNull(nrcTownShipCheck)) {
            autoCompleteTwspCode.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        companyStatusCheck = guar_companyName.getText().toString();
        if (!isEmptyOrNull(companyStatusCheck)) {
            guar_companyName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentStreetCheck = curStreet.getText().toString();
        if (!isEmptyOrNull(currentStreetCheck)) {
            curStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentCityCheck = currentGuarantorCity.getText().toString();
        if (!isEmptyOrNull(currentCityCheck)) {
            currentGuarantorCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentTownshipCheck = currentGuarantorTownship.getText().toString();
        if (!isEmptyOrNull(currentTownshipCheck)) {
            currentGuarantorTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyStreetCheck = perStreet.getText().toString();
        if (!isEmptyOrNull(companyStreetCheck)) {
            perStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyTelCheck = guar_companyTel.getText().toString();
        if (!isEmptyOrNull(companyTelCheck)) {
            guar_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyCityCheck = guarantorCompanyCity.getText().toString();
        if (!isEmptyOrNull(companyCityCheck)) {
            guarantorCompanyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyTownshipCheck = guarantorCompanyTownship.getText().toString();
        if (!isEmptyOrNull(companyTownshipCheck)) {
            guarantorCompanyTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        currentQuarterCheck = curQuarter.getText().toString();
        if (!isEmptyOrNull(currentQuarterCheck)) {
            curQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyQuarterCheck = perQuarter.getText().toString();
        if (!isEmptyOrNull(companyQuarterCheck)) {
            perQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.e("fragment ", "on destroy");
    }


    void displayNationalityDesc() {
        if (nationality == MYANMAR) {
            guar_nationalityDetail.setVisibility(View.GONE);
            guar_nationalityDetail.setText(BLANK);
        } else {
            guar_nationalityDetail.setVisibility(View.VISIBLE);
        }
    }

    void displayRelationshipDesc() {
        if (relationship == 5) {
            guar_relaitonshipWith.setVisibility(View.VISIBLE);
        } else {
            guar_relaitonshipWith.setVisibility(View.GONE);
            errRelationshipDetail.setText(BLANK);
            errRelationshipDetail.setVisibility(View.GONE);
        }
    }

    void displayResidentTypeDesc() {
        if (residentType == 5) {
            guar_residentTypeDetail.setVisibility(View.VISIBLE);
        } else {
            guar_residentTypeDetail.setVisibility(View.GONE);
            errResidentTypeDetail.setText(BLANK);
            errResidentTypeDetail.setVisibility(View.GONE);
        }
    }

    /*void displayLivingWithDesc() {
        if (livingWith == 5) {
            guar_livingWithDetail.setVisibility(View.VISIBLE);
        } else {
            guar_livingWithDetail.setVisibility(View.GONE);
        }
    }*/

    private String commaRemove(char[] chars) {
        int countChar = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ',') {
                countChar++;
            }
        }
        int counter = 0;
        char[] newchars = new char[countChar];
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ',') {
                newchars[counter] = chars[i];
                counter++;
            }
        }
        String input = new String(newchars);
        return input;
    }

    void setUpCityList(List<CityTownshipResBean> cityTownshipList){
        cityList = new ArrayList<>();
        cityId = new ArrayList<>();
        for(CityTownshipResBean listBean: cityTownshipList){
            cityList.add(listBean.getName());
            cityId.add(listBean.getCityId());
        }
    }

    void setUpCurrentTownshipList(List<CityTownshipResBean> cityTownshipList, String cityName){
        townshipList = new ArrayList<>();
        townshipId = new ArrayList<>();
        for(CityTownshipResBean listBean: cityTownshipList){
            if(listBean.getName().equals(cityName)){
                townshipBeanList = listBean.getTownshipInfoList();
                for(TownshipListBean townshipBean : townshipBeanList){
                    townshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }
            }
        }
    }

    void setUpPermanentTownshipList(List<CityTownshipResBean> cityTownshipList, String cityName){
        perTownshipList = new ArrayList<>();
        perTownshipId = new ArrayList<>();
        for(CityTownshipResBean listBean: cityTownshipList){
            if(listBean.getName().equals(cityName)){
                townshipBeanList = listBean.getTownshipInfoList();
                for(TownshipListBean townshipBean : townshipBeanList){
                    perTownshipList.add(townshipBean.getName());
                    perTownshipId.add(townshipBean.getTownshipId());
                }
            }
        }
    }

    String getCity(List<Integer> cityID,List<String> cityName, int cityId){
        String city = "";
        for(int id = 0; id<cityID.size(); id++){
            if(cityID.get(id) == cityId){
                city = cityName.get(id);
            }
        }
        return  city;
    }

    String getTownship(List<CityTownshipResBean> cityTownshipList, int cityId, int townId, int index){
        String township = "";
        townshipList = new ArrayList<>();
        townshipId = new ArrayList<>();
        for(CityTownshipResBean listBean: cityTownshipList){
            if(listBean.getCityId()== cityId){
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for(TownshipListBean townshipBean : townshipBeanList){
                    townshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }

                ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                addressTownship.setNotifyOnChange(true);
                if (index == CURRENT) {
                    currentGuarantorTownship.setThreshold(1);
                    currentGuarantorTownship.setAdapter(addressTownship);

                } else {
                    guarantorCompanyTownship.setThreshold(1);
                    guarantorCompanyTownship.setAdapter(addressTownship);

                }
                break;
            }
        }

        for(int id = 0; id<townshipId.size(); id++){
            if(townshipId.get(id) == townId){
                township = townshipList.get(id);
            }
        }

        return township;
    }

    int getCityId(List<Integer> cityID,List<String> cityName, String name){
        for(int id = 0; id<cityName.size(); id++){
            if(cityName.get(id).equals(name)){
                saveCityid = cityID.get(id);
            }
        }
        return  saveCityid;
    }

    private void setSelectedTownshipList(int cityId, int index){

        for(CityTownshipResBean listBean: cityTownshipList){
            if(listBean.getCityId()== cityId){
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for(TownshipListBean townshipBean : townshipBeanList){
                    townshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }

                ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                addressTownship.setNotifyOnChange(true);
                if (index == CURRENT) {
                    currentGuarantorTownship.setThreshold(1);
                    currentGuarantorTownship.setAdapter(addressTownship);

                } else {
                    guarantorCompanyTownship.setThreshold(1);
                    guarantorCompanyTownship.setAdapter(addressTownship);

                }
                break;
            }
        }
    }

    int getTownshipId(List<CityTownshipResBean> cityTownshipList, int cityId, String name){
        List<String> saveTownshipList = new ArrayList<>();
        List<Integer> saveTownshipId = new ArrayList<>();
        for(CityTownshipResBean listBean: cityTownshipList){
            if(listBean.getCityId()== cityId){
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for(TownshipListBean townshipBean : townshipBeanList){
                    saveTownshipList.add(townshipBean.getName());
                    saveTownshipId.add(townshipBean.getTownshipId());
                }
            }
        }

        for(int id = 0; id<saveTownshipList.size(); id++){
            if(saveTownshipList.get(id).equals(name)){
                saveCurTownshipid = saveTownshipId.get(id);
            }
        }

        return saveCurTownshipid;
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment());
    }
}