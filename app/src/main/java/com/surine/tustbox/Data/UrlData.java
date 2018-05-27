package com.surine.tustbox.Data;

/**
 * Created by surine on 2017/3/28.
 */

public class UrlData {
    public static String baseUrl = "http://surine.cn";
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
    public static String update_url = baseUrl+"/TustBox/index.php/Home/AppManager/update";
    //download
    public static String download_url = baseUrl+"/TustBox/index.php/Home/AppManager/download";
    //github
    public static String github_url = "https://github.com/surine/TustBox";

    //get server week
    public static String getServerWeek =baseUrl+"/TustBoxServer/Course/GetWeek.php";
    public static String celitea_url ="http://celitea.cn";
    //login
    public static String login_server_url =baseUrl+"/TustBox/index.php/Home/Login/login";
    //register
    public static String register_server_url =baseUrl+"/TustBox/index.php/Home/Login/register";
    //get today info
    public static String get_today_url =baseUrl+"/TustBox/index.php/Home/TodayCourse/getTodayInfo";
    //get head
    public static String get_head_url =baseUrl+"/TustBox/index.php/Home/User/getHead";

    //课程评分
    public static String setScore = baseUrl+"/TustBox/index.php/Home/CourseManager/setScore";
    public static String getScore = baseUrl+"/TustBox/index.php/Home/CourseManager/getScore";

    //七牛token
    public static String getToken = baseUrl+"/TustBox/index.php/Home/Action/getToken";
    //发送动态
    public static String sendAction = baseUrl+"/TustBox/index.php/Home/Action/sendAction";
    //获取动态列表
    public static String getAction = baseUrl+"/TustBox/index.php/Home/Action/getAction";
    //获取某个用户的动态
    public static String getActionByUid = baseUrl+"/TustBox/index.php/Home/Action/getActionByUid";
    //通过id获取动态
    public static String getActionById = baseUrl+"/TustBox/index.php/Home/Action/getActionById";
    //添加评论
    public static String addComment = baseUrl+"/TustBox/index.php/Home/Comment/addComment";
    //取得评论
    public static String getComment = baseUrl+"/TustBox/index.php/Home/Comment/getComment";
    //通过id取得评论
    public static String getCommentById = baseUrl+"/TustBox/index.php/Home/Comment/getCommentById";
    //在评论下回复
    public static String addReplyInComment = baseUrl+"/TustBox/index.php/Home/ReplyInComment/addReplyInComment";
    //取得评论下的回复列表
    public static String getReply = baseUrl+"/TustBox/index.php/Home/ReplyInComment/getReply";
    //赞动态
    public static String loveAction = baseUrl+"/TustBox/index.php/Home/Love/love";
    //获取赞列表
    public static String getLove = baseUrl+"/TustBox/index.php/Home/Love/getLove";
    //获取个人信息
    public static String getUserInfo = baseUrl+"/TustBox/index.php/Home/User/getUserInfo";
    //删除回复
    public static String deleteReplyById = baseUrl+"/TustBox/index.php/Home/ReplyInComment/deleteReplyById";
    //删除评论
    public static String deleteCommentById = baseUrl+"/TustBox/index.php/Home/Comment/deleteCommentById";
    //删除动态
    public static String deleteAction = baseUrl+"/TustBox/index.php/Home/Action/deleteAction";
    //获取消息列表
    public static String getMessageList = baseUrl+"/TustBox/index.php/Home/ActionLog/getMessageList";
    //标记已读
    public static String read = baseUrl+"/TustBox/index.php/Home/ActionLog/read";
    //获取未读条数
    public static String getMessageNum = baseUrl+"/TustBox/index.php/Home/ActionLog/getMessageNum";
    //全部已读
    public static String readAll = baseUrl+"/TustBox/index.php/Home/ActionLog/readAll";
    //设置签名
    public static String setUserSign = baseUrl+"/TustBox/index.php/Home/User/setUserSign";
    //设置性别
    public static String setUserSex = baseUrl+"/TustBox/index.php/Home/User/setUserSex";
    //更新昵称
    public static String setUserName = baseUrl+"/TustBox/index.php/Home/User/setUserName";
    //更新学院
    public static String setUserCollege = baseUrl+"/TustBox/index.php/Home/User/setUserCollege";
    //更新头像
    public static String setUserFace = baseUrl+"/TustBox/index.php/Home/User/setUserFace";
    public static String getEmptyClassRoomInfo = baseUrl+"/TustBox/index.php/Home/EmptyClassRoom/getEmptyClassRoom";
    //默认头像
    public static String normalFace = "https://i.loli.net/2017/10/18/59e71d7296e0c.jpg";



    //校园网登录页面
    public static final String net_post_url = "http://59.67.0.245/a30.htm";
    //校园网登录重定向
    public static final String net_get_url = "http://59.67.0.245";
    //网费充值
    public static final String charge_page = "http://59.67.5.142/WebPay/toRecharge";

    //用户须知和使用说明
    public static final String notice_and_introduce = "http://surine.cn/TustBox/WebPage/user_login_page.html";
    //校园板块公告
    public static final String rulesUrl = "http://surine.cn/TustBox/WebPage/rulesUrl.html";

    //云盘登录
    public static final String pan_login = "http://pan.tust.edu.cn/v1/auth/sign_in?link_name=web&link_device=web&";
}
