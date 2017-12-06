package cn.yokey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.GoodsSearchActivity;
import cn.yokey.nsg.R;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.TextUtil;

public class Class2ListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public Class2ListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (mArrayList != null) {
            return mArrayList.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(mActivity, R.layout.item_list_class2, null);
            holder.mTabLayout = (TabLayout) convertView.findViewById(R.id.mainTabLayout);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.contentTextView = new TextView[51];
            for (int i = 0; i < holder.contentTextView.length; i++) {
                holder.contentTextView[i] = (TextView) convertView.findViewById(R.id.content1TextView + i);
            }
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.titleTextView.setText(hashMap.get("gc_name"));

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, GoodsSearchActivity.class);
                intent.putExtra("model", "class");
                intent.putExtra("gc_id", hashMap.get("gc_id"));
                ActivityUtil.start(mActivity, intent);
            }
        });

        for (int i = 0; i < holder.contentTextView.length; i++) {
            holder.contentTextView[i].setText("");
        }

        if (hashMap.get("gc_class3").equals("null")) {
            Constant.mFinalHttp.get(Constant.LINK_MOBILE_CLASS + "&gc_id=" + hashMap.get("gc_id"), new AjaxCallBack<Object>() {
                @Override
                public void onSuccess(Object o) {
                    super.onSuccess(o);
                    if (TextUtil.isNcJson(o.toString())) {
                        try {
                            JSONObject jsonObject = new JSONObject(o.toString());
                            String datas = jsonObject.getString("datas");
                            jsonObject = new JSONObject(datas);
                            hashMap.put("gc_class3", jsonObject.getString("class_list"));
                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            try {
                JSONArray jsonArray = new JSONArray(hashMap.get("gc_class3"));
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        final String gc_id = jsonObject.getString("gc_id");
                        holder.contentTextView[i].setText(jsonObject.getString("gc_name"));
                        holder.contentTextView[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mActivity, GoodsSearchActivity.class);
                                intent.putExtra("model", "class");
                                intent.putExtra("gc_id", gc_id);
                                ActivityUtil.start(mActivity, intent);
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < holder.contentTextView.length; i++) {
            if (holder.contentTextView[i].getText().toString().length() == 0) {
                holder.contentTextView[i].setVisibility(View.GONE);
            } else {
                holder.contentTextView[i].setVisibility(View.VISIBLE);
            }
        }

        return convertView;

    }

    public class Holder {

        public TabLayout mTabLayout;
        public TextView titleTextView;
        public TextView[] contentTextView;

    }

}