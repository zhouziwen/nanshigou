package cn.yokey.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class YoKeyWebView extends WebView {

    private OnScrollChangedCallback mOnScrollChangedCallback;

    public YoKeyWebView(final Context context) {
        super(context);
    }

    public YoKeyWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public YoKeyWebView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldL, final int oldT) {
        super.onScrollChanged(l, t, oldL, oldT);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }

}