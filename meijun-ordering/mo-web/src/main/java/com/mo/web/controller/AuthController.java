package com.mo.web.controller;

import com.mo.api.dto.AuthLoginDTO;
import com.mo.api.dto.AuthRegisterDTO;
import com.mo.api.vo.AuthLoginVo;
import com.mo.common.constant.JwtClaimsConstant;
import com.mo.common.constant.MessageConstant;
import com.mo.common.context.BaseContext;
import com.mo.common.enumeration.UserIdentity;
import com.mo.common.exception.UnknownIdentityException;
import com.mo.common.properties.JwtProperties;
import com.mo.common.result.Result;
import com.mo.common.utils.JwtUtil;
import com.mo.entity.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mo.api.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "账号管理")
public class AuthController {
    //todo 可以改为构造器注入
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    @Tag(name = "登录")
    @Parameters({
            @Parameter(name = "authLoginDTO", description = "登录信息", required = true)
    })
    public Result<AuthLoginVo> login(@RequestBody AuthLoginDTO authLoginDTO) {
        log.info("login:{}", authLoginDTO);

        User user = authService.login(authLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        String id = JwtClaimsConstant.getId(user.getIdentity());
        claims.put(id, user.getId());

        // 生成JWT令牌
        String token = JwtUtil.createJwt(
                getKey(user.getIdentity()),
                getTtl(user.getIdentity()),
                claims);

        //放入当前线程
        BaseContext.setCurrentId(user.getUuid());
        //todo 检查输入是否合法
        //todo 使用redis缓存
        //todo 验证码
        AuthLoginVo authLoginVo = AuthLoginVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .uuid(user.getUuid())
                .token(token)
                .build();

        return Result.success(authLoginVo);
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody AuthRegisterDTO authRegisterDTO) {
        log.info("register:{}", authRegisterDTO);

        User user = authService.register(authRegisterDTO);

        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<String> logout(){
        BaseContext.removeCurrentId();
        return Result.success();
    }

    private String getKey(UserIdentity userIdentity){
        return switch (userIdentity) {
            case ADMIN -> jwtProperties.getAdminSecretKey();
            case CUSTOMER -> jwtProperties.getCustomerSecretKey();
            case EMPLOYEE -> jwtProperties.getEmployeeSecretKey();
            case MERCHANT -> jwtProperties.getMerchantSecretKey();
            default -> throw new UnknownIdentityException(MessageConstant.UNKNOWN_IDENTITY);
        };
    }

    private long getTtl(UserIdentity userIdentity){
        return switch (userIdentity) {
            case ADMIN -> jwtProperties.getAdminTtl();
            case CUSTOMER -> jwtProperties.getCustomerTtl();
            case EMPLOYEE -> jwtProperties.getEmployeeTtl();
            case MERCHANT -> jwtProperties.getMerchantTtl();
            default -> throw new UnknownIdentityException(MessageConstant.UNKNOWN_IDENTITY);
        };
    }
}
