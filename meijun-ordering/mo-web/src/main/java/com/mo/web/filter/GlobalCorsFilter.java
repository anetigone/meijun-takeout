package com.mo.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GlobalCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS,HEAD");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, " +
                "userType, adminToken, merchantToken, employeeToken, customerToken");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        if("OPTIONS".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
