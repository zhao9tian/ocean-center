package com.quxin.freshfun.impl.goods;

import com.quxin.freshfun.api.bean.GoodsPOJO;
import com.quxin.freshfun.api.goods.GoodsService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 商品service实现类
 * Created by qucheng on 2016/10/18.
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private GoodsMapper goodsMapper;

    public GoodsPOJO queryGoodsByGoodsId(Long goodsId) {
        GoodsPOJO goodsPOJO =  goodsMapper.selectGoodsByGoodsId(goodsId);
        DynamicDataSourceHolder.setDataSource("ptpDataSource");
//        System.out.println(count);
        return goodsPOJO;
    }

}
