package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "员工分页查询参数")
@Data
public class EmployeePageQueryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //员工姓名
    @Schema(description = "员工姓名")
    private String name;
    //页码
    @Schema(description = "页码")
    private int pageNum = 1;
    //每页显示记录数
    @Schema(description = "每页显示记录数")
    private int pageSize = 10;

}
