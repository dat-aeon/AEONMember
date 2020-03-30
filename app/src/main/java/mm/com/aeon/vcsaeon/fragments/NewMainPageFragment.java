package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
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

import java.util.Locale;

import mm.com.aeon.vcsaeon.BuildConfig;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.LoginActivity;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.RegistrationActivity;
import mm.com.aeon.vcsaeon.activities.VideoPlayActivity;
import mm.com.aeon.vcsaeon.beans.AutoReplyMessageBean;
import mm.com.aeon.vcsaeon.beans.FreeMessageUserReqBean;
import mm.com.aeon.vcsaeon.beans.FreeMessageUserResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.AccessPermissionResultDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.AUTO_MESSAGE_REPLY;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOCATION_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.hideKeyboard;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.isNetworkAvailable;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class NewMainPageFragment extends BaseFragment implements LanguageChangeListener , AccessPermissionResultDelegate, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    View view;
    View viewLogin;
    View viewRegister;

    View viewFreeChat;
    View viewCalculator;
    View viewGoodNews;
    View viewFindUs;

    View viewOurService;
    View viewAnnouncement;

    View viewFacebook;
    View viewHowToUse;
    View viewShare;

    TextView textVersion;
    TextView textVersionLbl;
    TextView textLogin;
    TextView textRegisterNow;

    TextView textFreeChat;
    TextView textCalculator;
    TextView textGoodNews;
    TextView textFindUs;

    TextView textAboutUs;
    TextView textOurService;
    TextView textFacebook;

    TextView textAnnouncement;
    TextView textHowToUse;
    TextView textShare;

    String curVersion;
    RelativeLayout layoutLoading;
    ImageView imgLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_body_layout, container, false);
        setHasOptionsMenu(true);

        layoutLoading = view.findViewById(R.id.layout_loading);
        imgLoading = view.findViewById(R.id.img_loading);

        ((MainActivity)getActivity()).setLanguageListener(this);
        ((MainActivity)getActivity()).setLocationDelegate(this);
        Toolbar toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.GONE);

        try{
            textVersion = view.findViewById(R.id.text_version);
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            curVersion = packageInfo.versionName;
            textVersion.setText(curVersion);
        } catch (Exception e){
            e.printStackTrace();
        }

        viewLogin = view.findViewById(R.id.include_login);
        viewRegister = view.findViewById(R.id.include_register);

        viewFreeChat = view.findViewById(R.id.include_free_chat);
        viewCalculator = view.findViewById(R.id.include_calculator);
        viewGoodNews = view.findViewById(R.id.include_news);
        viewFindUs = view.findViewById(R.id.include_nearby);

        viewOurService = view.findViewById(R.id.include_ourservice);
        viewAnnouncement = view.findViewById(R.id.include_announce);

        viewFacebook = view.findViewById(R.id.include_facebook);
        viewHowToUse = view.findViewById(R.id.include_howToUse);
        viewShare = view.findViewById(R.id.include_share);

        //View aboutUs = findViewById(R.id.include_about_us);

        textLogin = viewLogin.findViewById(R.id.text_login);
        textRegisterNow = viewRegister.findViewById(R.id.text_register);

        textFreeChat = viewFreeChat.findViewById(R.id.text_free_chat);
        textCalculator = viewCalculator.findViewById(R.id.text_calculator);
        textGoodNews = viewGoodNews.findViewById(R.id.text_news);
        textFindUs = viewFindUs.findViewById(R.id.text_findus);

        textOurService = viewOurService.findViewById(R.id.text_ourservice);
        textAnnouncement = viewAnnouncement.findViewById(R.id.text_announce);

        textFacebook = viewFacebook.findViewById(R.id.text_facebook);
        textHowToUse = viewHowToUse.findViewById(R.id.text_howToUse);
        textShare = viewShare.findViewById(R.id.text_share);

        textVersionLbl = view.findViewById(R.id.text_version_lbl);
        //textAboutUs = aboutUs.findViewById(R.id.textView1);

        String curLang = PreferencesManager.getCurrentLanguage(getContext());
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        PreferencesManager.keepMainNavFlag(getActivity(),true);
        //click login.
        viewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getActivity(),getNetErrMsg());
                } else {
                    //startActivity(new Intent(getContext(), LoginActivity.class));
                    //delegate.startActivityFromMain();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    //showLoginPopup();
                }
            }
        });

        //click register.
        viewRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    startActivity(new Intent(getActivity(), RegistrationActivity.class));
                }
            }
        });

        //click Contact Us.
        viewFreeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {

                    Service freeMessageService = APIClient.getUserService();
                    Call<BaseResponse<AutoReplyMessageBean>> messageRequest = freeMessageService.getAutoMessageReply();
                    showLoading();

                    messageRequest.enqueue(new Callback<BaseResponse<AutoReplyMessageBean>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<AutoReplyMessageBean>> call, Response<BaseResponse<AutoReplyMessageBean>> response) {
                            if (response.isSuccessful()) {
                                BaseResponse baseResponse = response.body();

                                if (baseResponse != null) {
                                    if (baseResponse.getStatus().equals(SUCCESS)) {
                                        AutoReplyMessageBean messageReply = (AutoReplyMessageBean)baseResponse.getData();
                                        String message = messageReply.getMessage();

                                        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(getActivity());
                                        PreferencesManager.addEntryToPreferences(preferences, AUTO_MESSAGE_REPLY,message);

                                        setFreMessageRoom();

                                    } else {
                                        closeLoading();
                                        showNetworkErrorDialog(getActivity(), getNetErrMsg());
                                    }
                                } else {
                                    closeLoading();
                                }
                            } else {
                                closeLoading();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<AutoReplyMessageBean>> call, Throwable t) {
                            closeLoading();
                        }
                    });

                }
            }
        });

        //click Loan Calculator.
        viewCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    //startActivity(new Intent(getContext(), FAQActivity.class));
                    replaceFragment(new NavLoanCalculationFragment());
                }
            }
        });

        //click Agent News.
        viewGoodNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    //startActivity(new Intent(getContext(), FAQActivity.class));
                    replaceFragment(new EventNewsTabFragment());
                }
            }
        });

        //click Find Us.
        viewFindUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    //startActivity(new Intent(getContext(), FAQActivity.class));

                    int permission = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION);

                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        makeLocationRequest();

                    } else {
                        openGoogleAPIClient();
                       // replaceFragment(new NavFindNearOutletFragment());
                    }

                }
            }
        });

        //click our service.
        viewOurService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getContext())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    //startActivity(new Intent(getContext(), AboutUsInfoActivity.class));
                    replaceFragment(new NavFAQFragment());
                }
            }
        });

        //click Facebook.
        viewFacebook
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    startActivity(getOpenFacebookIntent(getContext()));
                }
            }
        });

        //click FAQ.
        viewAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    replaceFragment(new PromotionTabFragment());
                }
            }
        });

        //click How To Use.
        viewHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(getActivity())) {
                    showNetworkErrorDialog(getContext(),getNetErrMsg());
                } else {
                    startActivity(new Intent(getContext(), VideoPlayActivity.class));
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
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Please choose one."));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        hideKeyboard(getActivity());

//        setFreMessageRoom();

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        changeLabel(PreferencesManager.getCurrentLanguage(getContext()));

    }

    private void setFreMessageRoom() {

        if (!CommonUtils.isNetworkAvailable(getContext())) {
            showNetworkErrorDialog(getActivity(), getNetErrMsg());

        } else {
            final FreeMessageUserReqBean freeMessageUserReqBean = new FreeMessageUserReqBean();
            freeMessageUserReqBean.setPhoneNo(PreferencesManager.getInstallPhoneNo(getContext()));

            Service freeMessageService = APIClient.getUserService();
            Call<BaseResponse<FreeMessageUserResBean>> req = freeMessageService.doFreeMessageRoomSync(freeMessageUserReqBean);
            showLoading();

            req.enqueue(new Callback<BaseResponse<FreeMessageUserResBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<FreeMessageUserResBean>> call, Response<BaseResponse<FreeMessageUserResBean>> response) {

                    if (response.isSuccessful()) {
                        BaseResponse baseResponse = response.body();

                        if (baseResponse != null) {
                            if (baseResponse.getStatus().equals(SUCCESS)) {
                                try {
                                    Log.e("Free chat create", SUCCESS);
                                    closeLoading();
                                    FreeMessageUserResBean freeMessageUserResBean = (FreeMessageUserResBean) baseResponse.getData();
                                    PreferencesManager.setCurrentFreeChatId(getContext(),freeMessageUserResBean.getFreeCustomerInfoId());
                                    Log.e("Free chat create", freeMessageUserResBean.getFreeCustomerInfoId());
                                    replaceFragment(new NavFreeChatFragment());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    closeLoading();
                                }

                            } else {
                                closeLoading();
                                showNetworkErrorDialog(getActivity(), getNetErrMsg());
                            }
                        } else {
                            closeLoading();
                        }
                    } else {
                        closeLoading();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<FreeMessageUserResBean>> call, Throwable t) {
                    closeLoading();
                }
            });
        }

    }


    private void closeLoading() {
        layoutLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        Glide.with(getActivity()).load(R.drawable.loading).into(imgLoading);
        layoutLoading.setVisibility(View.VISIBLE);
    }

    public final void makeLocationRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
        Log.e("oulet","request permission location");
    }

    private void showLoginPopup(){
        try {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.login);

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_new_main_drawer, fragment, "TAG");
        //transaction.addToBackStack("NewMainPageFragment");
        transaction.commit();
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getContext());
    }

    private static Intent getOpenFacebookIntent(Context context) {

        String url = "https://m.facebook.com/AEON-Microfinance-Myanmar-Co-Ltd-374820516619280/";

        /*try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/374820516619280"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/AEON-Microfinance-Myanmar-Co-Ltd-374820516619280/"));
        }*/

        Uri uri;
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);

            //if (PackageInfo. != null) {
                //uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                uri = Uri.parse("fb://page/374820516619280");
            //}
            // http://stackoverflow.com/a/24547437/1048340

        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        } catch (ActivityNotFoundException e) {
            uri = Uri.parse(url);
        }

        return new Intent(Intent.ACTION_VIEW, uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_myFlag:
                //this.languageFlag = item;
                Log.e("update flag", item.getTitle().toString());
                if(item.getTitle().equals(LANG_MM)){
                    //item.setIcon(R.drawable.en_flag2);
                    //item.setTitle(LANG_EN);
                    changeLabel(LANG_MM);

                } else if(item.getTitle().equals(LANG_EN)){
                    //item.setIcon(R.drawable.mm_flag);
                    //item.setTitle(LANG_MM);
                    changeLabel(LANG_EN);
                }
                changeLabel(LANG_MM);
                return true;

            case R.id.action_engFlag:
                changeLabel(LANG_EN);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLabel(String language){
        textLogin.setText(CommonUtils.getLocaleString(new Locale(language), R.string.home_login_button, getContext()));
        textRegisterNow.setText(CommonUtils.getLocaleString(new Locale(language), R.string.home_register_button, getContext()));

        textFreeChat.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_freechat_title, getContext()));
        textCalculator.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_calculator_title, getContext()));
        textGoodNews.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_news_title, getContext()));
        textFindUs.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_findus_title, getContext()));

        textOurService.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_ourservice_title, getContext()));
        textFacebook.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_facebook_title, getContext()));
        textAnnouncement.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_announce_title, getContext()));

        textHowToUse.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_video_title, getContext()));
        textShare.setText(CommonUtils.getLocaleString(new Locale(language), R.string.main_share_title, getContext()));

        //textFAQ.setText(CommonUtils.getLocaleString(new Locale(language), R.string.home_faq_button, getApplicationContext()));
        textVersionLbl.setText(CommonUtils.getLocaleString(new Locale(language), R.string.home_version_label, getContext()));

        //textAboutUs.setText(CommonUtils.getLocaleString(new Locale(language), R.string.home_aboutus_button, getApplicationContext()));

        PreferencesManager.setCurrentLanguage(getActivity(), language);
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {

    }

    @Override
    public void onAccessRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGoogleAPIClient();
                    //replaceFragment(new NavFindNearOutletFragment());

                } else {
                    // Permission Denied
                    showWarningDialog(getActivity(),"Please access to know the outlet information.");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static void showWarningDialog(final Activity mContext, String message){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.warning_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        messageBody.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                ActivityCompat.requestPermissions(mContext,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        LOCATION_REQUEST_CODE);
            }
        });
        dialog.show();
    }

    private void openGoogleAPIClient(){
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
                            replaceFragment(new NavFindNearOutletFragment());
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


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Log.e("Google api", resultCode+"");
                replaceFragment(new NavFindNearOutletFragment());

            } if (resultCode == Activity.RESULT_CANCELED) {
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
}
