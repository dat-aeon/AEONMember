package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.canner.stepsview.StepsView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.ApplicationFormErrMesgBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.OccupationDataFormBean;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.beans.TownshipListBean;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.COMPANY_STATUS_OTHER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_ID;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DA_TITLES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOAN_AMOUNT_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isEmptyOrNull;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class SmallLoanOccupationFragment extends PagerRootFragment implements LanguageChangeListener {

    private View view;

    private static String companyName;
    private static String companyTel;
    private static String contactTimeFrom;
    private static String contactTimeTo;
    private static String companyDepartment;
    private static String companyPosition;
    private static String companyStatusDetail;
    private static int salaryDate_company;
    private static String curLang;
    private static String customerId;

    private int selectedPosition;
    private int serviceYear;
    private int serviceMonth;
    /*private int company_status;*/
    private static long monthlyIncome;
    private static long otherIncome;
    private static long totalIncome;

    private TextView errCompanyName;
    private TextView errCompanyTel;
    private TextView errCompanyDepartment;
    private TextView errCompanyPosition;
    private TextView errCompanyStatus;
    private TextView errCompanyStatusDetail;
    private TextView errServiceYear;
    private TextView errMonthlyIncome;
    private TextView errContactTime;

    private TextView labelCompanyName;
    private TextView labelCompanyTel;
    private TextView labelContactTime;
    private TextView labelCompanyDepartment;
    private TextView labelCompanyPosition;
    private TextView labelCompanyStatusDetail;
    private TextView labelServiceYear;
    private TextView labelMonthlyIncome;
    private TextView labelOtherIncome;
    private TextView labelTotalIncome;
    private TextView labelSalaryDate;
    private TextView totalIncomeAutoFilled;
    private TextView currencyUnit;
    private TextView labelValServiceYear;
    private TextView labelValServiceMonth;

    private Integer errCompanyNameLocale;
    private Integer errCompanyDepartmentLocale;
    private Integer errCompanyPositionLocale;
    private Integer errCompanyStatusLocale;
    private Integer errCompanyStatusDetailLocale;
    private Integer errServiceYearLocale;
    private Integer errMonthlyIncomeLocale;
    private Integer errCompanyTelLocale;

    private EditText occu_companyName;
    private EditText occu_companyTel;
    private EditText occu_contactTimeFrom;
    private EditText occu_contactTimeTo;
    private EditText occu_companyDepartment;
    private EditText occu_companyPosition;
    private EditText occu_companyStatusDetail;
    static EditText occu_monthlyIncome;
    private EditText occu_otherIncome;

    private Button btnNext;
    private Button btnSave;
    private LinearLayout backToAppData;
    private LinearLayout goToEmergencyData;
    //private TextView applicationTitle;
    //private TextView emergencyTitle;
    //private TextView occupationTitle;

    private String txtCompanyStatus;
    private int companyStatusId;
    private AutoCompleteTextView autoCompleteCompanyStatus;
    private boolean isInit;

    private Spinner companyServiceYear;
    private Spinner companyServiceMonth;
    private Spinner spinnerSalaryDate;
    private static SharedPreferences validationPreferences;
    SharedPreferences sharedPreferences;

    private String companyNameCheck;
    private String companyTelCheck;
    private String contactFromCheck;
    private String contactToCheck;
    private String companyDepartmentCheck;
    private String companyPostionCheck;
    private String companyStatusCheck;
    private String monthlyIncomeCheck;
    MenuItem languageFlag;

    private TextView lblCompanyTitle;
    private TextView lblComApartment;
    private TextView lblComRoomNo;
    private TextView lblComFloorNo;
    private TextView lblComStreet;
    private TextView lblComQuarter;
    private TextView lblComTownship;
    private TextView lblComCity;

    private TextView errCompanyStreet;
    private TextView errCompanyQuarter;
    private TextView errCompanyCity;
    private TextView errCompanyTownship;

    private Integer errCompanyStreetLocale;
    private Integer errCompanyQuarterLocale;
    private Integer errCompanyCityLocale;
    private Integer errCompanyTownshipLocale;
    private Integer errContactTimeLocale;

    private String companyStreetCheck;
    private String companyQuarterCheck;
    private String companyCityCheck;
    private String companyTownshipCheck;

    private EditText comApartmentNo;
    private EditText comRoomNo;
    private EditText comFloorNo;
    private EditText comStreet;
    private EditText comQuarter;

    private static List<String> addressTownshipList = new ArrayList<>();
    private static List<String> addressCityList = new ArrayList<>();
    private AutoCompleteTextView autoCurrentTownship;
    private AutoCompleteTextView autoCurrentCity;

    private String companyApartmentNo;
    private String companyRoomNo;
    private String companyFloorNo;
    private String companyStreet;
    private String companyQuarter;

    private static int companyTownship;
    private static int companyCity;

    private String curCityName;
    private List<String> cityList;
    private List<String> townshipList;
    private List<Integer> cityId;
    private List<Integer> townshipId;
    private List<TownshipListBean> townshipBeanList = new ArrayList<>();
    private List<CityTownshipResBean> cityTownshipList;

    private int saveCityid;
    private int saveTownshipid;
    private String appCompanyCity;
    private String appCompanyTownship;
    private String[] companyStatus;

    private StateProgressBar occuStepView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_small_loan_occupation, container, false);
        setHasOptionsMenu(true);

        String[] pages = {"Application\nData", "Occupation\nData", "Emergency\nContact", "Guarantor\nData", "Loan\nConfirmation"};
        occuStepView = view.findViewById(R.id.occu_stepped_bar);
        occuStepView.setStateDescriptionData(pages);

        occuStepView.setOnStateItemClickListener(new OnStateItemClickListener() {
            @Override
            public void onStateItemClick(StateProgressBar stateProgressBar, StateItem stateItem, int stateNumber, boolean isCurrentState) {
                if (stateNumber == 1) {
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(0, true);
                } else if (stateNumber == 2) {
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(1, true);
                } else if (stateNumber == 3) {
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(2, true);
                } else if (stateNumber == 4) {
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(3, true);
                } else if (stateNumber == 5) {
                    setUpDataOnPageChanged();
                    viewPager.setCurrentItem(4, true);
                }
            }
        });

        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);
        curLang = PreferencesManager.getCurrentLanguage(getActivity());

        isInit = true;
        companyStatus = getResources().getStringArray(R.array.occupation_company_status);
        validationPreferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        customerId = PreferencesManager.getStringEntryFromPreferences(validationPreferences, CUSTOMER_ID);

        StepsView stepsView = view.findViewById(R.id.stepsView_2);
        stepsView.setLabels(DA_TITLES)
                .setBarColorIndicator(getContext().getColor(R.color.gray))
                .setProgressColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setLabelColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setCompletedPosition(1)
                .drawView();

        // Company Address Label
        lblCompanyTitle = view.findViewById(R.id.company_current_address);
        lblComApartment = view.findViewById(R.id.company_txt_apartment_no);
        lblComRoomNo = view.findViewById(R.id.company_txt_room_no);
        lblComFloorNo = view.findViewById(R.id.company_txt_floor_no);
        lblComStreet = view.findViewById(R.id.company_txt_street);
        lblComQuarter = view.findViewById(R.id.company_txt_quarter);
        lblComTownship = view.findViewById(R.id.company_txt_township);
        lblComCity = view.findViewById(R.id.company_txt_city);

        // Company Address EditText
        comApartmentNo = view.findViewById(R.id.company_apartment_no);
        comRoomNo = view.findViewById(R.id.company_room_no);
        comFloorNo = view.findViewById(R.id.company_floor_no);
        comStreet = view.findViewById(R.id.company_street);
        comQuarter = view.findViewById(R.id.company_quarter);

        errCompanyStreet = view.findViewById(R.id.occupation_err_street);
        errCompanyQuarter = view.findViewById(R.id.occupation_err_quarter);
        errCompanyCity = view.findViewById(R.id.occupation_err_city);
        errCompanyTownship = view.findViewById(R.id.occupation_err_township);

        errCompanyQuarterLocale = R.string.da_mesg_blank;
        errCompanyStreetLocale = R.string.da_mesg_blank;
        errCompanyCityLocale = R.string.da_mesg_blank;
        errCompanyTownshipLocale = R.string.da_mesg_blank;

        // Company Address Spinner
        autoCurrentTownship = view.findViewById(R.id.company_township);
        autoCurrentCity = view.findViewById(R.id.company_city);

        labelCompanyName = view.findViewById(R.id.occupation_text_name);
        errCompanyName = view.findViewById(R.id.occupation_err_name);
        errCompanyNameLocale = R.string.da_mesg_blank;

        labelCompanyTel = view.findViewById(R.id.occu_com_tel);
        errCompanyTel = view.findViewById(R.id.occupation_err_companyTel);
        errCompanyTelLocale = R.string.da_mesg_blank;

        labelContactTime = view.findViewById(R.id.occu_contact_time);

        labelCompanyDepartment = view.findViewById(R.id.occu_com_dept);
        errCompanyDepartment = view.findViewById(R.id.occupation_err_companyDept);
        errCompanyDepartmentLocale = R.string.da_mesg_blank;

        labelCompanyPosition = view.findViewById(R.id.occu_com_pos);
        errCompanyPosition = view.findViewById(R.id.occupation_err_companyPos);
        errCompanyPositionLocale = R.string.da_mesg_blank;

        labelCompanyStatusDetail = view.findViewById(R.id.text_company_status);
        errCompanyStatus = view.findViewById(R.id.occupation_err_companyStatus_auto);
        errCompanyStatusLocale = R.string.da_mesg_blank;

        errCompanyStatusDetail = view.findViewById(R.id.occupation_err_companyStatus);
        errCompanyStatusDetailLocale = R.string.da_mesg_blank;

        labelServiceYear = view.findViewById(R.id.occu_service_year);
        errServiceYear = view.findViewById(R.id.occupation_err_serviceYear);
        errServiceYearLocale = R.string.da_mesg_blank;

        labelMonthlyIncome = view.findViewById(R.id.occu_monthly_income);
        errMonthlyIncome = view.findViewById(R.id.occupation_err_basicIncome);
        errMonthlyIncomeLocale = R.string.da_mesg_blank;

        errContactTime = view.findViewById(R.id.occupation_err_contactTime);
        errContactTimeLocale = R.string.da_mesg_blank;

        labelOtherIncome = view.findViewById(R.id.occu_other_income);
        labelTotalIncome = view.findViewById(R.id.occu_total_income);
        labelSalaryDate = view.findViewById(R.id.occu_salary_date);

        labelValServiceYear = view.findViewById(R.id.lbl_occu_stay_year);
        labelValServiceMonth = view.findViewById(R.id.lbl_occu_stay_month);

        occu_companyName = view.findViewById(R.id.occupation_input_name);
        occu_companyName.requestFocus();
        occu_companyTel = view.findViewById(R.id.occupation_comapnyTel);
        occu_contactTimeFrom = view.findViewById(R.id.occupation_contact_am);
        occu_contactTimeTo = view.findViewById(R.id.occupation_contact_pm);
        occu_companyDepartment = view.findViewById(R.id.occupation_company_dept);
        occu_companyPosition = view.findViewById(R.id.occupation_company_pos);
        occu_companyStatusDetail = view.findViewById(R.id.company_status_detail);
        occu_monthlyIncome = view.findViewById(R.id.occupation_Basic_income);
        totalIncomeAutoFilled = view.findViewById(R.id.occupation_total_income);
        currencyUnit = view.findViewById(R.id.occupation_total_income_cur_unit);

        occu_otherIncome = view.findViewById(R.id.occupation_other_income);
        autoCompleteCompanyStatus = view.findViewById(R.id.autocomplete_company_status);

        companyServiceYear = view.findViewById(R.id.occu_spinner_service_year);
        companyServiceMonth = view.findViewById(R.id.occu_spinner_service_month);
        spinnerSalaryDate = view.findViewById(R.id.occu_spinner_salary_date);

        occu_companyName.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});
        occu_companyDepartment.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});
        occu_companyPosition.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});
        occu_companyStatusDetail.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});

        comApartmentNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        comRoomNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        comFloorNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        comStreet.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        comQuarter.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});

        companyStreetCheck = comStreet.getText().toString();
        if (!isEmptyOrNull(companyStreetCheck)) {
            comStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyCityCheck = autoCurrentCity.getText().toString();
        if (!isEmptyOrNull(companyCityCheck)) {
            autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyTownshipCheck = autoCurrentTownship.getText().toString();
        if (!isEmptyOrNull(companyTownshipCheck)) {
            autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        final List<TownshipCodeResDto> townshipCodeResDtoList = PreferencesManager.getTownshipCode(getActivity());
        addressTownshipList = townshipCodeResDtoList.get(0).getTownshipCodeList();
        addressCityList = townshipCodeResDtoList.get(0).getTownshipCodeList();
        cityTownshipList = PreferencesManager.getCityListInfo(getActivity());
        setUpCityList(cityTownshipList);
        curCityName = cityList.get(0);
        setUpCurrentTownshipList(cityTownshipList, curCityName);

        // Company Current City
        ArrayAdapter<String> companyAddressCity = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, cityList);
        companyAddressCity.setDropDownViewResource(R.layout.dialog_nrc_division);
        companyAddressCity.setNotifyOnChange(true);
        autoCurrentCity.setThreshold(1);
        autoCurrentCity.setAdapter(companyAddressCity);

        autoCurrentCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCurrentCity.setText((String) parent.getAdapter().getItem(position));
                companyCity = cityId.get(position);
                curCityName = (String) parent.getAdapter().getItem(position);
                setUpCurrentTownshipList(cityTownshipList, curCityName);
                autoCurrentTownship.setText(BLANK);
                autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));

                // Company Current Township
                final ArrayAdapter<String> companyAddressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                companyAddressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                companyAddressTownship.setNotifyOnChange(true);
                autoCurrentTownship.setThreshold(1);
                autoCurrentTownship.setAdapter(companyAddressTownship);

            }
        });

        autoCurrentCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoCurrentCity.showDropDown();
                    autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!cityList.contains(autoCurrentCity.getText().toString())) {
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
                companyTownship = townshipId.get(position);
            }
        });

        autoCurrentTownship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    autoCurrentTownship.showDropDown();
                    autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!townshipList.contains(autoCurrentTownship.getText().toString())) {
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

        final String[] yearOfService = getResources().getStringArray(R.array.year_array);
        ArrayAdapter<String> adapterServiceYear = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, yearOfService);
        companyServiceYear.setAdapter(adapterServiceYear);

        final String[] monthOfService = getResources().getStringArray(R.array.month_array);
        ArrayAdapter<String> adapterServiceMonth = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, monthOfService);
        companyServiceMonth.setAdapter(adapterServiceMonth);

        final String[] occuSalaryDate = getResources().getStringArray(R.array.day_array);
        ArrayAdapter<String> adapterSalaryDate = new ArrayAdapter<String>(getActivity(), R.layout.year_month_spinner, occuSalaryDate);
        spinnerSalaryDate.setAdapter(adapterSalaryDate);

        ScrollView scrollView = view.findViewById(R.id.occupation_scroll);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        spinnerSalaryDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                salaryDate_company = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        companyServiceYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                serviceYear = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        companyServiceMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                serviceMonth = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        comStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    comStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyStreetCheck = comStreet.getText().toString();
                    if (isEmptyOrNull(companyStreetCheck)) {
                        comStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        occu_monthlyIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                occu_monthlyIncome.removeTextChangedListener(this);
                long monthlyIncome = 0;
                long otherIncome = 0;
                try {

                    String originalString = s.toString();

                    if (originalString.equals(BLANK)) {
                        if (!occu_otherIncome.getText().toString().equals(BLANK)) {
                            otherIncome = Long.parseLong(occu_otherIncome.getText().toString().replaceAll(",", ""));
                            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                            formatter.applyPattern(LOAN_AMOUNT_FORMAT);
                            String formattedString = formatter.format(otherIncome);
                            totalIncomeAutoFilled.setText(formattedString);
                        }
                    } else {
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }

                        if (!occu_otherIncome.getText().toString().equals(BLANK)) {
                            otherIncome = Long.parseLong(occu_otherIncome.getText().toString().replaceAll(",", ""));
                        }

                        monthlyIncome = Long.parseLong(originalString);
                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern(LOAN_AMOUNT_FORMAT);
                        String formattedString = formatter.format(monthlyIncome);

                        Long total = monthlyIncome + otherIncome;
                        String totalIncomeString = formatter.format(total);

                        //setting text after format to EditText
                        if (totalIncomeString.length() > 1) {
                            totalIncomeAutoFilled.setText(totalIncomeString);
                        } else {
                            totalIncomeAutoFilled.setText("0");
                        }
                        occu_monthlyIncome.setText(formattedString);
                        occu_monthlyIncome.setSelection(occu_monthlyIncome.getText().length());
                    }

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                occu_monthlyIncome.addTextChangedListener(this);
            }
        });

        occu_otherIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                occu_otherIncome.removeTextChangedListener(this);
                long monthlyIncome = 0;
                long otherIncome = 0;
                try {
                    String originalString = s.toString();
                    if (originalString.equals(BLANK)) {
                        if (!occu_monthlyIncome.getText().toString().equals(BLANK)) {
                            monthlyIncome = Long.parseLong(occu_monthlyIncome.getText().toString().replaceAll(",", ""));
                            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                            formatter.applyPattern(LOAN_AMOUNT_FORMAT);
                            String formattedString = formatter.format(monthlyIncome);
                            totalIncomeAutoFilled.setText(formattedString);
                        }
                    } else {
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }

                        if (!occu_monthlyIncome.getText().toString().equals(BLANK)) {
                            monthlyIncome = Long.parseLong(occu_monthlyIncome.getText().toString().replaceAll(",", ""));
                        }

                        otherIncome = Long.parseLong(originalString);
                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern(LOAN_AMOUNT_FORMAT);
                        String formattedString = formatter.format(otherIncome);

                        Long total = monthlyIncome + otherIncome;
                        String totalIncomeString = formatter.format(total);

                        //setting text after format to EditText
                        if (totalIncomeString.length() > 1) {
                            totalIncomeAutoFilled.setText(totalIncomeString);
                        } else {
                            totalIncomeAutoFilled.setText("0");
                        }
                        occu_otherIncome.setText(formattedString);
                        occu_otherIncome.setSelection(occu_otherIncome.getText().length());
                    }

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                occu_otherIncome.addTextChangedListener(this);
            }
        });

        final ArrayList<String> companyStatusList = new ArrayList<String>(Arrays.asList(companyStatus));
        ArrayAdapter<String> adapterCompanyStatus = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, companyStatusList);
        adapterCompanyStatus.setDropDownViewResource(R.layout.dialog_nrc_division);
        adapterCompanyStatus.setNotifyOnChange(true);
        autoCompleteCompanyStatus.setThreshold(1);
        autoCompleteCompanyStatus.setAdapter(adapterCompanyStatus);

        autoCompleteCompanyStatus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteCompanyStatus.setText((String) parent.getAdapter().getItem(position));
                displayCompanyStatusDesc(autoCompleteCompanyStatus.getText().toString());
            }
        });

        autoCompleteCompanyStatus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!companyStatusList.contains(autoCompleteCompanyStatus.getText().toString())) {
                        autoCompleteCompanyStatus.setText(BLANK);
                        occu_companyStatusDetail.setVisibility(View.GONE);
                        autoCompleteCompanyStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                } else {
                    autoCompleteCompanyStatus.showDropDown();
                    autoCompleteCompanyStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                }
            }
        });

        autoCompleteCompanyStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteCompanyStatus.showDropDown();
            }
        });

        btnNext = view.findViewById(R.id.btn_occupation_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDataOnPageChanged();
                viewPager.setCurrentItem(2, true);
            }
        });

        btnSave = view.findViewById(R.id.btn_occudata_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOccupation();
            }
        });

        //applicationTitle = view.findViewById(R.id.da_app_data_title);
        //emergencyTitle = view.findViewById(R.id.da_emer_data_title);
        //occupationTitle = view.findViewById(R.id.da_occu_title);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                if (selectedPosition == 1) {
                    ((MainMenuActivityDrawer) getActivity()).setLanguageListener(SmallLoanOccupationFragment.this);
                    MainMenuActivityDrawer.isOccupationLanguageFlag = true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (selectedPosition == 2) {
                    setUpDataOnPageChanged();

                } else if (selectedPosition == 1) {
                    curLang = PreferencesManager.getCurrentLanguage(getContext());
                    changeLabel(curLang);

                    if (MainMenuActivityDrawer.isSubmitclickOccuData) {
                        MainMenuActivityDrawer.isSubmitclickOccuData = false;
                        showValidationMsg(curLang);
                    }
                }
            }

        });

        occu_contactTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        occu_contactTimeFrom.setText(getSelectedTime(hourOfDay, minute));
                        occu_contactTimeFrom.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                    }
                }, hour, minute, false);

                mTimePicker.show();
            }
        });

        occu_contactTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        occu_contactTimeTo.setText(getSelectedTime(hourOfDay, minute));
                        occu_contactTimeTo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                    }
                }, hour, minute, false);
                mTimePicker.show();
            }
        });

        replaceLastOccupationData();

        companyNameCheck = occu_companyName.getText().toString();
        if (!isEmptyOrNull(companyNameCheck)) {
            occu_companyName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        occu_companyName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    occu_companyName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyNameCheck = occu_companyName.getText().toString();
                    if (isEmptyOrNull(companyNameCheck)) {
                        occu_companyName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        companyQuarterCheck = comQuarter.getText().toString();
        if (!isEmptyOrNull(companyQuarterCheck)) {
            comQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        comQuarter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    comQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyQuarterCheck = comQuarter.getText().toString();
                    if (isEmptyOrNull(companyQuarterCheck)) {
                        comQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        companyTelCheck = occu_companyTel.getText().toString();
        if (!isEmptyOrNull(companyTelCheck)) {
            occu_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        occu_companyTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    occu_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyTelCheck = occu_companyTel.getText().toString();
                    if (isEmptyOrNull(companyTelCheck)) {
                        occu_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        contactFromCheck = occu_contactTimeFrom.getText().toString();
        contactToCheck = occu_contactTimeTo.getText().toString();

        companyDepartmentCheck = occu_companyDepartment.getText().toString();
        if (!isEmptyOrNull(companyDepartmentCheck)) {
            occu_companyDepartment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        occu_companyDepartment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    occu_companyDepartment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyDepartmentCheck = occu_companyDepartment.getText().toString();
                    if (isEmptyOrNull(companyDepartmentCheck)) {
                        occu_companyDepartment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        companyPostionCheck = occu_companyPosition.getText().toString();
        if (!isEmptyOrNull(companyPostionCheck)) {
            occu_companyPosition.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        occu_companyPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    occu_companyPosition.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyPostionCheck = occu_companyPosition.getText().toString();
                    if (isEmptyOrNull(companyPostionCheck)) {
                        occu_companyPosition.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        companyStatusCheck = occu_companyStatusDetail.getText().toString();
        if (!isEmptyOrNull(companyStatusCheck)) {
            occu_companyStatusDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        occu_companyStatusDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    occu_companyStatusDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    companyStatusCheck = occu_companyStatusDetail.getText().toString();
                    if (isEmptyOrNull(companyStatusCheck)) {
                        occu_companyStatusDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        monthlyIncomeCheck = occu_monthlyIncome.getText().toString();
        if (!isEmptyOrNull(monthlyIncomeCheck)) {
            occu_monthlyIncome.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        occu_monthlyIncome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    occu_monthlyIncome.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    monthlyIncomeCheck = occu_monthlyIncome.getText().toString();
                    if (isEmptyOrNull(monthlyIncomeCheck)) {
                        occu_monthlyIncome.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
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

        return true;
    }


    private void saveOccupation() {

        setUpOccupationFormData();

        OccupationDataFormBean occupationData = new OccupationDataFormBean();
        occupationData.setCompanyName(companyName);
        occupationData.setCompanyTelNo(companyTel);
        occupationData.setContactTimeFrom(contactTimeFrom);
        occupationData.setContactTimeTo(contactTimeTo);
        occupationData.setDepartment(companyDepartment);
        occupationData.setPosition(companyPosition);
        occupationData.setCompanyStatusOther(companyStatusDetail);
        occupationData.setYearOfServiceYear(serviceYear);
        occupationData.setYearOfServiceMonth(serviceMonth);
        occupationData.setMonthlyBasicIncome(Double.parseDouble(Long.toString(monthlyIncome)));
        occupationData.setOtherIncome(Double.parseDouble(Long.toString(otherIncome)));
        occupationData.setTotalIncome(Double.parseDouble(Long.toString(monthlyIncome + otherIncome)));
        occupationData.setSalaryDay(salaryDate_company);
        occupationData.setCompanyStatus(getCompanyStatusId(companyStatus, txtCompanyStatus));

        occupationData.setCompanyAddressBuildingNo(companyApartmentNo);
        occupationData.setCompanyAddressRoomNo(companyRoomNo);
        occupationData.setCompanyAddressFloor(companyFloorNo);
        occupationData.setCompanyAddressStreet(companyStreet);
        occupationData.setCompanyAddressQtr(companyQuarter);

        int city_id = getCityId(cityId, cityList, appCompanyCity);
        int town_id = getTownshipId(cityTownshipList, city_id, appCompanyTownship);
        occupationData.setCompanyAddressCity(city_id);
        occupationData.setCompanyAddressTownship(town_id);

        ApplicationRegisterSaveReqBean saveDataBean
                = new ApplicationRegisterSaveReqBean();

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            saveDataBean = PreferencesManager.getDaftSavedInfo(getActivity());
        }

        saveDataBean.setDaApplicationTypeId(APPLICATION_PLOAN);
        saveDataBean.setCustomerId(Integer.parseInt(customerId));
        saveDataBean.setChannelType(CHANNEL_MOBILE);

        saveDataBean.setApplicantCompanyInfoDto(occupationData);

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
                                    showSnackBarMessage("Applicant Company Info. saved.");
                                } else {
                                    saveDialog.dismiss();
                                    showSnackBarMessage("Applicant Company Info. cannot be saved.");
                                }
                            } else {
                                saveDialog.dismiss();
                                showSnackBarMessage("Applicant Company Info. cannot be saved.");
                            }
                        } else {
                            closeDialog(saveDialog);
                            showSnackBarMessage("Applicant Company Info. cannot be saved.");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<ApplicationRegisterSaveReqBean>> call, Throwable t) {
                        closeDialog(saveDialog);
                        showSnackBarMessage("Applicant Company Info. save failed.");
                    }
                });
            }

        } else {
            showSnackBarMessage("Applicant Company Info. is required to save.");
        }
    }

    private void replaceLastOccupationData() {

        if (PreferencesManager.isDaftSavedErrExisted(getActivity())) {
            ApplicationFormErrMesgBean savedInformation
                    = PreferencesManager.getErrMesgInfo(getContext());

            errCompanyStatusLocale = savedInformation.getOccCompanyStatusLocale();
            errCompanyNameLocale = savedInformation.getOccCompanyNameLocale();
            errCompanyTelLocale = savedInformation.getOccCompanyTelLocale();
            errCompanyDepartmentLocale = savedInformation.getOccCompanyDepartmentLocale();
            errCompanyPositionLocale = savedInformation.getOccCompanyPositionLocale();
            errCompanyStatusDetailLocale = savedInformation.getOccCompanyStatusDetailLocale();
            errServiceYearLocale = savedInformation.getOccServiceYearLocale();
            errMonthlyIncomeLocale = savedInformation.getOccMonthlyIncomeLocale();
            errCompanyStreetLocale = savedInformation.getOccCompanyStreetLocale();
            errCompanyQuarterLocale = savedInformation.getOccCompanyQuarterLocale();
            errCompanyCityLocale = savedInformation.getOccCompanyCityLocale();
            errCompanyTownshipLocale = savedInformation.getOccCompanyTownshipLocale();
            errContactTimeLocale = savedInformation.getOccContactTimeLocale();

            if (errCompanyNameLocale != R.string.da_mesg_blank) {
                errCompanyName.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyNameLocale, getContext()));
                errCompanyName.setVisibility(View.VISIBLE);
            }

            if (errCompanyTelLocale != R.string.da_mesg_blank) {
                errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyTelLocale, getContext()));
                errCompanyTel.setVisibility(View.VISIBLE);
            }

            if (errContactTimeLocale != R.string.da_mesg_blank) {
                errContactTime.setText(CommonUtils.getLocaleString(new Locale(curLang), errContactTimeLocale, getContext()));
                errContactTime.setVisibility(View.VISIBLE);
            }

            if (errCompanyStreetLocale != R.string.da_mesg_blank) {
                errCompanyStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyStreetLocale, getContext()));
                errCompanyStreet.setVisibility(View.VISIBLE);
            }

            if (errCompanyQuarterLocale != R.string.da_mesg_blank) {
                errCompanyQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyQuarterLocale, getContext()));
                errCompanyQuarter.setVisibility(View.VISIBLE);
            }

            if (errCompanyCityLocale != R.string.da_mesg_blank) {
                errCompanyCity.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyCityLocale, getContext()));
                errCompanyCity.setVisibility(View.VISIBLE);
            }

            if (errCompanyTownshipLocale != R.string.da_mesg_blank) {
                errCompanyTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyTownshipLocale, getContext()));
                errCompanyTownship.setVisibility(View.VISIBLE);
            }

            if (errCompanyDepartmentLocale != R.string.da_mesg_blank) {
                errCompanyDepartment.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyDepartmentLocale, getContext()));
                errCompanyDepartment.setVisibility(View.VISIBLE);
            }

            if (errCompanyPositionLocale != R.string.da_mesg_blank) {
                errCompanyPosition.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyPositionLocale, getContext()));
                errCompanyPosition.setVisibility(View.VISIBLE);
            }

            if (errCompanyStatusDetailLocale != R.string.da_mesg_blank) {
                errCompanyStatusDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyStatusDetailLocale, getContext()));
                errCompanyStatusDetail.setVisibility(View.VISIBLE);
            }

            if (errCompanyStatusLocale != R.string.da_mesg_blank) {
                errCompanyStatus.setText(CommonUtils.getLocaleString(new Locale(curLang), errCompanyStatusLocale, getContext()));
                errCompanyStatus.setVisibility(View.VISIBLE);
            }

            if (errServiceYearLocale != R.string.da_mesg_blank) {
                errServiceYear.setText(CommonUtils.getLocaleString(new Locale(curLang), errServiceYearLocale, getContext()));
                errServiceYear.setVisibility(View.VISIBLE);
            }

            if (errMonthlyIncomeLocale != R.string.da_mesg_blank) {
                errMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(curLang), errMonthlyIncomeLocale, getContext()));
                errMonthlyIncome.setVisibility(View.VISIBLE);
            }

        }

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            ApplicationRegisterSaveReqBean savedInformation
                    = PreferencesManager.getDaftSavedInfo(getActivity());
            try {
                OccupationDataFormBean occuLastData = savedInformation.getApplicantCompanyInfoDto();
                occu_companyName.setText(occuLastData.getCompanyName());
                occu_companyTel.setText(occuLastData.getCompanyTelNo());
                occu_contactTimeFrom.setText(occuLastData.getContactTimeFrom());
                occu_contactTimeTo.setText(occuLastData.getContactTimeTo());
                occu_companyDepartment.setText(occuLastData.getDepartment());
                occu_companyPosition.setText(occuLastData.getPosition());
                occu_companyStatusDetail.setText(occuLastData.getCompanyStatusOther());
                occu_monthlyIncome.setText(String.format("%,d", Long.valueOf(occuLastData.getMonthlyBasicIncome().longValue())));
                occu_otherIncome.setText(String.format("%,d", Long.valueOf(occuLastData.getOtherIncome().longValue())));
                totalIncomeAutoFilled.setText(String.format("%,d", Long.valueOf(occuLastData.getTotalIncome().longValue())));
                autoCompleteCompanyStatus.setText(getCompanyStatusName(companyStatus, occuLastData.getCompanyStatus()));
                companyServiceYear.setSelection(occuLastData.getYearOfServiceYear());
                companyServiceMonth.setSelection(occuLastData.getYearOfServiceMonth());
                spinnerSalaryDate.setSelection(occuLastData.getSalaryDay() - 1);

                comApartmentNo.setText(occuLastData.getCompanyAddressBuildingNo());
                comRoomNo.setText(occuLastData.getCompanyAddressRoomNo());
                comFloorNo.setText(occuLastData.getCompanyAddressFloor());
                comStreet.setText(occuLastData.getCompanyAddressStreet());
                comQuarter.setText(occuLastData.getCompanyAddressQtr());

                if (occuLastData.getCompanyAddressCity() == 0) {
                    autoCurrentCity.setText("");
                } else {
                    autoCurrentCity.setText(getCity(cityId, cityList, occuLastData.getCompanyAddressCity()));
                    this.setSelectedTownshipList(occuLastData.getCompanyAddressCity().intValue());

                    setUpCurrentTownshipList(cityTownshipList, autoCurrentCity.getText().toString());
                    final ArrayAdapter<String> companyAddressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                    companyAddressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                    companyAddressTownship.setNotifyOnChange(true);
                    autoCurrentTownship.setThreshold(1);
                    autoCurrentTownship.setAdapter(companyAddressTownship);
                }
                if (occuLastData.getCompanyAddressTownship() == 0) {
                    autoCurrentTownship.setText("");
                } else {
                    autoCurrentTownship.setText(getTownship(cityTownshipList, occuLastData.getCompanyAddressCity(), occuLastData.getCompanyAddressTownship()));
                }

            } catch (NullPointerException e) {
                occu_monthlyIncome.setText(BLANK);
                occu_otherIncome.setText(BLANK);
                totalIncomeAutoFilled.setText(BLANK);
            }
        }
    }

    private void showValidationMsg(String curLang) {
        setUpOccupationFormData();
        ApplicationFormErrMesgBean errOccuMesgBean = PreferencesManager.getErrMesgInfo(getContext());
        if (errOccuMesgBean == null) {
            errOccuMesgBean = new ApplicationFormErrMesgBean();
        }

        /*Company Name*/
        if (CommonUtils.isEmptyOrNull(companyName)) {
            errCompanyName.setVisibility(View.VISIBLE);
            errCompanyName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_companyName_require_err, getActivity()));
            errCompanyNameLocale = R.string.da_companyName_require_err;
        } else {
            errCompanyName.setVisibility(View.GONE);
            errCompanyNameLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccCompanyNameLocale(errCompanyNameLocale);

        /*Company Tel No.*/
        if (CommonUtils.isEmptyOrNull(companyTel)) {
            errCompanyTel.setVisibility(View.VISIBLE);
            errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_phoneno_err, getActivity()));
            errCompanyTelLocale = R.string.register_phoneno_err;
            errOccuMesgBean.setOccCompanyTelLocale(errCompanyTelLocale);
        } else if (!CommonUtils.isTelPhoneNoValid(companyTel)) {
            errCompanyTel.setVisibility(View.VISIBLE);
            errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_phoneno_format_err_msg, getActivity()));
            errCompanyTelLocale = R.string.register_phoneno_format_err_msg;
            errOccuMesgBean.setOccCompanyTelLocale(errCompanyTelLocale);
        } else {
            errCompanyTel.setVisibility(View.GONE);
            errCompanyTelLocale = R.string.da_mesg_blank;
            errOccuMesgBean.setOccCompanyTelLocale(errCompanyTelLocale);
        }

        /*Company Street*/
        if (CommonUtils.isEmptyOrNull(companyStreet)) {
            errCompanyStreet.setVisibility(View.VISIBLE);
            errCompanyStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_street, getActivity()));
            errCompanyStreetLocale = R.string.da_err_street;
        } else {
            errCompanyStreet.setVisibility(View.GONE);
            errCompanyStreetLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccCompanyStreetLocale(errCompanyStreetLocale);

        /*Company Quarter*/
        if (CommonUtils.isEmptyOrNull(companyQuarter)) {
            errCompanyQuarter.setVisibility(View.VISIBLE);
            errCompanyQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_quarter, getActivity()));
            errCompanyQuarterLocale = R.string.da_err_quarter;
        } else {
            errCompanyQuarter.setVisibility(View.GONE);
            errCompanyQuarterLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccCompanyQuarterLocale(errCompanyQuarterLocale);

        /*Company City*/
        if (CommonUtils.isEmptyOrNull(appCompanyCity)) {
            errCompanyCity.setVisibility(View.VISIBLE);
            errCompanyCity.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_city, getActivity()));
            errCompanyCityLocale = R.string.da_err_city;
        } else {
            errCompanyCity.setVisibility(View.GONE);
            errCompanyCityLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccCompanyCityLocale(errCompanyCityLocale);

        /*Company Township*/
        if (CommonUtils.isEmptyOrNull(appCompanyTownship)) {
            errCompanyTownship.setVisibility(View.VISIBLE);
            errCompanyTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_township, getActivity()));
            errCompanyTownshipLocale = R.string.da_err_township;
        } else {
            errCompanyTownship.setVisibility(View.GONE);
            errCompanyTownshipLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccCompanyTownshipLocale(errCompanyTownshipLocale);

        /*Company Department*/
        if (CommonUtils.isEmptyOrNull(companyDepartment)) {
            errCompanyDepartment.setVisibility(View.VISIBLE);
            errCompanyDepartment.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_department_require_err, getActivity()));
            errCompanyDepartmentLocale = R.string.da_department_require_err;
        } else {
            errCompanyDepartment.setVisibility(View.GONE);
            errCompanyDepartmentLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccCompanyDepartmentLocale(errCompanyDepartmentLocale);

        /*Company Position*/
        if (CommonUtils.isEmptyOrNull(companyPosition)) {
            errCompanyPosition.setVisibility(View.VISIBLE);
            errCompanyPosition.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_position_require_err, getActivity()));
            errCompanyPositionLocale = R.string.da_position_require_err;
        } else {
            errCompanyPosition.setVisibility(View.GONE);
            errCompanyPositionLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccCompanyPositionLocale(errCompanyPositionLocale);

        /*Year of Service*/
        if (serviceYear == 0 && serviceMonth == 0) {
            errServiceYear.setVisibility(View.VISIBLE);
            errServiceYear.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_serviceYear_require_err, getActivity()));
            errServiceYearLocale = R.string.da_serviceYear_require_err;
            errOccuMesgBean.setOccServiceYearLocale(errServiceYearLocale);
        } else if (serviceMonth > 11) {
            errServiceYear.setVisibility(View.VISIBLE);
            errServiceYear.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_yearStayMonth_exceed_err, getActivity()));
            errServiceYearLocale = R.string.da_yearStayMonth_exceed_err;
            errOccuMesgBean.setOccServiceYearLocale(errServiceYearLocale);
        } else {
            errServiceYear.setVisibility(View.GONE);
            errServiceYearLocale = R.string.da_mesg_blank;
            errOccuMesgBean.setOccServiceYearLocale(errServiceYearLocale);
        }

        if (isEmptyOrNull(txtCompanyStatus)) {
            errCompanyStatus.setVisibility(View.VISIBLE);
            errCompanyStatus.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_companyStatus_require_err, getActivity()));
            errCompanyStatusLocale = R.string.da_companyStatus_require_err;
            errOccuMesgBean.setOccCompanyStatusLocale(errCompanyStatusLocale);
        } else {
            errCompanyStatus.setVisibility(View.GONE);
            errCompanyStatusLocale = R.string.da_mesg_blank;
            errOccuMesgBean.setOccCompanyStatusLocale(errCompanyStatusLocale);
            int id = getCompanyStatusId(companyStatus, txtCompanyStatus);
            if (id == COMPANY_STATUS_OTHER) {
                if (isEmptyOrNull(companyStatusDetail)) {
                    occu_companyStatusDetail.setVisibility(View.VISIBLE);
                    errCompanyStatusDetail.setVisibility(View.VISIBLE);
                    errCompanyStatusDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_companyStatusOther_require_err, getActivity()));
                    errCompanyStatusDetailLocale = R.string.da_companyStatusOther_require_err;
                    errOccuMesgBean.setOccCompanyStatusDetailLocale(errCompanyStatusDetailLocale);
                } else {
                    errCompanyStatusDetail.setVisibility(View.GONE);
                    errCompanyStatusDetailLocale = R.string.da_mesg_blank;
                    errOccuMesgBean.setOccCompanyStatusDetailLocale(errCompanyStatusDetailLocale);
                }
            }
        }

        /*Monthly Basic Income*/
        if (monthlyIncome == 0.0) {
            errMonthlyIncome.setVisibility(View.VISIBLE);
            errMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_basicIncome_require_err, getActivity()));
            errMonthlyIncomeLocale = R.string.da_basicIncome_require_err;
        } else {
            errMonthlyIncome.setVisibility(View.GONE);
            errMonthlyIncomeLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccMonthlyIncomeLocale(errMonthlyIncomeLocale);

        if ((!isEmptyOrNull(contactTimeFrom) && isEmptyOrNull(contactTimeTo)) || (isEmptyOrNull(contactTimeFrom) && !isEmptyOrNull(contactTimeTo))) {
            errContactTime.setVisibility(View.VISIBLE);
            errContactTime.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_contactTime_require_err, getActivity()));
            errContactTimeLocale = R.string.da_contactTime_require_err;
        } else {
            errContactTime.setVisibility(View.GONE);
            errContactTimeLocale = R.string.da_mesg_blank;
        }
        errOccuMesgBean.setOccContactTimeLocale(errContactTimeLocale);

        PreferencesManager.saveErrorMesgInfo(getContext(), errOccuMesgBean);
    }

    boolean checkOccupationData() {

        boolean validate = true;
        setUpOccupationFormData();

        /*Company Name*/
        if (CommonUtils.isEmptyOrNull(companyName)) {
            validate = false;
        } else if (!isPureAscii(companyName)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(companyStreet)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(companyQuarter)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(appCompanyCity)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(appCompanyTownship)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(companyTel)) {
            validate = false;
        } else if (!CommonUtils.isTelPhoneNoValid(companyTel)) {
            validate = false;
        }

        /*Company Department*/

        if (CommonUtils.isEmptyOrNull(companyDepartment)) {
            validate = false;
        }

        /*Company Position*/

        if (CommonUtils.isEmptyOrNull(companyPosition)) {
            validate = false;
        }

        /*Year of Service*/
        if (serviceYear == 0 && serviceMonth == 0) {
            validate = false;
        } else if (serviceMonth > 11) {
            validate = false;
        }

        /*Company Status*/
        if (isEmptyOrNull(txtCompanyStatus)) {
            validate = false;
        } else {
            int id = getCompanyStatusId(companyStatus, txtCompanyStatus);
            if (id == COMPANY_STATUS_OTHER) {
                if (isEmptyOrNull(companyStatusDetail)) {
                    validate = false;
                }
            }
        }

        /*Contact Time*/
        if ((!isEmptyOrNull(contactTimeFrom) && isEmptyOrNull(contactTimeTo)) || (isEmptyOrNull(contactTimeFrom) && !isEmptyOrNull(contactTimeTo))) {
            validate = false;
        }

        /*Monthly Basic Income*/
        if (monthlyIncome == 0.0) {
            validate = false;
        }

        /*Total Income*/
        if (totalIncome == 0.0) {
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
        //occupationTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_title, getContext()));

        labelCompanyName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_companyName, getActivity()));
        errCompanyName.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyNameLocale, getActivity()));

        labelCompanyTel.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_companyTelNo, getActivity()));
        errCompanyTel.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyTelLocale, getActivity()));

        labelContactTime.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_contactTime, getActivity()));
        errContactTime.setText(CommonUtils.getLocaleString(new Locale(language), errContactTimeLocale, getActivity()));

        labelCompanyDepartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_department, getActivity()));
        errCompanyDepartment.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyDepartmentLocale, getActivity()));

        labelCompanyPosition.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_position, getActivity()));
        errCompanyPosition.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyPositionLocale, getActivity()));

        labelServiceYear.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_serviceyear, getActivity()));
        errServiceYear.setText(CommonUtils.getLocaleString(new Locale(language), errServiceYearLocale, getActivity()));

        labelCompanyStatusDetail.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_companyStatus, getActivity()));
        errCompanyStatusDetail.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyStatusDetailLocale, getActivity()));
        errCompanyStatus.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyStatusLocale, getActivity()));

        labelMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_basicIncome, getActivity()));
        errMonthlyIncome.setText(CommonUtils.getLocaleString(new Locale(language), errMonthlyIncomeLocale, getActivity()));

        labelValServiceYear.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_stay_year, getActivity()));
        labelValServiceMonth.setText(CommonUtils.getLocaleString(new Locale(language), R.string.register_stay_month, getActivity()));

        labelTotalIncome.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_totalIncome, getActivity()));
        labelSalaryDate.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_salaryDate, getActivity()));
        labelOtherIncome.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_otherIncome, getActivity()));

        lblCompanyTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_occupation_companyAdd, getActivity()));
        lblComApartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_apartment_no, getActivity()));
        lblComRoomNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_room_no, getActivity()));
        lblComFloorNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_floor_no, getActivity()));

        lblComStreet.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_street, getActivity()));
        errCompanyStreet.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyStreetLocale, getActivity()));

        lblComQuarter.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_quarter, getActivity()));
        errCompanyQuarter.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyQuarterLocale, getActivity()));

        lblComTownship.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_township, getActivity()));
        errCompanyTownship.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyTownshipLocale, getActivity()));

        lblComCity.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_city, getActivity()));
        errCompanyCity.setText(CommonUtils.getLocaleString(new Locale(language), errCompanyCityLocale, getActivity()));

        currencyUnit.setText((CommonUtils.getLocaleString(new Locale(language), R.string.mem_card_amt_unit, getActivity())));

        btnNext.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_next_btn, getActivity()));
        btnSave.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_save_btn, getActivity()));

        PreferencesManager.setCurrentLanguage(getContext(), language);

    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    void setUpOccupationFormData() {

        companyName = occu_companyName.getText().toString();
        companyTel = occu_companyTel.getText().toString();
        contactTimeFrom = occu_contactTimeFrom.getText().toString();
        contactTimeTo = occu_contactTimeTo.getText().toString();
        companyDepartment = occu_companyDepartment.getText().toString();
        companyPosition = occu_companyPosition.getText().toString();
        companyStatusDetail = occu_companyStatusDetail.getText().toString();
        txtCompanyStatus = autoCompleteCompanyStatus.getText().toString();

        companyApartmentNo = comApartmentNo.getText().toString();
        companyRoomNo = comRoomNo.getText().toString();
        companyFloorNo = comFloorNo.getText().toString();
        companyStreet = comStreet.getText().toString();
        companyQuarter = comQuarter.getText().toString();
        appCompanyCity = autoCurrentCity.getText().toString();
        appCompanyTownship = autoCurrentTownship.getText().toString();

        if (!occu_monthlyIncome.getText().toString().equals(BLANK)) {
            monthlyIncome = Long.parseLong(occu_monthlyIncome.getText().toString().replaceAll(",", ""));
        } else {
            monthlyIncome = 0;
        }

        if (!occu_otherIncome.getText().toString().equals(BLANK)) {
            otherIncome = Long.parseLong(occu_otherIncome.getText().toString().replaceAll(",", ""));
        } else {
            otherIncome = 0;
        }

        if (!totalIncomeAutoFilled.getText().toString().equals(BLANK)) {
            totalIncome = Long.parseLong(totalIncomeAutoFilled.getText().toString().replaceAll(",", ""));
        } else {
            totalIncome = 0;
        }

    }


    void appLoadInputData() {

        setUpOccupationFormData();

        ApplicationRegisterSaveReqBean registerSaveReqBean
                = new ApplicationRegisterSaveReqBean();
        OccupationDataFormBean occupationDataFormBean
                = new OccupationDataFormBean();

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            registerSaveReqBean = PreferencesManager.getDaftSavedInfo(getActivity());

            if (registerSaveReqBean.getApplicantCompanyInfoDto() != null) {
                occupationDataFormBean.setDaApplicantCompanyInfoId(registerSaveReqBean.getApplicantCompanyInfoDto().getDaApplicantCompanyInfoId());
            }
        }

        setUpOccupationFormData();

        occupationDataFormBean.setCompanyName(companyName);
        occupationDataFormBean.setCompanyTelNo(companyTel);
        occupationDataFormBean.setContactTimeFrom(contactTimeFrom);
        occupationDataFormBean.setContactTimeTo(contactTimeTo);
        occupationDataFormBean.setDepartment(companyDepartment);
        occupationDataFormBean.setPosition(companyPosition);
        occupationDataFormBean.setYearOfServiceYear(serviceYear);
        occupationDataFormBean.setYearOfServiceMonth(serviceMonth);
        occupationDataFormBean.setCompanyStatus(getCompanyStatusId(companyStatus, txtCompanyStatus));
        occupationDataFormBean.setCompanyStatusOther(companyStatusDetail);
        occupationDataFormBean.setMonthlyBasicIncome(Double.parseDouble(Long.toString(monthlyIncome)));
        occupationDataFormBean.setOtherIncome(Double.parseDouble(Long.toString(otherIncome)));
        occupationDataFormBean.setTotalIncome(Double.parseDouble(Long.toString(totalIncome)));
        occupationDataFormBean.setSalaryDay(salaryDate_company);

        occupationDataFormBean.setCompanyAddressBuildingNo(companyApartmentNo);
        occupationDataFormBean.setCompanyAddressRoomNo(companyRoomNo);
        occupationDataFormBean.setCompanyAddressFloor(companyFloorNo);
        occupationDataFormBean.setCompanyAddressStreet(companyStreet);
        occupationDataFormBean.setCompanyAddressQtr(companyQuarter);

        int city_id = getCityId(cityId, cityList, appCompanyCity);
        int town_id = getTownshipId(cityTownshipList, city_id, appCompanyTownship);
        occupationDataFormBean.setCompanyAddressCity(city_id);
        occupationDataFormBean.setCompanyAddressTownship(town_id);

        registerSaveReqBean.setApplicantCompanyInfoDto(occupationDataFormBean);
        PreferencesManager.saveDaftSavedInfo(getActivity(), registerSaveReqBean);
    }

    void setUpDataOnPageChanged() {
        curLang = PreferencesManager.getCurrentLanguage(getContext());
        changeLabel(curLang);
        appLoadInputData();
        MainMenuActivityDrawer.occuDataCorrect = checkOccupationData();
    }

    @Override
    public void onStart() {
        companyStatusCheck = autoCompleteCompanyStatus.getText().toString();
        if (!isEmptyOrNull(companyStatusCheck)) {
            autoCompleteCompanyStatus.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
            displayCompanyStatusDesc(autoCompleteCompanyStatus.getText().toString());
        }

        companyStreetCheck = comStreet.getText().toString();
        if (!isEmptyOrNull(companyStreetCheck)) {
            comStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyQuarterCheck = comQuarter.getText().toString();
        if (!isEmptyOrNull(companyQuarterCheck)) {
            comQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyCityCheck = autoCurrentCity.getText().toString();
        if (!isEmptyOrNull(companyCityCheck)) {
            autoCurrentCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyTownshipCheck = autoCurrentTownship.getText().toString();
        if (!isEmptyOrNull(companyTownshipCheck)) {
            autoCurrentTownship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        companyTelCheck = occu_companyTel.getText().toString();
        if (!isEmptyOrNull(companyTelCheck)) {
            occu_companyTel.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        super.onStart();
        setUpOccupationFormData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    void displayCompanyStatusDesc(String status) {
        if (status.equals("Other")) {
            occu_companyStatusDetail.setVisibility(View.VISIBLE);
        } else {
            occu_companyStatusDetail.setVisibility(View.GONE);
        }
    }

    String getSelectedTime(int hourOfDay, int minute) {
        String lblSelectedTime = BLANK;
        if (hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
        }
        if (hourOfDay < 10) {
            lblSelectedTime = "0" + hourOfDay;
        } else {
            lblSelectedTime = "" + hourOfDay;
        }
        if (minute == 0) {
            lblSelectedTime = lblSelectedTime + " : 00";
        } else if (minute < 10) {
            lblSelectedTime = lblSelectedTime + " : 0" + minute;
        } else {
            lblSelectedTime = lblSelectedTime + " : " + minute;
        }
        return lblSelectedTime;
    }

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

    void setUpCityList(List<CityTownshipResBean> cityTownshipList) {
        cityList = new ArrayList<>();
        cityId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            cityList.add(listBean.getName());
            cityId.add(listBean.getCityId());
        }
    }

    void setUpCurrentTownshipList(List<CityTownshipResBean> cityTownshipList, String cityName) {
        townshipList = new ArrayList<>();
        townshipId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getName().equals(cityName)) {
                townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    townshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
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

    private void setSelectedTownshipList(int cityId) {

        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getCityId() == cityId) {
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    townshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }

                ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                addressTownship.setNotifyOnChange(true);
                autoCurrentTownship.setThreshold(1);
                autoCurrentTownship.setAdapter(addressTownship);
                break;
            }
        }
    }

    String getTownship(List<CityTownshipResBean> cityTownshipList, int cityId, int townId) {
        String township = "";
        townshipList = new ArrayList<>();
        townshipId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getCityId() == cityId) {
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    townshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }

                ArrayAdapter<String> addressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                addressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                addressTownship.setNotifyOnChange(true);
                autoCurrentTownship.setThreshold(1);
                autoCurrentTownship.setAdapter(addressTownship);
                break;
            }
        }

        for (int id = 0; id < townshipId.size(); id++) {
            if (townshipId.get(id) == townId) {
                township = townshipList.get(id);
            }
        }

        return township;
    }

    int getCityId(List<Integer> cityID, List<String> cityName, String name) {
        for (int id = 0; id < cityName.size(); id++) {
            if (cityName.get(id).equals(name)) {
                saveCityid = cityID.get(id);
            }
        }
        return saveCityid;
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
                saveTownshipid = saveTownshipId.get(id);
            }
        }
        return saveTownshipid;
    }

    int getCompanyStatusId(String[] statusList, String companyStatus) {
        for (int id = 0; id < statusList.length; id++) {
            if (statusList[id].equals(companyStatus)) {
                companyStatusId = id + 1;
            }
        }
        return companyStatusId;
    }

    String getCompanyStatusName(String[] statusList, int companyStatusId) {
        if (companyStatusId == 0) {
            return "";
        }
        int id = companyStatusId - 1;
        return statusList[id];
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
