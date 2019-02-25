package com.surine.tustbox.Pojo;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/3/28.
 */

public class Student_info extends DataSupport{
    private String Tag;   //标签
    private String value;  //值
    private int id;

    public Student_info(String tag, String value) {
        Tag = tag;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
