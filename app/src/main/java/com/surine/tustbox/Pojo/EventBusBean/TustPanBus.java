package com.surine.tustbox.Pojo.EventBusBean;

/**
 * Created by Surine on 2019/1/2.
 */

public class TustPanBus {
    int code;
    String root_id;
    String dir_name;

    public TustPanBus(int code, String root_id, String dir_name) {
        this.code = code;
        this.root_id = root_id;
        this.dir_name = dir_name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getDir_name() {
        return dir_name;
    }

    public void setDir_name(String dir_name) {
        this.dir_name = dir_name;
    }
}
