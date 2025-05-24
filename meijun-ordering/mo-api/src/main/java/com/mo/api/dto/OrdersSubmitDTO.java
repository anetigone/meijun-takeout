package com.mo.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单提交数据传输对象
 */
@Schema(name = "订单提交参数")
@Data
public class OrdersSubmitDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //地址簿id
    @Schema(description = "地址簿id")
    private Long addressBookId;
    //付款方式
    @Schema(description = "付款方式")
    private int payMethod;
    //备注
    @Schema(description = "备注")
    private String remark;
    //预计送达时间
    @Schema(description = "预计送达时间")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;
    //配送状态  1立即送出  0选择具体时间
    @Schema(description = "配送状态  1立即送出  0选择具体时间")
    private Integer deliveryStatus;
    //餐具数量
    @Schema(description = "餐具数量")
    private Integer tablewareNumber;
    //餐具数量状态  1按餐量提供  0选择具体数量
    @Schema(description = "餐具数量状态  1按餐量提供  0选择具体数量")
    private Integer tablewareStatus;
    //打包费
    @Schema(description = "打包费")
    private Integer packAmount;
    //总金额
    @Schema(description = "总金额")
    private BigDecimal amount;
}
