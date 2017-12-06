package cn.yokey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.GoodsDetailActivity;
import cn.yokey.nsg.R;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.TextUtil;

public class GoodsSearchListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public GoodsSearchListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            holder.horRelativeLayout = new RelativeLayout[2];
            holder.horRelativeLayout[0] = (RelativeLayout) convertView.findViewById(R.id.hor1RelativeLayout);
            holder.horRelativeLayout[1] = (RelativeLayout) convertView.findViewById(R.id.hor2RelativeLayout);
            holder.horImageView = new ImageView[2];
            holder.horImageView[0] = (ImageView) convertView.findViewById(R.id.hor1ImageView);
            holder.horImageView[1] = (ImageView) convertView.findViewById(R.id.hor2ImageView);
            holder.horBorderImageView = new ImageView[2];
            holder.horBorderImageView[0] = (ImageView) convertView.findViewById(R.id.hor1BorderImageView);
            holder.horBorderImageView[1] = (ImageView) convertView.findViewById(R.id.hor2BorderImageView);
            holder.horTitleTextViews = new TextView[2];
            holder.horTitleTextViews[0] = (TextView) convertView.findViewById(R.id.hor1TitleTextView);
            holder.horTitleTextViews[1] = (TextView) convertView.findViewById(R.id.hor2TitleTextView);
            holder.horPromotionPriceTextViews = new TextView[2];
            holder.horPromotionPriceTextViews[0] = (TextView) convertView.findViewById(R.id.hor1PromotionPriceTextView);
            holder.horPromotionPriceTextViews[1] = (TextView) convertView.findViewById(R.id.hor2PromotionPriceTextView);
            holder.horPriceTextViews = new TextView[2];
            holder.horPriceTextViews[0] = (TextView) convertView.findViewById(R.id.hor1PriceTextView);
            holder.horPriceTextViews[1] = (TextView) convertView.findViewById(R.id.hor2PriceTextView);
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

        if (hashMap.get("list_type").equals("ver")) {
            holder.horLinearLayout.setVisibility(View.GONE);
            holder.verRelativeLayout.setVisibility(View.VISIBLE);
            holder.verBorderImageView.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(hashMap.get("goods_image_url"), holder.verImageView);
            holder.verTitleTextView.setText(hashMap.get("goods_name"));
            holder.verPromotionPriceTextView.setText(hashMap.get("goods_promotion_price"));
            holder.verPriceTextView.setText(hashMap.get("goods_salenum"));
            holder.verRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startGoods(hashMap.get("goods_id"));
                }
            });
        } else {
            holder.horLinearLayout.setVisibility(View.VISIBLE);
            holder.verRelativeLayout.setVisibility(View.GONE);
            holder.horBorderImageView[0].setVisibility(View.GONE);
            holder.horBorderImageView[1].setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(hashMap.get("goods_image_url_1"), holder.horImageView[0]);
            holder.horTitleTextViews[0].setText(hashMap.get("goods_name_1"));
            holder.horPromotionPriceTextViews[0].setText(hashMap.get("goods_promotion_price_1"));
            holder.horPriceTextViews[0].setText(hashMap.get("goods_salenum_1"));
            ImageLoader.getInstance().displayImage(hashMap.get("goods_image_url_2"), holder.horImageView[1]);
            holder.horTitleTextViews[1].setText(hashMap.get("goods_name_2"));
            holder.horPromotionPriceTextViews[1].setText(hashMap.get("goods_promotion_price_2"));
            holder.horPriceTextViews[1].setText(hashMap.get("goods_salenum_2"));
            if (TextUtil.isEmpty(hashMap.get("goods_id_2"))) {
                holder.horRelativeLayout[1].setVisibility(View.INVISIBLE);
            } else {
                holder.horRelativeLayout[1].setVisibility(View.VISIBLE);
            }
            holder.horRelativeLayout[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startGoods(hashMap.get("goods_id_1"));
                }
            });

            holder.horRelativeLayout[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startGoods(hashMap.get("goods_id_2"));
                }
            });
        }

        return convertView;

    }

    public void startGoods(String goods_id) {
        Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
        intent.putExtra("id", goods_id);
        ActivityUtil.start(mActivity, intent);
    }

    public class Holder {

        public LinearLayout mLinearLayout;

        public LinearLayout horLinearLayout;
        public RelativeLayout[] horRelativeLayout;
        public ImageView[] horImageView;
        public ImageView[] horBorderImageView;
        public TextView[] horTitleTextViews;
        public TextView[] horPromotionPriceTextViews;
        public TextView[] horPriceTextViews;

        public RelativeLayout verRelativeLayout;
        public ImageView verImageView;
        public ImageView verBorderImageView;
        public TextView verTitleTextView;
        public TextView verTimeTextView;
        public TextView verPromotionPriceTextView;
        public TextView verPriceTextView;

    }

}