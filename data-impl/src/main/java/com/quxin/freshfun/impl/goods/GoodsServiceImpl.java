package com.quxin.freshfun.impl.goods;

import com.quxin.freshfun.api.bean.GoodsPOJO;
import com.quxin.freshfun.api.goods.GoodsService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.db.DynamicDataSource;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 商品service实现类
 * Created by qucheng on 2016/10/18.
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private GoodsMapper goodsMapper;


    @Override
    public Integer queryGoodsUVByGoodsId(Long goodsId, Long startTime, Long endTime) {
        Integer pv = queryGoodsPVGoodsId(goodsId, startTime, endTime);
        //按userId 分组查询
//        Integer uv = goodsMapper.selectGoodsUVByGoodsId(goodsId,startTime ,endTime);
        return pv % 2 == 0 ? pv / 2 : pv / 2 + 1;
    }

    @Override
    public Integer queryGoodsPVGoodsId(Long goodsId, Long startTime, Long endTime) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.PTP_DATA);
        return goodsMapper.selectGoodsPVByGoodsId(goodsId , startTime , endTime);
    }

    @Override
    public Double queryGoodsCVRByGoodsId(Long goodsId, Long startTime, Long endTime) {
        //查询下单的用户数
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
//        Integer countUserOrdered = goodsMapper.selectOrderedUsersByGoodsId(goodsId , startTime ,endTime);
        //查询
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.PTP_DATA);
        Integer uv = queryGoodsUVByGoodsId(goodsId , startTime ,endTime);
//        return  ((double)countUserOrdered / uv) ;
        return null;
    }

    @Override
    public Double querySevenRPRByGoodsId(Long goodsId, Long startTime, Long endTime) {
        //复购人数
//        Integer RPNum = goodsMapper.selectRepeatedUsersByGoodsId(goodsId, startTime, endTime);
        //下单数
//        Integer countUserOrdered = goodsMapper.selectOrderedUsersByGoodsId(goodsId , startTime ,endTime);
//        return ((double) RPNum/countUserOrdered);
        return null;
    }



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

}
