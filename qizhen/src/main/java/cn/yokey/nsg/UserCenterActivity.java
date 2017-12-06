package cn.yokey.nsg;

import android.app.Activity;
import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.adapter.AddressListAdapter;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class UserCenterActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private TextView exitTextView;

    private void createControl() {

        mActivity = this;

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        exitTextView = (TextView) findViewById(R.id.exitTextView);

        //初始化参数
        titleTextView.setText("个人中心");
        rightImageView.setVisibility(View.GONE);

        //一些子程序

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

        exitTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.query(mActivity, "确认您的选择", "退出登录?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                        AjaxParams ajaxParams = new AjaxParams();
                        ajaxParams.put("key", Constant.userKeyString);
                        ajaxParams.put("client", Constant.SYSTEM_TYPE);
                        ajaxParams.put("username", Constant.userUsernameString);
                        Constant.mFinalHttp.post(Constant.LINK_MOBILE_LOGOUT, ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                if (o.toString().contains("1")) {
                                    ToastUtil.show(mActivity, "退出成功");
                                    Constant.mSharedPreferencesEditor.putBoolean("User_Login", false);
                                    Constant.mSharedPreferencesEditor.putString("User_Password", "");
                                    Constant.mSharedPreferencesEditor.apply();
                                    ActivityUtil.start(mActivity, new Intent(mActivity, LoadActivity.class));
                                    ActivityUtil.finish(mActivity);
                                    ActivityUtil.finish(MainActivity.mActivity);
                                } else {
                                    ToastUtil.show(mActivity, "退出失败");
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                ToastUtil.show(mActivity, "退出失败");
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        createControl();
        createEvent();
    }

}
