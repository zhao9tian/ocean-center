package com.quxin.freshfun.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fanyanlin on 2017/1/4.
 */
public class DateUtils {

    public static List<String> getDatesBetweenTwoDate(Long beginTime, Long endTime) {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date(beginTime*1000);
        Date endDate = new Date(endTime*1000);
        lDate.add(format.format(beginDate));//把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        while (true) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(format.format(cal.getTime()));
            } else {
                break;
            }
        }
        lDate.add(format.format(endDate));//把结束时间加入集合
        return lDate;
    }
}
