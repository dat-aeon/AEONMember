package mm.com.aeon.vcsaeon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.PurchaseInfoAttachmentResBean;
import mm.com.aeon.vcsaeon.delegates.PurchaseOthersAttachDelegate;
import mm.com.aeon.vcsaeon.views.viewholders.PurchaseDetailOtherAttachViewHolder;

public class PurchaseDetailOtherAttachRVAdapter extends RecyclerView.Adapter {

    private List<PurchaseInfoAttachmentResBean> purchaseInfoOtherAttachList;
    private PurchaseOthersAttachDelegate delegate;

    public PurchaseDetailOtherAttachRVAdapter(List<PurchaseInfoAttachmentResBean> purchaseInfoOtherAttachList,
                                              PurchaseOthersAttachDelegate delegate) {
        this.purchaseInfoOtherAttachList = purchaseInfoOtherAttachList;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_others_attachments, parent, false);
        return new PurchaseDetailOtherAttachViewHolder(view, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PurchaseInfoAttachmentResBean purchaseInfoAttachment = purchaseInfoOtherAttachList.get(position);
        ((PurchaseDetailOtherAttachViewHolder) holder).bindView(purchaseInfoAttachment);
    }

    @Override
    public int getItemCount() {
        return purchaseInfoOtherAttachList.size();
    }
}
