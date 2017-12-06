package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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
import cn.yokey.util.DialogUtil;
import cn.yokey.util.LogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class RegActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordRepeatEditText;
    private EditText emailEditText;
    private TextView regTextView;

    private void createControl() {

        mActivity = this;

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordRepeatEditText = (EditText) findViewById(R.id.passwordRepeatEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        regTextView = (TextView) findViewById(R.id.regTextView);

        //初始化参数
        titleTextView.setText("用户注册");
        rightImageView.setVisibility(View.GONE);

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivity();
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

        regTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordRepeat = passwordRepeatEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (TextUtil.isEmpty(username)) {
                    ToastUtil.show(mActivity, "用户名不能为空");
                    return;
                }

                if (TextUtil.isEmpty(password)) {
                    ToastUtil.show(mActivity, "密码不能为空");
                    return;
                }

                if (TextUtil.isEmpty(passwordRepeat)) {
                    ToastUtil.show(mActivity, "请再次输入密码");
                    return;
                }

                if (TextUtil.isEmpty(email)) {
                    ToastUtil.show(mActivity, "请输入邮箱地址");
                    return;
                }

                if (!password.equals(passwordRepeat)) {
                    ToastUtil.show(mActivity, "密码不一样");
                    return;
                }

                if (!password.equals(passwordRepeat)) {
                    ToastUtil.show(mActivity, "密码不一样");
                    return;
                }

                if (!TextUtil.isEmailAddress(email)) {
                    ToastUtil.show(mActivity, "邮箱格式不正确");
                    return;
                }

                regTextView.setEnabled(false);
                regTextView.setText("正在注册...");
                AjaxParams ajaxParams = new AjaxParams();
                ajaxParams.put("username", username);
                ajaxParams.put("password", password);
                ajaxParams.put("password_confirm", password);
                ajaxParams.put("email", email);
                ajaxParams.put("client", Constant.SYSTEM_TYPE);
                Constant.mFinalHttp.post(Constant.LINK_MOBILE_REG, ajaxParams, new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        if (TextUtil.isNcJson(o.toString())) {
                            ToastUtil.show(mActivity, "注册成功");
                            ActivityUtil.finish(mActivity);
                        } else {
                            try {
                                regTextView.setText("注 册");
                                regTextView.setEnabled(true);
                                JSONObject jsonObject = new JSONObject(o.toString());
                                String datas = jsonObject.getString("datas");
                                jsonObject = new JSONObject(datas);
                                ToastUtil.show(mActivity, jsonObject.getString("error"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        ToastUtil.show(mActivity, "注册失败,请重试");
                        regTextView.setText("注 册");
                        regTextView.setEnabled(true);
                    }
                });

            }
        });

    }

    private void returnActivity() {

        DialogUtil.query(mActivity, "确认您的选择", "返回将清空您正在输入的内容", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                ActivityUtil.finish(mActivity);
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            returnActivity();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        createControl();
        createEvent();
    }

}
