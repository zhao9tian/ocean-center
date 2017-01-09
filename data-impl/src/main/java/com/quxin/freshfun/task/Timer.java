package com.quxin.freshfun.task;

import com.quxin.freshfun.api.app.AppTaskService;
import com.quxin.freshfun.api.goods.GoodsDataService;
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
    private AppTaskService appTaskService;

//    @Scheduled(cron="0/10 * * * * ? ")   //ss : mm : HH
    @Scheduled(cron="0 0 3 * * ? ")   //ss : mm : HH
    public void autoImportGoodsData() {
        System.out.println(new Date()+"开始跑商品数据");
        goodsDataService.saveGoodsIndicator();
        System.out.println(new Date()+"开始统计公众号&商品数据");
        appTaskService.runAppTask(1);
    }



}
