package com.quxin.freshfun.api.app;

import com.quxin.freshfun.api.bean.app.AppOutParam;

import java.util.List;
import java.util.Map;

/**
 * Created by ziming on 2017/1/5.
 */
public interface AppDataService {
    /**
     * 定时任务
     */
    void runAppTask();

    /**
     * 查询统计时间段内某个公众号下的商品成交额分布数据
     * @param appId 公众号id
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 查询结果
     */
    List<AppOutParam> getAppGoodsVot(Long appId, Long startDate, Long endDate);
}
