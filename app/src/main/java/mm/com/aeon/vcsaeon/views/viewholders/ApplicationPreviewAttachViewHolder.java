package mm.com.aeon.vcsaeon.views.viewholders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoPhotoBean;
import mm.com.aeon.vcsaeon.delegates.ApplicationDetailAttachDelegate;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.APPLICATION_ATTACHMENT_TYPES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_APPLICANT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_APPLICANT_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_CRIMINAL_CLEARANCE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_GUARANTOR_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_INCOME_PROOF;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_INCOME_PROOF_PREVIEW;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_BACK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_FRONT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_GUARANTOR_BACK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_GUARANTOR_FRONT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_RESIDENT_PROOF;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_RESIDENT_PROOF_PREVIEW;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.APP_ATTACHMENT_URL;

public class ApplicationPreviewAttachViewHolder extends RecyclerView.ViewHolder {

    TextView lblTitle;
    ImageView imgAttach;

    ApplicationDetailAttachDelegate delegate;

    public ApplicationPreviewAttachViewHolder(@NonNull View itemView,
                                              ApplicationDetailAttachDelegate delegate) {
        super(itemView);
        lblTitle = itemView.findViewById(R.id.lbl_attach_detail_title);
        imgAttach = itemView.findViewById(R.id.img_attach_detail);

        this.delegate = delegate;
    }

    public void bindView(ApplicationInfoPhotoBean attachmentResBean){

        int attachType = attachmentResBean.getFileType();
        Log.e("file type", attachmentResBean.getFileType()+"");
        Log.e("file type", attachmentResBean.getFilePath()+"");

        Bitmap bitmap = BitmapFactory.decodeFile(attachmentResBean.getFilePath());
        imgAttach.setImageBitmap(bitmap);

        if(attachType != 0){

            switch (attachType){

                case PHOTO_NRC_FRONT :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_FRONT]);
                    break;

                case PHOTO_NRC_BACK :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_BACK]);
                    break;

                case PHOTO_INCOME_PROOF :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_INCOME_PROOF_PREVIEW]);
                    break;

                case PHOTO_RESIDENT_PROOF :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_RESIDENT_PROOF_PREVIEW]);
                    break;

                case PHOTO_NRC_GUARANTOR_FRONT :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_GUARANTOR_FRONT]);
                    break;

                case PHOTO_NRC_GUARANTOR_BACK :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_NRC_GUARANTOR_BACK]);
                    break;

                case PHOTO_CRIMINAL_CLEARANCE :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_CRIMINAL_CLEARANCE]);
                    break;

                case PHOTO_APPLICANT :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_APPLICANT]);
                    break;

                case PHOTO_APPLICANT_SIGNATURE :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_APPLICANT_SIGNATURE]);
                    break;

                case PHOTO_GUARANTOR_SIGNATURE :
                    lblTitle.setText(APPLICATION_ATTACHMENT_TYPES[PHOTO_GUARANTOR_SIGNATURE-1]);
                    break;
            }
        }

        /*final String fileUrl = APP_ATTACHMENT_URL + attachmentResBean.getFilePath();
        Glide.with(itemView)
                .load(fileUrl)
                .placeholder(R.drawable.noimage)
                .into(imgAttach);

        imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onTabAttach(fileUrl);
            }
        });*/

    }
}
