package com.mo.api.dto;

import com.mo.common.enumeration.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "优惠券DTO")
@Data
@NoArgsConstructor
public class CouponDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema (description = "用户id")
    private Long userId;
    @Schema (description = "优惠券名字")
    private String name;
     @Schema (description = "优惠券类型")
    private CouponType couponType;
    @Schema (description = "优惠券描述")
    private String description;
    @Schema (description = "优惠券值")
    private BigDecimal value;
    @Schema (description = "优惠券使用门槛")
    private BigDecimal minAmount;
    @Schema (description = "优惠券最大金额")
    private BigDecimal maxAmount;
    @Schema (description = "优惠券开始时间")
    private LocalDateTime startTime;
     @Schema (description = "优惠券结束时间")
    private LocalDateTime endTime;
}
