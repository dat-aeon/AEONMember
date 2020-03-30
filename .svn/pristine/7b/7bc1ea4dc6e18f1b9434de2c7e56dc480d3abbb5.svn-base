package mm.com.aeon.vcsaeon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoAttachmentResBean;
import mm.com.aeon.vcsaeon.delegates.ApplicationDetailAttachDelegate;
import mm.com.aeon.vcsaeon.views.viewholders.ApplicationDetailAttachViewHolder;

public class ApplicationDetailAttachRVAdapter extends RecyclerView.Adapter {

    List<ApplicationInfoAttachmentResBean> applicationInfoAttachmentResBeanList;
    ApplicationDetailAttachDelegate delegate;

    public ApplicationDetailAttachRVAdapter(List<ApplicationInfoAttachmentResBean> applicationInfoAttachmentResBeanList,
                                            ApplicationDetailAttachDelegate delegate) {
        this.applicationInfoAttachmentResBeanList = applicationInfoAttachmentResBeanList;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_detail_attachment, parent, false);
        return new ApplicationDetailAttachViewHolder(view, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApplicationInfoAttachmentResBean applicationInfoAttachmentResBean
                = applicationInfoAttachmentResBeanList.get(position);
        ((ApplicationDetailAttachViewHolder) holder).bindView(applicationInfoAttachmentResBean);
    }

    @Override
    public int getItemCount() {
        return applicationInfoAttachmentResBeanList.size();
    }

}
