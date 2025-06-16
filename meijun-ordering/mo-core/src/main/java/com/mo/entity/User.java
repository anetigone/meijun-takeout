package com.mo.entity;

import com.mo.common.enumeration.ActiveStatus;
import com.mo.common.enumeration.UserIdentity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(name = "用户", description = "用户基础类")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    protected Long id;
    @Schema(description = "用户唯一标识")
    protected String uuid;
    @Schema(description = "openid")
    protected String openid;
    @Schema(description = "姓名")
    protected String name;
    @Schema(description = "用户名")
    protected String username;
    @Schema(description = "密码")
    protected String password;
    @Schema(description = "用户身份")
    protected UserIdentity identity;
    @Schema(description = "状态 0 禁用 1 启用")
    protected ActiveStatus status;
    @Schema(description = "手机号")
    protected String phoneNum;
    @Schema(description = "性别")
    protected String gender;
    @Schema(description = "地址")
    protected String address;
    @Schema(description = "商家id")
    protected Long merchantId;
    @Schema(description = "头像")
    protected String avatar_url;
    @Schema(description = "jwt令牌")
    protected String token;
    @Schema(description = "邮箱")
    private String email;

    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;
}
