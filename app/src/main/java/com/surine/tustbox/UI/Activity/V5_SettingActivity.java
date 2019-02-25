package com.surine.tustbox.UI.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.UI.Fragment.setting.BaseSettingFragment;
import com.surine.tustbox.UI.Fragment.setting.ProSettingFragment;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.View.SuTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class V5_SettingActivity extends TustBaseActivity {

    @BindView(R.id.susuTab)
    SuTabLayout susuTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this,true);
        setContentView(R.layout.activity_tab_pager_by_susu);
        ButterKnife.bind(this);
        //加载标题
        List<String> tabTitle = new ArrayList<String>();
        tabTitle.add("基础");
        tabTitle.add("高级");
        //加载碎片
        List<Fragment> fragments = new ArrayList<Fragment>() {{
            add(BaseSettingFragment.getInstance());
            add(ProSettingFragment.getInstance());
        }};
        viewpager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
                fragments,tabTitle));
        viewpager.setOffscreenPageLimit(4);
        susuTab.setupWithViewPager(viewpager);
    }
}
