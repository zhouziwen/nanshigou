package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.NetworkUtil;
import cn.yokey.util.ToastUtil;

public class SettingActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private CheckBox messageNotifyCheckBox;
    private CheckBox messageVoiceNotifyCheckBox;
    private CheckBox messageVibratorNotifyCheckBox;

    private TextView clearCacheTextView;
    private TextView checkNetworkTextView;
    private TextView userHelpTextView;
    private TextView aboutTextView;

    private void createControl() {

        mActivity = this;

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        messageNotifyCheckBox = (CheckBox) findViewById(R.id.messageNotifyCheckBox);
        messageVoiceNotifyCheckBox = (CheckBox) findViewById(R.id.messageVoiceNotifyCheckBox);
        messageVibratorNotifyCheckBox = (CheckBox) findViewById(R.id.messageVibratorNotifyCheckBox);

        clearCacheTextView = (TextView) findViewById(R.id.clearCacheTextView);
        checkNetworkTextView = (TextView) findViewById(R.id.checkNetworkTextView);
        userHelpTextView = (TextView) findViewById(R.id.useHelpTextView);
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);

        //初始化参数
        titleTextView.setText("系统设置");

        if (Constant.settingMessageNotifyBoolean) {
            messageNotifyCheckBox.setChecked(true);
            messageVoiceNotifyCheckBox.setVisibility(View.VISIBLE);
            messageVibratorNotifyCheckBox.setVisibility(View.VISIBLE);
        } else {
            messageVoiceNotifyCheckBox.setVisibility(View.GONE);
            messageVibratorNotifyCheckBox.setVisibility(View.GONE);
        }

        if (Constant.settingMessageVoiceNotifyBoolean) {
            messageVoiceNotifyCheckBox.setChecked(true);
        }

        if (Constant.settingMessageVibratorNotifyBoolean) {
            messageVibratorNotifyCheckBox.setChecked(true);
        }

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

            }
        });

        messageNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                Constant.settingMessageNotifyBoolean = check;
                Constant.mSharedPreferencesEditor.putBoolean("Setting_Message_Notify", check).apply();
                if (check) {
                    messageVoiceNotifyCheckBox.setVisibility(View.VISIBLE);
                    messageVibratorNotifyCheckBox.setVisibility(View.VISIBLE);
                } else {
                    messageVoiceNotifyCheckBox.setVisibility(View.GONE);
                    messageVibratorNotifyCheckBox.setVisibility(View.GONE);
                }
            }
        });

        messageVoiceNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                Constant.settingMessageVoiceNotifyBoolean = check;
                Constant.mSharedPreferencesEditor.putBoolean("Setting_Message_Voice_Notify", check).apply();
            }
        });

        messageVibratorNotifyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                Constant.settingMessageVibratorNotifyBoolean = check;
                Constant.mSharedPreferencesEditor.putBoolean("Setting_Message_Vibrator_Notify", check).apply();
            }
        });

        clearCacheTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.query(mActivity, "确认您的选择", "清空所有缓存并重新登录", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityUtil.start(mActivity, new Intent(mActivity, LoadActivity.class));
                        Constant.mSharedPreferencesEditor.clear().apply();
                        ActivityUtil.finish(MainActivity.mActivity);
                        ActivityUtil.finish(mActivity);
                        DialogUtil.cancel();
                    }
                });
            }
        });

        checkNetworkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.query(mActivity, "确认您的选择", "即将进行网络检测", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                        checkNetworkTextView.setEnabled(false);
                        checkNetworkTextView.setText("正在检测...");
                        Constant.mFinalHttp.get(Constant.LINK_MOBILE_CART, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                checkNetworkTextView.setEnabled(true);
                                checkNetworkTextView.setText("网络检测");
                                String message = "网络连接状态：正常\n网络连接类型：";
                                if (NetworkUtil.isWifiActivity(mActivity)) {
                                    message += "WIFI";
                                } else {
                                    message += "移动网络";
                                }
                                DialogUtil.query(mActivity, "检测结果", message, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DialogUtil.cancel();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                checkNetworkTextView.setEnabled(true);
                                checkNetworkTextView.setText("网络检测");
                                ToastUtil.show(mActivity, "网络连接失败!");
                            }
                        });
                    }
                });
            }
        });

        userHelpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, BrowserActivity.class);
                intent.putExtra("Link", Constant.LINK_ANDROID_PUBLIC_HELP);
                ActivityUtil.start(mActivity, intent);
            }
        });

        aboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, BrowserActivity.class);
                intent.putExtra("Link", Constant.LINK_ANDROID_PUBLIC_ABOUT);
                ActivityUtil.start(mActivity, intent);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        createControl();
        createEvent();
    }

}