package com.mo.entity;

import com.mo.common.enumeration.OrderPayStaus;
import com.mo.common.enumeration.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "订单")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单ID")
    private Long id;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "商户id")
    private Long merchantId;
    @Schema(description = "下单时间")
    private LocalDateTime orderTime;// 下单时间
    @Schema(description = "订单状态")
    private OrderStatus status;    // 订单状态(0待付款，1待确认，2已确认，3派送中，4已完成，5已取消)
    @Schema(description = "支付状态")
    private OrderPayStaus payStatus;//  支付状态(0未支付，1已支付，2已退款)
    @Schema(description = "总金额")
    private BigDecimal total; // 总金额
    @Schema(description = "订单项列表")
    private List<OrderDetail> items;  // 订单项列表
    @Schema(description = "订单号")
    private String orderNumber;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "拒单原因")
    private String rejectionReason;
    // 地址、配送方式等其他字段

    LocalDateTime createTime;
    LocalDateTime updateTime;
}
