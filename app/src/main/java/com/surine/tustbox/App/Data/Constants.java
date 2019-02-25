package com.surine.tustbox.App.Data;

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
    public static final int[] colors_old = new int[]{
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

    //小天v5色
    public static final int[] colors = new int[]{
         R.color.v5_cc1,
         R.color.v5_cc2,
         R.color.v5_cc3,
         R.color.v5_cc4,
         R.color.v5_cc5,
         R.color.v5_cc6,
         R.color.v5_cc7,
         R.color.v5_cc8,
         R.color.v5_cc9,
         R.color.v5_cc10,
         R.color.v5_cc11,
         R.color.v5_cc12,
         R.color.v5_cc13,
         R.color.v5_cc14,
         R.color.v5_cc15,
         R.color.v5_cc16,
    };

    //课程id
    public static final String COURSE_ID = "course_id";
    //task位置
    public static final String TASK_POSITION = "task_postion";
    public static final String COURSE_DATA = "course_data";
    public static final String WIDGET_BACKGROUND_BUTTON = "widget_image_button";
    public static final String BACKGROUND_IMG_SAVE = "my_picture_path";

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


    public static final int PAN_LOGIN_SUCCESS = 10000;
    public static final int PAN_LOGIN_GROUP_ROOT = 10001;


    public static final String[] weekStr = new String[]{
            "第一周", "第二周", "第三周", "第四周", "第五周", "第六周",
            "第七周", "第八周", "第九周", "第十周", "第十一周", "第十二周",
            "第十三周", "第十四周", "第十五周", "第十六周", "第十七周", "第十八周",
            "第十九周", "第二十周", "第二十一周", "第二十二周", "第二十三周", "第二十四周",
    };

    //设置项
    public static final String ACCOUNTBIND = "账号绑定";
    public static final String ACCOUNTBIND_SUM = "设置您需要绑定的账号";

    public static final String BANNERBACKOUND = "Banner及课表背景";
    public static final String BANNERBACKOUND_SUM = "设置您的背景图片";

    public static final String WIDGETBACKOUND = "小部件背景图";
    public static final String WIDGETBACKOUND_SUM = "打开后，请点击小部件刷新";

    public static final String INDEXCARD = "首页信息卡";
    public static final String INDEXCARD_SUM = "自定义您的首页";

    public static final String NORMALPAGE = "默认页面";
    public static final String NORMALPAGE_SUM = "设置打开时的默认页面";

    public static final String HIDEBOTTOM = "隐藏底部栏";
    public static final String HIDEBOTTOM_SUM = "隐藏后仍可以滑动切换页面";

    public static final String FEEDBACK = "意见反馈";
    public static final String FEEDBACK_SUM = "在这里吐槽小天盒子";

    public static final String QQ = "小天QQ群";
    public static final String QQ_SUM = "在这里也可以哦！";

    public static final String SUPPORT = "支持小天";
    public static final String SUPPORT_SUM = "点击跳转支付宝，金额随意";

    public static final String ABOUT = "关于APP";
    public static final String ABOUT_SUM = "咚咚咚，开门查户口";


    //网络加载标识
    public static final boolean UPTOREFRESH = true;  //上拉标识
    public static final boolean DOWNTOREFRESH = false;  //下拉标识


    //临时话题
    public static final String[] staticTopicItems = {
            "#闲聊的日常#",
            "小天吐槽吧",
            "表白墙",
            "王者荣耀",
            "小姐姐",
            "小哥哥",
            "失物招领",
            "二手物品",
    };

    public static final String PICTURE_CHOOSE_LIST = "PICTURE_CHOOSE_LIST";
}
