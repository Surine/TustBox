package com.surine.tustbox.Mvp.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.surine.tustbox.App.Init.SystemUI;
import com.surine.tustbox.Mvp.base.BaseMvpActivity;
import com.surine.tustbox.Mvp.login.LoginMvpActivity;
import com.surine.tustbox.R;
import com.surine.tustbox.UI.Activity.MainActivity;
import com.surine.tustbox.Helper.Utils.LogUtil;

import butterknife.ButterKnife;

/**
 * Created by Surine on 2018/9/14.
 */

public class SplashMvpActivity extends BaseMvpActivity implements SplashView {
    private SplashPresenter mPresenter;
    private String TAG = this.getClass().getName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        SystemUI.hideStatusbar(this);
        context = this;
        mPresenter = new SplashPresenter(this);
        //绑定view
        mPresenter.attachView(this);
        //开始检查登录状态
        mPresenter.startCheckLoginStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mPresenter.detachView();
    }

    //辅助
    @Override
    public void showData(String data) {
        LogUtil.d(TAG,data);
    }


    //跳转主页
    @Override
    public void intentMain() {
        startActivity(new Intent(context, MainActivity.class));
        //结束当前活动
        finish();
    }

    //跳转登录
    @Override
    public void intentLogin() {
        Intent intent = new Intent(context, LoginMvpActivity.class);
        startActivity(intent);
        //结束当前活动
        finish();
    }

}
