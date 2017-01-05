package com.quxin.freshfun.dao;

import com.quxin.freshfun.api.bean.GoodsPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.Arrays;
import java.util.List;

/**
 * 商品dao层
 */
public interface GoodsMapper {

    /**
     * 商品PV
     * @param goodsId 商品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 商品pv
     */
    Integer selectGoodsPVByGoodsId(@Param("goodsId") Long goodsId,@Param("startTime") Long startTime, @Param("endTime") Long endTime);


    /**
     * 查询下单的用户数
     * @param goodsId 商品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 下单用户数
     */
    Integer selectOrderedUsersByGoodsId(@Param("goodsId") Long goodsId,@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询重复购买的用户数
     * @param goodsId 商品Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 复购人数
     */
    Integer selectRepeatedUsersByGoodsId(@Param("goodsId") Long goodsId,@Param("startTime") Long startTime, @Param("endTime") Long endTime);

//    Integer

    /**
     * 根据商品Id查询商品详情
     * @param ids 商品Id
     * @return 返回商品信息
     */
    List<GoodsPOJO> selectGoodsNameById(Long[] ids);

}