package mm.com.aeon.vcsaeon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.PasswordCheckReqBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_CAMERA;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_STORAGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;

public class ProfilePhotoUpdateActivity extends BaseActivity {

    Button btnUpload;
    Toolbar toolbar;

    ImageView myTitleBtn;
    ImageView engTitleBtn;
    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;

    TextView txtTitle;
    TextView txt1;
    TextView txt2;

    String mCurrentPhotoPath;

    private static UserInformationFormBean userInformationFormBean;

    private static final int PHOTO_REQUEST_CODE = 330;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{PERMISSION_CAMERA, PERMISSION_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile_photo_update);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getApplicationContext());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);
        menuBarName = toolbar.findViewById(R.id.menu_bar_name);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText(R.string.menu_level_three);
        menuBarPhoneNo.setText(userInformationFormBean.getPhoneNo());
        menuBarName.setText(userInformationFormBean.getName());

        myTitleBtn = toolbar.findViewById(R.id.my_flag);
        engTitleBtn = toolbar.findViewById(R.id.en_flag);

        myTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(myTitleBtn.getTag().toString());
            }
        });

        engTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLabel(engTitleBtn.getTag().toString());
            }
        });

        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        btnUpload = findViewById(R.id.btn_update);
        txtTitle = findViewById(R.id.update_title);
        txt1 = findViewById(R.id.txt_rule1);
        txt2 = findViewById(R.id.txt_rule2);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(this);
        final String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);
        changeLabel(curLang);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ProfilePhotoUpdateActivity.this);
                dialog.setContentView(R.layout.use_coupon_dialog);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView textTitle = dialog.findViewById(R.id.text_message);
                String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
                textTitle.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.title_conf_pwd, getApplicationContext()));
                Button btnOk = dialog.findViewById(R.id.btn_ok);
                Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                final EditText textPwd = dialog.findViewById(R.id.txt_coupon_pwd);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String password = textPwd.getText().toString();

                        if (password.equals(BLANK)) {
                            displayMessage(getApplicationContext(), getEmptyPwdMsg());
                        } else {

                            PasswordCheckReqBean passwordCheckReqBean = new PasswordCheckReqBean();
                            passwordCheckReqBean.setCustomerId(userInformationFormBean.getCustomerId());
                            passwordCheckReqBean.setPassword(password);
                            String accessToken = PreferencesManager.getAccessToken(getApplicationContext());

                            ProfilePhotoUpdateActivity.this.setTheme(R.style.MessageDialogTheme);
                            final ProgressDialog updCouponDialog = new ProgressDialog(ProfilePhotoUpdateActivity.this);
                            updCouponDialog.setMessage(getString(R.string.progress_confirming));
                            updCouponDialog.setCancelable(false);
                            updCouponDialog.show();

                            Service checkPasswordService = APIClient.getUserService();
                            final Call<BaseResponse> resp = checkPasswordService.checkPassword(accessToken, passwordCheckReqBean);

                            resp.enqueue(new Callback<BaseResponse>() {
                                @Override
                                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                    updCouponDialog.dismiss();
                                    if (response.isSuccessful()) {
                                        BaseResponse baseResponse = response.body();
                                        if (baseResponse.getStatus().equals(SUCCESS)) {
                                            dialog.dismiss();
                                            if (allPermissionsGranted()) {
                                                dispatchTakePictureIntent();
                                            } else {
                                                ActivityCompat.requestPermissions(ProfilePhotoUpdateActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                                            }
                                        } else {
                                            displayMessage(ProfilePhotoUpdateActivity.this, getIncorrectPwdMsg());
                                        }
                                    } else {
                                        displayMessage(ProfilePhotoUpdateActivity.this, getString(R.string.msg_password_check_failed));
                                    }
                                }

                                @Override
                                public void onFailure(Call<BaseResponse> call, Throwable t) {
                                    updCouponDialog.dismiss();
                                    displayMessage(ProfilePhotoUpdateActivity.this, getString(R.string.msg_password_check_failed));
                                }
                            });
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mCurrentPhotoPath = data.getData().toString();
            SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(this);
            PreferencesManager.addEntryToPreferences(preferences, "current_photo_path", mCurrentPhotoPath);
            Intent intent = intentPhotoConfirming(ProfilePhotoUpdateActivity.this);
            startActivity(intent);
        }
    }

    private static Intent intentPhotoConfirming(Context context) {
        Intent intent = new Intent(context, ProfilePhotoUpdateVerifyActivity.class);
        return intent;
    }

    private void dispatchTakePictureIntent() {
        Intent intent = intentPhotoTaking(ProfilePhotoUpdateActivity.this);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    private static Intent intentPhotoTaking(Context context) {
        Intent intent = new Intent(context, CameraxActivity.class);
        return intent;
    }

    public void changeLabel(String language) {
        btnUpload.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_update_button, this));
        txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_announce_label, this));
        txt1.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_notice1_label, this));
        txt2.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_notice5_label, this));

        PreferencesManager.setCurrentLanguage(this, language);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                dispatchTakePictureIntent();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            if (item.getTitle().equals(LANG_MM)) {
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
            } else if (item.getTitle().equals(LANG_EN)) {
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addValueToPreference(String lang) {
        PreferencesManager.setCurrentLanguage(getApplicationContext(), lang);
    }

    private String getIncorrectPwdMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.invalid_coupon_pwd, getApplicationContext());
    }

    private String getEmptyPwdMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        return CommonUtils.getLocaleString(new Locale(language), R.string.no_coupon_pwd, getApplicationContext());
    }
}
