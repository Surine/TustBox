package com.surine.tustbox.Mvp.login;

import com.surine.tustbox.Mvp.base.BaseCallBack;

/**
 * Created by Surine on 2018/9/2.
 */

public interface LoginModel {

    /**
     * 登录教务处
     * @param tustNumber 学号
     * @param pswd 密码
     * @param baseCallBack 回调
     * */
    void loginJwc(String tustNumber, String pswd, BaseCallBack<String> baseCallBack);

    /**
     * 取得课程表
     * @param baseCallBack 回调
     * */
    void getJwCourse(BaseCallBack<String> baseCallBack);

    /**
     * 解析课程表
     * @param baseCallBack 回调
     * @param json 被解析的json串
     * */
    void parseCourse(String json,BaseCallBack<String> baseCallBack);


    /**
     * 获取个人信息
     * @param baseCallBack 回调
     * */
    void getJwInfo(BaseCallBack<String> baseCallBack);

    /**
     * 解析个人信息
     * @param baseCallBack 回调
     * @param json 被解析的json串
     * */
    void parseUserInfo(String json,BaseCallBack<String> baseCallBack);


    /**
     * 备份课表
     * @param baseCallBack 回调
     * */
    void backUpCourse(BaseCallBack<String> baseCallBack);


    /**
     * 获取云课表
     * @param baseCallBack 回调
     * */
    void getBackUp(String tustNumber, String pswd,BaseCallBack<String> baseCallBack);
}
