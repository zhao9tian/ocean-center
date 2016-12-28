package com.quxin.freshfun.api;

import com.quxin.freshfun.api.bean.GoodsPOJO;

/**
 * 商品service
 * Created by qucheng on 2016/10/18.
 */
public interface GoodsService {


    /**
     * 根据商品Id查询商品信息
     * @param goodsId 商品Id
     * @return 商品所有信息
     */
    GoodsPOJO queryGoodsByGoodsId(Long goodsId);

}
