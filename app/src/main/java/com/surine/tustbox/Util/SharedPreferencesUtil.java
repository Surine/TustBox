package com.surine.tustbox.Util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/9/2.
 * 数据储存管理类
 */

public class SharedPreferencesUtil {
    //储存部分重载
    public static void Save(Context context,String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static void Save(Context context,String key, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public static void Save(Context context,String key, boolean value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    //读取部分重载
    public static int Read(Context context,String key,int normal){
        SharedPreferences pref = context.getSharedPreferences("data",MODE_PRIVATE);
        return pref.getInt(key,normal);
    }
    public static String Read(Context context,String key,String normal){
        SharedPreferences pref = context.getSharedPreferences("data",MODE_PRIVATE);
        return pref.getString(key,normal);
    }
    public static Boolean Read(Context context,String key,boolean normal){
        SharedPreferences pref = context.getSharedPreferences("data",MODE_PRIVATE);
        return pref.getBoolean(key,normal);
    }
}
