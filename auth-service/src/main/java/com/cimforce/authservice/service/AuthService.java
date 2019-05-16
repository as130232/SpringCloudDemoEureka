package com.cimforce.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cimforce.authservice.user.model.dto.UserDTO;
import com.cimforce.authservice.user.resource.UserResource;

@Service
public class AuthService {
	
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserResource userResource;
	/**
	 * 登入
	 * @author charles.chen
	 * @date 2018年1月25日 下午5:15:18
	 * @param userName
	 * @param password
	 * @return void
	 */
	public Authentication login(String username, String password) throws AuthenticationException{
		//將用戶在前端輸入的帳號密碼轉成UsernamePasswordAuthenticationToken物件
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        //驗證交由MyAuthenticationProvider執行
        try {
        	//查找數據庫是否擁有該用戶
    		UserDTO user = userResource.getUser(username);
    		if(user == null) {
    			throw new UsernameNotFoundException("username: " + username +" not found.");
    		}
    		
	        Authentication authentication = authenticationManager.authenticate(upToken);
	        //將該驗證回傳，給jwt取得身分及權限
	        return authentication;
	        //SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (BadCredentialsException e) {
        	throw new BadCredentialsException("Password is Wrong.");
        }
	}
	
	/**
	 * 註冊
	 * @author charles.chen
	 * @date 2018年1月25日 下午5:15:18
	 * @param UserDTO
	 * @return void
	 */
	public void register(UserDTO userDTO){
		
	}
}
