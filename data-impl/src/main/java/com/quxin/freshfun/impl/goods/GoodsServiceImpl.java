package com.quxin.freshfun.impl.goods;

import com.google.common.collect.Maps;
import com.quxin.freshfun.api.bean.GoodsPOJO;
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

    /**
     * 根据商品Id查询商品详情
     *
     * @param ids 商品Id
     * @return 返回商品信息
     */
    @Override
    public List<GoodsPOJO> selectGoodsNameById(Long[] ids) {
        return goodsMapper.selectGoodsNameById(ids);
    }

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
        if(sumVolumeByCategory != null){
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
        }else{
            logger.error("该时间段内没有数据");
        }
        return null;
    }

}
