package mm.com.aeon.vcsaeon.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.adapters.CardInfoOneQRListAdapter;
import mm.com.aeon.vcsaeon.beans.CustAgreementInfoReqBean;
import mm.com.aeon.vcsaeon.beans.CustAgreementListDto;
import mm.com.aeon.vcsaeon.beans.LoanTypeBean;
import mm.com.aeon.vcsaeon.beans.Product;
import mm.com.aeon.vcsaeon.beans.PurchaseConfirmationReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseInfoConfirmationReqBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.MembershipInfoDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;

public class MemberCardOneTabFragment extends BaseFragment implements MembershipInfoDelegate {

    View view;

    TextView textName;
    TextView textCustomerNo;

    UserInformationFormBean userInformationFormBean;

    List<CustAgreementListDto> customerAgreementListDtoList;

    FrameLayout layoutHeader;

    RecyclerView rvCardOne;

    List<LoanTypeBean> loanTypeBeanList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.member_card_one_tab, container, false);

        rvCardOne = view.findViewById(R.id.rv_card_one);

        layoutHeader = view.findViewById(R.id.layout_header);

        textName = view.findViewById(R.id.text_name);
        textCustomerNo = view.findViewById(R.id.text_customer_no);

        ImageView imageView2=view.findViewById(R.id.animate_img2);
        Glide.with(getActivity()).load(R.drawable.member_card_bg_animate).into(imageView2);

        textName.setText(userInformationFormBean.getName());
        textName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textName.setMarqueeRepeatLimit(-1);
        textName.setSingleLine(true);
        textName.setSelected(true);
        textCustomerNo.setText(userInformationFormBean.getCustomerNo());

        getLoanTypesList();

        final String accessToken = PreferencesManager.getAccessToken(getActivity());
        CustAgreementInfoReqBean custAgreementInfoReqBean = new CustAgreementInfoReqBean();
        custAgreementInfoReqBean.setCustomerId(userInformationFormBean.getCustomerId());

        Service getAgreementListService = APIClient.getApplicationRegisterService();
        Call<BaseResponse<List<CustAgreementListDto>>> req = getAgreementListService
                .getCustomerAgreementList(accessToken, custAgreementInfoReqBean);

        req.enqueue(new Callback<BaseResponse<List<CustAgreementListDto>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<CustAgreementListDto>>> call, Response<BaseResponse<List<CustAgreementListDto>>> response) {

                BaseResponse baseResponse = response.body();

                if(baseResponse != null) {

                    if(baseResponse.getStatus().equals(SUCCESS)){

                        customerAgreementListDtoList = (List<CustAgreementListDto>) baseResponse.getData();

                        if(customerAgreementListDtoList != null) {
                            CardInfoOneQRListAdapter adapter = new CardInfoOneQRListAdapter(customerAgreementListDtoList,
                                    getActivity(), MemberCardOneTabFragment.this);
                            rvCardOne.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            rvCardOne.setAdapter(adapter);
                        }

                    } else {
                        showMessageDialog("Service Unavailable.");
                    }
                } else {
                    showMessageDialog("Data not available.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<CustAgreementListDto>>> call, Throwable t) {
                showMessageDialog("Fetching Interrupted.");
            }
        });

        return view;
    }

    @Override
    public void onTouchQRCode(int daApplicationInfoId) {

        if(daApplicationInfoId == 0){
            showMessageDialog("No Product Information.");
        } else {

            final String accessToken = PreferencesManager.getAccessToken(getActivity());

            PurchaseInfoConfirmationReqBean purchaseInfoConfirmationReqBean =
                    new PurchaseInfoConfirmationReqBean();
            purchaseInfoConfirmationReqBean.setDaApplicationInfoId(daApplicationInfoId);

            Service getProductService = APIClient.getApplicationRegisterService();
            Call<BaseResponse<Product>> req = getProductService.getPurchaseInfoConfirmation(
                    accessToken, purchaseInfoConfirmationReqBean);

            req.enqueue(new Callback<BaseResponse<Product>>() {
                @Override
                public void onResponse(Call<BaseResponse<Product>> call, Response<BaseResponse<Product>> response) {

                    if(response != null){

                        BaseResponse<Product> baseResponse = response.body();

                        if(baseResponse.getStatus().equals(SUCCESS)){

                            final Product product = (Product) baseResponse.getData();

                            if(product != null) {

                                DecimalFormat df = new DecimalFormat("#,###,###");

                                final Dialog dialog = new Dialog(getActivity());
                                dialog.setContentView(R.layout.dialog_product_info);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                //TextView textLoanType = dialog.findViewById(R.id.lbl_loan_type);
                                /*TextView textProductCode = dialog.findViewById(R.id.lbl_product_code);
                                TextView textPrice = dialog.findViewById(R.id.lbl_price);
                                TextView textCashDown = dialog.findViewById(R.id.lbl_cash_down);
                                TextView textProductName = dialog.findViewById(R.id.lbl_product_name);
                                TextView textModel = dialog.findViewById(R.id.lbl_model);
                                TextView textBrand = dialog.findViewById(R.id.lbl_brand);
                                TextView textInvoice = dialog.findViewById(R.id.lbl_invoice);*/

                                /*final int loanTypeInt = product.getDaLoanTypeId();
                                if(loanTypeInt != 0){
                                    for (LoanTypeBean loanTypeBean : loanTypeBeanList) {
                                        if(loanTypeInt == loanTypeBean.getLoanTypeId()){
                                            textLoanType.setText(loanTypeBean.getName());
                                            break;
                                        }
                                    }
                                }*/

                                /*textProductCode.setText(product.getProductCode());
                                textPrice.setText(df.format(product.getPrice())+" Ks.");
                                textCashDown.setText(df.format(product.getCashDownAmount())+" Ks.");
                                textProductName.setText(product.getProductName());
                                textModel.setText(product.getModel());
                                textBrand.setText(product.getBrand());
                                textInvoice.setText(product.getInvoiceNo());*/

                                Button btnOk = dialog.findViewById(R.id.btn_product_info_ok);
                                Button btnCancel = dialog.findViewById(R.id.btn_product_info_cancel);

                                final Service service = APIClient.getApplicationRegisterService();
                                final String accessToken = PreferencesManager.getAccessToken(getActivity());

                                btnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        PurchaseConfirmationReqBean confirmReqBean
                                                = new PurchaseConfirmationReqBean();
                                        confirmReqBean.setCustomerId(userInformationFormBean.getCustomerId());
                                        confirmReqBean.setDaPurchaseInfoId(product.getDaPurchaseInfoId());
                                        confirmReqBean.setDaApplicationInfoId(product.getDaApplicationInfoId());

                                        Call<BaseResponse> callConfirm = service
                                                .confirmPurchase(accessToken, confirmReqBean);

                                        callConfirm.enqueue(new Callback<BaseResponse>() {
                                            @Override
                                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                                BaseResponse baseResponse = response.body();

                                                if(baseResponse != null){

                                                    if(baseResponse.getStatus().equals(SUCCESS)){
                                                        dialog.dismiss();
                                                    } else {
                                                        showMessageDialog("Service Unavailable.");
                                                    }

                                                } else {
                                                    showMessageDialog("Data not available.");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                                showMessageDialog("Fetching Interrupted.");
                                            }
                                        });
                                    }
                                });

                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        PurchaseConfirmationReqBean confirmReqBean
                                                = new PurchaseConfirmationReqBean();
                                        confirmReqBean.setCustomerId(userInformationFormBean.getCustomerId());
                                        confirmReqBean.setDaPurchaseInfoId(product.getDaPurchaseInfoId());
                                        confirmReqBean.setDaApplicationInfoId(product.getDaApplicationInfoId());

                                        Call<BaseResponse> callCancel = service
                                                .cancelPurchase(accessToken, confirmReqBean);

                                        callCancel.enqueue(new Callback<BaseResponse>() {
                                            @Override
                                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                                                BaseResponse baseResponse = response.body();

                                                if(baseResponse != null){

                                                    if(baseResponse.getStatus().equals(SUCCESS)){
                                                        dialog.dismiss();
                                                    } else {
                                                        showMessageDialog("Service Unavailable.");
                                                    }
                                                } else {
                                                    showMessageDialog("Data not available.");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                                showMessageDialog("Fetching Interrupted.");
                                            }
                                        });

                                    }
                                });

                                dialog.show();

                            } else {
                                showMessageDialog("No Product Information.");
                            }
                        } else {
                            showMessageDialog("Service Unavailable.");
                        }
                    } else {
                        showMessageDialog("Data not available.");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<Product>> call, Throwable t) {
                    showMessageDialog("Fetching Interrupted.");
                }
            });
        }
    }

    void showMessageDialog(String message){
        new MaterialAlertDialogBuilder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    void getLoanTypesList(){
        loanTypeBeanList = new ArrayList<>();
        Service service = APIClient.getApplicationRegisterService();
        Call<BaseResponse<List<LoanTypeBean>>> req = service.getLoanTypes();
        req.enqueue(new Callback<BaseResponse<List<LoanTypeBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<LoanTypeBean>>> call, Response<BaseResponse<List<LoanTypeBean>>> response) {
                BaseResponse baseResponse = response.body();
                if(baseResponse != null){
                    if(baseResponse.getStatus().equals(SUCCESS)){
                        loanTypeBeanList = (List<LoanTypeBean>) baseResponse.getData();
                    }
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<List<LoanTypeBean>>> call, Throwable t) { }
        });
    }
}
