package cn.yokey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.OrderRefundDetailedActivity;
import cn.yokey.nsg.R;
import cn.yokey.util.ActivityUtil;

public class OrderRefundListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public OrderRefundListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_order_refund, null);
            holder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
            holder.buttonRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.buttonRelativeLayout);
            holder.storeTextView = (TextView) convertView.findViewById(R.id.storeTextView);
            holder.statusTextView = (TextView) convertView.findViewById(R.id.statusTextView);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.mainImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.info1TextView = (TextView) convertView.findViewById(R.id.info1TextView);
            holder.info2TextView = (TextView) convertView.findViewById(R.id.info2TextView);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            holder.detailedButton = (Button) convertView.findViewById(R.id.detailedButton);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);
        final HashMap<String, String> goodsHashMap = mArrayList.get(position);

        try {
            JSONArray jsonArray = new JSONArray(hashMap.get("goods_list"));
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            goodsHashMap.put("goods_id", jsonObject.getString("goods_id"));
            goodsHashMap.put("goods_name", jsonObject.getString("goods_name"));
            goodsHashMap.put("goods_img_360", jsonObject.getString("goods_img_360"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.storeTextView.setText(hashMap.get("store_name"));
        holder.statusTextView.setText(hashMap.get("admin_state"));
        ImageLoader.getInstance().displayImage(goodsHashMap.get("goods_img_360"), holder.mImageView);
        holder.titleTextView.setText(goodsHashMap.get("goods_name"));
        final String info1 = " <font color='#FF5001'>" + hashMap.get("refund_amount") + "</font>";
        holder.info1TextView.setText(Html.fromHtml(info1));
        String info2 = "合计 <font color='#FF5001'>" + hashMap.get("refund_amount") + "</font> 元";
        holder.info2TextView.setText(Html.fromHtml(info2));
        holder.timeTextView.setText(hashMap.get("add_time"));

        holder.detailedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, OrderRefundDetailedActivity.class);
                intent.putExtra("refund_id", hashMap.get("refund_id"));
                intent.putExtra("refund_amount", hashMap.get("refund_amount"));
                intent.putExtra("add_time", hashMap.get("add_time"));
                intent.putExtra("admin_state", hashMap.get("admin_state"));
                intent.putExtra("store_id", hashMap.get("store_id"));
                intent.putExtra("store_name", hashMap.get("store_name"));
                intent.putExtra("goods_id", goodsHashMap.get("goods_id"));
                intent.putExtra("goods_name", goodsHashMap.get("goods_name"));
                intent.putExtra("goods_img_360", goodsHashMap.get("goods_img_360"));
                ActivityUtil.start(mActivity, intent);
            }
        });

        return convertView;

    }

    public class Holder {

        public RelativeLayout mRelativeLayout;
        public RelativeLayout buttonRelativeLayout;
        public TextView storeTextView;
        public TextView statusTextView;
        public ImageView mImageView;
        public TextView titleTextView;
        public TextView info1TextView;
        public TextView info2TextView;
        public TextView timeTextView;
        public Button detailedButton;

    }

}