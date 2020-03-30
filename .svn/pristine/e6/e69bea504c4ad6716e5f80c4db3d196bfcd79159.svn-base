package mm.com.aeon.vcsaeon.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.FAQInfo;
import mm.com.aeon.vcsaeon.beans.FAQInfoResDto;
import mm.com.aeon.vcsaeon.beans.TempFAQInfo;
import mm.com.aeon.vcsaeon.beans.TempFAQInfoResDto;
import mm.com.aeon.vcsaeon.adapters.ParentLevelAdapter;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class FAQTabFragment extends BaseFragment {

    View view;
    View serviceUnavailable;
    Toolbar toolbar;

    Service getFAQInfoService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.faq_info_tab, container, false);

        serviceUnavailable = view.findViewById(R.id.service_unavailable_faq_tab);
        serviceUnavailable.setVisibility(View.GONE);

        getFAQInfoService = APIClient.getUserService();

        Call<BaseResponse<List<FAQInfo>>> req = getFAQInfoService.getFAQInfo();

        final ProgressDialog getFaqDialog = new ProgressDialog(getActivity());
        getFaqDialog.setMessage("Loading ...");
        getFaqDialog.setCancelable(false);
        getFaqDialog.show();

        req.enqueue(new Callback<BaseResponse<List<FAQInfo>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<FAQInfo>>> call, Response<BaseResponse<List<FAQInfo>>> response) {

                if(response.isSuccessful()){

                    List<String> listDataHeader = new ArrayList<>();
                    BaseResponse baseResponse = response.body();

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        try{

                            List<FAQInfo> faqInfoList = (List<FAQInfo>)baseResponse.getData();

                            List<TempFAQInfo> tempFAQInfoList = new ArrayList<>();
                            List<TempFAQInfoResDto> tempFAQInfoResDtoList;

                            SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
                            String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
                            if(curLang.equals(LANG_MM)){
                                for (FAQInfo faqInfo : faqInfoList) {
                                    TempFAQInfo tempFAQInfo = new TempFAQInfo();
                                    tempFAQInfo.setFaqCategory(faqInfo.getFaqCategoryMyn());
                                    tempFAQInfoResDtoList = new ArrayList<>();
                                    for (FAQInfoResDto faqInfoResDto :faqInfo.getFaqInfoResDtoList()) {
                                        TempFAQInfoResDto tempFAQInfoResDto = new TempFAQInfoResDto();
                                        tempFAQInfoResDto.setQuestion(faqInfoResDto.getQuestionMM());
                                        tempFAQInfoResDto.setAnswer(faqInfoResDto.getAnswerMM());
                                        tempFAQInfoResDto.setCategoryId(faqInfoResDto.getCategoryId());
                                        tempFAQInfoResDto.setFaqId(faqInfoResDto.getFaqId());
                                        tempFAQInfoResDtoList.add(tempFAQInfoResDto);
                                    }
                                    tempFAQInfo.setFaqInfoResInfoList(tempFAQInfoResDtoList);
                                    tempFAQInfoList.add(tempFAQInfo);
                                }

                            } else {

                                for (FAQInfo faqInfo : faqInfoList) {
                                    TempFAQInfo tempFAQInfo = new TempFAQInfo();
                                    tempFAQInfo.setFaqCategory(faqInfo.getFaqCategoryEng());
                                    tempFAQInfoResDtoList = new ArrayList<>();
                                    for (FAQInfoResDto faqInfoResDto :faqInfo.getFaqInfoResDtoList()) {
                                        TempFAQInfoResDto tempFAQInfoResDto = new TempFAQInfoResDto();
                                        tempFAQInfoResDto.setQuestion(faqInfoResDto.getQuestionEN());
                                        tempFAQInfoResDto.setAnswer(faqInfoResDto.getAnswerEN());
                                        tempFAQInfoResDto.setCategoryId(faqInfoResDto.getCategoryId());
                                        tempFAQInfoResDto.setFaqId(faqInfoResDto.getFaqId());
                                        tempFAQInfoResDtoList.add(tempFAQInfoResDto);
                                    }
                                    tempFAQInfo.setFaqInfoResInfoList(tempFAQInfoResDtoList);
                                    tempFAQInfoList.add(tempFAQInfo);
                                }

                            }

                            String[] mItemHeaders = new String[tempFAQInfoList.size()];
                            int index=0;
                            for (TempFAQInfo tempFAQInfo : tempFAQInfoList) {
                                mItemHeaders[index] = tempFAQInfo.getFaqCategory();
                                index++;
                            }

                            Collections.addAll(listDataHeader, mItemHeaders); //set category
                            ExpandableListView mExpandableListView = view.findViewById(R.id.list_view);//getExpandableListView();
                            if (mExpandableListView != null) {
                                Display display = getActivity().getWindowManager(). getDefaultDisplay();
                                int mScreenWidth=display.getWidth();
                                ParentLevelAdapter parentLevelAdapter = new ParentLevelAdapter(getActivity(), listDataHeader, tempFAQInfoList,mScreenWidth);
                                mExpandableListView.setAdapter(parentLevelAdapter);
                            }

                            closeDialog(getFaqDialog);

                        } catch (Exception e){

                            closeDialog(getFaqDialog);
                            serviceUnavailable.setVisibility(View.VISIBLE);
                        }

                    } else {

                        closeDialog(getFaqDialog);
                        serviceUnavailable.setVisibility(View.VISIBLE);
                    }

                } else {

                    closeDialog(getFaqDialog);
                    serviceUnavailable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<FAQInfo>>> call, Throwable t) {

                closeDialog(getFaqDialog);
                serviceUnavailable.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
        transaction.commit();
    }
}
