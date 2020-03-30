package mm.com.aeon.vcsaeon.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CompanyInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class AboutUsInfoActivity extends BaseActivity {

    TextView textAbout;
    TextView textPhone;
    TextView textFacebook;
    TextView textWeb;
    TextView textAddress;
    TextView txtTitle;
    TextView txtService;
    View serviceNotFoundView;
    Button btnCall;
    Toolbar toolbar;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;

    CompanyInfoResBean companyInfoResBean;
    private static final int RECORD_REQUEST_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);
        menuBarName = toolbar.findViewById(R.id.menu_bar_name);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText("Lv.1 : Application User");
        String installPhone = PreferencesManager.getInstallPhoneNo(getApplicationContext()).trim();
        menuBarPhoneNo.setText(installPhone);
        menuBarName.setVisibility(View.GONE);

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        textAbout = findViewById(R.id.text_about);
        textPhone = findViewById(R.id.text_phone);
        textFacebook = findViewById(R.id.aeon_fb_link);
        textWeb = findViewById(R.id.aeon_web_link);
        textAddress = findViewById(R.id.text_address);

        txtTitle = findViewById(R.id.about_title);
        btnCall = findViewById(R.id.btn_about_call);

        serviceNotFoundView = findViewById(R.id.service_unavailable_about_us);
        serviceNotFoundView.setVisibility(View.GONE);

        txtService = serviceNotFoundView.findViewById(R.id.service_unavailable_label);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());

        companyInfoResBean = new CompanyInfoResBean();

        Service getAboutUsInfoService = APIClient.getUserService();
        Call<BaseResponse<CompanyInfoResBean>> req = getAboutUsInfoService.getCompanyInfo();

        AboutUsInfoActivity.this.setTheme(R.style.MessageDialogTheme);
        final ProgressDialog aboutUsInfoDialog = new ProgressDialog(AboutUsInfoActivity.this);
        aboutUsInfoDialog.setMessage(getString(R.string.progress_loading));
        aboutUsInfoDialog.setCancelable(false);
        aboutUsInfoDialog.show();

        req.enqueue(new Callback<BaseResponse<CompanyInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<CompanyInfoResBean>> call, Response<BaseResponse<CompanyInfoResBean>> response) {
                if(response.isSuccessful()){

                    final BaseResponse baseResponse = response.body();

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        companyInfoResBean = (CompanyInfoResBean) baseResponse.getData();

                        btnCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int permission = ContextCompat.checkSelfPermission(getApplicationContext(),
                                        Manifest.permission.CALL_PHONE);

                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    makeCallRequest();
                                } else {
                                    String hotlinePhoneNo = companyInfoResBean.getHotlinePhone();
                                    if(hotlinePhoneNo==null || hotlinePhoneNo.equals(BLANK)){
                                        //Toast.makeText(getApplicationContext(),getString(R.string.message_call_not_available), Toast.LENGTH_SHORT).show();
                                        displayMessage(getApplicationContext(),getString(R.string.message_call_not_available));
                                    } else {
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX+hotlinePhoneNo));
                                        startActivity(callIntent);
                                    }
                                }
                            }
                        });

                        changeLabel(curLang);
                        closeDialog(aboutUsInfoDialog);

                    } else {

                        closeDialog(aboutUsInfoDialog);
                        //do some "FAILED" conditions.
                    }

                } else {

                    // display service_unavailable layout if not success.
                    closeDialog(aboutUsInfoDialog);
                    serviceNotFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CompanyInfoResBean>> call, Throwable t) {
                closeDialog(aboutUsInfoDialog);
                serviceNotFoundView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.toolbar_main_menu, menu);

        /*String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
            menu.getItem(0).setTitle(LANG_EN);
            changeLabel(LANG_MM);
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
            menu.getItem(0).setTitle(LANG_MM);
            changeLabel(LANG_EN);
        }*/
//        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
//        menu.getItem(0).setTitle(LANG_MM);
//        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
//        menu.getItem(1).setTitle(LANG_EN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_myFlag) {
           /* if(item.getTitle().equals(LANG_MM)){
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
            } else if(item.getTitle().equals(LANG_EN)){
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
            }*/
            changeLabel(LANG_MM);
            addValueToPreference(LANG_MM);
            return true;
        }
        if (id == R.id.action_engFlag) {
            changeLabel(LANG_EN);
            addValueToPreference(LANG_EN);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addValueToPreference(String lang){
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    public void changeLabel(String language){

        btnCall.setText(CommonUtils.getLocaleString(new Locale(language), R.string.aboutus_all_now_button, getApplicationContext()));
        toolbar.setTitle(CommonUtils.getLocaleString(new Locale(language), R.string.aboutus_title, getApplicationContext()));
        txtService.setText(CommonUtils.getLocaleString(new Locale(language), R.string.service_unavailable, getApplicationContext()));

        textPhone.setText(companyInfoResBean.getHotlinePhone());
        textFacebook.setText(companyInfoResBean.getSocialMediaAddress());
        textWeb.setText(companyInfoResBean.getWebAddress());

        if(language.equals(LANG_EN)){
            textAbout.setText(companyInfoResBean.getAboutCompanyEn());
            textAddress.setText(companyInfoResBean.getAddressEn());
        } else {
            textAbout.setText(companyInfoResBean.getAboutCompanyMm());
            textAddress.setText(companyInfoResBean.getAddressMm());
        }
    }

    protected void makeCallRequest() {
        ActivityCompat.requestPermissions(AboutUsInfoActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    //Log.i(TAG, "Permission has been denied by user");
                } else {
                    String hotlinePhoneNo = companyInfoResBean.getHotlinePhone();
                    if(hotlinePhoneNo==null || hotlinePhoneNo.equals(BLANK)){
                        showSnackBarMessage(getString(R.string.message_call_not_available));
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX+hotlinePhoneNo));
                        startActivity(callIntent);
                    }
                }
                return;
            }
        }
    }
}