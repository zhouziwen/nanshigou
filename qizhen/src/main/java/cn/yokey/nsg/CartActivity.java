package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.adapter.CartListAdapter;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class CartActivity extends AppCompatActivity {

    public static Activity mActivity;

    public static Boolean getBoolean;

    public static TextView titleTextView;

    public static ListView mListView;
    public static TextView buyTextView;
    public static TextView tipsTextView;
    public static TextView calcTextView;
    public static TextView statusTextView;
    public static CartListAdapter mAdapter;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    public static ArrayList<HashMap<String, String>> mArrayList;
    public static HashMap<String, String> datas;

    private void createControl() {

        mActivity = this;

        getBoolean = true;

        //控件实例化
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        calcTextView = (TextView) findViewById(R.id.calcTextView);
        buyTextView = (TextView) findViewById(R.id.buyTextView);
        tipsTextView = (TextView) findViewById(R.id.tipsTextView);
        mListView = (ListView) findViewById(R.id.mainListView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

        //初始化参数
        mArrayList = new ArrayList<>();
        mAdapter = new CartListAdapter(mActivity, mArrayList);
        mListView.setAdapter(mAdapter);

        calcTextView.setVisibility(View.GONE);
        buyTextView.setVisibility(View.GONE);

        if (Constant.userLoginBoolean) {
            tipsTextView.setVisibility(View.GONE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (getBoolean) {
                        try {
                            if (Constant.userLoginSuccessBoolean) {
                                getBoolean = false;
                                getJson();
                            }
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } else {
            statusTextView.setVisibility(View.GONE);
        }

    }

    private void createEvent() {

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constant.userLoginBoolean) {
                    ActivityUtil.startWithLogin(mActivity, new Intent(mActivity, LoginActivity.class));
                } else {
                    if (tipsTextView.getText().toString().contains("为空")) {
                        MainActivity.setTab(0);
                    } else {
                        tipsTextView.setVisibility(View.GONE);
                    }
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJson();
                    }
                }, 1000);
            }
        });

        buyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cart_id = "";
                String store_id = "";
                for (int i = 0; i < mArrayList.size(); i++) {
                    String temp = mArrayList.get(i).get("cart_id") + "|" + mArrayList.get(i).get("goods_num") + ",";
                    cart_id += temp;
                    temp = mArrayList.get(i).get("store_id") + "|";
                    store_id += temp;
                }

                final String storeId = store_id.substring(0, store_id.lastIndexOf("|"));
                final String cartId = cart_id.substring(0, cart_id.lastIndexOf(","));

                DialogUtil.progress(mActivity);
                AjaxParams ajaxParams = new AjaxParams();
                ajaxParams.put("key", Constant.userKeyString);
                ajaxParams.put("cart_id", cartId);
                ajaxParams.put("ifcart", "1");
                Constant.mFinalHttp.post(Constant.LINK_MOBILE_BUY_SETUP1, ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        DialogUtil.cancel();
                        if (TextUtil.isNcJson(o.toString())) {
                            try {
                                JSONObject jsonObject = new JSONObject(o.toString());
                                Intent intent = new Intent(mActivity, BuySetupActivity.class);
                                intent.putExtra("store_id", storeId);
                                intent.putExtra("json", jsonObject.getString("datas"));
                                intent.putExtra("cart_id", cartId);
                                intent.putExtra("ifcart", "1");
                                ActivityUtil.start(mActivity, intent);
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
                        ToastUtil.show(mActivity, "未知错误!");
                        DialogUtil.cancel();
                    }
                });

            }
        });

    }

    public static void getJson() {

        statusTextView.setText("加载中...");
        calcTextView.setVisibility(View.GONE);
        buyTextView.setVisibility(View.GONE);
        statusTextView.setVisibility(View.VISIBLE);

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_CART, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        datas = new HashMap<>(TextUtil.jsonToHashMap(jsonObject.getString("datas")));
                        paresCartList(datas.get("cart_list"));
                        statusTextView.setVisibility(View.GONE);
                        String total = "共 <font color='#FF5001'>" + datas.get("cart_count") + "</font> 件商品，";
                        total += "共 <font color='#FF5001'>" + datas.get("sum") + "</font> 元";
                        calcTextView.setText(Html.fromHtml(total));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    failure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                failure();
            }
        });

    }

    public static void failure() {

        statusTextView.setText("加载失败...");

        DialogUtil.query(mActivity, "是否重试?", "读取购物车数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getJson();
            }
        });

    }

    public static void paresCartList(String json) {

        if (json.equals("[]")) {
            calcTextView.setVisibility(View.GONE);
            buyTextView.setVisibility(View.GONE);
            tipsTextView.setVisibility(View.VISIBLE);
            tipsTextView.setText("购物车为空\n去逛逛吧");
        } else {
            try {
                mArrayList.clear();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String goods = jsonObject.getString("goods");
                    JSONArray goodsArray = new JSONArray(goods);
                    for (int j = 0; j < goodsArray.length(); j++) {
                        jsonObject = (JSONObject) goodsArray.get(j);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("cart_id", jsonObject.getString("cart_id"));
                        hashMap.put("goods_id", jsonObject.getString("goods_id"));
                        hashMap.put("goods_name", jsonObject.getString("goods_name"));
                        hashMap.put("store_id", jsonObject.getString("store_id"));
                        hashMap.put("store_name", jsonObject.getString("store_name"));
                        hashMap.put("goods_promotion_price", jsonObject.getString("goods_promotion_price"));
                        hashMap.put("goods_num", jsonObject.getString("goods_num"));
                        hashMap.put("goods_image_url", jsonObject.getString("goods_image_url"));
                        mArrayList.add(hashMap);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                failure();
            }
            if (mArrayList.size() == 0) {
                calcTextView.setVisibility(View.GONE);
                buyTextView.setVisibility(View.GONE);
                tipsTextView.setVisibility(View.VISIBLE);
                tipsTextView.setText("购物车为空\n去逛逛吧");
            } else {
                calcTextView.setVisibility(View.VISIBLE);
                buyTextView.setVisibility(View.VISIBLE);
                tipsTextView.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }
        }

        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        createControl();
        createEvent();
    }

}