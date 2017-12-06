package cn.yokey.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.yokey.nsg.R;

public class DialogUtil {

    public static Dialog dialog;
    public static ProgressDialog progressDialog;

    public static void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    public static void progress(Activity activity) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在处理...");
        progressDialog.show();

    }

    public static void progress(Activity activity, String title) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(title);
        progressDialog.show();

    }

    public static void query(Activity activity, CharSequence title, CharSequence content, View.OnClickListener clickListener) {

        try {
            dialog = new AlertDialog.Builder(activity).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_query);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
            titleTextView.setText(title);
            TextView contentTextView = (TextView) window.findViewById(R.id.contentTextView);
            contentTextView.setText(content);
            TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
            confirmTextView.setOnClickListener(clickListener);
            TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
            cancelTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void query(Activity activity, CharSequence title, CharSequence content, View.OnClickListener clickListener, View.OnClickListener clickListener1) {

        try {
            dialog = new AlertDialog.Builder(activity).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Window window = dialog.getWindow();
            window.setContentView(R.layout.dialog_query);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
            titleTextView.setText(title);
            TextView contentTextView = (TextView) window.findViewById(R.id.contentTextView);
            contentTextView.setText(content);
            TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
            confirmTextView.setOnClickListener(clickListener);
            TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
            cancelTextView.setOnClickListener(clickListener1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}