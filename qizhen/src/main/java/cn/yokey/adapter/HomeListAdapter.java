package cn.yokey.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cn.yokey.nsg.GoodsDetailActivity;
import cn.yokey.nsg.MainActivity;
import cn.yokey.nsg.R;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.TextUtil;

public class HomeListAdapter extends BaseAdapter {

    private Activity mActivity;
    private CountDownTimer mCountDownTimer;
    private ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> mArrayList;

    public HomeListAdapter(Activity activity, ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> arrayList) {
        this.mActivity = activity;
        this.mArrayList = arrayList;
        this.mCountDownTimer = null;
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
            convertView = View.inflate(mActivity, R.layout.item_list_home, null);
            holder.mLinearLayout = (LinearLayout) convertView.findViewById(R.id.mainLinearLayout);
            holder.advListRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.advListRelativeLayout);
            holder.advListViewPager = (ViewPager) convertView.findViewById(R.id.advListViewPager);
            holder.navRadioGroup = (RadioGroup) convertView.findViewById(R.id.navRadioGroup);
            holder.navRadioButton = new RadioButton[4];
            holder.navRadioButton[0] = (RadioButton) convertView.findViewById(R.id.navClassRadioButton);
            holder.navRadioButton[1] = (RadioButton) convertView.findViewById(R.id.navCartRadioButton);
            holder.navRadioButton[2] = (RadioButton) convertView.findViewById(R.id.navUserRadioButton);
            holder.navRadioButton[3] = (RadioButton) convertView.findViewById(R.id.navSignRadioButton);
            holder.home2LinearLayout = (LinearLayout) convertView.findViewById(R.id.home2LinearLayout);
            holder.home2TitleTextView = (TextView) convertView.findViewById(R.id.home2TitleTextView);
            holder.home2ImageView = new ImageView[3];
            holder.home2ImageView[0] = (ImageView) convertView.findViewById(R.id.home2OneImageView);
            holder.home2ImageView[1] = (ImageView) convertView.findViewById(R.id.home2TwoImageView);
            holder.home2ImageView[2] = (ImageView) convertView.findViewById(R.id.home2ThrImageView);
            holder.home4LinearLayout = (LinearLayout) convertView.findViewById(R.id.home4LinearLayout);
            holder.home4TitleTextView = (TextView) convertView.findViewById(R.id.home4TitleTextView);
            holder.home4ImageView = new ImageView[3];
            holder.home4ImageView[0] = (ImageView) convertView.findViewById(R.id.home4OneImageView);
            holder.home4ImageView[1] = (ImageView) convertView.findViewById(R.id.home4TwoImageView);
            holder.home4ImageView[2] = (ImageView) convertView.findViewById(R.id.home4ThrImageView);
            holder.goodsLinearLayout = (LinearLayout) convertView.findViewById(R.id.goodsLinearLayout);
            holder.goodsTitleTextView = (TextView) convertView.findViewById(R.id.goodsTitleTextView);
            holder.goodsRelativeLayout = new RelativeLayout[6];
            holder.goodsRelativeLayout[0] = (RelativeLayout) convertView.findViewById(R.id.goods1RelativeLayout);
            holder.goodsRelativeLayout[1] = (RelativeLayout) convertView.findViewById(R.id.goods2RelativeLayout);
            holder.goodsRelativeLayout[2] = (RelativeLayout) convertView.findViewById(R.id.goods3RelativeLayout);
            holder.goodsRelativeLayout[3] = (RelativeLayout) convertView.findViewById(R.id.goods4RelativeLayout);
            holder.goodsRelativeLayout[4] = (RelativeLayout) convertView.findViewById(R.id.goods5RelativeLayout);
            holder.goodsRelativeLayout[5] = (RelativeLayout) convertView.findViewById(R.id.goods6RelativeLayout);
            holder.goodsImageView = new ImageView[6];
            holder.goodsImageView[0] = (ImageView) convertView.findViewById(R.id.goods1ImageView);
            holder.goodsImageView[1] = (ImageView) convertView.findViewById(R.id.goods2ImageView);
            holder.goodsImageView[2] = (ImageView) convertView.findViewById(R.id.goods3ImageView);
            holder.goodsImageView[3] = (ImageView) convertView.findViewById(R.id.goods4ImageView);
            holder.goodsImageView[4] = (ImageView) convertView.findViewById(R.id.goods5ImageView);
            holder.goodsImageView[5] = (ImageView) convertView.findViewById(R.id.goods6ImageView);
            holder.goodsBorderImageView = new ImageView[6];
            holder.goodsBorderImageView[0] = (ImageView) convertView.findViewById(R.id.goods1BorderImageView);
            holder.goodsBorderImageView[1] = (ImageView) convertView.findViewById(R.id.goods2BorderImageView);
            holder.goodsBorderImageView[2] = (ImageView) convertView.findViewById(R.id.goods3BorderImageView);
            holder.goodsBorderImageView[3] = (ImageView) convertView.findViewById(R.id.goods4BorderImageView);
            holder.goodsBorderImageView[4] = (ImageView) convertView.findViewById(R.id.goods5BorderImageView);
            holder.goodsBorderImageView[5] = (ImageView) convertView.findViewById(R.id.goods6BorderImageView);
            holder.goodsTitleTextViews = new TextView[6];
            holder.goodsTitleTextViews[0] = (TextView) convertView.findViewById(R.id.goods1TitleTextView);
            holder.goodsTitleTextViews[1] = (TextView) convertView.findViewById(R.id.goods2TitleTextView);
            holder.goodsTitleTextViews[2] = (TextView) convertView.findViewById(R.id.goods3TitleTextView);
            holder.goodsTitleTextViews[3] = (TextView) convertView.findViewById(R.id.goods4TitleTextView);
            holder.goodsTitleTextViews[4] = (TextView) convertView.findViewById(R.id.goods5TitleTextView);
            holder.goodsTitleTextViews[5] = (TextView) convertView.findViewById(R.id.goods6TitleTextView);
            holder.goodsPromotionPriceTextViews = new TextView[6];
            holder.goodsPromotionPriceTextViews[0] = (TextView) convertView.findViewById(R.id.goods1PromotionPriceTextView);
            holder.goodsPromotionPriceTextViews[1] = (TextView) convertView.findViewById(R.id.goods2PromotionPriceTextView);
            holder.goodsPromotionPriceTextViews[2] = (TextView) convertView.findViewById(R.id.goods3PromotionPriceTextView);
            holder.goodsPromotionPriceTextViews[3] = (TextView) convertView.findViewById(R.id.goods4PromotionPriceTextView);
            holder.goodsPromotionPriceTextViews[4] = (TextView) convertView.findViewById(R.id.goods5PromotionPriceTextView);
            holder.goodsPromotionPriceTextViews[5] = (TextView) convertView.findViewById(R.id.goods6PromotionPriceTextView);
            holder.goodsPriceTextViews = new TextView[6];
            holder.goodsPriceTextViews[0] = (TextView) convertView.findViewById(R.id.goods1PriceTextView);
            holder.goodsPriceTextViews[1] = (TextView) convertView.findViewById(R.id.goods2PriceTextView);
            holder.goodsPriceTextViews[2] = (TextView) convertView.findViewById(R.id.goods3PriceTextView);
            holder.goodsPriceTextViews[3] = (TextView) convertView.findViewById(R.id.goods4PriceTextView);
            holder.goodsPriceTextViews[4] = (TextView) convertView.findViewById(R.id.goods5PriceTextView);
            holder.goodsPriceTextViews[5] = (TextView) convertView.findViewById(R.id.goods6PriceTextView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final HashMap<String, ArrayList<HashMap<String, String>>> hashMap = mArrayList.get(position);

        holder.advListRelativeLayout.setVisibility(View.GONE);
        holder.navRadioGroup.setVisibility(View.GONE);
        holder.home2LinearLayout.setVisibility(View.GONE);
        holder.home4LinearLayout.setVisibility(View.GONE);
        holder.goodsLinearLayout.setVisibility(View.GONE);

        if (hashMap.get("adv_list") != null) {
            holder.advListRelativeLayout.setVisibility(View.VISIBLE);
            advList(holder, hashMap.get("adv_list"));
        }

        if (hashMap.get("nav") != null) {
            holder.navRadioGroup.setVisibility(View.VISIBLE);
            nav(holder);
        }

        if (hashMap.get("home2") != null) {
            holder.home2LinearLayout.setVisibility(View.VISIBLE);
            home2(holder, hashMap.get("home2"));
        }

        if (hashMap.get("home4") != null) {
            holder.home4LinearLayout.setVisibility(View.VISIBLE);
            home4(holder, hashMap.get("home4"));
        }

        if (hashMap.get("goods1") != null) {
            holder.goodsLinearLayout.setVisibility(View.VISIBLE);
            goods1(holder, hashMap.get("goods1"));
        }

        if (hashMap.get("goods2") != null) {
            holder.goodsLinearLayout.setVisibility(View.VISIBLE);
            goods2(holder, hashMap.get("goods2"));
        }

        if (hashMap.get("goods") != null) {
            holder.goodsLinearLayout.setVisibility(View.VISIBLE);
            goods(holder, hashMap.get("goods"));
        }

        return convertView;

    }

