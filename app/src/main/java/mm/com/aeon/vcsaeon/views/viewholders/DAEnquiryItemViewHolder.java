package mm.com.aeon.vcsaeon.views.viewholders;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.DAEnquiryDelegate;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_APPROVE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_CANCEL;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_COMPLETE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_ON_PROCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STATUS_UNSUCESSFUL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.dateToString;

public class DAEnquiryItemViewHolder extends RecyclerView.ViewHolder {

    private DAEnquiryDelegate delegate;

    Button btnStatus;
    Button btnPurchaseDetail;
    Button btnEditAttach;
    Button btnCancel;

    AppCompatTextView labelApplicationNo;
    AppCompatTextView labelAgreementNo;
    AppCompatTextView labelApplyDate;
    AppCompatTextView labelLoanAmt;
    AppCompatTextView labelApproveAmt;
    AppCompatTextView labelLoanTerm;
    AppCompatTextView labelApproveTerm;

    AppCompatTextView textApplicationNo;
    AppCompatTextView textAgreementNo;
    AppCompatTextView textApplyDate;
    AppCompatTextView textLoanAmt;
    AppCompatTextView textApproveAmt;
    AppCompatTextView textLoanTerm;
    AppCompatTextView textApproveTerm;

    boolean success = false;
    TextView textViewDetail;
    ImageView textStatusCount;

    private String curLang;

    public DAEnquiryItemViewHolder(@NonNull View itemView, DAEnquiryDelegate delegate) {
        super(itemView);

        this.delegate = delegate;
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(itemView.getContext());
        curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);

        btnStatus = itemView.findViewById(R.id.btn_enquiry_status);
        btnPurchaseDetail = itemView.findViewById(R.id.btn_purchase_detail);
        btnEditAttach = itemView.findViewById(R.id.btn_edit_attachment);
        btnCancel = itemView.findViewById(R.id.btn_purchase_cancel);
        textStatusCount=itemView.findViewById(R.id.status_count);

        labelApplicationNo=itemView.findViewById(R.id.lbl_aplication_number);
        labelAgreementNo=itemView.findViewById(R.id.lbl_agg_number);
        labelApplyDate = itemView.findViewById(R.id.lbl_apply_date);
        labelLoanAmt = itemView.findViewById(R.id.lbl_loan_amt);
        labelApproveAmt = itemView.findViewById(R.id.lbl_approve_amt);
        labelLoanTerm = itemView.findViewById(R.id.lbl_loan_term);
        labelApproveTerm = itemView.findViewById(R.id.lbl_approve_loan_term);


        textApplicationNo = itemView.findViewById(R.id.lbl_agg_no);
        textAgreementNo = itemView.findViewById(R.id.lbl_agg_no_value);
        textApplyDate = itemView.findViewById(R.id.lbl_apply_date_val);
        textLoanAmt = itemView.findViewById(R.id.lbl_loan_amt_val);
        textApproveAmt = itemView.findViewById(R.id.lbl_approve_amt_val);
        textLoanTerm = itemView.findViewById(R.id.lbl_loan_term_val);
        textApproveTerm = itemView.findViewById(R.id.lbl_approve_loan_term_val);
        textViewDetail = itemView.findViewById(R.id.lbl_view_detail);

