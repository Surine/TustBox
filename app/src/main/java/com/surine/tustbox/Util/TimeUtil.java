package com.surine.tustbox.Util;

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
        return format.format(date);
    }
}
