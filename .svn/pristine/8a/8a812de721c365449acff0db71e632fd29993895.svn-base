package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.modifyPhoneNo;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.setAeonPhoneNo;

public class TermsAndConditionTabFragment extends BaseFragment {

    View view;

    TextView textTitle;
    TextView textText1;
    TextView textText2;
    TextView textText3;
    TextView textText4;
    TextView textText5;
    TextView textText6;
    TextView textText7;
    TextView textText8;
    TextView textText9;
    TextView textText10;
    TextView textText11;

    private String hotlinePhone;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.terms_conditions_tab, container, false);

        textTitle = view.findViewById(R.id.faq_terms_cond_title);
        textText1 = view.findViewById(R.id.faq_terms_cond_text1);
        textText2 = view.findViewById(R.id.faq_terms_cond_text2);
        textText3 = view.findViewById(R.id.faq_terms_cond_text3);
        textText4 = view.findViewById(R.id.faq_terms_cond_text4);
        textText5 = view.findViewById(R.id.faq_terms_cond_text5);
        textText6 = view.findViewById(R.id.faq_terms_cond_text6);
        textText7 = view.findViewById(R.id.faq_terms_cond_text7);
        textText8 = view.findViewById(R.id.faq_terms_cond_text8);
        textText9 = view.findViewById(R.id.faq_terms_cond_text9);
        textText10 = view.findViewById(R.id.faq_terms_cond_text10);
        textText11 = view.findViewById(R.id.faq_terms_cond_text11);

        hotlinePhone = PreferencesManager.getHotlinePhone(getActivity());

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }
        return view;
    }

    public void changeLabel(String language){
        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.faq_terms_cond_title, getActivity()));
        textText1.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_001, getActivity()));
        textText2.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_002, getActivity()));
        textText3.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_003, getActivity()));
        textText4.setText(setAeonPhoneNo(CommonUtils.getLocaleString(new Locale(language), R.string.tac_004, getActivity()), modifyPhoneNo(hotlinePhone)));
        textText5.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_005, getActivity()));
        textText6.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_006, getActivity()));
        textText7.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_007, getActivity()));
        textText8.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_008, getActivity()));
        textText9.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_009, getActivity()));
        textText10.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_010, getActivity()));
        textText11.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_011, getActivity()));
        PreferencesManager.setCurrentLanguage(getActivity(), language);
    }
}







