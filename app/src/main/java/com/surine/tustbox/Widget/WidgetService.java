package com.surine.tustbox.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.PatternUtil;

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
    Course_Info course_info;

    int position1;
    int position2;
    String number_b = ",";
    int number;
    String help_string;


    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "GridWidgetService");
        return new GridRemoteViewsFactory(this, intent);
    }

    private class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private List<Course_Info > mLastList =new ArrayList<>();
        private List<Course_Info> mCourse_infos = new ArrayList<>();
        private Context mContext;
        private int mAppWidgetId;

        private String IMAGE_ITEM = "imgage_item";
        private String TEXT_ITEM = "text_item";

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
            Course_Info course_info = mLastList.get(position);
            if (course_info != null) {
                rv.setImageViewResource(R.id.itemImage, (course_info.getColor()));
                rv.setTextViewText(R.id.itemText, course_info.getCourse_name());
                rv.setTextViewText(R.id.widget_loca_text, "@"+course_info.getBuilding()+course_info.getClassroom());
            }else{
                rv.setTextViewText(R.id.itemText,"");
                rv.setTextViewText(R.id.widget_loca_text, "");
                rv.setImageViewResource(R.id.itemImage,0);
            }
            return rv;
        }

        /**
         * 初始化GridView的数据
         * @author skywang
         */
        private void initGridViewData() {
            mCourse_infos.clear();
            mLastList.clear();
            //获取数据库
            mCourse_infos = DataSupport.findAll(Course_Info.class);
            //数据筛选
            SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
            choose_week =  pref.getInt("choice_week",0);
            for(int j = 0;j<30;j++){
                mLastList.add(null);
            }
            for(int i = 0;i<mCourse_infos.size();i++){
                course_info = mCourse_infos.get(i);
                //排除了重修及网课导致的ANR情况
                if(course_info.getMethod().contains("正常")&&!(course_info.getCourse_number().contains("WL"))) {
                    position1 = Integer.parseInt(String.valueOf(course_info.getWeek_number().charAt(2))) - 1;
                    position2 = (GetNumber(course_info.getClass_()) - 1) * 5;
                    mLastList.set(position1 + position2, course_info);
                    if (course_info.getClass_count().contains("4")) {
                        mLastList.set(position1 + position2 + 5, course_info);
                    }
                }


            }

            //周筛选
            for(int e=0;e<mLastList.size();e++){
                if(mLastList.get(e)!=null) {
                    if (mLastList.get(e).getWeek().contains("-")) {

                        //正则解析，提取数字
                        List<String> week_range = PatternUtil.getNumber(mLastList.get(e).getWeek());

                        for(int i= Integer.parseInt(week_range.get(0));i<=Integer.parseInt(week_range.get(1));i++){
                            number_b+=(i+",");
                        }
                        if(!(number_b.contains(","+choose_week+","))){
                            mLastList.set(e, null);
                        }
                        number_b = ",";
                    } else {
                        String help_week = (","+mLastList.get(e).getWeek().substring(2,mLastList.get(e).getWeek().length()-2)+",");
                        if (!(help_week.contains(","+choose_week+","))) {
                            mLastList.set(e, null);
                        }
                    }
                }
            }

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
            initGridViewData();
        }

        @Override
        public void onDestroy() {
            mCourse_infos.clear();
            mLastList.clear();
        }


    }




    //s数字处理方法
    public int GetNumber(String word){
        if(word.contains("一")){
            number = 1;
        }else if(word.contains("二")){
            number = 2;
        }else if(word.contains("三")){
            number = 3;
        }else if(word.contains("四")){
            number = 4;
        }else if(word.contains("五")){
            number = 5;
        }else if(word.contains("六")){
            number = 6;
        }else if(word.contains("七")){
            number = 7;
        }else if(word.contains("八")){
            number = 8;
        }else if(word.contains("九")){
            number = 9;
        }else if(word.contains("十")){
            number = 10;
        }
        return number;
    }
}
