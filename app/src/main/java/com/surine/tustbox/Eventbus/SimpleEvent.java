package com.surine.tustbox.Eventbus;

/**
 * Created by surine on 2017/4/8.
 * count : 6
 */

public class SimpleEvent {
    private int id;
    private String message;

    public SimpleEvent(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
