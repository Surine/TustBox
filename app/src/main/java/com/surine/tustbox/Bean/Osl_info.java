package com.surine.tustbox.Bean;

/**
 * Created by surine on 2017/9/13.
 */

public class Osl_info {
    private String title;
    private String content;

    public Osl_info(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
