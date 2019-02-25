package com.surine.tustbox.UI.Fragment.PageFragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Course_new_adapter;
import com.surine.tustbox.Helper.Dao.CourseInfoDao;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.Pojo.CourseInfoHelper;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.CourseInfoActivity;
import com.surine.tustbox.UI.Activity.SchZoneActivity;
import com.surine.tustbox.Helper.Utils.SharedPreferencesUtil;
import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;
import com.surine.tustbox.UI.View.VgTopbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.surine.tustbox.App.Data.Constants.CHOOSE_WEEK;
import static com.surine.tustbox.App.Data.Constants.weekStr;
import static com.surine.tustbox.Helper.Utils.TimeUtil.E;
import static com.surine.tustbox.Helper.Utils.TimeUtil.dd2;
import static com.surine.tustbox.Helper.Utils.TimeUtil.getDate;
import static com.surine.tustbox.Helper.Utils.TimeUtil.getDateBeforeOrAfter;
import static com.surine.tustbox.Helper.Utils.TimeUtil.getStringByTimeString;
import static com.surine.tustbox.Helper.Utils.TimeUtil.getWeekNumber;
import static com.surine.tustbox.Helper.Utils.TimeUtil.yMdHm;

/**
 * Created by surine on 2017/3/29.
 */

public class SchedulePageFragment extends Fragment {
    private static final String ARG_ = "SchedulePageFragment";
    @BindView(R.id.topbar)
    VgTopbar topbar;
    private CourseInfoDao courseInfoDao = new CourseInfoDao();
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


    @BindView(R.id.week_word)
    LinearLayout weekWord;
    @BindView(R.id.text_course_1)
    TextView textCourse1;
    @BindView(R.id.text_course_2)
    TextView textCourse2;
    @BindView(R.id.text_course_3)
    TextView textCourse3;
    @BindView(R.id.text_course_4)
    TextView textCourse4;
    @BindView(R.id.text_course_5)
    TextView textCourse5;
    @BindView(R.id.text_course_6)
    TextView textCourse6;
    @BindView(R.id.cardView2)
    CardView cardView2;

    private List<String> mXdata = new ArrayList<>();
    private List<CourseInfoHelper> mCourseData = new ArrayList<>();
    private View v;
    private Course_new_adapter adpter;
    private int yourChoice;


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
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_schedule_page, container, false);
        //绑定knife
        ButterKnife.bind(this, v);
        //加载顶部栏
        initTopbar();
        //加载课程数据
        initData();
        //加载课程视图
        initView(v);
        //加载背景图片
        loadImage();
        //加载X时间轴
        loadX();

        return v;
    }

    private void initTopbar() {
        topbar.setTitle("第" + new TustBoxUtil(getActivity()).getWeek() + "周")
                .setLeftIcon(R.drawable.ic_add_black_24dp)
                .setRightIcon(R.drawable.ic_change_week_24dp)
                .setListener(new VgTopbar.OnClickListener() {
                    @Override
                    public void leftButton() {
                        addCourse();
                    }

                    @Override
                    public void rightButton() {
                       changeWeek();
                    }
                });
    }

    private void loadX() {
        //首先获取到今天周几
        String week = getDate(E);
        //获取周几的数字（周日为7）
        int weekNumber = Integer.parseInt(getWeekNumber());
        mXdata.clear();

        for (int i = 1; i < weekNumber; i++) {
            mXdata.add(getStringByTimeString(getDateBeforeOrAfter(getDate(yMdHm), weekNumber - i, false), dd2));
        }
        mXdata.add(week);
        for (int j = weekNumber + 1; j <= 7; j++) {
            mXdata.add(getStringByTimeString(getDateBeforeOrAfter(getDate(yMdHm), j - weekNumber, true), dd2));
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
    @SuppressLint("ClickableViewAccessibility")
    private void initView(final View v) {
        //配置课表布局 7列
        mCourseTable.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        //初始化adpater，设置adpter
        adpter = new Course_new_adapter(R.layout.item_course_card, mCourseData);
        mCourseTable.setAdapter(adpter);
        adpter.setEmptyView(R.layout.view_empty_2, mCourseTable);
        adpter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                if (mCourseData.get(pos) == null) {
                    return;
                }
                if (mCourseData.get(pos).getId() == -1) {
                    ToastUtil.show("发生了小错误，请重启APP");
                    initView(v);
                } else {
                    RelativeLayout relativeLayout = view.findViewById(R.id.item_course_relativelayout);
                    Intent intent = new Intent(getActivity(), CourseInfoActivity.class).putExtra(Constants.COURSE_ID, mCourseData.get(pos).getId());
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
     * 2019年1月25日20点59分数据获取封装
     */
    private void initData() {
        int week = new TustBoxUtil(getActivity()).getWeek();
        mCourseData.clear();
        List<CourseInfoHelper> mHelperList = courseInfoDao.getWeekCourse(week);
        for (CourseInfoHelper c : mHelperList) {
            mCourseData.add(c);
        }
    }


    //加载背景图
    private void loadImage() {
        String imagePath = SharedPreferencesUtil.Read(getActivity(), Constants.BACKGROUND_IMG_SAVE, null);
        if (imagePath != null) {
            Glide.with(this).
                    load(imagePath)
                    .asBitmap()
                    .into(new BitmapImageViewTarget(mBackGroundPicture) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void addCourse() {
        Intent intent = new Intent(getActivity(), SchZoneActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.main_enter_anim,
                R.anim.main_exit_anim);
    }

    //显示列表对话框
    private void changeWeek() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择本周");
        yourChoice = -1;
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(weekStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoice = which;
                if (yourChoice != -1) {
                    //储存，通知更新UI
                    SharedPreferencesUtil.save(getActivity(), CHOOSE_WEEK, yourChoice + 1);
                    ToastUtil.show(getString(R.string.choose_) + weekStr[yourChoice]);
                    topbar.setTitle("第" + (SharedPreferencesUtil.Read(getActivity(), CHOOSE_WEEK, 0)) + "周");
                    updateWidget();
                    initData();
                    adpter.notifyDataSetChanged();
                }
            }
        }).show();
    }


    private void updateWidget() {
        //发送桌面小部件广播
        Intent updateIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
        getActivity().sendBroadcast(updateIntent);
    }
}
