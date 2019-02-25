package com.surine.tustbox.Pojo;

/**
 * Created by Surine on 2018/8/20.
 */

public class SchoolUrl {
    private int id;
    private String urlName;
    private String urlIntro;
    private String urlText;

    public SchoolUrl(int id, String urlName, String urlIntro, String urlText) {
        this.id = id;
        this.urlName = urlName;
        this.urlIntro = urlIntro;
        this.urlText = urlText;
    }

    public SchoolUrl() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getUrlIntro() {
        return urlIntro;
    }

    public void setUrlIntro(String urlIntro) {
        this.urlIntro = urlIntro;
    }

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }
}
