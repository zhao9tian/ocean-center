package com.quxin.freshfun.dao;

import com.quxin.freshfun.api.bean.order.OrderDataPOJO;
import com.quxin.freshfun.api.bean.order.TimeDataPOJO;

import java.util.List;
import java.util.Map;

/**
 * Created by fanyanlin on 2017/1/4.
 */
public interface OrderMapper {

    OrderDataPOJO selectOrderData(Map<String,Object> map);

    /**
     * 七日复购率
     * @param map 参数
     * @return 七天用户数
     */
    List<OrderDataPOJO> selectSevenRpr(Map<String,Object> map);

    /**
     * 查询复购用户
     * @param list 参数集合
     * @return 复购用户
     */
    List<OrderDataPOJO> selectRePurchaseUser(List<OrderDataPOJO> list);

    /**
     * 添加数据纬度
     * @param timeData 数据
     * @return 受影响行数
     */
    int insertTimeLatitude(TimeDataPOJO timeData);

    /**
     * 查询订单统计数据
     * @param map 参数
     * @return 数据结果
     */
    List<TimeDataPOJO> selectOrderLatitude(Map<String ,Object> map);
}
