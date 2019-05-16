package com.cimforce.authservice.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cimforce.authservice.exception.model.ErrorInfo;
import com.fasterxml.jackson.core.JsonParseException;

import io.jsonwebtoken.ExpiredJwtException;


@ControllerAdvice
@RestController
public class GobalExceptionHandler {
	

	/**
	 * token過期
	 * @author Charles
	 * @date 2018年3月26日 上午11:05:59
	 */
	@ExceptionHandler(value = ExpiredJwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorInfo<String> ExpiredJwtExceptionHandler(HttpServletRequest req, HttpServletResponse res,
			ExpiredJwtException e) {
		ErrorInfo<String> error = new ErrorInfo<>();
		error.setCode(HttpStatus.UNAUTHORIZED.value());
		error.setMessage("authorize failed: " + e.getMessage());
		error.setUrl(req.getRequestURL().toString());
		return error;
	}
	
	/**
	 * token過期
	 * @author Charles
	 * @date 2018年3月26日 上午11:05:59
	 */
	@ExceptionHandler(value = JsonParseException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorInfo<String> JsonParseExceptionHandler(HttpServletRequest req, HttpServletResponse res,
			JsonParseException e) {
		ErrorInfo<String> error = new ErrorInfo<>();
		error.setCode(HttpStatus.UNAUTHORIZED.value());
		error.setMessage("authorize failed: token has not valid." + e.getMessage());
		error.setUrl(req.getRequestURL().toString());
		return error;
	}
	
	/**
	 * 登入時驗證失敗
	 * @author Charles
	 * @date 2017年9月17日 上午11:05:59
	 */
	@ExceptionHandler(value = AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorInfo<String> AuthenticationHandler(HttpServletRequest req, HttpServletResponse res,
			AuthenticationException e) {
		ErrorInfo<String> error = new ErrorInfo<>();
		error.setCode(HttpStatus.UNAUTHORIZED.value());
		error.setMessage("authorize failed." + e.getMessage());
		error.setUrl(req.getRequestURL().toString());
		return error;
	}
	
	
	
	
}
