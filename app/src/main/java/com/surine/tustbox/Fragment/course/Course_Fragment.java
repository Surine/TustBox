package com.surine.tustbox.Fragment.course;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.surine.tustbox.Adapter.Recycleview.Course_new_adapter;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Eventbus.SimpleEvent;
import com.surine.tustbox.R;
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

/**
 * Created by surine on 2017/3/29.
 */

public class Course_Fragment extends Fragment {
    private static final String ARG_ = "Course_Fragment";
    int choose_week;
    Course_Info course_info;
    int position1,position2;
    String number_b = ",";
    int week_number_for_todays_course = 0;
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

    public static Course_Fragment getInstance(String title) {
        Course_Fragment fra = new Course_Fragment();
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
        ButterKnife.bind(this, v);
        initData();
        initView(v);  //init the view
        load_image();
        return v;
    }


    //init the view
    private void initView(View v) {
        mCourseTable.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        adpter = new Course_new_adapter(R.layout.item_course_card, mLastList);
        mCourseTable.setAdapter(adpter);
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

    //load the Curriculum schedule
    private void initData() {
        mCourseList.clear();
        mLastList.clear();
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
        week_number_for_todays_course =Integer.parseInt(TimeUtil.GetWeekNumber());
        //read the sqlite list
        mCourseList = DataSupport.findAll(Course_Info.class);
        //load the week
        choose_week = SharedPreferencesUtil.Read(getActivity(), "choice_week", 0);

        //表格为6*7
        for (int j = 0; j < 42; j++) {
            mLastList.add(null);
        }
        for (int i = 0; i < mCourseList.size(); i++) {
            try {
                course_info = mCourseList.get(i);
                if (course_info.getUser() == user) {
                    //out of some cases(they may cause App ANR,for example:network course,etc)
                    if (course_info.getMethod().contains("正常") && !(course_info.getCourse_number().contains("WL"))) {
                        mLastList.set(Integer.parseInt(String.valueOf(course_info.getWeek_number().charAt(2))) - 1
                                + (TimeUtil.GetNumber(course_info.getClass_()) - 1) * 7, course_info);
                        //大物实验
                        if (course_info.getClass_count().contains("4")) {
                            mLastList.set(position1 + position2 + 7, course_info);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        R.string.error_get_data, Toast.LENGTH_LONG).show();
            }


        }

        //周筛选
        for (int e = 0; e < mLastList.size(); e++) {
            if (mLastList.get(e) != null) {
                if (mLastList.get(e).getWeek().contains("-")) {

                    //正则解析:提取数字
                    List<String> week_range = PatternUtil.getNumber(mLastList.get(e).getWeek());

                    for (int i = Integer.parseInt(week_range.get(0)); i <= Integer.parseInt(week_range.get(1)); i++) {
                        number_b += (i + ",");
                    }
                    if (!(number_b.contains("," + choose_week + ","))) {
                        mLastList.set(e, null);
                    }
                    number_b = ",";
                } else {
                    String help_week = ("," + mLastList.get(e).getWeek().substring(2, mLastList.get(e).getWeek().length() - 2) + ",");
                    if (!(help_week.contains("," + choose_week + ","))) {
                        mLastList.set(e, null);
                    }
                }
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
                                    }else {
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
        if (event.getId() == 5) {
            initData();
            adpter.notifyDataSetChanged();
        }
    }
}