        labelApplicationNo.setText(CommonUtils.getLocaleString(new Locale(curLang),R.string.lbl_application_no,itemView.getContext()));
        labelAgreementNo.setText(CommonUtils.getLocaleString(new Locale(curLang),R.string.lbl_agreement_no,itemView.getContext()));
        labelApplyDate.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_apply_date, itemView.getContext()));
        labelLoanAmt.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_loan_amount, itemView.getContext()));
        labelApproveAmt.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_approve_amount, itemView.getContext()));
        labelLoanTerm.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_loan_term, itemView.getContext()));
        labelApproveTerm.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_approve_term, itemView.getContext()));
        textViewDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_detail, itemView.getContext()));
        btnPurchaseDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_purchase_detail, itemView.getContext()));
        btnCancel.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_cancel, itemView.getContext()));
        btnEditAttach.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_attachment_edit, itemView.getContext()));
    }

    public void bindView(final ApplicationInfoResBean applicationInfo) {

        final int status = applicationInfo.getStatus();
        Log.e("Status:", String.valueOf(status));
        int statusCount=applicationInfo.getApplicationStatusChangedCount();
        Log.e("StatusCount:",String.valueOf(statusCount));
        if (statusCount==0){
            textStatusCount.setVisibility(View.GONE);
        }else {
            textStatusCount.setVisibility(View.VISIBLE);
        }

        switch (status) {

            case 2:
                setStatusNew();
                showApplicationCancel();
                break;
            case 3:
                setStatusIndex();
                showApplicationCancel();
                break;
            case 4:
                setStatusUploadFinished();
                showApplicationCancel();
                break;
            case 5:
                setStatusDocFuwaiting();
                showAttachmentEdit();
                showApplicationCancel();
                break;
            case 6:
                goneAttachmentEdit();
                setStatusDocFuAppUpdated();
                showApplicationCancel();
                break;
            case 7:
                goneAttachmentEdit();
                setStatusDocFuChecked();
                showApplicationCancel();
                break;
            case 8:
                goneCancelButton();
                setStatusCanceled();
                goneAttachmentEdit();
                gonePurchaseDetail();
                break;
            case 9:
                setStatusRejected();
                break;
            case 10:
                setStatusApproved();
                showApplicationCancel();
                break;

            case 11:
                setStatusModifyRequest();
                showApplicationCancel();
                break;
            case 12:
                setStatusModifyRequest();
                showApplicationCancel();
                break;

            case 13:
                setStatusPurchaseCancel();
                showApplicationCancel();
                break;
            case 14:
                setStatusPurchaseInitial();
                showApplicationCancel();
                break;
            case 15:
                setStatusPurchaseConfirmWaiting();
                showApplicationCancel();
                break;
            case 16:
                setStatusPurchaseConfirm();
                showApplicationCancel();
                break;
            case 17:
                goneCancelButton();
                setStatusPurchaseComplete();
                showPurchaseDetail();
                break;
            case 18:
                goneCancelButton();
                setStatusSaleClaimInfoList();
                showPurchaseDetail();
                break;
            case 19:
                goneCancelButton();
                setStatusAgentDocumentError();
                showPurchaseDetail();
                break;
            case 20:
                goneCancelButton();
                setStatusSaleEntryList();
                showPurchaseDetail();
                break;
        }

        curLang = PreferencesManager.getCurrentLanguage(itemView.getContext());
        Log.e("change list label to", curLang);

        labelApplicationNo.setText(CommonUtils.getLocaleString(new Locale(curLang),R.string.lbl_application_no,itemView.getContext()));
        labelAgreementNo.setText(CommonUtils.getLocaleString(new Locale(curLang),R.string.lbl_agreement_no,itemView.getContext()));
        labelApplyDate.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_apply_date, itemView.getContext()));
        labelLoanAmt.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_loan_amount, itemView.getContext()));
        labelApproveAmt.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_approve_amount, itemView.getContext()));
        labelLoanTerm.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_loan_term, itemView.getContext()));
        labelApproveTerm.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_approve_term, itemView.getContext()));
        textViewDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.lbl_detail, itemView.getContext()));
        btnPurchaseDetail.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_purchase_detail, itemView.getContext()));
        btnCancel.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_cancel, itemView.getContext()));
        btnEditAttach.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.btn_attachment_edit, itemView.getContext()));

        String agreementNo=applicationInfo.getAgreementNo();
        String applicationNo = String.valueOf(applicationInfo.getApplicationNo());
        String ymNo=applicationNo.substring(0,4);
        String middleNo=applicationNo.substring(4,7);
        String lastNo=applicationNo.substring(7,10);

        textApplicationNo.setText(" : "+ymNo+"-"+middleNo+"-"+lastNo);
        textLoanTerm.setText(" : " + applicationInfo.getFinanceTerm() + " Months");
        textApproveTerm.setText(getApproveTerm(applicationInfo.getApprovedFinanceTerm()));
        if (agreementNo!=null){
            textAgreementNo.setText(" : " +agreementNo);
        }else {
            textAgreementNo.setText(" : -");
        }


        String appliedDate = dateToString(applicationInfo.getAppliedDate());
        if (appliedDate != null) {
            textApplyDate.setText(" : " + appliedDate);
        }

        DecimalFormat df = new DecimalFormat("###,###");
        textLoanAmt.setText(" : " + df.format(applicationInfo.getFinanceAmount()) + " MMK");
        textApproveAmt.setText(" : " + df.format(applicationInfo.getApprovedFinanceAmount()) + " MMK");

        textViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onViewApplicationDetail(applicationInfo.getDaApplicationInfoId());
            }
        });

        btnPurchaseDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onViewPurchaseDetail(applicationInfo.getDaApplicationInfoId());
            }
        });

        btnEditAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onEditAttachments(applicationInfo.getDaApplicationInfoId());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.onCancel(applicationInfo.getDaApplicationInfoId(),curLang);
            }
        });

    }

    void showPurchaseDetail() {
        btnPurchaseDetail.setVisibility(View.VISIBLE);
    }

    void gonePurchaseDetail() {
        btnPurchaseDetail.setVisibility(View.GONE);
    }

    void showAttachmentEdit() {
        btnEditAttach.setVisibility(View.VISIBLE);
    }

    void goneAttachmentEdit() {
        btnEditAttach.setVisibility(View.GONE);
    }

    void showApplicationCancel() {
        btnCancel.setVisibility(View.VISIBLE);
    }

    void setStatusRejected() {
        btnStatus.setText(STATUS_UNSUCESSFUL);
    }

    void setStatusCanceled() {
        btnStatus.setText(STATUS_CANCEL);
    }

    void goneCancelButton() {
        btnCancel.setVisibility(View.GONE);
    }

    void setStatusNew() {
        btnStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusIndex() {
        btnStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusUploadFinished() {
        btnStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusDocFuwaiting() {
        btnStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusDocFuAppUpdated() {
        btnStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusDocFuChecked() {
        btnStatus.setText(STATUS_ON_PROCESS);
    }

    void setStatusApproved() {
        btnStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseCancel() {
        btnStatus.setText(STATUS_APPROVE);
    }

    void setStatusModifyRequest(){
        btnStatus.setText(STATUS_APPROVE);
    }

    void setStatusModifyUpload(){
        btnStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseInitial() {
        btnStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseConfirmWaiting() {
        btnStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseConfirm() {
        btnStatus.setText(STATUS_APPROVE);
    }

    void setStatusPurchaseComplete() {
        btnStatus.setText(STATUS_COMPLETE);
    }

    void setStatusSettlementUploadFinished() {
        btnStatus.setText(STATUS_COMPLETE);
    }

    void setStatusSettlementPending() {
        btnStatus.setText(STATUS_COMPLETE);
    }

    void setStatusSaleClaimInfoList() {
        btnStatus.setText(STATUS_COMPLETE);
    }

    void setStatusAgentDocumentError() {
        btnStatus.setText(STATUS_COMPLETE);
    }

    void setStatusSaleEntryList() {
        btnStatus.setText(STATUS_COMPLETE);
    }

    String getApproveTerm(int approveTerm){
        if(approveTerm == 0){
            return " : -";
        }
        return " : " + approveTerm + " Months";
    }

}
