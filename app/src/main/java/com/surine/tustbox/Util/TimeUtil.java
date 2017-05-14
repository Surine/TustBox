package com.surine.tustbox.Util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by surine on 2017/4/5.
 */

public class TimeUtil {
    //获取星期
    public static String GetWeek(){
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("E");
        Log.d("DEBUG", "GetWeek: "+format.format(date));
        return format.format(date);
    }
}
