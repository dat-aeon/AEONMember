package mm.com.aeon.vcsaeon.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.canner.stepsview.StepsView;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.adapters.ApplicationPreviewAttachImgAdapter;
import mm.com.aeon.vcsaeon.beans.ApplicationFormErrMesgBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoAttachmentFormBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoPhotoBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.DATempPhotoBean;
import mm.com.aeon.vcsaeon.beans.EmergencyContactFormBean;
import mm.com.aeon.vcsaeon.beans.GuarantorFormBean;
import mm.com.aeon.vcsaeon.beans.LoanCalculationReqBean;
import mm.com.aeon.vcsaeon.beans.LoanCalculationResBean;
import mm.com.aeon.vcsaeon.beans.OccupationDataFormBean;
import mm.com.aeon.vcsaeon.beans.PasswordCheckReqBean;
import mm.com.aeon.vcsaeon.beans.TownshipListBean;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;
import mm.com.aeon.vcsaeon.common_utils.CameraUtil;
import mm.com.aeon.vcsaeon.common_utils.CommonConstants;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.DigitalApplicationRegistrationAsyncTask;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.ApplicationDetailAttachDelegate;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import mm.com.aeon.vcsaeon.views.customviews.SendMessageImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer.appDataErrMsgShow;
import static mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer.emergencyErrMsgShow;
import static mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer.errorPages;
import static mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer.guarantorErrMsgShow;
import static mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer.occupationErrMsgShow;
import static mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer.photoList;
import static mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer.tempPhotoBeanList;
import static mm.com.aeon.vcsaeon.common_utils.CameraUtil.resizeImages;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ALL_ATTACHMENT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.APPLICANT_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.APPLICANT_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.APPLICATION_PLOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CAMERA;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CRIMINAL_CLEARANCE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.DA_TITLES;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.FAILED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.FEMALE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.GALLERY;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.GUARANTOR_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.INCOME_PROOF_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_EN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LANG_MM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOAN_AMOUNT_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOAN_TERM_PERIOD;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MALE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MARRIED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MYANMAR_CURRENCY;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NRC_BACK_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NRC_FRONT_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NRC_GUARANTOR_BACK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NRC_GUARANTOR_FRONT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_APPLICANT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_APPLICANT_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_CRIMINAL_CLEARANCE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_GUARANTOR_SIGNATURE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_INCOME_PROOF;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_BACK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_FRONT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_GUARANTOR_BACK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_NRC_GUARANTOR_FRONT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_QUALITY_80;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.PHOTO_RESIDENT_PROOF;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.RESIDENT_PROOF_PHOTO;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_M_LOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_NON_MOBILE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.ROLE_P_LOAN;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SINGLE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.dateToString2;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.hideKeyboard;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.rotateBitmap;
import static mm.com.aeon.vcsaeon.common_utils.PreferencesManager.isLoadImageLocked;
import static mm.com.aeon.vcsaeon.common_utils.PreferencesManager.unLockImageLoad;
import static mm.com.aeon.vcsaeon.common_utils.UiUtils.showNetworkErrorDialog;
import static mm.com.aeon.vcsaeon.fragments.SmallLoanOccupationFragment.occu_monthlyIncome;

public class SmallLoanConfirmFragment extends PagerRootFragment implements ApplicationDetailAttachDelegate, LanguageChangeListener {

    View view;
    private RadioGroup radioLoanTerm;

    private TextView labelFinanceAmount;
    private TextView labelLoanTerm;
    private TextView labelProcessFee;
    private TextView labelCompulsory;
    private TextView labelTotalRepayment;
    private TextView labelFirstRepayAmount;
    private TextView labelMonthlyRepayAmount;
    private TextView labelLastPayment;
    private TextView labelNrcFront;
    private TextView labelNrcBack;
    private TextView labelIncomeProof;
    private TextView labelResidentProof;
    private TextView labelGuarantorNrcFront;
    private TextView labelGuarantorNrcBack;
    private TextView labelCriminalClear;
    private TextView labelApplicantPhoto;
    private TextView labelGuarantorSign;
    private TextView labelApplicantSign;

    private Integer errFinanceAmountLocale;
    private Integer errNrcFrontLocale;
    private Integer errNrcBackLocale;
    private Integer errIncomeProofLocale;
    private Integer errResidentProofLocale;
    private Integer errGuarantorNrcFrontLocale;
    private Integer errGuarantorNrcBackLocale;
    private Integer errCriminalClearLocale;
    private Integer errApplicantPhotoLocale;
    private Integer errApplicantSignLocale;
    private Integer errGuarantorSignLocale;
    private Integer errLoanTermLocale;
    private String errBusinessErrLocale;

    private TextView errFinanceAmount;
    private TextView errNrcFront;
    private TextView errNrcBack;
    private TextView errIncomeProof;
    private TextView errResidentProof;
    private TextView errGuarantorNrcFront;
    private TextView errGuarantorNrcBack;
    private TextView errCriminalClear;
    private TextView errApplicantPhoto;
    private TextView errApplicantSign;
    private TextView errGuarantorSign;
    private TextView errLoanTerm;

    private EditText financeAmount;
    private EditText processingFee;
    private EditText compulsorySaving;
    private EditText totalRepayment;
    private EditText firstRepayment;
    private EditText lastRepayment;
    private EditText monthlyRepayment;

    private String previewProcessingFee = "-";
    private String previewCompulsorySaving = "-";
    private String previewTotalRepayment = "-";
    private String previewFirstRepayment = "-";
    private String previewLastRepayment = "-";
    private String previewMonthlyRepayment = "-";

    private RelativeLayout layoutLoading;
    private LinearLayout btnNrcFront;
    private LinearLayout btnNrcBack;
    private LinearLayout btnIncomeProof;
    private LinearLayout btnResidentProof;
    private LinearLayout btnGuarantorFront;
    private LinearLayout btnGuarantorBack;
    private LinearLayout btnCriminalClear;
    private LinearLayout btnApplicantPhoto;
    private LinearLayout btnGuarantorSign;
    private LinearLayout btnSignature;

    private ImageView imgLoading;
    private ImageView nrcFront;
    private ImageView nrcBack;
    private ImageView incomeProof;
    private ImageView residentProof;
    private ImageView guarantorNrcFront;
    private ImageView guarantorNrcBack;
    private ImageView criminalClearance;
    private ImageView applicantPhoto;
    private ImageView signaturePhoto;
    private ImageView guarantorSignPhoto;

    public static TextView textBusinessErrMsg;

    private final int REQUEST_TAKE_PHOTO = 1;
    private final int REQUEST_LOAD_IMAGE = 2;

    private static String photoStatus;
    private String mCurrentPhotoPath;

    private static Double txtFinanceAmount;
    private static Double txtProcessingFee;
    private double finance_amount;

    private static int loanTermId;
    private static int customerId;
    private static int loanTerm;

    UserInformationFormBean userInformationFormBean;

    private int selectedPosition;
    private boolean validate = true;
    private String curLang;
    private static final int INIT_LOAN_TERM_VAL = 0;
    private static final double MAX_LOAN_AMOUNT = 2000000;
    private static final double MIN_LOAN_AMOUNT = 50000;
    private static final double MICRO_LOAN_AMOUNT = 150000;
    private static final double MID_AMOUNT_AMOUNT = 700000;
    private static final int MIN_LOAN = 1;
    private static final int MICRO_LOAN = 2;
    private static final int MID_LOAN = 3;

    private String rootFolderName;
    private File storageDir;
    private Button submitLoan;
    private Button preview;
    private StepsView stepsView;

    //private TextView guarantorTitle;
    //private TextView loanDataTitle;

    private Double basicIncome;
    private String financeAmountCheck;
    private String[] productCategory;
    private int[] productId;
    private String productCategoryPreview;
    private int lastRadioGroup;

    private List<String> cityList;
    private List<String> townshipList;
    private List<Integer> cityId;
    private List<Integer> townshipId;
    private List<CityTownshipResBean> cityTownshipList;

    private static Bitmap nrcFrontBitmap;
    private static Bitmap nrcBackBitmap;
    private static Bitmap residentBitmap;
    private static Bitmap incomeBitmap;
    private static Bitmap guaNrcFrontBitmap;
    private static Bitmap guaNrcBackBitmap;
    private static Bitmap criminalClearBitmap;
    private static Bitmap applicantBitmap;
    private static Bitmap signBitmap;
    private static Bitmap guarantorSignBitmap;

    boolean termAndConditionCheck = false;
    boolean passwordBlank = false;

