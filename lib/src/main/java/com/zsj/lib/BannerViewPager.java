package com.zsj.lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by Administrator on 2018/11/28.
 * banner的控件处理
 */

public class BannerViewPager extends RelativeLayout implements ViewPager.OnPageChangeListener{
    private View mLayout;//布局
    private Activity mContext;//上下文
    private ViewPager mViewPager;//viewpager
    private BannerPagerAdapter mPagerAdapter;//adapter
    private LinearLayout mLineIndicator;//指示器集合容器
    private ImageView[] mImageView;//小圆点imageview对象
    private List<String> mList;//url数组
    private int currentIndex = 0;//当前实际page
    private int startCurrentIndex = 0;//当前page
    private int count;
    private Handler mHandler = null;
    private AutoRollRunnable mAutoRollRunnable = null;
    private int mRollTime=2 ;
    private int resId_piont_press= R.drawable.layer_vipager_indicator_select;
    private int resId_piont=R.drawable.layer_vipager_indicator_unselect;
    private boolean isPoint=false;//开启指示器

    public static interface OnClickBannerListener {
        void onBannerClick(int position);
    }
    private OnClickBannerListener mBannerListener;
    public BannerViewPager addBannerListener(OnClickBannerListener listener) {
        mBannerListener = listener;
        return this;
    }
    public BannerViewPager(Context context) {
        super(context);
    }
    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;

    }

    /**
     * 初始化viewpager
     * @param list  url集合
     * @param isGallery 是否使用3D画廊效果
     */
    public BannerViewPager initBanner(List<String> list, boolean isGallery){
        mList=list;
        //引入布局
        mLayout = LayoutInflater.from(mContext).inflate( R.layout.banner_view_layout, null);
        mViewPager  = (ViewPager) mLayout.findViewById(R.id.viewPager);//关闭
        mLineIndicator  = (LinearLayout) mLayout.findViewById(R.id.lineIndicator);
        //初始化位置
        currentIndex=startCurrentIndex%mList.size();

        mPagerAdapter = new BannerPagerAdapter(mList,mContext);
        mPagerAdapter.setCount(count);
        mPagerAdapter.setOnClickImagesListener(new BannerPagerAdapter.OnClickImagesListener() {
            @Override
            public void onImagesClick(int position) {
                if(mBannerListener!=null){
                    mBannerListener.onBannerClick(position);
                }
            }
        });

        mViewPager.setAdapter(mPagerAdapter);
        if(isGallery){
            mViewPager.setPageTransformer(true,new ZoomPageTransformer());
        }

        mViewPager.setCurrentItem(startCurrentIndex);
        mViewPager.setOffscreenPageLimit(2);//设置预加载的数量，这里设置了2,会预加载中心item左边两个Item和右边两个Item
        mViewPager.addOnPageChangeListener(this);
        return this;
    }


    public BannerViewPager setCount(int count){
        this.count = count;
        return this;
    }
    /**
     * 初始化viewpager
     * @param list  url集合
     * @param isGallery 是否使用3D画廊效果
     * @param alpha  滑动透明度变化
     */
    public BannerViewPager initBanner(List<String> list,boolean isGallery,float alpha){
        mList=list;

        //引入布局
        mLayout = LayoutInflater.from(mContext).inflate( R.layout.banner_view_layout, null);
        mViewPager  = (ViewPager) mLayout.findViewById(R.id.viewPager);//关闭
        mLineIndicator  = (LinearLayout) mLayout.findViewById(R.id.lineIndicator);
        //初始化位置
        currentIndex=startCurrentIndex%mList.size();

        mPagerAdapter = new BannerPagerAdapter(mList,mContext);
        mPagerAdapter.setOnClickImagesListener(new BannerPagerAdapter.OnClickImagesListener() {
            @Override
            public void onImagesClick(int position) {
                if(mBannerListener!=null){
                    mBannerListener.onBannerClick(position);
                }
            }
        });

        mViewPager.setAdapter(mPagerAdapter);
        if(isGallery){
            mViewPager.setPageTransformer(true,new ZoomPageTransformer());
        }

        mViewPager.setCurrentItem(startCurrentIndex);
        mViewPager.setOffscreenPageLimit(2);//设置预加载的数量，这里设置了2,会预加载中心item左边两个Item和右边两个Item
        mViewPager.addOnPageChangeListener(this);
        return this;
    }

    /**
     * 添加默认图片,当加载失败后显示
     * @param resId_img
     * @return
     */
    public BannerViewPager addDefaultImg(int resId_img){
        mPagerAdapter.setDefaultImg(resId_img);
        return this;
    }

    /**
     * 添加圆角
     * @param corners
     * @return
     */
    public BannerViewPager addRoundCorners(int corners){
        mPagerAdapter.setmRoundCorners(corners);
        return this;
    }
    /**
     *
     * @param columnMargin 两个Page之间的距离
     * @param rowMargin  page的外边距
     * 注意当添加了3D画廊效果时,columnMargin尽量设小。应该本是已经进行了x、y的缩放
     */
    public BannerViewPager addPageMargin(int columnMargin,int rowMargin) {

        mViewPager.setPageMargin(dip2px(columnMargin));
        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layout.setMargins(dip2px(rowMargin), 0, dip2px(rowMargin), 0);
        mViewPager.setLayoutParams(layout);
        return this;
    }

    /**
     * 添加小圆点
     * @param distance 间距
     */
    public BannerViewPager addPoint(int distance) {
        isPoint=true;
        mImageView = new ImageView[mList.size()];
        if(mList.size()>1){
            for (int i = 0; i < mList.size(); i++) {
                ImageView imageView=new ImageView(mContext);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(dip2px(distance)/2, 0, dip2px(distance)/2, 0);
                params.width = dip2px(20);
                params.height = dip2px(2);
                imageView.setLayoutParams(params);
                if(i==currentIndex){
                    imageView.setBackgroundResource(resId_piont_press);
                }
                else {
                    imageView.setBackgroundResource(resId_piont);
                }
                mImageView[i]=imageView;
                mLineIndicator.addView(imageView);
            }
        }

        return this;
    }

    public BannerViewPager setStartIndex(int index){
        this.startCurrentIndex = index;
        return this;
    }
    /**
     * 添加小圆点
     * @param distance 间距
     * @param piont_press 替换选中图标
     * @param piont       替换未选中图片
     */
    public BannerViewPager addPoint(int distance,int piont_press,int piont) {
        isPoint=true;
        resId_piont_press=piont_press;
        resId_piont=piont;
        mImageView = new ImageView[mList.size()];

        if(mList.size()>1){
            for (int i = 0; i < mList.size(); i++) {
                ImageView imageView=new ImageView(mContext);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(dip2px(distance)/2, 0, dip2px(distance)/2, 0);
                params.width = dip2px(20);
                params.height = dip2px(2);
                imageView.setLayoutParams(params);
                if(i==currentIndex){
                    imageView.setImageResource(resId_piont_press);
                }
                else {
                    imageView.setImageResource(resId_piont);
                }
                mImageView[i]=imageView;
                mLineIndicator.addView(imageView);
            }
        }


        return this;
    }
    /**
     * 添加小圆点底部间距
     * @param paddBottom
     */
    public BannerViewPager addPointBottom(int paddBottom){
        mLineIndicator.setPadding(0,0,0,dip2px(paddBottom));
        return this;
    }
    /**
     * 配置完成,将布局添加到父容器
     */
    public BannerViewPager  finishConfig(){
        this.addView(mLayout);
        return this;
    }
    public BannerViewPager  restart(){
        this.removeView(mLayout);
        return this;
    }
    //开始轮播
    public BannerViewPager addStartTimer(int time) {
        mRollTime=time;
        if(mHandler==null){
            mHandler = new Handler();
        }
        if(mAutoRollRunnable==null){
            mAutoRollRunnable = new AutoRollRunnable();
        }

        mAutoRollRunnable.start();
        return this;
    }

    // 停止轮播
    public void stopTimer() {
        if(mAutoRollRunnable!=null){
            mAutoRollRunnable.stop();
        }
    }
    private class AutoRollRunnable implements Runnable {
        //是否在轮播的标志
        boolean isRunning = false;
        @Override
        public void run() {
            if (isRunning) {
                int index =  mViewPager.getCurrentItem()+1;//下一个页
                mViewPager.setCurrentItem(index);//设置此次要显示的pager
                currentIndex=index%mList.size();
                setImageBackground(currentIndex);
                mHandler.postDelayed(this, 1000*mRollTime);
            }
        }
        public void start() {
            if (!isRunning) {
                isRunning = true;
                mHandler.removeCallbacks(this);
                mHandler.postDelayed(this, 1000*mRollTime);
            }
        }
        public void stop() {
            if (isRunning) {
                mHandler.removeCallbacks(this);
                isRunning = false;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoRollRunnable==null){
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()){
            // 按住Banner的时候，停止自动轮播
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_DOWN:
                int paddingLeft = mViewPager.getLeft();
                float touchX = ev.getRawX();
                // 如果是魅族模式，去除两边的区域
                if(touchX >= paddingLeft && touchX < getScreenWidth(getContext()) - paddingLeft){
                    mAutoRollRunnable.stop();
                }
                break;
            case MotionEvent.ACTION_UP:
                mAutoRollRunnable.start();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    public static int getScreenWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //---------------viewpager滑动事件-----------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
    /**
     * 滑动时同步改变底部小圆点
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        currentIndex=position % mList.size();
        setImageBackground(currentIndex);
    }
    /**
     * 改变指示器
     * @param selectItemsIndex
     */
    private void setImageBackground(int selectItemsIndex) {
        if(isPoint){
            if(mImageView.length>1 && mImageView != null){
                for (int i = 0; i < mImageView.length; i++) {
                    if (i == selectItemsIndex) {
                        mImageView[i].setImageResource(resId_piont_press);

                    } else {
                        mImageView[i].setImageResource(resId_piont);
                    }
                }
            }

        }
    }

}