package com.mo.web.controller;

import com.mo.api.dto.OrderPageQueryDTO;
import com.mo.api.dto.UserInfoDTO;
import com.mo.api.service.OrderService;
import com.mo.api.service.RedisService;
import com.mo.api.service.UserService;
import com.mo.common.constant.MessageConstant;
import com.mo.common.constant.RedisKeyConstant;
import com.mo.common.context.BaseContext;
import com.mo.common.enumeration.UserIdentity;
import com.mo.common.exception.UserNotLoginException;
import com.mo.common.result.PageResult;
import com.mo.common.result.Result;
import com.mo.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户管理")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderService orderService;

    @Operation(summary = "获取用户信息")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping("/info")
    public Result<User> info() {
        String uuid = BaseContext.getCurrentId();
        if(uuid == null) {
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        }

        String identity = (String) redisService.hGet(RedisKeyConstant.USER_IDENTITY, uuid);
        UserIdentity ui = UserIdentity.fromString(identity);

        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.mo.entity." + ui.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        User user = (User) redisService.getEntity(uuid, clazz);

        return Result.success(user);
    }

    @Operation(summary = "更新用户信息")
    @Parameters({
            @Parameter(name = "UserInfoDTO", description = "用户信息", required = true, schema = @Schema(implementation = UserInfoDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = String.class)))
    @PutMapping("/update")
    public Result<String> update(@RequestBody UserInfoDTO userInfoDTO) {
        User user = new User();
        BeanUtils.copyProperties(userInfoDTO, user);
        String uuid = BaseContext.getCurrentId();
        user.setUuid(uuid);
        Object obj = redisService.hGet(RedisKeyConstant.USER_ID, uuid);
        Long id = null;
        if(obj instanceof Number){
            id = ((Number) obj).longValue();
        }
        user.setId(id);

        userService.update(user);

        return Result.success();
    }

    @Operation(summary = "获取用户订单")
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Order.class)))
    @GetMapping("/orders")
    public Result<List<Order>> getOrders() {
        String uuid = BaseContext.getCurrentId();
        Long userId = (Long) redisService.hGet(RedisKeyConstant.USER_ID, uuid);
        List<Order> orders = orderService.getOrderByUserId(userId);

        return Result.success(orders);
    }

    @Operation(summary = "获取用户订单分页")
    @Parameters({
            @Parameter(name = "orderPageQueryDTO", description = "分页参数", required = true, schema = @Schema(implementation = OrderPageQueryDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Order.class)))
    @GetMapping("/orders/page")
    public PageResult getPage(OrderPageQueryDTO dto) {
        int num = dto.getPage();
        int size = dto.getSize();
        int offset = (num - 1) * size;

        List<Order> orders = orderService.getPage(offset, size, dto.getId());

        return PageResult.success(orders.size(), orders, num, size);
    }

    @Operation(summary = "获取用户订单详情")
    @Parameters({
            @Parameter(name = "orderId", description = "订单ID", required = true)
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = Order.class)))
    @GetMapping("/orders/{orderId}")
    public Result<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);

        return Result.success(order);
    }
}
