package cn.yokey.nsg;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;

import net.tsz.afinal.http.AjaxParams;

import cn.yokey.system.Constant;

/*
*
* 作者：刘龙锦 QQ：1002285057
*
* 网址：www.yokey.top || www.yokey.cn
*
* 作用：主界面
*
* 最后更新：2016-01-26
*
*/

@SuppressWarnings("all")
public class MainActivity extends ActivityGroup {

    public static Activity mActivity;

    public static TabHost mTabHost;
    public static RadioButton[] mRadioButton;

    private void createControl() {

        mActivity = this;

        //实例化控件
        mTabHost = (TabHost) findViewById(R.id.mainTabHost);
        mRadioButton = new RadioButton[4];
        mRadioButton[0] = (RadioButton) findViewById(R.id.homeRadioButton);
        mRadioButton[1] = (RadioButton) findViewById(R.id.classRadioButton);
        mRadioButton[2] = (RadioButton) findViewById(R.id.cartRadioButton);
        mRadioButton[3] = (RadioButton) findViewById(R.id.userRadioButton);

        //初始化参数
        mTabHost.setup(this.getLocalActivityManager());
        mTabHost.addTab(mTabHost.newTabSpec("Home").setIndicator("Home").setContent(new Intent(mActivity, HomeActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Class").setIndicator("Class").setContent(new Intent(mActivity, ClassActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("Cart").setIndicator("Cart").setContent(new Intent(mActivity, CartActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("User").setIndicator("User").setContent(new Intent(mActivity, UserActivity.class)));
        mTabHost.setCurrentTab(3);
        mTabHost.setCurrentTab(1);
        mTabHost.setCurrentTab(0);

    }

    private void createEvent() {

        for (int i = 0; i < mRadioButton.length; i++) {
            final int position = i;
            mRadioButton[position].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTab(position);
                }
            });
        }

    }

    public static void setTab(int pos) {

        mTabHost.setCurrentTab(pos);

        switch (pos) {
            case 0:
                mRadioButton[0].setTextColor(ContextCompat.getColor(mActivity, R.color.main));
                mRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_home_press), null, null);
                mRadioButton[1].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_class), null, null);
                mRadioButton[2].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_cart), null, null);
                mRadioButton[3].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[3].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_user), null, null);
                break;
            case 1:
                mRadioButton[0].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_home), null, null);
                mRadioButton[1].setTextColor(ContextCompat.getColor(mActivity, R.color.main));
                mRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_class_press), null, null);
                mRadioButton[2].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_cart), null, null);
                mRadioButton[3].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[3].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_user), null, null);
                break;
            case 2:
                mRadioButton[0].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_home), null, null);
                mRadioButton[1].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_class), null, null);
                mRadioButton[2].setTextColor(ContextCompat.getColor(mActivity, R.color.main));
                mRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_cart_press), null, null);
                mRadioButton[3].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[3].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_user), null, null);
                break;
            case 3:
                mRadioButton[0].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[0].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_home), null, null);
                mRadioButton[1].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[1].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_class), null, null);
                mRadioButton[2].setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
                mRadioButton[2].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_cart), null, null);
                mRadioButton[3].setTextColor(ContextCompat.getColor(mActivity, R.color.main));
                mRadioButton[3].setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(mActivity, R.mipmap.ic_nav_user_press), null, null);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            AjaxParams ajaxParams = new AjaxParams();
            ajaxParams.put("key", Constant.userKeyString);
            ajaxParams.put("client", Constant.SYSTEM_TYPE);
            ajaxParams.put("username", Constant.userUsernameString);
            Constant.mFinalHttp.post(Constant.LINK_MOBILE_LOGOUT, ajaxParams, null);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createControl();
        createEvent();
    }

}
