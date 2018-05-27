package com.surine.tustbox.Fragment.PageFragment;

/**
 * Created by surine on 2017/9/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.Action_List_Adapter;
import com.surine.tustbox.Adapter.Recycleview.Box_new_adapter;
import com.surine.tustbox.Adapter.Recycleview.NoticeAdapter;
import com.surine.tustbox.Adapter.Recycleview.Today_Course_Adapter;
import com.surine.tustbox.Bean.Action;
import com.surine.tustbox.Bean.Box;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.Notice;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Box_info_Activty;
import com.surine.tustbox.UI.EmptyClassRoomActivity;
import com.surine.tustbox.UI.NetWorkActivity;
import com.surine.tustbox.UI.ScoreActiviy;
import com.surine.tustbox.UI.ToolbarActivity;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.surine.tustbox.Fragment.PageFragment.SencondPageFragment.c_info;


public class FisrtPageFragment extends Fragment {
    public static final String ARG = "Fragment";

    //今日课程列表
    @BindView(R.id.rec_today_course)
    RecyclerView recTodayCourse;


    @BindView(R.id.box_rec)
    RecyclerView boxRec;

    @BindView(R.id.rec_today_message)
    RecyclerView recTodayMessage;


    private List<Course_Info> mCourse_infos = new ArrayList<>();
    private List<Course_Info> mLast_infos = new ArrayList<>();
    View v;
    private Today_Course_Adapter today_adapter;
    private List<String> week_range;
    private int week;
    private View noView;
    private List<Box> mboxs = new ArrayList<>();
    private Box_new_adapter adapter;
    private List<Action> data = new ArrayList<>();
    private Action_List_Adapter actionAdapter;
    private List<Action> actionsFromServer = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private int positionInActions = 1;
    private String[] items;
    private String picIdsInAction;
    private List<Course_Info> mCourse_infos_From_db = new ArrayList<>();
    private Course_Info cInfoFromDB;
    private List<Notice> notices = new ArrayList<>();
    private NoticeAdapter noticeAdapter;

    public static FisrtPageFragment getInstance(String title) {
        FisrtPageFragment fragment = new FisrtPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
       // EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_index_page, container, false);
        ButterKnife.bind(this, v);

        //初始化课程部分
        initBlockCourse();

        //初始化工具部分
        initBlockTools();

        //初始化动态部分
        initBlockMessage();

        return v;
    }

    private void initBlockMessage() {
        //初始化视图
        initMessageView();
        //初始化数据
        initMessageData();

        noticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //startActivity(new Intent(getActivity(), ActionInfoActivity.class).putExtra(FormData.did,actions.get(position).getId()));
            }
        });
    }

    private void initMessageData() {
        Notice notice = new Notice(1,"【新通知】小天盒子更新通知","时间");
        notices.add(notice);
        noticeAdapter.notifyDataSetChanged();
    }


    private void initMessageView() {

        noticeAdapter = new NoticeAdapter(R.layout.item_notice,notices);
        recTodayMessage.setLayoutManager(new LinearLayoutManager(getActivity()));
        recTodayMessage.setAdapter(noticeAdapter);
    }


    private void initBlockTools() {
        //初始化数据
        initToolData();
        //初始化视图
        initToolView();
    }

    private void initToolData() {
        //成绩
        Box score = new Box(R.drawable.ic_action_score, "成绩", null, R.color.Tust_Green);
        Box box4 = new Box(R.drawable.ic_action_network, "网络", null, R.color.colorPrimary);
        Box box3 = new Box(R.drawable.ic_action_library, "图书", null, R.color.Tust_Red);
        Box box5 = new Box(R.drawable.ic_action_gp, "下载", null, R.color.Tust_more_color_1);
        //空教室
        Box emptyClassRoom = new Box(R.drawable.ic_action_empty_classroom, "空教室", null, R.color.Tust_more_color_1);
        mboxs.add(score);
        mboxs.add(box3);
        mboxs.add(box5);
        mboxs.add(box4);
        mboxs.add(emptyClassRoom);
    }


    private void initToolView() {
        boxRec.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        adapter = new Box_new_adapter(R.layout.item_box, mboxs);
        boxRec.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        //成绩页面
                        intent.setClass(getActivity(),ScoreActiviy.class);
                        startActivity(intent);
                        break;
                    case 1:
                        //默认页面
                        Snackbar.make(boxRec,"下线维护",Snackbar.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //默认页面
                        Snackbar.make(boxRec,"下线维护",Snackbar.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //网络页面
                        intent.setClass(getActivity(),NetWorkActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        //空教室页面
                        intent.setClass(getActivity(),EmptyClassRoomActivity.class);
                        startActivity(intent);
                        break;
                }


            }
        });
    }


    private void initBlockCourse() {
        try {
            //初始化数据
            initCourseData();
            //初始化视图
            initCourseView();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "今日课表加载错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCourseData() throws Exception{
        //初始化
        mCourse_infos_From_db.clear();
        mCourse_infos.clear();
        mLast_infos.clear();
        //查询周
        week = SharedPreferencesUtil.Read(getActivity(), "choice_week", 1);
        //查询周？所上的全部课
        mCourse_infos_From_db = DataSupport.where("week_number like ?", "%" + TimeUtil.GetWeekNumber() + "%").order("class_ asc").find(Course_Info.class);


        //周筛选(此时mCourse_infos_From_db是数据库所有数据，mLastList是c_info)
        for (int e = 0; e < mCourse_infos_From_db.size(); e++) {
            if (mCourse_infos_From_db.get(e) != null) {
                //正则解析:提取数字(上课周)
                week_range = PatternUtil.getNumber(mCourse_infos_From_db.get(e).getWeek());
                if (mCourse_infos_From_db.get(e).getWeek().contains("-")) {
                    //连周上课（当上课周不在这个区间）
                    if (!(Integer.parseInt(week_range.get(0)) <= week && Integer.parseInt(week_range.get(1)) >= week)) {
                        mCourse_infos_From_db.set(e, c_info);
                    }
                } else {
                    if (week_range.indexOf(week + "") == -1) {
                        mCourse_infos_From_db.set(e,c_info);
                    }
                }
            }
        }

//        for (Course_Info course_info: mCourse_infos_From_db) {
//            if(course_info != null)
//            Log.d("DEBUG", "数据库处理后数据"+course_info.toString());
//        }

        //初始化
        Course_Info[] coursesArray = new Course_Info[6];  //每天6节课
        for(int i = 0;i <= 5;i++){
            coursesArray[i] = c_info;
        }
        for (Course_Info c: mCourse_infos_From_db) {
            if(c != null){
                if(c.getClass_().contains("一")){
                    coursesArray[0] = c;
                }else if(c.getClass_().contains("二")){
                    coursesArray[1] = c;
                }else if(c.getClass_().contains("三")){
                    coursesArray[2] = c;
                }else if(c.getClass_().contains("四")){
                    coursesArray[3] = c;
                }else if(c.getClass_().contains("五")){
                    coursesArray[4] = c;
                }else if(c.getClass_().contains("六")){
                    coursesArray[5] = c;
                }
            }
        }
        mCourse_infos = Arrays.asList(coursesArray);

        for (Course_Info course_info: mCourse_infos) {
         //  Log.d("DEBUG", "最终数据"+course_info.toString());
            if(!course_info.getCourse_number().equals("*")){
                mLast_infos.add(course_info);
            }
        }

//        for (Course_Info course_info: mLast_infos) {
//            if(course_info != null)
//            Log.d("DEBUG", "最终数据"+course_info.toString());
//        }




    }

    private void initCourseView() {

        recTodayCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        today_adapter = new Today_Course_Adapter(R.layout.item_course_today_new, mLast_infos);
        recTodayCourse.setAdapter(today_adapter);

        noView = getActivity().getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) recTodayCourse.getParent(), false);
        noView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        today_adapter.setEmptyView(noView);


        today_adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(mLast_infos.get(position) != null){
                    startActivity(new Intent(getActivity(), ToolbarActivity.class).putExtra("course_id", mLast_infos.get(position).getId()));
                }
            }
        });
    }

}
