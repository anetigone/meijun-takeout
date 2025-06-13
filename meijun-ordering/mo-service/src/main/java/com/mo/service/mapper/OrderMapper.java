package com.mo.service.mapper;

import com.mo.common.enumeration.OrderStatus;
import com.mo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    List<Order> getAll();

    List<Order> getPage(@Param("offset") int offset, @Param("size") int size, @Param("userId") Long userId);

    Order getOrderById(@Param("orderId") Long orderId);

    void updateOrder(@Param("order") Order order);

    void saveOrder(Order order);

    List<Order> getOrderByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Order> getOrderByUserId(Long userId);

    List<Order> getPage(int offset, int size);

    Integer countByMap(@Param("map") Map<String, Object> map);

    Double sumByMap(@Param("map") Map<String, Object> map);

    List<Order> getByStatusAndOrderTimeBefore(OrderStatus status, LocalDateTime begin);

    int getOrderCount();
}
