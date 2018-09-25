package com.surine.tustbox.Mvp.splash;

import com.surine.tustbox.Mvp.base.BaseView;

/**
 * Created by Surine on 2018/9/14.
 * 登录界面
 */

public interface SplashView extends BaseView {
    /**
     * 数据请求成功
     * @param data 请求的数据
     * */
    void showData(String data);

    /**
     * 跳转主页
     * */
    void intentMain();

    /**
     * 跳转登录
     * */
    void intentLogin();
}
