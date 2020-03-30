package mm.com.aeon.vcsaeon.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;

public class MemberCardTwoTabFragment extends BaseFragment {

    View view;

    ImageView imageView;

    UserInformationFormBean userInformationFormBean;

    TextView cardTwoText;
    TextView memberNumber;
    private String curLang;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.member_card_two_tab, container, false);
        //setHasOptionsMenu(true);

        cardTwoText = view.findViewById(R.id.card_two_text);
        memberNumber = view.findViewById(R.id.text_member_no);

        sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences,PARAM_LANG);
        if(curLang.equals(LANG_MM)){
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        imageView = view.findViewById(R.id.mem_card_two);

        final String imagePath = userInformationFormBean.getPhotoPath();

        ImageView imageView2=view.findViewById(R.id.animate_img2);
        Glide.with(getActivity()).load(R.drawable.member_card_bg_animate).into(imageView2);

        if(imagePath==null || imagePath=="") {
            Picasso.get().load(R.drawable.no_profile_image).into(imageView);
        } else {
            Picasso.get().load(imagePath).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() { Picasso.get().load(imagePath).into(imageView); }
                @Override
                public void onError(Exception e) {
                    Picasso.get().load(R.drawable.no_profile_image).into(imageView);
                }
            });
        }

        if(userInformationFormBean.getMemberNo()!=null && userInformationFormBean.isMemberNoValid()){
            memberNumber.setVisibility(View.VISIBLE);
            memberNumber.setText(getFormattedMemberNo(userInformationFormBean.getMemberNo()));
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_favorite:
                //this.languageFlag = item;
                if(item.getTitle().equals(LANG_MM)){
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeLabel(LANG_MM);

                } else if(item.getTitle().equals(LANG_EN)){
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeLabel(LANG_EN);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLabel(String language){
        cardTwoText.setText(CommonUtils.getLocaleString(new Locale(language), R.string.membership_card2_photo_label, getActivity()));
        PreferencesManager.setCurrentLanguage(getContext(), language);
    }

    private String getFormattedMemberNo(String memberNo){
        try{
            if(memberNo.equals(BLANK) || memberNo.length()<5){
                return memberNo;
            } else {
                char[] chars = memberNo.toCharArray();
                List<String> tempList = new ArrayList<>();
                for(int i=0;i<chars.length;i++){
                    if((i!=0) && (i%4)==0){
                        tempList.add("-");
                    }
                    tempList.add(String.valueOf(chars[i]));
                }
                return tempList.toString().replaceAll("[\\[\\]]", BLANK).replaceAll(",", BLANK).replace(" ",BLANK);
            }
        } catch (Exception e){
            return memberNo;
        }
    }
}
