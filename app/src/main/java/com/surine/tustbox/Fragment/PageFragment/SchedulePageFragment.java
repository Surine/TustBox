package com.surine.tustbox.Fragment.PageFragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Course_new_adapter;
import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.EventBusBean.SimpleEvent;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.CourseInfoActivity;
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

import static com.surine.tustbox.UI.LoginActivity.TAG;
import static com.surine.tustbox.Util.TimeUtil.*;
import static com.surine.tustbox.Util.TimeUtil.E;
import static com.surine.tustbox.Util.TimeUtil.dd;
import static com.surine.tustbox.Util.TimeUtil.getDate;
import static com.surine.tustbox.Util.TimeUtil.yMdHm;

/**
 * Created by surine on 2017/3/29.
 */

public class SchedulePageFragment extends Fragment {
    private static final String ARG_ = "SchedulePageFragment";
    int choose_week;
    CourseInfoHelper course_info;
    String number_b = ",";
    @BindView(R.id.back_ground_picture)
    ImageView mBackGroundPicture;

    @BindView(R.id.scrollview)
    ScrollView mScrollview;
    @BindView(R.id.course_table)
    RecyclerView mCourseTable;
    @BindView(R.id.mon)
    TextView mon;
    @BindView(R.id.tus)
    TextView tus;
    @BindView(R.id.wes)
    TextView wes;
    @BindView(R.id.thr)
    TextView thr;
    @BindView(R.id.fri)
    TextView fri;
    @BindView(R.id.sta)
    TextView sta;
    @BindView(R.id.sun)
    TextView sun;

    private List<String> mXdata = new ArrayList<>();
    private List<CourseInfoHelper> mCourseList = new ArrayList<>();
    private List<CourseInfoHelper> mLastList = new ArrayList<>();
    int user;
    private View v;
    private Course_new_adapter adpter;

    public static SchedulePageFragment getInstance(String title) {
        SchedulePageFragment fra = new SchedulePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
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
        v = inflater.inflate(R.layout.fragment_schedule_page, container, false);
        //绑定knife
        ButterKnife.bind(this, v);
        //加载课程数据
        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //加载课程视图
        initView(v);
        //加载背景图片
        load_image();
        //加载X时间轴
        loadX();

        return v;
    }

    private void loadX() {
        //首先获取到今天周几
        String week = getDate(E);
        //获取周几的数字（周日为7）
        int weekNumber = Integer.parseInt(getWeekNumber());
        mXdata.clear();

        for(int i = 1; i < weekNumber; i++){
           mXdata.add(getStringByTimeString(getDateBeforeOrAfter(getDate(yMdHm),weekNumber - i,false), dd2));
        }
        mXdata.add(week);
        for (int j = weekNumber + 1; j <= 7; j++){
            mXdata.add(getStringByTimeString(getDateBeforeOrAfter(getDate(yMdHm),j - weekNumber,true), dd2));
        }

        mon.setText(mXdata.get(0));
        tus.setText(mXdata.get(1));
        wes.setText(mXdata.get(2));
        thr.setText(mXdata.get(3));
        fri.setText(mXdata.get(4));
        sta.setText(mXdata.get(5));
        sun.setText(mXdata.get(6));
    }


    //init the view
    private void initView(final View v) {
        mCourseTable.setLayoutManager(new GridLayoutManager(getActivity(), 7));

        adpter = new Course_new_adapter(R.layout.item_course_card, mLastList);
        mCourseTable.setAdapter(adpter);
        adpter.setEmptyView(R.layout.view_empty_2,mCourseTable);
        adpter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                if(mLastList.get(pos) == null){
                    return;
                }
                if (mLastList.get(pos).getId() == -1) {
                    Toast.makeText(getActivity(), "系统回收错误，自动刷新课表", Toast.LENGTH_SHORT).show();
                    initView(v);
                } else {
                    RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.item_course_relativelayout);
                    Intent intent = new Intent(getActivity(), CourseInfoActivity.class).putExtra(Constants.COURSE_ID, mLastList.get(pos).getId());
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), relativeLayout, "backRelative").toBundle());
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
    private void initData() throws Exception {
        //清空列表
        mCourseList.clear();
        mLastList.clear();

        //读取数据库全部课程
        mCourseList = DataSupport.findAll(CourseInfoHelper.class);
        //加载本周
        choose_week = SharedPreferencesUtil.Read(getActivity(), Constants.CHOOSE_WEEK, 1);

        //初始化全部表格
        for (int j = 0; j < 42; j++) {
            mLastList.add(null);
        }

        //周筛选(此时mCourseList是数据库所有数据，mLastList是全为null)
        for (int e = 0; e < mCourseList.size(); e++) {
            Log.d(ARG_,"数据"+mCourseList.get(e).toString());

            //判空
            if (mCourseList.get(e) != null) {
                String week = mCourseList.get(e).getWeekDescription();
                //周判空
                if(week.isEmpty()){
                    return;
                }
                //确定是本周上课
                int temp = 0;
                if(choose_week >= 1){
                    String getWeekChar = week.substring(choose_week - 1,choose_week);
                    if(getWeekChar.isEmpty()){
                        return;
                    }
                    temp = Integer.parseInt(getWeekChar);
                }else{
                    Toast.makeText(getActivity(),"请联网更新周",Toast.LENGTH_SHORT).show();
                }
                CourseInfoHelper courseInfoHelper = mCourseList.get(e);
                if (temp == 1) { //上课周
                    if (courseInfoHelper != null) {

                        //判空
                        if(courseInfoHelper.getClassDay().isEmpty()||courseInfoHelper.getClassSessions().isEmpty()){
                            return;
                        }

                        if (courseInfoHelper.getStudyModeName().contains("正常") && !(courseInfoHelper.getCoureNumber().contains("WL"))) {
                            mLastList.set((Integer.parseInt(courseInfoHelper.getClassDay()) - 1)
                                    + (Op((Integer.parseInt(courseInfoHelper.getClassSessions()))) - 1) * 7, courseInfoHelper);
                        }
                        //4节课
                        if (courseInfoHelper.getContinuingSession().contains("4")) {
                            mLastList.set((Integer.parseInt(courseInfoHelper.getClassDay()) - 1)
                                    + (Op((Integer.parseInt(courseInfoHelper.getClassSessions()))) - 1) * 7 + 7, courseInfoHelper);
                        }

                    }//再次判空

                }//本周上课

            }//判空结束

        } //循环结束
    }

    //数据操作
    private int Op(int i) {
        return (i + 1) / 2;
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
                                    } else {
                                        SharedPreferencesUtil.Save(getActivity(), "TOOLBAR_C", R.color.colorPrimary);
                                    }
                                }
                            });
                        }
                    });
        }
    }


    @Subscribe
    public void GetMessage(SimpleEvent event) {
        if (event.getId() == 0) {
            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            adpter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
