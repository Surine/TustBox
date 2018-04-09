package com.surine.tustbox.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Fragment.PageFragment.SencondPageFragment;
import com.surine.tustbox.R;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.surine.tustbox.Fragment.PageFragment.SencondPageFragment.c_info;

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
    private List<String> week_range;


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

        /**
         * 2017年10月23日19:33:09加载课程算法优化
         * 2018年3月12日16点51分加载课程算法优化
         */
        private void initData() throws Exception{
            //清空列表
            mCourse_infos.clear();
            mLastList.clear();
            //TODO：在这里删掉了用户处理的部分不知道会不会出问题
            //读取数据库全部课程
            mCourse_infos = DataSupport.findAll(Course_Info.class);
            //加载本周
            SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
            choose_week =  pref.getInt("choice_week",0);

            //初始化全部表格
            for (int j = 0; j < 42; j++) {
                mLastList.add(null);
            }


            //周筛选(此时mCourseList是数据库所有数据，mLastList是c_info)
            for (int e = 0; e < mCourse_infos.size(); e++) {
                if (mCourse_infos.get(e) != null) {
                    //正则解析:提取数字(上课周)
                    week_range = PatternUtil.getNumber(mCourse_infos.get(e).getWeek());
                    if (mCourse_infos.get(e).getWeek().contains("-")) {
                        //连周上课（当上课周不在这个区间）
                        if (!(Integer.parseInt(week_range.get(0)) <= choose_week && Integer.parseInt(week_range.get(1)) >= choose_week)) {
                            mCourse_infos.set(e,null);
                        }
                    } else {
                        if (week_range.indexOf(choose_week + "") == -1) {
                            //不是在本周上课
                            //   Log.d("DEBUG",mCourseList.get(e).getCourse_name()+"被移除");
                            mCourse_infos.set(e,null);
                        }
                    }
                }
            }


            for (int i = 0; i < mCourse_infos.size(); i++) {
                try {
                    course_info = mCourse_infos.get(i);
                    if (course_info != null ) {
                        //out of some cases(they may cause App ANR,for example:network course,etc)
                        if (course_info.getMethod().contains("正常") && !(course_info.getCourse_number().contains("WL"))) {
                            mLastList.set(Integer.parseInt(PatternUtil.getNumber(course_info.getWeek_number()).get(0))- 1
                                    + (TimeUtil.GetNumber(course_info.getClass_()) - 1) * 7, course_info);
                            //4节课
                            if (course_info.getClass_count().contains("4")) {
                                mLastList.set(Integer.parseInt(PatternUtil.getNumber(course_info.getWeek_number()).get(0)) - 1
                                        + (TimeUtil.GetNumber(course_info.getClass_()) - 1) * 7 + 7, course_info);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
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
           // initGridViewData();
            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
