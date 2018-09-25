package com.surine.tustbox.Mvp.base;

/**
 * Created by Surine on 2018/9/2.
 * Mvp：presenter
 */

public class BasePresenter<V extends BaseView> {

    /**
     * 当前绑定的view，继承自Baseview
     * */
    private V mvpView;


    /**
     * 绑定view，在初始化时候使用
     * */
    public void  attachView(V mvpView){
       this.mvpView = mvpView;
    }

    /**
     * 销毁view，在destory中调用
     * */
    public void detachView(){
        this.mvpView = null;
    }

    /**
     * 返回view是否被绑定，调用前需要先检查
     * */
    public boolean isViewAttached(){
        return mvpView != null;
    }

    /**
     * 获取view
     * */
    public V getView(){
        return mvpView;
    }
}
