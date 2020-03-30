package mm.com.aeon.vcsaeon.common_utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GetImageThumbnail {
    public Bitmap getThumbnail(String uri, Context context, int widthLength, int heightLength, boolean isVertical) {
        Bitmap scaled = null;
        try {
            InputStream input;
            try{
                input = context.getContentResolver().openInputStream(Uri.parse(uri));
            } catch (Exception e){
                input = context.getContentResolver().openInputStream(Uri.fromFile(new File(uri)));
            }
            Options onlyBoundsOptions = new Options();
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
                return null;
            }
            Options bitmapOptions = new Options();
            try{
                input = context.getContentResolver().openInputStream(Uri.parse(uri));
            } catch (Exception e) {
                input = context.getContentResolver().openInputStream(Uri.fromFile(new File(uri)));
            }
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            int height = onlyBoundsOptions.outHeight;
            int width = onlyBoundsOptions.outWidth;

            int rotateDegree = 0;

            Matrix matrix = new Matrix();
            matrix.postRotate((float) rotateDegree);

            if(width > height){
                if(isVertical){
                    scaled = Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                            matrix, true), heightLength, widthLength,true);
                } else {
                    scaled = Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                            matrix, true), widthLength, heightLength, true);
                }
            } else {
                scaled = Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true), widthLength, heightLength, true);
            }

            if((scaled.getWidth()>scaled.getHeight())){
                rotateDegree=90;
                matrix.postRotate((float) rotateDegree);
                scaled = Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true), widthLength, heightLength, true);
            }

            return scaled;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
