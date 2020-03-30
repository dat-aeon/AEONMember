package mm.com.aeon.vcsaeon.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import mm.com.aeon.vcsaeon.R;
import mm.com.aeon.vcsaeon.common_utils.CommonUtils;
import mm.com.aeon.vcsaeon.common_utils.PreferencesManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Rational;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

import static mm.com.aeon.vcsaeon.common_utils.CommonUtils.displayMessage;

public class CameraxActivity extends AppCompatActivity {

    TextureView textureView;
    TextView textCamView;

    ViewGroup.LayoutParams layoutParams;

    private CameraX.LensFacing lensFacing = CameraX.LensFacing.FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_x_preview);

        Window window = this.getWindow();
        window.setStatusBarColor(getColor(R.color.statusBar));

        textureView = findViewById(R.id.view_finder);
        textCamView = findViewById(R.id.camera_text);
        changeLabel();
        startCamera();
    }

    @SuppressLint("RestrictedApi")
    private void startCamera() {

        CameraX.unbindAll();

        double cameraWidth = 0.0;
        double cameraHeight = 0.0;

        try{
            //landscape camera view.
            cameraWidth = CameraX.getSurfaceManager().getPreviewSize().getWidth();
            cameraHeight = CameraX.getSurfaceManager().getPreviewSize().getHeight();
        } catch (Exception e){
            e.printStackTrace();
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = (int)(cameraHeight);
        int height = (int)(cameraWidth);

        //set on texture-view.
        layoutParams = new RelativeLayout.LayoutParams(width,height);

        PreviewConfig pConfig = new PreviewConfig.Builder()
                .setLensFacing(lensFacing)
                .setTargetAspectRatio(new Rational(width,height))
                .setTargetResolution(new Size(width,height))
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

        Preview preview = new Preview(pConfig);
        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we  have to destroy it first then re-add it
                    @Override
                    public void onUpdated(Preview.PreviewOutput output){
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);
                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
                });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setLensFacing(lensFacing)
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        findViewById(R.id.imgCapture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir + "/" + System.currentTimeMillis() + ".jpg");
                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(file.getAbsolutePath()));
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed : " + message;
                        //Toast.makeText(getBaseContext(), msg,Toast.LENGTH_LONG).show();
                        displayMessage(getBaseContext(), msg);
                        if(cause != null){
                            cause.printStackTrace();
                        }
                    }
                });
            }
        });

        //bind to lifecycle:
        CameraX.bindToLifecycle(this, preview, imgCap);
    }

    private void updateTransform(){
        Matrix mx = new Matrix();

        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int)textureView.getRotation();

        switch(rotation){
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float)rotationDgr, cX, cY);
        textureView.setAlpha(1.0f);
        textureView.setLayoutParams(layoutParams);
        textureView.setCameraDistance(10.0f);
        textureView.setTransform(mx);
    }

    public void changeLabel(){
        final String language = PreferencesManager.getCurrentLanguage(getApplicationContext());
        textCamView.setText(CommonUtils.getLocaleString(new Locale(language), R.string.camera_view_label, getApplicationContext()));
    }
}
