package com.surine.tustbox.Pojo;

/**
 * Created by Surine on 2018/2/23.
 */

public class Love {
    private int id;  //id
    private int did;   //动态id
    private String uid;  //用户id
    private User user;   //用户
    private String love_time;   //赞时间

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLove_time() {
        return love_time;
    }

    public void setLove_time(String love_time) {
        this.love_time = love_time;
    }

    public Love() {
    }

    public Love(int id, int did, String uid, User user, String love_time) {
        this.id = id;
        this.did = did;
        this.uid = uid;
        this.user = user;
        this.love_time = love_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
