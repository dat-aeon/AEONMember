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
import mm.com.aeon.vcsaeon.beans.EventsNewsInfoResBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.adapters.EventsNewsListAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
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

public class EventNewsTabFragment extends BaseFragment implements LanguageChangeListener {

    View view;
    Toolbar toolbar;
    private RecyclerView eventsNewsInfoRecyclerView;
    private EventsNewsListAdapter eventsNewsListAdapter;

    List<EventsNewsInfoResBean> eventsNewsInfoResBeanList;

    View newsNotFoundView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.events_news_tab, container, false);

        if (PreferencesManager.getMainNavFlag(getContext())) {
            ((MainActivity) getActivity()).setLanguageListener(this);
            toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);
            LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
            menuBackBtn.setAnimation(UiUtils.animSlideToRight(getActivity()));
            menuBackBtn.setVisibility(View.VISIBLE);
        }

        final Bundle mBundle = savedInstanceState;
        newsNotFoundView = view.findViewById(R.id.news_unavailable);

        swipeRefreshLayout = view.findViewById(R.id.news_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getActivity().getColor(R.color.colorPrimary),
                getActivity().getColor(R.color.orange), getActivity().getColor(R.color.newsheader));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadGoodsNewsDataToRV(mBundle);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadGoodsNewsDataToRV(mBundle);
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_new_main_drawer, fragment, "TAG");
        transaction.commit();
    }

    //load data to recycler-view.
    private void loadGoodsNewsDataToRV(final Bundle mBundle) {

        swipeRefreshLayout.setRefreshing(true);

        Service getNewsInfoService = APIClient.getUserService();
        Call<BaseResponse<List<EventsNewsInfoResBean>>> req = getNewsInfoService.getNewsInfo();

        req.enqueue(new Callback<BaseResponse<List<EventsNewsInfoResBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<EventsNewsInfoResBean>>> call, Response<BaseResponse<List<EventsNewsInfoResBean>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse.getStatus().equals(SUCCESS)) {
                        try {
                            eventsNewsInfoResBeanList = (List<EventsNewsInfoResBean>) baseResponse.getData();
                            swipeRefreshLayout.setRefreshing(false);

                            if (eventsNewsInfoResBeanList.size() == 0) {
                                newsNotFoundView.setVisibility(View.VISIBLE);
                            } else {
                                newsNotFoundView.setVisibility(View.GONE);
                                //Bind Coupon List data to RecyclerView.
                                eventsNewsInfoRecyclerView = view.findViewById(R.id.recycler_view_events_news);
                                eventsNewsListAdapter = new EventsNewsListAdapter(getActivity(), eventsNewsInfoResBeanList, eventsNewsInfoRecyclerView, mBundle);
                                eventsNewsInfoRecyclerView.setAdapter(eventsNewsListAdapter);
                                eventsNewsInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            }

                        } catch (Exception e) {

                            swipeRefreshLayout.setRefreshing(false);
                            newsNotFoundView.setVisibility(View.VISIBLE);
                        }
                    } else {

                        swipeRefreshLayout.setRefreshing(false);
                        newsNotFoundView.setVisibility(View.VISIBLE);
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

                                swipeRefreshLayout.setRefreshing(false);

                                if (baseResponse.getStatus().equals(SUCCESS)) {

                                    LoginAccessTokenInfo loginAccessTokenInfo = (LoginAccessTokenInfo) baseResponse.getData();
                                    PreferencesManager.keepAccessToken(getActivity(), loginAccessTokenInfo.getAccessToken());
                                    PreferencesManager.keepRefreshToken(getActivity(), loginAccessTokenInfo.getRefreshToken());
                                    replaceFragment(new EventNewsTabFragment());
                                } else {

                                    newsNotFoundView.setVisibility(View.VISIBLE);
                                }

                            } else {

                                swipeRefreshLayout.setRefreshing(false);
                                newsNotFoundView.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<LoginAccessTokenInfo>> call, Throwable t) {

                            swipeRefreshLayout.setRefreshing(false);
                            newsNotFoundView.setVisibility(View.VISIBLE);
                        }
                    });

                } else {

                    swipeRefreshLayout.setRefreshing(false);
                    newsNotFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<EventsNewsInfoResBean>>> call, Throwable t) {

                swipeRefreshLayout.setRefreshing(false);
                newsNotFoundView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void changeLanguageTitle(String lang) {
        PreferencesManager.setCurrentLanguage(getContext(), lang);
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new EventNewsTabFragment());
        }
    }

    @Override
    public void clickMenuBarBackBtn() {
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new NewMainPageFragment());
        }
    }
}
