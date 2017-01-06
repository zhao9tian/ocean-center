package com.quxin.freshfun.utils;

/**
 * 百分比计算工具
 * Created by qucheng on 17/1/6.
 */
public class PercentUtils {

    public static Integer getPercent(Integer top , Integer bottom){
        if(0 == bottom){
            return 0 ;
        }else{
            return Math.round((float) top/bottom *10000);
        }
    }
}
