package com.quxin.freshfun.api.bean.goodsOrder;

/**
 * Created by ziming on 2016/12/29.
 * 根据商品查询结果的对象封装类
 */
public class GoodsOrderOutParam {
    //商品id
    private Long goodsId;
    //商品名称
    private String goodsName;
    //商品成交额
    private Long goodsMoney;
    //商品成交订单数
    private Integer goodsOrder;
    //总成交额
    private Long totalMoney;
    //总成交订单数
    private Integer totalOrder;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(Long goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public Integer getGoodsOrder() {
        return goodsOrder;
    }

    public void setGoodsOrder(Integer goodsOrder) {
        this.goodsOrder = goodsOrder;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Integer totalOrder) {
        this.totalOrder = totalOrder;
    }
}
