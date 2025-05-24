package com.mo.entity;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 地址簿
 */
@Schema(description = "地址簿")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressBook implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "主键")
    private Long id;
    @Schema(description = "用户id")
    private Long userId;
    @Schema(description = "收货人")
    private String consignee;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "性别")
    private String sex;
    @Schema(description = "省级区划编号")
    private String provinceCode;
    @Schema(description = "省级名称")
    private String provinceName;
    @Schema(description = "市级区划编号")
    private String cityCode;
    @Schema(description = "市级名称")
    private String cityName;
    @Schema(description = "区级区划编号")
    private String districtCode;
    @Schema(description = "区级名称")
    private String districtName;
    @Schema(description = "详细地址")
    private String detail;
    @Schema(description = "标签")
    private String label;
    @Schema(description = "默认 0 否 1是")
    private Integer isDefault;
}
