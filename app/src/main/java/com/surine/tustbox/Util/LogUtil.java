package com.surine.tustbox.Util;

import android.util.Log;

/**
 * Created by Surine on 2018/9/2.
 * 日志管理
 */

public class LogUtil {
    public static final String TAG = "测试";
    /**
     * 上线后需要返回false
     * */
    private static boolean isDebug(){
        return true;
    }

    /**
     * debug
     * */
    public static void d(String tag,String msg){
        if(isDebug()){
            Log.d(tag,msg);
        }
    }

    public static void d(String msg){
        if(isDebug()){
            Log.d(TAG,msg);
        }
    }

}
