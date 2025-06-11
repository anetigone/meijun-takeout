package com.mo.web.task;

import com.mo.common.enumeration.OrderStatus;
import com.mo.entity.Order;
import com.mo.service.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {


    private final OrderMapper orderMapper;

    public OrderTask(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void processOrderTimeTask(){
        log.info("开始处理订单超时 {}", LocalDateTime.now());
        LocalDateTime begin = LocalDateTime.now().minusMinutes(15);
        List<Order> orders = orderMapper.getByStatusAndOrderTimeBefore(OrderStatus.pending, begin);
        for(Order order : orders){
            order.setStatus(OrderStatus.cancelled);
            log.warn("订单超时取消 {}", order.getOrderNumber());
            orderMapper.updateOrder(order);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void processOrderDeliverTask(){
        log.info("开始处理订单配送 {}", LocalDateTime.now());
        List<Order> orders = orderMapper.getByStatusAndOrderTimeBefore(OrderStatus.delivering, LocalDateTime.now().minusDays(1));
        for(Order order : orders){
            order.setStatus(OrderStatus.completed);
            log.warn("订单配送完成 {}", order.getOrderNumber());
            orderMapper.updateOrder(order);
        }
    }
}
