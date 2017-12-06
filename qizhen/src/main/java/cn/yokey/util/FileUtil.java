package cn.yokey.util;

import android.os.Environment;

import java.io.File;

public class FileUtil {

    public static String downPathString = "Android/YoKey/Nsg/Down";
    public static String cachePathString = "Android/YoKey/Nsg/Cache";

    public static String getRootPath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/";
        }
    }

    public static String getCachePath() {
        if (hasSDCard()) {
            return getRootPath() + cachePathString;
        } else {
            return getRootPath() + cachePathString;
        }
    }

    public static String getDownPath() {
        if (hasSDCard()) {
            return getRootPath() + downPathString;
        } else {
            return getRootPath() + downPathString;
        }
    }

    public static void createCachePath() {
        File file = new File(getCachePath());
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createDownPath() {
        File file = new File(getDownPath());
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static boolean hasSDCard() {
        String str = Environment.getExternalStorageState();
        return str.equals(Environment.MEDIA_MOUNTED);
    }

}
