package com.mo.service.impl;

import com.mo.api.service.StatisticService;
import com.mo.entity.Order;
import com.mo.entity.OrderDetail;
import com.mo.entity.Product;
import com.mo.service.mapper.OrderDetailMapper;
import com.mo.service.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final OrderDetailMapper orderDetailMapper;
    private final OrderMapper orderMapper;

    public StatisticServiceImpl(OrderDetailMapper orderDetailMapper, OrderMapper orderMapper) {
        this.orderDetailMapper = orderDetailMapper;
        this.orderMapper = orderMapper;
    }

    /**
     * 获取销售报表
     * 该方法通过聚合订单详情来计算每个产品的总销量
     *
     * @return 包含所有产品销售信息的列表
     */
    @Override
    public List<Product> getSales() {
        // 获取所有订单详情
        List<OrderDetail> details = orderDetailMapper.getAllOrderDetail();

        // 使用Stream API对订单详情进行处理，聚合每个产品的销售数量
        Map<Long, Product> map = details.stream()
                // 将每个订单详情转换为Product对象，并用其itemId作为键创建一个Map
                .map(detail -> {
                    Product product = Product.builder()
                            .productId(detail.getItemId())
                            .name(detail.getName())
                            .type(detail.getItemType())
                            .sales(detail.getQuantity())
                            .build();
                    return Map.of(detail.getItemId(), product);
                })
                // 使用reduce方法将所有Map合并为一个，合并时将相同产品的销售数量相加
                .reduce(new HashMap<>(), (map1, map2) -> {
                    map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> {
                        v1.setSales(v1.getSales() + v2.getSales());
                        return v1;
                    }));
                    return map1;
                });

        // 将聚合后的Product对象收集到一个列表中并返回
        return map.values().stream().toList();
    }


    @Override
    public int getTraffic() {
        List<Order> list = orderMapper.getAll();

        return list.size();
    }

    @Override
    public double getSalesTotal() {
        List<Order> list = orderMapper.getAll();

        return list.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }

    @Override
    public List<Product> getSalesByPeriod(LocalDate begin, LocalDate end){
        List<Order> list = orderMapper.getOrderByPeriod(begin, end.plusDays(1L));
        List<OrderDetail> details = new ArrayList<>();
        for(Order order : list){
            List<OrderDetail> orderDetails = orderDetailMapper.getDetailsByOrderId(order.getId());
            details.addAll(orderDetails);
        }

         // 使用Stream API对订单详情进行处理，聚合每个产品的销售数量
        Map<Long, Product> mp = details.stream()
                // 将每个订单详情转换为Product对象，并用其itemId作为键创建一个Map
                .map(detail -> {
                    Product product = Product.builder()
                            .productId(detail.getItemId())
                            .name(detail.getName())
                            .type(detail.getItemType())
                            .sales(detail.getQuantity())
                            .build();
                    return Map.of(detail.getItemId(), product);
                })
                // 使用reduce方法将所有Map合并为一个，合并时将相同产品的销售数量相加
                .reduce(new HashMap<>(), (map1, map2) -> {
                    map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> {
                        v1.setSales(v1.getSales() + v2.getSales());
                        return v1;
                    }));
                    return map1;
                });

        // 将聚合后的Product对象收集到一个列表中并返回
        return mp.values().stream().toList();
    }

    @Override
    public double getSalesTotalByPeriod(LocalDate begin, LocalDate end) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end.plusDays(1L));

        return orderMapper.sumByMap(map);
    }

    @Override
    public int getTrafficByPeriod(LocalDate begin, LocalDate end) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end.plusDays(1L));

        return orderMapper.countByMap(map);
    }
}
