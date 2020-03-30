package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Locale;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class NavApplyAeonServiceFragment extends BaseFragment {

    View view;

    ViewPager viewPager;
    Button smallLoan;
    Button personalLoan;
    SharedPreferences validationPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.apply_aeon_service_fragment, container, false);

        validationPreferences = PreferencesManager.getCurrentUserPreferences(getActivity());

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        smallLoan = view.findViewById(R.id.btn_small_loan);
        personalLoan = view.findViewById(R.id.btn_personal_loan);

        smallLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApplicationRegisterSaveReqBean saveReqBean = new ApplicationRegisterSaveReqBean();
                String saveData = new Gson().toJson(saveReqBean);
                PreferencesManager.addEntryToPreferences(validationPreferences,"RegisterSaveData",saveData);
                PreferencesManager.clearCurrentUserPreferences(validationPreferences,"ApplicationData");
                PreferencesManager.clearCurrentUserPreferences(validationPreferences,"OccupationData");
                PreferencesManager.clearCurrentUserPreferences(validationPreferences,"EmergencyData");
                PreferencesManager.clearCurrentUserPreferences(validationPreferences,"GuarantorData");
                PreferencesManager.clearCurrentUserPreferences(validationPreferences,"ConfirmationData");
                MainMenuActivityDrawer.submitAppData = true;
                MainMenuActivityDrawer.submitOccuData = true;
                MainMenuActivityDrawer.submitEmerData = true;
                MainMenuActivityDrawer.submitGuraData = true;
                MainMenuActivityDrawer.submitConfirmData = true;
                MainMenuActivityDrawer.appLoadingData = true;
                MainMenuActivityDrawer.occuLoadingData = true;
                MainMenuActivityDrawer.emerLoadingData = true;
                MainMenuActivityDrawer.guarLoadingData = true;
                MainMenuActivityDrawer.appSwipeCheck = true;
                MainMenuActivityDrawer.gotoPage = 0;
                replaceFragment(new PagerRootFragment());
            }
        });

        personalLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something.
                replaceFragment(new DAEnquiryFragment());
            }
        });

        return view;
    }

    public void changeLabel(String language){
        //welcomeText.setText(CommonUtils.getLocaleString(new Locale(language), R.string.menu_welcome, getActivity()));
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.commit();
    }

}
