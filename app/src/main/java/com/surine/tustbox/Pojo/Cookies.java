package com.surine.tustbox.Pojo;

/**
 * Created by Surine on 2019/2/24.
 * Cookie管理类
 */

public class Cookies {
    private Object data;  //数据
    private String msg;   //信息
    private int id;       //id

    public Cookies(Object data, String msg, int id) {
        this.data = data;
        this.msg = msg;
        this.id = id;
    }

    public Cookies() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
