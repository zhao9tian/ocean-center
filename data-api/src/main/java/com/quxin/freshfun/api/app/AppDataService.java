package com.quxin.freshfun.api.app;

import com.quxin.freshfun.api.bean.app.AppOutParam;

import java.util.List;
import java.util.Map;

/**
 * Created by ziming on 2017/1/5.
 */
public interface AppDataService {
    /**
     * 定时任务
     */
    void runAppTask();

    /**
     * 查询统计时间段内某个公众号下的商品成交额分布数据
     * @param appId 公众号id
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<Map<String, Object>> getAppGoodsVot(Long appId, Long startDate, Long endDate);

    /**
     * 查询统计时间段内某个公众号下的商品成交量分布数据
     * @param appId 公众号id
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<Map<String, Object>> getAppGoodsTv(Long appId, Long startDate, Long endDate);

    /**
     * 查询统计时间段内某个商品各个公众号下成交额分布数据
     * @param goodsId 商品id
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<Map<String, Object>> getGoodsAppVot(Long goodsId, Long startDate, Long endDate);

    /**
     * 查询统计时间段内某个商品各个公众号下成交量分布数据
     * @param goodsId 商品id
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<Map<String, Object>> getGoodsAppTv(Long goodsId, Long startDate, Long endDate);

    /**
     * 查询统计时间段内公众号成交额分布数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<Map<String, Object>> getAppVot(Long startDate, Long endDate);

    /**
     * 查询统计时间段内公众号成交量分布数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<Map<String, Object>> getAppTv(Long startDate, Long endDate);

    /**
     * 查询一段时间内公众号下商品的数据
     * @param appId 公众号id
     * @param goodsIds 商品id，数组
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 商品的数据
     */
    List<Map<String,Object>> getAppGoodsInfo(Long appId,Long[] goodsIds,Long startDate, Long endDate);
}
