package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
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

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;

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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_ID;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DA_TITLES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.EMERGENCY_RELATION_OTHER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isEmptyOrNull;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isPureAscii;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class SmallLoanEmergencyFragment extends PagerRootFragment implements LanguageChangeListener {

    private View view;
    private Spinner spinnerRelation;

    private TextView labelName;
    private TextView labelRelation;
    private TextView labelMobileNo;
    private TextView labelResidentTel;
    private TextView labelOtherPhone;

    private Integer errNameLocale;
    private Integer errRelationshipDetailLocale;
    private Integer errMobileNoLocale;
    private Integer errResidentTelNoLocale;
    private Integer errOtherPhoneNoLocale;

    private TextView errName;
    private TextView errRelationshipDetail;
    private TextView errMobileNo;
    private TextView errResidentTelNo;
    private TextView errOtherPhoneNo;

    private EditText eme_name;
    private EditText eme_relationshipDetail;
    private EditText eme_mobileNo;
    private EditText eme_residentTel;
    private EditText eme_otherPhone;

    private static String name;
    private static String relationshipDetail;
    private static String mobileNo;
    private static String residentTel;
    private static String otherPhone;
    private static String customerId;

    private static String curLang;
    private static int relationship;
    private int selectedPosition;
    private static SharedPreferences validationPreferences;

    private String nameCheck;
    private String relationshipDetailCheck;
    private String mobileNoCheck;

    Button btnNext;
    Button btnSave;

    private LinearLayout backToOccuData;
    private LinearLayout goToGuarantorData;
    //private TextView occupationTitle;
    //private TextView guarantorTitle;
    //private TextView emergencyTitle;

    private TextView lblEmerTitle;
    private TextView lblEmerApartment;
    private TextView lblEmerRoomNo;
    private TextView lblEmerFloorNo;
    private TextView lblEmerStreet;
    private TextView lblEmerQuarter;
    private TextView lblEmerTownship;
    private TextView lblEmerCity;

    private EditText emerApartmentNo;
    private EditText emerRoomNo;
    private EditText emerFloorNo;
    private EditText emerStreet;
    private EditText emerQuarter;

    private AutoCompleteTextView autoEmergencyTowhship;
    private AutoCompleteTextView autoEmergencyCity;

    private String emergencyApartmentNo;
    private String emergencyRoomNo;
    private String emergencyFloorNo;
    private String emergencyStreet;
    private String emergencyQuarter;

    private TextView errEmergencyStreet;
    private TextView errEmergencyCity;
    private TextView errEmergencyTownship;
    private TextView errEmergencyQuarter;

    private Integer errEmergencyStreetLocale;
    private Integer errEmergencyCityLocale;
    private Integer errEmergencyTownshipLocale;
    private Integer errEmergencyQuarterLocale;

    private String emergencyStreetCheck;
    private String emergencyQuarterCheck;
    private String emergencyCityCheck;
    private String emergencyTownshipCheck;

    private int emergencyTownship;
    private int emergencyCity;

    private String curCityName;
    private List<String> cityList;
    private List<String> townshipList;
    private List<Integer> cityId;
    private List<Integer> townshipId;
    private List<TownshipListBean> townshipBeanList = new ArrayList<>();
    private List<CityTownshipResBean> cityTownshipList;

    private int saveCityid;
    private int saveTownshipid;
    private String appEmerCity;
    private String appEmerTownship;

    private StateProgressBar emerStepView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_small_loan_emergency, container, false);
        setHasOptionsMenu(true);

        String[] pages = {"Application\nData", "Occupation\nData", "Emergency\nContact", "Guarantor\nData", "Loan\nConfirmation"};
        emerStepView = view.findViewById(R.id.emer_stepped_bar);
        emerStepView.setStateDescriptionData(pages);

        emerStepView.setOnStateItemClickListener(new OnStateItemClickListener() {
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
        curLang = PreferencesManager.getCurrentLanguage(getContext());

        validationPreferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        customerId = PreferencesManager.getStringEntryFromPreferences(validationPreferences, CUSTOMER_ID);

        StepsView stepsView = view.findViewById(R.id.stepsView_3);
        stepsView.setLabels(DA_TITLES)
                .setBarColorIndicator(getContext().getColor(R.color.gray))
                .setProgressColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setLabelColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setCompletedPosition(2)
                .drawView();

        // Emergency Address Label
        lblEmerTitle = view.findViewById(R.id.emergency_address);
        lblEmerApartment = view.findViewById(R.id.emer_txt_apartment_no);
        lblEmerRoomNo = view.findViewById(R.id.emer_txt_room_no);
        lblEmerFloorNo = view.findViewById(R.id.emer_txt_floor_no);
        lblEmerStreet = view.findViewById(R.id.emer_txt_street);
        lblEmerQuarter = view.findViewById(R.id.emer_txt_quarter);
        lblEmerTownship = view.findViewById(R.id.emer_txt_township);
        lblEmerCity = view.findViewById(R.id.emer_txt_city);

        errResidentTelNo = view.findViewById(R.id.emergency_err_residentTelNo);
        errOtherPhoneNo = view.findViewById(R.id.emergency_err_otherPhoneNo);

        // Emergency Address EditText
        emerApartmentNo = view.findViewById(R.id.emer_apartment_no);
        emerRoomNo = view.findViewById(R.id.emer_room_no);
        emerFloorNo = view.findViewById(R.id.emer_floor_no);
        emerStreet = view.findViewById(R.id.emer_street);
        emerQuarter = view.findViewById(R.id.emer_quarter);

        // Emergency Address Spinner
        autoEmergencyTowhship = view.findViewById(R.id.emer_township);
        autoEmergencyCity = view.findViewById(R.id.emer_city);
        spinnerRelation = view.findViewById(R.id.spinner_relationship);
        cityTownshipList = PreferencesManager.getCityListInfo(getActivity());
        setUpCityList(cityTownshipList);
        curCityName = cityList.get(0);
        setUpCurrentTownshipList(cityTownshipList, curCityName);

        // Emergency Current City
        ArrayAdapter<String> emergencyAddressCity = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, cityList);
        emergencyAddressCity.setDropDownViewResource(R.layout.dialog_nrc_division);
        emergencyAddressCity.setNotifyOnChange(true);
        autoEmergencyCity.setThreshold(1);
        autoEmergencyCity.setAdapter(emergencyAddressCity);

        autoEmergencyCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoEmergencyCity.setText((String) parent.getAdapter().getItem(position));
                emergencyCity = cityId.get(position);
                curCityName = (String) parent.getAdapter().getItem(position);
                setUpCurrentTownshipList(cityTownshipList, curCityName);
                autoEmergencyTowhship.setText(BLANK);
                autoEmergencyTowhship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));

                // Emergency Current Township
                final ArrayAdapter<String> emergencyAddressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                emergencyAddressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                emergencyAddressTownship.setNotifyOnChange(true);
                autoEmergencyTowhship.setThreshold(1);
                autoEmergencyTowhship.setAdapter(emergencyAddressTownship);

            }
        });

        autoEmergencyCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoEmergencyCity.showDropDown();
                    autoEmergencyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!cityList.contains(autoEmergencyCity.getText().toString())) {
                        autoEmergencyCity.setText(BLANK);
                        autoEmergencyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        autoEmergencyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoEmergencyCity.showDropDown();
            }
        });

        autoEmergencyTowhship.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoEmergencyTowhship.setText((String) parent.getAdapter().getItem(position));
                emergencyTownship = townshipId.get(position);
            }
        });

        autoEmergencyTowhship.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    autoEmergencyTowhship.showDropDown();
                    autoEmergencyTowhship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));

                } else {
                    if (!townshipList.contains(autoEmergencyTowhship.getText().toString())) {
                        autoEmergencyTowhship.setText(BLANK);
                        autoEmergencyTowhship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        autoEmergencyTowhship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoEmergencyTowhship.showDropDown();
            }
        });

        emergencyStreetCheck = emerStreet.getText().toString();
        if (!isEmptyOrNull(emergencyStreetCheck)) {
            emerStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emergencyQuarterCheck = emerQuarter.getText().toString();
        if (!isEmptyOrNull(emergencyQuarterCheck)) {
            emerQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emergencyCityCheck = autoEmergencyCity.getText().toString();
        if (!isEmptyOrNull(emergencyCityCheck)) {
            autoEmergencyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emergencyTownshipCheck = autoEmergencyTowhship.getText().toString();
        if (!isEmptyOrNull(emergencyTownshipCheck)) {
            autoEmergencyTowhship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emerStreet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    emerStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    emergencyStreetCheck = emerStreet.getText().toString();
                    if (isEmptyOrNull(emergencyStreetCheck)) {
                        emerStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        emerQuarter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    emerQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    emergencyQuarterCheck = emerQuarter.getText().toString();
                    if (isEmptyOrNull(emergencyQuarterCheck)) {
                        emerQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        final String[] applicantRelation = getResources().getStringArray(R.array.applicant_relation);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getActivity(), R.layout.relation_spinner, applicantRelation);
        spinnerRelation.setAdapter(adapterType);

        btnNext = view.findViewById(R.id.btn_energency_next);

        labelName = view.findViewById(R.id.emer_contact_name);
        labelRelation = view.findViewById(R.id.emer_relation);
        labelMobileNo = view.findViewById(R.id.emer_mobile_no);
        labelResidentTel = view.findViewById(R.id.emer_resident);
        labelOtherPhone = view.findViewById(R.id.emer_other_no);

        errName = view.findViewById(R.id.emergency_err_name);
        errNameLocale = R.string.da_mesg_blank;

        errRelationshipDetail = view.findViewById(R.id.emergency_err_relationship);
        errRelationshipDetailLocale = R.string.da_mesg_blank;

        errMobileNo = view.findViewById(R.id.emergency_err_mobileNo);
        errMobileNoLocale = R.string.da_mesg_blank;
        ;

        errEmergencyStreet = view.findViewById(R.id.emergency_err_street);
        errEmergencyStreetLocale = R.string.da_mesg_blank;

        errEmergencyQuarter = view.findViewById(R.id.emergency_err_quarter);
        errEmergencyQuarterLocale = R.string.da_mesg_blank;

        errEmergencyCity = view.findViewById(R.id.emergency_err_city);
        errEmergencyCityLocale = R.string.da_mesg_blank;

        errEmergencyTownship = view.findViewById(R.id.emergency_err_township);
        errEmergencyTownshipLocale = R.string.da_mesg_blank;

        eme_name = view.findViewById(R.id.emergency_name);
        eme_relationshipDetail = view.findViewById(R.id.emergency_relationship);
        eme_mobileNo = view.findViewById(R.id.emergency_mobileNo);
        eme_residentTel = view.findViewById(R.id.emergency_residentTel);
        eme_otherPhone = view.findViewById(R.id.emergency_other_no);

        eme_name.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});
        emerApartmentNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        emerRoomNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        emerFloorNo.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        emerStreet.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        emerQuarter.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(100)});
        eme_relationshipDetail.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(256)});

        ScrollView scrollView = view.findViewById(R.id.emergency_scroll);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDataOnPageChanged();
                viewPager.setCurrentItem(3, true);
            }
        });

        btnSave = view.findViewById(R.id.btn_emerdata_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEmergencyContactToDatabase();
            }
        });

        //guarantorTitle = view.findViewById(R.id.da_gua_data_title);
        //emergencyTitle = view.findViewById(R.id.da_emer_data_title);
        //occupationTitle = view.findViewById(R.id.da_occu_title);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                if (selectedPosition == 2) {
                    ((MainMenuActivityDrawer) getActivity()).setLanguageListener(SmallLoanEmergencyFragment.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (selectedPosition == 3) {
                    setUpDataOnPageChanged();

                } else if (selectedPosition == 2) {
                    curLang = PreferencesManager.getCurrentLanguage(getContext());
                    changeLabel(curLang);

                    if (MainMenuActivityDrawer.isSubmitclickEmerData) {
                        MainMenuActivityDrawer.isSubmitclickEmerData = false;
                        showValidationMsg(curLang);
                    }
                }
            }

        });

        changeLabel(curLang);
        replaceLastEmergencyInfo();

        nameCheck = eme_name.getText().toString();
        if (!isEmptyOrNull(nameCheck)) {
            eme_name.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        eme_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    eme_name.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    nameCheck = eme_name.getText().toString();
                    if (isEmptyOrNull(nameCheck)) {
                        eme_name.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        relationshipDetailCheck = eme_relationshipDetail.getText().toString();
        if (!isEmptyOrNull(relationshipDetailCheck)) {
            eme_relationshipDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        eme_relationshipDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    eme_relationshipDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    relationshipDetailCheck = eme_relationshipDetail.getText().toString();
                    if (isEmptyOrNull(relationshipDetailCheck)) {
                        eme_relationshipDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });

        mobileNoCheck = eme_mobileNo.getText().toString();
        if (!isEmptyOrNull(mobileNoCheck)) {
            eme_mobileNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        eme_mobileNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    eme_mobileNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    mobileNoCheck = eme_mobileNo.getText().toString();
                    if (isEmptyOrNull(mobileNoCheck)) {
                        eme_mobileNo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        return super.onOptionsItemSelected(item);
    }

    private void saveEmergencyContactToDatabase() {
        setUpEmergencyContactFormData();
        ApplicationRegisterSaveReqBean saveDataBean
                = new ApplicationRegisterSaveReqBean();

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            saveDataBean = PreferencesManager.getDaftSavedInfo(getActivity());
        }

        saveDataBean.setDaApplicationTypeId(APPLICATION_PLOAN);
        saveDataBean.setCustomerId(Integer.parseInt(customerId));
        saveDataBean.setChannelType(CHANNEL_MOBILE);

        EmergencyContactFormBean emergencyContact = new EmergencyContactFormBean();
        emergencyContact.setName(name);
        emergencyContact.setRelationship(relationship);
        emergencyContact.setRelationshipOther(relationshipDetail);
        emergencyContact.setMobileNo(mobileNo);
        emergencyContact.setResidentTelNo(residentTel);
        emergencyContact.setOtherPhoneNo(otherPhone);

        emergencyContact.setCurrentAddressBuildingNo(emergencyApartmentNo);
        emergencyContact.setCurrentAddressRoomNo(emergencyRoomNo);
        emergencyContact.setCurrentAddressFloor(emergencyFloorNo);
        emergencyContact.setCurrentAddressStreet(emergencyStreet);
        emergencyContact.setCurrentAddressQtr(emergencyQuarter);

        int city_id = getCityId(cityId, cityList, appEmerCity);
        int town_id = getTownshipId(cityTownshipList, city_id, appEmerTownship);

        emergencyContact.setCurrentAddressCity(city_id);
        emergencyContact.setCurrentAddressTownship(town_id);

        saveDataBean.setEmergencyContactInfoDto(emergencyContact);

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
                                    showSnackBarMessage("Emergency Contact Data saved.");
                                } else {
                                    saveDialog.dismiss();
                                    showSnackBarMessage("Emergency Contact Data cannot be saved.");
                                }
                            } else {
                                saveDialog.dismiss();
                                showSnackBarMessage("Emergency Contact Data cannot be saved.");
                            }
                        } else {
                            closeDialog(saveDialog);
                            showSnackBarMessage("Emergency Contact Data cannot be saved.");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<ApplicationRegisterSaveReqBean>> call, Throwable t) {
                        closeDialog(saveDialog);
                        showSnackBarMessage("Emergency Contact Data save failed.");
                    }
                });
            }
        } else {
            showSnackBarMessage("Emergency Contact Data is required to save.");
        }
    }

    private void replaceLastEmergencyInfo() {

        if (PreferencesManager.isDaftSavedErrExisted(getActivity())) {
            ApplicationFormErrMesgBean savedInformation
                    = PreferencesManager.getErrMesgInfo(getContext());

            errNameLocale = savedInformation.getEmergencyNameLocale();
            errRelationshipDetailLocale = savedInformation.getEmergencyRelationshipDetailLocale();
            errMobileNoLocale = savedInformation.getEmergencyMobileNoLocale();
            errEmergencyStreetLocale = savedInformation.getEmergencyStreetLocale();
            errEmergencyCityLocale = savedInformation.getEmergencyCityLocale();
            errEmergencyTownshipLocale = savedInformation.getEmergencyTownshipLocale();
            errEmergencyQuarterLocale = savedInformation.getEmergencyQuarterLocale();

            if (errEmergencyStreetLocale != R.string.da_mesg_blank) {
                errEmergencyStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmergencyStreetLocale, getContext()));
                errEmergencyStreet.setVisibility(View.VISIBLE);
            }

            if (errEmergencyQuarterLocale != R.string.da_mesg_blank) {
                errEmergencyQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmergencyQuarterLocale, getContext()));
                errEmergencyQuarter.setVisibility(View.VISIBLE);
            }

            if (errEmergencyCityLocale != R.string.da_mesg_blank) {
                errEmergencyCity.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmergencyCityLocale, getContext()));
                errEmergencyCity.setVisibility(View.VISIBLE);
            }

            if (errEmergencyTownshipLocale != R.string.da_mesg_blank) {
                errEmergencyTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmergencyTownshipLocale, getContext()));
                errEmergencyTownship.setVisibility(View.VISIBLE);
            }

            if (errNameLocale != R.string.da_mesg_blank) {
                errName.setText(CommonUtils.getLocaleString(new Locale(curLang), errNameLocale, getContext()));
                errName.setVisibility(View.VISIBLE);
            }

            if (errRelationshipDetailLocale != R.string.da_mesg_blank) {
                errRelationshipDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), errRelationshipDetailLocale, getContext()));
                errRelationshipDetail.setVisibility(View.VISIBLE);
            }

            if (errMobileNoLocale != R.string.da_mesg_blank) {
                errMobileNo.setText(CommonUtils.getLocaleString(new Locale(curLang), errMobileNoLocale, getContext()));
                errMobileNo.setVisibility(View.VISIBLE);
            }
        }

        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {

            ApplicationRegisterSaveReqBean savedInformation
                    = PreferencesManager.getDaftSavedInfo(getActivity());

            try {
                EmergencyContactFormBean emerLastInfo = savedInformation.getEmergencyContactInfoDto();
                if (emerLastInfo != null) {
                    eme_name.setText(emerLastInfo.getName());
                    eme_relationshipDetail.setText(emerLastInfo.getRelationshipOther());
                    eme_mobileNo.setText(emerLastInfo.getMobileNo());
                    eme_residentTel.setText(emerLastInfo.getResidentTelNo());
                    eme_otherPhone.setText(emerLastInfo.getOtherPhoneNo());
                    spinnerRelation.setSelection(emerLastInfo.getRelationship() - 1);

                    emerApartmentNo.setText(emerLastInfo.getCurrentAddressBuildingNo());
                    emerRoomNo.setText(emerLastInfo.getCurrentAddressRoomNo());
                    emerFloorNo.setText(emerLastInfo.getCurrentAddressFloor());
                    emerStreet.setText(emerLastInfo.getCurrentAddressStreet());
                    emerQuarter.setText(emerLastInfo.getCurrentAddressQtr());

                    if (emerLastInfo.getCurrentAddressCity() == 0) {
                        autoEmergencyCity.setText("");
                    } else {
                        autoEmergencyCity.setText(getCity(cityId, cityList, emerLastInfo.getCurrentAddressCity()));
                        this.setSelectedTownshipList(emerLastInfo.getCurrentAddressCity());

                        setUpCurrentTownshipList(cityTownshipList, autoEmergencyCity.getText().toString());
                        final ArrayAdapter<String> emergencyAddressTownship = new ArrayAdapter<String>(getActivity(), R.layout.nrc_spinner_item_2, townshipList);
                        emergencyAddressTownship.setDropDownViewResource(R.layout.dialog_nrc_division);
                        emergencyAddressTownship.setNotifyOnChange(true);
                        autoEmergencyTowhship.setThreshold(1);
                        autoEmergencyTowhship.setAdapter(emergencyAddressTownship);
                    }
                    if (emerLastInfo.getCurrentAddressTownship() == 0) {
                        autoEmergencyTowhship.setText("");
                    } else {
                        autoEmergencyTowhship.setText(getTownship(cityTownshipList, emerLastInfo.getCurrentAddressCity(), emerLastInfo.getCurrentAddressTownship()));
                    }

                    errName.setText(CommonUtils.getLocaleString(new Locale(curLang), errNameLocale, getActivity()));
                    errEmergencyStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmergencyStreetLocale, getActivity()));
                    errEmergencyCity.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmergencyCityLocale, getActivity()));
                    errEmergencyTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), errEmergencyTownshipLocale, getActivity()));
                    errRelationshipDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), errRelationshipDetailLocale, getActivity()));
                    errMobileNo.setText(CommonUtils.getLocaleString(new Locale(curLang), errMobileNoLocale, getActivity()));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void showValidationMsg(String curLang) {
        setUpEmergencyContactFormData();
        ApplicationFormErrMesgBean errEmerMesgBean = PreferencesManager.getErrMesgInfo(getContext());
        if (errEmerMesgBean == null) {
            errEmerMesgBean = new ApplicationFormErrMesgBean();
        }

        /*Name*/
        if (CommonUtils.isEmptyOrNull(name)) {
            errName.setVisibility(View.VISIBLE);
            errName.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.register_name_err, getActivity()));
            errNameLocale = R.string.register_name_err;
        } else {
            errName.setVisibility(View.GONE);
            errNameLocale = R.string.da_mesg_blank;
        }
        errEmerMesgBean.setEmergencyNameLocale(errNameLocale);

        /*Emergency Contact Quarter*/
        if (CommonUtils.isEmptyOrNull(emergencyQuarter)) {
            errEmergencyQuarter.setVisibility(View.VISIBLE);
            errEmergencyQuarter.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_quarter, getActivity()));
            errEmergencyQuarterLocale = R.string.da_err_quarter;
        } else {
            errEmergencyQuarter.setVisibility(View.GONE);
            errEmergencyQuarterLocale = R.string.da_mesg_blank;
        }
        errEmerMesgBean.setEmergencyQuarterLocale(errEmergencyQuarterLocale);

        /*Emergency street*/
        if (CommonUtils.isEmptyOrNull(emergencyStreet)) {
            errEmergencyStreet.setVisibility(View.VISIBLE);
            errEmergencyStreet.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_street, getActivity()));
            errEmergencyStreetLocale = R.string.da_err_street;
        } else {
            errEmergencyStreet.setVisibility(View.GONE);
            errEmergencyStreetLocale = R.string.da_mesg_blank;
        }
        errEmerMesgBean.setEmergencyStreetLocale(errEmergencyStreetLocale);

        /*Emergency City*/
        if (CommonUtils.isEmptyOrNull(appEmerCity)) {
            errEmergencyCity.setVisibility(View.VISIBLE);
            errEmergencyCity.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_city, getActivity()));
            errEmergencyCityLocale = R.string.da_err_city;
        } else {
            errEmergencyCity.setVisibility(View.GONE);
            errEmergencyCityLocale = R.string.da_mesg_blank;
        }
        errEmerMesgBean.setEmergencyCityLocale(errEmergencyCityLocale);

        /*Emergency Township*/
        if (CommonUtils.isEmptyOrNull(appEmerTownship)) {
            errEmergencyTownship.setVisibility(View.VISIBLE);
            errEmergencyTownship.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_err_township, getActivity()));
            errEmergencyTownshipLocale = R.string.da_err_township;
        } else {
            errEmergencyTownship.setVisibility(View.GONE);
            errEmergencyTownshipLocale = R.string.da_mesg_blank;
        }
        errEmerMesgBean.setEmergencyTownshipLocale(errEmergencyTownshipLocale);

        /*Relationship*/
        if (relationship == EMERGENCY_RELATION_OTHER) {
            if (CommonUtils.isEmptyOrNull(relationshipDetail)) {
                errRelationshipDetail.setVisibility(View.VISIBLE);
                errRelationshipDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_relationship_require_err, getActivity()));
                errRelationshipDetailLocale = R.string.da_relationship_require_err;
            } else {
                errRelationshipDetail.setVisibility(View.GONE);
                errRelationshipDetailLocale = R.string.da_mesg_blank;
            }
            errEmerMesgBean.setEmergencyRelationshipDetailLocale(errRelationshipDetailLocale);
        }

        /*Mobile No.*/
        if (CommonUtils.isEmptyOrNull(mobileNo)) {
            errMobileNo.setVisibility(View.VISIBLE);
            errMobileNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_MobileNo_require_err, getActivity()));
            errMobileNoLocale = R.string.da_MobileNo_require_err;
            errEmerMesgBean.setEmergencyMobileNoLocale(errMobileNoLocale);

        } else if (!CommonUtils.isPhoneNoValid(mobileNo)) {
            errMobileNo.setVisibility(View.VISIBLE);
            errMobileNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_MobileNo_format_err, getActivity()));
            errMobileNoLocale = R.string.da_MobileNo_format_err;
            errEmerMesgBean.setEmergencyMobileNoLocale(errMobileNoLocale);
        } else {
            errMobileNo.setVisibility(View.GONE);
            errMobileNoLocale = R.string.da_mesg_blank;
            errEmerMesgBean.setEmergencyMobileNoLocale(errMobileNoLocale);
        }

        /*Residence No.*/
        if (!CommonUtils.isEmptyOrNull(residentTel)) {
            if (!CommonUtils.isTelPhoneNoValid(residentTel)) {
                errResidentTelNo.setVisibility(View.VISIBLE);
                errResidentTelNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_residentTelNo_format_err, getActivity()));
                errResidentTelNoLocale = R.string.da_residentTelNo_format_err;
            } else {
                errResidentTelNo.setVisibility(View.GONE);
                errResidentTelNoLocale = R.string.da_mesg_blank;
            }
        } else {
            errResidentTelNo.setVisibility(View.GONE);
            errResidentTelNoLocale = R.string.da_mesg_blank;
        }
        errEmerMesgBean.setEmergencyResidentTelNoLocale(errResidentTelNoLocale);

        /*Other Phone No.*/
        if (!CommonUtils.isEmptyOrNull(otherPhone)) {
            if (!CommonUtils.isPhoneNoValid(otherPhone)) {
                errOtherPhoneNo.setVisibility(View.VISIBLE);
                errOtherPhoneNo.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_otherPhoneNo_format_err, getActivity()));
                errOtherPhoneNoLocale = R.string.da_otherPhoneNo_format_err;
            } else {
                errOtherPhoneNo.setVisibility(View.GONE);
                errOtherPhoneNoLocale = R.string.da_mesg_blank;
            }
        } else {
            errOtherPhoneNo.setVisibility(View.GONE);
            errOtherPhoneNoLocale = R.string.da_mesg_blank;
        }
        errEmerMesgBean.setEmergencyOtherPhoneNoLocale(errOtherPhoneNoLocale);

        PreferencesManager.saveErrorMesgInfo(getContext(), errEmerMesgBean);

    }

    private boolean checkEmergencyData() {

        boolean validate = true;
        setUpEmergencyContactFormData();

        /*Name*/
        if (CommonUtils.isEmptyOrNull(name)) {
            validate = false;
        }

        /*Relationship*/
        if (relationship == EMERGENCY_RELATION_OTHER) {
            if (CommonUtils.isEmptyOrNull(relationshipDetail)) {   // myanmar language need to set
                validate = false;
            }
        }

        /*Mobile No.*/
        if (CommonUtils.isEmptyOrNull(mobileNo)) {
            validate = false;
        } else if (!CommonUtils.isPhoneNoValid(mobileNo)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(emergencyStreet)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(emergencyQuarter)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(appEmerCity)) {
            validate = false;
        }

        if (CommonUtils.isEmptyOrNull(appEmerTownship)) {
            validate = false;
        }

        if (!CommonUtils.isEmptyOrNull(residentTel)) {
            if (!CommonUtils.isTelPhoneNoValid(residentTel)) {
                validate = false;
            }
        }

        /*Other Phone No.*/
        if (!CommonUtils.isEmptyOrNull(otherPhone)) {
            if (!CommonUtils.isPhoneNoValid(otherPhone)) {
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

        //emergencyTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_emergency_title, getContext()));

        labelName.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_emergency_name, getActivity()));
        labelRelation.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_emergency_relationship, getActivity()));
        labelMobileNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_emergency_mobileNo, getActivity()));
        labelResidentTel.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_emergency_residentTelNo, getActivity()));
        labelOtherPhone.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_emergency_otherPhoneNo, getActivity()));

        errName.setText(CommonUtils.getLocaleString(new Locale(language), errNameLocale, getActivity()));
        errEmergencyStreet.setText(CommonUtils.getLocaleString(new Locale(language), errEmergencyStreetLocale, getActivity()));
        errEmergencyCity.setText(CommonUtils.getLocaleString(new Locale(language), errEmergencyCityLocale, getActivity()));
        errEmergencyTownship.setText(CommonUtils.getLocaleString(new Locale(language), errEmergencyTownshipLocale, getActivity()));
        errEmergencyQuarter.setText(CommonUtils.getLocaleString(new Locale(language), errEmergencyQuarterLocale, getActivity()));

        errRelationshipDetail.setText(CommonUtils.getLocaleString(new Locale(language), errRelationshipDetailLocale, getActivity()));
        errMobileNo.setText(CommonUtils.getLocaleString(new Locale(language), errMobileNoLocale, getActivity()));

        lblEmerTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_currAddress, getActivity()));
        lblEmerApartment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_apartment_no, getActivity()));
        lblEmerRoomNo.setText
                (CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_room_no, getActivity()));
        lblEmerFloorNo.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_floor_no, getActivity()));
        lblEmerStreet.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_street, getActivity()));
        lblEmerQuarter.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_quarter, getActivity()));
        lblEmerTownship.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_township, getActivity()));
        lblEmerCity.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_city, getActivity()));

        btnNext.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_next_btn, getActivity()));
        btnSave.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_applicant_save_btn, getActivity()));

        PreferencesManager.setCurrentLanguage(getContext(), language);
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    void setUpEmergencyContactFormData() {
        name = eme_name.getText().toString();
        relationshipDetail = eme_relationshipDetail.getText().toString();
        mobileNo = eme_mobileNo.getText().toString();
        residentTel = eme_residentTel.getText().toString();
        otherPhone = eme_otherPhone.getText().toString();

        emergencyApartmentNo = emerApartmentNo.getText().toString();
        emergencyRoomNo = emerRoomNo.getText().toString();
        emergencyFloorNo = emerFloorNo.getText().toString();
        emergencyStreet = emerStreet.getText().toString();
        emergencyQuarter = emerQuarter.getText().toString();
        appEmerCity = autoEmergencyCity.getText().toString();
        appEmerTownship = autoEmergencyTowhship.getText().toString();
    }

    void appLoadInputData() {
        setUpEmergencyContactFormData();

        ApplicationRegisterSaveReqBean registerSaveReqBean
                = new ApplicationRegisterSaveReqBean();

        EmergencyContactFormBean emergencyContactFormBean
                = new EmergencyContactFormBean();
        if (PreferencesManager.isDaftSavedInfoExisted(getActivity())) {
            registerSaveReqBean = PreferencesManager.getDaftSavedInfo(getActivity());

            if (registerSaveReqBean.getEmergencyContactInfoDto() != null) {
                emergencyContactFormBean.setDaEmergencyContactInfoId(registerSaveReqBean.getEmergencyContactInfoDto().getDaEmergencyContactInfoId());
            }
        }

        setUpEmergencyContactFormData();

        emergencyContactFormBean.setName(name);
        emergencyContactFormBean.setRelationship(relationship);
        emergencyContactFormBean.setRelationshipOther(relationshipDetail);
        emergencyContactFormBean.setMobileNo(mobileNo);
        emergencyContactFormBean.setResidentTelNo(residentTel);
        emergencyContactFormBean.setOtherPhoneNo(otherPhone);

        emergencyContactFormBean.setCurrentAddressBuildingNo(emergencyApartmentNo);
        emergencyContactFormBean.setCurrentAddressRoomNo(emergencyRoomNo);
        emergencyContactFormBean.setCurrentAddressFloor(emergencyFloorNo);
        emergencyContactFormBean.setCurrentAddressStreet(emergencyStreet);
        emergencyContactFormBean.setCurrentAddressQtr(emergencyQuarter);

        int city_id = getCityId(cityId, cityList, appEmerCity);
        int town_id = getTownshipId(cityTownshipList, city_id, appEmerTownship);

        emergencyContactFormBean.setCurrentAddressCity(city_id);
        emergencyContactFormBean.setCurrentAddressTownship(town_id);

        registerSaveReqBean.setEmergencyContactInfoDto(emergencyContactFormBean);
        PreferencesManager.saveDaftSavedInfo(getActivity(), registerSaveReqBean);

    }

    void setUpDataOnPageChanged() {
        appLoadInputData();
        MainMenuActivityDrawer.emerDataCorrect = checkEmergencyData();
    }

    @Override
    public void onStart() {
        super.onStart();
        relationshipDetailCheck = eme_relationshipDetail.getText().toString();
        if (!isEmptyOrNull(relationshipDetailCheck)) {
            eme_relationshipDetail.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emergencyStreetCheck = emerStreet.getText().toString();
        if (!isEmptyOrNull(emergencyStreetCheck)) {
            emerStreet.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emergencyQuarterCheck = emerQuarter.getText().toString();
        if (!isEmptyOrNull(emergencyQuarterCheck)) {
            emerQuarter.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emergencyCityCheck = autoEmergencyCity.getText().toString();
        if (!isEmptyOrNull(emergencyCityCheck)) {
            autoEmergencyCity.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }

        emergencyTownshipCheck = autoEmergencyTowhship.getText().toString();
        if (!isEmptyOrNull(emergencyTownshipCheck)) {
            autoEmergencyTowhship.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
    }

    void displayRelationshipDesc() {
        if (relationship == 5) {
            eme_relationshipDetail.setVisibility(View.VISIBLE);
        } else {
            eme_relationshipDetail.setVisibility(View.GONE);
            errRelationshipDetail.setText(BLANK);
            errRelationshipDetail.setVisibility(View.GONE);
        }
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
                autoEmergencyTowhship.setThreshold(1);
                autoEmergencyTowhship.setAdapter(addressTownship);
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
                autoEmergencyTowhship.setThreshold(1);
                autoEmergencyTowhship.setAdapter(addressTownship);
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

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment());
    }

}