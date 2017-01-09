package com.quxin.freshfun.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间戳工具类
 * Created by qucheng on 17/1/5.
 */
public class TimestampUtils {


    /**
     * 查询当天凌晨0:00的时间戳
     * @return 当天开始时间戳
     */
    public static Long getStartTimestamp(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        return todayStart.getTime().getTime()/1000 ;
    }

    /**
     * 根据起止时间获取日期数组
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @return 日期数组
     */
    public static String[] getDates(Long startDate, Long endDate){
        Long length = (endDate-startDate)/86400;
        Integer days = Integer.parseInt(length.toString())+1;
        String[] date = new String[days];
        for(int i=0;i<days;i++){
            date[i] = TimestampUtils.getStringDateFromLong(startDate+i*86400);
        }
        return date;
    }

    /**
     * 根据时间戳获取年月日时间字符串
     * @param date 时间戳
     * @return 时间字符串
     */
    public static String getStringDateFromLong(Long date){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        String dateString = "";
        if(date.toString().length()>10){
            dateString = format.format(date);
        }else{
            dateString = format.format(date*1000);
        }
        return dateString;
    }

    /**
     * 根据日期获取年月日时间字符串
     * @param date 时间戳
     * @return 时间字符串
     */
    public static String getStringDateFromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        return dateString;
    }

    /**
     * 获取前一天的date yyyy-MM-dd
     * @return 字符串型的日期
     */
    public static String getDateFromTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(System.currentTimeMillis()-86400000));
    }
    /**
     * 获取昨天的开始时间
     * @param date 当前开始时间
     * @return 昨天时间
     */
    public static Long getYesterdayStartTime(long date){
        return date - 86400;
    }

    /**
     * 获取指定时间后的天数
     * @param date 传入时间戳
     * @param day 需要减去的天数
     * @return 减去天后时间戳
     */
    public static long getPastDate(long date,int day){
        Calendar calendar = Calendar.getInstance();
        Date dateTime = new Date((date*1000));
        calendar.setTime(dateTime);
        calendar.add(Calendar.DATE, -day);
        return calendar.getTime().getTime()/1000;
    }

}
