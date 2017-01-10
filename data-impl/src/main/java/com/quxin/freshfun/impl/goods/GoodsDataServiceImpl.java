package com.quxin.freshfun.impl.goods;

import com.google.common.collect.Maps;
import com.quxin.freshfun.api.goods.GoodsDataService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.db.DynamicDataSource;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import com.quxin.freshfun.utils.PercentUtils;
import com.quxin.freshfun.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品指标
 * Created by qucheng on 17/1/5.
 */
@Service("goodsDataService")
public class GoodsDataServiceImpl implements GoodsDataService {

    @Autowired
    private GoodsMapper goodsMapper;

    private Logger logger = LoggerFactory.getLogger(GoodsDataService.class);

    @Override
    public Boolean saveGoodsIndicator() {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        List<Long> goodsIds = goodsMapper.selectAllGoodsIds();
        for (Long goodsId : goodsIds) {
            Long endTime = TimestampUtils.getStartTimestamp();
            Long startTime = endTime - 86400;
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.PTP_DATA);
            //pv 和 uv
            Integer goodsPV = goodsMapper.selectGoodsPVByGoodsId(goodsId, startTime, endTime);
            Integer goodsUV = Math.round((float) goodsPV / 2);

            DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
            //类目和成本价
            Map<String, Long> goodsInfo = goodsMapper.selectCategoryAndCostByGoodsId(goodsId);
            Integer category = goodsInfo.get("category").intValue();
            Integer cost = goodsInfo.get("goodsCost").intValue();
            Map<String, BigDecimal> gmvAndVolume = goodsMapper.selectGMVAndVolumeByGoodsId(goodsId, startTime, endTime);
            //gmv 和 volume
            //平均成交价 毛利率 = (gmv - goodsCost*volume )/gmv
            Integer gmv = 0;
            Integer volume = 0;
            Integer avgPrice = 0;
            Integer grossMargin = 0;
            if (gmvAndVolume != null) {//没有为null
                gmv = gmvAndVolume.get("gmv").intValue();
                volume = gmvAndVolume.get("volume").intValue();
                avgPrice = gmv / volume;
                grossMargin = PercentUtils.getPercent(gmv - volume * cost, gmv);
            }
            //商品7天下单用户数,复购的人数
            List<Long> sevenOrderedUsers = goodsMapper.selectOrderedUsersByGoodsId(goodsId, endTime - 86400 * 7, endTime);
            Integer sevenRpr = 0;
            if (sevenOrderedUsers.size() > 0) {
                sevenRpr = getRpr(goodsId, sevenOrderedUsers, endTime);
            }
            //商品30天下单用户数,复购的人数
            List<Long> monthOrderedUsers = goodsMapper.selectOrderedUsersByGoodsId(goodsId, endTime - 86400 * 30, endTime);
            Integer monthRpr = 0;
            if (monthOrderedUsers.size() > 0) {
                monthRpr = getRpr(goodsId, monthOrderedUsers, endTime);
            }
            //转化率 = 当天下单数/UV  有多少用户下单
            List<Long> todayOrderedUsers = goodsMapper.selectOrderedUsersByGoodsId(goodsId, startTime, endTime);
            Integer todayOrderedNum = todayOrderedUsers.size();
            Integer convertRate = 0;
            //gmv/uv  查看当天用户平均在商城支付了多少钱
            Integer gmvUv = 0;
            if (goodsUV != 0) {
                convertRate = PercentUtils.getPercent(todayOrderedNum, goodsUV);
                gmvUv = gmv / goodsUV;
            }
            Map<String, Object> goodsData = Maps.newHashMap();
            goodsData.put("goodsId", goodsId);
            goodsData.put("date", TimestampUtils.getDateFromTimestamp());
            goodsData.put("pv", goodsPV);
            goodsData.put("uv", goodsUV);
            goodsData.put("category", category);
            goodsData.put("gmv", gmv);
            goodsData.put("volume", volume);
            goodsData.put("gmvUv", gmvUv);
            goodsData.put("convertRate", convertRate);
            goodsData.put("sevenRpr", sevenRpr);
            goodsData.put("monthRpr", monthRpr);
            goodsData.put("avgPrice", avgPrice);
            goodsData.put("grossMargin", grossMargin);
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
            goodsMapper.insertGoodsData(goodsData);
        }
        return true;
    }

    /**
     * 获取复购率
     *
     * @param goodsId 商品id
     * @param userIds 购买用户id
     * @param endTime 结束时间
     * @return 返回*日复购率
     */
    private Integer getRpr(Long goodsId, List<Long> userIds, Long endTime) {
        if (userIds.size() > 0) {
            List<Long> repeatedUsers = goodsMapper.selectRepeatedUsersByGoodsId(goodsId, userIds, endTime);
            if (repeatedUsers.size() > 0) {
                //查询用户复购信息
                List<Map<String, Object>> userInfo = goodsMapper.selectUserInfoByGoodsId(goodsId, repeatedUsers, endTime);
                Set<Long> repeatUserIds = new HashSet<>();//所有的id
                Set<Long> repeatedUserIds = new HashSet<>();//去重后的用户id
                for (Map<String, Object> map : userInfo) {
                    repeatUserIds.add(Long.parseLong(map.get("userId").toString()));
                }
                for (Long userId : repeatUserIds) {
                    int i = 0;
                    for (Map<String, Object> map : userInfo) {
                        if (userId.equals(Long.parseLong(map.get("userId").toString()))) {
                            i++;
                            break;
                        }
                    }
                    if (i > 0) {
                        repeatedUserIds.add(userId);
                    }
                }
                Integer repeatedNum = repeatedUserIds.size();
                return PercentUtils.getPercent(repeatedNum, userIds.size());
            }
        }
        return 0;
    }


    @Override
    public Integer getGoodsCostByGoodsId(Long goodsId) {
        if (goodsId != null) {
            DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
            Map<String, Long> goodsInfo = goodsMapper.selectCategoryAndCostByGoodsId(goodsId);
            if(goodsInfo==null){
                return 0;
            }
            return goodsInfo.get("goodsCost").intValue();
        } else {
            logger.error("商品id为空");
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getPVAndUVByGoodsIdAndAppId(Long start, Long end) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.PTP_DATA);
        return goodsMapper.selectPvAndUv(start, end);
    }

    @Override
    public Map<Long, Object> getGoodsNamesByGoodsIds(Long[] goodsIds) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        if (goodsIds != null && goodsIds.length > 0) {
            List<Map<String, Object>> goods = goodsMapper.selectGoodsNamesByGoodsIds(goodsIds);
            Map<Long, Object> result = Maps.newHashMap();
            for (Map map : goods) {
                result.put(Long.parseLong(map.get("goodsId").toString()), map.get("name"));
            }
            return result;
        } else {
            logger.error("商品ids为空");
        }
        return null;
    }
}
