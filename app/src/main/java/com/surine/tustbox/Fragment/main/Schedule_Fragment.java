package com.surine.tustbox.Fragment.main;

/**
 * Created by surine on 2017/9/16.
 */

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Fragment.course.Course_Fragment;
import com.surine.tustbox.Fragment.course.Course_Fragment_Today;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/6/3.
 */

public class Schedule_Fragment extends Fragment{
    public static final String ARG = "Fragment";
    View v;
    private TabLayout tab;
    private ViewPager viewpager;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    public static Schedule_Fragment getInstance(String title){
        Schedule_Fragment fragment = new Schedule_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_schedule,container,false);
        initViewPager();
        return v;
    }

    private void initViewPager() {
        //1.实例化viewpager和tablayout
        viewpager = (ViewPager) v.findViewById(R.id.viewpager);
        tab = (TabLayout)v. findViewById(R.id.tabs);

        //2.使用fragment 的list集合管理碎片
        fragments.add(Course_Fragment_Today.getInstance("1"));
        fragments.add(Course_Fragment.getInstance("2"));
        //3.使用string的list集合来添加标题
        titles.add(TimeUtil.GetWeek());
        titles.add("周视图");


        //4.初始化适配器（传入参数：FragmentManager，碎片集合，标题）
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getActivity().getSupportFragmentManager(), fragments, titles);
        //5.设置viewpager适配器
        viewpager.setAdapter(pagerAdapter);
        //6.设置缓存
        viewpager.setOffscreenPageLimit(3);
        //7.关联viewpager
        tab.setupWithViewPager(viewpager);

    }

    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==6) {
            //总线事件处理
            tab.setBackground(new ColorDrawable(Integer.parseInt(event.getMessage())));
        }
    }
}
