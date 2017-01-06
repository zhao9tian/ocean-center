package com.quxin.freshfun.api.goods;

import java.util.Map;

/**
 * 商品统计数据导入服务
 * Created by qucheng on 17/1/5.
 */
public interface GoodsDataService {


    /**
     * 根据商品Id查询商品指标
     * @param goodsId 商品id
     * @return 商品指标
     */
    Map<String , Object> getGoodsIndicator(Long goodsId);


    /**
     * 根据商品id查询商品前一天成本价
     * @param goodsId 商品id
     * @return 商品成本价
     */
    Integer getGoodsCostByGoodsId(Long goodsId);

    /**
     * 根据商品id和appId查询前一天的pv和uv
     * @param goodsId 商品Id
     * @param appId appId
     * @return pv uv   key :  pv  uv
     */
    Map<String , Object> getPVAndUVByGoodsIdAndAppId(Long goodsId , Long appId);
}
