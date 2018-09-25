package com.surine.tustbox.Manager;

import com.surine.tustbox.Bean.Task;
import com.surine.tustbox.Util.TimeUtil;

import org.litepal.crud.DataSupport;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surine on 2018/7/14.
 * 日程（Task）管理接口
 * 通过本地更新和网络更新同步更新
 */

public class TaskManager {
    //添加任务
    public static boolean addTask(Task task) {
        return task.save();
    }

    //删除任务
    public static int deleteTask(int id) {
        return DataSupport.delete(Task.class, id);
    }

    //修改任务
    public static boolean modifyTask(Task task) {
        return task.save();
    }

    //查询任务
    public static Task getTaskById(int id) {
        return new Task();
    }

    //查询列表(通过时间顺序排序，过时限制)
    public static List<Task> getTaskListTimeASCWhereTimeLimit() {
        return DataSupport.order("task_time asc").where("task_time >= ?", TimeUtil.getDate(TimeUtil.yMdHm)).find(Task.class);
    }
    //查询列表（通过时间顺序排序，无过期限制）{
    public static List<Task> getTaskListTimeASC(){
       return DataSupport.order("task_time asc").find(Task.class);
    }
}
