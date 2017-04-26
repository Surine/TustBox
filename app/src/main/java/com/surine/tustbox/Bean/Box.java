package com.surine.tustbox.Bean;

/**
 * Created by surine on 2017/3/29.
 */

public class Box {
    private int image_id;
    private String box_name;
    private String info;
    private int color_id;

    public Box(int image_id, String box_name, String info, int color_id) {
        this.image_id = image_id;
        this.box_name = box_name;
        this.info = info;
        this.color_id = color_id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getBox_name() {
        return box_name;
    }

    public void setBox_name(String box_name) {
        this.box_name = box_name;
    }
}
