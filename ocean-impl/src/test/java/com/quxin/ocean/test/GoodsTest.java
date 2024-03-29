package com.quxin.ocean.test;

import com.quxin.ocean.api.goods.GoodsService;
import com.quxin.ocean.service.GoodsDataService;
import com.quxin.ocean.utils.TimestampUtils;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品单元测试
 * Created by qucheng on 2016/10/18.
 */
public class GoodsTest extends TestBase {
    @Resource
    private GoodsService goodsService;

    @Autowired
    private GoodsDataService goodsDataService;

    Logger logger = LoggerFactory.getLogger(GoodsTest.class);

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

//        Long now = System.currentTimeMillis();
//        System.out.println(now);
          System.out.println(goodsDataService.saveGoodsIndicator());//2071531L
//        System.out.println(System.currentTimeMillis() - now);
    }


    @org.junit.Test
    public void queryGoods() {
//            logger.error("hah");
        System.out.println(goodsService.getIndicatorsForHistogram(1483718400L ,1483804799L));
//        System.out.println(goodsService.getGoodsGMVTopTen(1483718400L ,1483804799L));
//        System.out.println(goodsService.getGoodsVolumeTopTen(1483718400L ,1483804799L));
//        System.out.println(goodsService.getCategoryGmvTopTen(1483718400L ,1483804799L));
//        System.out.println(goodsService.getGoodsGmvTopTenByCategory(108 ,1483718400L ,1483804799L));
//        System.out.println(goodsService.getCategoryVolumeTopTen(1483718400L ,1483804799L));
//        System.out.println(goodsService.getGoodsVolumeTopTenByCategory(106 ,1483718400L ,1483804799L));
        List<Long> ids = new ArrayList<Long>();
//        ids.add(55L);
//        ids.add(52L);
        ids.add(2071659L);
//        System.out.println(goodsService.getGoodsIndicator(ids ,1483622000L ,1483891199L ));
//        System.out.println(goodsDataService.getPVAndUVByGoodsIdAndAppId(1484891200L ,1484977600L));
    }

    public static void main(String[] args) {

        System.out.println(TimestampUtils.getDateFromTimestamp());
    }


}
