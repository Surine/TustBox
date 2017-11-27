package com.surine.tustbox.UI;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Fragment.Me.Me_info_fragment;
import com.surine.tustbox.Fragment.common.CommenFragment;
import com.surine.tustbox.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends TustBaseActivity {
    @BindView(R.id.art_back)
    ImageView mArtBack;

    @BindView(R.id.head_from_server)
    CircleImageView mHeadFromServer;
    @BindView(R.id.name_from_server)
    TextView mNameFromServer;
    @BindView(R.id.sign_from_server)
    TextView mSignFromServer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        //set the toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("资料");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initViewFromServer();

        initViewPager();
    }

    private void initViewFromServer() {
        //免流量加载

        String head_url = SharedPreferencesUtil.Read_safe(this, "face", "");
        String nick_name = SharedPreferencesUtil.Read_safe(this, "nick_name", "");
        String sign = SharedPreferencesUtil.Read_safe(this, "sign", "");
        if (!head_url.equals("")) {
            Glide.with(this).load(head_url).into(mHeadFromServer);
            Glide.with(this).load(head_url).into(mArtBack);
        }
        if (!nick_name.equals("")) {
            mNameFromServer.setText(nick_name);
        }
        if (!sign.equals("")) {
            mSignFromServer.setText(sign);
        }
    }

    private void initViewPager() {
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mTabs = (TabLayout) findViewById(R.id.tabs);
        //  fragments.add(new OslFragment());
        fragments.add(new CommenFragment());
        fragments.add(Me_info_fragment.getInstance("2"));
        titles.add("动态");
        titles.add("更多");
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragments, titles);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(3);
        mTabs.setupWithViewPager(mViewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit:

                //finish();
                break;
        }
        return true;
    }
}
