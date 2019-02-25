package com.surine.tustbox.UI.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.ImagePreviewActivity;
import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Fragment.loadImage.ImagePreFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lzy.ninegrid.preview.ImagePreviewActivity.CURRENT_ITEM;
import static com.lzy.ninegrid.preview.ImagePreviewActivity.IMAGE_INFO;
import static com.surine.tustbox.App.Data.Constants.PICTURE_CHOOSE_LIST;


/**
 * V5 九图图片预览器
 * ViewPager + PhoteView 实现 2019年2月21日
 *             Add.支持当前位显示
 *             Add.缩放预览
 *             Add.保存图片
 * */
public class ImagePreViewActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.pagerTitle)
    TextView pagerTitle;
    private PagerAdapter pagerAdapter;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private List<ImageInfo> imageInfos;
    private int currentItem = 0;
    private List<String> picData;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this,false);
        setContentView(R.layout.activity_image_pre_view);
        ButterKnife.bind(this);

        //加载图片集合，及相应的点击进入标志位
        imageInfos = (List<ImageInfo>) getIntent().getSerializableExtra(IMAGE_INFO);
        //本地选择
        picData = (List<String>) getIntent().getSerializableExtra(PICTURE_CHOOSE_LIST);

        if(imageInfos != null)
            size = imageInfos.size();
        if(picData != null)
            size = picData.size();

        currentItem = getIntent().getIntExtra(CURRENT_ITEM,0);
        pagerTitle.setText((currentItem+1)+"/"+size);


        if(imageInfos == null && picData == null)
            return;

        if(imageInfos != null){
            //构建碎片
            fragments.clear();
            for (ImageInfo ii:imageInfos) {
                Fragment f = ImagePreFragment.getInstance(ii.getBigImageUrl());
                fragments.add(f);
            }
        }

        if(picData != null){
            //构建碎片
            fragments.clear();
            for (String ii:picData) {
                Fragment f = ImagePreFragment.getInstance(ii);
                fragments.add(f);
            }
        }


        //加载适配器
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
               fragments,
               titles
        );

        //配置预缓存
        viewpager.setOffscreenPageLimit(size);
        viewpager.setAdapter(pagerAdapter);
        //配置当前位置
        viewpager.setCurrentItem(currentItem);
        //添加监听器，用于改变标志位
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    pagerTitle.setText(position+1 +"/"+size);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
