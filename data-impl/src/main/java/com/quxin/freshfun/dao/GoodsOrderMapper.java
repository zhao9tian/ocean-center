package com.quxin.freshfun.dao;

import com.quxin.freshfun.api.bean.goodsOrder.GoodsOrderOutParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ziming on 2016/12/29.
 * 专注商品相关的订单查询
 */
public interface GoodsOrderMapper {

    /**
     * 根据订单创建时间查询成交量
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 查询结果
     */
    List<GoodsOrderOutParam> selectGoodsTv(@Param("startDate")Long startDate,@Param("endDate")Long endDate);

    /**
     * 根据订单创建时间查询成交额
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 查询结果
     */
    List<GoodsOrderOutParam> selectGoodsVot(@Param("startDate")Long startDate,@Param("endDate")Long endDate);

    /**
     * 根据订单创建时间查询总成交量
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交量
     */
    Integer selectAllGoodsTv(@Param("startDate")Long startDate,@Param("endDate")Long endDate);

    /**
     * 根据订单创建时间查询总成交额
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额
     */
    Long selectAllGoodsVot(@Param("startDate")Long startDate,@Param("endDate")Long endDate);

    List<Map<String,Object>> selectErpAppInfo();
}
