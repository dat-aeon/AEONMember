package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.adapters.ApplicationDetailAttachRVAdapter;
import mm.com.aeon.vcsaeon.adapters.DAEnquiryAttachEditRVAdapter;
import mm.com.aeon.vcsaeon.adapters.DAEnquiryListRVAdapter;
import mm.com.aeon.vcsaeon.adapters.PurchaseDetailOtherAttachRVAdapter;
import mm.com.aeon.vcsaeon.beans.ApplicationAttachInfoEditResBean;
import mm.com.aeon.vcsaeon.beans.ApplicationDetailInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationDetailInfoResBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoAttachmentResBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoResBean;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.CustAgreementListDto;
import mm.com.aeon.vcsaeon.beans.DAEnquiryResBean;
import mm.com.aeon.vcsaeon.beans.EmergencyContactFormBean;
import mm.com.aeon.vcsaeon.beans.GuarantorDetailFormBean;
import mm.com.aeon.vcsaeon.beans.LoanTypeBean;
import mm.com.aeon.vcsaeon.beans.OccupationDataFormBean;
import mm.com.aeon.vcsaeon.beans.ProductTypeListBean;
import mm.com.aeon.vcsaeon.beans.PurchaseAttachEditReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseAttachEditTempBean;
import mm.com.aeon.vcsaeon.beans.PurchaseAttachInfoEditReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseDetailInfoCancelReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseDetailInfoReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseDetailInfoResBean;
import mm.com.aeon.vcsaeon.beans.PurchaseInfoAttachmentResBean;
import mm.com.aeon.vcsaeon.beans.PurchaseProductInfoBean;
import mm.com.aeon.vcsaeon.beans.StatusReadFlagReq;
import mm.com.aeon.vcsaeon.beans.TownshipListBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CameraUtil;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.ApplicationDetailAttachDelegate;
import mm.com.aeon.vcsaeon.delegates.DAEnquiryAttachEditDelegate;
import mm.com.aeon.vcsaeon.delegates.DAEnquiryDelegate;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.PurchaseOthersAttachDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import mm.com.aeon.vcsaeon.views.customviews.SendMessageImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BIRTH_DATE_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CAMERA;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.FEMALE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.GALLERY;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOAN_TERM_PERIOD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MALE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MARRIED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MYANMAR_CURRENCY;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_M_LOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_NON_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_P_LOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SINGLE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_APPROVE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_CANCEL;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_COMPLETE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_ON_PROCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_UNSUCESSFUL;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_AGREEMENT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_CASH_RECEIPT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_INVOICE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_MEMBER_CARD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_OTHERS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_ULOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.ATTACHMENT_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.addEditPhotoInfo;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.dateToString;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.dateToString2;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isEmptyOrNull;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class DAEnquiryFragment extends BaseFragment implements DAEnquiryDelegate,
        PurchaseOthersAttachDelegate, DAEnquiryAttachEditDelegate, ApplicationDetailAttachDelegate, LanguageChangeListener {

    View view;

    Button btnSearch;
    Button btnClear;

    TextView loanType;
    TextView lblLcStatus;

    EditText textApplicationNo1;
    EditText textApplicationNo2;
    EditText textApplicationNo3;
    EditText textAppliedDate;

    RecyclerView rvEnquiryList;
    RecyclerView rvOtherAttachments;
    static DAEnquiryListRVAdapter adapter;


    int selectedStatusId;

    /*Spinner spinnerLoanType;
    int[] loanTypeIds = null;
    int selectedLoanTypeId;
    TextView tvCategorySearched;*/

    UserInformationFormBean userInformationFormBean;

    static List<ApplicationInfoResBean> applicationInfoList;
    PurchaseDetailOtherAttachRVAdapter otherAttachRVAdapter;

    static int curDaAppInfoAttachmentId;
    static SendMessageImageView imgCurrentAttach;
    static int curFileType;
    static String curFilePath;
    /*static String textLoanType;*/

    private String mCurrentPhotoPath;
    private final int REQUEST_TAKE_PHOTO = 747;
    private final int REQUEST_LOAD_IMAGE = 787;
    private String curLang;

    private List<String> cityList;
    private List<String> townshipList;
    private List<Integer> cityId;
    private List<Integer> townshipId;
    private List<CityTownshipResBean> cityTownshipList;
    private String[] companyStatus;
    private String[] productCategory;
    private int[] productId;
    List<ProductTypeListBean> productTypeList;

    public DAEnquiryAttachEditRVAdapter attachEditRVAdapter;
    public List<ApplicationAttachInfoEditResBean> applicationAttachInfoEditResBeanList;
    private String rootFolderName;
    static int editPosition;
    boolean success;
    private boolean isMember;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.application_inqury_layout, container, false);
        setHasOptionsMenu(true);

        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(DAEnquiryFragment.this);
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        TextView menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        if (isMemberUser()) {
            menuBarLevelInfo.setText(R.string.menu_level_three);
        } else {
            menuBarLevelInfo.setText(R.string.menu_level_two);
        }
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);

        btnSearch = view.findViewById(R.id.btn_search);
        btnClear = view.findViewById(R.id.btn_clear);

        curLang = PreferencesManager.getCurrentLanguage(getContext());
        changeLabel(curLang);

        rootFolderName = CommonConstants.DA_IMAGE_PATH + "/" + CommonConstants.DA_ATTACH_EDIT_PATH;

        companyStatus = getResources().getStringArray(R.array.occupation_company_status);
        productTypeList = PreferencesManager.getProductTypeList(getActivity());

        if (productTypeList == null) {
            if (!CommonUtils.isNetworkAvailable(getActivity())) {
                showNetworkErrorDialog(getActivity(), getNetErrMsg());
            } else {
                final ProgressDialog productListDialog = new ProgressDialog(getActivity());
                productListDialog.setMessage("Loading Product List...");
                productListDialog.setCancelable(false);
                productListDialog.show();

                Service loadLastInformation = APIClient.getApplicationRegisterService();
                Call<BaseResponse<List<ProductTypeListBean>>> productListReq = loadLastInformation.getProductTypeList();

                productListReq.enqueue(new Callback<BaseResponse<List<ProductTypeListBean>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<ProductTypeListBean>>> call, Response<BaseResponse<List<ProductTypeListBean>>> response) {
                        BaseResponse baseResponse = response.body();
                        if (baseResponse != null) {
                            if (baseResponse.getStatus().equals(SUCCESS)) {
                                productTypeList = (List<ProductTypeListBean>) baseResponse.getData();

                                Log.e("Procduct list", "Success");

                                if (productTypeList != null) {
                                    saveProductType(productTypeList);

                                    int listSize = productTypeList.size();
                                    productCategory = new String[listSize];
                                    productId = new int[listSize];
                                    getProductType(productTypeList, listSize);

                                    productListDialog.dismiss();
                                } else {
                                    productListDialog.dismiss();
                                }
                            } else {
                                productListDialog.dismiss();
                                showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                            }
                        } else {
                            productListDialog.dismiss();
                            showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<ProductTypeListBean>>> call, Throwable t) {
                        productListDialog.dismiss();
                        showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                    }
                });
            }

        } else {
            int listSize = productTypeList.size();
            productCategory = new String[listSize];
            productId = new int[listSize];
            getProductType(productTypeList, listSize);

            Log.e("product id : ", String.valueOf(productId[0]));
            Log.e("product id : ", String.valueOf(productId[1]));

        }


        cityTownshipList = PreferencesManager.getCityListInfo(getActivity());
        if (cityTownshipList == null) {
            if (!CommonUtils.isNetworkAvailable(getActivity())) {
                showNetworkErrorDialog(getActivity(), getNetErrMsg());
            } else {
                final ProgressDialog loadingCityInfo = new ProgressDialog(getActivity());
                loadingCityInfo.setMessage("Loading City and Township Info...");
                loadingCityInfo.setCancelable(false);
                loadingCityInfo.show();

                Service loadInformation = APIClient.getApplicationRegisterService();
                Call<BaseResponse<List<CityTownshipResBean>>> cityInfoReq = loadInformation.getCityTownshipList();

                cityInfoReq.enqueue(new Callback<BaseResponse<List<CityTownshipResBean>>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<List<CityTownshipResBean>>> call, Response<BaseResponse<List<CityTownshipResBean>>> response) {
                        BaseResponse baseResponse = response.body();
                        if (baseResponse != null) {
                            if (baseResponse.getStatus().equals(SUCCESS)) {
                                final List<CityTownshipResBean> cityTspList =
                                        (List<CityTownshipResBean>) baseResponse.getData();

                                Log.e("TownshipCity list", "Success");
                                Log.e("City List : ", String.valueOf(cityTspList.size()));

                                if (cityTspList != null) {
                                    cityTownshipList = cityTspList;
                                    saveCityInfo(cityTownshipList);
                                    loadingCityInfo.dismiss();
                                } else {
                                    loadingCityInfo.dismiss();
                                }
                            } else {
                                loadingCityInfo.dismiss();
                                showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                            }
                        } else {
                            loadingCityInfo.dismiss();
                            showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<List<CityTownshipResBean>>> call, Throwable t) {
                        loadingCityInfo.dismiss();
                        showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                    }
                });
            }
        } else {
            setUpCityList(cityTownshipList);
        }


        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        rvEnquiryList = view.findViewById(R.id.rv_application_list);

        textApplicationNo1 = view.findViewById(R.id.text_application_no1);
        textApplicationNo2 = view.findViewById(R.id.text_application_no2);
        textApplicationNo3 = view.findViewById(R.id.text_application_no3);
        textAppliedDate = view.findViewById(R.id.text_applied_date);

        /*tvCategorySearched = view.findViewById(R.id.id_searched_label);*/
        /*spinnerLoanType = view.findViewById(R.id.spinner_loan_type);*/
        /*loanTypeIds = getResources().getIntArray(R.array.loan_type_id_array);*/

        /*final String[] loanTypes = getResources().getStringArray(R.array.loan_type_array);
        ArrayAdapter<String> loanTypeAdapter = new ArrayAdapter(getActivity(), R.layout.nrc_spinner_item_3, loanTypes);
        spinnerLoanType.setAdapter(loanTypeAdapter);

        spinnerLoanType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLoanTypeId = loanTypeIds[position];
                textLoanType = loanTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        textAppliedDate.setOnClickListener(new View.OnClickListener() {
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
                        textAppliedDate.setText(sdf.format(myCalendar.getTime()));
                    }
                });
                dialog.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dialog.setMaxDate(myCalendar.getTimeInMillis());
                dialog.show(getChildFragmentManager(), "TAG");
            }
        });

        btnClear.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.txt_clear, getContext()));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textApplicationNo1.setText("");
                textApplicationNo2.setText("");
                textApplicationNo3.setText("");
                textApplicationNo1.requestFocus();
                textAppliedDate.setText("");
            }
        });
        btnSearch = view.findViewById(R.id.btn_search);
        btnSearch.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_search, getContext()));
        enableSearch();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchApplicationList();
            }
        });

        if (MainMenuActivityDrawer.doRegistrationSuccess) {
            searchApplicationList();
            MainMenuActivityDrawer.doRegistrationSuccess = false;
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("StatusFrag", "Fragment is destroyed");
        StatusReadFlagReq readFlagReq = new StatusReadFlagReq();
        readFlagReq.setCustomerId(userInformationFormBean.getCustomerId());

        Service service = APIClient.getApplicationRegisterService();
        final Call<BaseResponse> request
                = service.setStatusReadFlag(readFlagReq);
        request.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("StatusCon:", "read flag success");
                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                showMessageDialog("Service temporary unavailable.");
            }
        });

    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                //this.languageFlag = item;
                Log.e("update flag", item.getTitle().toString());
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    //changeLabel(LANG_MM);
                    btnSearch.setText(CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.btn_search, getContext()));
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    PreferencesManager.setCurrentLanguage(getContext(), LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    //changeLabel(LANG_EN);
                    btnSearch.setText(CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.btn_search, getContext()));
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    PreferencesManager.setCurrentLanguage(getContext(), LANG_EN);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onViewApplicationDetail(int daApplicationInfoId) {

        final String accessToken = PreferencesManager.getAccessToken(getActivity());

        final ApplicationDetailInfoReqBean applicationDetailInfoReqBean
                = new ApplicationDetailInfoReqBean();
        applicationDetailInfoReqBean.setDaApplicationInfoId(daApplicationInfoId);

        Service applicationDetailViewService = APIClient.getApplicationRegisterService();
        final Call<BaseResponse<ApplicationDetailInfoResBean>> req
                = applicationDetailViewService.getApplicationDetailInfo(accessToken, applicationDetailInfoReqBean);

        req.enqueue(new Callback<BaseResponse<ApplicationDetailInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<ApplicationDetailInfoResBean>> call, Response<BaseResponse<ApplicationDetailInfoResBean>> response) {

                BaseResponse<ApplicationDetailInfoResBean> baseResponse
                        = response.body();

                if (baseResponse != null) {

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        ApplicationDetailInfoResBean applicationDetailInfoResBean
                                = (ApplicationDetailInfoResBean) baseResponse.getData();

                        final Dialog previewDialog = new Dialog(getActivity());
                        previewDialog.setContentView(R.layout.application_info_detail_layout);
                        previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        previewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        FloatingActionButton fab = previewDialog.findViewById(R.id.fab_detail_close);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                previewDialog.dismiss();
                            }
                        });

                        String curlang = PreferencesManager.getCurrentLanguage(getActivity());
                        cityTownshipList = PreferencesManager.getCityListInfo(getActivity());
                        setUpCityList(cityTownshipList);

                        //Set Application Inquiries Detail.
                        TextView appNo_title = previewDialog.findViewById(R.id.lbl_application_no);
                        appNo_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_application_no, getActivity()));
                        TextView lblApplicationNo = previewDialog.findViewById(R.id.val_application_no);
                        String application_no = applicationDetailInfoResBean.getApplicationNo();
                        String ymNo = application_no.substring(0, 4);
                        String middleNo = application_no.substring(4, 7);
                        String lastNo = application_no.substring(7, 10);
                        lblApplicationNo.setText(ymNo + "-" + middleNo + "-" + lastNo);

                        TextView appDate_title = previewDialog.findViewById(R.id.lbl_application_date);
                        appDate_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_application_date, getActivity()));
                        TextView lblApplicationDate = previewDialog.findViewById(R.id.val_application_date);
                        lblApplicationDate.setText(dateToString(applicationDetailInfoResBean.getAppliedDate()));

                        TextView appMember_card_no_title = previewDialog.findViewById(R.id.lbl_member_card_no);
                        appMember_card_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_member_card_no, getActivity()));
                        TextView lblMemberCardNo = previewDialog.findViewById(R.id.val_member_card_no);
                        lblMemberCardNo.setText(getMemberCardNo());

                        //Set Application Data.
                        TextView app_data_name_title = previewDialog.findViewById(R.id.lbl_name);
                        app_data_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_name, getActivity()));
                        TextView lblName = previewDialog.findViewById(R.id.val_name);
                        lblName.setText(getStringValue(applicationDetailInfoResBean.getName()));

                        TextView app_data_dob_title = previewDialog.findViewById(R.id.lbl_dob);
                        app_data_dob_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_dob, getActivity()));
                        TextView lblDob = previewDialog.findViewById(R.id.val_dob);
                        lblDob.setText(dateToString(applicationDetailInfoResBean.getDob()));

                        TextView app_data_nrc_no_title = previewDialog.findViewById(R.id.lbl_nrc_no);
                        app_data_nrc_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_nrc_no, getActivity()));
                        TextView lblNrcNo = previewDialog.findViewById(R.id.val_nrc_no);
                        lblNrcNo.setText(applicationDetailInfoResBean.getNrcNo());

                        TextView app_data_father_name_title = previewDialog.findViewById(R.id.lbl_father_name);
                        app_data_father_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_father_name, getActivity()));
                        TextView lblFatherName = previewDialog.findViewById(R.id.val_father_name);
                        lblFatherName.setText(getStringValue(applicationDetailInfoResBean.getFatherName()));

                        TextView app_data_education_title = previewDialog.findViewById(R.id.lbl_education);
                        app_data_education_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_education, getActivity()));
                        TextView lblEducation = previewDialog.findViewById(R.id.val_education);
                        lblEducation.setText(getEducationPreview(applicationDetailInfoResBean.getHighestEducationTypeId()));

                        TextView app_data_nationality_title = previewDialog.findViewById(R.id.lbl_Nationality);
                        app_data_nationality_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_nationality, getActivity()));
                        TextView lblNationality = previewDialog.findViewById(R.id.val_nationality);
                        if (applicationDetailInfoResBean.getNationality() == 2) {
                            lblNationality.setText(applicationDetailInfoResBean.getNationalityOther());
                        } else {
                            lblNationality.setText(getNationality(applicationDetailInfoResBean.getNationality()));
                        }
                        TextView app_data_gender_title = previewDialog.findViewById(R.id.lbl_gender);
                        app_data_gender_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_gender, getActivity()));
                        TextView lblGender = previewDialog.findViewById(R.id.val_gender);
                        lblGender.setText(getGender(applicationDetailInfoResBean.getGender()));

                        TextView app_data_marital_status_title = previewDialog.findViewById(R.id.lbl_marital_status);
                        app_data_marital_status_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_marital_status, getActivity()));
                        TextView lblMaritalStatus = previewDialog.findViewById(R.id.val_marital_status);
                        lblMaritalStatus.setText(getMaritalStatus(applicationDetailInfoResBean.getMaritalStatus()));

                        TextView app_data_type_of_residence_title = previewDialog.findViewById(R.id.lbl_type_of_resident);
                        app_data_type_of_residence_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_type_of_residence, getActivity()));
                        TextView lblTypeOfResidence = previewDialog.findViewById(R.id.val_type_of_resident);
                        if (applicationDetailInfoResBean.getTypeOfResidence() == 5) {
                            lblTypeOfResidence.setText(applicationDetailInfoResBean.getTypeOfResidenceOther());
                        } else {
                            lblTypeOfResidence.setText(getResidentType(applicationDetailInfoResBean.getTypeOfResidence()));
                        }
                        TextView app_data_living_with_title = previewDialog.findViewById(R.id.lbl_living_with);
                        app_data_living_with_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_living_with, getActivity()));
                        TextView lblLivingWith = previewDialog.findViewById(R.id.val_living_with);
                        lblLivingWith.setText(getLivingWith(applicationDetailInfoResBean.getLivingWith()));

                        TextView app_data_year_of_stay_title = previewDialog.findViewById(R.id.lbl_year_of_stay);
                        app_data_year_of_stay_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_year_of_stay, getActivity()));
                        TextView lblYearOfStay = previewDialog.findViewById(R.id.val_year_of_stay);
                        lblYearOfStay.setText(getYearOfStay(
                                applicationDetailInfoResBean.getYearOfStayYear(), applicationDetailInfoResBean.getYearOfStayMonth()));

                        TextView app_data_mobile_no_title = previewDialog.findViewById(R.id.lbl_mobile_no);
                        app_data_mobile_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_mobile_no, getActivity()));
                        TextView lblMobileNo = previewDialog.findViewById(R.id.val_mobile_no);
                        lblMobileNo.setText(applicationDetailInfoResBean.getMobileNo());

                        TextView app_data_resident_tel_no_title = previewDialog.findViewById(R.id.lbl_resident_tel_no);
                        app_data_resident_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_resident_tel_no, getActivity()));
                        TextView lblResidentTelNo = previewDialog.findViewById(R.id.val_resident_tel_no);
                        lblResidentTelNo.setText(getStringValue(applicationDetailInfoResBean.getResidentTelNo()));

                        TextView app_data_other_phone_no_title = previewDialog.findViewById(R.id.lbl_other_ph_no);
                        app_data_other_phone_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_other_phone_no, getActivity()));
                        TextView lblOtherPhoneNo = previewDialog.findViewById(R.id.val_other_ph_no);
                        lblOtherPhoneNo.setText(getStringValue(applicationDetailInfoResBean.getOtherPhoneNo()));

                        TextView app_data_email_title = previewDialog.findViewById(R.id.lbl_email);
                        app_data_email_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_email, getActivity()));
                        TextView lblEmail = previewDialog.findViewById(R.id.val_email);
                        lblEmail.setText(getStringValue(applicationDetailInfoResBean.getEmail()));

                        /*Current Address*/
                        TextView app_current_buildingno = previewDialog.findViewById(R.id.lbl_current_buildingNo);
                        app_current_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                        TextView value_current_buildingno = previewDialog.findViewById(R.id.val_current_buildingNo);
                        value_current_buildingno.setText(getStringValue(applicationDetailInfoResBean.getCurrentAddressBuildingNo()));

                        TextView app_current_roomno = previewDialog.findViewById(R.id.lbl_current_room_no);
                        app_current_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                        TextView value_current_roomno = previewDialog.findViewById(R.id.val_current_room_no);
                        value_current_roomno.setText(getStringValue(applicationDetailInfoResBean.getCurrentAddressRoomNo()));

                        TextView app_current_floorno = previewDialog.findViewById(R.id.lbl_current_floor_no);
                        app_current_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                        TextView value_current_floorno = previewDialog.findViewById(R.id.val_current_floor_no);
                        value_current_floorno.setText(getStringValue(applicationDetailInfoResBean.getCurrentAddressFloor()));

                        TextView app_current_street = previewDialog.findViewById(R.id.lbl_current_street);
                        app_current_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                        TextView value_current_street = previewDialog.findViewById(R.id.val_current_street);
                        value_current_street.setText(getStringValue(applicationDetailInfoResBean.getCurrentAddressStreet()));

                        TextView app_current_quarter = previewDialog.findViewById(R.id.lbl_current_quarter);
                        app_current_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                        TextView value_current_quarter = previewDialog.findViewById(R.id.val_current_quarter);
                        value_current_quarter.setText(getStringValue(applicationDetailInfoResBean.getCurrentAddressQtr()));

                        TextView app_current_city = previewDialog.findViewById(R.id.lbl_current_city);
                        app_current_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                        TextView value_current_city = previewDialog.findViewById(R.id.val_current_city);
                        value_current_city.setText(getStringValue(getCity(cityId, cityList, applicationDetailInfoResBean.getCurrentAddressCity())));

                        TextView app_current_township = previewDialog.findViewById(R.id.lbl_current_township);
                        app_current_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                        TextView value_current_township = previewDialog.findViewById(R.id.val_current_township);
                        value_current_township.setText(getStringValue(getTownship(cityTownshipList, applicationDetailInfoResBean.getCurrentAddressCity(), applicationDetailInfoResBean.getCurrentAddressTownship())));

                        /*Permanent Address*/
                        TextView app_permanent_buildingno = previewDialog.findViewById(R.id.lbl_permanent_buildingNo);
                        app_permanent_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                        TextView value_permanent_buildingno = previewDialog.findViewById(R.id.val_permanent_buildingNo);
                        value_permanent_buildingno.setText(getStringValue(applicationDetailInfoResBean.getPermanentAddressBuildingNo()));

                        TextView app_permanent_roomno = previewDialog.findViewById(R.id.lbl_permanent_room_no);
                        app_permanent_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                        TextView value_permanent_roomno = previewDialog.findViewById(R.id.val_permanent_room_no);
                        value_permanent_roomno.setText(getStringValue(applicationDetailInfoResBean.getPermanentAddressRoomNo()));

                        TextView app_permanent_floorno = previewDialog.findViewById(R.id.lbl_permanent_floor_no);
                        app_permanent_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                        TextView value_permanent_floorno = previewDialog.findViewById(R.id.val_permanent_floor_no);
                        value_permanent_floorno.setText(getStringValue(applicationDetailInfoResBean.getPermanentAddressFloor()));

                        TextView app_permanent_street = previewDialog.findViewById(R.id.lbl_permanent_street);
                        app_permanent_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                        TextView value_permanent_street = previewDialog.findViewById(R.id.val_permanent_street);
                        value_permanent_street.setText(getStringValue(applicationDetailInfoResBean.getPermanentAddressStreet()));

                        TextView app_permanent_quarter = previewDialog.findViewById(R.id.lbl_permanent_quarter);
                        app_permanent_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                        TextView value_permanent_quarter = previewDialog.findViewById(R.id.val_permanent_quarter);
                        value_permanent_quarter.setText(getStringValue(applicationDetailInfoResBean.getPermanentAddressQtr()));

                        TextView app_permanent_city = previewDialog.findViewById(R.id.lbl_permanent_city);
                        app_permanent_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                        TextView value_permanent_city = previewDialog.findViewById(R.id.val_permanent_city);
                        value_permanent_city.setText(getStringValue(getCity(cityId, cityList, applicationDetailInfoResBean.getPermanentAddressCity())));

                        TextView app_permanent_township = previewDialog.findViewById(R.id.lbl_permanent_township);
                        app_permanent_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                        TextView value_permanent_township = previewDialog.findViewById(R.id.val_permanent_township);
                        value_permanent_township.setText(getStringValue(getTownship(cityTownshipList, applicationDetailInfoResBean.getPermanentAddressCity(), applicationDetailInfoResBean.getPermanentAddressTownship())));


                        //Set Occupation Data.
                        OccupationDataFormBean occupationDataFormBean
                                = applicationDetailInfoResBean.getApplicantCompanyInfoDto();

                        TextView occu_company_name_title = previewDialog.findViewById(R.id.lbl_company_name);
                        occu_company_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_company_name, getActivity()));
                        TextView lblCompanyName = previewDialog.findViewById(R.id.val_company_name);
                        lblCompanyName.setText(getStringValue(occupationDataFormBean.getCompanyName()));

                        TextView occu_company_tel_no_title = previewDialog.findViewById(R.id.lbl_company_tel_no);
                        occu_company_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_company_tel_no, getActivity()));
                        TextView lblCompanyTelNo = previewDialog.findViewById(R.id.val_company_tel_no);
                        lblCompanyTelNo.setText(getStringValue(occupationDataFormBean.getCompanyTelNo()));

                        TextView occu_contact_time_title = previewDialog.findViewById(R.id.lbl_contact_time);
                        occu_contact_time_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_contact_time, getActivity()));
                        TextView lblContactTime = previewDialog.findViewById(R.id.val_contact_time);

                        if (isEmptyOrNull(occupationDataFormBean.getContactTimeFrom()) || isEmptyOrNull(occupationDataFormBean.getContactTimeTo())) {
                            lblContactTime.setText("-");
                        } else {
                            lblContactTime.setText(getContactTime(occupationDataFormBean.getContactTimeFrom()
                                    , occupationDataFormBean.getContactTimeTo()));
                        }

                        TextView occu_department_title = previewDialog.findViewById(R.id.lbl_department);
                        occu_department_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_department, getActivity()));
                        TextView lblDepartment = previewDialog.findViewById(R.id.val_department);
                        lblDepartment.setText(occupationDataFormBean.getDepartment());

                        TextView occu_data_position_title = previewDialog.findViewById(R.id.lbl_position);
                        occu_data_position_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_position, getActivity()));
                        TextView lblPosition = previewDialog.findViewById(R.id.val_position);
                        lblPosition.setText(occupationDataFormBean.getPosition());

                        TextView occu_year_of_service_title = previewDialog.findViewById(R.id.lbl_year_of_service);
                        occu_year_of_service_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_year_of_service, getActivity()));
                        TextView lblYearOfService = previewDialog.findViewById(R.id.val_year_of_service);
                        lblYearOfService.setText(getYearOfService(
                                occupationDataFormBean.getYearOfServiceYear(), occupationDataFormBean.getYearOfServiceMonth()));

                        TextView occu_company_status_title = previewDialog.findViewById(R.id.lbl_company_status);
                        occu_company_status_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_company_status, getActivity()));
                        TextView lblCompanyStatus = previewDialog.findViewById(R.id.val_company_status);
                        lblCompanyStatus.setText(getCompanyStatusPreview(occupationDataFormBean.getCompanyStatus(), occupationDataFormBean.getCompanyStatusOther()));

                        TextView occu_monthly_basic_income_title = previewDialog.findViewById(R.id.lbl_monthly_basic_income);
                        occu_monthly_basic_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_monthly_basic_income, getActivity()));
                        TextView lblMonthlyBasicIncome = previewDialog.findViewById(R.id.val_monthly_basic_income);
                        lblMonthlyBasicIncome.setText(getBasicIncome(occupationDataFormBean.getMonthlyBasicIncome()));

                        TextView occu_other_income_title = previewDialog.findViewById(R.id.lbl_other_income);
                        occu_other_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_other_income, getActivity()));
                        TextView lblOtherIncome = previewDialog.findViewById(R.id.val_other_income);
                        lblOtherIncome.setText(getStringValue(getOtherIncome(occupationDataFormBean.getOtherIncome())));

                        TextView occu_total_income_title = previewDialog.findViewById(R.id.lbl_total_income);
                        occu_total_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_total_income, getActivity()));
                        TextView lblTotalIncome = previewDialog.findViewById(R.id.val_total_income);
                        lblTotalIncome.setText(getTotalIncome(occupationDataFormBean.getTotalIncome()));

                        TextView occu_salary_date_title = previewDialog.findViewById(R.id.lbl_salary_date);
                        occu_salary_date_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_salary_date, getActivity()));
                        TextView lblSalaryDate = previewDialog.findViewById(R.id.val_salary_date);
                        lblSalaryDate.setText(getSalaryDay(occupationDataFormBean.getSalaryDay()));

                        /*Occupation Address*/
                        TextView app_occupation_buildingno = previewDialog.findViewById(R.id.lbl_occupation_buildingNo);
                        app_occupation_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                        TextView value_occupation_buildingno = previewDialog.findViewById(R.id.val_occupation_buildingNo);
                        value_occupation_buildingno.setText(getStringValue(occupationDataFormBean.getCompanyAddressBuildingNo()));

                        TextView app_occupation_roomno = previewDialog.findViewById(R.id.lbl_occupation_room_no);
                        app_occupation_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                        TextView value_occupation_roomno = previewDialog.findViewById(R.id.val_occupation_room_no);
                        value_occupation_roomno.setText(getStringValue(occupationDataFormBean.getCompanyAddressRoomNo()));

                        TextView app_occupation_floorno = previewDialog.findViewById(R.id.lbl_occupation_floor_no);
                        app_occupation_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                        TextView value_occupation_floorno = previewDialog.findViewById(R.id.val_occupation_floor_no);
                        value_occupation_floorno.setText(getStringValue(occupationDataFormBean.getCompanyAddressFloor()));

                        TextView app_occupation_street = previewDialog.findViewById(R.id.lbl_occupation_street);
                        app_occupation_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                        TextView value_occupation_street = previewDialog.findViewById(R.id.val_occupation_street);
                        value_occupation_street.setText(getStringValue(occupationDataFormBean.getCompanyAddressStreet()));

                        TextView app_occupation_quarter = previewDialog.findViewById(R.id.lbl_occupation_quarter);
                        app_occupation_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                        TextView value_occupation_quarter = previewDialog.findViewById(R.id.val_occupation_quarter);
                        value_occupation_quarter.setText(getStringValue(occupationDataFormBean.getCompanyAddressQtr()));

                        TextView app_occupation_city = previewDialog.findViewById(R.id.lbl_occupation_city);
                        app_occupation_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                        TextView value_occupation_city = previewDialog.findViewById(R.id.val_occupation_city);
                        value_occupation_city.setText(getStringValue(getCity(cityId, cityList, occupationDataFormBean.getCompanyAddressCity())));

                        TextView app_occupation_township = previewDialog.findViewById(R.id.lbl_occupation_township);
                        app_occupation_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                        TextView value_occupation_township = previewDialog.findViewById(R.id.val_occupation_township);
                        value_occupation_township.setText(getStringValue(getTownship(cityTownshipList, occupationDataFormBean.getCompanyAddressCity(), occupationDataFormBean.getCompanyAddressTownship())));

                        //Set Emergency Contact.
                        EmergencyContactFormBean emergencyContactFormBean
                                = applicationDetailInfoResBean.getEmergencyContactInfoDto();

                        TextView da_in_eme_name_title = previewDialog.findViewById(R.id.lbl_eme_name);
                        da_in_eme_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_name, getActivity()));
                        TextView lblEmeName = previewDialog.findViewById(R.id.val_eme_name);
                        lblEmeName.setText(getStringValue(emergencyContactFormBean.getName()));

                        TextView da_in_eme_relationship_with_applier_title = previewDialog.findViewById(R.id.lbl_eme_relation_with);
                        da_in_eme_relationship_with_applier_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_relationship_with_applier, getActivity()));
                        TextView lblEmeRelation = previewDialog.findViewById(R.id.val_eme_relationship_with);
                        if (emergencyContactFormBean.getRelationship() == 5) {
                            lblEmeRelation.setText(emergencyContactFormBean.getRelationshipOther());
                        } else {
                            lblEmeRelation.setText(getApplicationRelation(emergencyContactFormBean.getRelationship()));
                        }

                        TextView da_in_eme_mobile_no_title = previewDialog.findViewById(R.id.lbl_eme_mobile_no);
                        da_in_eme_mobile_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_mobile_no, getActivity()));
                        TextView lblEmeMobileNo = previewDialog.findViewById(R.id.val_eme_mobile_no);
                        lblEmeMobileNo.setText(emergencyContactFormBean.getMobileNo());

                        TextView da_in_eme_resident_tel_no_title = previewDialog.findViewById(R.id.lbl_eme_resident_tel_no);
                        da_in_eme_resident_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_resident_tel_no, getActivity()));
                        TextView lblEmeResdTelNo = previewDialog.findViewById(R.id.val_eme_resident_tel_no);
                        lblEmeResdTelNo.setText(getStringValue(emergencyContactFormBean.getResidentTelNo()));

                        TextView da_in_eme_other_phone_no_title = previewDialog.findViewById(R.id.lbl_eme_other_ph_no);
                        da_in_eme_other_phone_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_other_phone_no, getActivity()));
                        TextView lblOtherPhNo = previewDialog.findViewById(R.id.val_eme_other_ph_no);
                        lblOtherPhNo.setText(getStringValue(emergencyContactFormBean.getOtherPhoneNo()));

                        /*Emergency Address*/
                        TextView app_emergency_buildingno = previewDialog.findViewById(R.id.lbl_emergency_buildingNo);
                        app_emergency_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                        TextView value_emergency_buildingno = previewDialog.findViewById(R.id.val_emergency_buildingNo);
                        value_emergency_buildingno.setText(getStringValue(emergencyContactFormBean.getCurrentAddressBuildingNo()));

                        TextView app_emergency_roomno = previewDialog.findViewById(R.id.lbl_emergency_room_no);
                        app_emergency_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                        TextView value_emergency_roomno = previewDialog.findViewById(R.id.val_emergency_room_no);
                        value_emergency_roomno.setText(getStringValue(emergencyContactFormBean.getCurrentAddressRoomNo()));

                        TextView app_emergency_floorno = previewDialog.findViewById(R.id.lbl_emergency_floor_no);
                        app_emergency_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                        TextView value_emergency_floorno = previewDialog.findViewById(R.id.val_emergency_floor_no);
                        value_emergency_floorno.setText(getStringValue(emergencyContactFormBean.getCurrentAddressFloor()));

                        TextView app_emergency_street = previewDialog.findViewById(R.id.lbl_emergency_street);
                        app_emergency_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                        TextView value_emergency_street = previewDialog.findViewById(R.id.val_emergency_street);
                        value_emergency_street.setText(getStringValue(emergencyContactFormBean.getCurrentAddressStreet()));

                        TextView app_emergency_quarter = previewDialog.findViewById(R.id.lbl_emergency_quarter);
                        app_emergency_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                        TextView value_emergency_quarter = previewDialog.findViewById(R.id.val_emergency_quarter);
                        value_emergency_quarter.setText(getStringValue(emergencyContactFormBean.getCurrentAddressQtr()));

                        TextView app_emergency_city = previewDialog.findViewById(R.id.lbl_emergency_city);
                        app_emergency_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                        TextView value_emergency_city = previewDialog.findViewById(R.id.val_emergency_city);
                        value_emergency_city.setText(getStringValue(getCity(cityId, cityList, emergencyContactFormBean.getCurrentAddressCity())));

                        TextView app_emergency_township = previewDialog.findViewById(R.id.lbl_emergency_township);
                        app_emergency_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                        TextView value_emergency_township = previewDialog.findViewById(R.id.val_emergency_township);
                        value_emergency_township.setText(getStringValue(getTownship(cityTownshipList, emergencyContactFormBean.getCurrentAddressCity(), emergencyContactFormBean.getCurrentAddressTownship())));

                        //Set Guarantor.
                        GuarantorDetailFormBean guarantorFormBean
                                = applicationDetailInfoResBean.getGuarantorInfoDto();

                        TextView da_in_gur_name_title = previewDialog.findViewById(R.id.lbl_gur_name);
                        da_in_gur_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_name, getActivity()));
                        TextView lblGurName = previewDialog.findViewById(R.id.val_gur_name);
                        lblGurName.setText(getStringValue(guarantorFormBean.getName()));

                        TextView da_in_gur_dob_title = previewDialog.findViewById(R.id.lbl_gur_dob);
                        da_in_gur_dob_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_dob, getActivity()));
                        TextView lblGurDob = previewDialog.findViewById(R.id.val_gur_dob);
                        lblGurDob.setText(CommonUtils.dateToYYMMDDStrFormat(guarantorFormBean.getDob()));

                        TextView da_in_gur_nrc_no_title = previewDialog.findViewById(R.id.lbl_gur_nrc_no);
                        da_in_gur_nrc_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_nrc_no, getActivity()));
                        TextView lblGurNrcNo = previewDialog.findViewById(R.id.val_gur_nrc_no);
                        lblGurNrcNo.setText(guarantorFormBean.getNrcNo());

                        TextView da_in_gur_nationality_title = previewDialog.findViewById(R.id.lbl_gur_nationality);
                        da_in_gur_nationality_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_nationality, getActivity()));
                        TextView lblGurNationality = previewDialog.findViewById(R.id.val_gur_nationality);
                        if (guarantorFormBean.getNationality() == 2) {
                            lblGurNationality.setText(guarantorFormBean.getNationalityOther());
                        } else {
                            lblGurNationality.setText(getNationality(guarantorFormBean.getNationality()));
                        }

                        TextView da_in_gur_mobile_no_title = previewDialog.findViewById(R.id.lbl_gur_mobile_no);
                        da_in_gur_mobile_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_mobile_no, getActivity()));
                        TextView lblGurMobileNo = previewDialog.findViewById(R.id.val_gur_mobile_no);
                        lblGurMobileNo.setText(guarantorFormBean.getMobileNo());

                        TextView da_in_gur_resident_tel_no_title = previewDialog.findViewById(R.id.lbl_gur_resident_tel_no);
                        da_in_gur_resident_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_resident_tel_no, getActivity()));
                        TextView lblGurResdTelNo = previewDialog.findViewById(R.id.val_gur_resident_tel_no);
                        lblGurResdTelNo.setText(getStringValue(guarantorFormBean.getResidentTelNo()));

                        TextView da_in_gur_relationship_with_applier_title = previewDialog.findViewById(R.id.lbl_gur_relation_with);
                        da_in_gur_relationship_with_applier_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_relationship_with_applier, getActivity()));
                        TextView lblGurRelationWith = previewDialog.findViewById(R.id.val_gur_relation_with);

                        if (guarantorFormBean.getRelationship() == 5) {
                            lblGurRelationWith.setText(guarantorFormBean.getRelationshipOther());
                        } else {
                            lblGurRelationWith.setText(getGuarantorRelation(guarantorFormBean.getRelationship()));
                        }

                        TextView da_in_gur_type_of_resident_title = previewDialog.findViewById(R.id.lbl_gur_type_of_resident);
                        da_in_gur_type_of_resident_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_type_of_resident, getActivity()));
                        TextView lblGurTypeOfResd = previewDialog.findViewById(R.id.val_gur_type_of_resident);
                        if (guarantorFormBean.getTypeOfResidence() == 5) {
                            lblGurTypeOfResd.setText(guarantorFormBean.getTypeOfResidenceOther());
                        } else {
                            lblGurTypeOfResd.setText(getResidentType(guarantorFormBean.getTypeOfResidence()));
                        }

                        TextView da_in_gur_living_with_title = previewDialog.findViewById(R.id.lbl_gur_live_with);
                        da_in_gur_living_with_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_living_with, getActivity()));
                        TextView lblGurLivingWith = previewDialog.findViewById(R.id.val_gur_live_with);
                        lblGurLivingWith.setText(getLivingWith(guarantorFormBean.getLivingWith()));

                        TextView da_in_gur_gender_title = previewDialog.findViewById(R.id.lbl_gur_gender);
                        da_in_gur_gender_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_gender, getActivity()));
                        TextView lblGurGender = previewDialog.findViewById(R.id.val_gur_gender);
                        lblGurGender.setText(getGender(guarantorFormBean.getGender()));

                        TextView da_in_gur_marital_status_title = previewDialog.findViewById(R.id.lbl_gur_marital_status);
                        da_in_gur_marital_status_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_marital_status, getActivity()));
                        TextView lblGurMaritalStatus = previewDialog.findViewById(R.id.val_gur_marital_status);
                        lblGurMaritalStatus.setText(getMaritalStatus(guarantorFormBean.getMaritalStatus()));

                        TextView da_in_gur_year_of_stay_title = previewDialog.findViewById(R.id.lbl_gur_year_of_stay);
                        da_in_gur_year_of_stay_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_year_of_stay, getActivity()));
                        TextView lblGurYearOfStay = previewDialog.findViewById(R.id.val_gur_year_of_stay);
                        lblGurYearOfStay.setText(getYearOfStay(guarantorFormBean.getYearOfStayYear(),
                                guarantorFormBean.getYearOfStayMonth()));

                        TextView da_in_gur_company_name_title = previewDialog.findViewById(R.id.lbl_gur_company_name);
                        da_in_gur_company_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_company_name, getActivity()));
                        TextView lblGurCompanyName = previewDialog.findViewById(R.id.val_gur_company_name);
                        lblGurCompanyName.setText(guarantorFormBean.getCompanyName());

                        TextView da_in_gur_company_tel_no_title = previewDialog.findViewById(R.id.lbl_gur_company_tel_no);
                        da_in_gur_company_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_company_tel_no, getActivity()));
                        TextView lblGurCompanyTelNo = previewDialog.findViewById(R.id.val_gur_company_tel);
                        lblGurCompanyTelNo.setText(guarantorFormBean.getCompanyTelNo());

                        TextView da_in_gur_department_title = previewDialog.findViewById(R.id.lbl_gur_department);
                        da_in_gur_department_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_department, getActivity()));
                        TextView lblGurDept = previewDialog.findViewById(R.id.val_gur_company_department);
                        lblGurDept.setText(guarantorFormBean.getDepartment());

                        TextView da_in_gur_position_title = previewDialog.findViewById(R.id.lbl_gur_position);
                        da_in_gur_position_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_position, getActivity()));
                        TextView lblGurPosition = previewDialog.findViewById(R.id.val_gur_position);
                        lblGurPosition.setText(guarantorFormBean.getPosition());

                        TextView da_in_gur_year_of_service_title = previewDialog.findViewById(R.id.lbl_gur_year_of_service);
                        da_in_gur_year_of_service_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_year_of_service, getActivity()));
                        TextView lblGurYearOfService = previewDialog.findViewById(R.id.val_gur_year_of_service);
                        lblGurYearOfService.setText(getYearOfService(guarantorFormBean.getYearOfServiceYear(),
                                guarantorFormBean.getYearOfServiceMonth()));

                        TextView da_in_gur_monthly_basic_income_title = previewDialog.findViewById(R.id.lbl_gur_monthly_basic_income);
                        da_in_gur_monthly_basic_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_monthly_basic_income, getActivity()));
                        TextView lblGurMonthlyBasicIncome = previewDialog.findViewById(R.id.val_gur_monthly_basic_income);
                        lblGurMonthlyBasicIncome.setText(getBasicIncome(guarantorFormBean.getMonthlyBasicIncome()));

                        TextView da_in_gur_total_income_title = previewDialog.findViewById(R.id.lbl_gur_total_income);
                        da_in_gur_total_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_total_income, getActivity()));
                        TextView lblGurTotalIncome = previewDialog.findViewById(R.id.val_gur_total_income);
                        lblGurTotalIncome.setText(getTotalIncome(guarantorFormBean.getTotalIncome()));


                        //Loan Confirmation.
                        TextView da_in_loanConfirm_status_title = previewDialog.findViewById(R.id.lbl_lc_status);
                        da_in_loanConfirm_status_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_status, getActivity()));
                        lblLcStatus = previewDialog.findViewById(R.id.val_lc_status);
                        setApplicationStatus(applicationDetailInfoResBean.getStatus());

                        /*TextView da_in_loanConfirm_loan_type_title = previewDialog.findViewById(R.id.lbl_lc_loan_type);
                        da_in_loanConfirm_loan_type_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_loan_type, getActivity()));
                        TextView lblLcLoanType = previewDialog.findViewById(R.id.val_lc_loan_type);
                        lblLcLoanType.setText(getLoanType(applicationDetailInfoResBean.getDaLoanTypeId()));

                        TextView da_in_loanConfirm_product_cat_title = previewDialog.findViewById(R.id.lbl_lc_product_category);
                        da_in_loanConfirm_product_cat_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_product_cat, getActivity()));
                        TextView lblLcProductCategory = previewDialog.findViewById(R.id.val_lc_product_category);
                        lblLcProductCategory.setText(getProductCategory(applicationDetailInfoResBean.getDaProductTypeId()));

                        TextView da_in_loanConfirm_product_des_title = previewDialog.findViewById(R.id.lbl_lc_product_description);
                        da_in_loanConfirm_product_des_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_product_des, getActivity()));
                        TextView lblLcProductDesc = previewDialog.findViewById(R.id.val_lc_product_desc);
                        lblLcProductDesc.setText(getStringValue(applicationDetailInfoResBean.getProductDescription()));*/

                        TextView da_in_loanConfirm_finance_amount_title = previewDialog.findViewById(R.id.lbl_lc_finance_amt);
                        da_in_loanConfirm_finance_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_finance_amount, getActivity()));
                        TextView lblLcFinanceAmt = previewDialog.findViewById(R.id.val_lc_finance_amt);
                        lblLcFinanceAmt.setText(getFinanceAmount(applicationDetailInfoResBean.getFinanceAmount()));

                        TextView da_in_loanConfirm_approve_amount_title = previewDialog.findViewById(R.id.lbl_lc_approved_amt);
                        da_in_loanConfirm_approve_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_approve_amount, getActivity()));
                        TextView lblLcApproveAmt = previewDialog.findViewById(R.id.val_lc_approved_amt);
                        lblLcApproveAmt.setText(getFinanceAmount(applicationDetailInfoResBean.getApprovedFinanceAmount()));

                        TextView da_in_loanConfirm_term_of_finance_title = previewDialog.findViewById(R.id.lbl_lc_finance_term);
                        da_in_loanConfirm_term_of_finance_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_term_of_finance, getActivity()));
                        TextView lblLcTermOfFinance = previewDialog.findViewById(R.id.val_lc_finance_term);
                        lblLcTermOfFinance.setText(getLoanTerm(applicationDetailInfoResBean.getFinanceTerm()));

                        TextView da_in_loanConfirm_approve_term_title = previewDialog.findViewById(R.id.lbl_approve_finance_term);
                        da_in_loanConfirm_approve_term_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.lbl_approve_term, getActivity()));
                        TextView lblLcApproveTerm = previewDialog.findViewById(R.id.val_approve_finance_term);
                        lblLcApproveTerm.setText(getLoanTerm(applicationDetailInfoResBean.getApprovedFinanceTerm()));

                        TextView da_in_loanConfirm_processing_fee_title = previewDialog.findViewById(R.id.lbl_lc_processing_fee);
                        da_in_loanConfirm_processing_fee_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_processing_fee, getActivity()));
                        TextView lblLcProcessingFee = previewDialog.findViewById(R.id.val_lc_processing_fee);
                        lblLcProcessingFee.setText(getPaymentAmount(applicationDetailInfoResBean.getProcessingFees()));

                        TextView da_in_loanConfirm_compulsory_saving_title = previewDialog.findViewById(R.id.lbl_lc_comp_saving);
                        da_in_loanConfirm_compulsory_saving_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_compulsory_saving, getActivity()));
                        TextView lblLcCompulsorySaving = previewDialog.findViewById(R.id.val_lc_comp_saving);
                        lblLcCompulsorySaving.setText(getPaymentAmount(applicationDetailInfoResBean.getTotalConSaving()));

                        TextView da_in_loanConfirm_total_repayment_amount_title = previewDialog.findViewById(R.id.lbl_lc_total_pay_amt);
                        da_in_loanConfirm_total_repayment_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_total_repayment_amount, getActivity()));
                        TextView lblLcTotalPayAmt = previewDialog.findViewById(R.id.val_lc_total_amt);
                        lblLcTotalPayAmt.setText(getPaymentAmount(applicationDetailInfoResBean.getModifyTotalRepayment()));

                        TextView da_in_loanConfirm_first_repayment_amount_title = previewDialog.findViewById(R.id.lbl_lc_first_repay_amt);
                        da_in_loanConfirm_first_repayment_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_first_repayment_amount, getActivity()));
                        TextView lblLcFirstPayAmt = previewDialog.findViewById(R.id.val_first_repay_amt);
                        lblLcFirstPayAmt.setText(getPaymentAmount(applicationDetailInfoResBean.getFirstPayment()));

                        TextView da_in_loanConfirm_monthly_repayment_amount_title = previewDialog.findViewById(R.id.lbl_lc_monthly_repay_amt);
                        da_in_loanConfirm_monthly_repayment_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_monthly_repayment_amount, getActivity()));
                        TextView lblLcMonthlyPayAmt = previewDialog.findViewById(R.id.val_lc_month_repay_amt);
                        lblLcMonthlyPayAmt.setText(getPaymentAmount(applicationDetailInfoResBean.getMonthlyInstallment()));

                        TextView da_in_loanConfirm_last_payment_title = previewDialog.findViewById(R.id.lbl_lc_last_repay);
                        da_in_loanConfirm_last_payment_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_last_payment, getActivity()));
                        TextView lblLcLastPay = previewDialog.findViewById(R.id.val_lc_last_repay);
                        lblLcLastPay.setText(getPaymentAmount(applicationDetailInfoResBean.getLastPayment()));

                        /*Guarantor Address*/
                        TextView app_guarantor_buildingno = previewDialog.findViewById(R.id.lbl_guarantor_buildingNo);
                        app_guarantor_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                        TextView value_guarantor_buildingno = previewDialog.findViewById(R.id.val_guarantor_buildingNo);
                        value_guarantor_buildingno.setText(getStringValue(guarantorFormBean.getCurrentAddressBuildingNo()));

                        TextView app_guarantor_roomno = previewDialog.findViewById(R.id.lbl_guarantor_room_no);
                        app_guarantor_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                        TextView value_guarantor_roomno = previewDialog.findViewById(R.id.val_guarantor_room_no);
                        value_guarantor_roomno.setText(getStringValue(guarantorFormBean.getCurrentAddressRoomNo()));

                        TextView app_guarantor_floorno = previewDialog.findViewById(R.id.lbl_guarantor_floor_no);
                        app_guarantor_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                        TextView value_guarantor_floorno = previewDialog.findViewById(R.id.val_guarantor_floor_no);
                        value_guarantor_floorno.setText(getStringValue(guarantorFormBean.getCurrentAddressFloor()));

                        TextView app_guarantor_street = previewDialog.findViewById(R.id.lbl_guarantor_street);
                        app_guarantor_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                        TextView value_guarantor_street = previewDialog.findViewById(R.id.val_guarantor_street);
                        value_guarantor_street.setText(getStringValue(guarantorFormBean.getCurrentAddressStreet()));

                        TextView app_guarantor_quarter = previewDialog.findViewById(R.id.lbl_guarantor_quarter);
                        app_guarantor_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                        TextView value_guarantor_quarter = previewDialog.findViewById(R.id.val_guarantor_quarter);
                        value_guarantor_quarter.setText(getStringValue(guarantorFormBean.getCurrentAddressQtr()));

                        TextView app_guarantor_city = previewDialog.findViewById(R.id.lbl_guarantor_city);
                        app_guarantor_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                        TextView value_guarantor_city = previewDialog.findViewById(R.id.val_guarantor_city);
                        value_guarantor_city.setText(getStringValue(getCity(cityId, cityList, guarantorFormBean.getCurrentAddressCity())));

                        TextView app_guarantor_township = previewDialog.findViewById(R.id.lbl_guarantor_township);
                        app_guarantor_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                        TextView value_guarantor_township = previewDialog.findViewById(R.id.val_guarantor_township);
                        value_guarantor_township.setText(getStringValue(getTownship(cityTownshipList, guarantorFormBean.getCurrentAddressCity(), guarantorFormBean.getCurrentAddressTownship())));

                        /*Guarantor Company Address*/
                        TextView app_guarantor_company_buildingno = previewDialog.findViewById(R.id.lbl_guarantor_company_buildingNo);
                        app_guarantor_company_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                        TextView value_guarantor_company_buildingno = previewDialog.findViewById(R.id.val_guarantor_company_buildingNo);
                        value_guarantor_company_buildingno.setText(getStringValue(guarantorFormBean.getCompanyAddressBuildingNo()));

                        TextView app_guarantor_company_roomno = previewDialog.findViewById(R.id.lbl_guarantor_company_room_no);
                        app_guarantor_company_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                        TextView value_guarantor_company_roomno = previewDialog.findViewById(R.id.val_guarantor_company_room_no);
                        value_guarantor_company_roomno.setText(getStringValue(guarantorFormBean.getCompanyAddressRoomNo()));

                        TextView app_guarantor_company_floorno = previewDialog.findViewById(R.id.lbl_guarantor_company_floor_no);
                        app_guarantor_company_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                        TextView value_guarantor_company_floorno = previewDialog.findViewById(R.id.val_guarantor_company_floor_no);
                        value_guarantor_company_floorno.setText(getStringValue(guarantorFormBean.getCompanyAddressFloor()));

                        TextView app_guarantor_company_street = previewDialog.findViewById(R.id.lbl_guarantor_company_street);
                        app_guarantor_company_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                        TextView value_guarantor_company_street = previewDialog.findViewById(R.id.val_guarantor_company_street);
                        value_guarantor_company_street.setText(getStringValue(guarantorFormBean.getCompanyAddressStreet()));

                        TextView app_guarantor_company_quarter = previewDialog.findViewById(R.id.lbl_guarantor_company_quarter);
                        app_guarantor_company_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                        TextView value_guarantor_company_quarter = previewDialog.findViewById(R.id.val_guarantor_company_quarter);
                        value_guarantor_company_quarter.setText(getStringValue(guarantorFormBean.getCompanyAddressQtr()));

                        TextView app_guarantor_company_city = previewDialog.findViewById(R.id.lbl_guarantor_company_city);
                        app_guarantor_company_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                        TextView value_guarantor_company_city = previewDialog.findViewById(R.id.val_guarantor_company_city);
                        value_guarantor_company_city.setText(getStringValue(getCity(cityId, cityList, guarantorFormBean.getCompanyAddressCity())));

                        TextView app_guarantor_company_township = previewDialog.findViewById(R.id.lbl_guarantor_company_township);
                        app_guarantor_company_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                        TextView value_guarantor_company_township = previewDialog.findViewById(R.id.val_guarantor_company_township);
                        value_guarantor_company_township.setText(getStringValue(getTownship(cityTownshipList, guarantorFormBean.getCompanyAddressCity(), guarantorFormBean.getCompanyAddressTownship())));

                        //Set Attachments.
                        List<ApplicationInfoAttachmentResBean> applicationInfoAttachmentResBeanList
                                = applicationDetailInfoResBean.getApplicationInfoAttachmentDtoList();
                        RecyclerView rvDetailAttach = previewDialog.findViewById(R.id.rv_attach_img);
                        ApplicationDetailAttachRVAdapter adapter
                                = new ApplicationDetailAttachRVAdapter(applicationInfoAttachmentResBeanList, DAEnquiryFragment.this);
                        rvDetailAttach.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                        rvDetailAttach.setAdapter(adapter);

                        previewDialog.show();

                    } else {
                        showMessageDialog("No data found.");
                    }

                } else {
                    showMessageDialog("No data found.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ApplicationDetailInfoResBean>> call, Throwable t) {
                showMessageDialog("Service temporary unavailable.");
            }
        });

    }

    @Override
    public void onTabAttach(String imageUrl) {
        Log.e("TAG", "You Tab : " + imageUrl);
    }

    @Override
    public void onViewPurchaseDetail(int daApplicationInfoId) {

        final String accessToken = PreferencesManager.getAccessToken(getActivity());
        final PurchaseDetailInfoReqBean purchaseDetailInfoReqBean
                = new PurchaseDetailInfoReqBean();
        purchaseDetailInfoReqBean.setDaApplicationInfoId(daApplicationInfoId);

        Service purchaseDetailViewService = APIClient.getApplicationRegisterService();
        final Call<BaseResponse<PurchaseDetailInfoResBean>> req = purchaseDetailViewService
                .getPurchaseDetailInfo(accessToken, purchaseDetailInfoReqBean);

        req.enqueue(new Callback<BaseResponse<PurchaseDetailInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<PurchaseDetailInfoResBean>> call, Response<BaseResponse<PurchaseDetailInfoResBean>> response) {
                BaseResponse<PurchaseDetailInfoResBean> baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        PurchaseDetailInfoResBean purchaseDetailInfo = (PurchaseDetailInfoResBean) baseResponse.getData();

                        if (purchaseDetailInfo != null) {
                            List<PurchaseProductInfoBean> productList = purchaseDetailInfo.getPurchaseInfoProductDtoList();
                            PurchaseProductInfoBean productOne = productList.get(0);

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.purchase_info_detail_layout);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                            LinearLayout productTwoLayout = dialog.findViewById(R.id.product_two_layout);

                            ImageView imgBack = dialog.findViewById(R.id.purchase_detail_back);
                            imgBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            DecimalFormat df = new DecimalFormat("###,###");
                            String curlang = PreferencesManager.getCurrentLanguage(getActivity());

                            TextView detailTitle = dialog.findViewById(R.id.lbl_purchase_title);
                            detailTitle.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_title, getActivity()));

                            TextView lblOutletName = dialog.findViewById(R.id.outlet_name);
                            lblOutletName.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_outlet_name, getActivity()));
                            TextView outletName = dialog.findViewById(R.id.lbl_outlet_name);
                            outletName.setText(purchaseDetailInfo.getOutletName());

                            /*TextView lblLoanType = dialog.findViewById(R.id.loan_type);
                            lblLoanType.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_loan_type, getActivity()));
                            loanType = dialog.findViewById(R.id.lbl_loan_type);
                            setLoanTypeName(purchaseDetailInfo.getDaLoanTypeId());*/

                            TextView lblProductCate = dialog.findViewById(R.id.product_category_txt);
                            lblProductCate.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_product_category, getActivity()));
                            TextView productCateVal = dialog.findViewById(R.id.product_category_val);
                            productCateVal.setText(getProductCate(productOne.getDaProductTypeId()));

                            TextView lblProductDesc = dialog.findViewById(R.id.product_description_txt);
                            lblProductDesc.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_product_description, getActivity()));
                            TextView productDescVal = dialog.findViewById(R.id.product_description_val);
                            productDescVal.setText(productOne.getProductDescription());

                            TextView lblBrand = dialog.findViewById(R.id.brand);
                            lblBrand.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_brand, getActivity()));
                            TextView brand = dialog.findViewById(R.id.lbl_brand_val);
                            brand.setText(productOne.getBrand());

                            TextView lblModel = dialog.findViewById(R.id.model);
                            lblModel.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_model, getActivity()));
                            TextView model = dialog.findViewById(R.id.lbl_model_val);
                            model.setText(productOne.getModel());

                            TextView lblPrice = dialog.findViewById(R.id.price);
                            lblPrice.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_price, getActivity()));
                            TextView price = dialog.findViewById(R.id.lbl_price_val);
                            price.setText(df.format(productOne.getPrice()) + " MMK");

                            TextView loanType = dialog.findViewById(R.id.loan_type_val);
                            loanType.setText(getLoanType(productOne.getDaLoanTypeId()));

                            TextView lblProductCate_2 = dialog.findViewById(R.id.product_category_txt_2);
                            lblProductCate_2.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_product_category, getActivity()));
                            TextView productCate_2 = dialog.findViewById(R.id.product_category_val_2);

                            TextView lblProductDesc_2 = dialog.findViewById(R.id.product_description_txt_2);
                            lblProductDesc_2.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_product_description, getActivity()));
                            TextView productDesc_2 = dialog.findViewById(R.id.product_description_val_2);

                            TextView lblBrand_2 = dialog.findViewById(R.id.brand_2);
                            lblBrand_2.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_brand, getActivity()));
                            TextView brand_2 = dialog.findViewById(R.id.lbl_brand_val_2);

                            TextView lblModel_2 = dialog.findViewById(R.id.model_2);
                            lblModel_2.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_model, getActivity()));
                            TextView model_2 = dialog.findViewById(R.id.lbl_model_val_2);

                            TextView lblPrice_2 = dialog.findViewById(R.id.price_2);
                            lblPrice_2.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_price, getActivity()));
                            TextView price_2 = dialog.findViewById(R.id.lbl_price_val_2);

                            if (productList.size() > 1) {
                                PurchaseProductInfoBean productTwo = productList.get(1);
                                productCate_2.setText(getProductCate(productTwo.getDaProductTypeId()));
                                productDesc_2.setText(productTwo.getProductDescription());
                                brand_2.setText(productTwo.getBrand());
                                model_2.setText(productTwo.getModel());
                                price_2.setText(df.format(productTwo.getPrice()) + " MMK");
                                productTwoLayout.setVisibility(View.VISIBLE);
                            } else {
                                productTwoLayout.setVisibility(View.GONE);
                            }

                            TextView lblLocation = dialog.findViewById(R.id.location_txt);
                            lblLocation.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_location, getActivity()));
                            TextView locationVal = dialog.findViewById(R.id.location_val);
                            locationVal.setText(purchaseDetailInfo.getPurchaseLocation());

                            TextView lblCashdownAmount = dialog.findViewById(R.id.cashdown_amount);
                            lblCashdownAmount.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_cashdown_amount, getActivity()));
                            TextView cashDownAmt = dialog.findViewById(R.id.lbl_cash_down_val);
                            cashDownAmt.setText(df.format(productOne.getCashDownAmount()) + " MMK");

                            TextView lblPurchaseDate = dialog.findViewById(R.id.purchase_date);
                            lblPurchaseDate.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_date, getActivity()));
                            TextView purchaseDate = dialog.findViewById(R.id.lbl_purchase_date);
                            String purchaseDateVal = purchaseDetailInfo.getPurchaseDate();
                            if (purchaseDateVal != null) {
                                purchaseDate.setText(CommonUtils.stringToYMDDateFormatStr(purchaseDateVal));
                            }

                            TextView lblInvoiceNo = dialog.findViewById(R.id.invoice_no);
                            lblInvoiceNo.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_invoice_no, getActivity()));
                            TextView invoiceNo = dialog.findViewById(R.id.lbl_invoice_val);
                            invoiceNo.setText(purchaseDetailInfo.getInvoiceNo());

                            TextView lblAgreementNo = dialog.findViewById(R.id.agreement_no);
                            lblAgreementNo.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_agreement_no, getActivity()));
                            TextView agreementNo = dialog.findViewById(R.id.lbl_agg_no_val);
                            agreementNo.setText(purchaseDetailInfo.getAgreementNo());

                            TextView lblFinanceAmount = dialog.findViewById(R.id.finance_amount);
                            lblFinanceAmount.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_finance_amount, getActivity()));
                            TextView financeAmt = dialog.findViewById(R.id.lbl_finance_amt);
                            financeAmt.setText(df.format(purchaseDetailInfo.getFinanceAmount()) + " MMK");

                            TextView lblTermOfFinance = dialog.findViewById(R.id.term_of_finance);
                            lblTermOfFinance.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_term_of_finance, getActivity()));
                            TextView financeTerm = dialog.findViewById(R.id.lbl_finance_term);
                            financeTerm.setText(String.valueOf(purchaseDetailInfo.getFinanceTerm()) + " Months");

                            TextView lblProcessingFees = dialog.findViewById(R.id.processing_fees);
                            lblProcessingFees.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_processing_fees, getActivity()));
                            TextView processingFee = dialog.findViewById(R.id.lbl_processing_fee_val);
                            processingFee.setText(df.format(purchaseDetailInfo.getProcessingFees()) + " MMK");

                            TextView lblCompulsorySaving = dialog.findViewById(R.id.compulsory_saving);
                            lblCompulsorySaving.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_compulsory_saving, getActivity()));
                            TextView compSaving = dialog.findViewById(R.id.lbl_com_saving_val);
                            compSaving.setText(df.format(purchaseDetailInfo.getCompulsoryAmount()) + " MMK");

                            TextView lblSettlementAmount = dialog.findViewById(R.id.settlement_amount);
                            lblSettlementAmount.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_settlement_amount, getActivity()));
                            TextView sattAmt = dialog.findViewById(R.id.lbl_sett_amt_val);
                            sattAmt.setText(df.format(purchaseDetailInfo.getSettlementAmount()) + " MMK");

                            List<PurchaseInfoAttachmentResBean> purchaseInfoAttachmentDtoList
                                    = purchaseDetailInfo.getPurchaseInfoAttachmentDtoList();

                            List<PurchaseInfoAttachmentResBean> purchaseInfoOtherAttach = new ArrayList<>();

                            for (PurchaseInfoAttachmentResBean purchaseInfoAttach : purchaseInfoAttachmentDtoList) {
                                int fileType = purchaseInfoAttach.getFileType();
                                final String filePathUrl = ATTACHMENT_URL + purchaseInfoAttach.getFilePath();

                                switch (fileType) {
                                    case TYPE_MEMBER_CARD:
                                        LinearLayout layoutMemberCard = dialog.findViewById(R.id.layout_member_card);
                                        ImageView imgMemberCard = dialog.findViewById(R.id.img_member_card);
                                        TextView lblImgMemberCard = dialog.findViewById(R.id.lbl_img_member_card);
                                        lblImgMemberCard.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_img_member_card, getActivity()));
                                        Glide.with(layoutMemberCard).load(filePathUrl).placeholder(R.drawable.noimage).into(imgMemberCard);
                                        layoutMemberCard.setVisibility(View.VISIBLE);
                                        imgMemberCard.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showDetailPhotoDialog(filePathUrl);
                                            }
                                        });
                                        break;
                                    case TYPE_INVOICE:
                                        LinearLayout layoutInvoice = dialog.findViewById(R.id.layout_invoice);
                                        ImageView imgInvoice = dialog.findViewById(R.id.img_invoice_view);
                                        TextView lblImgInvoice = dialog.findViewById(R.id.lbl_img_invoice_view);
                                        lblImgInvoice.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_img_invoice, getActivity()));
                                        Glide.with(layoutInvoice).load(filePathUrl).placeholder(R.drawable.noimage).into(imgInvoice);
                                        layoutInvoice.setVisibility(View.VISIBLE);
                                        imgInvoice.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showDetailPhotoDialog(filePathUrl);
                                            }
                                        });
                                        break;
                                    case TYPE_AGREEMENT:
                                        LinearLayout layoutAgreement = dialog.findViewById(R.id.layout_agreement);
                                        ImageView imgAgreement = dialog.findViewById(R.id.img_agreement);
                                        TextView lblImgAgreement = dialog.findViewById(R.id.lbl_img_agreement);
                                        lblImgAgreement.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_img_agreement, getActivity()));
                                        Glide.with(layoutAgreement).load(filePathUrl).placeholder(R.drawable.noimage).into(imgAgreement);
                                        layoutAgreement.setVisibility(View.VISIBLE);
                                        imgAgreement.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showDetailPhotoDialog(filePathUrl);
                                            }
                                        });
                                        break;
                                    case TYPE_ULOAN:
                                        LinearLayout layoutUloan = dialog.findViewById(R.id.layout_uloan);
                                        ImageView imgUloan = dialog.findViewById(R.id.img_uloan);
                                        TextView lblImgUloan = dialog.findViewById(R.id.lbl_img_uloan);
                                        lblImgUloan.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_img_uloan, getActivity()));
                                        Glide.with(layoutUloan).load(filePathUrl).placeholder(R.drawable.noimage).into(imgUloan);
                                        layoutUloan.setVisibility(View.VISIBLE);
                                        imgUloan.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showDetailPhotoDialog(filePathUrl);
                                            }
                                        });
                                        break;
                                    case TYPE_CASH_RECEIPT:
                                        LinearLayout layoutCashReceipt = dialog.findViewById(R.id.layout_cash_receipt);
                                        ImageView imgCashReceipt = dialog.findViewById(R.id.img_cash_receipt);
                                        TextView lblImgCashReceipt = dialog.findViewById(R.id.lbl_img_cash_receipt);
                                        lblImgCashReceipt.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_img_cashReceipt, getActivity()));
                                        Glide.with(layoutCashReceipt).load(filePathUrl).placeholder(R.drawable.noimage).into(imgCashReceipt);
                                        layoutCashReceipt.setVisibility(View.VISIBLE);
                                        imgCashReceipt.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showDetailPhotoDialog(filePathUrl);
                                            }
                                        });
                                        break;
                                    case TYPE_OTHERS:
                                        purchaseInfoAttach.setFilePath(filePathUrl);
                                        purchaseInfoOtherAttach.add(purchaseInfoAttach);
                                        break;
                                }
                            }

                            if (!purchaseInfoOtherAttach.isEmpty()) {
                                LinearLayout layoutOthersAttach = dialog.findViewById(R.id.layout_others_info);
                                TextView lblImgOtherAttach = dialog.findViewById(R.id.lbl_others_attach);
                                lblImgOtherAttach.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.purchase_img_other, getActivity()));
                                layoutOthersAttach.setVisibility(View.VISIBLE);
                                otherAttachRVAdapter = new PurchaseDetailOtherAttachRVAdapter(purchaseInfoOtherAttach, DAEnquiryFragment.this);
                                rvOtherAttachments = dialog.findViewById(R.id.rv_others_attach);
                                rvOtherAttachments.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                                rvOtherAttachments.setAdapter(otherAttachRVAdapter);
                            }

                            dialog.show();

                        } else {
                            showMessageDialog("No data found.");
                        }

                    } else {
                        showMessageDialog("No data found.");
                    }

                } else {
                    showMessageDialog("No data found.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<PurchaseDetailInfoResBean>> call, Throwable t) {
                showMessageDialog("Service temporary unavailable.");
            }
        });

    }

    @Override
    public void onTouchOthersAttach(String imgUrl) {
        showDetailPhotoDialog(imgUrl);
    }

    @Override
    public void onEditAttachments(final int daApplicationInfoId) {

        PreferencesManager.removeEditPhotoList(getActivity());

        final String accessToken = PreferencesManager.getAccessToken(getActivity());

        ApplicationDetailInfoReqBean applicationDetailInfoReqBean
                = new ApplicationDetailInfoReqBean();
        applicationDetailInfoReqBean.setDaApplicationInfoId(daApplicationInfoId);

        Service applicationDetailViewService = APIClient.getApplicationRegisterService();
        final Call<BaseResponse<ApplicationDetailInfoResBean>> req =
                applicationDetailViewService.getApplicationDetailInfo(accessToken, applicationDetailInfoReqBean);

        req.enqueue(new Callback<BaseResponse<ApplicationDetailInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<ApplicationDetailInfoResBean>> call, Response<BaseResponse<ApplicationDetailInfoResBean>> response) {

                BaseResponse<ApplicationDetailInfoResBean> baseResponse = response.body();

                if (baseResponse != null) {

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        ApplicationDetailInfoResBean applicationDetailInfo
                                = (ApplicationDetailInfoResBean) baseResponse.getData();

                        if (applicationDetailInfo != null) {

                            List<ApplicationInfoAttachmentResBean> applicationInfoAttachmentDtoList
                                    = applicationDetailInfo.getApplicationInfoAttachmentDtoList();

                            if (applicationInfoAttachmentDtoList != null) {

                                if (applicationInfoAttachmentDtoList.size() > 0) {

                                    applicationAttachInfoEditResBeanList = new ArrayList<>();

                                    for (ApplicationInfoAttachmentResBean applicationAttachmentInfo : applicationInfoAttachmentDtoList) {

                                        ApplicationAttachInfoEditResBean applicationAttachInfo
                                                = new ApplicationAttachInfoEditResBean();
                                        applicationAttachInfo.setDaApplicationInfoAttachmentId(applicationAttachmentInfo.getDaApplicationInfoAttachmentId());
                                        applicationAttachInfo.setEditFlag(applicationAttachmentInfo.isEditFlag());
                                        applicationAttachInfo.setFilePath(applicationAttachmentInfo.getFilePath());
                                        applicationAttachInfo.setFileType(applicationAttachmentInfo.getFileType());
                                        applicationAttachInfo.setEdited(false);

                                        if (applicationAttachInfo.isEditFlag()) {
                                            applicationAttachInfoEditResBeanList.add(applicationAttachInfo);
                                        }
                                        //applicationAttachInfoEditResBeanList.add(applicationAttachInfo);
                                    }

                                    final Dialog dialog = new Dialog(getActivity());
                                    dialog.setContentView(R.layout.purchase_info_attach_edit_layout);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                                    ImageView imgBack = dialog.findViewById(R.id.purchase_detail_back);
                                    imgBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    Button btnSave = dialog.findViewById(R.id.btn_save_edit_attach);
                                    btnSave.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            final ProgressDialog loadDialog = new ProgressDialog(getActivity());
                                            loadDialog.setMessage("Uploading images...");
                                            loadDialog.setCancelable(false);
                                            loadDialog.show();

                                            List<PurchaseAttachEditTempBean> purchaseAttachEditTempBeanList
                                                    = PreferencesManager.getEditPhotoList(getActivity());

                                            if (purchaseAttachEditTempBeanList != null) {

                                                if (purchaseAttachEditTempBeanList.size() > 0 && purchaseAttachEditTempBeanList.size() == applicationAttachInfoEditResBeanList.size()) {

                                                    PurchaseAttachEditReqBean purchaseAttachEditReqBean
                                                            = new PurchaseAttachEditReqBean();
                                                    purchaseAttachEditReqBean.setDaApplicationInfoId(daApplicationInfoId);

                                                    List<PurchaseAttachInfoEditReqBean> applicationInfoAttachmentDtoList
                                                            = new ArrayList<>();

                                                    try {
                                                        List<MultipartBody.Part> parts = new ArrayList<>();
                                                        for (PurchaseAttachEditTempBean purchaseAttachEditTempBean : purchaseAttachEditTempBeanList) {

                                                            PurchaseAttachInfoEditReqBean purchaseAttachInfoEditReqBean
                                                                    = new PurchaseAttachInfoEditReqBean();

                                                            purchaseAttachInfoEditReqBean
                                                                    .setDaApplicationInfoAttachmentId(purchaseAttachEditTempBean.getDaApplicationInfoAttachmentId());
                                                            purchaseAttachInfoEditReqBean
                                                                    .setFilePath(purchaseAttachEditTempBean.getFilePath());
                                                            purchaseAttachInfoEditReqBean
                                                                    .setFileType(purchaseAttachEditTempBean.getFileType());
                                                            purchaseAttachInfoEditReqBean.setFileName(purchaseAttachEditTempBean.getFileName());
                                                            applicationInfoAttachmentDtoList.add(purchaseAttachInfoEditReqBean);

                                                            File mFile = new File(purchaseAttachEditTempBean.getAbsFilePath());
                                                            parts.add(CameraUtil.prepareFilePart(CommonConstants.IMG, mFile));

                                                        }

                                                        if (applicationInfoAttachmentDtoList.size() > 0) {

                                                            purchaseAttachEditReqBean
                                                                    .setApplicationInfoAttachmentDtoList(applicationInfoAttachmentDtoList);

                                                            final String accessToken = PreferencesManager.getAccessToken(getActivity());

                                                            Service attachEditService = APIClient.getApplicationRegisterService();
                                                            Call<BaseResponse> req = attachEditService.doAttachmentEdit(accessToken, purchaseAttachEditReqBean, parts);

                                                            req.enqueue(new Callback<BaseResponse>() {

                                                                @Override
                                                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                                                    BaseResponse baseResponse = response.body();

                                                                    if (baseResponse != null) {
                                                                        if (baseResponse.getStatus().equals(SUCCESS)) {
                                                                            dialog.dismiss();
                                                                            loadDialog.dismiss();
                                                                            btnSearch.performClick();
                                                                            showMessageDialog("Attachment Edit Success!");
                                                                        } else {
                                                                            loadDialog.dismiss();
                                                                            showMessageDialog(baseResponse.getMessage());
                                                                        }
                                                                    } else {
                                                                        loadDialog.dismiss();
                                                                        showMessageDialog("No data found.");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                                                    loadDialog.dismiss();
                                                                    showMessageDialog("Service temporary unavailable.");
                                                                }

                                                            });

                                                        } else {
                                                            loadDialog.dismiss();
                                                            showMessageDialog("No more image have edited.");
                                                        }

                                                    } catch (Exception e) {
                                                        loadDialog.dismiss();
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    loadDialog.dismiss();
                                                    showMessageDialog("Please edit all images.");
                                                }

                                            } else {
                                                loadDialog.dismiss();
                                                showMessageDialog("No more image have edited.");
                                            }

                                        }
                                    });

                                    //bind data to rv.
                                    RecyclerView rvEditAttach = dialog.findViewById(R.id.rv_attach);
                                    attachEditRVAdapter = new DAEnquiryAttachEditRVAdapter(applicationAttachInfoEditResBeanList, DAEnquiryFragment.this);
                                    rvEditAttach.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                    rvEditAttach.setAdapter(attachEditRVAdapter);

                                    dialog.show();

                                } else {
                                    showMessageDialog("No Data found.");
                                }
                            } else {
                                showMessageDialog("No data found.");
                            }
                        } else {
                            showMessageDialog("No data found.");
                        }
                    } else {
                        showMessageDialog("No data found.");
                    }
                } else {
                    showMessageDialog("No data found.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ApplicationDetailInfoResBean>> call, Throwable t) {
                showMessageDialog("Service temporary unavailable.");
            }
        });
    }

    @Override
    public void onEditAttach(ApplicationAttachInfoEditResBean purchaseAttachInfo, View itemView, SendMessageImageView imgAttach, int position) {
        curDaAppInfoAttachmentId = purchaseAttachInfo.getDaApplicationInfoAttachmentId();
        imgCurrentAttach = imgAttach;
        curFileType = purchaseAttachInfo.getFileType();
        curFilePath = purchaseAttachInfo.getFilePath();
        editPosition = position;

        PopupMenu popup = new PopupMenu(getActivity(), itemView);
        // if applicant photo , show only camera menu
        if (purchaseAttachInfo.getFileType() == CommonConstants.PHOTO_APPLICANT) {
            popup.getMenuInflater().inflate(R.menu.camera_popup, popup.getMenu());
        } else {
            popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final String menuItem = item.getTitle().toString();

                if (menuItem.equals(CAMERA)) {
                    if (isCameraAllowed()) {
                        openCamera();
                    }
                } else if (menuItem.equals(GALLERY)) {
                    if (isStorageAllowed()) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                    }
                }
                return true;
            }
        });

        popup.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            File currentFile = null;

            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

                currentFile = new File(mCurrentPhotoPath);
                CameraUtil.resizeImages(currentFile.getAbsolutePath(), CommonConstants.PHOTO_QUALITY_95, getActivity(), CameraUtil.PURCHASE_APP_IMG_WIDTH, CameraUtil.PURCHASE_APP_IMG_HEIGHT);

            } else if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                //Load Image Dialog.
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                //selected file.
                File galleryFile = new File(picturePath);

                if (galleryFile.exists()) {
                    // copy from gallery to temp folder
                    File destFile = createFileName();
                    CameraUtil.copyFile(galleryFile, destFile);
                    currentFile = destFile;
                    CameraUtil.resizeImages(destFile.getAbsolutePath(), CommonConstants.PHOTO_QUALITY_95, getActivity(), CameraUtil.PURCHASE_APP_IMG_WIDTH, CameraUtil.PURCHASE_APP_IMG_HEIGHT);
                }
            }

            File renameFile = CameraUtil.renameFileName(getActivity(), currentFile, rootFolderName);
            Log.e("rename file", renameFile.getAbsolutePath());

            if (renameFile.exists()) {
                PurchaseAttachEditTempBean purchaseAttachEditTempBean
                        = new PurchaseAttachEditTempBean();
                purchaseAttachEditTempBean.setFileType(curFileType);
                purchaseAttachEditTempBean.setDaApplicationInfoAttachmentId(curDaAppInfoAttachmentId);
                purchaseAttachEditTempBean.setAbsFilePath(renameFile.getAbsolutePath());
                purchaseAttachEditTempBean.setFilePath(curFilePath);
                purchaseAttachEditTempBean.setFileName(renameFile.getName());
                addEditPhotoInfo(getActivity(), purchaseAttachEditTempBean);
                //imgCurrentAttach.setImageURI(Uri.fromFile(mFile));

                // For photo display on list
                applicationAttachInfoEditResBeanList.get(editPosition).setFilePath(renameFile.getAbsolutePath());
                applicationAttachInfoEditResBeanList.get(editPosition).setEdited(true);
                attachEditRVAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createFileName() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + rootFolderName);
        File photoCaptured = createTempFile(imageFileName, storageDir);
        return photoCaptured;
    }

    @Override
    public void onCancel(final int daApplicationInfoId, String language) {
        new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialDialogTheme)
                .setTitle(CommonUtils.getLocaleString(new Locale(language), R.string.app_cancel_title, getActivity()))
                .setMessage(CommonUtils.getLocaleString(new Locale(language), R.string.app_cancel_message, getActivity()))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

        /*new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialDialogTheme)
                .setTitle("Delete the application?")
                .setMessage("This will clear the application data and files that you have applied.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        final String accessToken = PreferencesManager.getAccessToken(getActivity());

                        PurchaseDetailInfoCancelReqBean purchaseDetailInfoCancel
                                = new PurchaseDetailInfoCancelReqBean();
                        purchaseDetailInfoCancel.setDaApplicationInfoId(daApplicationInfoId);

                        Service purchaseDetailCancelService = APIClient.getApplicationRegisterService();
                        Call<BaseResponse> req = purchaseDetailCancelService
                                .doPurchaseInfoCancel(accessToken, purchaseDetailInfoCancel);

                        req.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                BaseResponse baseResponse = response.body();
                                if (baseResponse != null) {
                                    if (baseResponse.getStatus().equals(SUCCESS)) {
                                        dialog.dismiss();
                                        *//*removePurchaseInfoItemFromList(daApplicationInfoId);
                                        adapter.notifyDataSetChanged();*//*
                                        searchApplicationList();
                                    } else {
                                        dialog.dismiss();
                                        showMessageDialog("Cancel Failed.");
                                    }
                                } else {
                                    dialog.dismiss();
                                    showMessageDialog("Cancel Failed.");
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                dialog.dismiss();
                                showMessageDialog("Service temporary unavailable.");
                            }
                        });
                    }
                })
                .show();*/
    }

    void enableSearch() {
        btnSearch.setEnabled(true);
    }

    void disableSearch() {
        btnSearch.setEnabled(false);
    }

    /*void showSearchTitle() {
        tvCategorySearched.setText("Category : " + "All");
    }*/

    void removePurchaseInfoItemFromList(int daApplicationInfoId) {
        int i = 0;
        for (ApplicationInfoResBean applicationInfo : applicationInfoList) {
            if (applicationInfo.getDaApplicationInfoId() == daApplicationInfoId) {
                applicationInfoList.remove(i);
                break;
            }
            i++;
        }
    }

    void setLoanTypeName(final int loanTypeId) {
        Service service = APIClient.getApplicationRegisterService();
        Call<BaseResponse<List<LoanTypeBean>>> req = service.getLoanTypes();
        req.enqueue(new Callback<BaseResponse<List<LoanTypeBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<LoanTypeBean>>> call, Response<BaseResponse<List<LoanTypeBean>>> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.getStatus().equals(SUCCESS)) {
                        List<LoanTypeBean> loanTypeBeanList = (List<LoanTypeBean>) baseResponse.getData();
                        for (LoanTypeBean loanTypeBean : loanTypeBeanList) {
                            if (loanTypeBean.getLoanTypeId() == loanTypeId) {
                                loanType.setText(loanTypeBean.getName());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<LoanTypeBean>>> call, Throwable t) {
            }
        });
    }

    void showMessageDialog(String message) {
        final AlertDialog alert = new MaterialAlertDialogBuilder(getActivity(), R.style.MaterialDialogTheme)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    void showDetailPhotoDialog(String imgUrl) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.purchase_detail_attach_img_view_layout);
        RelativeLayout parent = dialog.findViewById(R.id.detail_parent_layout);
        ImageView imgDetail = dialog.findViewById(R.id.img_detail_attach);
        Glide.with(parent).load(imgUrl).into(imgDetail);
        dialog.show();
    }

    private boolean isCameraAllowed() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isStorageAllowed() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }

    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri temUri;
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss_SSS").format(new Date());
        String imageFileName = timeStamp;
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + rootFolderName);
        File photoCaptured = createTempFile(imageFileName, storageDir);
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                temUri = FileProvider.getUriForFile(getActivity(),
                        getActivity().getPackageName()
                                + ".my.package.name.provider", new File(photoCaptured.getAbsolutePath()));
            } else {
                temUri = Uri.fromFile(photoCaptured);
            }
            mCurrentPhotoPath = photoCaptured.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, temUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            if (curFileType == CommonConstants.PHOTO_APPLICANT) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createTempFile(String imageFileName, File storageDir) {
        try {
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    DecimalFormat df = new DecimalFormat("###,###.#");

    //Detail Helper Functions.
    String getNationality(int nationality) {
        if (nationality != 0) {
            if (nationality == 1) {
                return "Myanmar";
            } else {
                return "Others";
            }
        }
        return "-";
    }

    String getGender(int gender) {
        if (gender != 0) {
            switch (gender) {
                case MALE:
                    return "Male";
                case FEMALE:
                    return "Female";
                default:
                    return "-";
            }
        }
        return "-";
    }

    String getMaritalStatus(int maritalStatus) {
        switch (maritalStatus) {
            case SINGLE:
                return "Single";
            case MARRIED:
                return "Married";
            default:
                return "-";
        }
    }

    String getResidentType(int residentType) {
        if (residentType > 0) {
            residentType = residentType - 1;
            String[] resident_type = getResources().getStringArray(R.array.applicant_resident_type);
            return resident_type[residentType];
        }
        return "-";
    }

    String getLivingWith(int livingWith) {
        if (livingWith > 0) {
            livingWith = livingWith - 1;
            String[] living_with = getResources().getStringArray(R.array.applicant_living_with);
            return living_with[livingWith];
        }
        return "-";
    }

    String getYearOfStay(int stayedYear, int stayedMonth) {
        String lblYearOfStay = BLANK;
        if (stayedYear == 0 && stayedMonth == 0) {
            return "-";
        }
        if (stayedYear > 1) {
            lblYearOfStay = stayedYear + " Years ";
        } else if (stayedYear != 0) {
            lblYearOfStay = stayedYear + " Year ";
        }
        if (stayedMonth > 1) {
            lblYearOfStay = lblYearOfStay + stayedMonth + " Months";
        } else if (stayedMonth != 0) {
            lblYearOfStay = lblYearOfStay + stayedMonth + " Month";
        }
        return lblYearOfStay;
    }

    String getYearOfService(int serviceYear, int serviceMonth) {
        String lblYearOfService = BLANK;
        if (serviceYear == 0 && serviceMonth == 0) {
            return "-";
        }
        if (serviceYear > 1) {
            lblYearOfService = serviceYear + " Years  ";
        } else if (serviceYear != 0) {
            lblYearOfService = serviceYear + " Year  ";
        }
        if (serviceMonth > 1) {
            lblYearOfService = lblYearOfService + serviceMonth + " Months";
        } else if (serviceMonth != 0) {
            lblYearOfService = lblYearOfService + serviceMonth + " Month";
        }
        return lblYearOfService;
    }

    String getContactTime(String timeFrom, String timeTo) {
        return timeFrom + " AM - " + timeTo + " PM";
    }

    String getCompanyStatus(int companyStatus) {
        if (companyStatus > 0) {
            companyStatus = companyStatus - 1;
            String[] company_status = getResources().getStringArray(R.array.occupation_company_status);
            return company_status[companyStatus];
        }
        return "-";
    }

    String getBasicIncome(Double basicIncome) {
        return df.format(basicIncome) + " MMK";
    }

    String getOtherIncome(Double otherIncome) {
        return df.format(otherIncome) + " MMK";
    }

    String getTotalIncome(Double totalIncome) {
        return df.format(totalIncome) + " MMK";
    }

    String getApplicationRelation(int relation) {
        if (relation > 0) {
            relation = relation - 1;
            String[] app_relations = getResources().getStringArray(R.array.applicant_relation);
            return app_relations[relation];
        }
        return "-";
    }

    String getGuarantorRelation(int relation) {
        if (relation > 0) {
            relation = relation - 1;
            String[] gur_relations = getResources().getStringArray(R.array.guarantor_relation);
            return gur_relations[relation];
        }
        return "-";
    }

    String getApplicationStatus(int status) {
        try {
            String[] applicationStatus = getResources().getStringArray(R.array.application_status_array);
            return applicationStatus[status];
        } catch (Exception e) {
            return "-";
        }
    }

    String getLoanType(int loanTypeId) {
        if (loanTypeId != 0) {
            switch (loanTypeId) {
                case ROLE_MOBILE:
                    return "Mobile-Loan";
                case ROLE_NON_MOBILE:
                    return "Non-Mobile";
                case ROLE_P_LOAN:
                    return "Personal-Loan";
                case ROLE_M_LOAN:
                    return "MotorCycle-Loan";
            }
        }
        return "-";
    }

    String getFinanceAmount(Double financeAmount) {
        if (financeAmount == 0) {
            return "0 MMK";
        }
        return df.format(financeAmount) + MYANMAR_CURRENCY;
    }

    String getLoanTerm(int loanTerm) {
        if (loanTerm != 0) {
            return String.valueOf(loanTerm) + LOAN_TERM_PERIOD;
        }
        return "-";
    }

    String getSalaryDate(String salaryDate) {
        if (!salaryDate.equals(null)) {
            return dateToString2(salaryDate);
        } else {
            return "-";
        }
    }

    String getSalaryDay(int salaryDay) {
        if (salaryDay != 0) {
            String dfDay = String.valueOf(salaryDay);
            return "Day " + dfDay;
        } else {
            return "-";
        }
    }

    String getMemberCardNo() {
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        UserInformationFormBean userInformationFormBean =
                new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);
        return userInformationFormBean.getMemberNo();
    }

    String getProductCategory(int categoryId) {
        for (int id = 0; id < productId.length; id++) {
            if (productId[id] == categoryId) {
                return productCategory[id];
            }
        }
        return "-";
    }

    String getPaymentAmount(Double payAmount) {
        return df.format(payAmount) + " MMK";
    }

    void setUpCityList(List<CityTownshipResBean> cityTownshipList) {
        cityList = new ArrayList<>();
        cityId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            cityList.add(listBean.getName());
            cityId.add(listBean.getCityId());
        }
    }

    String getCity(List<Integer> cityID, List<String> cityName, Integer cityId) {
        String city = "";
        for (int id = 0; id < cityID.size(); id++) {
            if (cityID.get(id) == cityId) {
                city = cityName.get(id);
            }
        }
        return city;
    }

    String getTownship(List<CityTownshipResBean> cityTownshipList, Integer cityId, Integer townId) {
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
            }
        }

        for (int id = 0; id < townshipId.size(); id++) {
            if (townshipId.get(id) == townId) {
                township = townshipList.get(id);
            }
        }

        return township;
    }

    private void getProductType(List<ProductTypeListBean> productTypeList, int listSize) {
        for (int list = 0; list < listSize; list++) {
            productCategory[list] = productTypeList.get(list).getName();
            productId[list] = productTypeList.get(list).getProductTypeId();
        }
    }

    private String getProductCate(int productId) {
        String productCate = "";
        List<ProductTypeListBean> productTypeList = PreferencesManager.getProductTypeList(getActivity());
        int listSize = productTypeList.size();

        for (int list = 0; list < listSize; list++) {
            if (productTypeList.get(list).getProductTypeId() == productId) {
                productCate = productTypeList.get(list).getName();
            }
        }
        return productCate;
    }

    String getCompanyStatusName(String[] statusList, int companyStatusId) {
        if (companyStatusId == 0) {
            return "";
        }
        int id = companyStatusId - 1;
        return statusList[id];
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    void saveProductType(List<ProductTypeListBean> productTypeList) {
        PreferencesManager.saveProductTypeList(getActivity(), productTypeList);
    }

    void saveCityInfo(List<CityTownshipResBean> cityTownshipList) {
        PreferencesManager.saveCityListInfo(getActivity(), cityTownshipList);
    }

    void setApplicationStatus(int status) {

        if (status >= 2 && status <= 7) {
            setStatusOnprogress();
        }

        if (status == 8) {
            setStatusCanceled();
        }

        if (status == 9) {
            setStatusRejected();
        }

        if (status >= 10 && status <= 16) {
            setStatusApproved();
        }

        if (status >= 17 && status <= 20) {
            setStatusPurchaseComplete();
        }

        /*switch (status){

            case 2 :
                setStatusNew();
                break;
            case 3 :
                setStatusIndex();
                break;
            case 4 :
                setStatusUploadFinished();
                break;
            case 5 :
                setStatusDocFuwaiting();
                break;
            case 6 :
                setStatusDocFuAppUpdated();
                break;
            case 7 :
                setStatusDocFuChecked();
                break;
            case 8 :
                setStatusCanceled();

                break;
            case 9 :
                setStatusRejected();
                break;
            case 10 :
                setStatusApproved();
                break;
            case 11 :
                setStatusPurchaseCancel();
                break;
            case 12 :
                setStatusPurchaseInitial();
                break;
            case 13 :
                setStatusPurchaseConfirmWaiting();
                break;
            case 14 :
                setStatusPurchaseConfirm();
                break;
            case 15 :
                setStatusPurchaseComplete();
                break;
            case 16 :
                setStatusSettlementUploadFinished();
                break;
            case 17 :
                setStatusSettlementPending();
                break;
        }*/
    }

    void searchApplicationList() {
        disableSearch();
        /*showSearchTitle();*/

        String applicationNo = textApplicationNo1.getText().toString() + textApplicationNo2.getText().toString() + textApplicationNo3.getText().toString();
        String appliedDate = textAppliedDate.getText().toString();

        Service applicationService = APIClient.getApplicationRegisterService();

        final String accessToken = PreferencesManager.getAccessToken(getActivity());
        ApplicationInfoReqBean applicationInfoReqBean = new ApplicationInfoReqBean();
        applicationInfoReqBean.setCustomerId(userInformationFormBean.getCustomerId());
        applicationInfoReqBean.setDaLoanTypeId(BLANK);

        /*if (selectedLoanTypeId > 0) {
            applicationInfoReqBean.setDaLoanTypeId(String.valueOf(selectedLoanTypeId));
        } else {
            applicationInfoReqBean.setDaLoanTypeId(BLANK);
        }*/

        if (selectedStatusId > 0) {
            applicationInfoReqBean.setStatus(String.valueOf(selectedStatusId));
        } else {
            applicationInfoReqBean.setStatus(BLANK);
        }

        if (!applicationNo.equals(BLANK)) {
            applicationInfoReqBean.setApplicationNo(applicationNo.trim());
        }

        if (!appliedDate.equals(BLANK)) {
            applicationInfoReqBean
                    .setAppliedDate(dateToString2(textAppliedDate.getText().toString()));
        }

        Call<BaseResponse<DAEnquiryResBean>> req = applicationService
                .getApplicationEnquiry(accessToken, applicationInfoReqBean);

        req.enqueue(new Callback<BaseResponse<DAEnquiryResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<DAEnquiryResBean>> call, Response<BaseResponse<DAEnquiryResBean>> response) {

                BaseResponse<DAEnquiryResBean> baseResponse = response.body();

                if (baseResponse != null) {

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        DAEnquiryResBean daEnquiryResBean = (DAEnquiryResBean) baseResponse.getData();

                        if (daEnquiryResBean != null) {
                            applicationInfoList = daEnquiryResBean.getApplicationInfoDtoList();
                            if (applicationInfoList.size() > 0) {
                                adapter = new DAEnquiryListRVAdapter(applicationInfoList, DAEnquiryFragment.this);
                                rvEnquiryList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                rvEnquiryList.setAdapter(adapter);
                                enableSearch();
                            } else {
                                adapter = new DAEnquiryListRVAdapter(applicationInfoList, DAEnquiryFragment.this);
                                rvEnquiryList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                                rvEnquiryList.setAdapter(adapter);
                                enableSearch();
                                showMessageDialog("No searched data found.");
                            }
                        } else {
                            enableSearch();
                            showMessageDialog("No searched data found.");
                        }

                    } else {
                        enableSearch();
                        showMessageDialog("No searched data found.");
                    }

                } else {

                    enableSearch();
                    showMessageDialog("No searched data found.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DAEnquiryResBean>> call, Throwable t) {
                enableSearch();
                showMessageDialog("Service temporary unavailable.");
            }
        });
    }

    public void changeLabel(String curLang) {
        btnClear.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.txt_clear, getContext()));
        btnSearch.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_search, getContext()));
        PreferencesManager.setCurrentLanguage(getContext(), curLang);
    }

    String getCompanyStatusPreview(int companyStatus, String other) {
        String[] company_status = getResources().getStringArray(R.array.occupation_company_status);
        int length = company_status.length;
        if (companyStatus > 0 && companyStatus < length) {
            companyStatus = companyStatus - 1;
            return company_status[companyStatus];
        } else if (companyStatus == length) {
            return other;
        }
        return "-";
    }

    public static String getStringValue(String text) {
        if (text == null || text == "") {
            return "-";
        } else if (TextUtils.isEmpty(text.trim())) {
            return "-";
        } else {
            return text;
        }
    }

    void setStatusRejected() {
        lblLcStatus.setText(STATUS_UNSUCESSFUL);
    }

    void setStatusCanceled() {
        lblLcStatus.setText(STATUS_CANCEL);
    }

    void setStatusOnprogress() {
        lblLcStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusNew() {
        lblLcStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusIndex() {
        lblLcStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusUploadFinished() {
        lblLcStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusDocFuwaiting() {
        lblLcStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusDocFuAppUpdated() {
        lblLcStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusDocFuChecked() {
        lblLcStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusApproved() {
        lblLcStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseCancel() {
        lblLcStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseInitial() {
        lblLcStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseConfirmWaiting() {
        lblLcStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseConfirm() {
        lblLcStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseComplete() {
        lblLcStatus.setText(STATUS_COMPLETE);
    }

    void setStatusSettlementUploadFinished() {
        lblLcStatus.setText(STATUS_COMPLETE);
    }

    void setStatusSettlementPending() {
        lblLcStatus.setText(STATUS_COMPLETE);
    }

    String getEducationPreview(int educationLevel) {
        if (educationLevel > 0) {
            educationLevel = educationLevel - 1;
            String[] education_level = getResources().getStringArray(R.array.applicant_education);
            return education_level[educationLevel];
        }
        return "-";
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        Log.e("change Label", "action");
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
    }


    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
        transaction.commit();
    }

    public boolean isMemberUser() {
        boolean userType = false;
        String userInfoJson = PreferencesManager.getCurrentUserInfo(getActivity());
        UserInformationFormBean userInfo = new Gson().fromJson(userInfoJson, UserInformationFormBean.class);
        if (userInfo.getCustomerTypeId() == 1) {
            userType = true;
        } else if (userInfo.getCustomerTypeId() == 2) {
            userType = false;
        }
        return userType;
    }
}
