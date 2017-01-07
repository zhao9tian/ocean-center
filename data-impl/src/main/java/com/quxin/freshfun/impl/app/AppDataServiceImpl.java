package com.quxin.freshfun.impl.app;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
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

import java.util.ArrayList;
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
        //获取当天开始时间作为前一天的结束时间
        Long endDate = TimestampUtils.getStartTimestamp();
        Long startDate = endDate-86400;
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
            Map<String,Object> pvuv =  goodsDataService.getPVAndUVByGoodsIdAndAppId(goodsId,appId==888888l?0:appId);
            Integer pv = Integer.parseInt(pvuv.get("pv").toString());
            map.put("pv",pv);
            Integer uv = Integer.parseInt(pvuv.get("uv").toString());
            map.put("uv",uv);
            //浏览转化率
            if(uv==0){
                map.put("gmvUv",0);
            }else{
                map.put("gmvUv",gmv/uv);
            }
            //计算转化率
            Integer userCount = Integer.parseInt(map.get("userCount").toString());
            map.put("convertRate", ReckonRateUtil.getRate(userCount,uv));
            //计算毛利率
            Integer costPrice = goodsDataService.getGoodsCostByGoodsId(goodsId);
            map.put("grossMargin",ReckonRateUtil.getRate(gmv-costPrice*volume,gmv));
            //保存日期
            map.put("date",TimestampUtils.getStringDateFromLong(System.currentTimeMillis()/1000-86400));
        }
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Integer result = appDataMapper.insertAppDataInfo(orderList);
        System.out.println("插入数据行数："+result);
    }

    /**
     * 查询统计时间段内某个公众号下的商品成交额分布数据
     * @param appId     公众号id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询结果
     */
    @Override
    public List<Map<String, Object>> getAppGoodsVot(Long appId, Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<AppOutParam> list = appDataMapper.selectAppGoodsVot(appId, start,end);
        list = setGoodsName(list);
        if(list!=null&&list.size()==10){
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
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
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(AppOutParam appOutParam : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gmv",appOutParam.getGmv());
            map.put("goodsName",appOutParam.getGoodsName());
            result.add(map);
        }
        return result;
    }

    /**
     * 查询统计时间段内某个公众号下的商品成交量分布数据
     * @param appId     公众号id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询结果
     */
    @Override
    public List<Map<String, Object>> getAppGoodsTv(Long appId, Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<AppOutParam> list = appDataMapper.selectAppGoodsTv(appId, start,end);
        list = setGoodsName(list);
        if(list!=null&&list.size()==10){
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            Integer tv = appDataMapper.selectAllAppGoodsTv(appId, start, end);
            Integer topTen = 0;
            for(int i=0;i<list.size();i++){
                topTen+=list.get(i).getVolume();
            }
            Integer elseVolume = tv-topTen;
            if(elseVolume>0){
                AppOutParam appOutParam = new AppOutParam();
                appOutParam.setGoodsName("其他");
                appOutParam.setVolume(elseVolume);
                list.add(appOutParam);
            }
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(AppOutParam appOutParam : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("volume",appOutParam.getVolume());
            map.put("goodsName",appOutParam.getGoodsName());
            result.add(map);
        }
        return result;
    }

    /**
     * 查询统计时间段内某个商品各个公众号下成交额分布数据
     * @param goodsId   商品id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询结果
     */
    @Override
    public List<Map<String, Object>> getGoodsAppVot(Long goodsId, Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<AppOutParam> list = appDataMapper.selectGoodsAppVot(goodsId, start,end);
        list = setAppName(list);
        if(list!=null&&list.size()==10){
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            Integer vot = appDataMapper.selectAllGoodsAppVot(goodsId, start, end);
            Integer topTen = 0;
            for(int i=0;i<list.size();i++){
                topTen+=list.get(i).getGmv();
            }
            Integer elseMoney = vot-topTen;
            if(elseMoney>0){
                AppOutParam appOutParam = new AppOutParam();
                appOutParam.setAppName("其他");
                appOutParam.setGmv(elseMoney);
                list.add(appOutParam);
            }
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(AppOutParam appOutParam : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gmv",appOutParam.getGmv());
            map.put("appName",appOutParam.getAppName());
            result.add(map);
        }
        return result;
    }

    /**
     * 查询统计时间段内某个商品各个公众号下成交量分布数据
     * @param goodsId   商品id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询结果
     */
    @Override
    public List<Map<String, Object>> getGoodsAppTv(Long goodsId, Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<AppOutParam> list = appDataMapper.selectGoodsAppTv(goodsId, start,end);
        list = setAppName(list);
        if(list!=null&&list.size()==10){
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            Integer tv = appDataMapper.selectAllGoodsAppTv(goodsId, start, end);
            Integer topTen = 0;
            for(int i=0;i<list.size();i++){
                topTen+=list.get(i).getVolume();
            }
            Integer elseVolume = tv-topTen;
            if(elseVolume>0){
                AppOutParam appOutParam = new AppOutParam();
                appOutParam.setAppName("其他");
                appOutParam.setVolume(elseVolume);
                list.add(appOutParam);
            }
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(AppOutParam appOutParam : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("volume",appOutParam.getVolume());
            map.put("appName",appOutParam.getAppName());
            result.add(map);
        }
        return result;
    }

    /**
     * 查询统计时间段内公众号成交额分布数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询结果
     */
    @Override
    public List<Map<String, Object>> getAppVot(Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<AppOutParam> list = appDataMapper.selectAppVot(start,end);
        list = setAppName(list);
        if(list!=null&&list.size()==10){
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            Integer vot = appDataMapper.selectAllAppVot(start, end);
            Integer topTen = 0;
            for(int i = 0;i<list.size();i++){
                topTen += list.get(i).getGmv();
            }
            Integer elseGmv = vot-topTen;
            if(elseGmv>0){
                AppOutParam appOutParam = new AppOutParam();
                appOutParam.setAppName("其他");
                appOutParam.setGmv(elseGmv);
                list.add(appOutParam);
            }
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(AppOutParam appOutParam : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gmv",appOutParam.getGmv());
            map.put("appName",appOutParam.getAppName());
            result.add(map);
        }
        return result;
    }

    /**
     * 查询统计时间段内公众号成交量分布数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询结果
     */
    @Override
    public List<Map<String, Object>> getAppTv(Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<AppOutParam> list = appDataMapper.selectAppTv( start,end);
        list = setAppName(list);
        if(list!=null&&list.size()==10){
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            Integer tv = appDataMapper.selectAllAppTv(start, end);
            Integer topTen = 0;
            for(int i=0;i<list.size();i++){
                topTen+=list.get(i).getVolume();
            }
            Integer elseVolume = tv-topTen;
            if(elseVolume>0){
                AppOutParam appOutParam = new AppOutParam();
                appOutParam.setAppName("其他");
                appOutParam.setVolume(elseVolume);
                list.add(appOutParam);
            }
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(AppOutParam appOutParam : list){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("volume",appOutParam.getVolume());
            map.put("appName",appOutParam.getAppName());
            result.add(map);
        }
        return result;
    }

    /**
     * 查询一段时间内公众号下商品的数据
     * @param appId     公众号id
     * @param goodsIds  商品id，数组
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @return 商品的数据
     */
    @Override
    public Map<String, Object> getAppGoodsInfo(Long appId, Long[] goodsIds, Long startDate, Long endDate) {
        String start = TimestampUtils.getStringDateFromLong(startDate);
        String end = TimestampUtils.getStringDateFromLong(endDate);
        Map<String, Object> result = new HashMap<String, Object>();
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<AppOutParam> list = appDataMapper.selectAppGoodsInfo(appId,goodsIds,start,end);
        for(AppOutParam appOutParam : list){
            String dateString = TimestampUtils.getStringDateFromDate(appOutParam.getDate());
            if(result.get(dateString)==null){
                Map<String, Object> map = new HashMap<String, Object>();
                result.put(dateString,map);
            }
            Map<String, Object> map = (Map<String, Object>)result.get(dateString);
            map.put(appOutParam.getGoodsId().toString(),appOutParam);
        }
        Map<String,Object> mapPv = new HashMap<String, Object>();
        List<Map<String,Object>> mapPvList = new ArrayList<Map<String, Object>>();
        mapPv.put("series",mapPvList);
        Map<String,Object> mapUv = new HashMap<String, Object>();
        List<Map<String,Object>> mapUvList = new ArrayList<Map<String, Object>>();
        mapUv.put("series",mapUvList);
        Map<String,Object> mapGmv = new HashMap<String, Object>();
        List<Map<String,Object>> mapGmvList = new ArrayList<Map<String, Object>>();
        mapGmv.put("series",mapGmvList);
        Map<String,Object> mapGmvUv = new HashMap<String, Object>();
        List<Map<String,Object>> mapGmvUvList = new ArrayList<Map<String, Object>>();
        mapGmvUv.put("series",mapGmvUvList);
        Map<String,Object> mapGrossMargin = new HashMap<String, Object>();
        List<Map<String,Object>> mapGrossMarginList = new ArrayList<Map<String, Object>>();
        mapGrossMargin.put("series",mapGrossMarginList);
        Map<String,Object> mapConvertRate = new HashMap<String, Object>();
        List<Map<String,Object>> mapConvertRateList = new ArrayList<Map<String, Object>>();
        mapConvertRate.put("series",mapConvertRateList);
        Map<String,Object> mapAvgPrice = new HashMap<String, Object>();
        List<Map<String,Object>> mapAvgPriceList = new ArrayList<Map<String, Object>>();
        mapAvgPrice.put("series",mapAvgPriceList);
        Map<Long,Object> nameMap = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
        String[] dates = getDates(startDate,endDate);
        for (String date : dates) {
            //获取当前时间的结果数据
            Map<String, Object> mapOfDate = (Map<String, Object>) result.get(date);
            if (mapOfDate == null) {
                mapOfDate = new HashMap<String, Object>();
            }
            for (Long goodsId : goodsIds) {
                AppOutParam goods = (AppOutParam) mapOfDate.get(goodsId.toString());
                if (((List<Map<String, Object>>) mapPv.get("series")).size() == 0) {
                    for (Long id : goodsIds) {
                        Map<String, Object> map1 = new HashMap<String, Object>();
                        map1.put("goodsId", id);
                        map1.put("name", nameMap.get(id));
                        ((List<Map<String, Object>>) mapPv.get("series")).add(map1);
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("goodsId", id);
                        map2.put("name", nameMap.get(id));
                        ((List<Map<String, Object>>) mapUv.get("series")).add(map2);
                        Map<String, Object> map3 = new HashMap<String, Object>();
                        map3.put("goodsId", id);
                        map3.put("name", nameMap.get(id));
                        ((List<Map<String, Object>>) mapGmv.get("series")).add(map3);
                        Map<String, Object> map4 = new HashMap<String, Object>();
                        map4.put("goodsId", id);
                        map4.put("name", nameMap.get(id));
                        ((List<Map<String, Object>>) mapGmvUv.get("series")).add(map4);
                        Map<String, Object> map5 = new HashMap<String, Object>();
                        map5.put("goodsId", id);
                        map5.put("name", nameMap.get(id));
                        ((List<Map<String, Object>>) mapGrossMargin.get("series")).add(map5);
                        Map<String, Object> map6 = new HashMap<String, Object>();
                        map6.put("goodsId", id);
                        map6.put("name", nameMap.get(id));
                        ((List<Map<String, Object>>) mapConvertRate.get("series")).add(map6);
                        Map<String, Object> map7 = new HashMap<String, Object>();
                        map7.put("goodsId", id);
                        map7.put("name", nameMap.get(id));
                        ((List<Map<String, Object>>) mapAvgPrice.get("series")).add(map7);
                    }
                }
                if (goods != null) {
                    List<Map<String, Object>> listGoods1 = ((List<Map<String, Object>>) mapPv.get("series"));
                    for (Map<String, Object> mapGoods1 : listGoods1) {
                        if (mapGoods1.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods1.get("data");
                            if (data == null) {
                                mapGoods1.put("data", goods.getPv());
                            } else {
                                mapGoods1.put("data", data.toString() + "," + goods.getPv());
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods2 = ((List<Map<String, Object>>) mapUv.get("series"));
                    for (Map<String, Object> mapGoods2 : listGoods2) {
                        if (mapGoods2.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods2.get("data");
                            if (data == null) {
                                mapGoods2.put("data", goods.getUv());
                            } else {
                                mapGoods2.put("data", data.toString() + "," + goods.getUv());
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods3 = ((List<Map<String, Object>>) mapGmv.get("series"));
                    for (Map<String, Object> mapGoods3 : listGoods3) {
                        if (mapGoods3.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods3.get("data");
                            if (data == null) {
                                mapGoods3.put("data", goods.getGmv());
                            } else {
                                mapGoods3.put("data", data.toString() + "," + goods.getGmv());
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods4 = ((List<Map<String, Object>>) mapGmvUv.get("series"));
                    for (Map<String, Object> mapGoods4 : listGoods4) {
                        if (mapGoods4.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods4.get("data");
                            if (data == null) {
                                mapGoods4.put("data", goods.getGmvUv());
                            } else {
                                mapGoods4.put("data", data.toString() + "," + goods.getGmvUv());
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods5 = ((List<Map<String, Object>>) mapGrossMargin.get("series"));
                    for (Map<String, Object> mapGoods5 : listGoods5) {
                        if (mapGoods5.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods5.get("data");
                            if (data == null) {
                                mapGoods5.put("data", goods.getGrossMargin());
                            } else {
                                mapGoods5.put("data", data.toString() + "," + goods.getGrossMargin());
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods6 = ((List<Map<String, Object>>) mapConvertRate.get("series"));
                    for (Map<String, Object> mapGoods6 : listGoods6) {
                        if (mapGoods6.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods6.get("data");
                            if (data == null) {
                                mapGoods6.put("data", goods.getConvertRate());
                            } else {
                                mapGoods6.put("data", data.toString() + "," + goods.getConvertRate());
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods7 = ((List<Map<String, Object>>) mapAvgPrice.get("series"));
                    for (Map<String, Object> mapGoods7 : listGoods7) {
                        if (mapGoods7.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods7.get("data");
                            if (data == null) {
                                mapGoods7.put("data", goods.getAvgPrice());
                            } else {
                                mapGoods7.put("data", data.toString() + "," + goods.getAvgPrice());
                            }
                        }
                    }
                } else {
                    List<Map<String, Object>> listGoods1 = ((List<Map<String, Object>>) mapPv.get("series"));
                    for (Map<String, Object> mapGoods1 : listGoods1) {
                        if (mapGoods1.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods1.get("data");
                            if (data == null) {
                                mapGoods1.put("data", 0);
                            } else {
                                mapGoods1.put("data", data.toString() + "," + 0);
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods2 = ((List<Map<String, Object>>) mapUv.get("series"));
                    for (Map<String, Object> mapGoods2 : listGoods2) {
                        if (mapGoods2.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods2.get("data");
                            if (data == null) {
                                mapGoods2.put("data", 0);
                            } else {
                                mapGoods2.put("data", data.toString() + "," + 0);
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods3 = ((List<Map<String, Object>>) mapGmv.get("series"));
                    for (Map<String, Object> mapGoods3 : listGoods3) {
                        if (mapGoods3.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods3.get("data");
                            if (data == null) {
                                mapGoods3.put("data", 0);
                            } else {
                                mapGoods3.put("data", data.toString() + "," + 0);
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods4 = ((List<Map<String, Object>>) mapGmvUv.get("series"));
                    for (Map<String, Object> mapGoods4 : listGoods4) {
                        if (mapGoods4.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods4.get("data");
                            if (data == null) {
                                mapGoods4.put("data", 0);
                            } else {
                                mapGoods4.put("data", data.toString() + "," + 0);
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods5 = ((List<Map<String, Object>>) mapGrossMargin.get("series"));
                    for (Map<String, Object> mapGoods5 : listGoods5) {
                        if (mapGoods5.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods5.get("data");
                            if (data == null) {
                                mapGoods5.put("data", 0);
                            } else {
                                mapGoods5.put("data", data.toString() + "," + 0);
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods6 = ((List<Map<String, Object>>) mapConvertRate.get("series"));
                    for (Map<String, Object> mapGoods6 : listGoods6) {
                        if (mapGoods6.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods6.get("data");
                            if (data == null) {
                                mapGoods6.put("data", 0);
                            } else {
                                mapGoods6.put("data", data.toString() + "," + 0);
                            }
                        }
                    }
                    List<Map<String, Object>> listGoods7 = ((List<Map<String, Object>>) mapAvgPrice.get("series"));
                    for (Map<String, Object> mapGoods7 : listGoods7) {
                        if (mapGoods7.get("goodsId").toString().equals(goodsId.toString())) {
                            Object data = mapGoods7.get("data");
                            if (data == null) {
                                mapGoods7.put("data", 0);
                            } else {
                                mapGoods7.put("data", data.toString() + "," + 0);
                            }
                        }
                    }
                }
            }

        }
        Map<String,Object> returnMap = new HashMap<String,Object>();
        returnMap.put("pv",mapPv);
        returnMap.put("uv",mapUv);
        returnMap.put("gmv",mapGmv);
        returnMap.put("gmvUv",mapGmvUv);
        returnMap.put("convertRate",mapConvertRate);
        returnMap.put("grossMatgin",mapGrossMargin);
        returnMap.put("avgPrice",mapAvgPrice);
        Map<String,Object> dateMap = new HashMap<String,Object>();
        dateMap.put("data",dates);
        List<Map<String,Object>> dateList = new ArrayList<Map<String, Object>>();
        dateList.add(dateMap);
        returnMap.put("xAxis",dateList);
        return returnMap;
    }

    /**
     * 根据起止时间获取日期数组
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @return 日期数组
     */
    private String[] getDates(Long startDate, Long endDate){
        Long length = (endDate-startDate)/86400;
        Integer days = Integer.parseInt(length.toString())+1;
        String[] date = new String[days];
        for(int i=0;i<days;i++){
            date[i] = TimestampUtils.getStringDateFromLong(startDate+i*86400);
        }
        return date;
    }

    /**
     * 设置商品名称
     */
    private List<AppOutParam> setGoodsName(List<AppOutParam> list){
        Long[] ids = new Long[list.size()];
        for(int i=0;i<list.size();i++){
            ids[i] = list.get(i).getGoodsId();
        }
        Map<Long,Object> map = goodsDataService.getGoodsNamesByGoodsIds(ids);
        for(AppOutParam appOutParam : list){
            appOutParam.setGoodsName(map.get(appOutParam.getGoodsId()).toString());
        }
        return list;
    }

    /**
     * 设置公众号名称
     */
    private List<AppOutParam> setAppName(List<AppOutParam> list){
        Long[] ids = new Long[list.size()];
        for(int i=0;i<list.size();i++){
            ids[i] = list.get(i).getAppId();
        }
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        List<Map<String,Object>> mapList = appDataMapper.selectAppNamesByIds(ids);
        Map<Long,Object> map = new HashMap<Long,Object>();
        for (Map<String,Object> appMap : mapList){
            map.put(Long.parseLong(appMap.get("appId").toString()),appMap.get("appName"));
        }
        for(AppOutParam appOutParam : list){
            appOutParam.setAppName(map.get(appOutParam.getAppId()).toString());
        }
        return list;
    }
}
