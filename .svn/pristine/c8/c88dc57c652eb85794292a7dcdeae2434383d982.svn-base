package mm.com.aeon.vcsaeon.common_utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mm.com.aeon.vcsaeon.R;

import static mm.com.aeon.vcsaeon.common_utils.CommonConstants.NETWORK_ERROR;

public class UiUtils {

    public static void showWarningDialog(Context mContext, String message){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.warning_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        messageBody.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showErrorDialog(Context mContext, String message){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.error_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        messageBody.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showNetworkErrorDialog(Context mContext, String message){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.error_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        TextView textTitle = dialog.findViewById(R.id.text_title);
        textTitle.setText(NETWORK_ERROR);
        messageBody.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showSuccessDialog(Context mContext, String message){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.success_message_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        TextView messageBody = dialog.findViewById(R.id.text_message);
        messageBody.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showMessageDialog(Context mContext, String message){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void closeDialog(ProgressDialog dialog){
        if (dialog != null || dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
