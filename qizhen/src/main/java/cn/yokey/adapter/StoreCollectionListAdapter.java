package cn.yokey.adapter;

import android.app.Activity;
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

import cn.yokey.nsg.R;
import cn.yokey.system.Constant;
import cn.yokey.util.TextUtil;
import cn.yokey.util.TimeUtil;

public class StoreCollectionListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public StoreCollectionListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_store, null);
            holder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.mainImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.goodsTextView = (TextView) convertView.findViewById(R.id.goodsTextView);
            holder.collectionTextView = (TextView) convertView.findViewById(R.id.collectionTextView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);

        ImageLoader.getInstance().displayImage(hashMap.get("store_image"),holder.mImageView);
        holder.titleTextView.setText(hashMap.get("store_name"));
        holder.timeTextView.setText(TimeUtil.decode(hashMap.get("fav_time")));
        holder.collectionTextView.setText(hashMap.get("store_collect"));
        holder.goodsTextView.setText(hashMap.get("goods_count"));

        return convertView;

    }

    public class Holder {

        public RelativeLayout mRelativeLayout;
        public ImageView mImageView;
        public TextView timeTextView;
        public TextView titleTextView;
        public TextView goodsTextView;
        public TextView collectionTextView;

    }

}
