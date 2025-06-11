package com.mo.web.config;

import com.mo.web.filter.GlobalCorsFilter;
import com.mo.web.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/auth/**")
                .excludePathPatterns("/ws/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:*", "http://127.0.0.1/*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public FilterRegistrationBean<GlobalCorsFilter> corsFilter() {
        FilterRegistrationBean<GlobalCorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new GlobalCorsFilter());
        registration.addUrlPatterns("/*"); // 应用于所有 URL
        registration.setOrder(1); // 设置优先级高于其他 Filter
        return registration;
    }

}
