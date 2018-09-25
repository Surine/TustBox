package com.surine.tustbox.Util;

import android.content.Context;

import com.surine.tustbox.Data.Constants;
import com.surine.tustbox.Data.FormData;

/**
 * Created by Surine on 2018/8/9.
 * 提供一些工具，可以通过一定条件快速查询到信息
 */

public class TustBoxUtil {
    private Context context;

    public TustBoxUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取当前周，如果获取不到返回1
     * */
    public int getWeek(){
        return SharedPreferencesUtil.Read(context, Constants.CHOOSE_WEEK, 1);
    }

    /**
     * 获取当前用户科大学号
     * */
    public String getUid(){
        return SharedPreferencesUtil.Read(context, FormData.tust_number_server,"");
    }

    /**
     * 获取当前用户科大学号的密码
     * */
    public String getPswd(){
        return SharedPreferencesUtil.Read(context, FormData.pswd,"");
    }

    /**
     * 获取TOKEN
     * */
    public String getToken(){
        return SharedPreferencesUtil.Read_safe(context,FormData.TOKEN,"");
    }
}
