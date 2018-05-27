package com.surine.tustbox.Fragment.PageFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Course_new_adapter;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.PanActivity;
import com.surine.tustbox.UI.SchZoneActivity;
import com.surine.tustbox.UI.ToolbarActivity;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by surine on 2017/3/29.
 */

public class SencondPageFragment extends Fragment {
    private static final String ARG_ = "SencondPageFragment";
    int choose_week;
    Course_Info course_info;
    String number_b = ",";
    @BindView(R.id.back_ground_picture)
    ImageView mBackGroundPicture;
    @BindView(R.id.mon)
    TextView mMon;
    @BindView(R.id.tus)
    TextView mTus;
    @BindView(R.id.wes)
    TextView mWes;
    @BindView(R.id.thr)
    TextView mThr;
    @BindView(R.id.fri)
    TextView mFri;
    @BindView(R.id.sta)
    TextView mSta;
    @BindView(R.id.sun)
    TextView mSun;
    @BindView(R.id.week_word)
    LinearLayout mWeekWord;
    @BindView(R.id.text_course_1)
    TextView mTextCourse1;
    @BindView(R.id.text_course_2)
    TextView mTextCourse2;
    @BindView(R.id.text_course_3)
    TextView mTextCourse3;
    @BindView(R.id.text_course_4)
    TextView mTextCourse4;
    @BindView(R.id.text_course_5)
    TextView mTextCourse5;
    @BindView(R.id.text_course_6)
    TextView mTextCourse6;
    @BindView(R.id.scrollview)
    ScrollView mScrollview;
    @BindView(R.id.course_table)
    RecyclerView mCourseTable;

    private List<Course_Info> mCourseList = new ArrayList<>();
    private List<Course_Info> mLastList = new ArrayList<>();
    int user;
    static String other_user_string = "-1";
    private static final String EXTRA = "other_user";
    private View v;
    private Course_new_adapter adpter;
    private List<String> week_range;
    public static Course_Info c_info = new Course_Info
            ("*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", "*", -1, "*", "*", -1, -1);

    public static SencondPageFragment getInstance(String title) {
        SencondPageFragment fra = new SencondPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        other_user_string = title;
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        v = inflater.inflate(R.layout.fragment_course, container, false);
        //绑定knife
        ButterKnife.bind(this, v);
        //加载课程数据
        initData();
        //加载课程视图
        initView(v);
        //加载背景图片
        load_image();

        return v;
    }


