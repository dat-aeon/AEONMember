package mm.com.aeon.vcsaeon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ApplicationAttachInfoEditResBean;
import mm.com.aeon.vcsaeon.delegates.DAEnquiryAttachEditDelegate;
import mm.com.aeon.vcsaeon.views.viewholders.DAEditAttachViewHolder;

public class DAEnquiryAttachEditRVAdapter extends RecyclerView.Adapter {

    List<ApplicationAttachInfoEditResBean> purchaseAttachInfoEditResBeanList;
    DAEnquiryAttachEditDelegate delegate;

    public DAEnquiryAttachEditRVAdapter(List<ApplicationAttachInfoEditResBean> purchaseAttachInfoEditResBeanList,
                                        DAEnquiryAttachEditDelegate delegate) {
        this.purchaseAttachInfoEditResBeanList = purchaseAttachInfoEditResBeanList;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_attach_edit_layout, parent, false);
        return new DAEditAttachViewHolder(view, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApplicationAttachInfoEditResBean purchaseAttachInfoEdit = purchaseAttachInfoEditResBeanList.get(position);
        ((DAEditAttachViewHolder)holder).bindView(purchaseAttachInfoEdit, position);
    }

    @Override
    public int getItemCount() {
        return purchaseAttachInfoEditResBeanList.size();
    }
}
