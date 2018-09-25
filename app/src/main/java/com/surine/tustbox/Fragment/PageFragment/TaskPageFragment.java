package com.surine.tustbox.Fragment.PageFragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.TaskAdapter;
import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.EventBusBean.TaskCURD;
import com.surine.tustbox.Bean.Notice;
import com.surine.tustbox.Bean.Task;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;
import com.surine.tustbox.Data.UrlData;
import com.surine.tustbox.Manager.CourseManager;
import com.surine.tustbox.Manager.TaskManager;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.CourseInfoActivity;
import com.surine.tustbox.UI.NoticeActivity;
import com.surine.tustbox.UI.TaskInfoActivity;
import com.surine.tustbox.Util.GsonUtil;
import com.surine.tustbox.Util.HttpUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.surine.tustbox.Util.TimeUtil.yMd;

/**
 * Created by Surine on 2018/7/13.
 * 首页任务页面（完整的任务时间轴）
 */

public class TaskPageFragment extends Fragment {
    public static final String ARG = "TaskPageFragment";
    private static final int COURSE_TAG = -2;  //标志课程
    @BindView(R.id.textView23)
    TextView textView23;
    @BindView(R.id.taskList)
    RecyclerView taskList;
    private View v;
    private String noticeData;
    private TaskAdapter taskAdapter;
    private List<Task> mTasks = new ArrayList<>();

    public static TaskPageFragment getInstance(String title) {
        TaskPageFragment fragment = new TaskPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        v = inflater.inflate(R.layout.fragment_task_page, container, false);
        ButterKnife.bind(this, v);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initMessage();  //加载服务器通知
            }
        }).start();

        //加载任务列表
        loadTaskList();
        return v;
    }

    private void loadTaskList() {
        taskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //loadDbData();   //加载数据库数据
        taskAdapter = new TaskAdapter(R.layout.item_task, mTasks);
        taskList.setAdapter(taskAdapter);
        taskAdapter.setEmptyView(R.layout.view_empty_2,taskList);
        taskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.backRelative);
                if (mTasks.get(position).getTask_type() == COURSE_TAG) {
                    Intent intent = new Intent(getActivity(), CourseInfoActivity.class).putExtra(Constants.COURSE_ID, mTasks.get(position).getId());
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), relativeLayout, "backRelative").toBundle());
                } else {
                    Intent intent = new Intent(getActivity(), TaskInfoActivity.class);
                    intent.putExtra(Constants.TASK_ID, mTasks.get(position).getId());
                    intent.putExtra(Constants.TASK_POSITION, position);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), relativeLayout, "backRelative").toBundle());
                }
            }
        });
        addDbData();  //设置数据

    }


    public void addDbData() {
        mTasks.clear();
        List<Task> taskDataList = TaskManager.getTaskListTimeASCWhereTimeLimit();
        for (Task task : taskDataList) {
            mTasks.add(task);
        }
        taskAdapter.notifyDataSetChanged();
        //添加今日的课程
        addTodayCourse();
    }

    private void addTodayCourse() {
        List<CourseInfoHelper> mCourse = CourseManager.getTodayCourse(getActivity());
        for (int i = mCourse.size() - 1; i >= 0; i--) {
            CourseInfoHelper course_info = mCourse.get(i);
            Task task = new Task();
            task.setId(course_info.getId());
            task.setTask_name(course_info.getCourseName());
            task.setTask_type(COURSE_TAG);  //-1代表课程
            task.setTask_postion(course_info.getTeachingBuildingName() + course_info.getClassroomName());
            task.setTask_time(TimeUtil.getDate(yMd) + " " + TimeUtil.getCourseTime(course_info.getClassSessions()));
            task.setTask_color(course_info.getJwColor());
            if(mTasks.size() == 0){
                mTasks.add(task);
                taskAdapter.notifyItemInserted(0);
            }else{
                for (int j = 0; j < mTasks.size(); j++) {
                    if (TimeUtil.compareDate(task.getTask_time(), mTasks.get(j).getTask_time()) <= 0) {
                        mTasks.add(j, task);
                        taskAdapter.notifyItemInserted(j);
                        break;
                    }
                }
            }
        }

    }


    private void initMessage() {
        HttpUtil.get(UrlData.getFirstNoticeFromServer).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject.getInt(FormData.JCODE) == 200) {
                        noticeData = jsonObject.getString(FormData.JDATA);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final Notice noticeServer = GsonUtil.parseJsonWithGsonToList(noticeData, Notice.class).get(0);
                if(getActivity() == null){
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String title = null;
                        if (noticeServer.getTitle() != null) {
                            title = noticeServer.getTitle();
                        }
                        if (title != null && !title.equals(""))
                            textView23.setText(noticeServer.getTitle());
                        else
                            textView23.setText("暂无新通知");
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(TaskCURD taskCURD) {
        if (taskCURD.getTag() == taskCURD.DELETE) {
            //删除
            deleteUI(taskCURD.getMessage());
        } else if (taskCURD.getTag() == taskCURD.ADD) {
            addUI(taskCURD.getMessage());
        } else if (taskCURD.getTag() == taskCURD.MODIFY) {
            modifyUI(taskCURD.getMessage());
        }
    }

    private void modifyUI(int message) {
        //这里暂时用重新加载来处理数据
        addDbData();
    }

    private void addUI(int message) {
        //这里暂时用重新加载来处理数据
        addDbData();
    }

    //删除刷新
    private void deleteUI(int postion) {
        mTasks.remove(postion);
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.textView23)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), NoticeActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), textView23, "backRelative").toBundle());
    }
}
