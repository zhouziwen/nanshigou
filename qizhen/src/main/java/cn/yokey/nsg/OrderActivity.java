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
import cn.yokey.adapter.OrderListAdapter;
import cn.yokey.system.Android;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.LogUtil;
import cn.yokey.util.TextUtil;

public class OrderActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private ListView[] mListView;
    private TextView[] mTextView;
    private OrderListAdapter[] mAdapter;
    private SwipeRefreshLayout[] mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>>[] mArrayList;

    private void createControl() {

        mActivity = this;

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.mainViewPager);

        //初始化参数
        titleTextView.setText("我的订单");
        rightImageView.setImageResource(R.mipmap.ic_action_del);

        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("全部");
        mTitleList.add("待付款");
        mTitleList.add("待发货");
        mTitleList.add("待收货");
        mTitleList.add("待评价");

        List<View> mViewList = new ArrayList<>();
        mListView = new ListView[mTitleList.size()];
        mTextView = new TextView[mTitleList.size()];
        mArrayList = new ArrayList[mTitleList.size()];
        mAdapter = new OrderListAdapter[mTitleList.size()];
        mSwipeRefreshLayout = new SwipeRefreshLayout[mTitleList.size()];

        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
            mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
            mArrayList[i] = new ArrayList<>();
            mAdapter[i] = new OrderListAdapter(mActivity, mArrayList[i]);
            mListView[i] = (ListView) mViewList.get(i).findViewById(R.id.mainListView);
            mTextView[i] = (TextView) mViewList.get(i).findViewById(R.id.statusTextView);
            mSwipeRefreshLayout[i] = (SwipeRefreshLayout) mViewList.get(i).findViewById(R.id.mainSwipeRefreshLayout);
            mListView[i].setAdapter(mAdapter[i]);
        }

        Android.setTabLayout(mActivity, mTabLayout, new BasePagerAdapter(mViewList, mTitleList), mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        switch (mActivity.getIntent().getStringExtra("type")) {
            case "all":
                mViewPager.setCurrentItem(0);
                break;
            case "pay":
                mViewPager.setCurrentItem(1);
                break;
            case "drive":
                mViewPager.setCurrentItem(2);
                break;
            case "receipt":
                mViewPager.setCurrentItem(3);
                break;
            case "comment":
                mViewPager.setCurrentItem(4);
                break;
            default:
                mViewPager.setCurrentItem(0);
                break;
        }

        //一些子程序
        getJson();

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

        for (int i = 0; i < mSwipeRefreshLayout.length; i++) {
            mSwipeRefreshLayout[i].setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        }

    }

    private void getJson() {

        for (int i = 0; i < 5; i++) {
            mArrayList[i].clear();
            mTextView[i].setText("加载中...");
            mTextView[i].setVisibility(View.VISIBLE);
        }

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_ORDER, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        parseJson(jsonObject.getString("order_group_list"));
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

        DialogUtil.query(mActivity, "是否重试?", "读取订单数据失败", new View.OnClickListener() {
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
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONArray temp = new JSONArray(jsonObject.getString("order_list"));
                jsonObject = (JSONObject) temp.get(0);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("order_id", jsonObject.getString("order_id"));
                hashMap.put("order_sn", jsonObject.getString("order_sn"));
                hashMap.put("pay_sn", jsonObject.getString("pay_sn"));
                hashMap.put("store_id", jsonObject.getString("store_id"));
                hashMap.put("store_name", jsonObject.getString("store_name"));
                hashMap.put("add_time", jsonObject.getString("add_time"));
                hashMap.put("payment_code", jsonObject.getString("payment_code"));
                hashMap.put("payment_time", jsonObject.getString("payment_time"));
                hashMap.put("finnshed_time", jsonObject.getString("finnshed_time"));
                hashMap.put("order_amount", "￥ " + jsonObject.getString("order_amount"));
                hashMap.put("shipping_fee", "￥ " + jsonObject.getString("shipping_fee"));
                hashMap.put("order_state", jsonObject.getString("order_state"));
                hashMap.put("state_desc", jsonObject.getString("state_desc"));
                hashMap.put("payment_name", jsonObject.getString("payment_name"));
                hashMap.put("extend_order_common", jsonObject.getString("extend_order_common"));
                hashMap.put("extend_store", jsonObject.getString("extend_store"));
                hashMap.put("extend_order_goods", jsonObject.getString("extend_order_goods"));
                switch (jsonObject.getString("order_state")) {
                    case "10":
                        mArrayList[1].add(hashMap);
                        break;
                    case "20":
                        mArrayList[2].add(hashMap);
                        break;
                    case "30":
                        mArrayList[3].add(hashMap);
                        break;
                    case "40":
                        mArrayList[4].add(hashMap);
                        break;
                    default:
                        break;
                }
                mArrayList[0].add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < mAdapter.length; i++) {
            mAdapter[i].notifyDataSetChanged();
            mSwipeRefreshLayout[i].setRefreshing(false);
            if (mArrayList[i].size() == 0) {
                mTextView[i].setText("暂无订单");
            } else {
                mTextView[i].setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        createControl();
        createEvent();
    }

}
