package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import mm.com.aeon.vcsaeon.BuildConfig;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.activities.VideoPlayActivity;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.LogoutInfoReqBean;
import mm.com.aeon.vcsaeon.beans.OfflineLogoutReqBean;
import mm.com.aeon.vcsaeon.beans.ProductTypeListBean;
import mm.com.aeon.vcsaeon.beans.StatusChangeCountReq;
import mm.com.aeon.vcsaeon.beans.StatusChangeCountRes;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.AccessPermissionResultDelegate;
import mm.com.aeon.vcsaeon.delegates.LogoutDelegate;
import mm.com.aeon.vcsaeon.delegates.MessageCountDisplayDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import mm.com.aeon.vcsaeon.repositories.MessagingRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_TYPE_MEMBER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_TYPE_NON_MEMBER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOCATION_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REFRESH_TOKEN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ZERO;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.hideKeyboard;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isNetworkAvailable;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.CODE_TOKEN_TIMEOUT;

public class MainMenuWelcomeFragment extends BaseFragment
        implements LanguageChangeListener,
        AccessPermissionResultDelegate,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MessageCountDisplayDelegate {

    View view;
    Timer unreadMessageCountTask;

    private TextView textLoanApplication;
    private TextView textMembership;
    StatusChangeCountRes statusChangeCountRes;
    StatusChangeCountReq statusChangeCountReq;

    private TextView textFindProduct;
    private TextView textCustomerService;
    private TextView textCalculator;
    private TextView textFindUs;

    private TextView textInformationUpdate;
    private TextView textGoodNews;
    private TextView textAnnouncement;

    private TextView textOurService;
    private TextView textFacebook;
    private TextView textHowToUse;
    private TextView textShare;
    private TextView menuStatusCount;

    private LinearLayout layoutLogout;

    private TextView textLogout;
    LogoutDelegate logoutDelegate;

    static ProgressDialog logoutDialog;

    private boolean isAgentChannel;
    private boolean isLogout;

    UserInformationFormBean userInformationFormBean;

    public MainMenuWelcomeFragment() {
        this.logoutDelegate = (MainMenuActivityDrawer) getActivity();
    }

    public MainMenuWelcomeFragment(LogoutDelegate logoutDelegate) {
        this.logoutDelegate = logoutDelegate;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_menu_welcome_fragment, container, false);
        setHasOptionsMenu(true);
        statusChangeCountReq = new StatusChangeCountReq();
        statusChangeCountRes = new StatusChangeCountRes();

        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(MainMenuWelcomeFragment.this);
        ((MainMenuActivityDrawer) getActivity()).setAccessDelegate(MainMenuWelcomeFragment.this);

        // change level title on toolbar
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        TextView menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarLevelInfo.setText(R.string.menu_level_two);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.GONE);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getContext());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        View viewLoanApplication = view.findViewById(R.id.include_loan_app);
        View viewMembership = view.findViewById(R.id.include_membership);

        View viewCustomerService = view.findViewById(R.id.include_customer_service);
        View viewCalculator = view.findViewById(R.id.include_calculator);
        View viewFindProduct = view.findViewById(R.id.include_find_product);
        View viewGoodNews = view.findViewById(R.id.include_news);
        View viewFindUs = view.findViewById(R.id.include_nearby);

        View viewOurService = view.findViewById(R.id.include_ourservice);
        View viewFacebook = view.findViewById(R.id.include_facebook);
        View viewAnnouncement = view.findViewById(R.id.include_announce);

        View viewInfoUpdate = view.findViewById(R.id.include_info_update);
        View viewHowToUse = view.findViewById(R.id.include_howToUse);
        View viewShare = view.findViewById(R.id.include_share);

        layoutLogout = view.findViewById(R.id.layoutLogout);

        textLoanApplication = viewLoanApplication.findViewById(R.id.text_loan_app);
        menuStatusCount = viewLoanApplication.findViewById(R.id.menu_status_count);

        if (!isNetworkAvailable(getContext())) {
            showNetworkErrorDialog(getActivity(), getNetErrMsg());
        } else {
            final ProgressDialog countDialog = new ProgressDialog(getActivity());
            countDialog.setMessage("Loading...");
            countDialog.setCancelable(false);
            countDialog.show();

            statusChangeCountReq.setCustomerId(userInformationFormBean.getCustomerId());
            Service loadCountInformation = APIClient.getApplicationRegisterService();
            Call<BaseResponse<StatusChangeCountRes>> productListReq = loadCountInformation.getMenuStatusCount(statusChangeCountReq);

            productListReq.enqueue(new Callback<BaseResponse<StatusChangeCountRes>>() {
                @Override
                public void onResponse(Call<BaseResponse<StatusChangeCountRes>> call, Response<BaseResponse<StatusChangeCountRes>> response) {
                    BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus().equals(SUCCESS)) {
                        statusChangeCountRes = (StatusChangeCountRes) baseResponse.getData();
                        if (statusChangeCountRes.getApplicationStatusChangedCount() == 0) {
                            menuStatusCount.setVisibility(View.GONE);
                            countDialog.dismiss();
                        } else {
                            countDialog.dismiss();
                            menuStatusCount.setVisibility(View.VISIBLE);
                            menuStatusCount.setText(String.valueOf(statusChangeCountRes.getApplicationStatusChangedCount()));
                        }
                    } else {
                        countDialog.dismiss();
                        showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<StatusChangeCountRes>> call, Throwable t) {
                    countDialog.dismiss();
                    showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                }
            });
        }

        textMembership = viewMembership.findViewById(R.id.text_memebership);

        textCustomerService = viewCustomerService.findViewById(R.id.text_customer_service);
        textCalculator = viewCalculator.findViewById(R.id.text_calculator);
        textFindProduct = viewFindProduct.findViewById(R.id.text_find_product);
        textGoodNews = viewGoodNews.findViewById(R.id.text_news);
        textFindUs = viewFindUs.findViewById(R.id.text_findus);

        textOurService = viewOurService.findViewById(R.id.text_ourservice);
        textFacebook = viewFacebook.findViewById(R.id.text_facebook);
        textAnnouncement = viewAnnouncement.findViewById(R.id.text_announce);

        textInformationUpdate = viewInfoUpdate.findViewById(R.id.text_info_update);
        textHowToUse = viewHowToUse.findViewById(R.id.text_howToUse);
        textShare = viewShare.findViewById(R.id.text_share);

        textLogout = view.findViewById(R.id.text_logout);
        PreferencesManager.keepMainNavFlag(getActivity(), false);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);
        changeLabel(curLang);

        //click loan application.
        viewLoanApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivityDrawer.isOccupationLanguageFlag = false;
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getActivity(), getNetErrMsg());
                } else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.fragment_application_form_and_enquiry);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.show();

                    final String language = PreferencesManager.getCurrentLanguage(getActivity());
                    Button btnAppForm = (Button) dialog.findViewById(R.id.btn_app_form);
                    Button btnAppEnquiry = (Button) dialog.findViewById(R.id.btn_app_enquiry);
                    btnAppForm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_application_form_button, getActivity()));
                    btnAppEnquiry.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_application_enquiry_button, getActivity()));

                    btnAppForm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            replaceFragment(new PagerRootFragment());
                        }
                    });

                    btnAppEnquiry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            if (!isNetworkAvailable(getContext())) {
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
                                                final List<ProductTypeListBean> productTypeList =
                                                        (List<ProductTypeListBean>) baseResponse.getData();
                                                if (productTypeList != null) {
                                                    saveProductType(productTypeList);
                                                    replaceFragment(new DAEnquiryFragment());
                                                }
                                                productListDialog.dismiss();
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
                        }
                    });
                }
            }
        });

        //click Membership.
        viewMembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
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
                                    final List<ProductTypeListBean> productTypeList =
                                            (List<ProductTypeListBean>) baseResponse.getData();
                                    if (productTypeList != null) {
                                        saveProductType(productTypeList);
                                        if (userInformationFormBean.getCustomerTypeId() == CUSTOMER_TYPE_MEMBER) {
                                            replaceFragment(new MembershipInformationFragment());
                                        } else if (userInformationFormBean.getCustomerTypeId() == CUSTOMER_TYPE_NON_MEMBER) {
                                            replaceFragment(new NavMembershipFragment());
                                        }
                                    }
                                    productListDialog.dismiss();
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
            }
        });

        //click Contact Us.
        viewCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    replaceFragment(new MessagingTabFragment());
                }
            }
        });

        //click Loan Calculator.
        viewCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    replaceFragment(new NavLoanCalculationFragment());
                }
            }
        });

        //click Agent Channel.
        viewFindProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    int permission = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    isAgentChannel = true;
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        makeLocationRequest();
                    } else {
                        openGoogleAPIClient();
                    }
                }
            }
        });

        //click Agent News.
        viewGoodNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    replaceFragment(new NavEventsAndNewsFragment());
                }
            }
        });

        //click Find Us.
        viewFindUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    isAgentChannel = false;
                    int permission = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        makeLocationRequest();
                    } else {
                        openGoogleAPIClient();
                    }
                }
            }
        });

        //click our service.
        viewOurService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    replaceFragment(new NavFAQFragment());
                }
            }
        });

        //click Facebook.
        viewFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    startActivity(getOpenFacebookIntent(getContext()));
                }
            }
        });

        //click Announcement.
        viewAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    replaceFragment(new PromotionTabFragment());
                }
            }
        });

        //click Information Update.
        viewInfoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    replaceFragment(new NavInformationUpdateFragment());
                }
            }
        });

        //click How To Use.
        viewHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(), getNetErrMsg());
                } else {
                    startActivity(new Intent(getActivity(), VideoPlayActivity.class));
                }
            }
        });

        //click Share.
        viewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "VCS Member");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Please choose one."));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*textLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogout(userInformationFormBean);
            }
        });*/

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogout(userInformationFormBean);
            }
        });

        hideKeyboard(getActivity());
        setUpTimerTask();

        return view;
    }

    public final void makeLocationRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
    }

    public LogoutDelegate getLogoutDelegate() {
        return logoutDelegate;
    }

    public void setLogoutDelegate(LogoutDelegate logoutDelegate) {
        this.logoutDelegate = logoutDelegate;
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

    public void changeLabel(String language) {
        textLoanApplication.setText(CommonUtils.getLocaleString(new Locale(language), R.string.welcome_loan_app_title, getActivity()));
        textMembership.setText(CommonUtils.getLocaleString(new Locale(language), R.string.welcome_membership_title, getActivity()));
        textLogout.setText(CommonUtils.getLocaleString(new Locale(language), R.string.logout_title, getActivity()));

        textCustomerService.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_contactus_title, getActivity()));
        textCalculator.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_calculator_title, getActivity()));
        textFindProduct.setText(CommonUtils.getLocaleString(new Locale(language), R.string.welcome_agent_channel_title, getActivity()));
        textFindUs.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_findus_title, getActivity()));

        textInformationUpdate.setText(CommonUtils.getLocaleString(new Locale(language), R.string.welcome_info_update_title, getActivity()));
        textGoodNews.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_news_title, getActivity()));
        textAnnouncement.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_announce_title, getActivity()));

        textOurService.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_ourservice_title, getActivity()));
        textFacebook.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_facebook_title, getActivity()));
        textHowToUse.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_video_title, getActivity()));
        textShare.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_share_title, getActivity()));
        PreferencesManager.setCurrentLanguage(getActivity(), language);
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getContext());
    }

    private static Intent getOpenFacebookIntent(Context context) {
        String url = "https://m.facebook.com/AEON-Microfinance-Myanmar-Co-Ltd-374820516619280/";
        Uri uri;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            uri = Uri.parse("fb://page/374820516619280");
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        } catch (ActivityNotFoundException e) {
            uri = Uri.parse(url);
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
    }

    //logout | update end usage time.
    public void doLogout(UserInformationFormBean userInformationFormBean) {

        final LogoutInfoReqBean logoutInfoReqBean = new LogoutInfoReqBean(userInformationFormBean.getCustomerId());

        Service updateUsageTimeService = APIClient.getUserService();
        Call<BaseResponse> reqLogout = updateUsageTimeService.usrLogout(
                PreferencesManager.getAccessToken(getContext()), logoutInfoReqBean);

        getActivity().setTheme(R.style.MessageDialogTheme);
        logoutDialog = new ProgressDialog(getActivity());
        logoutDialog.setTitle(getString(R.string.logout_title));
        logoutDialog.setMessage(getString(R.string.logout_in_progress));
        logoutDialog.setCancelable(false);

        reqLogout.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getStatus().equals(SUCCESS)) {
                        closeDialog();
                        isLogout = true;
                        keepLastActivatedInfo(false);
                        resetAllTabPosition();
                    } else {
                        closeDialog();
                        keepLastActivatedInfo(true);
                    }

                    getActivity().finish();
                    if (PreferencesManager.isRegistrationCompleted(getContext())) {
                        PreferencesManager.setRegistrationCompleted(getContext(), false);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else if (response.code() == CODE_TOKEN_TIMEOUT) {

                    //Refresh Token.
                    Service refreshTokenService = APIClient.getAuthUserService();
                    Call<BaseResponse<LoginAccessTokenInfo>> refreshToken = refreshTokenService.refreshToken(
                            REFRESH_TOKEN, PreferencesManager.getRefreshToken(getContext()));

                    refreshToken.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {

                            if (response.isSuccessful()) {
                                BaseResponse baseResponse = response.body();
                                if (baseResponse.getStatus().equals(SUCCESS)) {
                                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) baseResponse.getData();
                                    PreferencesManager.keepAccessToken(getContext(), loginAccessTokenInfo.getAccessToken());
                                    PreferencesManager.keepRefreshToken(getContext(), loginAccessTokenInfo.getRefreshToken());

                                    Service updateUsageTimeService = APIClient.getUserService();
                                    Call<BaseResponse> reqLogout = updateUsageTimeService.usrLogout(
                                            PreferencesManager.getAccessToken(getContext()), logoutInfoReqBean);

                                    reqLogout.enqueue(new Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                            if (response.isSuccessful()) {
                                                BaseResponse baseResponse = response.body();
                                                if (baseResponse.getStatus().equals(SUCCESS)) {
                                                    closeDialog();
                                                    keepLastActivatedInfo(false);
                                                    resetAllTabPosition();
                                                    getActivity().finish();
                                                } else {
                                                    closeDialog();
                                                    keepLastActivatedInfo(true);
                                                    getActivity().finish();
                                                }
                                            } else {
                                                closeDialog();
                                                keepLastActivatedInfo(true);
                                                getActivity().finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                                            closeDialog();
                                            keepLastActivatedInfo(true);
                                            getActivity().finish();
                                        }
                                    });

                                } else {
                                    closeDialog();
                                    keepLastActivatedInfo(true);
                                    getActivity().finish();
                                }
                            } else {
                                closeDialog();
                                keepLastActivatedInfo(true);
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                            closeDialog();
                            keepLastActivatedInfo(true);
                            getActivity().finish();
                        }
                    });

                } else {
                    closeDialog();
                    keepLastActivatedInfo(true);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                closeDialog();
                keepLastActivatedInfo(true);
            }
        });
    }

    void closeDialog() {
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
    }

    private void keepLastActivatedInfo(Boolean isDestroy) {
        OfflineLogoutReqBean offlineLogoutReqBean = new OfflineLogoutReqBean();
        offlineLogoutReqBean.setCustomerId(userInformationFormBean.getCustomerId());
        offlineLogoutReqBean.setLogoutTime(CommonUtils.getCurTimeStringForLogout());
        final String lastActivatedInfo = new Gson().toJson(offlineLogoutReqBean);
        PreferencesManager.keepMainNavFlag(getContext(), true);
        PreferencesManager.keepLastActivatedInfo(getContext(), lastActivatedInfo, isDestroy);
    }

    //reset all selected-tab position.
    private static void resetAllTabPosition() {
        MemberCardFragment.memberCardsTabPosition = 0;
        NavInformationUpdateFragment.infoUpdateTabPosition = 0;
        NavEventsAndNewsFragment.EventsNewsTabPosition = 0;
        NavFindNearOutletFragment.FindNearOutletTabPosition = 0;
        NavFAQFragment.faqTabPosition = 0;
    }

    @Override
    public void onAccessRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGoogleAPIClient();
                } else {
                    // Permission Denied
                    if (isAgentChannel) {
                        showWarningDialog(getActivity(), "Please access to display where are you now.");
                    } else {
                        showWarningDialog(getActivity(), "Please access to know the outlet information.");
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static void showWarningDialog(final Activity mContext, String message) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.warning_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        messageBody.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openGoogleAPIClient() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            if (isAgentChannel) {
                                replaceFragment(new NavPurchaseMessagingTabFragment());
                            } else {
                                replaceFragment(new NavFindNearOutletFragment());
                            }

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    void setUpTimerTask() {
        unreadMessageCountTask = new Timer();
        unreadMessageCountTask.schedule(new LoadMessageCountTask(), 1000, 1000);
    }

    void saveProductType(List<ProductTypeListBean> productTypeList) {
        PreferencesManager.saveProductTypeList(getActivity(), productTypeList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                if (isAgentChannel) {
                    replaceFragment(new NavPurchaseMessagingTabFragment());
                } else {
                    replaceFragment(new NavFindNearOutletFragment());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    void loadMessageCount() {
        final int customerId = userInformationFormBean.getCustomerId();
        MessagingRepository.getInstance().getL2UnreadMessageCount(customerId, this);
        MessagingRepository.getInstance().getAskProductMessageCount(customerId, this);
    }

    @Override
    public void onDisplayL2MessageCount(Integer messageCount) {
        displayL2UnreadMsgCount(messageCount);
    }

    @Override
    public void onDisplayProductAskMsgCount(Integer messageCount) {
        displayAskProductUnreadMsgCount(messageCount);
    }

    void displayL2UnreadMsgCount(Integer msgCount) {
        View viewCustomerService = view.findViewById(R.id.include_customer_service);
        TextView lblMessageCount = viewCustomerService.findViewById(R.id.lblL2MessageCount);
        if (msgCount == ZERO) {
            lblMessageCount.setVisibility(View.GONE);
        } else {
            lblMessageCount.setVisibility(View.VISIBLE);
            lblMessageCount.setText(msgCount > 99 ? "+99" : msgCount.toString());
        }
    }

    void displayAskProductUnreadMsgCount(Integer msgCount) {
        View viewFindProduct = view.findViewById(R.id.include_find_product);
        TextView lblMessageCount = viewFindProduct.findViewById(R.id.lblAskProdMessageCount);
        if (msgCount == ZERO) {
            lblMessageCount.setVisibility(View.GONE);
        } else {
            lblMessageCount.setVisibility(View.VISIBLE);
            lblMessageCount.setText(msgCount > 99 ? "+99" : msgCount.toString());
        }
    }

    class LoadMessageCountTask extends TimerTask {
        @Override
        public void run() {
            loadMessageCount();
        }
    }

    void cancelTimerTask() {
        if (unreadMessageCountTask != null) {
            unreadMessageCountTask.cancel();
            unreadMessageCountTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimerTask();
    }
}