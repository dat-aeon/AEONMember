package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.ContactUsAdapater;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class NavContactUsFragment extends BaseFragment {

    View view;
    private TabLayout tabLayout;
    static int contactUsTabPosition=0;
    public static ContactUsAdapater adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_us_fragment, container, false);

        String tab1;
        String tab2;

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            //changeLabel(LANG_MM);
            tab1= CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.contactus_tab1_title, getActivity());
            tab2=CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.contactus_tab2_title, getActivity());
        } else {
            ///changeLabel(LANG_EN);
            tab1=CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.contactus_tab1_title, getActivity());
            tab2=CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.contactus_tab2_title, getActivity());
        }

        tabLayout = view.findViewById(R.id.tabLayout3);
        tabLayout.addTab(tabLayout.newTab().setText(tab1));
        tabLayout.addTab(tabLayout.newTab().setText(tab2));
        tabLayout.setTabTextColors(getActivity().getColor(R.color.white),getActivity().getColor(R.color.white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.viewPager3);
        adapter = new
                ContactUsAdapater(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(contactUsTabPosition);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                contactUsTabPosition=tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }
}
