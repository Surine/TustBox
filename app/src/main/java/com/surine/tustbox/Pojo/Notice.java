package com.surine.tustbox.Pojo;

/**
 * Created by Surine on 2018/7/17.
 * 服务器通知类
 */

public class Notice {

    private String id;
    private String title;
    private String content;
    private String ctime;
    private String type;

    public Notice(String id, String title, String content, String ctime, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ctime = ctime;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
