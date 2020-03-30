package mm.com.aeon.vcsaeon.common_utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import mm.com.aeon.vcsaeon.beans.ApplicationFormErrMesgBean;
import mm.com.aeon.vcsaeon.beans.ApplicationLastInfoResBean;
import mm.com.aeon.vcsaeon.beans.ApplicationRegisterSaveReqBean;
import mm.com.aeon.vcsaeon.beans.BrandResponse;
import mm.com.aeon.vcsaeon.beans.CategoryResponse;
import mm.com.aeon.vcsaeon.beans.CityTownshipResBean;
import mm.com.aeon.vcsaeon.beans.DATempPhotoBean;
import mm.com.aeon.vcsaeon.beans.ProductTypeListBean;
import mm.com.aeon.vcsaeon.beans.PurchaseAttachEditTempBean;
import mm.com.aeon.vcsaeon.beans.SingleLoginCheck;
import mm.com.aeon.vcsaeon.beans.SingleLoginStatus;
import mm.com.aeon.vcsaeon.beans.TownshipCodeResDto;
import mm.com.aeon.vcsaeon.beans.UserInformationFormBean;

import static android.content.Context.MODE_PRIVATE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.MESSAGING_SHARED_PREFERENCE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SOCKET_INFO_SHARED_PREFERENCE;

public class PreferencesManager {

