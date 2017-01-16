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

import java.util.HashMap;
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

    private  Integer getHalf(Integer num){
        if(num<=0){
            return 0;
        }else{
            if(num>2){
                if(num%2==0){
                    return num/2;
                }else{
                    return num/2+1;
                }
            }else{
                return 1;
            }
        }
    }

    @Override
    public void runAppTask(Integer day) {
        //获取当天开始时间作为前一天的结束时间
        Long endDate = TimestampUtils.getStartTimestamp()-86400*(day-1);
        Long startDate = endDate-86400;
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        List<Map<String,Object>> orderList = appDataMapper.selectOrderInfo(startDate,endDate);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        for(Map<String,Object> map : orderList){
            resultMap.put(map.get("appId").toString()+map.get("goodsId"),map);
        }
        List<Map<String,Object>> pvuv =  goodsDataService.getPVAndUVByGoodsIdAndAppId(startDate,endDate);
        for(Map<String,Object> map : pvuv){
            String key = (Integer.parseInt(map.get("appId").toString())==0?"888888":map.get("appId").toString())+map.get("goodsId").toString();
            if(resultMap.get(key)!=null){
                Map<String,Object> mapOrder = (Map<String,Object>)resultMap.get(key);
                mapOrder.put("pv",map.get("pv"));
                mapOrder.put("uv",getHalf(Integer.parseInt(map.get("pv").toString())));
            }else{
                Map<String,Object> newMap = new HashMap<>();
                newMap.put("volume",0);
                newMap.put("gmv",0);
                newMap.put("pv",map.get("pv"));
                newMap.put("uv",getHalf(Integer.parseInt(map.get("pv").toString())));
                newMap.put("appId",Integer.parseInt(map.get("appId").toString())==0?888888:map.get("appId"));
                newMap.put("goodsId",map.get("goodsId"));
                newMap.put("userCount",0);
                orderList.add(newMap);
            }
        }
        for(Map<String,Object> map : orderList){
            //计算平均成交价，统计时间段内商品的成交价总和/统计时间段内商品成交数
            Integer volume = Integer.parseInt(map.get("volume").toString());
            Integer gmv = Integer.parseInt(map.get("gmv").toString());
            Long appId = Long.parseLong(map.get("appId").toString());
            Long goodsId = Long.parseLong(map.get("goodsId").toString());
            String gap = appId.toString()+goodsId.toString();
            //平均成交价
            if(volume==0){
                map.put("avgPrice",0);
            }else{
                map.put("avgPrice",gmv/volume);
            }
            Integer pv = 0;
            Integer uv = 0;
            //获取pv,uv
            Map<String,Object> mapOrder = (Map<String,Object>)resultMap.get(gap);
            if(gmv!=0&&mapOrder!=null&&mapOrder.get("pv")!=null) {
                pv = Integer.parseInt(mapOrder.get("pv").toString());
                uv = Integer.parseInt(mapOrder.get("uv").toString());
            }else if(map.get("pv")!=null){
                pv = Integer.parseInt(map.get("pv").toString());
                uv = Integer.parseInt(map.get("uv").toString());
            }
            map.put("pv", pv);
            map.put("uv", uv);
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
            Integer costPrice = 0;
            if(goodsId!=0) {
                costPrice = goodsDataService.getGoodsCostByGoodsId(goodsId);
            }
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
