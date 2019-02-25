package com.surine.tustbox.Helper.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Surine on 2019/1/10.
 * 吐司大作战
 */

public class ToastUtil {
   private static Context mContext;
   public static void getInstance(Context context){
       mContext = context;
   }

   /**
    * 网络错误
    * */
   public static void netError(){
       Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
   }

   /**
    * 请求成功
    * */
   public static void getSuccess(){
       Toast.makeText(mContext, "请求成功！", Toast.LENGTH_SHORT).show();
   }

   /**
    * 身份认证失败
    * */
    public static void authError(){
        Toast.makeText(mContext, "帐号密码不正确或者身份认证失败！", Toast.LENGTH_SHORT).show();
    }

    /**
     * JSON解析失败
     * */
    public static void jsonError(){
        Toast.makeText(mContext, "JSON解析失败！", Toast.LENGTH_SHORT).show();
    }

    /**
     * 参数缺失
     * */
    public static void paramError(){
        Toast.makeText(mContext, "参数缺失！", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msg 文本
     * */
    public static void show(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msg 文本int
     * */
    public static void show(int msg){
        Toast.makeText(mContext, mContext.getResources().getString(msg), Toast.LENGTH_SHORT).show();
    }

    /**
     * 请求失败
     * */
    public static void getError() {
        Toast.makeText(mContext, "执行动作失败！", Toast.LENGTH_SHORT).show();
    }
}
