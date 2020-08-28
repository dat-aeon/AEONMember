package mm.com.aeon.vcsaeon.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.activities.ProfilePhotoUpdateActivity;
import mm.com.aeon.vcsaeon.adapters.CardInfoOneQRListAdapter;
import mm.com.aeon.vcsaeon.beans.CustAgreementInfoReqBean;
import mm.com.aeon.vcsaeon.beans.CustAgreementListDto;
import mm.com.aeon.vcsaeon.beans.LoanTypeBean;
import mm.com.aeon.vcsaeon.beans.Product;
import mm.com.aeon.vcsaeon.beans.ProductInfoBean;
import mm.com.aeon.vcsaeon.beans.ProductTypeListBean;
import mm.com.aeon.vcsaeon.beans.PurchaseConfirmationReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseInfoConfirmationReqBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.delegates.MembershipInfoDelegate;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PARAM_LANG;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;

public class MembershipInformationFragment extends BaseFragment
        implements LanguageChangeListener, MembershipInfoDelegate {

    View view;

    TextView textCustomerNo;
    TextView textMemberId;
    ImageView imageMemberPhoto;
    ImageView editImage;

    RecyclerView rvCardOne;

    UserInformationFormBean userInformationFormBean;
    List<CustAgreementListDto> customerAgreementListDtoList;
    List<LoanTypeBean> loanTypeBeanList;

    private String curLang;
    private SharedPreferences sharedPreferences;

    private LanguageChangeListener toolbarUpdateListener;
    CardInfoOneQRListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);

        view = inflater.inflate(R.layout.membership_info_activity, container, false);

        // set listener
        ((MainMenuActivityDrawer) getActivity()).setLanguageListener(MembershipInformationFragment.this);

        // change level text
        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        TextView menuBarLevelInfo = toolbar.findViewById(R.id.menu_bar_level);
        menuBarLevelInfo.setText(R.string.menu_level_three);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);

        textCustomerNo = view.findViewById(R.id.lbl_customer_id);
        textCustomerNo.setText(userInformationFormBean.getCustomerNo());
        textMemberId = view.findViewById(R.id.lbl_member_id);
        textMemberId.setText(getFormattedMemberNo(userInformationFormBean.getMemberNo()));
        getLoanTypesList();

        imageMemberPhoto = view.findViewById(R.id.member_photo);
        final String imagePath = userInformationFormBean.getPhotoPath();

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

        editImage = view.findViewById(R.id.ic_edit_bg);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int customerTypeId = userInformationFormBean.getCustomerTypeId();
                if (customerTypeId == CommonConstants.CUSTOMER_TYPE_MEMBER) {
                    Intent intent = intentProfilePhotoEdit(getActivity());
                    startActivity(intent);
                } else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.profile_edit_info);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button btnOk = dialog.findViewById(R.id.button_ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        rvCardOne = view.findViewById(R.id.rv_card_one);

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

                if (baseResponse != null) {

                    if (baseResponse.getStatus().equals(SUCCESS)) {

                        customerAgreementListDtoList = (List<CustAgreementListDto>) baseResponse.getData();

                        if (customerAgreementListDtoList != null) {
                            // add warning text view
                            CustAgreementListDto dto = new CustAgreementListDto();
                            dto.setWarningText(true);
                            customerAgreementListDtoList.add(dto);

                            adapter = new CardInfoOneQRListAdapter(customerAgreementListDtoList,
                                    getActivity(), MembershipInformationFragment.this);
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

        sharedPreferences = PreferencesManager.getApplicationPreference(getActivity());
        curLang = PreferencesManager.getStringEntryFromPreferences(sharedPreferences, PARAM_LANG);
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }
        return view;
    }

    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (item.getTitle().equals(LANG_MM)) {
                    item.setIcon(R.drawable.en_flag2);
                    item.setTitle(LANG_EN);
                    changeLabel(LANG_MM);

                } else if (item.getTitle().equals(LANG_EN)) {
                    item.setIcon(R.drawable.mm_flag);
                    item.setTitle(LANG_MM);
                    changeLabel(LANG_EN);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeLabel(String language) {
        Log.e("current lang", language);
        PreferencesManager.setCurrentLanguage(getContext(), language);
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

    private static Intent intentProfilePhotoEdit(Context context) {
        Intent intent = new Intent(context, ProfilePhotoUpdateActivity.class);
        return intent;
    }

    @Override
    public void onTouchQRCode(int daApplicationInfoId) {

        if (daApplicationInfoId == 0) {
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

                    if (response != null) {

                        BaseResponse<Product> baseResponse = response.body();

                        if (baseResponse.getStatus().equals(SUCCESS)) {

                            final Product product = (Product) baseResponse.getData();

                            if (product != null) {

                                DecimalFormat df = new DecimalFormat("#,###,###");

                                final Dialog dialog = new Dialog(getActivity());
                                dialog.setContentView(R.layout.dialog_product_info);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                dialog.setContentView(R.layout.dialog_product_info);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView productCategory = dialog.findViewById(R.id.lbl_product_category);
                                TextView productDescription = dialog.findViewById(R.id.lbl_product_desc);
                                TextView productBrand = dialog.findViewById(R.id.lbl_product_brand);
                                TextView productModel = dialog.findViewById(R.id.lbl_product_model);
                                TextView productPrice = dialog.findViewById(R.id.lbl_product_price);

                                TextView productCategory_2 = dialog.findViewById(R.id.lbl_product_category_2);
                                TextView productDescription_2 = dialog.findViewById(R.id.lbl_product_desc_2);
                                TextView productBrand_2 = dialog.findViewById(R.id.lbl_product_brand_2);
                                TextView productModel_2 = dialog.findViewById(R.id.lbl_product_model_2);
                                TextView productPrice_2 = dialog.findViewById(R.id.lbl_product_price_2);

                                TextView productTitle_2 = dialog.findViewById(R.id.product_titile_2);
                                LinearLayout productLayout_2 = dialog.findViewById(R.id.layout_form_product_2);
                                TextView paymentAmount = dialog.findViewById(R.id.lbl_product_payment);

                                List<ProductInfoBean> purchaseProduct = product.getPurchaseInfoProductDtoList();
                                ProductInfoBean productOne = purchaseProduct.get(0);
                                productCategory.setText(getProductCate(productOne.getDaProductTypeId()));
                                productDescription.setText(productOne.getProductDescription());
                                productBrand.setText(productOne.getBrand());
                                productModel.setText(productOne.getModel());
                                productPrice.setText(String.valueOf(productOne.getPrice()));
                                paymentAmount.setText(String.valueOf(productOne.getCashDownAmount()));

                                if (purchaseProduct.size() > 1) {
                                    ProductInfoBean productTwo = purchaseProduct.get(1);
                                    productCategory_2.setText(getProductCate(productTwo.getDaProductTypeId()));
                                    productDescription_2.setText(productTwo.getProductDescription());
                                    productBrand_2.setText(productTwo.getBrand());
                                    productModel_2.setText(productTwo.getModel());
                                    productPrice_2.setText(String.valueOf(productTwo.getPrice()));

                                    productTitle_2.setVisibility(View.VISIBLE);
                                    productLayout_2.setVisibility(View.VISIBLE);
                                }

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

                                                if (baseResponse != null) {

                                                    if (baseResponse.getStatus().equals(SUCCESS)) {
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

                                                if (baseResponse != null) {

                                                    if (baseResponse.getStatus().equals(SUCCESS)) {
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

    void showMessageDialog(String message) {
        new MaterialAlertDialogBuilder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    void getLoanTypesList() {
        loanTypeBeanList = new ArrayList<>();
        Service service = APIClient.getApplicationRegisterService();
        Call<BaseResponse<List<LoanTypeBean>>> req = service.getLoanTypes();
        req.enqueue(new Callback<BaseResponse<List<LoanTypeBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<LoanTypeBean>>> call, Response<BaseResponse<List<LoanTypeBean>>> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null) {
                    if (baseResponse.getStatus().equals(SUCCESS)) {
                        loanTypeBeanList = (List<LoanTypeBean>) baseResponse.getData();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<LoanTypeBean>>> call, Throwable t) {
            }
        });
    }

    @Override
    public void changeLanguageTitle(String lang) {
        adapter.notifyDataSetChanged();
        PreferencesManager.setCurrentLanguage(getContext(), lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
    }

    public LanguageChangeListener getToolbarUpdateListener() {
        return toolbarUpdateListener;
    }

    public void setToolbarUpdateListener(LanguageChangeListener toolbarUpdateListener) {
        this.toolbarUpdateListener = toolbarUpdateListener;
    }

    private String getProductCate(int productId) {
        String productCate = "";
        List<ProductTypeListBean> productTypeList = PreferencesManager.getProductTypeList(getActivity());
        int listSize = productTypeList.size();

        for (int list = 0; list < listSize; list++) {
            if (productTypeList.get(list).getProductTypeId() == productId) {
                productCate = productTypeList.get(list).getName();
            }
        }
        return productCate;
    }
}
