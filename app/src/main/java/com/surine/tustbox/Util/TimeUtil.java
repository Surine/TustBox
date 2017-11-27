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

    //获取时间
    public static String GetDate() {
        SimpleDateFormat df = new SimpleDateFormat("MMdd");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    //获取时间
    public static String GetDate_or() {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }


    //获取泰达西上课时间
    public static String getCourseTime(String a){
        String r = "0:00";
        if(a.contains("一")){
            r = "8:20";
        }else if(a.contains("二")){
            r = "10:20";
        }else if(a.contains("三")){
            r = "14:00";
        }else if(a.contains("四")){
            r = "15:55";
        }else if(a.contains("五")){
            r = "18:30";
        }else if(a.contains("六")){
            r = "20:00";
        }
        return r;
    }


    //获取周数字
    public static String GetWeekNumber() {
        String week = GetWeek();
        String week_numer = "1";
        if (week.equals("周一") || week.equals("Mon")) {
            week_numer = "1";
        } else if (week.equals("周二") || week.equals("Tue")) {
            week_numer = "2";
        } else if (week.equals("周三") || week.equals("Wed")) {
            week_numer = "3";
        } else if (week.equals("周四") || week.equals("Thu")) {
            week_numer = "4";
        } else if (week.equals("周五") || week.equals("Sat")) {
            week_numer = "5";
        } else if (week.equals("周六") || week.equals("Sun")) {
            week_numer = "6";
        } else if (week.equals("周日") || week.equals("Mon")) {
            week_numer = "7";
        }
        return week_numer;
    }

    //get number
    public static int GetNumber(String word){
        int number = 1;
        if(word.contains("一")){
            number = 1;
        }else if(word.contains("二")){
            number = 2;
        }else if(word.contains("三")){
            number = 3;
        }else if(word.contains("四")){
            number = 4;
        }else if(word.contains("五")){
            number = 5;
        }else if(word.contains("六")){
            number = 6;
        }else if(word.contains("七")){
            number = 7;
        }else if(word.contains("八")){
            number = 8;
        }else if(word.contains("九")){
            number = 9;
        }else if(word.contains("十")){
            number = 10;
        }
        return number;
    }

    public static String GetNavTime() {
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("dd/MM月 yy E");
        return format.format(date);
    }
}

