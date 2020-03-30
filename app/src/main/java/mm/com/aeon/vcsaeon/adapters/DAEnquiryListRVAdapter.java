package mm.com.aeon.vcsaeon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoResBean;
import mm.com.aeon.vcsaeon.delegates.DAEnquiryDelegate;
import mm.com.aeon.vcsaeon.views.viewholders.DAEnquiryItemViewHolder;

public class DAEnquiryListRVAdapter extends RecyclerView.Adapter {

    private List<ApplicationInfoResBean> applicationInfoList;
    private DAEnquiryDelegate delegate;

    public DAEnquiryListRVAdapter(List<ApplicationInfoResBean> applicationInfoList, DAEnquiryDelegate delegate) {
        this.applicationInfoList = applicationInfoList;
        this.delegate = delegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_application_inquiry, parent, false);
        return new DAEnquiryItemViewHolder(view, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApplicationInfoResBean applicationInfo = applicationInfoList.get(position);
        ((DAEnquiryItemViewHolder) holder).bindView(applicationInfo);
    }

    @Override
    public int getItemCount() {
        return applicationInfoList.size();
    }

}
