package com.quxin.freshfun.dao;

import com.quxin.freshfun.api.bean.GoodsPOJO;

/**
 * 商品dao层
 */
public interface GoodsMapper {

    /**
     * 根据商品Id查询商品详情
     * @param goodsId 商品Id
     * @return 返回商品信息
     */
    GoodsPOJO selectGoodsByGoodsId(Long goodsId);

}