package mm.com.aeon.vcsaeon.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.AccessPermissionResultDelegate;
import mm.com.aeon.vcsaeon.fragments.NavFreeChatFragment;
import mm.com.aeon.vcsaeon.fragments.NewMainPageFragment;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CAMERA_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOCATION_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STORAGE_REQUEST_CODE;

public class MainActivity extends BaseActivity {

    Toolbar toolbar;
    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackBtn;
    LanguageChangeListener delegate;
    AccessPermissionResultDelegate locationDelegate;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        PreferencesManager.clearAccessToken(this);
        PreferencesManager.clearRefreshToken(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.GONE);

        menuBarName = toolbar.findViewById(R.id.menu_bar_name);
        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText(R.string.menu_level_one);

        //get install phone number from fragment.
        String installPhone = PreferencesManager.getInstallPhoneNo(getApplicationContext()).trim();

        menuBarPhoneNo.setText(installPhone);
        menuBarName.setVisibility(View.GONE);

        myTitleBtn = toolbar.findViewById(R.id.my_flag);
        engTitleBtn = toolbar.findViewById(R.id.en_flag);

        myTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.changeLanguageTitle(myTitleBtn.getTag().toString());
            }
        });

        engTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.changeLanguageTitle(engTitleBtn.getTag().toString());
            }
        });

        menuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.clickMenuBarBackBtn();
            }
        });

        setUpDeviceId();

        /*Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));*/
        replaceFragment(new NewMainPageFragment());

    }

    public void setLanguageListener(LanguageChangeListener delegate) {
        this.delegate = delegate;
    }

    public void setLocationDelegate(AccessPermissionResultDelegate locationDelegate) {
        this.locationDelegate = locationDelegate;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_new_main_drawer, fragment, "TAG");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //get access to local-storage permission.
        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.content_new_main_drawer);
        if (frg instanceof NavFreeChatFragment) {
            if (requestCode == STORAGE_REQUEST_CODE) {
                frg.onRequestPermissionsResult(requestCode, permissions, grantResults);
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                frg.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

        //get access to location permission
        locationDelegate.onAccessRequestPermissionResult(requestCode, permissions, grantResults);
    }

    protected void makeLocationRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        showSnackBarMessage(getString(R.string.message_back_click));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            if (item.getTitle().equals(LANG_MM)) {
                addValueToPreference(LANG_MM);
            } else if (item.getTitle().equals(LANG_EN)) {
                addValueToPreference(LANG_EN);
            }
            return false;
        }
        if (id == R.id.action_engFlag) {
            if (item.getTitle().equals(LANG_MM)) {
                addValueToPreference(LANG_MM);
            } else if (item.getTitle().equals(LANG_EN)) {
                addValueToPreference(LANG_EN);
            }
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addValueToPreference(String lang) {
        PreferencesManager.setCurrentLanguage(getApplicationContext(), lang);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.content_new_main_drawer);
        if (frg instanceof NewMainPageFragment) {
            if (requestCode == 1000) {
                frg.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    void setUpDeviceId() {
        String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        PreferencesManager.setLoginDeviceId(getApplicationContext(), deviceId);
    }
}