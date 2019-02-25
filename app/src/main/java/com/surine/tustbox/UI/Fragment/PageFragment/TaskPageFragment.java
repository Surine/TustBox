package com.surine.tustbox.UI.Fragment.PageFragment;


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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.TaskAdapter;
import com.surine.tustbox.Helper.Dao.TaskDao;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.App.Data.FormData;
import com.surine.tustbox.App.Data.UrlData;
import com.surine.tustbox.Pojo.EventBusBean.TaskCURD;
import com.surine.tustbox.Pojo.Notice;
import com.surine.tustbox.Pojo.Task;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.AddTaskActivity;
import com.surine.tustbox.UI.Activity.CourseInfoActivity;
import com.surine.tustbox.UI.Activity.TaskInfoActivity;
import com.surine.tustbox.Helper.Utils.JsonUtil;
import com.surine.tustbox.Helper.Utils.HttpUtil;
import com.surine.tustbox.UI.View.VgTopbar;

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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Surine on 2018/7/13.
 * 首页任务页面（完整的任务时间轴）
 */

public class TaskPageFragment extends Fragment {
    public static final String ARG = "TaskPageFragment";
    private static final int COURSE_TAG = -2;  //标志课程

    @BindView(R.id.taskList)
    RecyclerView taskList;
    @BindView(R.id.vgTopbar)
    VgTopbar vgTopbar;

    private View v;
    private String noticeData;
    private TaskAdapter taskAdapter;
    private List<Task> mTasks = new ArrayList<>();
    private TaskDao taskDao;

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
        taskDao = new TaskDao();
        ButterKnife.bind(this, v);

        vgTopbar.setLeftIconGone(true).setTitle("任务").setRightIcon(R.drawable.ic_add_black_24dp)
                .setListener(new VgTopbar.OnClickListener() {
                    @Override
                    public void leftButton() {

                    }

                    @Override
                    public void rightButton() {
                        startTask();
                    }
                });

        //加载任务列表
        loadTaskList();
        return v;
    }

    private void loadTaskList() {
        taskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //loadDbData();   //加载数据库数据
        taskAdapter = new TaskAdapter(R.layout.item_task, mTasks);
        taskList.setAdapter(taskAdapter);
        taskAdapter.setEmptyView(R.layout.view_empty_2, taskList);
        taskAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RelativeLayout relativeLayout = view.findViewById(R.id.backRelative);
                Intent intent = new Intent(getActivity(), TaskInfoActivity.class);
                intent.putExtra(Constants.TASK_ID, mTasks.get(position).getId());
                intent.putExtra(Constants.TASK_POSITION, position);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), relativeLayout, "backRelative").toBundle());
            }
        });
        addDbData();  //设置数据

    }


    public void addDbData() {
        mTasks.clear();
        List<Task> taskDataList = taskDao.getTaskListTimeASCWhereTimeLimit();
        for (Task task : taskDataList) {
            mTasks.add(task);
        }
        taskAdapter.notifyDataSetChanged();

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


    private void startTask() {
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), fab, "mainEdit").toBundle());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.main_enter_anim,
                R.anim.main_exit_anim);
    }
}
