package mm.com.aeon.vcsaeon.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.UpdateProfilePhotoReqBean;
import mm.com.aeon.vcsaeon.beans.UpdateProfilePhotoResBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_CAMERA;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_STORAGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.PROFILE_URL;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class ProfilePhotoUpdateVerifyActivity extends BaseActivity {

    ImageView previewImg;
    TextView imgText;
    Button btnConfirm;
    Button btnRetry;
    Toolbar toolbar;

    String mCurrentPhotoPath;
    String customerNo;

    File editedImageFile;
    SharedPreferences preferences;

    ImageView myTitleBtn;
    ImageView engTitleBtn;
    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;

    UserInformationFormBean userInformationFormBean;

    private static final int PHOTO_REQUEST_CODE = 330;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{PERMISSION_CAMERA,PERMISSION_STORAGE};

    private ProgressDialog upgradeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile_photo_update_verify);

        preferences = PreferencesManager.getCurrentUserPreferences(this);
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(this);
        customerNo = PreferencesManager.getStringEntryFromPreferences(preferences,"customer_no");
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
        menuBackBtn.setVisibility(View.GONE);
        menuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        previewImg = findViewById(R.id.verify_photo);
        imgText = findViewById(R.id.verify_text);
        btnConfirm = findViewById(R.id.verify_confirm);
        btnRetry = findViewById(R.id.verify_retry);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(this);
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,"lang");

        if(curLang.equals(LANG_MM)){
            changeLabel(curLang);
        } else {
            changeLabel(curLang);
        }

        mCurrentPhotoPath=PreferencesManager.getStringEntryFromPreferences(preferences,"current_photo_path");
        editedImageFile = new File(mCurrentPhotoPath);

        if(editedImageFile.exists()){
            setImageConfirm(editedImageFile);
        }

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allPermissionsGranted()){
                    dispatchTakePictureIntent();
                } else{
                    ActivityCompat.requestPermissions(ProfilePhotoUpdateVerifyActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(editedImageFile.exists()){

                    if (!CommonUtils.isNetworkAvailable(ProfilePhotoUpdateVerifyActivity.this)) {
                        showNetworkErrorDialog(ProfilePhotoUpdateVerifyActivity.this,getNetErrMsg());
                    } else {

                        final UpdateProfilePhotoReqBean updateProfilePhotoReqBean = new UpdateProfilePhotoReqBean();
                        updateProfilePhotoReqBean.setCustomerId(String.valueOf(userInformationFormBean.getCustomerId()));

                        byte[] bytes = CommonUtils.encodedFileToByteArray(editedImageFile);
                        updateProfilePhotoReqBean.setPhotoByte(bytes);

                        new UpdateProfilePictureAsyTask().execute(updateProfilePhotoReqBean);
                    }

                } else {

                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.root_confirm_profile),getString(R.string.img_destroy_snackbar_info), Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(allPermissionsGranted()){
                                        dispatchTakePictureIntent();
                                    } else{
                                        ActivityCompat.requestPermissions(ProfilePhotoUpdateVerifyActivity.this,
                                                REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                                    }
                                }
                            });
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            mCurrentPhotoPath = data.getData().toString();
            editedImageFile = new File(mCurrentPhotoPath);
            if(editedImageFile.exists()){
                setImageConfirm(editedImageFile);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = intentPhotoTaking(ProfilePhotoUpdateVerifyActivity.this);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    private static Intent intentPhotoTaking(Context context){
        Intent intent = new Intent(context, CameraxActivity.class);
        return intent;
    }

    public void changeLabel(String language){
        btnRetry.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_retry, this));
        btnConfirm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_confirm, this));
        imgText.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_title, this));
        addValueToPreference(language);
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(this);
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, this);
    }

    //set captured image to image-view.
    private void setImageConfirm(File imgFile){
        Bitmap bitmap = CommonUtils.encodedFileToBitmap(imgFile);
        previewImg.setImageBitmap(bitmap);
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                dispatchTakePictureIntent();
            } else{
                showSnackBarMessage(getString(R.string.message_permission_deniled));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
            menu.getItem(0).setTitle(LANG_EN);
            changeLabel(LANG_MM);
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
            menu.getItem(0).setTitle(LANG_MM);
            changeLabel(LANG_EN);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {

            if(item.getTitle().equals(LANG_MM)){
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                changeLabel(LANG_MM);
                addValueToPreference(LANG_MM);
            } else if(item.getTitle().equals(LANG_EN)){
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                changeLabel(LANG_EN);
                addValueToPreference(LANG_EN);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addValueToPreference(String lang){
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    private static Intent intentMainMenuDrawer(Context context){
        Intent intent = new Intent(context, MainMenuActivityDrawer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    //Profile Photo Updating AsyTask.
    private class UpdateProfilePictureAsyTask extends AsyncTask<UpdateProfilePhotoReqBean, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProfilePhotoUpdateVerifyActivity.this.setTheme(R.style.MessageDialogTheme);
            upgradeProfile = new ProgressDialog(ProfilePhotoUpdateVerifyActivity.this);
            upgradeProfile.setMessage("Updating Photo ...");
            upgradeProfile.setCancelable(false);
            upgradeProfile.show();
        }

        @Override
        protected String doInBackground(UpdateProfilePhotoReqBean... updateProfilePhotoReqBeans) {

            UpdateProfilePhotoReqBean updateProfilePhotoReqBean = updateProfilePhotoReqBeans[0];

            Service profilePhotoUpdateService = APIClient.getAuthUserService();
            Call<BaseResponse<UpdateProfilePhotoResBean>> req = profilePhotoUpdateService
                    .getUpdateProfilePhotoInfo(PreferencesManager.getAccessToken(getApplicationContext()),updateProfilePhotoReqBean);

            req.enqueue(new Callback<BaseResponse<UpdateProfilePhotoResBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<UpdateProfilePhotoResBean>> call, Response<BaseResponse<UpdateProfilePhotoResBean>> response) {
                    if (response.isSuccessful()) {

                        BaseResponse baseResponse = response.body();

                        if (baseResponse.getStatus().equals(SUCCESS)) {

                            closeDialog(upgradeProfile);
                            final UpdateProfilePhotoResBean updateProfilePhotoResBean = (UpdateProfilePhotoResBean) baseResponse.getData();

                            //delete captured file.
                            File delFile = new File(mCurrentPhotoPath);
                            delFile.delete();

                            final Dialog dialog = new Dialog(ProfilePhotoUpdateVerifyActivity.this);
                            dialog.setContentView(R.layout.photo_update_success);
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            Button btnOk = dialog.findViewById(R.id.btn_ok);

                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    userInformationFormBean.setPhotoPath(PROFILE_URL+updateProfilePhotoResBean.getPhotoPath());
                                    String userInfoFormJson = new Gson().toJson(userInformationFormBean);
                                    PreferencesManager.setCurrentUserInfo(ProfilePhotoUpdateVerifyActivity.this,userInfoFormJson);
                                    Intent intent = intentMainMenuDrawer(getApplicationContext());
                                    startActivity(intent);
                                }
                            });

                            dialog.show();

                        }else {
                            closeDialog(upgradeProfile);
                            showErrorDialog(ProfilePhotoUpdateVerifyActivity.this,"Update Failed.");
                        }
                    }else {
                        closeDialog(upgradeProfile);
                        showErrorDialog(ProfilePhotoUpdateVerifyActivity.this,"Update Failed.");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<UpdateProfilePhotoResBean>> call, Throwable t) {
                    closeDialog(upgradeProfile);
                    showErrorDialog(ProfilePhotoUpdateVerifyActivity.this,getString(R.string.service_unavailable));
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }
}
