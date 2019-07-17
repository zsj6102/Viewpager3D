package com.zsj.lib;

import android.support.v4.view.ViewPager;
import android.view.View;

public class ZoomPageTransformer implements ViewPager.PageTransformer {


    private static final float MIN_SCALE = 0.857f;//0.85缩放
    public static final float DEFAULT_CENTER = 0.5f;

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        view.setPivotY(pageHeight / 2);
        view.setPivotX(pageWidth / 2);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setScaleX(1);
            view.setScaleY(MIN_SCALE);
            view.setPivotX(pageWidth);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            if (position < 0) //1-2:1[0,-1] ;2-1:1[-1,0]
            {

                float scaleFactor = (1 + position) * (1 - MIN_SCALE) + MIN_SCALE;
                view.setScaleX(1);
                view.setScaleY(scaleFactor);

                view.setPivotX(pageWidth * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));

            } else //1-2:2[1,0] ;2-1:2[0,1]
            {
                float scaleFactor = (1 - position) * (1 - MIN_SCALE) + MIN_SCALE;
                view.setScaleX(1);
                view.setScaleY(scaleFactor);
                view.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));
            }


        } else { // (1,+Infinity]
            view.setPivotX(0);
            view.setScaleX(1);
            view.setScaleY(MIN_SCALE);
        }
    }


}
