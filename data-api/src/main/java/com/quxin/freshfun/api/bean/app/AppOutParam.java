package com.quxin.freshfun.api.bean.app;

import java.util.Date;

/**
 * Created by ziming on 2017/1/6.
 */
public class AppOutParam {
    //公众号id
    private Long appId;
    //公众号名称
    private String appName;
    //商品id
    private Long goodsId;
    //商品名称
    private String goodsName;
    //页面浏览量
    private Integer pv;
    //页面用户浏览量
    private Integer uv;
    //成交额
    private Integer gmv;
    //成交量
    private Integer volume;
    //gmv/uv
    private Integer gmvUv;
    //毛利率
    private Integer grossMargin;
    //转化率
    private Integer convertRate;
    //平均成交价
    private Integer avgPrice;
    private Date date;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getPv() {
        return pv;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getGmv() {
        return gmv;
    }

    public void setGmv(Integer gmv) {
        this.gmv = gmv;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getGmvUv() {
        return gmvUv;
    }

    public void setGmvUv(Integer gmvUv) {
        this.gmvUv = gmvUv;
    }

    public Integer getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(Integer grossMargin) {
        this.grossMargin = grossMargin;
    }

    public Integer getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(Integer convertRate) {
        this.convertRate = convertRate;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Integer avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
