package com.mo.api.dto;

import com.mo.common.enumeration.ActiveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 员工传输对象
 */
@Schema(name = "员工传输对象")
@Data
public class EmployeeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "员工id")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "手机号")
    private String phoneNum;
    @Schema(description = "性别")
    private String gender;
    @Schema(description = "身份证号")
    private String idNumber;
    @Schema(description = "商家id")
    private Long merchantId;
    @Schema(description = "状态")
    private ActiveStatus status;

}
