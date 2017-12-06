package cn.yokey.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.CartActivity;
import cn.yokey.nsg.R;
import cn.yokey.system.Constant;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class CartListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public CartListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_cart, null);
            holder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
            holder.storeTextView = (TextView) convertView.findViewById(R.id.storeTextView);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.mainImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.info1TextView = (TextView) convertView.findViewById(R.id.info1TextView);
            holder.info2TextView = (TextView) convertView.findViewById(R.id.info2TextView);
            holder.delButton = (Button) convertView.findViewById(R.id.delButton);
            holder.addButton = (Button) convertView.findViewById(R.id.addButton);
            holder.numEditText = (EditText) convertView.findViewById(R.id.numEditText);
            holder.subButton = (Button) convertView.findViewById(R.id.subButton);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);
        final float priceFloat = Float.parseFloat(hashMap.get("goods_promotion_price"));
        final int countInt = Integer.parseInt(hashMap.get("goods_num"));
        final float countFloat = priceFloat * countInt;

        holder.storeTextView.setText(hashMap.get("store_name"));
        ImageLoader.getInstance().displayImage(hashMap.get("goods_image_url"), holder.mImageView);
        holder.titleTextView.setText(hashMap.get("goods_name"));
        String info1 = "<font color='#FF5001'> ￥ " + priceFloat + "</font><br>x " + countInt;
        holder.info1TextView.setText(Html.fromHtml(info1));
        String info2 = "共 <font color='#FF5001'>" + countInt + "</font> 件商品，";
        info2 += "合计 <font color='#FF5001'>" + countFloat + "</font> 元";
        holder.info2TextView.setText(Html.fromHtml(info2));
        info2 = countInt + "";
        holder.numEditText.setText(info2);

        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.query(mActivity, "确认您的选择", "删除这个商品", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AjaxParams ajaxParams = new AjaxParams();
                        ajaxParams.put("key", Constant.userKeyString);
                        ajaxParams.put("cart_id", hashMap.get("cart_id"));
                        Constant.mFinalHttp.post(Constant.LINK_MOBILE_CART_DEL, ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                DialogUtil.cancel();
                                if (TextUtil.isNcJson(o.toString())) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(o.toString());
                                        String datas = jsonObject.getString("datas");
                                        if (datas.equals("1")) {
                                            ToastUtil.show(mActivity, "删除成功");
                                            CartActivity.mArrayList.remove(position);
                                            CartActivity.mAdapter.notifyDataSetChanged();
                                            if (CartActivity.mArrayList.isEmpty()) {
                                                CartActivity.calcTextView.setVisibility(View.GONE);
                                                CartActivity.buyTextView.setVisibility(View.GONE);
                                                CartActivity.tipsTextView.setVisibility(View.VISIBLE);
                                                CartActivity.tipsTextView.setText("购物车为空\n去逛逛吧");
                                            }
                                        } else {
                                            ToastUtil.show(mActivity, datas);
                                        }
                                    } catch (JSONException e) {
                                        ToastUtil.show(mActivity, "未知错误");
                                        e.printStackTrace();
                                    }
                                } else {
                                    ToastUtil.show(mActivity, "未知错误");
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                ToastUtil.show(mActivity, "未知错误");
                                DialogUtil.cancel();
                            }
                        });
                    }
                });
            }
        });

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String total = CartActivity.datas.get("cart_count");
                CartActivity.datas.put("cart_count", Integer.parseInt(total) + 1 + "");
                CartActivity.mArrayList.get(position).put("goods_num", countInt + 1 + "");
                updateCalc();
            }
        });

        holder.subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countInt > 1) {
                    String total = CartActivity.datas.get("cart_count");
                    CartActivity.datas.put("cart_count", Integer.parseInt(total) - 1 + "");
                    CartActivity.mArrayList.get(position).put("goods_num", countInt - 1 + "");
                    updateCalc();
                }
            }
        });

        return convertView;

    }

    public void updateCalc() {

        float count = 0.0f;

        for (int i = 0; i < CartActivity.mArrayList.size(); i++) {
            HashMap<String, String> hashMap = mArrayList.get(i);
            float priceFloat = Float.parseFloat(hashMap.get("goods_promotion_price"));
            int countInt = Integer.parseInt(hashMap.get("goods_num"));
            float countFloat = priceFloat * countInt;
            count += countFloat;
        }

        CartActivity.datas.put("sum", count + "");
        String total = "共 <font color='#FF5001'>" + CartActivity.datas.get("cart_count") + "</font> 件商品，";
        total += "共 <font color='#FF5001'>" + CartActivity.datas.get("sum") + "</font> 元";
        CartActivity.calcTextView.setText(Html.fromHtml(total));
        CartActivity.mAdapter.notifyDataSetChanged();

    }

    public class Holder {

        public RelativeLayout mRelativeLayout;
        public TextView storeTextView;
        public ImageView mImageView;
        public TextView titleTextView;
        public TextView info1TextView;
        public TextView info2TextView;
        public Button delButton;
        public Button addButton;
        public EditText numEditText;
        public Button subButton;

    }

}