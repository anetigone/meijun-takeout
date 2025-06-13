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
@Schema(name = "售后分页查询参数")
public class AfterSalePageQueryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "页码")
    private int pageNum = 1;
    @Schema(description = "每页显示记录数")
    private int pageSize = 10;
}
