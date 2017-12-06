package cn.yokey.system;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

import cn.yokey.adapter.BasePagerAdapter;
import cn.yokey.nsg.R;

public class Android {

    public static void setTabLayout(Activity activity, TabLayout mTabLayout, BasePagerAdapter mAdapter, ViewPager mViewPager) {

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(activity, R.color.main));
        mTabLayout.setTabTextColors(ContextCompat.getColor(activity, R.color.greyAdd), ContextCompat.getColor(activity, R.color.main));
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.black));
        }
    }

    public static void setWebView(WebView webView) {

        if (Constant.settingIntelligentImageBoolean) {
            webView.getSettings().setBlockNetworkImage(true);
        } else {
            webView.getSettings().setBlockNetworkImage(false);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);

    }

}