    public void advList(final Holder holder, final ArrayList<HashMap<String, String>> arrayList) {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        //解析数据
        List<View> list = new ArrayList<>();
        ImageView[] imageViews = new ImageView[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            list.add(mActivity.getLayoutInflater().inflate(R.layout.include_image_view, null));
            imageViews[i] = (ImageView) list.get(i).findViewById(R.id.includeImageView);
            ImageLoader.getInstance().displayImage(arrayList.get(i).get("image"), imageViews[i]);
        }
        holder.advListViewPager.setAdapter(new ViewPagerAdapter(list));

        //自动轮播
        mCountDownTimer = new CountDownTimer(6000000, 5000) {
            @Override
            public void onTick(long l) {
                if (holder.advListViewPager.getCurrentItem() == arrayList.size() - 1) {
                    holder.advListViewPager.setCurrentItem(0);
                } else {
                    holder.advListViewPager.setCurrentItem(holder.advListViewPager.getCurrentItem() + 1);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

        //点击事件
        for (int i = 0; i < arrayList.size(); i++) {
            final int pos = i;
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String type = arrayList.get(pos).get("type");
                    String data = arrayList.get(pos).get("data");
                    switch (type) {
                        case "goods":
                            Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                            intent.putExtra("id", data);
                            ActivityUtil.start(mActivity, intent);
                            break;
                        default:
                            break;
                    }
                }
            });
        }

    }

