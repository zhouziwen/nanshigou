package cn.yokey.nsg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.DisplayUtil;
import cn.yokey.util.FileUtil;
import cn.yokey.util.NetworkUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

/*
*
* 作者：刘龙锦 QQ：1002285057
*
* 网址：www.yokey.top || www.yokey.cn
*
* 作用：启动页加载
*
* 最后更新：2016-01-26
*
*/

public class LoadActivity extends AppCompatActivity {

    public static Activity mActivity;

    private boolean loadBoolean; //加载完成
    private boolean leaveBoolean; //看广告了
    private boolean threadBoolean; //线程控制
    private boolean versionBoolean; //版本控制

    private String advertImageString; //广告图片
    private String advertLinkString; //广告连接
    private String systemVersionString; //程序版本
    private String systemContentString; //更新内容
    private String systemDownloadString; //下载连接

    private ImageView mImageView;  //广告图片

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getAdvert();
        }
    };

    private void createControl() {

        mActivity = this;

        loadBoolean = false;
        leaveBoolean = false;
        threadBoolean = true;
        versionBoolean = false;

        advertImageString = "";
        advertLinkString = "";

        //实例化控件
        mImageView = (ImageView) findViewById(R.id.mainImageView);

        //初始化参数
        Constant.init(mActivity); //初始化Constant类

        if (NetworkUtil.isConnection(mActivity)) {
            mHandler.sendEmptyMessage(0);
        } else {
            ToastUtil.showNetworkError(mActivity);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (threadBoolean) {
                        try {
                            if (NetworkUtil.isConnection(mActivity)) {
                                mHandler.sendEmptyMessage(0);
                                threadBoolean = false;
                            } else {
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        //一些子程序
        DisplayUtil.setFullScreen(mActivity);

    }

    private void createEvent() {

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtil.isConnection(mActivity)) {
                    if (!TextUtil.isEmpty(advertLinkString)) {
                        Intent intent = new Intent(mActivity, BrowserActivity.class);
                        intent.putExtra("Link", advertLinkString);
                        ActivityUtil.start(mActivity, intent);
                        leaveBoolean = true;
                    }
                } else {
                    ActivityUtil.startSetting(mActivity, "Wifi");
                }
            }
        });

    }

    //获取显示广告
    private void getAdvert() {

        //读取历史的广告信息
        advertImageString = Constant.getAdvertImage("load");
        advertLinkString = Constant.getAdvertLink("load");
        //下载广告
        ImageLoader.getInstance().displayImage(advertImageString, mImageView);
        //获取新的广告
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("submit", "get");
        Constant.mFinalHttp.post(Constant.LINK_ANDROID_API_ADVERT, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isJson(o.toString())) {
                    Constant.mSharedPreferencesEditor.putString("Json_Advert", o.toString()).apply();
                }
                getSystem();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mActivity, "连接服务器失败!正在重试");
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        getAdvert();
                    }
                }.start();
            }
        });


    }

    //获取系统信息
    private void getSystem() {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("submit", "get");
        Constant.mFinalHttp.post(Constant.LINK_ANDROID_API_SYSTEM, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isJson(o.toString())) {
                    try {
                        JSONArray jsonArray = new JSONArray(o.toString());
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("name", jsonObject.getString("name"));
                                hashMap.put("value", jsonObject.getString("value"));
                                Constant.systemArrayList.add(hashMap);
                            }
                        }
                        systemVersionString = Constant.getSystemValue("version");
                        systemContentString = Constant.getSystemValue("content");
                        systemDownloadString = Constant.getSystemValue("download");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                checkVersion();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mActivity, "连接服务器失败!正在重试");
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        getSystem();
                    }
                }.start();
            }
        });

    }

    //检查版本可用
    private void checkVersion() {

        Constant.mFinalHttp.get(Constant.LINK_ANDROID_PUBLIC_VERSION, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (!o.toString().equals(Constant.SYSTEM_VERSION + ":1")) {
                    DialogUtil.query(mActivity, "是否升级?", "该版本已弃用!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            downloadApk();
                            versionBoolean = true;
                            DialogUtil.cancel();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityUtil.finish(mActivity);
                        }
                    });
                } else {
                    checkUpdate();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mActivity, "连接服务器失败!正在重试");
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        checkVersion();
                    }
                }.start();
            }
        });

    }

    //检查版本更新
    private void checkUpdate() {

        if (!Constant.SYSTEM_VERSION.equals(systemVersionString)) {
            DialogUtil.query(mActivity, "发现新版本!", Html.fromHtml(systemContentString), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadApk();
                    versionBoolean = false;
                    DialogUtil.cancel();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startMain();
                }
            });
        } else {

            new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    startMain();
                }
            }.start();

        }

    }

    @SuppressWarnings("all")
    private void downloadApk() {

        final Dialog dialog = new AlertDialog.Builder(mActivity).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_query);
        final TextView titleTextView = (TextView) window.findViewById(R.id.titleTextView);
        final TextView contentTextView = (TextView) window.findViewById(R.id.contentTextView);
        final TextView confirmTextView = (TextView) window.findViewById(R.id.confirmTextView);
        final TextView cancelTextView = (TextView) window.findViewById(R.id.cancelTextView);
        cancelTextView.setVisibility(View.GONE);
        confirmTextView.setText("取消");

        titleTextView.setText("正在下载");
        contentTextView.setText("已下载: 0 %");

        final String filePath = FileUtil.getDownPath() + "/nsg.apk";

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        final HttpHandler httpHandler;

        httpHandler = Constant.mFinalHttp.download(systemDownloadString, filePath, new AjaxCallBack<File>() {
            @Override
            public void onStart() {
                super.onStart();
                contentTextView.setText("已下载: 0 %");
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                int progress = 0;
                if (current != count && current != 0) {
                    progress = (int) (current / (float) count * 100);
                } else {
                    progress = 100;
                }
                String progressString = "已下载: " + progress + " %";
                contentTextView.setText(progressString);
            }

            @Override
            public void onSuccess(File t) {
                super.onSuccess(t);
                ActivityUtil.startInstallApk(mActivity, new File(filePath));
                dialog.cancel();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                dialog.cancel();
            }
        });

        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.query(mActivity, "确认您的选择", "取消下载？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!versionBoolean) {
                            httpHandler.stop();
                            dialog.cancel();
                            startMain();
                        } else {
                            ActivityUtil.finish(mActivity);
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                    }
                });
            }
        });

    }

    //跳到首页
    private void startMain() {

        loadBoolean = true;

        if (leaveBoolean) {
            return;
        }

        ActivityUtil.start(mActivity, new Intent(mActivity, MainActivity.class));
        ActivityUtil.finish(mActivity);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        leaveBoolean = false;
        if (loadBoolean) {
            startMain();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadBoolean = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        createControl();
        createEvent();
    }

}