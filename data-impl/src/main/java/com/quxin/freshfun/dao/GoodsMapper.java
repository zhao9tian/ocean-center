package com.quxin.freshfun.dao;

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
     *
     * @param goodsId   商品id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商品pv
     */
    Integer selectGoodsPVByGoodsId(@Param("goodsId") Long goodsId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询总pv
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 总pv
     */
    Integer selectTotalPV(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询下单的用户数
     *
     * @param goodsId   商品id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 下单用户id
     */
    List<Long> selectOrderedUsersByGoodsId(@Param("goodsId") Long goodsId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询重复购买的用户数
     *
     * @param goodsId   商品Id
     * @param users     用户
     * @param endTime   结束时间
     * @return 复购id
     */
    List<Long> selectRepeatedUsersByGoodsId(@Param("goodsId") Long goodsId, @Param("users") List<Long> users, @Param("endTime") Long endTime);

    /**
     * 查询复购用户信息
     * @param goodsId 商品Id
     * @param users 复购用户id
     * @param endTime 结束时间
     * @return 用户信息
     */
    List<Map<String,Object>> selectUserInfoByGoodsId(@Param("goodsId") Long goodsId, @Param("users") List<Long> users, @Param("endTime") Long endTime);


    /**
     * 根据商品id查询前一天的gmv和成交量
     *
     * @param goodsId   商品Id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 查询结果
     */
    Map<String, BigDecimal> selectGMVAndVolumeByGoodsId(@Param("goodsId") Long goodsId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 商品id查询类目
     *
     * @param goodsId 商品id
     * @return 类目id
     */
    Map<String, Long> selectCategoryAndCostByGoodsId(@Param("goodsId") Long goodsId);


    /**
     * 根据商品Ids批量查询商品名称
     *
     * @param goodsIds 商品id
     * @return 列表结果
     */
    List<Map<String, Object>> selectGoodsNamesByGoodsIds(@Param("goodsIds") Long[] goodsIds);

    /**
     * 插入商品数据指标
     *
     * @param goodsData 商品指标
     * @return 插入记录数
     */
    Integer insertGoodsData(Map<String, Object> goodsData);

    /**
     * 查询所有商品Id
     *
     * @return goodsIds
     */
    List<Long> selectAllGoodsIds();

    /**
     * 所有商品的gmv总和
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 总和
     */
    Long selectSumGmv(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 前十的商品的id和gmv
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 前十的商品的id和gmv
     */
    List<Map<String, Object>> selectGmvTopTenGoods(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 所有商品的volume总和
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return volume总和
     */
    Long selectSumVolume(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 前十的商品的id和volume
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 前十的商品的id和volume
     */
    List<Map<String, Object>> selectVolumeTopTenGoods(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前8个品类的gmv排名和占比
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 品类的排名占比
     */
    List<Map<String, Object>> selectGmvTopTenCategory(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询类目下gmv前十的商品信息
     *
     * @param category  类目
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return gmv前十的商品信息
     */
    List<Map<String, Object>> selectGmvTopTenGoodsByCategory(@Param("categoryId") Integer category,
                                                             @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 类目下所有商品的gmv总和
     *
     * @param category  类目
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 总和
     */
    Long selectSumGmvByCategory(@Param("categoryId") Integer category,
                                @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询前8个品类的volume排名和占比
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 品类的排名占比
     */
    List<Map<String, Object>> selectVolumeTopTenCategory(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询类目下volume前十的商品信息
     *
     * @param category  类目
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return volume前十的商品信息
     */
    List<Map<String, Object>> selectVolumeTopTenGoodsByCategory(@Param("categoryId") Integer category,
                                                                @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 类目下所有商品的volume总和
     *
     * @param category  类目
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 总和
     */
    Long selectSumVolumeByCategory(@Param("categoryId") Integer category,
                                   @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 商品ids
     *
     * @param goodsIds  商品
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回结果
     */
    List<Map<String, Object>> selectGoodsIndicator(@Param("goodsIds") List<Long> goodsIds, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询一段时间内所有公众号下,所有商品的pv
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 列表
     */
    List<Map<String, Object>> selectPvAndUv(@Param("startTime") Long start, @Param("endTime") Long end);


}