    Bitmap rotateImage;
    private StateProgressBar confStepView;
    ProgressDialog imgLoadPD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_small_loan_confirm, container, false);
        setHasOptionsMenu(true);

        //unlock attach image load/capture.
        unLockImageLoad(getActivity());

        String[] pages = {"Application\nData", "Occupation\nData", "Emergency\nContact", "Guarantor\nData", "Loan\nConfirmation"};
        confStepView = view.findViewById(R.id.conf_stepped_bar);
        confStepView.setStateDescriptionData(pages);
        confStepView.setOnStateItemClickListener(new OnStateItemClickListener() {
            @Override
            public void onStateItemClick(StateProgressBar stateProgressBar, StateItem stateItem, int stateNumber, boolean isCurrentState) {
                if (stateNumber == 1) {
                    viewPager.setCurrentItem(0, true);
                } else if (stateNumber == 2) {
                    viewPager.setCurrentItem(1, true);
                } else if (stateNumber == 3) {
                    viewPager.setCurrentItem(2, true);
                } else if (stateNumber == 4) {
                    viewPager.setCurrentItem(3, true);
                } else if (stateNumber == 5) {
                    viewPager.setCurrentItem(4, true);
                }
            }
        });

        Toolbar toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);
        curLang = PreferencesManager.getCurrentLanguage(getContext());

        setBasicIncome();
        layoutLoading = view.findViewById(R.id.layout_loading_loan);
        imgLoading = view.findViewById(R.id.img_loading_loan);

        processingFee = view.findViewById(R.id.confirm_processing_fee);
        compulsorySaving = view.findViewById(R.id.confirm_compulsory_saving);
        totalRepayment = view.findViewById(R.id.confirm_total_repay);
        firstRepayment = view.findViewById(R.id.confirm_first_repay);
        monthlyRepayment = view.findViewById(R.id.confirm_monthly_repay);
        lastRepayment = view.findViewById(R.id.confirm_last_payment);

        userInformationFormBean = new UserInformationFormBean();
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        userInformationFormBean = new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);
        customerId = userInformationFormBean.getCustomerId();

        ScrollView scrollView = view.findViewById(R.id.loan_confirm_scroll);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        stepsView = view.findViewById(R.id.stepsView_5);
        stepsView.setLabels(DA_TITLES)
                .setBarColorIndicator(getContext().getColor(R.color.gray))
                .setProgressColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setLabelColorIndicator(getContext().getColor(R.color.colorPrimary))
                .setCompletedPosition(4)
                .drawView();

        labelFinanceAmount = view.findViewById(R.id.conf_finance_amount);
        labelLoanTerm = view.findViewById(R.id.conf_finance_term);
        labelProcessFee = view.findViewById(R.id.conf_processing_fee);
        labelCompulsory = view.findViewById(R.id.conf_compulsory_saving);
        labelTotalRepayment = view.findViewById(R.id.conf_total_repay);
        labelFirstRepayAmount = view.findViewById(R.id.conf_first_repay);
        labelMonthlyRepayAmount = view.findViewById(R.id.conf_monthly_repay);
        labelLastPayment = view.findViewById(R.id.conf_last_payment);
        labelNrcFront = view.findViewById(R.id.conf_nrc_front);
        labelNrcBack = view.findViewById(R.id.conf_nrc_back);
        labelIncomeProof = view.findViewById(R.id.conf_income_proof);
        labelResidentProof = view.findViewById(R.id.conf_resident_proof);
        labelGuarantorNrcFront = view.findViewById(R.id.conf_gurantor_front);
        labelGuarantorNrcBack = view.findViewById(R.id.conf_gurantor_back);
        labelCriminalClear = view.findViewById(R.id.conf_criminal_proof);
        labelApplicantPhoto = view.findViewById(R.id.conf_applicant);
        labelGuarantorSign = view.findViewById(R.id.conf_guar_sign);
        labelApplicantSign = view.findViewById(R.id.conf_signature);

        errLoanTerm = view.findViewById(R.id.confirm_err_financeterm);
        errFinanceAmount = view.findViewById(R.id.confirm_err_financeAmt);
        errNrcFront = view.findViewById(R.id.err_nrcFront);
        errNrcBack = view.findViewById(R.id.err_nrcBack);
        errIncomeProof = view.findViewById(R.id.err_incomeProof);
        errResidentProof = view.findViewById(R.id.err_residentProof);
        errGuarantorNrcFront = view.findViewById(R.id.err_guarantorNrcFront);
        errGuarantorNrcBack = view.findViewById(R.id.err_guarantorNrcBack);
        errCriminalClear = view.findViewById(R.id.err_criminalClearance);
        errApplicantPhoto = view.findViewById(R.id.err_applicantPhoto);
        errApplicantSign = view.findViewById(R.id.err_applicatnSign);
        errGuarantorSign = view.findViewById(R.id.err_guarantor_sign);

        errLoanTermLocale = R.string.da_mesg_blank;
        errFinanceAmountLocale = R.string.da_mesg_blank;
        errNrcFrontLocale = R.string.da_mesg_blank;
        errNrcBackLocale = R.string.da_mesg_blank;
        errIncomeProofLocale = R.string.da_mesg_blank;
        errResidentProofLocale = R.string.da_mesg_blank;
        errGuarantorNrcFrontLocale = R.string.da_mesg_blank;
        errGuarantorNrcBackLocale = R.string.da_mesg_blank;
        errCriminalClearLocale = R.string.da_mesg_blank;
        errApplicantPhotoLocale = R.string.da_mesg_blank;
        errApplicantSignLocale = R.string.da_mesg_blank;
        errGuarantorSignLocale = R.string.da_mesg_blank;

        financeAmount = view.findViewById(R.id.confirm_finance_amount);
        financeAmount.requestFocus();

        radioLoanTerm = view.findViewById(R.id.rd_btn_loanterm);

        if (!isEmptyOrNull(financeAmount.getText().toString())) {
            verifyLoanAmount();
            radioLoanTerm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = view.findViewById(checkedId);
                    loanTermId = checkedId;
                    radioLoanTerm.check(loanTermId);
                    setLoanTermValue(radioButton.getText().toString());
                    loanCalculation();
                    errLoanTerm.setVisibility(View.GONE);
                }
            });
        }

        btnNrcFront = view.findViewById(R.id.confirm_nrcfront_choose);
        btnNrcBack = view.findViewById(R.id.confirm_nrcback_choose);
        btnIncomeProof = view.findViewById(R.id.confirm_income_proof);
        btnResidentProof = view.findViewById(R.id.confirm_resident_proof);
        btnGuarantorFront = view.findViewById(R.id.confirm_gurfont_choose);
        btnGuarantorBack = view.findViewById(R.id.confirm_gurantor_back);
        btnCriminalClear = view.findViewById(R.id.confirm_criminal_proof);
        btnApplicantPhoto = view.findViewById(R.id.confirm_applicant);
        btnSignature = view.findViewById(R.id.confirm_signature);
        btnGuarantorSign = view.findViewById(R.id.confirm_guarantor_signature);

        nrcFront = view.findViewById(R.id.nrc_front_choose);
        nrcBack = view.findViewById(R.id.nrc_back_choose);
        incomeProof = view.findViewById(R.id.income_proof_choose);
        residentProof = view.findViewById(R.id.resident_proof_choose);
        guarantorNrcFront = view.findViewById(R.id.gurantor_front);
        guarantorNrcBack = view.findViewById(R.id.gurantor_back);
        criminalClearance = view.findViewById(R.id.crinimal_proof);
        applicantPhoto = view.findViewById(R.id.applicant_photo);
        signaturePhoto = view.findViewById(R.id.signature_photo);
        guarantorSignPhoto = view.findViewById(R.id.guarantor_sign_photo);
        textBusinessErrMsg = view.findViewById(R.id.err_business_mesg);

        submitLoan = view.findViewById(R.id.btn_confirm_next);
        preview = view.findViewById(R.id.btn_confirm_preview);

        financeAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (!cs.toString().equals(BLANK)) {
                    String newAmount = cs.toString();
                    char[] chars = newAmount.toCharArray();
                    String input = commaRemove(chars);
                    finance_amount = Double.parseDouble(input);
                    verifyLoanAmount();
                    radioLoanTerm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton radioButton = view.findViewById(checkedId);
                            loanTermId = checkedId;

                            radioButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    radioLoanTerm.check(loanTermId);
                                }
                            });
                            setLoanTermValue(radioButton.getText().toString());
                            loanCalculation();
                            errLoanTerm.setVisibility(View.GONE);
                        }
                    });

                    processingFee.setText(BLANK);
                    compulsorySaving.setText(BLANK);
                    totalRepayment.setText(BLANK);
                    firstRepayment.setText(BLANK);
                    monthlyRepayment.setText(BLANK);
                    lastRepayment.setText(BLANK);
                }
            }

            @Override
            public void afterTextChanged(Editable view) {
                financeAmount.removeTextChangedListener(this);
                try {

                    String originalString = view.toString();
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (originalString.equals(BLANK)) {
                        originalString = "0";
                    }

                    Long longValue = Long.parseLong(originalString);
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern(LOAN_AMOUNT_FORMAT);
                    String formattedString = formatter.format(longValue);

                    //setting text after format to EditText
                    financeAmount.setText(formattedString);
                    financeAmount.setSelection(financeAmount.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                financeAmount.addTextChangedListener(this);
            }
        });


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog previewDialog = new Dialog(getActivity());
                previewDialog.setCancelable(false);
                previewDialog.setContentView(R.layout.application_info_preview_layout);
                previewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                previewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView daDetailClose = previewDialog.findViewById(R.id.da_detail_close);

                cityTownshipList = PreferencesManager.getCityListInfo(getActivity());
                setUpCityList(cityTownshipList);

                /*Application Data - 16 */
                TextView appName = previewDialog.findViewById(R.id.preview_name);
                TextView appDob = previewDialog.findViewById(R.id.preview_dob);
                TextView appNrc = previewDialog.findViewById(R.id.preview_nrc_no);
                TextView appFatherName = previewDialog.findViewById(R.id.preview_father_name);
                TextView appNationality = previewDialog.findViewById(R.id.preview_nationality);
                TextView appGender = previewDialog.findViewById(R.id.preview_gender);
                TextView appMaritalStatus = previewDialog.findViewById(R.id.preview_marital_status);
                TextView appTypeResident = previewDialog.findViewById(R.id.preview_type_of_resident);
                TextView appLivingWith = previewDialog.findViewById(R.id.preview_living_with);
                TextView appYearStay = previewDialog.findViewById(R.id.preview_year_of_stay);
                TextView appMobileNo = previewDialog.findViewById(R.id.preview_mobile_no);
                TextView appResidentTel = previewDialog.findViewById(R.id.preview_resident_tel_no);
                TextView appOtherPhone = previewDialog.findViewById(R.id.preview_other_ph_no);
                TextView appEmail = previewDialog.findViewById(R.id.preview_email);
                TextView appEducation = previewDialog.findViewById(R.id.preview_education);

                /*Occupation Data - 12*/
                TextView occuName = previewDialog.findViewById(R.id.preview_company_name);
                TextView occuCompanyTel = previewDialog.findViewById(R.id.preview_company_tel_no);
                TextView occuContactTime = previewDialog.findViewById(R.id.preview_contact_time);
                TextView occuDepartment = previewDialog.findViewById(R.id.preview_department);
                TextView occuPosition = previewDialog.findViewById(R.id.preview_position);
                TextView occuYearService = previewDialog.findViewById(R.id.preview_year_of_service);
                TextView occuCompanyStatus = previewDialog.findViewById(R.id.preview_company_status);
                TextView occuMonthlyIncome = previewDialog.findViewById(R.id.preview_monthly_basic_income);
                TextView occuOtherIncome = previewDialog.findViewById(R.id.preview_other_income);
                TextView occuTotalIncome = previewDialog.findViewById(R.id.preview_total_income);
                TextView occuSalaryDate = previewDialog.findViewById(R.id.preview_salary_date);

                /*Emergency Contact - 6*/
                TextView emerName = previewDialog.findViewById(R.id.preview_eme_name);
                TextView emerRelationshipWith = previewDialog.findViewById(R.id.preview_eme_relationship_with);
                TextView emerMobileNo = previewDialog.findViewById(R.id.preview_eme_mobile_no);
                TextView emerResidentTel = previewDialog.findViewById(R.id.preview_eme_resident_tel_no);
                TextView emerOtherPhone = previewDialog.findViewById(R.id.preview_eme_other_ph_no);

                /*Guarantor Data - 21*/
                TextView guaName = previewDialog.findViewById(R.id.preview_gur_name);
                TextView guaDob = previewDialog.findViewById(R.id.preview_gur_dob);
                TextView guaNrc = previewDialog.findViewById(R.id.preview_gur_nrc_no);
                TextView guaNationality = previewDialog.findViewById(R.id.preview_gur_nationality);
                TextView guaMobileNo = previewDialog.findViewById(R.id.preview_gur_mobile_no);
                TextView guaResidentTel = previewDialog.findViewById(R.id.preview_gur_resident_tel_no);
                TextView guaRelationshipWith = previewDialog.findViewById(R.id.preview_gur_relation_with);
                TextView guaTypeResident = previewDialog.findViewById(R.id.preview_gur_type_of_resident);
                TextView guaLivingWith = previewDialog.findViewById(R.id.preview_gur_live_with);
                TextView guaGender = previewDialog.findViewById(R.id.preview_gur_gender);
                TextView guaMarialStatus = previewDialog.findViewById(R.id.preview_gur_marital_status);
                TextView guaYearStay = previewDialog.findViewById(R.id.preview_gur_year_of_stay);
                TextView guaCompanyName = previewDialog.findViewById(R.id.preview_gur_company_name);
                TextView guaCompanyTelNo = previewDialog.findViewById(R.id.preview_gur_company_tel);
                TextView guaDepartment = previewDialog.findViewById(R.id.preview_gur_company_department);
                TextView guaPosition = previewDialog.findViewById(R.id.preview_gur_position);
                TextView guaYearService = previewDialog.findViewById(R.id.preview_gur_year_of_service);
                TextView guaMonthlyBasic = previewDialog.findViewById(R.id.preview_gur_monthly_basic_income);
                TextView guaTotalIncome = previewDialog.findViewById(R.id.preview_gur_total_income);

                TextView confFinanceAmount = previewDialog.findViewById(R.id.preview_lc_finance_amt);
                TextView confTermOfFinance = previewDialog.findViewById(R.id.preview_lc_finance_term);
                TextView confProcessingFee = previewDialog.findViewById(R.id.preview_lc_processing_fee);
                TextView confCompulsorySave = previewDialog.findViewById(R.id.preview_lc_comp_saving);
                TextView confTotalRepay = previewDialog.findViewById(R.id.preview_lc_total_amt);
                TextView confFirstRepay = previewDialog.findViewById(R.id.preview_first_repay_amt);
                TextView confMonthlyRepay = previewDialog.findViewById(R.id.preview_lc_month_repay_amt);
                TextView confLastRepay = previewDialog.findViewById(R.id.preview_lc_last_repay);

                String curlang = PreferencesManager.getCurrentLanguage(getActivity());

                //Set Application Data.
                TextView app_data_name_title = previewDialog.findViewById(R.id.app_lbl_name);
                app_data_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_name, getActivity()));

                TextView app_data_dob_title = previewDialog.findViewById(R.id.app_lbl_dob);
                app_data_dob_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_dob, getActivity()));

                TextView app_data_nrc_no_title = previewDialog.findViewById(R.id.app_lbl_nrc_no);
                app_data_nrc_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_nrc_no, getActivity()));

                TextView app_data_father_name_title = previewDialog.findViewById(R.id.app_lbl_father_name);
                app_data_father_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_father_name, getActivity()));

                TextView app_data_ecucation_title = previewDialog.findViewById(R.id.app_lbl_education);
                app_data_ecucation_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_education, getActivity()));

                TextView app_data_nationality_title = previewDialog.findViewById(R.id.app_lbl_Nationality);
                app_data_nationality_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_nationality, getActivity()));

                TextView app_data_gender_title = previewDialog.findViewById(R.id.app_lbl_gender);
                app_data_gender_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_gender, getActivity()));

                TextView app_data_marital_status_title = previewDialog.findViewById(R.id.app_lbl_marital_status);
                app_data_marital_status_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_marital_status, getActivity()));

                TextView app_data_type_of_residence_title = previewDialog.findViewById(R.id.app_lbl_type_of_resident);
                app_data_type_of_residence_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_type_of_residence, getActivity()));

                TextView app_data_living_with_title = previewDialog.findViewById(R.id.app_lbl_living_with);
                app_data_living_with_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_living_with, getActivity()));

                TextView app_data_year_of_stay_title = previewDialog.findViewById(R.id.app_lbl_year_of_stay);
                app_data_year_of_stay_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_year_of_stay, getActivity()));

                TextView app_data_mobile_no_title = previewDialog.findViewById(R.id.app_lbl_mobile_no);
                app_data_mobile_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_mobile_no, getActivity()));

                TextView app_data_resident_tel_no_title = previewDialog.findViewById(R.id.app_lbl_resident_tel_no);
                app_data_resident_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_resident_tel_no, getActivity()));

                TextView app_data_other_phone_no_title = previewDialog.findViewById(R.id.app_lbl_other_ph_no);
                app_data_other_phone_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_other_phone_no, getActivity()));

                TextView app_data_email_title = previewDialog.findViewById(R.id.app_lbl_email);
                app_data_email_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_app_data_email, getActivity()));

                //Set Occupation Data.
                TextView occu_company_name_title = previewDialog.findViewById(R.id.occu_lbl_company_name);
                occu_company_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_company_name, getActivity()));

                TextView occu_company_tel_no_title = previewDialog.findViewById(R.id.occu_lbl_company_tel_no);
                occu_company_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_company_tel_no, getActivity()));

                TextView occu_contact_time_title = previewDialog.findViewById(R.id.occu_lbl_contact_time);
                occu_contact_time_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_contact_time, getActivity()));

                TextView occu_department_title = previewDialog.findViewById(R.id.occu_lbl_department);
                occu_department_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_department, getActivity()));

                TextView occu_data_position_title = previewDialog.findViewById(R.id.occu_lbl_position);
                occu_data_position_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_position, getActivity()));

                TextView occu_year_of_service_title = previewDialog.findViewById(R.id.occu_lbl_year_of_service);
                occu_year_of_service_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_year_of_service, getActivity()));

                TextView occu_company_status_title = previewDialog.findViewById(R.id.occu_lbl_company_status);
                occu_company_status_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_company_status, getActivity()));

                TextView occu_monthly_basic_income_title = previewDialog.findViewById(R.id.occu_lbl_monthly_basic_income);
                occu_monthly_basic_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_monthly_basic_income, getActivity()));

                TextView occu_other_income_title = previewDialog.findViewById(R.id.occu_lbl_other_income);
                occu_other_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_other_income, getActivity()));

                TextView occu_total_income_title = previewDialog.findViewById(R.id.occu_lbl_total_income);
                occu_total_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_total_income, getActivity()));

                TextView occu_salary_date_title = previewDialog.findViewById(R.id.occu_lbl_salary_date);
                occu_salary_date_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_occu_salary_date, getActivity()));

                //Set Emergency Contact.
                TextView da_in_eme_name_title = previewDialog.findViewById(R.id.eme_lbl_name);
                da_in_eme_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_name, getActivity()));

                TextView da_in_eme_relationship_with_applier_title = previewDialog.findViewById(R.id.eme_lbl_relation_with);
                da_in_eme_relationship_with_applier_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_relationship_with_applier, getActivity()));

                TextView da_in_eme_mobile_no_title = previewDialog.findViewById(R.id.eme_lbl_mobile_no);
                da_in_eme_mobile_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_mobile_no, getActivity()));

                TextView da_in_eme_resident_tel_no_title = previewDialog.findViewById(R.id.eme_lbl_resident_tel_no);
                da_in_eme_resident_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_resident_tel_no, getActivity()));

                TextView da_in_eme_other_phone_no_title = previewDialog.findViewById(R.id.eme_lbl_other_ph_no);
                da_in_eme_other_phone_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_eme_other_phone_no, getActivity()));

                //Set Guarantor.
                TextView da_in_gur_name_title = previewDialog.findViewById(R.id.gua_lbl_name);
                da_in_gur_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_name, getActivity()));

                TextView da_in_gur_dob_title = previewDialog.findViewById(R.id.gur_lbl_dob);
                da_in_gur_dob_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_dob, getActivity()));

                TextView da_in_gur_nrc_no_title = previewDialog.findViewById(R.id.gur_lbl_nrc_no);
                da_in_gur_nrc_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_nrc_no, getActivity()));

                TextView da_in_gur_nationality_title = previewDialog.findViewById(R.id.gur_lbl_nationality);
                da_in_gur_nationality_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_nationality, getActivity()));

                TextView da_in_gur_mobile_no_title = previewDialog.findViewById(R.id.gur_lbl_mobile_no);
                da_in_gur_mobile_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_mobile_no, getActivity()));

                TextView da_in_gur_resident_tel_no_title = previewDialog.findViewById(R.id.gur_lbl_resident_tel_no);
                da_in_gur_resident_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_resident_tel_no, getActivity()));

                TextView da_in_gur_relationship_with_applier_title = previewDialog.findViewById(R.id.gur_lbl_relation_with);
                da_in_gur_relationship_with_applier_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_relationship_with_applier, getActivity()));

                TextView da_in_gur_type_of_resident_title = previewDialog.findViewById(R.id.gur_lbl_type_of_resident);
                da_in_gur_type_of_resident_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_type_of_resident, getActivity()));

                TextView da_in_gur_living_with_title = previewDialog.findViewById(R.id.gur_lbl_live_with);
                da_in_gur_living_with_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_living_with, getActivity()));

                TextView da_in_gur_gender_title = previewDialog.findViewById(R.id.gur_lbl_gender);
                da_in_gur_gender_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_gender, getActivity()));

                TextView da_in_gur_marital_status_title = previewDialog.findViewById(R.id.gur_lbl_marital_status);
                da_in_gur_marital_status_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_marital_status, getActivity()));

                TextView da_in_gur_year_of_stay_title = previewDialog.findViewById(R.id.gur_lbl_year_of_stay);
                da_in_gur_year_of_stay_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_year_of_stay, getActivity()));

                TextView da_in_gur_company_name_title = previewDialog.findViewById(R.id.gur_lbl_company_name);
                da_in_gur_company_name_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_company_name, getActivity()));

                TextView da_in_gur_company_tel_no_title = previewDialog.findViewById(R.id.gur_lbl_company_tel_no);
                da_in_gur_company_tel_no_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_company_tel_no, getActivity()));

                TextView da_in_gur_department_title = previewDialog.findViewById(R.id.gur_lbl_department);
                da_in_gur_department_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_department, getActivity()));

                TextView da_in_gur_position_title = previewDialog.findViewById(R.id.gur_lbl_position);
                da_in_gur_position_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_position, getActivity()));

                TextView da_in_gur_year_of_service_title = previewDialog.findViewById(R.id.gur_lbl_year_of_service);
                da_in_gur_year_of_service_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_year_of_service, getActivity()));

                TextView da_in_gur_monthly_basic_income_title = previewDialog.findViewById(R.id.gur_lbl_monthly_basic_income);
                da_in_gur_monthly_basic_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_monthly_basic_income, getActivity()));

                TextView da_in_gur_total_income_title = previewDialog.findViewById(R.id.gur_lbl_total_income);
                da_in_gur_total_income_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_gur_total_income, getActivity()));

                TextView da_in_loanConfirm_finance_amount_title = previewDialog.findViewById(R.id.conf_lbl_finance_amt);
                da_in_loanConfirm_finance_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_finance_amount, getActivity()));

                TextView da_in_loanConfirm_term_of_finance_title = previewDialog.findViewById(R.id.conf_lbl_finance_term);
                da_in_loanConfirm_term_of_finance_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_term_of_finance, getActivity()));

                TextView da_in_loanConfirm_processing_fee_title = previewDialog.findViewById(R.id.conf_lbl_processing_fee);
                da_in_loanConfirm_processing_fee_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_processing_fee, getActivity()));

                TextView da_in_loanConfirm_compulsory_saving_title = previewDialog.findViewById(R.id.conf_lbl_comp_saving);
                da_in_loanConfirm_compulsory_saving_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_compulsory_saving, getActivity()));

                TextView da_in_loanConfirm_total_repayment_amount_title = previewDialog.findViewById(R.id.conf_lbl_total_pay_amt);
                da_in_loanConfirm_total_repayment_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_total_repayment_amount, getActivity()));

                TextView da_in_loanConfirm_first_repayment_amount_title = previewDialog.findViewById(R.id.conf_lbl_first_repay_amt);
                da_in_loanConfirm_first_repayment_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_first_repayment_amount, getActivity()));

                TextView da_in_loanConfirm_monthly_repayment_amount_title = previewDialog.findViewById(R.id.conf_lbl_monthly_repay_amt);
                da_in_loanConfirm_monthly_repayment_amount_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_monthly_repayment_amount, getActivity()));

                TextView da_in_loanConfirm_last_payment_title = previewDialog.findViewById(R.id.conf_lbl_last_repay);
                da_in_loanConfirm_last_payment_title.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_in_loanConfirm_last_payment, getActivity()));

                ApplicationRegisterSaveReqBean registerPreviewBean
                        = PreferencesManager.getDaftSavedInfo(getActivity());
                OccupationDataFormBean occupationBean = registerPreviewBean.getApplicantCompanyInfoDto();
                EmergencyContactFormBean emergencyContactDataBean = registerPreviewBean.getEmergencyContactInfoDto();
                GuarantorFormBean guarantorDataBean = registerPreviewBean.getGuarantorInfoDto();
                appName.setText(registerPreviewBean.getName());
                appDob.setText(registerPreviewBean.getDob());
                appNrc.setText(registerPreviewBean.getNrcNo());
                appFatherName.setText(getStringValue(registerPreviewBean.getFatherName()));
                appEducation.setText(getEducationPreview(registerPreviewBean.getHighestEducationTypeId()));
                appNationality.setText(getNationalityPreview(registerPreviewBean.getNationality(), registerPreviewBean.getNationalityOther()));
                appGender.setText(getGender(registerPreviewBean.getGender()));
                appMaritalStatus.setText(getMaritalStatus(registerPreviewBean.getMaritalStatus()));
                appTypeResident.setText(getResidentTypePreview(registerPreviewBean.getTypeOfResidence(), registerPreviewBean.getTypeOfResidenceOther()));
                appLivingWith.setText(getLivingWith(registerPreviewBean.getLivingWith()));
                appYearStay.setText(getYearOfStay(registerPreviewBean.getYearOfStayYear(), registerPreviewBean.getYearOfStayMonth()));
                appMobileNo.setText(registerPreviewBean.getMobileNo());
                appResidentTel.setText(getStringValue(registerPreviewBean.getResidentTelNo()));
                appOtherPhone.setText(getStringValue(registerPreviewBean.getOtherPhoneNo()));
                appEmail.setText(getStringValue(registerPreviewBean.getEmail()));

                occuName.setText(getStringValue(occupationBean.getCompanyName()));
                occuCompanyTel.setText(getStringValue(occupationBean.getCompanyTelNo()));
                occuContactTime.setText(getContactTime(occupationBean.getContactTimeFrom(), occupationBean.getContactTimeTo()));
                occuDepartment.setText(getStringValue(occupationBean.getDepartment()));
                occuPosition.setText(getStringValue(occupationBean.getPosition()));
                occuYearService.setText(getYearOfService(occupationBean.getYearOfServiceYear(), occupationBean.getYearOfServiceMonth()));
                occuCompanyStatus.setText(getCompanyStatusPreview(occupationBean.getCompanyStatus(), occupationBean.getCompanyStatusOther()));
                occuMonthlyIncome.setText(getBasicIncome(occupationBean.getMonthlyBasicIncome()));
                occuOtherIncome.setText(getOtherIncome(occupationBean.getOtherIncome()));
                occuTotalIncome.setText(getTotalIncome(occupationBean.getTotalIncome()));
                String salaryDayFormat = "Day " + String.valueOf(occupationBean.getSalaryDay());
                occuSalaryDate.setText(salaryDayFormat);

                emerName.setText(getStringValue(emergencyContactDataBean.getName()));
                emerRelationshipWith.setText(getApplicationRelationPreview(emergencyContactDataBean.getRelationship(), emergencyContactDataBean.getRelationshipOther()));
                emerMobileNo.setText(getStringValue(emergencyContactDataBean.getMobileNo()));
                emerResidentTel.setText(getStringValue(emergencyContactDataBean.getResidentTelNo()));
                emerOtherPhone.setText(getStringValue(emergencyContactDataBean.getOtherPhoneNo()));

                guaName.setText(getStringValue(guarantorDataBean.getName()));
                guaDob.setText(guarantorDataBean.getDob());
                guaNrc.setText(getStringValue(guarantorDataBean.getNrcNo()));
                guaNationality.setText(getNationalityPreview(guarantorDataBean.getNationality(), guarantorDataBean.getNationalityOther()));
                guaMobileNo.setText(getStringValue(guarantorDataBean.getMobileNo()));
                guaResidentTel.setText(getStringValue(guarantorDataBean.getResidentTelNo()));
                guaRelationshipWith.setText(getApplicationRelationPreview(guarantorDataBean.getRelationship(), guarantorDataBean.getRelationshipOther()));
                guaTypeResident.setText(getResidentTypePreview(guarantorDataBean.getTypeOfResidence(), guarantorDataBean.getTypeOfResidenceOther()));
                guaLivingWith.setText(getLivingWith(guarantorDataBean.getLivingWith()));
                guaGender.setText(getGender(guarantorDataBean.getGender()));
                guaMarialStatus.setText(getMaritalStatus(guarantorDataBean.getMaritalStatus()));
                guaYearStay.setText(getYearOfStay(guarantorDataBean.getYearOfStayYear(), guarantorDataBean.getYearOfStayMonth()));
                guaCompanyName.setText(getStringValue(guarantorDataBean.getCompanyName()));
                guaCompanyTelNo.setText(getStringValue(guarantorDataBean.getCompanyTelNo()));
                guaDepartment.setText(getStringValue(guarantorDataBean.getDepartment()));
                guaPosition.setText(getStringValue(guarantorDataBean.getPosition()));
                guaYearService.setText(getYearOfService(guarantorDataBean.getYearOfServiceYear(), guarantorDataBean.getYearOfServiceMonth()));
                guaMonthlyBasic.setText(getBasicIncome(guarantorDataBean.getMonthlyBasicIncome()));
                guaTotalIncome.setText(getTotalIncome(guarantorDataBean.getTotalIncome()));

                confFinanceAmount.setText(getFinanceAmount(finance_amount));
                confTermOfFinance.setText(getLoanTerm(loanTerm));
                confProcessingFee.setText(previewProcessingFee + MYANMAR_CURRENCY);
                confCompulsorySave.setText(previewCompulsorySaving + MYANMAR_CURRENCY);
                confTotalRepay.setText(previewTotalRepayment + MYANMAR_CURRENCY);
                confFirstRepay.setText(previewFirstRepayment + MYANMAR_CURRENCY);
                confMonthlyRepay.setText(previewMonthlyRepayment + MYANMAR_CURRENCY);
                confLastRepay.setText(previewLastRepayment + MYANMAR_CURRENCY);

                /*Current Address*/
                TextView app_current_buildingno = previewDialog.findViewById(R.id.lbl_preview_current_buildingNo);
                app_current_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                TextView value_current_buildingno = previewDialog.findViewById(R.id.val_preview_current_buildingNo);
                value_current_buildingno.setText(getStringValue(registerPreviewBean.getCurrentAddressBuildingNo()));

                TextView app_current_roomno = previewDialog.findViewById(R.id.lbl_preview_current_room_no);
                app_current_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                TextView value_current_roomno = previewDialog.findViewById(R.id.val_preview_current_room_no);
                value_current_roomno.setText(getStringValue(registerPreviewBean.getCurrentAddressRoomNo()));

                TextView app_current_floorno = previewDialog.findViewById(R.id.lbl_preview_current_floor_no);
                app_current_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                TextView value_current_floorno = previewDialog.findViewById(R.id.val_preview_current_floor_no);
                value_current_floorno.setText(getStringValue(registerPreviewBean.getCurrentAddressFloor()));

                TextView app_current_street = previewDialog.findViewById(R.id.lbl_preview_current_street);
                app_current_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                TextView value_current_street = previewDialog.findViewById(R.id.val_preview_current_street);
                value_current_street.setText(getStringValue(registerPreviewBean.getCurrentAddressStreet()));

                TextView app_current_quarter = previewDialog.findViewById(R.id.lbl_preview_current_quarter);
                app_current_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                TextView value_current_quarter = previewDialog.findViewById(R.id.val_preview_current_quarter);
                value_current_quarter.setText(getStringValue(registerPreviewBean.getCurrentAddressQtr()));

                TextView app_current_city = previewDialog.findViewById(R.id.lbl_preview_current_city);
                app_current_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                TextView value_current_city = previewDialog.findViewById(R.id.val_preview_current_city);
                value_current_city.setText(getStringValue(getCity(cityId, cityList, registerPreviewBean.getCurrentAddressCity())));

                TextView app_current_township = previewDialog.findViewById(R.id.lbl_preview_current_township);
                app_current_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                TextView value_current_township = previewDialog.findViewById(R.id.val_preview_current_township);
                value_current_township.setText(getStringValue(getTownship(cityTownshipList, registerPreviewBean.getCurrentAddressCity(), registerPreviewBean.getCurrentAddressTownship())));

                /*Permanent Address*/
                TextView app_permanent_buildingno = previewDialog.findViewById(R.id.lbl_preview_permanent_buildingNo);
                app_permanent_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                TextView value_permanent_buildingno = previewDialog.findViewById(R.id.val_preview_permanent_buildingNo);
                value_permanent_buildingno.setText(getStringValue(registerPreviewBean.getPermanentAddressBuildingNo()));

                TextView app_permanent_roomno = previewDialog.findViewById(R.id.lbl_preview_permanent_room_no);
                app_permanent_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                TextView value_permanent_roomno = previewDialog.findViewById(R.id.val_preview_permanent_room_no);
                value_permanent_roomno.setText(getStringValue(registerPreviewBean.getPermanentAddressRoomNo()));

                TextView app_permanent_floorno = previewDialog.findViewById(R.id.lbl_preview_permanent_floor_no);
                app_permanent_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                TextView value_permanent_floorno = previewDialog.findViewById(R.id.val_preview_permanent_floor_no);
                value_permanent_floorno.setText(getStringValue(registerPreviewBean.getPermanentAddressFloor()));

                TextView app_permanent_street = previewDialog.findViewById(R.id.lbl_preview_permanent_street);
                app_permanent_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                TextView value_permanent_street = previewDialog.findViewById(R.id.val_preview_permanent_street);
                value_permanent_street.setText(getStringValue(registerPreviewBean.getPermanentAddressStreet()));

                TextView app_permanent_quarter = previewDialog.findViewById(R.id.lbl_preview_permanent_quarter);
                app_permanent_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                TextView value_permanent_quarter = previewDialog.findViewById(R.id.val_preview_permanent_quarter);
                value_permanent_quarter.setText(getStringValue(registerPreviewBean.getPermanentAddressQtr()));

                TextView app_permanent_city = previewDialog.findViewById(R.id.lbl_preview_permanent_city);
                app_permanent_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                TextView value_permanent_city = previewDialog.findViewById(R.id.val_preview_permanent_city);
                value_permanent_city.setText(getStringValue(getCity(cityId, cityList, registerPreviewBean.getPermanentAddressCity())));

                TextView app_permanent_township = previewDialog.findViewById(R.id.lbl_preview_permanent_township);
                app_permanent_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                TextView value_permanent_township = previewDialog.findViewById(R.id.val_preview_permanent_township);
                value_permanent_township.setText(getStringValue(getTownship(cityTownshipList, registerPreviewBean.getPermanentAddressCity(), registerPreviewBean.getPermanentAddressTownship())));

                /*Occupation Company Address*/
                TextView app_occupation_buildingno = previewDialog.findViewById(R.id.lbl_preview_occupation_buildingNo);
                app_occupation_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                TextView value_occupation_buildingno = previewDialog.findViewById(R.id.val_preview_occupation_buildingNo);
                value_occupation_buildingno.setText(getStringValue(occupationBean.getCompanyAddressBuildingNo()));

                TextView app_occupation_roomno = previewDialog.findViewById(R.id.lbl_preview_occupation_room_no);
                app_occupation_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                TextView value_occupation_roomno = previewDialog.findViewById(R.id.val_preview_occupation_room_no);
                value_occupation_roomno.setText(getStringValue(occupationBean.getCompanyAddressRoomNo()));

                TextView app_occupation_floorno = previewDialog.findViewById(R.id.lbl_preview_occupation_floor_no);
                app_occupation_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                TextView value_occupation_floorno = previewDialog.findViewById(R.id.val_preview_occupation_floor_no);
                value_occupation_floorno.setText(getStringValue(occupationBean.getCompanyAddressFloor()));

                TextView app_occupation_street = previewDialog.findViewById(R.id.lbl_preview_occupation_street);
                app_occupation_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                TextView value_occupation_street = previewDialog.findViewById(R.id.val_preview_occupation_street);
                value_occupation_street.setText(getStringValue(occupationBean.getCompanyAddressStreet()));

                TextView app_occupation_quarter = previewDialog.findViewById(R.id.lbl_preview_occupation_quarter);
                app_occupation_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                TextView value_occupation_quarter = previewDialog.findViewById(R.id.val_preview_occupation_quarter);
                value_occupation_quarter.setText(getStringValue(occupationBean.getCompanyAddressQtr()));

                TextView app_occupation_city = previewDialog.findViewById(R.id.lbl_preview_occupation_city);
                app_occupation_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                TextView value_occupation_city = previewDialog.findViewById(R.id.val_preview_occupation_city);
                value_occupation_city.setText(getStringValue(getCity(cityId, cityList, occupationBean.getCompanyAddressCity())));

                TextView app_occupation_township = previewDialog.findViewById(R.id.lbl_preview_occupation_township);
                app_occupation_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                TextView value_occupation_township = previewDialog.findViewById(R.id.val_preview_occupation_township);
                value_occupation_township.setText(getStringValue(getTownship(cityTownshipList, occupationBean.getCompanyAddressCity(), occupationBean.getCompanyAddressTownship())));

                /*Emergency Contact Address*/
                TextView app_emergency_buildingno = previewDialog.findViewById(R.id.lbl_preview_emergency_buildingNo);
                app_emergency_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                TextView value_emergency_buildingno = previewDialog.findViewById(R.id.val_preview_emergency_buildingNo);
                value_emergency_buildingno.setText(getStringValue(emergencyContactDataBean.getCurrentAddressBuildingNo()));

                TextView app_emergency_roomno = previewDialog.findViewById(R.id.lbl_preview_emergency_room_no);
                app_emergency_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                TextView value_emergency_roomno = previewDialog.findViewById(R.id.val_preview_emergency_room_no);
                value_emergency_roomno.setText(getStringValue(emergencyContactDataBean.getCurrentAddressRoomNo()));

                TextView app_emergency_floorno = previewDialog.findViewById(R.id.lbl_preview_emergency_floor_no);
                app_emergency_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                TextView value_emergency_floorno = previewDialog.findViewById(R.id.val_preview_emergency_floor_no);
                value_emergency_floorno.setText(getStringValue(emergencyContactDataBean.getCurrentAddressFloor()));

                TextView app_emergency_street = previewDialog.findViewById(R.id.lbl_preview_emergency_street);
                app_emergency_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                TextView value_emergency_street = previewDialog.findViewById(R.id.val_preview_emergency_street);
                value_emergency_street.setText(getStringValue(emergencyContactDataBean.getCurrentAddressStreet()));

                TextView app_emergency_quarter = previewDialog.findViewById(R.id.lbl_preview_emergency_quarter);
                app_emergency_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                TextView value_emergency_quarter = previewDialog.findViewById(R.id.val_preview_emergency_quarter);
                value_emergency_quarter.setText(getStringValue(emergencyContactDataBean.getCurrentAddressQtr()));

                TextView app_emergency_city = previewDialog.findViewById(R.id.lbl_preview_emergency_city);
                app_emergency_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                TextView value_emergency_city = previewDialog.findViewById(R.id.val_preview_emergency_city);
                value_emergency_city.setText(getStringValue(getCity(cityId, cityList, emergencyContactDataBean.getCurrentAddressCity())));

                TextView app_emergency_township = previewDialog.findViewById(R.id.lbl_preview_emergency_township);
                app_emergency_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                TextView value_emergency_township = previewDialog.findViewById(R.id.val_preview_emergency_township);
                value_emergency_township.setText(getStringValue(getTownship(cityTownshipList, emergencyContactDataBean.getCurrentAddressCity(), emergencyContactDataBean.getCurrentAddressTownship())));

                /*Guarantor Current Address*/
                TextView cur_guarantor_buildingno = previewDialog.findViewById(R.id.lbl_preview_cur_guarantor_buildingNo);
                cur_guarantor_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                TextView value_cur_guarantor_buildingno = previewDialog.findViewById(R.id.val_preview_cur_guarantor_buildingNo);
                value_cur_guarantor_buildingno.setText(getStringValue(guarantorDataBean.getCurrentAddressBuildingNo()));

                TextView cur_guarantor_roomno = previewDialog.findViewById(R.id.lbl_preview_cur_guarantor_room_no);
                cur_guarantor_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                TextView value_cur_guarantor_roomno = previewDialog.findViewById(R.id.val_preview_cur_guarantor_room_no);
                value_cur_guarantor_roomno.setText(getStringValue(guarantorDataBean.getCurrentAddressRoomNo()));

                TextView cur_guarantor_floorno = previewDialog.findViewById(R.id.lbl_preview_cur_guarantor_floor_no);
                cur_guarantor_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                TextView value_cur_guarantor_floorno = previewDialog.findViewById(R.id.val_preview_cur_guarantor_floor_no);
                value_cur_guarantor_floorno.setText(getStringValue(guarantorDataBean.getCurrentAddressFloor()));

                TextView cur_guarantor_street = previewDialog.findViewById(R.id.lbl_preview_cur_guarantor_street);
                cur_guarantor_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                TextView value_cur_guarantor_street = previewDialog.findViewById(R.id.val_preview_cur_guarantor_street);
                value_cur_guarantor_street.setText(getStringValue(guarantorDataBean.getCurrentAddressStreet()));

                TextView cur_guarantor_quarter = previewDialog.findViewById(R.id.lbl_preview_cur_guarantor_quarter);
                cur_guarantor_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                TextView value_cur_guarantor_quarter = previewDialog.findViewById(R.id.val_preview_cur_guarantor_quarter);
                value_cur_guarantor_quarter.setText(getStringValue(guarantorDataBean.getCurrentAddressQtr()));

                TextView cur_guarantor_city = previewDialog.findViewById(R.id.lbl_preview_cur_guarantor_city);
                cur_guarantor_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                TextView value_cur_guarantor_city = previewDialog.findViewById(R.id.val_preview_cur_guarantor_city);
                value_cur_guarantor_city.setText(getStringValue(getCity(cityId, cityList, guarantorDataBean.getCurrentAddressCity())));

                TextView cur_guarantor_township = previewDialog.findViewById(R.id.lbl_preview_cur_guarantor_township);
                cur_guarantor_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_township, getActivity()));
                TextView value_cur_guarantor_township = previewDialog.findViewById(R.id.val_preview_cur_guarantor_township);
                value_cur_guarantor_township.setText(getStringValue(getTownship(cityTownshipList, guarantorDataBean.getCurrentAddressCity(), guarantorDataBean.getCurrentAddressTownship())));

                /*Guarantor Company Address*/
                TextView com_guarantor_buildingno = previewDialog.findViewById(R.id.lbl_preview_com_guarantor_buildingNo);
                com_guarantor_buildingno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_apartment_no, getActivity()));
                TextView value_com_guarantor_buildingno = previewDialog.findViewById(R.id.val_preview_com_guarantor_buildingNo);
                value_com_guarantor_buildingno.setText(getStringValue(guarantorDataBean.getCompanyAddressBuildingNo()));

                TextView com_guarantor_roomno = previewDialog.findViewById(R.id.lbl_preview_com_guarantor_room_no);
                com_guarantor_roomno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_room_no, getActivity()));
                TextView value_com_guarantor_roomno = previewDialog.findViewById(R.id.val_preview_com_guarantor_room_no);
                value_com_guarantor_roomno.setText(getStringValue(guarantorDataBean.getCompanyAddressRoomNo()));

                TextView com_guarantor_floorno = previewDialog.findViewById(R.id.lbl_preview_com_guarantor_floor_no);
                com_guarantor_floorno.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_applicant_floor_no, getActivity()));
                TextView value_com_guarantor_floorno = previewDialog.findViewById(R.id.val_preview_com_guarantor_floor_no);
                value_com_guarantor_floorno.setText(getStringValue(guarantorDataBean.getCompanyAddressFloor()));

                TextView com_guarantor_street = previewDialog.findViewById(R.id.lbl_preview_com_guarantor_street);
                com_guarantor_street.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_street, getActivity()));
                TextView value_com_guarantor_street = previewDialog.findViewById(R.id.val_preview_com_guarantor_street);
                value_com_guarantor_street.setText(getStringValue(guarantorDataBean.getCompanyAddressStreet()));

                TextView com_guarantor_quarter = previewDialog.findViewById(R.id.lbl_preview_com_guarantor_quarter);
                com_guarantor_quarter.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                TextView value_com_guarantor_quarter = previewDialog.findViewById(R.id.val_preview_com_guarantor_quarter);
                value_com_guarantor_quarter.setText(getStringValue(guarantorDataBean.getCompanyAddressQtr()));

                TextView com_guarantor_city = previewDialog.findViewById(R.id.lbl_preview_com_guarantor_city);
                com_guarantor_city.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_city, getActivity()));
                TextView value_com_guarantor_city = previewDialog.findViewById(R.id.val_preview_com_guarantor_city);
                value_com_guarantor_city.setText(getStringValue(getCity(cityId, cityList, guarantorDataBean.getCompanyAddressCity())));

                TextView com_guarantor_township = previewDialog.findViewById(R.id.lbl_preview_com_guarantor_township);
                com_guarantor_township.setText(CommonUtils.getLocaleString(new Locale(curlang), R.string.da_app_data_quarter, getActivity()));
                TextView value_com_guarantor_township = previewDialog.findViewById(R.id.val_preview_com_guarantor_township);
                value_com_guarantor_township.setText(getStringValue(getTownship(cityTownshipList, guarantorDataBean.getCompanyAddressCity(), guarantorDataBean.getCompanyAddressTownship())));

                daDetailClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previewDialog.dismiss();
                    }
                });

                //Set Attachments.
                List<ApplicationInfoPhotoBean> applicationInfoAttachmentResBeanList = photoList;
                RecyclerView rvPreviewAttach = previewDialog.findViewById(R.id.preview_attach_img);
                ApplicationPreviewAttachImgAdapter adapter = new ApplicationPreviewAttachImgAdapter(applicationInfoAttachmentResBeanList, SmallLoanConfirmFragment.this);
                rvPreviewAttach.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                rvPreviewAttach.setAdapter(adapter);
                previewDialog.show();
            }
        });

        submitLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate = true;
                MainMenuActivityDrawer.isSubmitclickGuaData = true;
                MainMenuActivityDrawer.isSubmitclickAppData = true;
                MainMenuActivityDrawer.isSubmitclickOccuData = true;
                MainMenuActivityDrawer.isSubmitclickEmerData = true;
                MainMenuActivityDrawer.isSubmitclickConfirmData = true;

                setUpLoanConfirmFormData();
                checkConfirmData();
                checkErrorPage();

                if (!validate) {
                    showErrorMsg();
                }

                if (errorPages.size() == 0) {
                    if (!CommonUtils.isNetworkAvailable(getActivity())) {
                        showNetworkErrorDialog(getActivity(), getNetErrMsg());
                    } else {

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.application_confirm_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        final EditText etPassword = dialog.findViewById(R.id.txt_user_pwd);
                        final TextView tvPwdErr = dialog.findViewById(R.id.err_conf_pwd);
                        final TextView tvCheckErr = dialog.findViewById(R.id.err_term_accept);
                        tvCheckErr.setVisibility(View.GONE);
                        tvPwdErr.setVisibility(View.GONE);
                        final CheckBox confirmRegister = dialog.findViewById(R.id.confirmation_checkbox_accept);
                        confirmRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                                if (check) {
                                    termAndConditionCheck = true;
                                    tvCheckErr.setVisibility(View.GONE);
                                } else {
                                    termAndConditionCheck = false;
                                }
                            }
                        });

                        Button btnSubmit = dialog.findViewById(R.id.btn_app_submit);
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String textPassword = etPassword.getText().toString();
                                if (!confirmRegister.isChecked()) {
                                    termAndConditionCheck = false;
                                    tvCheckErr.setVisibility(View.VISIBLE);
                                } else {
                                    termAndConditionCheck = true;
                                    tvCheckErr.setVisibility(View.GONE);
                                }
                                if (textPassword.equals(BLANK)) {
                                    passwordBlank = false;
                                    tvPwdErr.setVisibility(View.VISIBLE);
                                } else {
                                    passwordBlank = true;
                                    tvPwdErr.setVisibility(View.GONE);
                                }

                                if (termAndConditionCheck && passwordBlank) {
                                    PasswordCheckReqBean passwordCheckReqBean
                                            = new PasswordCheckReqBean();

                                    passwordCheckReqBean.setCustomerId(customerId);
                                    passwordCheckReqBean.setPassword(textPassword);
                                    String accessToken = PreferencesManager.getAccessToken(getActivity());

                                    final ProgressDialog checkPasswordDialog = new ProgressDialog(getActivity());
                                    checkPasswordDialog.setMessage("Password Checking ...");
                                    checkPasswordDialog.setCancelable(false);
                                    checkPasswordDialog.show();

                                    Service checkPasswordService = APIClient.getUserService();
                                    Call<BaseResponse> pwdCheck = checkPasswordService.checkPassword(accessToken, passwordCheckReqBean);
                                    pwdCheck.enqueue(new Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                            BaseResponse baseResponse = response.body();
                                            if (baseResponse != null) {
                                                if (baseResponse.getStatus().equals(SUCCESS)) {
                                                    checkPasswordDialog.dismiss();
                                                    dialog.dismiss();
                                                    doDaRegistration();

                                                } else {
                                                    checkPasswordDialog.dismiss();
                                                    String messageCode = baseResponse.getMessage();
                                                    tvPwdErr.setVisibility(View.VISIBLE);
                                                    if (messageCode.equals(null)) {
                                                        tvPwdErr.setText("Password Check Failed.");
                                                    } else {
                                                        tvPwdErr.setText(messageCode);
                                                    }
                                                }
                                            } else {
                                                checkPasswordDialog.dismiss();
                                                tvPwdErr.setText("Password Check Failed.");
                                                tvPwdErr.setVisibility(View.VISIBLE);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                                            checkPasswordDialog.dismiss();
                                            tvPwdErr.setText("Password Check Failed.");
                                            tvPwdErr.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                        dialog.show();
                    }
                } else {
                    viewPager.setCurrentItem(errorPages.get(0), true);
                }
            }
        });

        btnNrcFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnNrcFront);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (CameraUtil.isCameraAllowed(getActivity())) {
                                    photoStatus = NRC_FRONT_PHOTO;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = NRC_FRONT_PHOTO;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnNrcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnNrcBack);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = NRC_BACK_PHOTO;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = NRC_BACK_PHOTO;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnIncomeProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnIncomeProof);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = INCOME_PROOF_PHOTO;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = INCOME_PROOF_PHOTO;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnResidentProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnResidentProof);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = RESIDENT_PROOF_PHOTO;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = RESIDENT_PROOF_PHOTO;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnGuarantorFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnGuarantorFront);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = NRC_GUARANTOR_FRONT;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = NRC_GUARANTOR_FRONT;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnGuarantorBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnGuarantorBack);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = NRC_GUARANTOR_BACK;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = NRC_GUARANTOR_BACK;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnCriminalClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnCriminalClear);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = CRIMINAL_CLEARANCE;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = CRIMINAL_CLEARANCE;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnApplicantPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    if (isCameraAllowed()) {
                        photoStatus = APPLICANT_PHOTO;
                        openCamera();
                        financeAmount.clearFocus();
                    }
                }
            }
        });

        btnSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnSignature);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = APPLICANT_SIGNATURE;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = APPLICANT_SIGNATURE;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        btnGuarantorSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoadImageLocked(getActivity())) {
                    PopupMenu popup = new PopupMenu(getActivity(), btnGuarantorSign);
                    popup.getMenuInflater().inflate(R.menu.gallery_camera_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            String menuItem = item.getTitle().toString();
                            if (menuItem.equals(CAMERA)) {
                                if (isCameraAllowed()) {
                                    photoStatus = GUARANTOR_SIGNATURE;
                                    openCamera();
                                    financeAmount.clearFocus();
                                }
                            } else if (menuItem.equals(GALLERY)) {
                                if (isStorageAllowed()) {
                                    photoStatus = GUARANTOR_SIGNATURE;
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivityForResult(intent, REQUEST_LOAD_IMAGE);
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        //guarantorTitle = view.findViewById(R.id.da_gua_data_title);
        //loanDataTitle = view.findViewById(R.id.da_loan_data_title);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedPosition = position;
                if (selectedPosition == 4) {
                    ((MainMenuActivityDrawer) getActivity()).setLanguageListener(SmallLoanConfirmFragment.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (selectedPosition == 4) {
                    curLang = PreferencesManager.getCurrentLanguage(getContext());
                    changeLabel(curLang);
                    addSelectedPhotos();
                    if (MainMenuActivityDrawer.isSubmitclickConfirmData) {
                        MainMenuActivityDrawer.isSubmitclickConfirmData = false;
                        showErrorMsg();
                    }
                }
            }

        });

        curLang = PreferencesManager.getCurrentLanguage(getActivity());
        if (curLang.equals(LANG_MM)) {
            changeLabel(LANG_MM);
        } else {
            changeLabel(LANG_EN);
        }

        setUpFormData();
        setupErrMesgData();

        financeAmountCheck = financeAmount.getText().toString();
        if (!isEmptyOrNull(financeAmountCheck)) {
            financeAmount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
        financeAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    financeAmount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
                } else {
                    financeAmountCheck = financeAmount.getText().toString();
                    if (isEmptyOrNull(financeAmountCheck)) {
                        financeAmount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.mandatroy_edit_text_style));
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onTabAttach(String imageUrl) {
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

    private void checkConfirmData() {
        if (CommonUtils.isEmptyOrNull(Double.toString(txtFinanceAmount))) {
            validate = false;
        } else if (txtFinanceAmount < MIN_LOAN_AMOUNT) {
            validate = false;
        } else if (txtFinanceAmount > MAX_LOAN_AMOUNT) {
            validate = false;
        } else if (Double.compare(txtFinanceAmount, (basicIncome * 2)) == 1) {
            validate = false;
        }
        if (CommonUtils.isEmptyOrNull(Double.toString(txtProcessingFee))) {
            validate = false;
        }
        //Empty Attachments.
        if (photoList.size() != ALL_ATTACHMENT) {
            validate = false;
        }
    }

    private void showErrorMsg() {
        ApplicationFormErrMesgBean errConfirmMesgBean = PreferencesManager.getErrMesgInfo(getContext());

        if (errConfirmMesgBean == null) {
            errConfirmMesgBean = new ApplicationFormErrMesgBean();
        }

        if (CommonUtils.isEmptyOrNull(financeAmount.getText().toString())) {
            errFinanceAmount.setVisibility(View.VISIBLE);
            errFinanceAmount.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_financeAmt_require_err, getActivity()));
            errFinanceAmountLocale = R.string.da_financeAmt_require_err;
            errConfirmMesgBean.setConfFinanceAmountLocale(errFinanceAmountLocale);

        } else if (txtFinanceAmount < MIN_LOAN_AMOUNT || txtFinanceAmount > MAX_LOAN_AMOUNT) {
            errFinanceAmount.setVisibility(View.VISIBLE);
            errFinanceAmount.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_financeAmt_invalid_err, getActivity()));
            errFinanceAmountLocale = R.string.da_financeAmt_invalid_err;
            errConfirmMesgBean.setConfFinanceAmountLocale(errFinanceAmountLocale);

        } else if (Double.compare(txtFinanceAmount, (basicIncome * 2)) == 1) {
            errFinanceAmount.setVisibility(View.VISIBLE);
            errFinanceAmount.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_financeAmt_exceed_err, getActivity()));
            errFinanceAmountLocale = R.string.da_financeAmt_exceed_err;
            errConfirmMesgBean.setConfFinanceAmountLocale(errFinanceAmountLocale);

        } else {
            errFinanceAmount.setVisibility(View.GONE);
            errFinanceAmountLocale = R.string.da_mesg_blank;
            errConfirmMesgBean.setConfFinanceAmountLocale(errFinanceAmountLocale);
        }

        if (CommonUtils.isEmptyOrNull(Integer.toString(loanTerm)) && loanTerm == 0) {
            errLoanTerm.setVisibility(View.VISIBLE);
            errLoanTerm.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_financeTerm_require_err, getActivity()));
            errLoanTermLocale = R.string.da_financeTerm_require_err;

        } else {
            errLoanTerm.setVisibility(View.GONE);
            errLoanTermLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfLoanTermLocale(errLoanTermLocale);

        if (!isPhotoExisted(PHOTO_NRC_FRONT)) {
            errNrcFront.setVisibility(View.VISIBLE);
            errNrcFront.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_nrcfront_require_err, getActivity()));
            errNrcFrontLocale = R.string.da_nrcfront_require_err;

        } else {
            errNrcFront.setVisibility(View.GONE);
            errNrcFrontLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfNrcFrontLocale(errNrcFrontLocale);

        if (!isPhotoExisted(PHOTO_NRC_BACK)) {
            errNrcBack.setVisibility(View.VISIBLE);
            errNrcBack.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_nrcback_require_err, getActivity()));
            errNrcBackLocale = R.string.da_nrcback_require_err;

        } else {
            errNrcBack.setVisibility(View.GONE);
            errNrcBackLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfNrcBackLocale(errNrcBackLocale);

        if (!isPhotoExisted(PHOTO_INCOME_PROOF)) {
            errIncomeProof.setVisibility(View.VISIBLE);
            errIncomeProof.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_incomeProof_require_err, getActivity()));
            errIncomeProofLocale = R.string.da_incomeProof_require_err;

        } else {
            errIncomeProof.setVisibility(View.GONE);
            errIncomeProofLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfIncomeProofLocale(errIncomeProofLocale);

        if (!isPhotoExisted(PHOTO_RESIDENT_PROOF)) {
            errResidentProof.setVisibility(View.VISIBLE);
            errResidentProof.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_residentProof_require_err, getActivity()));
            errResidentProofLocale = R.string.da_residentProof_require_err;

        } else {
            errResidentProof.setVisibility(View.GONE);
            errResidentProofLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfResidentProofLocale(errResidentProofLocale);

        if (!isPhotoExisted(PHOTO_NRC_GUARANTOR_FRONT)) {
            errGuarantorNrcFront.setVisibility(View.VISIBLE);
            errGuarantorNrcFront.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_guaNrcfront_require_err, getActivity()));
            errGuarantorNrcFrontLocale = R.string.da_guaNrcfront_require_err;
        } else {
            errGuarantorNrcFront.setVisibility(View.GONE);
            errGuarantorNrcFrontLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfGuarantorNrcFrontLocale(errGuarantorNrcFrontLocale);

        if (!isPhotoExisted(PHOTO_NRC_GUARANTOR_BACK)) {
            errGuarantorNrcBack.setVisibility(View.VISIBLE);
            errGuarantorNrcBack.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_guaNrcback_require_err, getActivity()));
            errGuarantorNrcBackLocale = R.string.da_guaNrcback_require_err;
        } else {
            errGuarantorNrcBack.setVisibility(View.GONE);
            errGuarantorNrcBackLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfGuarantorNrcBackLocale(errGuarantorNrcBackLocale);

        if (!isPhotoExisted(PHOTO_CRIMINAL_CLEARANCE)) {
            errCriminalClear.setVisibility(View.VISIBLE);
            errCriminalClear.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_household_require_err, getActivity()));
            errCriminalClearLocale = R.string.da_household_require_err;
        } else {
            errCriminalClear.setVisibility(View.GONE);
            errCriminalClearLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfCriminalClearLocale(errCriminalClearLocale);

        if (!isPhotoExisted(PHOTO_APPLICANT)) {
            errApplicantPhoto.setVisibility(View.VISIBLE);
            errApplicantPhoto.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_applicantPhoto_require_err, getActivity()));
            errApplicantPhotoLocale = R.string.da_applicantPhoto_require_err;
        } else {
            errApplicantPhoto.setVisibility(View.GONE);
            errApplicantPhotoLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfApplicantPhotoLocale(errApplicantPhotoLocale);

        if (!isPhotoExisted(PHOTO_APPLICANT_SIGNATURE)) {
            errApplicantSign.setVisibility(View.VISIBLE);
            errApplicantSign.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_signature_require_err, getActivity()));
            errApplicantSignLocale = R.string.da_signature_require_err;
        } else {
            errApplicantSign.setVisibility(View.GONE);
            errApplicantSignLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfApplicantSignLocale(errApplicantSignLocale);

        if (!isPhotoExisted(PHOTO_GUARANTOR_SIGNATURE)) {
            errGuarantorSign.setVisibility(View.VISIBLE);
            errGuarantorSign.setText(CommonUtils.getLocaleString(new Locale(curLang), R.string.da_guarantor_sign_require_err, getActivity()));
            errGuarantorSignLocale = R.string.da_guarantor_sign_require_err;
        } else {
            errGuarantorSign.setVisibility(View.GONE);
            errGuarantorSignLocale = R.string.da_mesg_blank;
        }
        errConfirmMesgBean.setConfGuarantorSignLocale(errGuarantorSignLocale);

        PreferencesManager.saveErrorMesgInfo(getContext(), errConfirmMesgBean);
    }

    private void checkErrorPage() {
        errorPages.clear();

        if (!MainMenuActivityDrawer.appDataCorrect) {
            MainMenuActivityDrawer.appDataCorrect = true;
            appDataErrMsgShow = true;
            errorPages.add(0);
        } else {
            appDataErrMsgShow = false;
        }

        if (!MainMenuActivityDrawer.occuDataCorrect) {
            MainMenuActivityDrawer.occuDataCorrect = true;
            occupationErrMsgShow = true;
            errorPages.add(1);
        } else {
            occupationErrMsgShow = false;
        }

        if (!MainMenuActivityDrawer.emerDataCorrect) {
            MainMenuActivityDrawer.emerDataCorrect = true;
            emergencyErrMsgShow = true;
            errorPages.add(2);
        } else {
            emergencyErrMsgShow = false;
        }

        if (!MainMenuActivityDrawer.guraDataCorrect) {
            MainMenuActivityDrawer.guraDataCorrect = true;
            guarantorErrMsgShow = true;
            errorPages.add(3);
        } else {
            guarantorErrMsgShow = false;
        }

        if (!validate) {
            MainMenuActivityDrawer.submitConfirmData = false;
            errorPages.add(4);
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main_drawer, fragment, "TAG");
        transaction.commit();
    }

    private String commaRemove(char[] chars) {
        int countChar = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ',') {
                countChar++;
            }
        }
        int counter = 0;
        char[] newChars = new char[countChar];
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ',') {
                newChars[counter] = chars[i];
                counter++;
            }
        }
        String input = new String(newChars);
        return input;
    }

    @Override
    public void onResume() {
        super.onResume();
        endImageLoadProgressInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File currentFile = null;
        displayImageLoadProgressInfo();
        try {
            if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                //Load Image Dialog.
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                //selected file.
                File galleryFile = new File(picturePath);
                if (galleryFile.exists()) {
                    // copy from gallery to temp folder
                    File destFile = createFileName();
                    CameraUtil.copyFile(galleryFile, destFile);
                    currentFile = destFile;
                    try {
                        resizeImages(currentFile.getAbsolutePath(), PHOTO_QUALITY_80, getActivity());
                        rotateImage = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        endImageLoadProgressInfo();
                    }
                }
                endImageLoadProgressInfo();

                //Captured photo.
            } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                currentFile = new File(mCurrentPhotoPath);
                try {
                    if (currentFile.exists()) {
                        resizeImages(currentFile.getAbsolutePath(), PHOTO_QUALITY_80, getActivity());
                        rotateImage = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    endImageLoadProgressInfo();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            endImageLoadProgressInfo();
        }

        if (currentFile != null) {

            File renameFile = CameraUtil.renameFileName(getActivity(), currentFile, rootFolderName);

            if (renameFile != null) {

                Bitmap bitmap = BitmapFactory.decodeFile(renameFile.getAbsolutePath());

                ApplicationInfoPhotoBean photoBean = new ApplicationInfoPhotoBean();
                photoBean.setFileName(renameFile.getName());
                photoBean.setFilePath(renameFile.getAbsolutePath());
                photoBean.setFile(prepareFilePart(CommonConstants.IMG, renameFile));

                if (photoStatus.equals(NRC_FRONT_PHOTO)) {
                    nrcFrontBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(nrcFront);
                    nrcFront.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_NRC_FRONT);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_NRC_FRONT, renameFile.getAbsolutePath()));
                    setUpNrcFrontImageDetailView(rotateImage);

                } else if (photoStatus.equals(NRC_BACK_PHOTO)) {
                    nrcBackBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(nrcBack);
                    nrcBack.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_NRC_BACK);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_NRC_BACK, renameFile.getAbsolutePath()));
                    setUpNrcBackImageDetailView(rotateImage);

                } else if (photoStatus.equals(INCOME_PROOF_PHOTO)) {
                    incomeBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(incomeProof);
                    incomeProof.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_INCOME_PROOF);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_INCOME_PROOF, renameFile.getAbsolutePath()));
                    setUpIncomeProofImageDetailView(rotateImage);

                } else if (photoStatus.equals(RESIDENT_PROOF_PHOTO)) {
                    residentBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(residentProof);
                    residentProof.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_RESIDENT_PROOF);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_RESIDENT_PROOF, renameFile.getAbsolutePath()));
                    setUpResidentProofImageDetailView(rotateImage);

                } else if (photoStatus.equals(NRC_GUARANTOR_FRONT)) {
                    guaNrcFrontBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(guarantorNrcFront);
                    guarantorNrcFront.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_NRC_GUARANTOR_FRONT);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_NRC_GUARANTOR_FRONT, renameFile.getAbsolutePath()));
                    setUpGuarantorNrcFrontImageDetailView(rotateImage);

                } else if (photoStatus.equals(NRC_GUARANTOR_BACK)) {
                    guaNrcBackBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(guarantorNrcBack);
                    guarantorNrcBack.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_NRC_GUARANTOR_BACK);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_NRC_GUARANTOR_BACK, renameFile.getAbsolutePath()));
                    setUpGuarantorNrcBackImageDetailView(rotateImage);

                } else if (photoStatus.equals(CRIMINAL_CLEARANCE)) {
                    criminalClearBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(criminalClearance);
                    criminalClearance.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_CRIMINAL_CLEARANCE);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_CRIMINAL_CLEARANCE, renameFile.getAbsolutePath()));
                    setUpCriminalClearanceImageDetailView(rotateImage);

                } else if (photoStatus.equals(APPLICANT_PHOTO)) {
                    applicantBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(applicantPhoto);
                    applicantPhoto.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_APPLICANT);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_APPLICANT, renameFile.getAbsolutePath()));
                    setUpApplicantPhotoImageDetailView(rotateImage);

                } else if (photoStatus.equals(APPLICANT_SIGNATURE)) {
                    signBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(signaturePhoto);
                    signaturePhoto.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_APPLICANT_SIGNATURE);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_APPLICANT_SIGNATURE, renameFile.getAbsolutePath()));
                    setUpSignaturePhotoImageDetailView(rotateImage);

                } else if (photoStatus.equals(GUARANTOR_SIGNATURE)) {
                    guarantorSignBitmap = bitmap;
                    Glide.with(this).load(rotateImage).into(guarantorSignPhoto);
                    guarantorSignPhoto.setVisibility(View.VISIBLE);
                    photoBean.setFileType(PHOTO_GUARANTOR_SIGNATURE);
                    addAndReplacePhotoList(photoBean);
                    addAndReplaceTempPhotoList(new DATempPhotoBean(PHOTO_GUARANTOR_SIGNATURE, renameFile.getAbsolutePath()));
                    setUpGuarantorSignPhotoDetailView(rotateImage);
                }
            }
        }
    }

    private MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(CommonConstants.IMG), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private boolean isCameraAllowed() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isStorageAllowed() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri temUri;
        // Create an image file name
        File photoCaptured = createFileName();

        try {
            if (Build.VERSION.SDK_INT >= 24) {
                temUri = FileProvider.getUriForFile(getActivity(),
                        getActivity().getPackageName()
                                + ".my.package.name.provider", new File(photoCaptured.getAbsolutePath()));
            } else {
                temUri = Uri.fromFile(photoCaptured);
            }

            mCurrentPhotoPath = photoCaptured.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, temUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            if (photoStatus == APPLICANT_PHOTO) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Image cannot be created.", Toast.LENGTH_SHORT).show();
        }
    }

    private File createFileName() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        rootFolderName = CommonConstants.DA_IMAGE_PATH;
        storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + rootFolderName);
        File photoCaptured = createTempFile(imageFileName, storageDir);
        return photoCaptured;
    }

    private File createTempFile(String imageFileName, File storageDir) {
        try {
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    CommonConstants.UPLOAD_FILE_TYPE,         /* suffix */
                    storageDir      /* directory */
            );
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Image File could not be created.2\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void showLoading() {
        Glide.with(getActivity()).load(R.drawable.loading).into(imgLoading);
        layoutLoading.setVisibility(View.VISIBLE);
    }

    private void closeLoading() {
        layoutLoading.setVisibility(View.GONE);
    }

    private void setLoanTermValue(String loanTermValue) {
        if (loanTermValue != null) {
            try {
                loanTerm = Integer.parseInt(loanTermValue);
            } catch (Exception e) {
                loanTerm = INIT_LOAN_TERM_VAL;
            }
        } else {
            loanTerm = INIT_LOAN_TERM_VAL;
        }
    }

    private void verifyLoanAmount() {
        if (finance_amount >= MIN_LOAN_AMOUNT && finance_amount <= MICRO_LOAN_AMOUNT) {
            setMicroLoanTerms();
            lastRadioGroup = MIN_LOAN;
        } else if (finance_amount > MICRO_LOAN_AMOUNT && finance_amount <= MID_AMOUNT_AMOUNT) {
            setLowLoanTerms();
            lastRadioGroup = MICRO_LOAN;
        } else if (finance_amount > MID_AMOUNT_AMOUNT && finance_amount <= MAX_LOAN_AMOUNT) {
            setHighLoanTerms();
            lastRadioGroup = MID_LOAN;
        } else {
            removeAllRadioBtn();
        }
    }

    private void removeAllRadioBtn() {
        radioLoanTerm.removeAllViews();
    }

    private void setLowLoanTerms() {
        final String[] lLoanTerms = getResources().getStringArray(R.array.low_loan_term);
        addRadioBtn(lLoanTerms);
        loanTerm = INIT_LOAN_TERM_VAL;
    }

    private void setHighLoanTerms() {
        final String[] hLoanTerms = getResources().getStringArray(R.array.high_loan_term);
        addRadioBtn(hLoanTerms);
        loanTerm = INIT_LOAN_TERM_VAL;
    }

    private void setMicroLoanTerms() {
        final String[] microLoanTerms = getResources().getStringArray(R.array.micro_loan_term);
        addRadioBtn(microLoanTerms);
        loanTerm = INIT_LOAN_TERM_VAL;
    }

    private void addRadioBtn(final String[] term) {
        radioLoanTerm.removeAllViews();
        for (int i = 0; i < term.length; i++) {

            final RadioButton radioButton = new RadioButton(
                    new ContextThemeWrapper(getActivity(), R.style.RadioButton), null, 0);
            radioButton.setText(term[i]);
            radioButton.setId(i);
            radioLoanTerm.addView(radioButton);
            radioLoanTerm.setOrientation(RadioGroup.HORIZONTAL);
        }
    }

    private String getNetErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        return CommonUtils.getLocaleString(new Locale(language), R.string.network_connection_err, getActivity());
    }

    private void loanCalculation() {

        final LoanCalculationReqBean loanCalculationReqBean = new LoanCalculationReqBean();
        loanCalculationReqBean.setFinanceAmount(finance_amount);
        loanCalculationReqBean.setLoanTerm(loanTerm);
        loanCalculationReqBean.setMotorCycleLoanFlag(false);

        Service loanCalculateService = APIClient.getUserService();
        Call<BaseResponse<LoanCalculationResBean>> req = loanCalculateService.getLoanCalculationResult(loanCalculationReqBean);
        showLoading();
        req.enqueue(new Callback<BaseResponse<LoanCalculationResBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<LoanCalculationResBean>> call, Response<BaseResponse<LoanCalculationResBean>> response) {
                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();
                    if (baseResponse != null) {
                        if (baseResponse.getStatus().equals(SUCCESS)) {
                            LoanCalculationResBean loanCalculationResBean =
                                    (LoanCalculationResBean) baseResponse.getData();
                            try {

                                DecimalFormat df = new DecimalFormat("#,###,###");
                                processingFee.setText(df.format(loanCalculationResBean.getProcessingFees()));
                                totalRepayment.setText(df.format(loanCalculationResBean.getTotalRepayment()));
                                firstRepayment.setText(df.format(loanCalculationResBean.getFirstPayment()));
                                monthlyRepayment.setText(df.format(loanCalculationResBean.getMonthlyPayment()));
                                lastRepayment.setText(df.format(loanCalculationResBean.getLastPayment()));
                                compulsorySaving.setText(df.format(loanCalculationResBean.getConSaving()));

                                previewProcessingFee = df.format(loanCalculationResBean.getProcessingFees());
                                previewCompulsorySaving = df.format(loanCalculationResBean.getConSaving());
                                previewTotalRepayment = df.format(loanCalculationResBean.getTotalRepayment());
                                previewFirstRepayment = df.format(loanCalculationResBean.getFirstPayment());
                                previewLastRepayment = df.format(loanCalculationResBean.getLastPayment());
                                previewMonthlyRepayment = df.format(loanCalculationResBean.getMonthlyPayment());

                                hideKeyboard(getActivity());
                                closeLoading();

                            } catch (Exception e) {
                                e.printStackTrace();
                                closeLoading();
                            }
                        } else if (baseResponse.getStatus().equals(FAILED)) {
                            closeLoading();
                        } else {
                            closeLoading();
                        }
                    } else {
                        closeLoading();
                    }
                } else {
                    closeLoading();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<LoanCalculationResBean>> call, Throwable t) {
                closeLoading();
            }
        });

    }

    public void changeLabel(String language) {

        //loanDataTitle.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanConfirm_title, getContext()));

        labelFinanceAmount.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_financeAmount, getActivity()));
        errFinanceAmount.setText(CommonUtils.getLocaleString(new Locale(language), errFinanceAmountLocale, getActivity()));

        labelLoanTerm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_financeTerm, getActivity()));
        errLoanTerm.setText(CommonUtils.getLocaleString(new Locale(language), errLoanTermLocale, getActivity()));

        labelProcessFee.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_processFee, getActivity()));
        labelCompulsory.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_compulsory, getActivity()));
        labelTotalRepayment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_totalRepayAmount, getActivity()));
        labelFirstRepayAmount.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_firstRepayAmount, getActivity()));
        labelMonthlyRepayAmount.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_monthlyRepayAmount, getActivity()));
        labelLastPayment.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_lastpayment, getActivity()));

        labelNrcFront.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_nrcFront, getActivity()));
        errNrcFront.setText(CommonUtils.getLocaleString(new Locale(language), errNrcFrontLocale, getActivity()));

        labelNrcBack.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_nrcBack, getActivity()));
        errNrcBack.setText(CommonUtils.getLocaleString(new Locale(language), errNrcBackLocale, getActivity()));

        labelIncomeProof.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_incomeProof, getActivity()));
        errIncomeProof.setText(CommonUtils.getLocaleString(new Locale(language), errIncomeProofLocale, getActivity()));

        labelResidentProof.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_residentProof, getActivity()));
        errResidentProof.setText(CommonUtils.getLocaleString(new Locale(language), errResidentProofLocale, getActivity()));

        labelGuarantorNrcFront.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_GuaNrcFront, getActivity()));
        errGuarantorNrcFront.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorNrcFrontLocale, getActivity()));

        labelGuarantorNrcBack.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_GuaNrcBack, getActivity()));
        errGuarantorNrcBack.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorNrcBackLocale, getActivity()));

        labelCriminalClear.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_household, getActivity()));
        errCriminalClear.setText(CommonUtils.getLocaleString(new Locale(language), errCriminalClearLocale, getActivity()));

        labelApplicantPhoto.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_applicantPhoto, getActivity()));
        errApplicantPhoto.setText(CommonUtils.getLocaleString(new Locale(language), errApplicantPhotoLocale, getActivity()));

        labelApplicantSign.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_signature, getActivity()));
        errApplicantSign.setText(CommonUtils.getLocaleString(new Locale(language), errApplicantSignLocale, getActivity()));

        labelGuarantorSign.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_guar_signature, getActivity()));
        errGuarantorSign.setText(CommonUtils.getLocaleString(new Locale(language), errGuarantorSignLocale, getActivity()));

        submitLoan.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_loanconfirm_agree_btn, getActivity()));
        preview.setText(CommonUtils.getLocaleString(new Locale(language), R.string.da_preview_btn, getActivity()));

        textBusinessErrMsg.setText(errBusinessErrLocale);

        PreferencesManager.setCurrentLanguage(getContext(), language);
    }

    void doDaRegistration() {

        ApplicationRegisterSaveReqBean applicationRegisterSaveReqBean
                = PreferencesManager.getDaftSavedInfo(getActivity());

        OccupationDataFormBean occupationDataBean = applicationRegisterSaveReqBean.getApplicantCompanyInfoDto();
        EmergencyContactFormBean emergencyContactDataBean = applicationRegisterSaveReqBean.getEmergencyContactInfoDto();
        GuarantorFormBean guarantorDataBean = applicationRegisterSaveReqBean.getGuarantorInfoDto();

        ApplicationRegisterReqBean registerBean = new ApplicationRegisterReqBean();
        registerBean.setDaApplicationInfoId(applicationRegisterSaveReqBean.getDaApplicationInfoId());
        registerBean.setDaApplicationTypeId(APPLICATION_PLOAN);
        registerBean.setName(applicationRegisterSaveReqBean.getName());
        registerBean.setDob(applicationRegisterSaveReqBean.getDob());
        registerBean.setNrcNo(applicationRegisterSaveReqBean.getNrcNo());
        registerBean.setFatherName(applicationRegisterSaveReqBean.getFatherName());
        registerBean.setNationality(applicationRegisterSaveReqBean.getNationality());
        registerBean.setNationalityOther(applicationRegisterSaveReqBean.getNationalityOther());
        registerBean.setGender(applicationRegisterSaveReqBean.getGender());
        registerBean.setMaritalStatus(applicationRegisterSaveReqBean.getMaritalStatus());
        registerBean.setCurrentAddress(null);
        registerBean.setPermanentAddress(null);
        registerBean.setTypeOfResidence(applicationRegisterSaveReqBean.getTypeOfResidence());
        registerBean.setTypeOfResidenceOther(applicationRegisterSaveReqBean.getTypeOfResidenceOther());
        registerBean.setLivingWith(applicationRegisterSaveReqBean.getLivingWith());
        registerBean.setLivingWithOther(applicationRegisterSaveReqBean.getLivingWithOther());
        registerBean.setYearOfStayYear(applicationRegisterSaveReqBean.getYearOfStayYear());
        registerBean.setYearOfStayMonth(applicationRegisterSaveReqBean.getYearOfStayMonth());
        registerBean.setMobileNo(applicationRegisterSaveReqBean.getMobileNo());
        registerBean.setResidentTelNo(applicationRegisterSaveReqBean.getResidentTelNo());
        registerBean.setOtherPhoneNo(applicationRegisterSaveReqBean.getOtherPhoneNo());
        registerBean.setEmail(applicationRegisterSaveReqBean.getEmail());
        registerBean.setHighestEducationTypeId(applicationRegisterSaveReqBean.getHighestEducationTypeId());

        registerBean.setCurrentAddressBuildingNo(applicationRegisterSaveReqBean.getCurrentAddressBuildingNo());
        registerBean.setCurrentAddressRoomNo(applicationRegisterSaveReqBean.getCurrentAddressRoomNo());
        registerBean.setCurrentAddressFloor(applicationRegisterSaveReqBean.getCurrentAddressFloor());
        registerBean.setCurrentAddressStreet(applicationRegisterSaveReqBean.getCurrentAddressStreet());
        registerBean.setCurrentAddressQtr(applicationRegisterSaveReqBean.getCurrentAddressQtr());
        registerBean.setCurrentAddressCity(applicationRegisterSaveReqBean.getCurrentAddressCity());
        registerBean.setCurrentAddressTownship(applicationRegisterSaveReqBean.getCurrentAddressTownship());

        registerBean.setPermanentAddressBuildingNo(applicationRegisterSaveReqBean.getPermanentAddressBuildingNo());
        registerBean.setPermanentAddressRoomNo(applicationRegisterSaveReqBean.getPermanentAddressRoomNo());
        registerBean.setPermanentAddressFloor(applicationRegisterSaveReqBean.getPermanentAddressFloor());
        registerBean.setPermanentAddressStreet(applicationRegisterSaveReqBean.getPermanentAddressStreet());
        registerBean.setPermanentAddressQtr(applicationRegisterSaveReqBean.getPermanentAddressQtr());
        registerBean.setPermanentAddressCity(applicationRegisterSaveReqBean.getPermanentAddressCity());
        registerBean.setPermanentAddressTownship(applicationRegisterSaveReqBean.getPermanentAddressTownship());

        registerBean.setCustomerId(customerId);
        registerBean.setDaLoanTypeId(1);                            //default
        registerBean.setDaProductTypeId(1);                         //default
        registerBean.setProductDescription("Phone");                //default

        registerBean.setFinanceTerm(loanTerm);
        registerBean.setChannelType(MOBILE);
        registerBean.setFinanceAmount(finance_amount);

        registerBean.setApplicantCompanyInfoDto(occupationDataBean);
        registerBean.setEmergencyContactInfoDto(emergencyContactDataBean);
        registerBean.setGuarantorInfoDto(guarantorDataBean);

        List<ApplicationInfoAttachmentFormBean> attachList = new ArrayList<>();
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (ApplicationInfoPhotoBean photoBean : photoList) {
            ApplicationInfoAttachmentFormBean attachmentFormBean = new ApplicationInfoAttachmentFormBean();
            attachmentFormBean.setFileName(photoBean.getFileName());
            attachmentFormBean.setFileType(photoBean.getFileType());
            attachmentFormBean.setFilePath(photoBean.getFilePath());
            attachList.add(attachmentFormBean);
            parts.add(photoBean.getFile());
        }

        registerBean.setApplicationInfoAttachmentDtoList(attachList);
        final String accessToken = PreferencesManager.getAccessToken(getActivity());

        //do upload in background.
        new DigitalApplicationRegistrationAsyncTask(registerBean, accessToken, getActivity(), textBusinessErrMsg, parts, (AppCompatActivity) getActivity()).execute();
    }

    void setUpLoanConfirmFormData() {
        if (!processingFee.getText().toString().equals(BLANK)) {
            txtProcessingFee = Double
                    .parseDouble(processingFee.getText().toString()
                            .replace(",", BLANK));
        } else {
            txtProcessingFee = 0.0;
        }
        if (!financeAmount.getText().toString().equals(BLANK)) {
            txtFinanceAmount = Double.
                    parseDouble(financeAmount.getText().toString()
                            .replace(",", BLANK));
        } else {
            txtFinanceAmount = 0.0;
        }
    }

    boolean isPhotoExisted(int fileType) {
        if (photoList.size() == 0) {
            return false;
        }
        for (ApplicationInfoPhotoBean attachmentFormBean : photoList) {
            if (attachmentFormBean.getFileType() == fileType) {
                if (attachmentFormBean.getFile() != null) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    void addAndReplacePhotoList(ApplicationInfoPhotoBean photoBean) {
        if (photoBean != null) {
            if (photoList.size() == 0) {
                photoList.add(photoBean);
            } else if (photoList.size() > 0) {
                int fileType = photoBean.getFileType();
                int index = 0;
                boolean isReplaced = false;
                for (ApplicationInfoPhotoBean attachmentFormBean : photoList) {
                    if (attachmentFormBean.getFileType() == fileType) {
                        photoList.remove(index);
                        photoList.add(photoBean);
                        isReplaced = true;
                        break;
                    }
                    index++;
                }
                if (!isReplaced) {
                    photoList.add(photoBean);
                }
            }
        }
    }

    void addAndReplaceTempPhotoList(DATempPhotoBean tempPhotoBean) {
        if (tempPhotoBean != null) {
            if (tempPhotoBeanList.size() == 0) {
                tempPhotoBeanList.add(tempPhotoBean);
            } else if (tempPhotoBeanList.size() > 0) {
                int photoType = tempPhotoBean.getPhotoType();
                int index = 0;
                boolean isReplaced = false;
                for (DATempPhotoBean daTempPhotoBean : tempPhotoBeanList) {
                    if (daTempPhotoBean.getPhotoType() == photoType) {
                        tempPhotoBeanList.remove(index);
                        tempPhotoBeanList.add(tempPhotoBean);
                        isReplaced = true;
                        break;
                    }
                    index++;
                }
                if (!isReplaced) {
                    tempPhotoBeanList.add(tempPhotoBean);
                }
            }
        }
    }

    private void setupErrMesgData() {

        if (PreferencesManager.isDaftSavedErrExisted(getActivity())) {

            ApplicationFormErrMesgBean savedInformation
                    = PreferencesManager.getErrMesgInfo(getContext());
            errFinanceAmountLocale = savedInformation.getConfFinanceAmountLocale();
            errLoanTermLocale = savedInformation.getConfLoanTermLocale();
            errNrcFrontLocale = savedInformation.getConfNrcFrontLocale();
            errNrcBackLocale = savedInformation.getConfNrcBackLocale();
            errResidentProofLocale = savedInformation.getConfResidentProofLocale();
            errIncomeProofLocale = savedInformation.getConfIncomeProofLocale();
            errGuarantorNrcFrontLocale = savedInformation.getConfGuarantorNrcFrontLocale();
            errGuarantorNrcBackLocale = savedInformation.getConfGuarantorNrcBackLocale();
            errCriminalClearLocale = savedInformation.getConfCriminalClearLocale();
            errApplicantPhotoLocale = savedInformation.getConfApplicantPhotoLocale();
            errApplicantSignLocale = savedInformation.getConfApplicantSignLocale();
            errGuarantorSignLocale = savedInformation.getConfGuarantorSignLocale();
            errBusinessErrLocale = savedInformation.getConfBusinessErrLocale();

            if (errFinanceAmountLocale != R.string.da_mesg_blank) {
                errFinanceAmount.setText(CommonUtils.getLocaleString(new Locale(curLang), errFinanceAmountLocale, getContext()));
                errFinanceAmount.setVisibility(View.VISIBLE);
            }

            if (errLoanTermLocale != R.string.da_mesg_blank) {
                errLoanTerm.setText(CommonUtils.getLocaleString(new Locale(curLang), errLoanTermLocale, getContext()));
                errLoanTerm.setVisibility(View.VISIBLE);
            }

            if (errNrcFrontLocale != R.string.da_mesg_blank) {
                errNrcFront.setText(CommonUtils.getLocaleString(new Locale(curLang), errNrcFrontLocale, getContext()));
                errNrcFront.setVisibility(View.VISIBLE);
            }

            if (errNrcBackLocale != R.string.da_mesg_blank) {
                errNrcBack.setText(CommonUtils.getLocaleString(new Locale(curLang), errNrcBackLocale, getContext()));
                errNrcBack.setVisibility(View.VISIBLE);
            }

            if (errResidentProofLocale != R.string.da_mesg_blank) {
                errResidentProof.setText(CommonUtils.getLocaleString(new Locale(curLang), errResidentProofLocale, getContext()));
                errResidentProof.setVisibility(View.VISIBLE);
            }

            if (errIncomeProofLocale != R.string.da_mesg_blank) {
                errIncomeProof.setText(CommonUtils.getLocaleString(new Locale(curLang), errIncomeProofLocale, getContext()));
                errIncomeProof.setVisibility(View.VISIBLE);
            }

            if (errGuarantorNrcFrontLocale != R.string.da_mesg_blank) {
                errGuarantorNrcFront.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorNrcFrontLocale, getContext()));
                errGuarantorNrcFront.setVisibility(View.VISIBLE);
            }

            if (errGuarantorNrcBackLocale != R.string.da_mesg_blank) {
                errGuarantorNrcBack.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorNrcBackLocale, getContext()));
                errGuarantorNrcBack.setVisibility(View.VISIBLE);
            }

            if (errCriminalClearLocale != R.string.da_mesg_blank) {
                errCriminalClear.setText(CommonUtils.getLocaleString(new Locale(curLang), errCriminalClearLocale, getContext()));
                errCriminalClear.setVisibility(View.VISIBLE);
            }

            if (errApplicantPhotoLocale != R.string.da_mesg_blank) {
                errApplicantPhoto.setText(CommonUtils.getLocaleString(new Locale(curLang), errApplicantPhotoLocale, getContext()));
                errApplicantPhoto.setVisibility(View.VISIBLE);
            }

            if (errApplicantSignLocale != R.string.da_mesg_blank) {
                errApplicantSign.setText(CommonUtils.getLocaleString(new Locale(curLang), errApplicantSignLocale, getContext()));
                errApplicantSign.setVisibility(View.VISIBLE);
            }

            if (errGuarantorSignLocale != R.string.da_mesg_blank) {
                errGuarantorSign.setText(CommonUtils.getLocaleString(new Locale(curLang), errGuarantorSignLocale, getContext()));
                errGuarantorSign.setVisibility(View.VISIBLE);
            }

            if (errBusinessErrLocale != BLANK) {
                textBusinessErrMsg.setText(errBusinessErrLocale);
                textBusinessErrMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    void setUpSelectedPhotos() {

        if (PreferencesManager.isSelectedPhotoExisted(getActivity())) {

            List<DATempPhotoBean> daTempPhotoBeans
                    = PreferencesManager.getSelectedPhotos(getActivity());

            for (DATempPhotoBean daTempPhotoBean : daTempPhotoBeans) {
                int photoType = daTempPhotoBean.getPhotoType();
                Bitmap bitmap = BitmapFactory.decodeFile(daTempPhotoBean.getAbsFilePath());

                switch (photoType) {
                    case PHOTO_NRC_FRONT:
                        nrcFront.setImageBitmap(bitmap);
                        nrcFront.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_NRC_BACK:
                        nrcBack.setImageBitmap(bitmap);
                        nrcBack.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_INCOME_PROOF:
                        incomeProof.setImageBitmap(bitmap);
                        incomeProof.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_RESIDENT_PROOF:
                        residentProof.setImageBitmap(bitmap);
                        residentProof.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_NRC_GUARANTOR_FRONT:
                        guarantorNrcFront.setImageBitmap(bitmap);
                        guarantorNrcFront.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_NRC_GUARANTOR_BACK:
                        guarantorNrcBack.setImageBitmap(bitmap);
                        guarantorNrcBack.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_CRIMINAL_CLEARANCE:
                        criminalClearance.setImageBitmap(bitmap);
                        criminalClearance.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_APPLICANT:
                        applicantPhoto.setImageBitmap(bitmap);
                        applicantPhoto.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_APPLICANT_SIGNATURE:
                        signaturePhoto.setImageBitmap(bitmap);
                        signaturePhoto.setVisibility(View.VISIBLE);
                        break;
                    case PHOTO_GUARANTOR_SIGNATURE:
                        guarantorSignPhoto.setImageBitmap(bitmap);
                        guarantorSignPhoto.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    void setBasicIncome() {
        final String typedIncome = occu_monthlyIncome.getText().toString();
        if (typedIncome.equals(BLANK) || typedIncome == null) {
            basicIncome = 0.0;
        } else {
            basicIncome = Double.parseDouble(typedIncome.replaceAll(",", ""));
        }
    }

    void setUpFormData() {
        // set up form data
        setUpSelectedPhotos();
    }

    void addSelectedPhotos() {
        PreferencesManager.addSelectedPhotos(getActivity(), tempPhotoBeanList);
    }

    @Override
    public void onStart() {
        super.onStart();
        financeAmountCheck = financeAmount.getText().toString();
        if (!isEmptyOrNull(financeAmountCheck)) {
            financeAmount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.edit_text_style));
        }
    }

    DecimalFormat df = new DecimalFormat("#,###,###");

    //Detail Helper Functions.
    String getNationalityPreview(int nationality, String other) {
        if (nationality != 0) {
            if (nationality == 1) {
                return "Myanmar";
            } else {
                return other;
            }
        }
        return "-";
    }

    String getGender(int gender) {
        if (gender != 0) {
            switch (gender) {
                case MALE:
                    return "Male";
                case FEMALE:
                    return "Female";
                default:
                    return "-";
            }
        }
        return "-";
    }

    String getMaritalStatus(int maritalStatus) {
        switch (maritalStatus) {
            case SINGLE:
                return "Single";
            case MARRIED:
                return "Married";
            default:
                return "-";
        }
    }

    String getResidentTypePreview(int residentType, String other) {
        if (residentType > 0 && residentType < 5) {
            residentType = residentType - 1;
            String[] resident_type = getResources().getStringArray(R.array.applicant_resident_type);
            return resident_type[residentType];
        } else if (residentType == 5) {
            return other;
        }
        return "-";
    }

    String getLivingWith(int livingWith) {
        if (livingWith > 0) {
            livingWith = livingWith - 1;
            String[] living_with = getResources().getStringArray(R.array.applicant_living_with);
            return living_with[livingWith];
        }
        return "-";
    }

    String getYearOfStay(int stayedYear, int stayedMonth) {
        String lblYearOfStay = BLANK;
        if (stayedYear == 0 && stayedMonth == 0) {
            return "-";
        }
        if (stayedYear > 1) {
            lblYearOfStay = stayedYear + " Years ";
        } else if (stayedYear != 0) {
            lblYearOfStay = stayedYear + " Year ";
        }
        if (stayedMonth > 1) {
            lblYearOfStay = lblYearOfStay + stayedMonth + " Months";
        } else if (stayedMonth != 0) {
            lblYearOfStay = lblYearOfStay + stayedMonth + " Month";
        }
        return lblYearOfStay;
    }

    String getYearOfService(int serviceYear, int serviceMonth) {
        String lblYearOfService = BLANK;
        if (serviceYear == 0 && serviceMonth == 0) {
            return "-";
        }
        if (serviceYear > 1) {
            lblYearOfService = serviceYear + " Years  ";
        } else if (serviceYear != 0) {
            lblYearOfService = serviceYear + " Year  ";
        }
        if (serviceMonth > 1) {
            lblYearOfService = lblYearOfService + serviceMonth + " Months";
        } else if (serviceMonth != 0) {
            lblYearOfService = lblYearOfService + serviceMonth + " Month";
        }
        return lblYearOfService;
    }

    String getContactTime(String timeFrom, String timeTo) {
        if (isEmptyOrNull(timeFrom) && isEmptyOrNull(timeTo)) {
            return "-";
        } else {
            return timeFrom + " AM - " + timeTo + " PM";
        }
    }

    public static boolean isEmptyOrNull(String text) {
        if (text == null) {
            return true;
        } else if (TextUtils.isEmpty(text.trim())) {
            return true;
        }
        return false;
    }

    String getCompanyStatusPreview(int companyStatus, String other) {
        String[] company_status = getResources().getStringArray(R.array.occupation_company_status);
        int length = company_status.length;
        if (companyStatus > 0 && companyStatus < length) {
            companyStatus = companyStatus - 1;
            return company_status[companyStatus];
        } else if (companyStatus == length) {
            return other;
        }
        return "-";
    }

    String getBasicIncome(Double basicIncome) {
        if (basicIncome == 0) {
            return "- MMK";
        }
        return df.format(basicIncome) + " MMK";
    }

    String getOtherIncome(Double otherIncome) {
        if (otherIncome == 0) {
            return "- MMK";
        }
        return df.format(otherIncome) + " MMK";
    }

    String getTotalIncome(Double totalIncome) {
        if (totalIncome == 0) {
            return "- MMK";
        }
        return df.format(totalIncome) + " MMK";
    }

    String getFee(Double fee) {
        return df.format(fee) + " MMK";
    }

    String getApplicationRelationPreview(int relation, String other) {
        String[] app_relations = getResources().getStringArray(R.array.applicant_relation);
        int length = app_relations.length;
        if (relation > 0 && relation < length) {
            relation = relation - 1;
            return app_relations[relation];
        } else if (relation == length) {
            return other;
        }
        return "-";
    }

    String getGuarantorRelation(int relation) {
        if (relation > 0) {
            relation = relation - 1;
            String[] gur_relations = getResources().getStringArray(R.array.guarantor_relation);
            return gur_relations[relation];
        }
        return "-";
    }

    String getApplicationStatus(int status) {
        try {
            String[] applicationStatus = getResources().getStringArray(R.array.application_status_array);
            return applicationStatus[status];
        } catch (Exception e) {
            return "-";
        }
    }

    String getLoanType(int loanTypeId) {
        if (loanTypeId != 0) {
            switch (loanTypeId) {
                case ROLE_MOBILE:
                    return "Mobile-Loan";
                case ROLE_NON_MOBILE:
                    return "Non-Mobile";
                case ROLE_P_LOAN:
                    return "Personal-Loan";
                case ROLE_M_LOAN:
                    return "MotorCycle-Loan";
            }
        }
        return "-";
    }

    String getFinanceAmount(Double financeAmount) {
        if (financeAmount == 0) {
            return "- MMK";
        }
        return df.format(financeAmount) + MYANMAR_CURRENCY;
    }

    String getLoanTerm(int loanTerm) {
        if (loanTerm != 0) {
            return String.valueOf(loanTerm) + LOAN_TERM_PERIOD;
        }
        return "-";
    }

    String getSalaryDate(String salaryDate) {
        if (!salaryDate.equals(null)) {
            return dateToString2(salaryDate);
        } else {
            return "-";
        }
    }

    String getMemberCardNo() {
        final String userInfoFormJson = PreferencesManager.getCurrentUserInfo(getActivity());
        UserInformationFormBean userInformationFormBean =
                new Gson().fromJson(userInfoFormJson, UserInformationFormBean.class);
        return userInformationFormBean.getMemberNo();
    }

    public static String getStringValue(String text) {
        if (text == null || text == "blank") {
            return "-";
        } else if (TextUtils.isEmpty(text.trim())) {
            return "-";
        } else {
            return text;
        }
    }

    void setUpCityList(List<CityTownshipResBean> cityTownshipList) {
        cityList = new ArrayList<>();
        cityId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            cityList.add(listBean.getName());
            cityId.add(listBean.getCityId());
        }
    }

    String getCity(List<Integer> cityID, List<String> cityName, Integer cityId) {
        String city = "";
        for (int id = 0; id < cityID.size(); id++) {
            if (cityID.get(id) == cityId) {
                city = cityName.get(id);
            }
        }
        return city;
    }

    String getTownship(List<CityTownshipResBean> cityTownshipList, Integer cityId, Integer townId) {
        String township = "";
        townshipList = new ArrayList<>();
        townshipId = new ArrayList<>();
        for (CityTownshipResBean listBean : cityTownshipList) {
            if (listBean.getCityId() == cityId) {
                List<TownshipListBean> townshipBeanList = listBean.getTownshipInfoList();
                for (TownshipListBean townshipBean : townshipBeanList) {
                    townshipList.add(townshipBean.getName());
                    townshipId.add(townshipBean.getTownshipId());
                }
            }
        }
        for (int id = 0; id < townshipId.size(); id++) {
            if (townshipId.get(id) == townId) {
                township = townshipList.get(id);
            }
        }
        return township;
    }

    String getEducationPreview(int educationLevel) {
        if (educationLevel > 0) {
            educationLevel = educationLevel - 1;
            String[] education_level = getResources().getStringArray(R.array.applicant_education);
            return education_level[educationLevel];
        }
        return "-";
    }

    Bitmap setBitmapOrientation(File imageFile) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        return rotateBitmap(bitmap, orientation);
    }

    String getPaymentAmount(Double payAmount) {
        return df.format(payAmount) + " MMK";
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        replaceFragment(new MainMenuWelcomeFragment());
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    void setUpNrcFrontImageDetailView(final Bitmap bitmap) {
        nrcFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);

                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Nrc Front");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpNrcBackImageDetailView(final Bitmap bitmap) {
        nrcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Nrc Back");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpIncomeProofImageDetailView(final Bitmap bitmap) {
        incomeProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Income Proof");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpGuarantorNrcFrontImageDetailView(final Bitmap bitmap) {
        guarantorNrcFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Guarantor Nrc Front");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpGuarantorNrcBackImageDetailView(final Bitmap bitmap) {
        guarantorNrcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Guarantor Nrc Back");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpCriminalClearanceImageDetailView(final Bitmap bitmap) {
        criminalClearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Household or Criminal Clearance");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpApplicantPhotoImageDetailView(final Bitmap bitmap) {
        applicantPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Applicant Photo");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpSignaturePhotoImageDetailView(final Bitmap bitmap) {
        signaturePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Customer Signature");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpGuarantorSignPhotoDetailView(final Bitmap bitmap) {
        guarantorSignPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Guarantor Signature");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void setUpResidentProofImageDetailView(final Bitmap bitmap) {
        residentProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.attachment_image_enlarge_view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView imageTitle = dialog.findViewById(R.id.lbl_attach_image_title);
                SendMessageImageView imagePopup = dialog.findViewById(R.id.img_attach_photo);
                imagePopup.setImageBitmap(bitmap);
                imageTitle.setText("Residence Proof");

                ImageView imgPreviewClose = dialog.findViewById(R.id.da_img_preview_close);
                imgPreviewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    void displayImageLoadProgressInfo() {
        imgLoadPD = new ProgressDialog(getContext());
        imgLoadPD.setMessage("Processing...");
        imgLoadPD.setCancelable(false);
        imgLoadPD.show();
    }

    void endImageLoadProgressInfo() {
        if (imgLoadPD != null && imgLoadPD.isShowing()) {
            imgLoadPD.dismiss();
        }
    }
}