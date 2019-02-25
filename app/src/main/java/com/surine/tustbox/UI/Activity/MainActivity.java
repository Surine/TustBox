package com.surine.tustbox.UI.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.UI.Fragment.PageFragment.FirstPageFragment;
import com.surine.tustbox.UI.Fragment.PageFragment.FunctionPageFragment;
import com.surine.tustbox.UI.Fragment.PageFragment.SchedulePageFragment;
import com.surine.tustbox.UI.Fragment.PageFragment.SchoolPageFragment;
import com.surine.tustbox.UI.Fragment.PageFragment.TaskPageFragment;
import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.App.Init.TustBaseActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;

import static com.surine.tustbox.App.Data.Constants.HIDEBOTTOM;
import static com.surine.tustbox.App.Data.Constants.NORMALPAGE;
import static com.surine.tustbox.Helper.Utils.SharedPreferencesUtil.Read;

/**
 * 2018年2月20日 重构，UI重设计
 * 2019年1月24日 重构，UI重设计 V5.0
 */


public class MainActivity extends TustBaseActivity {




    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab)
    TabLayout tabLayout;


    int yourChoice;   //周选择变量
    private String update_message;
    private String version = "";
    private String log = "";
    private View view;
    private Context context;
    private Badge badge;
    private View itemView;
    private final int REQUEST_WRITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUI.setStatusBarUI(this,true);
        setContentView(R.layout.activity_main_v5);
        ButterKnife.bind(this);
        context = this;
        //加载首页界面
        initView();
        //首次登录
        SharedPreferencesUtil.save(this, "is_login", true);
        //更新桌面小部件
        updateWidget();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果没申请，那么需要申请
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //请求存储
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
            }
        }
    }

    //加载界面
    private void initView() {
        //加载标题
        String[] tabTitleString = getResources().getStringArray(R.array.mainTab);
        List<String> tabTitle = Arrays.asList(tabTitleString);
        //加载碎片
        List<Fragment> fragments = new ArrayList<Fragment>() {{
            add(FirstPageFragment.getInstance("1"));
            add(SchedulePageFragment.getInstance("2"));
            add(TaskPageFragment.getInstance("3"));
            add(FunctionPageFragment.getInstance("4"));
            add(new SchoolPageFragment());
        }};
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
                fragments,tabTitle));
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(Read(context,NORMALPAGE,0));
        Drawable d = null;
        for (int i = 0; i < tabLayout.getTabCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            switch (i){
                case 0:
                    d = getResources().getDrawable(R.drawable.main_tab_icon_1);
                    break;
                case 1:
                    d = getResources().getDrawable(R.drawable.main_tab_icon_2);
                    break;
                case 2:
                    d = getResources().getDrawable(R.drawable.main_tab_icon_3);
                    break;
                case 3:
                    d = getResources().getDrawable(R.drawable.main_tab_icon_4);
                    break;
                case 4:
                    d = getResources().getDrawable(R.drawable.main_tab_icon_5);
                    break;
            }
            tab.setIcon(d);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //设置项，配置底部栏显示与隐藏
        boolean hide = Read(context, HIDEBOTTOM,false);
        if(hide){
            tabLayout.setVisibility(View.GONE);
        }else{
            tabLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        // getUnReadNum();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }












    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功
                Toast.makeText(context, "mua~ 你真是个优秀的家伙哦！", Toast.LENGTH_SHORT).show();
            } else {
                //申请失败
                Toast.makeText(context, "不给我权限？那宝宝不给你更新APP了！哼！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void updateWidget() {
        //发送桌面小部件广播
        Intent updateIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(updateIntent);
    }

}
