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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.yokey.adapter.BasePagerAdapter;
import cn.yokey.adapter.OrderRefundListAdapter;
import cn.yokey.system.Android;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;

public class OrderRefundActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private ListView[] mListView;
    private TextView[] mTextView;
    private OrderRefundListAdapter[] mAdapter;
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
        titleTextView.setText("退货/退款");
        rightImageView.setVisibility(View.GONE);

        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("退款列表");
        mTitleList.add("退货列表");

        List<View> mViewList = new ArrayList<>();
        mListView = new ListView[mTitleList.size()];
        mTextView = new TextView[mTitleList.size()];
        mArrayList = new ArrayList[mTitleList.size()];
        mAdapter = new OrderRefundListAdapter[mTitleList.size()];
        mSwipeRefreshLayout = new SwipeRefreshLayout[mTitleList.size()];

        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
            mViewList.add(mActivity.getLayoutInflater().inflate(R.layout.include_list_view, null));
            mArrayList[i] = new ArrayList<>();
            mAdapter[i] = new OrderRefundListAdapter(mActivity, mArrayList[i]);
            mListView[i] = (ListView) mViewList.get(i).findViewById(R.id.mainListView);
            mTextView[i] = (TextView) mViewList.get(i).findViewById(R.id.statusTextView);
            mSwipeRefreshLayout[i] = (SwipeRefreshLayout) mViewList.get(i).findViewById(R.id.mainSwipeRefreshLayout);
            mListView[i].setAdapter(mAdapter[i]);
        }

        Android.setTabLayout(mActivity, mTabLayout, new BasePagerAdapter(mViewList, mTitleList), mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        //一些子程序
        getRefund();

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

        mSwipeRefreshLayout[0].setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getRefund();
                    }
                }, 1000);
            }
        });

    }

    private void getRefund() {

        mArrayList[0].clear();
        mTextView[0].setText("加载中...");
        mTextView[0].setVisibility(View.VISIBLE);

        Constant.mFinalHttp.get(Constant.LINK_MOBILE_ORDER_REFOUND + "&key=" + Constant.userKeyString, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        parseRefund(jsonObject.getString("refund_list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        failureRefund();
                    }
                } else {
                    failureRefund();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                failureRefund();
            }
        });

    }

    private void failureRefund() {

        DialogUtil.query(mActivity, "是否重试?", "读取退款数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getRefund();
            }
        });

    }

    private void parseRefund(String json) {

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("refund_id", jsonObject.getString("refund_id"));
                hashMap.put("order_id", jsonObject.getString("order_id"));
                hashMap.put("refund_amount", "￥ " + jsonObject.getString("refund_amount"));
                hashMap.put("refund_sn", jsonObject.getString("refund_sn"));
                hashMap.put("order_sn", jsonObject.getString("order_sn"));
                hashMap.put("add_time", jsonObject.getString("add_time"));
                hashMap.put("admin_state", jsonObject.getString("admin_state"));
                hashMap.put("store_id", jsonObject.getString("store_id"));
                hashMap.put("store_name", jsonObject.getString("store_name"));
                hashMap.put("goods_list", jsonObject.getString("goods_list"));
                mArrayList[0].add(hashMap);
            }
            if (mArrayList[0].size() == 0) {
                mTextView[0].setText("暂无订单");
            } else {
                mTextView[0].setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter[0].notifyDataSetChanged();
        mSwipeRefreshLayout[0].setRefreshing(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_refund);
        createControl();
        createEvent();
    }

}
