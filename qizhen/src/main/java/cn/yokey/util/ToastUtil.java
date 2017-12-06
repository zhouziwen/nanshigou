package cn.yokey.util;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast = null;

    public static void show(Activity activity, String content) {

        if (toast == null) {
            toast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        toast.show();

    }

    public static void showNetworkError(Activity activity) {

        String content = "网络连接失败,请检查设置";

        if (toast == null) {
            toast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        toast.show();

    }

}
