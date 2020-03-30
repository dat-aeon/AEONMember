package mm.com.aeon.vcsaeon.common_utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import mm.com.aeon.vcsaeon.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.CAMERA_REQUEST_CODE;
import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.STORAGE_REQUEST_CODE;

public class CameraUtil {

    public static final String FILE_DATE_FORMAT = "yyyyMMddHHmm";
    public static final long FOLDER_KB_DIVISOR = 1024;
    public static final long FILE_LIMITED_SIZE_IN_B = 1228; //in byte. ~1.2 MB.
    public static final String WARNING_MESSAGE = "Warning Message";
    public static final String CAMERA_PERMISSION = "Camera permission is need for photo taking.";

    public static final int PURCHASE_APP_IMG_WIDTH = 582;
    public static final int PURCHASE_APP_IMG_HEIGHT = 826;

    public static File CreateImageFile(Activity activity, String rootFolderName, String rootFolderPath, String currentApply){

        File photoFile = null;
        try {
            photoFile = createImageFile(activity, rootFolderName, rootFolderPath, currentApply);
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        return photoFile;
    }

    public static File createImageFile(Activity activity, String rootFolderName, String rootFolderPath, String currentApply) throws IOException {

        String timeStamp = new SimpleDateFormat(FILE_DATE_FORMAT).format(new Date());
        //String imageFileName= timeStamp + "-" + currentApply;
        String imageFileName= timeStamp+ "_";

        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + rootFolderName);
        File image = File.createTempFile(
                imageFileName.toUpperCase(),  /* prefix */
                CommonConstants.UPLOAD_FILE_TYPE,         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Check camera allowed or not.
    public static boolean isCameraAllowed(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            AlertDialog.Builder mDialog = new AlertDialog.Builder(activity);
            mDialog.setTitle(WARNING_MESSAGE);
            mDialog.setMessage(CAMERA_PERMISSION);
            mDialog.setIcon(R.drawable.ic_warning_black_24dp);
            mDialog.setCancelable(false);
            mDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                            dialog.dismiss();
                        }
                    });
            //mDialog.show();
            return false;
        }
    }

    public static boolean isStorageAllowed(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    //Resize captured image with defined resolution ratios.
    public static String resizeImages(String sPath, int photoQuality, Activity activity, int width, int height) throws IOException {
        String compressedPhotoPath = null;
        boolean isVertical = true;
        Bitmap bitmap = new GetImageThumbnail().getThumbnail(sPath, activity, width, height, isVertical);

        File before = new File(sPath);
        long photoMb = before.length() / FOLDER_KB_DIVISOR;
        Log.e("Original KB", String.valueOf(photoMb)+" KB");

        try {
            boolean keepReduce = true;
            int photoQuality2 = photoQuality;
            while (keepReduce) {
                compressedPhotoPath = saveBitmapToJpg(bitmap, sPath, photoQuality2, 800);
                File file = new File(compressedPhotoPath);
                long fileSize = file.length() / FOLDER_KB_DIVISOR;
                if (fileSize > FILE_LIMITED_SIZE_IN_B) {
                    photoQuality2 = photoQuality2 - 1;
                    keepReduce = true;
                } else {
                    Log.e("Resize KB", fileSize+" KB");
                    break;
                }
            }
            return compressedPhotoPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String reduceFileSize(String sPath, int photoQuality){
        String compressedPhotoPath = null;
        Bitmap bitmap = BitmapFactory.decodeFile(sPath);

        File before = new File(sPath);
        long photoMb = before.length() / FOLDER_KB_DIVISOR;
        Log.e("Original KB", String.valueOf(photoMb)+" KB");

        try {
            boolean keepReduce = true;
            int photoQuality2 = photoQuality;
            while (keepReduce) {
                compressedPhotoPath = saveBitmapToJpg(bitmap, sPath, photoQuality2, 800);
                File file = new File(compressedPhotoPath);
                long fileSize = file.length() / FOLDER_KB_DIVISOR;
                if (fileSize > FILE_LIMITED_SIZE_IN_B) {
                    photoQuality2 = photoQuality2 - 1;
                    keepReduce = true;
                } else {
                    Log.e("Resize KB", fileSize+" KB");
                    break;
                }
            }
            return compressedPhotoPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String saveBitmapToJpg(Bitmap bitmap, String fileName, int photoQuality, int dpi) throws IOException {
        File file = new File(fileName);
        String fileAbsPath = file.getAbsolutePath();
        ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, photoQuality, imageByteArray);
        byte[] imageData = imageByteArray.toByteArray();
        setDpi(imageData, dpi);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(imageData);
            fileOutputStream.flush();
            fileOutputStream.close();
            return fileAbsPath;
        } catch (Exception e) {
            e.printStackTrace();
            return fileAbsPath;
        }
    }

    public static byte[] BitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);

        return  baos.toByteArray();
    }

    private static void setDpi(byte[] imageData, int dpi) {
        imageData[13] = (byte) 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 255);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 255);
    }

    public static void removeCapturedNrc(String key, Map<String, String> imgMap){
        for(Map.Entry<String, String> entry : imgMap.entrySet()) {
            if(entry.getKey()!=null){
                if(entry.getKey().equals(key)){
                    File file = new File(entry.getValue());
                    if(file.isFile()){
                        file.delete();
                    }
                }
            }
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(CommonConstants.IMG), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static File renameFileName(Context context, File currentFile, String rootFolderName){

        String filepath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + rootFolderName + "/").getPath();
        File from = new File(filepath, currentFile.getName());
        File renameFile = new File(filepath, new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".jpg");
        from.renameTo(renameFile);
        Log.e("from file", from.getAbsolutePath());

        return renameFile;
    }
}
