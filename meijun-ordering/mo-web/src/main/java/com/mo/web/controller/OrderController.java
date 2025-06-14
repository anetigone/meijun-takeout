package com.mo.web.controller;

import com.mo.api.dto.AfterSaleDTO;
import com.mo.api.dto.OrderCommentDTO;
import com.mo.api.dto.OrderSubmitDTO;
import com.mo.api.service.OrderService;
import com.mo.api.vo.OrderSubmitVO;
import com.mo.common.result.PageResult;
import com.mo.common.result.Result;
import com.mo.entity.*;
import com.mo.service.mapper.OrderDetailMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "订单管理")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Operation(summary = "获取订单列表")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Order.class)))
    @GetMapping("/overview")
    public Result<List<Order>> getAll(){
        List<Order> orders = orderService.getAll();

        return Result.success(orders);
    }

    @Operation(summary = "获取订单分页")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页数据量", required = true)
    })
    @GetMapping("/page")
    public PageResult getPage(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){

        int offset = (page - 1) * size;
        List<Order> orders = orderService.getPage(offset, size);
        int total = orderService.getOrderCount();

        return PageResult.success(orders.size(), orders, page, size);
    }

    @Operation(summary = "获取订单详情")
    @Parameters({
            @Parameter(name = "orderId", description = "订单ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Order.class)))
    @GetMapping("/{orderId}")
    public Result<Order> getOrderById( @PathVariable Long orderId){
        Order order = orderService.getOrderById(orderId);
        List<OrderDetail> list = orderDetailMapper.getDetailsByOrderId(orderId);
        order.setItems(list);

        return Result.success(order);
    }

    @Operation(summary = "保存订单评价")
    @Parameters({
            @Parameter(name = "orderCommentQueryDTO", schema = @Schema(implementation = OrderCommentDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = OrderComment.class)))
    @PostMapping("/comment")
    public Result<OrderComment> saveOrderComment(@RequestBody OrderCommentDTO orderCommentDTO){
        Long orderId = orderCommentDTO.getOrderId();
        String content = orderCommentDTO.getComment();
        OrderComment orderComment = orderService.saveOrderComment(orderId, content);

        return Result.success(orderComment);
    }

    @Operation(summary = "提交订单售后")
    @Parameters({
            @Parameter(name = "orderAfterSaleDTO", description = "订单售后参数", required = true, schema = @Schema(implementation = AfterSaleDTO.class))
    })
    @PostMapping("/after-sale")
    public Result<String> applyForAfterSale(@RequestBody AfterSaleDTO afterSaleDTO){
        AfterSale afterSale = new AfterSale();
        BeanUtils.copyProperties(afterSaleDTO, afterSale);
        orderService.saveAfterSale(afterSale);

        return Result.success();
    }

    @Operation(summary = "提交订单")
    @Parameters({
            @Parameter(name = "orderDTO", description = "订单参数", required = true, schema = @Schema(implementation = OrderSubmitDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = OrderSubmitVO.class)))
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrderSubmitDTO orderDTO){
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        OrderSubmitVO vo = orderService.saveOrder(order);

        return Result.success(vo);
    }

    @PostMapping("/cancel/{orderId}")
    public Result<String> cancelOrder(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);

        return Result.success();
    }
}
