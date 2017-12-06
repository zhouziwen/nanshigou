package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.LogUtil;
import cn.yokey.util.TextUtil;

public class OrderRefundDetailedActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private TextView snTextView;
    private TextView reasonTextView;
    private TextView moneyTextView;
    private TextView messageTextView;
    private TextView sellerStatusTextView;
    private TextView sellerRemarksTextView;
    private TextView adminStatusTextView;
    private TextView adminRemarksTextView;

    private void createControl() {

        mActivity = this;

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        snTextView = (TextView) findViewById(R.id.snTextView);
        reasonTextView = (TextView) findViewById(R.id.reasonTextView);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        sellerStatusTextView = (TextView) findViewById(R.id.sellerStatusTextView);
        sellerRemarksTextView = (TextView) findViewById(R.id.sellerRemarksTextView);
        adminStatusTextView = (TextView) findViewById(R.id.adminStatusTextView);
        adminRemarksTextView = (TextView) findViewById(R.id.adminRemarksTextView);

        TextView storeTextView = (TextView) findViewById(R.id.storeTextView);
        TextView statusTextView = (TextView) findViewById(R.id.statusTextView);
        ImageView mImageView = (ImageView) findViewById(R.id.mainImageView);
        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        TextView info1TextView = (TextView) findViewById(R.id.info1TextView);
        TextView info2TextView = (TextView) findViewById(R.id.info2TextView);
        TextView timeTextView = (TextView) findViewById(R.id.timeTextView);

        //初始化参数
        titleTextView.setText("退款/货详细");
        rightImageView.setVisibility(View.GONE);

        storeTextView.setText(mActivity.getIntent().getStringExtra("store_name"));
        statusTextView.setText(mActivity.getIntent().getStringExtra("admin_state"));
        ImageLoader.getInstance().displayImage(mActivity.getIntent().getStringExtra("goods_img_360"), mImageView);
        nameTextView.setText(mActivity.getIntent().getStringExtra("goods_name"));
        final String info1 = " <font color='#FF5001'>" + mActivity.getIntent().getStringExtra("refund_amount") + "</font>";
        info1TextView.setText(Html.fromHtml(info1));
        String info2 = "合计 <font color='#FF5001'>" + mActivity.getIntent().getStringExtra("refund_amount") + "</font> 元";
        info2TextView.setText(Html.fromHtml(info2));
        timeTextView.setText(mActivity.getIntent().getStringExtra("add_time"));

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

    }

    private void getJson() {

        DialogUtil.progress(mActivity);
        String link = "&key=" + Constant.userKeyString + "&refund_id=" + mActivity.getIntent().getStringExtra("refund_id");
        Constant.mFinalHttp.get(Constant.LINK_MOBILE_ORDER_REFOUND_INFO + link, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                DialogUtil.cancel();
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        parseJson(jsonObject.getString("datas"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        failure();
                    }
                } else {
                    failure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.cancel();
                failure();
            }
        });

    }

    private void failure() {

        DialogUtil.query(mActivity, "是否重试?", "读取信息失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJson();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.finish(mActivity);
            }
        });

    }

    private void parseJson(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            String refund = jsonObject.getString("refund");
            JSONObject refundObject = new JSONObject(refund);
            snTextView.setText(refundObject.getString("refund_sn"));
            reasonTextView.setText(refundObject.getString("reason_info"));
            moneyTextView.setText(refundObject.getString("refund_amount"));
            messageTextView.setText(refundObject.getString("buyer_message"));
            sellerStatusTextView.setText(refundObject.getString("seller_state"));
            sellerRemarksTextView.setText(refundObject.getString("seller_message"));
            adminStatusTextView.setText(refundObject.getString("admin_state"));
            adminRemarksTextView.setText(refundObject.getString("admin_message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_refund_detailed);
        createControl();
        createEvent();
    }

}
