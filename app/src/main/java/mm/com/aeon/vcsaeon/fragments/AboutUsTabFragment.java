package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.CompanyInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.AccessPermissionResultDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CALL_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHONE_URI_PREFIX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class AboutUsTabFragment extends BaseFragment implements AccessPermissionResultDelegate {

    View view;

    TextView textAbout;
    TextView textPhone;
    TextView textFacebook;
    TextView textWeb;
    TextView textAddress;
    TextView txtTitle;
    TextView txtService;
    View serviceNotFoundView;
    Button btnCall;
    Toolbar toolbar;

    View infoView;

    CompanyInfoResBean companyInfoResBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.about_us, container, false);

        textAbout = view.findViewById(R.id.text_about);
        textPhone = view.findViewById(R.id.text_phone);
        textFacebook = view.findViewById(R.id.aeon_fb_link);
        textWeb = view.findViewById(R.id.aeon_web_link);
        textAddress = view.findViewById(R.id.text_address);

        txtTitle = view.findViewById(R.id.about_title);
        btnCall = view.findViewById(R.id.btn_about_call);

        infoView = view.findViewById(R.id.info_view);

        serviceNotFoundView = view.findViewById(R.id.service_unavailable_about_us);
        serviceNotFoundView.setVisibility(View.GONE);

        txtService = serviceNotFoundView.findViewById(R.id.service_unavailable_label);

        final String curLang = PreferencesManager.getCurrentLanguage(getContext());

        companyInfoResBean = new CompanyInfoResBean();

        if (PreferencesManager.getMainNavFlag(getContext())) {
            ((MainActivity) getActivity()).setLocationDelegate(this);
        } else {
            ((MainMenuActivityDrawer) getActivity()).setAccessDelegate(this);
        }

        Service getAboutUsInfoService = APIClient.getUserService();
        Call<BaseResponse<CompanyInfoResBean>> req = getAboutUsInfoService.getCompanyInfo();

        getActivity().setTheme(R.style.MessageDialogTheme);

        req.enqueue(new Callback<BaseResponse<CompanyInfoResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<CompanyInfoResBean>> call, Response<BaseResponse<CompanyInfoResBean>> response) {
                if (response.isSuccessful()) {

                    final BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        companyInfoResBean = (CompanyInfoResBean) baseResponse.getData();

                        btnCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int permission = ContextCompat.checkSelfPermission(getContext(),
                                        Manifest.permission.CALL_PHONE);

                                if (permission != PackageManager.PERMISSION_GRANTED) {
                                    makeCallRequest();
                                } else {
                                    String hotLinePhoneNo = companyInfoResBean.getHotlinePhone();
                                    if (hotLinePhoneNo == null || hotLinePhoneNo.equals(BLANK)) {
                                        displayMessage(getContext(), getString(R.string.message_call_not_available));
                                    } else {
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX + hotLinePhoneNo));
                                        startActivity(callIntent);
                                    }
                                }
                            }
                        });

                        changeLabel(curLang);
                    }

                } else {

                    // display service_unavailable layout if not success.
                    serviceNotFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CompanyInfoResBean>> call, Throwable t) {
                serviceNotFoundView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public void addValueToPreference(String lang) {
        PreferencesManager.setCurrentLanguage(getContext(), lang);
    }

    public void changeLabel(String language) {

        btnCall.setText(CommonUtils.getLocaleString(new Locale(language), R.string.aboutus_all_now_button, getActivity()));
        txtService.setText(CommonUtils.getLocaleString(new Locale(language), R.string.service_unavailable, getActivity()));

        textPhone.setText(companyInfoResBean.getHotlinePhone());
        textFacebook.setText(companyInfoResBean.getSocialMediaAddress());
        textWeb.setText(companyInfoResBean.getWebAddress());

        if (language.equals(LANG_EN)) {
            textAbout.setText(companyInfoResBean.getAboutCompanyEn());
            textAddress.setText(companyInfoResBean.getAddressEn());
        } else {
            textAbout.setText(companyInfoResBean.getAboutCompanyMm());
            textAddress.setText(companyInfoResBean.getAddressMm());
        }
        PreferencesManager.setCurrentLanguage(getActivity(), language);

        infoView.setVisibility(View.VISIBLE);
    }

    protected void makeCallRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                CALL_REQUEST_CODE);
    }

    protected void showSnackBarMessage(String message) {
        final Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(ResourcesCompat.getFont(getContext(), R.font.pyidaungsu_regular));
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public void onAccessRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //Call Service.
            case CALL_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    showSnackBarMessage(getString(R.string.message_permission_deniled));
                } else {
                    final String hotLinePhoneNo = companyInfoResBean.getHotlinePhone();
                    if (hotLinePhoneNo == null || hotLinePhoneNo.equals(BLANK)) {
                        showSnackBarMessage(getString(R.string.message_call_not_available));
                    } else {
                        Log.e("call", "About");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(PHONE_URI_PREFIX + hotLinePhoneNo));
                        startActivity(callIntent);
                    }
                }
                return;
            }
        }
    }
}