    public static SharedPreferences getApplicationPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("app_pref_info", MODE_PRIVATE);
        return preferences;
    }

    public static SharedPreferences getCurrentUserPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user_info_form", MODE_PRIVATE);
        return preferences;
    }

    public static void addEntryToPreferences(SharedPreferences preferences, String entryName, String entryValue) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(entryName, entryValue).commit();
    }

    public static String getStringEntryFromPreferences(SharedPreferences preferences, String entryName) {
        return preferences.getString(entryName, "");
    }

    public static void addIntegerEntryToPreferences(SharedPreferences preferences, String entryName, int entryValue) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(entryName, entryValue).commit();
    }

    public static int getIntegerEntryFromPreferences(SharedPreferences preferences, String entryName) {
        return preferences.getInt(entryName, 0);
    }

    public static boolean isBiometricRegistered(Context mContext) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        return preferences.getBoolean("biometric_reg", false);
    }

    public static void setBiometricRegistered(Context mContext, boolean biometricFlag) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("biometric_reg", biometricFlag).commit();
    }

    public static void clearCurrentUserPreferences(SharedPreferences preferences, String entryName) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(entryName).commit();
    }

    public static SharedPreferences getSocketPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SOCKET_INFO_SHARED_PREFERENCE, MODE_PRIVATE);
        return preferences;
    }

    public static SharedPreferences getMessagingPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MESSAGING_SHARED_PREFERENCE, MODE_PRIVATE);
        return preferences;
    }

    public static void addEntryToPreferences(SharedPreferences preferences, String entryName, boolean entryValue) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(entryName, entryValue).commit();
    }

    public static void keepMainNavFlag(Context mContext, boolean mainNavFlag) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("main_page", mainNavFlag).commit();
    }

    public static boolean getMainNavFlag(Context mContext) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        return preferences.getBoolean("main_page", false);
    }

    public static void keepAccessToken(Context mContext, String access_token) {
        SharedPreferences preferences = getCurrentUserPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("access_token", access_token).commit();
    }

    public static void keepRefreshToken(Context mContext, String refresh_token) {
        SharedPreferences preferences = getCurrentUserPreferences(mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("refresh_token", refresh_token).commit();
    }

    public static void clearAccessToken(Context mContext) {
        SharedPreferences preferences = getCurrentUserPreferences(mContext);
        preferences.edit().remove("access_token").commit();
    }

    public static void clearRefreshToken(Context mContext) {
        SharedPreferences preferences = getCurrentUserPreferences(mContext);
        preferences.edit().remove("refresh_token").commit();
    }

    public static String getAccessToken(Context mContext) {
        SharedPreferences preferences = getCurrentUserPreferences(mContext);
        return preferences.getString("access_token", "");
    }

    public static String getRefreshToken(Context mContext) {
        SharedPreferences preferences = getCurrentUserPreferences(mContext);
        return preferences.getString("refresh_token", "");
    }

    public static void keepLastActivatedInfo(Context mContext, String lastActivatedInfo, boolean isDestroy) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        preferences.edit().putString("last_activated_info", lastActivatedInfo).commit();
        preferences.edit().putBoolean("is_destroy", isDestroy).commit();
    }

    public static void clearIsDestroy(Context mContext) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        preferences.edit().remove("is_destroy").commit();
    }

    public static String getLastActivatedInfo(Context mContext) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        return preferences.getString("last_activated_info", "");
    }

    public static boolean isDestroy(Context mContext) {
        SharedPreferences preferences = getApplicationPreference(mContext);
        return preferences.getBoolean("is_destroy", false);
    }

    public static String getCurrentFreeChatId(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        return preferences.getString("cur_free_chat_id", "");
    }

    public static void setCurrentFreeChatId(Context mContext, String freeChatId) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().putString("cur_free_chat_id", freeChatId).commit();
    }

    public static String getInstallPhoneNo(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        return preferences.getString("install_ph_no", "");
    }

    public static void setInstallPhoneNo(Context mContext, String phoneNo) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().putString("install_ph_no", phoneNo).commit();
    }

    public static String getCurrentLoginPhoneNo(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        return preferences.getString("cur_ph_no", "");
    }

    public static void setCurrentLoginPhoneNo(Context mContext, String phoneNo) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().putString("cur_ph_no", phoneNo).commit();
    }

    public static void setHotlinePhone(Context mContext, String phoneNo) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().putString("hot_line_ph_no", phoneNo).commit();
    }

    public static String getHotlinePhone(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        return preferences.getString("hot_line_ph_no", "");
    }

    public static void setCurrentUserInfo(Context mContext, String currentUserInfo) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().putString("cur_user_info", currentUserInfo).commit();
        UserInformationFormBean userInformationFormBean
                = new Gson().fromJson(currentUserInfo, UserInformationFormBean.class);
        setCustomerId(preferences, userInformationFormBean.getCustomerId());
    }

    public static String getCurrentUserInfo(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        return preferences.getString("cur_user_info", "");
    }

    public static void clearCurrentUserInfo(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().remove("cur_user_info").commit();
    }

    public static void setBiometricRegPhoneNo(Context mContext, String biometricPhoneNo) {
        SharedPreferences myPreferences = PreferencesManager.getApplicationPreference(mContext);
        myPreferences.edit().putString("biometric_phone", biometricPhoneNo).commit();
    }

    public static String getBiometricRegPhoneNo(Context mContext) {
        SharedPreferences myPreferences = PreferencesManager.getApplicationPreference(mContext);
        return myPreferences.getString("biometric_phone", "");
    }

    public static String getCurrentLanguage(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(mContext);
        return sharedPreferences.getString("lang", "");
    }

    public static void setCurrentLanguage(Context mContext, String lang) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(mContext);
        sharedPreferences.edit().putString("lang", lang).commit();
    }

    public static void clearCouponInfo(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().remove("cur_coupon_info").commit();
    }

    public static void setSocketFlagClose(Context mContext, boolean flag) {
        SharedPreferences sharedPreferences = PreferencesManager.getSocketPreference(mContext);
        sharedPreferences.edit().putBoolean("socket_flag_close", flag).commit();
    }

    public static void setSocketFlagOpen(Context mContext, boolean flag) {
        SharedPreferences sharedPreferences = PreferencesManager.getSocketPreference(mContext);
        sharedPreferences.edit().putBoolean("socket_flag_open", flag).commit();
    }

    public static boolean getSocketFlagClose(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getSocketPreference(mContext);
        return sharedPreferences.getBoolean("socket_flag_close", false);
    }

    public static boolean getSocketFlagOpen(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getSocketPreference(mContext);
        return sharedPreferences.getBoolean("socket_flag_open", false);
    }

    public static void clearSocketFlag(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getSocketPreference(mContext);
        sharedPreferences.edit().clear();
    }

    public static boolean isSocketOpen(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(mContext);
        return sharedPreferences.getBoolean("socket_flag", false);
    }

    public static void setBrowsedOrCaptured(Context mContext, boolean flag) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(mContext);
        sharedPreferences.edit().putBoolean("browse_captured_flag", flag).commit();
    }

    public static boolean isBrowsedOrCaptured(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(mContext);
        return sharedPreferences.getBoolean("browse_captured_flag", false);
    }

    public static void saveTempMemVerifyInfo(Context mContext, String agreementNo, String dateOfBirth) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        sharedPreferences.edit().putString("agreement_no", agreementNo).commit();
        sharedPreferences.edit().putString("date_of_birth", dateOfBirth).commit();
        sharedPreferences.edit().putBoolean("isData", true).commit();
    }

    public static void clearTempMemVerifyInfo(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        sharedPreferences.edit().remove("agreement_no").commit();
        sharedPreferences.edit().remove("date_of_birth").commit();
    }

    public static boolean isDataExisted(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        return sharedPreferences.getBoolean("isData", false);
    }

    public static void saveBrands(Context mContext, List<BrandResponse> brandResponses) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String brandListJson = new Gson().toJson(brandResponses);
        sharedPreferences.edit().putString("brands_list", brandListJson).commit();
    }

    public static List<BrandResponse> getBrands(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String brandListJson = sharedPreferences.getString("brands_list", BLANK);
        List<BrandResponse> brandResponses = new Gson().fromJson(brandListJson, new TypeToken<List<BrandResponse>>() {
        }.getType());
        return brandResponses;
    }

    public static void saveCategories(Context mContext, List<CategoryResponse> categoryResponses) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String categoryListJson = new Gson().toJson(categoryResponses);
        sharedPreferences.edit().putString("categories_list", categoryListJson).commit();
    }

    public static List<CategoryResponse> getCategories(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String categoryListJson = sharedPreferences.getString("categories_list", BLANK);
        List<CategoryResponse> categoryResponses = new Gson().fromJson(categoryListJson, new TypeToken<List<CategoryResponse>>() {
        }.getType());
        return categoryResponses;
    }

    public static void clearBandAndCategory(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        sharedPreferences.edit().remove("brands_list").commit();
        sharedPreferences.edit().remove("categories_list").commit();
    }

    public static void saveTownshipCode(Context mContext, List<TownshipCodeResDto> townshipCodeResDtoList) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String townshipCodeResDtoListJson = new Gson().toJson(townshipCodeResDtoList);
        sharedPreferences.edit().putString("township_code_list", townshipCodeResDtoListJson).commit();
    }

    public static List<TownshipCodeResDto> getTownshipCode(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String townshipCodeResDtoListJson = sharedPreferences.getString("township_code_list", "");
        List<TownshipCodeResDto> townshipCodeResDtoList = new Gson().fromJson(townshipCodeResDtoListJson, new TypeToken<List<TownshipCodeResDto>>() {
        }.getType());
        return townshipCodeResDtoList;
    }

    public static void saveLastRegisteredInfo(Context mContext, ApplicationLastInfoResBean lastRegisterData) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String lastRegisteredDataJson = new Gson().toJson(lastRegisterData);
        sharedPreferences.edit().putString("LastRegisterInfo", lastRegisteredDataJson).commit();
    }

    public static void saveEditPhotoList(Context mContext, List<PurchaseAttachEditTempBean> purchaseAttachEditTempBeanList) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String purchaseAttachEditInfoJson = new Gson().toJson(purchaseAttachEditTempBeanList);
        Log.e("TAG", "saveEditPhotoList JSON : " + purchaseAttachEditInfoJson);
        sharedPreferences.edit().putString("edit_attach_list", purchaseAttachEditInfoJson).commit();
    }

    public static List<PurchaseAttachEditTempBean> getEditPhotoList(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String purchaseAttachEditInfoJson = sharedPreferences.getString("edit_attach_list", BLANK);
        Log.e("TAG", "getEditPhotoList JSON : " + purchaseAttachEditInfoJson);
        List<PurchaseAttachEditTempBean> purchaseAttachEditTempBeanList = new ArrayList<>();
        if (!purchaseAttachEditInfoJson.equals(BLANK)) {
            purchaseAttachEditTempBeanList = new Gson().fromJson(purchaseAttachEditInfoJson, new TypeToken<List<PurchaseAttachEditTempBean>>() {
            }.getType());
        }
        return purchaseAttachEditTempBeanList;
    }

    public static void removeEditPhotoList(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        sharedPreferences.edit().remove("edit_attach_list").commit();
        Log.e("TAG", "removeEditPhotoList");
    }

    public static void saveDaftSavedInfo(Context mContext, ApplicationRegisterSaveReqBean localSaveReqBean) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String localSaveReqBeanJson = new Gson().toJson(localSaveReqBean);
        sharedPreferences.edit().putString("RegisterSaveData", localSaveReqBeanJson).commit();
    }

    public static ApplicationRegisterSaveReqBean getDaftSavedInfo(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String localSaveReqBeanJson = sharedPreferences.getString("RegisterSaveData", BLANK);
        ApplicationRegisterSaveReqBean localSaveReqBean = new ApplicationRegisterSaveReqBean();
        if (localSaveReqBeanJson.equals("null")) {
            return localSaveReqBean;
        }

        localSaveReqBean = new Gson().fromJson(localSaveReqBeanJson, ApplicationRegisterSaveReqBean.class);
        return localSaveReqBean;

    }

    public static boolean isDaftSavedInfoExisted(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String localSaveReqBeanJson = sharedPreferences.getString("RegisterSaveData", BLANK);
        if (localSaveReqBeanJson.equals("null")) {
            return false;
        } else {
            return true;
        }
    }

    public static void removeDaftSavedInfo(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        sharedPreferences.edit().remove("RegisterSaveData");
    }

    public static boolean isSelectedPhotoExisted(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String selectedPhotosJson = sharedPreferences.getString("selected_photos", BLANK);
        if (selectedPhotosJson.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static void saveProductTypeList(Context mContext, List<ProductTypeListBean> productTypeList) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String productTypeListJson = new Gson().toJson(productTypeList);
        sharedPreferences.edit().putString("product_type_list", productTypeListJson).commit();
    }

    public static List<ProductTypeListBean> getProductTypeList(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String productTypeListJson = sharedPreferences.getString("product_type_list", "");
        List<ProductTypeListBean> productTypeList = new Gson().fromJson(productTypeListJson, new TypeToken<List<ProductTypeListBean>>() {
        }.getType());
        return productTypeList;
    }

    public static void saveCityListInfo(Context mContext, List<CityTownshipResBean> cityTownshipList) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String cityTownshipJson = new Gson().toJson(cityTownshipList);
        sharedPreferences.edit().putString("city_township_list", cityTownshipJson).commit();
    }

    public static List<CityTownshipResBean> getCityListInfo(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String cityTownshipJson = sharedPreferences.getString("city_township_list", "");
        List<CityTownshipResBean> cityTownshipList = new Gson().fromJson(cityTownshipJson, new TypeToken<List<CityTownshipResBean>>() {
        }.getType());
        return cityTownshipList;
    }

    public static void addSelectedPhotos(Context mContext, List<DATempPhotoBean> tempPhotoBeanList) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String daTempPhotoBeanJson = new Gson().toJson(tempPhotoBeanList);
        sharedPreferences.edit().putString("selected_photos", daTempPhotoBeanJson).commit();
    }

    public static List<DATempPhotoBean> getSelectedPhotos(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String daTempPhotoBeanListJson = sharedPreferences.getString("selected_photos", BLANK);
        final List<DATempPhotoBean> daTempPhotoBeanList =
                new Gson().fromJson(daTempPhotoBeanListJson, new TypeToken<List<DATempPhotoBean>>() {
                }.getType());
        return daTempPhotoBeanList;
    }

    public static void clearSelectedPhoto(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        sharedPreferences.edit().remove("selected_photos").commit();
    }

    /*=============error message ============*/
    public static void saveErrorMesgInfo(Context mContext, ApplicationFormErrMesgBean errMesgBean) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String localSaveReqBeanJson = new Gson().toJson(errMesgBean);
        sharedPreferences.edit().putString(CommonConstants.APP_ERR_MESG_DATA, localSaveReqBeanJson).commit();
    }

    public static ApplicationFormErrMesgBean getErrMesgInfo(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String localSaveErrReqBeanJson = sharedPreferences.getString(CommonConstants.APP_ERR_MESG_DATA, BLANK);
        ApplicationFormErrMesgBean localSaveErrReqBean = new ApplicationFormErrMesgBean();
        if (localSaveErrReqBeanJson.equals("null")) {
            return localSaveErrReqBean;
        }

        localSaveErrReqBean = new Gson().fromJson(localSaveErrReqBeanJson, ApplicationFormErrMesgBean.class);
        return localSaveErrReqBean;

    }

    public static void clearErrMesgInfo(Context mContext) {
        SharedPreferences preferences = PreferencesManager.getCurrentUserPreferences(mContext);
        preferences.edit().remove(CommonConstants.APP_ERR_MESG_DATA).commit();
    }

    public static boolean isDaftSavedErrExisted(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        final String localSaveReqBeanJson = sharedPreferences.getString(CommonConstants.APP_ERR_MESG_DATA, BLANK);
        if (localSaveReqBeanJson.equals(BLANK)) {
            return false;
        } else {
            return true;
        }
    }

    public static void removeDaftSavedErrInfo(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(mContext);
        sharedPreferences.edit().remove(CommonConstants.APP_ERR_MESG_DATA).commit();
    }

    public static void setRegistrationCompleted(Context mContext, boolean flag) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(mContext);
        sharedPreferences.edit().putBoolean("register_completed", flag).commit();
    }

    public static boolean isRegistrationCompleted(Context mContext) {
        SharedPreferences sharedPreferences = PreferencesManager.getApplicationPreference(mContext);
        return sharedPreferences.getBoolean("register_completed", false);
    }

    public static SingleLoginCheck getSingleLoginCheck(Context context) {
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(context);
        final String singleLoginCheckJson = sharedPreferences.getString("single_login_check", BLANK);
        if (singleLoginCheckJson.equals(BLANK)) {
            return null;
        }
        return new Gson().fromJson(singleLoginCheckJson, SingleLoginCheck.class);
    }

    public static SingleLoginCheck getSingleLoginCheck(SharedPreferences sharedPreferences) {
        final String singleLoginCheckJson = sharedPreferences.getString("single_login_check", BLANK);
        if (singleLoginCheckJson.equals(BLANK)) {
            return new SingleLoginCheck(0,"");
        }
        return new Gson().fromJson(singleLoginCheckJson, SingleLoginCheck.class);
    }

    private static void setSingleLoginCheck(SingleLoginCheck singleLoginCheck, SharedPreferences sharedPreferences){
        final String singleLoginCheckJson = new Gson().toJson(singleLoginCheck);
        sharedPreferences.edit().putString("single_login_check", singleLoginCheckJson).commit();
    }

    private static void setCustomerId(SharedPreferences sharedPreferences, int customerId){
        SingleLoginCheck singleLoginCheck = getSingleLoginCheck(sharedPreferences);
        singleLoginCheck.setCustomerId(customerId);
        setSingleLoginCheck(singleLoginCheck, sharedPreferences);
    }

    public static void setLoginDeviceId(Context context, String loginDeviceId){
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(context);
        SingleLoginCheck singleLoginCheck = getSingleLoginCheck(sharedPreferences);
        singleLoginCheck.setLoginDeviceId(loginDeviceId);
        setSingleLoginCheck(singleLoginCheck, sharedPreferences);
    }

    public static String getLoginDeviceId(Context context){
        SharedPreferences sharedPreferences = PreferencesManager.getCurrentUserPreferences(context);
        SingleLoginCheck singleLoginCheck = getSingleLoginCheck(sharedPreferences);
        return singleLoginCheck.getLoginDeviceId();
    }

}
