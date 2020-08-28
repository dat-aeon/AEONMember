package mm.com.aeon.vcsaeon.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.PromotionsInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.adapters.AnnouncementListAdapter;
import mm.com.aeon.vcsaeon.common_utils.UiUtils;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.REFRESH_TOKEN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.CODE_TOKEN_TIMEOUT;

public class PromotionTabFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    Toolbar toolbar;

    private RecyclerView promotionsRecyclerView;
    private AnnouncementListAdapter promotionListAdapter;

    View promotionsNotFoundView;
    List<PromotionsInfoResBean> promotionsInfoResBeanList;

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.promotion_tab, container, false);

        if (PreferencesManager.getMainNavFlag(getContext())) {
            ((MainActivity) getActivity()).setLanguageListener(this);
            toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);
        } else {
            ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
            toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        }

        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setAnimation(UiUtils.animSlideToRight(getActivity()));
        menuBackBtn.setVisibility(View.VISIBLE);

        final Bundle mBundle = savedInstanceState;
        promotionsNotFoundView = view.findViewById(R.id.promotions_unavailable);

        swipeRefreshLayout = view.findViewById(R.id.announce_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getColor(R.color.colorPrimary),
                getActivity().getColor(R.color.orange), getActivity().getColor(R.color.newsheader));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadAnnouncementDataToRV(mBundle);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAnnouncementDataToRV(mBundle);
            }
        });

        view.setAnimation(UiUtils.animSlideToLeft(getActivity()));
        return view;
    }

    public void replaceFragment(Fragment fragment, int containerView) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerView, fragment, "TAG");
        transaction.commit();
    }

    private void loadAnnouncementDataToRV(final Bundle mBundle) {

        swipeRefreshLayout.setRefreshing(true);

        Service getPromotionsInfoService = APIClient.getUserService();
        Call<BaseResponse<List<PromotionsInfoResBean>>> req = getPromotionsInfoService.getPromotionsInfo();

        req.enqueue(new Callback<BaseResponse<List<PromotionsInfoResBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PromotionsInfoResBean>>> call, Response<BaseResponse<List<PromotionsInfoResBean>>> response) {

                if (response.isSuccessful()) {

                    BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        try {

                            promotionsInfoResBeanList = (List<PromotionsInfoResBean>) baseResponse.getData();
                            swipeRefreshLayout.setRefreshing(false);

                            if (promotionsInfoResBeanList.size() == 0) {
                                promotionsNotFoundView.setVisibility(View.VISIBLE);
                            } else {
                                promotionsNotFoundView.setVisibility(View.GONE);
                                //Bind Coupon List data to RecyclerView.
                                promotionsRecyclerView = view.findViewById(R.id.recycler_view_promotions);
                                promotionListAdapter = new AnnouncementListAdapter(getActivity(), promotionsInfoResBeanList, mBundle);
                                promotionsRecyclerView.setAdapter(promotionListAdapter);
                                promotionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }

                        } catch (Exception e) {

                            swipeRefreshLayout.setRefreshing(false);
                            promotionsNotFoundView.setVisibility(View.VISIBLE);
                        }

                    } else {

                        swipeRefreshLayout.setRefreshing(false);
                        promotionsNotFoundView.setVisibility(View.VISIBLE);
                    }

                } else if (response.code() == CODE_TOKEN_TIMEOUT) {

                    //Refresh Token.
                    Service refreshTokenService = APIClient.getAuthUserService();
                    Call<BaseResponse<LoginAccessTokenInfo>> refreshToken = refreshTokenService.refreshToken(
                            REFRESH_TOKEN, PreferencesManager.getRefreshToken(getActivity())
                    );

                    refreshToken.enqueue(new Callback<BaseResponse<LoginAccessTokenInfo>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<LoginAccessTokenInfo>> call, Response<BaseResponse<LoginAccessTokenInfo>> response) {
                            if (response.isSuccessful()) {
                                BaseResponse baseResponse = response.body();
                                if (baseResponse.getStatus().equals(SUCCESS)) {
                                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) baseResponse.getData();
                                    PreferencesManager.keepAccessToken(getActivity(), loginAccessTokenInfo.getAccessToken());
                                    PreferencesManager.keepRefreshToken(getActivity(), loginAccessTokenInfo.getRefreshToken());
                                    swipeRefreshLayout.setRefreshing(false);
                                    if (PreferencesManager.getMainNavFlag(getActivity())) {
                                        replaceFragment(new PromotionTabFragment(), R.id.content_new_main_drawer);
                                    } else {
                                        replaceFragment(new PromotionTabFragment(), R.id.content_main_drawer);
                                    }
                                } else {
                                    swipeRefreshLayout.setRefreshing(false);
                                    promotionsNotFoundView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                swipeRefreshLayout.setRefreshing(false);
                                promotionsNotFoundView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {
                            swipeRefreshLayout.setRefreshing(false);
                            promotionsNotFoundView.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    promotionsNotFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PromotionsInfoResBean>>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                promotionsNotFoundView.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void changeLanguageTitle(String lang) {
        PreferencesManager.setCurrentLanguage(getContext(), lang);
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new PromotionTabFragment(), R.id.content_new_main_drawer);
        } else {
            replaceFragment(new PromotionTabFragment(), R.id.content_main_drawer);
        }
    }

    @Override
    public void clickMenuBarBackBtn() {
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new NewMainPageFragment(), R.id.content_new_main_drawer);
        } else {
            replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
        }
    }
}
