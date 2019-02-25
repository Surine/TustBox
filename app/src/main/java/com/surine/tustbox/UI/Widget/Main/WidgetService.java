package com.surine.tustbox.UI.Widget.Main;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.surine.tustbox.Helper.Dao.CourseInfoDao;
import com.surine.tustbox.Pojo.CourseInfoHelper;
import com.surine.tustbox.R;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/12. 实现功能
 * Modified by surine on 2019年1月30日 封装数据加载，简化代码
 */

public class WidgetService extends RemoteViewsService {
    private static final String TAG = "SURINE_SERVICE";
    private List<CourseInfoHelper> mLastList =new ArrayList<>();

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this, intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;
        CourseInfoDao courseInfoDao = new CourseInfoDao();

        /**
         * 构造GridRemoteViewsFactory
         */
        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        //item加载
        @Override
        public RemoteViews getViewAt(int position) {
            // 获取 grid_view_item.xml 对应的RemoteViews
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_course);

            // 设置 第position位的“视图”的数据
            CourseInfoHelper course_info = mLastList.get(position);
            if (course_info != null) {
                rv.setImageViewResource(R.id.itemImage,course_info.getJwColor());
               // rv.setImageViewBitmap(R.id.itemImage,bm);
                rv.setTextViewText(R.id.itemText, course_info.getCourseName());
                rv.setTextViewText(R.id.widget_loca_text, "@"+course_info.getTeachingBuildingName()+course_info.getClassroomName());
            }else{
                rv.setTextViewText(R.id.itemText,"");
                rv.setTextViewText(R.id.widget_loca_text, "");
                rv.setImageViewResource(R.id.itemImage,0);
            }
            return rv;
        }

        /**
         * 2017年10月23日19:33:09加载课程算法优化
         * 2018年3月12日16点51分加载课程算法优化
         */
        private void initData() {
            int week = new TustBoxUtil(mContext).getWeek();
            mLastList.clear();
            List<CourseInfoHelper> mHelperList = courseInfoDao.getWeekCourse(week);
            for (CourseInfoHelper c: mHelperList) {
                mLastList.add(c);
            }
        }

        //数据集合发生变化，重新加载数据
        @Override
        public void onDataSetChanged() {
            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //销毁处理
        @Override
        public void onDestroy() {
            mLastList.clear();
        }

        @Override
        public void onCreate() {
            Log.d(TAG, "onCreate");
            // 初始化“集合视图”中的数据
        }

        @Override
        public int getCount() {
            // 返回“集合视图”中的数据的总数
            return mLastList.size();
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
    }
}
