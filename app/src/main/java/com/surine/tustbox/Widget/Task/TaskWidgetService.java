package com.surine.tustbox.Widget.Task;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Bean.Task;
import com.surine.tustbox.Manager.TaskManager;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskWidgetService extends RemoteViewsService {
    public static final String TAG = "TaskWidgetService";
    private List<Task> tasks = new ArrayList<>();
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this, intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private List<Course_Info > mLastList =new ArrayList<>();
        private List<Course_Info> mCourse_infos = new ArrayList<>();
        private Context mContext;
        private int mAppWidgetId;


        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // 获取 grid_view_item.xml 对应的RemoteViews
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_task);
            // 设置 第position位的“视图”的数据
            Task task = tasks.get(position);
            if (task != null) {
                rv.setTextViewText(R.id.task_name, task.getTask_name());
                rv.setTextViewText(R.id.task_point,task.getTask_postion());
                rv.setTextViewText(R.id.day,TimeUtil.getTimeSubDayString(task.getTask_time()));
            }else{
                rv.setTextViewText(R.id.task_name,"");
                rv.setTextViewText(R.id.task_point, "");
                rv.setTextViewText(R.id.day, "");
            }
            return rv;
        }



        @Override
        public void onCreate() {
            // 初始化“集合视图”中的数据
        }

        @Override
        public int getCount() {
            // 返回“集合视图”中的数据的总数
            return tasks.size();
        }

        @Override
        public long getItemId(int position) {
            // 返回当前项在“集合视图”中的位置
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            // 只有一类 GridView
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            // initGridViewData();
            try {
                // initData();  //加载数据
                tasks = TaskManager.getTaskListTimeASCWhereTimeLimit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            tasks.clear();
        }


    }

}
