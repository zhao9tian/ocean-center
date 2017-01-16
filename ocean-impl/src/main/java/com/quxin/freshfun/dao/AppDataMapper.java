package com.quxin.freshfun.dao;

import com.quxin.freshfun.api.bean.app.AppOutParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ziming on 2017/1/5.
 * 数据统计mapper
 */
public interface AppDataMapper {
    /**
     * 获取前一天的订单信息
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 订单信息
     */
    List<Map<String,Object>> selectOrderInfo(@Param("startDate")Long startDate,@Param("endDate")Long endDate);

    /**
     * 根据appId数组查询对应appName
     * @param ids 公众号id
     * @return appName
     */
    List<Map<String,Object>> selectAppNamesByIds(Long[] ids);

    /**
     * 批量插入公众号数据信息
     * @param appData 数据信息集合
     * @return 受影响行数
     */
    Integer insertAppDataInfo(List<Map<String,Object>> appData);

    /**
     * 查询公众号一段时间内的各个商品总成交额
     * @param appId 公众号id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 成交额分布信息
     */
    List<AppOutParam> selectAppGoodsVot(@Param("appId")Long appId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询公众号一段时间内的总成交额
     * @param appId 公众号id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额
     */
    Integer selectAllAppGoodsVot(@Param("appId")Long appId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询公众号一段时间内的各个商品总成交量
     * @param appId 公众号id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额分布信息
     */
    List<AppOutParam> selectAppGoodsTv(@Param("appId")Long appId, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 查询公众号一段时间内的总成交量
     * @param appId 公众号id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交量
     */
    Integer selectAllAppGoodsTv(@Param("appId")Long appId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询商品一段时间内的各个公众号总成交额
     * @param goodsId 商品id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 成交额分布信息
     */
    List<AppOutParam> selectGoodsAppVot(@Param("goodsId")Long goodsId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询商品一段时间内的总成交额
     * @param goodsId 商品id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额
     */
    Integer selectAllGoodsAppVot(@Param("goodsId")Long goodsId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询商品一段时间内的各个公众号总成交量
     * @param goodsId 商品id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额分布信息
     */
    List<AppOutParam> selectGoodsAppTv(@Param("goodsId")Long goodsId, @Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 查询商品一段时间内的总成交量
     * @param goodsId 商品id
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交量
     */
    Integer selectAllGoodsAppTv(@Param("goodsId")Long goodsId,@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询一段时间内的各个公众号总成交额
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 成交额分布信息
     */
    List<AppOutParam> selectAppVot(@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询一段时间内公众号的总成交额
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额
     */
    Integer selectAllAppVot(@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询一段时间内的各个公众号总成交量
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交额分布信息
     */
    List<AppOutParam> selectAppTv(@Param("startDate")String startDate, @Param("endDate")String endDate);

    /**
     * 查询一段时间内公众号的总成交量
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 总成交量
     */
    Integer selectAllAppTv(@Param("startDate")String startDate,@Param("endDate")String endDate);

    /**
     * 查询一段时间内公众号下商品的数据
     * @param appId 公众号id
     * @param goodsIds 商品id，数组
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 商品的数据
     */
    List<AppOutParam> selectAppGoodsInfo(@Param("appId")Long appId,@Param("goodsIds")Long[] goodsIds,@Param("startDate")String startDate,@Param("endDate")String endDate);

}
