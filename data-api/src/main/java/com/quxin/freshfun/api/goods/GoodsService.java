package com.quxin.freshfun.api.goods;

/**
 * 商品service
 * Created by qucheng on 2016/10/18.
 */
public interface GoodsService {


    /**
     * 根据商品ID查询商品UV
     * @param goodsId 商品Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 商品UV
     */
    Integer queryGoodsUVByGoodsId(Long goodsId , Long startTime , Long endTime);

    /**
     * 根据商品Id查询商品pv
     * @param goodsId 商品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 商品pv
     */
    Integer queryGoodsPVGoodsId(Long goodsId , Long startTime , Long endTime);

    /**
     * 商品转化率
     * @param goodsId 商品id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 返回商品转化率
     */
    Double queryGoodsCVRByGoodsId(Long goodsId , Long startTime , Long endTime);

    /**
     * 商品7日复购率
     * @param goodsId 商品Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 7日复购率
     */
    Double querySevenRPRByGoodsId(Long goodsId , Long startTime , Long endTime);

}
