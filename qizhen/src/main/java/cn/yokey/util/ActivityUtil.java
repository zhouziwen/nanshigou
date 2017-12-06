package cn.yokey.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import java.io.File;

import cn.yokey.nsg.LoginActivity;
import cn.yokey.system.Constant;

public class ActivityUtil {

    public static void finish(Activity activity) {

        activity.finish();

    }

    public static void start(Activity activity, Intent intent) {

        activity.startActivity(intent);

    }

    public static void startWithLogin(Activity activity, Intent intent) {

        if (Constant.userLoginBoolean) {
            activity.startActivity(intent);
        } else {
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }

    }

    public static void startWithResult(Activity activity, Intent intent, int result) {

        activity.startActivityForResult(intent, result);

    }

    public static void startSetting(Activity activity, String name) {

        try {
            switch (name) {
                case "All":
                    activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    break;
                case "Wifi":
                    activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    break;
                default:
                    activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void startInstallApk(Activity activity, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }

    public static void startShare(Activity activity, String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/*");
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(sendIntent);
    }

}
