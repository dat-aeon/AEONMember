package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.adapters.ViewPagerAdapter;
import mm.com.aeon.vcsaeon.beans.ApplicationLastInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationLastInfoResBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.ProductTypeListBean;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;

public class PagerRootFragment extends BaseFragment {

    View view;
    public static ViewPager viewPager;

    UserInformationFormBean userInformationFormBean;
    int customerId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainMenuActivityDrawer.appDataErrMsgShow = false;
        MainMenuActivityDrawer.occupationErrMsgShow = false;
        MainMenuActivityDrawer.emergencyErrMsgShow = false;
        MainMenuActivityDrawer.guarantorErrMsgShow = false;
        //Log.e("TAG","PagerRootFragment : onAttach()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pager_root, container, false);

        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);
        customerId = userInformationFormBean.getCustomerId();

        PreferencesManager.clearErrMesgInfo(getActivity());
        MainMenuActivityDrawer.isSubmitclickOccuData = false;
        MainMenuActivityDrawer.isSubmitclickEmerData = false;
        MainMenuActivityDrawer.isSubmitclickGuaData = false;

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showNetworkErrorDialog(getActivity(), getNetErrMsg());
        } else {

            //clear previous data.
            PreferencesManager.removeDaftSavedInfo(getActivity());

            final ProgressDialog loadDialog = new ProgressDialog(getActivity());
            loadDialog.setMessage("Loading Applicant's Data...");
            loadDialog.setCancelable(false);
            loadDialog.show();

            ApplicationLastInfoReqBean lastInfoReqBean = new ApplicationLastInfoReqBean();
            lastInfoReqBean.setCustomerId(customerId);

            Service loadLastInformation = APIClient.getApplicationRegisterService();
            Call<BaseResponse<ApplicationRegisterSaveReqBean>> req = loadLastInformation.getLastRegisterInfo(PreferencesManager.getAccessToken(getActivity()), lastInfoReqBean);

            req.enqueue(new Callback<BaseResponse<ApplicationRegisterSaveReqBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<ApplicationRegisterSaveReqBean>> call, Response<BaseResponse<ApplicationRegisterSaveReqBean>> response) {
                    if (response.isSuccessful()) {
                        BaseResponse baseResponse = response.body();
                        if (baseResponse != null) {
                            if (baseResponse.getStatus().equals(SUCCESS)) {

                                loadDialog.dismiss();
                                Log.e("Save Reload Data", "Success");
                                ApplicationRegisterSaveReqBean savedRegisterData = (ApplicationRegisterSaveReqBean) baseResponse.getData();
                                PreferencesManager.saveDaftSavedInfo(getActivity(), savedRegisterData);

                                //Get NRC Information.
                                Service getNrcInfoService = APIClient.getUserService();
                                final Call<BaseResponse<List<TownshipCodeResDto>>> req = getNrcInfoService.getTownshipCode();

                                req.enqueue(new Callback<BaseResponse<List<TownshipCodeResDto>>>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse<List<TownshipCodeResDto>>> call, Response<BaseResponse<List<TownshipCodeResDto>>> response) {

                                        BaseResponse baseResponse = response.body();

                                        if (baseResponse.getStatus().equals(SUCCESS)) {

                                            final List<TownshipCodeResDto> townshipCodeResDtoList =
                                                    (List<TownshipCodeResDto>) baseResponse.getData();

                                            if (townshipCodeResDtoList != null) {

                                                saveTownshipCode(townshipCodeResDtoList);

                                                viewPager = view.findViewById(R.id.view_pager);
                                                viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
                                                viewPager.setCurrentItem(viewPager.getCurrentItem(), true);

                                                loadDialog.dismiss();

                                            } else {
                                                loadDialog.dismiss();
                                            }

                                        } else {
                                            loadDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse<List<TownshipCodeResDto>>> call, Throwable t) {
                                        loadDialog.dismiss();
                                    }
                                });

                            } else {
                                loadDialog.dismiss();
                                Log.e("BaseResponse", "FAILED");
                            }
                        } else {
                            loadDialog.dismiss();
                            Log.e("BaseResponse", "BaseResponse NULL");
                        }
                    } else {
                        closeDialog(loadDialog);
                        showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                        Log.e("TAG", "Request Un-successed.");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<ApplicationRegisterSaveReqBean>> call, Throwable t) {
                    closeDialog(loadDialog);
                    Log.e("TAG", "Request Un-successed.");
                }
            });
        }

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            showNetworkErrorDialog(getActivity(), getNetErrMsg());
        } else {
            final ProgressDialog loadingCityInfo = new ProgressDialog(getActivity());
            loadingCityInfo.setMessage("Loading City and Township Info...");
            loadingCityInfo.setCancelable(false);
            loadingCityInfo.show();

            Service loadInformation = APIClient.getApplicationRegisterService();
            Call<BaseResponse<List<CityTownshipResBean>>> cityInfoReq = loadInformation.getCityTownshipList();


            cityInfoReq.enqueue(new Callback<BaseResponse<List<CityTownshipResBean>>>() {
                @Override
                public void onResponse(Call<BaseResponse<List<CityTownshipResBean>>> call, Response<BaseResponse<List<CityTownshipResBean>>> response) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null) {
                        if (baseResponse.getStatus().equals(SUCCESS)) {
                            final List<CityTownshipResBean> cityTownShipList =
                                    (List<CityTownshipResBean>) baseResponse.getData();

                            Log.e("TownshipCity list", "Success");

                            if (cityTownShipList != null) {
                                saveCityInfo(cityTownShipList);
                                loadingCityInfo.dismiss();
                            } else {
                                loadingCityInfo.dismiss();
                            }
                        } else {
                            loadingCityInfo.dismiss();
                            showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                        }
                    } else {
                        loadingCityInfo.dismiss();
                        showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<List<CityTownshipResBean>>> call, Throwable t) {
                    loadingCityInfo.dismiss();
                    showErrorDialog(getActivity(), getString(R.string.service_unavailable));
                }
            });
        }

        return view;
    }

    void saveLastRegisteredData(ApplicationLastInfoResBean lastRegisterData) {
        Log.e("TAG", "lastRegisterData JSON : " + new Gson().toJson(lastRegisterData));
        PreferencesManager.saveLastRegisteredInfo(getActivity(), lastRegisterData);
    }

    void saveTownshipCode(List<TownshipCodeResDto> townshipCodeResDtoList) {
        PreferencesManager.saveTownshipCode(getActivity(), townshipCodeResDtoList);
    }

    void saveProductType(List<ProductTypeListBean> productTypeList) {
        PreferencesManager.saveProductTypeList(getActivity(), productTypeList);
    }

    void saveCityInfo(List<CityTownshipResBean> cityTownshipList) {
        PreferencesManager.saveCityListInfo(getActivity(), cityTownshipList);
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    protected void showSnackBarMessage(String message) {
        final Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
        snackbar.show();
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.e("TAG","PagerRootFragment : onStart()");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.e("TAG","PagerRootFragment : onPause()");
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e("TAG","PagerRootFragment : onResume()");
    }
}
