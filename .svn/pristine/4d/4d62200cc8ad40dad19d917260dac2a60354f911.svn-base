package mm.com.aeon.vcsaeon.fragments;

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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.CameraxActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.BiometricSensorStatus;
import mm.com.aeon.vcsaeon.beans.CurrentUserInformationResBean;
import mm.com.aeon.vcsaeon.beans.UpdateVerificationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_CAMERA;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PERMISSION_STORAGE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REGISTER_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.PROFILE_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.replaceVerifiedPhoneNo;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class VerifyConfirmPhotoFragment extends BaseFragment implements LanguageChangeListener {

    View view;

    Button btnConfirm;
    Button btnRetry;

    ImageView imgView;
    File imgFile;

    UserInformationFormBean userInformationFormBean;
    UserInformationFormBean userInformationFormBean2;
    SharedPreferences preferences;

    TextView txtConfirm;

    String mCurrentPhotoPath;

    static String customerNo;

    private static final int PHOTO_REQUEST_CODE = 330;
    private static final int RECORD_REQUEST_CODE = 102;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{PERMISSION_CAMERA,PERMISSION_STORAGE};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userInformationFormBean = new UserInformationFormBean();
        preferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        customerNo = PreferencesManager.getStringEntryFromPreferences(preferences,"customer_no");
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.verify_photo_confirmation, container, false);

        // close button listener
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);

        btnConfirm = view.findViewById(R.id.btn_photo_verify_confirm);
        btnRetry = view.findViewById(R.id.btn_photo_verify_retry);
        txtConfirm = view.findViewById(R.id.confirm_photo_txt);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);

        if(curLang.equals(LANG_MM)){
            changeLabel(curLang);
        } else {
            changeLabel(curLang);
        }

        Bundle bundle = getArguments();

        //for change language.
        if(bundle==null){
            mCurrentPhotoPath=PreferencesManager.getStringEntryFromPreferences(preferences,"current_photo_path");
        } else {
            mCurrentPhotoPath = bundle.getString(REGISTER_PHOTO);
        }

        imgView = view.findViewById(R.id.conf_photo_view);
        imgFile = new File(mCurrentPhotoPath);

        if(imgFile.exists()){
            setImageConfirm(imgFile);
        }

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allPermissionsGranted()){
                    dispatchTakePictureIntent();
                } else{
                    ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imgFile.exists()){

                    final EditText input = new EditText(getActivity());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        showNetworkErrorDialog(getActivity(),getNetErrMsg());
                    } else {

                        //(1) upload data to service.
                        UpdateVerificationInfoReqBean updateVerificationInfoReqBean =
                                new UpdateVerificationInfoReqBean();

                        updateVerificationInfoReqBean.setCustomerId(String.valueOf(userInformationFormBean.getCustomerId()));
                        updateVerificationInfoReqBean.setCustomerNo(customerNo);

                        byte[] bytes = CommonUtils.encodedFileToByteArray(imgFile);
                        updateVerificationInfoReqBean.setPhotoByte(bytes);

                        //Call Member Updating Async Task.
                        new UpdateNewMemberAsynTask().execute(updateVerificationInfoReqBean);
                    }
                } else {

                    final Snackbar snackbar = Snackbar.make(view, getString(R.string.img_destroy_snackbar_info), Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(allPermissionsGranted()){
                                        dispatchTakePictureIntent();
                                    } else{
                                        ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                                    }
                                }
                            });
                    snackbar.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            mCurrentPhotoPath = data.getData().toString();
            imgFile = new File(mCurrentPhotoPath);
            if(imgFile.exists()){
                PreferencesManager.addEntryToPreferences(preferences,"current_photo_path", mCurrentPhotoPath);
                setImageConfirm(imgFile);
            }
        } else {

        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = intentPhotoTaking(getActivity());
        startActivityForResult(intent, PHOTO_REQUEST_CODE);
    }

    private static Intent intentPhotoTaking(Context context){
        Intent intent = new Intent(context, CameraxActivity.class);
        return intent;
    }

    private String getVerifyMsgAlertWithBiometricOk(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.verified_ph_no_alert, getActivity());
    }

    private String getVerifyMsgAlert(String language){
        return CommonUtils.getLocaleString(new Locale(language), R.string.verified_ph_no_alert_biometric_not_ok, getActivity());
    }

    private void changeLabel(String language){
        btnRetry.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_retry, getActivity()));
        btnConfirm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_confirm, getActivity()));
        txtConfirm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.photoconfirm_confirm_title, getActivity()));
    }

    private String getNetErrMsg(){
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED){
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
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new VerifyPhotoUploadFragment());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.addToBackStack("verify_member");
        transaction.commit();
    }

    private class UpdateNewMemberAsynTask extends AsyncTask<UpdateVerificationInfoReqBean, Integer, String> {

        ProgressDialog upgradeMemberDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            getActivity().setTheme(R.style.MessageDialogTheme);
            upgradeMemberDialog = new ProgressDialog(getActivity());
            upgradeMemberDialog.setMessage(getString(R.string.progress_verify));
            upgradeMemberDialog.setCancelable(false);
            upgradeMemberDialog.show();
        }

        @Override
        protected String doInBackground(UpdateVerificationInfoReqBean... updateVerificationInfoReqBeans) {

            UpdateVerificationInfoReqBean updateVerifyInfoReqBean = updateVerificationInfoReqBeans[0];

            Service verifyNewUserService = APIClient.getUserService();
            Call<BaseResponse<CurrentUserInformationResBean>> req = verifyNewUserService.verifyNewUser(
                    PreferencesManager.getAccessToken(getActivity()), updateVerifyInfoReqBean);

            req.enqueue(new Callback<BaseResponse<CurrentUserInformationResBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<CurrentUserInformationResBean>> call, Response<BaseResponse<CurrentUserInformationResBean>> response) {

                    if (response.isSuccessful()) {

                        BaseResponse baseResponse = response.body();
                        if (baseResponse.getStatus().equals(SUCCESS)) {

                            closeDialog(upgradeMemberDialog);

                            final CurrentUserInformationResBean currentUserInfoResBean =
                                    (CurrentUserInformationResBean) baseResponse.getData();

                            final String curLang2 = PreferencesManager.getCurrentLanguage(getActivity());

                            //delete captured file.
                            File delFile = new File(mCurrentPhotoPath);
                            delFile.delete();

                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.success_message_dialog);
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            Button btnOk = dialog.findViewById(R.id.btn_ok);
                            TextView messageBody = dialog.findViewById(R.id.text_message);

                            //Check biometric sensor in device.
                            BiometricSensorStatus biometricSensorStatus = CommonUtils.checkBiometricSensor(getActivity());
                            switch (biometricSensorStatus){
                                case BIOMETRIC_OK:
                                    messageBody.setText(replaceVerifiedPhoneNo(getVerifyMsgAlertWithBiometricOk(curLang2),currentUserInfoResBean.getPhoneNo()));
                                    break;
                                default:
                                    messageBody.setText(replaceVerifiedPhoneNo(getVerifyMsgAlert(curLang2),currentUserInfoResBean.getPhoneNo()));
                                    break;
                            }

                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                    userInformationFormBean2 = new UserInformationFormBean();
                                    userInformationFormBean2.setCustomerId(currentUserInfoResBean.getCustomerId());
                                    userInformationFormBean2.setCustomerNo(currentUserInfoResBean.getCustomerNo());
                                    userInformationFormBean2.setPhoneNo(currentUserInfoResBean.getPhoneNo());
                                    userInformationFormBean2.setCustomerTypeId(currentUserInfoResBean.getCustomerTypeId());
                                    userInformationFormBean2.setName(currentUserInfoResBean.getName());
                                    userInformationFormBean2.setDateOfBirth(currentUserInfoResBean.getDateOfBirth());
                                    userInformationFormBean2.setNrcNo(currentUserInfoResBean.getNrcNo());
                                    userInformationFormBean2.setPhotoPath(PROFILE_URL+currentUserInfoResBean.getPhotoPath());
                                    userInformationFormBean2.setAppUsageDetailId(userInformationFormBean.getAppUsageDetailId());
                                    userInformationFormBean2.setMemberNo(currentUserInfoResBean.getMemberNo());
                                    userInformationFormBean2.setMemberNoValid(currentUserInfoResBean.isMemberNoValid());
                                    userInformationFormBean2.setHotlinePhone(currentUserInfoResBean.getHotlinePhone());
                                    userInformationFormBean2.setCustAgreementListDtoList(currentUserInfoResBean.getCustomerAgreementDtoList());

                                    String userInfoFormJson = new Gson().toJson(userInformationFormBean2);
                                    PreferencesManager.setCurrentLoginPhoneNo(getActivity(),currentUserInfoResBean.getPhoneNo());
                                    PreferencesManager.setCurrentUserInfo(getActivity(),userInfoFormJson);

                                    //(3) go to menu | show card.
                                    getActivity().finish();
                                    PreferencesManager.setRegistrationCompleted(getContext(), true);
                                    Intent intent = new Intent(getContext(), MainMenuActivityDrawer.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            });
                            dialog.show();

                        } else {
                            closeDialog(upgradeMemberDialog);
                            showErrorDialog(getActivity(),getString(R.string.message_verify_failed));
                        }

                    } else {
                        closeDialog(upgradeMemberDialog);
                        showErrorDialog(getActivity(),getString(R.string.message_verify_failed));
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<CurrentUserInformationResBean>> call, Throwable t) {
                    closeDialog(upgradeMemberDialog);
                    showErrorDialog(getActivity(),getString(R.string.service_unavailable));
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
