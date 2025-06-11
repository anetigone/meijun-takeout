package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Schema(name = "分类分页查询参数")
@Data
public class CategoryPageQueryDTO implements Serializable {

    @Schema(description = "页码")
    private int page = 1;
    @Schema(description = "每页数量")
    private int pageSize = 10;
    @Schema(description = "分类名称")
    private String name;
    @Schema(description = "分类类型")
    private Integer type;

}