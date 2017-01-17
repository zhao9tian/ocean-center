package com.quxin.ocean.api.app;

/**
 * Created by ziming on 2017/1/9.
 */
public interface AppTaskService {
    /**
     * 定时任务
     */
    void runAppTask(Integer day);
}
