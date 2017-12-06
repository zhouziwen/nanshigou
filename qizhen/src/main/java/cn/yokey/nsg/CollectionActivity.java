package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.yokey.adapter.BasePagerAdapter;
import cn.yokey.adapter.GoodsBrowsListAdapter;
import cn.yokey.adapter.GoodsCollectionListAdapter;
import cn.yokey.adapter.StoreCollectionListAdapter;
import cn.yokey.system.Android;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;

public class CollectionActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    public static ListView goodsListView;
    public static TextView goodsTextView;
    public static GoodsCollectionListAdapter goodsAdapter;
    public static SwipeRefreshLayout goodsSwipeRefreshLayout;
    public static ArrayList<HashMap<String, String>> goodsArrayList;

    private ListView storeListView;
    private TextView storeTextView;
    private StoreCollectionListAdapter storeAdapter;
    private SwipeRefreshLayout storeSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> storeArrayList;

    private ListView browsListView;
    private TextView browsTextView;
    private GoodsBrowsListAdapter browsAdapter;
    private SwipeRefreshLayout browsSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> browsArrayList;

    private void createControl() {

        mActivity = this;

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.mainViewPager);

        //初始化参数
        titleTextView.setText("我的收藏");
        rightImageView.setImageResource(R.mipmap.ic_action_del);

        List<View> mViewList = new ArrayList<>();
        mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
        mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
        mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));

        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("商品收藏");
        mTitleList.add("店铺收藏");
        mTitleList.add("我的足迹");

        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
        }

        Android.setTabLayout(mActivity, mTabLayout, new BasePagerAdapter(mViewList, mTitleList), mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        goodsListView = (ListView) mViewList.get(0).findViewById(R.id.mainListView);
        goodsTextView = (TextView) mViewList.get(0).findViewById(R.id.statusTextView);
        goodsSwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(0).findViewById(R.id.mainSwipeRefreshLayout);

        storeListView = (ListView) mViewList.get(1).findViewById(R.id.mainListView);
        storeTextView = (TextView) mViewList.get(1).findViewById(R.id.statusTextView);
        storeSwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(1).findViewById(R.id.mainSwipeRefreshLayout);

        browsListView = (ListView) mViewList.get(2).findViewById(R.id.mainListView);
        browsTextView = (TextView) mViewList.get(2).findViewById(R.id.statusTextView);
        browsSwipeRefreshLayout = (SwipeRefreshLayout) mViewList.get(2).findViewById(R.id.mainSwipeRefreshLayout);

        goodsArrayList = new ArrayList<>();
        goodsAdapter = new GoodsCollectionListAdapter(mActivity, goodsArrayList);
        goodsListView.setAdapter(goodsAdapter);

        storeArrayList = new ArrayList<>();
        storeAdapter = new StoreCollectionListAdapter(mActivity, storeArrayList);
        storeListView.setAdapter(storeAdapter);

        browsArrayList = new ArrayList<>();
        browsAdapter = new GoodsBrowsListAdapter(mActivity, browsArrayList);
        browsListView.setAdapter(browsAdapter);

        //一些子程序
        if (mActivity.getIntent().getStringExtra("type").equals("store")) {
            mViewPager.setCurrentItem(1);
            getStoreJson();
            getGoodsJson();
            getBrowsJson();
        } else if (mActivity.getIntent().getStringExtra("type").equals("goods")) {
            mViewPager.setCurrentItem(0);
            getGoodsJson();
            getStoreJson();
            getBrowsJson();
        } else {
            mViewPager.setCurrentItem(2);
            getBrowsJson();
            getStoreJson();
            getGoodsJson();
        }

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finish(mActivity);
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        goodsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getGoodsJson();
                    }
                }, 1000);
            }
        });

        storeSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getStoreJson();
                    }
                }, 1000);
            }
        });

        browsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBrowsJson();
                    }
                }, 1000);
            }
        });

    }

    public static void getGoodsJson() {

        goodsTextView.setVisibility(View.VISIBLE);
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_COLLECTION_GOODS, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        paresGoodsJson(jsonObject.getString("favorites_list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showGoodsFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                showGoodsFailure();
            }
        });

    }

    public static void showGoodsFailure() {

        DialogUtil.query(mActivity, "是否重试?", "读取商品收藏数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getGoodsJson();
            }
        });

    }

    public static void paresGoodsJson(String json) {

        try {

            goodsArrayList.clear();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i += 2) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("fav_id_1", jsonObject.getString("fav_id"));
                hashMap.put("store_id_1", jsonObject.getString("store_id"));
                hashMap.put("goods_id_1", jsonObject.getString("goods_id"));
                hashMap.put("goods_name_1", jsonObject.getString("goods_name"));
                hashMap.put("goods_price_1", "￥" + jsonObject.getString("goods_price"));
                hashMap.put("goods_image_1", jsonObject.getString("goods_image_url"));
                if ((i + 1) < jsonArray.length()) {
                    jsonObject = (JSONObject) jsonArray.get(i + 1);
                    hashMap.put("fav_id_2", jsonObject.getString("fav_id"));
                    hashMap.put("store_id_2", jsonObject.getString("store_id"));
                    hashMap.put("goods_id_2", jsonObject.getString("goods_id"));
                    hashMap.put("goods_name_2", jsonObject.getString("goods_name"));
                    hashMap.put("goods_price_2", "￥" + jsonObject.getString("goods_price"));
                    hashMap.put("goods_image_2", jsonObject.getString("goods_image_url"));
                } else {
                    hashMap.put("fav_id_2", "");
                    hashMap.put("store_id_2", "");
                    hashMap.put("goods_id_2", "");
                    hashMap.put("goods_name_2", "");
                    hashMap.put("goods_price_2", "");
                    hashMap.put("goods_image_2", "");
                }
                goodsArrayList.add(hashMap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        goodsAdapter.notifyDataSetChanged();
        goodsTextView.setVisibility(View.GONE);
        goodsSwipeRefreshLayout.setRefreshing(false);

    }

    private void getStoreJson() {

        storeTextView.setVisibility(View.VISIBLE);

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_COLLECTION_STORE, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        paresStoreJson(jsonObject.getString("favorites_list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showStoreFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                showStoreFailure();
            }
        });

    }

    private void showStoreFailure() {

        DialogUtil.query(mActivity, "是否重试?", "读取商店收藏数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getStoreJson();
            }
        });

    }

    private void paresStoreJson(String json) {

        try {

            storeArrayList.clear();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("store_id", jsonObject.getString("store_id"));
                hashMap.put("store_name", jsonObject.getString("store_name"));
                hashMap.put("store_image", jsonObject.getString("store_avatar_url"));
                hashMap.put("fav_time", jsonObject.getString("fav_time_text"));
                hashMap.put("goods_count", "商品:" + jsonObject.getString("goods_count"));
                hashMap.put("store_collect", "收藏:" + jsonObject.getString("store_collect"));
                storeArrayList.add(hashMap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        storeAdapter.notifyDataSetChanged();
        storeTextView.setVisibility(View.GONE);
        storeSwipeRefreshLayout.setRefreshing(false);

    }

    private void getBrowsJson() {

        browsTextView.setVisibility(View.VISIBLE);

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_COLLECTION_BROWS, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        paresBrowsJson(jsonObject.getString("goodsbrowse_list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showBrowsFailure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                showBrowsFailure();
            }
        });

    }

    private void showBrowsFailure() {

        DialogUtil.query(mActivity, "是否重试?", "读取我的足迹数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getBrowsJson();
            }
        });

    }

    private void paresBrowsJson(String json) {

        try {

            browsArrayList.clear();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i += 2) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("goods_id", jsonObject.getString("goods_id"));
                hashMap.put("goods_name", jsonObject.getString("goods_name"));
                hashMap.put("goods_promotion_price", "￥" + jsonObject.getString("goods_promotion_price"));
                hashMap.put("goods_marketprice", "￥" + jsonObject.getString("goods_marketprice"));
                hashMap.put("goods_image_url", jsonObject.getString("goods_image_url"));
                hashMap.put("browsetime_text", jsonObject.getString("browsetime_text"));
                browsArrayList.add(hashMap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        browsAdapter.notifyDataSetChanged();
        browsTextView.setVisibility(View.GONE);
        browsSwipeRefreshLayout.setRefreshing(false);

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        createControl();
        createEvent();
    }

}