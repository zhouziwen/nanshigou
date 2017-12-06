package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.yokey.adapter.GoodsDetailListAdapter;
import cn.yokey.adapter.ViewPagerAdapter;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.QRCodeUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class GoodsDetailActivity extends AppCompatActivity {

    public static Activity mActivity;

    private MyHandler mHandler;

    private boolean isCollection;

    private String id, specList1, specList2;
    private HashMap<String, String> datas;
    //goods_info
    private HashMap<String, String> goods_info;
    private ArrayList<HashMap<String, String>> spec_name;
    private ArrayList<HashMap<String, String>> spec_value;
    private ArrayList<HashMap<String, String>> spec_value1;
    private ArrayList<HashMap<String, String>> spec_value2;
    private ArrayList<HashMap<String, String>> goods_spec;
    //spec_image
    private ArrayList<HashMap<String, String>> spec_image;
    //commend_list
    private ArrayList<HashMap<String, String>> commend_list;
    //store_info
    private HashMap<String, String> store_info;
    private HashMap<String, String> store_desc;
    private HashMap<String, String> store_server;
    private HashMap<String, String> store_delivery;
    //spec_list
    private ArrayList<HashMap<String, String>> spec_list;
    //goods_image
    private ArrayList<HashMap<String, String>> goods_image;
    //eval_list
    private ArrayList<HashMap<String, String>> eval_list;
    //eval_info
    private HashMap<String, String> eval_info;
    //hair_info
    private HashMap<String, String> hair_info;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;
    private ImageView right1ImageView;
    private ScrollView mScrollView;
    private ViewPager goodsViewPager;
    private TextView nameTextView;
    private TextView jingleTextView;
    private TextView pricePromotionTextView;
    private TextView priceTextView;
    private TextView saleNumTextView;
    private LinearLayout activityLinearLayout;
    private TextView activityNameTextView;
    private TextView activityRemarkTextView;
    private TextView addressTextView;
    private TextView addressTipTextView;
    private LinearLayout chooseLinearLayout;
    private TextView choose1TextView;
    private TextView choose2TextView;
    private TextView commentTextView;
    private TextView commentNumTextView;
    private TextView bodyTextView;
    private TextView storeTextView;
    private TextView storeDescTextView;
    private TextView storeDescStatusTextView;
    private TextView storeServerTextView;
    private TextView storeServerStatusTextView;
    private TextView storeDeliveryTextView;
    private TextView storeDeliveryStatusTextView;
    private ListView mListView;

    private TextView backgroundTextView;
    private RelativeLayout qrCodeRelativeLayout;
    private ImageView qrCodeCloseImageView;
    private ImageView qrCodeContentImageView;
    private TextView qrCodeOtherTextView;

    private LinearLayout bottomLinearLayout;
    private TextView keFuTextView;
    private TextView cartTextView;
    private TextView joinCartTextView;
    private TextView buyTextView;

    private RelativeLayout chooseRelativeLayout;
    private ImageView chooseImageView;
    private TextView chooseNameTextView;
    private TextView choosePriceTextView;
    private TextView chooseStorageTextView;
    private TextView chooseSpec1TextView;
    private HorizontalScrollView chooseSpec1ScrollView;
    private HorizontalScrollView chooseSpec2ScrollView;
    private TextView[] chooseSpec1AttrTextView;
    private TextView chooseSpec2TextView;
    private TextView[] chooseSpec2AttrTextView;
    private View chooseLine2View;
    private View chooseLine3View;
    private Button chooseAddButton;
    private EditText chooseNumberEditText;
    private Button chooseSubButton;
    private Button chooseCartButton;
    private Button chooseBuyButton;

    private void createControl() {

        mActivity = this;

        isCollection = false;

        id = mActivity.getIntent().getStringExtra("id");

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        right1ImageView = (ImageView) findViewById(R.id.right1ImageView);
        mScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        goodsViewPager = (ViewPager) findViewById(R.id.goodsViewPager);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        jingleTextView = (TextView) findViewById(R.id.jingleTextView);
        pricePromotionTextView = (TextView) findViewById(R.id.pricePromotionTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        saleNumTextView = (TextView) findViewById(R.id.saleNumTextView);
        activityLinearLayout = (LinearLayout) findViewById(R.id.activityLinearLayout);
        activityNameTextView = (TextView) findViewById(R.id.activityNameTextView);
        activityRemarkTextView = (TextView) findViewById(R.id.activityRemarkTextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);
        addressTipTextView = (TextView) findViewById(R.id.addressTipTextView);
        chooseLinearLayout = (LinearLayout) findViewById(R.id.chooseLinearLayout);
        choose1TextView = (TextView) findViewById(R.id.choose1TextView);
        choose2TextView = (TextView) findViewById(R.id.choose2TextView);
        commentTextView = (TextView) findViewById(R.id.commentTextView);
        commentNumTextView = (TextView) findViewById(R.id.commentNumTextView);
        bodyTextView = (TextView) findViewById(R.id.bodyTextView);
        storeTextView = (TextView) findViewById(R.id.storeTextView);
        storeDescTextView = (TextView) findViewById(R.id.storeDescTextView);
        storeDescStatusTextView = (TextView) findViewById(R.id.storeDescStatusTextView);
        storeServerTextView = (TextView) findViewById(R.id.storeServerTextView);
        storeServerStatusTextView = (TextView) findViewById(R.id.storeServerStatusTextView);
        storeDeliveryTextView = (TextView) findViewById(R.id.storeDeliveryTextView);
        storeDeliveryStatusTextView = (TextView) findViewById(R.id.storeDeliveryStatusTextView);
        mListView = (ListView) findViewById(R.id.mainListView);

        backgroundTextView = (TextView) findViewById(R.id.backgroundTextView);
        qrCodeRelativeLayout = (RelativeLayout) findViewById(R.id.qrCodeRelativeLayout);
        qrCodeCloseImageView = (ImageView) findViewById(R.id.qrCodeCloseImageView);
        qrCodeContentImageView = (ImageView) findViewById(R.id.qrCodeContentImageView);
        qrCodeOtherTextView = (TextView) findViewById(R.id.qrCodeOtherTextView);

        bottomLinearLayout = (LinearLayout) findViewById(R.id.bottomLinearLayout);
        keFuTextView = (TextView) findViewById(R.id.kefuTextView);
        cartTextView = (TextView) findViewById(R.id.cartTextView);
        joinCartTextView = (TextView) findViewById(R.id.joinCartTextView);
        buyTextView = (TextView) findViewById(R.id.buyTextView);

        chooseRelativeLayout = (RelativeLayout) findViewById(R.id.chooseRelativeLayout);
        chooseImageView = (ImageView) findViewById(R.id.chooseImageView);
        chooseNameTextView = (TextView) findViewById(R.id.chooseNameTextView);
        choosePriceTextView = (TextView) findViewById(R.id.choosePriceTextView);
        chooseStorageTextView = (TextView) findViewById(R.id.chooseStorageTextView);
        chooseSpec1ScrollView = (HorizontalScrollView) findViewById(R.id.chooseSpec1ScrollView);
        chooseSpec2ScrollView = (HorizontalScrollView) findViewById(R.id.chooseSpec2ScrollView);
        chooseSpec1TextView = (TextView) findViewById(R.id.chooseSpec1TextView);
        chooseSpec2TextView = (TextView) findViewById(R.id.chooseSpec2TextView);
        chooseLine2View = findViewById(R.id.chooseLine2View);
        chooseLine3View = findViewById(R.id.chooseLine3View);
        chooseAddButton = (Button) findViewById(R.id.chooseAddButton);
        chooseNumberEditText = (EditText) findViewById(R.id.chooseNumberEditText);
        chooseSubButton = (Button) findViewById(R.id.chooseSubButton);
        chooseCartButton = (Button) findViewById(R.id.chooseCartButton);
        chooseBuyButton = (Button) findViewById(R.id.chooseBuyButton);
        chooseSpec1AttrTextView = new TextView[10];
        chooseSpec2AttrTextView = new TextView[10];
        for (int i = 0; i < 10; i++) {
            chooseSpec1AttrTextView[i] = (TextView) findViewById(R.id.chooseSpec1Attr1TextView + i);
            chooseSpec2AttrTextView[i] = (TextView) findViewById(R.id.chooseSpec2Attr1TextView + i);
            chooseSpec1AttrTextView[i].setVisibility(View.GONE);
            chooseSpec2AttrTextView[i].setVisibility(View.GONE);
        }

        //初始化参数
        titleTextView.setText("商品详细");

        if (TextUtil.isEmpty(id)) {
            ActivityUtil.finish(mActivity);
            ToastUtil.show(mActivity, "传入的参数异常");
        }

        mHandler = new MyHandler(GoodsDetailActivity.this);

        //一些子程序
        getJson();
        if (Constant.userLoginBoolean) {
            checkCollect();
        }

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
                if (Constant.userLoginBoolean) {
                    if (!isCollection) {
                        AjaxParams ajaxParams = new AjaxParams();
                        ajaxParams.put("key", Constant.userKeyString);
                        ajaxParams.put("goods_id", id);
                        Constant.mFinalHttp.post(Constant.LINK_MOBILE_COLLECTION_GOODS_ADD, ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                try {
                                    JSONObject jsonObject = new JSONObject(o.toString());
                                    String datas = jsonObject.getString("datas");
                                    if (datas.equals("1")) {
                                        isCollection = true;
                                        rightImageView.setImageResource(R.mipmap.ic_action_collection_press);
                                    } else {
                                        isCollection = false;
                                        rightImageView.setImageResource(R.mipmap.ic_action_collection);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                ToastUtil.show(mActivity, "失败了,请重试");
                            }
                        });
                    } else {
                        ToastUtil.show(mActivity, "请在收藏中心取消收藏");
                    }
                } else {
                    ActivityUtil.start(mActivity, new Intent(mActivity, LoginActivity.class));
                }
            }
        });

        right1ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qrCodeRelativeLayout.getVisibility() == View.VISIBLE) {
                    backgroundTextView.setVisibility(View.GONE);
                    chooseRelativeLayout.setVisibility(View.GONE);
                    qrCodeRelativeLayout.setVisibility(View.GONE);
                } else {
                    showQRCode();
                }
            }
        });

        backgroundTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundTextView.setVisibility(View.GONE);
                chooseRelativeLayout.setVisibility(View.GONE);
                qrCodeRelativeLayout.setVisibility(View.GONE);
                bottomLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        qrCodeCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundTextView.setVisibility(View.GONE);
                chooseRelativeLayout.setVisibility(View.GONE);
                qrCodeRelativeLayout.setVisibility(View.GONE);
            }
        });

        qrCodeOtherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundTextView.setVisibility(View.GONE);
                chooseRelativeLayout.setVisibility(View.GONE);
                qrCodeRelativeLayout.setVisibility(View.GONE);
                ActivityUtil.startShare(mActivity, goods_info.get("goods_url"));
            }
        });

        chooseLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.userLoginBoolean) {
                    showChoose();
                } else {
                    ToastUtil.show(mActivity, "请先登录");
                }
            }
        });

        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, GoodsIntroduceActivity.class);
                intent.putExtra("Link", Constant.LINK_MOBILE_GOODS_BODY + "&goods_id=" + id);
                ActivityUtil.start(mActivity, intent);
            }
        });

        keFuTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        joinCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.userLoginBoolean) {
                    showChoose();
                } else {
                    ToastUtil.show(mActivity, "请先登录");
                }
            }
        });

        buyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.userLoginBoolean) {
                    showChoose();
                } else {
                    ToastUtil.show(mActivity, "请先登录");
                }
            }
        });

        chooseRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        chooseAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = chooseNumberEditText.getText().toString();
                if (temp.isEmpty()) {
                    chooseNumberEditText.setText("1");
                } else {
                    int number = Integer.parseInt(temp);
                    temp = number + 1 + "";
                    chooseNumberEditText.setText(temp);
                }
                chooseNumberEditText.setSelection(chooseNumberEditText.getText().toString().length());
            }
        });

        chooseSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = chooseNumberEditText.getText().toString();
                if (temp.isEmpty()) {
                    chooseNumberEditText.setText("1");
                } else {
                    if (!temp.equals("0")) {
                        int number = Integer.parseInt(temp);
                        temp = number - 1 + "";
                        chooseNumberEditText.setText(temp);
                    }
                }
                chooseNumberEditText.setSelection(chooseNumberEditText.getText().toString().length());
            }
        });

        chooseCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = chooseNumberEditText.getText().toString();
                if (quantity.isEmpty() || quantity.equals("0")) {
                    ToastUtil.show(mActivity, "请确认数量是否正确");
                }
                AjaxParams ajaxParams = new AjaxParams();
                ajaxParams.put("key", Constant.userKeyString);
                ajaxParams.put("goods_id", id);
                ajaxParams.put("quantity", quantity);
                Constant.mFinalHttp.post(Constant.LINK_MOBILE_CART_ADD, ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        if (TextUtil.isNcJson(o.toString())) {
                            try {
                                JSONObject jsonObject = new JSONObject(o.toString());
                                String datas = jsonObject.getString("datas");
                                if (datas.equals("1")) {
                                    backgroundTextView.setVisibility(View.GONE);
                                    qrCodeRelativeLayout.setVisibility(View.GONE);
                                    chooseRelativeLayout.setVisibility(View.GONE);
                                    bottomLinearLayout.setVisibility(View.VISIBLE);
                                    ToastUtil.show(mActivity, "添加到购物车成功");
                                    if (CartActivity.mActivity != null) {
                                        CartActivity.getJson();
                                    }
                                } else {
                                    ToastUtil.show(mActivity, datas);
                                }
                            } catch (JSONException e) {
                                ToastUtil.show(mActivity, "未知错误");
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
                    }
                });
            }
        });

        chooseBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.progress(mActivity);
                backgroundTextView.setVisibility(View.GONE);
                chooseRelativeLayout.setVisibility(View.GONE);
                qrCodeRelativeLayout.setVisibility(View.GONE);
                bottomLinearLayout.setVisibility(View.VISIBLE);
                String number = chooseNumberEditText.getText().toString();
                final String cart_id = id + "|" + number;
                AjaxParams ajaxParams = new AjaxParams();
                ajaxParams.put("key", Constant.userKeyString);
                ajaxParams.put("cart_id", cart_id);
                Constant.mFinalHttp.post(Constant.LINK_MOBILE_BUY_SETUP1, ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        DialogUtil.cancel();
                        if (TextUtil.isNcJson(o.toString())) {
                            try {
                                JSONObject jsonObject = new JSONObject(o.toString());
                                Intent intent = new Intent(mActivity, BuySetupActivity.class);
                                intent.putExtra("store_id", store_info.get("store_id"));
                                intent.putExtra("json", jsonObject.getString("datas"));
                                intent.putExtra("cart_id", cart_id);
                                intent.putExtra("ifcart", "");
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

    private void getJson() {

        DialogUtil.progress(mActivity, "请稍后...");

        Constant.mFinalHttp.get(Constant.LINK_MOBILE_GOODS_DETAIL + "&goods_id=" + id, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        datas = new HashMap<>(TextUtil.jsonToHashMap(jsonObject.getString("datas")));
                        parseGoodsInfo(datas.get("goods_info"));
                        parseSpecImage(datas.get("spec_image"));
                        parseCommendList(datas.get("goods_commend_list"));
                        parseStoreInfo(datas.get("store_info"));
                        parseSpecList(datas.get("spec_list"));
                        paresGoodsImage(datas.get("goods_image"));
                        parseEvalList(datas.get("goods_eval_list"));
                        parseEvalInfo(datas.get("goods_evaluate_info"));
                        parseHairInfo(datas.get("goods_hair_info"));
                        mHandler.sendEmptyMessage(0);
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

    private void failure() {

        DialogUtil.query(mActivity, "是否重试?", "读取数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getJson();
            }
        });

    }

    private void showQRCode() {

        backgroundTextView.setVisibility(View.GONE);
        chooseRelativeLayout.setVisibility(View.GONE);
        qrCodeRelativeLayout.setVisibility(View.GONE);
        bottomLinearLayout.setVisibility(View.VISIBLE);

        backgroundTextView.setVisibility(View.VISIBLE);
        qrCodeRelativeLayout.setVisibility(View.VISIBLE);
        backgroundTextView.startAnimation(Constant.showAlphaAnimation);
        qrCodeRelativeLayout.startAnimation(Constant.showAlphaAnimation);
        qrCodeContentImageView.setImageBitmap(QRCodeUtil.create(goods_info.get("goods_url"), 512, 512));

    }

    private void showChoose() {

        backgroundTextView.setVisibility(View.GONE);
        chooseRelativeLayout.setVisibility(View.GONE);
        qrCodeRelativeLayout.setVisibility(View.GONE);
        bottomLinearLayout.setVisibility(View.GONE);

        backgroundTextView.setVisibility(View.VISIBLE);
        chooseRelativeLayout.setVisibility(View.VISIBLE);
        backgroundTextView.startAnimation(Constant.showAlphaAnimation);
        chooseRelativeLayout.startAnimation(Constant.showAlphaAnimation);

    }

    private void checkCollect() {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        ajaxParams.put("goods_id", id);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_COLLECTION_GOODS_CHECK, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        if (datas.equals("1")) {
                            isCollection = true;
                            rightImageView.setImageResource(R.mipmap.ic_action_collection_press);
                        } else {
                            isCollection = false;
                            rightImageView.setImageResource(R.mipmap.ic_action_collection);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    failure();
                }
            }
        });

    }

    private void parseGoodsInfo(String json) {

        try {

            goods_info = new HashMap<>(TextUtil.jsonToHashMap(json));

            //解析spec_name
            spec_name = new ArrayList<>();
            if (goods_info.get("spec_name") != null) {
                JSONObject jsonObject = new JSONObject(goods_info.get("spec_name"));
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    String key = (String) iterator.next();
                    String value = jsonObject.get(key).toString();
                    hashMap.put("spec_name_id", key);
                    hashMap.put("spec_name_value", value);
                    spec_name.add(hashMap);
                }
            }

            //解析spec_value
            spec_value = new ArrayList<>();
            if (goods_info.get("spec_value") != null) {
                JSONObject jsonObject = new JSONObject(goods_info.get("spec_value"));
                for (int i = 0; i < spec_name.size(); i++) {
                    String spec_name_id = spec_name.get(i).get("spec_name_id");
                    if (jsonObject.getString(spec_name_id) != null) {
                        JSONObject tempObject = new JSONObject(jsonObject.getString(spec_name_id));
                        Iterator iterator = tempObject.keys();
                        while (iterator.hasNext()) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            String key = (String) iterator.next();
                            String value = tempObject.get(key).toString();
                            hashMap.put("spec_name_id", spec_name_id);
                            hashMap.put("spec_value_id", key);
                            hashMap.put("spec_value_value", value);
                            spec_value.add(hashMap);
                        }
                    }
                }
            }

            //解析goods_spec
            goods_spec = new ArrayList<>();
            if (goods_info.get("goods_spec") != null) {
                JSONObject jsonObject = new JSONObject(goods_info.get("goods_spec"));
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    String key = (String) iterator.next();
                    String value = jsonObject.get(key).toString();
                    hashMap.put("goods_spec_id", key);
                    hashMap.put("goods_spec_value", value);
                    goods_spec.add(hashMap);
                }
            }

            nameTextView.setText(goods_info.get("goods_name"));
            if (!TextUtil.isEmpty(goods_info.get("goods_jingle"))) {
                jingleTextView.setText(goods_info.get("goods_jingle"));
            } else {
                jingleTextView.setVisibility(View.GONE);
            }

            String pricePromotion = "￥" + goods_info.get("goods_promotion_price");
            pricePromotionTextView.setText(pricePromotion);
            saleNumTextView.setText(Html.fromHtml("销量 : <font color='#FF0000'>" + goods_info.get("goods_salenum") + "</font> 件"));

            if (goods_info.get("goods_promotion_type").equals("0")) {

                priceTextView.setVisibility(View.GONE);
                activityLinearLayout.setVisibility(View.GONE);

            } else {

                String goodsPrice = "￥" + goods_info.get("goods_price");
                priceTextView.setText(goodsPrice);
                priceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                String remark = "";
                if (TextUtil.isEmpty(goods_info.get("title"))) {
                    remark = "   ";
                    activityNameTextView.setVisibility(View.GONE);
                } else {
                    activityNameTextView.setText(goods_info.get("title"));
                }

                remark += "直降 ￥" + goods_info.get("down_price");

                if (goods_info.get("upper_limit") != null) {
                    remark += " 每人限购 " + goods_info.get("upper_limit") + " 件！";
                }

                if (goods_info.get("lower_limit") != null) {
                    remark += " 最低 " + goods_info.get("lower_limit") + " 件起！";
                }

                activityRemarkTextView.setText(remark);

            }

            if (goods_spec.size() != 0) {
                String goods_spec_id_1 = goods_spec.get(0).get("goods_spec_id");
                for (int i = 0; i < spec_value.size(); i++) {
                    String spec_value_id = spec_value.get(i).get("spec_value_id");
                    if (goods_spec_id_1.equals(spec_value_id)) {
                        String spec_name_id = spec_value.get(i).get("spec_name_id");
                        for (int j = 0; j < spec_name.size(); j++) {
                            String temp = spec_name.get(j).get("spec_name_id");
                            if (temp.equals(spec_name_id)) {
                                String value = spec_name.get(j).get("spec_name_value") + " : ";
                                value += goods_spec.get(0).get("goods_spec_value");
                                choose1TextView.setText(value);
                            }
                        }
                    }
                }
                if (goods_spec.size() == 2) {
                    String goods_spec_id_2 = goods_spec.get(1).get("goods_spec_id");
                    for (int i = 0; i < spec_value.size(); i++) {
                        String spec_value_id = spec_value.get(i).get("spec_value_id");
                        if (goods_spec_id_2.equals(spec_value_id)) {
                            String spec_name_id = spec_value.get(i).get("spec_name_id");
                            for (int j = 0; j < spec_name.size(); j++) {
                                String temp = spec_name.get(j).get("spec_name_id");
                                if (temp.equals(spec_name_id)) {
                                    String value = spec_name.get(j).get("spec_name_value") + " : ";
                                    value += goods_spec.get(1).get("goods_spec_value");
                                    choose2TextView.setText(value);
                                }
                            }
                        }
                    }
                } else {
                    choose2TextView.setVisibility(View.GONE);
                }
            } else {
                chooseLinearLayout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseSpecImage(String json) {

        try {

            spec_image = new ArrayList<>();

            if (json.contains("[") && json.contains("]")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("spec_value_id", "-1");
                hashMap.put("spec_image_value", json.substring(2, json.length() - 2));
                spec_image.add(hashMap);
            } else {
                JSONObject jsonObject = new JSONObject(json);
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    String key = (String) iterator.next();
                    String value = jsonObject.get(key).toString();
                    hashMap.put("spec_value_id", key);
                    hashMap.put("spec_image_value", value);
                    spec_image.add(hashMap);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseCommendList(String json) {

        try {
            commend_list = new ArrayList<>();
            GoodsDetailListAdapter mAdapter = new GoodsDetailListAdapter(mActivity, commend_list);
            mListView.setAdapter(mAdapter);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("goods_id", jsonObject.getString("goods_id"));
                hashMap.put("goods_name", jsonObject.getString("goods_name"));
                hashMap.put("goods_promotion_price", "￥" + jsonObject.getString("goods_promotion_price"));
                hashMap.put("goods_image_url", jsonObject.getString("goods_image_url"));
                commend_list.add(hashMap);
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseStoreInfo(String json) {

        try {

            store_info = new HashMap<>(TextUtil.jsonToHashMap(json));
            JSONObject jsonObject = new JSONObject(store_info.get("store_credit"));
            store_desc = new HashMap<>(TextUtil.jsonToHashMap(jsonObject.getString("store_desccredit")));
            store_server = new HashMap<>(TextUtil.jsonToHashMap(jsonObject.getString("store_servicecredit")));
            store_delivery = new HashMap<>(TextUtil.jsonToHashMap(jsonObject.getString("store_deliverycredit")));

            storeTextView.setText(store_info.get("store_name"));
            String desc = store_desc.get("text").substring(0, 2) + "：" + store_desc.get("credit") + " 分";
            storeDescTextView.setText(desc);
            storeDescStatusTextView.setText(store_desc.get("percent_text"));
            String server = store_server.get("text").substring(0, 2) + "：" + store_server.get("credit") + " 分";
            storeServerTextView.setText(server);
            storeServerStatusTextView.setText(store_server.get("percent_text"));
            String delivery = store_delivery.get("text").substring(0, 2) + "：" + store_delivery.get("credit") + " 分";
            storeDeliveryTextView.setText(delivery);
            storeDeliveryStatusTextView.setText(store_delivery.get("percent_text"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseSpecList(String json) {

        try {

            spec_list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                HashMap<String, String> hashMap = new HashMap<>();
                String key = (String) iterator.next();
                String value = jsonObject.get(key).toString();
                if (TextUtil.isEmpty(key)) {
                    key = "-1";
                }
                hashMap.put("spec_list_id", key);
                hashMap.put("spec_list_value", value);
                spec_list.add(hashMap);
            }

            ImageLoader.getInstance().displayImage(spec_image.get(0).get("spec_image_value"), chooseImageView);
            chooseNameTextView.setText(goods_info.get("goods_name"));
            String pricePromotion = "￥" + goods_info.get("goods_promotion_price");
            choosePriceTextView.setText(pricePromotion);
            String storage = "库存：" + goods_info.get("goods_storage") + " 件";
            chooseStorageTextView.setText(storage);

            if (chooseLinearLayout.getVisibility() == View.GONE) {
                chooseSpec1TextView.setVisibility(View.GONE);
                chooseSpec1ScrollView.setVisibility(View.GONE);
                chooseLine2View.setVisibility(View.GONE);
                chooseLine3View.setVisibility(View.GONE);
                chooseSpec2TextView.setVisibility(View.GONE);
                chooseSpec2ScrollView.setVisibility(View.GONE);
                return;
            }

            specList1 = "";
            specList2 = "";

            for (int i = 0; i < chooseSpec1AttrTextView.length; i++) {
                chooseSpec1AttrTextView[i].setBackgroundResource(R.drawable.drawable_button_order);
                chooseSpec2AttrTextView[i].setBackgroundResource(R.drawable.drawable_button_order);
                chooseSpec1AttrTextView[i].setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
                chooseSpec2AttrTextView[i].setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
            }

            switch (spec_name.size()) {
                case 1:
                    String value = spec_name.get(0).get("spec_name_value") + "：";
                    chooseSpec1TextView.setText(value);
                    chooseLine2View.setVisibility(View.GONE);
                    chooseLine3View.setVisibility(View.GONE);
                    chooseSpec2TextView.setVisibility(View.GONE);
                    chooseSpec2ScrollView.setVisibility(View.GONE);
                    spec_value1 = new ArrayList<>();
                    for (int i = 0; i < spec_value.size(); i++) {
                        if (spec_value.get(i).get("spec_name_id").equals(spec_name.get(0).get("spec_name_id"))) {
                            spec_value1.add(spec_value.get(i));
                        }
                    }
                    for (int i = 0; i < spec_value1.size(); i++) {
                        chooseSpec1AttrTextView[i].setVisibility(View.VISIBLE);
                        chooseSpec1AttrTextView[i].setText(spec_value1.get(i).get("spec_value_value"));
                        for (int j = 0; j < goods_spec.size(); j++) {
                            if (spec_value1.get(i).get("spec_value_value").equals(goods_spec.get(j).get("goods_spec_value"))) {
                                chooseSpec1AttrTextView[i].setBackgroundResource(R.drawable.drawable_button_order_press);
                                chooseSpec1AttrTextView[i].setTextColor(Color.WHITE);
                                specList1 = spec_value1.get(i).get("spec_value_id");
                            }
                        }
                        final int position = i;
                        chooseSpec1AttrTextView[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                specList1 = spec_value1.get(position).get("spec_value_id");
                            }
                        });
                    }
                    break;
                case 2:
                    String value1 = spec_name.get(0).get("spec_name_value") + "：";
                    String value2 = spec_name.get(1).get("spec_name_value") + "：";
                    chooseSpec1TextView.setText(value1);
                    chooseSpec2TextView.setText(value2);
                    spec_value1 = new ArrayList<>();
                    for (int i = 0; i < spec_value.size(); i++) {
                        if (spec_value.get(i).get("spec_name_id").equals(spec_name.get(0).get("spec_name_id"))) {
                            spec_value1.add(spec_value.get(i));
                        }
                    }
                    spec_value2 = new ArrayList<>();
                    for (int i = 0; i < spec_value.size(); i++) {
                        if (spec_value.get(i).get("spec_name_id").equals(spec_name.get(1).get("spec_name_id"))) {
                            spec_value2.add(spec_value.get(i));
                        }
                    }
                    for (int i = 0; i < spec_value1.size(); i++) {
                        chooseSpec1AttrTextView[i].setVisibility(View.VISIBLE);
                        chooseSpec1AttrTextView[i].setText(spec_value1.get(i).get("spec_value_value"));
                        for (int j = 0; j < goods_spec.size(); j++) {
                            if (spec_value1.get(i).get("spec_value_value").equals(goods_spec.get(j).get("goods_spec_value"))) {
                                chooseSpec1AttrTextView[i].setBackgroundResource(R.drawable.drawable_button_order_press);
                                chooseSpec1AttrTextView[i].setTextColor(Color.WHITE);
                                specList1 = spec_value1.get(i).get("spec_value_id");
                            }
                        }
                        final int position = i;
                        chooseSpec1AttrTextView[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                specList1 = spec_value1.get(position).get("spec_value_id");
                                for (int i = 0; i < spec_list.size(); i++) {
                                    String spec_id = spec_list.get(i).get("spec_list_id");
                                    String spec_value = spec_list.get(i).get("spec_list_value");
                                    if (spec_id.contains(specList1) && spec_id.contains(specList2)) {
                                        id = spec_value;
                                        getJson();
                                    }
                                }
                            }
                        });
                    }
                    for (int i = 0; i < spec_value2.size(); i++) {
                        chooseSpec2AttrTextView[i].setVisibility(View.VISIBLE);
                        chooseSpec2AttrTextView[i].setText(spec_value2.get(i).get("spec_value_value"));
                        for (int j = 0; j < goods_spec.size(); j++) {
                            if (spec_value2.get(i).get("spec_value_value").equals(goods_spec.get(j).get("goods_spec_value"))) {
                                chooseSpec2AttrTextView[i].setBackgroundResource(R.drawable.drawable_button_order_press);
                                chooseSpec2AttrTextView[i].setTextColor(Color.WHITE);
                                specList2 = spec_value2.get(i).get("spec_value_id");
                            }
                        }
                        final int position = i;
                        chooseSpec2AttrTextView[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                specList2 = spec_value2.get(position).get("spec_value_id");
                                for (int i = 0; i < spec_list.size(); i++) {
                                    String spec_id = spec_list.get(i).get("spec_list_id");
                                    String spec_value = spec_list.get(i).get("spec_list_value");
                                    if (spec_id.contains(specList1) && spec_id.contains(specList2)) {
                                        id = spec_value;
                                        getJson();
                                    }
                                }
                            }
                        });
                    }
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void paresGoodsImage(final String json) {

        goods_image = new ArrayList<>(TextUtil.encodeImageWithArrayList(json));

        List<View> list = new ArrayList<>();
        ImageView[] imageViews = new ImageView[goods_image.size()];
        for (int i = 0; i < goods_image.size(); i++) {
            final int position = i;
            list.add(mActivity.getLayoutInflater().inflate(R.layout.include_image_view, null));
            imageViews[i] = (ImageView) list.get(i).findViewById(R.id.includeImageView);
            ImageLoader.getInstance().displayImage(goods_image.get(i).get("image"), imageViews[i]);
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, PhotoActivity.class);
                    intent.putExtra("title", "商品图片");
                    intent.putExtra("position", position);
                    intent.putExtra("image", json);
                    ActivityUtil.start(mActivity, intent);
                }
            });
        }

        goodsViewPager.setAdapter(new ViewPagerAdapter(list));

    }

    private void parseEvalList(String json) {

    }

    private void parseEvalInfo(String json) {

        eval_info = new HashMap<>(TextUtil.jsonToHashMap(json));

        String comment = "好评率 : " + eval_info.get("good_percent") + "%";
        String all = eval_info.get("all") + " 人评价";

        commentTextView.setText(comment);
        commentNumTextView.setText(all);

    }

    private void parseHairInfo(String json) {

        hair_info = new HashMap<>(TextUtil.jsonToHashMap(json));

        addressTextView.setText(hair_info.get("area_name"));
        addressTipTextView.setText(hair_info.get("if_store_cn"));
        addressTipTextView.append(" ");
        addressTipTextView.append(hair_info.get("content"));

    }

    private void returnActivity() {

        if (qrCodeRelativeLayout.getVisibility() == View.VISIBLE || chooseRelativeLayout.getVisibility() == View.VISIBLE) {
            chooseRelativeLayout.setVisibility(View.GONE);
            qrCodeRelativeLayout.setVisibility(View.GONE);
            backgroundTextView.setVisibility(View.GONE);
        } else {
            ActivityUtil.finish(mActivity);
        }

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
        setContentView(R.layout.activity_goods_detail);
        createControl();
        createEvent();
    }

    static class MyHandler extends Handler {

        private WeakReference<GoodsDetailActivity> mWeakActivity;

        public MyHandler(GoodsDetailActivity activity) {
            mWeakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GoodsDetailActivity activity = mWeakActivity.get();
            if (activity != null) {
                DialogUtil.cancel();
                activity.mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        }

    }

}