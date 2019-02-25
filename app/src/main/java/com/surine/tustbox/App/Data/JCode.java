package com.surine.tustbox.App.Data;

/**
 * Created by Surine on 2018/8/14.
 * 将逐渐规范返回码
 */

public class JCode {
    //请求成功
    public static final String J2000 = "2000";
    //请求失败
    public static final String J4004 = "4004";
    //参数缺少
    public static final String J4000 = "4000";
    //鉴权失败
    public static final String J4001 = "4001";


    /**
    *  下面的code是小天常用的，后期将统一重新开发后台，编写固定的状态吗
    * */

    //请求成功
    public static final int J400 = 400;
    //待定
    public static final int J200 = 200;

    //JSON错误码
    public static final int ERROR = Integer.MIN_VALUE;
}
