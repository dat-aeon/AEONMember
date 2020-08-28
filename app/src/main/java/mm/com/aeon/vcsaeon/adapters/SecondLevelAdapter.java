package mm.com.aeon.vcsaeon.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import androidx.core.content.res.ResourcesCompat;

import mm.com.aeon.vcsaeon.R;

public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final List<String> mListDataHeader;
    private final Map<String, List<String>> mListDataChild;
    private final int mScreenWidth;

    public SecondLevelAdapter(Context mContext, List<String> mListDataHeader, Map<String, List<String>> mListDataChild, int mScreenWidth) {
        this.mContext = mContext;
        this.mListDataHeader = mListDataHeader;
        this.mListDataChild = mListDataChild;
        this.mScreenWidth = mScreenWidth;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);
        }
        TextView txtListChild = convertView.findViewById(R.id.lblListItem);
        txtListChild.setTypeface(ResourcesCompat.getFont(mContext, R.font.pyidaungsu_bold), Typeface.NORMAL);
        txtListChild.setSingleLine(false);
        txtListChild.setTextColor(mContext.getColor(R.color.black));
        txtListChild.setHorizontallyScrolling(false);
        txtListChild.setPadding(96, 8, 100, 0);
        txtListChild.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        txtListChild.setText(childText);
        txtListChild.setWidth(mScreenWidth);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        } catch (Exception e) {
            return 0;
        }
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
            convertView = layoutInflater.inflate(R.layout.drawer_list_group_second, parent, false);
        }

        parent.setNestedScrollingEnabled(true);

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(ResourcesCompat.getFont(mContext, R.font.pyidaungsu_bold), Typeface.BOLD);
        lblListHeader.setWidth(mScreenWidth);
        lblListHeader.setText(headerTitle);
        lblListHeader.setSingleLine(false);
        lblListHeader.setHorizontallyScrolling(false);
        lblListHeader.setPadding(10, 8, 100, 0);
        lblListHeader.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        lblListHeader.setTextColor(mContext.getColor(R.color.black));
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
