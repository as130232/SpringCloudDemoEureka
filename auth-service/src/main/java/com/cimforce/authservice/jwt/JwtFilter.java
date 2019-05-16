package com.cimforce.authservice.jwt;
//package com.cimforce.authservice.jwt;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import io.jsonwebtoken.SignatureException;
//import io.jsonwebtoken.UnsupportedJwtException;
//
///**
// * 若第一次登入成功會返回jwt token 之後的連線取值，都根據filter驗證jwt中的會員，因此後續都不需要再查詢數據庫，直接採用token中的會員數據
// * @author charles.chen
// * @date 2018年1月25日 下午4:55:09
// */
//@Component
//public class JWTFilter  extends GenericFilterBean {
//
//	private final static String AUTHORIZATION_HEADER = "Authorization";
//
//	@Autowired
//	private TokenProvider tokenProvider;
//
//	@Override
//	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
//			FilterChain filterChain) throws IOException, ServletException {
//		try {
//			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//			
//			String jwt = tokenProvider.resolveToken(httpServletRequest);
//			
//			if (jwt != null) {
//				Authentication authentication = this.tokenProvider.getAuthentication(jwt);
//				if (authentication != null) {
//					//若驗證成功回傳authentication，並將該物件存在該thread local variable中
//					SecurityContextHolder.getContext().setAuthentication(authentication);
//				}
//			}
//			
//			filterChain.doFilter(servletRequest, servletResponse);
//		}
//		catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
//				| SignatureException | UsernameNotFoundException e) {
//			//當該密鑰有效但用戶不存在於數據庫中(ex:auth.inMemoryAuthentication())時，或者當JWT的驗證失敗時，會拋出例外
//			((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			((HttpServletResponse) servletResponse).getWriter().write("Please login again and get valid Token.");
//		}
//	}
//
//}
