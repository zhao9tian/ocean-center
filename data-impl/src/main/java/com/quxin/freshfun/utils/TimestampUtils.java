package com.quxin.freshfun.utils;

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


}
