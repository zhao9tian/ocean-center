package com.quxin.freshfun.api.goods;

import com.quxin.freshfun.api.bean.GoodsPOJO;

import java.util.List;
import java.util.Map;

/**
 * 商品service
 * Created by qucheng on 2016/10/18.
 */
public interface GoodsService {



    /**
     * 查询一段时间内gmv前十的商品
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return gmv前十商品数组
     */
    List<Map<String , Object>> getGoodsGMVTopTen(Long startTime , Long endTime);


    /**
     * 查询一段时间内销量前十的商品
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return volume前十商品数组
     */
    List<Map<String , Object>> getGoodsVolumeTopTen(Long startTime , Long endTime);


    /**
     * 查询一段时间内gmv前十的品类
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 前十品类数组
     */
    List<Map<String , Object>> getCategoryGmvTopTen(Long startTime , Long endTime);

    /**
     * 查询某一个品类下一段时间内前十的商品
     * @param category 类目
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 前十品类数组
     */
    List<Map<String , Object>> getGoodsGmvTopTenByCategory(Integer category , Long startTime , Long endTime);

    /**
     * 查询一段时间内销量前十的品类
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 前十品类数组
     */
    List<Map<String , Object>> getCategoryVolumeTopTen(Long startTime , Long endTime);

    /**
     * 查询某一个品类下一段时间内销量前十的商品
     * @param category 类目
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 前十品类数组
     */
    List<Map<String , Object>> getGoodsVolumeTopTenByCategory(Integer category , Long startTime , Long endTime);

    /**
     * 查询一段时间内的商品指标
     * @param goodsIds 商品ids
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 商品指标
     */
    Map<String , Object> getGoodsIndicator(List<Long> goodsIds , Long startTime , Long endTime);


    /**
     * 查询一段时间内的商品指标
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 商品指标
     */
    Map<String , Object> getIndicatorsForHistogram(Long startTime , Long endTime);

    /**
     * 根据商品Id查询商品名称
     * @param goodsIds 商品id
     * @return 列表 map的key为商品id , value为 商品name
     */
    Map<Long, Object> getGoodsNamesByGoodsIds(Long[] goodsIds);

}
