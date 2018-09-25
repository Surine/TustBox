package com.surine.tustbox.Manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.surine.tustbox.Bean.CourseInfoHelper;
import com.surine.tustbox.Bean.Course_Info;
import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Util.PatternUtil;
import com.surine.tustbox.Util.SharedPreferencesUtil;
import com.surine.tustbox.Util.TimeUtil;
import com.surine.tustbox.Util.TustBoxUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Created by Surine on 2018/8/9.
 * 课程管理类
 */

public class CourseManager {

    /*
    * tip:获取今天的课程
    * para:上下文
    * return：今天上的课的列表
    * */
    public static List<CourseInfoHelper> getTodayCourse(Context context){
          //查询今天是第几周
        int week = new TustBoxUtil(context).getWeek();
        List<CourseInfoHelper> mCourse_infos_From_db = DataSupport.where("classDay like ?", "%" + TimeUtil.getWeekNumber() + "%").order("classSessions asc").find(CourseInfoHelper.class);
        List<CourseInfoHelper> mLastList = new ArrayList<>();
        for (CourseInfoHelper courseInfoHelper:mCourse_infos_From_db) {
            if(courseInfoHelper != null){
                String courseWeek = courseInfoHelper.getWeekDescription();
                int temp = 0;
                if(week >= 1){
                    try {
                        temp = Integer.parseInt(courseWeek.substring(week - 1,week));
                    } catch (NumberFormatException e) {
                        temp = 0;
                        e.printStackTrace();
                    }
                    if(temp == 1){
                        mLastList.add(courseInfoHelper);
                    }
                }
            }
        }
        return mLastList;
    }




//
//        //初始化
//        Course_Info[] coursesArray = new Course_Info[6];  //每天6节课
//        for(int i = 0;i <= 5;i++){
//            coursesArray[i] = c_info;
//        }
//        for (Course_Info c: mCourse_infos_From_db) {
//            if(c != null){
//                if(c.getClass_().contains("一")){
//                    coursesArray[0] = c;
//                }else if(c.getClass_().contains("二")){
//                    coursesArray[1] = c;
//                }else if(c.getClass_().contains("三")){
//                    coursesArray[2] = c;
//                }else if(c.getClass_().contains("四")){
//                    coursesArray[3] = c;
//                }else if(c.getClass_().contains("五")){
//                    coursesArray[4] = c;
//                }else if(c.getClass_().contains("六")){
//                    coursesArray[5] = c;
//                }
//            }
//        }
//        mCourse_infos = Arrays.asList(coursesArray);
//
//        for (Course_Info course_info: mCourse_infos) {
//            if(!course_info.getCourse_number().equals("*")){
//                mLast_infos.add(course_info);
//            }
//        }
//
//        return mLast_infos;
//    }
}
