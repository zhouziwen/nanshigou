package cn.yokey.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import cn.yokey.nsg.R;

public class SearchListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public SearchListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_search, null);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.typeTextView = (TextView) convertView.findViewById(R.id.typeTextView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.titleTextView.setText(hashMap.get("title"));
        holder.typeTextView.setText(hashMap.get("type"));

        return convertView;

    }

    public class Holder {

        public LinearLayout mLinearLayout;
        public TextView titleTextView;
        public TextView typeTextView;

    }

}