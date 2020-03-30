package mm.com.aeon.vcsaeon;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import mm.com.aeon.vcsaeon.common_utils.AppUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class VCSApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences preferences = PreferencesManager.getApplicationPreference(this);
        String lang = PreferencesManager.getStringEntryFromPreferences(preferences,PARAM_LANG);

        if(lang.equals(LANG_MM)){
            AppUtils.setLocale(new Locale(LANG_MM));
            AppUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
        } else {
            AppUtils.setLocale(new Locale(LANG_EN));
            AppUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AppUtils.updateConfig(this, newConfig);
    }
}
