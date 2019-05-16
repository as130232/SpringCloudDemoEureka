package com.cimforce.authservice.user.resource;

import com.cimforce.authservice.user.model.dto.UserDTO;

public interface UserResource {
	
	/**
	 * 取得該用戶資訊
	 * @author charles.chen
	 * @date 2018年1月25日 下午3:52:44
	 * @param username
	 * @return UserDto
	 */
	public UserDTO getUser(String username);
}
