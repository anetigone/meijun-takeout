package com.mo.web.interceptor;

import com.mo.common.constant.JwtClaimsConstant;
import com.mo.common.context.BaseContext;
import com.mo.common.enumeration.UserIdentity;
import com.mo.common.properties.JwtProperties;
import com.mo.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;
    @Autowired
    public JwtTokenInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        String userType = request.getHeader("userType");
        if(userType == null || userType.isEmpty()){
            log.warn("Missing userType header.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing userType header.");
            return false;
        }

        UserIdentity identity = UserIdentity.fromValue(Integer.parseInt(userType));
        if(identity == null){
            log.warn("Invalid userType header.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid userType header.");
            return false;
        }

        String token = request.getHeader(jwtProperties.getTokenNameByType(identity));
        String key = jwtProperties.getSecretKeyByType(identity);

        if(token == null || token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "missing token");
            return false;
        }

        try{
            if(token.startsWith("Bearer ")) token = token.substring(7);
            Jws<Claims> jws = JwtUtil.parseJwt(key, token);
            Claims claims = jws.getPayload();
            request.setAttribute("claims:", claims);
            String id = claims.get(JwtClaimsConstant.getId(identity), String.class);
            BaseContext.setCurrentId(id);
            log.info("当前{} ： {}", JwtClaimsConstant.getName(identity) ,id);
            return true;
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token invalid");
            log.error("JWT 验证失败: ", e);
            return false;
        }
    }
}
