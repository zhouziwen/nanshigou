package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.LogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class UserActivity extends AppCompatActivity {

    public static Activity mActivity;

    private RelativeLayout userRelativeLayout;
    private ImageView headImageView;
    private TextView usernameTextView;
    private TextView collectionGoodsTextView;
    private TextView collectionStoreTextView;
    private TextView myFootprintTextView;

    private TextView orderTextView;
    private TextView orderWaitPayTextView;
    private TextView orderWaitDriveTextView;
    private TextView orderWaitReceiptTextView;
    private TextView orderWaitCommentTextView;
    private TextView orderWaitRefundTextView;

    private TextView propertyTextView;
    private TextView propertyMoneyTextView;
    private TextView propertyCardTextView;
    private TextView propertyVouchersTextView;
    private TextView propertyRedTextView;
    private TextView propertyIntegralTextView;

    private TextView addressTextView;
    private TextView settingTextView;

    private void createControl() {

        mActivity = this;

        //实例化控件
        userRelativeLayout = (RelativeLayout) findViewById(R.id.userRelativeLayout);
        headImageView = (ImageView) findViewById(R.id.headImageView);
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        collectionGoodsTextView = (TextView) findViewById(R.id.collectionGoodsTextView);
        collectionStoreTextView = (TextView) findViewById(R.id.collectionStoreTextView);
        myFootprintTextView = (TextView) findViewById(R.id.myFootprintTextView);

        orderTextView = (TextView) findViewById(R.id.orderTextView);
        orderWaitPayTextView = (TextView) findViewById(R.id.orderWaitPayTextView);
        orderWaitDriveTextView = (TextView) findViewById(R.id.orderWaitDriveTextView);
        orderWaitReceiptTextView = (TextView) findViewById(R.id.orderWaitReceiptTextView);
        orderWaitCommentTextView = (TextView) findViewById(R.id.orderWaitCommentTextView);
        orderWaitRefundTextView = (TextView) findViewById(R.id.orderWaitRefundTextView);

        propertyTextView = (TextView) findViewById(R.id.propertyTextView);
        propertyMoneyTextView = (TextView) findViewById(R.id.propertyMoneyTextView);
        propertyCardTextView = (TextView) findViewById(R.id.propertyCardTextView);
        propertyVouchersTextView = (TextView) findViewById(R.id.propertyVouchersTextView);
        propertyRedTextView = (TextView) findViewById(R.id.propertyRedTextView);
        propertyIntegralTextView = (TextView) findViewById(R.id.propertyIntegralTextView);

        addressTextView = (TextView) findViewById(R.id.addressTextView);
        settingTextView = (TextView) findViewById(R.id.settingTextView);

        //初始化参数

        if (Constant.userLoginBoolean) {
            login();
        }

    }

    private void createEvent() {

        userRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startWithLogin(mActivity, new Intent(mActivity, UserCenterActivity.class));
            }
        });

        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startWithLogin(mActivity, new Intent(mActivity, UserCenterActivity.class));
            }
        });

        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startWithLogin(mActivity, new Intent(mActivity, UserCenterActivity.class));
            }
        });

        collectionGoodsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CollectionActivity.class);
                intent.putExtra("type", "goods");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        collectionStoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CollectionActivity.class);
                intent.putExtra("type", "store");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        myFootprintTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CollectionActivity.class);
                intent.putExtra("type", "browse");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        orderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("type", "all");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        orderWaitPayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("type", "pay");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        orderWaitDriveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("type", "drive");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        orderWaitReceiptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("type", "receipt");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        orderWaitCommentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, OrderActivity.class);
                intent.putExtra("type", "comment");
                ActivityUtil.startWithLogin(mActivity, intent);
            }
        });

        orderWaitRefundTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startWithLogin(mActivity, new Intent(mActivity, OrderRefundActivity.class));
            }
        });

        addressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startWithLogin(mActivity, new Intent(mActivity, AddressActivity.class));
            }
        });

        settingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.startWithLogin(mActivity, new Intent(mActivity, SettingActivity.class));
            }
        });

    }

    private void login() {

        usernameTextView.setText("登录中...");

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("username", Constant.userUsernameString);
        ajaxParams.put("password", Constant.userPasswordString);
        ajaxParams.put("client", Constant.SYSTEM_TYPE);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_LOGIN, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String datas = jsonObject.getString("datas");
                    jsonObject = new JSONObject(datas);
                    if (TextUtil.isNcJson(o.toString())) {
                        usernameTextView.setText(jsonObject.getString("username"));
                        Constant.userIdString = jsonObject.getString("userid");
                        Constant.userKeyString = jsonObject.getString("key");
                        Constant.userLoginSuccessBoolean = true;
                        index();
                    } else {
                        Constant.userLoginBoolean = false;
                        Constant.mSharedPreferencesEditor.putBoolean("User_Login", false);
                        ToastUtil.show(mActivity, "请重新登录");
                        ActivityUtil.start(mActivity, new Intent(mActivity, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    Constant.userLoginBoolean = false;
                    Constant.mSharedPreferencesEditor.putBoolean("User_Login", false);
                    ToastUtil.show(mActivity, "请重新登录");
                    ActivityUtil.start(mActivity, new Intent(mActivity, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                failure(0);
            }
        });

    }

    private void index() {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_USER, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String datas = jsonObject.getString("datas");
                    jsonObject = new JSONObject(datas);
                    String member_info = jsonObject.getString("member_info");
                    jsonObject = new JSONObject(member_info);
                    Constant.userAvatorString = jsonObject.getString("avator");
                    Constant.userIntegralString = jsonObject.getString("point");
                    Constant.userLevelString = jsonObject.getString("level_name");
                    String goods = "商品：" + jsonObject.getString("favorites_goods");
                    String store = "店铺：" + jsonObject.getString("favorites_store");
                    ImageLoader.getInstance().displayImage(Constant.userAvatorString, headImageView);
                    collectionGoodsTextView.setText(goods);
                    collectionStoreTextView.setText(store);
                } catch (JSONException e) {
                    e.printStackTrace();
                    failure(1);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                failure(1);
            }
        });

    }

    private void failure(final int type) {

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                switch (type) {
                    case 0:
                        login();
                        break;
                    case 1:
                        index();
                        break;
                    default:
                        break;
                }
            }
        }.start();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Constant.userLoginBoolean) {
            index();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        createControl();
        createEvent();
    }

}