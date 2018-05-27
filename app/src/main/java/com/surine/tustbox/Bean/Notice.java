package com.surine.tustbox.Bean;

/**
 * Created by Surine on 2018/4/12.
 */

public class Notice {
  private int id;
  private String text;
  private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Notice(int id, String text, String data) {
        this.id = id;
        this.text = text;
        this.date = data;
    }
}
