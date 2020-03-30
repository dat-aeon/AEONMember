package mm.com.aeon.vcsaeon.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ExistedMemberRegistrationInfoReqBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.EXISTED_REG_MEM_INFO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_CAMERA;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_STORAGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REGISTER_PHOTO;

public class RegistrationPhotoUploadActivity extends BaseActivity {

    Toolbar toolbar;
    Button btnTakePhoto;
    TextView txtTitle;
    TextView txtInfo1;
    TextView txtInfo5;

    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

    String mCurrentPhotoPath;
    ExistedMemberRegistrationInfoReqBean existedMemberRegistrationInfoReqBean;

    private static final int PHOTO_REQUEST_CODE = 330;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{PERMISSION_CAMERA,PERMISSION_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_upload_photo);

        existedMemberRegistrationInfoReqBean = new ExistedMemberRegistrationInfoReqBean();
        existedMemberRegistrationInfoReqBean = (ExistedMemberRegistrationInfoReqBean) getIntent().getSerializableExtra(EXISTED_REG_MEM_INFO);

        toolbar = findViewById(R.id.toolbar_main_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBackbtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackbtn.setVisibility(View.VISIBLE);

        menuBarName = toolbar.findViewById(R.id.menu_bar_name);
        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText(R.string.menu_level_one);
        //get logged in phone number from fragment.
        String installPhone = PreferencesManager.getInstallPhoneNo(getApplicationContext()).trim();
        menuBarPhoneNo.setText(installPhone);
        menuBarName.setVisibility(View.GONE);

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

        menuBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        txtTitle = findViewById(R.id.photo_upload_title);
        txtInfo1 = findViewById(R.id.upload_text_info_1);
        txtInfo5 = findViewById(R.id.upload_text_info_5);

        btnTakePhoto = findViewById(R.id.btn_reg_upload);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allPermissionsGranted()){
                    dispatchTakePictureIntent();
                } else{
                    ActivityCompat.requestPermissions(RegistrationPhotoUploadActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            mCurrentPhotoPath = data.getData().toString();
            Intent intent = intentPhotoConfirming(RegistrationPhotoUploadActivity.this, mCurrentPhotoPath,
                    existedMemberRegistrationInfoReqBean);
            startActivity(intent);
        } else {

        }
    }

    @Override
    public void onStart(){
        super.onStart();
        changeLabel(PreferencesManager.getCurrentLanguage(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.toolbar_menu, menu);

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

    private void dispatchTakePictureIntent() {
        Intent intent = intentPhotoTaking(RegistrationPhotoUploadActivity.this);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    public void addValueToPreference(String lang){
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    public void changeLabel(String language){
        btnTakePhoto.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_upload_button, getApplicationContext()));
        txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_announce_label, getApplicationContext()));
        txtInfo1.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_notice1_label, getApplicationContext()));
        txtInfo5.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_notice5_label, getApplicationContext()));
    }

    private static Intent intentPhotoTaking(Context context){
        Intent intent = new Intent(context, CameraxActivity.class);
        return intent;
    }

    private static Intent intentPhotoConfirming(Context context, String currentPhotoPath,
                                                ExistedMemberRegistrationInfoReqBean existedMemberRegInfoReqBean){
        Intent intent = new Intent(context, PhotoConfirmationActivity.class);
        intent.putExtra(REGISTER_PHOTO, currentPhotoPath);
        intent.putExtra(EXISTED_REG_MEM_INFO, existedMemberRegInfoReqBean);
        return intent;
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

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}
