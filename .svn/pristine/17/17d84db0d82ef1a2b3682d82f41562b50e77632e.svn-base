package mm.com.aeon.vcsaeon.common_utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.StringTokenizer;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.MobileVersionConfigReqBean;
import mm.com.aeon.vcsaeon.beans.MobileVersionConfigResBean;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ANDROID_OS_TYPE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CHECK_REQUEST_TIMEOUT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.G_PLAY_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REFERRER;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.USER_AGENT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.VERSION_NAME_LABEL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.getAppUrl;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class VersionCheckerAsyncTask extends AsyncTask<Void,String,String> {

    private Context context;
    private String applicationId;
    private String currentVersion;
    private Activity activity;

    ProgressDialog dialog;

    public VersionCheckerAsyncTask(Context context, String applicationId, String currentVersion, Activity activity){
        this.context=context;
        this.applicationId=applicationId;
        this.currentVersion=currentVersion;
        this.activity=activity;

        activity.setTheme(R.style.MessageDialogTheme);
        dialog = new ProgressDialog(activity);
        dialog.setMessage(context.getString(R.string.progress_ver_checking));
        dialog.setCancelable(false);
        if(!activity.isFinishing()) dialog.show();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String newVersion = null;
        try {
            String url = getAppUrl(applicationId);
            Document document = Jsoup.connect(url)
                    .timeout(CHECK_REQUEST_TIMEOUT)
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText(VERSION_NAME_LABEL);
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return currentVersion;
        }
        return newVersion;
    }

    @Override
    protected void onPostExecute(final String onlineVersion) {
        super.onPostExecute(onlineVersion);

        //check update available | "OK" for download and update app.
        if(CommonUtils.isUpdateAvailable(currentVersion,onlineVersion)){

            //get version status from api.
            Service getUpdateStatusService = APIClient.getUserService();
            MobileVersionConfigReqBean mobileVersionConfigReqBean = new MobileVersionConfigReqBean();
            mobileVersionConfigReqBean.setVersionNo(onlineVersion);
            mobileVersionConfigReqBean.setOsType(ANDROID_OS_TYPE);

            Call<BaseResponse<MobileVersionConfigResBean>> req = getUpdateStatusService.getUpdateStatus(mobileVersionConfigReqBean);

            req.enqueue(new Callback<BaseResponse<MobileVersionConfigResBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<MobileVersionConfigResBean>> call, Response<BaseResponse<MobileVersionConfigResBean>> response) {

                    if(response.isSuccessful()){

                        BaseResponse baseResponse = response.body();

                        if(baseResponse.getStatus().equals(SUCCESS)){

                            closeDialog(dialog);
                            final MobileVersionConfigResBean mobVerConfigResBean = (MobileVersionConfigResBean) baseResponse.getData();

                            final Dialog alertDialog = new Dialog(context);
                            alertDialog.setContentView(R.layout.version_message_dialog);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            TextView textMsg = alertDialog.findViewById(R.id.text_message);
                            TextView textTitle = alertDialog.findViewById(R.id.text_title);
                            textTitle.setText(context.getString(R.string.msg_popup_new_ver));
                            textMsg.setTextSize(12);
                            textMsg.setGravity(Gravity.LEFT);
                            textMsg.setText(getNewVersionMsg(mobVerConfigResBean.getVersionUpdateInfo()));

                            //New Intent for Upgrade on G-Play-Service.
                            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(G_PLAY_URL + applicationId));
                            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

                            if(mobVerConfigResBean.getForceUpdFlag().equals(CommonConstants.MUST_UPDATE)){
                                alertDialog.setCancelable(false);
                                Button btnSkip = alertDialog.findViewById(R.id.btn_skip);
                                Button btnUpgrade = alertDialog.findViewById(R.id.btn_upgrade);
                                btnSkip.setVisibility(View.GONE);

                                btnUpgrade.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(intent.resolveActivity(context.getPackageManager()) != null){
                                            context.startActivity(intent);
                                        } else {
                                            //Toast.makeText(context, context.getString(R.string.msg_g_play_not_support), Toast.LENGTH_SHORT).show();
                                            displayMessage(context, context.getString(R.string.msg_g_play_not_support));
                                        }
                                        alertDialog.dismiss();
                                    }
                                });
                                if(!activity.isFinishing()) alertDialog.show();

                            } else {

                                alertDialog.setCancelable(true);
                                Button btnSkip = alertDialog.findViewById(R.id.btn_skip);
                                Button btnUpgrade = alertDialog.findViewById(R.id.btn_upgrade);

                                btnSkip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });

                                btnUpgrade.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(intent.resolveActivity(context.getPackageManager()) != null){
                                            context.startActivity(intent);
                                        } else {
                                            //Toast.makeText(context, context.getString(R.string.msg_g_play_not_support), Toast.LENGTH_SHORT).show();
                                            displayMessage(context,context.getString(R.string.msg_g_play_not_support));
                                        }
                                        alertDialog.dismiss();
                                    }
                                });
                                if(!activity.isFinishing()) alertDialog.show();
                            }

                        } else {

                            closeDialog(dialog);
                            showWarningDialog(context,context.getString(R.string.ver_upd_status_unavailable));
                        }

                    } else {
                        //Service Stopped.
                        closeDialog(dialog);
                        showErrorDialog(context,context.getString(R.string.ver_upd_status_failed));
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<MobileVersionConfigResBean>> call, Throwable t) {
                    closeDialog(dialog);
                    showErrorDialog(context,context.getString(R.string.ver_upd_status_failed));
                }
            });
        } else {
            if(!activity.isFinishing())
                closeDialog(dialog);
        }
    }

    private String getNewVersionMsg(String updatedInfo){
        String updatedFeatures=BLANK;
        int index=1;

        if(updatedInfo!=null){
            StringTokenizer tokenizer = new StringTokenizer(updatedInfo,"/");
            while (tokenizer.hasMoreTokens()){
                updatedFeatures=updatedFeatures+"("+index+") ";
                updatedFeatures=updatedFeatures+tokenizer.nextToken()+"\n";
                index++;
            }
            return "New version is available on Play Store. \n\nUpdated features are \n"+updatedFeatures+"\nEnjoy new features!";
        } else {
            return "New version is available on Play Store. \n\nEnjoy new features!";
        }
    }
}
