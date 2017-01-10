package com.quxin.freshfun.impl.goods;

import com.google.common.collect.Maps;
import com.quxin.freshfun.api.goods.GoodsDataService;
import com.quxin.freshfun.api.goods.GoodsService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.db.DynamicDataSource;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import com.quxin.freshfun.utils.CategoryUtils;
import com.quxin.freshfun.utils.MoneyFormatUtils;
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
            return getPieChartForMoney(topTen, sumGmv, goodsNames, "gmv");
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
                sumTopTenVolume = sumTopTenVolume + Long.parseLong(map.get("volume").toString());
            }
            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            return getPieChartForNum(volumeTopTen, sumVolume, goodsNames, "volume");
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
                sumTopTenCategoryGmv = sumTopTenCategoryGmv + Long.parseLong(map.get("gmv").toString());
            }
            for (Map<String, Object> map : gmvTopTenCategory) {
                Map<String, Object> record = Maps.newHashMap();
                Integer categoryId = Integer.parseInt(map.get("categoryId").toString());
                record.put("categoryId", categoryId);
                record.put("name", CategoryUtils.getCategoryNameById(categoryId));
                record.put("value", MoneyFormatUtils.getDoubleMoney(Long.parseLong(map.get("gmv").toString())));
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
            return getPieChartForMoney(goodsGmvTopTenByCategory, sumTopTenGmv, goodsNames, "gmv");
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
            Long[] goodsIds = new Long[goodsVolumeTopTenByCategory.size()];
            Long sumTopTenVolume = 0L;
            int i = 0;
            for (Map<String, Object> map : goodsVolumeTopTenByCategory) {
                goodsIds[i] = Long.parseLong(map.get("goodsId").toString());
                i++;
                sumTopTenVolume = sumTopTenVolume + Long.parseLong(map.get("volume").toString());
            }

            Map<Long, Object> goodsNames = goodsDataService.getGoodsNamesByGoodsIds(goodsIds);
            return getPieChartForNum(goodsVolumeTopTenByCategory, sumVolumeByCategory, goodsNames, "volume");
        } else {
            logger.error("该时间段内没有数据");
        }
        return null;
    }

    @Override
    public Map<String, Object> getGoodsIndicator(List<Long> goodsIds, Long startTime, Long endTime) {
        if (goodsIds != null && goodsIds.size() > 0) {
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
                result.put("pv", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "pv"));
                result.put("uv", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "uv"));
                result.put("avgPrice", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "avgPrice"));
                result.put("grossMargin", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "grossMargin"));
                result.put("convertRate", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "convertRate"));
                result.put("gmv", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "gmv"));
                result.put("gmvUv", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "gmvUv"));
                result.put("sevenRpr", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "sevenRpr"));
                result.put("monthRpr", getSingleIndicatorByGoodsIds(allGoodsIndicator, goodsIds, dates, "monthRpr"));
                return result;
            } else {
                logger.error(TimestampUtils.getStringDateFromLong(startTime) + "到" + TimestampUtils.getStringDateFromLong(endTime) + "期间没有数据");
            }
        }
        return null;
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
            if ("pv".equals(indicator) || "uv".equals(indicator)) {
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
