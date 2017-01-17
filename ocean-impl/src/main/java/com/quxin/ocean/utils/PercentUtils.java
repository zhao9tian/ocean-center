package com.quxin.ocean.utils;

/**
 * 百分比计算工具
 * Created by qucheng on 17/1/6.
 */
public class PercentUtils {

    public static Integer getPercent(Integer top , Integer bottom){
        if(bottom <= 0 || top <= 0){
            return 0 ;
        }else{
            return Math.round((float) top/bottom *10000);
        }
    }
}
