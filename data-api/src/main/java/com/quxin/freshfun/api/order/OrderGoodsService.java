package com.quxin.freshfun.api.order;

import com.quxin.freshfun.api.bean.goodsOrder.GoodsOrderOutParam;

import java.util.List;

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
    List<GoodsOrderOutParam> findGoodsOrderVot(Long startTime,Long endTime);

    /**
     * 根据商品查询成交量
     * 注解Vot：trading volume （成交量）
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @return 商品数据的输出参数列表
     */
    List<GoodsOrderOutParam> findGoodsOrderTv(Long startTime,Long endTime);
}
