package com.surine.tustbox.Bean;

/**
 * Created by Surine on 2018/2/23.
 */

public class Comment {
    private int id;    //id
    private int did;  //动态id
    private String uid;  //用户id
    private String comment_content;  //评论内容
    private int comment_say_num;   //评论点评数
    private int comment_love_num;   //评论喜欢数
    private String comment_time;    //评论时间
    private User user;  //用户


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public int getComment_say_num() {
        return comment_say_num;
    }

    public void setComment_say_num(int comment_say_num) {
        this.comment_say_num = comment_say_num;
    }

    public int getComment_love_num() {
        return comment_love_num;
    }

    public void setComment_love_num(int comment_love_num) {
        this.comment_love_num = comment_love_num;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", did=" + did +
                ", uid=" + uid +
                ", comment_content='" + comment_content + '\'' +
                ", comment_say_num=" + comment_say_num +
                ", comment_love_num=" + comment_love_num +
                ", comment_time='" + comment_time + '\'' +
                ", user=" + user +
                '}';
    }

    public Comment(int id, int did, String uid, String comment_content, int comment_say_num, int comment_love_num, String comment_time, User user) {
        this.id = id;
        this.did = did;
        this.uid = uid;
        this.comment_content = comment_content;
        this.comment_say_num = comment_say_num;
        this.comment_love_num = comment_love_num;
        this.comment_time = comment_time;
        this.user = user;
    }
}
