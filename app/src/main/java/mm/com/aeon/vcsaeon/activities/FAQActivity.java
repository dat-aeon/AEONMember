package mm.com.aeon.vcsaeon.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.FAQInfo;
import mm.com.aeon.vcsaeon.beans.FAQInfoResDto;
import mm.com.aeon.vcsaeon.beans.TempFAQInfo;
import mm.com.aeon.vcsaeon.beans.TempFAQInfoResDto;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.adapters.ParentLevelAdapter;
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
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;

public class FAQActivity extends BaseActivity {

    Toolbar toolbar;
    Service getFAQInfoService;

    TextView txtService;
    View serviceNotFoundView;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq);

        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        serviceNotFoundView = findViewById(R.id.service_unavailable_faq);
        serviceNotFoundView.setVisibility(View.GONE);
        txtService = serviceNotFoundView.findViewById(R.id.service_unavailable_label);

        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);
        menuBarName = toolbar.findViewById(R.id.menu_bar_name);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText("Lv.1 : Application User");
        String installPhone = PreferencesManager.getInstallPhoneNo(getApplicationContext()).trim();
        menuBarPhoneNo.setText(installPhone);
        menuBarName.setVisibility(View.GONE);

        final String mCurLang = PreferencesManager.getCurrentLanguage(getApplicationContext());
        if(mCurLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        getFAQInfoService = APIClient.getUserService();
        Call<BaseResponse<List<FAQInfo>>> req = getFAQInfoService.getFAQInfo();

        FAQActivity.this.setTheme(R.style.MessageDialogTheme);
        final ProgressDialog getFaqDialog = new ProgressDialog(FAQActivity.this);
        getFaqDialog.setMessage(getString(R.string.progress_loading));
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

                            SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(getApplicationContext());
                            String curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,"lang");
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

                            //ExpandableListView mExpandableListView = getExpandableListView(); ExpandableListActivity
                            ExpandableListView mExpandableListView = findViewById(R.id.list);

                            if (mExpandableListView != null) {
                                Display display = getWindowManager(). getDefaultDisplay();
                                int mScreenWidth=display.getWidth();
                                ParentLevelAdapter parentLevelAdapter = new ParentLevelAdapter(getApplicationContext(), listDataHeader, tempFAQInfoList,mScreenWidth);
                                mExpandableListView.setAdapter(parentLevelAdapter);
                            }

                            closeDialog(getFaqDialog);

                        } catch (Exception e){

                            closeDialog(getFaqDialog);
                            serviceNotFoundView.setVisibility(View.VISIBLE);
                        }

                    } else {

                        // display service_unavailable layout if not success.
                        closeDialog(getFaqDialog);
                        serviceNotFoundView.setVisibility(View.VISIBLE);
                    }

                } else {

                    // display service_unavailable layout if not success.
                    closeDialog(getFaqDialog);
                    serviceNotFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<FAQInfo>>> call, Throwable t) {
                // display service_unavailable layout if not success.
                closeDialog(getFaqDialog);
                serviceNotFoundView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.toolbar_main_menu, menu);

        final String curLang = PreferencesManager.getCurrentLanguage(getApplicationContext());

        if(curLang.equals(LANG_MM)){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
            menu.getItem(0).setTitle(LANG_EN);
            changeLabel(LANG_MM);
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
            menu.getItem(0).setTitle(LANG_MM);
            changeLabel(LANG_EN);
        }
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.mm_flag));
        menu.getItem(0).setTitle(LANG_MM);
        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.en_flag2));
        menu.getItem(1).setTitle(LANG_EN);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_myFlag) {
            /*if(item.getTitle().equals(LANG_MM)){
                item.setIcon(R.drawable.en_flag2);
                item.setTitle(LANG_EN);
                addValueToPreference(LANG_MM);
                recreate();
            } else if(item.getTitle().equals(LANG_EN)){
                item.setIcon(R.drawable.mm_flag);
                item.setTitle(LANG_MM);
                addValueToPreference(LANG_EN);
                recreate();
            }*/
            addValueToPreference(LANG_MM);
            recreate();
            return true;
        }
        if (id == R.id.action_engFlag) {
            addValueToPreference(LANG_EN);
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addValueToPreference(String lang){
        PreferencesManager.setCurrentLanguage(getApplicationContext(),lang);
    }

    public void changeLabel(String language){
        toolbar.setTitle(CommonUtils.getLocaleString(new Locale(language), R.string.faq_title, getApplicationContext()));
        txtService.setText(CommonUtils.getLocaleString(new Locale(language), R.string.service_unavailable, getApplicationContext()));
    }
}