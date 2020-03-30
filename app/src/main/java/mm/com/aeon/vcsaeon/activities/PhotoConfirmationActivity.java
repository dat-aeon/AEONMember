package mm.com.aeon.vcsaeon.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import java.io.File;
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

public class PhotoConfirmationActivity extends BaseActivity {

    String mCurrentPhotoPath;
    ImageView imgView;

    Toolbar toolbar;

    ImageView myTitleBtn;
    ImageView engTitleBtn;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;
    LinearLayout menuBackbtn;

    Button btnConfirm;
    Button btnRetry;
    TextView txtConfirm;

    File imgFile;

    private static final int PHOTO_REQUEST_CODE = 330;
    private static final int RECORD_REQUEST_CODE = 102;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{PERMISSION_CAMERA,PERMISSION_STORAGE};

    ExistedMemberRegistrationInfoReqBean existedMemberRegistrationInfoReqBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_confirmation);

        existedMemberRegistrationInfoReqBean = new ExistedMemberRegistrationInfoReqBean();
        existedMemberRegistrationInfoReqBean = (ExistedMemberRegistrationInfoReqBean) getIntent().getSerializableExtra(EXISTED_REG_MEM_INFO);

        mCurrentPhotoPath = getIntent().getStringExtra(REGISTER_PHOTO);
        imgView = findViewById(R.id.photo_view);

        imgFile = new File(mCurrentPhotoPath);

        toolbar = findViewById(R.id.toolbar_main_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBackbtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackbtn.setVisibility(View.GONE);

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

        btnConfirm = findViewById(R.id.btn_photo_confirm);
        btnRetry = findViewById(R.id.btn_photo_retry);
        txtConfirm = findViewById(R.id.confirm_photo_txt2);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(curLang.equals(LANG_MM)){
            changeLabel(curLang);
        } else {
            changeLabel(curLang);
        }

        if(imgFile.exists()){
            setImageConfirm(imgFile);
        } else {
            //make confirm disable.
        }

        //confirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoConfirmationActivity.this, RegistrationGetOtpCodeAndSendActivity.class);
                intent.putExtra(REGISTER_PHOTO, mCurrentPhotoPath);
                intent.putExtra(EXISTED_REG_MEM_INFO, existedMemberRegistrationInfoReqBean);
                startActivity(intent);
            }
        });

        //retry
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allPermissionsGranted()){
                    dispatchTakePictureIntent();
                } else{
                    makeCameraRequest();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            mCurrentPhotoPath = data.getData().toString();
            imgFile = new File(mCurrentPhotoPath);
            if(imgFile.exists()){
                setImageConfirm(imgFile);
            } else {
                //
            }
        } else {

        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = intentPhotoTaking(PhotoConfirmationActivity.this);
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    private static Intent intentPhotoTaking(Context context){
        Intent intent = new Intent(context, CameraxActivity.class);
        return intent;
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
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

    private void addValueToPreference(String lang){
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    private void changeLabel(String language){
        btnRetry.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_retry, getApplicationContext()));
        btnConfirm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_confirm, getApplicationContext()));
        txtConfirm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_title, getApplicationContext()));
        toolbar.setTitle(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_title, getApplicationContext()));
    }

    private void makeCameraRequest(){
        ActivityCompat.requestPermissions(PhotoConfirmationActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
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

    //set captured image to image-view.
    private void setImageConfirm(File imgFile){
        Bitmap bitmap = CommonUtils.encodedFileToBitmap(imgFile);
        imgView.setImageBitmap(bitmap);
    }

    @Override
    public void onStart(){
        super.onStart();
        changeLabel(PreferencesManager.getCurrentLanguage(this));
    }

}
