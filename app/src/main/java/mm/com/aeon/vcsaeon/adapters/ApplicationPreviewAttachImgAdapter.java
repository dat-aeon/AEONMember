package mm.com.aeon.vcsaeon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoPhotoBean;
import mm.com.aeon.vcsaeon.delegates.ApplicationDetailAttachDelegate;
import mm.com.aeon.vcsaeon.views.viewholders.ApplicationPreviewAttachViewHolder;

public class ApplicationPreviewAttachImgAdapter extends RecyclerView.Adapter {

    List<ApplicationInfoPhotoBean> applicationttachmentBeanList;
    ApplicationDetailAttachDelegate delegate;

    public ApplicationPreviewAttachImgAdapter(List<ApplicationInfoPhotoBean> applicationttachmentBeanList,
                                              ApplicationDetailAttachDelegate delegate) {
        this.applicationttachmentBeanList = applicationttachmentBeanList;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_detail_attachment, parent, false);
        return new ApplicationPreviewAttachViewHolder(view, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApplicationInfoPhotoBean applicationInfoPhotoBean
                = applicationttachmentBeanList.get(position);
        ((ApplicationPreviewAttachViewHolder) holder).bindView(applicationInfoPhotoBean);
    }

    @Override
    public int getItemCount() {
        return applicationttachmentBeanList.size();
    }

}
