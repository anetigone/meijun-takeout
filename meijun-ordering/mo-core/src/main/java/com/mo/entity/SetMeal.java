package com.mo.entity;

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

@Schema(description = "套餐")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMeal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "分类id")
    private Long categoryId;
    @Schema(description = "套餐名称")
    private String name;
    @Schema(description = "套餐价格")
    private BigDecimal price;
    @Schema(description = "状态 0 停售 1 起售")
    private Integer status;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "图片")
    private String image;
    @Schema(description = "菜品项")
    List<Long> dishes;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String createUser;
    private String updateUser;
}
