package mm.com.aeon.vcsaeon.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.CouponInfoResBean;
import mm.com.aeon.vcsaeon.beans.CouponUseInfoReqBean;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.INCORRECT_PASSWORD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.UNIT_PERCENT;
import static mm.com.aeon.vcsaeon.common_utils.CommonURL.COUPON_URL;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.getChangeTimestampToString2;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.closeDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showErrorDialog;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showWarningDialog;

public class CouponInfoListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_HISTORY_ROW = 1;
    private Context context;
    private List<CouponInfoResBean> couponInfoResBeanList;

    public CouponInfoListAdapter(Context context, List<CouponInfoResBean> couponInfoResBeanList) {
        this.context = context;
        this.couponInfoResBeanList = couponInfoResBeanList;
    }

    @Override
    public int getItemCount() {
        return couponInfoResBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_HISTORY_ROW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_template, parent, false);
        return new CouponInfoDataRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CouponInfoResBean couponInfoResBean = couponInfoResBeanList.get(position);
        ((CouponInfoDataRowHolder) viewHolder).bind(couponInfoResBean);
    }

    private class CouponInfoDataRowHolder extends RecyclerView.ViewHolder{

        TextView textSpecialEvent;
        ImageView imgCouponImg;
        TextView couponName;
        TextView goodsPrice;
        TextView goodsPriceUnit;

        TextView expiredDateLbl1;
        TextView expiredDate1;
        TextView couponPrice1;
        TextView couponPriceUnit1;

        LinearLayout layout2;
        TextView expiredDateLbl2;
        TextView expiredDate2;
        TextView couponPrice2;
        TextView couponPriceUnit2;

        TextView textDesc;
        LinearLayout linearLayout;
        ImageView usedImageView;
        LinearLayout layoutMiddle;

        CouponInfoDataRowHolder(final View itemView){
            super(itemView);
            textSpecialEvent = itemView.findViewById(R.id.text_special_event);
            imgCouponImg = itemView.findViewById(R.id.img_coupon);
            couponName = itemView.findViewById(R.id.coupon_name);
            goodsPrice = itemView.findViewById(R.id.txt_goods_price);
            goodsPriceUnit = itemView.findViewById(R.id.text_goods_price_unit);

            expiredDateLbl1 = itemView.findViewById(R.id.lbl_end_of_1);
            expiredDate1 = itemView.findViewById(R.id.expired_date_1);
            couponPrice1 = itemView.findViewById(R.id.coupon_amt_1);
            couponPriceUnit1 = itemView.findViewById(R.id.coupon_amt_unit_1);

            layout2 = itemView.findViewById(R.id.layout_2);
            expiredDateLbl2 = itemView.findViewById(R.id.lbl_end_of_2);
            expiredDate2 = itemView.findViewById(R.id.expired_date_2);
            couponPrice2 = itemView.findViewById(R.id.coupon_amt_2);
            couponPriceUnit2 = itemView.findViewById(R.id.coupon_amt_unit_2);

            textDesc = itemView.findViewById(R.id.text_desc);
            linearLayout = itemView.findViewById(R.id.info_layout);
            usedImageView = itemView.findViewById(R.id.used_img);
            layoutMiddle = itemView.findViewById(R.id.layout_middle);
        }

        void bind(final CouponInfoResBean couponInfo){

            usedImageView.setVisibility(View.GONE);

            final SharedPreferences mPreferences= PreferencesManager.getApplicationPreference(context);
            final String curLang = PreferencesManager.getStringEntryFromPreferences(mPreferences, "lang");

            if(curLang.equals(LANG_MM)){
                //Special Event.
                textSpecialEvent.setText(couponInfo.getSpecialEventMM());
                //Coupon Name.
                couponName.setText(couponInfo.getCouponNameMM());
                //Descriptions.
                textDesc.setText(couponInfo.getDescriptionMM());
            } else {
                //Special Event.
                textSpecialEvent.setText(couponInfo.getSpecialEventEN());
                //Coupon Name.
                couponName.setText(couponInfo.getCouponNameEN());
                //Descriptions.
                textDesc.setText(couponInfo.getDescriptionEN());
            }

            DecimalFormat df = new DecimalFormat("#.##");

            //Goods Price.
            goodsPrice.setText(df.format(couponInfo.getGoodsPrice()));
            goodsPrice.setPaintFlags(goodsPrice.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);

            //Goods Price Unit.
            goodsPriceUnit.setText("mmk");

            //Coupon Price.
            String couponAmount = df.format(couponInfo.getCouponAmount());
            if(couponAmount!=null){
                if(couponAmount.length()<2){
                    layout2.setVisibility(View.GONE);
                    couponPriceUnit1.setTextSize(26);
                    expiredDateLbl1.setText("End of");
                    expiredDate1.setText(getChangeTimestampToString2(couponInfo.getExpiredTime()));
                    couponPrice1.setText(couponAmount);
                    //Coupon Price Unit.
                    String discountUnit = couponInfo.getDiscountUnit();
                    if(discountUnit!=null){
                        if (discountUnit.equals(UNIT_PERCENT)) {
                            couponPriceUnit1.setText("%");
                            couponPriceUnit2.setTextSize(30);
                        }
                        else { couponPriceUnit1.setText("mmk"); }
                    } else { couponPriceUnit1.setText("mmk"); }

                } else {

                    switch (couponAmount.length()){
                        case 2 : couponPrice2.setTextSize(55); break;
                        case 3 : couponPrice2.setTextSize(55);
                            couponPriceUnit2.setTextSize(20); break;
                        case 4 : couponPrice2.setTextSize(48);
                                 couponPriceUnit2.setTextSize(16); break;
                        default: couponPrice2.setTextSize(40);
                            couponPriceUnit2.setTextSize(16); break;
                    }

                    expiredDateLbl2.setText("End of");
                    expiredDate2.setText(getChangeTimestampToString2(couponInfo.getExpiredTime()));
                    couponPrice2.setText(couponAmount);
                    //Coupon Price Unit.
                    String discountUnit = couponInfo.getDiscountUnit();
                    if(discountUnit!=null){
                        if (discountUnit.equals(UNIT_PERCENT)) {
                            couponPriceUnit2.setText("%");
                            couponPriceUnit2.setTextSize(30);
                        }
                        else { couponPriceUnit2.setText("mmk"); }
                    } else { couponPriceUnit2.setText("mmk"); }
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String couponId = couponInfo.getCouponId();
                    final String couponCode = couponInfo.getCouponCode();
                    final Service couponUpdateService = APIClient.getUserService();

                    if (!CommonUtils.isNetworkAvailable(context)) {
                        showErrorDialog(context,context.getString(R.string.network_connection_err));
                    } else {

                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.use_coupon_dialog);
                        dialog.setCancelable(false);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        Button btnOk = dialog.findViewById(R.id.btn_ok);
                        Button btnCalcel = dialog.findViewById(R.id.btn_cancel);
                        final EditText textPwd = dialog.findViewById(R.id.txt_coupon_pwd);
                        btnCalcel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String password = textPwd.getText().toString();

                                if (password.equals(BLANK)) {
                                    //Toast.makeText(context, getEmptyPwdMsg(), Toast.LENGTH_SHORT).show();
                                    displayMessage(context,getEmptyPwdMsg());
                                } else {

                                    final CouponUseInfoReqBean couponUseInfoReqBean =
                                            new CouponUseInfoReqBean();

                                    couponUseInfoReqBean.setCouponId(couponId);
                                    couponUseInfoReqBean.setCouponPassword(password);
                                    couponUseInfoReqBean.setCustomerId(String.valueOf(couponInfo.getCustomerId()));

                                    Call<BaseResponse> req2 =
                                            couponUpdateService.updateCouponInfo(PreferencesManager.getAccessToken(context),couponUseInfoReqBean);

                                    context.setTheme(R.style.MessageDialogTheme);
                                    final ProgressDialog updCouponDialog = new ProgressDialog(context);
                                    updCouponDialog.setMessage("Updating...");
                                    updCouponDialog.setCancelable(false);
                                    updCouponDialog.show();

                                    req2.enqueue(new retrofit2.Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                            if (response.isSuccessful()) {
                                                BaseResponse baseResponse = response.body();

                                                if(baseResponse.getStatus().equals(SUCCESS)){

                                                    dialog.dismiss();
                                                    closeDialog(updCouponDialog);

                                                    usedImageView.setVisibility(View.VISIBLE);
                                                    Picasso.get().load(R.drawable.used_stamp).into(usedImageView);

                                                    int i = 0;
                                                    for (CouponInfoResBean couponInfoResBean : couponInfoResBeanList) {
                                                        if (couponInfoResBean.getCouponCode().equals(couponCode)) {
                                                            couponInfoResBeanList.get(i).setUsedFlag(CommonConstants.COUPON_FLAG_USED);
                                                            itemView.setClickable(false);
                                                            break;
                                                        }
                                                        i++;
                                                    }

                                                    //update preference value.
                                                    String updateCouponInfoParam = new Gson().toJson(couponInfoResBeanList);
                                                    final SharedPreferences preferences=PreferencesManager.getCurrentUserPreferences(context);
                                                    PreferencesManager.addEntryToPreferences(preferences, "cur_coupon_info", updateCouponInfoParam);

                                                } else {

                                                    textPwd.setText(BLANK);
                                                    closeDialog(updCouponDialog);

                                                    if(baseResponse.getMessageCode().equals(INCORRECT_PASSWORD)){
                                                        showWarningDialog(context,getIncorrectPwdMsg());
                                                    } else {
                                                        showWarningDialog(context,"Coupon is Invalid.");
                                                    }
                                                }

                                            } else {
                                                closeDialog(updCouponDialog);
                                                showErrorDialog(context,context.getString(R.string.service_unavailable));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                                            closeDialog(updCouponDialog);
                                            showErrorDialog(context,context.getString(R.string.service_unavailable));
                                        }
                                    });
                                }
                            }
                        });
                        dialog.show();
                    }
                }
            });

            if(couponInfo.getUsedFlag()!=null){
                if(couponInfo.getUsedFlag().equals(CommonConstants.COUPON_FLAG_USED)){
                    usedImageView.setVisibility(View.VISIBLE);
                    Picasso.get().load(R.drawable.used_stamp).into(usedImageView);
                    itemView.setClickable(false);
                }
            }

            //Coupon Img.
            final String imagePath = COUPON_URL+couponInfo.getUnuseImagePath();
            if(imagePath==null || imagePath==BLANK) {
                //Picasso.get().load(R.drawable.noimage).into(imgCouponImg);
            } else {
                Picasso.get().load(imagePath).into(imgCouponImg, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imgCouponImg.setMinimumWidth(linearLayout.getHeight());
                        Picasso.get().load(imagePath).into(imgCouponImg);
                    }
                    @Override
                    public void onError(Exception e) {
                        imgCouponImg.setPadding(20,0,20,0);
                        imgCouponImg.setMinimumWidth(linearLayout.getHeight());
                        //Picasso.get().load(R.drawable.noimage).into(imgCouponImg);
                    }
                });
            }

        }
    }

    private String getIncorrectPwdMsg(){
        final String language = PreferencesManager.getCurrentLanguage(context);
        return CommonUtils.getLocaleString(new Locale(language), R.string.invalid_coupon_pwd, context);
    }

    private String getEmptyPwdMsg(){
        final String language = PreferencesManager.getCurrentLanguage(context);
        return CommonUtils.getLocaleString(new Locale(language), R.string.no_coupon_pwd, context);
    }

}





