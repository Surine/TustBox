package com.surine.tustbox.Mvp.splash;

import com.surine.tustbox.Mvp.base.BaseCallBack;

/**
 * Created by Surine on 2018/9/14.
 * Splach model
 */

public interface SplashModel {

    /**
     * 检查token合法性
     * @param baseCallBack 回调
     * */
    void checkToken(BaseCallBack<String> baseCallBack);

    /**
     * 注册
     * @param baseCallBack
     */
    void register(BaseCallBack<String> baseCallBack);

    /**
     * 获取当前周
     * @param baseCallBack
     * */
    void getWeek(BaseCallBack<String> baseCallBack);

    /**
     * 检查登录状态
     * @param baseCallBack 回调
     * */
    void checkLoginStatus(BaseCallBack<String> baseCallBack);
}
