package com.surine.tustbox.App.Init;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by surine on 2017/2/17.
 *
 * 类名：SystemUI
 */

public class SystemUI {

    /**
     * 隐藏状态栏
     * @param activity 待处理Acitivity
     * */
    public static void hideStatusbar(Activity activity){
        //full screen
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 状态栏字体颜色
     * @param b b为true时候是黑色字体，false是白色字体
     * @param activity 待处理Acitivity
     * */
    public static void setStatusBarColor(boolean b, Activity activity) {
        Window window = activity.getWindow();
        //黑色状态栏
        if(b){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }else{
            //白色状态栏
            int flag = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                flag = window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    /**
     * 设置沉浸式状态栏
     * @param activity 页面
     * */
    public static void fullScreenStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);//透明
    }


    public static void setStatusBarTextColor(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 对外接口（Google原生）
     * 配置沉浸状态栏
     * @param activity UI
     * @param dark 通知栏文字颜色是否为黑色，true为黑色
     * */
    public static void setStatusBarUI(Activity activity,boolean dark){
        SystemUI.fullScreenStatusBar(activity);
        SystemUI.setStatusBarTextColor(activity,dark);
    }

    public static void setStatusBarUI(Activity activity){
        setStatusBarUI(activity,true);
    }

}
