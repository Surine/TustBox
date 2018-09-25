package com.surine.tustbox.Mvp.base;

/**
 * Created by Surine on 2018/9/2.
 * MVP：登录回调
 */

public interface BaseCallBack<T> {

    /**
     * 请求成功
     * @param data 请求到的数据
     * */
    void onSuccess(T data);


    /**
     * 请求失败，一般是因为返回码不正常导致的
     * @param msg 失败原因
     * */
    void onFail(String msg);


    /**
     * 请求错误，一般是因为网络不通畅
     * */
    void onError();


    /**
     * 请求完成，做一些view操作
     * */
    void onComplete();
}
