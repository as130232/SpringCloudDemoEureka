package com.cimforce.authservice.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.cimforce.authservice.password.Password;

/**
 * 可以自行實作AuthenticationProvider定義認證方式
 * 一般Spring Security 默認使用 DaoAuthenticationProvider
 * @author charles.chen
 * @date 2018年1月25日 下午4:11:29
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
	
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        //載入用戶資訊
        UserDetails user = null;
        try {
        	user = myUserDetailsService.loadUserByUsername(username);
        }catch(UsernameNotFoundException e) {
        	throw new UsernameNotFoundException("User ID :" + username +" not found.");
        }
        
        //前台用戶輸入的密碼與數據庫儲存用戶密碼(加密過)，驗證是否正確
        if (!Password.encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }
        
        //取得該用戶的權限(myUserDetailsService中定義賦予的權限)
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        //將用戶放入Authentication物件，代表已通過驗證
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    public boolean supports(Class<?> authentication) {
        return true;
    }
}