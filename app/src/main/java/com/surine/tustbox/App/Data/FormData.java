package com.surine.tustbox.App.Data;

/**
 * Created by surine on 2017/7/9.
 *表单数据段
 */

public class FormData {


   public static final String JCODE = "jcode";
   public static final String JDATA = "jdata";
   public static final String POSITION = "POSITION";
   public static final String TOKEN = "TOKEN";
   public static final String USER_TYPE = "user_type";
   public static final String pswd = "pswd";
   public static final String CREATE = "CREATE";
   public static final String USERID = "USERID";


   //请求登陆教务处的账号和密码（启动APP登陆页）
   public static final String login_id = "IDToken1";
   public static final String login_pswd = "IDToken2";
   public static final String login_id_new = "j_username";
   public static final String login_pswd_new = "j_password";
   public static final String login_captcha_new = "j_captcha";

    //请求登录校园网固定参数
    public static final String R1 = "0";
    public static final String R2 = "";
    public static final String R7 = "0";
    public static final String para = "00";
    public static final String MKKey = "123456";
    public static final String R6 = "1";
    public static final String Drcom_pc_Login_success = "Drcom PC登陆成功页";
    public static final String Drcom_pc_get_success = "Drcom PC注销页";

    //服务器固定参数
    public static final String tust_number_server = "tust_number";
    public static final String pass_server = "pass";
    public static final String sign_server = "sign";
    public static final String school_name_server = "schoolname";
    public static final String face_server = "face";
    public static final String nick_name_server = "nick_name";
    public static final String college_server = "college";
    public static final String sex = "sex";
    public static final String token = "token";
    public static final String courseNumber = "course_number";
    public static final String courseScore = "course_score";
    public static final String messages_info = "messages_info";
    public static final String messages_topic = "messages_topic";
    public static final String messages_device = "messages_device";
    public static final String pic_ids = "pic_ids";  //图片链接集合
    public static final String did = "did";   //动态id
    public static final String uid = "uid";  //uid一般是指学号
    public static final String comment_content = "comment_content";  //评论内容
    public static final String page = "page";  //页码
    public static final String cid = "cid";  //评论id
    public static final String id = "id";  //通用id
    public static final String say_info = "say_info";  //回复内容
    public static final String say_at = "say_at";  //@用户
    public static final String toUser = "touser";  //消息接受用户




    //科大云盘固定参数
    public static final String link_name = "link_name";
    public static final String link_device = "link_device";
    public static final String username = "username";
    public static final String password = "password";
    public static final String locale = "locale";


    //tag(key值)
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String PICCROP = "pic_crop";

    //服务器当前周key
    public static final String WEEK = "week";

    //课表云备份
    public static final String VALUE = "value";


    //语言字段
    public static final String LOCALE_DATA = "zh_CN";
    //设置Cookie
    public static final String SET_COOKIE = "Set-Cookie";
    //UA及默认UA
    public static final String UA = "User-Agent";
    public static final String NORMAL_UA = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36";
    //CT及默认CT
    public static final String CT = "Content-Type";
    public static final String NORMAL_CT = "application/x-www-form-urlencoded";

    public static final String JSESSIONID = "JSESSIONID";

    //video intent flag
    public static final String VIDEO_URL = "VIDEO_URL";
    public static final String VIDEO_NAME = "VIDEO_NAME";


    //认证时间
    public static final String AUTHTIME = "AUTH_TIME";
}
