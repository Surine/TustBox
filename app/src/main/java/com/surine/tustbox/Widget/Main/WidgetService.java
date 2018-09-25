package com.surine.tustbox.Widget.Main;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surine on 2017/4/12.
 * 用了fragment里的代码，直接拿过来的没做处理
 */

public class WidgetService extends RemoteViewsService {
    private static final String TAG = "SURINE_SERVICE";
    int choose_week;
    int number;
    private List<CourseInfoHelper> mLastList =new ArrayList<>();
    private List<CourseInfoHelper> mCourseList = new ArrayList<>();
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "GridWidgetService");
        return new GridRemoteViewsFactory(this, intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;


        /**
         * 构造GridRemoteViewsFactory
         * @author skywang
         */
        public GridRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.d(TAG, "GridRemoteViewsFactory mAppWidgetId:"+mAppWidgetId);
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(TAG, "GridRemoteViewsFactory getViewAt:"+position);
            // 获取 grid_view_item.xml 对应的RemoteViews
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_course);

            // 设置 第position位的“视图”的数据
            CourseInfoHelper course_info = mLastList.get(position);
            if (course_info != null) {
                rv.setImageViewResource(R.id.itemImage, (course_info.getJwColor()));
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
            //清空列表
            mCourseList.clear();
            mLastList.clear();

            //读取数据库全部课程
            mCourseList = DataSupport.findAll(CourseInfoHelper.class);
            //加载本周
            choose_week = SharedPreferencesUtil.Read(mContext, Constants.CHOOSE_WEEK, 1);

            //初始化全部表格
            for (int j = 0; j < 42; j++) {
                mLastList.add(null);
            }

            //周筛选(此时mCourseList是数据库所有数据，mLastList是全为null)
            for (int e = 0; e < mCourseList.size(); e++) {
                //判空
                if (mCourseList.get(e) != null) {
                    String week = mCourseList.get(e).getWeekDescription();
                    //确定是本周上课
                    int temp = 0;
                    if(choose_week >= 1){
                        temp = Integer.parseInt(week.substring(choose_week - 1,choose_week));
                    }else{
                        Toast.makeText(mContext,"请联网更新周",Toast.LENGTH_SHORT).show();
                    }
                    CourseInfoHelper courseInfoHelper = mCourseList.get(e);
                    if (temp == 1) { //上课周
                        if (courseInfoHelper != null) {
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

        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged: ");
           // initGridViewData();
            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            mCourseList.clear();
            mLastList.clear();
        }


    }

}
