package com.surine.tustbox.Eventbus;

import java.io.Serializable;

/**
 * Created by surine on 2017/9/13.
 */

public class Net_EventBus implements Serializable{
    private int i;
    private String use_time;
    private String use_data;
    private String use_fee;

    public Net_EventBus(int i, String use_time, String use_data, String use_fee) {
        this.i = i;
        this.use_time = use_time;
        this.use_data = use_data;
        this.use_fee = use_fee;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getUse_time() {
        return use_time;
    }

    public void setUse_time(String use_time) {
        this.use_time = use_time;
    }

    public String getUse_data() {
        return use_data;
    }

    public void setUse_data(String use_data) {
        this.use_data = use_data;
    }

    public String getUse_fee() {
        return use_fee;
    }

    public void setUse_fee(String use_fee) {
        this.use_fee = use_fee;
    }
}
