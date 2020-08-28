package mm.com.aeon.vcsaeon.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CustAgreementListDto;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.MembershipInfoDelegate;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.QR_INFO_SHOW;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.getUnderlineText;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.getWelcomeLetterLink;

public class CardInfoOneQRListAdapter extends RecyclerView.Adapter {

    private List<CustAgreementListDto> customerAgreementListDtoList;
    private Context context;
    private MembershipInfoDelegate delegate;
    private UserInformationFormBean userInformationFormBean;

    private static final int VIEW_TYPE_INFO = 1;
    private static final int VIEW_TYPE_WARN = 2;

    public CardInfoOneQRListAdapter(List<CustAgreementListDto> custAgreementListDtoList, Context context,
                                    MembershipInfoDelegate delegate) {
        this.customerAgreementListDtoList = custAgreementListDtoList;
        this.context = context;
        this.delegate = delegate;

        final String userInfoBeanJson = PreferencesManager.getCurrentUserInfo(context);
        this.userInformationFormBean = new Gson().fromJson(userInfoBeanJson, UserInformationFormBean.class);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_card_info_one, parent, false);
            return new QRListItemViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_card_info_two, parent, false);
        return new CardOneWarningHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustAgreementListDto custAgreementListDto = customerAgreementListDtoList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_INFO) {
            ((QRListItemViewHolder) holder).bindView(custAgreementListDto);
        } else {
            ((CardOneWarningHolder) holder).bindView(custAgreementListDto);
        }
    }

    @Override
    public int getItemViewType(int position) {
        CustAgreementListDto dto = customerAgreementListDtoList.get(position);
        if (dto.isWarningText()) {
            return VIEW_TYPE_WARN;
        }
        return VIEW_TYPE_INFO;
    }

    @Override
    public int getItemCount() {
        return customerAgreementListDtoList.size();
    }

    private class QRListItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleAgreementNo;
        TextView lblAgreementNo;
        TextView titleQRCode;
        ImageView imgQrCode;
        TextView titleDate;
        TextView lblDate;

        RelativeLayout layoutLoanInfo;

        String finAmtUnit;
        String finTermUnit;

        public QRListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleAgreementNo = itemView.findViewById(R.id.title_agreement_no);
            titleQRCode = itemView.findViewById(R.id.title_qr_code);
            titleDate = itemView.findViewById(R.id.title_date);
            lblAgreementNo = itemView.findViewById(R.id.lbl_agreement_no);
            imgQrCode = itemView.findViewById(R.id.img_qr_code);
            lblDate = itemView.findViewById(R.id.lbl_last_payment_day);
            layoutLoanInfo = itemView.findViewById(R.id.layout_loan_info);
        }

        void bindView(final CustAgreementListDto custAgreementListDto) {

            String curLang = PreferencesManager.getCurrentLanguage(itemView.getContext());
            titleDate.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.last_received_day, itemView.getContext()));
            finAmtUnit = CommonUtils.getLocaleString(new Locale(curLang), R.string.mem_card_amt_unit, itemView.getContext());
            finTermUnit = CommonUtils.getLocaleString(new Locale(curLang), R.string.mem_card_fin_term, itemView.getContext());

            final int API_SHOW_STATUS = custAgreementListDto.getQrShow();

            layoutLoanInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(getWelcomeLetterLink(custAgreementListDto.getAgreementNo())));
                    context.startActivity(intent);
                }
            });

            lblAgreementNo.setText(Html.fromHtml(getUnderlineText(custAgreementListDto.getAgreementNo())));

            if (custAgreementListDto.getLastPaymentDate() != null) {
                lblDate.setText(CommonUtils.dateToString(custAgreementListDto.getLastPaymentDate()));
            }

            if (API_SHOW_STATUS == QR_INFO_SHOW) {

                try {

                    final int daApplicationInfoId = custAgreementListDto.getDaApplicationInfoId();
                    final String qrCodeStr = custAgreementListDto.getEncodeStringForQr();

                    if (qrCodeStr != null) {
                        Bitmap bitmap = textToImage(qrCodeStr, 640, 640);
                        imgQrCode.setImageBitmap(bitmap);
                        imgQrCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delegate.onTouchQRCode(daApplicationInfoId);
                            }
                        });
                        enableQrCode();
                    } else {
                        disableQrCode();
                    }

                } catch (Exception e) {
                    disableQrCode();
                }

            } else {
                disableQrCode();
            }
        }

        private void enableQrCode() {
            imgQrCode.setVisibility(View.VISIBLE);
            titleQRCode.setVisibility(View.VISIBLE);
        }

        private void disableQrCode() {
            imgQrCode.setVisibility(View.GONE);
            titleQRCode.setVisibility(View.GONE);
        }

        //generate qr-code from string.
        private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
            BitMatrix bitMatrix;
            try {
                bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
                        width, height, null);
            } catch (IllegalArgumentException Illegalargumentexception) {
                return null;
            }
            int bitMatrixWidth = bitMatrix.getWidth();
            int bitMatrixHeight = bitMatrix.getHeight();
            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
            int colorWhite = 0xFFFFFFFF;
            int colorBlack = 0xFF000000;
            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;
                for (int x = 0; x < bitMatrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
            bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
            return bitmap;
        }
    }

    private class CardOneWarningHolder extends RecyclerView.ViewHolder {
        TextView warningMessage;

        CardOneWarningHolder(View itemView) {
            super(itemView);
            warningMessage = itemView.findViewById(R.id.warn_text);
        }

        void bindView(final CustAgreementListDto dto) {
            String curLang = PreferencesManager.getCurrentLanguage(itemView.getContext());
            warningMessage.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.received_day_warning_text, itemView.getContext()));
        }
    }

}
