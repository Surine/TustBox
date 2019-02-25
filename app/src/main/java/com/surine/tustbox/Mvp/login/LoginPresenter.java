package com.surine.tustbox.Mvp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.surine.tustbox.Mvp.base.BaseCallBack;
import com.surine.tustbox.Mvp.base.BasePresenter;
import com.surine.tustbox.Mvp.base.DialogEvent;
import com.surine.tustbox.Pojo.Cookies;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.MainActivity;

/**
 * Created by Surine on 2018/9/2.
 * Mvp：presenter
 */

public class LoginPresenter extends BasePresenter<LoginView> {

    private Resources r;
    private Context context;
    private LoginImpl loginImpl;


    public LoginPresenter(Context context) {
        this.context = context;
        r = context.getResources();
        loginImpl = new LoginImpl(context);
    }

    //登录教务处
    public void startLogin(final String tustNumber, final String pswd, String verifyCode,final String cookies) {
        //如果没有View引用就不加载数据
        if(!isViewAttached()){
            return;
        }

        if(tustNumber.isEmpty()||pswd.isEmpty()){
            getView().showToast(r.getString(R.string.mvp_empty_field));
            return;
        }

        if(verifyCode.isEmpty()){
            verifyCode = "";
        }

        //显示正在加载进度条
        getView().showLoading(r.getString(R.string.mvp_notice),r.getString(R.string.mvp_login_jwc));

        loginImpl.loginJwc(tustNumber, pswd, verifyCode,cookies,new BaseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().setLoadingText(r.getString(R.string.readyGetCourse));
                }
                //开始获取教务处课表
                getJwCourse();
            }

            @Override
            public void onFail(String msg) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().showToast(msg);
                    getView().hideLoading();
                }
            }

            @Override
            public void onError() {
                //调用view接口提示请求异常
                if(isViewAttached()){
                    getView().showErrorMessage();
                    getView().hideLoading();

                    //普通对话框显示
                    getView().showDialog(r.getString(R.string.mvp_notice),
                            r.getString(R.string.loginJwcFail_get_back_error),
                            r.getString(R.string.mvp_get_cloud),
                            r.getString(R.string.cancel),
                            new DialogEvent() {
                                @Override
                                public void p() {
                                    if(isViewAttached()){
                                        getView().showLoading(r.getString(R.string.mvp_notice),r.getString(R.string.get_backUping));
                                    }
                                    //获取备份
                                    getBackup(tustNumber,pswd);
                                }

                                @Override
                                public void n() {
                                    if(isViewAttached()){
                                        getView().hideDialog();
                                    }
                                }
                            }
                    );
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //获取云备份
    private void getBackup(String tustNumber, String passWord) {
        loginImpl.getBackUp(tustNumber,passWord,new BaseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().setLoadingText(r.getString(R.string.parseCourse));
                }
                parseCourse(data);
            }

            @Override
            public void onFail(String msg) {
               if (isViewAttached()){
                   getView().showToast(msg);
                   getView().hideLoading();
               }
            }

            @Override
            public void onError() {
               if(isViewAttached()){
                   getView().showErrorMessage();
                   getView().hideLoading();
               }
            }

            @Override
            public void onComplete() {

            }
        });
    }


    //获取课程表
    private void getJwCourse(){
        loginImpl.getJwCourse(new BaseCallBack<String>() {

            @Override
            public void onSuccess(String data) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().setLoadingText(r.getString(R.string.parseCourse));
                }
                parseCourse(data);
            }

            @Override
            public void onFail(String msg) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().showToast(msg);
                    getView().hideLoading();
                }
            }

            @Override
            public void onError() {
                //调用view接口提示请求异常
                if(isViewAttached()){
                    getView().showErrorMessage();
                    getView().hideLoading();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //解析课程表
    public void parseCourse(String data) {
       loginImpl.parseCourse(data, new BaseCallBack<String>() {
           @Override
           public void onSuccess(String data) {
               //解析成功，获取个人信息
               //调用view接口提示失败信息
               if(isViewAttached()){
                   getView().setLoadingText(r.getString(R.string.get_info));
               }

               //如果是tag1说明是从云备份来的，将跳转
               if(data.equals("TAG1")){
                  mainIntent();
               }else {
                   getUserInfo();
               }
           }

           @Override
           public void onFail(String msg) {
               //调用view接口提示失败信息
               if(isViewAttached()){
                   getView().showToast(msg);
                   getView().hideLoading();
               }
           }

           @Override
           public void onError() {
                //调用view接口提示请求异常
               if(isViewAttached()){
                   getView().showErrorMessage();
                   getView().hideLoading();
               }
           }

           @Override
           public void onComplete() {

           }
       });
    }

    //获取个人信息
    private void getUserInfo() {
        loginImpl.getJwInfo(new BaseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().setLoadingText(r.getString(R.string.parse_info));
                }
                //解析个人数据
                parseUserInfo(data);
            }

            @Override
            public void onFail(String msg) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().showToast(msg);
                    getView().hideLoading();
                }
            }

            @Override
            public void onError() {
                //调用view接口提示请求异常
                if(isViewAttached()){
                    getView().showErrorMessage();
                    getView().hideLoading();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //解析个人数据
    private void parseUserInfo(String data) {
        loginImpl.parseUserInfo(data, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                //解析个人信息成功，准备云备份
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().setLoadingText(r.getString(R.string.backUping));
                }
                backUpCourse();
            }

            @Override
            public void onFail(String msg) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().showToast(msg);
                    getView().hideLoading();
                }
            }

            @Override
            public void onError() {
                //调用view接口提示请求异常
                if(isViewAttached()){
                    getView().showErrorMessage();
                    getView().hideLoading();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    //云备份
    private void backUpCourse() {
      loginImpl.backUpCourse(new BaseCallBack<String>() {
          @Override
          public void onSuccess(String data) {
              if(isViewAttached()){
                  getView().hideLoading();
              }
          }

          @Override
          public void onFail(String msg) {
              //调用view接口提示失败信息
              if(isViewAttached()){
                  //加载对话框隐藏
                  getView().hideLoading();
                  getView().showToast(msg);
              }
          }

          @Override
          public void onError() {
              //调用view接口提示请求异常
              if(isViewAttached()){
                  getView().showErrorMessage();
                  getView().hideLoading();
                  //普通对话框显示
                  getView().showDialog(r.getString(R.string.mvp_notice),
                          r.getString(R.string.loginJwcFail_backup_error),
                          r.getString(R.string.mvp_retry),
                          r.getString(R.string.cancel),
                          new DialogEvent() {
                              @Override
                              public void p() {
                                  if(isViewAttached()){
                                      getView().showLoading(r.getString(R.string.mvp_notice),r.getString(R.string.backUping));
                                  }
                                  //重新备份
                                  backUpCourse();
                              }

                              @Override
                              public void n() {
                                  if(isViewAttached()){
                                      getView().hideDialog();
                                  }
                                  mainIntent();
                              }
                          }
                  );
              }
          }

          @Override
          public void onComplete() {
              //跳转
              mainIntent();
          }
      });
    }

    //跳转
    private void mainIntent(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        Activity activity = (Activity)context;
        activity.finish();
    }

    /**
     * 加载验证码
     * */
    public void getVerifyCode(Context context) {
        loginImpl.getVerifyCode(new BaseCallBack<Cookies>() {
            @Override
            public void onSuccess(Cookies data) {
                getView().getVerifyData(data);
            }

            @Override
            public void onFail(String msg) {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
