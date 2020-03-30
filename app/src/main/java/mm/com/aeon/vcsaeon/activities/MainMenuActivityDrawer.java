package mm.com.aeon.vcsaeon.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import mm.com.aeon.vcsaeon.BuildConfig;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.adapters.MenuListRecyclerAdapter;
import mm.com.aeon.vcsaeon.beans.ApplicationDataFormBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoPhotoBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.BuyMsgReqInfo;
import mm.com.aeon.vcsaeon.beans.BuyReqBean;
import mm.com.aeon.vcsaeon.beans.DATempPhotoBean;
import mm.com.aeon.vcsaeon.beans.EmergencyContactFormBean;
import mm.com.aeon.vcsaeon.beans.GuarantorFormBean;
import mm.com.aeon.vcsaeon.beans.HowToUseVideoResBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.LogoutInfoReqBean;
import mm.com.aeon.vcsaeon.beans.MainMenuItemBean;
import mm.com.aeon.vcsaeon.beans.OccupationDataFormBean;
import mm.com.aeon.vcsaeon.beans.OfflineLogoutReqBean;
import mm.com.aeon.vcsaeon.beans.SingleLoginCheck;
import mm.com.aeon.vcsaeon.beans.SingleLoginStatus;
import mm.com.aeon.vcsaeon.beans.SubMenuItemBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CameraUtil;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.AccessPermissionResultDelegate;
import mm.com.aeon.vcsaeon.delegates.LogoutDelegate;
import mm.com.aeon.vcsaeon.delegates.MenuNavigatorDelegate;
import mm.com.aeon.vcsaeon.fragments.DAEnquiryFragment;
import mm.com.aeon.vcsaeon.fragments.EventNewsTabFragment;
import mm.com.aeon.vcsaeon.fragments.MainMenuWelcomeFragment;
import mm.com.aeon.vcsaeon.fragments.MemberCardFragment;
import mm.com.aeon.vcsaeon.fragments.MessagingTabFragment;
import mm.com.aeon.vcsaeon.fragments.NavApplyAeonServiceFragment;
import mm.com.aeon.vcsaeon.fragments.NavContactUsFragment;
import mm.com.aeon.vcsaeon.fragments.NavEventsAndNewsFragment;
import mm.com.aeon.vcsaeon.fragments.NavFAQFragment;
import mm.com.aeon.vcsaeon.fragments.NavFindNearOutletFragment;
import mm.com.aeon.vcsaeon.fragments.NavInformationUpdateFragment;
import mm.com.aeon.vcsaeon.fragments.NavLoanCalculationFragment;
import mm.com.aeon.vcsaeon.fragments.NavMembershipFragment;
import mm.com.aeon.vcsaeon.fragments.NavPurchaseMessagingTabFragment;
import mm.com.aeon.vcsaeon.fragments.NavSecQAInfoUpdateTabFragment;
import mm.com.aeon.vcsaeon.fragments.PagerRootFragment;
import mm.com.aeon.vcsaeon.fragments.PromotionTabFragment;
import mm.com.aeon.vcsaeon.fragments.SmallLoanApplicationDataFragment;
import mm.com.aeon.vcsaeon.fragments.SmallLoanOccupationFragment;
import mm.com.aeon.vcsaeon.fragments.VerificationMemberInfoFragment;
import mm.com.aeon.vcsaeon.fragments.VerifyConfirmPhotoFragment;
import mm.com.aeon.vcsaeon.fragments.VerifyPhotoUploadFragment;
import mm.com.aeon.vcsaeon.fragments.VerifySecurityQuestionFragment;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import mm.com.aeon.vcsaeon.networking.WebSocketClientListener;
import mm.com.aeon.vcsaeon.repositories.SingleLoginStatusRepository;
import mm.com.aeon.vcsaeon.viewmodels.MainMenuViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CALL_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_TYPE_MEMBER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CUSTOMER_TYPE_NON_MEMBER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOCATION_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REFRESH_TOKEN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.PreferencesManager.clearTempMemVerifyInfo;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.CODE_TOKEN_TIMEOUT;

public class MainMenuActivityDrawer extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MenuNavigatorDelegate, LogoutDelegate {

    static UserInformationFormBean userInformationFormBean;

    public static int gotoPage;
    public static List<Integer> errorPages = new ArrayList<Integer>();
    public static boolean appDataCorrect = true;
    public static boolean occuDataCorrect = true;
    public static boolean emerDataCorrect = true;
    public static boolean guraDataCorrect = true;
    public static boolean confirmDataCorrect = true;
    public static boolean submitAppData = true;
    public static boolean submitOccuData = true;
    public static boolean submitEmerData = true;
    public static boolean submitGuraData = true;
    public static boolean submitConfirmData = true;
    public static boolean appLoadingData = true;
    public static boolean occuLoadingData = true;
    public static boolean emerLoadingData = true;
    public static boolean guarLoadingData = true;
    public static boolean appcheck = true;
    public static ApplicationDataFormBean applicationData;
    public static OccupationDataFormBean occupationData;
    public static EmergencyContactFormBean emergencyContact;
    public static GuarantorFormBean guarantorData;
    public static boolean appSwipeCheck = true;

    public static boolean appDataErrMsgShow = false;
    public static boolean occupationErrMsgShow = false;
    public static boolean emergencyErrMsgShow = false;
    public static boolean guarantorErrMsgShow = false;

    public static boolean isSubmitclickGuaData = false;
    public static boolean isSubmitclickAppData = false;
    public static boolean isSubmitclickOccuData = false;
    public static boolean isSubmitclickEmerData = false;
    public static boolean isSubmitclickConfirmData = false;
    public static int lastLoanTerm;

    public static boolean doRegistrationSuccess = false;
    public static boolean isOccupationLanguageFlag = false;

    boolean doubleBackToExitPressedOnce = false;
    private boolean isLogout;

    Toolbar toolbar;
    //NavigationView navigationView;
    //DrawerLayout drawer;

    public static WebSocketClientListener socketClientListener;
    public static WebSocketClient menuSocketClient;

    public static WebSocketClientListener buySocketClientListener;
    public static WebSocketClient menuBuySocketClient;

