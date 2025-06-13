package com.mo.api.service;

import com.mo.api.dto.*;
import com.mo.api.vo.OrderSubmitVO;
import com.mo.entity.AfterSale;
import com.mo.entity.Order;
import com.mo.entity.OrderComment;

import java.util.List;

public interface OrderService {

    List<Order> getAll();

    /**
     * 根据用户id获取分页订单
     * @param offset 偏移量
     * @param size 大小
     * @param userId 用户id
     * @return list
     */
    List<Order> getPage(int offset, int size, Long userId);

    Order getOrderById(Long orderId);

    OrderComment saveOrderComment(Long orderId, String content);

    void updateOrder(Order order);

    void saveAfterSale(AfterSale afterSale);

    OrderSubmitVO saveOrder(Order order);

    List<Order> getOrderByUserId(Long userId);

    /**
     * 获取分页订单
     * @param offset 偏移量
     * @param size 大小
     * @return list
     */
    List<Order> getPage(int offset, int size);

    void refund(Long orderId);

    void cancelOrder(Long orderId);

    void confirm(OrderConfirmDTO orderConfirmDTO);

    void rejection(OrderRejectionDTO ordersRejectionDTO);

    void cancel(OrderCancelDTO ordersCancelDTO);

    int getOrderCount();

    void complete(OrderCompleteDTO orderCompleteDTO);

    void deliver(OrderDeliverDTO orderDeliverDTO);
}