    //init the view
    private void initView(final View v) {
        mCourseTable.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        adpter = new Course_new_adapter(R.layout.item_course_card, mLastList);
        mCourseTable.setAdapter(adpter);

        adpter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                if (mLastList.get(pos).getId() == -1) {
                    Toast.makeText(getActivity(), "系统回收错误，自动刷新课表", Toast.LENGTH_SHORT).show();
                    initView(v);
                } else {
                    startActivity(new Intent(getActivity(), ToolbarActivity.class).putExtra("course_id", mLastList.get(pos).getId()));
                }
            }
        });
        //列表滑动监听
        mCourseTable.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //绑定滚动视图
                mScrollview.scrollBy(0, dy);
            }
        });


        //屏蔽触摸滑动
        mScrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

    }


    /**
     * 2017年10月23日19:33:09加载课程算法优化
     * 2018年3月12日16点51分加载课程算法优化
     */
    private void initData() {
        //清空列表
        mCourseList.clear();
        mLastList.clear();
        //加载默认用户
        if (other_user_string.equals("1")) {
            user = 0;
        } else {
            if (SharedPreferencesUtil.Read(getActivity(), "user_flag", 0) == 0) {
                user = 0;
                SharedPreferencesUtil.Save(getActivity(), "user_flag", user);
            } else {
                user = 1;
                SharedPreferencesUtil.Save(getActivity(), "user_flag", user);
            }
        }
        //读取数据库全部课程
        mCourseList = DataSupport.findAll(Course_Info.class);
        //加载本周
        choose_week = SharedPreferencesUtil.Read(getActivity(), "choice_week", 1);

        //初始化全部表格
        for (int j = 0; j < 42; j++) {
            mLastList.add(c_info);
        }

//        for (Course_Info course_info: mCourseList) {
//            Log.d("DEBUG","从数据读取"+course_info.toString());
//        }

        //周筛选(此时mCourseList是数据库所有数据，mLastList是c_info)
        for (int e = 0; e < mCourseList.size(); e++) {
            if (mCourseList.get(e) != null) {
                //正则解析:提取数字(上课周)
                week_range = PatternUtil.getNumber(mCourseList.get(e).getWeek());
                if (mCourseList.get(e).getWeek().contains("-")) {
                    //连周上课（当上课周不在这个区间）
                    if (!(Integer.parseInt(week_range.get(0)) <= choose_week && Integer.parseInt(week_range.get(1)) >= choose_week)) {
                        mCourseList.set(e,c_info);
                    }
                } else {
                    if (week_range.indexOf(choose_week + "") == -1) {
                        //不是在本周上课
                     //   Log.d("DEBUG",mCourseList.get(e).getCourse_name()+"被移除");
                        mCourseList.set(e,c_info);
                    }
                }
            }
        }


         for (int i = 0; i < mCourseList.size(); i++) {
            try {
                course_info = mCourseList.get(i);
                if (course_info != null && !course_info.getCourse_number().equals("*") && course_info.getUser() == user) {
                    //out of some cases(they may cause App ANR,for example:network course,etc)
                    if (course_info.getMethod().contains("正常") && !(course_info.getCourse_number().contains("WL"))) {
                        mLastList.set(Integer.parseInt(PatternUtil.getNumber(course_info.getWeek_number()).get(0))- 1
                                + (TimeUtil.GetNumber(course_info.getClass_()) - 1) * 7, course_info);
                        //4节课
                        if (course_info.getClass_count().contains("4")) {
                            mLastList.set(Integer.parseInt(PatternUtil.getNumber(course_info.getWeek_number()).get(0)) - 1
                                    + (TimeUtil.GetNumber(course_info.getClass_()) - 1) * 7 + 7, course_info);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        R.string.error_get_data, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void load_image() {
        String image_path = SharedPreferencesUtil.Read(getActivity(), "my_picture_path", null);
        if (image_path != null) {
            Glide.with(this).
                    load(image_path)
                    .asBitmap()
                    .into(new BitmapImageViewTarget(mBackGroundPicture) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette p) {
                                    Palette.Swatch vibrant = p.getVibrantSwatch();
                                    if (vibrant != null) {
                                        SharedPreferencesUtil.Save(getActivity(), "TOOLBAR_C", vibrant.getRgb());
                                        EventBus.getDefault().post(new SimpleEvent(6, vibrant.getRgb() + ""));
                                        SetTextColor(vibrant.getTitleTextColor());
                                    } else {
                                        SharedPreferencesUtil.Save(getActivity(), "TOOLBAR_C", R.color.colorPrimary);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    private void SetTextColor(int titleTextColor) {
        mMon.setTextColor(titleTextColor);
        mTus.setTextColor(titleTextColor);
        mWes.setTextColor(titleTextColor);
        mThr.setTextColor(titleTextColor);
        mFri.setTextColor(titleTextColor);
        mSta.setTextColor(titleTextColor);
        mSun.setTextColor(titleTextColor);
        mTextCourse1.setTextColor(titleTextColor);
        mTextCourse2.setTextColor(titleTextColor);
        mTextCourse3.setTextColor(titleTextColor);
        mTextCourse4.setTextColor(titleTextColor);
        mTextCourse5.setTextColor(titleTextColor);
        mTextCourse6.setTextColor(titleTextColor);
    }

    @Subscribe
    public void GetMessage(SimpleEvent event) {
        if (event.getId() == 0) {
            initData();
            adpter.notifyDataSetChanged();
        }
    }


}
