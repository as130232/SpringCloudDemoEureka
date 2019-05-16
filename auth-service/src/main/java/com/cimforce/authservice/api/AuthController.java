package com.cimforce.authservice.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cimforce.authservice.jwt.Token;
import com.cimforce.authservice.jwt.TokenProvider;
import com.cimforce.authservice.service.AuthService;
import com.cimforce.authservice.user.model.dto.UserDTO;
import com.cimforce.authservice.user.resource.UserResource;
import com.fasterxml.jackson.core.JsonParseException;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
public class AuthController {

	@Autowired
	TokenProvider tokenProvider;

	@Autowired
	AuthService authService;
	@Autowired
	UserResource userResource;

	/**
	 * 登入，成功後回傳合法token
	 * 
	 * @author charles.chen
	 * @date 2018年1月25日 下午5:15:18
	 */
	@PostMapping("/login")
	public String login(@Valid @RequestBody UserDTO userDto) throws AuthenticationException {
		Authentication authentication = authService.login(userDto.getUsername(), userDto.getPassword());
		// 將驗證角色傳入jwt中
		final String token = tokenProvider.createToken(userDto.getUsername());
		// 成功登入回傳token
		return token;
	}

	/**
	 * 驗證token是否正確
	 * 
	 * @author charles.chen
	 * @date 2018年3月23日 下午2:26:50
	 */
	@PostMapping("/authenticationToken")
	public Boolean authenticationToken(@Valid @RequestBody String jwt)
			throws ExpiredJwtException, AuthenticationException {
		Boolean isAuthenticationSuccess = false;
		try {
			Authentication authentication = this.tokenProvider.getAuthentication(jwt);
			if (authentication != null) {
				// 若不等於null表示驗證token成功
				isAuthenticationSuccess = true;
				// 後續對Request設置權限
				// 若驗證成功回傳authentication，並將該物件存在該thread local variable中
				// SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			// 有錯誤直接回傳false驗證失敗
		}
		return isAuthenticationSuccess;
	}

	/**
	 * 解析token並回傳會員資訊
	 * 
	 * @author charles.chen
	 * @date 2018年3月26日 下午4:14:16
	 */
	@PostMapping("/public/user")
	public UserDTO getUser(@Valid @RequestBody String jwt)
			throws ExpiredJwtException, AuthenticationException, JsonParseException {
		Authentication authentication = this.tokenProvider.getAuthentication(jwt);
		if (authentication != null) {
			UserDTO userDTO = userResource.getUser(authentication.getName());
			// 不讓前端看到密碼
			userDTO.setPassword("******");
			return userDTO;
		}
		return null;
	}

	/**
	 * 刷新token
	 * @author charles.chen
	 * @date 2018年5月14日 下午2:17:42
	 */
	@PostMapping("/public/refreshToken")
	public String refreshToken(@Valid @RequestBody Token token)
			throws ExpiredJwtException, AuthenticationException, JsonParseException {
		Authentication authentication = this.tokenProvider.getAuthentication(token.getJwt());
		if (authentication != null) {
			UserDTO userDTO = userResource.getUser(authentication.getName());
			final String newToken = tokenProvider.createToken(userDTO.getUsername());
			return newToken;
		}
		return null;
	}
}
