package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "订单取消参数")
@Data
public class OrderCancelDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单id")
    private Long id;
    //订单取消原因
    @Schema(description = "订单取消原因")
    private String reason;

}
