package com.quxin.ocean.utils;

import java.text.DecimalFormat;

/**
 * 数据平台金额格式化
 * Created by qucheng on 17/1/10.
 */
public class MoneyFormatUtils {


    /**
     * 将数据库金额除以100拿出来用浮点型传出,前端需要做计算
     * @return double型的金额
     */
    public static Double getDoubleMoney(Long money){
        DecimalFormat r=new DecimalFormat();
        r.applyPattern("#0.00");//保留小数位数，不足会补零
        return new Double(r.format(money / 100.0));
    }


    /**
     * 将数据库金额除以100拿出来用浮点型传出,前端需要做计算
     * @return double型的金额
     */
    public static Double getDoubleMoney(Double money){
        DecimalFormat r=new DecimalFormat();
        r.applyPattern("#0.00");//保留小数位数，不足会补零
        return new Double(r.format(money));
    }
}
