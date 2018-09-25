package com.surine.tustbox.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Surine on 2018/7/13.
 * 任务版本：任务实体
 */

public class Task extends DataSupport {
    private int id;
    private String task_name;  //日程名
    private String task_postion;  //日程地点
    private boolean task_is_important;  //是否重要
    private String task_time; //日程时间
    private int task_color;  //日程颜色
    private int task_type;  //日程种类
    private String task_tust_number; //创建用户
    private String create_time;   //创建时间时间戳
    private int state;   //任务状态（0未进行，1删除，2已完成）
    private boolean is_Share;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", task_name='" + task_name + '\'' +
                ", task_postion='" + task_postion + '\'' +
                ", task_is_important=" + task_is_important +
                ", task_time='" + task_time + '\'' +
                ", task_color=" + task_color +
                ", task_type=" + task_type +
                ", task_tust_number='" + task_tust_number + '\'' +
                ", create_time='" + create_time + '\'' +
                ", state=" + state +
                ", is_Share=" + is_Share +
                '}';
    }

    public boolean isIs_Share() {
        return is_Share;
    }

    public void setIs_Share(boolean is_Share) {
        this.is_Share = is_Share;
    }


    public Task() {
    }


    public String getTask_tust_number() {
        return task_tust_number;
    }

    public void setTask_tust_number(String task_tust_number) {
        this.task_tust_number = task_tust_number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_postion() {
        return task_postion;
    }

    public void setTask_postion(String task_postion) {
        this.task_postion = task_postion;
    }

    public boolean isTask_is_important() {
        return task_is_important;
    }

    public void setTask_is_important(boolean task_is_important) {
        this.task_is_important = task_is_important;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }

    public int getTask_color() {
        return task_color;
    }

    public void setTask_color(int task_color) {
        this.task_color = task_color;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }
}
