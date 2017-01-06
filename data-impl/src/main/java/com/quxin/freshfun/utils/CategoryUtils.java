package com.quxin.freshfun.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 二级类目key value
 * Created by qucheng on 17/1/6.
 */
public class CategoryUtils {

    static Map<Integer, String> categorys = Maps.newHashMap();

    static {
        categorys.put(101, "茶饮");
        categorys.put(102, "滋补");
        categorys.put(103, "糖巧");
        categorys.put(104, "速食");
        categorys.put(105, "生活");
        categorys.put(106, "零食");
        categorys.put(107, "酒水");
        categorys.put(108, "生鲜");
    }

    /**
     * 根据categoryId 查询名称
     *
     * @param categoryId categoryId
     * @return 品类名称
     */
    public static String getCategoryNameById(Integer categoryId) {
        if (categoryId != null) {
            return categorys.get(categoryId);
        }
        return null ;
    }

}
