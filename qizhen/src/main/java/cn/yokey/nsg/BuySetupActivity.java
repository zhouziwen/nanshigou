package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import cn.yokey.adapter.BuyGoodsListAdapter;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.LogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;
import cn.yokey.view.AutoListView;

public class BuySetupActivity extends AppCompatActivity {

    public static Activity mActivity;

    private String cart_id, address_id, ifcart;
    private String vat_hash, offpay_hash, password;
    private String invoice_id, voucher, pd_pay, fcode;
    private String offpay_hash_batch, pay_name, rcb_pay;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private ScrollView mScrollView;
    private TextView addressTitleTextView;
    private TextView addressTrueNameTextView;
    private TextView addressPhoneTextView;
    private TextView addressContentTextView;

    private TextView payOnlineTextView;
    private TextView payOfflineTextView;
    private TextView invoiceYesTextView;
    private TextView invoiceNoTextView;

    private TextView calcTextView;
    private TextView confirmTextView;

    private MyHandler mHandler;
    private AutoListView goodsListView;
    private BuyGoodsListAdapter goodsAdapter;
    private ArrayList<HashMap<String, String>> goodsArrayList;

    public static Vector<String> store_id;
    public static HashMap<String, String> datas;
    public static HashMap<String, String> pay_message;

    private void createControl() {

        mActivity = this;

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        mScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        addressTitleTextView = (TextView) findViewById(R.id.addressTitleTextView);
        addressTrueNameTextView = (TextView) findViewById(R.id.addressTrueNameTetView);
        addressPhoneTextView = (TextView) findViewById(R.id.addressPhoneTextView);
        addressContentTextView = (TextView) findViewById(R.id.addressContentTextView);

        payOnlineTextView = (TextView) findViewById(R.id.payOnlineTextView);
        payOfflineTextView = (TextView) findViewById(R.id.payOfflineTextView);
        invoiceNoTextView = (TextView) findViewById(R.id.invoiceNoTextView);
        invoiceYesTextView = (TextView) findViewById(R.id.invoiceYesTextView);

        calcTextView = (TextView) findViewById(R.id.calcTextView);
        confirmTextView = (TextView) findViewById(R.id.confirmTextView);

        goodsListView = (AutoListView) findViewById(R.id.goodsListView);

        //初始化参数
        titleTextView.setText("确认订单信息");
        rightImageView.setVisibility(View.GONE);

        goodsArrayList = new ArrayList<>();
        goodsAdapter = new BuyGoodsListAdapter(mActivity, goodsArrayList);
        goodsListView.setAdapter(goodsAdapter);

        pay_message = new HashMap<>();
        mHandler = new MyHandler(this);

        pay_name = "online";
        payOnlineTextView.setTextColor(Color.WHITE);
        payOnlineTextView.setBackgroundResource(R.drawable.drawable_button_order_press);

        fcode = "";
        rcb_pay = "0";
        voucher = "";
        pd_pay = "0";
        password = "";
        invoice_id = "0";
        invoiceNoTextView.setTextColor(Color.WHITE);
        invoiceNoTextView.setBackgroundResource(R.drawable.drawable_button_order_press);

        //一些子程序
        parseJson();

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        payOnlineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay_name = "online";
                payOnlineTextView.setTextColor(Color.WHITE);
                payOfflineTextView.setBackgroundResource(R.drawable.drawable_button_order);
                payOnlineTextView.setBackgroundResource(R.drawable.drawable_button_order_press);
                payOfflineTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
            }
        });

        payOfflineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay_name = "offline";
                payOfflineTextView.setTextColor(Color.WHITE);
                payOnlineTextView.setBackgroundResource(R.drawable.drawable_button_order);
                payOfflineTextView.setBackgroundResource(R.drawable.drawable_button_order_press);
                payOnlineTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
            }
        });

        invoiceNoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoice_id = "0";
                invoiceNoTextView.setTextColor(Color.WHITE);
                invoiceYesTextView.setBackgroundResource(R.drawable.drawable_button_order);
                invoiceNoTextView.setBackgroundResource(R.drawable.drawable_button_order_press);
                invoiceYesTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
            }
        });

        invoiceYesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invoice_id = "0";
                invoiceYesTextView.setTextColor(Color.WHITE);
                invoiceNoTextView.setBackgroundResource(R.drawable.drawable_button_order);
                invoiceYesTextView.setBackgroundResource(R.drawable.drawable_button_order_press);
                invoiceNoTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
            }
        });

        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.progress(mActivity);
                String message = "";
                if (store_id.size() != 0) {

                } else {
                    message = pay_message.get(mActivity.getIntent().getStringExtra("store_id"));
                }
                AjaxParams ajaxParams = new AjaxParams();
                ajaxParams.put("key", Constant.userKeyString);
                ajaxParams.put("ifcart", ifcart);
                ajaxParams.put("cart_id", cart_id);
                ajaxParams.put("address_id", address_id);
                ajaxParams.put("vat_hash", vat_hash);
                ajaxParams.put("offpay_hash", offpay_hash);
                ajaxParams.put("offpay_hash_batch", offpay_hash_batch);
                ajaxParams.put("pay_name", pay_name);
                ajaxParams.put("invoice_id", invoice_id);
                ajaxParams.put("voucher", voucher);
                ajaxParams.put("pd_pay", pd_pay);
                ajaxParams.put("password", password);
                ajaxParams.put("fcode", fcode);
                ajaxParams.put("rcb_pay", rcb_pay);
                ajaxParams.put("rpt", "");
                ajaxParams.put("pay_message", message);
                Constant.mFinalHttp.post(Constant.LINK_MOBILE_BUY_SETUP2, ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        DialogUtil.cancel();
                        if (TextUtil.isNcJson(o.toString())) {
                            try {
                                JSONObject jsonObject = new JSONObject(o.toString());
                                String datas = jsonObject.getString("datas");
                                jsonObject = new JSONObject(datas);
                                String pay_sn = jsonObject.getString("pay_sn");
                                String pay_code = jsonObject.getString("payment_code");
                                if (pay_code.equals("online")) {
                                    Intent intent = new Intent(mActivity, BuyPayActivity.class);
                                    intent.putExtra("pay_sn", pay_sn);
                                    ActivityUtil.start(mActivity, intent);
                                }
                                ActivityUtil.finish(mActivity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.show(mActivity, "未知错误");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        ToastUtil.show(mActivity, "未知错误");
                        DialogUtil.cancel();
                    }
                });
            }
        });

    }

    private void parseJson() {

        ifcart = mActivity.getIntent().getStringExtra("ifcart");
        cart_id = mActivity.getIntent().getStringExtra("cart_id");
        String json = mActivity.getIntent().getStringExtra("json");
        String storeId = mActivity.getIntent().getStringExtra("store_id");

        if (TextUtil.isEmpty(json)) {
            ToastUtil.show(mActivity, "数据不合法");
            ActivityUtil.finish(mActivity);
        }

        store_id = new Vector<>();
        if (storeId.contains("|")) {
            while (storeId.contains("|")) {
                String temp = storeId.substring(0, storeId.indexOf("|"));
                storeId = storeId.substring(storeId.indexOf("|") + 1, storeId.length());
                if (!store_id.toString().contains(temp)) {
                    store_id.add(temp.replace(" ", ""));
                }
            }
            if (!store_id.toString().contains(storeId)) {
                store_id.add(storeId.replace(" ", ""));
            }
        }

        if (store_id.size() == 1) {
            storeId = store_id.get(0);
            store_id.clear();
        }

        try {

            datas = new HashMap<>(TextUtil.jsonToHashMap(json));

            //解析store_cart_list
            goodsArrayList.clear();
            String store_cart_list = datas.get("store_cart_list");
            if (store_id.size() == 0) {
                JSONObject jsonObject = new JSONObject(store_cart_list);
                String store_goods_list = jsonObject.getString(storeId);
                jsonObject = new JSONObject(store_goods_list);
                JSONArray jsonArray = new JSONArray(jsonObject.getString("goods_list"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    jsonObject = (JSONObject) jsonArray.get(i);
                    hashMap.put("goods_num", jsonObject.getString("goods_num"));
                    hashMap.put("goods_id", jsonObject.getString("goods_id"));
                    hashMap.put("goods_name", jsonObject.getString("goods_name"));
                    hashMap.put("goods_price", jsonObject.getString("goods_price"));
                    hashMap.put("goods_total", jsonObject.getString("goods_total"));
                    hashMap.put("goods_image_url", jsonObject.getString("goods_image_url"));
                    hashMap.put("store_id", jsonObject.getString("store_id"));
                    hashMap.put("store_name", jsonObject.getString("store_name"));
                    hashMap.put("cart_id", jsonObject.getString("cart_id"));
                    goodsArrayList.add(hashMap);
                }
            }
            goodsAdapter.notifyDataSetChanged();

            //解析address_info
            String address_info = datas.get("address_info");
            JSONObject addressObject = new JSONObject(address_info);
            address_id = addressObject.getString("address_id");
            addressTrueNameTextView.setText(addressObject.getString("true_name"));
            addressContentTextView.setText(addressObject.getString("area_info"));
            addressContentTextView.append(" ");
            addressContentTextView.append(addressObject.getString("address"));
            try {
                addressPhoneTextView.setText(addressObject.getString("mob_phone"));
                addressPhoneTextView.append(" ");
                addressPhoneTextView.append(addressObject.getString("tel_phone"));
            } catch (JSONException e) {
                addressPhoneTextView.setText(addressPhoneTextView.getText().toString().replace(" ", ""));
            }

            //解析store_final_total_list
            String store_final_total_list = datas.get("store_final_total_list");
            if (store_id.size() == 0) {
                JSONObject jsonObject = new JSONObject(store_final_total_list);
                String count = jsonObject.getString(storeId);
                count = "共 <font color='#FF5001'>" + count + "</font> 元";
                calcTextView.setText(Html.fromHtml(count));
            }

            if (!datas.get("ifshow_offpay").equals("true")) {
                payOfflineTextView.setVisibility(View.GONE);
            }

            //解析address_api
            String address_api = datas.get("address_api");
            JSONObject addressApiObject = new JSONObject(address_api);
            vat_hash = datas.get("vat_hash");
            offpay_hash = addressApiObject.getString("offpay_hash");
            offpay_hash_batch = addressApiObject.getString("offpay_hash_batch");

            mHandler.sendEmptyMessage(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void returnActivity() {

        DialogUtil.query(mActivity, "确认您的选择", "返回将会取消订单!", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.finish(mActivity);
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            returnActivity();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_setup);
        createControl();
        createEvent();
    }

    static class MyHandler extends Handler {

        private WeakReference<BuySetupActivity> mWeakActivity;

        public MyHandler(BuySetupActivity activity) {
            mWeakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BuySetupActivity activity = mWeakActivity.get();
            if (activity != null) {
                DialogUtil.cancel();
                activity.mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }

    }

}
