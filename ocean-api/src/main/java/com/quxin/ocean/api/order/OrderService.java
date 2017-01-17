package com.quxin.ocean.api.order;

import com.quxin.ocean.api.bean.order.TimeDataPOJO;

import java.util.List;

/**
 * Created by fanyanlin on 2016/12/29.
 */
public interface OrderService {

    /**
     * 查询指定日期订单纬度数据
     * @return 结果数据
     */
    List<TimeDataPOJO> queryOrderLatitude(Long startTime, Long endTime);
}
