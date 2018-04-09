package com.surine.tustbox.Bean;

/**
 * Created by Surine on 2018/2/22.
 * 动态实体
 */

public class Action {
    private int id;
    private String messages_info;   //内容
    private String messages_time;   //发布时间
    private String messages_commentnum;   //评论数
    private String messages_agreenum;   //点赞数
    private String messages_readnum;    //阅读数
    private String messages_topic;    //话题
    private String messages_device;  //设备
    private String picbaseurl;  //图片基础域名
    private String pic_ids;  //图片url
    private String uid;  //用户唯一标识
    private User user;   //用户
    private String message_ctime; //发布时间
    private boolean islove;  //是否点赞
    private int show_name;  //是否匿名



    public Action(int id, String messages_info, String messages_time, String messages_commentnum, String messages_agreenum, String messages_readnum, String messages_topic, String messages_device, String picbaseurl, String pic_ids, String uid, User user, String message_ctime, boolean islove, int show_name) {
        this.id = id;
        this.messages_info = messages_info;
        this.messages_time = messages_time;
        this.messages_commentnum = messages_commentnum;
        this.messages_agreenum = messages_agreenum;
        this.messages_readnum = messages_readnum;
        this.messages_topic = messages_topic;
        this.messages_device = messages_device;
        this.picbaseurl = picbaseurl;
        this.pic_ids = pic_ids;
        this.uid = uid;
        this.user = user;
        this.message_ctime = message_ctime;
        this.islove = islove;
        this.show_name = show_name;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", messages_info='" + messages_info + '\'' +
                ", messages_time='" + messages_time + '\'' +
                ", messages_commentnum='" + messages_commentnum + '\'' +
                ", messages_agreenum='" + messages_agreenum + '\'' +
                ", messages_readnum='" + messages_readnum + '\'' +
                ", messages_topic='" + messages_topic + '\'' +
                ", messages_device='" + messages_device + '\'' +
                ", picbaseurl='" + picbaseurl + '\'' +
                ", pic_ids='" + pic_ids + '\'' +
                ", uid='" + uid + '\'' +
                ", user=" + user +
                ", message_ctime='" + message_ctime + '\'' +
                ", islove=" + islove +
                ", show_name=" + show_name +
                '}';
    }

    public int getShow_name() {
        return show_name;
    }

    public void setShow_name(int show_name) {
        this.show_name = show_name;
    }

    public boolean isIslove() {
        return islove;
    }

    public void setIslove(boolean islove) {
        this.islove = islove;
    }

    public Action() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessages_info() {
        return messages_info;
    }

    public void setMessages_info(String messages_info) {
        this.messages_info = messages_info;
    }

    public String getMessages_time() {
        return messages_time;
    }

    public void setMessages_time(String messages_time) {
        this.messages_time = messages_time;
    }

    public String getMessages_commentnum() {
        return messages_commentnum;
    }

    public void setMessages_commentnum(String messages_commentnum) {
        this.messages_commentnum = messages_commentnum;
    }

    public String getMessages_agreenum() {
        return messages_agreenum;
    }

    public void setMessages_agreenum(String messages_agreenum) {
        this.messages_agreenum = messages_agreenum;
    }

    public String getMessages_readnum() {
        return messages_readnum;
    }

    public void setMessages_readnum(String messages_readnum) {
        this.messages_readnum = messages_readnum;
    }

    public String getMessages_topic() {
        return messages_topic;
    }

    public void setMessages_topic(String messages_topic) {
        this.messages_topic = messages_topic;
    }

    public String getMessages_device() {
        return messages_device;
    }

    public void setMessages_device(String messages_device) {
        this.messages_device = messages_device;
    }

    public String getPicbaseurl() {
        return picbaseurl;
    }

    public void setPicbaseurl(String picbaseurl) {
        this.picbaseurl = picbaseurl;
    }

    public String getPic_ids() {
        return pic_ids;
    }

    public void setPic_ids(String pic_ids) {
        this.pic_ids = pic_ids;
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


    public String getMessage_ctime() {
        return message_ctime;
    }

    public void setMessage_ctime(String message_ctime) {
        this.message_ctime = message_ctime;
    }
}
