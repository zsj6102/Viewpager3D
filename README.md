# Viewpager3D
1.根目录 引入maven仓库
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
引入依赖
implementation 'com.github.zsj6102:Viewpager3D:1.0.0'

2.使用
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
