package mm.com.aeon.vcsaeon.networking;

import java.util.List;

import mm.com.aeon.vcsaeon.beans.ApplicationDetailInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationDetailInfoResBean;
import mm.com.aeon.vcsaeon.beans.ApplicationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationLastInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterReqBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.AutoReplyMessageBean;
import mm.com.aeon.vcsaeon.beans.CheckAccountLockReqBean;
import mm.com.aeon.vcsaeon.beans.CheckAccountLockResBean;
import mm.com.aeon.vcsaeon.beans.CheckMemberInformationReqBean;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.CompanyInfoResBean;
import mm.com.aeon.vcsaeon.beans.CouponInfoReqBean;
import mm.com.aeon.vcsaeon.beans.CouponInfoResBean;
import mm.com.aeon.vcsaeon.beans.CouponUseInfoReqBean;
import mm.com.aeon.vcsaeon.beans.CurrentUserInformationResBean;
import mm.com.aeon.vcsaeon.beans.CustAgreementInfoReqBean;
import mm.com.aeon.vcsaeon.beans.CustAgreementListDto;
import mm.com.aeon.vcsaeon.beans.CustomerInfoBean;
import mm.com.aeon.vcsaeon.beans.CustomerInfoRequest;
import mm.com.aeon.vcsaeon.beans.DAEnquiryResBean;
import mm.com.aeon.vcsaeon.beans.EventsNewsInfoResBean;
import mm.com.aeon.vcsaeon.beans.ExistedMemberRegistrationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.FAQInfo;
import mm.com.aeon.vcsaeon.beans.ForceResetPasswordReqBean;
import mm.com.aeon.vcsaeon.beans.FreeMessageUserReqBean;
import mm.com.aeon.vcsaeon.beans.FreeMessageUserResBean;
import mm.com.aeon.vcsaeon.beans.HotlineInfoResBean;
import mm.com.aeon.vcsaeon.beans.HowToUseVideoResBean;
import mm.com.aeon.vcsaeon.beans.LoanCalculationReqBean;
import mm.com.aeon.vcsaeon.beans.LoanCalculationResBean;
import mm.com.aeon.vcsaeon.beans.LoanTypeBean;
import mm.com.aeon.vcsaeon.beans.LoginAccessTokenInfo;
import mm.com.aeon.vcsaeon.beans.LogoutInfoReqBean;
import mm.com.aeon.vcsaeon.beans.MobileVersionConfigReqBean;
import mm.com.aeon.vcsaeon.beans.MobileVersionConfigResBean;
import mm.com.aeon.vcsaeon.beans.NewMemberRegistrationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.OTPInfoReqBean;
import mm.com.aeon.vcsaeon.beans.OTPInfoResBean;
import mm.com.aeon.vcsaeon.beans.OfflineLogoutReqBean;
import mm.com.aeon.vcsaeon.beans.OutletInfoListBaseResBean;
import mm.com.aeon.vcsaeon.beans.PasswordCheckReqBean;
import mm.com.aeon.vcsaeon.beans.Product;
import mm.com.aeon.vcsaeon.beans.ProductTypeListBean;
import mm.com.aeon.vcsaeon.beans.PromotionsInfoResBean;
import mm.com.aeon.vcsaeon.beans.PurchaseAttachEditReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseConfirmationReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseDetailInfoCancelReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseDetailInfoReqBean;
import mm.com.aeon.vcsaeon.beans.PurchaseDetailInfoResBean;
import mm.com.aeon.vcsaeon.beans.PurchaseInfoConfirmationReqBean;
import mm.com.aeon.vcsaeon.beans.RegSecurityQuestionInfoResBean;
import mm.com.aeon.vcsaeon.beans.RegisterSuccessResponseBean;
import mm.com.aeon.vcsaeon.beans.ResetPasswordConfirmedInfoReqBean;
import mm.com.aeon.vcsaeon.beans.ResetPasswordConfirmedInfoResBean;
import mm.com.aeon.vcsaeon.beans.ResetPasswordReqBean;
import mm.com.aeon.vcsaeon.beans.SecurityQAReqBean;
import mm.com.aeon.vcsaeon.beans.SingleLoginCheck;
import mm.com.aeon.vcsaeon.beans.SingleLoginStatus;
import mm.com.aeon.vcsaeon.beans.StatusChangeCountReq;
import mm.com.aeon.vcsaeon.beans.StatusChangeCountRes;
import mm.com.aeon.vcsaeon.beans.StatusReadFlagReq;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.beans.UpdateProfilePhotoReqBean;
import mm.com.aeon.vcsaeon.beans.UpdateProfilePhotoResBean;
import mm.com.aeon.vcsaeon.beans.UpdateUserQAInfoReqBean;
import mm.com.aeon.vcsaeon.beans.UpdateUserQAInfoResBean;
import mm.com.aeon.vcsaeon.beans.UpdateVerificationInfoReqBean;
import mm.com.aeon.vcsaeon.beans.VerifyNewRegisteredUserInfoReqBean;
import mm.com.aeon.vcsaeon.beans.VerifyNewRegisteredUserInfoResBean;
import mm.com.aeon.vcsaeon.beans.VerifyQAInfoReqBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_APPLICATION_ENQUIRY;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_APPLICATION_INFO_DETAIL;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_APPLICATION_REGISTER;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_ATTACHMENT_EDIT;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_AUTO_MESSAGE_REPLY;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CHECK_PASSWORD;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CHECK_UPDATE_STATUS;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CITY_TOWNSHIP_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_COUPON_INFO_GET_COUPON_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_COUPON_INFO_UPDATE_COUPON_INFO;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_CONFIRM_SECURITY_ANSWER;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_GET_SECURITY_QUESTION_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_GET_USER_INFO;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_UPDATE_PROFILE_;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_UPDATE_SECURITY_ANSWER;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_UPDATE_USER_LOGOUT;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_UPGRADE_MEMBER;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_INFO_VERIFY_MEMBER_INFO;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_REGISTRATION_CHECK_MEMBER;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_REGISTRATION_REGISTER_NEW;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_REGISTRATION_REGISTER_OLD;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_CUSTOMER_REGISTRATION_SEND_OTP;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_FREE_MESSAGE_SYNC;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_GET_AEON_ANNOUNCEMENT;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_GET_AGREEEMTN_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_GET_GOOD_NEWS_INFO;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_GET_OUTLET_INFO_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_GET_PRODUCT_INFO;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_HOW_TO_USE;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_INFORMATION_ABOUT_US;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_INFORMATION_FAQ_INFO_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_INFORMATION_HOTLINE;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_INFORMATION_TOWNSHIP_CODE_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_LOAD_SAVE_DATA;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_LOAN_CALCULATOR;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_LOAN_TYPES;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_LOGIN_CHECK;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_MENU_STATUS_COUNT;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_OAUTH_TOKEN;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_OFFLINE_LOGOUT;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_PRODUCT_TYPE_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_PURCHASE_CANCEL;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_PURCHASE_DETAIL_INFO;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_PURCHASE_INFO_CANCEL;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_PURCHASE_INFO_CONFIRM;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_RESET_PWD_CHECK_ACCOUNT_LOCKED;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_RESET_PWD_CONFIRM_SECURITY_ANSWER;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_RESET_PWD_DO_RESET;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_RESET_PWD_FORCE_PWD_CHANGE;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_RESET_PWD_GET_SECURITY_QUESTION_LIST;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_SAVE_REGISTER_DATA;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.API_STATUS_READ_FLAG;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.LOGIN_DEVICE_ID;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.PARAM_ACCESS_TOKEN;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.PARAM_GRANT_TYPE;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.PARAM_PASSWORD;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.PARAM_REFRESH_TOKEN;
import static mm.com.aeon.vcsaeon.networking.NetworkingConstants.PARAM_USERNAME;

