package com.surine.tustbox.Util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by surine on 2017/4/5.
 * 时间工具
 */

public class TimeUtil {
    //yMdHm格式
    public static final String yMdHm = "yyyy-MM-dd HH:mm";
    //yMdHm格式
    public static final String yMd = "yyyy-MM-dd";
    //E格式(周)
    public static final String E = "E";
    //dd格式（日）
    public static final String dd = "dd";
    //dd格式2
    public static final String dd2 = "dd日";

    //月份中文
    public static final String []monthCC={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};

    //一天的时间戳
    public static final long dayTimeMillis = 1000 * 60 * 60 *24;
    //一个小时的时间戳
    public static final long hourTimeMillis = dayTimeMillis / 24;
    //一分钟的时间戳
    public static final long minTimeMillis = hourTimeMillis / 60 ;

    static SimpleDateFormat format;


    //获取某种格式下的时间
    public static String getDate(String pattern){
        format = new SimpleDateFormat(pattern);
        return format.format(new Date(System.currentTimeMillis()));
    }


    //获取星期
    /**
     * 此方法已过时：推荐使用getData(E)
     * 2018年8月13日 13点13分
     * */
    public static String GetWeek(){
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        SimpleDateFormat format=new SimpleDateFormat("E");
        return format.format(date);
    }


    //获取时间
    public static int GetHour() {
        SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
        return Integer.parseInt(df.format(new Date()));// new Date()为获取当前系统时间
    }



    //获取泰达西上课时间
    public static String getCourseTime(String a){
        String r = "0:00";
        if(a.contains("1")){
            r = "8:20";
        }else if(a.contains("3")){
            r = "10:20";
        }else if(a.contains("5")){
            r = "14:00";
        }else if(a.contains("7")){
            r = "15:55";
        }else if(a.contains("9")){
            r = "18:30";
        }else if(a.contains("11")){
            r = "20:00";
        }
        return r;
    }


    //获取周数字
    public static String getWeekNumber() {
        String week = getDate(E);
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


    //通过日期和时间字符串得到日期字符串
    //已过时，推荐getStringByTimeString
    public static String getDateByTimeString(String dateString){
        SimpleDateFormat df = new SimpleDateFormat(yMdHm);//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Date date;
        try {
            date = df.parse(dateString);
            return df2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //通过日期和时间字符串得到日字符串
    //已过时，推荐getStringByTimeString
    public static String getDayByTimeString(String dateString){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("dd");//设置日期格式
        Date date;
        try {
            date = df.parse(dateString);
            return df2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //通过日期和时间字符串得到月字符串
    //已过时，推荐getStringByTimeString
    public static String getMonthByTimeString(String dateString){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        SimpleDateFormat df2 = new SimpleDateFormat("MM");//设置日期格式
        Date date;
        try {
            date = df.parse(dateString);
            return df2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取当前月份的中文
    public static String getNowMonthCC(){
        return monthCC[new Date().getMonth()];
    }

    //获取月份中文
    public static String getMonthCC(String dateString){
        String month = getMonthByTimeString(dateString);
        if(month == null){
            return null;
        }else{
            int monthNumber = Integer.parseInt(month);
            return monthCC[monthNumber - 1];
        }
    }

    //获取时间离目前时间的差（个性化提示）
    public static String getTimeSubString(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        long from = 0;
        long to = 0;
        int days = 0;
        int hours = 0;
        try {
            to = df.parse(time).getTime();
            from = new Date().getTime();
            hours = (int) ((to - from) / (1000 * 60 * 60));
            if(hours >= 24){
                if(hours % 24 == 0){
                    return hours / 24 + "天";
                }else{
                    return (hours / 24) + "天"+(hours % 24 ) + "小时";
                }
            }else if(hours > 0 && hours < 24){
                return hours+"小时";
            }else if(hours == 0){
                return "<1小时";
            }
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    //获取时间距离目前时间的差（精确到天）
    public static String getTimeSubDayString(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        long from = 0;
        long to = 0;
        int days = 0;
        int hours = 0;
        try {
            to = df.parse(time).getTime();
            from = new Date().getTime();
            hours = (int) ((to - from) / (1000 * 60 * 60));
            if(hours >= 24){
                return (hours / 24 +1)+ "天";
            }else{
                return 1 + "天";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    //比较两个时间大小,前者小返回-1，后者小返回1，相等为0
    public static int compareDate(String s1,String s2){
        format = new SimpleDateFormat(yMdHm);
        long sTime1,sTime2;
        try {
            sTime1 = format.parse(s1).getTime();
            sTime2 = format.parse(s2).getTime();

            if(sTime1 < sTime2){
                return -1;
            }

            if(sTime1 == sTime2){
                return 0;
            }

            if(sTime1 > sTime2){
                return 1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取一个时间的前几天或者后几天
    /**
     * 参数：时间字符串（ymdhm），几天，是前还是后（true是后）
     * */
    public static String getDateBeforeOrAfter(String date,int number,boolean isAfter){
        format = new SimpleDateFormat(yMdHm);
        try {
            long dateNumber = format.parse(date).getTime();
            if(isAfter) {
               //向后运算
                long dateResult = dateNumber + number * (1000 * 60 * 60 * 24);
                return format.format(dateResult);
            }else{
                long dateResult = dateNumber - number * (1000 * 60 * 60 * 24);
                return format.format(dateResult);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 通过日期和时间字符串得到某种形式的字符串
     * 参数:日期时间字符串（ymdhm格式），目标字符串格式
     * 注意：前者的形式一定能得出后者才能使用，如果前者精确度差，后者精确度高则不能使用
     * */
    public static String getStringByTimeString(String dateString,String pattern){
        format = new SimpleDateFormat(yMdHm);
        SimpleDateFormat formatResult = new SimpleDateFormat(pattern);//设置日期格式
        Date date;
        try {
            date = format.parse(dateString);
            return formatResult.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }
}

