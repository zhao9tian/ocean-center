package com.quxin.freshfun.test;

import com.quxin.freshfun.api.goods.GoodsDataService;
import com.quxin.freshfun.api.goods.GoodsService;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

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
        Long now = System.currentTimeMillis();
          System.out.println(goodsDataService.saveGoodsIndicator());//2071531L
        System.out.println(System.currentTimeMillis() - now);
//        System.out.println(goodsDataService.getGoodsCostByGoodsId(55L));//2071531L
//        System.out.println(goodsDataService.getPVAndUVByGoodsIdAndAppId(2071531L , 900435L));//2071531L
//        Long[] ids = new Long[5];
//        ids[0] = 2L;
//        ids[1] = 56L;
//        ids[2] = 57L;
//        ids[3] = 3L;
//        ids[4] = 59L;
//        System.out.println(goodsDataService.getGoodsNamesByGoodsIds(ids));
    }


    @org.junit.Test
    public void queryGoods() {

//        System.out.println(goodsService.getGoodsGMVTopTen(1483718400L ,1483718400L));
//        System.out.println(goodsService.getGoodsVolumeTopTen(1483632000L ,1483632000L));
        System.out.println(goodsService.getCategoryGmvTopTen(1483718400L ,1483718400L));
//        System.out.println(goodsService.getGoodsGmvTopTenByCategory(108 ,1483632000L ,1483632000L));
//        System.out.println(goodsService.getCategoryVolumeTopTen(1483632000L ,1483632000L));
//        System.out.println(goodsService.getGoodsVolumeTopTenByCategory(106 ,1483632000L ,1483632000L));
    }

    public static void main(String[] args) {

        Integer aa = 56 ;
        Integer bb = 66 ;

//        Integer cc = Math.round((float)aa/bb*10000);
        System.out.println(Math.round((float) 55/2));
//        System.out.println(TimestampUtils.getDateFromTimestamp());
//        Long now = System.currentTimeMillis();
//        Date date = new Date(now);
//        System.out.println(date);

    }


}
