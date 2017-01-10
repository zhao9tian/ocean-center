package com.quxin.freshfun.impl.order;

import com.quxin.freshfun.api.bean.order.OrderDataPOJO;
import com.quxin.freshfun.api.bean.order.TimeDataPOJO;
import com.quxin.freshfun.api.order.OrderDataService;
import com.quxin.freshfun.api.order.OrderService;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.OrderMapper;
import com.quxin.freshfun.db.DynamicDataSource;
import com.quxin.freshfun.db.DynamicDataSourceHolder;
import com.quxin.freshfun.utils.TimestampUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by fanyanlin on 2016/12/29.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService,OrderDataService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    /**
     * 查询订单纬度
     */
    @Override
    public void addOrderLatitude() {

        TimeDataPOJO timeData = new TimeDataPOJO();

        OrderDataPOJO orderData = getOrderLatitude();

        timeData.setGmv(orderData.getOrderPrice());

        timeData.setOrderNum(orderData.getOrderNum());

        timeData.setCreated(orderData.getOrderDate());
        // gmv/uv
        int GU = queryTotalGU(orderData);
        timeData.setGmvUv(GU);
        // 7日复购率
        timeData.setSevenRpr(queryRpr(7));
        // 30日复购率
        timeData.setThirtyRpr(queryRpr(30));

        addTimeLatitude(timeData);
    }

    /**
     * 查询指定日期数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 结果数据
     */
    @Override
    public List<TimeDataPOJO> queryOrderLatitude(Long startTime, Long endTime) {
        if(startTime == null || startTime == 0 || endTime == null || endTime == 0){
            endTime = TimestampUtils.getStartTimestamp();
            startTime =  TimestampUtils.getPastDate(endTime,7);
        }
        Map<String ,Object> map = new HashMap<String,Object>();
        map.put("beginTime",startTime);
        map.put("endTime",endTime);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        return orderMapper.selectOrderLatitude(map);
    }




    private OrderDataPOJO getOrderLatitude() {
        Map<String,Object> map = new HashMap<String,Object>();
        long endTime = TimestampUtils.getStartTimestamp();
        long startTime = TimestampUtils.getYesterdayStartTime(endTime);
        map.put("endTime",endTime);
        map.put("beginTime",startTime);
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        return orderMapper.selectOrderData(map);
    }

    /**
     * 添加时间纬度数据
     * @param timeData 数据
     */
    private void addTimeLatitude(TimeDataPOJO timeData) {
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.OCEAN_DATA);
        int state = orderMapper.insertTimeLatitude(timeData);
    }

    /**
     * 查询gmv/uv
     */
    private int queryTotalGU(OrderDataPOJO orderData) {
        long endTime = TimestampUtils.getStartTimestamp();
        long startTime = TimestampUtils.getYesterdayStartTime(endTime);
        //设置数据源
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.PTP_DATA);
        Integer totalPV = goodsMapper.selectTotalPV(startTime, endTime);
        Double GU = formatDecimal((orderData.getOrderPrice()/100.0) / (totalPV / 2.0))*100;
        return GU.intValue();
    }


    /**
     * 格式化小数
     * @param param 参数
     * @return 格式化后的值
     */
    private double formatDecimal(double param){
        BigDecimal val = new BigDecimal(param);
        return val.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 查询指定日期复购率
     */
    private int queryRpr(int day) {
        Map<String,Object> map = new HashMap<>();
        long endTime = TimestampUtils.getStartTimestamp();
        long startTime = TimestampUtils.getPastDate(endTime,day);
        map.put("endTime",endTime);
        map.put("beginTime",startTime);
        //设置数据源
        DynamicDataSourceHolder.setDataSource(DynamicDataSource.ONLINE_DATA);
        List<OrderDataPOJO> orderUserList = orderMapper.selectSevenRpr(map);
        //复购率
        Double rprVal = 0.0;
        if(orderUserList != null && orderUserList.size() > 0) {
            List<OrderDataPOJO> rePurchaseUserList = orderMapper.selectRePurchaseUser(orderUserList,endTime);
            Set<Long> set = new HashSet<>();
            Set<Long> rprSet = new HashSet<>();
            for (OrderDataPOJO orderData : rePurchaseUserList) {
                if (set.contains(orderData.getUserId())) {
                    rprSet.add(orderData.getUserId());
                }
                set.add(orderData.getUserId());
            }
            double rpr = rprSet.size() / (orderUserList.size() * 1.0);
            rprVal = formatDecimal(rpr)*100;
        }
        return rprVal.intValue();
    }
}
