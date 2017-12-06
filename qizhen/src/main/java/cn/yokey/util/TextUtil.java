package cn.yokey.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    public static boolean isJson(String content) {

        return content.length() > 8 && content.contains("{") && content.contains("}");

    }

    public static boolean isEmpty(String content) {

        return content == null || content.isEmpty() || content.length() == 0 || content.equals("");

    }

    public static boolean isNcJson(String content) {

        return content.length() > 8 && content.contains("{") && content.contains("}") && content.contains("\"code\":200") && !content.contains("\"error\"");

    }

    public static boolean isUrlAddress(String url) {
        if (url.contains("http") || url.contains("www.") || url.contains(".com") || url.contains(".cn")) {
            return true;
        }
        return false;
    }

    public static boolean isEmailAddress(String url) {
        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(url);
        return matcher.matches();
    }

    public static String encodeHtml(String html) {
        html = "<html><head><style type='text/css'>p,img,table{width:100%}</style></head><body>" + html + "</body></html>";
        return html;
    }

    public static String encodeBrand(String brand) {
        String s;
        switch (brand) {
            case "teclast":
                s = "台电";
                break;
            case "Teclast":
                s = "台电";
                break;
            case "TECLAST":
                s = "台电";
                break;
            case "sony":
                s = "索尼";
                break;
            case "Sony":
                s = "索尼";
                break;
            case "SONY":
                s = "索尼";
                break;
            case "huawei":
                s = "华为";
                break;
            case "Huawei":
                s = "华为";
                break;
            case "HUAWEI":
                s = "华为";
                break;
            case "honor":
                s = "华为荣耀";
                break;
            case "Honor":
                s = "华为荣耀";
                break;
            case "HONNOR":
                s = "华为荣耀";
                break;
            case "meizu":
                s = "魅族";
                break;
            case "Meizu":
                s = "魅族";
                break;
            case "MEIZU":
                s = "魅族";
                break;
            case "motorola":
                s = "摩托罗拉";
                break;
            case "Motorola":
                s = "摩托罗拉";
                break;
            case "MOTOROLA":
                s = "摩托罗拉";
                break;
            case "lenovo":
                s = "联想";
                break;
            case "Lenovo":
                s = "联想";
                break;
            case "LENOVO":
                s = "联想";
                break;
            case "coolpad":
                s = "酷派";
                break;
            case "Coolpad":
                s = "酷派";
                break;
            case "COOLPAD":
                s = "酷派";
                break;
            case "letv":
                s = "乐视";
                break;
            case "Letv":
                s = "乐视";
                break;
            case "LETV":
                s = "乐视";
                break;
            case "xiaomi":
                s = "小米";
                break;
            case "Xiaomi":
                s = "小米";
                break;
            case "XIAOMI":
                s = "小米";
                break;
            case "nubia":
                s = "努比亚";
                break;
            case "Nubia":
                s = "努比亚";
                break;
            case "NUBIA":
                s = "努比亚";
                break;
            case "zte":
                s = "中兴";
                break;
            case "Zte":
                s = "中兴";
                break;
            case "ZTE":
                s = "中兴";
                break;
            case "asus":
                s = "华硕";
                break;
            case "Asus":
                s = "华硕";
                break;
            case "ASUS":
                s = "华硕";
                break;
            case "gionee":
                s = "金立";
                break;
            case "Gionee":
                s = "金立";
                break;
            case "GIONEE":
                s = "金立";
                break;
            case "K-touch":
                s = "天语";
                break;
            case "K-Touch":
                s = "天语";
                break;
            case "K-TOUCH":
                s = "天语";
                break;
            case "doov":
                s = "朵唯";
                break;
            case "Doov":
                s = "朵唯";
                break;
            case "DOOV":
                s = "朵唯";
                break;
            default:
                s = brand;
                break;
        }
        return s;
    }

    public static HashMap<String, String> jsonToHashMap(String json) {

        return JSON.parseObject(json, new TypeReference<HashMap<String, String>>() {
        });

    }

    public static Vector<String> encodeImageWithVector(String content) {
        Vector<String> vector = new Vector<>();
        if (!content.equals("null")) {
            content = content.replace("[", "").replace("]", "");
            if (!content.contains(",")) {
                vector.add(content);
            } else {
                while (content.contains(",")) {
                    String temp = content.substring(0, content.indexOf(","));
                    content = content.substring(content.indexOf(",") + 1, content.length());
                    vector.add(temp.replace(" ", ""));
                }
                vector.add(content.replace(" ", ""));
            }
        }
        return vector;
    }

    public static ArrayList<HashMap<String, String>> jsonToArrayList(String json) {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        try {
            HashMap<String, String> hashMap = new HashMap<>();
            JSONObject jsonObject = new JSONObject(json);
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jsonObject.get(key).toString();
                hashMap.put("key", key);
                hashMap.put("value", value);
                arrayList.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;

    }

    public static ArrayList<HashMap<String, String>> encodeImageWithArrayList(String content) {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        if (!content.equals("null")) {
            content = content.replace("[", "").replace("]", "");
            if (!content.contains(",")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("image", content);
                arrayList.add(hashMap);
            } else {
                while (content.contains(",")) {
                    String temp = content.substring(0, content.indexOf(","));
                    content = content.substring(content.indexOf(",") + 1, content.length());
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("image", temp.replace(" ", ""));
                    arrayList.add(hashMap);
                }
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("image", content.replace(" ", ""));
                arrayList.add(hashMap);
            }
        }

        return arrayList;

    }

}