package com.quxin.freshfun.dao;

import com.quxin.freshfun.api.bean.goodsOrder.GoodsOrderOutParam;

import java.util.List;

/**
 * Created by ziming on 2016/12/29.
 * 专注商品相关的订单查询
 */
public interface GoodsOrderMapper {

    List<GoodsOrderOutParam> queryGoodsTv();

    List<GoodsOrderOutParam> queryGoodsVot();
}
