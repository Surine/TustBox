package com.surine.tustbox.Pojo;

import org.litepal.crud.DataSupport;

/**
 * Created by Surine on 2018/8/24.
 */

public class JwcUserInfo extends DataSupport {
    private int id;
    private String jwcName;
    private String jwcValue;

    @Override
    public String toString() {
        return "JwcUserInfo{" +
                "id=" + id +
                ", jwcName='" + jwcName + '\'' +
                ", jwcValue='" + jwcValue + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJwcName() {
        return jwcName;
    }

    public void setJwcName(String jwcName) {
        this.jwcName = jwcName;
    }

    public String getJwcValue() {
        return jwcValue;
    }

    public void setJwcValue(String jwcValue) {
        this.jwcValue = jwcValue;
    }
}
