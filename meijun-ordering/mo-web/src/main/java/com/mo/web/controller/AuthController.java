package com.mo.web.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mo.api.dto.AuthLoginDTO;
import com.mo.api.dto.AuthRegisterDTO;
import com.mo.api.dto.MpLoginDTO;
import com.mo.api.service.RedisService;
import com.mo.api.vo.AuthLoginVo;
import com.mo.api.vo.KaptchaVO;
import com.mo.common.constant.CaptchaConstant;
import com.mo.common.constant.JwtClaimsConstant;
import com.mo.common.constant.MessageConstant;
import com.mo.common.constant.RedisKeyConstant;
import com.mo.common.context.BaseContext;
import com.mo.common.enumeration.UserIdentity;
import com.mo.common.exception.CaptchaExpiredException;
import com.mo.common.exception.RedisAccessException;
import com.mo.common.exception.UnknownIdentityException;
import com.mo.common.exception.UserNotLoginException;
import com.mo.common.properties.JwtProperties;
import com.mo.common.result.Result;
import com.mo.common.utils.JwtUtil;
import com.mo.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mo.api.service.AuthService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private RedisService redisService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Operation(summary = "登录")
    @Parameters({
            @Parameter(name = "AuthLoginDTO", description = "登录参数", required = true, schema = @Schema(implementation = AuthLoginDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = AuthLoginVo.class)))
    @PostMapping("/login")
    public Result<AuthLoginVo> login(@RequestBody AuthLoginDTO authLoginDTO) {
        log.info("login:{}", authLoginDTO);

        String code = authLoginDTO.getCode();
        String uid = authLoginDTO.getUuid();
        if (!checkCaptcha(code, uid))
            throw new CaptchaExpiredException(MessageConstant.CAPTCHA_INCORRECT);

        User user = authService.login(authLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        String id = JwtClaimsConstant.getId(user.getIdentity());
        claims.put(id, user.getUuid());

        // 生成JWT令牌
        String token = JwtUtil.createJwt(
                getKey(user.getIdentity()),
                getTtl(user.getIdentity()),
                claims);

        //放入当前线程
        String uuid = user.getUuid();
        redisService.hSet(RedisKeyConstant.USER_IDENTITY, uuid , user.getIdentity().getName());
        redisService.hSet(RedisKeyConstant.USER_ID, uuid , user.getId());
        redisService.setEntity(uuid, user);
        BaseContext.setCurrentId(uuid);
        //todo 检查输入是否合法
        AuthLoginVo authLoginVo = AuthLoginVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .uuid(user.getUuid())
                .token(token)
                .build();

        return Result.success(authLoginVo);
    }

    @Operation(summary = "微信小程序登录")
    @Parameters({
            @Parameter(name = "loginData", description = "登录参数", required = true, schema = @Schema(implementation = Map.class))
    })
    @PostMapping("/mp/login")
    public Result<AuthLoginVo> mpLogin(@RequestBody MpLoginDTO dto) {
        log.info("微信小程序登录: {}", dto);
        
        User user = authService.mpLogin(dto);
        
        Map<String, Object> claims = new HashMap<>();
        String id = JwtClaimsConstant.getId(user.getIdentity());
        claims.put(id, user.getId());

        // 生成JWT令牌
        String token = JwtUtil.createJwt(
                getKey(user.getIdentity()),
                getTtl(user.getIdentity()),
                claims);

        //放入当前线程
        String uuid = user.getUuid();
        redisService.hSet(RedisKeyConstant.USER_IDENTITY, uuid, user.getIdentity().getName());
        redisService.hSet(RedisKeyConstant.USER_ID, uuid, user.getId());
        redisService.setEntity(uuid, user);
        BaseContext.setCurrentId(uuid);
        
        AuthLoginVo authLoginVo = AuthLoginVo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .uuid(user.getUuid())
                .token(token)
                .build();

        return Result.success(authLoginVo);
    }

    @Operation(summary = "微信小程序登录")
    @Parameters({
            @Parameter(name = "AuthRegisterDTO", description = "注册参数", required = true, schema = @Schema(implementation = AuthRegisterDTO.class))
    })
    @ApiResponse(responseCode = "200", description = "成功", content = @Content(schema = @Schema(implementation = User.class)))
    @PostMapping("/register")
    public Result<User> register(@RequestBody AuthRegisterDTO authRegisterDTO) {
        log.info("register:{}", authRegisterDTO);

        User user = new User();
        BeanUtils.copyProperties(authRegisterDTO, user);
        authService.saveUser(user);

        return Result.success(user);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<String> logout(){
        String uuid = BaseContext.getCurrentId();
        log.info("logout: {}", uuid);
        BaseContext.removeCurrentId();
        redisService.del(uuid);
        redisService.hDel(RedisKeyConstant.USER_IDENTITY, uuid);
        return Result.success();
    }

    @Operation(summary = "刷新jwt")
    @PostMapping("/refresh-token")
    public Result<String> refreshToken() throws ClassNotFoundException {
        String uuid = BaseContext.getCurrentId();
        if(uuid == null) throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        String identity = (String) redisService.hGet(RedisKeyConstant.USER_IDENTITY, uuid);
        UserIdentity ui = UserIdentity.fromString(identity);

        Class<?> clazz = Class.forName("com.mo.entity." + ui.getName());
        User user = (User) redisService.getEntity(uuid, clazz);
        if(user == null) throw new RedisAccessException(MessageConstant.REDIS_ACCESS_ERROR);

        Map<String, Object> claims = new HashMap<>();
        String id = JwtClaimsConstant.getId(user.getIdentity());
        claims.put(id, user.getId());

        String token = JwtUtil.createJwt(
                getKey(user.getIdentity()),
                getTtl(user.getIdentity()),
                claims);

        user.setToken(token);
        redisService.setEntity(uuid, user);

        return Result.success(token);
    }

    @GetMapping("/base64-captcha")
    public Result<KaptchaVO> getCaptcha() {
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);

        String base64;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ImageIO.write(image, "jpg", baos);
            base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        base64 = "data:image/jpeg;base64," + base64;

        String uuid = UUID.randomUUID().toString();
        redisService.hSet(RedisKeyConstant.KAPTCHA, uuid, base64, CaptchaConstant.CAPTCHA_EXPIRE_TIME);

        KaptchaVO kaptchaVO = KaptchaVO.builder()
                .uuid(uuid)
                .code(base64)
                .build();

        return Result.success(kaptchaVO);
    }

    @GetMapping("/stream-captcha")
    public void getCaptcha(HttpServletResponse response, HttpServletRequest request) {
        String text = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(text);
        String uuid = UUID.randomUUID().toString();
        redisService.hSet(RedisKeyConstant.KAPTCHA, uuid, text, CaptchaConstant.CAPTCHA_EXPIRE_TIME);

        try {
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.setHeader(CaptchaConstant.CAPTCHA_UUID, uuid);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
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

    private boolean checkCaptcha(String uuid, String code) {
        String captcha = (String) redisService.hGet(RedisKeyConstant.KAPTCHA, uuid);
        if(captcha == null) throw new CaptchaExpiredException(MessageConstant.CAPTCHA_EXPIRED);

        return captcha.equals(code);
    }
}
