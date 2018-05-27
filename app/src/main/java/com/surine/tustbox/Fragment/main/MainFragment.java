package com.surine.tustbox.Fragment.main;

/**
 * Created by surine on 2017/9/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.tustbox.Adapter.ViewPager.SimpleFragmentPagerAdapter;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.Fragment.PageFragment.ThirdPageFragment;
import com.surine.tustbox.Fragment.PageFragment.SencondPageFragment;
import com.surine.tustbox.Fragment.PageFragment.FisrtPageFragment;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.SchZoneActivity;
import com.surine.tustbox.UI.SendActionActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;



public class MainFragment extends Fragment{
    public static final String ARG = "Fragment";
    View v;
    private TabLayout tab;
    private ViewPager viewpager;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    /**
     * 2017年11月14日22:25:20 更新混合UI调整
     * */
    Fragment mSchoolFragment = new ThirdPageFragment();
    private FloatingActionButton fab;

    public static MainFragment getInstance(String title){
        MainFragment fragment = new MainFragment();
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
        viewpager = (ViewPager) v.findViewById(R.id.viewpager);
        tab = (TabLayout)v. findViewById(R.id.tabs);
        fragments.add(FisrtPageFragment.getInstance("1"));
        fragments.add(SencondPageFragment.getInstance("2"));
        fragments.add(mSchoolFragment);
        titles.add("首页");
        titles.add("课表");
        titles.add("校园");
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getActivity().getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        tab.setupWithViewPager(viewpager);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"新功能即将上线",Snackbar.LENGTH_SHORT).show();
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
                switch (position){
                    case 0:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v,"新功能即将上线",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 1:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getActivity(), SchZoneActivity.class));
                            }
                        });
                        break;
                    case 2:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getActivity(), SendActionActivity.class));
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

    @Subscribe
    public void GetMessage(SimpleEvent event){
        if(event.getId()==6) {
            //总线事件处理
           // tab.setBackground(new ColorDrawable(Integer.parseInt(event.getMessage())));
        }
    }
}
