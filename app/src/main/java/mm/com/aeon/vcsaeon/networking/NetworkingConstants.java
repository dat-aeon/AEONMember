package mm.com.aeon.vcsaeon.networking;

public class NetworkingConstants {

    //API Client | OkHttp3 Client
    public static final int OK_HTTP_READ_TIMEOUT = 120;
    public static final int OK_HTTP_CONNECT_TIMEOUT = 120;
    public static final int OK_HTTP_WRITE_TIMEOUT = 120;

    //API Client | Retrofit 2.0
    public static final int RETROFIT_READ_TIMEOUT = 120;
    public static final int RETROFIT_CONNECT_TIMEOUT = 120;

    //OAuth
    public static final String AUTH_ATTRIBUTE = "Authorization";
    public static final int CODE_TOKEN_TIMEOUT = 401;

    //WebSocket
    public static final int WEB_SOCKET_CONNECTION_TIMEOUT = 1000;

    //Service Api | Params
    public static final String PARAM_ACCESS_TOKEN = "access_token";
    public static final String PARAM_REFRESH_TOKEN = "refresh_token";
    public static final String PARAM_GRANT_TYPE = "grant_type";
    public static final String LOGIN_DEVICE_ID = "login_device_id";

    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";

    //Service Api | Api Endpoints
    /*Customer-Info-Manage*/
    public static final String API_CUSTOMER_INFO_UPDATE_PROFILE_ = "customer-info-manage/update-customer-profile-image";
    public static final String API_CUSTOMER_INFO_GET_USER_INFO = "customer-info-manage/get-user-information";
    public static final String API_CUSTOMER_INFO_GET_SECURITY_QUESTION_LIST = "customer-info-manage/get-customer-security-question-list";
    public static final String API_CUSTOMER_INFO_UPDATE_SECURITY_ANSWER = "customer-info-manage/update-customer-security-question-answer";
    public static final String API_CUSTOMER_INFO_UPDATE_USER_LOGOUT = "customer-info-manage/update-app-usage-detail-for-logout";
    public static final String API_CUSTOMER_INFO_VERIFY_MEMBER_INFO = "customer-info-manage/verify-member-info";
    public static final String API_CUSTOMER_INFO_CONFIRM_SECURITY_ANSWER = "customer-info-manage/confirm-security-question-answer";
    public static final String API_CUSTOMER_INFO_UPGRADE_MEMBER = "customer-info-manage/upgrade-member";

    /*Information*/
    public static final String API_INFORMATION_ABOUT_US = "information/about-us";
    public static final String API_INFORMATION_FAQ_INFO_LIST = "information/faq-info-list";
    public static final String API_INFORMATION_TOWNSHIP_CODE_LIST = "information/township-code-list";
    public static final String API_INFORMATION_HOTLINE = "information/hotline";

    /*OAuth*/
    public static final String API_OAUTH_TOKEN = "oauth/token";

    /*Reset Password*/
    public static final String API_RESET_PWD_GET_SECURITY_QUESTION_LIST = "reset-password/security-question-list";
    public static final String API_RESET_PWD_CONFIRM_SECURITY_ANSWER = "reset-password/confirm-security-question-answer";
    public static final String API_RESET_PWD_DO_RESET = "reset-password/reset-password";
    public static final String API_RESET_PWD_CHECK_ACCOUNT_LOCKED = "reset-password/check-account-lock";
    public static final String API_RESET_PWD_FORCE_PWD_CHANGE = "reset-password/force-password-change";

    /*Customer Info Registration*/
    public static final String API_CUSTOMER_REGISTRATION_CHECK_MEMBER = "customer-info-registration/check-member";
    public static final String API_CUSTOMER_REGISTRATION_REGISTER_NEW = "customer-info-registration/register-new-customer";
    public static final String API_CUSTOMER_REGISTRATION_REGISTER_OLD = "customer-info-registration/register-old-customer";
    public static final String API_CUSTOMER_REGISTRATION_SEND_OTP = "customer-info-registration/send-otp";

    /*Coupon Info*/
    public static final String API_COUPON_INFO_GET_COUPON_LIST = "coupon-info/customer-coupon-info-list";
    public static final String API_COUPON_INFO_UPDATE_COUPON_INFO = "coupon-info/use-coupon-info";

    /*Version Check*/
    public static final String API_CHECK_UPDATE_STATUS = "mobile-version-config/check-update-status";

    /*Events & News*/
    //public static final String API_GET_GOOD_NEWS_INFO = "news-info/news-info-list";
    //public static final String API_GET_AEON_ANNOUNCEMENT = "promotions-info/promotions-info-list";
    public static final String API_GET_GOOD_NEWS_INFO = "free-token/news-info-list";
    public static final String API_GET_AEON_ANNOUNCEMENT = "free-token/promotions-info-list";

    /*Offline Logout*/
    public static final String API_OFFLINE_LOGOUT = "offline-logout/logout";

    /*Outlet Info*/
    public static final String API_GET_OUTLET_INFO_LIST = "free-token/outlet-info-list";

    /*Loan Calculator*/
    //public static final String API_LOAN_CALCULATOR = "loan-calculator/loan-calculate";
    public static final String API_LOAN_CALCULATOR = "free-token/loan-calculate";

    /*Check Password*/
    public static final String API_CHECK_PASSWORD = "customer-info-manage/check-password";

    /*Purchase Confirmations*/
    public static final String API_GET_AGREEEMTN_LIST = "customer-info-manage/get-customer-agreement-list";
    public static final String API_GET_PRODUCT_INFO = "application/get-purchase-info-confirm-waiting";
    public static final String API_PURCHASE_INFO_CONFIRM = "application/purchase-info-confirm";
    public static final String API_PURCHASE_INFO_CANCEL = "application/purchase-info-cancel";

    /*Get Loan Types*/
    public static final String API_LOAN_TYPES = "information/loan-type-list";

    /*Save Registration Data*/
    public static final String API_SAVE_REGISTER_DATA = "application/save-draft";

    /*Application Registration*/
    public static final String API_APPLICATION_REGISTER = "application/register-multipart";

    /*Save Registration Data*/
    public static final String API_LOAD_SAVE_DATA = "application/last-application-info";

    /*Get digital application info*/
    public static final String API_APPLICATION_ENQUIRY = "application/application-inquries-list";

    /*Get purchase detail info*/
    public static final String API_PURCHASE_DETAIL_INFO = "application/purchase-info-detail";

    /*Get application detail info*/
    public static final String API_APPLICATION_INFO_DETAIL = "application/application-info-detail";

    /*Do purchase cancel.*/
    public static final String API_PURCHASE_CANCEL = "application/application-cancel";

    /*Attachments Edit*/
    public static final String API_ATTACHMENT_EDIT = "application/attachment-edit-multipart";

    public static final String API_PRODUCT_TYPE_LIST= "information/product-type-list";

    public static final String API_CITY_TOWNSHIP_LIST= "information/city-township-info-list";

    public static final String API_AUTO_MESSAGE_REPLY= "information/get-chat-auto-reply-message";

    public static final String API_FREE_MESSAGE_SYNC= "free-message/room-sync";

    public static final String API_HOW_TO_USE= "information/get-how-to-use-video-file-name";

    public static final String API_LOGIN_CHECK = "free-token/check-mutli-login";

    public static final String API_MENU_STATUS_COUNT = "application/get-application-status-changed-count";

    public static final String API_STATUS_READ_FLAG = "application/update-application-status-changed-read-flag";
}
