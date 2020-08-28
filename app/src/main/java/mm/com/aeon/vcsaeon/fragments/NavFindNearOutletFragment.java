package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.FindNearOutletAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.AccessPermissionResultDelegate;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOCATION_REQUEST_CODE;

public class NavFindNearOutletFragment extends BaseFragment implements LanguageChangeListener, AccessPermissionResultDelegate {

    View view;
    TabLayout tabLayout;
    Toolbar toolbar;
    public static int FindNearOutletTabPosition = 0;

    String tab1;
    String tab2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.find_nearest_outlets, container, false);
        setHasOptionsMenu(true);

        // close button listener
        if (PreferencesManager.getMainNavFlag(getContext())) {
            ((MainActivity) getActivity()).setLanguageListener(this);
            ((MainActivity) getActivity()).setLocationDelegate(this);
            toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);

        } else {
            ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
            toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);

        }
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);

        menuBackBtn.setAnimation(UiUtils.animSlideToRight(getActivity()));
        menuBackBtn.setVisibility(View.VISIBLE);

        int permission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeLocationRequest();

        } else {
            showTabFragment();
        }
        return view;
    }

    private void showTabFragment() {

        String curLang = PreferencesManager.getCurrentLanguage(getActivity());
        if (curLang.equals(LANG_MM)) {
            tab1 = CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.near_by_tab1, getActivity());
            tab2 = CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.near_by_tab2, getActivity());
        } else {
            tab1 = CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.near_by_tab1, getActivity());
            tab2 = CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.near_by_tab2, getActivity());
        }

        tabLayout = view.findViewById(R.id.loc_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(tab1));
        tabLayout.addTab(tabLayout.newTab().setText(tab2));
        tabLayout.setTabTextColors(getActivity().getColor(R.color.white), getActivity().getColor(R.color.white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.loc_viewpager);
        final FindNearOutletAdapter adapter = new FindNearOutletAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(FindNearOutletTabPosition);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                FindNearOutletTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void changeLabel(String lang) {
        tabLayout.getTabAt(0).setText(CommonUtils.getLocaleString(new Locale(lang), R.string.near_by_tab1, getActivity()));
        tabLayout.getTabAt(1).setText(CommonUtils.getLocaleString(new Locale(lang), R.string.near_by_tab2, getActivity()));
        Log.e("outlet list", lang);
        PreferencesManager.setCurrentLanguage(getContext(), lang);
    }

    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //get access to location permission
        Log.e("oulet", "location activity result");
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showTabFragment();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Please accept to get outlet information.", Toast.LENGTH_SHORT).show();
                    makeLocationRequest();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void makeLocationRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_CODE);
        Log.e("oulet", "request permission location");
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {

        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new NewMainPageFragment(), R.id.content_new_main_drawer);
        } else {
            replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
        }
    }

    @Override
    public void onAccessRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (PreferencesManager.getMainNavFlag(getActivity())) {
                        replaceFragment(new NewMainPageFragment(), R.id.content_new_main_drawer);
                    } else {
                        replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Please accept to get outlet information.", Toast.LENGTH_SHORT).show();
                    makeLocationRequest();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
