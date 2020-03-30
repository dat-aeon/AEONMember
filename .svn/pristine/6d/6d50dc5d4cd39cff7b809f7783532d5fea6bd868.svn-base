package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.MemberCardAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class MemberCardFragment extends BaseFragment {

    View view;

    private TabLayout tabLayout;
    public static int memberCardsTabPosition=0;
    private MemberCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.member_card_fragment, container, false);
        //setHasOptionsMenu(true);

        String tab1;
        String tab2;

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        final String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            tab1=CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.membership_tab_card1, getActivity());
            tab2=CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.membership_tab_card2, getActivity());
        } else {
            tab1=CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.membership_tab_card1, getActivity());
            tab2=CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.membership_tab_card2, getActivity());
        }

        tabLayout = view.findViewById(R.id.tabLayout9);
        tabLayout.addTab(tabLayout.newTab().setText(tab1));
        tabLayout.addTab(tabLayout.newTab().setText(tab2));
        tabLayout.setTabTextColors(getActivity().getColor(R.color.white),getActivity().getColor(R.color.white));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = view.findViewById(R.id.viewPager4);
        adapter = new MemberCardAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount(), curLang);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(memberCardsTabPosition);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                memberCardsTabPosition=tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("tab change to", tab.getPosition()+" " + curLang);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_favorite:
                //this.languageFlag = item;
                if(item.getTitle().equals(LANG_MM)){
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeLabel(LANG_MM);

                } else if(item.getTitle().equals(LANG_EN)){
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeLabel(LANG_EN);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeLabel(String curLang){
        tabLayout.getTabAt(0).setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.membership_tab_card1, getActivity()));
        tabLayout.getTabAt(1).setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.membership_tab_card2, getActivity()));
        adapter.notifyDataSetChanged();
    }
}

