package mm.com.aeon.vcsaeon.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;

public class LanguageSelectActivity extends BaseActivity {

    ImageView btnLangMm;
    ImageView btnLangEn;

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lang_selection);

        toolbar = findViewById(R.id.my_toolbar_lang);
        toolbar.setTitle(getString(R.string.def_tool_bar_title));
        toolbar.setTitleTextColor(getColor(R.color.white));

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        btnLangEn = findViewById(R.id.btn_lang_en);
        btnLangMm = findViewById(R.id.btn_lang_mm);

        btnLangEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setCurrentLanguage(getApplicationContext(),LANG_EN);
                startActivity(new Intent(LanguageSelectActivity.this, TermsAndConditionAgreementsActivity.class));
                finish();
            }
        });


        btnLangMm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesManager.setCurrentLanguage(getApplicationContext(),LANG_MM);
                startActivity(new Intent(LanguageSelectActivity.this, TermsAndConditionAgreementsActivity.class));
                finish();
            }
        });
    }
}
