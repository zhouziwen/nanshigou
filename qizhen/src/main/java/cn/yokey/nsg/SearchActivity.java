package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.adapter.SearchListAdapter;
import cn.yokey.system.Android;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class SearchActivity extends AppCompatActivity {

    public static Activity mActivity;

    private String typeString;
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
    private ArrayList<HashMap<String, String>> tempArrayList;

    private void createControl() {

        mActivity = this;

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        keywordEditText = (EditText) findViewById(R.id.keywordEditText);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        mSpinner = (Spinner) findViewById(R.id.typeSpinner);
        mListView = (ListView) findViewById(R.id.mainListView);
        mTextView = (TextView) findViewById(R.id.statusTextView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mainSwipeRefreshLayout);

        //初始化参数
        mTextView.setText("清除记录");
        mTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.red));

        mArrayList = new ArrayList<>();
        tempArrayList = new ArrayList<>();
        mAdapter = new SearchListAdapter(mActivity, tempArrayList);
        mListView.setAdapter(mAdapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"宝贝", "店铺"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

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
                if (!TextUtil.isEmpty(keywordString)) {
                    startActivity(typeString, keywordString);
                    saveJson();
                } else {
                    ToastUtil.show(mActivity, "关键字不能为空");
                }
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

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(14.0f);
                typeString = textView.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.query(mActivity, "确认您的选择", "清空搜索记录", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mArrayList.clear();
                        tempArrayList.clear();
                        DialogUtil.cancel();
                        mAdapter.notifyDataSetChanged();
                        mTextView.setVisibility(View.GONE);
                        Constant.mSharedPreferencesEditor.putString("Json_Search_List", "").apply();
                    }
                });
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(tempArrayList.get(i).get("type"), tempArrayList.get(i).get("title"));
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                DialogUtil.query(mActivity, "确认您的选择", "删除这条记录", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                        tempArrayList.remove(i);
                        if (tempArrayList.size() == 0) {
                            Constant.mSharedPreferencesEditor.putString("Json_Search_List", "").apply();
                        } else {
                            mArrayList.clear();
                            for (int j = tempArrayList.size() - 1; j >= 0; j--) {
                                mArrayList.add(tempArrayList.get(j));
                            }
                            Constant.mSharedPreferencesEditor.putString("Json_Search_List", com.alibaba.fastjson.JSONArray.toJSONString(mArrayList)).apply();
                        }
                        getJson();
                    }
                });
                return false;
            }
        });

    }

    private void getJson() {

        String json = Constant.mSharedPreferences.getString("Json_Search_List", "");

        if (json.isEmpty() || json.equals("[]")) {
            mTextView.setVisibility(View.GONE);
        } else {
            try {
                mArrayList.clear();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("title", jsonObject.getString("title"));
                    hashMap.put("type", jsonObject.getString("type"));
                    mArrayList.add(hashMap);
                }
                tempArrayList.clear();
                for (int i = mArrayList.size() - 1; i >= 0; i--) {
                    tempArrayList.add(mArrayList.get(i));
                }
                mTextView.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();

    }

    private void saveJson() {

        if (mArrayList.size() != 0) {
            for (int i = 0; i < mArrayList.size(); i++) {
                String title = mArrayList.get(i).get("title");
                String type = mArrayList.get(i).get("type");
                if (title.equals(keywordString) && type.equals(typeString)) {
                    mArrayList.remove(i);
                    break;
                }
            }
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("title", keywordString);
        hashMap.put("type", typeString);
        mArrayList.add(hashMap);

        Constant.mSharedPreferencesEditor.putString("Json_Search_List", com.alibaba.fastjson.JSONArray.toJSONString(mArrayList)).apply();
        keywordEditText.setText("");
        keywordString = "";
        getJson();

    }

    private void returnActivity() {

        if (keywordEditText.getText().length() != 0) {
            keywordEditText.setText("");
        } else {
            ActivityUtil.finish(mActivity);
        }

    }

    private void startActivity(String type, String content) {

        Intent intent = new Intent(mActivity, GoodsSearchActivity.class);

        if (type.equals("宝贝")) {
            intent = new Intent(mActivity, GoodsSearchActivity.class);
        }

        if (type.equals("店铺")) {
            intent = new Intent(mActivity, StoreSearchActivity.class);
        }

        intent.putExtra("model", "search");
        intent.putExtra("keyword", content);
        ActivityUtil.start(mActivity, intent);

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
    protected void onDestroy() {
        super.onDestroy();
        Android.hideKeyboard(mActivity, rightImageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        createControl();
        createEvent();
    }

}