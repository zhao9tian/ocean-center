package com.quxin.freshfun.impl.app;

import com.quxin.freshfun.api.app.AppTaskService;
import com.quxin.freshfun.api.goods.GoodsDataService;
import com.quxin.freshfun.dao.AppDataMapper;
import com.quxin.freshfun.db.DynamicDataSource;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import com.quxin.freshfun.utils.ReckonRateUtil;
import com.quxin.freshfun.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by ziming on 2017/1/9.
 */
@Service("appTaskService")
public class AppTaskServiceImpl implements AppTaskService{

    @Autowired
    private AppDataMapper appDataMapper;
    @Autowired
    private GoodsDataService goodsDataService;

    @Override
    public void runAppTask(Integer day) {
        //获取当天开始时间作为前一天的结束时间
        Long endDate = TimestampUtils.getStartTimestamp()-86400*(day-1);
        Long startDate = endDate-86400*day;
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        List<Map<String,Object>> orderList = appDataMapper.selectOrderInfo(startDate,endDate);
        for(Map<String,Object> map : orderList){
            //计算平均成交价，统计时间段内商品的成交价总和/统计时间段内商品成交数
            Integer volume = Integer.parseInt(map.get("volume").toString());
            Integer gmv = Integer.parseInt(map.get("gmv").toString());
            Long appId = Long.parseLong(map.get("appId").toString());
            Long goodsId = Long.parseLong(map.get("goodsId").toString());
            //平均成交价
            map.put("avgPrice",gmv/volume);
            //获取pv,uv
            Map<String,Object> pvuv =  goodsDataService.getPVAndUVByGoodsIdAndAppId(goodsId,appId==888888l?0:appId,startDate,endDate);
            Integer pv = Integer.parseInt(pvuv.get("pv").toString());
            map.put("pv",pv);
            Integer uv = Integer.parseInt(pvuv.get("uv").toString());
            map.put("uv",uv);
            //计算转化率
            Integer userCount = Integer.parseInt(map.get("userCount").toString());
            //浏览转化率
            if(uv==0){
                map.put("gmvUv",0);
                map.put("convertRate", 0);
            }else{
                map.put("gmvUv",gmv/uv);
                map.put("convertRate", ReckonRateUtil.getRate(userCount,uv));
            }
            //计算毛利率
            Integer costPrice = goodsDataService.getGoodsCostByGoodsId(goodsId);
            if(gmv-costPrice*volume<0){
                map.put("grossMargin",0);
            }else{
                map.put("grossMargin",ReckonRateUtil.getRate(gmv-costPrice*volume,gmv));
            }
            //保存日期
            map.put("date",TimestampUtils.getStringDateFromLong(System.currentTimeMillis()/1000-86400*day));
        }
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Integer result = appDataMapper.insertAppDataInfo(orderList);
        System.out.println("插入数据行数："+result);
    }
}
