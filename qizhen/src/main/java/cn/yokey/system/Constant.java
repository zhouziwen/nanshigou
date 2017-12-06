package cn.yokey.system;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.animation.AlphaAnimation;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import net.tsz.afinal.FinalHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.util.FileUtil;
import cn.yokey.util.TextUtil;

@SuppressWarnings("all")
public class Constant {

    //系统常量
    public static final String SYSTEM_TYPE = "android";
    public static final String SYSTEM_VERSION = "V1.0";
    public static final String SYSTEM_SHARE_NAME = "Yokey_Nsg";

    public static final String LINK_MAIN = "http://www.nanshig.com/";

    public static final String LINK_WAP = LINK_MAIN + "wap/";
    public static final String LINK_WAP_FIND_PASSWORD = LINK_WAP + "tmpl/member/find_password.html";

    public static final String LINK_MOBILE = LINK_MAIN + "mobile/index.php?act=";
    public static final String LINK_MOBILE_INDEX = LINK_MOBILE + "index";                                                          //首页 GET
    public static final String LINK_MOBILE_LOGIN = LINK_MOBILE + "login";                                                          //登录 POST
    public static final String LINK_MOBILE_REG = LINK_MOBILE + "login&op=register";                                                //注册 POST
    public static final String LINK_MOBILE_LOGOUT = LINK_MOBILE + "logout";                                                        //注销 POST
    public static final String LINK_MOBILE_USER = LINK_MOBILE + "member_index";                                                    //个人中心 POST
    public static final String LINK_MOBILE_CLASS = LINK_MOBILE + "goods_class";                                                    //分类 GET
    public static final String LINK_MOBILE_CART = LINK_MOBILE + "member_cart&op=cart_list";                                        //购物车 POST
    public static final String LINK_MOBILE_CART_DEL = LINK_MOBILE + "member_cart&op=cart_del";                                     //购物车删除 POST
    public static final String LINK_MOBILE_CART_ADD = LINK_MOBILE + "member_cart&op=cart_add";                                     //购物车添加 POST
    public static final String LINK_MOBILE_AREA = LINK_MOBILE + "member_address&op=area_list";                                     //地区列表 POST
    public static final String LINK_MOBILE_ORDER = LINK_MOBILE + "member_order&op=order_list";                                     //所有订单 POST
    public static final String LINK_MOBILE_ORDER_CANCEL = LINK_MOBILE + "member_order&op=order_cancel";                            //取消订单 POST
    public static final String LINK_MOBILE_ORDER_REFOUND = LINK_MOBILE + "member_refund&op=get_refund_list&page=100";              //退款订单 GET
    public static final String LINK_MOBILE_ORDER_REFOUND_INFO = LINK_MOBILE + "member_refund&op=get_refund_info";                  //退款订单详细 GET
    public static final String LINK_MOBILE_ORDER_RETURN = LINK_MOBILE + "member_return&op=get_return_list&page=100";               //退货订单 GET
    public static final String LINK_MOBILE_ADDRESS = LINK_MOBILE + "member_address&op=address_list";                               //收货地址 POST
    public static final String LINK_MOBILE_ADDRESS_ADD = LINK_MOBILE + "member_address&op=address_add";                            //添加收货地址 POST
    public static final String LINK_MOBILE_ADDRESS_DEL = LINK_MOBILE + "member_address&op=address_del";                            //删除收货地址 POST
    public static final String LINK_MOBILE_ADDRESS_EDIT = LINK_MOBILE + "member_address&op=address_edit";                          //编辑收货地址 POST
    public static final String LINK_MOBILE_GOODS_BODY = LINK_MOBILE + "goods&op=goods_body";                                       //商品介绍 GET
    public static final String LINK_MOBILE_GOODS_DETAIL = LINK_MOBILE + "goods&op=goods_detail";                                   //商品详细 GET
    public static final String LINK_MOBILE_GOODS_SEARCH = LINK_MOBILE + "goods&op=goods_list&page=100";                            //商品搜索 GET
    public static final String LINK_MOBILE_COLLECTION_BROWS = LINK_MOBILE + "member_goodsbrowse&op=browse_list&page=100";          //我的足迹列表 POST
    public static final String LINK_MOBILE_COLLECTION_GOODS = LINK_MOBILE + "member_favorites&op=favorites_list&page=100";         //商品收藏列表 POST
    public static final String LINK_MOBILE_COLLECTION_GOODS_ADD = LINK_MOBILE + "member_favorites&op=favorites_add";               //商品收藏添加 POST
    public static final String LINK_MOBILE_COLLECTION_GOODS_DEL = LINK_MOBILE + "member_favorites&op=favorites_del";               //商品收藏删除 POST
    public static final String LINK_MOBILE_COLLECTION_GOODS_CHECK = LINK_MOBILE + "member_favorites&op=favorites_check";           //商品是否收藏 POST
    public static final String LINK_MOBILE_COLLECTION_STORE = LINK_MOBILE + "member_favorites_store&op=favorites_list&page=100";   //店铺收藏列表 POST
    public static final String LINK_MOBILE_COLLECTION_STORE_ADD = LINK_MOBILE + "member_favorites_store&op=favorites_add";         //店铺收藏添加 POST
    public static final String LINK_MOBILE_COLLECTION_STORE_DEL = LINK_MOBILE + "member_favorites_store&op=favorites_del";         //店铺收藏删除 POST
    public static final String LINK_MOBILE_COLLECTION_STORE_CHECK = LINK_MOBILE + "member_favorites_store&op=favorites_check";     //店铺是否收藏 POST
    public static final String LINK_MOBILE_BUY_SETUP1 = LINK_MOBILE + "member_buy&op=buy_step1";                                   //商品购买第一步 POST
    public static final String LINK_MOBILE_BUY_SETUP2 = LINK_MOBILE + "member_buy&op=buy_step2";                                   //商品购买第二步 POST
    public static final String LINK_MOBILE_PAY_LIST = LINK_MOBILE + "member_payment&op=payment_list";                              //支付方式列表 POST
    public static final String LINK_MOBILE_PAY_INFO = LINK_MOBILE + "member_buy&op=pay";                                           //订单信息 POST
    public static final String LINK_MOBILE_PAY = LINK_MOBILE + "member_payment&op=pay_new";                                        //支付 GET


