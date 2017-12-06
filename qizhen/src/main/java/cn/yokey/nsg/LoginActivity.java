package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView loginTextView;

    private TextView regTextView;
    private TextView backTextView;

    private void createControl() {

        mActivity = this;

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginTextView = (TextView) findViewById(R.id.loginTextView);

        regTextView = (TextView) findViewById(R.id.regTextView);
        backTextView = (TextView) findViewById(R.id.backTextView);

        //初始化参数
        titleTextView.setText("用户登录");
        rightImageView.setVisibility(View.GONE);
        usernameEditText.setText(Constant.userUsernameString);


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

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        regTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.start(mActivity, new Intent(mActivity, RegActivity.class));
            }
        });

        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, BrowserActivity.class);
                intent.putExtra("Link", Constant.LINK_WAP_FIND_PASSWORD);
                ActivityUtil.start(mActivity, intent);
            }
        });

    }

    private void login() {

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (TextUtil.isEmpty(username)) {
            ToastUtil.show(mActivity, "用户名不能为空!");
            return;
        }

        if (TextUtil.isEmpty(password)) {
            ToastUtil.show(mActivity, "密码不能为空！");
            return;
        }

        loginTextView.setEnabled(false);
        loginTextView.setText("登录中...");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("username", username);
        ajaxParams.put("password", password);
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
                        Constant.userLoginBoolean = true;
                        Constant.userUsernameString = username;
                        Constant.userPasswordString = password;
                        Constant.mSharedPreferencesEditor.putBoolean("User_Login", true);
                        Constant.mSharedPreferencesEditor.putString("User_Username", username);
                        Constant.mSharedPreferencesEditor.putString("User_Password", password);
                        Constant.mSharedPreferencesEditor.apply();
                        Constant.userIdString = jsonObject.getString("userid");
                        Constant.userKeyString = jsonObject.getString("key");
                        ToastUtil.show(mActivity, "登录成功!");
                        ActivityUtil.start(mActivity, new Intent(mActivity, LoadActivity.class));
                        ActivityUtil.finish(MainActivity.mActivity);
                        ActivityUtil.finish(mActivity);
                    } else {
                        ToastUtil.show(mActivity, jsonObject.getString("error"));
                        failure();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.show(mActivity, "解析用户数据失败");
                    failure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mActivity, "连接服务器失败");
                failure();
            }
        });

    }

    private void failure() {

        loginTextView.setEnabled(true);
        loginTextView.setText("登 录");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createControl();
        createEvent();
    }

}