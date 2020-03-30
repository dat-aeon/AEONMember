package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.adapters.MemberCardAdapter;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class ComingSoonFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    TextView textComingSoon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.coming_soon_fragment, container, false);
        //setHasOptionsMenu(true);
        textComingSoon = view.findViewById(R.id.text_coming_soon);

        // set listener
        ((MainMenuActivityDrawer)getActivity()).setLanguageListener(this);

        // change level text
        Toolbar toolbar = ((MainMenuActivityDrawer)getActivity()).findViewById(R.id.toolbar_home);
        TextView menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarLevelInfo.setText(R.string.menu_level_two);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);
        return view;
    }

    private void changeLabel(String curLang){
        textComingSoon.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.membership_tab_card1, getActivity()));
        PreferencesManager.setCurrentLanguage(getContext(),curLang);
    }

    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
        transaction.commit();
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment(),R.id.content_main_drawer);
    }
}

