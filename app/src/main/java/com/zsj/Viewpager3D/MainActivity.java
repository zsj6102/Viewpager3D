package com.zsj.Viewpager3D;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zsj.lib.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BannerViewPager bannerViewPager;
    private int resId_piont_press = R.drawable.layer_vipager_indicator_select;
    private int resId_piont = R.drawable.layer_vipager_indicator_unselect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> list = new ArrayList<>();
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f19cc0079.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1ac12d1d.jpg");
        list.add("http://bpic.588ku.com/element_origin_min_pic/00/00/05/115732f1bad97d1.jpg");
        bannerViewPager = findViewById(R.id.banner_view);
        bannerViewPager.restart().setStartIndex(list.size() * 1000).setCount(50000).initBanner(list, true).addPageMargin(5, 15)//参数1page之间的间距,参数2中间item距离边界的间距
                .addPoint(0, resId_piont_press, resId_piont)//添加指示器
                .addPointBottom(10)
                .addStartTimer(3)
                .addRoundCorners(20)//圆角
                .finishConfig()//这句必须加
                .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                        //点击item


                    }
                });
    }
}
