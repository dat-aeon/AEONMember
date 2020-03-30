package mm.com.aeon.vcsaeon.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.MainMenuItemBean;
import mm.com.aeon.vcsaeon.beans.MessageInfoBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CircleTransform;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.MenuNavigatorDelegate;
import mm.com.aeon.vcsaeon.fragments.MessagingTabFragment;
import mm.com.aeon.vcsaeon.views.customviews.SendMessageImageView;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.decodeStringToBitmap;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.purifyMessage;
import static mm.com.aeon.vcsaeon.fragments.MessagingTabFragment.getMsgId;
import static mm.com.aeon.vcsaeon.fragments.MessagingTabFragment.mesgSocketClient;

public class MenuListRecyclerAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MAIN_MENU = 1;
    private static final int VIEW_TYPE_MAIN_MENU_COUNT = 2;

    private Context mContext;
    private List<MainMenuItemBean> menuItemBeanList;
    //Delegate
    private MenuNavigatorDelegate menuNavigatorDelegate;

    public MenuListRecyclerAdapter(Context context, List<MainMenuItemBean> menuItemBeanList, MenuNavigatorDelegate delegate) {
        this.mContext = context;
        this.menuItemBeanList = menuItemBeanList;
        this.menuNavigatorDelegate = delegate;
    }

    @Override
    public int getItemCount() {
        return menuItemBeanList.size();
    }

    //Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {

        MainMenuItemBean menuItemBean = menuItemBeanList.get(position);

        if(menuItemBean.isHasMessageCount()){
            return VIEW_TYPE_MAIN_MENU_COUNT;
        } else {
            return VIEW_TYPE_MAIN_MENU;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;

        if(viewType==VIEW_TYPE_MAIN_MENU){
            view= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_header_menu,viewGroup,false);
            //view.setMinimumHeight(60);
            return new MenuViewHolder(view);
        }

        if(viewType==VIEW_TYPE_MAIN_MENU_COUNT){
            view= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_header_menu_with_count,viewGroup,false);
            //view.setMinimumHeight(60);
            return new MessageMenuViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        MainMenuItemBean menuItem = menuItemBeanList.get(position);

        switch (viewHolder.getItemViewType()){

            case VIEW_TYPE_MAIN_MENU:
                ((MenuViewHolder) viewHolder).bind(menuItem);
                break;
            case VIEW_TYPE_MAIN_MENU_COUNT:
                ((MessageMenuViewHolder) viewHolder).bind(menuItem);
                break;

        }
    }

    private class MessageMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iconImage;
        TextView menuName;
        TextView messageCount;
        LinearLayout headerMenu;

        MessageMenuViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconimage_withcount);
            menuName = itemView.findViewById(R.id.submenu_withcount);
            messageCount = itemView.findViewById(R.id.msg_count);
            headerMenu = itemView.findViewById(R.id.head_menu_withcount);

            menuName.setOnClickListener(this);
            iconImage.setOnClickListener(this);

            /*TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
            itemView.setLayoutParams(params);*/

        }

        void bind(final MainMenuItemBean menuItemBean) {
            iconImage.setImageResource(menuItemBean.getIconImage());
            iconImage.setTag(menuItemBean.getIndex());
            menuName.setText(menuItemBean.getMenuName());
            menuName.setTag(menuItemBean.getIndex());
            int count = menuItemBean.getMessageCount();

            if ( count > 0 && count <= 99){
                messageCount.setVisibility(View.VISIBLE);
                messageCount.setText(Integer.toString(menuItemBean.getMessageCount()));

            } else if (count > 99) {
                messageCount.setText("+99");
                messageCount.setVisibility(View.VISIBLE);

            } else {
                messageCount.setVisibility(View.GONE);
            }
            //Log.e("message menu index ", Integer.toString(menuItemBean.getIndex()));
            //Log.e("message menu count", Integer.toString(menuItemBean.getMessageCount()));

        }

        @Override
        public void onClick(View view) {
            //TextView textView = (TextView) view;
            //int index = (Integer)view.getTag();
            menuNavigatorDelegate.onClickMsgMenuItem(view);
        }
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private ImageView iconImage;
        private TextView textView_parentName;
        private LinearLayout linearLayout_childItems;

        MenuViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            iconImage = itemView.findViewById(R.id.iconimage);
            textView_parentName = itemView.findViewById(R.id.submenu);
            linearLayout_childItems = itemView.findViewById(R.id.ll_child_items);
            //linearLayout_childItems.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout_childItems.setVisibility(View.GONE);

            int intMaxNoOfChild = 0;
            for (int index = 0; index < menuItemBeanList.size(); index++) {
                int intMaxSizeTemp = menuItemBeanList.get(index).getSubMenuNameList().size();
                if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;
            }
            if (intMaxNoOfChild == 0){
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
                itemView.setLayoutParams(params);
            }
            for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                /*//icon
                ImageView iconView = new ImageView(context);
                iconView.setImageResource(R.drawable.apply_aeon);
                TableRow.LayoutParams params = new TableRow.LayoutParams(50, 50);
                iconView.setLayoutParams(params);
                iconView.setClickable(true);
                iconView.setOnClickListener(this);
                linearLayout_childItems.addView(iconView, layoutParams);
*/
                // menu name
                TextView textView = new TextView(context);
                textView.setId(indexView);
                textView.setPadding(150, 20, 0, 20);
                textView.setGravity(Gravity.LEFT);
                //textView.setBackground(ContextCompat.getDrawable(context, R.drawable.dialog_bg_primary));
                textView.setOnClickListener(this);
                linearLayout_childItems.addView(textView, layoutParams);
            }
            textView_parentName.setOnClickListener(this);
            iconImage.setClickable(true);
            iconImage.setOnClickListener(this);
        }

        void bind(final MainMenuItemBean menuItemBean) {
            iconImage.setImageResource(menuItemBean.getIconImage());
            iconImage.setTag(menuItemBean.getIndex());
            textView_parentName.setText(menuItemBean.getMenuName());
            textView_parentName.setTag(menuItemBean.getIndex());

            int noOfChildTextViews = linearLayout_childItems.getChildCount();
            for (int index = 0; index < noOfChildTextViews; index++) {
                TextView currentTextView = (TextView) linearLayout_childItems.getChildAt(index);
                currentTextView.setVisibility(View.VISIBLE);
            }

            int noOfChild = menuItemBean.getSubMenuNameList().size();
            if (noOfChild < noOfChildTextViews) {
                for (int index = noOfChild; index < noOfChildTextViews; index++) {
                    TextView currentTextView = (TextView) linearLayout_childItems.getChildAt(index);
                    currentTextView.setVisibility(View.GONE);
                }
            }
            for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {
                TextView currentTextView = (TextView) linearLayout_childItems.getChildAt(textViewIndex);
                currentTextView.setText(menuItemBean.getSubMenuNameList().get(textViewIndex).getSubMenuName());
                currentTextView.setTag(menuItemBean.getSubMenuNameList().get(textViewIndex).getIndex());
                currentTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menuNavigatorDelegate.onClickSubMenuItem(view);
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {
            menuNavigatorDelegate.onClickMenuItem(view , linearLayout_childItems);

            /*if (view.getId() == R.id.submenu) {
                if (linearLayout_childItems.getVisibility() == View.VISIBLE) {
                    linearLayout_childItems.setVisibility(View.GONE);
                } else {
                    linearLayout_childItems.setVisibility(View.VISIBLE);
                }
            } else {
                TextView textViewClicked = (TextView) view;
                Toast.makeText(context, "" + textViewClicked.getText().toString(), Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}
