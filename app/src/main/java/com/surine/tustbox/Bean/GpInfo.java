package com.surine.tustbox.Bean;

/**
 * Created by surine on 2017/4/24.
 */

public class GpInfo {
    private String ImageUrl;
    private String gp_name;
    private String gp_content;
    private String gp_download_url;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getGp_name() {
        return gp_name;
    }

    public void setGp_name(String gp_name) {
        this.gp_name = gp_name;
    }

    public String getGp_content() {
        return gp_content;
    }

    public void setGp_content(String gp_content) {
        this.gp_content = gp_content;
    }

    public String getGp_download_url() {
        return gp_download_url;
    }

    public void setGp_download_url(String gp_download_url) {
        this.gp_download_url = gp_download_url;
    }

    public GpInfo(String imageUrl, String gp_name, String gp_content, String gp_download_url) {
        ImageUrl = imageUrl;
        this.gp_name = gp_name;
        this.gp_content = gp_content;
        this.gp_download_url = gp_download_url;
    }
}
