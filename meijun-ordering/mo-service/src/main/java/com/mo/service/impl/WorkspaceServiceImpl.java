package com.mo.service.impl;

import com.mo.api.service.WorkspaceService;
import com.mo.api.vo.OrderOverviewVO;
import com.mo.api.vo.WorkspaceDataVO;
import com.mo.common.enumeration.OrderStatus;
import com.mo.service.mapper.CustomerMapper;
import com.mo.service.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public WorkspaceDataVO getWorkspaceData(LocalDateTime begin, LocalDateTime end) {
        Map<String, Object> map = new HashMap<>();

        map.put("begin", begin);
        map.put("end", end);
        map.put("status", null);

        Integer totalOrderCount = orderMapper.countByMap(map);

        map.put("status", OrderStatus.completed);
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null ? 0.0 : turnover;

        Integer validOrderCount = orderMapper.countByMap(map);

        double unitPrice = 0.0;
        double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }

        Integer newUsers = customerMapper.countByMap(map);

        return WorkspaceDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    @Override
    public OrderOverviewVO getOrderOverview() {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("end", LocalDateTime.now().with(LocalTime.MAX));
        map.put("status", OrderStatus.pending);

        //待接单
        Integer waitingOrders = orderMapper.countByMap(map);

        //待派送
        map.put("status", OrderStatus.confirmed);
        Integer deliveredOrders = orderMapper.countByMap(map);

        //已完成
        map.put("status", OrderStatus.completed);
        Integer completedOrders = orderMapper.countByMap(map);

        //已取消
        map.put("status", OrderStatus.cancelled);
        Integer cancelledOrders = orderMapper.countByMap(map);

        //全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverviewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }
}
