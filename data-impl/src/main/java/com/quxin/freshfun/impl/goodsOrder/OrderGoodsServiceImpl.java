package com.quxin.freshfun.impl.goodsOrder;

import com.quxin.freshfun.api.bean.goodsOrder.GoodsOrderOutParam;
import com.quxin.freshfun.api.order.OrderGoodsService;
import com.quxin.freshfun.dao.GoodsOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ziming on 2016/12/29.
 */
@Service("orderGoodsService")
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private GoodsOrderMapper goodsOrderMapper;
    /**
     * 根据商品查询成交额
     * 注解Vot：volume of transaction（成交额）
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 商品数据的输出参数列表
     */
    @Override
    public List<GoodsOrderOutParam> findGoodsOrderVot(Long startTime, Long endTime) {
        return goodsOrderMapper.queryGoodsVot();
    }

    /**
     * 根据商品查询成交量
     * 注解Tv：trading volume （成交量）
     * @param startTime 开始时间
     * @param endTime   截止时间
     * @return 商品数据的输出参数列表
     */
    @Override
    public List<GoodsOrderOutParam> findGoodsOrderTv(Long startTime, Long endTime) {
        return goodsOrderMapper.queryGoodsTv();
    }
}
