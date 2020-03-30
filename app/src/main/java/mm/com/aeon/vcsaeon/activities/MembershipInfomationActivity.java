package mm.com.aeon.vcsaeon.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CustAgreementListDto;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;

public class MembershipInfomationActivity extends BaseActivity {

    Toolbar toolbar;

    TextView menuBarLevelInfo;
    TextView menuBarDate;
    TextView menuBarPhoneNo;
    TextView menuBarName;

    TextView textMemberId;
    TextView textContractId;
    TextView textQRLabel;
    ImageView imageQRCode;
    ImageView imageMemberPhoto;

    UserInformationFormBean userInformationFormBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.membership_info_activity);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getApplicationContext());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(toolbar);

        menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarDate = toolbar.findViewById(R.id.menu_bar_date);
        menuBarPhoneNo = toolbar.findViewById(R.id.menu_bar_phone);
        menuBarName = toolbar.findViewById(R.id.menu_bar_name);

        menuBarDate.setText(CommonUtils.getCurTimeStringForLogout());
        menuBarLevelInfo.setText("Lv.3 : Member User");
        menuBarPhoneNo.setText(userInformationFormBean.getPhoneNo());
        menuBarName.setText(userInformationFormBean.getName());
        menuBarName.setVisibility(View.VISIBLE);

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        textMemberId = findViewById(R.id.lbl_member_id);
        textMemberId.setText("Member ID : " + getFormattedMemberNo(userInformationFormBean.getCustomerNo()));

        imageMemberPhoto = findViewById(R.id.member_photo);

        final String imagePath = userInformationFormBean.getPhotoPath();

        //ImageView imageView2 = findViewById(R.id.animate_img2);
        //Glide.with(this).load(R.drawable.member_card_bg_animate).into(imageView2);

        if (imagePath == null || imagePath == "") {
            Picasso.get().load(R.drawable.no_profile_image).into(imageMemberPhoto);
        } else {
            Picasso.get().load(imagePath).into(imageMemberPhoto, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    Picasso.get().load(imagePath).into(imageMemberPhoto);
                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(R.drawable.no_profile_image).into(imageMemberPhoto);
                }
            });
        }

        textContractId = findViewById(R.id.lbl_agreement_no);
        //textQRLabel = findViewById(R.id.lbl_qr_code);

        if (userInformationFormBean.getCustAgreementListDtoList() != null) {
            CustAgreementListDto custAgreementListDto = userInformationFormBean.getCustAgreementListDtoList().get(0);

            //textContractId.setText("Contract ID : " + custAgreementListDto.getCustAgreementId());
            String qrShow = String.valueOf(custAgreementListDto.getQrShow());

            if (qrShow.equals(CommonConstants.INFO_SHOW)) {
                imageQRCode.setVisibility(View.GONE);

            } else {
                try {
                    final int daApplicationInfoId = custAgreementListDto.getDaApplicationInfoId();
                    final String qrCodeStr = custAgreementListDto.getEncodeStringForQr();

                    if (qrCodeStr != null) {
                        Bitmap bitmap = textToImage(qrCodeStr, 480, 480);
                        imageQRCode.setImageBitmap(bitmap);
                        imageQRCode.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //delegate.onTouchQRCode(daApplicationInfoId);
                            }
                        });
                    } else {
                        imageQRCode.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

    private String getFormattedMemberNo(String memberNo) {
        try {
            if (memberNo.equals(BLANK) || memberNo.length() < 5) {
                return memberNo;
            } else {
                char[] chars = memberNo.toCharArray();
                List<String> tempList = new ArrayList<>();
                for (int i = 0; i < chars.length; i++) {
                    if ((i != 0) && (i % 4) == 0) {
                        tempList.add("-");
                    }
                    tempList.add(String.valueOf(chars[i]));
                }
                return tempList.toString().replaceAll("[\\[\\]]", BLANK).replaceAll(",", BLANK).replace(" ", BLANK);
            }
        } catch (Exception e) {
            return memberNo;
        }
    }
}
