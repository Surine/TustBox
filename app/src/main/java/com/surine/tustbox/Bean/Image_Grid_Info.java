package com.surine.tustbox.Bean;

/**
 * Created by surine on 2017/10/30.
 */

public class Image_Grid_Info {
    private String url;
    private String width;
    private String height;

    public Image_Grid_Info(String url, String width, String height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
