package cn.yokey.nsg;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Vector;

import cn.yokey.adapter.MainPagerAdapter;
import cn.yokey.system.Android;
import cn.yokey.util.ActivityUtil;
import cn.yokey.util.TextUtil;
import cn.yokey.view.PhotoViewPager;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoActivity extends Activity {

    public static Activity mActivity;

    private String titleString;
    private Vector<String> imageVector;

    private ImageView leftImageView;
    private TextView titleTextView;
    private ImageView rightImageView;

    private ImageView[] mImageView;
    private PhotoViewPager mViewPager;
    private PhotoViewAttacher mAttCher;

    private void createControl() {

        mActivity = this;

        titleString = mActivity.getIntent().getStringExtra("title");
        int positionInt = mActivity.getIntent().getIntExtra("position", 0);
        imageVector = new Vector<>(TextUtil.encodeImageWithVector(mActivity.getIntent().getStringExtra("image")));

        //实例化
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);

        mViewPager = (PhotoViewPager) findViewById(R.id.mainViewPager);
        mImageView = new ImageView[imageVector.size()];

        //赋值
        leftImageView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.black));
        titleTextView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.black));
        rightImageView.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.black));
        rightImageView.setImageResource(R.mipmap.ic_action_more);
        titleTextView.setText(titleString);
        titleTextView.append(" - ");
        titleTextView.append(positionInt + 1 + "");
        titleTextView.append("/");
        titleTextView.append(imageVector.size() + "");

        ArrayList<View> mArrayList = new ArrayList<>();
        for (int i = 0; i < imageVector.size(); i++) {
            mArrayList.add(getLayoutInflater().inflate(R.layout.include_image_view, null));
            mImageView[i] = (ImageView) mArrayList.get(i).findViewById(R.id.includeImageView);
            mImageView[i].setBackgroundColor(ContextCompat.getColor(mActivity, R.color.black));
            ImageLoader.getInstance().displayImage(imageVector.get(i), mImageView[i]);
        }
        mViewPager.setAdapter(new MainPagerAdapter(mArrayList));
        mViewPager.setCurrentItem(positionInt);

        mAttCher = new PhotoViewAttacher(mImageView[positionInt]);
        mAttCher.update();

        //一些子程序
        Android.setStatusBar(mActivity);

    }

    private void createEvent() {

        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.finish(mActivity);
            }
        });

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleTextView.setText(titleString);
                titleTextView.append(" - ");
                titleTextView.append(position + 1 + "");
                titleTextView.append("/");
                titleTextView.append(imageVector.size() + "");
                mAttCher = new PhotoViewAttacher(mImageView[position]);
                mAttCher.update();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAttCher.cleanup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        createControl();
        createEvent();
    }

}