package cn.yokey.nsg;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.LogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class BuyPayActivity extends AppCompatActivity {

    public static Activity mActivity;

    private String pay_sn;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private TextView orderIdTextView;
    private TextView calcTextView;

    private RadioButton aliPayRadioButton;
    private RadioButton wxPayRadioButton;

    private TextView payTextView;

    private void createControl() {

        mActivity = this;

        pay_sn = mActivity.getIntent().getStringExtra("pay_sn");

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        orderIdTextView = (TextView) findViewById(R.id.orderIdTextView);
        calcTextView = (TextView) findViewById(R.id.calcTextView);
        aliPayRadioButton = (RadioButton) findViewById(R.id.aliPayTextView);
        wxPayRadioButton = (RadioButton) findViewById(R.id.wxPayTextView);
        payTextView = (TextView) findViewById(R.id.payTextView);

        //初始化参数
        titleTextView.setText("订单支付");
        rightImageView.setVisibility(View.GONE);
        aliPayRadioButton.setVisibility(View.GONE);
        wxPayRadioButton.setVisibility(View.GONE);
        orderIdTextView.setText("编号:");
        orderIdTextView.append(pay_sn);

        //一些子程序
        getOrderInfo();

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

            }
        });

        payTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = Constant.LINK_MOBILE_PAY + "&key=" + Constant.userKeyString + "&pay_sn=" + pay_sn + "&password=&rcb_pay=0&pd_pay=0&payment_code=alipay";
                Intent intent = new Intent(mActivity, BrowserActivity.class);
                intent.putExtra("Link", link);
                ActivityUtil.start(mActivity, intent);
                ActivityUtil.finish(mActivity);
            }
        });

    }

    private void getOrderInfo() {

        DialogUtil.progress(mActivity, "获取订单信息");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        ajaxParams.put("pay_sn", pay_sn);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_PAY_INFO, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        String pay_info = jsonObject.getString("pay_info");
                        jsonObject = new JSONObject(pay_info);
                        String calc = "共 <font color='#FF5001'>" + jsonObject.getString("pay_amount") + "</font> 元";
                        calcTextView.setText(Html.fromHtml(calc));
                        getPayList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        failureOrderInfo();
                    }
                } else {
                    failureOrderInfo();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                failureOrderInfo();
            }
        });

    }

    private void failureOrderInfo() {

        DialogUtil.query(mActivity, "是否重试？", "读取订单信息失败!", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderInfo();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.finish(mActivity);
            }
        });

    }

    private void getPayList() {

        DialogUtil.progress(mActivity, "正在获取支持的支付方式");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_PAY_LIST, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        String payment_list = jsonObject.getString("payment_list");
                        if (payment_list.contains("alipay")) {
                            aliPayRadioButton.setChecked(true);
                            aliPayRadioButton.setVisibility(View.VISIBLE);
                        }
                        if (payment_list.contains("wx")) {
                            wxPayRadioButton.setVisibility(View.VISIBLE);
                            if (aliPayRadioButton.getVisibility() == View.GONE) {
                                wxPayRadioButton.setChecked(true);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        failurePayList();
                    }
                } else {
                    failurePayList();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                failurePayList();
            }
        });

    }

    private void failurePayList() {

        DialogUtil.query(mActivity, "是否重试？", "读取支持的支付方式失败!", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPayList();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.finish(mActivity);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_pay);
        createControl();
        createEvent();
    }

}
