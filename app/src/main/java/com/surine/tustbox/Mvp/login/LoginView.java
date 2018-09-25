package com.surine.tustbox.Mvp.login;

import com.surine.tustbox.Mvp.base.BaseView;

/**
 * Created by Surine on 2018/9/2.
 * MVP：登录界面接口
 */

public interface LoginView extends BaseView{

    /**
     * 数据请求成功
     * @param data 请求的数据
     * */
    void showData(String data);

}
