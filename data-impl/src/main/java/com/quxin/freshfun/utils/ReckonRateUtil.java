package com.quxin.freshfun.utils;

import java.text.DecimalFormat;

/**
 * Created by ziming on 2017/1/6.
 */
public class ReckonRateUtil {
    /**
     * 两数相除，结果保留两位小数
     * @param numerator 分子
     * @param denominator 分母
     * @return 结果
     */
    public static Integer getRate(Integer numerator,Integer denominator){
        Integer result = Math.round((float)numerator/denominator*10000);
        return result;
    }
}
