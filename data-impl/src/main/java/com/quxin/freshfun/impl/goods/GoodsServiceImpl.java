package com.quxin.freshfun.impl.goods;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.api.goods.GoodsService;
import com.quxin.freshfun.api.bean.GoodsPOJO;
import com.quxin.freshfun.dao.GoodsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 商品service实现类
 * Created by qucheng on 2016/10/18.
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GoodsMapper goodsMapper;

    public GoodsPOJO queryGoodsByGoodsId(Long goodsId) {
        GoodsPOJO goodsPOJO = new GoodsPOJO();
        goodsPOJO.setGoodsId(goodsId);
        goodsPOJO.setAppId(888l);
//                goodsMapper.selectGoodsByGoodsId(goodsId);
        logger.error(JSON.toJSONString(goodsPOJO));
        return goodsPOJO;
    }

}
