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
@Schema(description = "工作台数据")
public class WorkspaceDataVO {

    @Schema(description = "销售总额")
    private Double turnover;//营业额
    @Schema(description = "有效订单数")
    private Integer validOrderCount;//有效订单数
    @Schema(description = "订单完成率")
    private Double orderCompletionRate;//订单完成率
    @Schema(description = "平均客单价")
    private Double unitPrice;//平均客单价
    @Schema(description = "新增用户数")
    private Integer newUsers;//新增用户数

}