    public void nav(final Holder holder) {

        holder.navRadioButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setTab(1);
            }
        });

        holder.navRadioButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setTab(2);
            }
        });

        holder.navRadioButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setTab(3);
            }
        });

    }

    public void home2(final Holder holder, final ArrayList<HashMap<String, String>> arrayList) {

        if (TextUtil.isEmpty(arrayList.get(0).get("title"))) {
            holder.home2TitleTextView.setVisibility(View.GONE);
        } else {
            holder.home2TitleTextView.setVisibility(View.VISIBLE);
            holder.home2TitleTextView.setText(arrayList.get(0).get("title"));
        }

        ImageLoader.getInstance().displayImage(arrayList.get(0).get("square_image"), holder.home2ImageView[0]);
        ImageLoader.getInstance().displayImage(arrayList.get(0).get("rectangle1_image"), holder.home2ImageView[1]);
        ImageLoader.getInstance().displayImage(arrayList.get(0).get("rectangle2_image"), holder.home2ImageView[2]);

    }

    public void home4(final Holder holder, final ArrayList<HashMap<String, String>> arrayList) {

        if (TextUtil.isEmpty(arrayList.get(0).get("title"))) {
            holder.home4TitleTextView.setVisibility(View.GONE);
        } else {
            holder.home4TitleTextView.setVisibility(View.VISIBLE);
            holder.home4TitleTextView.setText(arrayList.get(0).get("title"));
        }

        ImageLoader.getInstance().displayImage(arrayList.get(0).get("square_image"), holder.home4ImageView[0]);
        ImageLoader.getInstance().displayImage(arrayList.get(0).get("rectangle1_image"), holder.home4ImageView[1]);
        ImageLoader.getInstance().displayImage(arrayList.get(0).get("rectangle2_image"), holder.home4ImageView[2]);

    }

    public void goods1(final Holder holder, final ArrayList<HashMap<String, String>> arrayList) {

        if (arrayList.size() == 0) {
            holder.goodsLinearLayout.setVisibility(View.GONE);
            return;
        } else {
            holder.goodsLinearLayout.setVisibility(View.VISIBLE);
        }

        if (!TextUtil.isEmpty(arrayList.get(0).get("title"))) {
            holder.goodsTitleTextView.setVisibility(View.VISIBLE);
            holder.goodsTitleTextView.setText(arrayList.get(0).get("title"));
        } else {
            holder.goodsTitleTextView.setVisibility(View.GONE);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            final int position = i;
            ImageLoader.getInstance().displayImage(arrayList.get(i).get("goods_image"), holder.goodsImageView[i]);
            holder.goodsBorderImageView[i].setImageResource(R.mipmap.ic_border_goods_qg);
            holder.goodsTitleTextViews[i].setText(arrayList.get(i).get("goods_name"));
            holder.goodsPromotionPriceTextViews[i].setText(arrayList.get(i).get("goods_promotion_price"));
            holder.goodsPriceTextViews[i].setVisibility(View.VISIBLE);
            holder.goodsPriceTextViews[i].setText(arrayList.get(i).get("goods_price"));
            holder.goodsPriceTextViews[i].getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.goodsRelativeLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra("id", arrayList.get(position).get("goods_id"));
                    ActivityUtil.start(mActivity, intent);
                }
            });
        }

    }

    public void goods2(final Holder holder, final ArrayList<HashMap<String, String>> arrayList) {

        if (arrayList.size() == 0) {
            holder.goodsLinearLayout.setVisibility(View.GONE);
            return;
        } else {
            holder.goodsLinearLayout.setVisibility(View.VISIBLE);
        }

        if (!TextUtil.isEmpty(arrayList.get(0).get("title"))) {
            holder.goodsTitleTextView.setVisibility(View.VISIBLE);
            holder.goodsTitleTextView.setText(arrayList.get(0).get("title"));
        } else {
            holder.goodsTitleTextView.setVisibility(View.GONE);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            final int position = i;
            ImageLoader.getInstance().displayImage(arrayList.get(i).get("goods_image"), holder.goodsImageView[i]);
            holder.goodsBorderImageView[i].setImageResource(R.mipmap.ic_border_goods_tg);
            holder.goodsTitleTextViews[i].setText(arrayList.get(i).get("goods_name"));
            holder.goodsPromotionPriceTextViews[i].setText(arrayList.get(i).get("goods_promotion_price"));
            holder.goodsPriceTextViews[i].setVisibility(View.VISIBLE);
            holder.goodsPriceTextViews[i].setText(arrayList.get(i).get("goods_price"));
            holder.goodsPriceTextViews[i].getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.goodsRelativeLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra("id", arrayList.get(position).get("goods_id"));
                    ActivityUtil.start(mActivity, intent);
                }
            });
        }

    }

    public void goods(final Holder holder, final ArrayList<HashMap<String, String>> arrayList) {

        if (arrayList.size() == 0) {
            holder.goodsLinearLayout.setVisibility(View.GONE);
            return;
        } else {
            holder.goodsLinearLayout.setVisibility(View.VISIBLE);
        }

        if (!TextUtil.isEmpty(arrayList.get(0).get("title"))) {
            holder.goodsTitleTextView.setVisibility(View.VISIBLE);
            holder.goodsTitleTextView.setText(arrayList.get(0).get("title"));
        } else {
            holder.goodsTitleTextView.setVisibility(View.GONE);
        }

        for (int i = 0; i < holder.goodsRelativeLayout.length; i++) {
            holder.goodsRelativeLayout[i].setVisibility(View.GONE);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            final int position = i;
            holder.goodsRelativeLayout[i].setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(arrayList.get(i).get("goods_image"), holder.goodsImageView[i]);
            holder.goodsBorderImageView[i].setImageResource(R.mipmap.ic_border_goods_hot);
            holder.goodsTitleTextViews[i].setText(arrayList.get(i).get("goods_name"));
            holder.goodsPromotionPriceTextViews[i].setText(arrayList.get(i).get("goods_promotion_price"));
            holder.goodsPriceTextViews[i].setVisibility(View.GONE);
            holder.goodsRelativeLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra("id", arrayList.get(position).get("goods_id"));
                    ActivityUtil.start(mActivity, intent);
                }
            });
        }

    }

    public class Holder {

        public LinearLayout mLinearLayout;
        //adv_list
        public RelativeLayout advListRelativeLayout;
        public ViewPager advListViewPager;
        //nav
        public RadioGroup navRadioGroup;
        public RadioButton[] navRadioButton;
        //home2
        public LinearLayout home2LinearLayout;
        public TextView home2TitleTextView;
        public ImageView[] home2ImageView;
        //home4
        public LinearLayout home4LinearLayout;
        public TextView home4TitleTextView;
        public ImageView[] home4ImageView;
        //goods
        public LinearLayout goodsLinearLayout;
        public TextView goodsTitleTextView;
        public RelativeLayout[] goodsRelativeLayout;
        public ImageView[] goodsImageView;
        public ImageView[] goodsBorderImageView;
        public TextView[] goodsTitleTextViews;
        public TextView[] goodsPromotionPriceTextViews;
        public TextView[] goodsPriceTextViews;

    }

}