package com.quxin.freshfun.impl.goods;

import com.google.common.collect.Maps;
import com.quxin.freshfun.api.goods.GoodsDataService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.db.DynamicDataSource;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import com.quxin.freshfun.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品指标
 * Created by qucheng on 17/1/5.
 */
@Service("goodsDataService")
public class GoodsDataServiceImpl implements GoodsDataService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Map<String, Object> getGoodsIndicator(Long goodsId) {
        Long endTime = TimestampUtils.getStartTimestamp();
        Long startTime = endTime - 86400 ;
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.PTP_DATA);

        //类目和成本价
        Map<String, Long> goodsInfo = goodsMapper.selectCategoryAndCostByGoodsId(goodsId);
        Integer category = goodsInfo.get("category").intValue();
        Integer cost = goodsInfo.get("goodsCost").intValue();

        //pv 和 uv
        Integer goodsPV = goodsMapper.selectGoodsPVByGoodsId(goodsId , startTime , endTime);
        Integer goodsUV = goodsPV % 2 == 0 ? goodsPV/2 : goodsPV/2 +1 ;

        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        Map<String , BigDecimal> gmvAndVolume = goodsMapper.selectGMVAndVolumeByGoodsId(goodsId , startTime , endTime);
        //gmv 和 volume
        Integer gmv = 0;
        Integer volume = 0 ;
        //平均成交价
        //毛利率 = (gmv - goodsCost*volume )/gmv
        Integer avgPrice = 0 ;
        Integer grossMargin = 0;
        if(gmvAndVolume != null){//没有为null
            gmv = gmvAndVolume.get("gmv").intValue() ;
            volume = gmvAndVolume.get("volume").intValue();
            avgPrice = gmv / volume ;
            grossMargin = (gmv - volume*cost)/gmv ;
        }


        //商品7天下单用户数,复购的人数
        List<Long> sevenOrderedUsers = goodsMapper.selectOrderedUsersByGoodsId(goodsId , endTime-86400*7 , endTime);
        Integer sevenOrderedNum = sevenOrderedUsers.size();
        Integer repeatedNum = 0;
        Integer sevenRpr = 0 ;
        if(sevenOrderedUsers.size() > 0 ){
            List<Long> repeatedUsers = goodsMapper.selectRepeatedUsersByGoodsId(goodsId , sevenOrderedUsers , endTime-86400*7 , endTime);
            if(repeatedUsers .size() > 0){
                repeatedNum = repeatedUsers.size();
                sevenRpr = Math.round((float)repeatedNum/sevenOrderedNum*10000) ;
            }
        }
        //商品30天下单用户数,复购的人数
        List<Long> monthOrderedUsers = goodsMapper.selectOrderedUsersByGoodsId(goodsId , endTime-86400*30 , endTime);
        Integer monthOrderedNum = monthOrderedUsers.size();
        Integer monthRepeatedNum = 0;
        Integer monthRpr = 0 ;
        if(monthOrderedUsers.size() > 0 ){
            List<Long> monthRepeatedUsers = goodsMapper.selectRepeatedUsersByGoodsId(goodsId , monthOrderedUsers , endTime-86400*30 , endTime);
            if(monthRepeatedUsers .size() > 0){
                monthRepeatedNum = monthRepeatedUsers.size();
                monthRpr = Math.round((float)monthOrderedNum/monthRepeatedNum*10000) ;
            }
        }


        //转化率 = 当天下单数/UV
        List<Long> todayOrderedUsers = goodsMapper.selectOrderedUsersByGoodsId(goodsId , startTime , endTime);
        Integer todayOrderedNum = todayOrderedUsers.size() ;
        //TODO 除法校验分母0


        return null;
    }

    @Override
    public Integer getGoodsCostByGoodsId(Long goodsId) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        Map<String, Long> goodsInfo = goodsMapper.selectCategoryAndCostByGoodsId(goodsId);
        return goodsInfo.get("goodsCost").intValue();
    }

    @Override
    public Map<String, Object> getPVAndUVByGoodsIdAndAppId(Long goodsId, Long appId) {
        Long endTime = TimestampUtils.getStartTimestamp();
        Long startTime = endTime - 86400 ;
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.PTP_DATA);
        Integer pv = goodsMapper.selectGoodsPVByGoodsIdAndAppId(goodsId , appId , startTime , endTime);
        Integer uv = pv % 2 == 0 ? pv/2 : pv/2 +1 ;
        Map<String , Object> result = Maps.newHashMap();
        result.put("pv" , pv);
        result.put("uv" , uv);
        return result;
    }
}
