package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class NavMembershipFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    Button btnUpgrade;
    UserInformationFormBean userInformationFormBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.membership_fragment, container, false);
        setHasOptionsMenu(true);

        // close button listener
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);

        // show back button on toolbar
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);

        btnUpgrade = view.findViewById(R.id.btn_upgrade);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear previous data.
                clearVerificationForm();
                replaceFragment(new VerificationMemberInfoFragment(),R.id.content_main_drawer);
            }
        });

        return view;

    }

    public void replaceFragment(Fragment fragment, int containerView){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerView, fragment, "TAG");
        transaction.addToBackStack("nav_membership");
        transaction.commit();
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

    public void changeLabel(String language){
        btnUpgrade.setText(CommonUtils.getLocaleString(new Locale(language), R.string.customertype_oldcustomer_button, getActivity()));
        PreferencesManager.setCurrentLanguage(getActivity(), language);
    }

    private void clearVerificationForm(){
        VerificationMemberInfoFragment.tempAgreementNoVal=null;
        VerificationMemberInfoFragment.tempDateOfBirth=null;
        VerificationMemberInfoFragment.tempNrcCode=null;
        VerificationMemberInfoFragment.tempDivCode=0;
        VerificationMemberInfoFragment.tempNrcType=0;
        VerificationMemberInfoFragment.tempNrcCode=null;
        VerificationMemberInfoFragment.tempTwspCode=null;
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
    }
}
