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

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.FAQAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class NavFAQFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    private TabLayout tabLayout;
    Toolbar toolbar;

    public static int faqTabPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faq_and_terms_condition_fragment, container, false);

        String tab1;
        String tab2;
        String tab3;

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);

        if (PreferencesManager.getMainNavFlag(getContext())) {
            ((MainActivity) getActivity()).setLanguageListener(this);
            toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);
        } else {
            ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
            toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        }
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setAnimation(UiUtils.animSlideToRight(getActivity()));
        menuBackBtn.setVisibility(View.VISIBLE);

        //changeLabel(LANG_MM);
        tab1 = CommonUtils.getLocaleString(new Locale(curLang), R.string.faq_tab1_title, getActivity());
        tab2 = CommonUtils.getLocaleString(new Locale(curLang), R.string.faq_tab2_title, getActivity());
        tab3 = CommonUtils.getLocaleString(new Locale(curLang), R.string.aboutus_title, getActivity());

        tabLayout = view.findViewById(R.id.tabLayout4);
        tabLayout.addTab(tabLayout.newTab().setText(tab1));
        tabLayout.addTab(tabLayout.newTab().setText(tab2));
        tabLayout.addTab(tabLayout.newTab().setText(tab3));
        tabLayout.setTabTextColors(getActivity().getColor(R.color.white), getActivity().getColor(R.color.white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.viewPager4);
        final FAQAdapter adapter = new
                FAQAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(faqTabPosition);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                faqTabPosition = tab.getPosition();
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
        //changeLanguageTitle(lang);
        PreferencesManager.setCurrentLanguage(getContext(), lang);
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new NavFAQFragment(), R.id.content_new_main_drawer);
        } else {
            replaceFragment(new NavFAQFragment(), R.id.content_main_drawer);
        }
    }

    @Override
    public void clickMenuBarBackBtn() {
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new NewMainPageFragment(), R.id.content_new_main_drawer);
        } else {
            replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
        }
    }
}
