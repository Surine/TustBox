package com.surine.tustbox.Data;

/**
 * Created by Surine on 2018/2/20.
 * 静态常量类
 */

public class Constants {
    //持久化文件目录
    public static final String DATA_FILE_PATH = "/data/data/com.surine.tustbox/shared_prefs/data.xml";
    //头像文件目录
    public static final String HEAD_FILE_PATH = "/head.jpg";
    //消息类型常量
    public static final int LOVE_ACTION = 1;
    public static final int COMMENT_ACTION = 2;
    public static final int REPLY_COMMENT = 3;
    public static final int REPLY_AT_SOMEONE = 4;
    //图片处理后缀
    public static final String PIC_CROP = "-picture";
    //HTTP前缀
    public static final String HTTP = "http://";

    //请求超时
    public static final long CONNECT_TIMEOUT = 5;

}
