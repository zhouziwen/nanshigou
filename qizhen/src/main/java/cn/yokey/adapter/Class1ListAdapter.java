package cn.yokey.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.R;
import cn.yokey.util.TextUtil;

public class Class1ListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public Class1ListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_class1, null);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.mainImageView);
            holder.mTextView = (TextView) convertView.findViewById(R.id.mainTextView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.mTextView.setText(hashMap.get("gc_name"));

        if (hashMap.get("click").equals("0")) {
            holder.mTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.nav));
            if (TextUtil.isEmpty(hashMap.get("image"))) {
                holder.mImageView.setImageResource(R.mipmap.ic_normal_class);
            } else {
                ImageLoader.getInstance().displayImage(hashMap.get("image"), holder.mImageView);
            }
        } else {
            holder.mTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.main));
            if (TextUtil.isEmpty(hashMap.get("image"))) {
                holder.mImageView.setImageResource(R.mipmap.ic_normal_class_press);
            } else {
                ImageLoader.getInstance().displayImage(hashMap.get("image"), holder.mImageView);
            }
        }

        return convertView;

    }

    public class Holder {

        public LinearLayout mLinearLayout;
        public ImageView mImageView;
        public TextView mTextView;

    }

}