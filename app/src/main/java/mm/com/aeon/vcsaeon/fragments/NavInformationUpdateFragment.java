package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.adapters.UpdateInformationAdapter;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class NavInformationUpdateFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    private TabLayout tabLayout;

    UserInformationFormBean userInformationFormBean;

    public static int infoUpdateTabPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.information_update_fragment, container, false);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        String tab1;
        String tab2;

        // close button listener
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        TextView menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarLevelInfo.setText(R.string.menu_level_two);

        menuBackBtn.setAnimation(UiUtils.animSlideToRight(getActivity()));
        menuBackBtn.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);

        tab1 = CommonUtils.getLocaleString(new Locale(curLang), R.string.sec_ans_update, getActivity());
        tab2 = CommonUtils.getLocaleString(new Locale(curLang), R.string.infoupdate_tab2_title, getActivity());

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(tab1));
        tabLayout.addTab(tabLayout.newTab().setText(tab2));
        tabLayout.setTabTextColors(getActivity().getColor(R.color.white), getActivity().getColor(R.color.white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.viewPager);
        final UpdateInformationAdapter adapter = new
                UpdateInformationAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(infoUpdateTabPosition);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                infoUpdateTabPosition = tab.getPosition();
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

    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
        transaction.commit();
    }

    @Override
    public void changeLanguageTitle(String lang) {
        //PreferencesManager.setCurrentLanguage(getActivity(),lang);
        replaceFragment(new NavInformationUpdateFragment(), R.id.content_main_drawer);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
    }
}
