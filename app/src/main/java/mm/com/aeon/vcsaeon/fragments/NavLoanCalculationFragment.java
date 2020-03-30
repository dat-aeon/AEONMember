package mm.com.aeon.vcsaeon.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.activities.MainActivity;
import mm.com.aeon.vcsaeon.activities.MainMenuActivityDrawer;
import mm.com.aeon.vcsaeon.beans.LoanCalculationReqBean;
import mm.com.aeon.vcsaeon.beans.LoanCalculationResBean;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;
import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.networking.APIClient;
import mm.com.aeon.vcsaeon.networking.BaseResponse;
import mm.com.aeon.vcsaeon.networking.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.FAILED;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.INVALID_LOAN_TERM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.INVALID_LOAN_TERM_MINIMUM;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.LOAN_AMOUNT_FORMAT;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SUCCESS;
import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.hideKeyboard;

public class NavLoanCalculationFragment extends BaseFragment implements LanguageChangeListener {

    View view;

    TextView errLoanAmt;
    TextView errLoanTerm;
    EditText textLoanAmt;

    TextView lblLoanTerm;
    TextView lblMLoan;

    TextView lblProcessingFee;
    TextView lblTotalRepayAmt;
    TextView lblFirstRepayAmt;
    TextView lblMonthlyRepayAmt;
    TextView lblLastPay;

    TextView lblCompulsorySaving;
    TextView lblTotalCompulsorySaving;

    EditText textProcessingFee;
    EditText textTotalRepayAmt;
    EditText textFirstRepayAmt;
    EditText textMonthlyRepayAmt;
    EditText textLastPay;

    EditText textCompulsorySaving;
    EditText textTotalCompulsorySaving;

    Button btnCalculate;
    SwitchCompat switchMLoan;

    ScrollView layoutResult;
    RadioGroup rdButtonGroup;

    //localization
    public static final String LANG_MM = "my";
    public static final String LANG_EN = "en";
    private String curLang;

    private static final double MIN_FEE_ZERO_AMOUNT = 50000;
    private static final double MAX_LOAN_AMOUNT = 2000000;
    private static final double MIN_LOAN_AMOUNT = 100000;
    private static final double MIN_M_LOAN_AMOUNT = 350000;
    private static final double MICRO_LOAN_AMOUNT = 150000;
    private static final double MID_AMOUNT_AMOUNT = 700000;

    private static final int INIT_LOAN_TERM_VAL = 0;

    private double loanAmount;
    private int loanTerm;
    private boolean isMotorCycleLoan;

    private int errLoanAmtMesgLocale;
    private int errLoanTermMesgLocale;

