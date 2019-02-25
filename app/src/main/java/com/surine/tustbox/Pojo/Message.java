package com.surine.tustbox.Pojo;

/**
 * Created by Surine on 2018/2/25.
 */

public class Message {
    private int id;
    private String fromuser;  //from 用户id
    private User user;  //用户
    private int type; //消息种类
    private int did; //动态id
    private int cid; //评论id
    private int rid; //回复id
    private String time; //时间
    private int isread; //是否已读
    private String text; //即时消息

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromuser() {
        return fromuser;
    }

    public void setFromuser(String fromuser) {
        this.fromuser = fromuser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Message() {
    }

    public Message(int id, String fromUser, User user, int type, int did, int cid, int rid, String time, int isread, String text) {
        this.id = id;
        this.fromuser = fromUser;
        this.user = user;
        this.type = type;
        this.did = did;
        this.cid = cid;
        this.rid = rid;
        this.time = time;
        this.isread = isread;
        this.text = text;
    }
}
