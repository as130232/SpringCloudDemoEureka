package com.cimforce.authservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cimforce.authservice.constant.AUTHORITY;
import com.cimforce.authservice.user.model.dto.UserDTO;
import com.cimforce.authservice.user.resource.UserResource;


/**
 * 自行定義載入用戶訊息(數據庫取得)，並賦予權限
 * @author charles.chen
 * @date 2018年1月25日 上午10:30:06
 */
@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserResource userResource;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//查找數據庫是否擁有該用戶
		UserDTO user = userResource.getUser(username);
		
		if(user != null) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			//創建權限列表(該用戶的權限，可賦予多個權限)，未來查詢表格，賦予對應角色權限
			authorities.add(new SimpleGrantedAuthority(AUTHORITY.USER.role));
			
			return new org.springframework.security.core.userdetails.User(
					user.getUsername().toString(), user.getPassword(), 
                    true, //是否可用
                    true, //是否过期
                    true, //證書不過期為true
                    true, //帳戶未鎖定為true
                    authorities);
		}
		throw new UsernameNotFoundException("User ID not found.");
	}
}
