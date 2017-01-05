package com.quxin.freshfun.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quxin.freshfun.api.bean.goodsOrder.GoodsOrderOutParam;
import com.quxin.freshfun.api.order.OrderGoodsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by Ziming on 2017/1/3.
 */
public class GoodsOrderTest extends TestBase {

    private OrderGoodsService orderGoodsService;

    @Before
    public void setUp() throws Exception {
        orderGoodsService = getContext().getBean("orderGoodsService",OrderGoodsService.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test(){
        List<Map<String,Object>> goList = orderGoodsService.findGoodsOrderTv(1475340896l,1481159084l);
        System.out.print(JSON.toJSONString(goList));
    }

    @Test
    public void test1(){
        List<Map<String,Object>> goList = orderGoodsService.selectErpAppInfo();
        System.out.print(JSON.toJSONString(goList));
    }
}