    ImageView imgLoading;
    RelativeLayout layoutLoading;

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.loan_calculation, container, false);
        setHasOptionsMenu(true);
        curLang = PreferencesManager.getCurrentLanguage(getContext());

        // close button listener
        if (PreferencesManager.getMainNavFlag(getContext())) {
            ((MainActivity) getActivity()).setLanguageListener(this);
            toolbar = ((MainActivity) getActivity()).findViewById(R.id.toolbar_main_home);

        } else {
            ((MainMenuActivityDrawer) getActivity()).setLanguageListener(this);
            toolbar = ((MainMenuActivityDrawer) getActivity()).findViewById(R.id.toolbar_home);

        }
        // show back button on toolbar
        LinearLayout menuBackBtn = toolbar.findViewById(R.id.menu_back_btn_view);
        menuBackBtn.setVisibility(View.VISIBLE);

        layoutResult = view.findViewById(R.id.layout_result);

        errLoanAmt = view.findViewById(R.id.err_amt_exceed);
        errLoanTerm = view.findViewById(R.id.err_invalid_loan_term);
        textLoanAmt = view.findViewById(R.id.text_loan_amt);

        lblLoanTerm = view.findViewById(R.id.lbl_loan_term);
        lblMLoan = view.findViewById(R.id.lbl_switch);

        lblProcessingFee = view.findViewById(R.id.lbl_processing_fee);
        lblTotalRepayAmt = view.findViewById(R.id.lbl_total_repay);
        lblFirstRepayAmt = view.findViewById(R.id.lbl_first_pay);
        lblMonthlyRepayAmt = view.findViewById(R.id.lbl_monthly_pay);
        lblLastPay = view.findViewById(R.id.lbl_last_pay);

        lblCompulsorySaving = view.findViewById(R.id.lbl_com_saving);
        lblTotalCompulsorySaving = view.findViewById(R.id.lbl_total_saving);

        textProcessingFee = view.findViewById(R.id.text_processing_fee);
        textTotalRepayAmt = view.findViewById(R.id.text_total_repay);
        textFirstRepayAmt = view.findViewById(R.id.text_first_pay);
        textMonthlyRepayAmt = view.findViewById(R.id.text_monthly_pay);
        textLastPay = view.findViewById(R.id.text_total_pay);
        textCompulsorySaving = view.findViewById(R.id.text_saving);
        textTotalCompulsorySaving = view.findViewById(R.id.text_total_saving);

        btnCalculate = view.findViewById(R.id.btn_calculate);
        switchMLoan = view.findViewById(R.id.ch_motor_cycle_loan);

        imgLoading = view.findViewById(R.id.img_loading);
        layoutLoading = view.findViewById(R.id.layout_loading);

        errLoanAmtMesgLocale = R.string.da_mesg_blank;
        errLoanTermMesgLocale = R.string.da_mesg_blank;

        // close button listener
        View backLayoutView = view.findViewById(R.id.include_back_button);

        rdButtonGroup = view.findViewById(R.id.rd_btn_group);
        rdButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                setLoanTermValue(radioButton.getText().toString());
                loanCalculation();
            }
        });

        switchMLoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMotorCycleLoan = true;
                    setMotorCycleLoanTerms();
                    verifyLoanAmount();
                } else {
                    isMotorCycleLoan = false;
                    clearMessage();
                    verifyLoanAmount();
                }
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loanAmount == 0.0) {
                    errLoanAmt.setText(getRquiredLoanAmountErrMsg());
                    errLoanAmt.setVisibility(View.VISIBLE);
                    layoutResult.setVisibility(View.GONE);
                } else {
                    loanCalculation();
                }
            }
        });

        textLoanAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                if (!cs.toString().equals(BLANK)) {
                    String newAmount = cs.toString();
                    char[] chars = newAmount.toCharArray();
                    String input = commaRemove(chars);
                    loanAmount = Integer.parseInt(input);
                    verifyLoanAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable view) {
                textLoanAmt.removeTextChangedListener(this);
                try {

                    String originalString = view.toString();
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }

                    Long longValue = Long.parseLong(originalString);
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern(LOAN_AMOUNT_FORMAT);
                    String formattedString = formatter.format(longValue);

                    //setting text after format to EditText
                    textLoanAmt.setText(formattedString);
                    textLoanAmt.setSelection(textLoanAmt.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                textLoanAmt.addTextChangedListener(this);
            }
        });

        changeLabel(curLang);
        return view;
    }

    public void replaceFragment(Fragment fragment, int containerViewId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment, "TAG");
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
        char[] newchars = new char[countChar];
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ',') {
                newchars[counter] = chars[i];
                counter++;
            }
        }
        String input = new String(newchars);
        return input;
    }

    private void verifyLoanAmount() {
        layoutResult.setVisibility(View.GONE);
        errLoanAmt.setVisibility(View.GONE);
        if (isMotorCycleLoan) {
            setMotorCycleLoanTerms();
        } else if (loanAmount >= MIN_FEE_ZERO_AMOUNT && loanAmount <= MICRO_LOAN_AMOUNT) {
            setMicroLoanTerms();
        } else if (loanAmount > MICRO_LOAN_AMOUNT && loanAmount <= MID_AMOUNT_AMOUNT) {
            setLowLoanTerms();
        } else if (loanAmount > MID_AMOUNT_AMOUNT && loanAmount <= MAX_LOAN_AMOUNT) {
            setHighLoanTerms();
        } else {
            removeAllRadioBtn();
        }
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

    private void setMotorCycleLoanTerms() {
        final String[] mLoanTerms = getResources().getStringArray(R.array.m_loan_term);
        addRadioBtn(mLoanTerms);
        loanTerm = INIT_LOAN_TERM_VAL;
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
        rdButtonGroup.removeAllViews();
        for (int i = 0; i < term.length; i++) {
            RadioButton radioButton = new RadioButton(
                    new ContextThemeWrapper(getActivity(), R.style.RadioButton), null, 0);
            radioButton.setText(term[i]);
            radioButton.setTypeface(ResourcesCompat.getFont(getActivity(), R.font.pyidaungsu_regular));
            rdButtonGroup.addView(radioButton);
            rdButtonGroup.setOrientation(RadioGroup.HORIZONTAL);
            removeLoanTermErrMessage();
        }
    }

    private void removeAllRadioBtn() {
        rdButtonGroup.removeAllViews();
        layoutResult.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorite:
                //this.languageFlag = item;
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

    private void changeLabel(String language) {
        //final String language = PreferencesManager.getCurrentLanguage(getActivity());
        textLoanAmt.setHint(CommonUtils.getLocaleString(new Locale(language), R.string.hint_loan_amount, getActivity()));
        lblLoanTerm.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_loan_term, getActivity()));
        lblMLoan.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_motor_cycle_loan, getActivity()));
        lblProcessingFee.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_processing_fee, getActivity()));
        lblTotalRepayAmt.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_total_repayment_amount, getActivity()));
        lblFirstRepayAmt.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_first_repayment_amount, getActivity()));
        lblMonthlyRepayAmt.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_monthly_repayment_amount, getActivity()));
        lblLastPay.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_last_payment, getActivity()));
        lblCompulsorySaving.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_compulsory_saving, getActivity()));
        lblTotalCompulsorySaving.setText(CommonUtils.getLocaleString(new Locale(language), R.string.lbl_total_compulsory_saving, getActivity()));
        btnCalculate.setText(CommonUtils.getLocaleString(new Locale(language), R.string.btn_calculate, getActivity()));
        errLoanAmt.setText(CommonUtils.getLocaleString(new Locale(language), errLoanAmtMesgLocale, getActivity()));
        errLoanTerm.setText(CommonUtils.getLocaleString(new Locale(language), errLoanTermMesgLocale, getActivity()));

        PreferencesManager.setCurrentLanguage(getContext(), language);
    }

    private String getExceedMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        errLoanAmtMesgLocale = R.string.warning_msg_high_amount;
        return CommonUtils.getLocaleString(new Locale(language), R.string.warning_msg_high_amount, getActivity());
    }

    private String getRequiredMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        errLoanAmtMesgLocale = R.string.warning_msg_low_amount;
        return CommonUtils.getLocaleString(new Locale(language), R.string.warning_msg_low_amount, getActivity());
    }

    private String getMotorCycleLoanErrorMessage() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        errLoanAmtMesgLocale = R.string.warning_mloan_msg_low_amount;
        return CommonUtils.getLocaleString(new Locale(language), R.string.warning_mloan_msg_low_amount, getActivity());
    }

    private String getInvalidLoanTermErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        errLoanAmtMesgLocale = R.string.error_invlid_loan_term;
        return CommonUtils.getLocaleString(new Locale(language), R.string.error_invlid_loan_term, getActivity());
    }

    private String getRequiredLoanTermErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        errLoanTermMesgLocale = R.string.error_require_loan_term;
        return CommonUtils.getLocaleString(new Locale(language), R.string.error_require_loan_term, getActivity());
    }

    private String getRquiredLoanAmountErrMsg() {
        final String language = PreferencesManager.getCurrentLanguage(getActivity());
        errLoanAmtMesgLocale = R.string.error_require_loan_amount;
        return CommonUtils.getLocaleString(new Locale(language), R.string.error_require_loan_amount, getActivity());
    }

    private void closeLoading() {
        layoutLoading.setVisibility(View.GONE);
    }

    private void showLoading() {
        Glide.with(getActivity()).load(R.drawable.loading).into(imgLoading);
        layoutLoading.setVisibility(View.VISIBLE);
    }

    private void showInvalidLoanTermErrMessage() {
        errLoanAmt.setText(getInvalidLoanTermErrMsg());
        errLoanAmt.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void showLoanTermErrMessage() {
        errLoanTerm.setText(getRequiredLoanTermErrMsg());
        errLoanTerm.setVisibility(View.VISIBLE);
    }

    private void removeLoanTermErrMessage() {
        errLoanTerm.setVisibility(View.GONE);
    }

    private void removeLoanAmountErrMessage() {
        errLoanAmt.setVisibility(View.GONE);
    }

    private void clearMessage() {
        removeLoanTermErrMessage();
        removeLoanAmountErrMessage();
    }

    private void loanCalculation() {

        boolean isValid = true;

        if (isMotorCycleLoan) {
            if (loanAmount > MAX_LOAN_AMOUNT) {
                errLoanAmt.setText(getExceedMsg());
                errLoanAmt.setVisibility(View.VISIBLE);
                layoutResult.setVisibility(View.GONE);
                isValid = false;
            } else if (loanAmount < MIN_M_LOAN_AMOUNT) {
                errLoanAmt.setText(getMotorCycleLoanErrorMessage());
                errLoanAmt.setVisibility(View.VISIBLE);
                layoutResult.setVisibility(View.GONE);
                isValid = false;
            } else {
                errLoanAmt.setVisibility(View.GONE);
            }

        } else {
            if (loanAmount > MAX_LOAN_AMOUNT) {
                errLoanAmt.setText(getExceedMsg());
                errLoanAmt.setVisibility(View.VISIBLE);
                layoutResult.setVisibility(View.GONE);
                isValid = false;
            } else if (loanAmount < MIN_FEE_ZERO_AMOUNT) {
                errLoanAmt.setText(getRequiredMsg());
                errLoanAmt.setVisibility(View.VISIBLE);
                layoutResult.setVisibility(View.GONE);
                isValid = false;
            } else {
                errLoanAmt.setVisibility(View.GONE);
            }
        }

        if (isValid) {

            final LoanCalculationReqBean loanCalculationReqBean = new LoanCalculationReqBean();
            loanCalculationReqBean.setFinanceAmount(loanAmount);
            loanCalculationReqBean.setLoanTerm(loanTerm);
            loanCalculationReqBean.setMotorCycleLoanFlag(isMotorCycleLoan);

            Service loanCalculateService = APIClient.getUserService();
            Call<BaseResponse<LoanCalculationResBean>> req = loanCalculateService
                    .getLoanCalculationResult(loanCalculationReqBean);
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

                                    textProcessingFee.setText(df.format(loanCalculationResBean.getProcessingFees()));
                                    textTotalRepayAmt.setText(df.format(loanCalculationResBean.getTotalRepayment()));
                                    textFirstRepayAmt.setText(df.format(loanCalculationResBean.getFirstPayment()));
                                    textMonthlyRepayAmt.setText(df.format(loanCalculationResBean.getMonthlyPayment()));
                                    textLastPay.setText(df.format(loanCalculationResBean.getLastPayment()));
                                    textCompulsorySaving.setText(df.format(loanCalculationResBean.getConSaving()));
                                    textTotalCompulsorySaving.setText(df.format(loanCalculationResBean.getTotalConSaving()));

                                    hideKeyboard(getActivity());
                                    layoutResult.setVisibility(View.VISIBLE);
                                    clearMessage();
                                    closeLoading();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    closeLoading();
                                }

                            } else if (baseResponse.getStatus().equals(FAILED)) {

                                closeLoading();
                                switch (baseResponse.getMessageCode()) {
                                    case INVALID_LOAN_TERM:
                                        showInvalidLoanTermErrMessage();
                                        break;
                                    case INVALID_LOAN_TERM_MINIMUM:
                                        showLoanTermErrMessage();
                                        break;
                                }

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
    }

    @Override
    public void changeLanguageTitle(String lang) {
        changeLabel(lang);
    }

    @Override
    public void clickMenuBarBackBtn() {
        if (PreferencesManager.getMainNavFlag(getActivity())) {
            replaceFragment(new NewMainPageFragment(), R.id.content_new_main_drawer);
        } else {
            replaceFragment(new MainMenuWelcomeFragment(), R.id.content_main_drawer);
        }
    }
}