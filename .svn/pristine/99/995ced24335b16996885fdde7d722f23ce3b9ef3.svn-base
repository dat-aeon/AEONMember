package mm.com.aeon.vcsaeon.views.viewholders;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ApplicationAttachInfoEditResBean;
import mm.com.aeon.vcsaeon.delegates.DAEnquiryAttachEditDelegate;
import mm.com.aeon.vcsaeon.views.customviews.SendMessageImageView;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.APPLICATION_ATTACHMENT_TYPES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ATTACHMENT_TYPES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_APPLICANT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_APPLICANT_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_CRIMINAL_CLEARANCE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_GUARANTOR_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_INCOME_PROOF;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_BACK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_FRONT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_GUARANTOR_BACK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_GUARANTOR_FRONT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_RESIDENT_PROOF;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_AGREEMENT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_INVOICE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_MEMBER_CARD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.TYPE_OTHERS;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.APP_ATTACHMENT_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.ATTACHMENT_URL;

public class DAEditAttachViewHolder extends RecyclerView.ViewHolder {

    TextView lblTitle;
    SendMessageImageView imgEditAttach;
    TextView textEdit;

    DAEnquiryAttachEditDelegate delegate;

    public DAEditAttachViewHolder(@NonNull View itemView, DAEnquiryAttachEditDelegate delegate) {
        super(itemView);
        lblTitle = itemView.findViewById(R.id.lbl_attach_edit_title);
        imgEditAttach = itemView.findViewById(R.id.img_attach_edit);
        textEdit = itemView.findViewById(R.id.do_attach_edit);

        this.delegate = delegate;
    }

    public void bindView(final ApplicationAttachInfoEditResBean purchaseAttachInfo, final int position) {

        int attachType = purchaseAttachInfo.getFileType();

        if (attachType != 0) {

            switch (attachType) {

                case PHOTO_NRC_FRONT:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_FRONT]);
                    break;

                case PHOTO_NRC_BACK:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_BACK]);
                    break;

                case PHOTO_INCOME_PROOF:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_INCOME_PROOF]);
                    break;

                case PHOTO_RESIDENT_PROOF:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_RESIDENT_PROOF]);
                    break;

                case PHOTO_NRC_GUARANTOR_FRONT:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_GUARANTOR_FRONT]);
                    break;

                case PHOTO_NRC_GUARANTOR_BACK:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_GUARANTOR_BACK]);
                    break;

                case PHOTO_CRIMINAL_CLEARANCE:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_CRIMINAL_CLEARANCE]);
                    break;

                case PHOTO_APPLICANT:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_APPLICANT]);
                    break;

                case PHOTO_APPLICANT_SIGNATURE:
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_APPLICANT_SIGNATURE]);
                    break;

                case PHOTO_GUARANTOR_SIGNATURE :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_GUARANTOR_SIGNATURE-1]);
                    break;
            }
        }

        boolean isEditable = purchaseAttachInfo.isEditFlag();
        if (isEditable) {
            textEdit.setVisibility(View.VISIBLE);
            textEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delegate.onEditAttach(purchaseAttachInfo, textEdit, imgEditAttach , position);
                }
            });
        } else {
            textEdit.setVisibility(View.GONE);
        }

        String imgPath;
        if (purchaseAttachInfo.isEdited()){
            imgPath = purchaseAttachInfo.getFilePath();
        } else {
            imgPath = APP_ATTACHMENT_URL + purchaseAttachInfo.getFilePath();
        }

        Glide.with(itemView)
                .load(imgPath)
                .placeholder(R.drawable.noimage)
                .into(imgEditAttach);

    }

}
