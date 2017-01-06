package com.quxin.freshfun.impl.app;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.api.app.AppDataService;
import com.quxin.freshfun.api.bean.app.AppOutParam;
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
 * Created by ziming on 2017/1/5.
 */
@Service("appDataService")
public class AppDataServiceImpl implements AppDataService{

    @Autowired
    private AppDataMapper appDataMapper;
    @Autowired
    private GoodsDataService goodsDataService;

    @Override
    public void runAppTask() {
        Long endDate = TimestampUtils.getStartTimestamp();
        Long startDate = endDate-86400;
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        List<Map<String,Object>> orderList = appDataMapper.selectOrderInfo(startDate,endDate);
        System.out.println(JSON.toJSONString(orderList));
        for(Map<String,Object> map : orderList){
            //计算平均成交价，统计时间段内商品的成交价总和/统计时间段内商品成交数
            Integer volume = Integer.parseInt(map.get("volume").toString());
            Integer gmv = Integer.parseInt(map.get("gmv").toString());
            Long appId = Long.parseLong(map.get("appId").toString());
            Long goodsId = Long.parseLong(map.get("goodsId").toString());
            //平均成交价
            map.put("avgPrice",gmv/volume);
            //获取pv,uv
            Map<String,Object> pvuv =  goodsDataService.getPVAndUVByGoodsIdAndAppId(goodsId,appId);
            Integer pv = Integer.parseInt(pvuv.get("pv").toString());
            map.put("pv",pv);
            Integer uv = Integer.parseInt(pvuv.get("uv").toString());
            map.put("uv",uv);
            //浏览转化率
            map.put("gmvUv",ReckonRateUtil.getRate(gmv,uv));
            //计算转化率
            Integer userCount = Integer.parseInt(map.get("userCount").toString());
            map.put("convertRate", ReckonRateUtil.getRate(userCount,uv));
            //计算毛利率
            Integer costPrice = goodsDataService.getGoodsCostByGoodsId(goodsId);
            map.put("grossMargin",ReckonRateUtil.getRate(gmv-costPrice*volume,gmv));
            //保存日期
            map.put("date",TimestampUtils.getStringDateFromLong(System.currentTimeMillis()/1000));
        }
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Integer result = appDataMapper.insertAppDataInfo(orderList);
        System.out.println("插入数据行数："+result);
    }

    /**
     * 查询统计时间段内某个公众号下的商品成交额分布数据
     * 实现方法
     * @param appId     公众号id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询结果
     */
    @Override
    public List<AppOutParam> getAppGoodsVot(Long appId, Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        List<AppOutParam> list = appDataMapper.selectAppGoodsVot(appId, start,end);
        list = setGoodsName(list);
        if(list!=null&&list.size()==10){
            Integer vot = appDataMapper.selectAllAppGoodsVot(appId, start, end);
            Integer topTen = 0;
            for(int i=0;i<list.size();i++){
                topTen+=list.get(i).getGmv();
            }
            Integer elseMoney = vot-topTen;
            if(elseMoney>0){
                AppOutParam appOutParam = new AppOutParam();
                appOutParam.setGoodsName("其他");
                appOutParam.setGmv(elseMoney);
                list.add(appOutParam);
            }
        }
        return list;
    }

    /**
     * 设置商品名称
     */
    private List<AppOutParam> setGoodsName(List<AppOutParam> list){
        Long[] ids = new Long[list.size()];
        for(int i=0;i<list.size();i++){
            ids[i] = list.get(i).getGoodsId();
            list.get(i).setGoodsName(list.get(i).getGoodsId().toString());
        }
        return list;
    }
}
