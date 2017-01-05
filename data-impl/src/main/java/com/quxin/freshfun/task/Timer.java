package com.quxin.freshfun.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 定时器
 * Created by qucheng on 17/1/5.
 */
@Service("timer")
public class Timer {


//    @Scheduled(cron="0/10 * * * * ? ")   //ss : mm : HH
    @Scheduled(cron="0/10 * * * * ? ")   //ss : mm : HH
    public void autoImportGoodsData() {
        System.out.println("timer");
    }



}
