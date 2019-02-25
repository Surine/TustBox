package com.surine.tustbox.Helper.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by surine on 2017/9/2.
 * 数据储存管理类
 */

public class SharedPreferencesUtil {
    //储存部分重载
    public static void save(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static void save(Context context, String key, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static void save(Context context, String key, boolean value){
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


    //储存部分重载（值加密，key无加密）
    public static void Save_safe(Context context,String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString(key,EncryptionUtil.base64_en(value));
        editor.apply();
    }
    public static void Save_safe(Context context,String key, int value){
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
            editor.putInt(key, Integer.parseInt(EncryptionUtil.base64_en(String.valueOf(value))));
            editor.apply();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    public static void Save_safe(Context context,String key, boolean value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putBoolean(key, Boolean.parseBoolean(EncryptionUtil.base64_en(String.valueOf(value))));
        editor.apply();
    }
    //读取部分重载（值解密）
    public static int Read_safe(Context context,String key,int normal){
        SharedPreferences pref = context.getSharedPreferences("data",MODE_PRIVATE);
        return Integer.parseInt(EncryptionUtil.base64_de(String.valueOf(pref.getInt(key,normal))));
    }
    public static String Read_safe(Context context,String key,String normal){
        SharedPreferences pref = context.getSharedPreferences("data",MODE_PRIVATE);
        return EncryptionUtil.base64_de(pref.getString(key,normal));
    }
    public static Boolean Read_safe(Context context,String key,boolean normal){
        SharedPreferences pref = context.getSharedPreferences("data",MODE_PRIVATE);
        return Boolean.valueOf(EncryptionUtil.base64_de(String.valueOf(pref.getBoolean(key,normal))));
    }
}
