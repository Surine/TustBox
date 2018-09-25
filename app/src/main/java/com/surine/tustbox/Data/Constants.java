package com.surine.tustbox.Data;

import com.surine.tustbox.R;

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

    //是否登陆key
    public static final String IS_LOGIN = "is_login";
    //当前周持久化key
    public static final String CHOOSE_WEEK = "choice_week";

    //任务id intent字段key
    public static final String TASK_ID = "TASK_ID";
    //网页加载标题和连接
    public static final String TITLE = "title";
    public static final String URL = "url";

    public static final int[] colorsOfTask = new int[]{
            R.color.taskB1,
            R.color.taskB2,
            R.color.taskB3,
            R.color.taskB4,
            R.color.taskB5,
            R.color.taskB6,
            R.color.taskB7,
            R.color.taskB9,
            R.color.taskB10,
    };

    //酷安色系
    public static final int[] coolapkColor = new int[]{
        R.color.coolapk_1,
        R.color.coolapk_2,
        R.color.coolapk_3,
        R.color.coolapk_4,
        R.color.coolapk_5,
        R.color.coolapk_6,
        R.color.coolapk_7,
        R.color.coolapk_8,
        R.color.coolapk_9,
        R.color.coolapk_10,
    };

    //酷安色系名称
    public static final String[] coolapkColorName = new String[]{
          "酷安绿",
          "姨妈红",
          "哔哩粉",
          "颐堤蓝",
          "水鸭青",
          "伊藤程",
          "基佬紫",
          "知乎蓝",
          "古铜棕",
          "高端黑"
    };

    //小天色系
    public static final int[] colors = new int[]{
            R.color.Tust_1,
            R.color.Tust_2,
            R.color.Tust_3,
            R.color.Tust_4,
            R.color.Tust_5,
            R.color.Tust_6,
            R.color.Tust_7,
            R.color.Tust_8,
            R.color.Tust_9,
            R.color.Tust_10,
            R.color.Tust_11,
            R.color.Tust_12,
            R.color.Tust_13,
            R.color.Tust_14,
            R.color.Tust_15,
            R.color.Tust_16,
    };

    //课程id
    public static final String COURSE_ID = "course_id";
    //task位置
    public static final String TASK_POSITION = "task_postion";
    public static final String COURSE_DATA = "course_data";

    //task默认标签
    public static String[] tagNameForTask = new String[]{
            "考试",
            "会议",
            "旅行",
            "琐事",
            "其他"
    };

    public static final String NON_REGISTER = "用户未注册";
    public static final String ERROR_PSWD = "帐号密码错误";
    public static final String LOGIN_SAVE_TOKEN = "登录成功，储存TOKEN";
    public static final String LOGIN_SAVE = "登录成功，准备下一步";
    public static final String USER_EXIST = "用户已存在或者数据库操作失败";
    public static final String INFO_ERROR = "个人信息不完整，请重新登录教务处！";
}
