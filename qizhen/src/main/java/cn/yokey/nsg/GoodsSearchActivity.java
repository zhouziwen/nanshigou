package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.adapter.GoodsSearchListAdapter;
import cn.yokey.system.Android;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.LogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class GoodsSearchActivity extends AppCompatActivity {

    public static Activity mActivity;

    private String keyString;
    private String jsonString;
    private String modelString;
    private String gc_idString;
    private String keywordString;
    private String listTypeString;

    private ImageView leftImageView;
    private EditText keywordEditText;
    private ImageView rightImageView;
    private Spinner mSpinner;

    private Spinner sortSpinner;
    private TextView saleTextView;
    private TextView screenTextView;
    private ImageView listImageView;
    private RelativeLayout screenRelativeLayout;

    private ListView mListView;
    private TextView mTextView;
    private GoodsSearchListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> mArrayList;

    private void createControl() {

        mActivity = this;

        keyString = "1";
        modelString = mActivity.getIntent().getStringExtra("model");

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        keywordEditText = (EditText) findViewById(R.id.keywordEditText);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        mSpinner = (Spinner) findViewById(R.id.typeSpinner);

        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        saleTextView = (TextView) findViewById(R.id.saleTextView);
        screenTextView = (TextView) findViewById(R.id.screenTextView);
        listImageView = (ImageView) findViewById(R.id.listImageView);
        screenRelativeLayout = (RelativeLayout) findViewById(R.id.screenRelativeLayout);

        mListView = (ListView) findViewById(R.id.mainListView);
        mTextView = (TextView) findViewById(R.id.statusTextView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

        //初始化参数
        mSpinner.setVisibility(View.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"综合排序", "人气排序", "价格从高到低", "价格从低到高"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        mArrayList = new ArrayList<>();
        mAdapter = new GoodsSearchListAdapter(mActivity, mArrayList);
        mListView.setAdapter(mAdapter);

        if (modelString.equals("search")) {
            keywordString = mActivity.getIntent().getStringExtra("keyword");
            keywordEditText.setText(keywordString);
            keywordEditText.setSelection(keywordString.length());
        } else {
            gc_idString = mActivity.getIntent().getStringExtra("gc_id");
        }

        //一些子程序
        getJson();

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.finish(mActivity);
            }
        });

        keywordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelString.equals("search")) {
                    String keyword = keywordEditText.getText().toString();
                    if (keyword.equals(keywordString)) {
                        return;
                    }
                    if (keyword.isEmpty()) {
                        ToastUtil.show(mActivity, "请输入关键字");
                        return;
                    }
                    keywordString = keyword;
                    modelString = "search";
                    getJson();
                }
            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywordString = keywordEditText.getText().toString();
                if (TextUtil.isEmpty(keywordString)) {
                    ToastUtil.show(mActivity, "关键字不能为空");
                } else {
                    Android.hideKeyboard(mActivity, rightImageView);
                    modelString = "search";
                    getJson();
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

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(14.0f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        screenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (screenRelativeLayout.getVisibility() == View.VISIBLE) {
                    screenRelativeLayout.setVisibility(View.GONE);
                    screenTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.main));
                } else {
                    screenRelativeLayout.setVisibility(View.VISIBLE);
                    screenTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
                }
            }
        });

        listImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listTypeString.equals("ver")) {
                    listImageView.setImageResource(R.mipmap.ic_goods_list_hor);
                    listTypeString = "hor";
                    setListHor();
                } else {
                    listImageView.setImageResource(R.mipmap.ic_goods_list_ver);
                    listTypeString = "ver";
                    setListVer();
                }
            }
        });

    }

    private void getJson() {

        listTypeString = "ver";
        mSpinner.setSelection(0);
        mTextView.setText("加载中...");
        mTextView.setVisibility(View.VISIBLE);
        screenRelativeLayout.setVisibility(View.GONE);
        listImageView.setImageResource(R.mipmap.ic_goods_list_ver);
        saleTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));
        screenTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.greyAdd));

        String link;
        if (modelString.equals("search")) {
            link = Constant.LINK_MOBILE_GOODS_SEARCH + "&key=" + keyString + "&keyword=" + keywordString;
        } else {
            link = Constant.LINK_MOBILE_GOODS_SEARCH + "&key=" + keyString + "&gc_id=" + gc_idString;
        }


        Constant.mFinalHttp.get(link, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        jsonString = jsonObject.getString("goods_list");
                        setListVer();
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

        DialogUtil.query(mActivity, "是否重试", "读取数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getJson();
            }
        });

    }

    private void setListVer() {

        try {
            mArrayList.clear();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("list_type", listTypeString);
                hashMap.put("goods_id", jsonObject.getString("goods_id"));
                hashMap.put("goods_name", jsonObject.getString("goods_name"));
                hashMap.put("goods_promotion_price", "￥" + jsonObject.getString("goods_promotion_price"));
                hashMap.put("goods_salenum", "已售：" + jsonObject.getString("goods_salenum") + " 件");
                hashMap.put("goods_image_url", jsonObject.getString("goods_image_url"));
                mArrayList.add(hashMap);
            }
            if (mArrayList.size() == 0) {
                mTextView.setText("暂无结果...");
            } else {
                mTextView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private void setListHor() {

        try {
            mArrayList.clear();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i += 2) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("list_type", listTypeString);
                hashMap.put("goods_id_1", jsonObject.getString("goods_id"));
                hashMap.put("goods_name_1", jsonObject.getString("goods_name"));
                hashMap.put("goods_promotion_price_1", "￥" + jsonObject.getString("goods_promotion_price"));
                hashMap.put("goods_salenum_1", "已售：" + jsonObject.getString("goods_salenum") + " 件");
                hashMap.put("goods_image_url_1", jsonObject.getString("goods_image_url"));
                if ((i + 1) < jsonArray.length()) {
                    jsonObject = (JSONObject) jsonArray.get(i + 1);
                    hashMap.put("goods_id_2", jsonObject.getString("goods_id"));
                    hashMap.put("goods_name_2", jsonObject.getString("goods_name"));
                    hashMap.put("goods_promotion_price_2", "￥" + jsonObject.getString("goods_promotion_price"));
                    hashMap.put("goods_salenum_2", "已售：" + jsonObject.getString("goods_salenum") + " 件");
                    hashMap.put("goods_image_url_2", jsonObject.getString("goods_image_url"));
                } else {
                    hashMap.put("goods_id_2", "");
                    hashMap.put("goods_name_2", "");
                    hashMap.put("goods_promotion_price_2", "");
                    hashMap.put("goods_salenum_2", "");
                    hashMap.put("goods_image_url_2", "");
                }
                mArrayList.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Android.hideKeyboard(mActivity, rightImageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_search);
        createControl();
        createEvent();
    }

}