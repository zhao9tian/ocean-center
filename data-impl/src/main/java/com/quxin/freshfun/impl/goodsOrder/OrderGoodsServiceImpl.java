package com.quxin.freshfun.impl.goodsOrder;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.api.bean.GoodsPOJO;
import com.quxin.freshfun.api.bean.goodsOrder.GoodsOrderOutParam;
import com.quxin.freshfun.api.order.OrderGoodsService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.GoodsOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ziming on 2016/12/29.
 */
@Service("orderGoodsService")
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private GoodsOrderMapper goodsOrderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    /**
     * 根据商品查询成交额
     * 注解Vot：volume of transaction（成交额）
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 商品数据的输出参数列表
     */
    @Override
    public List<Map<String,Object>> findGoodsOrderVot(Long startDate, Long endDate) {
        List<GoodsOrderOutParam> list = goodsOrderMapper.selectGoodsVot(startDate, endDate);
        Long allMoney = goodsOrderMapper.selectAllGoodsVot(startDate,endDate);
        List<Map<String,Object>> listMap = sealGoodsOrderList(list,null,allMoney);
        return listMap;
    }

    /**
     * 根据商品查询成交量
     * 注解Tv：trading volume （成交量）
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @return 商品数据的输出参数列表
     */
    @Override
    public List<Map<String,Object>> findGoodsOrderTv(Long startDate, Long endDate) {
        List<GoodsOrderOutParam> list = goodsOrderMapper.selectGoodsTv(startDate, endDate);
        Integer allOrder = goodsOrderMapper.selectAllGoodsTv(startDate,endDate);
        List<Map<String,Object>> listMap = sealGoodsOrderList(list,allOrder,null);
        return listMap;
    }

    /**
     * 根据订单创建时间查询总成交量
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @return 总成交量
     */
    @Override
    public Integer findAllGoodsTv(Long startDate, Long endDate) {
        return goodsOrderMapper.selectAllGoodsTv(startDate,endDate);
    }

    /**
     * 根据订单创建时间查询总成交额
     * @param startDate 开始时间
     * @param endDate   截止时间
     * @return 总成交额
     */
    @Override
    public Long findAllGoodsVot(Long startDate, Long endDate) {
        return goodsOrderMapper.selectAllGoodsVot(startDate,endDate);
    }

    //封装查询的输出参数
    //商品名称的补充
    private List<Map<String,Object>> sealGoodsOrderList(List<GoodsOrderOutParam> list,Integer allOrder,Long allMoney) {
        List<Map<String,Object>> listMap = new ArrayList<Map<String, Object>>();
        Long[] ids = new Long[10];
        Integer totalOrder = 0 ;
        Long totalMoney = 0l;
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).getGoodsId();
            if(allOrder!=null){
                totalOrder+=list.get(i).getGoodsOrder();
            }
            if(allMoney!=null){
                totalMoney+=list.get(i).getGoodsMoney();
            }
        }
        List<GoodsPOJO> goodsPOJOList = goodsMapper.selectGoodsNameById(ids);
        Map<String, Object> goodsMap = getGoodsMap(goodsPOJOList);

        if(allOrder!=null){
            for (GoodsOrderOutParam goodsOrder : list) {
                Map<String,Object> map = new HashMap<String,Object>();
                String goodsName = goodsMap.get(goodsOrder.getGoodsId().toString()).toString();
                map.put("value",goodsOrder.getGoodsOrder());
                map.put("name",goodsName);
                listMap.add(map);
            }
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("value",allOrder-totalOrder);
            map.put("name","其他");
            listMap.add(map);
        }
        if(allMoney!=null){
            for (GoodsOrderOutParam goodsOrder : list) {
                Map<String,Object> map = new HashMap<String,Object>();
                String goodsName = goodsMap.get(goodsOrder.getGoodsId().toString()).toString();
                map.put("value",goodsOrder.getGoodsMoney());
                map.put("name",goodsName);
                listMap.add(map);
            }
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("value",allMoney-totalMoney);
            map.put("name","其他");
            listMap.add(map);
        }
        return listMap;
    }

    //获取商品map
    private Map<String, Object> getGoodsMap(List<GoodsPOJO> list) {
        Map<String, Object> goodsMap = new HashMap<String, Object>();
        for (GoodsPOJO goods : list) {
            goodsMap.put(goods.getGoodsId().toString(), goods.getTitle());
        }
        return goodsMap;
    }

    public List<Map<String,Object>> selectErpAppInfo(){
        return goodsOrderMapper.selectErpAppInfo();
    }
}
