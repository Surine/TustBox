package com.surine.tustbox.Pojo;

import android.view.View;

/**
 * Created by Surine on 2019/1/31.
 */

public class SettingItem {
    private int icon;
    private String title;
    private String subTitle;
    private View view;

    public SettingItem() {
    }

    public SettingItem(int icon, String title, String subTitle, View view) {
        this.icon = icon;
        this.title = title;
        this.subTitle = subTitle;
        this.view = view;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