    public static TextView unReadMessageCount;
    public static TextView unReadBuyMsgCount;

    private int msgCount;
    private int buyMsgCount;

    private ImageView imgEditBackground;
    private ImageView imgEditCamera;

    private String curLang;
    private int currMenuIndex;

    Timer timer;
    Timer singleLoginCheckTimer;

    static ProgressDialog logoutDialog;

    List<MainMenuItemBean> mainMenuList;
    RecyclerView mRecyclerView;
    MenuListRecyclerAdapter menuListRecyclerAdapter;

    public static List<DATempPhotoBean> tempPhotoBeanList;
    public static List<ApplicationInfoPhotoBean> photoList;

    ImageView myTitleBtn;
    ImageView engTitleBtn;
    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LanguageChangeListener languageChangeListener;
    AccessPermissionResultDelegate locationDelegate;

    MainMenuViewModel mainMenuViewModel;

    @Override
    public void onClickLogout() {
        doLogout(userInformationFormBean);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_drawer);
        showSnackBarMessage(getString(R.string.login_success));

        // msssage count reset
        msgCount = 0;
        buyMsgCount = 0;
        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getApplicationContext());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);
        PreferencesManager.setHotlinePhone(getApplicationContext(), userInformationFormBean.getHotlinePhone());

        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);
        menuBarName = toolbar.findViewById(R.id.menu_bar_name);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText(R.string.menu_level_two);
        menuBarPhoneNo.setText(userInformationFormBean.getPhoneNo());
        menuBarName.setText(userInformationFormBean.getName());
        menuBarName.setVisibility(View.VISIBLE);

        myTitleBtn = toolbar.findViewById(R.id.my_flag);
        engTitleBtn = toolbar.findViewById(R.id.en_flag);

        myTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setCurrentLanguage(getApplicationContext(), myTitleBtn.getTag().toString());
                languageChangeListener.changeLanguageTitle(myTitleBtn.getTag().toString());
            }
        });

        engTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setCurrentLanguage(getApplicationContext(), engTitleBtn.getTag().toString());
                languageChangeListener.changeLanguageTitle(engTitleBtn.getTag().toString());
            }
        });

        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageChangeListener.clickMenuBarBackBtn();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));
        curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());

        if (curLang.equals(LANG_MM)) {
            changeNavLabel(LANG_MM);
        } else {
            changeNavLabel(LANG_EN);
        }

       /* int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeLocationRequest();
        }*/
        CameraUtil.isCameraAllowed(this);
        CameraUtil.isStorageAllowed(this);

        getSupportActionBar().setTitle(R.string.menu_welcome);
        setUpDAData();
        tempPhotoBeanList = new ArrayList();
        photoList = new ArrayList();
        CameraUtil.isCameraAllowed(this);
        CameraUtil.isStorageAllowed(this);
        if (PreferencesManager.isSelectedPhotoExisted(this)) {
            List<DATempPhotoBean> daTempPhotoBeans
                    = PreferencesManager.getSelectedPhotos(this);
            if (daTempPhotoBeans != null) {
                for (DATempPhotoBean daTempPhotoBean : daTempPhotoBeans) {
                    File file = new File(daTempPhotoBean.getAbsFilePath());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }

        PreferencesManager.clearSelectedPhoto(getApplicationContext());
        PreferencesManager.removeDaftSavedErrInfo(getApplicationContext());
        MainMenuWelcomeFragment fragment = new MainMenuWelcomeFragment();
        fragment.setLogoutDelegate(MainMenuActivityDrawer.this);
        replaceFragment(fragment);

        /*drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setMinimumWidth(20);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (navigationView != null) {
            Log.e("draw setup", ":::::::::::true");
            setupDrawerContent(navigationView);
        }


        this.mainMenuList = new ArrayList<>();
        prepareListData();
        Log.e("menu list", Integer.toString(mainMenuList.size()));
        mRecyclerView  = findViewById(R.id.menuRecycler);
        menuListRecyclerAdapter = new MenuListRecyclerAdapter(this,mainMenuList, MainMenuActivityDrawer.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(menuListRecyclerAdapter);
        mRecyclerView.setHasFixedSize(true);

        //Header-View
        View headerView = navigationView.getHeaderView(0);
        final ImageView profileImgView = headerView.findViewById(R.id.profileImg);
        imgEditBackground = headerView.findViewById(R.id.ic_edit_bg);
        imgEditCamera = headerView.findViewById(R.id.ic_edit_camera);
        TextView textUserName = headerView.findViewById(R.id.user_name);
        textUserName.setText(userInformationFormBean.getName());
        TextView textCustNo = headerView.findViewById(R.id.customer_no);
        textCustNo.setText(userInformationFormBean.getCustomerNo());

        if(userInformationFormBean.getCustomerTypeId()==CUSTOMER_TYPE_MEMBER){
            imgEditBackground.setVisibility(View.VISIBLE);
            imgEditCamera.setVisibility(View.VISIBLE);
        }

        //Set Profile Image.
        final String imagePath = userInformationFormBean.getPhotoPath();
        if(imagePath==null || imagePath==BLANK) {
            Picasso.get().load(R.drawable.no_profile_image).into(profileImgView);
        } else {
            Picasso.get().load(imagePath).into(profileImgView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    Picasso.get().load(imagePath).transform(new CircleTransform()).into(profileImgView);
                }
                @Override
                public void onError(Exception e) {
                    Picasso.get().load(R.drawable.no_profile_image).into(profileImgView);
                }
            });
        }

        profileImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int customerTypeId = userInformationFormBean.getCustomerTypeId();
                if(customerTypeId == CUSTOMER_TYPE_MEMBER){
                    Intent intent = intentProfilePhotoEdit(MainMenuActivityDrawer.this);
                    startActivity(intent);
                }else{
                    final Dialog dialog = new Dialog(MainMenuActivityDrawer.this);
                    dialog.setContentView(R.layout.profile_edit_info);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button btnOk = dialog.findViewById(R.id.button_ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                hideKeyboard(MainMenuActivityDrawer.this);
                curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
                changeNavLabel(curLang);
                Log.e("drawer open", curLang);
                if(menuSocketClient != null){
                    //socketClient.close();
                    if (menuSocketClient.isClosed()) {
                        displayUnreadMessageCount();
                        mainMenuList.get(7).setMessageCount(0);
                    } else {
                        menuSocketClient.send("unReadMesgCount:");
                    }
                } else {
                    mainMenuList.get(7).setMessageCount(0);
                    displayUnreadMessageCount();
                }

                if(menuBuySocketClient != null){

                    if (menuBuySocketClient.isClosed()){
                        mainMenuList.get(9).setMessageCount(0);
                        displayUnreadBuyMsgCount();

                    } else {
                        BuyReqBean<BuyMsgReqInfo> brandsReqBean = new BuyReqBean();
                        brandsReqBean.setApi("get-unread-message-count");
                        BuyMsgReqInfo buyMsgReqInfo = new BuyMsgReqInfo();
                        buyMsgReqInfo.setCustomerId(userInformationFormBean.getCustomerId());
                        brandsReqBean.setParam(buyMsgReqInfo);
                        final String get_unread_message_count = new Gson().toJson(brandsReqBean);
                        menuBuySocketClient.send(get_unread_message_count);
                    }

                } else {
                    displayUnreadBuyMsgCount();
                }
                menuListRecyclerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if(menuSocketClient!=null){
                    //menuSocketClient.close();
                }
                if(menuBuySocketClient!=null){
                    //menuBuySocketClient.close();
                }
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
        */

        setUpViewModel();
        setUpTimerTask();
    }

    public void setLanguageListener(LanguageChangeListener languageChangeListener) {
        this.languageChangeListener = languageChangeListener;
    }

    public void setAccessDelegate(AccessPermissionResultDelegate locationDelegate) {
        this.locationDelegate = locationDelegate;
    }

    @Override
    public void onClickMsgMenuItem(View view) {

        //TextView menuName = (TextView) view;
        int menuIndex = (Integer) view.getTag();
        currMenuIndex = menuIndex;

        //Toast.makeText(this, "click message menu " + menuIndex , Toast.LENGTH_SHORT).show();
        switch (menuIndex) {
            case 7:
                closePurchaseMessageSocket();
                setActionBarLabel(curLang, R.string.nav_contact_us);
                Fragment myFragment = getSupportFragmentManager().findFragmentByTag("TAG");
                if (!(myFragment instanceof MessagingTabFragment)) {
                    closeMessagingSocket();
                    replaceFragment(new MessagingTabFragment());
                }
                break;

            case 9:
                int permission = ContextCompat.checkSelfPermission(MainMenuActivityDrawer.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    makeLocationRequest();
                } else {
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    setActionBarLabel(curLang, R.string.nav_shopping);
                    replaceFragment(new NavPurchaseMessagingTabFragment());
                }
                break;
        }

        //drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onClickMenuItem(View view, LinearLayout linearLayout_childItems) {
        if (view.getId() == R.id.submenu || view.getId() == R.id.iconimage) {
            if (linearLayout_childItems.getVisibility() == View.VISIBLE) {
                linearLayout_childItems.setVisibility(View.GONE);
            } else {
                linearLayout_childItems.setVisibility(View.VISIBLE);
            }

            //TextView menuName = (TextView) view;
            int menuIndex = (Integer) view.getTag();
            currMenuIndex = menuIndex;
            int permission;
            //Log.e("menu index",""+currMenuIndex);
            switch (menuIndex) {
                case 0:
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    setActionBarLabel(curLang, R.string.nav_home);
                    replaceFragment(new MainMenuWelcomeFragment());
                    break;

                case 1:
                    setActionBarLabel(curLang, R.string.nav_mem_title);
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    if (userInformationFormBean.getCustomerTypeId() == CUSTOMER_TYPE_MEMBER) {
                        replaceFragment(new MemberCardFragment());

                    } else if (userInformationFormBean.getCustomerTypeId() == CUSTOMER_TYPE_NON_MEMBER) {
                        replaceFragment(new NavMembershipFragment());
                    }
                    break;

                case 2:
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    setActionBarLabel(curLang, R.string.nav_info_update);
                    replaceFragment(new NavSecQAInfoUpdateTabFragment());
                    break;

                case 3:
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    return;

                case 4:
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    setActionBarLabel(curLang, R.string.nav_loan_calculator);
                    replaceFragment(new NavLoanCalculationFragment());
                    break;

                case 5:
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    setActionBarLabel(curLang, R.string.nav_events_news);
                    replaceFragment(new NavEventsAndNewsFragment());
                    break;

                case 6:
                    permission = ContextCompat.checkSelfPermission(MainMenuActivityDrawer.this,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        makeLocationRequest();
                    } else {
                        closeMessagingSocket();
                        closePurchaseMessageSocket();
                        setActionBarLabel(curLang, R.string.nav_near_outlet);
                        replaceFragment(new NavFindNearOutletFragment());
                    }
                    break;

                case 7:
                    closePurchaseMessageSocket();
                    setActionBarLabel(curLang, R.string.nav_contact_us);
                    Fragment myFragment = getSupportFragmentManager().findFragmentByTag("TAG");
                    if (!(myFragment instanceof MessagingTabFragment)) {
                        closeMessagingSocket();
                        replaceFragment(new MessagingTabFragment());
                    }
                    break;

                case 8:
                    closeMessagingSocket();
                    closePurchaseMessageSocket();
                    setActionBarLabel(curLang, R.string.nav_faq);
                    replaceFragment(new NavFAQFragment());
                    break;

                case 9:
                    permission = ContextCompat.checkSelfPermission(MainMenuActivityDrawer.this,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        makeLocationRequest();
                    } else {
                        closeMessagingSocket();
                        //closePurchaseMessageSocket();
                        setActionBarLabel(curLang, R.string.nav_shopping);
                        replaceFragment(new NavPurchaseMessagingTabFragment());
                    }
                    break;

                case 10:
                    closeMessagingSocket();
                    closeMenuSocket();
                    closeMenuBuySocket();
                    closePurchaseMessageSocket();
                    doLogout(userInformationFormBean);
                    PreferencesManager.clearCurrentUserInfo(getApplicationContext());
                    PreferencesManager.clearCouponInfo(getApplicationContext());
                    Intent intent = new Intent(MainMenuActivityDrawer.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;

            }
        }
        //drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onClickSubMenuItem(View view) {
        //TextView menuName = (TextView) view;
        int menuIndex = (Integer) view.getTag();
        currMenuIndex = menuIndex;

        switch (menuIndex) {
            case 11:
                setActionBarLabel(curLang, R.string.aeon_service_da_title);
                setUpDAData();
                tempPhotoBeanList = new ArrayList();
                photoList = new ArrayList();
                CameraUtil.isCameraAllowed(this);
                CameraUtil.isStorageAllowed(this);
                if (PreferencesManager.isSelectedPhotoExisted(this)) {

                    List<DATempPhotoBean> daTempPhotoBeans
                            = PreferencesManager.getSelectedPhotos(this);

                    for (DATempPhotoBean daTempPhotoBean : daTempPhotoBeans) {
                        File file = new File(daTempPhotoBean.getAbsFilePath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

                PreferencesManager.clearSelectedPhoto(getApplicationContext());
                PreferencesManager.removeDaftSavedErrInfo(getApplicationContext());
                replaceFragment(new PagerRootFragment());
                break;

            case 12:
                setActionBarLabel(curLang, R.string.aeon_service_enquiry_title);
                CameraUtil.isCameraAllowed(this);
                CameraUtil.isStorageAllowed(this);
                replaceFragment(new DAEnquiryFragment());
                break;
        }
        //drawer.closeDrawer(GravityCompat.START);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        //drawer.closeDrawers();
                        //drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    private void prepareListData() {
        ArrayList<SubMenuItemBean> subMenuList;
        /////////
        subMenuList = new ArrayList<>();
        MainMenuItemBean menu0 = new MainMenuItemBean();
        menu0.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_home, getApplicationContext()));
        menu0.setIconImage(R.drawable.ic_home_black_nav_24dp);
        menu0.setIndex(0);
        menu0.setHasMessageCount(false);
        menu0.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu0);

        MainMenuItemBean menu1 = new MainMenuItemBean();
        menu1.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_membership, getApplicationContext()));
        menu1.setIconImage(R.drawable.ic_person_black_nav_24dp);
        menu1.setIndex(1);
        menu1.setHasMessageCount(false);
        menu1.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu1);

        MainMenuItemBean menu2 = new MainMenuItemBean();
        menu2.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_info_update, getApplicationContext()));
        menu2.setIconImage(R.drawable.ic_update_black_24dp);
        menu2.setIndex(2);
        menu2.setHasMessageCount(false);
        menu2.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu2);

        ArrayList<SubMenuItemBean> msgSubMenuList = new ArrayList<>();
        msgSubMenuList.add(new SubMenuItemBean("Application Form", 11));
        msgSubMenuList.add(new SubMenuItemBean("Application Inquiry", 12));

        MainMenuItemBean menu3 = new MainMenuItemBean();
        menu3.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_aeon_service, getApplicationContext()));
        menu3.setIconImage(R.drawable.aeon_service);
        menu3.setIndex(3);
        menu3.setHasMessageCount(false);
        menu3.setSubMenuNameList(msgSubMenuList);
        mainMenuList.add(menu3);

        MainMenuItemBean menu4 = new MainMenuItemBean();
        menu4.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_loan_calculator, getApplicationContext()));
        menu4.setIconImage(R.drawable.loan_calculator);
        menu4.setIndex(4);
        menu4.setHasMessageCount(false);
        menu4.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu4);

        MainMenuItemBean menu5 = new MainMenuItemBean();
        menu5.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_events_news, getApplicationContext()));
        menu5.setIconImage(R.drawable.ic_event_note_black_24dp);
        menu5.setIndex(5);
        menu5.setHasMessageCount(false);
        menu5.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu5);

        MainMenuItemBean menu6 = new MainMenuItemBean();
        menu6.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_near_outlet, getApplicationContext()));
        menu6.setIconImage(R.drawable.ic_location_on_black_24dpx);
        menu6.setIndex(6);
        menu6.setHasMessageCount(false);
        menu6.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu6);

        MainMenuItemBean menu7 = new MainMenuItemBean();
        menu7.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_contact_us, getApplicationContext()));
        menu7.setIconImage(R.drawable.ic_contact_phone_black_24dp);
        menu7.setIndex(7);
        menu7.setHasMessageCount(true);
        menu7.setMessageCount(0);
        menu7.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu7);

        MainMenuItemBean menu8 = new MainMenuItemBean();
        menu8.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_faq, getApplicationContext()));
        menu8.setIconImage(R.drawable.ic_question_answer_black_nav_24dp);
        menu8.setIndex(8);
        menu8.setHasMessageCount(false);
        menu8.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu8);

        MainMenuItemBean menu9 = new MainMenuItemBean();
        menu9.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_shopping, getApplicationContext()));
        menu9.setIconImage(R.drawable.ic_shopping_cart_black_24dp);
        menu9.setIndex(9);
        menu9.setHasMessageCount(true);
        menu9.setMessageCount(0);
        menu9.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu9);

        MainMenuItemBean menu10 = new MainMenuItemBean();
        menu10.setMenuName(CommonUtils.getLocaleString(new Locale(curLang), R.string.nav_logout, getApplicationContext()));
        menu10.setIconImage(R.drawable.ic_exit_to_app_black_24dp);
        menu10.setIndex(10);
        menu10.setHasMessageCount(false);
        menu10.setSubMenuNameList(subMenuList);
        mainMenuList.add(menu10);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if (curLang.equals(LANG_MM)) {
            changeNavLabel(LANG_MM);
            //refreshFragment(curLang);
        } else {
            changeNavLabel(LANG_EN);
            //refreshFragment(curLang);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer = new Timer();
        timer.schedule(new LogOutTimerTask(), CommonConstants.TIMEOUT_IN_MS); //auto logout in 5 minutes
        if (isLogout) {
            keepLastActivatedInfo(false);
        } else {
            keepLastActivatedInfo(true);
        }
    }

    @Override
    public void onBackPressed() {

        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("TAG");
        if (myFragment instanceof VerificationMemberInfoFragment) {
            clearTempMemVerifyInfo(getApplicationContext()); //clear verification form data.
            replaceFragment(new NavMembershipFragment());

        } else if (myFragment instanceof VerifyPhotoUploadFragment) {
            replaceFragment(new VerificationMemberInfoFragment());

        } else if (myFragment instanceof VerifyConfirmPhotoFragment) {
            replaceFragment(new VerifyPhotoUploadFragment());

        } else {
            if (doubleBackToExitPressedOnce) {
                //doLogout(userInformationFormBean);
                PreferencesManager.clearCurrentUserInfo(getApplicationContext());
                PreferencesManager.clearCouponInfo(getApplicationContext());
                PreferencesManager.keepMainNavFlag(getApplicationContext(), true);
                Intent intent = new Intent(MainMenuActivityDrawer.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            showSnackBarMessage(getString(R.string.logout_alert_in_back));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (menuSocketClient != null) {
            if (!menuSocketClient.isClosed()) {
                menuSocketClient.close();
            }
        }
        cancelTimerTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
            menu.getItem(0).setTitle(LANG_EN);
            changeNavLabel(LANG_MM);
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
            menu.getItem(0).setTitle(LANG_MM);
            changeNavLabel(LANG_EN);
        }*/
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                //drawer.openDrawer(GravityCompat.START);
                //Log.e("drawer refresh home", item.getItemId()+"");
                return true;
        }

        curLang = item.getTitle().toString();
        //Log.e("drawer refresh index", currMenuIndex+" " + curLang);

        switch (currMenuIndex) {
            case 0:
                setActionBarLabel(curLang, R.string.nav_home);
                break;

            case 1:
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeNavLabel(LANG_MM);
                    addValueToPreference(LANG_MM);
                    refreshFragment(LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeNavLabel(LANG_EN);
                    addValueToPreference(LANG_EN);
                    refreshFragment(LANG_EN);
                }
                return true;

            case 2:
                setActionBarLabel(curLang, R.string.nav_info_update);
                break;

            case 3:
                //setActionBarLabel(curLang, R.string.aeon_service_da_title);
                break;

            case 4:
                setActionBarLabel(curLang, R.string.nav_loan_calculator);
                break;

            case 5:
                setActionBarLabel(curLang, R.string.nav_events_news);
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeNavLabel(LANG_MM);
                    addValueToPreference(LANG_MM);
                    refreshFragment(LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeNavLabel(LANG_EN);
                    addValueToPreference(LANG_EN);
                    refreshFragment(LANG_EN);
                }
                return true;

            case 6:
                setActionBarLabel(curLang, R.string.nav_near_outlet);
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeNavLabel(LANG_MM);
                    addValueToPreference(LANG_MM);
                    refreshFragment(LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeNavLabel(LANG_EN);
                    addValueToPreference(LANG_EN);
                    refreshFragment(LANG_EN);
                }
                return true;

            case 7:
                setActionBarLabel(curLang, R.string.nav_contact_us);
                break;

            case 8:
                setActionBarLabel(curLang, R.string.nav_faq);
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeNavLabel(LANG_MM);
                    addValueToPreference(LANG_MM);
                    refreshFragment(LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeNavLabel(LANG_EN);
                    addValueToPreference(LANG_EN);
                    refreshFragment(LANG_EN);
                }
                return true;

            case 9:
                setActionBarLabel(curLang, R.string.nav_shopping);
                break;

            case 11:
                setActionBarLabel(curLang, R.string.aeon_service_da_title);
                break;

            case 12:
                setActionBarLabel(curLang, R.string.aeon_service_enquiry_title);
                break;
        }
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_favorite) {
            if(item.getTitle().equals(LANG_MM)){
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeNavLabel(LANG_MM);
                addValueToPreference(LANG_MM);
                //refreshFragment(LANG_MM);

            } else if(item.getTitle().equals(LANG_EN)){
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeNavLabel(LANG_EN);
                addValueToPreference(LANG_EN);
                //refreshFragment(LANG_EN);
            }
            return false;
        }*/
        /*return super.onOptionsItemSelected(item);*/
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if (curLang.equals(LANG_MM)) {
            changeNavLabel(LANG_MM);
        } else {
            changeNavLabel(LANG_EN);
        }

        //Log.e("Item select ", Integer.toString(item.getItemId()));

        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.commitAllowingStateLoss();
    }

    public void addValueToPreference(String lang) {
        PreferencesManager.setCurrentLanguage(getApplicationContext(), lang);
    }

    public void changeNavLabel(String language) {
/*

        this.mainMenuList.get(0).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_home, getApplicationContext()));
        this.mainMenuList.get(1).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_membership, getApplicationContext()));
        this.mainMenuList.get(2).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_info_update, getApplicationContext()));
        this.mainMenuList.get(3).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_aeon_service, getApplicationContext()));
        this.mainMenuList.get(3).getSubMenuNameList().get(0).setSubMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_aeon_service_da, getApplicationContext()));
        this.mainMenuList.get(3).getSubMenuNameList().get(1).setSubMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_aeon_service_enquiry, getApplicationContext()));
        this.mainMenuList.get(4).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_loan_calculator, getApplicationContext()));
        this.mainMenuList.get(5).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_events_news, getApplicationContext()));
        this.mainMenuList.get(6).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_near_outlet, getApplicationContext()));
        this.mainMenuList.get(7).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_contact_us, getApplicationContext()));
        this.mainMenuList.get(8).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_faq, getApplicationContext()));
        this.mainMenuList.get(9).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_shopping, getApplicationContext()));
        this.mainMenuList.get(10).setMenuName(CommonUtils.getLocaleString(new Locale(language), R.string.nav_logout, getApplicationContext()));
        this.menuListRecyclerAdapter.notifyDataSetChanged();
*/

        PreferencesManager.setCurrentLanguage(getApplicationContext(), language);

    }

    //refresh fragment after language changed.
    public void refreshFragment(String language) {

        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("TAG");

        if (myFragment instanceof MemberCardFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_mem_title, getApplicationContext())));
            replaceFragment(new MemberCardFragment());
        }

        if (myFragment instanceof MainMenuWelcomeFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_home, getApplicationContext())));
            replaceFragment(new MainMenuWelcomeFragment());
        }

        if (myFragment instanceof NavMembershipFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_mem_title, getApplicationContext())));
            replaceFragment(new NavMembershipFragment());
        }

        if (myFragment instanceof NavInformationUpdateFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_info_update, getApplicationContext())));
            replaceFragment(new NavInformationUpdateFragment());
        }

        if (myFragment instanceof NavSecQAInfoUpdateTabFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_info_update, getApplicationContext())));
            replaceFragment(new NavSecQAInfoUpdateTabFragment());
        }

        if (myFragment instanceof NavApplyAeonServiceFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.aeon_service_da_title, getApplicationContext())));
            replaceFragment(new NavApplyAeonServiceFragment());
        }

        if (myFragment instanceof NavEventsAndNewsFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_events_news, getApplicationContext())));
            replaceFragment(new NavEventsAndNewsFragment());
        }

        if (myFragment instanceof MessagingTabFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_contact_us, getApplicationContext())));
            replaceFragment(new MessagingTabFragment());
            MessagingTabFragment.sendBtn.setText(CommonUtils.getLocaleString(new Locale(language), R.string.btn_send, getApplicationContext()));
            MessagingTabFragment.textMsg.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.hint_msg, getApplicationContext()));
        }

        if (myFragment instanceof NavFAQFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_faq, getApplicationContext())));
            replaceFragment(new NavFAQFragment());
        }

        if (myFragment instanceof VerificationMemberInfoFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_membership, getApplicationContext())));
            replaceFragment(new VerificationMemberInfoFragment());
        }

        if (myFragment instanceof VerifySecurityQuestionFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_membership, getApplicationContext())));
            replaceFragment(new VerifySecurityQuestionFragment());
        }

        if (myFragment instanceof NavSecQAInfoUpdateTabFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_info_update, getApplicationContext())));
            replaceFragment(new NavSecQAInfoUpdateTabFragment());
        }

        if (myFragment instanceof VerifyPhotoUploadFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_membership, getApplicationContext())));
            replaceFragment(new VerifyPhotoUploadFragment());
        }

        if (myFragment instanceof VerifyConfirmPhotoFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_membership, getApplicationContext())));
            replaceFragment(new VerifyConfirmPhotoFragment());
        }

        if (myFragment instanceof PromotionTabFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_events_news, getApplicationContext())));
            replaceFragment(new NavEventsAndNewsFragment());
        }

        if (myFragment instanceof EventNewsTabFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_events_news, getApplicationContext())));
            replaceFragment(new NavEventsAndNewsFragment());
        }

        if (myFragment instanceof NavFindNearOutletFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_near_outlet, getApplicationContext())));
            replaceFragment(new NavFindNearOutletFragment());
        }

        if (myFragment instanceof NavLoanCalculationFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_loan_calculator, getApplicationContext())));
            replaceFragment(new NavLoanCalculationFragment());
        }

        if (myFragment instanceof SmallLoanApplicationDataFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.aeon_service_da_title, getApplicationContext())));
            replaceFragment(new SmallLoanApplicationDataFragment());
        }

        if (myFragment instanceof SmallLoanOccupationFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.aeon_service_da_title, getApplicationContext())));
            replaceFragment(new SmallLoanOccupationFragment());
        }

        if (myFragment instanceof NavPurchaseMessagingTabFragment) {
            toolbar.setTitle((CommonUtils.getLocaleString(new Locale(language), R.string.nav_shopping, getApplicationContext())));
            replaceFragment(new NavPurchaseMessagingTabFragment());
        }
    }

    public void setActionBarLabel(String language, int message) {
        //final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        getSupportActionBar().setTitle((CommonUtils.getLocaleString(new Locale(language), message, getApplicationContext())));
    }

    private void keepLastActivatedInfo(Boolean isDestroy) {
        OfflineLogoutReqBean offlineLogoutReqBean = new OfflineLogoutReqBean();
        offlineLogoutReqBean.setCustomerId(userInformationFormBean.getCustomerId());
        offlineLogoutReqBean.setLogoutTime(CommonUtils.getCurTimeStringForLogout());
        final String lastActivatedInfo = new Gson().toJson(offlineLogoutReqBean);
        PreferencesManager.keepLastActivatedInfo(getApplicationContext(), lastActivatedInfo, isDestroy);
    }

    //logout | update end usage time.
    public void doLogout(UserInformationFormBean userInformationFormBean) {

        final LogoutInfoReqBean logoutInfoReqBean = new LogoutInfoReqBean(userInformationFormBean.getCustomerId());

        Service updateUsageTimeService = APIClient.getUserService();
        Call<BaseResponse> reqLogout = updateUsageTimeService.usrLogout(
                PreferencesManager.getAccessToken(getApplicationContext()), logoutInfoReqBean);

        MainMenuActivityDrawer.this.setTheme(R.style.MessageDialogTheme);
        logoutDialog = new ProgressDialog(MainMenuActivityDrawer.this);
        logoutDialog.setTitle(getString(R.string.logout_title));
        logoutDialog.setMessage(getString(R.string.logout_in_progress));
        logoutDialog.setCancelable(false);
        //logoutDialog.show();

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

                    startActivity(new Intent(MainMenuActivityDrawer.this, MainActivity.class));

                } else if (response.code() == CODE_TOKEN_TIMEOUT) {

                    //Refresh Token.
                    Service refreshTokenService = APIClient.getAuthUserService();
                    Call<BaseResponse<LoginAccessTokenInfo>> refreshToken = refreshTokenService.refreshToken(
                            REFRESH_TOKEN, PreferencesManager.getRefreshToken(getApplicationContext()));

                    refreshToken.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {

                            if (response.isSuccessful()) {
                                BaseResponse baseResponse = response.body();
                                if (baseResponse.getStatus().equals(SUCCESS)) {
                                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) baseResponse.getData();
                                    PreferencesManager.keepAccessToken(getApplicationContext(), loginAccessTokenInfo.getAccessToken());
                                    PreferencesManager.keepRefreshToken(getApplicationContext(), loginAccessTokenInfo.getRefreshToken());

                                    Service updateUsageTimeService = APIClient.getUserService();
                                    Call<BaseResponse> reqLogout = updateUsageTimeService.usrLogout(
                                            PreferencesManager.getAccessToken(getApplicationContext()), logoutInfoReqBean);

                                    reqLogout.enqueue(new Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                            if (response.isSuccessful()) {

                                                BaseResponse baseResponse = response.body();
                                                if (baseResponse.getStatus().equals(SUCCESS)) {
                                                    closeDialog();
                                                    keepLastActivatedInfo(false);
                                                    resetAllTabPosition();
                                                } else {
                                                    closeDialog();
                                                    keepLastActivatedInfo(true);
                                                }
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

                                } else {
                                    closeDialog();
                                    keepLastActivatedInfo(true);
                                }
                            } else {
                                closeDialog();
                                keepLastActivatedInfo(true);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                            closeDialog();
                            keepLastActivatedInfo(true);
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

    public void closeMessagingSocket() {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("TAG");
        if (myFragment instanceof NavContactUsFragment) {
            NavContactUsFragment navContactUsFragment = (NavContactUsFragment) myFragment;
            MessagingTabFragment messagingTabFragment = navContactUsFragment.adapter.tab2;
            if (!messagingTabFragment.mesgSocketClient.isClosed()) {
                messagingTabFragment.mesgSocketClient.close();
            }
        }
    }

    public void closePurchaseMessageSocket() {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("TAG");
        if (myFragment instanceof NavPurchaseMessagingTabFragment) {
            NavPurchaseMessagingTabFragment navPurchaseMessagingTabFragment =
                    (NavPurchaseMessagingTabFragment) myFragment;
            if (!navPurchaseMessagingTabFragment.webSocketClient.isClosed()) {
                navPurchaseMessagingTabFragment.webSocketClient.close();
            }
        }
    }

    private void displayUnreadMessageCount() {

        if (CommonUtils.isNetworkAvailable(this)) {

            final int senderId = userInformationFormBean.getCustomerId();
            final String phoneNo = userInformationFormBean.getPhoneNo();

            WebSocketClientListener.SocketListiner listener = new WebSocketClientListener.SocketListiner() {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    menuSocketClient.send("userName:" + phoneNo + "userId:" + senderId);
                    menuSocketClient.send("cr:" + phoneNo + "or:" + "userWithAgency:");
                }

                @Override
                public void onMessage(String message) {
                    final String messageStr = message;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(messageStr);
                                String type = object.getString("type");

                                if (type.equals("message")) {

                                    JSONObject data = new JSONObject(object.getString("data"));
                                    final int opSendFlag = data.getInt("op_send_flag");

                                    if (isMessagingSocketOpen()) {
                                        mainMenuList.get(7).setMessageCount(0);

                                    } else if (opSendFlag == 1) {
                                        msgCount = mainMenuList.get(7).getMessageCount();
                                        mainMenuList.get(7).setMessageCount(msgCount + 1);

                                        // expandableListAdapter.notifyDataSetChanged();

                                    }

                                } else if (type.equals("unReadMesgCountForMobile")) {

                                    JSONObject messageObj = object.getJSONObject("data");
                                    msgCount = Integer.parseInt(messageObj.getString("count"));
                                    Log.e("message count", Integer.toString(msgCount));
                                    mainMenuList.get(7).setMessageCount(msgCount);
                                    //expandableListAdapter.notifyDataSetChanged();

                                } else if (type.equals("room")) {

                                    menuSocketClient.send("unReadMesgCount:");
                                }
                                /*if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }*/
                                menuListRecyclerAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.e("TAG", "On Message Socket Closed.");
                }
            };

            socketClientListener = new WebSocketClientListener(getApplicationContext(), listener, BuildConfig.WEB_SOCKET_URL);

            try {
                menuSocketClient = socketClientListener.connectWebsocket();
            } catch (URISyntaxException e) {
                showSnackBarMessage("Socket Connect Exception.");
            }

        } else {
            showNetworkErrorDialog(MainMenuActivityDrawer.this, getNetErrMsg());
        }
    }

    private void displayUnreadBuyMsgCount() {

        if (CommonUtils.isNetworkAvailable(this)) {

            WebSocketClientListener.SocketListiner buyListener = new WebSocketClientListener.SocketListiner() {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    BuyReqBean<BuyMsgReqInfo> brandsReqBean = new BuyReqBean();
                    brandsReqBean.setApi("get-unread-message-count");
                    BuyMsgReqInfo buyMsgReqInfo = new BuyMsgReqInfo();
                    buyMsgReqInfo.setCustomerId(userInformationFormBean.getCustomerId());
                    brandsReqBean.setParam(buyMsgReqInfo);
                    final String get_unread_message_count = new Gson().toJson(brandsReqBean);
                    //Log.e("TAG","get_unread_message_count : " + get_unread_message_count);
                    menuBuySocketClient.send(get_unread_message_count);
                }

                @Override
                public void onMessage(final String message) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONObject object = new JSONObject(message);
                                String type = object.getString("type");

                                if (type.equals("get-unread-message-count")) {

                                    buyMsgCount = object.getInt("data");
                                    mainMenuList.get(9).setMessageCount(buyMsgCount);
                                    //Log.e("TAG", "onMessage() .... Count : " + object.getInt("data"));
                                }
                                //expandableListAdapter.notifyDataSetChanged();
                                menuListRecyclerAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.e("TAG", "On Buy Message Socket Closed.");
                }
            };

            buySocketClientListener = new WebSocketClientListener(getApplicationContext(), buyListener, BuildConfig.PL_CHAT_URL);

            try {
                menuBuySocketClient = buySocketClientListener.connectWebsocket();
            } catch (URISyntaxException e) {
                showSnackBarMessage("Buy Socket Connect Exception.");
            }

        } else {
            showNetworkErrorDialog(MainMenuActivityDrawer.this, getNetErrMsg());
        }
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getApplicationContext());
    }

    public boolean isMessagingSocketOpen() {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("TAG");
        if (myFragment instanceof NavContactUsFragment) {
            NavContactUsFragment navContactUsFragment = (NavContactUsFragment) myFragment;
            MessagingTabFragment messagingTabFragment = navContactUsFragment.adapter.tab2;
            if (!messagingTabFragment.mesgSocketClient.isClosed()) {
                return true;
            }
        }
        return false;
    }

    public void closeMenuSocket() {
        if (menuSocketClient != null) {
            if (!menuSocketClient.isClosed()) {
                menuSocketClient.close();
            }
        }
    }

    public void closeMenuBuySocket() {
        if (menuBuySocketClient != null) {
            if (!menuBuySocketClient.isClosed()) {
                menuBuySocketClient.close();
            }
        }
    }

    private class LogOutTimerTask extends TimerTask {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
                    final Dialog dialog = new Dialog(MainMenuActivityDrawer.this);
                    dialog.setContentView(R.layout.warning_message_dialog);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button btnOk = dialog.findViewById(R.id.btn_ok);
                    TextView messageBody = dialog.findViewById(R.id.text_message);
                    messageBody.setText(getSessionTimeOutMsg(curLang));

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            doLogout(userInformationFormBean);
                            PreferencesManager.clearCurrentUserInfo(getApplicationContext());
                            PreferencesManager.clearCouponInfo(getApplicationContext());
                            finish();
                            Intent intent = new Intent(MainMenuActivityDrawer.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });

                    if (!(MainMenuActivityDrawer.this).isFinishing()) {
                        dialog.show();
                    }
                }
            });
        }
    }

    public String getSessionTimeOutMsg(String language) {
        return (CommonUtils.getLocaleString(new Locale(language), R.string.session_time_out_msg, getApplicationContext()));
    }

    protected void makeLocationRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
    }

    private static Intent intentProfilePhotoEdit(Context context) {
        Intent intent = new Intent(context, ProfilePhotoUpdateActivity.class);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.content_main_drawer);
        if (frg instanceof MainMenuWelcomeFragment) {
            if (requestCode == 1000) {
                frg.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    //callback for permission granted from VerificationMemberInfoFragment.java
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            //Call Service.
            case CALL_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    showSnackBarMessage(getString(R.string.message_permission_deniled));
                } else {
                    final String hotlinePhoneNo = userInformationFormBean.getHotlinePhone();
                    if (hotlinePhoneNo == null || hotlinePhoneNo.equals(BLANK)) {
                        showSnackBarMessage(getString(R.string.message_call_not_available));
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX + hotlinePhoneNo));
                        startActivity(callIntent);
                    }
                }
                return;
            }

            //Location Service.
            case LOCATION_REQUEST_CODE: {

                Log.e("Request code", grantResults[0] + "");
                locationDelegate.onAccessRequestPermissionResult(requestCode, permissions, grantResults);
            }
        }
    }

    //reset all selected-tab position.
    private static void resetAllTabPosition() {
        MemberCardFragment.memberCardsTabPosition = 0;
        NavInformationUpdateFragment.infoUpdateTabPosition = 0;
        NavEventsAndNewsFragment.EventsNewsTabPosition = 0;
        NavFindNearOutletFragment.FindNearOutletTabPosition = 0;
        NavFAQFragment.faqTabPosition = 0;
    }

    void setUpDAData() {

        SharedPreferences validationPreferences = PreferencesManager.getCurrentUserPreferences(getApplicationContext());

        ApplicationRegisterSaveReqBean saveReqBean = new ApplicationRegisterSaveReqBean();
        String saveData = new Gson().toJson(saveReqBean);
        PreferencesManager.addEntryToPreferences(validationPreferences, "RegisterSaveData", saveData);
        PreferencesManager.clearCurrentUserPreferences(validationPreferences, "ApplicationData");
        PreferencesManager.clearCurrentUserPreferences(validationPreferences, "OccupationData");
        PreferencesManager.clearCurrentUserPreferences(validationPreferences, "EmergencyData");
        PreferencesManager.clearCurrentUserPreferences(validationPreferences, "GuarantorData");
        PreferencesManager.clearCurrentUserPreferences(validationPreferences, "ConfirmationData");
        MainMenuActivityDrawer.submitAppData = true;
        MainMenuActivityDrawer.submitOccuData = true;
        MainMenuActivityDrawer.submitEmerData = true;
        MainMenuActivityDrawer.submitGuraData = true;
        MainMenuActivityDrawer.submitConfirmData = true;
        MainMenuActivityDrawer.appLoadingData = true;
        MainMenuActivityDrawer.occuLoadingData = true;
        MainMenuActivityDrawer.emerLoadingData = true;
        MainMenuActivityDrawer.guarLoadingData = true;
        MainMenuActivityDrawer.appSwipeCheck = true;
        MainMenuActivityDrawer.gotoPage = 0;
    }

    void closeDialog() {
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
    }

    void setUpViewModel() {
        mainMenuViewModel = ViewModelProviders.of(this).get(MainMenuViewModel.class);
        mainMenuViewModel.init(getApplicationContext());
        mainMenuViewModel.getSingleLoginStatus().observe(this, new Observer<SingleLoginStatus>() {
            @Override
            public void onChanged(SingleLoginStatus singleLoginStatus) {
                if (singleLoginStatus != null) {
                    if (singleLoginStatus.isLogoutFlag()) {
                        cancelTimerTask();
                        displayAlertDialog();
                    }
                }
            }
        });
    }

    void setUpTimerTask() {
        singleLoginCheckTimer = new Timer();
        singleLoginCheckTimer.schedule(new SingleLoginCheckTimerTask(), 1000, 1000);
    }

    private class SingleLoginCheckTimerTask extends TimerTask {
        @Override
        public void run() {
            SingleLoginCheck singleLoginCheck =
                    PreferencesManager.getSingleLoginCheck(getApplicationContext());
            SingleLoginStatusRepository.getInstance().getSingleLoginStatus(singleLoginCheck);
        }
    }

    private void cancelTimerTask() {
        if (singleLoginCheckTimer != null) {
            singleLoginCheckTimer.cancel();
            singleLoginCheckTimer = null;
        }
    }

    private void displayAlertDialog() {
        final Dialog dialog = new Dialog(MainMenuActivityDrawer.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.login_alert_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogout(userInformationFormBean);
            }
        });
        dialog.show();
    }

}
