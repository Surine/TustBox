package com.surine.tustbox.Helper.Dao;

import android.content.Context;
import android.widget.Toast;

import com.surine.tustbox.Helper.Utils.ToastUtil;
import com.surine.tustbox.Pojo.CourseInfoHelper;
import com.surine.tustbox.Helper.Utils.TimeUtil;
import com.surine.tustbox.Helper.Utils.TustBoxUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surine on 2018/9/2.
 * 课程表数据管理
 */

public class CourseInfoDao implements CurdManager<CourseInfoHelper> {

    /**
     * 添加课程到数据库
     * @param courseInfoHelper 课程bean
     * @return boolean 是否成功
     * */
    @Override
    public boolean add(CourseInfoHelper courseInfoHelper) {
        return courseInfoHelper.save();
    }

    /**
     * 从数据库按id删除课程
     * @param id 数据id
     * @return int 影响的列数
     * */
    @Override
    public int delete(int id) {
       return DataSupport.delete(CourseInfoHelper.class,id);
    }


    /**
     * 按id查询数据
     * @param id 数据id
     *
     * */
    @Override
    public CourseInfoHelper select(int id) {
        return DataSupport.find(CourseInfoHelper.class,id);
    }


    /**
     * 更新数据
     * @param courseInfoHelper 待更新数据
     * @return boolean 操作是否成功
     * */
    @Override
    public boolean update(CourseInfoHelper courseInfoHelper) {
        return courseInfoHelper.save();
    }

    //暂不需实现
    @Override
    public List<CourseInfoHelper> selectAll() {
        return null;
    }

    /**
     * 获取今日课程
     * @return 返回某日课程表
     * */
    public  List<CourseInfoHelper> getToDayCourse(Context context){
        //查询今天是第几周
        int week = new TustBoxUtil(context).getWeek();
        //按照今天星期几为条件，查询所有在今天上的课
        List<CourseInfoHelper> mCourseInfosFromDb = DataSupport.where("classDay like ?", "%" + TimeUtil.getWeekNumber() + "%").order("classSessions asc").find(CourseInfoHelper.class);
        List<CourseInfoHelper> mLastList = new ArrayList<>();
        for (CourseInfoHelper courseInfoHelper:mCourseInfosFromDb) {
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
                    //找到是本周上的课，添加到last列表中
                    if(temp == 1){
                        mLastList.add(courseInfoHelper);
                    }
                }
            }
        }
        return mLastList;
    }


    /**
     * 获取某周课程
     * @param week 某周
     * @return 返回课程表
     * */
    public  List<CourseInfoHelper> getWeekCourse(int week){
        List<CourseInfoHelper> mLastList = new ArrayList<>();
        //初始化全部
        for (int j = 0; j < 42; j++) {
            mLastList.add(null);
        }
        //读取数据库全部课程
        List<CourseInfoHelper> mCourseListFromDb = DataSupport.findAll(CourseInfoHelper.class);

        for(int i = 0; i < mCourseListFromDb.size(); i++){
            CourseInfoHelper courseInfoHelper = mCourseListFromDb.get(i);
            //如果课程信息不为空，就开始访问
            if(courseInfoHelper != null){

                String courseRunWeek = courseInfoHelper.getWeekDescription();
                //上课周信息判空
                if(courseRunWeek.isEmpty()){
                    break;
                }

                //是否是本周上课
                int temp = 0;
                if(week >= 1){
                    String getWeekChar = courseRunWeek.substring(week - 1,week);
                    if(getWeekChar.isEmpty()){
                        break;
                    }
                    //确定是本周上课
                    try {
                        temp = Integer.parseInt(getWeekChar);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if(temp == 1){
                        //判空
                        if(courseInfoHelper.getClassDay().isEmpty()||courseInfoHelper.getClassSessions().isEmpty()){
                            break;
                        }
                        if (courseInfoHelper.getStudyModeName().contains("正常") && !(courseInfoHelper.getCoureNumber().contains("WL"))) {
                            try{
                                mLastList.set((Integer.parseInt(courseInfoHelper.getClassDay()) - 1)
                                        + (Op((Integer.parseInt(courseInfoHelper.getClassSessions()))) - 1) * 7, courseInfoHelper);
                            }catch (Exception e){
                                ToastUtil.show("课表加载错误，请联系开发者");
                            }

                        }
                        //4节课
                        if (courseInfoHelper.getContinuingSession().contains("4")) {
                            try{
                            mLastList.set((Integer.parseInt(courseInfoHelper.getClassDay()) - 1)
                                    + (Op((Integer.parseInt(courseInfoHelper.getClassSessions()))) - 1) * 7 + 7, courseInfoHelper);
                            }catch (Exception e){
                                ToastUtil.show("课表加载错误，请联系开发者");
                            }
                        }
                    }
                }
            }
        }
        return mLastList;
    }


    //数据操作
    private static int Op(int i) {
        return (i + 1) / 2;
    }
}
