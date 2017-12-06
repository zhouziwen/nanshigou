package cn.yokey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.GoodsDetailActivity;
import cn.yokey.nsg.R;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;

public class GoodsBrowsListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public GoodsBrowsListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_goods, null);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
            holder.horLinearLayout = (LinearLayout) convertView.findViewById(R.id.horLinearLayout);
            holder.verRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.verRelativeLayout);
            holder.verImageView = (ImageView) convertView.findViewById(R.id.verImageView);
            holder.verBorderImageView = (ImageView) convertView.findViewById(R.id.verBorderImageView);
            holder.verTitleTextView = (TextView) convertView.findViewById(R.id.verTitleTextView);
            holder.verTimeTextView = (TextView) convertView.findViewById(R.id.verTimeTextView);
            holder.verPromotionPriceTextView = (TextView) convertView.findViewById(R.id.verPricePromotionTextView);
            holder.verPriceTextView = (TextView) convertView.findViewById(R.id.verPriceTextView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.horLinearLayout.setVisibility(View.GONE);
        holder.verBorderImageView.setVisibility(View.GONE);

        ImageLoader.getInstance().displayImage(hashMap.get("goods_image_url"), holder.verImageView);
        holder.verTitleTextView.setText(hashMap.get("goods_name"));
        holder.verPromotionPriceTextView.setText(hashMap.get("goods_promotion_price"));
        holder.verPriceTextView.setText(hashMap.get("goods_marketprice"));
        holder.verPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        holder.verRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                intent.putExtra("id", hashMap.get("goods_id"));
                ActivityUtil.start(mActivity, intent);
            }
        });

        return convertView;

    }

    public class Holder {

        public LinearLayout mLinearLayout;

        public LinearLayout horLinearLayout;
        public RelativeLayout verRelativeLayout;
        public ImageView verImageView;
        public ImageView verBorderImageView;
        public TextView verTitleTextView;
        public TextView verTimeTextView;
        public TextView verPromotionPriceTextView;
        public TextView verPriceTextView;

    }

}