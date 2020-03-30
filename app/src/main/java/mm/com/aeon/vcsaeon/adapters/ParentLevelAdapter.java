package mm.com.aeon.vcsaeon.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.res.ResourcesCompat;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.TempFAQInfo;
import mm.com.aeon.vcsaeon.beans.TempFAQInfoResDto;
import mm.com.aeon.vcsaeon.views.customviews.CustomExpListView;

public class ParentLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private final List<String> mListDataHeader;
    private final Map<String, List<String>> mListData_SecondLevel_Map;
    private final Map<String, List<String>> mListData_ThirdLevel_Map;
    private final int mScreenWidth;

    public ParentLevelAdapter(Context mContext, List<String> mListDataHeader, List<TempFAQInfo> tempFAQInfoList, int mScreenWidth) {
        this.mContext = mContext;
        this.mListDataHeader = new ArrayList<>();
        this.mListDataHeader.addAll(mListDataHeader);
        this.mScreenWidth=mScreenWidth;

        // Init second level data
        String[] mItemHeaders=null;
        mListData_SecondLevel_Map = new HashMap<>();
        int parentCount = mListDataHeader.size();

        for (int i = 0; i < parentCount; i++) {  //Category.
            String content = mListDataHeader.get(i);
            for (TempFAQInfo tempFAQInfo : tempFAQInfoList) {
                String category = tempFAQInfo.getFaqCategory();
                List<TempFAQInfoResDto> faqInfoResDtoList = tempFAQInfo.getFaqInfoResInfoList();
                String[] questionList = new String[faqInfoResDtoList.size()];
                String[] answerList = new String[faqInfoResDtoList.size()];
                int in=0;
                for (TempFAQInfoResDto tempFAQInfoResDto :faqInfoResDtoList) {
                    questionList[in]=tempFAQInfoResDto.getQuestion();
                    answerList[in]=tempFAQInfoResDto.getAnswer();
                    in++;
                }
                if(content.equals(category)){
                    mItemHeaders = questionList;
                    break;
                }
            }
            mListData_SecondLevel_Map.put(mListDataHeader.get(i), Arrays.asList(mItemHeaders));
        }

        // THIRD LEVEL
        String[] mItemChildOfChild;
        List<String> listChild=null;
        mListData_ThirdLevel_Map = new HashMap<>();
        for (Object o : mListData_SecondLevel_Map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object object = entry.getValue();
            if (object instanceof List) {
                List<String> stringList = new ArrayList<>();
                Collections.addAll(stringList, (String[]) ((List) object).toArray());
                for (int i = 0; i < stringList.size(); i++) {
                    for (TempFAQInfo tempFAQInfo : tempFAQInfoList) {
                        List<TempFAQInfoResDto> c_tempFaqInfoResDtoList = tempFAQInfo.getFaqInfoResInfoList();
                        int ix=0;
                        for (TempFAQInfoResDto tempFAQInfoResDto : c_tempFaqInfoResDtoList) {
                            if(stringList.get(i).equals(tempFAQInfoResDto.getQuestion())){ //Answer
                                String[] childOfChildAns = {tempFAQInfoResDto.getAnswer()};
                                mItemChildOfChild = childOfChildAns;
                                listChild = Arrays.asList(mItemChildOfChild);
                            }
                        }
                    }
                    mListData_ThirdLevel_Map.put(stringList.get(i), listChild);
                }
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        String parentNode = (String) getGroup(groupPosition);
        secondLevelExpListView.setAdapter(new SecondLevelAdapter(this.mContext, mListData_SecondLevel_Map.get(parentNode), mListData_ThirdLevel_Map,mScreenWidth));
        secondLevelExpListView.setGroupIndicator(null);
        return secondLevelExpListView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_group, parent, false);
        }
        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(ResourcesCompat.getFont(mContext, R.font.pyidaungsu_bold), Typeface.BOLD);
        lblListHeader.setTextColor(Color.rgb(53,55,158));
        lblListHeader.setText(headerTitle);
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
