package com.quxin.freshfun.dao;

import com.quxin.freshfun.api.bean.GoodsPOJO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
     * @return 下单用户id
     */
    List<Long> selectOrderedUsersByGoodsId(@Param("goodsId") Long goodsId,@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询重复购买的用户数
     * @param goodsId 商品Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 复购id
     */
    List<Long> selectRepeatedUsersByGoodsId(@Param("goodsId") Long goodsId, @Param("users") List<Long> users , @Param("startTime") Long startTime,
                                         @Param("endTime") Long endTime);

//    Integer

    /**
     * 根据商品Id查询商品详情
     * @param ids 商品Id
     * @return 返回商品信息
     */
    List<GoodsPOJO> selectGoodsNameById(Long[] ids);

    /**
     * 根据商品id查询前一天的gmv和成交量
     * @param goodsId 商品Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 查询结果
     */
    Map<String,BigDecimal> selectGMVAndVolumeByGoodsId(@Param("goodsId") Long goodsId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 商品id查询类目
     * @param goodsId 商品id
     * @return 类目id
     */
    Map<String, Long> selectCategoryAndCostByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 查询app下面的商品前一天的pv
     * @param goodsId 商品id
     * @param appId appId
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return pv
     */
    Integer selectGoodsPVByGoodsIdAndAppId(@Param("goodsId") Long goodsId, @Param("appId") Long appId ,
                                           @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 根据商品Ids批量查询商品名称
     * @param goodsIds 商品id
     * @return 列表结果
     */
    List<Map<String,Object>> selectGoodsNamesByGoodsIds(@Param("goodsIds") Long[] goodsIds);
}