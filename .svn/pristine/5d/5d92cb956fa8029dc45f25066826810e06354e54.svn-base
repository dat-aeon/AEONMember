package mm.com.aeon.vcsaeon.views.viewholders;

import android.view.View;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.PurchaseInfoAttachmentResBean;
import mm.com.aeon.vcsaeon.delegates.PurchaseOthersAttachDelegate;
import mm.com.aeon.vcsaeon.views.customviews.SendMessageImageView;

public class PurchaseDetailOtherAttachViewHolder extends RecyclerView.ViewHolder {

    SendMessageImageView imgAttachImg;
    private PurchaseOthersAttachDelegate delegate;

    public PurchaseDetailOtherAttachViewHolder(@NonNull View itemView, PurchaseOthersAttachDelegate delegate) {
        super(itemView);
        imgAttachImg = itemView.findViewById(R.id.img_others_attach);
        this.delegate = delegate;
    }

    public void bindView(final PurchaseInfoAttachmentResBean purchaseDetailAttachInfo){
        Glide.with(itemView)
                .load(purchaseDetailAttachInfo.getFilePath())
                .placeholder(R.drawable.noimage)
                .into(imgAttachImg);
        imgAttachImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAttachImg.getParent().requestDisallowInterceptTouchEvent(true);
                delegate.onTouchOthersAttach(purchaseDetailAttachInfo.getFilePath());
            }
        });
    }
}
