package com.mo.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "工作区订单概览")
public class OrderOverviewVO {

    @Schema(description = "待处理订单数量")
    //待接单数量
    private Integer waitingOrders;
    @Schema(description = "待派送订单数量")
    //待派送数量
    private Integer deliveredOrders;
    @Schema(description = "已完成订单数量")
    //已完成数量
    private Integer completedOrders;
    @Schema(description = "已取消订单数量")
    //已取消数量
    private Integer cancelledOrders;
    @Schema(description = "全部订单数量")
    //全部订单
    private Integer allOrders;
}
