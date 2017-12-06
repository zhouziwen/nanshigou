package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.adapter.AddressListAdapter;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;

public class AddressActivity extends AppCompatActivity {

    public static Activity mActivity;

    public static HashMap<String,String> mHashMap;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private ListView mListView;
    private TextView mTextView;
    private AddressListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> mArrayList;

    private void createControl() {

        mActivity = this;

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        mListView = (ListView) findViewById(R.id.mainListView);
        mTextView = (TextView) findViewById(R.id.statusTextView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

        //初始化参数
        titleTextView.setText("收货地址");
        rightImageView.setImageResource(R.mipmap.ic_action_add);

        mArrayList = new ArrayList<>();
        mAdapter = new AddressListAdapter(mActivity, mArrayList);
        mListView.setAdapter(mAdapter);

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

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startWithResult(mActivity, new Intent(mActivity, AddressAddActivity.class), 0);
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

        mTextView.setText("正在加载...");
        mTextView.setVisibility(View.VISIBLE);
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_ADDRESS, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        parse(jsonObject.getString("address_list"));
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

    private void parse(String json) {

        if (TextUtil.isEmpty(json) || json.equals("[]")) {

            mTextView.setText("暂无收货地址");
            mSwipeRefreshLayout.setRefreshing(false);

        } else {

            try {
                mArrayList.clear();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("address_id", jsonObject.getString("address_id"));
                    hashMap.put("member_id", jsonObject.getString("member_id"));
                    hashMap.put("true_name", jsonObject.getString("true_name"));
                    hashMap.put("area_id", jsonObject.getString("area_id"));
                    hashMap.put("city_id", jsonObject.getString("city_id"));
                    hashMap.put("area_info", jsonObject.getString("area_info"));
                    hashMap.put("address", jsonObject.getString("address"));
                    hashMap.put("tel_phone", jsonObject.getString("tel_phone"));
                    hashMap.put("mob_phone", jsonObject.getString("mob_phone"));
                    hashMap.put("is_default", jsonObject.getString("is_default"));
                    hashMap.put("dlyp_id", jsonObject.getString("dlyp_id"));
                    mArrayList.add(hashMap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mAdapter.notifyDataSetChanged();
            mTextView.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);

        }

    }

    @Override
    protected void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (res == RESULT_OK) {
            getJson();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        createControl();
        createEvent();
    }

}