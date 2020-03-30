package mm.com.aeon.vcsaeon.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.CameraxActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;

import static android.app.Activity.RESULT_OK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CAMERA_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_CAMERA;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_STORAGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REGISTER_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;

public class VerifyPhotoUploadFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    Button btnUpload;

    String mCurrentPhotoPath;

    TextView txtTitle;
    TextView txt0;
    TextView txt03;

    /*private static final int PHOTO_REQUEST_CODE = 330;
    private int REQUEST_CODE_PERMISSIONS = 101;*/
    private final String[] REQUIRED_PERMISSIONS = new String[]{PERMISSION_CAMERA, PERMISSION_STORAGE};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.verify_photo_upload, container, false);

        // close button listener
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);

        txtTitle = view.findViewById(R.id.verify_upload_title);
        txt0 = view.findViewById(R.id.txt_0);
        txt03 = view.findViewById(R.id.txt_03);
        btnUpload = view.findViewById(R.id.btn_upload);
        mCurrentPhotoPath = getActivity().getIntent().getStringExtra("mCurrentPhotoPath");

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, "lang");

        if (curLang.equals(LANG_MM)) {
            changeLabel(curLang);
        } else {
            changeLabel(curLang);
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allPermissionsGranted()) {
                    dispatchTakePictureIntent();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, CAMERA_REQUEST_CODE);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mCurrentPhotoPath = data.getData().toString();
            SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(getActivity());
            PreferencesManager.addEntryToPreferences(preferences, "current_photo_path", mCurrentPhotoPath);
            VerifyConfirmPhotoFragment verifyConfirmPhotoFragment = (VerifyConfirmPhotoFragment) fragmentPhotoConfirming(mCurrentPhotoPath);
            replaceFragment(verifyConfirmPhotoFragment);
        } else {
            //
        }

    }

    private Fragment fragmentPhotoConfirming(String currentPhotoPath) {
        VerifyConfirmPhotoFragment verifyConfirmPhotoFragment =
                new VerifyConfirmPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(REGISTER_PHOTO, currentPhotoPath);
        verifyConfirmPhotoFragment.setArguments(bundle);
        return verifyConfirmPhotoFragment;
    }

    private void dispatchTakePictureIntent() {
        Intent intent = intentPhotoTaking(getActivity());
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private static Intent intentPhotoTaking(Context context) {
        Intent intent = new Intent(context, CameraxActivity.class);
        return intent;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.addToBackStack("verify_member");
        transaction.commit();
    }

    public void changeLabel(String language) {
        btnUpload.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_upload_button, getActivity()));
        txtTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_announce_label, getActivity()));
        txt0.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_notice1_label, getActivity()));
        txt03.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoupload_notice5_label, getActivity()));
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (allPermissionsGranted()) {
                dispatchTakePictureIntent();
            } else {
                //Toast.makeText(getActivity(), getString(R.string.message_permission_deniled), Toast.LENGTH_SHORT).show();
                displayMessage(getActivity(), getString(R.string.message_permission_deniled));
            }
        }
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new VerificationMemberInfoFragment());
    }
}

