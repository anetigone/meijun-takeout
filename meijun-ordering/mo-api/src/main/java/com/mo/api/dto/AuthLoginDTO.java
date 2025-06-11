package com.mo.api.dto;

import com.mo.common.enumeration.UserIdentity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "登录信息")
public class AuthLoginDTO implements Serializable {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "验证码")
    private String code;
    @Schema(description = "验证码的uuid")
    private String uuid;
    @Schema(description = "用户身份")
    private UserIdentity identity;
}
