package com.surine.tustbox.Bean;

/**
 * Created by Surine on 2018/2/24.
 */

public class Reply {
    private int id;
    private int cid;   //评论id
    private String say_info; //发布内容
    private String say_at;   //@uid
    private String say_time;  //发布时间
    private String uid;  //用户id
    private String say_at_name;   //@用户名
    private User user;  //user

    public String getSay_at_name() {
        return say_at_name;
    }

    public void setSay_at_name(String say_at_name) {
        this.say_at_name = say_at_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getSay_info() {
        return say_info;
    }

    public void setSay_info(String say_info) {
        this.say_info = say_info;
    }

    public String getSay_at() {
        return say_at;
    }

    public void setSay_at(String say_at) {
        this.say_at = say_at;
    }

    public String getSay_time() {
        return say_time;
    }

    public void setSay_time(String say_time) {
        this.say_time = say_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reply() {
    }

    public Reply(int id, int cid, String say_info, String say_at, String say_time, String uid, String say_at_name, User user) {
        this.id = id;
        this.cid = cid;
        this.say_info = say_info;
        this.say_at = say_at;
        this.say_time = say_time;
        this.uid = uid;
        this.say_at_name = say_at_name;
        this.user = user;
    }
}
