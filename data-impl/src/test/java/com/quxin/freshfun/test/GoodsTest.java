package com.quxin.freshfun.test;

import com.quxin.freshfun.api.goods.GoodsService;
import org.junit.After;
import org.junit.Before;

import javax.annotation.Resource;

/**
 * 商品单元测试
 * Created by qucheng on 2016/10/18.
 */
public class GoodsTest extends TestBase {
    @Resource
    private GoodsService goodsService;

    @Before
    public void setUp() throws Exception {
        goodsService = getContext().getBean("goodsService", GoodsService.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }


    @org.junit.Test
    public void queryGoodsById() {

//        System.out.println(goodsService.queryGoodsPVGoodsId(55L, 0L, 111111111110L));
//        System.out.println(goodsService.queryGoodsUVByGoodsId(55L, 0L, 111111111110L));
//        System.out.println(goodsService.queryGoodsCVRByGoodsId(2071531L, 0L, 111111111110L));
        System.out.println(goodsService.querySevenRPRByGoodsId(2071531L, 0L, 111111111110L));
    }


}
