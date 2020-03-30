package mm.com.aeon.vcsaeon.delegates;

import android.view.View;

import mm.com.aeon.vcsaeon.beans.ApplicationAttachInfoEditResBean;
import mm.com.aeon.vcsaeon.views.customviews.SendMessageImageView;

public interface DAEnquiryAttachEditDelegate {
    void onEditAttach(ApplicationAttachInfoEditResBean purchaseAttachInfo, View itemView, SendMessageImageView imgAttach, int position);
}
