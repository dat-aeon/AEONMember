package mm.com.aeon.vcsaeon.common_utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.beans.BiometricSensorStatus;
import mm.com.aeon.vcsaeon.beans.MessageInfoBean;
import mm.com.aeon.vcsaeon.beans.NrcBean;
import mm.com.aeon.vcsaeon.beans.PurchaseAttachEditTempBean;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.AGREEMENT_NO_REGX;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.BLANK;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.SPACE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.WELCOME_LETTER_BASE_URL;

public class CommonUtils {

    public static String replacePhoneNo(String message, String phoneNo) {
        String last3PhNo = phoneNo.substring(phoneNo.length() - 3);
        String replacedStr = message.replace("yyy", last3PhNo);
        return replacedStr;
    }

    public static String replaceVerifiedPhoneNo(String message, String phoneNo) {
        if (message != null && phoneNo != null) {
            return message.replace("phoneNo", phoneNo);
        } else {
            return "";
        }
    }

    public static String getLocaleString(Locale locale, int resourceId, Context context) {
        String result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.setLocale(locale);
            result = context.createConfigurationContext(configuration).getText(resourceId).toString();
        } else {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            Locale savedLocale = configuration.locale;
            configuration.locale = locale;
            resources.updateConfiguration(configuration, null);
            result = resources.getString(resourceId);
            configuration.locale = savedLocale;
            resources.updateConfiguration(configuration, null);
        }
        return result;
    }

    public static Timestamp getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        return timestamp;
    }

    public static String getChangeTimestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static String getChangeTimestampToString2(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static String getCurTimeString() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static String getCurTimeString2() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy KK:mm a");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static String getCurTimeStringForLogout() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static String getStringFromDate(Date date) {
        Timestamp timestamp = new Timestamp(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static String getStringFromDateDisplay(Date date) {
        Timestamp timestamp = new Timestamp(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static boolean isPhoneNoValid(String phoneNo) {
        if (phoneNo.length() > 11) {
            return false;
        }
        if (phoneNo.length() < 9) {
            return false;
        }
        try {
            Long.parseLong(phoneNo);
            if (phoneNo.substring(0, 2).equals("09")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTelPhoneNoValid(String phoneNo) {
        if (phoneNo.length() > 11) {
            return false;
        }
        if (phoneNo.length() < 9) {
            return false;
        }
        try {
            Long.parseLong(phoneNo);
            if (phoneNo.substring(0, 1).equals("0")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNumberValid(String phoneNo) {
        if (phoneNo.isEmpty()) {
            return false;
        } else {
            if (phoneNo.matches("[0-9]{6,11}")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean isNumeric(String phoneNo) {
        try {
            Integer.parseInt(phoneNo);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNrcCodeValid(String nrcCode) {
        if (!nrcCode.isEmpty()) {
            if (nrcCode.length() != 6) {
                return false;
            }
            try {
                Integer.parseInt(nrcCode);
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAgreementNoValid(String agreementNo) {
        if (agreementNo.isEmpty()) {
            return false;
        } else {
            if (agreementNo.matches(AGREEMENT_NO_REGX)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static Date stringToDate(String dateStr) {
        try {
            if (dateStr.equals(BLANK)) {
                return null;
            }
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            return date.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return BLANK;
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);
            return strDate;
        } catch (Exception e) {
            return BLANK;
        }
    }

    public static String dateToString2(String dateStr) {
        Date date = stringToDate(dateStr);
        if (date == null) {
            return BLANK;
        }
        return dateToString(date);
    }

    public static String dateToYYMMDDStrFormat(Date date) {
        String str = BLANK;
        if (date == null) {
            return BLANK;
        }
        try {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String strDate = dateFormat2.format(date);
            Log.e("format 2 ", strDate);

            str = dateFormat1.format(dateFormat2.parse(strDate));
            Log.e("format 1 ", str);

        } catch (Exception e) {
            return BLANK;
        }
        return str;
    }

    public static String stringToYMDDateFormatStr(String strdate) {
        String str = BLANK;
        if (strdate == null) {
            return BLANK;
        }
        try {
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date date = dateFormat2.parse(strdate);
            str = dateFormat2.format(date);
            //str = dateFormat.format(date.parse(strdate));
            str = dateFormat1.format(dateFormat2.parse(strdate));
        } catch (Exception e) {
            return BLANK;
        }
        return str;
    }

    public static String stringToDateFormatStr(String strdate) {
        String str = BLANK;
        if (strdate == null) {
            return BLANK;
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
            str = dateFormat.format(date.parse(strdate));

        } catch (Exception e) {
            return BLANK;
        }
        return str;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isUpdateAvailable(String curVer, String storeVer) {

        int reqCode[] = new int[3];
        int latestCode[] = new int[3];
        int i = 0;

        try {
            StringTokenizer st = new StringTokenizer(curVer.trim(), ".");
            while (st.hasMoreElements()) {
                reqCode[i] = Integer.parseInt(st.nextElement().toString());
                i++;
            }
            i = 0;
            StringTokenizer st2 = new StringTokenizer(storeVer.trim(), ".");
            while (st2.hasMoreElements()) {
                latestCode[i] = Integer.parseInt(st2.nextElement().toString());
                i++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

        for (int j = 0; j < 3; j++) {
            if (reqCode[j] > latestCode[j]) {
                return false;
            } else if (reqCode[j] < latestCode[j]) {
                return true;
            }
        }

        return false;
    }

    public static String getFormatTimestampString(String timestamp) {
        String timestampString = null;

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy KK:mm a");

        try {
            Date date = inputFormat.parse(timestamp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, 6);
            cal.add(Calendar.MINUTE, 30);
            date = cal.getTime();

            timestampString = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestampString;
    }

    public static String getFormatTimestampStringOld(String timestamp) {
        String timestampString = null;

        DateFormat inputFormat = new SimpleDateFormat("dd MMM yy hh:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy KK:mm a");

        try {
            Date date = inputFormat.parse(timestamp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, 6);
            cal.add(Calendar.MINUTE, 30);
            date = cal.getTime();

            timestampString = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestampString;
    }

    public static String getFormatTimestampStringFreeOld(String timestamp) {
        String timestampString = null;

        DateFormat inputFormat = new SimpleDateFormat("dd MMM yy KK:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yy KK:mm a");

        try {
            Date date = inputFormat.parse(timestamp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            date = cal.getTime();

            timestampString = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestampString;
    }

    public static Bitmap fileToBitmapOrientationVerified(File mFile) {
        Bitmap verifiedBitmap = null;
        if (mFile != null) {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(mFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
            verifiedBitmap = rotateBitmap(bitmap, orientation);
        }
        return verifiedBitmap;
    }

    //encode image to string
    public static String encodeFileToString(File mFile) {

        final long fileSize = mFile.length();

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        //convert image to byte array.
        Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());

        Bitmap bmRotated = rotateBitmap(bitmap, orientation);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final long LOW_QUALITY_IMAGE = 262145; //0.25MB.
        final long MEDIUM_QUALITY_IMAGE = 524289; //0.5MB.
        final long HIGH_QUALITY_IMAGE = 1572865; //1.5MB.

        if (fileSize < LOW_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        }
        if (fileSize > LOW_QUALITY_IMAGE && fileSize < MEDIUM_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        } else if (fileSize > MEDIUM_QUALITY_IMAGE && fileSize < HIGH_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        } else {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }

        byte[] bytes = baos.toByteArray();
        //long length = bytes.length;
        //Convert image to Base64 encoded string.
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    //encoded image to byte array
    public static byte[] encodedFileToByteArray(File mFile) {

        final long fileSize = mFile.length();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        //convert image to byte array.
        Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());

        Bitmap bmRotated = rotateBitmap(bitmap, orientation);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final long LOW_QUALITY_IMAGE = 262145; //0.25MB.
        final long MEDIUM_QUALITY_IMAGE = 524289; //0.5MB.
        final long HIGH_QUALITY_IMAGE = 1572865; //1.5MB.

        if (fileSize < LOW_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        if (fileSize > LOW_QUALITY_IMAGE && fileSize < MEDIUM_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        } else if (fileSize > MEDIUM_QUALITY_IMAGE && fileSize < HIGH_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        } else {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public static byte[] encodedFileToByteArray2(File mFile) {

        final long fileSize = mFile.length();
        Log.e("TAG", " Encoded File Size : " + (fileSize / 1024) + " KB.");
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        //convert image to byte array.
        Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());

        Bitmap bmRotated = rotateBitmap(bitmap, orientation);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final long LOW_QUALITY_IMAGE = 262145; //0.25MB.
        final long MEDIUM_QUALITY_IMAGE = 524289; //0.5MB.
        final long HIGH_QUALITY_IMAGE = 1572865; //1.5MB.
        final long HIGH_DEFINITION_IMAGE = 2621440; //2.5MB.
        final long HIGH_RESOLUTION_IMAGE = 3670016; //3.5MB.

        if (fileSize < LOW_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 30, baos); //n < 0.25 MB.
            Log.e("TAG", "Compressed to 30% of original.");
        }
        if (fileSize > LOW_QUALITY_IMAGE && fileSize < MEDIUM_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 25, baos); //0.25 < n < 0.5 MB.
            Log.e("TAG", "Compressed to 25% of original.");
        } else if (fileSize > MEDIUM_QUALITY_IMAGE && fileSize < HIGH_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 15, baos); //0.5 < n < 1.5 MB.
            Log.e("TAG", "Compressed to 15% of original.");
        } else if (fileSize > HIGH_QUALITY_IMAGE && fileSize < HIGH_DEFINITION_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 10, baos); //1.5 < n < 2.5 MB.
            Log.e("TAG", "Compressed to 10% of original.");
        } else if (fileSize > HIGH_DEFINITION_IMAGE && fileSize < HIGH_RESOLUTION_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 8, baos); //2.5 < n < 3.5 MB.
            Log.e("TAG", "Compressed to 8% of original.");
        } else {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 5, baos); // n > 3.5 MB.
            Log.e("TAG", "Compressed to 5% of original.");
        }

        byte[] bytes = baos.toByteArray();
        return bytes;

    }

    public static Bitmap encodedFileToBitmap(File mFile) {

        final long fileSize = mFile.length();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        //convert image to byte array.
        Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());

        Bitmap bmRotated = rotateBitmap(bitmap, orientation);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        final long LOW_QUALITY_IMAGE = 262145; //0.25MB.
        final long MEDIUM_QUALITY_IMAGE = 524289; //0.5MB.
        final long HIGH_QUALITY_IMAGE = 1572865; //1.5MB.

        if (fileSize < LOW_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        if (fileSize > LOW_QUALITY_IMAGE && fileSize < MEDIUM_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        } else if (fileSize > MEDIUM_QUALITY_IMAGE && fileSize < HIGH_QUALITY_IMAGE) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        } else {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        return bmRotated;
    }

    public static Bitmap decodeStringToBitmap(String imgEncodeStr) {
        try {
            byte[] decodedString = Base64.decode(imgEncodeStr, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //get app url.
    public static String getAppUrl(String applicationId) {
        return "https://play.google.com/store/apps/details?id=" + applicationId + "&hl=en";
    }

    //eng-only
    public static boolean isPureAscii(String v) {
        //return Charset.forName("US-ASCII").newEncoder().canEncode(v);
        // or "ISO-8859-1" for ISO Latin 1
        // or StandardCharsets.US_ASCII with JDK1.7+
        if (!v.isEmpty()) {
            if (v.matches("[a-zA-Z0-9 ]+")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isAsciiString(String v) {
        byte bytearray[] = v.getBytes();
        CharsetDecoder d = Charset.forName("US-ASCII").newDecoder();
        try {
            CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
            r.toString();
        } catch (CharacterCodingException e) {
            return false;
        }
        return true;
    }

    //check lat-lang validation
    public static boolean isLatLongValid(String latitude, String longitude) {
        try {
            final double lat = Double.parseDouble(latitude);
            final double lng = Double.parseDouble(longitude);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //set url word linkable.
    public static String purifyMessage(String message) {
        String text = BLANK;
        if (message != null || message.isEmpty()) {
            String[] words = message.split("[:space:]");
            for (String word : words) {
                if (URLUtil.isValidUrl(word)) {
                    text = text + SPACE + concatStrToUrl(word);
                } else {
                    text = text + SPACE + word;
                }
            }
            return text;
        } else {
            return text;
        }
    }

    public static String concatStrToUrl(String msg) {
        return "<a href='" + msg + "'>" + msg + "</a>";
    }

    public static String stringToHtmlUrl(String strUrl) {
        String tempHtml = "<a href = '#'>" + strUrl + "</a>";
        if (URLUtil.isValidUrl(strUrl)) {
            //tempHtml = "<a href = '"+strUrl+"'>"+strUrl+"</a>&nbsp;&nbsp;<img src='../res/drawable/url_link_icon.png' width='8' height='8' padding='2'/>";
            tempHtml = "<a style='text-decoration:none;' href = '" + strUrl + "'>" + strUrl + "</a>";
        }
        return tempHtml;
    }

    public static boolean isUnique(int[] nums) {
        Set<Integer> set = new HashSet<>(nums.length);
        for (int a : nums) {
            if (!set.add(a))
                return false;
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    //check biometric sensor-exist or not.
    public static BiometricSensorStatus checkBiometricSensor(Context mContext) {

        //Use FingerprintManager for API > 28
        FingerprintManager fingerprintManager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);

        if (fingerprintManager == null) {
            //Device does not support getSystemService() method.
            return BiometricSensorStatus.DOES_NOT_SUPPORT;
        }

        if (!fingerprintManager.isHardwareDetected()) {
            // Device doesn't support fingerprint authentication
            return BiometricSensorStatus.DOES_NOT_SUPPORT;
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            // User hasn't enrolled any fingerprints to authenticate with
            return BiometricSensorStatus.NOT_ENROLLED;
        }

        // Everything is ready for fingerprint authentication
        return BiometricSensorStatus.BIOMETRIC_OK;

    }

    public static String setAeonPhoneNo(String charSequence, String phoneNo) {
        return charSequence.replace("[phone_no]", phoneNo);
    }

    public static String modifyPhoneNo(String phoneNo) {
        if (phoneNo != null) {
            if (phoneNo.length() > 4) {
                String countryCode = phoneNo.substring(0, 3);
                if (countryCode != null) {
                    if (countryCode.equals("+95")) {
                        return "0" + phoneNo.substring(3, phoneNo.length());
                    } else {
                        return phoneNo;
                    }
                }
            }
        }
        return BLANK;
    }

    //welcome letter.
    public static String getWelcomeLetterLink(String agreementNo) {
        return WELCOME_LETTER_BASE_URL + agreementNo;
    }

    public static String getUnderlineText(String agreementNo) {
        return "<u>" + agreementNo + "</u>";
    }

    public static Typeface getPyidaungSuRegular(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/pyidaungsu_regular.ttf");
        return typeface;
    }

    public static Typeface getPyidaungSuBold(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/pyidaungsu_bold.ttf");
        return typeface;
    }

    public static void displayMessage(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_msg_layout, null, false);
        TextView textView = view.findViewById(R.id.lbl_message);
        textView.setText(message);
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.pyidaungsu_regular));
        Toast toast = new Toast(context);
        toast.setView(view);
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static String getChangeTimestampToString3(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String string = dateFormat.format(timestamp);
        return string;
    }

    public static void addEditPhotoInfo(Context context, PurchaseAttachEditTempBean purchaseAttachEditTempBean) {
        final int daApplicationInfoAttachmentId = purchaseAttachEditTempBean.getDaApplicationInfoAttachmentId();

        List<PurchaseAttachEditTempBean> purchaseAttachEditTempBeanList
                = PreferencesManager.getEditPhotoList(context);

        if (purchaseAttachEditTempBeanList != null) {

            if (purchaseAttachEditTempBeanList.size() > 0) {
                int index = 0;
                boolean isDuplicate = false;
                for (PurchaseAttachEditTempBean attachEditTempBean : purchaseAttachEditTempBeanList) {
                    if (daApplicationInfoAttachmentId
                            == attachEditTempBean.getDaApplicationInfoAttachmentId()) {
                        purchaseAttachEditTempBeanList.get(index).setAbsFilePath(purchaseAttachEditTempBean.getAbsFilePath());
                        purchaseAttachEditTempBeanList.get(index).setDaApplicationInfoAttachmentId(daApplicationInfoAttachmentId);
                        purchaseAttachEditTempBeanList.get(index).setFileType(purchaseAttachEditTempBean.getFileType());
                        purchaseAttachEditTempBeanList.get(index).setFileName(purchaseAttachEditTempBean.getFileName());
                        isDuplicate = true;
                        break;
                    }
                    index++;
                }
                if (!isDuplicate) {
                    purchaseAttachEditTempBeanList.add(purchaseAttachEditTempBean);
                }
            } else {
                purchaseAttachEditTempBeanList.add(purchaseAttachEditTempBean);
            }

            PreferencesManager.saveEditPhotoList(context, purchaseAttachEditTempBeanList);
        }
    }

    public static NrcBean getNrcFromString(String nrcStr, Context context) {
        try {
            byte[] bytes = nrcStr.getBytes();
            int startDivCodeIndex = 0;
            int endDivCodeIndex = 0;
            for (int i = 0; i < bytes.length; i++) {
                char ch = (char) bytes[i];
                if (ch == '/') {
                    endDivCodeIndex = i;
                    break;
                }
            }
            final int divCode = Integer
                    .parseInt(nrcStr.substring(startDivCodeIndex, endDivCodeIndex));
            int startTsIndex = 0;
            int endTsIndex = 0;
            for (int j = 0; j < bytes.length; j++) {
                char ch = (char) bytes[j];
                if (ch == '/') {
                    startTsIndex = j + 1;
                }
                if (ch == '(') {
                    endTsIndex = j;
                    break;
                }
            }
            final String townshipCode = nrcStr.substring(startTsIndex, endTsIndex);
            String nrcTypeStr = nrcStr.substring((nrcStr.length() - 9), (nrcStr.length() - 6));
            final int nrcType = getNrcType(nrcTypeStr, context);
            final int registrationCode = Integer
                    .parseInt(nrcStr.substring(nrcStr.length() - 6));
            NrcBean nrc = new NrcBean();
            nrc.setDivCode(divCode);
            nrc.setNrcType(nrcType);
            nrc.setTownshipCode(townshipCode);
            nrc.setRegistrationCode(registrationCode);
            return nrc;
        } catch (Exception e) {
            return null;
        }
    }

    private static int getNrcType(String nrcType, Context context) {
        String[] nrcTypes = context.getResources().getStringArray(R.array.nrc_type);
        for (int i = 0; i < nrcTypes.length; i++) {
            if (nrcType.equals(nrcTypes[i])) {
                return i;
            }
        }
        return 0;
    }

    public static boolean isEmptyOrNull(String text) {
        if (text == null) {
            return true;
        } else if (TextUtils.isEmpty(text.trim())) {
            return true;
        }
        return false;
    }

    /*public static int getActionBarSize(Resources resources) {
        return resources.getInteger(R.integer.action_bar_size);
    }*/
}
