package mm.com.aeon.vcsaeon.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.MessageInfoBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CircleTransform;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.views.customviews.SendMessageImageView;
import mm.com.aeon.vcsaeon.fragments.MessagingTabFragment;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.decodeStringToBitmap;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.purifyMessage;
import static mm.com.aeon.vcsaeon.fragments.MessagingTabFragment.getMsgId;
import static mm.com.aeon.vcsaeon.fragments.MessagingTabFragment.mesgSocketClient;

public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MORE_MESSAGE = 3;
    private static final int VIEW_TYPE_IMAGE_SENT = 4;
    private static final int VIEW_TYPE_FREE_CHAT = 5;

    UserInformationFormBean userInformationFormBean;

    private Context mContext;
    private List<MessageInfoBean> mMessageList;
    private String currentRoom;
    private MessagingTabFragment messagingTabFragment;

    public MessageListAdapter(Context context, List<MessageInfoBean> messageList, Fragment fragment, String currentRoom) {
        mContext = context;
        mMessageList = messageList;
        messagingTabFragment = (MessagingTabFragment) fragment;
        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(context);
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);
        this.currentRoom = currentRoom;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    //Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {

        MessageInfoBean message = mMessageList.get(position);

        if (message.isButton()) {
            return VIEW_TYPE_MORE_MESSAGE;
        }

        if (message.isPhoto()) {
            return VIEW_TYPE_IMAGE_SENT;
        }

        if (message.isIntro()) {
            return VIEW_TYPE_FREE_CHAT;
        }

        if (message.isReceiveMesg()) {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            return VIEW_TYPE_MESSAGE_SENT;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_MORE_MESSAGE) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.more_message_button, viewGroup, false);
            return new MoreMessageHolder(view);
        }

        if (viewType == VIEW_TYPE_IMAGE_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.image_message_sent, viewGroup, false);
            return new SentImageHolder(view);
        }

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_sent, viewGroup, false);
            return new SentMessageHolder(view);
        }

        if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageHolder(view);
        }

        if (viewType == VIEW_TYPE_FREE_CHAT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_free_chat_introduction, viewGroup, false);
            return new FreeChatMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        MessageInfoBean message = mMessageList.get(position);

        switch (viewHolder.getItemViewType()) {

            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_MORE_MESSAGE:
                ((MoreMessageHolder) viewHolder).bind();
                break;
            case VIEW_TYPE_IMAGE_SENT:
                ((SentImageHolder) viewHolder).bind(message);
                break;
            case VIEW_TYPE_FREE_CHAT:
                ((FreeChatMessageHolder) viewHolder).bind(message);
                break;
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView sentMsgBody;
        TextView sentDate;

        SentMessageHolder(View itemView) {
            super(itemView);
            sentMsgBody = itemView.findViewById(R.id.text_message_body_send);
            sentDate = itemView.findViewById(R.id.text_message_time);
        }

        void bind(final MessageInfoBean message) {
            sentMsgBody.setLinksClickable(true);
            sentMsgBody.setMovementMethod(LinkMovementMethod.getInstance());
            String text = purifyMessage(message.getMessage());
            sentMsgBody.setText(Html.fromHtml(text));
            sentDate.setText(message.getSendTime());
        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder {

        SendMessageImageView sentImage;
        TextView sentDate;

        SentImageHolder(View itemView) {
            super(itemView);
            sentImage = itemView.findViewById(R.id.img_message_body);
            sentDate = itemView.findViewById(R.id.text_message_time);
        }

        void bind(MessageInfoBean message) {
            sentDate.setText(message.getSendTime());

            //Coupon Img.
            final String imagePath = message.getMessage();
            if (imagePath == null || imagePath == BLANK) {
                Picasso.get().load(R.drawable.noimage).into(sentImage);
            } else {

                Picasso.get().load(imagePath).into(sentImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        try {

                            //Adjust Image Orientation.
                            Picasso.get().load(imagePath).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();

                                    if (height > width) {
                                        sentImage.getLayoutParams().height = 532;
                                        sentImage.getLayoutParams().width = 400;
                                    } else if (height == width) {
                                        sentImage.getLayoutParams().height = 532;
                                        sentImage.getLayoutParams().width = 532;
                                    } else {
                                        sentImage.getLayoutParams().height = 400;
                                        sentImage.getLayoutParams().width = 532;
                                    }

                                    Picasso.get().load(imagePath).into(sentImage);

                                    //view image detail in click image.
                                    sentImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final Dialog dialog = new Dialog(mContext);
                                            dialog.setContentView(R.layout.detail_messaging_img_layout);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                            ImageView ivMsg = dialog.findViewById(R.id.iv_msg_img_detail);
                                            ImageView ivBack = dialog.findViewById(R.id.iv_back);
                                            ivBack.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            Picasso.get().load(imagePath).into(ivMsg);
                                            dialog.show();
                                        }
                                    });
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    //decodeStringToBitmap
                                    Log.d("TAG", "-------------- onBitmapFailed() ");
                                    Picasso.get().load(R.drawable.noimage).into(sentImage);
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    Log.d("TAG", "-------------- onPrepareLoad() ");
                                    Picasso.get().load(R.drawable.noimage).into(sentImage);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            Picasso.get().load(R.drawable.noimage).into(sentImage);
                            Log.d("TAG", "-------------- Exception() ");
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                        final Bitmap bitmap = decodeStringToBitmap(imagePath);

                        if (bitmap == null) {
                            Picasso.get().load(R.drawable.noimage).into(sentImage);
                        } else {

                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            if (height > width) {
                                sentImage.getLayoutParams().height = 532;
                                sentImage.getLayoutParams().width = 400;
                            } else if (height == width) {
                                sentImage.getLayoutParams().height = 532;
                                sentImage.getLayoutParams().width = 532;
                            } else {
                                sentImage.getLayoutParams().height = 400;
                                sentImage.getLayoutParams().width = 532;
                            }

                            sentImage.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        ImageView adminImg;
        TextView receivedMsgBody;
        TextView receivedDate;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            adminImg = itemView.findViewById(R.id.img_admin);
            receivedMsgBody = itemView.findViewById(R.id.text_message_body);
            receivedDate = itemView.findViewById(R.id.text_message_time);
        }

        void bind(final MessageInfoBean message) {

            receivedMsgBody.setLinksClickable(true);
            receivedMsgBody.setMovementMethod(LinkMovementMethod.getInstance());
            String text = purifyMessage(message.getMessage());
            receivedMsgBody.setText(Html.fromHtml(text));
            receivedDate.setText(message.getSendTime());
            Picasso.get().load(R.drawable.aeon_msg_logo).transform(new CircleTransform()).into(adminImg);
        }
    }

    private class MoreMessageHolder extends RecyclerView.ViewHolder {

        Button btnMoreMessage;
        //TextView textView;

        MoreMessageHolder(View itemView) {
            super(itemView);
            btnMoreMessage = itemView.findViewById(R.id.more_message_btn);

            btnMoreMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (CommonUtils.isNetworkAvailable(mContext)) {

                        int msgId = getMsgId();
                        Log.e("MessagingListAdapter", "send(mobile_old_msgid:" + msgId + "room:" + currentRoom);
                        mesgSocketClient.send("mobile_old_msgid:" + msgId + "room:" + currentRoom);

                    } else {
                        //Toast.makeText(mContext, R.string.network_connection_err, Toast.LENGTH_SHORT).show();
                        displayMessage(mContext, mContext.getString(R.string.network_connection_err));
                    }
                }
            });
        }

        void bind() {
            btnMoreMessage.setText("More Messages");
        }
    }

    private class FreeChatMessageHolder extends RecyclerView.ViewHolder {

        ImageView adminImg;
        TextView receivedMsgBody;

        FreeChatMessageHolder(View itemView) {
            super(itemView);
            adminImg = itemView.findViewById(R.id.img_admin);
            receivedMsgBody = itemView.findViewById(R.id.text_message_body);
        }

        void bind(final MessageInfoBean message) {

            receivedMsgBody.setLinksClickable(true);
            receivedMsgBody.setMovementMethod(LinkMovementMethod.getInstance());
            String text = purifyMessage(message.getMessage());
            receivedMsgBody.setText(Html.fromHtml(text));
            Picasso.get().load(R.drawable.aeon_msg_logo).transform(new CircleTransform()).into(adminImg);
        }
    }

}
