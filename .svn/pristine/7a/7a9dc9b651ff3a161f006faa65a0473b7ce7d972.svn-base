package mm.com.aeon.vcsaeon.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_TERM_ACCEPTED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TERM_ACCEPTED;

public class LaunchActivity extends Activity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);

        preferences = PreferencesManager.getApplicationPreference(getApplicationContext());
        String termsAccepted = PreferencesManager.getStringEntryFromPreferences(preferences, PARAM_TERM_ACCEPTED);

        String installPhoneNo = PreferencesManager.getInstallPhoneNo(getApplicationContext());
        Log.e("term", termsAccepted);

        if(termsAccepted.equals(TERM_ACCEPTED)){

             if(installPhoneNo == null || installPhoneNo == BLANK) {
                Intent intent = new Intent(this, PhoneRequestActivity.class);
                finish();
                startActivity(intent);

            } else {
                 Intent intent = new Intent(this, MainActivity.class);
                 finish();
                 startActivity(intent);
             }

        } else {
            Intent intent = new Intent(this, TermsAndConditionAgreementsActivity.class);
            finish();
            startActivity(intent);

        }
    }
}
