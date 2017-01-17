package com.quxin.ocean.task;

import com.quxin.ocean.service.AppDataService;
import com.quxin.ocean.service.GoodsDataService;
import com.quxin.ocean.service.OrderDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 定时器
 * Created by qucheng on 17/1/5.
 */
@Service("timer")
public class Timer {

    @Autowired
    private GoodsDataService goodsDataService;
    @Autowired
    private AppDataService appDataService;
    @Autowired
    private OrderDataService orderDataService;

//    @Scheduled(cron="0/10 * * * * ? ")   //ss : mm : HH
    @Scheduled(cron="0 0 3 * * ? ")   //ss : mm : HH
    public void autoImportGoodsData() {
        System.out.println(new Date()+"开始跑商品数据");
        goodsDataService.saveGoodsIndicator();
    }

    @Scheduled(cron="0 0 3 * * ? ")
    public void autoImportAppLatitudeData() {
        System.out.println(new Date()+"开始统计公众号&商品数据");
        appDataService.runAppTask(1);
    }

    @Scheduled(cron="0 0 3 * * ? ")
    public void autoImportDateLatitudeData() {
        orderDataService.addOrderLatitude();
    }
}
