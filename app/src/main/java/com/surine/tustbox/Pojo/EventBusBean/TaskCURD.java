package com.surine.tustbox.Pojo.EventBusBean;

/**
 * Created by Surine on 2018/8/7.
 * 用于通知Task增删改查的响应
 * tag = 1 add
 * tag = 2 delete
 * tag = 3 modify
 * tag = 4 find
 */

public class TaskCURD {

    public static final int ADD = 1;
    public static final int DELETE = 2;
    public static final int MODIFY = 3;
    public static final int FIND = 4;
    private int id;
    private int tag;
    private int message;

    public TaskCURD(int id, int tag, int message) {
        this.id = id;
        this.tag = tag;
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}
