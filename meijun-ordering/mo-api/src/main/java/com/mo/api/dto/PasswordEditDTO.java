package com.mo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "密码修改参数")
@Data
public class PasswordEditDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //员工id
    @Schema(description = "id")
    private Long id;
    //旧密码
    @Schema(description = "旧密码")
    private String oldPassword;
    //新密码
    @Schema(description = "新密码")
    private String newPassword;

}
