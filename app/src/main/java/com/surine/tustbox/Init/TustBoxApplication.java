package com.surine.tustbox.Init;

import android.app.Application;

import com.lzy.ninegrid.NineGridView;
import com.surine.tustbox.Util.GlideImageLoader;

import org.litepal.LitePalApplication;



/**
 * Created by surine on 2017/5/3.
 */

public class TustBoxApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init the litepal
        LitePalApplication.initialize(getApplicationContext());
        NineGridView.setImageLoader(new GlideImageLoader());
    }
}

