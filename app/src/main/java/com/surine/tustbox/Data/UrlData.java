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
    public static String update_url = "http://surine.cn/TustBox/index.php/Home/AppManager/update";
    //download
    public static String download_url = "http://surine.cn/TustBox/index.php/Home/AppManager/download";
    //github
    public static String github_url = "https://github.com/surine/TustBox";

    //get server week
    public static String getServerWeek ="http://surine.cn/TustBoxServer/Course/GetWeek.php";
    public static String celitea_url ="http://celitea.cn";
    //login
    public static String login_server_url ="http://surine.cn/TustBox/index.php/Home/Login/login";
    //register
    public static String register_server_url ="http://surine.cn/TustBox/index.php/Home/Login/register";
    //get today info
    public static String get_today_url ="http://surine.cn/TustBox/index.php/Home/TodayCourse/getTodayInfo";
    //get head
    public static String get_head_url ="http://surine.cn/TustBox/index.php/Home/User/getHead";

    //校园网登录页面
    public static final String net_post_url = "http://59.67.0.245/a30.htm";
    //校园网登录重定向
    public static final String net_get_url = "http://59.67.0.245";
    //网费充值
    public static final String charge_page = "http://59.67.5.142/WebPay/toRecharge";

    //用户须知和使用说明
    public static final String notice_and_introduce = "http://surine.cn/TustBox/WebPage/user_login_page";

    //云盘登录
    public static final String pan_login = "http://pan.tust.edu.cn/v1/auth/sign_in?link_name=web&link_device=web&";
}
