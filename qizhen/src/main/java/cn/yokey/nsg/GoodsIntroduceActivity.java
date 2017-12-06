package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import cn.yokey.system.Android;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.view.YoKeyWebView;

public class GoodsIntroduceActivity extends Activity {

    public static Activity mActivity;

    private String linkString;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private YoKeyWebView mWebView;
    private ProgressBar mProgressBar;

    private void createControl() {

        mActivity = this;

        linkString = mActivity.getIntent().getStringExtra("Link");
        if (!linkString.contains("http://")) {
            linkString = "http://" + linkString;
        }

        /*实例化*/
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        mWebView = (YoKeyWebView) findViewById(R.id.mainWebView);
        mProgressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        /*赋值*/
        rightImageView.setVisibility(View.GONE);
        titleTextView.setText("商品详细");
        Android.setWebView(mWebView);
        getHtml();

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
            public void onClick(View view) {

            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                linkString = url;
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleTextView.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

    }

    private void getHtml() {

        Constant.mFinalHttp.get(linkString, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                mWebView.loadDataWithBaseURL(null, TextUtil.encodeHtml(o.toString()), "text/html", "UTF-8", null);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.query(mActivity, "是否重试?", "读取商品详细信息失败", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                        getHtml();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                        ActivityUtil.finish(mActivity);
                    }
                });
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_introduce);
        createControl();
        createEvent();
    }

}