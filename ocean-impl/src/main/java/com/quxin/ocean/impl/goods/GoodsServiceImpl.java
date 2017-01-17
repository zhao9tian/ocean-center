package com.quxin.ocean.impl.goods;

import com.google.common.collect.Maps;
import com.quxin.ocean.service.GoodsDataService;
import com.quxin.ocean.api.goods.GoodsService;
import com.quxin.ocean.boot.Indicator;
import com.quxin.ocean.dao.GoodsMapper;
import com.quxin.ocean.db.DynamicDataSource;
import com.quxin.ocean.db.DynamicDataSourceHolder;
import com.quxin.ocean.utils.CategoryUtils;
import com.quxin.ocean.utils.MoneyFormatUtils;
import com.quxin.ocean.utils.TimestampUtils;
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

    private Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);


    @Override
    public List<Map<String, Object>> getGoodsGMVTopTen(Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        Long sumGmv = goodsMapper.selectSumGmv(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
        if (sumGmv != null) { //时间段内没有gmv
            List<Map<String, Object>> topTen = goodsMapper.selectGmvTopTenGoods(TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
            Long[] goodsIds = new Long[topTen.size()];
            int i = 0;
            for (Map<String, Object> map : topTen) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
            }
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            return getPieChartForMoney(topTen, sumGmv, goodsNames, Indicator.GMV);
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
            Long[] goodsIds = new Long[volumeTopTen.size()];
            Long sumTopTenVolume = 0L;
            int i = 0;
            for (Map<String, Object> map : volumeTopTen) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
                sumTopTenVolume = sumTopTenVolume + Long.parseLong(map.get(Indicator.VOLUME).toString());
            }
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            return getPieChartForNum(volumeTopTen, sumVolume, goodsNames, Indicator.VOLUME);
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
            List<Map<String, Object>> result = new ArrayList<>();
            Long sumTopTenCategoryGmv = 0L;
            for (Map<String, Object> map : gmvTopTenCategory) {
                sumTopTenCategoryGmv = sumTopTenCategoryGmv + Long.parseLong(map.get(Indicator.GMV).toString());
            }
            for (Map<String, Object> map : gmvTopTenCategory) {
                Map<String, Object> record = Maps.newHashMap();
                Integer categoryId = Integer.parseInt(map.get("categoryId").toString());
                record.put("categoryId", categoryId);
                record.put("name", CategoryUtils.getCategoryNameById(categoryId));
                record.put("value", MoneyFormatUtils.getDoubleMoney(Long.parseLong(map.get(Indicator.GMV).toString())));
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
            Long[] goodsIds = new Long[goodsGmvTopTenByCategory.size()];
            Long sumTopTenGmv = 0L;
            int i = 0;
            for (Map<String, Object> map : goodsGmvTopTenByCategory) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
            }

            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            return getPieChartForMoney(goodsGmvTopTenByCategory, sumTopTenGmv, goodsNames, Indicator.GMV);
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
            List<Map<String, Object>> result = new ArrayList<>();
            Long sumTopTenCategoryGmv = 0L;
            for (Map<String, Object> map : volumeTopTenCategory) {
                sumTopTenCategoryGmv = sumTopTenCategoryGmv + Long.parseLong(map.get(Indicator.VOLUME).toString());
            }
            for (Map<String, Object> map : volumeTopTenCategory) {
                Map<String, Object> record = Maps.newHashMap();
                Integer categoryId = Integer.parseInt(map.get("categoryId").toString());
                record.put("categoryId", categoryId);
                record.put("name", CategoryUtils.getCategoryNameById(categoryId));
                record.put("value", map.get(Indicator.VOLUME));
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
            Long[] goodsIds = new Long[goodsVolumeTopTenByCategory.size()];
            Long sumTopTenVolume = 0L;
            int i = 0;
            for (Map<String, Object> map : goodsVolumeTopTenByCategory) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
                sumTopTenVolume = sumTopTenVolume + Long.parseLong(map.get(Indicator.VOLUME).toString());
            }

            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            return getPieChartForNum(goodsVolumeTopTenByCategory, sumVolumeByCategory, goodsNames, Indicator.VOLUME);
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public Map<String, Object> getGoodsIndicator(List<Long> goodsIds, Long startTime, Long endTime) {
        if (goodsIds != null && goodsIds.size() > 0) {
            //先校验商品id
            Long[] goodsIdsArr = new Long[goodsIds.size()];
            int i = 0;
            for (Long goodsId : goodsIds) {
                goodsIdsArr[i] = goodsId;
                i++;
            }
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIdsArr);
            for (Long goodsId : goodsIds) {
                if (goodsNames.get(goodsId) == null) {
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("code", 1004);
                    map.put("msg", "id为" + goodsId + "的商品不存在");
                    Map<String, Object> resultMap = Maps.newHashMap();
                    resultMap.put("status", map);
                    return resultMap;
                }
            }
            //获取x轴
            String[] dates = TimestampUtils.getDates(startTime, endTime);
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            //查询时间段内商品指标的数据
            List<Map<String, Object>> allGoodsIndicator = goodsMapper.selectGoodsIndicator(goodsIds,
                    TimestampUtils.getStringDateFromLong(startTime), TimestampUtils.getStringDateFromLong(endTime));
            Map<String, Object> result = Maps.newHashMap();
            if (allGoodsIndicator.size() > 0) {
                //日期处理
                Map<String, Object> data = Maps.newHashMap();
                List<Map<String, Object>> xAxisValue = new ArrayList<>();
                data.put("data", dates);
                xAxisValue.add(data);
                result.put("xAxis", xAxisValue);
                result.put(Indicator.PV, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.PV));
                result.put(Indicator.UV, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.UV));
                result.put(Indicator.VOLUME, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.VOLUME));
                result.put(Indicator.AVG_PRICE, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.AVG_PRICE));
                result.put(Indicator.GROSS_MARGIN, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.GROSS_MARGIN));
                result.put(Indicator.CONVERT_RATE, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.CONVERT_RATE));
                result.put(Indicator.GMV, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.GMV));
                result.put(Indicator.GMV_UV, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.GMV_UV));
                result.put(Indicator.SEVEN_RPR, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.SEVEN_RPR));
                result.put(Indicator.MONTH_RPR, getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, Indicator.MONTH_RPR));
                return result;
            } else {
                logger.error(TimestampUtils.getStringDateFromLong(startTime) + "到" + TimestampUtils.getStringDateFromLong(endTime) + "期间没有数据");
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getIndicatorsForHistogram(Long startTime, Long endTime) {
        String startDate = TimestampUtils.getStringDateFromLong(startTime);
        String endDate = TimestampUtils.getStringDateFromLong(endTime);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        //时间段内各项指标前十排名
        List<Map<String, Object>> pvList = goodsMapper.selectTopIndicatorForNum(Indicator.PV, startDate, endDate);
        List<Map<String, Object>> uvList = goodsMapper.selectTopIndicatorForNum(Indicator.UV, startDate, endDate);
        List<Map<String, Object>> avgPriceList = goodsMapper.selectTopIndicator("avg_price", Indicator.AVG_PRICE, startDate, endDate);
        List<Map<String, Object>> grossMarginList = goodsMapper.selectTopIndicator("gross_margin", Indicator.GROSS_MARGIN, startDate, endDate);
        List<Map<String, Object>> convertRateList = goodsMapper.selectTopIndicator("convert_rate", Indicator.CONVERT_RATE, startDate, endDate);
        List<Map<String, Object>> gmvUvList = goodsMapper.selectTopIndicator("gmv_uv", Indicator.GMV_UV, startDate, endDate);
        List<Map<String, Object>> sevenRprList = goodsMapper.selectTopIndicator("seven_rpr", Indicator.SEVEN_RPR, startDate, endDate);
        List<Map<String, Object>> monthRprList = goodsMapper.selectTopIndicator("month_rpr", Indicator.MONTH_RPR, startDate, endDate);
        List<Map<String, Object>> all = new ArrayList<>();
        all.addAll(pvList);
        all.addAll(uvList);
        all.addAll(avgPriceList);
        all.addAll(grossMarginList);
        all.addAll(convertRateList);
        all.addAll(gmvUvList);
        all.addAll(sevenRprList);
        all.addAll(monthRprList);
        List<Long> goodsIds = new ArrayList<>();
        if (all.size() > 0) {
            for (Map<String, Object> map : all) {
                goodsIds.add(Long.parseLong(map.get("goodsId").toString()));
            }
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            Map<String, Object> result = Maps.newHashMap();
            result.put(Indicator.PV, getHistogramData(pvList, goodsNames, Indicator.PV));
            result.put(Indicator.UV, getHistogramData(uvList, goodsNames, Indicator.UV));
            result.put(Indicator.AVG_PRICE, getHistogramData(avgPriceList, goodsNames, Indicator.AVG_PRICE));
            result.put(Indicator.GROSS_MARGIN, getHistogramData(grossMarginList, goodsNames, Indicator.GROSS_MARGIN));
            result.put(Indicator.CONVERT_RATE, getHistogramData(convertRateList, goodsNames, Indicator.CONVERT_RATE));
            result.put(Indicator.GMV_UV, getHistogramData(gmvUvList, goodsNames, Indicator.GMV_UV));
            result.put(Indicator.SEVEN_RPR, getHistogramData(sevenRprList, goodsNames, Indicator.SEVEN_RPR));
            result.put(Indicator.MONTH_RPR, getHistogramData(monthRprList, goodsNames, Indicator.MONTH_RPR));
            return result;
        } else {
            return null;
        }

    }

    /**
     * 封装top10的数据
     *
     * @param dataList   数据列表
     * @param goodsNames 商品名称
     * @param indicator  指标
     * @return 柱状图数据
     */
    private Map<String, Object> getHistogramData(List<Map<String, Object>> dataList, Map<Long, Object> goodsNames, String indicator) {
        List<Long> goodsIds = new ArrayList<>();
        List<String> goodsName = new ArrayList<>();
        Map<String, Object> result = Maps.newHashMap();
        if (Indicator.PV.equals(indicator) || Indicator.UV.equals(indicator)) {
            List<Integer> goodsIndicatorValue = new ArrayList<>();
            for (Map<String, Object> map : dataList) {
                Long goodsId = Long.parseLong(map.get("goodsId").toString());
                goodsIds.add(goodsId);
                goodsName.add(goodsNames.get(goodsId).toString());
                goodsIndicatorValue.add(Integer.parseInt(map.get(indicator).toString()));
            }
            result.put("values", goodsIndicatorValue);
        } else {
            List<Double> goodsIndicatorValue = new ArrayList<>();
            for (Map<String, Object> map : dataList) {
                Long goodsId = Long.parseLong(map.get("goodsId").toString());
                goodsIds.add(goodsId);
                goodsName.add(goodsNames.get(goodsId).toString());
                goodsIndicatorValue.add(MoneyFormatUtils.getDoubleMoney(Double.parseDouble(map.get(indicator).toString()) / 100));
            }
            result.put("values", goodsIndicatorValue);
        }

        result.put("goodsIds", goodsIds);
        result.put("goodsName", goodsName);
        return result;
    }


    @Override
    public Map<Long, Object> getGoodsNamesByGoodsIds(Long[] goodsIds) {
        return goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
    }

    /**
     * 获取某些商品某些天的某个指标
     *
     * @param allGoodsInfo 查询结果
     * @param goodsIds     商品ids
     * @param dates        格式化后的日期
     * @param indicator    查询的指标
     * @return 结果
     */
    private Map<String, Object> getSingleIndicatorByGoodsIds(List<Map<String, Object>> allGoodsInfo, List<Long> goodsIds, String[] dates, String indicator) {
        Map<String, Object> result = Maps.newHashMap();
        List<Map<String, Object>> series = new ArrayList<>();
        Long[] goodsIdsArr = new Long[goodsIds.size()];
        int i = 0;
        for (Long goodsId : goodsIds) {
            goodsIdsArr[i] = goodsId;
            i++;
        }
        for (Long goodsId : goodsIds) {
            //指标不同,返回值类型不同
            List<Map<String, Object>> goods = new ArrayList<>();//某一个商品的一段时间的数据
            Map<String, Object> goodsInfo = Maps.newHashMap();
            for (Map<String, Object> map : allGoodsInfo) {
                if (goodsId.equals(Long.parseLong(map.get("goodsId").toString()))) {
                    goods.add(map);
                }
            }
            //获取商品名称
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIdsArr);
            goodsInfo.put("name", goodsNames.get(goodsId));
            //不同指标获取的格式不一样
            if (Indicator.PV.equals(indicator) || Indicator.UV.equals(indicator) || Indicator.VOLUME.equals(indicator)) {
                List<Integer> indicatorArr = new ArrayList<>();
                for (String date : dates) {//时间段
                    Boolean flag = false;
                    for (Map<String, Object> map : goods) {
                        if (date.equals(map.get("created").toString())) {
                            indicatorArr.add(Integer.parseInt(map.get(indicator).toString()));
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        indicatorArr.add(0);
                    }
                }
                goodsInfo.put("data", indicatorArr);
            } else {
                List<Double> indicatorArr = new ArrayList<>();
                for (String date : dates) {//时间段
                    Boolean flag = false;
                    for (Map<String, Object> map : goods) {
                        if (date.equals(map.get("created").toString())) {
                            indicatorArr.add(Double.parseDouble(map.get(indicator).toString()) / 100);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        indicatorArr.add(0d);
                    }
                }
                goodsInfo.put("data", indicatorArr);
            }
            series.add(goodsInfo);
        }
        result.put("series", series);
        return result;
    }

    /**
     * 处理金额
     *
     * @param topTen     前十的对象
     * @param sumGmv     总的金额
     * @param goodsNames 商品名称map
     * @param indicator  查询的指标
     * @return 返回处理后的对象
     */
    private List<Map<String, Object>> getPieChartForMoney(List<Map<String, Object>> topTen, Long sumGmv,
                                                          Map<Long, Object> goodsNames, String indicator) {
        List<Map<String, Object>> result = new ArrayList<>();
        Double sumTopTenGmv = 0d;
        for (Map<String, Object> map : topTen) {
            Double gmv = MoneyFormatUtils.getDoubleMoney(Long.parseLong(map.get(indicator).toString()));
            sumTopTenGmv = sumTopTenGmv + gmv;
            Map<String, Object> record = Maps.newHashMap();
            Long goodsId = Long.parseLong(map.get("goodsId").toString());
            record.put("goodsId", goodsId);
            record.put("name", goodsNames.get(goodsId));
            record.put("value", gmv);
            result.add(record);
        }
        double otherGmv = sumGmv / 100.0 - sumTopTenGmv;
        if (otherGmv > 0) {
            Map<String, Object> other = Maps.newHashMap();
            other.put("goodsId", 0);
            other.put("name", "其他");
            other.put("value", MoneyFormatUtils.getDoubleMoney(otherGmv));
            result.add(other);
        }

        return result;
    }

    /**
     * 处理金额
     *
     * @param topTen     前十的对象
     * @param sumGmv     总的金额
     * @param goodsNames 商品名称map
     * @param indicator  查询的指标
     * @return 返回处理后的对象
     */
    private List<Map<String, Object>> getPieChartForNum(List<Map<String, Object>> topTen, Long sumGmv,
                                                        Map<Long, Object> goodsNames, String indicator) {
        List<Map<String, Object>> result = new ArrayList<>();
        Long sumTopTenGmv = 0L;
        for (Map<String, Object> map : topTen) {
            Long gmv = Long.parseLong(map.get(indicator).toString());
            sumTopTenGmv = sumTopTenGmv + gmv;
            Map<String, Object> record = Maps.newHashMap();
            Long goodsId = Long.parseLong(map.get("goodsId").toString());
            record.put("goodsId", goodsId);
            record.put("name", goodsNames.get(goodsId));
            record.put("value", gmv);
            result.add(record);
        }
        Long otherGmv = sumGmv - sumTopTenGmv;
        if (otherGmv > 0) {
            Map<String, Object> other = Maps.newHashMap();
            other.put("goodsId", 0);
            other.put("name", "其他");
            other.put("value", otherGmv);
            result.add(other);
        }

        return result;
    }


}
