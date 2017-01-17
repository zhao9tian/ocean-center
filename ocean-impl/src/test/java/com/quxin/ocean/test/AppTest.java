package com.quxin.ocean.test;

import com.quxin.ocean.service.AppDataService;
import com.quxin.ocean.api.app.AppService;
import com.quxin.ocean.api.goods.GoodsService;
import com.quxin.ocean.utils.ReckonRateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by ASus on 2017/1/5.
 */
public class AppTest extends TestBase {
    @Resource
    private AppDataService appDataService;
    @Resource
    private GoodsService goodsService;

    @Before
    public void setUp() throws Exception {
        appDataService = getContext().getBean("appDataService",AppDataService.class);
        goodsService = getContext().getBean("goodsService",GoodsService.class);
    }


    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test(){
        for(int i=1;i<46;i++){
            appDataService.runAppTask(i);
        }
    }

    @Test
    public void test1(){
        System.out.println("***********************");
        //System.out.println(JSON.toJSONString(appDataService.getAppGoodsTv(888888l,1483545600,1484546600l)));
        //System.out.println(JSON.toJSONString(appDataService.getAppVot(1483459200l,1484546600l)));
        //Long[] ids = new Long[2];
        //ids[0] = 2071531l;
        //ids[1] = 2071514l;
        //System.out.println(JSON.toJSONString(appDataService.getAppGoodsInfo(888888l,ids,1483200000l,1483545600l)));
        Long[] goodsIds = new Long[1];
        goodsIds[0] = 2071531l;
        Map<Long,Object> mapName = goodsService.getGoodsNamesByGoodsIds(goodsIds);
        if(mapName==null||mapName.get(goodsIds[0])==null){
            System.out.println("*********1111111**************");
        }
        System.out.println("***********************");
    }



    public static void main(String[] args) {
        Integer a = 3;
        Integer b = 9;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        System.out.println(df.format((float)a*100/b));
        Integer result = ReckonRateUtil.getRate(a,b);
        System.out.println(result);

        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        Long time=1483459200l;
        String c = format.format(time*1000);
        String d = format.format(System.currentTimeMillis());
        System.out.println(c);
        System.out.println(d);
    }
}
