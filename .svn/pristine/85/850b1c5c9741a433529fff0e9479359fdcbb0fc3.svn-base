package mm.com.aeon.vcsaeon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Locale;

import androidx.core.content.ContextCompat;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_TERM_ACCEPTED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TERM_ACCEPTED;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.replacePhoneNo;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.setAeonPhoneNo;

public class TermsAndConditionAgreementsActivity extends BaseActivity {

    Button btnAgree;
    CheckBox checkBoxAccept;
    Toolbar toolbar;
    SharedPreferences preferences;

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

    private final String PHONE_NO = "09969712111";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_condition_agreement);

        toolbar = findViewById(R.id.my_toolbar_agreements);
        toolbar.setTitle(R.string.def_tool_bar_title);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        btnAgree = findViewById(R.id.btn_agreement);
        checkBoxAccept = findViewById(R.id.checkbox_accept);

        textTitle = findViewById(R.id.faq_terms_cond_title);
        textText1 = findViewById(R.id.faq_terms_cond_text1);
        textText2 = findViewById(R.id.faq_terms_cond_text2);
        textText3 = findViewById(R.id.faq_terms_cond_text3);
        textText4 = findViewById(R.id.faq_terms_cond_text4);
        textText5 = findViewById(R.id.faq_terms_cond_text5);
        textText6 = findViewById(R.id.faq_terms_cond_text6);
        textText7 = findViewById(R.id.faq_terms_cond_text7);
        textText8 = findViewById(R.id.faq_terms_cond_text8);
        textText9 = findViewById(R.id.faq_terms_cond_text9);
        textText10 = findViewById(R.id.faq_terms_cond_text10);
        textText11 = findViewById(R.id.faq_terms_cond_text11);

        PreferencesManager.setCurrentLanguage(getApplicationContext(),LANG_MM);
        changeLabel(LANG_MM);

        checkBoxAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    btnAgree.setBackground(getDrawable(R.drawable.button_border));
                    btnAgree.setEnabled(true);
                } else {
                    btnAgree.setBackground(getDrawable(R.drawable.disable_button_border));
                    btnAgree.setEnabled(false);
                }
            }
        });

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermsAndConditionAgreementsActivity.this, PhoneRequestActivity.class));
                //finish();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        preferences = PreferencesManager.getApplicationPreference(getApplicationContext());
        String termsAccepted = PreferencesManager.getStringEntryFromPreferences(preferences, PARAM_TERM_ACCEPTED);

        if(termsAccepted.equals(TERM_ACCEPTED)) {
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
        menu.getItem(0).setTitle(LANG_MM);

        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
        menu.getItem(1).setTitle(LANG_EN);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_myFlag) {
            changeLabel(LANG_MM);
            addValueToPreference(LANG_MM);
        } else if (id == R.id.action_engFlag) {
            changeLabel(LANG_EN);
            addValueToPreference(LANG_EN);
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeLabel(String language) {
        btnAgree.setText(CommonUtils.getLocaleString(new Locale(language), R.string.terms_agree, getApplicationContext()));
        checkBoxAccept.setText(CommonUtils.getLocaleString(new Locale(language), R.string.terms_accept, getApplicationContext()));
        textTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.faq_terms_cond_title, getApplicationContext()));
        textText1.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_001, getApplicationContext()));
        textText2.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_002, getApplicationContext()));
        textText3.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_003, getApplicationContext()));
        textText4.setText(setAeonPhoneNo(CommonUtils.getLocaleString(new Locale(language), R.string.tac_004, getApplicationContext()), PHONE_NO));
        textText5.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_005, getApplicationContext()));
        textText6.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_006, getApplicationContext()));
        textText7.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_007, getApplicationContext()));
        textText8.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_008, getApplicationContext()));
        textText9.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_009, getApplicationContext()));
        textText10.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_010, getApplicationContext()));
        textText11.setText(CommonUtils.getLocaleString(new Locale(language), R.string.tac_011, getApplicationContext()));
    }

    public void addValueToPreference(String lang) {
        PreferencesManager.setCurrentLanguage(getApplicationContext(), lang);
    }

}
