package com.surine.tustbox.UI.View.Header;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.surine.tustbox.Adapter.Recycleview.TaskAdapter;
import com.surine.tustbox.Adapter.Recycleview.TodaytaskAdapter;
import com.surine.tustbox.App.Data.Constants;
import com.surine.tustbox.Helper.Dao.TaskDao;
import com.surine.tustbox.Helper.Utils.LogUtil;
import com.surine.tustbox.Pojo.Task;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.CourseInfoActivity;
import com.surine.tustbox.UI.Activity.TaskInfoActivity;

import java.util.List;

/**
 * Created by Surine on 2019/1/30.
 */

public class HeaderTask {
    public static View getInstance(final Context context, RecyclerView r) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_task, (ViewGroup) r.getParent(), false);
        TaskDao taskDao = new TaskDao();
        final List<Task> taskList = taskDao.getTaskListTimeASCWhereTimeLimit();
        RecyclerView recyclerView = view.findViewById(R.id.myTodayTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        TodaytaskAdapter adapter = new TodaytaskAdapter(R.layout.item_today_task_v5,taskList);
        recyclerView.setAdapter(adapter);

        adapter.setEmptyView(R.layout.view_empty_card, recyclerView);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
            if (taskList.get(pos) == null) {
                return;
            } else {
                LinearLayout layout = view.findViewById(R.id.taskCard);
                Intent intent = new Intent(context, TaskInfoActivity.class)
                        .putExtra(Constants.TASK_ID, taskList.get(pos).getId())
                        .putExtra(Constants.TASK_POSITION, pos);
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, layout, "backRelative").toBundle());
            }
            }
        });

        return view;
    }
}
