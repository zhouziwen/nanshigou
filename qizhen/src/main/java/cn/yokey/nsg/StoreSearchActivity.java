package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import cn.yokey.adapter.SearchListAdapter;
import cn.yokey.util.ActivityUtil;

public class StoreSearchActivity extends AppCompatActivity {

    public static Activity mActivity;

    private String keywordString;

    private ImageView leftImageView;
    private EditText keywordEditText;
    private ImageView rightImageView;

    private Spinner mSpinner;
    private ListView mListView;
    private TextView mTextView;
    private SearchListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<HashMap<String, String>> mArrayList;

    private void createControl() {

        mActivity = this;

        keywordString = mActivity.getIntent().getStringExtra("keyword");

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        keywordEditText = (EditText) findViewById(R.id.keywordEditText);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        mSpinner = (Spinner) findViewById(R.id.typeSpinner);
        mListView = (ListView) findViewById(R.id.mainListView);
        mTextView = (TextView) findViewById(R.id.statusTextView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

        //初始化参数
        mSpinner.setVisibility(View.GONE);
        keywordEditText.setText(keywordString);

        //一些子程序
        getJson();

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnActivity();
            }
        });

        keywordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywordString = keywordEditText.getText().toString();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getJson();
                    }
                }, 1000);
            }
        });

    }

    private void getJson() {

    }

    private void returnActivity() {

        if (keywordEditText.getText().length() != 0) {
            keywordEditText.setText("");
        } else {
            ActivityUtil.finish(mActivity);
        }

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
        setContentView(R.layout.activity_store_search);
        createControl();
        createEvent();
    }

}
