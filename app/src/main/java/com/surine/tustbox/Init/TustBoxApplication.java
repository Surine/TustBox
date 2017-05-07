package com.surine.tustbox.Init;

import android.app.Application;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by surine on 2017/5/3.
 */

public class TustBoxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init the litepal
        LitePalApplication.initialize(getApplicationContext());
        //init the jpush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}

