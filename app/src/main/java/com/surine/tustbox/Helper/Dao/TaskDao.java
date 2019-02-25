package com.surine.tustbox.Helper.Dao;

import com.surine.tustbox.Pojo.Task;
import com.surine.tustbox.Helper.Utils.TimeUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Surine on 2019/1/30.
 * Task任务数据库管理类
 */

public class TaskDao implements CurdManager<Task> {


    /**
     * 添加任务
     * @param task TASK对象
     * @return boolean 添加状态
     * */
    @Override
    public boolean add(Task task) {
        return task.save();
    }


    /**
     * 删除任务
     * @param id TASK id
     * @return int 影响列
     * */
    @Override
    public int delete(int id) {
        return DataSupport.delete(Task.class, id);
    }


    /**
     * 按id选择内容
     * @param id task id
     * @return Task 对象
     * */
    @Override
    public Task select(int id) {
        return DataSupport.find(Task.class, id);
    }

    /**
     * 更新任务数据
     * @param task task修改对象
     * @return boolean 状态
     * */
    @Override
    public boolean update(Task task) {
        return task.save();
    }


    /**
     * 选择全部任务
     * @return List<Task> task对象列表
     * */
    @Override
    public List<Task> selectAll() {
        return null;
    }

    /**
     * 列表查询
     * @return List<Task> 通过时间顺序排序，过时限制
     * */
    public  List<Task> getTaskListTimeASCWhereTimeLimit() {
        return DataSupport.order("task_time asc").where("task_time >= ?", TimeUtil.getDate(TimeUtil.yMdHm)).find(Task.class);
    }

    /**
     * 列表查询
     * @return List<Task> 通过时间顺序排序，无过期限制
     * */
    public  List<Task> getTaskListTimeASC(){
        return DataSupport.order("task_time asc").find(Task.class);
    }

}
