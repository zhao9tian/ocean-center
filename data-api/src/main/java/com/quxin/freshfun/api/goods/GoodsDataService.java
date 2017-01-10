package com.quxin.freshfun.api.goods;

import java.util.List;
import java.util.Map;

/**
 * 商品统计数据导入服务
 * Created by qucheng on 17/1/5.
 */
public interface GoodsDataService {


    /**
     * 录入商品指标
     * @return 商品指标
     */
    Boolean saveGoodsIndicator();


    /**
     * 根据商品id查询商品前一天成本价
     * @param goodsId 商品id
     * @return 商品成本价
     */
    Integer getGoodsCostByGoodsId(Long goodsId);

    /**
     * 根据商品id和appId查询前一天的pv和uv
     * @return 结果
     */
    List<Map<String , Object>> getPVAndUVByGoodsIdAndAppId(Long start,Long end);

    /**
     * 根据商品Id查询商品名称
     * @param goodsIds 商品id
     * @return 列表 map的key为商品id , value为 商品name
     */
    Map<Long, Object> getGoodsNamesByGoodsIds(Long[] goodsIds);


}
