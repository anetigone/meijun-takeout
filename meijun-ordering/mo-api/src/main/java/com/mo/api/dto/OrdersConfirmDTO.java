package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "订单确认参数")
@Data
public class OrdersConfirmDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单id")
    private Long id;
    //订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款
    @Schema(description = "订单状态 1待付款 2待接单 3 已接单 4 派送中 5 已完成 6 已取消 7 退款")
    private Integer status;

}
