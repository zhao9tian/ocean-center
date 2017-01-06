package com.quxin.freshfun.test;

import com.quxin.freshfun.api.goods.GoodsDataService;
import com.quxin.freshfun.api.goods.GoodsService;
import com.quxin.freshfun.utils.TimestampUtils;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * 商品单元测试
 * Created by qucheng on 2016/10/18.
 */
public class GoodsTest extends TestBase {
    @Resource
    private GoodsService goodsService;

    @Autowired
    private GoodsDataService goodsDataService;

    @Before
    public void setUp() throws Exception {
        goodsService = getContext().getBean("goodsService", GoodsService.class);
        goodsDataService = getContext().getBean("goodsDataService", GoodsDataService.class);
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
//        System.out.println(goodsService.querySevenRPRByGoodsId(2071531L, 0L, 111111111110L));
//        goodsDataService.getGoodsIndicator(2071531L);//2071531L
//        System.out.println(goodsDataService.getGoodsCostByGoodsId(55L));//2071531L
        System.out.println(goodsDataService.getPVAndUVByGoodsIdAndAppId(2071531L , 900435L));//2071531L
    }

    public static void main(String[] args) {

//        System.out.println(TimestampUtils.getStartTimestamp());
        Integer aa = 56 ;
        Integer bb = 66 ;

        Integer cc = Math.round((float)aa/bb*10000);
        System.out.println(cc);
    }


}
