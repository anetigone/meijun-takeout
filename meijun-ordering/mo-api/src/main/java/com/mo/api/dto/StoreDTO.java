package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "门店信息")
public class StoreDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "店长id")
    private Long merchantId;
    @Schema(description = "门店名称")
    private String name;
}
