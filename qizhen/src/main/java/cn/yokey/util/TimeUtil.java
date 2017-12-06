package cn.yokey.util;

import android.text.format.Time;

@SuppressWarnings("all")
public class TimeUtil {

    public static String getAll() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%Y-%m-%d %H:%M:%S");
    }

    public static String getDate() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%Y-%m-%d");
    }

    public static String getTime() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%H:%M:%S");
    }

    public static String getYear() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%Y");
    }

    public static String getMouth() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%m");
    }

    public static String getDay() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%d");
    }

    public static String getHour() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%H");
    }

    public static String getMinute() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%M");
    }

    public static String getSeconds() {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        return localTime.format("%S");
    }

    public static String decode(String time) {

        int iDayNow = Integer.parseInt(getDay());
        int iDayRec = Integer.parseInt(time.substring(time.lastIndexOf("-") + 1, time.lastIndexOf("-") + 3));
        int iMouthNow = Integer.parseInt(getMouth());
        int iMouthRec = Integer.parseInt(time.substring(time.indexOf("-") + 1, time.indexOf("-") + 3));
        int iYearNow = Integer.parseInt(getYear());
        int iYearRec = Integer.parseInt(time.substring(0, time.indexOf("-")));

        if (iYearNow == iYearRec) {
            if (iMouthNow == iMouthRec) {
                if (iDayNow == iDayRec) {
                    return time.substring(time.lastIndexOf("-") + 3, time.length() - 3);
                } else if (iDayNow - iDayRec == 1) {
                    return "昨天" + time.substring(time.lastIndexOf("-") + 3, time.length() - 3);
                } else if (iDayNow - iDayRec == 2) {
                    return "前天" + time.substring(time.lastIndexOf("-") + 3, time.length() - 3);
                } else {
                    return iDayNow - iDayRec + " 天前" + time.substring(time.lastIndexOf("-") + 3, time.length() - 3);
                }
            } else {
                return (iMouthNow - iMouthRec) + " 个月前";
            }
        } else {
            return (iYearNow - iYearRec) + " 年前";
        }

    }

}
