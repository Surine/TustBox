package com.surine.tustbox.Pojo;

import java.util.List;

/**
 * Created by surine on 2017/10/30.
 */

public class ActionInfo {
    private int id;
    private String head_url;
    private String name;
    private int user_id;
    private String time;
    private String text;
    private List<Image_Grid_Info> mImage_grid_infos;
    private String love_num;
    private String comment_num;
    private String other;

    public ActionInfo(int id, String head_url, String name, int user_id, String time, String text, List<Image_Grid_Info> image_grid_infos, String love_num, String comment_num, String other) {
        this.id = id;
        this.head_url = head_url;
        this.name = name;
        this.user_id = user_id;
        this.time = time;
        this.text = text;
        mImage_grid_infos = image_grid_infos;
        this.love_num = love_num;
        this.comment_num = comment_num;
        this.other = other;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Image_Grid_Info> getImage_grid_infos() {
        return mImage_grid_infos;
    }

    public void setImage_grid_infos(List<Image_Grid_Info> image_grid_infos) {
        mImage_grid_infos = image_grid_infos;
    }

    public String getLove_num() {
        return love_num;
    }

    public void setLove_num(String love_num) {
        this.love_num = love_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