public interface Service {

    @POST(API_CUSTOMER_INFO_UPDATE_PROFILE_)
        // MTH
    Call<BaseResponse<UpdateProfilePhotoResBean>> getUpdateProfilePhotoInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                            @Body UpdateProfilePhotoReqBean updateProfilePhotoReqBean);

    @GET(API_INFORMATION_ABOUT_US)
    Call<BaseResponse<CompanyInfoResBean>> getCompanyInfo();

    @GET(API_INFORMATION_FAQ_INFO_LIST)
    Call<BaseResponse<List<FAQInfo>>> getFAQInfo();

    @GET(API_RESET_PWD_GET_SECURITY_QUESTION_LIST)
    Call<BaseResponse<RegSecurityQuestionInfoResBean>> getSecurityQuestion();

    @GET(API_INFORMATION_TOWNSHIP_CODE_LIST)
    Call<BaseResponse<List<TownshipCodeResDto>>> getTownshipCode();

    @POST(API_CUSTOMER_REGISTRATION_CHECK_MEMBER)
    Call<BaseResponse<CustomerInfoBean>> checkMemberInfo(@Body CheckMemberInformationReqBean checkMemberInformationReqBean);

    @POST(API_CUSTOMER_REGISTRATION_REGISTER_NEW)
    Call<BaseResponse> registerNewCustomer(@Body NewMemberRegistrationInfoReqBean newMemberRegistrationInfoReqBean);

    @POST(API_CUSTOMER_REGISTRATION_REGISTER_OLD)
    Call<BaseResponse> registerOldCustomer(@Body ExistedMemberRegistrationInfoReqBean existedMemberRegistrationInfoReqBean);

    @FormUrlEncoded
    @POST(API_OAUTH_TOKEN)
    Call<BaseResponse<LoginAccessTokenInfo>> doLogin(@Field(value = PARAM_USERNAME) String username,
                                                     @Field(value = PARAM_PASSWORD) String password,
                                                     @Field(value = PARAM_GRANT_TYPE) String grant_type,
                                                     @Field(value = LOGIN_DEVICE_ID) String login_device_id);

    @FormUrlEncoded
    @POST(API_OAUTH_TOKEN)
    Call<BaseResponse<LoginAccessTokenInfo>> refreshToken(@Field(PARAM_GRANT_TYPE) String grantType,
                                                          @Field(PARAM_REFRESH_TOKEN) String refreshToken);

    @POST(API_CUSTOMER_INFO_GET_USER_INFO)
    Call<BaseResponse<CurrentUserInformationResBean>> getUserInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                  @Body CustomerInfoRequest customerInfoRequest);

    @POST(API_CUSTOMER_INFO_GET_SECURITY_QUESTION_LIST)
    Call<BaseResponse<UpdateUserQAInfoResBean>> getUpdateSecurityQuestion(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                          @Body SecurityQAReqBean securityQAReqBean);

    @POST(API_CUSTOMER_INFO_UPDATE_SECURITY_ANSWER)
    Call<BaseResponse> doUpdateSecQAInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                         @Body UpdateUserQAInfoReqBean updateUserQAInfoReqBean);

    @POST(API_CUSTOMER_INFO_UPDATE_USER_LOGOUT)
    Call<BaseResponse> usrLogout(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                 @Body LogoutInfoReqBean logoutInfoReqBean);

    @POST(API_CUSTOMER_INFO_VERIFY_MEMBER_INFO)
    Call<BaseResponse<VerifyNewRegisteredUserInfoResBean>> checkVerifyMember(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                             @Body VerifyNewRegisteredUserInfoReqBean verifyNewRegisteredUserInfoReqBean);

    @POST(API_CUSTOMER_INFO_CONFIRM_SECURITY_ANSWER)
    Call<BaseResponse> verifyAnswer(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                    @Body VerifyQAInfoReqBean verifyQAInfoReqBean);

    @POST(API_CUSTOMER_INFO_UPGRADE_MEMBER)
    Call<BaseResponse<CurrentUserInformationResBean>> verifyNewUser(
            @Query(PARAM_ACCESS_TOKEN) String accessToken,
            @Body UpdateVerificationInfoReqBean updateVerificationInfoReqBean);

    @POST(API_COUPON_INFO_GET_COUPON_LIST)
    Call<BaseResponse<List<CouponInfoResBean>>> getCouponInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                              @Body CouponInfoReqBean couponInfoReqBean);

    @POST(API_COUPON_INFO_UPDATE_COUPON_INFO)
    Call<BaseResponse> updateCouponInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                        @Body CouponUseInfoReqBean couponUseInfoReqBean);

    @GET(API_INFORMATION_HOTLINE)
    Call<BaseResponse<HotlineInfoResBean>> getHotlineInfo();

    @POST(API_CUSTOMER_REGISTRATION_SEND_OTP)
    Call<BaseResponse<OTPInfoResBean>> getOTPCode(@Body OTPInfoReqBean otpInfoReqBean);

    @POST(API_RESET_PWD_CONFIRM_SECURITY_ANSWER)
    Call<BaseResponse<ResetPasswordConfirmedInfoResBean>> checkResetPwdInfo(
            @Body ResetPasswordConfirmedInfoReqBean resetPasswordConfirmedInfoReqBean);

    @POST(API_RESET_PWD_DO_RESET)
    Call<BaseResponse<ResetPasswordConfirmedInfoResBean>> updateResetPwdInfo(@Body ResetPasswordReqBean resetPasswordReqBean);

    @POST(API_CHECK_UPDATE_STATUS)
    Call<BaseResponse<MobileVersionConfigResBean>> getUpdateStatus(@Body MobileVersionConfigReqBean mobileVersionConfigReqBean);

    @GET(API_GET_GOOD_NEWS_INFO)
    Call<BaseResponse<List<EventsNewsInfoResBean>>> getNewsInfo();
    //Call<BaseResponse<List<EventsNewsInfoResBean>>> getNewsInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken);

    @GET(API_GET_AEON_ANNOUNCEMENT)
    Call<BaseResponse<List<PromotionsInfoResBean>>> getPromotionsInfo();
    //Call<BaseResponse<List<PromotionsInfoResBean>>> getPromotionsInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken);

    @POST(API_RESET_PWD_CHECK_ACCOUNT_LOCKED)
    Call<BaseResponse<CheckAccountLockResBean>> checkAccountLock(@Body CheckAccountLockReqBean checkAccountLockReqBean);

    @POST(API_RESET_PWD_FORCE_PWD_CHANGE)
    Call<BaseResponse> forcePasswordReset(@Body ForceResetPasswordReqBean forceResetPasswordReqBean);

    @POST(API_OFFLINE_LOGOUT)
    Call<BaseResponse> offlineLogout(@Body OfflineLogoutReqBean offlineLogoutReqBean);

    @GET(API_GET_OUTLET_INFO_LIST)
    Call<BaseResponse<OutletInfoListBaseResBean>> getOutletInfoList();

    @POST(API_LOAN_CALCULATOR)
    Call<BaseResponse<LoanCalculationResBean>> getLoanCalculationResult(@Body LoanCalculationReqBean loanCalculationReqBean);
    /*Call<BaseResponse<LoanCalculationResBean>> getLoanCalculationResult(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                        @Body LoanCalculationReqBean loanCalculationReqBean);*/

    @POST(API_CHECK_PASSWORD)
    Call<BaseResponse> checkPassword(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                     @Body PasswordCheckReqBean passwordCheckReqBean);

    @POST(API_GET_AGREEEMTN_LIST)
    Call<BaseResponse<List<CustAgreementListDto>>> getCustomerAgreementList(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                            @Body CustAgreementInfoReqBean custAgreementInfoReqBean);

    @POST(API_GET_PRODUCT_INFO)
    Call<BaseResponse<Product>> getPurchaseInfoConfirmation(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                            @Body PurchaseInfoConfirmationReqBean purchaseInfoConfirmationReqBean);

    @POST(API_PURCHASE_INFO_CONFIRM)
    Call<BaseResponse> confirmPurchase(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                       @Body PurchaseConfirmationReqBean purchaseConfirmationReqBean);

    @POST(API_PURCHASE_INFO_CANCEL)
    Call<BaseResponse> cancelPurchase(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                      @Body PurchaseConfirmationReqBean purchaseConfirmationReqBean);

    @GET(API_LOAN_TYPES)
    Call<BaseResponse<List<LoanTypeBean>>> getLoanTypes();

    /*@POST(API_LOAD_SAVE_DATA)
    Call<BaseResponse<ApplicationLastInfoResBean>> getLastRegisterInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                       @Body ApplicationLastInfoReqBean applicationLastInfoReqBean);*/

    @POST(API_LOAD_SAVE_DATA)
    Call<BaseResponse<ApplicationRegisterSaveReqBean>> getLastRegisterInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                           @Body ApplicationLastInfoReqBean applicationLastInfoReqBean);

    @POST(API_SAVE_REGISTER_DATA)
    Call<BaseResponse<ApplicationRegisterSaveReqBean>> saveRegisterData(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                        @Body ApplicationRegisterSaveReqBean applicationRegisterSaveReqBean);

    @Multipart
    @POST(API_APPLICATION_REGISTER)
    Call<BaseResponse<RegisterSuccessResponseBean>> registerLoanApplication(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                            @Part("applicationInfoDto") ApplicationRegisterReqBean applicationRegisterReqBean, @Part List<MultipartBody.Part> imgList);

    @POST(API_APPLICATION_ENQUIRY)
    Call<BaseResponse<DAEnquiryResBean>> getApplicationEnquiry(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                               @Body ApplicationInfoReqBean applicationInfoReqBean);

    @POST(API_PURCHASE_DETAIL_INFO)
    Call<BaseResponse<PurchaseDetailInfoResBean>> getPurchaseDetailInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                        @Body PurchaseDetailInfoReqBean purchaseDetailInfoReqBean);

    @POST(API_APPLICATION_INFO_DETAIL)
    Call<BaseResponse<ApplicationDetailInfoResBean>> getApplicationDetailInfo(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                                                              @Body ApplicationDetailInfoReqBean applicationDetailInfoReqBean);

    @POST(API_PURCHASE_CANCEL)
    Call<BaseResponse> doPurchaseInfoCancel(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                            @Body PurchaseDetailInfoCancelReqBean purchaseDetailInfoCancelReqBean);

    @Multipart
    @POST(API_ATTACHMENT_EDIT)
    Call<BaseResponse> doAttachmentEdit(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                        @Part("applicationInfoDto") PurchaseAttachEditReqBean purchaseAttachEditReqBean, @Part List<MultipartBody.Part> imgList);

    /*Call<BaseResponse> doAttachmentEdit(@Query(PARAM_ACCESS_TOKEN) String accessToken,
                                        @Body PurchaseAttachEditReqBean purchaseAttachEditReqBean);*/

    @GET(API_PRODUCT_TYPE_LIST)
    Call<BaseResponse<List<ProductTypeListBean>>> getProductTypeList();

    @GET(API_CITY_TOWNSHIP_LIST)
    Call<BaseResponse<List<CityTownshipResBean>>> getCityTownshipList();

    @GET(API_AUTO_MESSAGE_REPLY)
    Call<BaseResponse<AutoReplyMessageBean>> getAutoMessageReply();

    @POST(API_FREE_MESSAGE_SYNC)
    Call<BaseResponse<FreeMessageUserResBean>> doFreeMessageRoomSync(@Body FreeMessageUserReqBean userReqBean);

    @GET(API_HOW_TO_USE)
    Call<BaseResponse<HowToUseVideoResBean>> getVideoLink();

    @POST(API_LOGIN_CHECK)
    Call<BaseResponse<SingleLoginStatus>> getSingleLoginStatus(@Body SingleLoginCheck singleLoginCheck);

    @POST(API_MENU_STATUS_COUNT)
    Call<BaseResponse<StatusChangeCountRes>> getMenuStatusCount(@Body StatusChangeCountReq statusChangeCountReq);

    @POST(API_STATUS_READ_FLAG)
    Call<BaseResponse> setStatusReadFlag(@Body StatusReadFlagReq statusReadFlagReq);
}
