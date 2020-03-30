package mm.com.aeon.vcsaeon.adapters;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.BuyMessagesBean;
import mm.com.aeon.vcsaeon.delegates.PurchaseEventDelegate;

public class ShoppingMessageListAdapter extends RecyclerView.Adapter {

    private final int VIEW_TYPE_INIT_MSG = 1;
    private final int VIEW_TYPE_BUY_SEND_MSG = 2;
    private final int VIEW_TYPE_BUY_RECEIVE_MSG = 3;
    private final int VIEW_TYPE_READ_MORE_MSG = 4;

    private PurchaseEventDelegate delegate;
    private List<BuyMessagesBean> buyMessagesBeanList;

    public ShoppingMessageListAdapter(List<BuyMessagesBean> buyMessagesBeanList, PurchaseEventDelegate delegate) {
        this.buyMessagesBeanList = buyMessagesBeanList;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {

            case VIEW_TYPE_INIT_MSG:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_buy_init, parent, false);
                return new InitMessageViewHolder(view);

            case VIEW_TYPE_BUY_SEND_MSG:
                View viewSend = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_send_buy_item, parent, false);
                return new BuySendViewHolder(viewSend);

            case VIEW_TYPE_BUY_RECEIVE_MSG:
                View viewReceive = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_receive_buy_item, parent, false);
                return new BuyReceiveViewHolder(viewReceive);

            case VIEW_TYPE_READ_MORE_MSG:
                View viewReadMore = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_message_button, parent, false);
                return new BuyMoreMessageHolder(viewReadMore);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BuyMessagesBean buyMessagesBean = buyMessagesBeanList.get(position);
        switch (holder.getItemViewType()) {

            case VIEW_TYPE_INIT_MSG:
                ((InitMessageViewHolder) holder).BindView(buyMessagesBean);
                break;

            case VIEW_TYPE_BUY_SEND_MSG:
                ((BuySendViewHolder) holder).BindView(buyMessagesBean);
                break;

            case VIEW_TYPE_BUY_RECEIVE_MSG:
                ((BuyReceiveViewHolder) holder).BindView(buyMessagesBean);
                break;

            case VIEW_TYPE_READ_MORE_MSG:
                ((BuyMoreMessageHolder) holder).onBindView(buyMessagesBean);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return buyMessagesBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return buyMessagesBeanList.get(position).getMessageType();
    }

    //Sender Message.
    private class BuySendViewHolder extends RecyclerView.ViewHolder {

        TextView text01;
        TextView text02;
        TextView text03;
        TextView textSentTime;

        public BuySendViewHolder(@NonNull View itemView) {
            super(itemView);
            text01 = itemView.findViewById(R.id.send_body_01);
            text02 = itemView.findViewById(R.id.send_body_02);
            text03 = itemView.findViewById(R.id.send_body_03);
            textSentTime = itemView.findViewById(R.id.buy_send_time);
        }

        public void BindView(BuyMessagesBean buyMessagesBean) {

            Resources res = itemView.getResources();

            text01.setText(res.getString(R.string.buy_send_01_format, buyMessagesBean.getBuySendMsgUIBean().getBrandName(),
                    buyMessagesBean.getBuySendMsgUIBean().getCategoryName()));
            text02.setText(res.getString(R.string.buy_send_02_format, buyMessagesBean.getBuySendMsgUIBean().getLocation()));
            text03.setText(res.getString(R.string.buy_send_03_format, buyMessagesBean.getBuySendMsgUIBean().getMessageBody()));

            textSentTime.setText(buyMessagesBean.getBuySendMsgUIBean().getSendTime());
        }
    }

    //Received Message.
    private class BuyReceiveViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAgentLogo;
        ImageView imgPhoneCall;

        TextView text01;
        TextView text02;
        TextView text03;
        TextView text04;
        TextView text05;
        TextView textReceivedTime;

        public BuyReceiveViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAgentLogo = itemView.findViewById(R.id.img_admin);
            imgPhoneCall = itemView.findViewById(R.id.ic_call);

            text01 = itemView.findViewById(R.id.received_body_01);
            text02 = itemView.findViewById(R.id.received_body_02);
            text03 = itemView.findViewById(R.id.received_body_03);
            text04 = itemView.findViewById(R.id.received_body_04);
            text05 = itemView.findViewById(R.id.received_body_05);
            textReceivedTime = itemView.findViewById(R.id.text_message_time);
        }

        public void BindView(final BuyMessagesBean buyMessagesBean) {

            final Resources res = itemView.getResources();
            final String url = buyMessagesBean.getBuyReceiveMsgUIBean().getUrlLink();
            final String phoneNo = buyMessagesBean.getBuyReceiveMsgUIBean().getPhoneNo();
            final int agentId = buyMessagesBean.getBuyReceiveMsgUIBean().getAgentId();
            final int messageId = buyMessagesBean.getBuyReceiveMsgUIBean().getMessageId();

            text01.setText(res.getString(R.string.buy_receive_01_format, buyMessagesBean.getBuyReceiveMsgUIBean().getAgentName(),
                    buyMessagesBean.getBuyReceiveMsgUIBean().getBrandName(), buyMessagesBean.getBuyReceiveMsgUIBean().getCategoryName()));
            text02.setText(res.getString(R.string.buy_receive_02_format, buyMessagesBean.getBuyReceiveMsgUIBean().getPrice()));
            text03.setText(buyMessagesBean.getBuyReceiveMsgUIBean().getMessageContent());
            text04.setText(res.getString(R.string.buy_receive_04_format, phoneNo));
            text05.setText(res.getString(R.string.buy_receive_05_format, url));
            textReceivedTime.setText(buyMessagesBean.getBuyReceiveMsgUIBean().getSendTime());
            Log.e("TAG", "bind agent id : " + agentId);

            imgPhoneCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.onTouchPhoneCall(agentId, phoneNo, messageId);
                }
            });

            if (url != null) {
                text05.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delegate.onClickUrlLink(url);
                    }
                });
            }
        }
    }

    //more message.
    private class BuyMoreMessageHolder extends RecyclerView.ViewHolder {

        Button btnMoreMessage;

        public BuyMoreMessageHolder(@NonNull View itemView) {
            super(itemView);
            btnMoreMessage = itemView.findViewById(R.id.more_message_btn);
        }

        public void onBindView(final BuyMessagesBean buyMessagesBean) {
            btnMoreMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.onTouchReadMore(buyMessagesBean.getBuyMoreMessageUIBean().getCurrentIndex(),
                            buyMessagesBean.getBuyMoreMessageUIBean().getEndingIndex());
                }
            });
        }

    }

    //Init Message Sown by AEON.
    private class InitMessageViewHolder extends RecyclerView.ViewHolder {

        TextView textMsgBody;
        TextView textSendTime;

        public InitMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMsgBody = itemView.findViewById(R.id.text_message_body);
            textSendTime = itemView.findViewById(R.id.text_message_time);
        }

        public void BindView(BuyMessagesBean buyMessagesBean) {
            textMsgBody.setText(buyMessagesBean.getBuyInitMsgUIBean().getMessageBody());
            textSendTime.setText(buyMessagesBean.getBuyInitMsgUIBean().getSendTime());
        }
    }

}
