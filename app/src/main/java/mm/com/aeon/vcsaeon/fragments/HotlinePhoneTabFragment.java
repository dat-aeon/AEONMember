package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import mm.com.aeon.vcsaeon.beans.HotlineInfoResBean;

import mm.com.aeon.vcsaeon.R;
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
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class HotlinePhoneTabFragment extends BaseFragment {

    View view;
    View serviceUnavailable;

    TextView textPhone;
    Button btnCall;

    Service getHotlineInfo;

    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hotlinephone_tab, container, false);

        serviceUnavailable = view.findViewById(R.id.service_unavailable_hotline);
        serviceUnavailable.setVisibility(View.GONE);

        textPhone = view.findViewById(R.id.text_phone);
        btnCall = view.findViewById(R.id.btn_call);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        getHotlineInfo = APIClient.getUserService();
        Call<BaseResponse<HotlineInfoResBean>> req = getHotlineInfo.getHotlineInfo();

        getActivity().setTheme(R.style.MessageDialogTheme);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.progress_loading));
        dialog.setCancelable(false);
        dialog.show();

        req.enqueue(new Callback<BaseResponse<HotlineInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<HotlineInfoResBean>> call, Response<BaseResponse<HotlineInfoResBean>> response) {

                if(response.isSuccessful()){

                    BaseResponse baseResponse = response.body();

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        final HotlineInfoResBean hotlineInfoResBean = (HotlineInfoResBean) baseResponse.getData();

                        textPhone.setText(hotlineInfoResBean.getHotlinePhone());

                        btnCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isCallAllowed()){
                                    String hotlinePhoneNo = hotlineInfoResBean.getHotlinePhone();
                                    if(hotlinePhoneNo==null || hotlinePhoneNo.equals("")){
                                        //Toast.makeText(getActivity(),getString(R.string.message_call_not_available), Toast.LENGTH_SHORT).show();
                                        displayMessage(getActivity(),getString(R.string.message_call_not_available));
                                    } else {
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX+hotlinePhoneNo));
                                        startActivity(callIntent);
                                    }
                                }
                            }
                        });

                        closeDialog(dialog);

                    } else {

                        closeDialog(dialog);
                        serviceUnavailable.setVisibility(View.VISIBLE);
                    }

                } else {

                    closeDialog(dialog);
                    serviceUnavailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<HotlineInfoResBean>> call, Throwable t) {

                closeDialog(dialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public void changeLabel(String language){
        btnCall.setText(CommonUtils.getLocaleString(new Locale(language), R.string.contactus_callnow__button, getActivity()));
    }

    //Check call allowed or not.
    public boolean isCallAllowed() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    0);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
