package com.quxin.freshfun.impl.goods;

import com.google.common.collect.Maps;
import com.quxin.freshfun.api.goods.GoodsDataService;
import com.quxin.freshfun.api.goods.GoodsService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.db.DynamicDataSource;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import com.quxin.freshfun.utils.CategoryUtils;
import com.quxin.freshfun.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 商品service实现类
 * Created by qucheng on 2016/10/18.
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDataService goodsDataService;

    Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);


    @Override
    public List<Map<String, Object>> getGoodsGMVTopTen(Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Long sumGmv = goodsMapper.selectSumGmv(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
        if (sumGmv != null) { //时间段内没有gmv
            List<Map<String, Object>> topTen = goodsMapper.selectGmvTopTenGoods(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Long[] goodsIds = new Long[topTen.size()];
            Long sumTopTenGmv = 0L;
            int i = 0;
            for (Map<String, Object> map : topTen) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
                sumTopTenGmv = sumTopTenGmv + Long.parseLong(map.get("gmv").toString());
            }

            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            for (Map<String, Object> map : topTen) {
                Map<String, Object> record = Maps.newHashMap();
                Long goodsId = Long.parseLong(map.get("goodsId").toString());
                record.put("goodsId", goodsId);
                record.put("name", goodsNames.get(goodsId));
                record.put("value", map.get("gmv"));
                result.add(record);
            }
            //如果总的gmv大于前十的gmv才有其他
            Long otherGmv = sumGmv - sumTopTenGmv;
            if (otherGmv > 0) {
                Map<String, Object> other = Maps.newHashMap();
                other.put("goodsId", 0);
                other.put("name", "其他");
                other.put("value", otherGmv);
                result.add(other);
            }
            return result;
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getGoodsVolumeTopTen(Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Long sumVolume = goodsMapper.selectSumVolume(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
        if (sumVolume != null) { //没有成交额
            List<Map<String, Object>> volumeTopTen = goodsMapper.selectVolumeTopTenGoods(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Long[] goodsIds = new Long[volumeTopTen.size()];
            Long sumTopTenVolume = 0L;
            int i = 0;
            for (Map<String, Object> map : volumeTopTen) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
                sumTopTenVolume = sumTopTenVolume + Long.parseLong(map.get("volume").toString());
            }
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            for (Map<String, Object> map : volumeTopTen) {
                Map<String, Object> record = Maps.newHashMap();
                Long goodsId = Long.parseLong(map.get("goodsId").toString());
                record.put("goodsId", goodsId);
                record.put("name", goodsNames.get(goodsId));
                record.put("value", map.get("volume"));
                result.add(record);
            }
            //如果总的成交量大于前十的成交量才有其他
            Long otherVolume = sumVolume - sumTopTenVolume;
            if (otherVolume > 0) {
                Map<String, Object> other = Maps.newHashMap();
                other.put("goodsId", 0);
                other.put("name", "其他");
                other.put("value", otherVolume);
                result.add(other);
            }
            return result;
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getCategoryGmvTopTen(Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<Map<String, Object>> gmvTopTenCategory = goodsMapper.selectGmvTopTenCategory(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
        if (gmvTopTenCategory.size() > 0) {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Long sumTopTenCategoryGmv = 0L;
            for (Map<String, Object> map : gmvTopTenCategory) {
                sumTopTenCategoryGmv = sumTopTenCategoryGmv + Long.parseLong(map.get("gmv").toString());
            }
            for (Map<String, Object> map : gmvTopTenCategory) {
                Map<String, Object> record = Maps.newHashMap();
                Integer categoryId = Integer.parseInt(map.get("categoryId").toString());
                record.put("categoryId", categoryId);
                record.put("name", CategoryUtils.getCategoryNameById(categoryId));
                record.put("value", map.get("gmv"));
                result.add(record);
            }
            return result;
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getGoodsGmvTopTenByCategory(Integer category, Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Long sumGmvByCategory = goodsMapper.selectSumGmvByCategory(category,
                TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
        if (sumGmvByCategory != null) {
            List<Map<String, Object>> goodsGmvTopTenByCategory = goodsMapper.selectGmvTopTenGoodsByCategory(category,
                    TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Long[] goodsIds = new Long[goodsGmvTopTenByCategory.size()];
            Long sumTopTenGmv = 0L;
            int i = 0;
            for (Map<String, Object> map : goodsGmvTopTenByCategory) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
                sumTopTenGmv = sumTopTenGmv + Long.parseLong(map.get("gmv").toString());
            }

            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            for (Map<String, Object> map : goodsGmvTopTenByCategory) {
                Map<String, Object> record = Maps.newHashMap();
                Long goodsId = Long.parseLong(map.get("goodsId").toString());
                record.put("goodsId", goodsId);
                record.put("name", goodsNames.get(goodsId));
                record.put("value", map.get("gmv"));
                result.add(record);
            }
            //如果总的gmv大于前十的gmv才有其他
            Long otherGmv = sumGmvByCategory - sumTopTenGmv;
            if (otherGmv > 0) {
                Map<String, Object> other = Maps.newHashMap();
                other.put("goodsId", 0);
                other.put("name", "其他");
                other.put("value", otherGmv);
                result.add(other);
            }
            return result;
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getCategoryVolumeTopTen(Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        List<Map<String, Object>> volumeTopTenCategory = goodsMapper.selectVolumeTopTenCategory(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
        if (volumeTopTenCategory.size() > 0) {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Long sumTopTenCategoryGmv = 0L;
            for (Map<String, Object> map : volumeTopTenCategory) {
                sumTopTenCategoryGmv = sumTopTenCategoryGmv + Long.parseLong(map.get("volume").toString());
            }
            for (Map<String, Object> map : volumeTopTenCategory) {
                Map<String, Object> record = Maps.newHashMap();
                Integer categoryId = Integer.parseInt(map.get("categoryId").toString());
                record.put("categoryId", categoryId);
                record.put("name", CategoryUtils.getCategoryNameById(categoryId));
                record.put("value", map.get("volume"));
                result.add(record);
            }
            return result;
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getGoodsVolumeTopTenByCategory(Integer category, Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Long sumVolumeByCategory = goodsMapper.selectSumVolumeByCategory(category,
                TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
        if (sumVolumeByCategory != null) {
            List<Map<String, Object>> goodsVolumeTopTenByCategory = goodsMapper.selectVolumeTopTenGoodsByCategory(category,
                    TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Long[] goodsIds = new Long[goodsVolumeTopTenByCategory.size()];
            Long sumTopTenVolume = 0L;
            int i = 0;
            for (Map<String, Object> map : goodsVolumeTopTenByCategory) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
                sumTopTenVolume = sumTopTenVolume + Long.parseLong(map.get("volume").toString());
            }

            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            for (Map<String, Object> map : goodsVolumeTopTenByCategory) {
                Map<String, Object> record = Maps.newHashMap();
                Long goodsId = Long.parseLong(map.get("goodsId").toString());
                record.put("goodsId", goodsId);
                record.put("name", goodsNames.get(goodsId));
                record.put("value", map.get("volume"));
                result.add(record);
            }
            //如果总的gmv大于前十的gmv才有其他
            Long otherVolume = sumVolumeByCategory - sumTopTenVolume;
            if (otherVolume > 0) {
                Map<String, Object> other = Maps.newHashMap();
                other.put("goodsId", 0);
                other.put("name", "其他");
                other.put("value", otherVolume);
                result.add(other);
            }
            return result;
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public Map<String, Object> getGoodsIndicator(List<Long> goodsIds, Long startTime, Long endTime) {
        if (goodsIds != null && goodsIds.size() > 0) {
            Long[] goodsIdsArr = new Long[goodsIds.size()];
            int i = 0;
            for (Long goodsId : goodsIds) {
                goodsIdsArr[i] = goodsId;
                i++;
            }
            //设置数据源
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIdsArr);
            //获取x轴
            String[] dates = TimestampUtils.getDates(startTime, endTime);
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            //查询时间段内商品指标的数据
            List<Map<String, Object>> allGoodsIndicator = goodsMapper.selectGoodsIndicator(goodsIds,
                    TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
            Map<String , Object> result = Maps.newHashMap();
            //日期处理
            Map<String , Object> data = Maps.newHashMap();
            List<Map<String , Object>> xAxisValue = new ArrayList<>();
            data.put("data" , dates);
            xAxisValue.add(data);
            result.put("xAxis" , xAxisValue);
            if (allGoodsIndicator.size() > 0) {
                Map<String, Object> pv = Maps.newHashMap();
                Map<String, Object> uv = Maps.newHashMap();
                Map<String, Object> avgPrice = Maps.newHashMap();
                Map<String, Object> grossMargin = Maps.newHashMap();
                Map<String, Object> convertRate = Maps.newHashMap();
                Map<String, Object> gmv = Maps.newHashMap();
                Map<String, Object> gmvUv = Maps.newHashMap();
                Map<String, Object> sevenRpr = Maps.newHashMap();
                Map<String, Object> monthRpr = Maps.newHashMap();
                List<Map<String, Object>> seriesPv = new ArrayList<>();
                List<Map<String, Object>> seriesUv = new ArrayList<>();
                List<Map<String, Object>> seriesAvgPrice = new ArrayList<>();
                List<Map<String, Object>> seriesGrossMargin = new ArrayList<>();
                List<Map<String, Object>> seriesConvertRate = new ArrayList<>();
                List<Map<String, Object>> seriesGmv = new ArrayList<>();
                List<Map<String, Object>> seriesGmvUv = new ArrayList<>();
                List<Map<String, Object>> seriesSevenRpr = new ArrayList<>();
                List<Map<String, Object>> seriesMonthRpr = new ArrayList<>();
                for (Long goodsId : goodsIds) {
                    List<Map<String, Object>> goods = new ArrayList<>();
                    //商品基本信息
                    Map<String, Object> goodsMapPv = Maps.newHashMap();
                    Map<String, Object> goodsMapUv = Maps.newHashMap();
                    Map<String, Object> goodsMapAvgPrice = Maps.newHashMap();
                    Map<String, Object> goodsMapGrossMargin = Maps.newHashMap();
                    Map<String, Object> goodsMapConvertRate = Maps.newHashMap();
                    Map<String, Object> goodsMapGmv = Maps.newHashMap();
                    Map<String, Object> goodsMapGmvUv = Maps.newHashMap();
                    Map<String, Object> goodsMapSevenRpr = Maps.newHashMap();
                    Map<String, Object> goodsMapMonthRpr = Maps.newHashMap();
                    goodsMapPv.put("name", goodsNames.get(goodsId));
                    goodsMapUv.put("name", goodsNames.get(goodsId));
                    goodsMapAvgPrice.put("name", goodsNames.get(goodsId));
                    goodsMapGrossMargin.put("name", goodsNames.get(goodsId));
                    goodsMapConvertRate.put("name", goodsNames.get(goodsId));
                    goodsMapGmv.put("name", goodsNames.get(goodsId));
                    goodsMapGmvUv.put("name", goodsNames.get(goodsId));
                    goodsMapSevenRpr.put("name", goodsNames.get(goodsId));
                    goodsMapMonthRpr.put("name", goodsNames.get(goodsId));
                    //将id相同的数据统一起来重新排序,获取按时间排序的指标
                    for (Map<String, Object> map : allGoodsIndicator) {
                        if (goodsId.equals(Long.parseLong(map.get("goodsId").toString()))) {
                            goods.add(map);
                        }
                    }
                    List<Integer> pvArr = new ArrayList<>();
                    List<Integer> uvArr = new ArrayList<>();
                    List<Double> avgPriceArr = new ArrayList<>();
                    List<Double> grossMarginArr = new ArrayList<>();
                    List<Double> convertRateArr = new ArrayList<>();
                    List<Double> gmvArr = new ArrayList<>();
                    List<Double> gmvUvArr = new ArrayList<>();
                    List<Double> sevenRprArr = new ArrayList<>();
                    List<Double> monthRprArr = new ArrayList<>();
                    for (String date : dates) {
                        for (Map<String, Object> map : goods) { //根据时间排序pv
                            if (date.equals(map.get("created").toString())) {
                                pvArr.add(Integer.parseInt(map.get("pv").toString()));
                                uvArr.add(Integer.parseInt(map.get("uv").toString()));
                                avgPriceArr.add(Double.parseDouble(map.get("avgPrice").toString()) / 100);
                                grossMarginArr.add(Double.parseDouble(map.get("grossMargin").toString()) / 100);
                                convertRateArr.add(Double.parseDouble(map.get("convertRate").toString()) / 100);
                                gmvArr.add(Double.parseDouble(map.get("gmv").toString()) / 100);
                                gmvUvArr.add(Double.parseDouble(map.get("gmvUv").toString()) / 100);
                                sevenRprArr.add(Double.parseDouble(map.get("sevenRpr").toString()) / 100);
                                monthRprArr.add(Double.parseDouble(map.get("monthRpr").toString()) / 100);
                            }
                        }
                    }
                    goodsMapPv.put("data", pvArr);//pv数组
                    seriesPv.add(goodsMapPv);
                    goodsMapUv.put("data", uvArr);//uv数组
                    seriesUv.add(goodsMapUv);
                    goodsMapAvgPrice.put("data", avgPriceArr);
                    seriesAvgPrice.add(goodsMapAvgPrice);
                    goodsMapGrossMargin.put("data", grossMarginArr);
                    seriesGrossMargin.add(goodsMapGrossMargin);
                    goodsMapConvertRate.put("data", convertRateArr);
                    seriesConvertRate.add(goodsMapConvertRate);
                    goodsMapGmv.put("data", gmvArr);
                    seriesGmv.add(goodsMapGmv);
                    goodsMapGmvUv.put("data", gmvUvArr);
                    seriesGmvUv.add(goodsMapGmvUv);
                    goodsMapSevenRpr.put("data", sevenRprArr);
                    seriesSevenRpr.add(goodsMapSevenRpr);
                    goodsMapMonthRpr.put("data", monthRprArr);
                    seriesMonthRpr.add(goodsMapMonthRpr);
                }
                pv.put("series", seriesPv);
                uv.put("series", seriesUv);
                avgPrice.put("series", seriesAvgPrice);
                grossMargin.put("series", seriesGrossMargin);
                convertRate.put("series", seriesConvertRate);
                gmv.put("series", seriesGmv);
                gmvUv.put("series", seriesGmvUv);
                sevenRpr.put("series", seriesSevenRpr);
                monthRpr.put("series", seriesMonthRpr);

                result.put("pv" , pv);
                result.put("uv" , uv);
                result.put("avgPrice" , avgPrice);
                result.put("grossMargin" , grossMargin);
                result.put("convertRate" , convertRate);
                result.put("gmv" , gmv);
                result.put("gmvUv" , gmvUv);
                result.put("sevenRpr" , sevenRpr);
                result.put("monthRpr" , monthRpr);
            } else {
                logger.error(TimestampUtils.getStringDateFromLong(startTime) + "到" + TimestampUtils.getStringDateFromLong(endTime) + "期间没有数据");
            }
        }
        return null;
    }

}
