package com.zsj.lib;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class BannerPagerAdapter  extends PagerAdapter {
    private List<String> mList;
    private Context mContext;
    private int defaultImg= R.drawable.bg_image_loading;//默认图片
    private int mRoundCorners=-1;
    private int count = 0;

    /**
     * 默认
     * @param defaultImg
     */
    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    /**
     * 设置圆角
     * @param mRoundCorners
     */
    public void setmRoundCorners(int mRoundCorners) {
        this.mRoundCorners = mRoundCorners;
    }

    /**
     * 点击回调
     */
    public  interface OnClickImagesListener {
        void onImagesClick(int position);
    }
    private OnClickImagesListener mImagesListener;

    public void setOnClickImagesListener(OnClickImagesListener listener) {
        mImagesListener = listener;

    }
    public void setCount(int count){
        this.count = count;
    }
    public BannerPagerAdapter(List<String> list,Context context){
        this.mList = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_img_layout,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        final int index=position % mList.size();
        LoadImage(mList.get(index) ,imageView);
        //OnClick
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagesListener.onImagesClick(index);
            }
        });

        container.addView(view);
        return view;
    }

    /**
     * 加载图片
     */
    public  void LoadImage(String url, ImageView imageview) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.bg_image_loading)
                .error(R.drawable.bg_image_loading).transform(new GlideRoundTransform(mRoundCorners))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        if(mRoundCorners==-1){
            Glide.with(mContext)
                    .load(url)
                    .into(imageview);
        }
        else {

            Glide.with(mContext)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageview);
        }
    }


}
