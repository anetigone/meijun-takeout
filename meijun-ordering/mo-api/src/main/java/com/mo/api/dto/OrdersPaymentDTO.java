package com.mo.api.dto;

import com.mo.common.enumeration.PayMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "订单拒绝参数")
@Data
public class OrdersPaymentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //订单号
    @Schema(description = "订单号")
    private String orderNumber;
    //付款方式
    @Schema(description = "付款方式")
    private PayMethod payMethod;

}
