package com.mo.api.dto;

import com.mo.common.enumeration.AfterSaleStatus;
import com.mo.common.enumeration.AfterSaleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(name = "售后信息")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AfterSaleDTO implements Serializable {

    @Schema(description = "订单id")
    private Long orderId;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "售后类型")
    private AfterSaleType type;
    @Schema(description = "售后原因")
    private String reason;
    @Schema(description = "售后内容")
    private String content;
    @Schema(description = "售后状态")
    private AfterSaleStatus status;
}
