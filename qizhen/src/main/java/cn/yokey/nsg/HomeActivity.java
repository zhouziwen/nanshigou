package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.adapter.HomeListAdapter;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;

public class HomeActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private EditText titleEditText;
    private ImageView rightImageView;

    private ListView mListView;
    private TextView statusTextView;
    private HomeListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> mArrayList;

    private void createControl() {

        mActivity = this;

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        mListView = (ListView) findViewById(R.id.mainListView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

        //初始化参数
        mArrayList = new ArrayList<>();
        mAdapter = new HomeListAdapter(mActivity, mArrayList);
        mListView.setAdapter(mAdapter);

        //一些子程序
        getJson();

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.start(mActivity, new Intent(mActivity, ScanActivity.class));
            }
        });

        titleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.start(mActivity, new Intent(mActivity, SearchActivity.class));
            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

    }

    private void getJson() {

        mArrayList.clear();
        statusTextView.setVisibility(View.VISIBLE);

        Constant.mFinalHttp.get(Constant.LINK_MOBILE_INDEX, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        parseJson(jsonObject.getString("datas"));
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

        statusTextView.setText("加载失败...");

        DialogUtil.query(mActivity, "是否重试?", "读取首页数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getJson();
            }
        });

    }

    private void parseJson(String json) {

        try {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                if (i == 1) {
                    HashMap<String, ArrayList<HashMap<String, String>>> hashMap = new HashMap<>();
                    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("nav", "nav");
                    arrayList.add(temp);
                    hashMap.put("nav", arrayList);
                    mArrayList.add(hashMap);
                }

                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                if (jsonObject.toString().contains("adv_list")) {
                    String adv_list = jsonObject.getString("adv_list");
                    JSONObject advJsonObject = new JSONObject(adv_list);
                    String adv_list_item = advJsonObject.getString("item");
                    parseAdvList(adv_list_item);
                }

                if (jsonObject.toString().contains("home2")) {
                    parseHome2(jsonObject.getString("home2"));
                }

                if (jsonObject.toString().contains("home4")) {
                    parseHome4(jsonObject.getString("home4"));
                }

                if (jsonObject.toString().contains("goods1")) {
                    String goods1 = jsonObject.getString("goods1");
                    JSONObject goods1JsonObject = new JSONObject(goods1);
                    String goods1_title = goods1JsonObject.getString("title");
                    String goods1_item = goods1JsonObject.getString("item");
                    parseGoods1(goods1_title, goods1_item);
                }

                if (jsonObject.toString().contains("goods2")) {
                    String goods2 = jsonObject.getString("goods2");
                    JSONObject goods2JsonObject = new JSONObject(goods2);
                    String goods2_title = goods2JsonObject.getString("title");
                    String goods2_item = goods2JsonObject.getString("item");
                    parseGoods2(goods2_title, goods2_item);
                }

                if (jsonObject.toString().contains("\"goods\":")) {
                    String goods = jsonObject.getString("goods");
                    JSONObject goodsJsonObject = new JSONObject(goods);
                    String goods_title = goodsJsonObject.getString("title");
                    String goods_item = goodsJsonObject.getString("item");
                    if (!goods_item.equals("[]")) {
                        parseGoods(goods_title, goods_item);
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSwipeRefreshLayout.setRefreshing(false);
        statusTextView.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();

    }

    private void parseAdvList(String json) {

        try {
            HashMap<String, ArrayList<HashMap<String, String>>> hashMap = new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("image", jsonObject.getString("image"));
                    temp.put("type", jsonObject.getString("type"));
                    temp.put("data", jsonObject.getString("data"));
                    arrayList.add(temp);
                }
            }
            hashMap.put("adv_list", arrayList);
            mArrayList.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseHome2(String json) {

        try {
            HashMap<String, ArrayList<HashMap<String, String>>> hashMap = new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            HashMap<String, String> temp = new HashMap<>();
            temp.put("title", jsonObject.getString("title"));
            temp.put("square_image", jsonObject.getString("square_image"));
            temp.put("square_type", jsonObject.getString("square_type"));
            temp.put("square_data", jsonObject.getString("square_data"));
            temp.put("rectangle1_image", jsonObject.getString("rectangle1_image"));
            temp.put("rectangle1_type", jsonObject.getString("rectangle1_type"));
            temp.put("rectangle1_data", jsonObject.getString("rectangle1_data"));
            temp.put("rectangle2_image", jsonObject.getString("rectangle2_image"));
            temp.put("rectangle2_type", jsonObject.getString("rectangle2_type"));
            temp.put("rectangle2_data", jsonObject.getString("rectangle2_data"));
            arrayList.add(temp);
            hashMap.put("home2", arrayList);
            mArrayList.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseHome4(String json) {

        try {
            HashMap<String, ArrayList<HashMap<String, String>>> hashMap = new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            HashMap<String, String> temp = new HashMap<>();
            temp.put("title", jsonObject.getString("title"));
            temp.put("square_image", jsonObject.getString("square_image"));
            temp.put("square_type", jsonObject.getString("square_type"));
            temp.put("square_data", jsonObject.getString("square_data"));
            temp.put("rectangle1_image", jsonObject.getString("rectangle1_image"));
            temp.put("rectangle1_type", jsonObject.getString("rectangle1_type"));
            temp.put("rectangle1_data", jsonObject.getString("rectangle1_data"));
            temp.put("rectangle2_image", jsonObject.getString("rectangle2_image"));
            temp.put("rectangle2_type", jsonObject.getString("rectangle2_type"));
            temp.put("rectangle2_data", jsonObject.getString("rectangle2_data"));
            arrayList.add(temp);
            hashMap.put("home4", arrayList);
            mArrayList.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseGoods1(String title, String json) {

        try {
            HashMap<String, ArrayList<HashMap<String, String>>> hashMap = new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("title", title);
                    temp.put("goods_id", jsonObject.getString("goods_id"));
                    temp.put("goods_name", jsonObject.getString("goods_name"));
                    temp.put("goods_promotion_price", "￥" + jsonObject.getString("goods_promotion_price"));
                    temp.put("goods_price", "￥" + jsonObject.getString("goods_price"));
                    temp.put("goods_image", jsonObject.getString("goods_image"));
                    arrayList.add(temp);
                }
            }
            hashMap.put("goods1", arrayList);
            mArrayList.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseGoods2(String title, String json) {

        try {
            HashMap<String, ArrayList<HashMap<String, String>>> hashMap = new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("title", title);
                    temp.put("goods_id", jsonObject.getString("goods_id"));
                    temp.put("goods_name", jsonObject.getString("goods_name"));
                    temp.put("goods_promotion_price", "￥" + jsonObject.getString("goods_promotion_price"));
                    temp.put("goods_price", "￥" + jsonObject.getString("goods_price"));
                    temp.put("goods_image", jsonObject.getString("goods_image"));
                    arrayList.add(temp);
                }
            }
            hashMap.put("goods2", arrayList);
            mArrayList.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseGoods(String title, String json) {

        try {
            HashMap<String, ArrayList<HashMap<String, String>>> hashMap = new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("title", title);
                    temp.put("goods_id", jsonObject.getString("goods_id"));
                    temp.put("goods_name", jsonObject.getString("goods_name"));
                    temp.put("goods_promotion_price", "￥" + jsonObject.getString("goods_promotion_price"));
                    temp.put("goods_price", "￥" + jsonObject.getString("goods_price"));
                    temp.put("goods_image", jsonObject.getString("goods_image"));
                    arrayList.add(temp);
                }
            }
            hashMap.put("goods", arrayList);
            mArrayList.add(hashMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createControl();
        createEvent();
    }

}