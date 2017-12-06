package cn.yokey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.yokey.nsg.AddressActivity;
import cn.yokey.nsg.AddressEditActivity;
import cn.yokey.nsg.R;
import cn.yokey.system.Constant;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.DialogUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.util.ToastUtil;

public class AddressListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, String>> mArrayList;

    public AddressListAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayList) {
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
            convertView = View.inflate(mActivity, R.layout.item_list_address, null);
            holder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.mainRelativeLayout);
            holder.defaultCheckBox = (CheckBox) convertView.findViewById(R.id.defaultCheckBox);
            holder.trueNameTextView = (TextView) convertView.findViewById(R.id.trueNameTetView);
            holder.phoneTextView = (TextView) convertView.findViewById(R.id.phoneTextView);
            holder.addressTextView = (TextView) convertView.findViewById(R.id.addressTextView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, String> hashMap = mArrayList.get(position);

        holder.trueNameTextView.setText(hashMap.get("true_name"));
        holder.phoneTextView.setText(hashMap.get("mob_phone"));
        holder.addressTextView.setText(hashMap.get("area_info"));
        holder.addressTextView.append(" ");
        holder.addressTextView.append(hashMap.get("address"));

        if (hashMap.get("is_default").equals("1")) {
            holder.defaultCheckBox.setChecked(true);
        } else {
            holder.defaultCheckBox.setChecked(false);
        }

        holder.defaultCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (hashMap.get("is_default").equals("1")) {
                    holder.defaultCheckBox.setChecked(true);
                } else {
                    holder.defaultCheckBox.setChecked(false);
                }
            }
        });

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressActivity.mHashMap = new HashMap<>(hashMap);
                ActivityUtil.startWithResult(mActivity, new Intent(mActivity, AddressEditActivity.class), 0);
            }
        });

        holder.mRelativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogUtil.query(mActivity, "确认您的选择", "删除这个地址", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtil.cancel();
                        AjaxParams ajaxParams = new AjaxParams();
                        ajaxParams.put("key", Constant.userKeyString);
                        ajaxParams.put("address_id", hashMap.get("address_id"));
                        Constant.mFinalHttp.post(Constant.LINK_MOBILE_ADDRESS_DEL, ajaxParams, new AjaxCallBack<Object>() {
                            @Override
                            public void onSuccess(Object o) {
                                super.onSuccess(o);
                                if (TextUtil.isNcJson(o.toString())) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(o.toString());
                                        String datas = jsonObject.getString("datas");
                                        if (datas.equals("1")) {
                                            ToastUtil.show(mActivity, "删除成功!");
                                            mArrayList.remove(position);
                                            notifyDataSetChanged();
                                        } else {
                                            ToastUtil.show(mActivity, "删除失败，请重试");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    ToastUtil.show(mActivity, "删除失败，请重试");
                                }
                            }

                            @Override
                            public void onFailure(Throwable t, int errorNo, String strMsg) {
                                super.onFailure(t, errorNo, strMsg);
                                ToastUtil.show(mActivity, "删除失败，请重试");
                            }
                        });
                    }
                });
                return false;
            }
        });

        return convertView;

    }

    public class Holder {

        public RelativeLayout mRelativeLayout;
        public CheckBox defaultCheckBox;
        public TextView trueNameTextView;
        public TextView phoneTextView;
        public TextView addressTextView;

    }

}