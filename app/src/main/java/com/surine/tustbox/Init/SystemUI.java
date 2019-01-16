package com.surine.tustbox.Init;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;

import com.surine.tustbox.Util.SharedPreferencesUtil;

/**
 * Created by surine on 2017/2/17.
 * 类名：SystemUI
 */

public class SystemUI {


    //设置沉浸状态栏（SDK>21）
    public static void Setting(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    //颜色
    public static void StatusUISetting(Activity context, String color){
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = context.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            context.getWindow().setStatusBarColor(Color.parseColor(color));
        }
    }



    //隐藏状态栏
    public static void hideStatusbar(Activity activity){
        //full screen
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static  void color_toolbar(Activity activity,ActionBar ab){
        //设置toolbar颜色
        int color = Integer.parseInt
                (String.valueOf(SharedPreferencesUtil.Read(activity,"TOOLBAR_C", -1)));
        if(color != -1){
            ab.setBackgroundDrawable(new ColorDrawable(color));
        }
    }
}
