package com.mo.api.dto;

import com.mo.common.enumeration.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "订单确认参数")
@Data
public class OrderConfirmDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单id")
    private Long id;
    @Schema(description = "订单状态")
    private OrderStatus status;

}
