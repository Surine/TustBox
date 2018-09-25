package com.surine.tustbox.Mvp.Dao;

import com.surine.tustbox.Bean.CourseInfoHelper;

import org.litepal.crud.DataSupport;

/**
 * Created by Surine on 2018/9/2.
 */

public class CourseInfoDao implements CurdManager<CourseInfoHelper>{

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
}
