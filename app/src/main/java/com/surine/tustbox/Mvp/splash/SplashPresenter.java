package com.surine.tustbox.Mvp.splash;

import android.content.Context;
import android.content.res.Resources;

import com.surine.tustbox.Mvp.base.BaseCallBack;
import com.surine.tustbox.Mvp.base.BasePresenter;
import com.surine.tustbox.R;

import static com.surine.tustbox.Data.Constants.ERROR_PSWD;
import static com.surine.tustbox.Data.Constants.NON_REGISTER;

/**
 * Created by Surine on 2018/9/14.
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    private Resources r;
    private Context context;
    private SplashImpl splashImpl;


    public SplashPresenter(Context context) {
        this.context = context;
        r = context.getResources();
        splashImpl = new SplashImpl(context);
    }

    //检查登录状态
    public void startCheckLoginStatus(){
        splashImpl.checkLoginStatus(new BaseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //检查成功-准备登录
                if(isViewAttached()){
                    getView().intentLogin();
                }
            }

            @Override
            public void onFail(String msg) {
                //检查失败- 跳转主页准备
                if(isViewAttached()){
                    checkToken();
                }
            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    //检查token
    private void checkToken() {
        splashImpl.checkToken(new BaseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()){
                    //成功之后
                    getWeek();
                }
            }

            @Override
            public void onFail(String msg) {
               if (isViewAttached()){
                   if(msg.equals(NON_REGISTER)){
                       //用户未注册
                       register();
                   }else if(msg.equals(ERROR_PSWD)){
                       //帐号密码错误
                       getView().showToast(r.getString(R.string.failtologinpswderror));
                   }
               }
            }

            @Override
            public void onError() {
               if(isViewAttached()){
                   getView().intentMain();
                   //网络错误
                   getView().showErrorMessage();
               }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //进行用户注册
    private void register() {
        splashImpl.register(new BaseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()){
                    //准备登录
                    checkToken();
                }
            }

            @Override
            public void onFail(String msg) {
                //注册失败
              if (isViewAttached()){
                  getView().showToast(msg);
              }
            }

            @Override
            public void onError() {
                //注册错误
              if (isViewAttached()){
                  getView().showErrorMessage();
              }
            }

            @Override
            public void onComplete() {

            }
        });
    }


    //获取当前周
    private void getWeek() {
       splashImpl.getWeek(new BaseCallBack<String>() {
           @Override
           public void onSuccess(String data) {
               if (isViewAttached()){
                   getView().intentMain();
               }
           }

           @Override
           public void onFail(String msg) {

           }

           @Override
           public void onError() {
             if (isViewAttached()){
                 getView().showErrorMessage();
             }
           }

           @Override
           public void onComplete() {

           }
       });
    }

}
