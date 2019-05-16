package com.cimforce.authservice.jwt;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 當用戶嘗試訪問安全的REST資源而不提供任何憑據(token)時，將被調用並回傳401(需要授權(token)以回應請求)
 * @author charles.chen
 * @date 2018年1月25日 下午4:57:59
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
    	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized. Please login again.");
    }
}