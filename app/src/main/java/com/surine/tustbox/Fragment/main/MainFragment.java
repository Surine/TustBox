package com.surine.tustbox.Fragment.main;

/**
 * Created by surine on 2017/9/16
 * 2018年8月6日 ：精简规整本类
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Fragment.PageFragment.FunctionPageFragment;
import com.surine.tustbox.Fragment.PageFragment.SchedulePageFragment;
import com.surine.tustbox.Fragment.PageFragment.TaskPageFragment;
import com.surine.tustbox.Fragment.PageFragment.SchoolPageFragment;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.AddTaskActivity;
import com.surine.tustbox.UI.SchZoneActivity;
import com.surine.tustbox.UI.SendActionActivity;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainFragment extends Fragment {
    public static final String ARG = "Fragment";
    View v;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    Unbinder unbinder;
    private Toolbar toolbar;

    SimpleFragmentPagerAdapter pagerAdapter;
    //页面
    private List<Fragment> fragments = new ArrayList<Fragment>() {{
        add(TaskPageFragment.getInstance("TaskPageFragment"));
        add(SchedulePageFragment.getInstance("SchedulePageFragment"));
        add(FunctionPageFragment.getInstance("FunctionPageFragment"));
        add(new SchoolPageFragment());
    }};
    //标题
    private List<String> titles = new ArrayList<String>() {{
        add("任务");
        add("课表");
        add("盒子");
        add("广场");
    }};

    private FloatingActionButton fab;

    public static MainFragment getInstance(String title) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main_container, container, false);
        unbinder = ButterKnife.bind(this, v);
        //父activity控件
        fab = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButton);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        initViewPager();
        return v;
    }

    private void initViewPager() {
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getActivity().getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(4);
        tabs.setupWithViewPager(viewpager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTask();
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动监听器
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页卡选中监听器
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        //设置当前月份
                        toolbar.setTitle(TimeUtil.getNowMonthCC());
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startTask();
                            }
                        });
                        break;
                    case 1:
                        toolbar.setTitle(SharedPreferencesUtil.Read(getActivity(), Constants.CHOOSE_WEEK, 0) + "周");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startAddCourse();
                            }
                        });
                        break;
                    case 2:
                        toolbar.setTitle("功能");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        break;
                    case 3:
                        toolbar.setTitle("校园");
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startSendAction();
                            }
                        });
                        break;
                }
            }

            //滚动状态变化监听器
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void startSendAction() {
        Intent intent = new Intent(getActivity(), SendActionActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.main_enter_anim,
                R.anim.main_exit_anim);
    }

    private void startAddCourse() {
        Intent intent = new Intent(getActivity(), SchZoneActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.main_enter_anim,
                R.anim.main_exit_anim);
    }

    private void startTask() {
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), fab, "mainEdit").toBundle());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.main_enter_anim,
                R.anim.main_exit_anim);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