    public static final String LINK_ANDROID = LINK_MAIN + "android/";
    public static final String LINK_ANDROID_API = LINK_ANDROID + "api/";
    public static final String LINK_ANDROID_API_ADVERT = LINK_ANDROID_API + "advert.php";
    public static final String LINK_ANDROID_API_SYSTEM = LINK_ANDROID_API + "system.php";
    public static final String LINK_ANDROID_PUBLIC = LINK_ANDROID + "public/";
    public static final String LINK_ANDROID_PUBLIC_HELP = LINK_ANDROID_PUBLIC + "help.html";
    public static final String LINK_ANDROID_PUBLIC_ABOUT = LINK_ANDROID_PUBLIC + "about.html";
    public static final String LINK_ANDROID_PUBLIC_VERSION = LINK_ANDROID_PUBLIC + "version.html";

    //系统变量
    public static FinalHttp mFinalHttp;
    public static AlphaAnimation showAlphaAnimation;
    public static AlphaAnimation goneAlphaAnimation;
    public static SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor mSharedPreferencesEditor;

    public static ArrayList<HashMap<String, String>> advertArrayList;//广告
    public static ArrayList<HashMap<String, String>> systemArrayList;//系统

    //手机信息
    public static String phoneModelString;
    public static String phoneSystemString;

    //系统设置
    public static boolean settingMessageNotifyBoolean;//新消息提醒
    public static boolean settingMessageVoiceNotifyBoolean;//消息到达时-声音提醒
    public static boolean settingMessageVibratorNotifyBoolean;//消息到达时-震动提醒

    public static boolean settingIntelligentImageBoolean;//智能无图

    //用户信息
    public static boolean userLoginBoolean;
    public static boolean userLoginSuccessBoolean;
    public static String userIdString;
    public static String userKeyString;
    public static String userUsernameString;
    public static String userPasswordString;
    public static String userAvatorString;
    public static String userIntegralString;
    public static String userLevelString;
    public static String userMoneyString;

    //初始化
    public static void init(Activity activity) {

        FileUtil.createDownPath();
        FileUtil.createCachePath();

        mFinalHttp = new FinalHttp();
        mFinalHttp.configTimeout(5000);
        mFinalHttp.configCharset("UTF-8");

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(4))
                .build();
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(activity)
                .diskCache(new UnlimitedDiskCache(new File(FileUtil.getCachePath())))
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(displayImageOptions)
                .build());

        showAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        showAlphaAnimation.setDuration(500);
        goneAlphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        goneAlphaAnimation.setDuration(500);

        mSharedPreferences = activity.getSharedPreferences(Constant.SYSTEM_SHARE_NAME, Activity.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();

        try {
            advertArrayList = new ArrayList<>();
            systemArrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(mSharedPreferences.getString("Json_Advert", ""));
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("type", jsonObject.getString("type"));
                    hashMap.put("image", jsonObject.getString("image"));
                    hashMap.put("link", jsonObject.getString("link"));
                    advertArrayList.add(hashMap);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        phoneSystemString = SYSTEM_TYPE + " " + Build.VERSION.RELEASE;
        phoneModelString = TextUtil.encodeBrand(Build.BRAND) + " " + Build.MODEL.replace(Build.BRAND, "");


        settingMessageNotifyBoolean = Constant.mSharedPreferences.getBoolean("Setting_Message_Notify", false);
        settingMessageVoiceNotifyBoolean = Constant.mSharedPreferences.getBoolean("Setting_Message_Voice_Notify", false);
        settingMessageVibratorNotifyBoolean = Constant.mSharedPreferences.getBoolean("Setting_Message_Vibrator_Notify", false);

        userLoginSuccessBoolean = false;
        userLoginBoolean = Constant.mSharedPreferences.getBoolean("User_Login", false);
        userUsernameString = Constant.mSharedPreferences.getString("User_Username", "");
        userPasswordString = Constant.mSharedPreferences.getString("User_Password", "");
        userIdString = "";
        userKeyString = "";
        userMoneyString = "";
        userLevelString = "";
        userAvatorString = "";
        userIntegralString = "";

    }

    //获取广告的图像
    public static String getAdvertImage(String type) {

        if (advertArrayList.size() == 0) {
            return "";
        }

        for (int i = 0; i < advertArrayList.size(); i++) {
            if (type.equals(advertArrayList.get(i).get("type"))) {
                return advertArrayList.get(i).get("image");
            }
        }

        return "";

    }

    //获取广告的连接
    public static String getAdvertLink(String type) {

        if (advertArrayList.size() == 0) {
            return "";
        }

        for (int i = 0; i < advertArrayList.size(); i++) {
            if (type.equals(advertArrayList.get(i).get("type"))) {
                return advertArrayList.get(i).get("link");
            }
        }

        return "";

    }

    //获取系统的信息
    public static String getSystemValue(String name) {

        if (systemArrayList.size() == 0) {
            return "";
        }

        for (int i = 0; i < systemArrayList.size(); i++) {
            if (name.equals(systemArrayList.get(i).get("name"))) {
                return systemArrayList.get(i).get("value");
            }
        }

        return "";

    }

}