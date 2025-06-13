package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "订单发货参数")
public class OrderDeliverDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单id")
    private Long orderId;
}
