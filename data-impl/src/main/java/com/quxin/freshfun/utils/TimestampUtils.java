package com.quxin.freshfun.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

}
