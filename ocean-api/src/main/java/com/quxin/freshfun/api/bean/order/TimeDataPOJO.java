package com.quxin.freshfun.api.bean.order;

/**
 * Created by fanyanlin on 2017/1/6.
 */
public class TimeDataPOJO {

    private Long id;

    private Long gmv;
    /**
     * gmv/uv
     */
    private Integer gmvUv;
    /**
     * 订单数量
     */
    private Integer orderNum;
    /**
     * 七日复购率
     */
    private Integer sevenRpr;
    /**
     * 30复购率
     */
    private Integer thirtyRpr;

    private String created;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGmv() {
        return gmv;
    }

    public void setGmv(Long gmv) {
        this.gmv = gmv;
    }

    public Integer getGmvUv() {
        return gmvUv;
    }

    public void setGmvUv(Integer gmvUv) {
        this.gmvUv = gmvUv;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getSevenRpr() {
        return sevenRpr;
    }

    public void setSevenRpr(Integer sevenRpr) {
        this.sevenRpr = sevenRpr;
    }

    public Integer getThirtyRpr() {
        return thirtyRpr;
    }

    public void setThirtyRpr(Integer thirtyRpr) {
        this.thirtyRpr = thirtyRpr;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
