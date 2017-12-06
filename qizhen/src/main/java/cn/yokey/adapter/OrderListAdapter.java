package cn.yokey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.BuyPayActivity;
import cn.yokey.nsg.CollectionActivity;
import cn.yokey.nsg.R;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class OrderListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public OrderListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_order, null);
            holder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
            holder.buttonRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.buttonRelativeLayout);
            holder.storeTextView = (TextView) convertView.findViewById(R.id.storeTextView);
            holder.statusTextView = (TextView) convertView.findViewById(R.id.statusTextView);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.mainImageView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.info1TextView = (TextView) convertView.findViewById(R.id.info1TextView);
            holder.info2TextView = (TextView) convertView.findViewById(R.id.info2TextView);
            holder.cancelButton = (Button) convertView.findViewById(R.id.cancelButton);
            holder.payButton = (Button) convertView.findViewById(R.id.payButton);
            holder.confirmButton = (Button) convertView.findViewById(R.id.confirmButton);
            holder.commentButton = (Button) convertView.findViewById(R.id.commentButton);
            holder.refundButton = (Button) convertView.findViewById(R.id.refundButton);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);
        final HashMap<String, String> goodsInfoHashMap = new HashMap<>();

        try {
            JSONArray jsonArray = new JSONArray(hashMap.get("extend_order_goods"));
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            goodsInfoHashMap.put("goods_id", jsonObject.getString("goods_id"));
            goodsInfoHashMap.put("goods_name", jsonObject.getString("goods_name"));
            goodsInfoHashMap.put("goods_price", "￥ " + jsonObject.getString("goods_price"));
            goodsInfoHashMap.put("goods_num", jsonObject.getString("goods_num"));
            goodsInfoHashMap.put("goods_pay_price", "￥ " + jsonObject.getString("goods_pay_price"));
            goodsInfoHashMap.put("goods_image_url", jsonObject.getString("goods_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.storeTextView.setText(hashMap.get("store_name"));
        holder.statusTextView.setText(hashMap.get("state_desc"));
        ImageLoader.getInstance().displayImage(goodsInfoHashMap.get("goods_image_url"), holder.mImageView);
        holder.titleTextView.setText(goodsInfoHashMap.get("goods_name"));
        String info1 = "<font color='#FF5001'>" + goodsInfoHashMap.get("goods_price") + "</font><br>x " + goodsInfoHashMap.get("goods_num");
        holder.info1TextView.setText(Html.fromHtml(info1));
        String info2 = "共 <font color='#FF5001'>" + goodsInfoHashMap.get("goods_num") + "</font> 件商品，";
        info2 += "合计 <font color='#FF5001'>" + goodsInfoHashMap.get("goods_pay_price") + "</font> 元，";
        info2 += "运费 <font color='#FF5001'>" + hashMap.get("shipping_fee") + "</font> 元";
        holder.info2TextView.setText(Html.fromHtml(info2));

        holder.cancelButton.setVisibility(View.GONE);
        holder.payButton.setVisibility(View.GONE);
        holder.confirmButton.setVisibility(View.GONE);
        holder.commentButton.setVisibility(View.GONE);
        holder.refundButton.setVisibility(View.GONE);
        holder.buttonRelativeLayout.setVisibility(View.VISIBLE);
        switch (hashMap.get("order_state")) {
            case "0":
                holder.buttonRelativeLayout.setVisibility(View.GONE);
                break;
            case "10":
                holder.cancelButton.setVisibility(View.VISIBLE);
                holder.payButton.setVisibility(View.VISIBLE);
                break;
            case "20":
                holder.cancelButton.setVisibility(View.VISIBLE);
                holder.confirmButton.setVisibility(View.VISIBLE);
                break;
            case "30":
                holder.cancelButton.setVisibility(View.VISIBLE);
                holder.confirmButton.setVisibility(View.VISIBLE);
                break;
            case "40":
                holder.commentButton.setVisibility(View.VISIBLE);
                holder.refundButton.setVisibility(View.VISIBLE);
            default:
                break;
        }

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.query(mActivity, "确认您的选择", "取消这个订单", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                        AjaxParams ajaxParams = new AjaxParams();
                        ajaxParams.put("key", Constant.userKeyString);
                        ajaxParams.put("order_id", hashMap.get("order_id"));
                        Constant.mFinalHttp.post(Constant.LINK_MOBILE_ORDER_CANCEL, ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                if (TextUtil.isNcJson(o.toString())) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(o.toString());
                                        String datas = jsonObject.getString("datas");
                                        if (datas.equals("1")) {
                                            ToastUtil.show(mActivity, "取消成功");
                                            hashMap.put("state_desc", "已取消");
                                            hashMap.put("order_state", "0");
                                            notifyDataSetChanged();
                                        } else {
                                            ToastUtil.show(mActivity, "失败了，请重试");
                                        }
                                    } catch (JSONException e) {
                                        ToastUtil.show(mActivity, "失败了，请重试");
                                        e.printStackTrace();
                                    }
                                } else {
                                    ToastUtil.show(mActivity, "失败了，请重试");
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                ToastUtil.show(mActivity, "失败了，请重试");
                            }
                        });
                    }
                });
            }
        });

        holder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hashMap.get("payment_code").equals("online")) {
                    Intent intent = new Intent(mActivity, BuyPayActivity.class);
                    intent.putExtra("pay_sn", hashMap.get("pay_sn"));
                    ActivityUtil.start(mActivity, intent);
                }
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
        public Button cancelButton;
        public Button payButton;
        public Button confirmButton;
        public Button commentButton;
        public Button refundButton;

    }

}