package mm.com.aeon.vcsaeon.activities;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

public abstract class BaseActivity extends AppCompatActivity {

    protected void showSnackBarMessage(String message) {
        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.pyidaungsu_regular));
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    protected String getLoginDeviceId() {
        return PreferencesManager.getLoginDeviceId(getApplicationContext());
    }
}