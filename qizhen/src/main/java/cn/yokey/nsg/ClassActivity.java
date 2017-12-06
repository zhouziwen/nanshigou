package cn.yokey.nsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.adapter.Class1ListAdapter;
import cn.yokey.adapter.Class2ListAdapter;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;

public class ClassActivity extends AppCompatActivity {

    public static Activity mActivity;

    private ImageView leftImageView;
    private EditText titleEditText;
    private ImageView rightImageView;

    private ListView class1ListView;
    private Class1ListAdapter class1Adapter;
    private ArrayList<HashMap<String, String>> class1ArrayList;

    private ListView class2ListView;
    private Class2ListAdapter class2Adapter;
    private ArrayList<HashMap<String, String>> class2ArrayList;

    private void createControl() {

        mActivity = this;

        //实例化控件
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        class1ListView = (ListView) findViewById(R.id.class1ListView);
        class2ListView = (ListView) findViewById(R.id.class2ListView);

        //初始化参数
        class1ArrayList = new ArrayList<>();
        class1Adapter = new Class1ListAdapter(mActivity, class1ArrayList);
        class1ListView.setAdapter(class1Adapter);

        class2ArrayList = new ArrayList<>();
        class2Adapter = new Class2ListAdapter(mActivity, class2ArrayList);
        class2ListView.setAdapter(class2Adapter);

        //一些子程序
        getClass1Json();

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.start(mActivity, new Intent(mActivity, ScanActivity.class));
            }
        });

        titleEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtil.start(mActivity, new Intent(mActivity, SearchActivity.class));
            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        class1ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                getClass2Json(class1ArrayList.get(pos).get("gc_id"));
                for (int i = 0; i < class1ArrayList.size(); i++) {
                    if (i == pos) {
                        class1ArrayList.get(i).put("click", "1");
                    } else {
                        class1ArrayList.get(i).put("click", "0");
                    }
                }
                class1Adapter.notifyDataSetChanged();
            }
        });

    }

    private void getClass1Json() {

        Constant.mFinalHttp.get(Constant.LINK_MOBILE_CLASS, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        class1ArrayList.clear();
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        String class_list = jsonObject.getString("class_list");
                        JSONArray jsonArray = new JSONArray(class_list);
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = (JSONObject) jsonArray.get(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("gc_id", jsonObject.getString("gc_id"));
                                hashMap.put("gc_name", jsonObject.getString("gc_name"));
                                hashMap.put("image", jsonObject.getString("image"));
                                hashMap.put("click", "0");
                                class1ArrayList.add(hashMap);
                            }
                        }
                        getClass2Json(class1ArrayList.get(0).get("gc_id"));
                        class1ArrayList.get(0).put("click", "1");
                        class1Adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showClass1Failure();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                showClass1Failure();
            }
        });

    }

    private void showClass1Failure() {

        DialogUtil.query(mActivity, "是否重试?", "读取分类数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getClass1Json();
            }
        });

    }

    private void getClass2Json(final String gc_id) {

        Constant.mFinalHttp.get(Constant.LINK_MOBILE_CLASS + "&gc_id=" + gc_id, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        class2ArrayList.clear();
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        String class_list = jsonObject.getString("class_list");
                        JSONArray jsonArray = new JSONArray(class_list);
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = (JSONObject) jsonArray.get(i);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("gc_id", jsonObject.getString("gc_id"));
                                hashMap.put("gc_name", jsonObject.getString("gc_name"));
                                hashMap.put("gc_class3", "null");
                                class2ArrayList.add(hashMap);
                            }
                        }
                        class2Adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showClass2Failure(gc_id);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                showClass2Failure(gc_id);
            }
        });

    }

    private void showClass2Failure(final String gc_id) {

        DialogUtil.query(mActivity, "是否重试?", "读取分类数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getClass2Json(gc_id);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        createControl();
        createEvent();
    }

}