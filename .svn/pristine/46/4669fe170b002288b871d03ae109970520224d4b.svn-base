package mm.com.aeon.vcsaeon.common_utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.List;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.ApplicationFormErrMesgBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoAttachmentFormBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterReqBean;
import mm.com.aeon.vcsaeon.beans.RegisterSuccessResponseBean;
import mm.com.aeon.vcsaeon.fragments.DAEnquiryFragment;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REGISTER_SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class DigitalApplicationRegistrationAsyncTask extends AsyncTask<Void, Void, String> {

    private ApplicationRegisterReqBean registerBean;
    private String accessToken;
    private Context context;
    ProgressDialog registerDialog;
    private TextView textErrMsg;
    private List<MultipartBody.Part> multipartFiles;
    private String applicationNo = "";
    private IsRegistrationSuccess isRegistrationSuccess;
    private AppCompatActivity menuActivity;

    public DigitalApplicationRegistrationAsyncTask(ApplicationRegisterReqBean registerBean, String accessToken, Context context, TextView textErrMsg, List<MultipartBody.Part> parts, AppCompatActivity activity) {
        this.registerBean = registerBean;
        this.accessToken = accessToken;
        this.context = context;
        this.textErrMsg = textErrMsg;
        this.multipartFiles = parts;
        this.menuActivity = activity;
        registerDialog = new ProgressDialog(context);
    }

    public interface IsRegistrationSuccess {
        void registrationComplete(String result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        registerDialog.setMessage("Uploading...");
        registerDialog.setCancelable(false);
        registerDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        Service regService = APIClient.getApplicationRegisterService();

        //String requestParam = new Gson().toJson(purchaseFinalRegisterReqBean);
        //RequestBody uploadObj = RequestBody.create(MediaType.parse(CommonConstants.MEDIA_TYPE), requestParam);

        Call<BaseResponse<RegisterSuccessResponseBean>> request = regService.registerLoanApplication(
                accessToken, registerBean, multipartFiles);

        request.enqueue(new Callback<BaseResponse<RegisterSuccessResponseBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<RegisterSuccessResponseBean>> call, Response<BaseResponse<RegisterSuccessResponseBean>> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    ApplicationFormErrMesgBean savedInformation
                            = PreferencesManager.getErrMesgInfo(context);

                    if (savedInformation == null) {
                        savedInformation = new ApplicationFormErrMesgBean();
                    }

                    if (baseResponse.getStatus().equals(SUCCESS)) {
                        closeDialog(registerDialog);
                        MainMenuActivityDrawer.isSubmitclickConfirmData = false;
                        RegisterSuccessResponseBean registerSuccessResponseBean = (RegisterSuccessResponseBean) baseResponse.getData();
                        applicationNo = registerSuccessResponseBean.getApplicationNo();
                        String ymNo=applicationNo.substring(0,4);
                        String middleNo=applicationNo.substring(4,7);
                        String lastNo=applicationNo.substring(7,10);
                        showSuccessDialog("Application No is : " + ymNo+"-"+middleNo+"-"+lastNo);

                        // clear error message
                        textErrMsg.setVisibility(View.GONE);
                        savedInformation.setConfBusinessErrLocale(CommonConstants.BLANK);

                        // clear image
                        for (ApplicationInfoAttachmentFormBean photoBean : registerBean.getApplicationInfoAttachmentDtoList()) {
                            File file = new File(photoBean.getFilePath());
                            if (file.exists()) {
                                file.delete();
                            }
                        }

                    } else {
                        closeDialog(registerDialog);
                        showMessageDialog("Registration Un-success : " + baseResponse.getMessage());
                        textErrMsg.setVisibility(View.VISIBLE);
                        textErrMsg.setText(baseResponse.getMessage());

                        savedInformation.setConfBusinessErrLocale(baseResponse.getMessage());
                    }

                    PreferencesManager.saveErrorMesgInfo(context, savedInformation);
                } else {
                    closeDialog(registerDialog);
                    showMessageDialog("Response is null.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<RegisterSuccessResponseBean>> call, Throwable t) {
                closeDialog(registerDialog);
                showMessageDialog("Request Failed.");
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            isRegistrationSuccess.registrationComplete(result);
        }
    }

    void showMessageDialog(String message) {
        new MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    void showSuccessDialog(String message) {
        final AlertDialog alertDialog = new MaterialAlertDialogBuilder(context)
                .setTitle(REGISTER_SUCCESS)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();

        Button btnOk = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenuActivityDrawer.doRegistrationSuccess = true;
                replaceFragment(new DAEnquiryFragment());
                alertDialog.dismiss();
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = menuActivity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.commit();
    }
}
