package com.quxin.freshfun.api.order;

import java.util.List;
import java.util.Map;

/**
 * Created by ziming on 2016/12/29.
 * 专注商品的相关订单数据查询
 */
public interface OrderGoodsService {

    /**
     * 根据商品查询成交额
     * 注解Vot：volume of transaction（成交额）
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @return 商品数据的输出参数列表
     */
    List<Map<String,Object>> findGoodsOrderVot(Long startTime,Long endTime);

    /**
     * 根据商品查询成交量
     * 注解Tv：trading volume （成交量）
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @return 商品数据的输出参数列表
     */
    List<Map<String,Object>> findGoodsOrderTv(Long startTime, Long endTime);

    /**
     * 根据订单创建时间查询总成交量
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交量
     */
    Integer findAllGoodsTv(Long startDate,Long endDate);

    /**
     * 根据订单创建时间查询总成交额
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额
     */
    Long findAllGoodsVot(Long startDate,Long endDate);

    List<Map<String,Object>> selectErpAppInfo();
}
