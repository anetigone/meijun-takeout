package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "订单拒绝参数")
@Data
public class OrderRejectionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单id")
    private Long id;
    //订单拒绝原因
    @Schema(description = "订单拒绝原因")
    private String rejectionReason;

}
