package com.surine.tustbox.Bean;

/**
 * Created by Surine on 2018/12/31.
 */

public class RootDir {
    //群组id
    private String id;
    //群组简介
    private String intro;
    //成员
    private String name;
    //标签
    private String tags;
    //根id
    private String root_id;
    //用户数
    private String user_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getUser_count() {
        return user_count;
    }

    public void setUser_count(String user_count) {
        this.user_count = user_count;
    }
}
