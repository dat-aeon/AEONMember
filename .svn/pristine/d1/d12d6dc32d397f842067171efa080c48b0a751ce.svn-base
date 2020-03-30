package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.w3c.dom.Text;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CouponInfoReqBean;
import mm.com.aeon.vcsaeon.beans.CouponInfoResBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.CouponInfoListAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;

public class CouponTabFragment extends BaseFragment {

    View view;

    UserInformationFormBean userInformationFormBean;
    SharedPreferences preferences;

    View couponNotFoundView;
    TextView txtCouponNotFound;

    private RecyclerView couponInfoRecyclerView;
    private CouponInfoListAdapter couponInfoListAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coupon_tab, container, false);

        userInformationFormBean = new UserInformationFormBean();
        preferences = PreferencesManager.getCurrentUserPreferences(getActivity());
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        final String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, "lang");

        couponNotFoundView = view.findViewById(R.id.coupon_unavailable);
        couponNotFoundView.setVisibility(View.GONE);
        txtCouponNotFound = couponNotFoundView.findViewById(R.id.coupon_unavailable_label);
        if(curLang.equals(LANG_MM)){
            txtCouponNotFound.setText(CommonUtils.getLocaleString(new Locale(LANG_MM), R.string.no_gift, getActivity()));
        } else {
            txtCouponNotFound.setText(CommonUtils.getLocaleString(new Locale(LANG_EN), R.string.no_gift, getActivity()));
        }

        // check page
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            Log.e("check user info", "Come from main menu.");
            couponNotFoundView.setVisibility(View.VISIBLE);
            TextView text = couponNotFoundView.findViewById(R.id.coupon_unavailable_label);
            text.setText("Features not available.");
        } else {

            swipeRefreshLayout = view.findViewById(R.id.coupon_swipe_refresh_layout);
            swipeRefreshLayout.setColorSchemeColors(getActivity().getColor(R.color.colorPrimary),
                    getActivity().getColor(R.color.orange), getActivity().getColor(R.color.newsheader));
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadCouponInfoToRV();
                }
            });

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadCouponInfoToRV();
                }
            });

        }

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    //union two list.
    private List<CouponInfoResBean> updateCouponInfoList(List<CouponInfoResBean> existingList, List<CouponInfoResBean> newList){
        for (CouponInfoResBean couponInfoResBean : existingList) {
            if(couponInfoResBean.getUsedFlag()!=null){
                newList.add(couponInfoResBean);
            }
        }
        return newList;
    }

    private void loadCouponInfoToRV(){

        swipeRefreshLayout.setRefreshing(true);

        //load coupon info.
        Service getCouponInfoService = APIClient.getUserService();
        Call<BaseResponse<List<CouponInfoResBean>>> reqCouponInfo =
                getCouponInfoService.getCouponInfo(PreferencesManager.getAccessToken(getActivity()),
                        new CouponInfoReqBean(userInformationFormBean.getCustomerId()));

        reqCouponInfo.enqueue(new Callback<BaseResponse<List<CouponInfoResBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CouponInfoResBean>>> call, Response<BaseResponse<List<CouponInfoResBean>>> response) {
                if(response.isSuccessful()){
                    BaseResponse baseResponse = response.body();

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        List<CouponInfoResBean> couponInfoResBeanList2 = (List<CouponInfoResBean>) baseResponse.getData();

                        try{
                            //Get coupon info from logged in.
                            final String couponResInfo = PreferencesManager.getStringEntryFromPreferences(preferences, "cur_coupon_info");

                            Gson gson = new Gson();
                            Type token = new TypeToken<List<CouponInfoResBean>>() {}.getType();
                            List<CouponInfoResBean> couponInfoResBeanList = gson.fromJson(couponResInfo, token);

                            couponInfoResBeanList = updateCouponInfoList(couponInfoResBeanList,couponInfoResBeanList2);

                            if (couponInfoResBeanList != null && couponInfoResBeanList.size() > 0) {
                                couponNotFoundView.setVisibility(View.GONE);
                                //Bind Coupon List data to RecyclerView.
                                couponInfoRecyclerView = view.findViewById(R.id.recycler_view_coupon_info);
                                couponInfoListAdapter = new CouponInfoListAdapter(getActivity(), couponInfoResBeanList);
                                couponInfoRecyclerView.setAdapter(couponInfoListAdapter);
                                couponInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                swipeRefreshLayout.setRefreshing(false);

                            } else {

                                swipeRefreshLayout.setRefreshing(false);
                                couponNotFoundView.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e){

                            swipeRefreshLayout.setRefreshing(false);
                            couponNotFoundView.setVisibility(View.VISIBLE);
                        }

                    } else {

                        swipeRefreshLayout.setRefreshing(false);
                        couponNotFoundView.setVisibility(View.VISIBLE);
                    }

                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    couponNotFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<CouponInfoResBean>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                couponNotFoundView.setVisibility(View.VISIBLE);
            }
        });
    }

}