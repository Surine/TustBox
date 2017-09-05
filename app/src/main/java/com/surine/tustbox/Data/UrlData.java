package com.surine.tustbox.Data;

/**
 * Created by surine on 2017/3/28.
 */

public class UrlData {
    //请求登陆地址
    public static String login_post_url = "http://jwxt.tust.edu.cn/newLoginAction.do";
    //个人信息请求地址
    public static String get_stu_info_url = "http://jwxt.tust.edu.cn/xjInfoAction.do?oper=xjxx";
    //课程请求地址
    public static String get_Course_info_url = "http://jwxt.tust.edu.cn/xkAction.do?actionType=6";
    //头像地址
    public static String get_head = "http://jwxt.tust.edu.cn/xjInfoAction.do?oper=img";
    //网络地址
    public static String login_net_post_url = "http://uss.tust.edu.cn/LoginAction.action";
    //网络地址准备地址
    public static String login_net_prepare_url = "http://uss.tust.edu.cn/nav_login";
    //网络验证码地址
    public static String random_code = "http://uss.tust.edu.cn/RandomCodeAction.action?randomNum=" + Math.random();
    //成绩地址
    public static String socre_get_url = "http://jwxt.tust.edu.cn/bxqcjcxAction.do";
    //所有成绩
    public static String all_socre_get_url = "http://jwxt.tust.edu.cn/gradeLnAllAction.do?type=ln&oper=fainfo&fajhh=2214";
    //图书馆地址
    public static String library_url = "http://211.81.31.34/uhtbin/cgisirsi/?ps=";
    public static String my_borrow_url = "http://211.81.31.34/uhtbin/cgisirsi/?ps=";

    //GP下载平台
    public static String gp_download = "http://tust.gp.tust.edu.cn/download.html";
    public static String gp_download_short = "http://tust.gp.tust.edu.cn/";
    //update
    public static String update_url = "http://surine.cn/update/update_log.html";
    //download
    public static String download_url = "http://surine.cn/TustBox/index.php/Home/AppManager/download";
    //github
    public static String github_url = "https://github.com/surine/TustBox";
    //osl
    public static String osl_url ="file:///android_asset/Html/osl.html";
    //notice
    public static String notice_url ="file:///android_asset/Html/notice.html";
    //get server week
    public static String getServerWeek ="http://surine.cn/TustBoxServer/Course/GetWeek.php";
    public static String celitea_url ="http://surine.cn:8889";
}
