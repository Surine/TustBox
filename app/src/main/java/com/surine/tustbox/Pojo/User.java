package com.surine.tustbox.Pojo;

import java.io.Serializable;

/**
 * Created by Surine on 2018/2/22.
 */

public class User implements Serializable{
    private int id;
    private String tust_number;
    private String pass;
    private String face;
    private String sign;
    private String nick_name;
    private String college;
    private String uptime;
    private String crtime;
    private String token;
    private String token_time;
    private String sex;
    private String user_type;
    private String schoolname;

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTust_number() {
        return tust_number;
    }

    public void setTust_number(String tust_number) {
        this.tust_number = tust_number;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getCrtime() {
        return crtime;
    }

    public void setCrtime(String crtime) {
        this.crtime = crtime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken_time() {
        return token_time;
    }

    public void setToken_time(String token_time) {
        this.token_time = token_time;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User(int id, String tust_number, String pass, String face, String sign, String nick_name, String college, String uptime, String crtime, String token, String token_time, String sex, String user_type, String schoolname) {
        this.id = id;
        this.tust_number = tust_number;
        this.pass = pass;
        this.face = face;
        this.sign = sign;
        this.nick_name = nick_name;
        this.college = college;
        this.uptime = uptime;
        this.crtime = crtime;
        this.token = token;
        this.token_time = token_time;
        this.sex = sex;
        this.user_type = user_type;
        this.schoolname = schoolname;
    }
}
