package com.surine.tustbox.UI.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Fragment.message.AppMessageFragment;
import com.surine.tustbox.UI.Fragment.message.MomentMessageFragment;
import com.surine.tustbox.UI.Fragment.setting.BaseSettingFragment;
import com.surine.tustbox.UI.Fragment.setting.ProSettingFragment;
import com.surine.tustbox.UI.View.SuTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  V5 版本：用于展示消息通知和公告通知的Activity
 * */
public class V5_MessageActivity extends AppCompatActivity {

    @BindView(R.id.susuTab)
    SuTabLayout susuTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this);
        setContentView(R.layout.activity_tab_pager_by_susu);
        ButterKnife.bind(this);
        //加载标题
        List<String> tabTitle = new ArrayList<>();
        tabTitle.add("动态");
        tabTitle.add("公告");
        //加载碎片
        List<Fragment> fragments = new ArrayList<Fragment>() {{
            add(new MomentMessageFragment());
            add(new AppMessageFragment());
        }};
        viewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
                fragments,tabTitle));
       // viewpager.setOffscreenPageLimit(4);
        susuTab.setupWithViewPager(viewpager);
    }
}
