package com.surine.tustbox.Bean;

/**
 * Created by Surine on 2018/2/20.
 */

public class TagInfo {
    private String tagTitle;  //tag标题
    private String imageUrl;   //tag图像链接

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public TagInfo(String tagTitle, String imageUrl) {
        this.tagTitle = tagTitle;
        this.imageUrl = imageUrl;
    }
}
