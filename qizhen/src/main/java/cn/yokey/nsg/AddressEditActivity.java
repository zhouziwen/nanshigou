package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class AddressEditActivity extends AppCompatActivity {

    public static Activity mActivity;

    private String cityString;
    private String areaString;
    private String cityIdString;
    private String areaIdString;
    private String provinceString;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private Spinner provinceSpinner;
    private Spinner citySpinner;
    private Spinner areaSpinner;

    private EditText addressEditText;
    private EditText mobilePhoneEditText;
    private EditText trueNameEditText;

    private CheckBox defaultCheckBox;

    private Vector<String> provinceIdVector;
    private Vector<String> provinceNameVector;
    private Vector<String> cityIdVector;
    private Vector<String> cityNameVector;
    private Vector<String> areaIdVector;
    private Vector<String> areaNameVector;

    private void createControl() {

        mActivity = this;

        //控件实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        provinceSpinner = (Spinner) findViewById(R.id.provinceSpinner);
        citySpinner = (Spinner) findViewById(R.id.citySpinner);
        areaSpinner = (Spinner) findViewById(R.id.areaSpinner);

        addressEditText = (EditText) findViewById(R.id.addressEditText);
        mobilePhoneEditText = (EditText) findViewById(R.id.mobilePhoneEditText);
        trueNameEditText = (EditText) findViewById(R.id.trueNameEditText);

        defaultCheckBox = (CheckBox) findViewById(R.id.defaultCheckBox);

        //初始化参数
        titleTextView.setText("修改收货地址");
        rightImageView.setImageResource(R.mipmap.ic_action_write);

        //一些子程序
        getProvinceJson();
        setValue();

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
                if (addressEditText.getText().toString().isEmpty()) {
                    ToastUtil.show(mActivity, "请输入详细地址");
                    return;
                }
                if (mobilePhoneEditText.getText().toString().isEmpty()) {
                    ToastUtil.show(mActivity, "请输入手机号码");
                    return;
                }
                if (trueNameEditText.getText().toString().isEmpty()) {
                    ToastUtil.show(mActivity, "请输入真实姓名");
                    return;
                }
                editAddress();
            }
        });

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(14.0f);
                getCityJson(provinceIdVector.get(i));
                provinceString = provinceNameVector.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(14.0f);
                getAreaJson(cityIdVector.get(i));
                cityIdString = cityIdVector.get(i);
                cityString = cityNameVector.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(14.0f);
                areaIdString = areaIdVector.get(i);
                areaString = areaNameVector.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getProvinceJson() {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_AREA, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        parseProvince(jsonObject.getString("area_list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    failureProvince();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                failureProvince();
            }
        });

    }

    private void failureProvince() {

        DialogUtil.query(mActivity, "是否重试", "读取省份数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getProvinceJson();
            }
        });

    }

    private void parseProvince(String json) {

        try {
            provinceIdVector = new Vector<>();
            provinceNameVector = new Vector<>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                provinceIdVector.add(jsonObject.getString("area_id"));
                provinceNameVector.add(jsonObject.getString("area_name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceNameVector);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            provinceSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getCityJson(final String area_id) {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        ajaxParams.put("area_id", area_id);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_AREA, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        parseCity(jsonObject.getString("area_list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    failureCity(area_id);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                failureCity(area_id);
            }
        });

    }

    private void failureCity(final String area_id) {

        DialogUtil.query(mActivity, "是否重试", "读取城市数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getCityJson(area_id);
            }
        });

    }

    private void parseCity(String json) {

        try {
            cityIdVector = new Vector<>();
            cityNameVector = new Vector<>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                cityIdVector.add(jsonObject.getString("area_id"));
                cityNameVector.add(jsonObject.getString("area_name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNameVector);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            citySpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getAreaJson(final String area_id) {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        ajaxParams.put("area_id", area_id);
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_AREA, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        jsonObject = new JSONObject(datas);
                        parseArea(jsonObject.getString("area_list"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    failureArea(area_id);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                failureArea(area_id);
            }
        });

    }

    private void failureArea(final String area_id) {

        DialogUtil.query(mActivity, "是否重试", "读取地区数据失败", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.cancel();
                getAreaJson(area_id);
            }
        });

    }

    private void parseArea(String json) {

        try {
            areaIdVector = new Vector<>();
            areaNameVector = new Vector<>();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                areaIdVector.add(jsonObject.getString("area_id"));
                areaNameVector.add(jsonObject.getString("area_name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areaNameVector);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            areaSpinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setValue() {

        addressEditText.setText(AddressActivity.mHashMap.get("address"));
        addressEditText.setSelection(addressEditText.getText().length());
        mobilePhoneEditText.setText(AddressActivity.mHashMap.get("mob_phone"));
        trueNameEditText.setText(AddressActivity.mHashMap.get("true_name"));
        if (AddressActivity.mHashMap.get("is_default").equals("1")) {
            defaultCheckBox.setChecked(true);
        } else {
            defaultCheckBox.setChecked(false);
        }

    }

    private void editAddress() {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("key", Constant.userKeyString);
        ajaxParams.put("address_id", AddressActivity.mHashMap.get("address_id"));
        ajaxParams.put("true_name", trueNameEditText.getText().toString());
        ajaxParams.put("mob_phone", mobilePhoneEditText.getText().toString());
        ajaxParams.put("city_id", cityIdString);
        ajaxParams.put("area_id", areaIdString);
        ajaxParams.put("address", addressEditText.getText().toString());
        ajaxParams.put("area_info", provinceString + " " + cityString + " " + areaString);
        if (defaultCheckBox.isChecked()) {
            ajaxParams.put("is_default", "1");
        } else {
            ajaxParams.put("is_default", "0");
        }
        Constant.mFinalHttp.post(Constant.LINK_MOBILE_ADDRESS_EDIT, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                if (TextUtil.isNcJson(o.toString())) {
                    try {
                        JSONObject jsonObject = new JSONObject(o.toString());
                        String datas = jsonObject.getString("datas");
                        if (datas.contains("1")) {
                            ToastUtil.show(mActivity, "修改成功");
                            mActivity.setResult(RESULT_OK);
                            ActivityUtil.finish(mActivity);
                        } else {
                            ToastUtil.show(mActivity, "修改失败，请重试");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtil.show(mActivity, "修改失败，请重试");
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mActivity, "修改失败，请重试");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        createControl();
        createEvent();
    }

}
