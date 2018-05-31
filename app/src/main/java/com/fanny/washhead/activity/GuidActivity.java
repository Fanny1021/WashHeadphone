package com.fanny.washhead.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanny.washhead.R;
import com.fanny.washhead.adapter.ImageAdapter;
import com.fanny.washhead.utils.GuideUtils;
import com.fanny.washhead.utils.SharePrefrenceUtil;
import com.fanny.washhead.view.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuidActivity extends AppCompatActivity {
    public String TAG = "GuidActivity";
    @BindView(R.id.ll_item)
    LinearLayout llItem;
    @BindView(R.id.blue_iv)
    ImageView blueIv;
    @BindView(R.id.vp_broad)
    ViewPager vpBroad;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;

    private int pointWidth;//小灰点的距离
    int position;//当前界面数（从0开始）
    int[] imageResIDs = {
            R.drawable.bg1, R.drawable.bg2, R.drawable.bg3};


    private int preColor = Color.parseColor("#2c2200");
    private int progressColor = Color.parseColor("#6bb849");
    private int CircleColor = Color.parseColor("#CCCCCC");
    private int textColor = Color.parseColor("#9bb879");
    private CircleProgressBar pg;

    private GuideUtils guideUtils = null;
    private boolean isFirstAct;

    private List<View> lists;
    private ArrayList<ImageView> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);
        ButterKnife.bind(this);

//        isFirstAct= (boolean) SharePrefrenceUtil.getData(getBaseContext(),SharePrefrenceUtil.IS_FIRST_ACT,true);
//        Log.e(TAG,isFirstAct+"");

        isFirstAct = true;

        if (isFirstAct) {
            initGuidView();
        } else {
            Intent intent = new Intent(GuidActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    private void initGuidView() {

        /**
         *
         */
        initData();

        /**
         * 添加手势引导工具
         */
//        guideUtils = GuideUtils.getInstance();
//        guideUtils.initGuide(this, R.mipmap.icon_guid);

        VpAdapter adapter=new VpAdapter();
        vpBroad.setAdapter(adapter);
        vpBroad.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            /**
             * 界面滑动时回调此方法
             * arg0:当前界面数
             * positionOffset:界面滑动过的百分数（0.0-1.0）
             * positionOffsetPixels:当前界面偏移的像素位置
             */
            public void onPageScrolled(int arg0, float positionOffset, int positionOffsetPixels) {
                //小蓝点当前滑动距离
                int width;
                //1个界面就要一个小灰点的距离，再加上滑动过的百分比距离就是当前蓝点的位置
                width= (int) (positionOffset*pointWidth+arg0*pointWidth);
                RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) blueIv.getLayoutParams();
                //设置蓝点的左外边距
                lp.leftMargin=width;
                blueIv.setLayoutParams(lp);
                /**
                 * 开始体验按钮只能出现在最后一页，
                 * 并且在滑动的过程中保持消失
                 */
                if(position==imageResIDs.length-1 && positionOffset==0){
                    btn3.setVisibility(View.VISIBLE);
                }else {
                    btn3.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            //当前选中第几个界面
            public void onPageSelected(int arg0) {
                position=arg0;
            }

            @Override
            //状态改变时调用：arg0=0还没滑动,arg0=1正在滑动,arg0=2滑动完毕
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    //初始化图片轮播的图片
    private void initData() {

        mImageList = new ArrayList<>();

        for (int i = 0; i < imageResIDs.length; i++) {
            /**
             * 将图片的引用转化为图片控件存在mImageList的集合中
             */
            ImageView iv = new ImageView(this);
            iv.setImageResource(imageResIDs[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//设置图片的拉伸方式为充满
            mImageList.add(iv);
            /**
             * 绘制灰点
             */
            ImageView points = new ImageView(this);
            points.setImageResource(R.drawable.white_point);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            //给第一个以外的小灰点儿设置左边距，保证三个灰点水平居中
            if (i > 0) {
                lp.leftMargin = 30;//设置左外边距，像素
            }

            points.setLayoutParams(lp);
            llItem.addView(points);
        }

        /**
         * 为了完成蓝点在界面滑动时的动画效果，
         * 必须获取到灰点的边距，通过动态的给蓝点设置边距来完成动画效果
         * 由于在执行onCreate方法时，界面还没有绘制完成，无法获取pointWidth，设定小蓝点绘制完成的事件监听，当小蓝点绘制完成再获取
         */
        blueIv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                /**
                 * 获取小灰点圆心间的距离，第1个灰点和第二个灰点的距离
                 */
                pointWidth = llItem.getChildAt(1).getLeft() - llItem.getChildAt(0).getLeft();
            }
        });
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                guideUtils.initGuide(GuidActivity.this, R.drawable.add_guid);
                guideUtils.setFirst(true);
                SharePrefrenceUtil.saveParam(getBaseContext(), SharePrefrenceUtil.IS_FIRST_ACT, true);
                break;
            case R.id.btn2:
                guideUtils.setFirst(false);
                SharePrefrenceUtil.saveParam(getBaseContext(), SharePrefrenceUtil.IS_FIRST_ACT, false);
                guideUtils.initGuide(GuidActivity.this, R.drawable.add_guid);
                break;
            case R.id.btn3:
                Intent intent = new Intent(GuidActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    class VpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(GuidActivity.this);
            imageView.setImageResource(imageResIDs[position]);
            imageView = mImageList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
